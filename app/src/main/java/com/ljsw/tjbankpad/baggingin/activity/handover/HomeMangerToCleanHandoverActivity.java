package com.ljsw.tjbankpad.baggingin.activity.handover;

import afu.util.BaseFingerActivity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.app.entity.User;
import com.example.pda.HomeMangeerToCenterDataScanActivity;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.KuGuanYuanByZhangHuzhongxinLogin;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.AccountInfomationReturnService;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * lc 库管员到请分员现金装袋后会有个提交
 * 
 * @author Administrator
 *
 */
public class HomeMangerToCleanHandoverActivity extends BaseFingerActivity{
	protected static final String TAG = "HomeMangerCleanHandover";
	private TextView top, fname, bottom;// 顶部提示 指纹对应人员姓名 底部提示
	private ImageView finger;// 指纹图片
	private ManagerClass managerClass;

	public User result_user;
	public Handler handler;
	private Intent intent;
	private int fingerCount;// 验证指纹失败的次数
	private boolean isFlag = true;
	private String updataresult;// 获取的网络请求结果

//	接受传的list
	private List<String> returnaccountinteninfolist = new ArrayList<String>();
	private String Tasknumber = "";// 账户中心归还的任务列表
	private String updatastr;

	private long lastClickTime = 0;/// 限制点击的时间间隔
	private boolean flag = false;

	private Dialog dialog;// dialog 成功
	private Dialog dialogfa;// 失败
	private Dialog dialogforreturnaccountinten;
	private String number = "";// 账号

	private String userId = "";


	@SuppressLint("HandlerLeak")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_manger_to_clean_handover);
		initView();
		/// 装袋后集合后提交数据
		intent = getIntent();
//				returnaccountinteninfolist =(List<String>) getIntent().getSerializableExtra("list");
		updatastr = getIntent().getStringExtra("str");
//				str
		managerClass = new ManagerClass();


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
					fname.setText(result_user.getUsername());
//							managerClass.getSureCancel().makeSuerCancel2(
//									DiZhiYaPinSaoMiaoZhiWenActivity.this, "管库："+result_user.getUsername(),
//									new View.OnClickListener() {
//										@Override
					//
//										public void onClick(View arg0) {
					// 此处是押运人员的管理界面所注释掉 我们是货位管理库人员
//											managerClass.getRuning().runding(DiZhiYaPinSaoMiaoZhiWenActivity.this,
//													"即将请稍后...");
					// 跳转下一个页面
//											Skip.skip(DiZhiYaPinSaoMiaoZhiWenActivity.this,
//													YayunRwLbSActivity.class, null, 0);
					long now = System.currentTimeMillis();
					if (now - lastClickTime > 1000) {
						lastClickTime = now;
						updatapackger();// 调用提交接口抵制押品提交
					} else {

						return;

					}
//									}, false);
					break;
				case -1:
					top.setText("验证异常，请重按");

					fingerCount++;
					top.setText("验证失败" + fingerCount + "次，请重按");
					if (fingerCount >= ShareUtil.three) {
						// 跳用户登录
						intent.setClass(HomeMangerToCleanHandoverActivity.this, KuGuanYuanByZhangHuzhongxinLogin.class);
						HomeMangerToCleanHandoverActivity.this.startActivityForResult(intent, 1);
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
						intent.setClass(HomeMangerToCleanHandoverActivity.this, KuGuanYuanByZhangHuzhongxinLogin.class);
						HomeMangerToCleanHandoverActivity.this.startActivityForResult(intent, 1);
						top.setText("");
						fingerCount = 0;
					}
					break;

				case 10000:
					dialog = new Dialog(HomeMangerToCleanHandoverActivity.this);
					LayoutInflater inflater = LayoutInflater.from(HomeMangerToCleanHandoverActivity.this);
					View v = inflater.inflate(R.layout.dialog_success, null);
					Button but = (Button) v.findViewById(R.id.success);
					but.setText("提交成功");
					dialog.setCancelable(false);
					dialog.setContentView(v);
					but.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
							managerClass.getRuning().runding(HomeMangerToCleanHandoverActivity.this, "即将请稍后...");
							// 跳转下一个页面
							Intent mIntent = new Intent(HomeMangerToCleanHandoverActivity.this,
									HomeMangeerToCenterDataScanActivity.class);
//			            				ZhangHuZiLiaoGuiHuanActivity.instance.finish();
//			            				ZhangHuZiLiaoGuiHuanJiaojieActivity.instance.finish();
//			            				HomeMangerToCleanHandoverActivity.this.finish();
							HomeMangerToCleanHandoverActivity.this.finish();
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mIntent);
						}
					});

					dialog.show();
					break;

				case 999:// 提交成功后的dialog 显示和跳转其它 抵制押品
					dialogforreturnaccountinten = new Dialog(HomeMangerToCleanHandoverActivity.this);
					LayoutInflater inflaterforreturnaccountinten = LayoutInflater
							.from(HomeMangerToCleanHandoverActivity.this);
					View vforreturnaccountinten = inflaterforreturnaccountinten.inflate(R.layout.dialog_success, null);
					Button butforreturnaccountinten = (Button) vforreturnaccountinten.findViewById(R.id.success);
					butforreturnaccountinten.setText("提交成功");
					dialogforreturnaccountinten.setCancelable(false);
					dialogforreturnaccountinten.setContentView(vforreturnaccountinten);
					if (butforreturnaccountinten != null) {
						butforreturnaccountinten.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								dialogforreturnaccountinten.dismiss();
								managerClass.getRuning().runding(HomeMangerToCleanHandoverActivity.this, "即将请稍后...");
								// 跳转下一个页面
								Intent intent = new Intent(HomeMangerToCleanHandoverActivity.this,
										DiZhiYaPinKuangJiaActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

								/*if (rfid != null) {
									getRfid().close_a20();
								}*/
								HomeMangerToCleanHandoverActivity.this.finish();
							}

						});
					}
					dialogforreturnaccountinten.show();
					break;
				case 998:// 提交失败显示dialog
					dialogfa = new Dialog(HomeMangerToCleanHandoverActivity.this);
					LayoutInflater inflaterfa = LayoutInflater.from(HomeMangerToCleanHandoverActivity.this);
					View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
					Button butfa = (Button) vfa.findViewById(R.id.success);
					butfa.setText("提交失败");
					dialogfa.setCancelable(false);
					dialogfa.setContentView(vfa);
					butfa.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							dialogfa.dismiss();
						}

					});
					dialogfa.show();
//			            	  更改显示的弹窗  需要测试
//			            	managerClass.getResultmsg().resultmsg(null, "提交数据失败",false);
					break;
				case 997:
					Toast.makeText(HomeMangerToCleanHandoverActivity.this, "参数不完整请检查参数", Toast.LENGTH_SHORT).show();
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
		fname = (TextView) findViewById(R.id.dzypyy_fname);
		finger = (ImageView) this.findViewById(R.id.dzypyy_image);
		top = (TextView) this.findViewById(R.id.dzypyy_top);
		bottom = (TextView) this.findViewById(R.id.dzypll_yayun_bottom);
	}

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        top.setText("正在获取特征值");
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
//						S_application.getApplication().s_userguankuyaun = GApplication.user.getOrganizationId();     
				// 改动
				S_application.s_yayunJigouId = GApplication.user.getOrganizationId();

				System.out
						.println("getLoginUserId():" + "货位" + 26 + "=" + S_application.s_yayunJigouId);
				System.out.println("getLoginUserId():" + GApplication.user.getLoginUserId());
				result_user = yz.checkFingerprint(S_application.s_yayunJigouId,
						GApplication.user.getLoginUserId(), ShareUtil.ivalBack);

				System.out.println("============" + GApplication.user);
				System.out.println("result_user============" + result_user);
				System.out.println("yyl============" + GApplication.user.getLoginUserId());
				System.out.println("yyl============" + GApplication.user.getOrganizationId());
				if (result_user != null) {// 验证成功
					GApplication.use = result_user;
					S_application.s_userguankuyaun = result_user.getUserzhanghu();/// 这里更改可能出错

					if (flag) {
						userId = result_user.getUserzhanghu();
					} else {
						number = result_user.getUserzhanghu();

					}
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		isFlag = false;
		managerClass.getRuning().remove();

	}


	protected void onDestroy() {
		super.onDestroy();
		isFlag = false;
		managerClass.getRuning().remove();
		if (dialog != null) {
			dialog.dismiss();
		}
		if (dialogfa != null) {
			dialogfa.dismiss();
		}
	}

	/***
	 * 用户名和密码回来
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Bundle bundle = data.getExtras();
			String isOk = bundle.getString("isOk");
			if (isOk.equals("success")) {
				fname.setText(o_Application.yayunyuan.getLoginUserName());
				finger.setImageResource(R.drawable.result_isok);
				bottom.setText("验证成功!");
				if (bundle.getString("name") != null && !bundle.getString("name").equals("")) {
					S_application.s_userYayun = bundle.getString("name");
				}
//						当账号密码登陆成功后的网络请求判断190402
//						userId=o_Application.yayunyuan.getYonghuZhanghao();/// 登录账户
//						operator=GApplication.user.getYonghuZhanghao()	;//	 将账户名进行传入	

//						if(flag){
//							userId=bundle
//									.getString("name");;
//							undateAccountReturnFromCenterToWareHouse(); //账户中心归还任务列表提交数据
//						}else{
//							number=bundle
//									.getString("name");;
//							getRuKu();//调用提交接口抵制押品
//						}
				updatapackger();/// 验证成功后直接提交数据
				// 跳转下一个页面
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000);

//									handler.sendEmptyMessage(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		}
	}
	/**
	 * 抵制押品入库提交数据网络请求 库管员
	 */
//			public void getRuKu() {
////				manager.getRuning()
////						.runding(DiZhiYaPinSaoMiaoZhiWenActivity.this, "数据加载中...");
//				new Thread() {
//					@Override
//					public void run() {
//						super.run();
//						try {
//						 number=GApplication.loginname;
//							Log.e(TAG,"number"+o_Application.qlruku.getDanhao()+"=="+number);
//							updataresult =  new GetResistCollateralBaggingService().GetUpdata(o_Application.qlruku.getDanhao(),number);
//							Log.e(TAG,"测试"+updataresult.toString());
//							if (updataresult.equals("00")) {// 返回结果为00 表示成功 显示提交成功  否者返回失败或者是没有任务
//								handler.sendEmptyMessage(999);
//							} else {
//								handler.sendEmptyMessage(998);
//							}
//						} catch (SocketTimeoutException e) {
//							e.printStackTrace();
//							handler.sendEmptyMessage(0);
//						} catch (NullPointerException e) {
//							e.printStackTrace();
//						
//						} catch (Exception e) {
//							e.printStackTrace();
//							handler.sendEmptyMessage(1);
//						}
//					}
//
//				}.start();
//			}

	/**
	 * 账户资料归还： 交接（账户中心人员 -> 管库员)
	 *
	 * @param cvoun             订单号
	 * @param stockCodeListJson 扫描到的袋号数据
	 * @param userId            管库员id
	 * @return
	 */

	String cvoun = "";
//			 String userId="";
	String stockCodeListJson = "";

	/***
	 * 返回键关闭扫描
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			HomeMangerToCleanHandoverActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 库管员给清分人员的包进行的数据提交 参数一 交接人员id 参数2 扫描到包号的json集合 20191024
	 */
	private void updatapackger() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.e(TAG, "Tasknumber======" + Tasknumber);
					Gson gson = new Gson();

					stockCodeListJson = gson.toJson(returnaccountinteninfolist);
					userId = GApplication.use.getUserzhanghu();

					Log.e(TAG, "======" + "====" + returnaccountinteninfolist.size() + "===" + userId);
//							 先判断是否为null 直接发送失败 失败的dialog提示
					if (stockCodeListJson.equals("") || userId.equals("")) {
						handler.sendEmptyMessage(998);
					} else {
						updataresult = new AccountInfomationReturnService().updataHomeManger_handle(updatastr, userId);
					}
					Log.e(TAG, "测试" + updataresult.toString());
					if (updataresult.equals("00")) {
						handler.sendEmptyMessage(10000);
					} else {
						handler.sendEmptyMessage(998);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

}
