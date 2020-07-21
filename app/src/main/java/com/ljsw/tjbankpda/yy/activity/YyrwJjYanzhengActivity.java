package com.ljsw.tjbankpda.yy.activity;

import java.net.SocketTimeoutException;

import android.os.SystemClock;
import hdjc.rfid.operator.RFID_Device;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.app.activity.YayunJiaojieActivity;
import com.example.app.entity.User;
import com.example.pda.R;
import com.example.pda.YayunSelectRewuUseActivity;
import com.golbal.pda.GolbalUtil;
import com.imple.getnumber.GetFingerValue;
import com.ljsw.tjbankpad.baggingin.activity.CustomDialog;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpad.baggingin.activity.SuccessDialog;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;
import poka_global_constant.GlobalConstant;

/**
 * 押运任务交接 押运员验证手指页面
 * 
 * @author 石锚
 */
public class YyrwJjYanzhengActivity extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "YyrwJjYanzhengActivity";
	private TextView top, // 头部提示文字
			fname, // 押运员姓名
			toubuTishi; // 头部提示
	private ImageView finger;// 指纹图片
	private ManagerClass managerClass;
	public User result_user;
	public Handler handler;
	private Intent intent;
	private int fingerCount;// 验证指纹失败的次数
	OnClickListener onclickreplace;
	private boolean isFlag = true;
	private String bundleName;

	private RFID_Device rfid;
	private Dialog successDialog;// 成功弹出框
	private DialogManager dmanager;// 弹出框管理类
	private CustomDialog outDialog;
	private String qlJiaojieTypesecond;
	private String sjJiaojieTypesecond;
	private Context mContext;

	RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	Handler jiaoJieHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mContext != null) {
				managerClass.getRuning().remove();
			}
			switch (msg.what) {
			case 1:
				// 提交成功,回到押运任务列表界面
				managerClass.getResultmsg().resultOnlyMsg(YyrwJjYanzhengActivity.this, "提交成功,页面跳转中...", true);
				S_application.getApplication().s_zzxShangjiao = null;
				S_application.getApplication().s_zzxQingling = null;
				S_application.getApplication().s_userWangdian = null;
//				Skip.skip(YyrwJjYanzhengActivity.this,YayunRwLbSActivity.class, null, 0);  /// 跳轉页面进行修改
				Skip.skip(YyrwJjYanzhengActivity.this, YayunSelectRewuUseActivity.class, null, 0);
				if (null != S_application.getApplication().s_userYayun) {
					GApplication.use.setUserzhanghu(S_application.getApplication().s_userYayun);
				}
				S_application.wrong = null;
				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(1000);
						managerClass.getResultmsg().remove();
					}
				}).start();
				break;
			case -1:
				managerClass.getAbnormal().timeout(YyrwJjYanzhengActivity.this, "数据连接超时", onclickreplace);

				break;
			case -2:
				managerClass.getAbnormal().timeout(YyrwJjYanzhengActivity.this, "数据错误", onclickreplace);

				break;
			case -3:
				managerClass.getAbnormal().timeout(YyrwJjYanzhengActivity.this, "网络连接失败", onclickreplace);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yy_jjyayun_s);
		managerClass = new ManagerClass();
		intent = new Intent();
		initView();
		dmanager = new DialogManager(YyrwJjYanzhengActivity.this);
		// 重试单击事件
		onclickreplace = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				managerClass.getAbnormal().remove();
				submit();
			}
		};
		mContext = YyrwJjYanzhengActivity.this;
		handler = new Handler() {
			@SuppressWarnings("static-access")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				isFlag = true; // 将点击置为 可点击
				switch (msg.what) {
				case 1:// 验证成功跳转
					/*
					 * revised by zhangxuewei at 2016/9/6 此类写了两个
					 */
					Log.d(TAG, "" + S_application.getApplication().s_userYayun);
					System.out.println("!!!!!!!" + S_application.getApplication().s_userYayun);
					System.out.println("!!!!!!!" + bundleName);
					System.out.println("!!!!!!! getLoginUserName()" + GApplication.user.getLoginUserName());
//				    GApplication.userInfo.getNameZhanghao();
//				    GApplication.user.getYonghuZhanghao();//测试
//				    GApplication.user.getLoginUserName();// 测试登录时显示账号系统
//				    GApplication.use.getUserzhanghu();// 测试

					System.out.println("!!!!!!! getLoginUserName()" + GApplication.user.getLoginUserName());
					System.out.print("!!!!!!!" + "数据不为空！！  GApplication.user.setLoginUserName！"
							+ GApplication.user.getLoginUserName());
					if (result_user != null) {
						System.out.print("!!!!!!!" + "数据不为空！！！！！！！！！！！！！！！！！！！！！！！！！！！");
						finger.setImageBitmap(ShareUtil.finger_gather);
						fname.setText(result_user.getUsername());

						if (GApplication.use != null) {
							fname.setText(GApplication.use.getUsername());
						}
						managerClass.getSureCancel().makeSuerCancel2(YyrwJjYanzhengActivity.this,
								"押运员：" + result_user.getUsername(), new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										managerClass.getSureCancel().remove();
										top.setText("验证成功");
										System.out.println("handler-->submit");
										isFlag = false;
										System.out.println("第一个");
										submit();
									}
								}, false);
					} else if (S_application.getApplication().s_userYayun.equals(bundleName)) {
						top.setText("验证成功");
						System.out.println("handler-->submit");
						isFlag = false;
						System.out.println("第二个");
						submit();
					} else if (!S_application.getApplication().s_userYayun.equals(result_user.getUserzhanghu())) {
						top.setText("押运员身份不符合");
						isFlag = true; // 重置为可点击
					}

					break;
				case -1:
					fingerCount++;
					top.setText("验证失败" + fingerCount + "次");
					if (fingerCount >= ShareUtil.three) {
						S_application.wrong = "jiaojie";
						// 跳用户登录
						intent.setClass(YyrwJjYanzhengActivity.this, YayunDenglu.class);
						YyrwJjYanzhengActivity.this.startActivityForResult(intent, 1);
						top.setText("");
						fingerCount = 0;
					}
					break;
				case -4:
					top.setText("验证超时，请重按");
					break;
				case 0:
					fingerCount++;
					top.setText("验证失败" + fingerCount + "次");
					if (fingerCount >= ShareUtil.three) {
						// 跳用户登录
						intent.setClass(YyrwJjYanzhengActivity.this, YayunDenglu.class);
						YyrwJjYanzhengActivity.this.startActivityForResult(intent, 1);
						top.setText("");
						fingerCount = 0;
					}
					break;
				}
			}
		};
	}

	@Override
	protected void onResume() {
		getRfid().setOpenClose(GlobalConstant.IO_AS602_POWER, GlobalConstant.ENABLE_IO);
		super.onResume();
		isFlag = true;
		// ShareUtil.finger_gather=null;
		managerClass.getRfid().addNotifly(new GetFingerValue()); // 添加通知
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				managerClass.getRfid().fingerOpen(); // 打开指纹
			}
		}).start();

		// 获得指纹通知
		GetFingerValue.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle bundle;
				if (msg.what == 1) {
					bundle = msg.getData();
					if (bundle != null && bundle.getString("finger").equals("正在获取指纹特征值！")) {

					} else if (bundle != null && bundle.getString("finger").equals("获取指纹特征值成功！")) {
						if (isFlag) {
							toubuTishi.setText("正在验证指纹...");
							isFlag = false;
							CheckFingerThread cf = new CheckFingerThread();
							cf.start();
						}
					}
				}
			}
		};
	}

	public void initView() {
		fname = (TextView) this.findViewById(R.id.yy_fname);
		top = (TextView) this.findViewById(R.id.ll_yayun_bottom);
		finger = (ImageView) this.findViewById(R.id.yy_image);
		toubuTishi = (TextView) this.findViewById(R.id.yy_top);
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
			// GApplication.user.getLoginUserId()
			IPdaOfBoxOperateService yz = new IPdaOfBoxOperateService();
			System.out.println("验证机构:" + S_application.getApplication().s_yayunJigouId);
			try {
				result_user = yz.checkFingerprint(S_application.getApplication().s_yayunJigouId, "9",
						ShareUtil.ivalBack);
				System.out.println("我是空吗??" + ShareUtil.finger_gather);
				if (result_user != null) {// 验证成功
					System.out.println("交接押运ID:" + result_user.getUserzhanghu());
					GApplication.use = result_user; // 新加入需要测试
					m.what = 1;
					Log.e("YYEjjyanzhengactivuty", "发送是到1");
					GApplication.user.setLoginUserName("" + result_user.getUsername());
				} else {
					m.what = 0;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				m.what = -4;// 超时验证
			} catch (NullPointerException e) {
				e.printStackTrace();
				m.what = 0;// 验证异常
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult---->");
		if (data != null) {
			Bundle bundle = data.getExtras();
			String isOk = bundle.getString("isOk");
			if (isOk.equals("success")) {
				fname.setText(GApplication.user.getLoginUserName());
				finger.setImageResource(R.drawable.result_isok);
				System.out.println("onActivityResult--name-->" + bundle.getString("name"));
				if (bundle.getString("name") != null && !"".equals(bundle.getString("name"))) {
					bundleName = bundle.getString("name");
					handler.sendEmptyMessage(1);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isFlag = false;
		getRfid().close_a20();
		managerClass.getRuning().remove();
	}

	@Override
	protected void onStop() {
		getRfid().setOpenClose(GlobalConstant.IO_AS602_POWER, GlobalConstant.DISABLE_IO);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		S_application.wrong = null;
		managerClass.getResultmsg().remove();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.ll_yy_back_bank:
		// YyrwJjYanzhengActivity.this.finish();
		// break;

		default:
			break;
		}
	}

	private void submit() {
		String qlJiaojieType = "", sjJiaojieType = "";

		// 根据交接状态 判断交接类型
		switch (S_application.getApplication().jiaojieType) {
		case 1:
			qlJiaojieType = "24";
			sjJiaojieType = "29";
			break;
		case 2:
			qlJiaojieType = "26";
			sjJiaojieType = "27";
			break;
		case 3:
			qlJiaojieType = "2H";
			sjJiaojieType = "2I";
			break;
		}
		Thread thread = new Thread(new yayunjiaojie(qlJiaojieType, sjJiaojieType));
		thread.start();
	}

	/**
	 * 押运员 交接接口调用
	 * 
	 * @author yuyunheng lainc 修改接口添加
	 *         刚开始交接的时候，最后一次参数默认传“n”，如果我返回结果code=01，那你就要弹出一个确认框，下图为异常处理中上缴入库的提示 新版功能
	 *         如果请领和上缴同时存在 那么交接两次 只有上缴 交接两次 如果 只有请领需要交接一次就可以 并且最后的参数为也传n
	 * 
	 */

	private String strflage = "n";

	private class yayunjiaojie implements Runnable {
		private String qlJiaojieType;
		private String sjJiaojieType;

		public yayunjiaojie(String qlJiaojieType, String sjJiaojieType) {
			this.qlJiaojieType = qlJiaojieType;
			this.sjJiaojieType = sjJiaojieType;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			managerClass.getRuning().runding(YyrwJjYanzhengActivity.this,
//					"正在处理...");
			try {
				System.out.println("调用押运员交接接口");
				System.out.println("押运员ID:" + GApplication.use.getUserzhanghu());
				// 接口修改加入一个参数
				String param = new ICleaningManService().SaveAuthLogYayun(S_application.getApplication().s_zzxShangjiao,
						S_application.getApplication().s_zzxQingling, S_application.getApplication().s_userWangdian,
						S_application.getApplication().s_userYayun, S_application.getApplication().s_yayunXianluId,
						qlJiaojieType, sjJiaojieType);
				if (param != null && param.equals("交接成功")) {
					jiaoJieHandler.sendEmptyMessage(1);
				} else {
					System.out.println("失败-->submit");
					jiaoJieHandler.sendEmptyMessage(-2);// 造成当前原因是参数不正确 网点人员信息录入不正确 或者派工单号未录入

				}
			} catch (SocketTimeoutException e) {
				jiaoJieHandler.sendEmptyMessage(-1);
			} catch (Exception e) {
				Log.e(TAG, "---------------------------报错" + e);
				e.printStackTrace();
				System.out.println("系统出错");
				jiaoJieHandler.sendEmptyMessage(-2);
			}
		}
	}

}
