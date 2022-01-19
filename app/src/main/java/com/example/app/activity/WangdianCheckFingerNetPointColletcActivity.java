package com.example.app.activity;

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
import com.example.app.entity.User;
import com.example.app.util.Skip;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.loginsystem.biz.SystemLoginBiz;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.poka.device.ShareUtil;
import com.service.NetService;

import static com.example.app.activity.JiaoJieActivity.REQUEST_CODE_B;

/****
 *
 * 网点指纹错误账号吗密码验证
 */
public class WangdianCheckFingerNetPointColletcActivity extends Activity implements OnTouchListener {

    Button login; // 登陆按钮
    Button cancel; // 取消按钮
    EditText editname; // 用户名输入框 帐号
    EditText editpwd; // 密码输入框 密码

    TextView textlogin; // 网络状态提示
    String name = null; // 用户名
    String pwd = null; // 密码
    OnClickListener onclickreplace;
    SharedPreferences sharepre;
    String space; // 空间
    String webservice; // webservice地址
    int error = 3;

    boolean network = true; // 是否有网络
    public static boolean current; // 当前界面

    // 记住用户名
    SharedPreferences share;
    Editor editor;
    private ManagerClass managerClass;

    private SystemLoginBiz systemLogin;

    public SystemLoginBiz getSystemLogin() {
        if (systemLogin == null) {
            systemLogin = new SystemLoginBiz();
        }
        return systemLogin;
    }

    Intent intentA;

    private String jigouidCheck;//  存放第一个人机构id和第二人进行对比
    private String jigouidCheck2;//  存放第一个人机构id和第二人进行对比

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangdian_checkfinger);

        // 全局异常处理
        // CrashHandler.getInstance().init(this);
        managerClass = new ManagerClass();
        login = (Button) findViewById(R.id.wangdian_login_btn1);
        cancel = (Button) findViewById(R.id.wangdian_login_cancel1);
        editname = (EditText) findViewById(R.id.wangdian_name1);
        editpwd = (EditText) findViewById(R.id.wangdian_pwd1);
        textlogin = (TextView) findViewById(R.id.netmsgtext2);

        login.setOnTouchListener(this);
        cancel.setOnTouchListener(this);
        intentA = new Intent(WangdianCheckFingerNetPointColletcActivity.this, WangdianCheckFingerNetPointColletcActivity.class);


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
                    managerClass.getRuning().runding(WangdianCheckFingerNetPointColletcActivity.this, "正在登录...");
                    // System.out.println("账号"+name +"密码"+pwd);
                    getSystemLogin().login(name, pwd);
                } else {
                    // Toast.makeText(SystemLogin.this,"网络没有连通，无法登录",
                    // Toast.LENGTH_LONG).show();
                    managerClass.getGolbalView().toastShow(WangdianCheckFingerNetPointColletcActivity.this, "网络没有连通，无法登录");
                }
            }
        };

        // Hand通知操作
        getSystemLogin().handler_login = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                managerClass.getRuning().remove();
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        error = 3;
                        Log.e("", "orgid===" + GApplication.user.getOrganizationId());
                        //  第一个人只验证是否为网点人员就好第二个人需要验证是否和第一个人同意网点和角色5

                        if (!"5".equals(GApplication.user.getLoginUserId())) {// 角色不为5
                            System.out.println("网点登录匹配角色id：" + GApplication.user.getLoginUserId());
                            managerClass.getAbnormal().timeout(WangdianCheckFingerNetPointColletcActivity.this, "请使用网点人员帐号登录",
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
                            Intent     intentC   = new Intent(WangdianCheckFingerNetPointColletcActivity.this, KuanXiangNetPointCollectActivity.class);
                            if (netpoincollect != null) {
                                String flag = netpoincollect.getString("FLAG");
                                if (flag.equals("wangdianone")) {
                                    netpoincollect.putString("FLAG", flag);
                                    netpoincollect.putString("netpoincollect", "账号验证成功");
                                    User u = new User();
                                    u.setUserzhanghu(name);
                                    u.setPwd(pwd);
                                    u.setUsername(GApplication.user.getLoginUserName());
                                    // ShareUtil.zhiwenid_left = name;
                                    GApplication.wd_user1 = u;
                                    // 网点人员的机构
                                    GApplication.jigouid = GApplication.user.getOrganizationId();
                                    System.out.println("网点人员的机构:" + GApplication.jigouid);
                                    System.out.println("网点人员的机构2:" + GApplication.user.getOrganizationId());
                                    intentC.putExtras(netpoincollect);
                                    managerClass.getRuning().runding(WangdianCheckFingerNetPointColletcActivity.this, "用户名和密码验证成功");
//                                    managerClass.getGolbalutil().gotoActivity(WangdianCheckFingerNetPointColletcActivity.this,
//                                            KuanXiangNetPointCollectActivity.class, netpoincollect, GolbalUtil.ismover);

                                    WangdianCheckFingerNetPointColletcActivity.this.setResult(5, intentC);
                                    WangdianCheckFingerNetPointColletcActivity.this.finish();
                                }
                                if (flag.equals("wangdiantwo")) {
                                    netpoincollect.putString("FLAG", flag);
                                    String left = netpoincollect.getString("left");
                                    User u = new User();
                                    u.setUserzhanghu(name);
                                    u.setPwd(pwd);
                                    u.setUsername(GApplication.user.getLoginUserName());
                                    // ShareUtil.zhiwenid_right = name;
                                    GApplication.wd_user2 = u;
                                    System.out.println("GApplication.user.getLoginUserName()2  :"
                                            + GApplication.user.getLoginUserName());
                                    if (left.equals(GApplication.user.getLoginUserName())) {
                                        Toast.makeText(WangdianCheckFingerNetPointColletcActivity.this, "该用户已经验证过!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        jigouidCheck = GApplication.jigouid;
                                        String jigouidCheck3= GApplication.jigouidwangdian2;
                                        String jigouidCheck4=GApplication.jigouidwangdian1;
                                        jigouidCheck2 = GApplication.user.getOrganizationId();
//                                        比对同一个机构号
                                        if (jigouidCheck.equals(jigouidCheck2)||jigouidCheck2.equals(jigouidCheck4)) {
                                            intentC.putExtras(netpoincollect);
                                            managerClass.getRuning().runding(WangdianCheckFingerNetPointColletcActivity.this, "正在验证用户名和密码...");
//                                            managerClass.getGolbalutil().gotoActivity(WangdianCheckFingerNetPointColletcActivity.this,
//                                                    KuanXiangNetPointCollectActivity.class, netpoincollect, GolbalUtil.ismover);
//                                            finish();

                                            WangdianCheckFingerNetPointColletcActivity.this.setResult(5, intentC);
                                            WangdianCheckFingerNetPointColletcActivity.this.finish();
                                        } else {
                                            Toast.makeText(WangdianCheckFingerNetPointColletcActivity.this, "两人不是同一个网点!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }
                        }
                        break;
                    case 0:
                        if (msg.obj != null) {
                            managerClass.getGolbalView().toastShow(WangdianCheckFingerNetPointColletcActivity.this, msg.obj.toString());
                        } else {
                            managerClass.getGolbalView().toastShow(WangdianCheckFingerNetPointColletcActivity.this, "");
                        }
                        break;
                    case -4:
                        managerClass.getAbnormal().timeout(WangdianCheckFingerNetPointColletcActivity.this, "登陆超时，重新链接？", onclickreplace);
                        break;
                    case -1:
                        managerClass.getAbnormal().timeout(WangdianCheckFingerNetPointColletcActivity.this, "登录出现异常", onclickreplace);
                        break;
                    case -3:
                        managerClass.getGolbalView().toastShow(WangdianCheckFingerNetPointColletcActivity.this, "用户或密码为空！");
                        break;

                }

            }

        };

        share = this.getPreferences(0);
        editor = share.edit();

        // 把当前activity放进集合
        GApplication.addActivity(this, "1system");
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
                    name = editname.getText().toString();
                    pwd = editpwd.getText().toString();
                    login.setBackgroundResource(R.drawable.buttom_selector_bg);
                    // 非空验证
                    if (isnull(name, pwd)) {
                        // 有网络才可以执行登录操作
                        Log.i("network", network + "");
                        if (network) {
                            // 提示
                            try {
                                managerClass.getRuning().runding(WangdianCheckFingerNetPointColletcActivity.this, "正在登录...");
                            } catch (Exception e) {
                                e.printStackTrace();
                                // System.out.println(e.getMessage());
                            }
                            // System.out.println("开始调用登陆的方法========"+"checkFingerprint");
                            // 登陆方法
                            getSystemLogin().login(name, pwd);
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
        if (name != null) {
            editor.putString("uid", name);
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
        WangdianCheckFingerNetPointColletcActivity.this.finish();
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
//			Skip.skip(WangdianCheckFingerNetPointColletcActivity.this, JiaoJieActivity.class, null, 0);
            WangdianCheckFingerNetPointColletcActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
