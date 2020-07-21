package com.ljsw.tjbankpad.baggingin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.pda.R;
import com.handheld.UHF.UhfManager;
import com.ljsw.tjbankpad.baggingin.activity.adapter.SpinnnerAdapter;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangZHongkongFragment;
import com.ljsw.tjbankpda.qf.fragment.ZaiRuKuZhuangDai;
import com.ljsw.tjbankpda.util.MySpinner;

import a20.android_serialport_api.SerialPort;
import a20.cn.uhf.admin.Tools;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;
import com.poka.device.GPIO;
import hdjc.rfid.operator.RFID_Device;
import poka_global_constant.GlobalConstant;

/***
 * 库管 入门式天线之前做的操作 库管员进行装袋操作 当前的页面为了装袋时 时生成标签19位 暂定 用户选择券别和券种 lc 2018-9-13
 * * @author Administrator
 * 
 */
public class BaggingActivity extends FragmentActivity implements OnClickListener {
	private Button btn_bc_cash, btn_bcimportnull, btn_ac_yuliu;// 现金和重要空白凭证，预留
	private Button btn_acmakecard, btn_acreadcard;// 读卡和制卡
	private TextView ba_resultnumber, karid, textView1, textView2, readinfo;
	private Spinner ba_Spinner, bc_spinneridtype, bc_spkindof, bc_spcoinorpaper;
	private EditText ba_EdiText;
	private List<String> quanzhonglist = new ArrayList<String>();// 券种
	private List<String> cahstypelist = new ArrayList<String>();// 券别
	private List<String> cashcoinorpaperlist = new ArrayList<String>();// 纸币硬币
	private SpinnnerAdapter mSpquanzhongAdapter;// 券种
	private SpinnnerAdapter quabbieSpinnnerAdapter;// 券别
	private SpinnnerAdapter cpSpinnnerAdapter;// 纸币硬币
	private SpinnnerAdapter cpSpinnnerAdapter1;// 纸币硬币
//	private powercontrol rFidPowerControl;// yang
	private UhfManager manager;
	private SerialPort uhfSerialPort;// 超高频串口
	String epc = "";
	String strmoneytype;// 全的类型
	String paperorcoin;// 全的类型
	String strmoneykindof;// 全的类型
	private String innumber; // 钱的输入值
	private LinearLayout kj_layout, putong_layout;
	private int flag = 0;

	private String zhengze = "" + "^[1-9]\\d{0,2}$";// /正则表达式 允许数字限制3位

	FrameLayout fl;
	private FragmentManager fm;
	// Handler handler1;
	public static final int TEST = 0;
	public static final int TEST50 = 1;
	public static final int TEST10 = 2;
	public static final int BACANSUN = 800;
	public static final int BAWANZENG = 801;
	public static final int BAZHIBI = 802;
	public static final int BAYOINGBI = 803;
	private static final String TAG = "BaggingActivity";

	public TextView tv = null;
	public Map<String, Fragment> fragments = new HashMap<String, Fragment>();
	private Map<String, QuanbieXinxi> quanbieXinxi = new HashMap<String, QuanbieXinxi>();
	private Context mContext;

	private String strmoneytype1;
	private String strmoneyquanzhong;
	private String paperorcointrslate;

	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BaggingActivity.TEST:
				String params = msg.obj.toString();
				Log.i("------params====", params);
				strmoneytype1 = params;
				break;
			case BaggingActivity.TEST50:
				String params50 = msg.obj.toString();
				Log.i("------params====", params50);
				strmoneytype1 = params50;
				break;
			case BaggingActivity.TEST10:
				String params10 = msg.obj.toString();
				Log.i("------params====", params10);
				strmoneytype1 = params10;
				break;
			case BaggingActivity.BAWANZENG:
				String paramswanzheng = msg.obj.toString();
				Log.i("------params====", paramswanzheng);
				strmoneyquanzhong = paramswanzheng;
				break;
			case BaggingActivity.BACANSUN:
				break;
			case BaggingActivity.BAZHIBI:
				break;
			case BaggingActivity.BAYOINGBI:
				break;
			}
		}
	};

	private RFID_Device rfid; // 串口

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bagging);
//		// ================================打开RFID电源控制
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================
		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);

		epc = getIntent().getStringExtra("epc");
		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}
		poweron();
		initView();
//		loadData();
		select();
		if (handler1 != null) {
			// 选中那个选项
			MySpinner sp = new MySpinner();
			sp.setHandler(handler1);
//			sp.sendyibaiyuan();
//			sp.sendwushhiyuan();
//			sp.sendcoin();
//			sp.sendpaper();
//			sp.sendcansun();
//			sp.sendwanzheng();

		}

	}

	public BaggingActivity() {
		super();
	}

	private void select() {
		// bc_spinneridtype
		// .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> adapterView,
		// View view, int i, long l) {
		// strmoneytype = cahstypelist.get(i).toString();
		// if(strmoneytype.equals("纸100元（5套）")){
		// strmoneytype1="51";
		// }else if(strmoneytype.equals("纸50元（5套")){
		// strmoneytype1="52";
		// }else if(strmoneytype.equals("纸50元（5套)")){
		// strmoneytype1="53";
		// }
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> adapterView) {
		// }
		// });
		// bc_spkindof
		// .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> adapterView,
		// View view, int i, long l) {
		// strmoneykindof = quanzhonglist.get(i).toString();
		// if(strmoneykindof.equals("残损")){
		// strmoneykindof1="4";
		// }else if(strmoneykindof.equals("残损")){
		// strmoneykindof1="2";
		// }
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> adapterView) {
		// }
		// });
		// bc_spcoinorpaper
		// .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> adapterView,
		// View view, int i, long l) {
		// paperorcoin=quanzhonglist.get(i).toString();
		// if(paperorcoin.equals("纸币")){
		// paperorcointrslate="1";
		// }else if(paperorcoin.equals("硬币")){
		// paperorcointrslate="0";
		// }}
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> adapterView) {
		// }
		// });
	}

	@Override
	protected void onResume() {
		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);
		super.onResume();
	}

	@Override
	protected void onPause() {
		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.DISABLE_IO);
		super.onPause();
	}

	private void poweron() {
//		// TODO Auto-generated method stub
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================
		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);
		epc = getIntent().getStringExtra("epc");
		;

		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}
	}

	/***
	 * 数据源
	 */
//	private void loadData() {
//		
//		handler1test = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case TEST:
//					String params = msg.obj.toString();
//					Log.e("params====", params);
//					strmoneytype1 = params;
//					Log.e("测试传值1====", strmoneytype1);
//					break;
//				case TEST50:
//					String params999 = msg.obj.toString();
//					if (!params999.isEmpty()) {
//						strmoneytype1 = params999;
//					}
//					strmoneytype1 = params999;
//					Log.e("测试传值1====", strmoneytype1.toString());
//					break;
//				case TEST10:
//					String params998 = msg.obj.toString();
//
//					if (!params998.isEmpty()) {
//						strmoneytype1 = params998;
//					}
//					Log.e("测试传值1====", strmoneytype1.toString());
//
//					break;
//				case 997:
//					String params997 = msg.obj.toString();
//					strmoneytype1 = params997;
//					break;
//
//				case BACANSUN: // 残损
//					String paramscan = msg.obj.toString();
//					Log.e("测试paramscan====",paramscan);
//					if (paramscan != null||!paramscan.equals("")) {
//						Log.e("测试传值paramscan==",paramscan);
//						strmoneykindof1 = paramscan;
//					}
//
//					Log.e("测试传值1====", strmoneykindof1.toString());
//					break;
//				case BAWANZENG: // 完整
//					String paramswan = msg.obj.toString();
//					strmoneykindof1 = paramswan;
//					break;
//				case BAZHIBI: // 纸币
//					String paramszhibi = msg.obj.toString();
//					paperorcointrslate = paramszhibi;
//					Log.e("测试传值1====", paperorcointrslate.toString());
//					break;
//				case BAYOINGBI: // 硬币
//					String paramsyingbi = msg.obj.toString();
//					paperorcointrslate = paramsyingbi;
//					break;
//
//				}
//			};
//		};
//		
//	}

	/***
	 * 组件初始化
	 */
	private void initView() {
		// TODO Auto-generated method stub
		// btnmakenumer = (Button) findViewById(R.id.bc_btn_makesure);
		// btnmakenumer.setOnClickListener(this);
		// ba_resultnumber = (TextView) findViewById(R.id.bc_resultnumber);
		// bc_spkindof= (Spinner) findViewById(R.id.bc_spkindof);
		// bc_spcoinorpaper= (Spinner) findViewById(R.id.bc_spcoinorpaper);
		// bc_spinneridtype = (Spinner) findViewById(R.id.bc_spinneridtype);
		// ba_EdiText = (EditText) findViewById(R.id.bc_et);
		// karid = (TextView) findViewById(R.id.karid);
		// kj_layout=(LinearLayout) findViewById(R.id.kj_layout);
		// kj_layout.setOnClickListener(this);
		// putong_layout=(LinearLayout) findViewById(R.id.zhongkong_layout);
		// putong_layout.setOnClickListener(this);
		//
		// readinfo=findViewById(R.id.readinfo);
		btn_acreadcard = (Button) findViewById(R.id.btn_acreadcard);
		btn_acreadcard.setOnClickListener(this);

		btn_ac_yuliu = (Button) findViewById(R.id.btn_ac_yuliu);
		btn_ac_yuliu.setOnClickListener(this);
		btn_bcimportnull = (Button) findViewById(R.id.btn_bcimportnull);
		btn_bcimportnull.setOnClickListener(this);
		btn_bc_cash = (Button) findViewById(R.id.btn_bc_cash);
		btn_bc_cash.setOnClickListener(this);
		btn_acmakecard = (Button) findViewById(R.id.btn_acmakecard);
		btn_acmakecard.setOnClickListener(this);

		if (Mapplication.getApplication().zxLtXianjing.size() == 0) {
			Mapplication.getApplication().IsXianjingOK = true;
		}
		if (Mapplication.getApplication().zxLtZhongkong.size() == 0) {
			Mapplication.getApplication().IsZhongkongOK = true;
		}
		if (Mapplication.getApplication().zxTjDizhi.getWeiZhuang() == 0) {
			Mapplication.getApplication().IsDizhiOK = true;
		}

		fm = getSupportFragmentManager();
		// 当前页面的现金
		btn_bc_cash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!fragments.containsKey("xianjin")) {
					// fragments.put("xianjin", new
					// QinglingZhuangxiangXianjinFragment(quanbieXinxi));
					fragments.put("xianjin", new ZaiRuKuZhuangDai(quanbieXinxi));
				}
				fm.beginTransaction().replace(R.id.fl_qingfen_ac_context, fragments.get("xianjin")).commit();
				btn_bc_cash.setBackgroundResource(R.color.white);
				btn_bcimportnull.setBackgroundResource(R.drawable.btn_bottom_gray);
				btn_ac_yuliu.setBackgroundResource(R.drawable.btn_bottom_gray);
			}
		});
		btn_bcimportnull.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!fragments.containsKey("zhongkong")) {
					fragments.put("zhongkong", new QinglingZhuangxiangZHongkongFragment());
				}
				fm.beginTransaction().replace(R.id.fl_qingfen_zhuangxiang_context, fragments.get("zhongkong")).commit();
				btn_bc_cash.setBackgroundResource(R.drawable.btn_bottom_gray);
				btn_bcimportnull.setBackgroundResource(R.color.white);
				btn_ac_yuliu.setBackgroundResource(R.drawable.btn_bottom_gray);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stubf

		switch (arg0.getId()) {
		// /截取字符串并写入标签
		case R.id.btn_acmakecard:
			// getvalue();
			// ZaiRuKuZhuangDai zrzd = new ZaiRuKuZhuangDai();
			innumber = "200";
			Log.e(TAG, "查看传过来的变量值" + strmoneytype1 + "=券别==" + strmoneyquanzhong + "券种的值==" + paperorcointrslate);
			if (innumber.matches(zhengze)) {
				for (int i = 0; i < 5; i++) {
					final byte[] accessPassword = Tools.HexString2Bytes("00000000");
					if (accessPassword.length != 4) {
						Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
						return;
					}
					StringBuffer sb = new StringBuffer();
					// String editWrite = "90721143000000000000000";
					// String editWrite = "201809145A2000000";
					String edno = "0005";
					String edwritedatas = "180914";
					String edwritequantype = "" + strmoneytype1;// 券别
					String edwritekindof = "" + strmoneyquanzhong;// 残损完整
					String papercoin = "" + paperorcointrslate;// 纸币硬币
					String edwriteinfo = innumber + "00000";// 以万元为结尾
					// String editWrite = "02055A21803091712383";
					// 020518091451400000
					String editWrite = edno + edwritequantype + edwritekindof + edwritedatas + edwriteinfo;
					Log.e(TAG, "========" + editWrite);
					if (editWrite.length() < 24) {
						sb.append(editWrite);
						for (int j = 0; j < 24 - editWrite.length(); j++) {
							sb.append("0");
						}
					} else {
						sb.append(editWrite.substring(0, 24));
					}
					String writeData = sb.toString();
					Log.i("writeData.length()", writeData.length() + "");
					Log.i("writeData", writeData);
					Log.e("11111", "$$$$$$$$$$$$$$$$$$");
					if (writeData.length() % 4 != 0) {
						Toast.makeText(getApplicationContext(), "写入数据的长度以字为单位，1word = 2bytes", Toast.LENGTH_SHORT)
								.show();
					}
					byte[] dataBytes = Tools.HexString2Bytes(writeData);
					boolean writeFlag = manager.writeTo6C(accessPassword, 1, 2, dataBytes.length / 2, dataBytes);
					Log.e(TAG, "$$$$$$$$$$$$$$$$$$2222");
					if (writeFlag) {
						Log.e(TAG, "成功了");
						karid.setText(sb);
						ba_resultnumber.append("写数据成功！" + "\n");

						break;
					} else {
						Log.e(TAG, "失败了");
						ba_resultnumber.append("失败！" + "\n");
						continue;
					}

				}
			} else {
				Toast.makeText(getApplicationContext(), "输入的不正确注意以万元结尾", Toast.LENGTH_SHORT).show();
			}
			break;

		// 读取卡片信息并拆分内容
		case R.id.btn_acreadcard:
			final byte[] accessPassword = Tools.HexString2Bytes("00000000");
			if (accessPassword.length != 4) {
				Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
				return;
			}

			byte[] data = manager.readFrom6C(1, 2, 6, accessPassword);

			if (data != null && data.length > 1) {
				String dataStr = Tools.Bytes2HexString(data, data.length);
				karid.setText(dataStr);
				karid.append("读数据：" + dataStr + "\n");
				break;
			} else {
				if (data != null) {
					karid.append("读数据失败，错误码：" + (data[0] & 0xff) + "\n");
					return;
				}
				karid.append("读数据失败，返回为空" + "\n");
			}
			break;
		default:
			break;
		}

	}

}
