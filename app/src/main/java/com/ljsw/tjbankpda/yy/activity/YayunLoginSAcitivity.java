package com.ljsw.tjbankpda.yy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.app.entity.User;
import com.example.pda.R;
import com.example.pda.YayunSelectRewuUseActivity;
import com.example.pda.YayunSelectTaskActivity;
import com.golbal.pda.GolbalUtil;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

import java.net.SocketTimeoutException;

import afu.util.BaseFingerActivity;

/**
 * 押运员单人登录页面
 *
 * @author Administrator
 */
public class YayunLoginSAcitivity extends BaseFingerActivity {

    protected static final String TAG = "YayunLoginSAcitivity";
    private TextView top, fname, bottom;// 顶部提示 指纹对应人员姓名 底部提示
    private ImageView finger;// 指纹图片
    public User result_user;
    private int fingerCount;// 验证指纹失败的次数

    private String userid = "";// 用户的id全局

    private Intent intent;
    private ManagerClass managerClass;

    public Handler handler;
    private boolean isFlag = true;

    private boolean selectTast = false;// 标识当前是否有任务

    @SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yayun_login_s);

        initView();

        intent = new Intent();
        managerClass = new ManagerClass();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                managerClass.getRuning().remove();
                isFlag = true;
                switch (msg.what) {
                    case 1:// 验证成功跳转

                        finger.setImageBitmap(ShareUtil.finger_gather);
                        fname.setText(result_user.getUsername());
                        top.setText("指纹验证成功");

                        System.out.print("!!!" + result_user.getUserid() + ":::"
                                + result_user.getUsername() + "!!!" + result_user.getUserzhanghu());
                        managerClass.getSureCancel().makeSuerCancel2(
                                YayunLoginSAcitivity.this, "押运员：" + result_user.getUsername(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "即将请稍后...");
                                        Log.d(TAG, "===GApplication.use" + GApplication.use.getUserzhanghu());
                                        S_application.s_userYayun = result_user
                                                .getUserzhanghu();
                                        getescortselectTask();
                                    }


                                }, false);
                        break;
                    case -1:
                        // top.setText("验证异常，请重按");
                        S_application.s_userYayun = null;// 修改20200320
                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
                            intent.setClass(YayunLoginSAcitivity.this,
                                    YayunDenglu.class);
                            YayunLoginSAcitivity.this.startActivityForResult(
                                    intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;
                    case -4:

                        top.setText("验证超时，请重按");
                        break;
                    case 0:

                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
                            intent.setClass(YayunLoginSAcitivity.this,
                                    YayunDenglu.class);
                            YayunLoginSAcitivity.this.startActivityForResult(
                                    intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;
                    case 10:

                    case 11:
                        //					Skip.skip(YayunLoginSAcitivity.this, YayunRwLbSActivity.class,null, 0);  // 修改以前的接口
                        Skip.skip(YayunLoginSAcitivity.this,
                                YayunSelectRewuUseActivity.class, null, 0);
                        break;


                    case 9:
                        Skip.skip(YayunLoginSAcitivity.this,
                                YayunSelectTaskActivity.class, null, 0);
//								YayunRwLbSActivity.class, null, 0);
                        break;
                    case 12:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "网络请求失败",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "网络请求失败");
                                        managerClass.getRuning().remove();
                                    }
                                }, false);
                        break;
                    case 13:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "服务器没数据",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "网络请求失败");
                                        managerClass.getRuning().remove();
                                    }


                                }, false);
                        break;
                    case 14:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "网络请求失败",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "连接超时");
                                        managerClass.getRuning().remove();
                                    }
                                }, false);
                        break;


                    default:


                        break;
                }
            }
        };
    }

    public void initView() {
        fname = (TextView) findViewById(R.id.yy_fname);
        finger = (ImageView) this.findViewById(R.id.yy_image);
        top = (TextView) this.findViewById(R.id.yy_top);
        bottom = (TextView) this.findViewById(R.id.ll_yayun_bottom);
    }

    @Override
    public void onStop() {
        super.onStop();
        isFlag = false;
        managerClass.getRuning().remove();
    }

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        ShareUtil.ivalBack = charBytes;
        ShareUtil.finger_gather = img;

        if (isFlag) {
            top.setText("正在验证指纹...");
            isFlag = false;
            // 开始调用服务器验证指纹
            CheckFingerThread cf = new CheckFingerThread();
            cf.start();
        }
    }

    @Override
    public void findFinger() {
        top.setText("正在获取指纹特征值");
    }

    @Override
    public void badCharHandler() {
        top.setText("获取指纹特征值失败,请重试");
    }

    /**
     * 指纹验证线程
     *
     * @author Administrator
     */
    class CheckFingerThread extends Thread {
        Message m;

        public CheckFingerThread() {
            super();
            m = handler.obtainMessage();
        }

        @Override
        public void run() {
            super.run();
            IPdaOfBoxOperateService yz = new IPdaOfBoxOperateService();
            try {
                // 押运员的机构id默认 等于最开始登录用户的机构id
                S_application.s_yayunJigouId = GApplication.user
                        .getOrganizationId();
                System.out.println("GApplication.user.getLoginUserId():" + 9);
                Log.d(TAG, "=====+sjigou" + S_application.s_yayunJigouId + "");
                Log.d(TAG, "=====userid" + GApplication.user.getLoginUserId() + "");
                Log.d(TAG, "=====ivalBack" + ShareUtil.ivalBack + "");

                result_user = yz.checkFingerprint(
                        S_application.s_yayunJigouId,
                        GApplication.user.getLoginUserId(), ShareUtil.ivalBack);
                System.out.println("result_user============"
                        + result_user);
                System.out.println("yyl============"
                        + GApplication.user.getLoginUserId());
                System.out.println("yyl============"
                        + GApplication.user.getOrganizationId());
                if (result_user != null) {// 验证成功

                    Log.d(TAG, result_user + ":::::::::::::::------");
                    GApplication.use = result_user;

                    Log.d(TAG, GApplication.use.getUsername() + ":::::覆盖后");
                    Log.d(TAG, GApplication.use.getUserzhanghu() + ":::::覆盖后");
                    Log.d(TAG, S_application.s_userYayun + ":::::");
                    m.what = 1;
                } else {
                    m.what = 0;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                m.what = -4;// 超时验证
            } catch (Exception e) {
                e.printStackTrace();
                m.what = -1;// 验证异常
            } finally {
                handler.sendMessage(m);
                GolbalUtil.onclicks = true;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            String isOk = bundle.getString("isOk");
            if (isOk.equals("success")) {
                fname.setText(GApplication.use.getUsername());//  返回系统登录人员lianchao 20201.3.31jilu  无修改
                finger.setImageResource(R.drawable.result_isok);
                bottom.setText("验证成功!");
                if (bundle.getString("name") != null
                        && !bundle.getString("name").equals("")) {
                    S_application.s_userYayun = bundle.getString("name");
                }
                // 跳转下一个页面
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            getescortselectTask();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    /***
     * 押运员是否领取任务 用户的id 押运员当前没有任务需要走
     * 修改于20191126
     * lianc
     * 201912.3 建议只判断当前是否有任务
     */

    private boolean getescortselectTask() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    System.out.println(GApplication.user.getLoginUserId() + "---" +
                            GApplication.user.getLoginUserName() + "--" +
                            GApplication.user.getOrganizationId() + "--"
                    );

                    Log.e(TAG, "获取押运员数据S_application.getApplication().s_userYayun" + S_application.s_userYayun);
                    userid = S_application.s_userYayun;
                    Log.e(TAG, "userid----" + userid);
                    if (null == userid || userid.equals("")) {
                        Log.e(TAG, "userid是===" + userid);
                    } else {
                        Log.e(TAG, "userid不为null===" + userid);
                        String
                                netresultClean = new AccountAndResistCollateralService()
                                .getescortselectTask(userid);
                        Log.e(TAG, "测试数据源----" + netresultClean + "---" + netresultClean);
                        Log.e(TAG, "测试数据源是否有任务----" + netresultClean.toString());
                        selectTast = true;
                        if (netresultClean.equals("1")) {
                            Skip.skip(YayunLoginSAcitivity.this,
                                    YayunSelectTaskActivity.class, null, 0);//  无任务押运员身上是否有任务   有任务：0 无任务：1
                            Log.e(TAG, "任务----");
                        } else if (netresultClean.equals("0")) {
                            Skip.skip(YayunLoginSAcitivity.this,
                                    YayunSelectRewuUseActivity.class, null, 0);
                            Log.e(TAG, "没有任务----");
                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e("", "**===" + e);

                    managerClass.getSureCancel().makeSuerCancel(
                            YayunLoginSAcitivity.this, "网络请求失败",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                            "连接超时");
                                    managerClass.getRuning().remove();
                                }
                            }, false);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("TAG", "**===" + e);
                    selectTast = false;

                    managerClass.getSureCancel().makeSuerCancel(
                            YayunLoginSAcitivity.this, "服务器没数据",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                            "网络请求失败");
                                    managerClass.getRuning().remove();
                                }


                            }, false);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "***===" + e);
                    managerClass.getSureCancel().makeSuerCancel(
                            YayunLoginSAcitivity.this, "网络请求失败",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                            "网络请求失败");
                                    managerClass.getRuning().remove();
                                }
                            }, false);
                }
            }

        }.start();
        return selectTast;
    }
}
