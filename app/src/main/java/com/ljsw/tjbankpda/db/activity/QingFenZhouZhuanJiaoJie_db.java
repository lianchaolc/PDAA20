package com.ljsw.tjbankpda.db.activity;

import afu.util.BaseFingerActivity;

import java.net.SocketTimeoutException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entity.SystemUser;
import com.example.pda.R;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.service.YanZhengZhiWenService;
import com.ljsw.tjbankpda.db.service.ZhouZhuanXiangJiaoJie;
import com.ljsw.tjbankpda.util.Skip;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.FingerCheckBiz;
import com.poka.device.ShareUtil;

/**
 * 上缴清分出库交接(库管员>清分管理员)
 * 
 * @author yuyunheng
 * 上缴清分出库交接(库管员>抵质押品管理员)
 */
@SuppressLint("HandlerLeak")
public class QingFenZhouZhuanJiaoJie_db extends BaseFingerActivity implements OnClickListener {
	private ImageView back, img_left, img_right;
	private TextView setName_left, setName_right, top_tishi, bottom_tishi;
	private SystemUser result_user;// 指纹验证
	public static boolean firstSuccess = false; // 第一位是否已成功验证指纹
	String f1 = ""; // 第一个按手指的人
	String f2 = ""; // 第二个按手指的人
	private String left_name, right_name;
	private OnClickListener OnClick1;
	private int wrongleftNum, wrongrightNum;// 指纹验证失败次数统计
	private Intent intent;
	private String cashBoxNum;// 提交款箱
	private String userId;// 提交指纹验证人的账户
	private boolean isFlag = true;
	private String isFlagState = "0";// 1 跳转现金中控3 跳转抵质押品  0 是返回或者清除数据
	/**
	 * 演示用 功能实现请删除
	 */
	private LinearLayout yanshi;

	private FingerCheckBiz fingerCheck;
	private ManagerClass manager;
	private TextView textView4, textView3 ,qingfenshangjiao_bottom_tishi;
	private  String  NetResult="";//  返回结果进行提示

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shangjiaoqinfenjiaojiezhiwen_db);
		o_Application.FingerJiaojieNum.clear();
		intent = new Intent();
		manager = new ManagerClass();
		cashBoxNum = "";
		userId = "";
		OnClick1 = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();

				if (isFlag) {
					isFlag = false;
					yanzhengFinger();
				}
			}
		};
// 这里是3 就是抵质押品否则是 重控或者现金走老流程
//		if(null!=o_Application.qingfendanmingxi.getOrdertype()||!o_Application.qingfendanmingxi.getOrdertype().equals("")){
//			if(o_Application.qingfendanmingxi.getOrdertype().equals("3")){
////				yanzhengFingerbyDZypmanger();
//
//			}else{
////			走老流程
//				yanzhengFinger();
//
//			}
//		}else{
//			System.out.println("没有数据！！！");
//
//		}

	}



	@Override
	protected void onResume() {
		super.onResume();
		isFlag = true;

		load();
		if(null!=o_Application.qingfendanmingxi.getOrdertype()||!o_Application.qingfendanmingxi.getOrdertype().equals("")){
			if(o_Application.qingfendanmingxi.getOrdertype().equals("3")){
				isFlagState="3";
				qingfenshangjiao_bottom_tishi.setText("请抵质押品管库员按压指纹");
				textView3.setText("抵押管库员");;//顶部标识
				textView4.setText("抵押管库员");

			}else{
//			走老流程
				isFlagState="1";
			}
		}else{
			System.out.println("没有数据！！！");
			isFlagState="0";
		}


	}
	/****
	 *  验证指纹方法22.5.12
	 */
	private void yanzhengFingerbyDZypmanger() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				YanZhengZhiWenService yanzheng = new YanZhengZhiWenService();
				try {
					System.out.println("----"+o_Application.kuguan_db.getOrganizationId());
					System.out.println(29);
					result_user = yanzheng.checkFingerprint(o_Application.kuguan_db.getOrganizationId(), "29",
							ShareUtil.ivalBack);
					Thread.sleep(1000);
					if (result_user != null) {
						handler.sendEmptyMessage(100);
					} else {
						handler.sendEmptyMessage(99);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(97);
				} catch (Exception e1) {
					e1.printStackTrace();
					handler.sendEmptyMessage(98);
				}
			}

		}.start();
	}

	public void yanzhengFinger() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				YanZhengZhiWenService yanzheng = new YanZhengZhiWenService();
				try {
					result_user = yanzheng.checkFingerprint(o_Application.kuguan_db.getOrganizationId(), "17",
							ShareUtil.ivalBack);
					Thread.sleep(1000);
					if (result_user != null) {
						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				} catch (Exception e1) {
					e1.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isFlag = true;
			switch (msg.what) {
			case 0:
				manager.getAbnormal().timeout(QingFenZhouZhuanJiaoJie_db.this, "验证超时,重试?", OnClick1);
				break;
			case 1:
				manager.getAbnormal().timeout(QingFenZhouZhuanJiaoJie_db.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				// 第一位验证
				if (!firstSuccess && !"1".equals(f1)) {
					img_left.setImageBitmap(ShareUtil.finger_kuguandenglu_left);
					left_name = result_user.getLoginUserName();
					o_Application.FingerJiaojieNum.add(result_user.getLoginUserId());
					setName_left.setText(left_name);
					f1 = "1";
					// //System.out.println("左边指纹特征值："+ShareUtil.ivalBack);
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < ShareUtil.ivalBack.length; i++) {
						sb.append(ShareUtil.ivalBack[i]);
					}
					firstSuccess = true;
					bottom_tishi.setText("请第二位库管员按手指...");
					top_tishi.setText("第一位验证成功");
					// 第二位验证
				} else if (firstSuccess && !"2".equals(f2)) {
					right_name = result_user.getLoginUserName();
					if (right_name != null && right_name.equals(left_name)) {
						top_tishi.setText("重复验证!");
					} else {
						top_tishi.setText("");
						img_right.setImageBitmap(ShareUtil.finger_kuguandenglu_right);
						o_Application.FingerJiaojieNum.add(result_user.getLoginUserId());
						setName_right.setText(right_name);
						f2 = "2";
						bottom_tishi.setText("验证成功！");
						firstSuccess = false;
						manager.getRuning().runding(QingFenZhouZhuanJiaoJie_db.this, "数据提交中...");
						Jiaojie();
					}
				}
				break;
			case 3:
				// 验证3次失败跳登录页面 未实现
				if (!f1.equals("1")) {
					wrongleftNum++;
					top_tishi.setText("验证失败:" + wrongleftNum + "次");
				} else {
					wrongrightNum++;
					top_tishi.setText("验证失败:" + wrongrightNum + "次");
				}
				if (wrongleftNum >= ShareUtil.three) {
					// 左侧跳用户登录
					firstSuccess = false;
					intent.setClass(QingFenZhouZhuanJiaoJie_db.this, QingFenYuanDengLu.class);
					QingFenZhouZhuanJiaoJie_db.this.startActivityForResult(intent, 1);
					top_tishi.setText("");
					wrongleftNum = 0;
				} else if (wrongrightNum >= ShareUtil.three) {
					// 右侧跳用户登录
					firstSuccess = true;
					intent.setClass(QingFenZhouZhuanJiaoJie_db.this, QingFenYuanDengLu.class);
					QingFenZhouZhuanJiaoJie_db.this.startActivityForResult(intent, 1);
					wrongrightNum = 0;
					top_tishi.setText("");
				}
				break;
			case 4:
				manager.getRuning().remove();
				o_Application.numberlist.clear();
				o_Application.qingfendanmingxi.getZhouzhuanxiang().clear();
				o_Application.guolv.clear();

				cashBoxNum = "";
				userId = "";
				Skip.skip(QingFenZhouZhuanJiaoJie_db.this, RenWuLieBiao_db.class, null, 9);
				break;
			case 5:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(QingFenZhouZhuanJiaoJie_db.this, "交接超时,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						cashBoxNum = "";
						userId = "";
						Jiaojie();
					}
				});
				cashBoxNum = "";
				userId = "";
				break;
			case 6:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(QingFenZhouZhuanJiaoJie_db.this, "网络连接失败,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						Jiaojie();
					}
				});
				break;
			case 7:
				manager.getRuning().remove();
				if(NetResult.equals("")||NetResult==null){}else{


				manager.getAbnormal().timeout(QingFenZhouZhuanJiaoJie_db.this, NetResult.trim()+"提交失败,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						Jiaojie();
					}
				});
				}
				break;

				///  抵质押品管库员要走的处理方法
				case 99:

					if (!f1.equals("1")) {
						wrongleftNum++;
						top_tishi.setText("验证失败:" + wrongleftNum + "次");
					} else {
						wrongrightNum++;
						top_tishi.setText("验证失败:" + wrongrightNum + "次");
					}
					if (wrongleftNum >= ShareUtil.three) {
						// 左侧跳用户登录
						firstSuccess = false;
						intent.setClass(QingFenZhouZhuanJiaoJie_db.this, DiZhiYaPinGuanKuYuanDengLu.class);
						QingFenZhouZhuanJiaoJie_db.this.startActivityForResult(intent, 100);
						top_tishi.setText("");
						wrongleftNum = 0;
					} else if (wrongrightNum >= ShareUtil.three) {
						// 右侧跳用户登录
						firstSuccess = true;
						intent.setClass(QingFenZhouZhuanJiaoJie_db.this, DiZhiYaPinGuanKuYuanDengLu.class);
						QingFenZhouZhuanJiaoJie_db.this.startActivityForResult(intent, 100);
						wrongrightNum = 0;
						top_tishi.setText("");
					}
					break;
				case 100: //  成功

					if (!firstSuccess && !"1".equals(f1)) {
						img_left.setImageBitmap(ShareUtil.finger_dizhikuguandenglu_left);
						left_name = result_user.getLoginUserName();
						o_Application.FingerJiaojieNum.add(result_user.getLoginUserId());
						setName_left.setText(left_name);
						f1 = "1";
						// //System.out.println("左边指纹特征值："+ShareUtil.ivalBack);
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < ShareUtil.ivalBack.length; i++) {
							sb.append(ShareUtil.ivalBack[i]);
						}
						firstSuccess = true;
						bottom_tishi.setText("请第二位库管员按手指...");
						top_tishi.setText("第一位验证成功");
						// 第二位验证
					} else if (firstSuccess && !"2".equals(f2)) {
						right_name = result_user.getLoginUserName();
						if (right_name != null && right_name.equals(left_name)) {
							top_tishi.setText("重复验证!");
						} else {
							top_tishi.setText("");
							img_right.setImageBitmap(ShareUtil.finger_dizhikuguandenglu_right);
							o_Application.FingerJiaojieNum.add(result_user.getLoginUserId());
							setName_right.setText(right_name);
							f2 = "2";
							bottom_tishi.setText("验证成功！");
							firstSuccess = false;
							manager.getRuning().runding(QingFenZhouZhuanJiaoJie_db.this, "数据提交中...");
							Jiaojie();
						}
					}
					break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		System.out.println("arg0"+arg0);
		System.out.println("arg1"+arg1);
		if (arg2 != null&&arg1!=100) {
			Bundle bundle = arg2.getExtras();
			String isOk = bundle.getString("isOk");
			if (isOk.equals("success")) {
				if (!f1.equals("1")) {
					f1 = "1";
					left_name = o_Application.qingfenyuan.getLoginUserName();
					setName_left.setText(left_name);
					img_left.setImageResource(R.drawable.result_isok);
					firstSuccess = true;
					top_tishi.setText("第一位验证成功");
					bottom_tishi.setText("请第二位库管员按手指...");
					o_Application.left_user = o_Application.qingfenyuan;
				} else {
					right_name = o_Application.qingfenyuan.getLoginUserName();
					// 判断是否重复验证
					if (right_name != null && right_name.equals(left_name)) {
						top_tishi.setText("重复验证!");
					} else {
						f2 = "2";
						setName_right.setText(right_name);
						img_right.setImageResource(R.drawable.result_isok);
						firstSuccess = false;
						top_tishi.setText("");
						bottom_tishi.setText("验证成功！");
						o_Application.right_user = o_Application.qingfenyuan;
						manager.getRuning().runding(this, "数据提交中...");
						Jiaojie();
					}
				}

			}
		}
		if (arg2 != null&&arg1==100) {
			Bundle bundle = arg2.getExtras();
			String isOk = bundle.getString("isOk");
			if (isOk.equals("success")) {
				if (!f1.equals("1")) {
					f1 = "1";
					left_name = o_Application.diziyuan.getLoginUserName();
					setName_left.setText(left_name);
					img_left.setImageResource(R.drawable.result_isok);
					firstSuccess = true;
					top_tishi.setText("第一位验证成功");
					bottom_tishi.setText("请第二位库管员按手指...");
					o_Application.left_user = o_Application.diziyuan;
				} else {
					right_name = o_Application.diziyuan.getLoginUserName();
					// 判断是否重复验证
					if (right_name != null && right_name.equals(left_name)) {
						top_tishi.setText("重复验证!");
					} else {
						f2 = "2";
						setName_right.setText(right_name);
						img_right.setImageResource(R.drawable.result_isok);
						firstSuccess = false;
						top_tishi.setText("");
						bottom_tishi.setText("验证成功！");
						o_Application.right_user = o_Application.diziyuan;
						manager.getRuning().runding(this, "数据提交中...");
						Jiaojie();
					}
				}

			}
		}

	}

	public void load() {
		setName_left = (TextView) findViewById(R.id.qingfenjiaojie_setname_left);
		setName_right = (TextView) findViewById(R.id.qingfenjiaojie_setname_right);
		back = (ImageView) findViewById(R.id.qingfenjiaojie_back);
		back.setOnClickListener(this);
		yanshi = (LinearLayout) findViewById(R.id.qingfenjiaojie_yanshi);
		yanshi.setOnClickListener(this);
		top_tishi = (TextView) findViewById(R.id.qingfenshangjiao_top_tishi);
		bottom_tishi = (TextView) findViewById(R.id.qingfenshangjiao_bottom_tishi);
		img_left = (ImageView) findViewById(R.id.qingfenshangjiao_img_left);
		img_right = (ImageView) findViewById(R.id.qingfenshangjiao_img_right);
		textView3=(TextView)findViewById(R.id.textView3);//顶部标识
		textView4=(TextView)findViewById(R.id.textView4);
		qingfenshangjiao_bottom_tishi=(TextView)findViewById(R.id.qingfenshangjiao_bottom_tishi);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.qingfenjiaojie_back:
			Skip.skip(QingFenZhouZhuanJiaoJie_db.this, ShangJiaoQingFenLieBiao_db.class, null, 0);
			break;
		/*
		 * case R.id.qingfenjiaojie_yanshi: manager.getRuning().runding(this,
		 * "数据提交中..."); Jiaojie(); break;
		 */
		default:
			break;
		}
	}

	public void Jiaojie() {
		getcashBoxNumAnduserId();
		new Thread() {
			@Override
			public void run() {
				super.run();
				ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
				try {
					String userKuguanyuan = "";
					int c = 0;
					// 遍历取出库管员
					for (int i = 0; i < o_Application.FingerLoginNum.size(); i++) {
						if (c == o_Application.FingerLoginNum.size() - 1) {
							userKuguanyuan += o_Application.FingerLoginNum.get(i);
						} else {
							userKuguanyuan += o_Application.FingerLoginNum.get(i) + "_";
						}
						c++;
					}

					String isOk = jiaojie.saveAuthLog(cashBoxNum, userId, userKuguanyuan, "2B",
							o_Application.qingfendanmingxi.getJihuadan());
					isOk = isOk == null ? "" : isOk;
					if (isOk.trim().equals("00")) {
						handler.sendEmptyMessage(4);
					} else {
						NetResult=isOk;
						handler.sendEmptyMessage(7);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(5);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(7);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(6);
				}
			}

		}.start();

	}

	/**
	 * 拼接周转箱和交接人账户信息
	 */
	public void getcashBoxNumAnduserId() {
		cashBoxNum = "";
		userId = "";
		int a = 0;
		// int b = 0;
		int c = 0;
		for (int i = 0; i < o_Application.numberlist.size(); i++) {
			if (a == o_Application.numberlist.size() - 1) {
				cashBoxNum += o_Application.numberlist.get(i);
			} else {
				cashBoxNum += o_Application.numberlist.get(i) + "_";
			}
			a++;
		}

		for (int i = 0; i < o_Application.FingerJiaojieNum.size(); i++) {
			if (c == o_Application.FingerJiaojieNum.size() - 1) {
				userId += o_Application.FingerJiaojieNum.get(i);
			} else {
				userId += o_Application.FingerJiaojieNum.get(i) + "_";
			}
			c++;
		}
		System.out.println("上缴清分出库 cashBoxNum：" + cashBoxNum);
		System.out.println("上缴清分出库 userId：" + userId);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		f1 = "";
		f2 = "";
		cashBoxNum = "";
		userId = "";
		o_Application.left_user = null;
		o_Application.right_user = null;

		isFlag = false;
		isFlagState="0";
		manager.getRuning().remove();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			QingFenZhouZhuanJiaoJie_db.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
	    top_tishi.setText("正在验证指纹...");
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        super.getCharImgSucceed(charBytes, img);

        ShareUtil.ivalBack = charBytes;
        if (!firstSuccess && !"1".equals(f1)) {
            ShareUtil.finger_kuguandenglu_left = img;
        } else {
            ShareUtil.finger_kuguandenglu_right = img;
        }
//         设置图片指纹特征值
		if (!firstSuccess && !"1".equals(f1)) {
			ShareUtil.finger_dizhikuguandenglu_left = img;
		} else {
			ShareUtil.finger_dizhikuguandenglu_right = img;
		}
        if (isFlag) {
            isFlag = false;
//            yanzhengFinger();
			if(isFlagState.equals("1")){
				yanzhengFinger();
			}else if(isFlagState.equals("3")){
				yanzhengFingerbyDZypmanger();
			}else{
				isFlagState.equals("0");
			}
        }

    }
}
