package com.ljsw.tjbankpda.db.activity;

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
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.FingerCheckBiz;
import com.poka.device.ShareUtil;

import afu.util.BaseFingerActivity;

/**
 * 配送交接
 *
 * @author yuyunheng
 *
 */
@SuppressLint("HandlerLeak")
public class PeiSongJiaoJie_db extends BaseFingerActivity implements OnClickListener {
	private ImageView back, img;
	private TextView setname, bottom_tishi, top;
	private SystemUser result_user;// 指纹验证
	String f1 = ""; // 按手指的人
	private String yayunname;
	private int wrongNum;
	private Intent intent;
	private String cashBoxNum;
	private String userId;
	private String yayunId = "";
	private String jiaojieOk;
	private OnClickListener OnClick1, onclickreplace, OnClick2;
	ICleaningManService is = new ICleaningManService();
	private boolean isFlag = true;
	private String jigouleibie;
	/**
	 * 功能实现时删除
	 */
	private LinearLayout yanshi;

	private FingerCheckBiz fingerCheck;
	private ManagerClass manager;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_peisongjiaojie);
		load();
		o_Application.FingerJiaojieNum.clear();
		cashBoxNum = "";
		userId = "";
		manager = new ManagerClass();
		intent = new Intent();
		OnClick1 = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
			}
		};
		onclickreplace = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				Xianlu();
			}
		};
		OnClick2 = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getJigouLeibie();
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		isFlag = true;
		getJigouLeibie();
	}

	String params;// 返回机构的类别

	/**
	 * 通过机构ID获取线路类型
	 */
	public void Xianlu() {
		manager.getRuning().runding(PeiSongJiaoJie_db.this, "提交中...");
		new Thread() {
			public void run() {
				super.run();
				try { // o_Application.kuguan_db.getOrganizationId()
					params = is.getJigouLeibie(o_Application.kuguan_db.getOrganizationId());
					if (params != null || !params.equals("")) {
						JiaoJie();
					} else {
						handler.sendEmptyMessage(10);
					}
				} catch (SocketTimeoutException e) {
					handler.sendEmptyMessage(9);
				} catch (NullPointerException e) {
					handler.sendEmptyMessage(10);
				} catch (Exception e) {
					handler.sendEmptyMessage(11);
				}

			};
		}.start();
	}

	public void yanzhengFinger() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					result_user = new YanZhengZhiWenService()
							.checkFingerprint(o_Application.kuguan_db.getOrganizationId(), "9", ShareUtil.ivalBack);

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
			System.out.println("handel回复");
			isFlag = true;
			switch (msg.what) {
			case 0:
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "验证超时,重试?", OnClick1);
				break;
			case 1:
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				/*
				 * revised by zhangXueWei,增加押运员确认
				 */
				img.setImageBitmap(ShareUtil.finger_gather);
				yayunname = result_user.getLoginUserName();
				// 每次按指纹后清空原来里面的东西
				o_Application.FingerJiaojieNum.clear();
				o_Application.FingerJiaojieNum.add(result_user.getLoginUserId());
				System.out.println("size:" + o_Application.FingerJiaojieNum.size());
				setname.setText("");
				f1 = "1";
				bottom_tishi.setText("");
				manager.getSureCancel().makeSuerCancel2(

						PeiSongJiaoJie_db.this, "押运员：" + yayunname, new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								manager.getSureCancel().remove();
								manager.getRuning().runding(PeiSongJiaoJie_db.this, "提交中...");
								// 交接
								JiaoJie();
							}
						}, false);
				break;
			case 3:
				// 验证3次失败跳登录页面 未实现
				if (!f1.equals("1")) {
					wrongNum++;
					top.setText("验证失败" + wrongNum + "次");
				}
				if (wrongNum >= ShareUtil.three) {
					intent.setClass(PeiSongJiaoJie_db.this, YaYunYuanDengLu.class);
					PeiSongJiaoJie_db.this.startActivityForResult(intent, 1);
					wrongNum = 0;
					bottom_tishi.setText("请押运员按手指...");
				}
				break;
			case 4:
				if (o_Application.FingerJiaojieNum.size() > 0) {
					o_Application.FingerJiaojieNum.clear();
				}
				manager.getRuning().remove();
				manager.getSureCancel().remove();
				Skip.skip(PeiSongJiaoJie_db.this, RenWuLieBiao_db.class, null, 0);
				break;
			case 5:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "网络连接失败,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						JiaoJie();

					}
				});
				break;

			case 6:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "提交超时,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						JiaoJie();

					}
				});
				break;
			case 7:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, jiaojieOk, new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						JiaoJie();

					}
				});
				break;
			case 8:
				manager.getRuning().remove();
				break;
			case 9:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "连接超时，重新链接？", onclickreplace);
				break;
			case 10:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "提交失败", onclickreplace);
				break;
			case 11:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "信息加载异常", onclickreplace);
				break;
			case 13:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "获取失败", OnClick2);
				break;
			case 14:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "信息加载异常", OnClick2);
				break;
			case 15:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(PeiSongJiaoJie_db.this, "超时连接", OnClick2);
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null) {
			Bundle bundle = arg2.getExtras();
			String isOk = bundle.getString("isOk");
			if (isOk.equals("success")) {
				f1 = "1";
				setname.setText(o_Application.yayunyuan.getLoginUserName());
				img.setImageResource(R.drawable.result_isok);
				bottom_tishi.setText("帐号验证成功");
				manager.getRuning().runding(this, "提交中...");
				JiaoJie();
			}
		}
	}

	public void load() {
		back = (ImageView) findViewById(R.id.peisongjiaojie_back);
		back.setOnClickListener(this);
		yanshi = (LinearLayout) findViewById(R.id.peisongjiaojie_yanshi);
		yanshi.setOnClickListener(this);
		top = (TextView) findViewById(R.id.textViewtop);
		setname = (TextView) findViewById(R.id.peisongjiaojie_setname);
		img = (ImageView) findViewById(R.id.peisongjiaojie_img);
		bottom_tishi = (TextView) findViewById(R.id.peisongjiaojie_buttom_prompt);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.peisongjiaojie_back:
			PeiSongJiaoJie_db.this.finish();
			break;
		default:
			break;
		}
	}

	public void JiaoJie() {
		getcashBoxNumAnduserId();
		new Thread() {
			@Override
			public void run() {
				super.run();
				ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
				try {
					if (jigouleibie.equals("0")) {
						if (o_Application.qinglingchuruku.getType().equals("0")) {
							System.out.println("PeiSongJiaoJie_db<--->23:SaveAuthLogPeisong-->" + cashBoxNum + userId
									+ yayunId + "23" + o_Application.qinglingchuruku.getXianluid()
									+ o_Application.kuguan_db.getOrganizationId()
									+ o_Application.qinglingchuruku.getCaozuotype());
							jiaojieOk = jiaojie.SaveAuthLogPeisong(cashBoxNum, userId, yayunId, "23",
									o_Application.qinglingchuruku.getXianluid(),
									o_Application.kuguan_db.getOrganizationId(),
									o_Application.qinglingchuruku.getCaozuotype());
						} else {

							System.out.println("PeiSongJiaoJie_db<--->2D:SaveAuthLogPeisong-->" + cashBoxNum + userId
									+ yayunId + "2D" + o_Application.qinglingchuruku.getXianluid()
									+ o_Application.kuguan_db.getOrganizationId()
									+ o_Application.qinglingchuruku.getCaozuotype());

							jiaojieOk = jiaojie.SaveAuthLogPeisong(cashBoxNum, userId, yayunId, "2D",
									o_Application.qinglingchuruku.getXianluid(),
									o_Application.kuguan_db.getOrganizationId(),
									o_Application.qinglingchuruku.getCaozuotype());
						}
					} else {
						System.out.println("PeiSongJiaoJie_db<--->25:SaveAuthLogPeisong-->" + cashBoxNum + userId
								+ yayunId + "25" + o_Application.qinglingchuruku.getXianluid()
								+ o_Application.kuguan_db.getOrganizationId()
								+ o_Application.qinglingchuruku.getCaozuotype());
						jiaojieOk = jiaojie.SaveAuthLogPeisong(cashBoxNum, userId, yayunId, "25",
								o_Application.qinglingchuruku.getXianluid(),
								o_Application.kuguan_db.getOrganizationId(),
								o_Application.qinglingchuruku.getCaozuotype());
					}
					System.out.println("---------交接完成，返回：" + jiaojieOk);
					if (jiaojieOk.equals("00")) {
						handler.sendEmptyMessage(4);
					} else {
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
		int c = 0;
		for (int i = 0; i < o_Application.numberlist.size(); i++) {
			if (a == o_Application.numberlist.size() - 1) {
				cashBoxNum += o_Application.numberlist.get(i);
			} else {
				cashBoxNum += o_Application.numberlist.get(i) + "_";
			}
			a++;
		}

		for (int i = 0; i < o_Application.FingerLoginNum.size(); i++) {
			userId += o_Application.FingerLoginNum.get(i) + "_";
		}

		for (int i = 0; i < o_Application.FingerJiaojieNum.size(); i++) {
			if (c == o_Application.FingerJiaojieNum.size() - 1) {
				yayunId = o_Application.FingerJiaojieNum.get(i);
				System.out.println("yayunId:" + yayunId);
			} else {
				yayunId += o_Application.FingerJiaojieNum.get(i) + "_";
				System.out.println("yayunId:" + yayunId);
			}
			c++;
		}

		yayunId = o_Application.FingerJiaojieNum.get(0);
		System.out.println("上缴清分出库 cashBoxNum：" + cashBoxNum);
		System.out.println("上缴清分出库 userId：" + userId);
	}

	/**
	 * 获取机构类别
	 */
	public void getJigouLeibie() {
		manager.getRuning().runding(PeiSongJiaoJie_db.this, "获取机构类别中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					jigouleibie = is.getJigouLeibie(o_Application.kuguan_db.getOrganizationId());
					if (null != jigouleibie || !"".equals(jigouleibie)) {
						handler.sendEmptyMessage(8);
					} else {
						handler.sendEmptyMessage(13);
					}
				} catch (SocketTimeoutException e) {
					handler.sendEmptyMessage(15);
					e.printStackTrace();
				} catch (NullPointerException e) {
					handler.sendEmptyMessage(13);
					e.printStackTrace();
				} catch (Exception e) {
					handler.sendEmptyMessage(14);
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isFlag = false;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		f1 = "";
		userId = "";
		cashBoxNum = "";
		o_Application.yayunyuan = null;
		manager.getRuning().remove();
		manager.getSureCancel().remove();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PeiSongJiaoJie_db.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        top.setText("正在验证指纹");
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        super.getCharImgSucceed(charBytes, img);

        ShareUtil.ivalBack = charBytes;
        ShareUtil.finger_gather = img;

        System.out.println("调取验证接口");
        if (isFlag) {
            isFlag = false;
            yanzhengFinger();
        }
    }
}
