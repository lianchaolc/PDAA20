package com.moneyboxadmin.pda;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.application.GApplication;
import com.clearadmin.pda.ClearManager;
import com.entity.BoxDetail;
import com.entity.Finger;
import com.example.app.activity.LookStorageTaskListActivity;
import com.example.app.entity.User;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.imple.getnumber.Getnumber;
import com.imple.getnumber.StopNewClearBox;
import com.imple.getnumber.WebJoin;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.FingerCheckBiz;
import com.moneyboxadmin.biz.GetBoxDetailListBiz;
import com.moneyboxadmin.biz.MoneyBoxOutDoBiz;
import com.moneyboxadmin.biz.SaveAuthLogBiz;
import com.out.admin.pda.ClearMachineIing;
import com.out.admin.pda.OrderWork;
import com.out.admin.pda.WebSiteJoin;
import com.poka.device.ShareUtil;
import com.service.FixationValue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import afu.util.BaseFingerActivity;


/**
 * 双人指纹交接
 *
 * @author Administrator
 */
public class BankDoublePersonLogin extends BaseFingerActivity implements OnTouchListener {

    private static final String TAG = "BankDoublePersonLogin";
    ImageView backimg;
    TextView name1; // 名字1
    TextView name2; // 名字2
    TextView show; // 提示信息
    TextView resulttext; // 登陆结果
    ImageView resultimg; // 登陆结果
    TextView resultmsg; // 中间提示
    Timer timer;
    TextView loginTitle; // 标题
    String busin;// 业务
    TextView username1; // 库管员1
    TextView username2; // 库管员2
    ImageView finger_left; // 左手指纹图
    ImageView finger_right; // 左手指纹图
    String clearjoin;
    String bizNum;

    String admin;
    Bundle bundleIntent;
    Bundle bundle;
    String where; // 标识是从清分管理进入
    public static String userid1; // 角色ID
    public static String userid2; // 角色ID
    public static boolean firstSuccess = false; // 第一位是否已成功验证指纹
    private boolean isscuess = false; // 是否已交接成功

    String f1; // 第一个按手指的人
    String f2; // 第二个按手指的人
    String type = null; // 交接类型
    String planNum; // 计划编号

    String user = null;
    String userid = null;

    public static String textname1; // 姓名1
    public static String textname2; // 姓名2

    String bizName;
    /**
     * 钞箱编号字符串（ZH0001|ZH0002）
     */
    private String cashBoxNums;
    private String fingeruserone;

    OnClickListener clickreplace;
    private FingerCheckBiz fingerCheck;

    FingerCheckBiz getFingerCheck() {
        return fingerCheck = fingerCheck == null ? new FingerCheckBiz() : fingerCheck;
    }

    private MoneyBoxOutDoBiz moneyBoxOutDo;

    MoneyBoxOutDoBiz getMoneyBoxOutDo() {
        return moneyBoxOutDo = moneyBoxOutDo == null ? new MoneyBoxOutDoBiz() : moneyBoxOutDo;
    }

    SaveAuthLogBiz saveAuthLogBiz;

    SaveAuthLogBiz getSaveAuthLogBiz() {
        return saveAuthLogBiz = saveAuthLogBiz == null ? new SaveAuthLogBiz() : saveAuthLogBiz;
    }

    private ManagerClass managerClass;
    int count = 0;// 验证指纹的次数
    private String fname_left;
    private String fname_right;
    int one = 0;// 统计第一个验证指纹失败的次数
    int two = 0;// 统计第二个验证指纹失败的次数

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.y_bank_double_person_login);

        managerClass = new ManagerClass();

        initView();

        clickreplace = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 开始出库操作
                getMoneyBoxOutDo().getemptyMoneyBoxoutdo(busin, StopNewClearBox.list, planNum,
                        BankDoublePersonLogin.userid1, BankDoublePersonLogin.userid2,
                        GApplication.user.getOrganizationId(), bizNum, BoxDetailInfoDo.isfirst);
                managerClass.getAbnormal().remove();

                managerClass.getRuning().runding(BankDoublePersonLogin.this, "操作正在执行,请等待...");
            }
        };

        getMoneyBoxOutDo().hand_out = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                managerClass.getRuning().remove();
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        break;
                    case 0:
                        managerClass.getSureCancel().makeSuerCancel(BankDoublePersonLogin.this, busin + "失败",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                    }
                                }, true);
                        break;
                    case -1:
                        managerClass.getAbnormal().timeout(BankDoublePersonLogin.this, "连接异常，要重试吗？", clickreplace);
                        break;
                    case -4:
                        managerClass.getAbnormal().timeout(BankDoublePersonLogin.this, "连接超时，要重试吗？", clickreplace);
                        break;
                }

            }
        };

        // 钞箱交接
        getSaveAuthLogBiz().handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                bundle = msg.getData();

                switch (msg.what) {
                    case 1:
                        firstSuccess = true;
                        fingerDo();
                        break;
                    case -1:
                    case 0:
                        resultmsg.setText("验证失败，请重按");
//                        resultmsg.setText("验证异常，请重按");
                        if (null == admin || admin.equals("") || (null == type)) {
                            Log.d(TAG, "admin==" + admin);
                            Log.d(TAG, "type==" + type);

                        } else if (admin.equals("加钞人员") && type.equals("04")) {
                            if (!firstSuccess) {// 失败就累加次数
                                one++;
                                Toast.makeText(BankDoublePersonLogin.this,
                                        /**
                                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                                         */
                                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
                            } else {
                                two++;
                                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            if (!firstSuccess && one >= FixationValue.PRESS) {
                                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerNetPointCActivity.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("FLAG", "wangdianone");
                                ShareUtil.ivalBack = null;
                                ShareUtil.w_finger_bitmap_left = null;
                                intent1.putExtras(bundle2);
                                startActivityForResult(intent1, 7);

                            } else if (two >= FixationValue.PRESS) {
                                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerNetPointCActivity.class);
                                Bundle bundle3 = new Bundle();
                                bundle3.putString("FLAG", "wangdiantwo");
                                if (ShareUtil.w_finger_bitmap_left != null)
                                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                                if (null != fingeruserone) {
                                    bundle3.putString("left", fingeruserone);
                                } else if (null != userid1) {
                                    bundle3.putString("left", userid1);
                                }
                                intent2.putExtras(bundle3);
                                startActivityForResult(intent2, 7);
                            }
                        } else if (admin.equals("清分员") && type.equals("03")) {// 未清回收钞箱入库
                            if (!firstSuccess) {// 失败就累加次数
                                one++;
                                Toast.makeText(BankDoublePersonLogin.this,
                                        /**
                                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                                         */
                                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
                            } else {
                                two++;
                                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                                        .show();
                            }
                            if (!firstSuccess && one >= FixationValue.PRESS) {
                                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerClenWeiqingCActivity.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("FLAG", "QFweiqinghuishouone");
                                ShareUtil.ivalBack = null;
                                ShareUtil.w_finger_bitmap_left = null;
                                intent1.putExtras(bundle2);
                                startActivityForResult(intent1, 071);//  局部登录方法仅仅验证

                            } else if (two >= FixationValue.PRESS) {
                                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerClenWeiqingCActivity.class);
                                Bundle bundle3 = new Bundle();
                                bundle3.putString("FLAG", "QFweiqinghuishoutwo");
                                if (ShareUtil.w_finger_bitmap_left != null)
                                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                                if (null != fingeruserone) {
                                    bundle3.putString("left", fingeruserone);
                                } else if (null != userid1) {
                                    bundle3.putString("left", userid1);
                                }
                                intent2.putExtras(bundle3);
                                startActivityForResult(intent2, 071);
                            }

                        } else if (admin.equals("清分员") && type.equals("01")) {// 空钞箱出库的的后清分员清分
                            if (!firstSuccess) {// 失败就累加次数
                                one++;
                                Toast.makeText(BankDoublePersonLogin.this,
                                        /**
                                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                                         */
                                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
                            } else {
                                two++;
                                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                                        .show();
                            }
                            if (!firstSuccess && one >= FixationValue.PRESS) {
                                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerEmptAfterCleanActionCActivity.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("FLAG", "EmptAfterCleanone");
                                ShareUtil.ivalBack = null;
                                ShareUtil.w_finger_bitmap_left = null;
                                intent1.putExtras(bundle2);
                                startActivityForResult(intent1, 072);//  局部登录方法仅仅验证 空钞箱出库的的后清分员清分

                            } else if (two >= FixationValue.PRESS) {
                                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerEmptAfterCleanActionCActivity.class);
                                Bundle bundle3 = new Bundle();
                                bundle3.putString("FLAG", "EmptAfterCleantwo");
                                if (ShareUtil.w_finger_bitmap_left != null)
                                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                                if (null != fingeruserone) {
                                    bundle3.putString("left", fingeruserone);
                                } else if (null != userid1) {
                                    bundle3.putString("left", userid1);
                                }
                                intent2.putExtras(bundle3);
                                startActivityForResult(intent2, 072); //空钞箱出库的的后清分员清分
                            }
                        }
                        break;
                    case -4:
                        resultmsg.setText("网络超时，请重按");
                        break;

                    case 2:
                        resultmsg.setText("验证身份无效，请重按");
                        break;
                }

            }

        };

        bundleIntent = getIntent().getExtras();

        if (bundleIntent != null) {
            admin = bundleIntent.getString("user");
            where = bundleIntent.getString("where");
            type = bundleIntent.getString("type");
            planNum = bundleIntent.getString("planNum");
            Log.i("planNum", planNum + "");
            bizName = bundleIntent.getString("busin");
            clearjoin = bundleIntent.getString("clearjoin");
            bizNum = bundleIntent.getString("bizNum");
            cashBoxNums = bundleIntent.getString("cashBoxNums");
        }

        name1.setText(admin);
        name2.setText(admin);

        if ("库管员".equals(admin)) {
            loginTitle.setText(admin + "双人登陆");
            show.setText("请第一位库管员按压手指...");
        } else if ("清分员".equals(admin) && !"清分管理".equals(where)) {
            loginTitle.setText(admin + "交接");
            show.setText("请第一位清分员按压手指...");
        } else if ("清分员".equals(admin) && "清分管理".equals(where)) {
            show.setText("请第一位清分员按压手指...");
            loginTitle.setText("清分管理登录");
        } else if ("加钞人员".equals(admin)) {
            show.setText("请第一位加钞人员按压手指...");
            loginTitle.setText("加钞人员交接");
        } else if ("清分员交接".equals(clearjoin)) {
            show.setText("请第一位清分人员按压手指...");
            loginTitle.setText("清分员交接");
        }

        backimg.setOnTouchListener(this);

        // 登录验证
        getFingerCheck().hand_finger = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                bundle = msg.getData();
                switch (msg.what) {
                    case 1:
                        // 指纹登录操作及提示
                        firstSuccess = true;
                        fingerDo();
                        Log.e(TAG, "指纹验证");
                        break;
                    case -1:
                        resultmsg.setText("验证异常，请重按");
                        break;
                    case -4:
                        resultmsg.setText("验证超时，请重按");
                        break;
                    case 0:
                        resultmsg.setText("验证失败，请重按");
                        Cechecorole();
                        break;
                    default:
                        resultmsg.setText("验证失败!，请重按");
                        break;
                }
            }
        };
    }

    /***
     * 检查当前的人员信息和角色
     */
    private void Cechecorole() {

        if (null == admin || admin.equals("")) {
            Log.d(TAG, "admin==" + admin);
            Log.d(TAG, "type==" + type);

        } else if (admin.equals("库管员")) {
            // 累计验证失败的次数

            if (!firstSuccess) {// 失败就累加次数
                one++;
                Toast.makeText(BankDoublePersonLogin.this,
                        /**
                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                         */
                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
            } else {
                two++;
                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                        .show();
            }

            if (!firstSuccess && one >= FixationValue.PRESS) {
                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerLibraryCActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("FLAG", "kuguanyuanbykognchaoxiangone");
                ShareUtil.ivalBack = null;
                ShareUtil.w_finger_bitmap_left = null;
                intent1.putExtras(bundle2);
                startActivityForResult(intent1, 040);// 按角色 去跳转不同的页面04+0 以此类推

            } else if (two >= FixationValue.PRESS) {
                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerLibraryCActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("FLAG", "kuguanyuanbykognchaoxiangtwo");

                if (ShareUtil.w_finger_bitmap_left != null)
                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                if (null != fingeruserone) {
                    bundle3.putString("left", fingeruserone);
                } else if (null != userid1) {
                    bundle3.putString("left", userid1);
                }
                intent2.putExtras(bundle3);
                startActivityForResult(intent2, 040);
            }
//        } else if (clearjoin.equals("清分员交接") && type.equals("07")) {

        } else if (admin.equals("清分员") && !"清分管理".equals(where)) {
            if (!firstSuccess) {// 失败就累加次数
                one++;
                Toast.makeText(BankDoublePersonLogin.this,
                        /**
                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                         */
                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
            } else {
                two++;
                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                        .show();
            }

            if (!firstSuccess && one >= FixationValue.PRESS) {
                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerLibraryCActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("FLAG", "qingfenyuanone");
                ShareUtil.ivalBack = null;
                ShareUtil.w_finger_bitmap_left = null;
                intent1.putExtras(bundle2);
                startActivityForResult(intent1, 070);// 按角色 去跳转不同的页面04+0 以此类推

            } else if (two >= FixationValue.PRESS) {
                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerLibraryCActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("FLAG", "qingfenyuantwo");
                if (ShareUtil.w_finger_bitmap_left != null)
                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                if (null != fingeruserone) {
                    bundle3.putString("left", fingeruserone);
                } else if (null != userid1) {
                    bundle3.putString("left", userid1);
                }
                intent2.putExtras(bundle3);
                startActivityForResult(intent2, 070);
            }
        } else if ("清分管理".equals(where) && GApplication.user.getLoginUserId().equals("7")) {
            if (!firstSuccess) {// 失败就累加次数
                one++;
                Toast.makeText(BankDoublePersonLogin.this,
                        /**
                         * 3 改为FixationValue.PRESS，用FixationValue.PRESS来控制按压次数
                         */
                        "验证失败，您还有" + (FixationValue.PRESS - one) + "次机会", Toast.LENGTH_SHORT).show();
            } else {
                two++;
                Toast.makeText(BankDoublePersonLogin.this, "验证失败，您还有" + (FixationValue.PRESS - two) + "次机会", Toast.LENGTH_SHORT)
                        .show();
            }

            if (!firstSuccess && one >= FixationValue.PRESS) {
                Intent intent1 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerMangerCleanCActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("FLAG", "qingfenguanlione");
                bundle2.putString("where", where);// 清分管理员
                ShareUtil.ivalBack = null;
                ShareUtil.w_finger_bitmap_left = null;
                intent1.putExtras(bundle2);
                startActivityForResult(intent1, 0700);// 按角色 去跳转不同的页面04+0 以此类推

            } else if (two >= FixationValue.PRESS) {
                Intent intent2 = new Intent(BankDoublePersonLogin.this, BDPLCheckFingerMangerCleanCActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("where", where);// 清分管理员
                bundle3.putString("FLAG", "qingfenguanlitwo");
                bundle3.putString("userid", userid);
                if (ShareUtil.w_finger_bitmap_left != null)
                    GApplication.map = ShareUtil.w_finger_bitmap_left;
                if (null != fingeruserone) {
                    bundle3.putString("left", fingeruserone);
                } else if (null != userid1) {
                    bundle3.putString("left", userid1);
                }
                intent2.putExtras(bundle3);

                startActivityForResult(intent2, 0700);
            }
        }

    }

    private void initView() {
        backimg = (ImageView) findViewById(R.id.back_bank);
        resultimg = (ImageView) findViewById(R.id.resultimg);
        username1 = (TextView) findViewById(R.id.name1);
        username2 = (TextView) findViewById(R.id.name2);
        name1 = (TextView) findViewById(R.id.username1);
        name2 = (TextView) findViewById(R.id.username2);

        show = (TextView) findViewById(R.id.show);
        resultmsg = (TextView) findViewById(R.id.resultmsg);
        resulttext = (TextView) findViewById(R.id.resulttext);
        loginTitle = (TextView) findViewById(R.id.login_title);
        finger_left = (ImageView) findViewById(R.id.finger_left);
        finger_right = (ImageView) findViewById(R.id.finger_right);
    }

    @Override
    public boolean onTouch(View view, MotionEvent even) {
        // 按下的时候
        if (MotionEvent.ACTION_DOWN == even.getAction()) {
            switch (view.getId()) {
                case R.id.back_bank:
                    backimg.setImageResource(R.drawable.back_cirle_press);
                    break;
            }
        }

        // 手指松开的时候
        if (MotionEvent.ACTION_UP == even.getAction()) {
            switch (view.getId()) {
                case R.id.back_bank:
                    backimg.setImageResource(R.drawable.back_cirle);
                    BankDoublePersonLogin.this.finish();
                    firstSuccess = false;
                    break;
            }
            GolbalUtil.ismover = 0;
        }
        // 手指移动的时候
        if (MotionEvent.ACTION_MOVE == even.getAction()) {
            GolbalUtil.ismover++;
        }
        // 意外中断事件取消
        if (MotionEvent.ACTION_CANCEL == even.getAction()) {
            GolbalUtil.ismover = 0;
            switch (view.getId()) {
                case R.id.back_bank:
                    backimg.setImageResource(R.drawable.back_cirle);
                    break;
            }
        }
        return true;
    }

    /**
     * 2秒后自动跳下一个页面
     */
    public void towseconds() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                type = null;
                firstSuccess = false;
                // 库管员
                if (GApplication.user.getLoginUserId().equals("4")) {
                    String taskType = BankDoublePersonLogin.this.bundleIntent.getString("taskType");
                    /*
                     * 判断是否为查库服务
                     *
                     * @author zhouKai
                     */
                    if (taskType != null && taskType.equals("lookStorageService")) {
                        gotoLookStorageTaskListActivity();
                    } else {
                        managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, MoneyBoxManager.class,
                                bundleIntent, GolbalUtil.ismover);
                    }
                    BankDoublePersonLogin.this.finish();
                    // 清分员
                } else if (GApplication.user.getLoginUserId().equals("7") && !"清分管理".equals(where)) {
                    managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, JoinResult.class,
                            bundleIntent, GolbalUtil.ismover);
                    BankDoublePersonLogin.this.finish();
                    // 清分管理
                } else if ("清分管理".equals(where) && GApplication.user.getLoginUserId().equals("7")) {
                    managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, ClearManager.class,
                            bundleIntent, GolbalUtil.ismover);
                    BankDoublePersonLogin.this.finish();
                    // 网点加钞
                } else if ("加钞人员".equals(admin)) {
                    managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, ClearMachineIing.class,
                            bundleIntent, GolbalUtil.ismover);
                    BankDoublePersonLogin.this.finish();
                    // 未清回收钞箱
                } else if (type.equals("03")) {
                    managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, MoneyBoxManager.class,
                            bundleIntent, GolbalUtil.ismover);
                    BankDoublePersonLogin.this.finish();
                }
                // 重置交接状态
                isscuess = false;
                ShareUtil.finger_bitmap_left = null;
                ShareUtil.finger_bitmap_right = null;
                // 关闭当前页面
                BankDoublePersonLogin.this.finish();

            }
        }, 2000);

    }

    // 接钞箱编号
    String getBoxNum() {
        StringBuilder boxNum = new StringBuilder(); // 钞箱编号
        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
            if (GetBoxDetailListBiz.list.size() == 1) {
                return boxNum.toString();
            } else {
                boxNum.append(GetBoxDetailListBiz.list.get(i).getNum());
                boxNum.append("|");
            }
        }
        return boxNum.toString().substring(0, boxNum.lastIndexOf("|"));
    }

    // 指纹登录操作及提示
    void fingerDo() {

        int num = bundle.getInt("num");
        // 如果是同一个人
        if (username1.getText() != null && !(username1.getText() + "").equals("")) {
            num = 2;
        }
        if (username2.getText() != null && !(username2.getText() + "").equals("")) {
            num =1;
        }

        switch (num) {
            case 1:
                user = bundle.getString("username"); // 用户名
                userid = bundle.getString("userid"); // 用户ID
                username1.setText("姓名:" + user);
                textname1 = admin + ": " + user; // 头部显示用的用户名
                userid1 = userid;
                fingeruserone = userid1;
                resultmsg.setText("成功1位");
                finger_left.setVisibility(View.VISIBLE);
                show.setText("请第二位" + admin + "按压手指..");

                break;
            case 2:
                getFingerCheck().num = 0;

                user = bundle.getString("username"); // 用户名
                userid = bundle.getString("userid"); // 用户ID
                userid2 = userid;
                if (null == userid1) {
                    userid1 = fingeruserone;
                }
                Log.i("userid1", userid1);
                Log.i("userid2", userid2);
                // 如果是同一个人
                if (userid1.equals(userid2)) {
                    resultmsg.setText("不可重复登录");
                    return;
                }
                if (admin.equals("加钞人员")) {
                    if (null == fingeruserone && userid1 == null) {
                        resultmsg.setText("账号获取失败");
                        return;
                    } else if (fingeruserone.equals(userid2)) {
                        resultmsg.setText("不可重复登录");
                        return;
                    }
                }


                username2.setText("姓名:" + user);
                textname2 = admin + ": " + user; // 头部显示用的用户名
                resultmsg.setText("成功2位");
                finger_right.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
                resulttext.setVisibility(View.VISIBLE);
                resultimg.setVisibility(View.VISIBLE);
                // 交接成功2位后，不再交接
                isscuess = true;
                /*
                 * revised by zhangxuewei
                 */

                if (bizName != null && bizName.equals("未清回收钞箱出库")) {
                    System.out.println(StopNewClearBox.list.size());
                    System.out.println(bizName);
                    System.out.println("bizNum" + bizNum);
                    GolbalUtil.onclicks = true;
                    getMoneyBoxOutDo().getemptyMoneyBoxoutdo(bizName, StopNewClearBox.list, planNum,
                            BankDoublePersonLogin.userid1, BankDoublePersonLogin.userid2,
                            GApplication.user.getOrganizationId(), bizNum, BoxDetailInfoDo.isfirst);
                }
                // 2秒后跳转
                towseconds();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        firstSuccess = false;
    }

    // 根据交接类型获取返回角色ID
    public String getUserID(String type) {
        // 空钞箱交接
        if (type.equals("01")) {
            return FixationValue.clearer + "";
            // 未清回收钞箱交接
        } else if (type.equals("03")) {
            return FixationValue.clearer + "";
            // 网点人员加钞钞箱交接
        } else if (type.equals("04")) {
            return FixationValue.webuser + "";
        }
        return null;

    }

    /**
     * 跳转到查库服务任务单列表界面
     *
     * @author zhouKai
     */
    private void gotoLookStorageTaskListActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("nameOne", this.username1.getText().toString().replaceAll("姓名:", ""));
        bundle.putString("nameTwo", this.username2.getText().toString().replaceAll("姓名:", ""));
        bundle.putString("codeOne", BankDoublePersonLogin.userid1);
        bundle.putString("codeTwo", BankDoublePersonLogin.userid2);
        managerClass.getGolbalutil().gotoActivity(BankDoublePersonLogin.this, LookStorageTaskListActivity.class, bundle,
                GolbalUtil.ismover);
    }

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        resultmsg.setText("正在获取指纹特征值");
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        resultmsg.setText("正在验证...");

        ShareUtil.ivalBack = charBytes;
        // 1.第一位交接人员验证指纹
        if (!firstSuccess) {
            ShareUtil.finger_bitmap_left = img;
            finger_left.setImageBitmap(img);
            f1 = "1";
            Finger finger = new Finger();
            finger.setCorpId(GApplication.user.getOrganizationId()); // 机构ID
            finger.setRoleId(GApplication.user.getLoginUserId()); // 角色ID
            finger.setcValue(ShareUtil.ivalBack); // 特征值

            if (type == null) {
                // 登录
                getFingerCheck().fingerLoginCheck(finger);
            } else { // 交接 WebSiteJoin.webJoinID
                if (admin.equals("加钞人员")) {

                    Log.i("开始交接", "开始交接");
                    if ("在行式".equals(OrderWork.type)) {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                getUserID(type), ShareUtil.ivalBack, type, f1, "2");


                    } else {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list,
                                GApplication.user.getOrganizationId(), "5", ShareUtil.ivalBack, "C0",
                                f1, "2");
                    }

                } else {
                    // 如果是空钞箱交接
                    if ("01".equals(type)) {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, Getnumber.list_boxdeatil,
                                GApplication.user.getOrganizationId(), getUserID(type),
                                ShareUtil.ivalBack, type, f1, "2");
                    } else {// 当前判断分支是：未清回收钞箱出库
                        // modify begin 2017-4-25 by liuchang 清分员指纹验证通过后交接的钞箱数量有原来通过sql查询，
                        // 改为通过页面中“钞箱出入库前检测”按钮检测后确认的数量
                        String[] cashBoxArray = cashBoxNums.split(";");
                        ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                        for (int i = 0; i < cashBoxArray.length; i++) {
                            BoxDetail boxDetail = new BoxDetail();
                            boxDetail.setNum(cashBoxArray[i]);
                            cashBoxList.add(boxDetail);
                        }
                        // modify end
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, cashBoxList,
                                GApplication.user.getOrganizationId(), getUserID(type),
                                ShareUtil.ivalBack, type, f1, "2");
                    }
                    Log.i("aaaaaaa", "sss");
                }
            }
            // 2.第二位交接人验证指纹
        } else {
            ShareUtil.finger_bitmap_right = img;
            finger_right.setImageBitmap(ShareUtil.finger_bitmap_right);
            f2 = "2";

            if (type == null) { // 登录
                Finger finger = new Finger();
                finger.setCorpId(GApplication.user.getOrganizationId()); // 机构ID
                finger.setRoleId(GApplication.user.getLoginUserId()); // 角色ID
                finger.setcValue(ShareUtil.ivalBack); // 特征值
                getFingerCheck().fingerLoginCheck(finger);
            } else { // 交接 WebSiteJoin
                if (isscuess) {
                    return;
                }
                if (admin.equals("加钞人员")) {
                    if ("在行式".equals(OrderWork.type)) {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                getUserID(type), ShareUtil.ivalBack, type, f2, "2");
                    } else {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list,
                                GApplication.user.getOrganizationId(), "5", ShareUtil.ivalBack, "C0",
                                f2, "2");
                    }

                } else {
                    // 如果是空钞箱交接
                    if ("01".equals(type)) {
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, Getnumber.list_boxdeatil,
                                GApplication.user.getOrganizationId(), getUserID(type),
                                ShareUtil.ivalBack, type, f2, "2");
                    } else {
                        // 未清回收钞箱出库交接
                        // modify begin 2017-4-25 by liuchang 清分员指纹验证通过后交接的钞箱数量有原来通过sql查询，
                        // 改为通过页面中“钞箱出入库前检测”按钮检测后确认的数量
                        String[] cashBoxArray = cashBoxNums.split(";");
                        ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                        for (int i = 0; i < cashBoxArray.length; i++) {
                            BoxDetail boxDetail = new BoxDetail();
                            boxDetail.setNum(cashBoxArray[i]);
                            cashBoxList.add(boxDetail);
                        }
                        // modify end
                        getSaveAuthLogBiz().getSaveAuthLog(planNum, cashBoxList,
                                GApplication.user.getOrganizationId(), getUserID(type),
                                ShareUtil.ivalBack, type, f2, "2");
                    }
                }

            }
        }
    }

    String username;
    String getyanzhenuser1 = "";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            if (null != data) {
                String flag = data.getStringExtra("FLAG");
                String result1 = data.getStringExtra("netpoincollect");


                Log.e("kkk", "onStop" + flag);
                Log.e("kkk", "onStop" + result1);
                String username = "";
                if (flag.equals("wangdianone")) {// 网点人局部登录
                    String zhanghao = data.getStringExtra("zhanghao");
                    username = data.getStringExtra("name");
                    if (null != zhanghao || !zhanghao.equals("")) {
                        fingeruserone = zhanghao;
                    }
                    userid1 = zhanghao;
                    finger_left.setImageResource(R.drawable.sccuss);
                    if (GApplication.wd_user1.getUsername().isEmpty()) {
                    } else {
                        if (null == textname1 || textname1.equals("") && null != GApplication.user) {
                            textname1 = admin + username;
                        }
                    }
                    fname_left = username;
                    username1.setText(fname_left);
                    f1 = "3";
                    one = 0;
                    firstSuccess = true;
                    Log.e("kkk", username);
                    byte[] cValue = null;
                    if ("在行式".equals(OrderWork.type)) {
                        getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                getUserID(type), cValue, type, "3", "1", GApplication.wd_user1.getUserzhanghu());

                    } else {
                        getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, WebJoin.list,
                                GApplication.user.getOrganizationId(), "5", cValue, "C0",
                                "3", "1", GApplication.wd_user1.getUserzhanghu());
                    }
                }
                if (flag.equals("wangdiantwo")) {
                    userid2 = GApplication.wd_user2.getUserzhanghu();
                    textname2 = admin + GApplication.wd_user2.getUsername();
                    fname_right = GApplication.wd_user2.getUsername();
                    fname_right = data.getStringExtra("name");
                    finger_right.setImageResource(R.drawable.sccuss);
                    // System.out.println("one is :"+one);
                    if (GApplication.wd_user1 == null) {
                        GApplication.wd_user1 = new User();
                    }

                    if (GApplication.wd_user1.getUsername() != null && GApplication.map != null) {
                        finger_left.setImageBitmap(GApplication.map);
                    } else {
                        // 指纹获取账号名
                        if (null != user) {
                            fname_left = user;
                        } else if (null == GApplication.wd_user1.getUsername()) {
                            if (!username.isEmpty()) {
                                fname_left = user;
                            }
                            finger_left.setImageResource(R.drawable.sccuss);
                        }
                    }
                    f1 = "1";
                    f2 = "2";

                    Log.e("kkk", GApplication.wd_user2.getUserzhanghu());
                    Log.e("kkk", fname_left);
                    Log.e("kkk", fname_right);
                    if (fname_left.equals(fname_right)) {
                        Toast.makeText(BankDoublePersonLogin.this, "请两位网点人员进行验证！", Toast.LENGTH_SHORT).show();
                    } else {
                        firstSuccess = false;
                        fname_left = null;
                        fname_right = null;

                        if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {
                            byte[] cValue = null;
                            if ("在行式".equals(OrderWork.type)) {
                                getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                        getUserID(type), cValue, type, "4", "2", GApplication.wd_user2.getUserzhanghu());

                            } else {
                                getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, WebJoin.list,
                                        GApplication.user.getOrganizationId(), "5", cValue, "C0",
                                        "4", "2", GApplication.wd_user2.getUserzhanghu());
                            }
                            towseconds();//  跳转页面
                        }

                    }
                }

            }

        } else if (requestCode == 040) {//  啥也没有单纯的库管员
            if (null != data) {
                String flag = data.getStringExtra("FLAG");
                String result1 = data.getStringExtra("netpoincollect");


                Log.e("kkk", "onStop" + flag);
                Log.e("kkk", "onStop" + result1);


                if (flag.equals("kuguanyuanbykognchaoxiangone")) {
                    String zhanghao = data.getStringExtra("zhanghao");
                    String username = data.getStringExtra("name");
                    textname1 = admin + ": " + username; // 头部显示用的用户名
                    if (null != zhanghao || !zhanghao.equals("")) {
                        fingeruserone = zhanghao;
                    }
                    if (null == userid1||userid1.equals("")) {
                        userid1 = GApplication.user.getYonghuZhanghao();
                    }
                    finger_left.setImageResource(R.drawable.sccuss);
                    if (GApplication.user.getLoginUserName().isEmpty()) {
                    } else {

                    }
                    fname_left = username;
                    username1.setText(fname_left);
                    f1 = "3";
                    one = 0;
                    firstSuccess = true;
                    Log.e("kkk", GApplication.user.getYonghuZhanghao());
                    if (admin.equals("库管员")) {
                        Toast.makeText(BankDoublePersonLogin.this, "验证成功" + "", Toast.LENGTH_SHORT).show();
                    }
                }
                if (flag.equals("kuguanyuanbykognchaoxiangtwo")) {

                    if (null == userid2||userid2.equals("")) {
                        userid2 = GApplication.user.getYonghuZhanghao();
                    }
                    String username = data.getStringExtra("name");
                    textname2 = admin + ": " + username; // 头部显示用的用户名
                    fname_right = GApplication.user.getLoginUserName();
                    fname_right = username;
                    finger_right.setImageResource(R.drawable.sccuss);
                    // System.out.println("one is :"+one);
                    if (GApplication.user == null) {
//                        GApplication.user = new  GApplication.user;
                        return;
                    }

                    if (GApplication.user.getLoginUserName() != null && GApplication.map != null) {
                        finger_left.setImageBitmap(GApplication.map);
                    } else {
                        if (null != user) {
                            fname_left = user;
                        }
                        finger_left.setImageResource(R.drawable.sccuss);

                    }
                    f1 = "1";
                    f2 = "2";

                    Log.e("kkk", GApplication.user.getYonghuZhanghao());
                    Log.e("kkk", fname_left);
                    Log.e("kkk", fname_right);
                    if (fname_left.equals(fname_right)) {
                        Toast.makeText(BankDoublePersonLogin.this, "请两位库管员人员进行验证！", Toast.LENGTH_SHORT).show();
                    } else {
                        firstSuccess = false;
                        fname_left = null;
                        fname_right = null;

                        if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {
                            towseconds();//  跳转页面
                        }

                    }
                }

            }
        } else if (requestCode == 070 || requestCode == 071 || requestCode == 072) { //  清分员   未清回收钞箱交接   //  空钞箱出库后072
            if (null != data) {
                String flag = data.getStringExtra("FLAG");


                Log.e("kkk", "onStop" + flag);


                if (flag.equals("QFweiqinghuishouone")) { //未清回收钞箱交接
                    ;
                    String zhanghao = data.getStringExtra("zhanghao");
                    username = data.getStringExtra("name");
                    textname1 = username;
                    if (null != zhanghao || !zhanghao.equals("")) {
                        fingeruserone = zhanghao;
                    }
                    if (null != o_Application.qingfen) {
                        userid1 = o_Application.qingfen.getYonghuZhanghao();
                    }
                    finger_left.setImageResource(R.drawable.sccuss);
                    if (o_Application.qingfen.getLoginUserName().isEmpty()) {
                    } else {
                        Log.e("kkk", "账号不为空" + o_Application.qingfen.getLoginUserName());
                    }
                    fname_left = username;
                    username1.setText(fname_left);
                    f1 = "3";
                    one = 0;
                    firstSuccess = true;
                    Log.e("kkk", o_Application.qingfen.getYonghuZhanghao());
                    if (admin.equals("清分员")) {
                        Toast.makeText(BankDoublePersonLogin.this, "验证成功" + "", Toast.LENGTH_SHORT).show();
                        if (bizName != null && bizName.equals("未清回收钞箱出库")) {
                            if (null != BankDoublePersonLogin.userid1) {
                                getyanzhenuser1 = BankDoublePersonLogin.userid1;
                            }
                            if (null == BankDoublePersonLogin.userid1) {
                                getyanzhenuser1 = zhanghao;
                                BankDoublePersonLogin.userid1 = zhanghao;
                            }

                            String[] cashBoxArray = cashBoxNums.split(";");
                            ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                            for (int i = 0; i < cashBoxArray.length; i++) {
                                BoxDetail boxDetail = new BoxDetail();
                                boxDetail.setNum(cashBoxArray[i]);
                                cashBoxList.add(boxDetail);
                            }

                            Log.d(TAG, "getOrganizationId()" + GApplication.user.getOrganizationId());
                            Log.d(TAG, "getOrganizationId()" + BankDoublePersonLogin.userid1);
                            byte[] cValue = null;
                            // modify end
                            getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, cashBoxList,
                                    GApplication.user.getOrganizationId(), getUserID(type),
                                    cValue, type, "3", "1", BankDoublePersonLogin.userid1);

                        }
                    }
                }
                if (flag.equals("QFweiqinghuishoutwo")) {
                    textname2 = o_Application.qingfen.getLoginUserName();
                    userid2 = o_Application.qingfen.getYonghuZhanghao();
                    fname_right = o_Application.qingfen.getLoginUserName();
                    fname_right = data.getStringExtra("name");
                    finger_right.setImageResource(R.drawable.sccuss);
                    if (o_Application.qingfen == null) {
                        userid2 = o_Application.qingfen.getYonghuZhanghao();
                        return;
                    }
                    username2.setText(fname_right + "");// 加入人
                    if (o_Application.qingfen.getLoginUserName() != null && GApplication.map != null) {
                        finger_left.setImageBitmap(GApplication.map);
                    } else {
                        if (null != user) {
                            fname_left = user;
                        }
                        if (null != username) {
                            fname_left = username;
                        }
                        finger_left.setImageResource(R.drawable.sccuss);

                    }
                    f1 = "1";
                    f2 = "2";

                    Log.e("kkk", GApplication.user.getYonghuZhanghao());
                    Log.e("kkk", fname_left);
                    Log.e("kkk", fname_right);
                    if (fname_left.equals(fname_right)) {
                        Toast.makeText(BankDoublePersonLogin.this, "请两位清分人员进行验证！", Toast.LENGTH_SHORT).show();
                    } else {
                        firstSuccess = false;
                        fname_left = null;
                        fname_right = null;

                        if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {


                            if (bizName != null && bizName.equals("未清回收钞箱出库")) {
                                System.out.println(StopNewClearBox.list.size());
                                System.out.println(bizName);
                                System.out.println("bizNum" + bizNum);
                                GolbalUtil.onclicks = true;
                                String[] cashBoxArray = cashBoxNums.split(";");
                                ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                                for (int i = 0; i < cashBoxArray.length; i++) {
                                    BoxDetail boxDetail = new BoxDetail();
                                    boxDetail.setNum(cashBoxArray[i]);
                                    cashBoxList.add(boxDetail);
                                }
                                Log.d(TAG, "getOrganizationId()" + GApplication.user.getOrganizationId());
                                Log.d(TAG, "userid2()" + BankDoublePersonLogin.userid2);
                                Log.d(TAG, "type()" + getUserID(type));
//                                这里面调用了2个接口// 这一要穿两个清分员和清分员id\
                                byte[] cValue = null;
                                getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, cashBoxList,
                                        GApplication.user.getOrganizationId(), getUserID(type),
                                        cValue, type, "4", "2", BankDoublePersonLogin.userid2);
//                                userid1和user2 都是清分员
                                Log.d(TAG, "userid1Boxoutdo" + userid1 + userid2);
                                Log.d(TAG, "userid1Boxoutdo" + userid1 + userid2);
                                getMoneyBoxOutDo().getemptyMoneyBoxoutdo(bizName, StopNewClearBox.list, planNum,
                                        userid1, userid2,
                                        GApplication.user.getOrganizationId(), bizNum, BoxDetailInfoDo.isfirst);

                            }

                            if (type == null) {
                                Log.d(TAG, " 请重新做");
                                // 登录
//                                getFingerCheck().fingerLoginCheck(finger);
//                                当type 为null应该处理
                            } else { // 交接 WebSiteJoin.webJoinID
                                if (admin.equals("加钞人员")) {
                                    byte[] cValue = null;
                                    Log.i("开始交接", "开始交接");
                                    if ("在行式".equals(OrderWork.type)) {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                                getUserID(type), cValue, type, f1, "2");


                                    } else {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list,
                                                GApplication.user.getOrganizationId(), "5", cValue, "C0",
                                                f1, "2");
                                    }

                                } else {
                                    // 如果是空钞箱交接
                                    if ("01".equals(type)) {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, Getnumber.list_boxdeatil,
                                                GApplication.user.getOrganizationId(), getUserID(type),
                                                ShareUtil.ivalBack, type, f1, "2");
                                    } else {// 当前判断分支是：未清回收钞箱出库
                                        // modify begin 2017-4-25 by liuchang 清分员指纹验证通过后交接的钞箱数量有原来通过sql查询，
                                        // 改为通过页面中“钞箱出入库前检测”按钮检测后确认的数量
                                        String[] cashBoxArray = cashBoxNums.split(";");
                                        ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                                        for (int i = 0; i < cashBoxArray.length; i++) {
                                            BoxDetail boxDetail = new BoxDetail();
                                            boxDetail.setNum(cashBoxArray[i]);
                                            cashBoxList.add(boxDetail);
                                        }
                                        // modify end
                                        byte[] cValue = null;
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, cashBoxList,
                                                GApplication.user.getOrganizationId(), getUserID(type),
                                                cValue, type, f1, "2");
                                    }
                                    Log.i("aaaaaaa", "sss");
                                }
                            }
                            // 2.第二位交接人验证指纹
                        } else {
//                            ShareUtil.finger_bitmap_right = img;
//                            finger_right.setImageBitmap(ShareUtil.finger_bitmap_right);
                            f2 = "2";

                            if (type == null) { // 登录
                                Finger finger = new Finger();
                                finger.setCorpId(GApplication.user.getOrganizationId()); // 机构ID
                                finger.setRoleId(GApplication.user.getLoginUserId()); // 角色ID
                                finger.setcValue(ShareUtil.ivalBack); // 特征值
                                getFingerCheck().fingerLoginCheck(finger);
                            } else { // 交接 WebSiteJoin
                                if (isscuess) {
                                    return;
                                }
                                if (admin.equals("加钞人员")) {
                                    if ("在行式".equals(OrderWork.type)) {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list, WebSiteJoin.webJoinID,
                                                getUserID(type), ShareUtil.ivalBack, type, f2, "2");
                                    } else {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, WebJoin.list,
                                                GApplication.user.getOrganizationId(), "5", ShareUtil.ivalBack, "C0",
                                                f2, "2");
                                    }

                                } else {
                                    // 如果是空钞箱交接
                                    if ("01".equals(type)) {
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, Getnumber.list_boxdeatil,
                                                GApplication.user.getOrganizationId(), getUserID(type),
                                                ShareUtil.ivalBack, type, f2, "2");
                                    } else {
                                        // 未清回收钞箱出库交接
                                        // modify begin 2017-4-25 by liuchang 清分员指纹验证通过后交接的钞箱数量有原来通过sql查询，
                                        // 改为通过页面中“钞箱出入库前检测”按钮检测后确认的数量
                                        String[] cashBoxArray = cashBoxNums.split(";");
                                        ArrayList<BoxDetail> cashBoxList = new ArrayList<BoxDetail>();
                                        for (int i = 0; i < cashBoxArray.length; i++) {
                                            BoxDetail boxDetail = new BoxDetail();
                                            boxDetail.setNum(cashBoxArray[i]);
                                            cashBoxList.add(boxDetail);
                                        }
                                        // modify end
                                        byte[] cValue = null;
                                        getSaveAuthLogBiz().getSaveAuthLog(planNum, cashBoxList,
                                                GApplication.user.getOrganizationId(), getUserID(type),
                                                cValue, type, f2, "2");
                                    }
                                }

                            }
                            towseconds();//  跳转页面
                        }

                    }
                }

                if (flag.equals("EmptAfterCleanone")) { //空钞箱出库后给清分
                    String zhanghao = data.getStringExtra("zhanghao");
                    String username = data.getStringExtra("name");
                    textname1 = username;
                    if (null != zhanghao || !zhanghao.equals("")) {
                        fingeruserone = zhanghao;
                    }
                    if (null != o_Application.qingfen) {
                        userid1 = o_Application.qingfen.getYonghuZhanghao();
                    }
                    finger_left.setImageResource(R.drawable.sccuss);
                    if (o_Application.qingfen.getLoginUserName().isEmpty()) {
                    } else {

                    }
                    fname_left = username;
                    username1.setText(fname_left);
                    f1 = "3";
                    one = 0;
                    firstSuccess = true;
                    Log.e("kkk", o_Application.qingfen.getYonghuZhanghao());
                    if (admin.equals("库管员")) {
                        Toast.makeText(BankDoublePersonLogin.this, "验证成功" + "", Toast.LENGTH_SHORT).show();
                    }
                    byte[] cValue = null;
                    getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, Getnumber.list_boxdeatil,
                            GApplication.user.getOrganizationId(), getUserID(type),
                            cValue, type, "3", "1", BankDoublePersonLogin.userid1);
                }
                if (flag.equals("EmptAfterCleantwo")) {
                    textname2 = o_Application.qingfen.getLoginUserName();
                    if (null == userid2) {
                        userid2 = o_Application.qingfen.getYonghuZhanghao();
                    }
                    fname_right = o_Application.qingfen.getLoginUserName();
                    fname_right = data.getStringExtra("name");
                    finger_right.setImageResource(R.drawable.sccuss);
                    // System.out.println("one is :"+one);
                    if (o_Application.qingfen == null) {
                        return;
                    }
                    username2.setText(fname_right + "");// 加入人
                    if (o_Application.qingfen.getLoginUserName() != null && GApplication.map != null) {
                        finger_left.setImageBitmap(GApplication.map);
                    } else {
                        if (null != user) {
                            fname_left = user;
                        }
                        finger_left.setImageResource(R.drawable.sccuss);

                    }
                    f1 = "1";
                    f2 = "2";

                    Log.e("kkk", GApplication.user.getYonghuZhanghao());
                    Log.e("kkk", fname_left);
                    Log.e("kkk", fname_right);
                    if (fname_left.equals(fname_right)) {
                        Toast.makeText(BankDoublePersonLogin.this, "请两位清分人员进行验证！", Toast.LENGTH_SHORT).show();
                    } else {
                        firstSuccess = false;
                        fname_left = null;
                        fname_right = null;

                        if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {
                            if (type == null) { // 登录
                                Finger finger = new Finger();
                                finger.setCorpId(GApplication.user.getOrganizationId()); // 机构ID
                                finger.setRoleId(GApplication.user.getLoginUserId()); // 角色ID
                                finger.setcValue(ShareUtil.ivalBack); // 特征值
                                getFingerCheck().fingerLoginCheck(finger);
                            } else { // 交接 WebSiteJoin
                                // 如果是空钞箱交接
                                byte[] cValue = null;
                                getSaveAuthLogBiz().getSaveAuthLogbyPW(planNum, Getnumber.list_boxdeatil,
                                        GApplication.user.getOrganizationId(), getUserID(type),
                                        cValue, type, "3", "1", BankDoublePersonLogin.userid2);
                            }
//                            缺少保存日志


                            towseconds();//  跳转页面  只存saveAuthLog  这个 没调用其他东西
                        }

                    }

                }

            }
        } else if (requestCode == 0700) {//  清分管理员  做出库前清分工作
            if (null != data) {
                String flag = data.getStringExtra("FLAG");
                String result1 = data.getStringExtra("netpoincollect");

                Log.e("kkk", "onStop" + flag);

                if (flag.equals("qingfenguanlione")) {
                    String zhanghao = data.getStringExtra("zhanghao");
                    String username = data.getStringExtra("name");
                    if (null != zhanghao || !zhanghao.equals("")) {
                        fingeruserone = zhanghao;
                    }
                    userid1 = GApplication.user.getYonghuZhanghao();
                    finger_left.setImageResource(R.drawable.sccuss);
                    if (GApplication.user.getYonghuZhanghao().isEmpty()) {
                    } else {

                    }
                    fname_left = username;
                    username1.setText(fname_left);
                    f1 = "3";
                    one = 0;
                    firstSuccess = true;
                    if (null != f1 && f1.equals("3")) {
                        if (null != GApplication.user) {
                            textname1 = admin + GApplication.user.getLoginUserName();
                        }
                        if ("清分管理".equals(where) && GApplication.user.getLoginUserId().equals("7")) {

                            Toast.makeText(BankDoublePersonLogin.this, "验证成功" + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                Toast.makeText(BankDoublePersonLogin.this, "1！" + username, Toast.LENGTH_SHORT).show();

                if (flag.equals("qingfenguanlitwo")) {
                    if (null == userid2) {
                        userid2 = GApplication.user.getYonghuZhanghao();
                    }
                    fname_right = GApplication.user.getLoginUserName();
                    fname_right = data.getStringExtra("name");


                    finger_right.setImageResource(R.drawable.sccuss);
                    // System.out.println("one is :"+one);
                    Toast.makeText(BankDoublePersonLogin.this, "2！" + GApplication.user.getLoginUserName(), Toast.LENGTH_SHORT).show();

                    if (GApplication.user == null) {

                        return;
                    }

                    if (GApplication.user.getYonghuZhanghao() != null && GApplication.map != null) {
                        finger_left.setImageBitmap(GApplication.map);
                    } else {
                        if (null != user) {
                            fname_left = user;
                        }
                        finger_left.setImageResource(R.drawable.sccuss);

                    }
                    f1 = "1";
                    f2 = "2";

                    Log.e("kkk", GApplication.user.getYonghuZhanghao());
                    Log.e("kkk", fname_left);
                    Log.e("kkk", fname_right);
                    if (fname_left.equals(fname_right)) {
                        Toast.makeText(BankDoublePersonLogin.this, "请两位清分人员进行验证！", Toast.LENGTH_SHORT).show();
                    } else {
                        firstSuccess = false;
                        fname_left = null;
                        fname_right = null;

                        Log.e("BankDoublePersonLogin", "账号新方法");
                        if (null != GApplication.user) {
                            textname2 = admin + GApplication.user.getLoginUserName();
                        }
                        towseconds();//  跳转页面
                    }

                }
            }


        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        userid1 = "";
        userid2 = "";
    }
}
