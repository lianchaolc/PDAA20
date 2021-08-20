package com.example.pda.homemagnetopackge;

import afu.util.BaseFingerActivity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

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
import com.example.pda.homemagnetopackge.accountinfo.HomemangertoAccountMangerLoginActivity;
import com.example.pda.homemagnetopackge.server.AccountAndPostmanServer;
import com.golbal.pda.GolbalUtil;
import com.imple.getnumber.GetFingerValue;
import com.ljsw.tjbankpda.db.application.o_Application;

import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

/***
 * 账户资料的指纹验证
 *
 * @author Administrator 仿 押运员 上缴 lc 202004120
 */
public class AccountCenterByHomemangerFingerActivity extends BaseFingerActivity {
    protected static final String TAG = "AccountCenterByHomemangerFingerActivity";
    private TextView top, fname, bottom, logintitle, dzypyy_usertextView1;// 顶部提示 指纹对应人员姓名 底部提示,标题头, 人员名称
    private ImageView finger;// 指纹图片
    private ManagerClass managerClass;
    public User result_user;
    public Handler handler;
    private Intent intent;
    private int fingerCount;// 验证指纹失败的次数
    private boolean isFlag = true;
    private String updataresult;// 获取的网络请求结果
    Dialog dialog;// 成功
    Dialog dialogfa; // 失败的dialog

    private String Tasknumber;// 传递的周转箱子号码
    private List<String> returnaccountinteninfolist = new ArrayList<String>();/// 传入的周转箱 list集合
    private String FingerUser = "";// 当前传递的验证指纹或者登陆成功的人账号

    @SuppressLint("HandlerLeak")
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accouninfocheckfinger);
//		intent=getIntent();
//		accountInfolist =(List<String>) getIntent().getSerializableExtra("list");
        initView();
        managerClass = new ManagerClass();
        intent = new Intent();
        Tasknumber = getIntent().getStringExtra("intLinmnum");
        returnaccountinteninfolist = (List<String>) getIntent().getSerializableExtra("postmanlist");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isFlag = true;
                switch (msg.what) {
                    case 1:// 验证成功跳转
                        /*
                         * revised by zhangxuewei 2016-8-25 add frame confirm
                         */
                        finger.setImageBitmap(ShareUtil.finger_gather);
                        try{

                            if(null==result_user.getUsername()||result_user.getUsername().equals("")){}else{
                                fname.setText(result_user.getUsername());
                                dzypyy_usertextView1.setText(result_user.getUsername());
                            }

                        }catch (Exception e){
                            System.out.print("异常："+e);
                        }


                        getRuKuByAccount();// 提交数据到账户中心

                        break;
                    case -1:
                        top.setText("验证异常，请重按");

                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
//						if(Flags){  //账户中心管理员要进入的
                            intent.setClass(AccountCenterByHomemangerFingerActivity.this,
                                    HomemangertoAccountMangerLoginActivity.class);
                            AccountCenterByHomemangerFingerActivity.this.startActivityForResult(intent, 1);
                            top.setText("");
                            fingerCount = 0;
//						}else{intent.setClass(AccountCenterByHomemangerFingerActivity.this,
//								AccountCenterByHomemangerFingerActivity.class);
//						AccountCenterByHomemangerFingerActivity.this.startActivityForResult(
//								intent, 1);
//						top.setText("");
//						fingerCount = 0;
//						}
                        }
                        break;
                    case -4:

                        top.setText("验证超时，请重按");
                        break;
                    case 0:

                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
//						// 跳用户登录
//						if(Flags){  //账户中心管理员要进入的
//							intent.setClass(AccountCenterByHomemangerFingerActivity.this,
//									KuGuanYuanByZhangHuzhongxinLogin.class);
//							AccountCenterByHomemangerFingerActivity.this.startActivityForResult(
//									intent, 1);
//							top.setText("");
//							fingerCount = 0;
//						}else{
//						
//						intent.setClass(AccountCenterByHomemangerFingerActivity.this,
//								AccountCenterByHomemangerFingerActivity.class);
//						AccountCenterByHomemangerFingerActivity.this.startActivityForResult(
//								intent, 1);
//						top.setText("");
//						fingerCount = 0;
//						}
                            intent.setClass(AccountCenterByHomemangerFingerActivity.this,
                                    HomemangertoAccountMangerLoginActivity.class);
                            AccountCenterByHomemangerFingerActivity.this.startActivityForResult(intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;
                    case 999:
                        dialog = new Dialog(AccountCenterByHomemangerFingerActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(AccountCenterByHomemangerFingerActivity.this);
                        View v = inflater.inflate(R.layout.dialog_success, null);
                        Button but = (Button) v.findViewById(R.id.success);
                        but.setText("提交成功");
                        dialog.setCancelable(false);
                        dialog.setContentView(v);
                        if (!isSingle()) {
                            but.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();
                                    managerClass.getRuning().runding(AccountCenterByHomemangerFingerActivity.this,
                                            "即将请稍后...");
                                    // 跳转下一个页面
                                    Intent intent = new Intent(AccountCenterByHomemangerFingerActivity.this,
                                            AccountinformationSelectMainActivity.class);
								/*if (rfid != null) {
									getRfid().close_a20();
									rfid.close_a20();// 关闭扫描指纹线程
								}*/

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                            });
                            if(!isFinishing()) {
                                dialog.show();
                            }
                        } else {
                            Toast.makeText(AccountCenterByHomemangerFingerActivity.this, "您提交的太快了稍后重试", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 998:
                        dialogfa = new Dialog(AccountCenterByHomemangerFingerActivity.this);
                        LayoutInflater inflaterfa = LayoutInflater.from(AccountCenterByHomemangerFingerActivity.this);
                        View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
                        Button butfa = (Button) vfa.findViewById(R.id.success);
                        butfa.setText("提交失败" + updataresult);
                        dialogfa.setCancelable(false);
                        dialogfa.setContentView(vfa);
                        if (!isSingle()) {
                            butfa.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    dialogfa.dismiss();
                                }

                            });
                        }
                        if(!isFinishing()) {
                            dialogfa.show();
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFlag = true;
    }

    private void initView() {
        logintitle = (TextView) findViewById(R.id.accountcenter_login_title);
        fname = (TextView) findViewById(R.id.accouninfocheckfinger_fname);
        finger = (ImageView) this.findViewById(R.id.accouninfocheckfinger_image);
        top = (TextView) this.findViewById(R.id.postmanaccounty_top);
        bottom = (TextView) this.findViewById(R.id.accouninfocheckfinger_bottom);
        dzypyy_usertextView1 = (TextView) findViewById(R.id.accouninfocheckfinger_usertextView1);
    }

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        top.setText("正在获取指纹特征值");
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

        @Override
        public void run() {
            super.run();
            IPdaOfBoxOperateService yz = new IPdaOfBoxOperateService();
            try {
                // 押运员的机构id默认 等于最开始登录用户的机构id
                S_application.s_yayunJigouId = GApplication.user.getOrganizationId();
                System.out.println("账户中心管理员" + 27 + "===" + S_application.s_yayunJigouId);
                System.out.println("GApplication:" + "" + GApplication.user.getLoginUserId());
                result_user = yz.checkFingerprint(S_application.s_yayunJigouId, 27 + "",
                        ShareUtil.ivalBack);
                System.out.println("============" + GApplication.user);
                System.out.println("result_user============" + result_user);
                System.out.println("yyl============" + GApplication.user.getLoginUserId());
                System.out.println("yyl============" + GApplication.user.getOrganizationId());
                if (result_user != null) {// 验证成功
                    GApplication.use = result_user;
                    S_application.s_zhanghuzhonginguanliyaun = result_user/// 这里更改可能出错
                            .getUserzhanghu();

//					连个方法传递参数
//					if(Flags){
                    FingerUser = result_user.getUserzhanghu();
                    ;// 指纹成功验证后
//					}else{
//						
//								RequestUserid=S_application.getApplication().s_userguankuyaun;// 目的同意接受
//					}

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

    /**
     * 彻底退出后销毁并关闭串口的扫描
     */
    public void onDestroy() {
        super.onDestroy();
        isFlag = false;
        managerClass.getRuning().remove();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            String isOk = bundle.getString("isOk");
            if (isOk.equals("success")) {
                fname.setText(S_application.s_userYayunName);
                finger.setImageResource(R.drawable.result_isok);
                bottom.setText("验证成功!");
                if (bundle.getString("name") != null && !bundle.getString("name").equals("")) {
                    S_application.s_zhanghuzhongxinguanliname = bundle.getString("name");
                }
//				getRuKu() ;// 重新网络请求
                // 跳转下一个页面

//				if(Flags){
//					Log.e(TAG,"===*******");
////					getAccountInfoReturn();// 入账户中心 账户中心人员 账户资料交接入库的第二个页面：查询待做的线路列表以及具体周转箱号以及数量
//					  userId= S_application.getApplication().s_userYayun;
//				}else{
//					RequestUserid=S_application.getApplication().s_userYayun;
//					getRuKuByAccount();//调用提交接	账户资料交接入库的第二个页面：查询待做的线路列表以及具体周转箱号以及数量
//				}	

                //// 又上一个页面返回来的时候进行方法
                FingerUser = S_application.s_zhanghuzhongxinguanliname;
                dzypyy_usertextView1.setText(bundle.getString("name"));
                if (FingerUser.equals("")) {

                } else {
                    getRuKuByAccount();
                }
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
     * 账户资料交接入库的第二个页面：查询待做的线路列表以及具体周转箱号以及数量
     */

    public void getRuKuByAccount() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    // 押运员id 线路号 对方机构号

                    String BoxNum = "";
                    String updata1 = "";
                    number = GApplication.user.getYonghuZhanghao();
                    for (int i = 0; i < returnaccountinteninfolist.size(); i++) {
                        BoxNum = BoxNum + returnaccountinteninfolist.get(i) + ",";
                    }
                    updata1 = BoxNum.substring(0, BoxNum.length() - 1);
                    String linnum = Tasknumber;
                    Log.d(TAG, "-----" + FingerUser);
                    Log.d(TAG, "---" + number);
                    Log.d(TAG, "---" + updata1);
                    Log.d(TAG, "----" + linnum);
                    if (FingerUser.equals("") && number.equals("") && updata1.equals("") && linnum.equals("")) {
                        Log.e(AccountCenterByHomemangerFingerActivity.this + "", "4个参数不合法 为null");
                        handler.sendEmptyMessage(998);
                    } else {
                        Log.e(TAG,
                                "测试=====" + o_Application.qlruku.getRiqi() + "===="
                                        + GApplication.user.getYonghuZhanghao() + "==="
                                        + GApplication.user.getOrganizationId());//// 向后台发送请求提交数据
                        updataresult = new AccountAndPostmanServer().AccountCenterByHomemanger(FingerUser, number,
                                updata1, linnum);

                        Log.e(TAG, "测试" + updataresult);
                        if (updataresult.equals("00")) {
                            handler.sendEmptyMessage(999);
                        } else {
                            handler.sendEmptyMessage(998);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    Log.e(TAG, "异常=====" + e);
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();
    }

    /***
     * 阻止用户多次提交 2018_12_7 对两次提交数据进行监听
     */
    private static final int DEFAULT_TIME = 1000;
    private static long lastTime;

    public static boolean isSingle() {
        boolean isSingle;
        long currentTime = System.currentTimeMillis();
        isSingle = currentTime - lastTime <= DEFAULT_TIME;
        lastTime = currentTime;

        return isSingle;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AccountCenterByHomemangerFingerActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
