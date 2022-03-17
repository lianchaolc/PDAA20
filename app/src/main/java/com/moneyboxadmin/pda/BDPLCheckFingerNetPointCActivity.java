package com.moneyboxadmin.pda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.entity.SystemUser;
import com.example.app.activity.KuanXiangNetPointCollectActivity;
import com.example.app.entity.User;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.ljsw.tjbankpda.db.activity.QingFenYuanDengLu;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.service.SecondLogin;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.loginsystem.biz.SystemLoginBiz;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.moneyboxadmin.biz.LoginInfoByNetPont;
import com.poka.device.ShareUtil;
import com.service.NetService;

import java.net.SocketTimeoutException;

/****
 *
 * 网点指纹错误三次后需要账户和密码
 * 仿写
 * QingFenYuanDengLu
 * 二位翁
 */
public class BDPLCheckFingerNetPointCActivity extends Activity implements OnTouchListener {
    int one = 0;// 统计第一个验证指纹失败的次数
    int two = 0;// 统计第二个验证指纹失败的次数
    Button login; // 登陆按钮
    Button cancel; // 取消按钮
    EditText editname; // 用户名输入框 帐号
    EditText editpwd; // 密码输入框 密码

    TextView textlogin; // 网络状态提示
    String UserNo = null; // 用户名
    String pwd = null; // 密码
    OnClickListener onclickreplace;
    int error = 3;
    boolean network = true; // 是否有网络
    public static boolean current; // 当前界面

    // 记住用户名
    SharedPreferences share;
    Editor editor;
    private ManagerClass managerClass;

    private SystemLoginBiz systemLogin;
    private LoginInfoByNetPont   loginInfoByNetPont= null; //网点人员登录实体
    public SystemLoginBiz getSystemLogin() {
        if (systemLogin == null) {
            systemLogin = new SystemLoginBiz();
        }
        return systemLogin;
    }
    private String jigouidCheck;//  存放第一个人机构id和第二人进行对比
    private String jigouidCheck2;//  存放第一个人机构id和第二人进行对比

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangdian_checkfinger);
        managerClass = new ManagerClass();
        login = (Button) findViewById(R.id.wangdian_login_btn1);
        cancel = (Button) findViewById(R.id.wangdian_login_cancel1);
        editname = (EditText) findViewById(R.id.wangdian_name1);
        editpwd = (EditText) findViewById(R.id.wangdian_pwd1);
        textlogin = (TextView) findViewById(R.id.netmsgtext2);

        login.setOnTouchListener(this);
        cancel.setOnTouchListener(this);


        // 接收网络状态广播后，发出handler通知
        NetService.handnet = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    textlogin.setVisibility(View.GONE);
                    network = true;

                } else {
                    textlogin.setVisibility(View.VISIBLE);
                    network = false;
                }
            }
        };
        NetService.setSendMsg(this);

        // 手动判断是否有网络
        if (NetService.info != null && !NetService.info.isConnectedOrConnecting()) {
            textlogin.setVisibility(View.VISIBLE);
            network = false;
        } else if (NetService.info == null) {
            textlogin.setVisibility(View.VISIBLE);
            network = false;
        }

        // 重试单击事件
        onclickreplace = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                managerClass.getAbnormal().remove();
                if (network) {
                    managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "正在登录...");
                    LoginBynetPointUser();
                } else {
                    managerClass.getGolbalView().toastShow(BDPLCheckFingerNetPointCActivity.this, "网络没有连通，无法登录");
                }
            }
        };

        // Hand通知操作
//        getSystemLogin().handler_login = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                managerClass.getRuning().remove();
//                super.handleMessage(msg);
//
//                switch (msg.what) {
//                    case 1:
//                        error = 3;
//                        Log.e("", "orgid===" + GApplication.user.getOrganizationId());
//                        //  第一个人只验证是否为网点人员就好第二个人需要验证是否和第一个人同意网点和角色5
//
//                        if (!"5".equals(GApplication.user.getLoginUserId())) {// 角色不为5
//                            System.out.println("网点登录匹配角色id：" + GApplication.user.getLoginUserId());
//                            managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "请使用网点人员帐号登录",
//                                    new OnClickListener() {
//                                        @Override
//                                        public void onClick(View arg0) {
//                                            managerClass.getAbnormal().remove();
//                                            editname.setText("");
//                                            editpwd.setText("");
//                                        }
//                                    });
//                        } else {
//
//                            GApplication.getApplication().app_hash.put("login_username", editname.getText());
//                            /**
//                             * SM得到传来的标识
//                             */
//                            Intent intent = getIntent();
//                            Bundle netpoincollect = intent.getExtras();
//                            Intent     intentC   = new Intent(BDPLCheckFingerNetPointCActivity.this, KuanXiangNetPointCollectActivity.class);
//                            if (netpoincollect != null) {
//                                String flag = netpoincollect.getString("FLAG");
//                                if (flag.equals("wangdianone")) {
//                                    Bundle bundlebdpcf=new Bundle();
//                                    bundlebdpcf.putString("FLAG", flag);
//                                    bundlebdpcf.putString("netpoincollect", "账号验证成功");
//                                    bundlebdpcf.putString("zhanghao",name);
//                                    bundlebdpcf.putString("name",GApplication.user.getLoginUserName());
//                                    User u = new User();
//                                    u.setUserzhanghu(name);
//                                    u.setPwd(pwd);
//                                    u.setUsername(GApplication.user.getLoginUserName());
//                                    u.setUserzhanghu(GApplication.user.getYonghuZhanghao());
//                                    GApplication.wd_user1 = u;
//                                    intentC.putExtras(bundlebdpcf);
//                                    managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "用户名和密码验证成功");
//
//                                    BDPLCheckFingerNetPointCActivity.this.setResult(7, intentC);
//                                    BDPLCheckFingerNetPointCActivity.this.finish();
//                                }
//                                if (flag.equals("wangdiantwo")) {
//                                    Bundle bundlebdpcfwdt=new Bundle();
//                                    bundlebdpcfwdt.putString("FLAG", flag);
//                                    bundlebdpcfwdt.putString("name",GApplication.user.getLoginUserName());
//                                    String left = netpoincollect.getString("left");
//                                    User u = new User();
//                                    u.setUserzhanghu(name);
//                                    u.setPwd(pwd);
//                                    u.setUsername(GApplication.user.getLoginUserName());
//                                    GApplication.wd_user2 = u;
//                                    System.out.println("GApplication.user.getLoginUserName()2  :"
//                                            + GApplication.user.getLoginUserName());
//                                    if(null!=left){
//
//                                    if (left.equals(name)) {
//                                        Toast.makeText(BDPLCheckFingerNetPointCActivity.this, "该用户已经验证过!", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        jigouidCheck = GApplication.jigouid;
//                                            intentC.putExtras(bundlebdpcfwdt);
//                                            managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "正在验证用户名和密码...");
//
//                                            BDPLCheckFingerNetPointCActivity.this.setResult(7, intentC);
//                                            BDPLCheckFingerNetPointCActivity.this.finish();
//
//                                    }
//                                    }
//                                }
//                            }
//                        }
//                        break;
//                    case 0:
//                        if (msg.obj != null) {
//                            managerClass.getGolbalView().toastShow(BDPLCheckFingerNetPointCActivity.this, msg.obj.toString());
//                        } else {
//                            managerClass.getGolbalView().toastShow(BDPLCheckFingerNetPointCActivity.this, "");
//                        }
//                        break;
//                    case -4:
//                        managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "登陆超时，重新链接？", onclickreplace);
//                        break;
//                    case -1:
//                        managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "登录出现异常", onclickreplace);
//                        break;
//                    case -3:
//                        managerClass.getGolbalView().toastShow(BDPLCheckFingerNetPointCActivity.this, "用户或密码为空！");
//                        break;
//
//                }
//
//            }
//
//        };

        share = this.getPreferences(0);
        editor = share.edit();

        // 把当前activity放进集合
        GApplication.addActivity(this, "1system");
    }

    /***
     * 网点人员登录  类似清分管理员
     */
    private void LoginBynetPointUser() {
            managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "登录中...");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {

                        o_Application.netpoint= new SecondLogin().login(UserNo, pwd);
                        if (o_Application.netpoint != null) {
                            handler.sendEmptyMessage(2);
                        } else {
                            handler.sendEmptyMessage(3);
                        }
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(1);
                    }
                }

            }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        current = true;
        Log.i("1111", "11111");
    }

    @Override
    public boolean onTouch(View view, MotionEvent even) {
        // 按下的时候
        if (MotionEvent.ACTION_DOWN == even.getAction()) {
            switch (view.getId()) {
                // 登陆
                case R.id.wangdian_login_btn1:
                    login.setBackgroundResource(R.drawable.buttom_select_press);
                    break;
                // 取消
                case R.id.wangdian_login_cancel1:
                    cancel.setBackgroundResource(R.drawable.buttom_select_press);
                    break;
            }
        }

        // 手指松开的时候
        if (MotionEvent.ACTION_UP == even.getAction()) {
            switch (view.getId()) {
                // 登陆
                case R.id.wangdian_login_btn1:
                    UserNo = editname.getText().toString();
                    pwd = editpwd.getText().toString();
                    login.setBackgroundResource(R.drawable.buttom_selector_bg);
                    // 非空验证
                    if (isnull(UserNo, pwd)) {
                        // 有网络才可以执行登录操作
                        Log.i("network", network + "");
                        if (network) {
                            // 提示
                            try {
                                managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "正在登录...");
                            } catch (Exception e) {
                                e.printStackTrace();
                                // System.out.println(e.getMessage());
                            }
                            // System.out.println("开始调用登陆的方法========"+"checkFingerprint");
                            // 登陆方法
//                            getSystemLogin().login(name, pwd);
                            LoginBynetPointUser();
                        } else {
                            managerClass.getGolbalView().toastShow(this, "网络没有连通，无法登录");
                            // Toast.makeText(this,"网络没有连通，无法登录",
                            // Toast.LENGTH_LONG).show();
                        }
                    }

                    break;
                // 取消
                case R.id.wangdian_login_cancel1:

                    login.setBackgroundResource(R.drawable.buttom_select_press);
                    editname.setText("");
                    editpwd.setText("");
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
                // 登陆
                case R.id.wangdian_login_btn1:
                    login.setBackgroundResource(R.drawable.buttom_selector_bg);
                    break;
                // 取消
                case R.id.wangdian_login_cancel1:
                    cancel.setBackgroundResource(R.drawable.buttom_selector_bg);
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (UserNo != null) {
            editor.putString("uid", UserNo);
            editor.commit();
        }

        current = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = this.getPreferences(0);
        String nametext = preferences.getString("uid", "");
        editpwd.setFocusable(false);
        editpwd.setFocusableInTouchMode(true);
        editname.setText("");
    }

    // 非空验证
    public boolean isnull(String name, String pwd) {
        if (name == null || "".equals(name)) {
            managerClass.getGolbalView().toastShow(this, "用户名不能为空");
            return false;

        } else if (pwd == null || "".equals(pwd)) {
            managerClass.getGolbalView().toastShow(this, "密码不能为空");
            return false;
        }
        return true;
    }

    // 拦截Menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        new MenuShow().menu(this);
        MenuShow.pw.showAtLocation(findViewById(R.id.loginparent), Gravity.BOTTOM, 0, 0);
        return false;
    }


    @Override
    protected void onStop() {
        super.onStop();
        managerClass.getRuning().remove();
        BDPLCheckFingerNetPointCActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (GApplication.wd_user1 != null) {
                GApplication.wd_user1 = null;
            }
            if (GApplication.wd_user2 != null) {
                GApplication.wd_user2 = null;
            }
            if (ShareUtil.zhiwenid_left != null) {
                ShareUtil.zhiwenid_left = null;
            }
            if (ShareUtil.zhiwenid_right != null) {
                ShareUtil.zhiwenid_right = null;
            }
            if (o_Application.netpoint != null) {
                o_Application.netpoint = null;
            }

            BDPLCheckFingerNetPointCActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler() {

        @SuppressWarnings("static-access")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    managerClass.getRuning().remove();
                    managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "登录超时,重试?", onclickreplace);
                    break;
                case 1:
                    managerClass.getRuning().remove();
                    managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "网络连接失败,重试?", onclickreplace);
                    break;

                case 2:
                    managerClass.getRuning().remove();
//                    if (!o_Application.qingfenyuan.getOrganizationId().equals(o_Application.kuguan_db.getOrganizationId())
//                            || !o_Application.qingfenyuan.getLoginUserId().equals("17")) {
//                        managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "请使用清分管理员身份!", new OnClickListener() {
//
//                            @Override
//                            public void onClick(View arg0) {
//                                editname.setText("");
//                                editpwd.setText("");
//                                managerClass.getAbnormal().remove();
//                            }
//                        });
//                    } else {
//                        if (o_Application.left_user != null) {
//                            if (o_Application.qingfenyuan.getLoginUserName()
//                                    .equals(o_Application.left_user.getLoginUserName())) {
//                                managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "重复验证!", new OnClickListener() {
//                                    @Override
//                                    public void onClick(View arg0) {
//                                        managerClass.getAbnormal().remove();
//                                    }
//                                });
//                            } else {
//                                Intent intent = new Intent();
//                                intent.putExtra("isOk", "success");
//                                BDPLCheckFingerNetPointCActivity.this.setResult(BDPLCheckFingerNetPointCActivity.this.RESULT_OK, intent);
//                                o_Application.FingerJiaojieNum.add(name);
//                                BDPLCheckFingerNetPointCActivity.this.finish();
//                            }
//                        } else {
//                            Intent intent = new Intent();
//                            intent.putExtra("isOk", "success");
//                            BDPLCheckFingerNetPointCActivity.this.setResult(BDPLCheckFingerNetPointCActivity.this.RESULT_OK, intent);
//                            o_Application.FingerJiaojieNum.add(name);
//                            BDPLCheckFingerNetPointCActivity.this.finish();
//                        }
//                    }

                    error = 3;
                    //  第一个人只验证是否为网点人员就好第二个人需要验证是否和第一个人同意网点和角色5
                    if (!"5".equals(o_Application.netpoint.getLoginUserId())) {// 角色不为5 网点人员
                        System.out.println("网点登录匹配角色id：" +o_Application.netpoint.getLoginUserId());
                        managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, "请使用网点人员帐号登录",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getAbnormal().remove();
                                        editname.setText("");
                                        editpwd.setText("");
                                    }
                                });
                    } else {

                        GApplication.getApplication().app_hash.put("login_username", editname.getText());
                        /**
                         * SM得到传来的标识
                         */
                        Intent intent = getIntent();
                        Bundle netpoincollect = intent.getExtras();
                        Intent     intentC   = new Intent(BDPLCheckFingerNetPointCActivity.this, KuanXiangNetPointCollectActivity.class);
                        if (netpoincollect != null) {
                            String flag = netpoincollect.getString("FLAG");
                            if (flag.equals("wangdianone")) {
                                Bundle bundlebdpcf=new Bundle();
                                bundlebdpcf.putString("FLAG", flag);
                                bundlebdpcf.putString("netpoincollect", "账号验证成功");
                                bundlebdpcf.putString("zhanghao",UserNo);
                                bundlebdpcf.putString("name",o_Application.netpoint.getLoginUserName());
                                User u = new User();
                                u.setUserzhanghu(UserNo);
                                u.setPwd(pwd);
                                u.setUsername(o_Application.netpoint.getLoginUserName());
                                u.setUserzhanghu(o_Application.netpoint.getYonghuZhanghao());
                                GApplication.wd_user1 = u;
                                intentC.putExtras(bundlebdpcf);
                                managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "用户名和密码验证成功");

                                BDPLCheckFingerNetPointCActivity.this.setResult(7, intentC);
                                BDPLCheckFingerNetPointCActivity.this.finish();
                            }
                            if (flag.equals("wangdiantwo")) {
                                Bundle bundlebdpcfwdt=new Bundle();
                                bundlebdpcfwdt.putString("FLAG", flag);
                                bundlebdpcfwdt.putString("name",o_Application.netpoint.getLoginUserName());
                                String left = netpoincollect.getString("left");
                                User u = new User();
                                u.setUserzhanghu(UserNo);
                                u.setPwd(pwd);
                                u.setUsername(o_Application.netpoint.getLoginUserName());
                                GApplication.wd_user2 = u;
                                System.out.println("getLoginUserName()2"
                                        + GApplication.user.getLoginUserName());
                                if(null!=left){

                                    if (left.equals(UserNo)) {
                                        Toast.makeText(BDPLCheckFingerNetPointCActivity.this, "该用户已经验证过!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        jigouidCheck = o_Application.netpoint.getOrganizationId();
                                        intentC.putExtras(bundlebdpcfwdt);
                                        managerClass.getRuning().runding(BDPLCheckFingerNetPointCActivity.this, "正在验证用户名和密码...");

                                        BDPLCheckFingerNetPointCActivity.this.setResult(7, intentC);
                                        BDPLCheckFingerNetPointCActivity.this.finish();

                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    managerClass.getRuning().remove();
                    managerClass.getAbnormal().timeout(BDPLCheckFingerNetPointCActivity.this, S_application.wrong, new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            editpwd.setText("");
                            managerClass.getAbnormal().remove();
                        }
                    });
                    break;
                default:
                    break;
            }
        }

    };
}
