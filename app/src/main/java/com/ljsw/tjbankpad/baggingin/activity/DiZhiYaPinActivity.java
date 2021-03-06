package com.ljsw.tjbankpad.baggingin.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.application.GApplication;
import com.example.app.activity.KuanxiangChuruActivity;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handheld.UHF.UhfManager;
import com.ljsw.tjbankpad.baggingin.activity.adapter.AutoComperAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinHeDuiAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangDaiItemActivity;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity;
import com.ljsw.tjbankpad.baggingin.activity.entity.JsonRootBean;
import com.ljsw.tjbankpad.baggingin.activity.entity.UndoPacketList;
import com.ljsw.tjbankpda.main.QinglingZhouzhuanxiangluruActivity;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.MySpinner.SpinnerItemClickImp;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;
import com.strings.tocase.CaseString;
//import android.support.v7.app.Activity;
import a20.cn.uhf.admin.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;

/***
 * ?????????????????? ???????????? ??????????????? ????????????????????????????????????
 * 
 * @author Administrator ???????????? ??????????????????
 */
public class DiZhiYaPinActivity extends Activity implements OnClickListener {

	private static final String TAG = "DiZhiYaPinActivity";
	private TextView ql_ruku_update;// ??????
	private Button zhuangdai_btn, Btn_Saomiao, btn_dizhiyapinsaomiao;
	private TextView tv_spinnerinfo, dizhiyapin_username; // ?????????????????????????????????????????????
	private TextView tv_dizhiyapinsousuo;// ??????
	private TextView textView1;
	private TextView org_tv;
	private AutoCompleteTextView dizhiyapin_tv;
	private List<String> datalist = new ArrayList<String>();
	private List<String> datalisttvtishi = new ArrayList<String>();
	private List<UndoPacketList> UndoPacketList = new ArrayList<UndoPacketList>();

	private Map<String, Object> mapUnpacketorg = new LinkedHashMap<String, Object>();// ??????key value ????????????????????????
	private Map<String, Object> mapvalueUnpacketuser = new LinkedHashMap<String, Object>();

	private TextView kaihuren;// ?????????
	private TextView kaiwupin;// ????????????
	private TextView tv_resultsaomiao;// ??????????????????

	private Spinner spinner_name;
	private ListView dzyplistview;
	private ManagerClass managerclass = new ManagerClass();
	String postion;
	String weisaoliebiao;// json ??????
	private String result;
	private AutoComperAdapter Autoadapter;
	private TextView tvallpage, unsancanpage;
	private JsonRootBean jrb = new JsonRootBean();// ???????????????????????????????????????
	// ?????????
	private String allpagers;
	private String unscanpagers;
	private ManagerClass manageractivity;// /activity??????
	// / ????????????
	private final String[] strMemBank = { "RESERVE", "EPC", "TID", "USER" };// RESERVE
																			// EPC
																			// TID
																			// USER????????????0,1,2,3
	private powercontrol rFidPowerControl;// yang
	private UhfManager manager;
	private String upnumber = "";// ?????????????????????
	private String upsaomiao = "";// ????????????
	private String str;// ??????????????????????????????
	String epc = "";
	private Dialog dialog;
	private Dialog dialogfa;
	// / ?????????
	private ManagerClass managerClassadialog;
	private SuccessDialog successDialog;// ???????????????
	private DialogManager dmanager;// ??????????????????
	private CustomDialog outDialog;
	private CustomDialog outDialogfaile;// ??????

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acctivity_dizhiyapin);
		Intent i2 = getIntent();
		manageractivity = new ManagerClass();
		postion = i2.getStringExtra("postion");

//		postion = "RW0QF0620181024161112";
		Log.e("postion", "postion====" + postion);
		// ================================??????RFID????????????
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		dmanager = new DialogManager(DiZhiYaPinActivity.this);

		initView();
		manager = UhfManager.getInstance();
		if (manager == null) {
//			Toast.makeText(getApplicationContext(), "?????????????????????", 0).show();
			showBigToast("?????????????????????", 1000);
			return;
		}

		getRenWuInfo();
	}

	private void initView() {
		// TODO Auto-generated method stub
		zhuangdai_btn = (Button) findViewById(R.id.zhuangdai_btn);
		zhuangdai_btn.setOnClickListener(this);
		dizhiyapin_username = (TextView) findViewById(R.id.dizhiyapin_username);// ????????????
		dizhiyapin_username.setText("" + GApplication.user.getLoginUserName());

		dizhiyapin_tv = (AutoCompleteTextView) findViewById(R.id.dizhiyapintv);// ?????????????????????
		dizhiyapin_tv.setThreshold(1);
		tv_dizhiyapinsousuo = (TextView) findViewById(R.id.tv_dizhiyapinsousuo);
		tv_dizhiyapinsousuo.setOnClickListener(this);
		tv_dizhiyapinsousuo.setText("??????");
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setOnClickListener(this);

		tv_resultsaomiao = (TextView) findViewById(R.id.tv_resultsaomiao);// ????????????
		btn_dizhiyapinsaomiao = (Button) findViewById(R.id.btn_dizhiyapinsaomiao);
		btn_dizhiyapinsaomiao.setOnClickListener(this);
		tvallpage = (TextView) findViewById(R.id.dizhiyapin_allpakager);// ?????????
		unsancanpage = (TextView) findViewById(R.id.dizhiyapin_unallpakager);// ????????????
		kaihuren = (TextView) findViewById(R.id.kaihuren);
		kaiwupin = (TextView) findViewById(R.id.kaiwupin);
		ql_ruku_update = (TextView) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);

	}

	/****
	 * ????????????????????????
	 */
	public void getRenWuInfo() {
//		manager.getRuning().runding(this, "???????????????...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					weisaoliebiao = new GetResistCollateralBaggingService().SendBoxBagSubmission(postion);
					// json??????list
					Gson gson = new Gson();
					jrb = gson.fromJson(weisaoliebiao, JsonRootBean.class);
					allpagers = jrb.getSum() + "";
					unscanpagers = jrb.getUndoSum() + "";

					Log.e(TAG, "??????" + jrb.getSum());
					Log.e(TAG, "??????" + jrb.getUndoSum());
					// ????????????????????????
					datalisttvtishi.clear();
					for (int i = 0; i < jrb.getUndoPacketNumList().size(); i++) {
						datalisttvtishi.add(jrb.getUndoPacketNumList().get(i));
						Log.e(TAG, "??????" + datalisttvtishi.get(i));
					}
					// ????????????????????????
					UndoPacketList.clear();
					for (int j = 0; j < jrb.getUndoPacketList().size(); j++) {
						UndoPacketList.add(jrb.getUndoPacketList().get(j));
						mapUnpacketorg.put(jrb.getUndoPacketList().get(j).getPLANNO(),
								UndoPacketList.get(j).getCUSTOMERNAME());
						mapvalueUnpacketuser.put(jrb.getUndoPacketList().get(j).getPLANNO(),
								UndoPacketList.get(j).getPACKETNUM());
					}

//					adapter = new ArrayAdapter<String>(DiZhiYaPinActivity.this,android.R.layout.simple_dropdown_item_1line,datalisttvtishi);

					Autoadapter = new AutoComperAdapter(DiZhiYaPinActivity.this,
							android.R.layout.simple_dropdown_item_1line, datalisttvtishi, AutoComperAdapter.ALL);

					handler.sendEmptyMessage(0);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "SocketTimeout??????" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer??????" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "??????" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		byte[] accessPassword = Tools.HexString2Bytes("00000000");

		switch (v.getId()) {
		case R.id.zhuangdai_btn:// ???????????????dialog

			if (kaihuren.getText().equals("") || kaihuren.getText() == null) {
				showBigToast("????????????????????????", 500);
				return;

			} else if (tv_resultsaomiao.getText().equals("") || tv_resultsaomiao.getText() == null) {
				showBigToast("???????????????", 500);
				return;
			} else {

				Zhuangdai();

			}

			break;
		case R.id.btn_dizhiyapinsaomiao:// ????????????
			if (datalisttvtishi.size() == 0) {
				showBigToast("????????????????????????", 1000);
				return;
			} else if (kaihuren.getText().equals("")) {
				showBigToast("????????????????????????", 1000);
				return;
			} else {
				tv_resultsaomiao.setText("");
				if (accessPassword.length != 4) {
					showBigToast("?????????4?????????", 1000);
					return;
				}

				byte[] data = manager.readFrom6C(1, 2, 6, accessPassword);
				if (data != null && data.length > 1) {
					String dataStr = Tools.Bytes2HexString(data, data.length);
//					Toast.makeText(getApplicationContext(),dataStr, 0).show();

//					read.setText(dataStr);		

					if (dataStr == null || !CaseString.reg(dataStr)) {
						return;
					}
					str = dataStr;
					str = CaseString.getBoxNum2(str);
					tv_resultsaomiao.setText(str);

					upsaomiao = str;
					break;
				} else {
					if (data != null) {
						showBigToast("??????????????????", 1000);
						return;
					}
				}
				btn_dizhiyapinsaomiao.setText("??????");
			}

			break;
		case R.id.tv_dizhiyapinsousuo:
			getRenWuInfo();
			break;
		case R.id.ql_ruku_update:// ????????????
			getRenWuInfo();
			break;
		default:
			break;
		}
	}

	/***
	 * ??????????????????
	 */

	private void Zhuangdai() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {

					result = new GetResistCollateralBaggingService()
//					str
							.GetResistzhuangdai(upnumber, upsaomiao, GApplication.loginname);
					// json??????list
					if ("00".equals(result)) {
						Log.e(TAG, "result" + result);
						handler.sendEmptyMessage(9);
					} else {
						Log.e(TAG, "result" + result);
						handler.sendEmptyMessage(4);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "SocketTimeout??????" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer??????" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception??????" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.e(TAG, "??????TExtview====" + allpagers + unscanpagers);

				tvallpage.setText(allpagers);
				unsancanpage.setText(unscanpagers);

				dizhiyapin_tv.setAdapter(Autoadapter);
				Log.e(TAG, "====" + datalisttvtishi.toString());

//				dizhiyapintv.setOnFocusChangeListener(new OnFocusChangeListener() {
//					
//					@Override
//					public void onFocusChange(View v, boolean hasFocus) {
//						AutoCompleteTextView view = (AutoCompleteTextView) v;
//						if(hasFocus){
//							 view.showDropDown();
//						}
//						  
//					}
//				});
//				
				dizhiyapin_tv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						upnumber = dizhiyapin_tv.getText().toString();// ???????????????????????????
						for (Map.Entry<String, Object> entry : mapUnpacketorg.entrySet()) {

							// ?????????????????? ?????????????????????????????????
							if (dizhiyapin_tv.getText().toString().trim().equals(entry.getKey())) {
								kaihuren.setText(entry.getValue() + "");

							}
						}
						for (Map.Entry<String, Object> entryvalue : mapvalueUnpacketuser.entrySet()) {
							if (dizhiyapin_tv.getText().toString().trim().equals(entryvalue.getKey())) {
								kaiwupin.setText(entryvalue.getValue() + "");
								return;
							}
						}

						kaihuren.setText(UndoPacketList.get(arg2).getCUSTOMERNAME());
						kaiwupin.setText(UndoPacketList.get(arg2).getPACKETNUM());
					}
				});
				break;
			case 3:

				break;

			case 4: /// ??????
				dialogfa = new Dialog(DiZhiYaPinActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(DiZhiYaPinActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("????????????" + result);
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
//            				 getRenWuInfo();
						// ?????????????????????
						tv_resultsaomiao.setText("");
					}
				});
				dialogfa.show();

				break;
			case 9:/// ?????????????????? ??????

				dialog = new Dialog(DiZhiYaPinActivity.this);
				LayoutInflater inflater = LayoutInflater.from(DiZhiYaPinActivity.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("????????????");
				dialog.setCancelable(false);
				dialog.setContentView(v);
				but.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						getRenWuInfo();
						// ?????????????????????
						tv_resultsaomiao.setText("");
						kaihuren.setText("");
						kaiwupin.setText("");
						dizhiyapin_tv.setText("");
						Log.e(TAG, "+++++++++++" + unscanpagers);
						if (unscanpagers.equals("1")) {
							manageractivity.getRuning().runding(DiZhiYaPinActivity.this, "????????????");
							// Skip.skip(DiZhiYaPinActivity.this,
							// BoxToDiZhiYaPinZhuangdaiActivity.class, null,
							// 0);
							// S_application.wrong="huitui";

							Intent intent = new Intent(DiZhiYaPinActivity.this, BoxToDiZhiYaPinZhuangdaiActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();

						}
					}
				});
				dialog.show();

				break;

			}
		}

	};

	/**
	 * ????????? toast??? ????????????
	 *
	 * @param info     ????????????
	 * @param duration ???????????????0???????????????1????????????
	 */
	public void showBigToast(String info, int duration) {
//        Toast toast = new Toast(kf.getActivity());

		Toast toast = new Toast(DiZhiYaPinActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(DiZhiYaPinActivity.this);
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
	 * dialog
	 */

//    private void showLoginOutDialog() {
//        outDialog = new CustomDialog(DiZhiYaPinActivity.this);
//        outDialog.builder().setTitle("????????????")
//                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() { // ??????
//            @Override
//            public void onClick(View view) {
//                outDialog.dismiss();
//                getRenWuInfo();
//                //?????????????????????
//            	tv_resultsaomiao.setText("");
//            	kaihuren.setText("");
//            	kaiwupin.setText("");
//            	dizhiyapintv.setText("");
//            	Log.e(TAG,"dizhilistinfo..size()===="+dizhilistinfo.size());
//            	 if(dizhilistinfo.size()==0){
//     	        	zhuangdai_btn.setText("??????");
//             		if(zhuangdai_btn.getText().equals("??????")){
// 	        					// TODO Auto-generated method stub
// 	        					manageractivity.getRuning().runding(DiZhiYaPinActivity.this, "");
// 	        					Skip.skip(DiZhiYaPinActivity.this, BoxToDiZhiYaPinZhuangdaiActivity.class, null, 0);	
// 	        					 
// 	        				}
// 	        				
// 	        		
// 	        			}
////             		  		 DiZhiYaPinActivity.this.finish();
//     	        }
//        });
//}

//    private void showLoginOutDialogfaile() {
//        outDialogfaile = new CustomDialog(DiZhiYaPinActivity.this);
//        outDialogfaile.builder().setTitle("????????????"+result1)
//                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() { // ??????
//            @Override
//            public void onClick(View view) {
//                outDialogfaile.dismiss();
//                getRenWuInfo();
//                //?????????????????????
//            	tv_resultsaomiao.setText("");
//            	kaihuren.setText("");
//            	kaiwupin.setText("");
//            	dizhiyapintv.setText("");
//            	Log.e(TAG,"??????");
//      
//     	        }
//        });
//}

}
