package com.main.pda;

import cn.poka.util.CrashHandler;
import cn.poka.util.SharedPreUtil;
import okhttp3.Call;

import com.application.GApplication;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.golbal.pda.GolbalView;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.loginsystem.biz.SystemLoginBiz;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.online.update.biz.GetPDA;
import com.online.update.biz.VersionInfo;
import com.service.FixationValue;
import com.service.NetService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

public class SystemLogin extends Activity implements OnTouchListener {
    // 系统登陆界面

    Button login; // 登陆按钮
    Button cancel; // 取消按钮
    AutoCompleteTextView editname; // 用户名输入框
    EditText editpwd; // 密码输入框

    TextView textlogin; // 网络状态提示
    String name = null; // 用户名
    String pwd = null; // 密码
    OnClickListener onclickreplace;
    SharedPreferences sharepre;
    String space; // 空间
    String webservice; // webservice地址
    int error = 3;
    // public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
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

    private GetPDA Pda;

    GetPDA getPda() {
        if (Pda == null) {
            Pda = new GetPDA();
        }
        return Pda;
    }

    Handler handler = null; // 获取版本号
    private Context context;
    private GolbalView g;

    GolbalView getG() {
        if (g == null) {
            g = new GolbalView();
        }
        return g;
    }

    private String serViceVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
        // FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.c_login_system);
        context = SystemLogin.this;
        // 全局异常处理
        CrashHandler.getInstance().init(this);
        managerClass = new ManagerClass();
        login = (Button) findViewById(R.id.system_login_btn);
        cancel = (Button) findViewById(R.id.system_login_cancel);
        editname = (AutoCompleteTextView) findViewById(R.id.name);

        // 初始化SharedPreference工具类
        SharedPreUtil.initSharedPreferences(getApplicationContext());
        // 获取缓存成功的用户名，并赋值给“用户名”输入框
        String[] userNameCache = SharedPreUtil.getInstance().getUserArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                userNameCache);
        editname.setAdapter(adapter);

        editpwd = (EditText) findViewById(R.id.pwd);
        textlogin = (TextView) findViewById(R.id.netmsgtext);

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
                managerClass.getRuning().remove();
                managerClass.getAbnormal().remove();
                if (network) {
                    managerClass.getRuning().runding(SystemLogin.this, "正在登录...");
                    getSystemLogin().login(name, pwd);
                } else {
                    // Toast.makeText(SystemLogin.this,"网络没有连通，无法登录",
                    // Toast.LENGTH_LONG).show();
                    managerClass.getGolbalView().toastShow(SystemLogin.this, "网络没有连通，无法登录");
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
                        GApplication.getApplication().app_hash.put("login_username", editname.getText());
                        GApplication.loginUsername = GApplication.user.getLoginUserName();
                        GApplication.organizationName = GApplication.user.getOrganizationName();
                        GApplication.loginname = name;
                        GApplication.loginpwd = pwd; // 修改密码用
                        o_Application.kuguan_db = GApplication.user;
                        GApplication.loginJidouId = GApplication.user.getOrganizationId();
                        GApplication.getApplication().loginjueseid = GApplication.user.getLoginUserId();
                        System.out.println("GApplication.loginJidouId :" + GApplication.user.getOrganizationId());
                        // 登录成功，缓存用户名到SharedPreference文件中
                        SharedPreUtil.getInstance().putUserName(name);
                        // 修改2022.4.18
                        Mapplication.getApplication().user1 = null;
                        Mapplication.getApplication().user2 = null;
                        managerClass.getGolbalutil().gotoActivity(SystemLogin.this, HomeMenu.class, null,
                                GolbalUtil.ismover);
                        break;
                    case 0:
                        if (msg.obj != null) {
                            managerClass.getGolbalView().toastShow(SystemLogin.this, msg.obj.toString());
                        } else {
                            managerClass.getGolbalView().toastShow(SystemLogin.this, "");
                        }
                        break;
                    case -4:
                        managerClass.getAbnormal().timeout(SystemLogin.this, "登录超时，重新链接？", onclickreplace);
                        break;
                    case -1:
                        managerClass.getAbnormal().timeout(SystemLogin.this, "登录出现异常", onclickreplace);
                        break;
                    case -3:
                        managerClass.getGolbalView().toastShow(SystemLogin.this, "用户或密码为空！");
                        break;

                }

            }

        };

        share = this.getPreferences(0);
        editor = share.edit();

        // 把当前activity放进集合
        GApplication.addActivity(this, "1system");

//		比对版本

        // 获取版本号并更新
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1:
                        managerClass.getSureCancel().makeSuerCancel5(SystemLogin.this, "获取版本号失败,是否重新获取？",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        getPda().getpath(handler, SystemLogin.this);
                                        managerClass.getSureCancel().remove5();
                                    }
                                }, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        managerClass.getSureCancel().remove5();
                                        SystemLogin.this.finish();
                                    }
                                }, false);
                        break;
                    case 99:
                        AlertDialog dialog = new AlertDialog.Builder(SystemLogin.this).setTitle
                                ("提示：").setMessage("apk有新版发布请更新")
                                .setNeutralButton("取消", new DialogInterface
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                SystemLogin.this.finish();
                                            }
                                        }
                                )
                                .setNegativeButton("更新", new DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadAPK();

                                    }
                                }).show();
                        dialog.setCanceledOnTouchOutside(false);//可选
                        dialog.setCancelable(false);//可选


                        break;
                    case 44:
//                        GolbalView.toastShow(SystemLogin.this, "目前已是最高版本");
                        break;
                }

            }

        };

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
                case R.id.system_login_btn:
                    login.setBackgroundResource(R.drawable.buttom_select_press);
                    break;
                // 取消
                case R.id.system_login_cancel:
                    cancel.setBackgroundResource(R.drawable.buttom_select_press);
                    break;
            }
        }

        // 手指松开的时候
        if (MotionEvent.ACTION_UP == even.getAction()) {
            switch (view.getId()) {
                // 登陆
                case R.id.system_login_btn:
                    name = editname.getText().toString();
                    pwd = editpwd.getText().toString();
                    login.setBackgroundResource(R.drawable.buttom_selector_bg);
                    long lastClick = 0;

                    lastClick = System.currentTimeMillis();
                    // 非空验证
                    if (isnull(name, pwd)) {
                        // 有网络才可以执行登录操作
                        Log.i("network", network + "");
                        if (network) {
                            // 提示
                            try {

                                managerClass.getRuning().runding(SystemLogin.this, "正在登录...");
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(e.getMessage());
                            }

                            // 登陆方法
                            getSystemLogin().login(name, pwd);
                            if (System.currentTimeMillis() - lastClick >= 5000) {
                                managerClass.getRuning().remove();
                                System.out.println("大于等于5m 移除遮罩");
                            }
                        } else {
                            managerClass.getGolbalView().toastShow(this, "网络没有连通，无法登录");
                        }
                    }

                    break;
                // 取消
                case R.id.system_login_cancel:
                    login.setBackgroundResource(R.drawable.buttom_select_press);
                    SystemLogin.this.finish();
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
                case R.id.system_login_btn:
                    login.setBackgroundResource(R.drawable.buttom_selector_bg);
                    break;
                // 取消
                case R.id.system_login_cancel:
                    cancel.setBackgroundResource(R.drawable.buttom_selector_bg);
                    break;
            }
        }

        return true;
    }

    /**
     * 版本号
     *
     * @return
     */
    public String getVersion() {
        String versioncode = "";
        PackageManager packageManager = SystemLogin.this.getPackageManager();
        PackageInfo info;
        try {
            info = packageManager.getPackageInfo(SystemLogin.this.getPackageName(), 0);
            // 褰撳墠鐗堟湰鍙?
            versioncode = info.versionName;
            serViceVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        getPda().getpath(handler, SystemLogin.this);
        return versioncode;
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
        getVersion();
        /*
		 * if(!"".equals(GApplication.user.getOrganizationId())){
		 * GApplication.user=null; }
		 */
        editpwd.setFocusable(false);
        editpwd.setFocusableInTouchMode(true);
        GApplication.loginJidouId = "";
        GApplication.jigouid = "";

//		S_application.getApplication().s_userYayun = null;//   清空押运员缓存2021.3.29
        // SharedPreferences preferences = this.getPreferences(0);
        // String nametext = preferences.getString("uid", "");
        // editname.setText(nametext);
        editname.setText("");
        editpwd.setText("");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("a");
        return true;
    }

	/*
	 * @Override public void onAttachedToWindow() {
	 * System.out.println("Page01 -->onAttachedToWindow");
	 * this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	 * super.onAttachedToWindow(); }
	 */

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * System.out.println("Page01 -->onKeyDown: keyCode: " + keyCode); if
	 * (KeyEvent.KEYCODE_HOME == keyCode) {
	 * System.out.println("HOME has been pressed yet ..."); //
	 * android.os.Process.killProcess(android.os.Process.myPid());
	 * Toast.makeText(getApplicationContext(), "HOME 键已被禁用...",
	 * Toast.LENGTH_LONG).show();
	 * 
	 * } return super.onKeyDown(keyCode, event); // 不会回到 home 页面 }
	 */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private View v;
    ProgressBar bar;
    TextView percentText;

    public void downloadAPK() {

        if (v == null) {
            v = GolbalView.getLF(context).inflate(R.layout.loading, null);
        }
        bar = (ProgressBar) v.findViewById(R.id.progress_version);
        percentText = (TextView) v.findViewById(R.id.percentText);
        try {

            Log.d("SystemLogin", "!!!!!!!" + VersionInfo.URL);
        } catch (Exception e) {

        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils
                        .get()
                        .url(VersionInfo.URL)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "PDA.apk") {//保存路径      APK名称
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                Log.d("SystemLogin", "progress" + progress);
                                Log.d("SystemLogin", "total" + total);
                                Log.d("SystemLogin", "id" + id);

                                String p = null;
                                double percent = progress;
                                if (total <= 0) {
                                    p = "0.00";
                                }
                                Log.i("percent", percent + "");
                                DecimalFormat fnum = new DecimalFormat("##0.00");

                                String dd = fnum.format(progress);
//                                long localintdata = Long.parseLong(str);//返回long基本数据类型
                                String ss = dd.substring(2, 4);
                                Integer newin1 = Integer.parseInt(ss);
                                Log.d("newin1", "newin1::" + newin1);
                                if (newin1 < 100&&newin1>0) {
                                    Log.d("newin1", "newin1::" + newin1);
                                    percentText.setText(newin1 + "%");
                                    bar.setProgress(newin1);
                                } else if (newin1.equals("99")||newin1==99) {
                                    Log.d("99newin1", "newin1::" + newin1);
                                    percentText.setText(100 + "%");
                                    bar.setProgress(100);
                                    getG().removeV(v);

                                }
                            }

                            @Override
                            public void onResponse(File response, int id) {
                                Log.d("response", "response::" + response.getAbsolutePath());
                                Log.d("response", "id::" + id);
                                try {
//                                    Thread.sleep(1500);
                                    showSelectAPK(context);
                                    bar.setVisibility(View.GONE);
                                    percentText.setVisibility(View.GONE);
                                    getG().removeV(v);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println(e);
                                }

                            }

                        });

            }

        }).start();
        getG().createFloatView(context, v);

    }

    private void showSelectAPK(Context context) {
        getG().removeV(v);
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/" + VersionInfo.APKNAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
//Android 7.0 系统共享文件需要通过 FileProvider 添加临时权限，否则系统会抛出 FileUriExposedException .
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, getPackageName()+".FileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            if (RESULT_CANCELED == 0) {
                Log.d("SystemLogin", "!!!!程序被添加" + RESULT_CANCELED);
                SystemLogin.this.finish();
            } else {
                Toast.makeText(SystemLogin.this, "程序安装..", Toast.LENGTH_SHORT).show();
            }


        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(
                    Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
        }


        context.startActivity(intent);
    }
//    private boolean isForeground() {
//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        String currentPackageName = cn.getPackageName();
//        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName())) {
//            return true;
//        }
//        return false;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(SystemLogin.this, "程序被替换..", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == RESULT_OK) {
            Toast.makeText(SystemLogin.this, "取消..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getG().removeV(v);
        managerClass.getSureCancel().remove5();
    }
}
