package com.ljsw.tjbankpad.baggingin.activity.cash;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.handheld.UHF.UhfManager;
import com.ljsw.tjbankpad.baggingin.activity.cash.entity.BaggingForCashEntity;
import com.ljsw.tjbankpad.baggingin.activity.cash.service.BaggingForCashService;
import com.ljsw.tjbankpda.db.entity.Qingfenxianjin;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.MySpinner.SpinnerItemClickImp;
import com.manager.classs.pad.ManagerClass;

import a20.android_serialport_api.SerialPort;
import a20.cn.uhf.admin.Tools;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;
import com.poka.device.GPIO;
import hdjc.rfid.operator.RFID_Device;
import poka_global_constant.GlobalConstant;

/***
 * 2018_11_22
 * 
 * @author Administrator 现金过门市天线时 制卡 廉超 20190418再次更改 2019.10.25
 *         制卡修改需要获取数据和加入版别的类型
 * 
 *         制卡修改10.31日
 * 
 */
public class BaggingActivitySend extends FragmentActivity implements OnClickListener, SpinnerItemClickImp {
	private static final String TAG = "BaggingActivity";
	private Button btn_acmakecard, btn_acreadcard, updata_btn;// 读卡和制卡按钮,数据提交
	private TextView karid, tv_readcard; // 卡号
	private Spinner ba_Spinner, bc_spinneridtype, bc_spkindof; // 尾零

	private EditText ba_EdiText;
	private UhfManager manager;
	private SerialPort uhfSerialPort;// 超高频串口
	String epc = "";
	String strmoneyquanzhong;// 券种
	String paperorcoin;// 全的类型
	String strmoneyqunbe;// 券别
	private String rfidquanzhong;// 写入标签的券种 名称反了
	private String rfidpapercoin; // 写入标签的纸币硬币
	private String rfidquanbie;// 写入标签券别
	private int flag = 0;
	private Button ql_ruku_updatebtnzhika;
	private ImageView ql_ruku_back;
	private String zhengze = "" + "^[1-9]\\d{0,2}$";// /正则表达式 允许数字限制3位

	private TextView bc_textviewidtype, bc_tvspcoinorpaper, bc_tvwkindof, zrkztv;// 券别 券种 纸币和硬币
	private List<Qingfenxianjin> ltQuanbie = Mapplication.getApplication().boxLtXianjing;// 存放券别信息
	private Map<String, QuanbieXinxi> quanbieXinxi; // 券别信息
	private MySpinner spinner;
	private ManagerClass manageryuan;
	// 网络获取实体类

	private BaggingForCashEntity bfce = new BaggingForCashEntity();

	private String resultmoneytype = "";// 网络结果获取券别
	private List<BaggingForCashEntity> BaggingList = new ArrayList<BaggingForCashEntity>();
	private List<BaggingForCashEntity> baggingArray = new ArrayList<BaggingForCashEntity>();
	private List<String> BaggingListmoneytype = new ArrayList<String>();
	private List<String> baggingListmoneyid = new ArrayList<String>();

	private OnClickListener OnClick1;
	private Spinner sp_quabzhongbuckc;
	private Spinner sp_qunabeibuckc;
	private Spinner sp_sp_banbieckc;// 版别
	private SpinnerAdapter mSpinnerAdapter, mSpinnerAdapterquanzhong, mSpinnnerAdaCoinVersion, mspinndrTailzero;// 版别
	private String monetype = "";
	private String moneyid = "";
	private String cansunwanczhegn = "";
	private String cansunid = "";
	private String[] array41 = new String[] { "完整", "残损", "半损" };
	private String[] array4 = new String[] { "完整", "残损" };
//	private String[] array4 = new String[] { "全额", "半损","全损" };
	private String[] array5 = new String[] { "整钞", "尾零" };
	private Dialog dialog1;
	private Dialog dialog;
	private Dialog undialog;
	private LinearLayout lin_banbie;
	private Spinner mSpinner, sp_weiling;
	private Spinner sp_banbieckc;// 版本201910.10.21
	private Map<String, List<Map<String, String>>> moneyEditionMap = new HashMap<String, List<Map<String, String>>>();
	// 存放一个数据集合
	private Map<String, List<Map<String, String>>> moneyEditionMapstr = new HashMap<String, List<Map<String, String>>>();

	private JsonArray baggingarray = new JsonArray();
	private String TailZeromeid = "";
	private String iffalseTailZero = "";// / 是否尾零0，1
	private String reusltCardnumber = "";// 网络返回的
	private String str_weiling = "";
	private String TailZerorecive;// 尾零接受
	// 残损
	private String iscansunshow = "";
	private String WriterData = "";/// 提交数据变量
	// 新增  读卡的操作20200825
	private  Button bagginsend_copy_btn;//复制卡片时按钮
	private  String  str_remCardno="";// 存放后台返给的生成卡数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baggingactivitvsend);// baggingactivitvsend
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================

		epc = getIntent().getStringExtra("epc");
		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}

		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		manageryuan.getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);
		loadData();
		/*****
		 * 点击获取网络请求
		 */
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manageryuan.getAbnormal().remove();
				loadData();
			}
		};

	}

	String sss = "";

	/****
	 * 网络获取数据 所有的券别
	 */
	String[] array2 = baggingListmoneyid.toArray(new String[0]);// /接受券别id
	String[] array3 = BaggingListmoneytype.toArray(new String[0]);// /接受券别的对应类型

	private void loadData() {

		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 仿写数据源格式{"MONEYTYPE":"尾零）","MONEYID":"98"},

					// 用户账号
					BaggingList.clear();
					resultmoneytype = new BaggingForCashService().getAllMoneyType_getMoneyEditionList();
					if (!resultmoneytype.equals("") || resultmoneytype == null) {
						Gson gson = new Gson();
						Log.e(TAG, "测试数据源=====" + resultmoneytype.toString());
						BaggingForCashEntity[] baggingForCashEntity = gson.fromJson(resultmoneytype,
								BaggingForCashEntity[].class);

						BaggingForCashEntity bce = new BaggingForCashEntity();
						baggingArray.clear();
						baggingArray = Arrays.asList(baggingForCashEntity);
						List arrList = new ArrayList(baggingArray);

						BaggingList.addAll(arrList);

						// private String MONEYTYPE; // 纸币名称
						// private String MEID; // 版别的id
						// private String PARVALUE; /// 面值的金额
						// private String EDITION; // 版别年份
						// private String LOSSVALUE;// 半残值

						baggingListmoneyid.clear();
						BaggingListmoneytype.clear();

						// 向集合中分别添加券别券种
						for (BaggingForCashEntity item : baggingForCashEntity) {
							String moneytypeweiling = item.getMONEYTYPE();
							String monestr = item.getEDITION();
							Map<String, String> baggmap = new HashMap<String, String>();
							baggmap.put("edition", item.getEDITION());
							baggmap.put("lossvalue", item.getLOSSVALUE());
							baggmap.put("meid", item.getMEID());
							baggmap.put("paryvalue", item.getPARVALUE());

							if (moneyEditionMap.containsKey(item.getMONEYTYPE())) {
								List<Map<String, String>> listmap = moneyEditionMap.get(moneytypeweiling);

								listmap.add(baggmap);

							} else {
								List<Map<String, String>> list = new ArrayList<Map<String, String>>();
								list.add(baggmap);
								moneyEditionMap.put(moneytypeweiling, list);
							}

							if (moneyEditionMapstr.containsValue(item.getEDITION())) {
								List<Map<String, String>> listmapedition = moneyEditionMapstr.get(item.getEDITION());
								Log.e(TAG, "找张" + moneyEditionMapstr.get(item.getEDITION()));
								listmapedition.add(baggmap);

							} else {
								List<Map<String, String>> list = new ArrayList<Map<String, String>>();
								list.add(baggmap);
								moneyEditionMapstr.put(item.getEDITION(), list);

							}
						}

						// 文字版别 "纸100元（5套）
						Set<String> moneySet = moneyEditionMap.keySet();
						array2 = new String[moneySet.size()];
						moneySet.toArray(array2);

						// 版别"EDITION":2015
						Set<String> moneysetbanbie = moneyEditionMapstr.keySet();
						array3 = new String[moneysetbanbie.size()];

						Log.e(TAG, "AAAAA" + array3.length);
						// moneysetbanbie.toArray(ar)
						moneysetbanbie.toArray(array3);
						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e);
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e);
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
				manageryuan.getRuning().remove();
				manageryuan.getAbnormal().timeout(BaggingActivitySend.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manageryuan.getRuning().remove();
				manageryuan.getAbnormal().timeout(BaggingActivitySend.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				// manageryuan.getRuning().remove();
				for (int i = 0; i < array2.length; i++) {
					Log.d(TAG, "券别" + array2[i]);
				}
				for (int i = 0; i < array3.length; i++) {
					Log.d(TAG, "版别:::" + array3[i]);
				}

				Log.d(TAG, array3.length + "长度");
				mSpinnerAdapterquanzhong = new SpinnerAdapter(BaggingActivitySend.this,
						android.R.layout.simple_spinner_dropdown_item, array2);
				mSpinnerAdapterquanzhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_qunabeibuckc.setAdapter(mSpinnerAdapterquanzhong);
				sp_qunabeibuckc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

						// monetype = array3[pos];
						monetype = array2[pos];
						Toast.makeText(BaggingActivitySend.this, "" + monetype, Toast.LENGTH_SHORT).show();

						List<Map<String, String>> list = moneyEditionMap.get(monetype);
						final String[] editions = new String[list.size()];
						for (int i = 0; i < list.size(); i++) {
							editions[i] = list.get(i).get("edition");
						}
						// 设置现金版别选择
						mSpinnnerAdaCoinVersion = new SpinnerAdapter(BaggingActivitySend.this,
								android.R.layout.simple_spinner_dropdown_item, editions);
						mSpinnnerAdaCoinVersion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sp_sp_banbieckc.setAdapter(mSpinnnerAdaCoinVersion);
						sp_sp_banbieckc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

								System.out.println(pos);
								moneyid = editions[pos];
								System.out.println(moneyid);
								Toast.makeText(BaggingActivitySend.this, "" + moneyid, Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// Another interface callback
							}
						});
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});

				// 设置尾零

				mspinndrTailzero = new SpinnerAdapter(BaggingActivitySend.this,
						android.R.layout.simple_spinner_dropdown_item, array5);
				mspinndrTailzero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_weiling.setAdapter(mspinndrTailzero);
				sp_weiling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						Log.d(TAG, array5[pos] + ")))))))");
						Log.d(TAG, cansunwanczhegn);
						str_weiling = array5[pos];/// 正钞还是尾零
						setTailZero(str_weiling);

						if (array5[pos].equals("尾零")) {
							mSpinnerAdapter = new SpinnerAdapter(BaggingActivitySend.this,
									android.R.layout.simple_spinner_dropdown_item, array4);
							mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sp_quabzhongbuckc.setAdapter(mSpinnerAdapter);
							sp_quabzhongbuckc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
									Log.d(TAG, array41[pos] + ")))))))");
									Log.d(TAG, cansunwanczhegn);
									cansunwanczhegn = array41[pos];
									getdata(cansunwanczhegn);/// 获取残损完整券

									Toast.makeText(getApplicationContext(), array4[pos], Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onNothingSelected(AdapterView<?> parent) {
									// Another interface callback
								}
							});

						} else {

							mSpinnerAdapter = new SpinnerAdapter(BaggingActivitySend.this,
									android.R.layout.simple_spinner_dropdown_item, array4);
							mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							sp_quabzhongbuckc.setAdapter(mSpinnerAdapter);
							sp_quabzhongbuckc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
									Log.d(TAG, array4[pos] + ")))))))");
									Log.d(TAG, cansunwanczhegn);
									cansunwanczhegn = array4[pos];
									getdata(cansunwanczhegn);/// 获取残损完整券

									Toast.makeText(getApplicationContext(), array4[pos], Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onNothingSelected(AdapterView<?> parent) {
									// Another interface callback
								}
							});

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});

				// 设置残损
//				mSpinnerAdapter = new SpinnerAdapter(BaggingActivitySend.this,
//						android.R.layout.simple_spinner_dropdown_item, array4);
//				mSpinnerAdapter
//						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//				sp_quabzhongbuckc.setAdapter(mSpinnerAdapter);
//				sp_quabzhongbuckc
//						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//							@Override
//							public void onItemSelected(AdapterView<?> parent,
//									View view, int pos, long id) {
//								Log.d(TAG, array4[pos] + ")))))))");
//								Log.d(TAG, cansunwanczhegn);
//								cansunwanczhegn = array4[pos];
//								getdata(cansunwanczhegn);///获取残损完整券
//								
//								Toast.makeText(getApplicationContext(), array4[pos],
//										Toast.LENGTH_SHORT).show();	
//
//							}
//
//							@Override
//							public void onNothingSelected(AdapterView<?> parent) {
//								// Another interface callback
//							}
//						});

				break;
			case 3:
				manageryuan.getRuning().remove();
				manageryuan.getAbnormal().timeout(BaggingActivitySend.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manageryuan.getAbnormal().remove();
					}
				});
				break;

			case 6:/// 写入逻辑
				for (int i = 0; i < 5; i++) {
					final byte[] accessPassword = Tools.HexString2Bytes("00000000");
					if (accessPassword.length != 4) {
						Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
						return;
					}

					StringBuffer sb = new StringBuffer();
					if (reusltCardnumber.equals("") || reusltCardnumber == null) {
						return;
					} else {
						String editWrite = reusltCardnumber;
						Log.e(TAG, "========！！！！！！！！！！" + editWrite);
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
						if (writeData.length() % 4 != 0) {
							Toast.makeText(getApplicationContext(), "写入数据的长度以字为单位，1word = 2bytes", Toast.LENGTH_SHORT)
									.show();
						}
						byte[] dataBytes = Tools.HexString2Bytes(writeData);
						boolean writeFlag = manager.writeTo6C(accessPassword, 1, 2, dataBytes.length / 2, dataBytes);
						if (writeFlag) {
							String s = new String(reusltCardnumber);
							String cdcode = s.substring(0, 4);
							StringBuffer sb1 = new StringBuffer();
							if (cdcode.equals("6768")) {
								String strresultmoneytypeb = "";
								strresultmoneytypeb = reusltCardnumber;
								Log.e(TAG, "!!!!!!!!!!!!!!!" + strresultmoneytypeb);
								// 676800014191027180357000
								if ("0".equals(strresultmoneytypeb.substring(4, 5))
										&& "0".equals(strresultmoneytypeb.substring(5, 6))) {
									// 是否是残损=

//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(0, 2)));
//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(2, 4)));
									sb1.append(strresultmoneytypeb, 0, 9);
									iscansunshow = strresultmoneytypeb.substring(8, 9);

									sb1.append(strresultmoneytypeb.substring(9, 24));

									// 尾零带字母
									// 6768010A4191027180357000
									// 6768010564191027180357000
								} else if ("0".equals(strresultmoneytypeb.substring(4, 5))
										&& "1".equals(strresultmoneytypeb.substring(5, 6))) {

//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(0, 2)));
//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(2, 4)));
									sb1.append(strresultmoneytypeb, 0, 4);
									sb1.append(strresultmoneytypeb, 4, 7);
//									sb1.append((char) Integer
//											.parseInt(strresultmoneytypeb
//													.substring(7, 9)));
									sb1.append(strresultmoneytypeb, 7, 9);
									iscansunshow = strresultmoneytypeb.substring(8, 9);
									sb1.append(strresultmoneytypeb.substring(9, 24));

									// 不尾零不带字母
									// 676810014191027180357000
								} else if ("1".equals(strresultmoneytypeb.substring(4, 5))
										&& "0".equals(strresultmoneytypeb.substring(5, 6))) {
//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(0, 2)));
//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(2, 4)));
									sb1.append(strresultmoneytypeb, 0, 4);
									iscansunshow = strresultmoneytypeb.substring(8, 9);
									sb1.append(strresultmoneytypeb.substring(4, 24));

									Log.e(TAG, "!!!!!!!!!!" + sb1);
									// 不尾零带带字母
									// 6768110B4191027180357000
								} else if ("1".equals(strresultmoneytypeb.substring(4, 5))
										&& "1".equals(strresultmoneytypeb.substring(5, 6))) {
//									sb1.append((char) Integer.parseInt(strresultmoneytypeb.substring(0, 2)));
									sb1.append(strresultmoneytypeb, 0, 4);
									sb1.append(strresultmoneytypeb, 4, 7);
									iscansunshow = strresultmoneytypeb.substring(9, 10);

									Log.e(TAG, "!!!!!!!!!!" + iscansunshow);
									sb1.append((strresultmoneytypeb.substring(7, 9)));
									Log.e(TAG, "!!!***" + (char) Integer.parseInt(strresultmoneytypeb.substring(7, 9)));
									sb1.append(strresultmoneytypeb.substring(9, 24));
									sb1.toString();
								}
							}
							Log.e(TAG, "成功了写===" + sb1);
							karid.setText(sb1);

							WriterData = sb1 + "";
							handler.sendEmptyMessage(7);
							break;
						} else {
							karid.setText("制卡失败" + "");
							Log.e(TAG, "失败了");
							handler.sendEmptyMessage(8);
							continue;
						}
					}
				}

				break;

			case 7:
				dialog = new Dialog(BaggingActivitySend.this);
				LayoutInflater inflater = LayoutInflater.from(BaggingActivitySend.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("制卡成功");
				dialog.setCancelable(false);
				dialog.setContentView(v);
				but.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();

					}
				});

				dialog.show();
				break;
			case 10:
				Toast.makeText(BaggingActivitySend.this, "提交制卡信息失败", Toast.LENGTH_SHORT).show();

				break;
			case 11:
				dialog1 = new Dialog(BaggingActivitySend.this);
				LayoutInflater inflater1 = LayoutInflater.from(BaggingActivitySend.this);
				View v1 = inflater1.inflate(R.layout.dialog_success, null);
				Button but1 = (Button) v1.findViewById(R.id.success);
				but1.setText("提交信息成功");

				dialog1.setCancelable(false);
				dialog1.setContentView(v1);
				but1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog1.dismiss();

					}
				});
				dialog1.show();

			default:
				break;
			}
		}

	};

//	 "全额", "全损","半损"
	private void getdata(String cansunwanczhegn) {
		if (null != TailZerorecive && TailZerorecive.equals("0")) {
			if (cansunwanczhegn.equals("完整")) {
				cansunid = "2";
			} else if (cansunwanczhegn.equals("残损")) {
				cansunid = "4";
			}
		} else if (null != TailZerorecive && TailZerorecive.equals("1")) {
			if (cansunwanczhegn.equals("完整")) {
				cansunid = "2";
			} else if (cansunwanczhegn.equals("残损")) {
				cansunid = "4";

			}
		}

	}

	private void setTailZero(String str_weiling) {
		if (str_weiling.equals("尾零")) {
			Log.i(TAG, "data========" + str_weiling);
			TailZerorecive = "0";
			Toast.makeText(BaggingActivitySend.this, "尾零===" + TailZerorecive, 100).show();
		} else if (str_weiling.equals("整钞")) {
			TailZerorecive = "1";
			Log.i(TAG, "data========" + str_weiling);
			Toast.makeText(BaggingActivitySend.this, "不尾零" + TailZerorecive, 100).show();
		}
	}

	private void initView() {

		bagginsend_copy_btn=(Button) findViewById(R.id.bagginsend_copy_btn);//  复制时的按钮
		bagginsend_copy_btn.setOnClickListener(this);
		sp_sp_banbieckc = (Spinner) findViewById(R.id.sp_banbieckc);
		tv_readcard = (TextView) findViewById(R.id.tv_readcard);
		btn_acmakecard = (Button) findViewById(R.id.makecard_btn);
		btn_acmakecard.setOnClickListener(this);
		btn_acreadcard = (Button) findViewById(R.id.redad_btn);
		btn_acreadcard.setOnClickListener(this);
		karid = (TextView) findViewById(R.id.bc_karid);
		ql_ruku_updatebtnzhika = (Button) findViewById(R.id.ql_ruku_updatebtnzhika);
		ql_ruku_updatebtnzhika.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isFastClick() == true) {
					loadData();
				}

			}
		});
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaggingActivitySend.this.finish();

			}
		});
		sp_quabzhongbuckc = (Spinner) findViewById(R.id.sp_quabzhongbuckc);
		sp_qunabeibuckc = (Spinner) findViewById(R.id.sp_qunabeibuckc);

		// 版别的选项
		lin_banbie = (LinearLayout) findViewById(R.id.bagsendbanbielin);
		sp_banbieckc = (Spinner) findViewById(R.id.sp_banbieckc);

		manageryuan = new ManagerClass();
		manageryuan.getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);

		updata_btn = (Button) findViewById(R.id.updata_btn);// / 制卡的数据提交
		updata_btn.setOnClickListener(this);
		sp_weiling = (Spinner) findViewById(R.id.sp_weiling);
	}

	/***
	 * 方法的封装 避免代码重复 实现spinner 传值
	 * 
	 * @param view
	 * @param tv
	 * @param data
	 */
	public void viewSpinnerShow(View view, TextView tv, String[] data) {
		spinner = new MySpinner(BaggingActivitySend.this, view, tv);
		spinner.setSpinnerItemClick(this);
		spinner.setSpinnerHeight(view.getHeight() * 2);
		spinner.setList(BaggingActivitySend.this, data);
		spinner.showPopupWindow(view);

		// spinner.setList(BaggingActivitySend.this, str_juanzhong, 40);

	}

	@Override
	protected void onPause() {
		manageryuan.getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.DISABLE_IO);
		super.onPause();
	}

	private void poweron() {
//		// TODO Auto-generated method stub
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================
		manageryuan.getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		poweron();// 串口初始化
		if (manager == null) {
			poweron();
		}
		switch (arg0.getId()) {
		case R.id.makecard_btn:
			if (isFastClick()) {
				tv_readcard.setText("");
				requestMadecar();
			}

			break;
		case R.id.redad_btn:
			if (isFastClick() != true) {
			} else {
				// 长按后
				tv_readcard.setText("");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final byte[] accessPassword = Tools.HexString2Bytes("00000000");
				if (accessPassword.length != 4) {
					Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
					return;
				}

				// 读取数据区数据
				// byte[] data = manager.readFrom6C(membank, addr, length,
				// accessPassword);
				// 目前只操作EPC
				byte[] data = manager.readFrom6C(1, 2, 6, accessPassword);
				Log.i(TAG, "data========" + data);

				if (data != null && data.length > 1) {
					Log.i(TAG, "data========" + data.length);
					String dataStr = Tools.Bytes2HexString(data, data.length);

					// 钞袋编号规则 ：CD 01 5A 4 190227092020
					// 钞袋 券别是否含有字母，券别 券种 时间
					// CD005A419022709302000000
					// 67680055641902270930200
					// 4在显示钞袋号：
					// CD115A419022709302000000
					// 67681055641902270930200

					String cdcode = dataStr.substring(0, 4);
					StringBuffer sb = new StringBuffer();
					sb.setLength(0);
					if (cdcode.equals("6768")) {

						if ("0".equals(dataStr.substring(4, 5)) && "0".equals(dataStr.substring(5, 6))) {
							// TailZero=sb.substring(4,5); 返现数据包
//							sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
//							sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));

							sb.append(dataStr.substring(0, 24));
							// 带字母尾零
							// CD115A419022709302000000
						} else if ("0".equals(dataStr.substring(4, 5)) && "1".equals(dataStr.substring(5, 6))) {
//							sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
//							sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));
//							
//							sb.append(dataStr, 4, 7);
//							sb.append((char) Integer
//									.parseInt(dataStr
//											.substring(7, 9)));
//							sb.append(dataStr.substring(9,
//									24));

							sb.append((dataStr.substring(0, 2)));
							sb.append((dataStr.substring(2, 4)));

							sb.append(dataStr, 4, 7);
							sb.append(dataStr.substring(7, 9));
							sb.append(dataStr.substring(9, 24));

							// 不带字母不尾零的情况 6768000554190227093020
						} else if ("1".equals(dataStr.substring(4, 5)) && "0".equals(dataStr.substring(5, 6))) {
//							sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
//							sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));
							sb.append(dataStr.substring(0, 24));

						} else if ("1".equals(dataStr.substring(4, 5)) && "1".equals(dataStr.substring(5, 6))) {
//							sb.append((char) Integer.parseInt(dataStr.substring(0, 2)));
//							sb.append((char) Integer.parseInt(dataStr.substring(2, 4)));
//							sb.append(dataStr, 4, 7);
//							sb.append((char) Integer.parseInt(dataStr
//											.substring(7, 9)));
//							Log.e(TAG,"!!!***"+(char) Integer
//									.parseInt(dataStr
//											.substring(7, 9)));
//							sb.append(dataStr.substring(10,
//									24));
//							

							sb.append((dataStr.substring(0, 2)));
							sb.append((dataStr.substring(2, 4)));
							sb.append(dataStr, 4, 7);
							sb.append((dataStr.substring(7, 9)));
							Log.e(TAG, "!!!***" + (char) Integer.parseInt(dataStr.substring(7, 9)));
							sb.append(dataStr.substring(9, 24));
						}
						sb.toString();
						tv_readcard.append(sb + "\n");
						tv_readcard.setText(sb+ "\n");
						Log.i(TAG, "成功了读" + sb);
						handler.sendEmptyMessage(8);
						break;
					}

				} else {
					if (data != null) {
						tv_readcard.append("读数据失败，错误码：" + (data[0] & 0xff) + "\n");
						return;
					}
				}
				break;

			}

			break;
		case R.id.updata_btn:
			updatabyMCardData();// /提交制卡信息
			break;
			case R.id. bagginsend_copy_btn:///  执行复制操作
				CopyAction();
				break;
		default:
			break;
		}
	}


	/****
	 * 执行复制操作
	 */
	private void CopyAction() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(6);
	}
	/***
	 * 网络请求制卡
	 * 
	 * noneyid(miei)
	 * 
	 * 是否残损 第一个参数应该是 meid，第二个参数是残损状态，第三个参数是是否尾零
	 * 
	 */

	private void requestMadecar() {
		selectmeid(monetype, moneyid);
		if (null == monetype || monetype.equals("") || null == moneyid || null == TailZeromeid
				|| ("").equals(TailZeromeid)) {

			Log.e(TAG, "测试数据源=====" + monetype + "=" + moneyid + "==" + "===" + TailZeromeid);

			Toast.makeText(BaggingActivitySend.this, "券别和版别不能空，请重新选择", 400).show();
		} else {

			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						// 用户账号
						Log.d(TAG, "!!!!" + TailZeromeid + "==" + cansunid + "=" + TailZerorecive);
						reusltCardnumber = new BaggingForCashService().generateTag(TailZeromeid, cansunid,
								TailZerorecive);
						if (!reusltCardnumber.equals("") || reusltCardnumber == null) {
							Gson gson = new Gson();
							Log.e(TAG, "测试数据源=====" + reusltCardnumber.toString());
							handler.sendEmptyMessage(6);
						} else {
							handler.sendEmptyMessage(3);
						}
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						Log.e(TAG, "**===" + e);
						handler.sendEmptyMessage(0);
					} catch (NullPointerException e) {
						e.printStackTrace();
						Log.e(TAG, "**===" + e);
						handler.sendEmptyMessage(3);
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "***===" + e);
						handler.sendEmptyMessage(1);
					}
				}

			}.start();

		}
	}

	/***
	 * 网络请求制卡成功后的数据提交 lc 201910.25
	 */
	String updatacard = "";
	String CDcard = "";
	String Userid = "";

	private void updatabyMCardData() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					Log.d(TAG, "!!!!" + WriterData + Userid);
					CDcard = WriterData;

					Userid = GApplication.user.getYonghuZhanghao();
					if (!CDcard.equals("") || Userid == null) {
						Gson gson = new Gson();
						Log.e(TAG, "测试数据源=====" + resultmoneytype.toString());
						updatacard = new BaggingForCashService().updata_create(CDcard, Userid);
						if (null != updatacard && updatacard.equals("00")) {
							handler.sendEmptyMessage(11);
							CDcard = "";
						} else {
							handler.sendEmptyMessage(10);
							CDcard = "";
						}

					} else {
						handler.sendEmptyMessage(10);
						CDcard = "";
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e);
					handler.sendEmptyMessage(10);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e);
					handler.sendEmptyMessage(10);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e);
					handler.sendEmptyMessage(10);
				}
			}

		}.start();

	}

	/***
	 * popwowind 的点击实现接口方法 在此处 判断 用户点击的是那个条目 残损还是 券别 popwowind 包含着一个listview
	 * listview 点击条目
	 */
	@Override
	public void OnItemClicK(String result) {
		Log.i("", "----------result：" + result);
		if (MySpinner.str.equals("纸100元（5套）")) {
			rfidquanzhong = "51";
		} else if (MySpinner.str.equals("纸50元（5套）")) {
			rfidquanzhong = "52";
		} else if (MySpinner.str.equals("纸20元（5套）")) {
			rfidquanzhong = "53";
		} else if (MySpinner.str.equals("纸10元（5套）")) {
			rfidquanzhong = "54";
		} else if (MySpinner.str.equals("纸5元（5套）")) {
			rfidquanzhong = "55";

		} else if (MySpinner.str.equals("纸1元（5套）")) {
			rfidquanzhong = "56";

		} else if (MySpinner.str.equals("纸100元（05版）")) {
			rfidquanzhong = "5A";

		} else if (MySpinner.str.equals("纸50元（05版）")) {
			rfidquanzhong = "5B";

		} else if (MySpinner.str.equals("纸20元（05版）")) {
			rfidquanzhong = "5C";

		} else if (MySpinner.str.equals("纸10元（05版新包装）")) {
			rfidquanzhong = "5D";
		} else if (MySpinner.str.equals("尾零")) {
			rfidquanzhong = "98";

		} else if (MySpinner.str.equals("残损")) {
			strmoneyqunbe = "2";
		} else if (MySpinner.str.equals("完整")) {
			strmoneyqunbe = "4";
		} else if (MySpinner.str.equals("纸币")) {
			paperorcoin = "1";
		} else if (MySpinner.str.equals("硬币")) {
			paperorcoin = "0";
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

	/***
	 * 遍历数据查找meid 传后台
	 * 
	 * @param monetype
	 * @param moneyid
	 */
	public String selectmeid(String monetype, String moneyid) {

		if (monetype.equals("") || moneyid.equals("")) {
			Toast.makeText(BaggingActivitySend.this, "券别" + monetype + "版别" + moneyid + "不能为空", 400).show();
		} else {
			for (Map.Entry<String, List<Map<String, String>>> item : moneyEditionMap.entrySet()) {
				if (monetype.equals(item.getKey())) {
					for (Map<String, String> map : item.getValue()) {
						if (moneyid.equals(map.get("edition"))) {

							Log.e(TAG, "meid======" + map.get("meid"));
							TailZeromeid = map.get("meid");
							break;
						} else {
							Log.e(TAG, "meid=====" + map.get("meid"));
							TailZeromeid = null;
						}
					}
				}
			}

		}
		// 没找到号码
		return TailZeromeid;

	}
}
