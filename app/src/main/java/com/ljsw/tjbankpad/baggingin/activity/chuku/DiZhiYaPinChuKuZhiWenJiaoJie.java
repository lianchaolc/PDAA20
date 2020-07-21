package com.ljsw.tjbankpad.baggingin.activity.chuku;

import afu.util.BaseFingerActivity;

import java.net.SocketTimeoutException;

import com.application.GApplication;
import com.entity.SystemUser;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.CashToPackgersActivity;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.service.CashToPackgersService;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.service.YanZhengZhiWenService;
import com.ljsw.tjbankpda.util.BianyiType;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/***
 *
 * 清分人员的双人指纹验证 双人指纹验证
 *
 * @author Administrator BankDoublePersonLogin 包含清分人员的验证
 */
public class DiZhiYaPinChuKuZhiWenJiaoJie extends BaseFingerActivity implements OnClickListener {
    protected static final String TAG = "DiZhiYaPinChuKu";
    private TextView setName_left, setName_right, prompt, yy_fingerTop, top;
    private ImageView img_left, img_right;
    private SystemUser result_user;// 指纹验证
    public static boolean firstSuccess = false; // 第一位是否已成功验证指纹
    String f1 = null; // 第一个按手指的人
    String f2 = null; // 第二个按手指的人
    StringBuffer f1AndF2 = new StringBuffer();
    private ManagerClass manager;
    private OnClickListener OnClick, OnClick2;
    private String left_name, right_name;
    private int wrongleftNum, wrongrightNum;// 指纹验证失败次数统计
    private Intent intent;
    private Handler handler;
    private TextView userTxt1;
    private TextView userTxt2;
    private boolean isFlag = true;

    private String jiaosheId;
    private String blackResult;// 传向后台的返回结果
    private String cleanmmangerone = "";// 第一位清分员
    private String cleanmangerother = ""; // 第二位清分员编号
    private Dialog dialog; /// 成功显示
    private Dialog dialogfa;// 失败的dialog显示
    private String Cardid = "";
    private boolean checkcardid = false;// 判断是否走现金装袋的方法默认不走


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committedgoods_qingfenshuangrenjiaojie);
        manager = new ManagerClass();
        intent = new Intent();
        userTxt1 = (TextView) this.findViewById(R.id.qfusername11);
        userTxt2 = (TextView) this.findViewById(R.id.qfusername22);
        prompt = (TextView) this.findViewById(R.id.qfyanshi);

        Intent intent1 = getIntent();
        Cardid = intent1.getStringExtra("cardid");
        if (Cardid != null) {
            checkcardid = true;
        }

        initView();

        OnClick = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                if (isFlag) {
                    isFlag = false;
                    YanZhenFinger yf = new YanZhenFinger();
                    yf.start();
                }

            }
        };

        OnClick2 = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
            }
        };

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onResume() {
        super.onResume();

        isFlag = true;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isFlag = true;
                switch (msg.what) {
                    case 1:
                        // 第一位验证
                        if (!firstSuccess && !"1".equals(f1)) {
                            img_left.setImageBitmap(ShareUtil.finger_bitmap_left);// 有值不为null
                            left_name = result_user.getLoginUserName();
                            ShareUtil.zhiwenid_left = result_user.getLoginUserId();// 12001032

                            setName_left.setText(left_name);
                            f1 = "1";

                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < ShareUtil.ivalBack.length; i++) {
                                sb.append(ShareUtil.ivalBack[i]);
                            }
                            firstSuccess = true;
                            yy_fingerTop.setText("第一位验证成功");
                            if (S_application.jiaojieType == 1) {
                                prompt.setText("请第二位库管员按手指...");
                            } else {
                                System.out.println("2015-11-09:hander");
                                prompt.setText("请第二位清分人员按手指...");
                            }
                            // 第二位验证
                        } else if (firstSuccess && !"2".equals(f2)) {
                            right_name = result_user.getLoginUserName();
                            if (right_name != null && right_name.equals(left_name)) {
                                System.out.println("重复验证了吗？");
                                yy_fingerTop.setText("重复验证!");
                            } else {
                                yy_fingerTop.setText("");
                                img_right.setImageBitmap(ShareUtil.finger_bitmap_right);
                                ShareUtil.zhiwenid_right = result_user.getLoginUserId();
                                System.out.println("右边指纹特征值：" + ShareUtil.finger_jiaojie_qingfen_right);
                                setName_right.setText(right_name);
                                f2 = "2";
                                f1AndF2.append(ShareUtil.zhiwenid_left + BianyiType.xiahuaxian + ShareUtil.zhiwenid_right);
                                S_application.s_userWangdian = f1AndF2.toString();
                                prompt.setText("验证成功！");
                                top.setText("");
                                firstSuccess = false;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        try {
                                            Thread.sleep(1000);
                                            handler.sendEmptyMessage(4);
                                        } catch (InterruptedException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                        break;

                    case -1:
                        manager.getAbnormal().timeout(DiZhiYaPinChuKuZhiWenJiaoJie.this, "网络连接失败,重试?", OnClick);
                        break;
                    case -4:
                        manager.getAbnormal().timeout(DiZhiYaPinChuKuZhiWenJiaoJie.this, "验证超时,重试?", OnClick);
                        break;
                    case 0:
                        // 验证3次失败跳登录页面 未实现
                        if (f1 == null) {
                            wrongleftNum++;
                            yy_fingerTop.setText("验证失败:" + wrongleftNum + "次");
                        } else if (f2 == null) {
                            wrongrightNum++;
                            yy_fingerTop.setText("验证失败:" + wrongrightNum + "次");
                        } else {
                            manager.getAbnormal().timeout(DiZhiYaPinChuKuZhiWenJiaoJie.this, "验证已完成!",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            manager.getAbnormal().remove();
                                            yy_fingerTop.setText("");
                                            return;
                                        }
                                    });
                        }
                        if (wrongleftNum >= ShareUtil.three) {
                            // 左侧跳用户登录
                            firstSuccess = false;
                            intent.setClass(DiZhiYaPinChuKuZhiWenJiaoJie.this, CleanOpretarDoubleLogin.class);
                            DiZhiYaPinChuKuZhiWenJiaoJie.this.startActivityForResult(intent, 1);
                            yy_fingerTop.setText("");
                            wrongleftNum = 0;
                        } else if (wrongrightNum >= ShareUtil.three) {
                            // 右侧跳用户登录
                            firstSuccess = true;
                            intent.setClass(DiZhiYaPinChuKuZhiWenJiaoJie.this, CleanOpretarDoubleLogin.class);
                            DiZhiYaPinChuKuZhiWenJiaoJie.this.startActivityForResult(intent, 1);
                            wrongrightNum = 0;
                            yy_fingerTop.setText("");
                        }
                        break;
                    case 4:
                        if (f1 != null && f2 != null) {// 指纹交接后提交数据
                            Log.e(TAG, "指纹交接成功");
                            sendPlanItem();

                            if (checkcardid) {
                                // 执行一个现金装袋的方法
                                CleanClienToCash();

                            }
                        }
                        break;
                    case 9:
                        dialog = new Dialog(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        LayoutInflater inflater = LayoutInflater.from(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        View v = inflater.inflate(R.layout.dialog_success, null);
                        Button but = (Button) v.findViewById(R.id.success);
                        but.setText("出库成功");
                        dialog.setCancelable(false);
                        dialog.setContentView(v);
                        if (but != null) {
                            but.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();

                                    Intent intent = new Intent(DiZhiYaPinChuKuZhiWenJiaoJie.this,
                                            DiZhiYaPinChuKuActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        }
                        dialog.show();

                        break;

                    case 10:
                        dialogfa = new Dialog(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        LayoutInflater inflaterfa = LayoutInflater.from(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
                        Button butfa = (Button) vfa.findViewById(R.id.success);
                        butfa.setText("" + blackResult);
                        dialogfa.setCancelable(false);
                        dialogfa.setContentView(vfa);
                        if (butfa != null) {
                            butfa.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialogfa.dismiss();
                                }
                            });
                        }
                        dialogfa.show();
                        break;

                    case 11:
                        dialog = new Dialog(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        LayoutInflater cashtopackgerinflater = LayoutInflater.from(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        View cashtopackgerv = cashtopackgerinflater.inflate(R.layout.dialog_success, null);
                        Button cashtopackgerbut = (Button) cashtopackgerv.findViewById(R.id.success);
                        cashtopackgerbut.setText("装袋成功");
                        dialog.setCancelable(false);
                        dialog.setContentView(cashtopackgerv);
                        if (cashtopackgerbut != null) {
                            cashtopackgerbut.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(DiZhiYaPinChuKuZhiWenJiaoJie.this,
                                            CashToPackgersActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        }
                        dialog.show();

                        break;
                    case 12:
                        dialogfa = new Dialog(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        LayoutInflater cashtopackgerinflaterfa = LayoutInflater.from(DiZhiYaPinChuKuZhiWenJiaoJie.this);
                        View cashtopackgervfa = cashtopackgerinflaterfa.inflate(R.layout.dialog_success, null);
                        Button cashtopackgerbutfa = (Button) cashtopackgervfa.findViewById(R.id.success);
                        cashtopackgerbutfa.setText("数据提交失败请重试!");
                        dialogfa.setCancelable(false);
                        dialogfa.setContentView(cashtopackgervfa);
                        if (cashtopackgerbutfa != null) {
                            cashtopackgerbutfa.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialogfa.dismiss();
                                }
                            });
                        }
                        dialogfa.show();
                        break;
                }
            }
        };
    }

    public void initView() {
        if (S_application.jiaojieType == 7) {
            prompt.setText("请第一位清分员按手指...");
            jiaosheId = "4";
        } else {
            userTxt1.setText("清分人员");
            userTxt2.setText("清分人员");
            prompt.setText("请第一位网点人员按手指...");
            jiaosheId = ShareUtil.WdId;
        }

        img_left = (ImageView) this.findViewById(R.id.qfyyjj_left);
        img_right = (ImageView) this.findViewById(R.id.qfyyjj_right);
        yy_fingerTop = (TextView) this.findViewById(R.id.qfyanshi);
        top = (TextView) findViewById(R.id.qftextViewtop);
        setName_left = (TextView) this.findViewById(R.id.qfyy_leftName);
        setName_right = (TextView) this.findViewById(R.id.qfyy_rightName);
        findViewById(R.id.qfyayun_backS5).setOnClickListener(this);
        findViewById(R.id.qfyanshi).setOnClickListener((OnClickListener) this);
    }

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        top.setText("正在获取指纹特征值！");
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        super.getCharImgSucceed(charBytes, img);

        if (!firstSuccess && !"1".equals(f1)) {
            ShareUtil.finger_bitmap_left = img;
        } else {
            ShareUtil.finger_bitmap_right = img;
        }

        ShareUtil.ivalBack = charBytes;

        if (isFlag) {
            top.setText("正在验证指纹...");
            isFlag = false;
            YanZhenFinger yf = new YanZhenFinger();
            yf.start();
        }
    }

    /**
     * 指纹验证线程 1 正确的记过有图样和人员的名称 2错误的结果 显示图样但是人员们名称不显示
     *
     * @author Administrator
     */
    class YanZhenFinger extends Thread {
        Message m;

        public YanZhenFinger() {
            super();
            m = handler.obtainMessage();
        }

        @Override
        public void run() {
            super.run();
            YanZhengZhiWenService yanzheng = new YanZhengZhiWenService();
            System.out.println("网点机构:" + S_application.wangdianJigouId);
            System.out.println("网点角色ID" + 7);
            System.out.println("网点指纹特征值:" + ShareUtil.ivalBack);
            try {
                // 网络请求
                result_user = yanzheng.checkFingerprint("988050000", // 机构编号
                        7 + "", // 网点人员角色Id
                        ShareUtil.ivalBack); // 指纹的特征
                Log.e(TAG, "==" + cleanmmangerone);
                if (result_user != null) {
                    System.out.println("我是帐号吗==" + result_user.getLoginUserId());
                    if (cleanmmangerone == null || "".equals(cleanmmangerone)) {
                        cleanmmangerone = result_user.getLoginUserId();
                    } else {
                        cleanmangerother = result_user.getLoginUserId();
                    }
                    m.what = 1;
                } else {
                    m.what = 0;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                System.out.print(e);
                m.what = -4;
            } catch (Exception e) {
                System.out.print(e);
                e.printStackTrace();
                m.what = -1;
            } finally {
                handler.sendMessage(m);

                fingerUtil.getFingerCharAndImg();
            }
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        top.setText("");
        if (null == f1) {
            firstSuccess = false;
            if (S_application.jiaojieType == 1) {
                prompt.setText("请第一位清分员按手指...");
            } else {
                prompt.setText("请第一位清分人员按手指...");
            }
        } else {
            if (S_application.jiaojieType == 1) {
                prompt.setText("请第二位清分按手指...");
            } else {
                prompt.setText("请第一位清分员按手指...");
            }
        }
        /// 通过登录密码的方式进行验证获取到的账户名称
        if (arg2 != null) {
            Bundle bundle = arg2.getExtras();
            String isOk = bundle.getString("isOk");
            String name = bundle.getString("name");
            if (isOk.equals("success") && !firstSuccess) {
                f1 = "1";
                left_name = o_Application.qingfenyuan.getLoginUserName();// 改动190225
                setName_left.setText(left_name);
                img_left.setImageResource(R.drawable.result_isok);
                firstSuccess = true;
                top.setText("第一位验证成功");
                ShareUtil.zhiwenid_left = name;
                cleanmmangerone = name;
                if (S_application.jiaojieType == 1) {
                    prompt.setText("请第二位清分员按手指...");
                } else {
                    System.out.println("2015-11-09:hander2");
                    prompt.setText("请第二位清分员按手指...");
                }
                S_application.left_user = GApplication.user;
            } else if (isOk.equals("success") && firstSuccess) {
                f2 = "2";
                right_name = o_Application.qingfenyuan.getLoginUserName();// 改动190225
                if (right_name != null && right_name.equals(left_name)) {
                    yy_fingerTop.setText("重复验证!");
                } else {
                    setName_right.setText(right_name);
                    img_right.setImageResource(R.drawable.result_isok);
                    firstSuccess = false;
                    yy_fingerTop.setText("");
                    ShareUtil.zhiwenid_right = name;
                    cleanmangerother = name;
                    prompt.setText("验证成功！！");
                    S_application.right_user = o_Application.qingfenyuan;// 改动190225
                    f1AndF2.append(ShareUtil.zhiwenid_left + BianyiType.xiahuaxian + ShareUtil.zhiwenid_right);
                    S_application.s_userWangdian = f1AndF2.toString();
                    if (f1 != null && f2 != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    handler.sendEmptyMessage(4);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        manager.getAbnormal().timeout(this, "身份验证未通过,请完成验证!", OnClick2);
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.yayun_backS5:// 返回
                DiZhiYaPinChuKuZhiWenJiaoJie.this.finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFlag = false;
        manager.getRuning().remove();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        S_application.left_user = null;
        S_application.right_user = null;
        f2 = null;
        f1 = null;
        firstSuccess = false;
    }

    /***
     * 向后台提交数据 清分人员 清分人员2 任务编号 货位管理员
     */
    private void sendPlanItem() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.e(TAG, "===" + o_Application.qlruku.getDanhao() + GApplication.loginname + cleanmmangerone
                            + "==" + cleanmangerother);
                    blackResult = new GetResistCollateralBaggingService().getResultfingerprint(
                            o_Application.qlruku.getDanhao(), GApplication.loginname, cleanmmangerone,
                            cleanmangerother);
                    // json转成list
                    Log.e(TAG, "===" + blackResult);
//					===RW1QF0620181031155900==cleanmmangerone12001033==12001032
                    if ("00".equals(blackResult)) {
                        handler.sendEmptyMessage(9);
                    } else {
                        handler.sendEmptyMessage(10);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SocketTimeout异常" + e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(TAG, "NullPointer异常" + e);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception异常" + e);
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();
    }

    /***
     * |lc 20190430 现金装袋后指纹传送数据
     */

    private void CleanClienToCash() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.e(TAG, "===" + Cardid + cleanmmangerone + "==" + cleanmangerother);
                    blackResult = new CashToPackgersService().updateCashClearingWork(cleanmmangerone, cleanmangerother,
                            Cardid);
                    Log.e(TAG, "===" + blackResult);
//					===RW1QF0620181031155900==cleanmmangerone12001033==12001032
                    if ("00".equals(blackResult)) {
                        handler.sendEmptyMessage(11);
                    } else {
                        handler.sendEmptyMessage(12);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SocketTimeout异常" + e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(TAG, "NullPointer异常" + e);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception异常" + e);
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();

    }

    /**
     * 监听地步返回键关闭当前页面
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DiZhiYaPinChuKuZhiWenJiaoJie.this.finish();
    }
}
