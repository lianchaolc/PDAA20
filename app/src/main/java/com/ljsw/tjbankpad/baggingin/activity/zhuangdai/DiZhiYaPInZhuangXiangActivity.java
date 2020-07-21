package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import com.handheld.UHF.UhfManager;
import hdjc.rfid.operator.RFID_Device;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountWithDeleteButtonAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangDaiHeDuiActivity.LeftAdapter;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangDaiHeDuiActivity.LeftHolder;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangDaiHeDuiActivity.RightAdapter;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangDaiHeDuiActivity.RightHolder;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.QingLingRuKuSaoMiao;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;
import com.strings.tocase.CaseString;

import a20.cn.uhf.admin.Tools;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;

/**
 * 最后一步 抵制押品 》装箱
 * 
 * @author Administrator 11-6抵制押品装箱
 */
public class DiZhiYaPInZhuangXiangActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPInZhuangXiangActivity";
	private ListView dzypzhuangxiang_right;
	private ListView dzypzhuangxiang_left;
	private ListView ZZlistview;
	private Button btnzhuangxiang, last_btn_scan, btn_savelocaknum;

	private WalkieDataCountWithDeleteButtonAdapter adawithdeltebtn;// 删除button

	private List<String> saodaoZZ = new ArrayList<String>();
	private List<ScanZZandLockNum> scanZZandrlocalnumlist = new LinkedList<ScanZZandLockNum>();// 存放锁号码 和周转箱号的集合
	private ScanZZandLockNum mScanZZandLockNum;
	private List<String> covrlist = new ArrayList<String>();
	private ImageView ql_ruku_back;
	private TextView Operator;// 操作人
	private TextView righttv;// 带扫面
	private TextView lefttv;// 已经扫描

	private TextView tvlocalnum;// 周转箱号
	private EditText etlocalnum;// 锁号码
	private ScrollView mScrollView;
	private Boolean bl = false;

	List<String> copylist = new ArrayList<String>();// 扫描锁好需要添加的集合
	private String jsonlistzz;
	private static String localMumberStr = ""; // 锁扣号码 非必填项

	private QingLingRuKuSaoMiao getnumber1;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private LeftAdapter ladapter;
	private RightAdapter radapter;

	// 执行扫描的方法
	private ManagerClass managersaomiao;
	private OnClickListener OnClick1;

	private String str;// 接收到的转化的字符串
	private powercontrol rFidPowerControl;// yang
	private UhfManager managercaozuo;//

	private String resultparms;// 提交的返回结果
	private String cvoun;// 接受上一个一面传过来的任务号
	private String banknumber;
	private String position;
	// dialog
	private Dialog dialog;// 成功
	private Dialog dialogfa;// 失败
	private Dialog dialogfaungetbox;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapintozhungxiang);
		Intent intent = getIntent();
		// 接受数据

		banknumber = intent.getStringExtra("banknumber");// 订单所属机构号
		covrlist = (List<String>) getIntent().getSerializableExtra("list"); // 订单集合的json
		position = intent.getStringExtra("position"); // 清分任务 RW1QF0720181031170457
		Log.e(TAG, "=====" + copylist.size() + banknumber + covrlist + position);

		// ================================打开RFID电源控制
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		initView();
		/*
		 * 获取managercaozuo时，有串口的初始化操作 若managercaozuo为null，则串口初始化失败
		 * 
		 */
		managercaozuo = UhfManager.getInstance();
		if (managercaozuo == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}
		// 获取指令操作
		managersaomiao = new ManagerClass();

		initView();

		managercaozuo = UhfManager.getInstance();
		if (managercaozuo == null) {
//			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			showBigToast("串口初始化失败", 1000);
			return;
		}
		initView();
		getnumber1 = new QingLingRuKuSaoMiao();
		getnumber1.setHandler(handler);
		copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		adawithdeltebtn = new WalkieDataCountWithDeleteButtonAdapter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		saodaoZZ.clear();
		Log.e("btdzypSaomiao", "===" + o_Application.qlruku.getZhouzhuanxiang().toString());
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
		getRfid().addNotifly(getnumber1);
		o_Application.numberlist.clear();// 每次进入清空集合
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();

		righttv.setText("" + o_Application.numberlist.size());
		lefttv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		dzypzhuangxiang_left.setAdapter(ladapter);
		dzypzhuangxiang_right.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();

	}

	// 组件初始化
	private void initView() {
		// TODO Auto-generated method stub
		mScrollView = (ScrollView) findViewById(R.id.srcowvie_id);
		dzypzhuangxiang_right = (ListView) findViewById(R.id.dzypztohuangxiang_right);
		dzypzhuangxiang_left = (ListView) findViewById(R.id.dzyptozhuangxiang_left);
		ZZlistview = (ListView) findViewById(R.id.cuowulistview);

		ZZlistview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// 当手指触摸listview时，让父控件焦点,不能滚动
				case MotionEvent.ACTION_DOWN:
					mScrollView.requestDisallowInterceptTouchEvent(true);
				case MotionEvent.ACTION_MOVE:
					break;

				case MotionEvent.ACTION_CANCEL:
					// 当手指松开时，让父控件重新获取焦点
					mScrollView.requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}

		});

		last_btn_scan = (Button) findViewById(R.id.last_btn_zhuangxiangscan);
		last_btn_scan.setBackgroundResource(R.drawable.button_gray);
		last_btn_scan.setOnClickListener(this);
		last_btn_scan.setEnabled(false);

		btnzhuangxiang = (Button) findViewById(R.id.dizhiyapin_btnzhuangxiang);
		btnzhuangxiang.setOnClickListener(this);

		righttv = (TextView) findViewById(R.id.dzypzx_right);
		lefttv = (TextView) findViewById(R.id.dzypzx_left);

		tvlocalnum = (TextView) findViewById(R.id.tvlocalnum);// 显示的周转箱
		etlocalnum = (EditText) findViewById(R.id.etlocalnum);/// 输入 锁扣号
		btn_savelocaknum = (Button) findViewById(R.id.tv_savelocaknum);// 点击录入后显示listview
		btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
		btn_savelocaknum.setOnClickListener(this);// 保存并显示
		btn_savelocaknum.setEnabled(false);
		// localNumberet=(EditText) findViewById(R.id.etlocalnum);
//		localMumberStr=localNumberet.getText().toString().trim();
//		扫大适配器

		// 扫到数据
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		Operator = (TextView) findViewById(R.id.zhuangcxiang_tvusername1);
		Operator.setText("" + GApplication.user.getLoginUserName());

		dzypzhuangxiang_right.setDividerHeight(0);
		dzypzhuangxiang_left.setDividerHeight(0);
	}

	/**
	 * 无限刷新集合
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					managersaomiao.getRuning().remove();
					getRfid().stop_a20();
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
				}
				Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());
				righttv.setText("" + o_Application.numberlist.size());
				lefttv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				dzypzhuangxiang_left.setAdapter(ladapter);
				dzypzhuangxiang_right.setAdapter(radapter);
				// 判断扫描周转箱是否可以点击
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					managersaomiao.getRuning().remove();
//					last_btn_scan.setFocusable(true);
					last_btn_scan.setEnabled(true);
					last_btn_scan.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				break;
			case 4: /// 失败
				dialogfa = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("装箱" + "失败");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);

				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
//            				 getRenWuInfo();
						// 提交成功后清除

					}
				});

				dialogfa.show();

				break;
			case 5: /// 失败
				dialogfaungetbox = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflaterungetbox = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View vungetbox = inflaterungetbox.inflate(R.layout.dialog_success, null);
				Button butungetbox = (Button) vungetbox.findViewById(R.id.success);
				butungetbox.setText("装箱" + "失败" + "请检查是否有这样的箱子");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(butungetbox);

				butungetbox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfaungetbox.dismiss();
						// 提交成功后清除

					}
				});

				dialogfaungetbox.show();

				break;
			case 9:/// 项目中的交接 成功
				dialog = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflater = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("装箱成功");
				dialog.setCancelable(false);
				dialog.setContentView(v);

				but.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						Intent intent = new Intent(DiZhiYaPInZhuangXiangActivity.this,
								DiZhiYaPinKuangJiaActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						DiZhiYaPInZhuangXiangActivity.this.finish();

					}
				});

				dialog.show();

				break;
			}
		}
	};

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);

		@Override
		public int getCount() {
			Log.e("o_Application.numberlist.size()", "测试" + o_Application.qlruku.getZhouzhuanxiang().size());
			return o_Application.qlruku.getZhouzhuanxiang().size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.qlruku.getZhouzhuanxiang().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				lh = new LeftHolder();
				arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_left, null);
				lh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_left_text);
				arg1.setTag(lh);
			} else {
				lh = (LeftHolder) arg1.getTag();
			}
			lh.tv.setText(o_Application.qlruku.getZhouzhuanxiang().get(arg0));
			return arg1;
		}

	}

	public static class LeftHolder {
		TextView tv;
	}

	/****
	 * 左右的适配器
	 * 
	 * @author Administrator
	 *
	 */
	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);

		@Override
		public int getCount() {
			Log.e("o_Application.numberlist.size()", "测试" + o_Application.numberlist.size());
			return o_Application.numberlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.numberlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				rh = new RightHolder();
				arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_right, null);
				rh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_right_text);
				arg1.setTag(rh);
			} else {
				rh = (RightHolder) arg1.getTag();
			}
			rh.tv.setText(o_Application.numberlist.get(arg0));
			return arg1;
		}

	}

	public static class RightHolder {
		TextView tv;
	}

	@Override
	protected void onPause() {
		super.onPause();
		managersaomiao.getRuning().remove();
		o_Application.wrong = "";
		getRfid().close_a20();
	}
//跳转

	ScanZZandLockNum scanZZandLockNum = new ScanZZandLockNum();

	@Override
	public void onClick(View v) {
		byte[] accessPassword = Tools.HexString2Bytes("00000000");
		switch (v.getId()) {
		case R.id.last_btn_zhuangxiangscan:// 扫描
			if (accessPassword.length != 4) {
				Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
				return;
			}
			try {

				byte[] data = managercaozuo.readFrom6C(1, 2, 6, accessPassword);

				if (data != null && data.length > 1) {
					String dataStr = Tools.Bytes2HexString(data, data.length);
// 得到10位的周转箱子号码
					str = CaseString.getBoxNum2(dataStr);

					// 存储号码验证重复
					saodaoZZ.clear();// 每次只保存一个在保存按钮时验证重复判断按钮的被点击状态

					if (str.equals("") || str == null) {
						return;
					}
					saodaoZZ.add(str);// 向集合添加数据
					Log.e(TAG, "===" + str);
//					mScanZZandLockNum.setBoxnum(str);
					tvlocalnum.setText(str);// 显示锁号码
					for (int i = 0; i < saodaoZZ.size(); i++) {
						Log.e(TAG, "=======^^^^^^" + saodaoZZ.get(i));
					}
					if (saodaoZZ.size() != 0) {
						btn_savelocaknum.setEnabled(true);
						btn_savelocaknum.setBackgroundResource(R.drawable.buttom_selector_bg);
					}
					etlocalnum.getText().toString();
					Log.e(TAG, "返回结果====" + etlocalnum.getText().toString().trim());

					break;
				} else {
					if (data != null) {
						showBigToast("扫描失败重试", 500);
						return;
					}
				}

			} catch (Exception e) {
				Log.e(TAG, "===" + e);
			}
			last_btn_scan.setText("重扫");

			etlocalnum.setFocusable(true);
			break;
		case R.id.ql_ruku_back:
			saodaoZZ.clear();
			DiZhiYaPInZhuangXiangActivity.this.finish();
			break;
		case R.id.dizhiyapin_btnzhuangxiang:
			updataMessage();
			break;
		case R.id.tv_savelocaknum:
			bl = false;

			// 得到输入框的一次性锁扣号和扫描到的箱子号放入对象
			mScanZZandLockNum = new ScanZZandLockNum();// 存放锁号码 和周转箱号实体类
			// 正则表达式判断 输入是否符合规范
			if ((etlocalnum.getText().toString().trim().length() > 10)) {
				showBigToast("只能小于10个字符", 500);
				etlocalnum.setText("");
				return;
			} else {
// 判断str buweninull
				if (!str.equals("") && mScanZZandLockNum != null) {
//				先判断是否是第一次 集合长度为0时
					if (scanZZandrlocalnumlist.size() == 0) {

						mScanZZandLockNum.setBoxnum(str);
						mScanZZandLockNum.setBundleNum(etlocalnum.getText().toString().trim());
//			传入后台的集合添加对象
						//// 需要验证重复操作
						scanZZandrlocalnumlist.add(mScanZZandLockNum);

						adawithdeltebtn = new WalkieDataCountWithDeleteButtonAdapter();
						ZZlistview.setAdapter(adawithdeltebtn);

						adawithdeltebtn.notifyDataSetChanged();
						tvlocalnum.setText("");// 显示锁号码
						etlocalnum.setText("");
						btn_savelocaknum.setEnabled(false);
						btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
						// 目的验证重复
					} else if (scanZZandrlocalnumlist.size() != 0) {
						for (int i = 0; i < scanZZandrlocalnumlist.size(); i++) {
							if (scanZZandrlocalnumlist.get(i).getBoxnum().contains(str)) {
								bl = true;
								showBigToast("添加重复", 500);

								break;
							}

//		
						}
						/// 循环后进行数据存储
						if (bl == false) {
							SaveData();
							str = "";

						}
					}
				}
			}
			break;
		default:
			break;
		}

	}

	/***
	 * 
	 * 
	 * clearTaskNum 清分任务 corpId 订单所属机构号 userId 装箱人员 boxnumlistjson 周转箱集合的json
	 * cvounlistJson 订单集合的json
	 * 
	 * 上传数据到服务器最后提交
	 */
	private void updataMessage() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Gson gson2 = new Gson();
					// 转成json两次
					for (int i = 0; i < covrlist.size(); i++) {
						cvoun = gson2.toJson(covrlist);
					}
//		修改妆花为json
					jsonlistzz = gson2.toJson(scanZZandrlocalnumlist);

					String clearTaskNum = position;// 任务号
					String corpId = "" + banknumber; /// 所属机构
					String userId = "" + GApplication.user.getYonghuZhanghao();// 登录人员
//					String boxnumlistjson = jsonlistzz;   // 周转箱集合json 扫描到周装箱成功
//					String cvounlistJson =cvoun;  // 任务集合json
					// localMumberStr锁扣号码 非必填项
					Log.e(TAG, "返回结果" + localMumberStr);
					Log.e(TAG, "==" + clearTaskNum + banknumber + "====" + userId + jsonlistzz + cvoun);
					if (jsonlistzz.equals("") || jsonlistzz == null) {
						showBigToast("请重新扫描", 1000);
						return;
					}
					resultparms = new GetResistCollateralBaggingService().updateCollateralPacket(clearTaskNum, corpId,
							userId, jsonlistzz, cvoun);
					Log.e(TAG, "返回结果" + resultparms);
					if (resultparms.equals("00")) {/// 成功的方法
						handler.sendEmptyMessage(9);
					} else {

						if (resultparms.equals("30")) {
							handler.sendEmptyMessage(5);
						} else {
							handler.sendEmptyMessage(4);
						}

					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}

		}.start();

	}

	/**
	 * 自定义 toast， 增大字体
	 *
	 * @param info     提示信息
	 * @param duration 显示时间，0：短时间，1：长时间
	 */
	public void showBigToast(String info, int duration) {
//        Toast toast = new Toast(kf.getActivity());

		Toast toast = new Toast(DiZhiYaPInZhuangXiangActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(DiZhiYaPInZhuangXiangActivity.this);
		tv.setBackgroundResource(R.drawable.bg_toast);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(25, 5, 25, 5);
		tv.setTextColor(Color.WHITE);
		tv.setText(info);
		tv.setTextSize(30);
		toast.setView(tv);
		toast.setDuration(duration);
		toast.show();

	}

	/***
	 * 带有删除按钮的适配器
	 */

	public class WalkieDataCountWithDeleteButtonAdapter extends BaseAdapter {
		Holder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);

		@Override
		public int getCount() {
			Log.e("*****", "测试长度" + scanZZandrlocalnumlist.size());
			return scanZZandrlocalnumlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return scanZZandrlocalnumlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			lh = new Holder();
			if (arg1 == null) {

				arg1 = lf.inflate(R.layout.listview_withdeletebtn_item, null);
				lh.tv = (TextView) arg1.findViewById(R.id.liebaiodizhiyapinitem);
				lh.liebaiodizhiyapintype = (Button) arg1.findViewById(R.id.liebaiodizhiyapintype);
				lh.localnumber_tv = (TextView) arg1.findViewById(R.id.localnumber_tv);
				arg1.setTag(lh);
			} else {
				lh = (Holder) arg1.getTag();
			}
			lh.liebaiodizhiyapintype.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (scanZZandrlocalnumlist.size() > 0) {
						showInfo(arg0);
						notifyDataSetChanged();
					}
				}
			});
			Log.e(TAG,
					arg0 + "******scanZZandrlocalnumlist.get(arg0)" + scanZZandrlocalnumlist.get(arg0).getBundleNum());
			Log.e(TAG, arg0 + "******scanZZandrlocalnumlist.get(arg0)" + scanZZandrlocalnumlist.get(arg0).getBoxnum());

			lh.tv.setText(scanZZandrlocalnumlist.get(arg0).getBoxnum());
			lh.localnumber_tv.setText(scanZZandrlocalnumlist.get(arg0).getBundleNum());
			return arg1;
		}

	}

	static class Holder {
		TextView tv, localnumber_tv;
		Button liebaiodizhiyapintype;
	}

	static class MyTextWatcher implements TextWatcher {
		private int arg0;
		private SparseArray<String> sparseArray;

		public void updatePosition(int arg0) {
			this.arg0 = arg0;
		}

		public MyTextWatcher(int arg0, SparseArray<String> sparseArray) {
			this.arg0 = arg0;
			this.sparseArray = sparseArray;
		}

		@Override
		public void afterTextChanged(Editable s) {
			sparseArray.put(arg0, s.toString());
			localMumberStr = s.toString();
			Log.e(TAG, "******=====" + localMumberStr);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 执行删除
	 * 
	 * @param position
	 */
	private void showInfo(int arg0) {
		Log.e(TAG, "******====" + arg0);
		scanZZandrlocalnumlist.remove(arg0);
		adawithdeltebtn.notifyDataSetChanged();
	}

	public void SaveData() {
		if (!scanZZandrlocalnumlist.contains(saodaoZZ)) {
			Log.e(TAG, "===" + str);
			mScanZZandLockNum.setBoxnum(str);
			mScanZZandLockNum.setBundleNum(etlocalnum.getText().toString().trim());
			// 传入后台的集合添加对象
			// 需要验证重复操作
			scanZZandrlocalnumlist.add(0, mScanZZandLockNum);

			adawithdeltebtn = new WalkieDataCountWithDeleteButtonAdapter();
			ZZlistview.setAdapter(adawithdeltebtn);
			adawithdeltebtn.notifyDataSetChanged();
			tvlocalnum.setText("");// 显示锁号码
			etlocalnum.setText("");
			btn_savelocaknum.setEnabled(false);
			btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
			return;

		} else if (scanZZandrlocalnumlist.contains(str)) {
			showBigToast("重复", 100);
			return;
		}
	}

}
