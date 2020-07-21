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
 * 地址押品页面 抵制押品 上缴和请领 输入提示显示的号进行操作
 * 
 * @author Administrator 抵制押品 最后一步装袋
 */
public class DiZhiYaPinActivity extends Activity implements OnClickListener {

	private static final String TAG = "DiZhiYaPinActivity";
	private TextView ql_ruku_update;// 刷新
	private Button zhuangdai_btn, Btn_Saomiao, btn_dizhiyapinsaomiao;
	private TextView tv_spinnerinfo, dizhiyapin_username; // 用户选中那个袋子进行装袋的操作
	private TextView tv_dizhiyapinsousuo;// 搜索
	private TextView textView1;
	private TextView org_tv;
	private AutoCompleteTextView dizhiyapin_tv;
	private List<String> datalist = new ArrayList<String>();
	private List<String> datalisttvtishi = new ArrayList<String>();
	private List<UndoPacketList> UndoPacketList = new ArrayList<UndoPacketList>();

	private Map<String, Object> mapUnpacketorg = new LinkedHashMap<String, Object>();// 通过key value 保存要显示的条目
	private Map<String, Object> mapvalueUnpacketuser = new LinkedHashMap<String, Object>();

	private TextView kaihuren;// 所属人
	private TextView kaiwupin;// 物品种类
	private TextView tv_resultsaomiao;// 扫，到的号码

	private Spinner spinner_name;
	private ListView dzyplistview;
	private ManagerClass managerclass = new ManagerClass();
	String postion;
	String weisaoliebiao;// json 数据
	private String result;
	private AutoComperAdapter Autoadapter;
	private TextView tvallpage, unsancanpage;
	private JsonRootBean jrb = new JsonRootBean();// 实体类抵制押品装袋的实体类
	// 总袋数
	private String allpagers;
	private String unscanpagers;
	private ManagerClass manageractivity;// /activity提示
	// / 扫描需要
	private final String[] strMemBank = { "RESERVE", "EPC", "TID", "USER" };// RESERVE
																			// EPC
																			// TID
																			// USER分别对应0,1,2,3
	private powercontrol rFidPowerControl;// yang
	private UhfManager manager;
	private String upnumber = "";// 上传服务的箱子
	private String upsaomiao = "";// 扫描箱子
	private String str;// 接收到的转化的字符串
	String epc = "";
	private Dialog dialog;
	private Dialog dialogfa;
	// / 提示框
	private ManagerClass managerClassadialog;
	private SuccessDialog successDialog;// 成功弹出框
	private DialogManager dmanager;// 弹出框管理类
	private CustomDialog outDialog;
	private CustomDialog outDialogfaile;// 失败

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acctivity_dizhiyapin);
		Intent i2 = getIntent();
		manageractivity = new ManagerClass();
		postion = i2.getStringExtra("postion");

//		postion = "RW0QF0620181024161112";
		Log.e("postion", "postion====" + postion);
		// ================================打开RFID电源控制
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		dmanager = new DialogManager(DiZhiYaPinActivity.this);

		initView();
		manager = UhfManager.getInstance();
		if (manager == null) {
//			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			showBigToast("串口初始化失败", 1000);
			return;
		}

		getRenWuInfo();
	}

	private void initView() {
		// TODO Auto-generated method stub
		zhuangdai_btn = (Button) findViewById(R.id.zhuangdai_btn);
		zhuangdai_btn.setOnClickListener(this);
		dizhiyapin_username = (TextView) findViewById(R.id.dizhiyapin_username);// 操作人员
		dizhiyapin_username.setText("" + GApplication.user.getLoginUserName());

		dizhiyapin_tv = (AutoCompleteTextView) findViewById(R.id.dizhiyapintv);// 输入的自动提示
		dizhiyapin_tv.setThreshold(1);
		tv_dizhiyapinsousuo = (TextView) findViewById(R.id.tv_dizhiyapinsousuo);
		tv_dizhiyapinsousuo.setOnClickListener(this);
		tv_dizhiyapinsousuo.setText("搜索");
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setOnClickListener(this);

		tv_resultsaomiao = (TextView) findViewById(R.id.tv_resultsaomiao);// 扫描号码
		btn_dizhiyapinsaomiao = (Button) findViewById(R.id.btn_dizhiyapinsaomiao);
		btn_dizhiyapinsaomiao.setOnClickListener(this);
		tvallpage = (TextView) findViewById(R.id.dizhiyapin_allpakager);// 全部包
		unsancanpage = (TextView) findViewById(R.id.dizhiyapin_unallpakager);// 没扫的包
		kaihuren = (TextView) findViewById(R.id.kaihuren);
		kaiwupin = (TextView) findViewById(R.id.kaiwupin);
		ql_ruku_update = (TextView) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);

	}

	/****
	 * 获取到箱子的列表
	 */
	public void getRenWuInfo() {
//		manager.getRuning().runding(this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					weisaoliebiao = new GetResistCollateralBaggingService().SendBoxBagSubmission(postion);
					// json转成list
					Gson gson = new Gson();
					jrb = gson.fromJson(weisaoliebiao, JsonRootBean.class);
					allpagers = jrb.getSum() + "";
					unscanpagers = jrb.getUndoSum() + "";

					Log.e(TAG, "测试" + jrb.getSum());
					Log.e(TAG, "测试" + jrb.getUndoSum());
					// 获取下拉框的数据
					datalisttvtishi.clear();
					for (int i = 0; i < jrb.getUndoPacketNumList().size(); i++) {
						datalisttvtishi.add(jrb.getUndoPacketNumList().get(i));
						Log.e(TAG, "元素" + datalisttvtishi.get(i));
					}
					// 提交数据返回结果
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
					Log.e(TAG, "SocketTimeout异常" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer异常" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "异常" + e);
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
		case R.id.zhuangdai_btn:// 弹出成功的dialog

			if (kaihuren.getText().equals("") || kaihuren.getText() == null) {
				showBigToast("请检查抵制押品号", 500);
				return;

			} else if (tv_resultsaomiao.getText().equals("") || tv_resultsaomiao.getText() == null) {
				showBigToast("请检查锁号", 500);
				return;
			} else {

				Zhuangdai();

			}

			break;
		case R.id.btn_dizhiyapinsaomiao:// 进行扫描
			if (datalisttvtishi.size() == 0) {
				showBigToast("请检查抵制押品号", 1000);
				return;
			} else if (kaihuren.getText().equals("")) {
				showBigToast("请检查抵制押品号", 1000);
				return;
			} else {
				tv_resultsaomiao.setText("");
				if (accessPassword.length != 4) {
					showBigToast("密码为4个字节", 1000);
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
						showBigToast("扫描失败重试", 1000);
						return;
					}
				}
				btn_dizhiyapinsaomiao.setText("重扫");
			}

			break;
		case R.id.tv_dizhiyapinsousuo:
			getRenWuInfo();
			break;
		case R.id.ql_ruku_update:// 刷新数据
			getRenWuInfo();
			break;
		default:
			break;
		}
	}

	/***
	 * 装袋网络请求
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
					// json转成list
					if ("00".equals(result)) {
						Log.e(TAG, "result" + result);
						handler.sendEmptyMessage(9);
					} else {
						Log.e(TAG, "result" + result);
						handler.sendEmptyMessage(4);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "SocketTimeout异常" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer异常" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception异常" + e);
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
				Log.e(TAG, "测试TExtview====" + allpagers + unscanpagers);

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
						upnumber = dizhiyapin_tv.getText().toString();// 得到值传给后台变量
						for (Map.Entry<String, Object> entry : mapUnpacketorg.entrySet()) {

							// 在组件获取值 进行哪来和集合进行对比
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

			case 4: /// 失败
				dialogfa = new Dialog(DiZhiYaPinActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(DiZhiYaPinActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("装袋失败" + result);
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
//            				 getRenWuInfo();
						// 提交成功后清除
						tv_resultsaomiao.setText("");
					}
				});
				dialogfa.show();

				break;
			case 9:/// 项目中的交接 成功

				dialog = new Dialog(DiZhiYaPinActivity.this);
				LayoutInflater inflater = LayoutInflater.from(DiZhiYaPinActivity.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("装袋成功");
				dialog.setCancelable(false);
				dialog.setContentView(v);
				but.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						getRenWuInfo();
						// 提交成功后清除
						tv_resultsaomiao.setText("");
						kaihuren.setText("");
						kaiwupin.setText("");
						dizhiyapin_tv.setText("");
						Log.e(TAG, "+++++++++++" + unscanpagers);
						if (unscanpagers.equals("1")) {
							manageractivity.getRuning().runding(DiZhiYaPinActivity.this, "即将跳转");
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
	 * 自定义 toast， 增大字体
	 *
	 * @param info     提示信息
	 * @param duration 显示时间，0：短时间，1：长时间
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
//        outDialog.builder().setTitle("提交成功")
//                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() { // 确定
//            @Override
//            public void onClick(View view) {
//                outDialog.dismiss();
//                getRenWuInfo();
//                //提交成功后清除
//            	tv_resultsaomiao.setText("");
//            	kaihuren.setText("");
//            	kaiwupin.setText("");
//            	dizhiyapintv.setText("");
//            	Log.e(TAG,"dizhilistinfo..size()===="+dizhilistinfo.size());
//            	 if(dizhilistinfo.size()==0){
//     	        	zhuangdai_btn.setText("完成");
//             		if(zhuangdai_btn.getText().equals("完成")){
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
//        outDialogfaile.builder().setTitle("提交失败"+result1)
//                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() { // 确定
//            @Override
//            public void onClick(View view) {
//                outDialogfaile.dismiss();
//                getRenWuInfo();
//                //提交成功后清除
//            	tv_resultsaomiao.setText("");
//            	kaihuren.setText("");
//            	kaiwupin.setText("");
//            	dizhiyapintv.setText("");
//            	Log.e(TAG,"失败");
//      
//     	        }
//        });
//}

}
