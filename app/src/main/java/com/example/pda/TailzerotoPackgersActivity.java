package com.example.pda;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.handheld.UHF.UhfManager;
import com.poka.device.GPIO;
import hdjc.rfid.operator.RFID_Device;
import org.json.JSONArray;
import org.json.JSONObject;

import a20.cn.uhf.admin.Tools;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;

import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.service.CashToPackgersService;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity.TailPackgerEntityQuanbieXinxi;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity.TailZerotoPackgerTianJiaXianJin;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.entity.TianJiaXianJin;
import com.ljsw.tjbankpda.util.HxbSpinner;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.NumFormat;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.manager.classs.pad.ManagerClass;
import poka_global_constant.GlobalConstant;

/***
 * lc
 * 
 * @author Administrator 有一个实体类是不需要的 装袋数据是清点得到的 然后录入明细 业务需求明确
 *         只要是尾零钞袋就可以提交无论是否为那种券别和版别111105* 钞袋没有残损状态 只符合尾零就可以
 */
public class TailzerotoPackgersActivity extends Activity implements OnClickListener {
	private static final String defaultOptions = "请选择";
	public static TailzerotoPackgersActivity instance = null;
	private static final String TAG = "TailzerotoPackgersActivity";
	private Button tailzmakecard_btn;
	private Button tailztoredadupdata_btn;
	private ListView tailzerotopackgeslistview;// 显示列表数据
	private UhfManager newsendmanager;
	private String epc = "";
	private ManagerClass manager;
	private OnClickListener OnClick1;
	private Spinner tailzerosp_cansun;// spinner

	private String moneyid;
	private String cansunwanczheng;// 残损完整
	private String cansunid = "";
	private String moneyCount = "";
	private String readcount = "";// d\读的金额
	private String readcansun = "";// 残损
	private String readqumbei = "";// 读取券别
//	private powercontrol rFidPowerControl;// yang
	private List<TailZerotoEntity> TailZerotoEntitylist = new ArrayList<TailZerotoEntity>();// 数据源
	private EditText tailzeroet;// 输入数QuanbieXinxiroet;// 变量数据
	private List<TailPackgerEntityQuanbieXinxi> quanbieListxin = new ArrayList<TailPackgerEntityQuanbieXinxi>(); // 创建券别信息集合新版
	private List<QuanbieXinxi> quanbieList = new ArrayList<QuanbieXinxi>(); // 创建券别信息集合
	private List<QuanbieXinxi> banbielist = new ArrayList<QuanbieXinxi>(); // 创建券别信息集合
	private List<QuanbieXinxi> jiazhilist = new ArrayList<QuanbieXinxi>(); // 创建价值信息集合
	private String xianjingMsg;// 现金msg
	private Table[] table3 = new Table[10];// 声明一个数据
	private List<TianJiaXianJin> xianjinlist = new ArrayList<TianJiaXianJin>();
	private List<TailZerotoPackgerTianJiaXianJin> xianjinlisttinajia = new ArrayList<TailZerotoPackgerTianJiaXianJin>();

	private XianJinAdapter adapter;
	private Table[] shangjiaoMark; // 对比信息
	private String showcardid;// 显示的卡号
	// 组件
	private TextView tailzero_readcard; // 显示的卡号
	private ImageView ivblack;
	// private EditText shangjiaoqingfen_edit;// 现金数量
	private TextView tailzero_heji;// 合计
	private String juanbie;// 现金券别

	private LinearLayout tailzerot_spinner_layout, taizero_spinner_layout_zhuangtai; // spinnner 现金券别， 现金的残损

	private HxbSpinner couopnSpinner;
	private HxbSpinner editionSpinner;
	private MySpinner spinner;
	private TextView tailzero_spinner_text, taizero_spinner_text_zhuangtai;// 尾零券别,残损状态
	private Button tailzero_xianjin_tianjia;// 现金添加

	// 声明一一个数组
	private int isok = -1; // 现金的残损状态
	private String[] str_zhuangtai;
	private String zhuangtai;// / 是否为残损
	private double heji_xianjin = 0;
	private String delete = "0";// 是否删除过

	private String updataresult;// 提交数据返回值
	// 接受的数据源
	private LinearLayout tailzerotbanbie_spinner_layout;// 版别下拉框
	private TextView tailzeroto_banbie_spinner_text;// 版别的文字显示
	private String param;// 声明一个请求结果

	private String titlk;

	private TextView tailzert_zhuangtai, tailzert_banbie, tailzert_quanbie;// 读卡状态, 读卡的版别。读卡券别

	private String str_cansun;
	private String str_meid;
	private String str_banbiecash;
	private String str_quanbie;
	// 字母

	private String str_tailzeroet = "";
	private String selectmeid = "";//// 查找的meid
	private String setstate = ""; // 接受判断stat状态

	private Dialog dialogfa;// 失败
	private Dialog dialogforreturnaccountinten;
	private ManagerClass managerClass;
	private Map<String, List<Map<String, String>>> moneyEditionMap = new HashMap<String, List<Map<String, String>>>();

	private String taiZerocodeIdentification;// 检测是否为尾零

	private RFID_Device rfid; // 串口

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	// 适配器
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tailzeroto_packgers);
		managerClass = new ManagerClass();
		loadRfid();
		initView();// 组件初始化
		// getDate();/// 获取数据券别版别 和面值数据
		instance = this;
		manager = new ManagerClass();
		adapter = new XianJinAdapter();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// manager.getAbnormal().remove();
				initView();// 组件初始化
				// TODO Auto-generated method stub

			}

		};

	}

	@Override
	protected void onResume() {
		showcardid = "";

		if (null == newsendmanager) {
			loadRfid();
		}
		getSpinnerData();
		super.onResume();
	}

	private void initView() {
		// shangjiaoqingfen_edit = (EditText)
		// findViewById(R.id.shangjiaoqingfen_edit);

		Log.e(TAG, "data========");
		// TODO Auto-generated method stub
		tailzmakecard_btn = (Button) findViewById(R.id.tailzmakecard_btn);
		ivblack = (ImageView) findViewById(R.id.ta_ql_ruku_back);
		ivblack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TailzerotoPackgersActivity.this.finish();

			}
		});
		tailzmakecard_btn.setOnClickListener(this);

		tailzmakecard_btn.setBackgroundResource(R.drawable.button_gray);
		tailztoredadupdata_btn = (Button) findViewById(R.id.tailztoredadupdata_btn);
		tailztoredadupdata_btn.setOnClickListener(this);
		tailzerotopackgeslistview = (ListView) findViewById(R.id.tailzerotopackgeslistview);
		tailzeroet = (EditText) findViewById(R.id.tailzeroet);
		str_tailzeroet = tailzeroet.getText().toString();

		tailzero_readcard = (TextView) findViewById(R.id.tailzero_readcard);
		tailzero_heji = (TextView) findViewById(R.id.tailzero_heji);// /合计

		tailzerot_spinner_layout = (LinearLayout) findViewById(R.id.tailzeroto_spinner_layout);
		tailzerot_spinner_layout.setOnClickListener(this);
		tailzero_spinner_text = (TextView) findViewById(R.id.tailzeroto_spinner_text);
		tailzero_xianjin_tianjia = (Button) findViewById(R.id.tailzero_xianjin_tianjia);
		tailzero_xianjin_tianjia.setOnClickListener(this);

		taizero_spinner_layout_zhuangtai = (LinearLayout) findViewById(R.id.taizero_spinner_layout_zhuangtai);
		taizero_spinner_layout_zhuangtai.setOnClickListener(this);

		taizero_spinner_text_zhuangtai = (TextView) findViewById(R.id.taizero_spinner_text_zhuangtai);
		tailzero_heji = (TextView) findViewById(R.id.tailzero_heji);

		tailzerotbanbie_spinner_layout = (LinearLayout) findViewById(R.id.tailzerotbanbie_spinner_layout);
		tailzerotbanbie_spinner_layout.setOnClickListener(this);
		tailzeroto_banbie_spinner_text = (TextView) findViewById(R.id.tailzeroto_banbie_spinner_text);
		/// 读卡结果显示
		tailzert_zhuangtai = (TextView) findViewById(R.id.tailzert_zhuangtai);
		tailzert_banbie = (TextView) findViewById(R.id.tailzert_banbie);
		tailzert_quanbie = (TextView) findViewById(R.id.tailzert_quanbie);
	}

	/**
	 * 获取清分登记信息
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tailzmakecard_btn:

			if (null == zhuangtai || null == moneyversion || null == moneyversion) {
				if (null == zhuangtai) {
					showBigToast("残损状态", 400);
					return;
				}
				if (null == moneyversion) {
					showBigToast("版别状态", 400);
					return;
				}
				if (null == juanbie) {
					showBigToast("券别状态", 400);
					return;
				}

			} else {
				tailzert_quanbie.setText("");
				tailzert_banbie.setText("");
				tailzero_readcard.setText("");
				tailzert_zhuangtai.setText("");
				showcardid = "";
				if (isFastClick() != true) {

				} else {
					// 长按后
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					final byte[] accessPassword = Tools.HexString2Bytes("00000000");
					if (accessPassword.length != 4) {
						showBigToast("密码为4个字节", 1000);
						return;
					}
					// 读取数据区数据
					// byte[] data = manager.readFrom6C(membank, addr, length,
					// accessPassword);
					// 目前只操作EPC
					byte[] data = newsendmanager.readFrom6C(1, 2, 6, accessPassword);
					Log.i(TAG, "data========" + data);

					if (data != null && data.length > 1) {
						Log.i(TAG, "data========" + data.length);
						String dataStr = Tools.Bytes2HexString(data, data.length);
						// 钞袋编号规则 ：CD 01 5A 4 190227092020
						// 钞袋 券别是否含有字母，券别 券种 时间（0代表5是否为字母1代表A是否有字母）
						// 钞袋编号规则 ：CD 00 98 4 190227092020
//					钞袋尾零规则修改后

//					CD000554190227093020
//					6768000554190227093020
//					4在显示钞袋号：
//					CD115A419022709302000000
//					676810155641902270930200

						String cdcode = dataStr.substring(0, 4);
						StringBuffer sb = new StringBuffer();
						if (dataStr.length() > 18 && dataStr.length() <= 24) {

//						6768000554190227093020   无字母
							if (cdcode.equals("6768")) {// 无字母
								if ("0".equals(dataStr.substring(4, 5)) && "0".equals(dataStr.substring(5, 6))) {
									taiZerocodeIdentification = dataStr.substring(4, 5);
									sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
									sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));

//								676800055219102709302000
									readcansun = (dataStr.substring(8, 9));// 获取参数的值
									if (readcansun.equals("4") || readcansun.equals("2")) {
										Log.i(TAG, "readcansun*********" + readcansun);

										sb.append(dataStr.substring(4, 21));
										readqumbei = (dataStr.substring(6, 8)).toString();
									}
									showcardid = dataStr;
									Log.d(TAG, "卡" + sb + "===!!!!!==" + dataStr);
									Log.d(TAG, "卡" + sb + "===!!!!!==" + readcansun);
									Log.d(TAG, "卡" + sb + "===!!!!!==" + readqumbei);
//								showcardid=sb.toString();
									handler.sendEmptyMessage(6);
									// 有字母 676810155641902270930200 有字母
								} else if ("0".equals(dataStr.substring(4, 5)) && "1".equals(dataStr.substring(5, 6))) {
									taiZerocodeIdentification = dataStr.substring(4, 5);
									sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
									sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));
									sb.append(dataStr, 4, 7);
									sb.append(dataStr, 7, 9);
									Log.d(TAG, sb + "!!!!!");
									// int
									// readqumbei=((char)Integer.parseInt(dataStr.substring(8,9)));
									readcansun = (dataStr.substring(9, 10));// 获取参数的值
									if (readcansun.equals("4") || readcansun.equals("2")) {
										Log.d(TAG, "readcansun*********" + readcansun);
										readqumbei = (dataStr.substring(6, 9)).toString();
										Log.d(TAG, "readqumbei*********" + readqumbei);
										sb.append(dataStr.substring(9, 24));
										showcardid = dataStr;
										handler.sendEmptyMessage(6);
									} else {
										readcansun = "";
										sb = new StringBuffer("");// 清空字符串拼接
										Log.d(TAG, sb + "===!!!!!==" + "残损状态不正确");
										handler.sendEmptyMessage(7);
									}
									Log.d(TAG, sb + "===!!!!!==" + dataStr);
								} else {
									showcardid = "";
									showBigToast("卡的状态不对" + dataStr, 10);
								}

							}

						}
					} else {
						showcardid = "";
						if (data != null) {
							// tvCardid.append("读数据失败，错误码：" + (data[0] & 0xff) +
							// "\n");
							showBigToast("读数据失败，错误码：" + (data[0] & 0xff) + "\n", 10);
							return;
						}
					}
				}

//			TaiLZeroRedaCard();//  尾零读卡

			}

			break;
		case R.id.tailztoredadupdata_btn:
			if (xianjinlisttinajia.size() != 0) {
				if (null == showcardid || showcardid.equals("")) {
					showBigToast("您需要读卡", 20);
					return;
				} else {
					TailzerotoPackgersupdata();
				}

			} else {
				showBigToast("录入数据不能为空", 400);
			}

			break;

		// 券别
		case R.id.tailzeroto_spinner_layout:
			List<String> couponList = new ArrayList<String>();
			for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
				String coupon = item.getQuanbieName();
				if (!couponList.contains(coupon)) {
					couponList.add(coupon);
				}
			}
			couopnSpinner = new HxbSpinner(this, tailzerot_spinner_layout, tailzero_spinner_text, couponList);
			couopnSpinner.setSpinnerHeight(tailzerot_spinner_layout.getHeight() * 2);
			couopnSpinner.showPopupWindow(tailzerot_spinner_layout);
			if (editionSpinner != null) {
				editionSpinner.clear();
				if (tailzeroto_banbie_spinner_text != null)
					tailzeroto_banbie_spinner_text.setText(defaultOptions);
			}

			/*
			 * titlk = tailzero_spinner_text.getText().toString(); if(!titlk.equals("")){
			 * setbanbie(); //设置版别 }else{ // 提示 }
			 */

			break;
		// 版别选择
		case R.id.tailzerotbanbie_spinner_layout:
			if (null == couopnSpinner || couopnSpinner.equals("")) {
				showBigToast("请选版别", 100);
			} else {

				List<String> editionList = new ArrayList<String>();
				String coupon = couopnSpinner.getChooseText();
				if (null != coupon) {
					for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
						if (coupon.equals(item.getQuanbieName())) {
							editionList.add(item.getEDITION());
						}
					}
				}
				editionSpinner = new HxbSpinner(this, tailzerotbanbie_spinner_layout, tailzeroto_banbie_spinner_text,
						editionList);
				editionSpinner.setSpinnerHeight(tailzerotbanbie_spinner_layout.getHeight() * 2);
				editionSpinner.showPopupWindow(tailzerotbanbie_spinner_layout);
			}
			break;

		case R.id.tailzero_xianjin_tianjia:
			if (null == couopnSpinner || null == couopnSpinner.getChooseText() || null == editionSpinner
					|| null == editionSpinner.getChooseText()) {
				Log.d(TAG, "空的不让选！！！！！！！");
				showBigToast("请先选择版别和券别数据", 400);
				tailzmakecard_btn.setEnabled(false);
				tailzmakecard_btn.setBackgroundResource(R.drawable.buttom_selector_bg);
				return;
			} else {

				addXianJin();
				tailzmakecard_btn.setEnabled(true);
				tailzmakecard_btn.setBackgroundResource(R.drawable.buttom_selector_bg);
			}
			break;

		case R.id.taizero_spinner_layout_zhuangtai:
			spinner = new MySpinner(this, taizero_spinner_layout_zhuangtai, taizero_spinner_text_zhuangtai);
			spinner.setSpinnerHeight(taizero_spinner_layout_zhuangtai.getHeight() * 2);
			spinner.setList(this, str_zhuangtai);
			spinner.showPopupWindow(taizero_spinner_layout_zhuangtai);
			spinner.setList(this, str_zhuangtai, 40);

			break;
		default:
			break;
		}
	}

	/***
	 * 网络请求成功获取后的显示
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();

				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:

				break;
			case 5:
				break;
			case 6:
				tailzero_readcard.setText(showcardid);

				// 获取版别和券别 修改
//			char	readqumbei1	=(char)readqumbei;
//			char	readqumbei1=	(char) Integer.parseInt(readqumbei
//					.substring(0, 3));

				selectdatameid(juanbie, moneyversion);

//				int转char
//				for( int i=0; i<quanbieListxin.size(); i++ ){
//					if(readqumbei.equals(selectmeid)){
//						Log.d(TAG,"readqumbei#########"+readqumbei+"********************&&&&&&"+quanbieListxin.size());
//						str_meid=	TailZerotoEntitylist.get(i).getMEID();
//						str_quanbie=TailZerotoEntitylist.get(i).getMONEYTYPE();
//						str_banbiecash=TailZerotoEntitylist.get(i).getEDITION();
				Log.d(TAG, "查meidreadqumbei==" + str_quanbie);
				Log.d(TAG, "查meidreadqumbei==" + str_banbiecash);
//						str_quanbie=item1.getQuanbieName();
//						str_banbiecash=item1.getEDITION();

//					}
//				}

//				selectcashversion(str_meid);

				if (null != str_banbiecash && (!str_banbiecash.equals(""))) {
					tailzert_quanbie.setText("" + str_quanbie);
					tailzert_banbie.setText("" + str_banbiecash);
				}

				Log.d(TAG, readqumbei + "查meid===!!!!!");
				Log.d(TAG, str_banbiecash + "查meid===!!!!!");
				Log.d(TAG, str_quanbie + "查meid===!!!!!");
				Log.d(TAG, readcansun + "查meid===readcansun");
				if (readcansun.equals("4")) {
					tailzert_zhuangtai.setText("残损");
				} else if (readcansun.equals("2")) {
					tailzert_zhuangtai.setText("完整");
//				} 
//				else  if(readcansun.equals("6")){
					// tv_redcashcansun.setText(" ");
//					 tailzert_zhuangtai.setText("半损");
					break;
				} else {
					showBigToast("残损状态不正确", 400);
				}
				break;

			case 7:
				// readcash_qunabeibu.setText("非法券别");
				showBigToast("非法券别", 400);
				break;

			case 8:// 显示读到卡号

				tailzero_readcard.setText(showcardid);
				break;

			case 998:// 提交失败显示dialog
				dialogfa = new Dialog(TailzerotoPackgersActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(TailzerotoPackgersActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("提交失败+您的卡号不能为空或者录入信息不全");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
					}

				});
				dialogfa.show();
//            	  更改显示的弹窗  需要测试
//            	managerClass.getResultmsg().resultmsg(null, "提交数据失败",false);

				break;

			case 10:
				showBigToast("数据提交成功", 400);

				dialogforreturnaccountinten = new Dialog(TailzerotoPackgersActivity.this);
				LayoutInflater inflaterforreturnaccountinten = LayoutInflater.from(TailzerotoPackgersActivity.this);
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

						}

					});
				}
				dialogforreturnaccountinten.show();
				break;

			default:
				break;
			}
		}

	};

	public static void main(String[] args) {
		System.out.println((char) 70);
	}

	public void showBigToast(String info, int duration) {

		Toast toast = new Toast(TailzerotoPackgersActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(TailzerotoPackgersActivity.this);
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

	public static final int MIN_CLICK_DELAY_TIME = 1500;
	private static long lastClickTime;

	public static boolean isFastClick() {
		boolean flag = false;
		long curClickTime = System.currentTimeMillis();
		if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
			flag = true;
		}
		lastClickTime = curClickTime;
		return flag;

	}

	private void getdata(String cansunwanczhegn) {
		if (cansunwanczhegn.equals("完整")) {
			cansunid = "2";
		} else if (cansunwanczhegn.equals("残损")) {
			cansunid = "4";
		} else {
			showBigToast("残损状态不正确", 400);
		}

	}

	// 适配器
	class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

		public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
			}
			// 此处设置spinner的字体大小 和文字颜色
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLUE);
			tv.setTextSize(40);
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
			}

			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLUE);
			tv.setTextSize(30);
			return convertView;
		}

		private class SpinnerAdapterquanzhong extends ArrayAdapter<String> {
			Context context;
			String[] items = new String[] {};

			public SpinnerAdapterquanzhong(final Context context, final int textViewResourceId,
					final String[] objects) {
				super(context, textViewResourceId, objects);
				this.items = objects;
				this.context = context;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(context);
					convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
				}
				// 此处设置spinner的字体大小 和文字颜色
				TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
				tv.setText(items[position]);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(Color.BLUE);
				tv.setTextSize(40);
				return convertView;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(context);
					convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
				}

				TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
				tv.setText(items[position]);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(Color.BLUE);
				tv.setTextSize(30);
				return convertView;
			}

		}
	}

	/**
	 * 合计
	 */
	public void Heji() {

		heji_xianjin = 0;
		if (xianjinlisttinajia.size() != 0) {

			for (int i = 0; i < xianjinlisttinajia.size(); i++) {
				int count = Integer.parseInt(xianjinlisttinajia.get(i).getCount().trim());
				double quanJiazhi = Double.parseDouble(xianjinlisttinajia.get(i).getQuanJiazhi().trim());
				double canshuJiazhi = Double.parseDouble(xianjinlisttinajia.get(i).getCanshunJiazhi().trim());
				int zhuangtai = Integer.parseInt(xianjinlisttinajia.get(i).getZhuangtai().trim());

				switch (zhuangtai) {
				case 0: // 全额
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 1: // 全损
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 2: // 半损
					heji_xianjin += (double) (canshuJiazhi * count);
					break;
				}
			}
		}
		tailzero_heji.setText(new NumFormat().format(heji_xianjin + ""));
	}

	class XianJinAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(TailzerotoPackgersActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return xianjinlisttinajia.size();
		}

		@Override
		public Object getItem(int arg0) {
			return xianjinlisttinajia.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = lf.inflate(R.layout.adapter_tailzerot_packger_tianjia, null);
				view = new ViewHodler();
				view.banbie = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv0);
				view.juanbie = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv1);
				view.zhuangtai = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv2);
				view.shuliang = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv3);
				view.shanchu = (Button) arg1.findViewById(R.id.tailzerotada_title_button);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			TailZerotoPackgerTianJiaXianJin iObj = xianjinlisttinajia.get(position);
			view.banbie.setText(iObj.getCashbanbie());// 券别
			view.juanbie.setText(iObj.getJuanbie());

			String state = iObj.getZhuangtai();
			if (state.equals("0")) {
				view.zhuangtai.setText("全额");
				setstate = "2";
			} else if (state.equals("1")) {
				view.zhuangtai.setText("全损");
				setstate = "4";
			} else if (state.equals("2")) {
				view.zhuangtai.setText("半损");
				setstate = "4";
			}
			// view.zhuangtai.setText(xianjinlist.get(position).getZhuangtai());
			view.shuliang.setText(xianjinlisttinajia.get(position).getCount());
			view.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					xianjinlisttinajia.remove(position);
					delete = "2"; // 现金被删除过
					Heji();
					adapter.notifyDataSetChanged();
					new TurnListviewHeight(tailzerotopackgeslistview);
					// if (xianjinlist.size() > 0) {
					// qingdfen_ok.setEnabled(true);
					// qingdfen_ok
					// .setBackgroundResource(R.drawable.buttom_selector_bg);
					// lin_lloutgo.setVisibility(View.GONE);
					//
					// } else if (xianjinlist.size() == 0) {
					// qingdfen_ok.setEnabled(false);
					// qingdfen_ok
					// .setBackgroundResource(R.drawable.button_gray);
					// lin_lloutgo.setVisibility(View.GONE);
					//
					// Log.e("数据不匹配", "数据不匹配");
					//
					// }
				}
			});
			return arg1;
		}
	}

	public static class ViewHodler {
		TextView juanbie, zhuangtai, shuliang, banbie;
		Button shanchu;
	}

	@Override
	protected void onPause() {
		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.DISABLE_IO);
		super.onPause();
	}

	public void loadRfid() {

		getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);
//		// 串口加载文件
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================

		epc = getIntent().getStringExtra("epc");
		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		newsendmanager = UhfManager.getInstance();
		if (newsendmanager == null) {
			showBigToast("串口初始化失败", 1000);
			return;
		}
	}

	/**
	 * 从服务器获取券别信息
	 * 
	 * @author yuyunheng
	 */
	private class GetQuanbieXinxi implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = create.obtainMessage();
			try {
				param = new CashToPackgersService().TailCoupon_getMoneyEditionList();
				Gson gson = new Gson();
				TailZerotoEntity[] mTailZerotoEntity = gson.fromJson(param, TailZerotoEntity[].class);
				for (int i = 0; i < mTailZerotoEntity.length; i++) {
					TailZerotoEntitylist = Arrays.asList(mTailZerotoEntity);
				}
				if (TailZerotoEntitylist != null && !TailZerotoEntitylist.equals("")) {
					msg.obj = param;
					msg.what = 1;
				} else {
					msg.what = 3; // 获取券别信息失败
				}
			} catch (SocketTimeoutException ee) {
				// TODO: handle exception
				msg.what = 2;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 3; // 获取券别信息失败
			}

			create.sendMessage(msg); // 发送消息
		}

	}

	private List<String> quanbieIds = new ArrayList<String>();
	private List<String> quanbieNames = new ArrayList<String>();;
	private List<String> quanjiazhis = new ArrayList<String>();;
	private List<String> canshunjiazhis = new ArrayList<String>();;

	private Map<String, String> mapbanbie = new HashMap<String, String>();
	private Map<String, Map<String, String>> maplistquanbie = new HashMap<String, Map<String, String>>();// 版别
	private Map<String, Map<String, String>> mapbanbieid = new HashMap<String, Map<String, String>>(); // 存储版别
	private Map<String, String> mapjiazhi = new HashMap<String, String>(); // 存储价值

	private Handler create = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			manager.getRuning().remove(); // 关闭弹窗
			switch (msg.what) {
			case 1: // 正常获取
				// 设置券别信息
				str_zhuangtai = new String[] { "完整券", "半损券", "全损券" };
				String quanbieXinxi = msg.obj.toString();
				System.out.println("获取的券别信息为：" + quanbieXinxi);
				Table[] table = Table.doParse(quanbieXinxi);

				for (int i = 0; i < TailZerotoEntitylist.size(); i++) {

					Map<String, String> val = new HashMap<String, String>();

					val.put("moneytype", TailZerotoEntitylist.get(i).getMONEYTYPE());
					val.put("meid", TailZerotoEntitylist.get(i).getMEID());
					val.put("parvalue", TailZerotoEntitylist.get(i).getPARVALUE());
					val.put("eidtion", TailZerotoEntitylist.get(i).getEDITION());
					val.put("lossvalue", TailZerotoEntitylist.get(i).getLOSSVALUE());
					mapbanbieid.put(TailZerotoEntitylist.get(i).getMEID(), val);

				}
				// 版别 banbielist
				for (Entry<String, Map<String, String>> str : mapbanbieid.entrySet()) {
					TailPackgerEntityQuanbieXinxi tpexx = new TailPackgerEntityQuanbieXinxi();
					Map<String, String> val = str.getValue();
					tpexx.setCanshunJiazhi(val.get("lossvalue"));
					tpexx.setEDITION(val.get("eidtion"));
					tpexx.setQuanbieId(val.get("meid"));
					tpexx.setQuanbieName(val.get("moneytype"));
					tpexx.setQuanJiazhi(val.get("parvalue"));
					quanbieListxin.add(tpexx);

				}

				break;

			case 2: // 连接超时
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // 点击重新获取
					}
				});
				break;

			case 3: // 获取失败
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "券别信息获取失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // 点击重新获取
					}
				});
				break;

			}
		}
	};

	private Handler timeoutHandle = new Handler() {// 连接超时handler
		public void handleMessage(Message msg) {
			manager.getRuning().remove();
			if (msg.what == 00) {

				System.out.println("我是提交超时的时候---");
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						manager.getRuning().runding(TailzerotoPackgersActivity.this, "提交中...");
						// submit();
					}
				});
			}
			if (msg.what == 10) {

				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "网络连接失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						manager.getRuning().runding(TailzerotoPackgersActivity.this, "提交中...");
						// submit();

					}
				});
			}
			if (msg.what == 01) {
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						manager.getAbnormal().remove();
						manager.getRuning().runding(TailzerotoPackgersActivity.this, "数据加载中...");
						getSpinnerData();
					}
				});
			}
			if (msg.what == 11) {
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "网络连接失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						manager.getAbnormal().remove();
						manager.getRuning().runding(TailzerotoPackgersActivity.this, "数据加载中...");
						getSpinnerData();
					}
				});
			}

			if (msg.what == 12) {
				manager.getAbnormal().timeout(TailzerotoPackgersActivity.this, "没有任务", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						// manager.getRuning().runding(
						// ShangJiaoQingFen_o_qf.this, "数据加载中...");
						// getDate();
					}
				});
			}
		};
	};
	private Handler okHandle = new Handler() {// 数据上传成功handler
		public void handleMessage(Message msg) {
			if (msg.what == 9) {
				manager.getRuning().remove();
				System.out.println("------------------------------页面跳转");
				getSpinnerData();/// 网络请求
			}

		};
	};

	/**
	 * 添加现金
	 * 
	 * @author lc
	 * 
	 */
	String moneyversion = "";

	public void addXianJin() {
		String xjcount = tailzeroet.getText().toString();

		if (null == couopnSpinner.getChooseText()) {

			manager.getResultmsg().resultmsg(this, "请选择券别类型", false);
			return;
		} else {
			juanbie = couopnSpinner.getChooseText();
			moneyversion = editionSpinner.getChooseText();
		}

		if (juanbie.equals(defaultOptions)) {
			manager.getResultmsg().resultmsg(this, "请选择券别类型", false);
			return;
		}
		if (null == moneyversion) {

			manager.getResultmsg().resultmsg(this, "请选择版别", false);
		} else {

			if (moneyversion.equals(defaultOptions)) {
				manager.getResultmsg().resultmsg(this, "请选择版别", false);
				return;
			}
		}

		zhuangtai = taizero_spinner_text_zhuangtai.getText().toString();
		if (TextUtils.isEmpty(zhuangtai) || zhuangtai.equals(defaultOptions)) {
			manager.getResultmsg().resultmsg(this, "请选择状态", false);
			return;

		}
		if (TextUtils.isEmpty(xjcount)) {
			manager.getResultmsg().resultmsg(this, "请输入数量", false);
			return;
		}
		// 将残损状态名称改为残损状态id
		if (!"".equals(zhuangtai) && null != zhuangtai) {
			if (zhuangtai.equals("完整券")) {
				zhuangtai = "0";
			} else if (zhuangtai.equals("半损券")) {
				zhuangtai = "2";
			} else if (zhuangtai.equals("全损券")) {
				zhuangtai = "1";
			}
		}
		TailPackgerEntityQuanbieXinxi choose = this.getTailPackgerEntityQuanbieXinxi(juanbie, moneyversion);

		if (choose != null) {
			System.out.println("现金集合开始添加-->" + zhuangtai);
			TailZerotoPackgerTianJiaXianJin model = this.chooseExists(choose.getQuanbieId(), zhuangtai);
			if (model != null) {
				model.setCount(String.valueOf(Integer.parseInt(xjcount) + Integer.parseInt(model.getCount())));
			} else {
				model = new TailZerotoPackgerTianJiaXianJin();
				model.setCanshunJiazhi(choose.getCanshunJiazhi());
				model.setCash_type(choose.getQuanbieId());
				model.setCashbanbie(moneyversion);
				model.setCount(xjcount);
				model.setJuanbie(juanbie);
				model.setQuanJiazhi(choose.getQuanJiazhi());
				model.setZhuangtai(zhuangtai);
				xianjinlisttinajia.add(model);
			}
		} else {
			int listcount = Integer.parseInt(xianjinlisttinajia.get(isok).getCount());
			int xjshuliang = Integer.parseInt(xjcount);
			int count = listcount + xjshuliang;
			xianjinlisttinajia.get(isok).setCount(count + "");
			isok = -1;
		}

		Heji();
		tailzerotopackgeslistview.setAdapter(adapter);
		new TurnListviewHeight(tailzerotopackgeslistview);
		System.out.println("现金集合的数量----->");
		for (int i = 0; i < xianjinlisttinajia.size(); i++) {
			System.out
					.println(xianjinlisttinajia.get(i).getJuanbie() + "----->>" + xianjinlisttinajia.get(i).getCount());
		}

	}

	/**
	 * 是否已经添加过
	 * 
	 * @param meid
	 * @return
	 */
	private TailZerotoPackgerTianJiaXianJin chooseExists(String meid, String state) {
		for (TailZerotoPackgerTianJiaXianJin item : xianjinlisttinajia) {
			if (meid.equals(item.getCash_type()) && state.equals(item.getZhuangtai())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 根据券别、版别得到唯一的那一条记录
	 * 
	 * @param moneyType
	 * @param edition
	 * @return
	 */
	private TailPackgerEntityQuanbieXinxi getTailPackgerEntityQuanbieXinxi(String moneyType, String edition) {
		if ((null == edition || null == moneyType) || moneyType.equals("") || edition.equals("")) {

			manager.getResultmsg().resultmsg(this, "请选择版别", false);

		} else {
			if (moneyType.equals("")) {
				showBigToast("请选择" + moneyType, 100);
			} else if (edition.equals("") || null == edition) {
				showBigToast("请选择" + edition, 100);
			}

			for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
				if (moneyType.equals(item.getQuanbieName()) && edition.equals(item.getEDITION()))
					return item;
			}
		}
		return null;
	}

	/****
	 * 20191017 创建标签规则扫描
	 */
	// CD 00 01 4 190227093020000
	// 标签号规则CD 装袋 00是否包含字母 01 版别 4 残损 后面添加时间和补零位

	/*****
	 * 提交数据 packegerid CD 00 01 4 190227093020000 list（）
	 * 
	 */
	String cvoun = ""; // 合计
	String userId = ""; // 用户id
	String stockCodeListJson = "";// / 需要的json 数据
	String packgerid = "";

	/***
	 * 参数1 ： 钞袋编号 参数2： 填写的尾零数据（json）
	 * 格式：[{"meid":"","total":"总数","lossstate":"完损状态，0完整 1残损"},...] 尾零的钞袋不存在完整和残损
	 * 里面的明细是要的
	 */

//	提交数据限定 所读取meid 和选中meid一致时可提交
	private void TailzerotoPackgersupdata() {
//		solutionUPList();///// 处理上传的数据
//		new Thread() {
		JSONArray array = new JSONArray();
		try {
			// Log.e(TAG,"Tasknumber======"+Tasknumber);
			for (TailZerotoPackgerTianJiaXianJin item : xianjinlisttinajia) {
				JSONObject json = new JSONObject();
				json.put("meid", item.getCash_type());
				json.put("total", item.getCount());
				json.put("lossstate", item.getZhuangtai());

				double amount = 0.0;
				int count = Integer.parseInt(item.getCount());
				if ("1".equals(item.getZhuangtai())) {// 半损券
					amount = count * Double.parseDouble(item.getCanshunJiazhi());
				} else if ("0".equals(item.getZhuangtai())) {
					amount = count * Double.parseDouble(item.getQuanJiazhi());
				} else if ("2".equals(item.getZhuangtai())) {
					amount = (count * Double.parseDouble(item.getQuanJiazhi()) * 0.5);
				}
				json.put("amount", amount);
				array.put(json);
			}

			// Log.e(TAG,"======"+cvoun+"===="+returnaccountinteninfolist.size()+"==="+userId);
			// 先判断是否为null 直接发送失败 失败的dialog提示
			if (array.length() == 0) {
				handler.sendEmptyMessage(998);
			} else {

				Log.e(TAG, "showcardid" + "====" + showcardid);
				Log.e(TAG, "测试!!!!" + taiZerocodeIdentification + "===" + taiZerocodeIdentification + setstate + "c=="
						+ readcansun);
				if (null != taiZerocodeIdentification || !taiZerocodeIdentification.equals("")) {
					if (taiZerocodeIdentification.equals("0")) {
						if (null == showcardid) {
							showBigToast("没有卡号", 100);
						} else {
							Intent i = new Intent(TailzerotoPackgersActivity.this, Cash_TailZeroAllctivity.class);
							i.putExtra("cardidta", showcardid);/// 不带字母
							i.putExtra("array", array.toString());
							startActivity(i);
						}
					} else {
						showBigToast("请选检查" + "读取数据和选择数据一致版别券别残损状态", 100);
					}

				} else {
					showBigToast("请读卡", 100);
				}

				// 这里序要重新写
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/***
	 * 处理要上传 json 数据
	 */

//	public void  solutionUPList(){
//		for (int i = 0; i < xianjinlisttinajia.size(); i++) {
//			JSONObject solutionUPo = new JSONObject();
//			try {
//				solutionUPo.put( "meid", xianjinlisttinajia.get(i).getCashbanbie());
//				solutionUPo.put( "total" , xianjinlisttinajia.get(i).getCount());
//				solutionUPo.put( "amount" , "10");
//				solutionUPo.put( "lossstate" , xianjinlisttinajia.get(i).getZhuangtai());
//				array.put(solutionUPo);
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//		
//	}

	/// 版别数据添加
	public void setbanbie() {
		for (int i = 0; i < TailZerotoEntitylist.size(); i++) {
			if (titlk.equals(TailZerotoEntitylist.get(i).getMONEYTYPE())) {
				mapbanbie.put(TailZerotoEntitylist.get(i).getMEID(), TailZerotoEntitylist.get(i).getEDITION() + "");
			}

			mapjiazhi.put(TailZerotoEntitylist.get(i).getMEID(), TailZerotoEntitylist.get(i).getPARVALUE());
		}
		// 创建券别信息集合
		// quanbiemap.add(maplistquanbie);
		// banbiemap.add(mapbanbie);

		// 版别 banbielist
		QuanbieXinxi xinxib;
		for (Entry<String, String> str : mapbanbie.entrySet()) {
			xinxib = new QuanbieXinxi();
			// 思路 // 获取券别 随后获取版别号 随后记录MEID
			xinxib.setQuanbieName(str.getValue());
			xinxib.setQuanJiazhi(str.getKey());
			xinxib.setCanshunJiazhi("0");
			banbielist.add(xinxib);
		}
	}

	/***
	 * 通过meid查询 EDITION 显示的时候用到
	 * 
	 * @param str_banbie lc19 10.30
	 */
	private TailZerotoEntity selectcashversion(String str_banbie) {
		Log.e(TAG, "=========" + str_banbie + TailZerotoEntitylist.size());

		if (str_banbie.equals("") || null == str_banbie) {
		} else {
			for (int i = 0; i < TailZerotoEntitylist.size(); i++) {
				Log.e(TAG, "=========" + str_banbie + TailZerotoEntitylist.size());
				if (str_banbie.equals(TailZerotoEntitylist.get(i).getMEID())) {
					return TailZerotoEntitylist.get(i);
				}
			}
			return null;

		}
		return null;

	}

	/**
	 * 获取券别数据
	 */
	public void getSpinnerData() {
		manager.getRuning().runding(TailzerotoPackgersActivity.this, "数据加载中...");
		// 获取券别信息
		Thread thread = new Thread(new GetQuanbieXinxi());
		thread.start();
	}

	/// 数据格式
//	{"MONEYTYPE":"纸100元（5套）","MEID":"01","PARVALUE":100.00,"EDITION":2015}
//	{"MONEYTYPE":"纸100元（5套）","MEID":"02","PARVALUE":100.00,"EDITION":2010}
//
//	{
//		 "纸100元（5套）" : [{"MONEYTYPE":"纸100元（5套）","MEID":"01","PARVALUE":100.00,"EDITION":2015},{"MONEYTYPE":"纸100元（5套）","MEID":"02","PARVALUE":100.00,"EDITION":2010}]
//		 "纸50元（5套）" : [{"MONEYTYPE":"纸100元（5套）","MEID":"01","PARVALUE":100.00,"EDITION":2015},{"MONEYTYPE":"纸100元（5套）","MEID":"02","PARVALUE":100.00,"EDITION":2010}]
//	}
	public void set() {

	}

	private String selectdatameid(String juanbie2, String moneyversion2) {
		Log.d(TAG, "juanbie2==" + juanbie2);
		Log.d(TAG, "moneyversion2==" + moneyversion2);
		for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
			String coupon = item.getQuanbieName();
			if (juanbie2.equals(coupon)) {
				for (TailPackgerEntityQuanbieXinxi item1 : quanbieListxin) {
					Log.d(TAG, "juanbie2=======" + juanbie2);
					Log.d(TAG, "moneyversion2========" + moneyversion2);
					if (moneyversion2.equals(item1.getEDITION())) {
						Log.d(TAG, "juanbie2==***********************" + readqumbei + "=====" + item1.getQuanbieId());
//对硬币进行处理带字母
						StringBuffer sbuffer = new StringBuffer();
						// 166
						if (readqumbei.length() > 2) {
							String str_codeq = readqumbei;
							sbuffer.append((str_codeq.substring(0, 1)));
							Log.d(TAG, "**********==" + sbuffer);
							Log.d(TAG, "**********==!!!!" + (str_codeq.substring(1, 3)));
							sbuffer.append((char) Integer.parseInt(str_codeq.substring(1, 3)));
							Log.d(TAG, "**********==" + sbuffer);
							String gets = sbuffer.toString();
							Log.d(TAG, "sbuffer==*****" + gets + "=====yyyy=====" + item1.getQuanbieId());
							if (gets.equals(item1.getQuanbieId())) {
								str_quanbie = item1.getQuanbieName();
								str_banbiecash = item1.getEDITION();
								selectmeid = readqumbei;
								Log.d(TAG, "readqumbei   " + readqumbei);
								return selectmeid;
							} else {
								Log.d(TAG, "啥也不做   ");
								continue;
							}

						} else {

							if (readqumbei.equals(item1.getQuanbieId())) {
								str_quanbie = item1.getQuanbieName();
								str_banbiecash = item1.getEDITION();
								Log.d(TAG, "juanbie2=           " + juanbie2);
								Log.d(TAG, "moneyversion2=   " + moneyversion2);
								selectmeid = item1.getQuanbieId();
								Log.d(TAG, "selectmeid=   " + item1.getQuanbieId());
								return selectmeid;
							}

						}
					}
				}

			}
		}
		return null;
	}

}
