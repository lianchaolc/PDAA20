package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import afu.util.BaseFingerActivity;

import java.net.SocketTimeoutException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.app.entity.User;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpda.db.application.o_Application;

import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

/***
 * 账户中心人员借查看 2018_11_15 账户中心人员的指纹验证提交 入账户中心的指纹验证
 *
 * @author Administrator 仿 押运员
 */

public class OutLibraryToAccountCenterFingerActivity extends BaseFingerActivity {
    protected static final String TAG = "OutLibraryToAccountCenterFingerActivity";
    private TextView top, fname, bottom;// 顶部提示 指纹对应人员姓名 底部提示
    private ImageView finger;// 指纹图片
    private ManagerClass managerClass;
    public User result_user;
    public Handler handler;
    private Intent intent;
    private int fingerCount;// 验证指纹失败的次数
    private boolean isFlag = true;
    private String updataresult;// 获取的网络请求结果
    private String userId = "";// 指纹返回给我的人员编号
    private String operator = "";// 当前登录人员（库管员操作）

    Dialog dialog;// dialog
    Dialog dialogfa;

    private static String covun ="";

    @SuppressLint({"HandlerLeak", "LongLogTag"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_library_toaccountcenter);
//        intent = getIntent();

        initView();
        managerClass = new ManagerClass();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                intent=getIntent();
                super.handleMessage(msg);
                isFlag = true;
                switch (msg.what) {
                    case 1:// 验证成功跳转
                    /*
                     * revised by zhangxuewei 2016-8-25 add frame confirm
					 */
                        finger.setImageBitmap(ShareUtil.finger_gather);
                        fname.setText(result_user.getUsername());
                        Log.d(TAG, "result_user" + result_user.getUsername() + "!!!" + result_user.getUserzhanghu());
//					managerClass.getSureCancel().makeSuerCancel2(
//							DiZhiYaPinSaoMiaoZhiWenActivity.this, "管库："+result_user.getUsername(),
//							new View.OnClickListener() {
//								@Override
//	
//								public void onClick(View arg0) {
                        // 此处是押运人员的管理界面所注释掉 我们是货位管理库人员
//									managerClass.getRuning().runding(DiZhiYaPinSaoMiaoZhiWenActivity.this,
//											"即将请稍后...");
                        // 跳转下一个页面
//									Skip.skip(DiZhiYaPinSaoMiaoZhiWenActivity.this,
//											YayunRwLbSActivity.class, null, 0);

//					账户资料出库借阅 交接 管库员 》账户中心人员

//					operator = GApplication.user.getYonghuZhanghao();// 将账户名进行传入

                        updateAccountOutFromWarehouserToCenter();
                        break;
                    case -1:
                        top.setText("验证异常，请重按");

                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
                            intent.setClass(OutLibraryToAccountCenterFingerActivity.this,
                                    AccountCenterManagerLoginActivity.class);
                            OutLibraryToAccountCenterFingerActivity.this.startActivityForResult(intent, 1);
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
                            intent.setClass(OutLibraryToAccountCenterFingerActivity.this,
                                    AccountCenterManagerLoginActivity.class);
                            OutLibraryToAccountCenterFingerActivity.this.startActivityForResult(intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;

                    case 999:
                        dialog = new Dialog(OutLibraryToAccountCenterFingerActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(OutLibraryToAccountCenterFingerActivity.this);
                        View v = inflater.inflate(R.layout.dialog_success, null);
                        Button but = (Button) v.findViewById(R.id.success);
                        but.setText("提交成功");
                        dialog.setCancelable(false);
                        dialog.setContentView(v);
                        but.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                dialog.dismiss();
                                managerClass.getRuning().runding(OutLibraryToAccountCenterFingerActivity.this,
                                        "即将请稍后...");
                                // 跳转下一个页面
//	            				Skip.skip(OutLibraryToAccountCenterFingerActivity.this,
//	            						DiZhiYaPinKuangJiaActivity.class, null, 0);
                                Intent mIntent = new Intent(OutLibraryToAccountCenterFingerActivity.this,
                                        DiZhiYaPinKuangJiaActivity.class);
                                ZhangHuZiLiaoChuKuJieYueActivity.instance.finish();// 此方法 关闭页面（父）
                                ZhangHuZiLiaoChuRuKuMingXiActivity.instance.finish();
                                ZhangHuZiLiaoChuKuJiaoJieActivity.instance.finish();
                                DiZhiYaPinKuangJiaActivity.instance.finish();

                            /*if (rfid != null) {
                                rfid.close_a20();// 关闭扫描指纹线程
                                getRfid().close_a20();
                            }*/
                                o_Application.yayunyuan = null;// 执行完成后清空押员信息
                                OutLibraryToAccountCenterFingerActivity.this.finish();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mIntent);

                            }

                        });
                        if (!isFinishing()) {
                            dialog.show();
                        }
                        break;

                    case 998:
                        dialogfa = new Dialog(OutLibraryToAccountCenterFingerActivity.this);
                        LayoutInflater inflaterfa = LayoutInflater.from(OutLibraryToAccountCenterFingerActivity.this);
                        View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
                        Button butfa = (Button) vfa.findViewById(R.id.success);
                        butfa.setText("提交失败");
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
                        if (!isFinishing()) {
                            dialogfa.show();
                        }
                        break;

                    case 997:
                        Toast.makeText(OutLibraryToAccountCenterFingerActivity.this, "缺少必要参数" + covun, 500).show();
                        break;
                }
            }

        };

    }
    final  Intent intent1=getIntent();
    @SuppressLint("LongLogTag")
    @Override
    protected void onStart() {
        super.onStart();

        if(covun==null||covun.equals("")){

            covun =  getIntent().getStringExtra("cvoun");

        }
        Log.e(TAG, getIntent().getStringExtra("cvoun") + "*******");
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        isFlag = true;
//        if (covun == null || covun.equals("")) {
//            Log.e(TAG, "onResume我是空操作");
//            covun = intent1.getStringExtra("cvoun");
//            Log.e(TAG, "onResume我是空操作"+covun);
//        } else {
//
//        }
        Log.e(TAG, "onResume  covun======"+covun);
    }

    private void initView() {
        fname = (TextView) findViewById(R.id.accountinfomationmangerd_fname);
        finger = (ImageView) this.findViewById(R.id.accountinfomationmanger_image);
        top = (TextView) this.findViewById(R.id.accountinfomationmanger_top);
        bottom = (TextView) this.findViewById(R.id.accountinfomationmangerbottom);
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

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            super.run();
            IPdaOfBoxOperateService yz = new IPdaOfBoxOperateService();
            try {
                // 押运员的机构id默认 等于最开始登录用户的机构id
                S_application.s_yayunJigouId = GApplication.user.getOrganizationId();
                System.out.println("GApplication.user.getLoginUserId():" + "账户中心管理员" + 27 + "==="
                        + GApplication.user.getOrganizationId());
//账户中心管理员的角色  是27
//				GApplication.user.getLoginUserId()=27+"";
                result_user = yz.checkFingerprint(GApplication.user.getOrganizationId(), 27 + "",
                        ShareUtil.ivalBack);

                System.out.println("============" + GApplication.user);
                System.out.println("============" + result_user);
                System.out.println("============" + GApplication.user.getLoginUserId());
                System.out.println("============" + GApplication.user.getOrganizationId());
                if (result_user != null) {// 验证成功
//					GApplication.use = result_user;
                    userId = GApplication.user.getYonghuZhanghao();/// 系统登录账户
                    operator = result_user.getUserzhanghu();// 当前页面将账户名进行传入
                    Log.d(TAG, "========userId" + userId);
                    S_application.s_zhanghuzhonginguanliyaun = result_user/// 这里更改可能出错
                            .getUserzhanghu();
                    Log.d(TAG, "========我走成功道路  " + userId + "当前页面：" + operator);
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
    protected void onDestroy() {
        super.onDestroy();
        isFlag = false;
        managerClass.getRuning().remove();
        if (dialogfa != null) {
            dialogfa.dismiss();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        covun="";
    }

    /****
     * 账户和密码所需要的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            String isOk = bundle.getString("isOk");
            if (isOk.equals("success")) {
                fname.setText(o_Application.Accountcenterusers.getLoginUserName());// 显示登录时传过来的人员名字 lc20190402
                finger.setImageResource(R.drawable.result_isok);
                bottom.setText("验证成功!");
                if (bundle.getString("name") != null && !bundle.getString("name").equals("")) {
                    S_application.s_zhanghuzhonginguanliyaun = bundle.getString("name");
//					if (userId == null || userId.equals("")) {// 账户交接,密码和没有userid 2019_4_1
//						userId=S_application.s_huoweiguanliyuanname;//需要更改为
                    userId = GApplication.user.getYonghuZhanghao();/// 登录账户
                    operator = S_application.s_zhanghuzhonginguanliyaun;// 将账户名进行传入操作账户
//					}
                }
                updateAccountOutFromWarehouserToCenter();// 重新网络请求
                // 跳转下一个页面
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
//							handler.sendEmptyMessage(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    }

    /***
     * 账户资料出库借阅 交接 管库员 》账户中心人员 covun 任务号 ,userId 账户中心的人
     * GApplication.use.getUserzhanghu(); 修改增加了一个登录人（库管员的id）的id
     */
    private void updateAccountOutFromWarehouserToCenter() {
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {

                    if (null != covun) {

                        Log.d(TAG, "*****" + covun + "****" + userId + "***" + operator);
                        updataresult = new AccountInfomationReturnService().updateAccountOutFromWarehouserToCenter(covun,
                                userId, operator);// 修改添加参数 2019_04_02
                        Log.d(TAG, "测试" + updataresult.toString());
                        if (updataresult.equals("00")) {
                            handler.sendEmptyMessage(999);
                        } else {
                            handler.sendEmptyMessage(998);
                        }

                    } else {
                        Log.d(TAG, "*****covuncovuncovuncovuncovuncovuncovuncovun" + covun );

                        handler.sendEmptyMessage(998);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    Log.e(TAG, "异常=====" + e);
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }


            }

        }.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }


}
