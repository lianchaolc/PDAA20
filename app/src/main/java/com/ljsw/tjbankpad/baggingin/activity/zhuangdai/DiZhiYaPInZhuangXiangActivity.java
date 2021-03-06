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
 * ???????????? ???????????? ?????????
 * 
 * @author Administrator 11-6??????????????????
 */
public class DiZhiYaPInZhuangXiangActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPInZhuangXiangActivity";
	private ListView dzypzhuangxiang_right;
	private ListView dzypzhuangxiang_left;
	private ListView ZZlistview;
	private Button btnzhuangxiang, last_btn_scan, btn_savelocaknum;

	private WalkieDataCountWithDeleteButtonAdapter adawithdeltebtn;// ??????button

	private List<String> saodaoZZ = new ArrayList<String>();
	private List<ScanZZandLockNum> scanZZandrlocalnumlist = new LinkedList<ScanZZandLockNum>();// ??????????????? ????????????????????????
	private ScanZZandLockNum mScanZZandLockNum;
	private List<String> covrlist = new ArrayList<String>();
	private ImageView ql_ruku_back;
	private TextView Operator;// ?????????
	private TextView righttv;// ?????????
	private TextView lefttv;// ????????????

	private TextView tvlocalnum;// ????????????
	private EditText etlocalnum;// ?????????
	private ScrollView mScrollView;
	private Boolean bl = false;

	List<String> copylist = new ArrayList<String>();// ?????????????????????????????????
	private String jsonlistzz;
	private static String localMumberStr = ""; // ???????????? ????????????

	private QingLingRuKuSaoMiao getnumber1;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private LeftAdapter ladapter;
	private RightAdapter radapter;

	// ?????????????????????
	private ManagerClass managersaomiao;
	private OnClickListener OnClick1;

	private String str;// ??????????????????????????????
	private powercontrol rFidPowerControl;// yang
	private UhfManager managercaozuo;//

	private String resultparms;// ?????????????????????
	private String cvoun;// ??????????????????????????????????????????
	private String banknumber;
	private String position;
	// dialog
	private Dialog dialog;// ??????
	private Dialog dialogfa;// ??????
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
		// ????????????

		banknumber = intent.getStringExtra("banknumber");// ?????????????????????
		covrlist = (List<String>) getIntent().getSerializableExtra("list"); // ???????????????json
		position = intent.getStringExtra("position"); // ???????????? RW1QF0720181031170457
		Log.e(TAG, "=====" + copylist.size() + banknumber + covrlist + position);

		// ================================??????RFID????????????
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		initView();
		/*
		 * ??????managercaozuo????????????????????????????????? ???managercaozuo???null???????????????????????????
		 * 
		 */
		managercaozuo = UhfManager.getInstance();
		if (managercaozuo == null) {
			Toast.makeText(getApplicationContext(), "?????????????????????", 0).show();
			return;
		}
		// ??????????????????
		managersaomiao = new ManagerClass();

		initView();

		managercaozuo = UhfManager.getInstance();
		if (managercaozuo == null) {
//			Toast.makeText(getApplicationContext(), "?????????????????????", 0).show();
			showBigToast("?????????????????????", 1000);
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
		o_Application.numberlist.clear();// ????????????????????????
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

	// ???????????????
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
				// ???????????????listview????????????????????????,????????????
				case MotionEvent.ACTION_DOWN:
					mScrollView.requestDisallowInterceptTouchEvent(true);
				case MotionEvent.ACTION_MOVE:
					break;

				case MotionEvent.ACTION_CANCEL:
					// ???????????????????????????????????????????????????
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

		tvlocalnum = (TextView) findViewById(R.id.tvlocalnum);// ??????????????????
		etlocalnum = (EditText) findViewById(R.id.etlocalnum);/// ?????? ?????????
		btn_savelocaknum = (Button) findViewById(R.id.tv_savelocaknum);// ?????????????????????listview
		btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
		btn_savelocaknum.setOnClickListener(this);// ???????????????
		btn_savelocaknum.setEnabled(false);
		// localNumberet=(EditText) findViewById(R.id.etlocalnum);
//		localMumberStr=localNumberet.getText().toString().trim();
//		???????????????

		// ????????????
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		Operator = (TextView) findViewById(R.id.zhuangcxiang_tvusername1);
		Operator.setText("" + GApplication.user.getLoginUserName());

		dzypzhuangxiang_right.setDividerHeight(0);
		dzypzhuangxiang_left.setDividerHeight(0);
	}

	/**
	 * ??????????????????
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
				// ????????????????????????????????????
				if (o_Application.numberlist.size() > 0) {
				}
				Log.e(TAG, "?????????=== " + o_Application.numberlist.size());
				righttv.setText("" + o_Application.numberlist.size());
				lefttv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				Log.e(TAG, "??????===" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				dzypzhuangxiang_left.setAdapter(ladapter);
				dzypzhuangxiang_right.setAdapter(radapter);
				// ???????????????????????????????????????
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					managersaomiao.getRuning().remove();
//					last_btn_scan.setFocusable(true);
					last_btn_scan.setEnabled(true);
					last_btn_scan.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				break;
			case 4: /// ??????
				dialogfa = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("??????" + "??????");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);

				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
//            				 getRenWuInfo();
						// ?????????????????????

					}
				});

				dialogfa.show();

				break;
			case 5: /// ??????
				dialogfaungetbox = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflaterungetbox = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View vungetbox = inflaterungetbox.inflate(R.layout.dialog_success, null);
				Button butungetbox = (Button) vungetbox.findViewById(R.id.success);
				butungetbox.setText("??????" + "??????" + "?????????????????????????????????");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(butungetbox);

				butungetbox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfaungetbox.dismiss();
						// ?????????????????????

					}
				});

				dialogfaungetbox.show();

				break;
			case 9:/// ?????????????????? ??????
				dialog = new Dialog(DiZhiYaPInZhuangXiangActivity.this);
				LayoutInflater inflater = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("????????????");
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
			Log.e("o_Application.numberlist.size()", "??????" + o_Application.qlruku.getZhouzhuanxiang().size());
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
	 * ??????????????????
	 * 
	 * @author Administrator
	 *
	 */
	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);

		@Override
		public int getCount() {
			Log.e("o_Application.numberlist.size()", "??????" + o_Application.numberlist.size());
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
//??????

	ScanZZandLockNum scanZZandLockNum = new ScanZZandLockNum();

	@Override
	public void onClick(View v) {
		byte[] accessPassword = Tools.HexString2Bytes("00000000");
		switch (v.getId()) {
		case R.id.last_btn_zhuangxiangscan:// ??????
			if (accessPassword.length != 4) {
				Toast.makeText(getApplicationContext(), "?????????4?????????", Toast.LENGTH_SHORT).show();
				return;
			}
			try {

				byte[] data = managercaozuo.readFrom6C(1, 2, 6, accessPassword);

				if (data != null && data.length > 1) {
					String dataStr = Tools.Bytes2HexString(data, data.length);
// ??????10????????????????????????
					str = CaseString.getBoxNum2(dataStr);

					// ????????????????????????
					saodaoZZ.clear();// ?????????????????????????????????????????????????????????????????????????????????

					if (str.equals("") || str == null) {
						return;
					}
					saodaoZZ.add(str);// ?????????????????????
					Log.e(TAG, "===" + str);
//					mScanZZandLockNum.setBoxnum(str);
					tvlocalnum.setText(str);// ???????????????
					for (int i = 0; i < saodaoZZ.size(); i++) {
						Log.e(TAG, "=======^^^^^^" + saodaoZZ.get(i));
					}
					if (saodaoZZ.size() != 0) {
						btn_savelocaknum.setEnabled(true);
						btn_savelocaknum.setBackgroundResource(R.drawable.buttom_selector_bg);
					}
					etlocalnum.getText().toString();
					Log.e(TAG, "????????????====" + etlocalnum.getText().toString().trim());

					break;
				} else {
					if (data != null) {
						showBigToast("??????????????????", 500);
						return;
					}
				}

			} catch (Exception e) {
				Log.e(TAG, "===" + e);
			}
			last_btn_scan.setText("??????");

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

			// ????????????????????????????????????????????????????????????????????????
			mScanZZandLockNum = new ScanZZandLockNum();// ??????????????? ????????????????????????
			// ????????????????????? ????????????????????????
			if ((etlocalnum.getText().toString().trim().length() > 10)) {
				showBigToast("????????????10?????????", 500);
				etlocalnum.setText("");
				return;
			} else {
// ??????str buweninull
				if (!str.equals("") && mScanZZandLockNum != null) {
//				??????????????????????????? ???????????????0???
					if (scanZZandrlocalnumlist.size() == 0) {

						mScanZZandLockNum.setBoxnum(str);
						mScanZZandLockNum.setBundleNum(etlocalnum.getText().toString().trim());
//			?????????????????????????????????
						//// ????????????????????????
						scanZZandrlocalnumlist.add(mScanZZandLockNum);

						adawithdeltebtn = new WalkieDataCountWithDeleteButtonAdapter();
						ZZlistview.setAdapter(adawithdeltebtn);

						adawithdeltebtn.notifyDataSetChanged();
						tvlocalnum.setText("");// ???????????????
						etlocalnum.setText("");
						btn_savelocaknum.setEnabled(false);
						btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
						// ??????????????????
					} else if (scanZZandrlocalnumlist.size() != 0) {
						for (int i = 0; i < scanZZandrlocalnumlist.size(); i++) {
							if (scanZZandrlocalnumlist.get(i).getBoxnum().contains(str)) {
								bl = true;
								showBigToast("????????????", 500);

								break;
							}

//		
						}
						/// ???????????????????????????
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
	 * clearTaskNum ???????????? corpId ????????????????????? userId ???????????? boxnumlistjson ??????????????????json
	 * cvounlistJson ???????????????json
	 * 
	 * ????????????????????????????????????
	 */
	private void updataMessage() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Gson gson2 = new Gson();
					// ??????json??????
					for (int i = 0; i < covrlist.size(); i++) {
						cvoun = gson2.toJson(covrlist);
					}
//		???????????????json
					jsonlistzz = gson2.toJson(scanZZandrlocalnumlist);

					String clearTaskNum = position;// ?????????
					String corpId = "" + banknumber; /// ????????????
					String userId = "" + GApplication.user.getYonghuZhanghao();// ????????????
//					String boxnumlistjson = jsonlistzz;   // ???????????????json ????????????????????????
//					String cvounlistJson =cvoun;  // ????????????json
					// localMumberStr???????????? ????????????
					Log.e(TAG, "????????????" + localMumberStr);
					Log.e(TAG, "==" + clearTaskNum + banknumber + "====" + userId + jsonlistzz + cvoun);
					if (jsonlistzz.equals("") || jsonlistzz == null) {
						showBigToast("???????????????", 1000);
						return;
					}
					resultparms = new GetResistCollateralBaggingService().updateCollateralPacket(clearTaskNum, corpId,
							userId, jsonlistzz, cvoun);
					Log.e(TAG, "????????????" + resultparms);
					if (resultparms.equals("00")) {/// ???????????????
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
	 * ????????? toast??? ????????????
	 *
	 * @param info     ????????????
	 * @param duration ???????????????0???????????????1????????????
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
	 * ??????????????????????????????
	 */

	public class WalkieDataCountWithDeleteButtonAdapter extends BaseAdapter {
		Holder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPInZhuangXiangActivity.this);

		@Override
		public int getCount() {
			Log.e("*****", "????????????" + scanZZandrlocalnumlist.size());
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
	 * ????????????
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
			// ?????????????????????????????????
			// ????????????????????????
			scanZZandrlocalnumlist.add(0, mScanZZandLockNum);

			adawithdeltebtn = new WalkieDataCountWithDeleteButtonAdapter();
			ZZlistview.setAdapter(adawithdeltebtn);
			adawithdeltebtn.notifyDataSetChanged();
			tvlocalnum.setText("");// ???????????????
			etlocalnum.setText("");
			btn_savelocaknum.setEnabled(false);
			btn_savelocaknum.setBackgroundResource(R.drawable.button_gray);
			return;

		} else if (scanZZandrlocalnumlist.contains(str)) {
			showBigToast("??????", 100);
			return;
		}
	}

}
