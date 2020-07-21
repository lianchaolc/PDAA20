package com.pda.checksupplement.activity;

import hdjc.rfid.operator.RFID_Device;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.CheckLibraryBoxUntil;
import com.manager.classs.pad.ManagerClass;
import com.pda.checksupplement.entity.ChecklibraryReplenisBaseEntity;
import com.pda.checksupplement.entity.ChelibrarySupplemnetBaseEntity;
import com.pda.checksupplement.entity.GetTypeOfCouponEntity;
import com.pda.checksupplement.entity.getReScanDetailEntity;
import com.pda.checksupplement.service.CheckLibrarySupplementService;

/***
 * 库管员
 * 
 * @author Administrator 盘查补录操作 独立功能 lc 2018_11_29
 * @param <CheChecklibraryReplenishmentActivity>
 */
public class ChecklibraryReplenishmentActivity extends Activity implements OnClickListener {
	protected static final String TAG = "ChecklibraryReplenishmentActivity";
	// 组件
	private TextView CheckLRoprater, CheckLRunreadpackes, CheckLTasknum_tv, CheckLType_tv, Left_tv, right_tv,
			checklibraryno_tv, cointype_tv;// 查库人,,
	// 盘库和查库的任务,查库任务编号
	private ListView rightlist; // listview
	private Button btn_Sure, btn_Cancle;// 确定取消按钮
	private ImageView ql_ruku_back;// 更新按钮
	private Button CheckLR_Updata;// 更新
	// 数据源
	private Spinner mSpinnerui;
	private List<ChecklibraryReplenisBaseEntity> clrbList = new ArrayList<ChecklibraryReplenisBaseEntity>();
	private List<GetTypeOfCouponEntity> quanbie = new ArrayList<GetTypeOfCouponEntity>();// 漏扫的
	private Map<String, String> maplist = new HashMap<String, String>();
	private List<String> unneedScanpakgeList = new ArrayList<String>(); // 后台返给已经扫描的箱子
	// 接受最外层的数据
	private List<String> showarrayList = new ArrayList<String>();// 显示和提交的数据
	private List<ChelibrarySupplemnetBaseEntity> clsList = new ArrayList<ChelibrarySupplemnetBaseEntity>();

	// 券别的数据显示
	private List<String> quanbiespinnerlist = new ArrayList<String>();
	// 实体类
	ChecklibraryReplenisBaseEntity clrb = new ChecklibraryReplenisBaseEntity();
	// 适配器
	private RightAdapter radapter;
	private ManagerClass manager;
	private CheckLibraryBoxUntil getnumber1;// 工具类
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private List<String> copylist = new ArrayList<String>();
	private Dialog dialog;
	private Dialog dialogfa;
	private ManagerClass manageractivity;// /activity提示
	private String MissCount = ""; // 右上角显示需要扫几个箱子
	// 声明适配器

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	private OnClickListener OnClick1;
	// 变量
	private String parms;// 接受网络返回结果
	private String Taskitem = "";// 传的任务号
	private String PackgersCount = "0";
	private String PackgersType = "";
	private String PackgesrErr = "0"; // /箱子数量
	private String taskType = "";
	private Context mContext;
	private String typemoneyselect = "";
	private String typemoneyselect2 = "";
	private String countforTask = "0";
	private String selectquanbie = "";// 券别
	private SpinnerAdapter mSpinnerAdapter;
	String[] array2 = maplist.keySet().toArray(new String[0]);// /接受券别id
	String[] array3 = maplist.values().toArray(new String[0]);// /接受券别的对应类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklibraryreplemis);
		Intent mIntent = getIntent();
		Taskitem = mIntent.getStringExtra("Taskitem");
		taskType = Taskitem.substring(0, 2);
		manageractivity = new ManagerClass();

		initView();
		getnumber1 = new CheckLibraryBoxUntil();
		getnumber1.setHandler(handler);
		mContext = ChecklibraryReplenishmentActivity.this;

	}

	private void initView() {
		CheckLR_Updata = (Button) findViewById(R.id.ql_ruku_update); // / 更新
		CheckLR_Updata.setOnClickListener(this);
		// 当前有网络请求
		checklibraryno_tv = (TextView) findViewById(R.id.checklibraryno_tv);// 任务编号
		checklibraryno_tv.setText("" + Taskitem);
		CheckLRoprater = (TextView) findViewById(R.id.checklibrary_usertv);
		CheckLRoprater.setText("" + GApplication.user.getYonghuZhanghao());// 操作人
		CheckLType_tv = (TextView) findViewById(R.id.checklibrarytype_tv); // 任务类型
		CheckLType_tv.setText(taskType.equals("PK") ? ("盘库") : ("查库"));
		mSpinnerui = (Spinner) findViewById(R.id.checklibrarytasknum_tv);
		btn_Sure = (Button) findViewById(R.id.checklibrarysupplement_btnsure);
		btn_Sure.setOnClickListener(this);
		btn_Sure.setEnabled(false);
		btn_Sure.setBackgroundColor(R.drawable.buttom_selector_bg);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);// /返回
		ql_ruku_back.setOnClickListener(this);
		rightlist = (ListView) findViewById(R.id.clr_right);
		right_tv = (TextView) findViewById(R.id.right_clrtv);
		Left_tv = (TextView) findViewById(R.id.leftclr_tv);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checklibrarysupplement_btnsure:
			updateReScan();// 提交数据
			break;
		case R.id.ql_ruku_update:
			manageractivity.getRuning().runding(this, "正在刷新,请稍后...");
			showarrayList.clear(); // 清除显示的数据长度刷新后一起归零
			unneedScanpakgeList.clear();
			if (o_Application.qinglingruku != null) {
				o_Application.qinglingruku.clear();
			}
			getRfid().close_a20();// 关闭
			if (rfid == null) {
				getRfid().open_a20();
				showarrayList.clear(); // 清除显示的数据长度刷新后一起归零
				o_Application.qinglingruku.clear();
//				radapter.notifyDataSetChanged();
				unneedScanpakgeList.clear();
			} else {
				// 什么也不做

			}
			getReScanDetail();// 更新从新获取网络数据

			break;
		case R.id.ql_ruku_back:
			ChecklibraryReplenishmentActivity.this.finish();// 返回时关闭
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		radapter = new RightAdapter();
		clrbList.clear();
		o_Application.numberlist.clear();
		getReScanDetail();// 查看需要扫的券别1

		// 获取券别类型
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getReScanDetail();
			}

		};
	}

	/***
	 * 查看需要扫的券别
	 */

	public void getReScanDetail() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
//					if (Taskitem.equals("") || Taskitem == null) {
//						return;
//					}
					if (Taskitem.isEmpty()) {
						return;
					}
					String taskNum = Taskitem;
					parms = new CheckLibrarySupplementService().GetTypeOfCoupon(taskNum);
					Log.e(TAG, "网络返回结果：：：：" + parms.toString());
					maplist.clear();
					array3 = maplist.values().toArray(new String[0]);
					if (parms != null) {
						Gson gson = new Gson();
						GetTypeOfCouponEntity[] gtoe = gson.fromJson(parms, GetTypeOfCouponEntity[].class);

						List<GetTypeOfCouponEntity> listoCheck = Arrays.asList(gtoe);
						List arrList = new ArrayList(listoCheck);// 集合添加数据
						for (int i = 0; i < listoCheck.size(); i++) {
							maplist.put(listoCheck.get(i).getMONEYID(), listoCheck.get(i).getMONEYTYPE());
//					
						}
						Log.e(TAG, "quanbiespinnerlist：：：：" + quanbiespinnerlist.size());

						// 键
						Set<String> keySet = maplist.keySet();
						// 值
						Collection<String> values = maplist.values();
						// 视图
						Set<Entry<String, String>> entrySet = maplist.entrySet();

						array2 = maplist.keySet().toArray(new String[maplist.size()]);
						array3 = maplist.values().toArray(new String[maplist.size()]);
						System.out.println("======" + array2[0]);
						System.out.println("======" + array3[0]);
						handlernet.sendEmptyMessage(4);
					} else {
//						handlernet.sendEmptyMessage(3);
					}
					manager.getRuning().remove();
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
//					handlernet.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	private Handler handlernet = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ChecklibraryReplenishmentActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ChecklibraryReplenishmentActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				// getData();
				CheckLType_tv.setText("" + PackgersType);
				CheckLTasknum_tv.setText("" + Taskitem);
				CheckLRunreadpackes.setText(PackgesrErr);// / 错误箱子的数量
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ChecklibraryReplenishmentActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:
				/// 网络获取卷别成后操作

				SpinnerOnclick1();
				manageractivity.getRuning().remove();
				break;

			case 5:
				dialogfa = new Dialog(ChecklibraryReplenishmentActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(ChecklibraryReplenishmentActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("提交失败");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
						handlernet.sendEmptyMessage(9);
					}

				});
				dialogfa.show();
				break;
			case 6:// / 项目中的交接 成功
				dialog = new Dialog(ChecklibraryReplenishmentActivity.this);
				LayoutInflater inflater = LayoutInflater.from(ChecklibraryReplenishmentActivity.this);
				View v = inflater.inflate(R.layout.dialog_success, null);
				Button but = (Button) v.findViewById(R.id.success);
				but.setText("提交成功");
				dialog.setCancelable(false);
				dialog.setContentView(v);
				but.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						showarrayList.clear();
						o_Application.qinglingruku.clear();
						radapter.notifyDataSetChanged();
						btn_Sure.setEnabled(false);
						// 提交成功后清除 1 提交后查看是否还有任务
						getCheckLibrarySupplementTaskList();

					}
				});

				dialog.show();
				break;

			case 7:
				// 网络获取成功
				right_tv.setText("" + showarrayList.size());
				Left_tv.setText("" + MissCount);
				if (showarrayList.size() == 0) {
					radapter.notifyDataSetChanged();
					rightlist.setAdapter(radapter);
				}
				break;

			case 8:
				// 2 选中券别时 券别的类别时显示 网络请求
				right_tv.setText("0");
				getReScanDetail();

				break;
			case 9:
				if (rfid == null) {
					StartScan();
				} else {

					// 用原有线程
				}

				break;
			case 12:
				radapter = new RightAdapter();
				if (showarrayList.size() > 0) {
					btn_Sure.setEnabled(true);
					btn_Sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				radapter.notifyDataSetChanged();
				rightlist.setAdapter(radapter);
				break;
			default:

				break;
			}
		}

	};

	/***
	 * 监听选中哪项
	 */
	public void SpinnerOnclick1() {
		StartScan();
		o_Application.numberlist.clear();
		mSpinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, array3);
		Log.e(TAG, "array3" + "==========" + array3.toString());
		mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerui.setAdapter(mSpinnerAdapter);
		mSpinnerui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				selectquanbie = array3[pos];
				typemoneyselect2 = array2[pos];

				Log.e(TAG, selectquanbie + "===-=====" + typemoneyselect2 + "****" + selectquanbie);
				Toast.makeText(ChecklibraryReplenishmentActivity.this, "" + selectquanbie, Toast.LENGTH_SHORT).show();
				if (!typemoneyselect2.isEmpty()) {
					if (typemoneyselect2.equals(o_Application.quanbie)) {// 查看是否切换了券别 1切换清出2没切换将现有的付给
						Requetnetbymiss();// 网络获取后台的箱子
					} else {
						o_Application.quanbie = typemoneyselect2;
						Requetnetbymiss();// 网络获取后台的箱子 10
						showarrayList.clear();
						o_Application.numberlist.clear();
						btn_Sure.setBackgroundColor(R.drawable.buttom_selector_bg);
						btn_Sure.setFocusable(false);
						handlernet.sendEmptyMessage(12);
					}

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		});

	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(ChecklibraryReplenishmentActivity.this);

		@Override
		public int getCount() {
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

	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(ChecklibraryReplenishmentActivity.this);

		@Override
		public int getCount() {
			Log.e(TAG, "!!!!!" + showarrayList.size());
			return (showarrayList.size());
		}

		@Override
		public Object getItem(int arg0) {
			return showarrayList.get(arg0);
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
			rh.tv.setText(showarrayList.get(arg0));
			return arg1;
		}

	}

	public static class RightHolder {
		TextView tv;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.getRuning().remove();
		if (showarrayList.size() > 0) {
			showarrayList.clear();
		}
		if (o_Application.guolv.size() > 0) {
			o_Application.guolv.clear();
		}
		getRfid().close_a20();
		ChecklibraryReplenishmentActivity.this.finish();

	}

	public static class WalkieDataCountAdapterHolder1 {
		TextView tv;
	}

	/***
	 * 开启扫描
	 */
	private void StartScan() {
		radapter = new RightAdapter();
		manager = new ManagerClass();
		o_Application.guolv.clear();

		getRfid().addNotifly(getnumber1);
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();

	}

	private void StopScan() {
		getRfid().close_a20();
	}

	/***
	 * 主要是对比标签 查看是否符合规则 符合后天数据直接移除集合否者添加进行展示 2 是否有字母A=65 3 如果 选中100元 A
	 * 排除掉后台反的数据并且扫到其它券别的移除掉不能提交数据
	 */

	public void NumberSelect() {
		if (o_Application.numberlist.size() == 0) {
			return;
		} else { // CD015A1190227093020
					// 676800581190227093076000
			if (o_Application.numberlist.isEmpty()) {

			} else {
				for (int i = 0; i < o_Application.numberlist.size(); i++) {
					// 扫描到和请求道相同直接移除集合
					if (!o_Application.numberlist.get(i).isEmpty()) {
						String sub_string = o_Application.numberlist.get(i).substring(2, 4); // 是否包含字母
						String codestr = o_Application.numberlist.get(i).substring(4, 6);
						Log.d(TAG, "sub" + sub_string + "===" + codestr + "====***" + o_Application.quanbie);
						if (codestr.equals(o_Application.quanbie)) {
							if (sub_string.equals("01")) {// /包含字母8,9位如果有字母添加到集合用于显示
								if (unneedScanpakgeList.isEmpty()) {
									if (showarrayList.contains(o_Application.numberlist.get(i))) {
										return;
									} else if (!(showarrayList.contains(o_Application.numberlist.get(i)))) {
										showarrayList.add(o_Application.numberlist.get(i));// 1 后台已有的数据为null
																							// 显示数据扫描中数据和心事数据不一致时添加
										handlernet.sendEmptyMessage(12);
									}
								} else {
									for (int j = 0; j < unneedScanpakgeList.size(); j++) {
										if (!(o_Application.numberlist.get(i).equals(unneedScanpakgeList.get(j)))) {
											if (!(showarrayList.contains(o_Application.numberlist.get(i)))) {
												showarrayList.add(o_Application.numberlist.get(i));
											}
											handlernet.sendEmptyMessage(12);
										}

									}

								}
							} else if (sub_string.equals("00")) {
								if (unneedScanpakgeList.isEmpty()) {
									if (!(showarrayList.contains(o_Application.numberlist.get(i)))) {
										// 1 后台已有的数据为null 显示数据扫描中数据和心事数据不一致时添加

										if (!(showarrayList.contains(o_Application.numberlist.get(i)))) {
											showarrayList.add(o_Application.numberlist.get(i));
										} else if (showarrayList.contains(o_Application.numberlist.get(i))) {
											o_Application.numberlist.remove(i);
											return;
										}

									}
									handlernet.sendEmptyMessage(12);
								} else {
									for (int j = 0; j < unneedScanpakgeList.size(); j++) {
										if (!(o_Application.numberlist.get(i).equals(unneedScanpakgeList.get(j)))) {
											if (!(showarrayList.contains(o_Application.numberlist.get(i)))) {
												showarrayList.add(o_Application.numberlist.get(i));
											}
											handlernet.sendEmptyMessage(12);
										}

									}

								}

							}
						}
					}
				}
			}
		}
		handlernet.sendEmptyMessage(12);
	}

	/***
	 * 扫描时传来数据
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:

				NumberSelect();
				right_tv.setText("" + showarrayList.size());
				Left_tv.setText("" + MissCount);
				o_Application.numberlist.clear();
				break;
			}
		}

	};

	/***
	 * 3 网络请求 漏扫（需要补扫的钞袋子的方法） 请求提交是否成功
	 */
	public void Requetnetbymiss() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String tasknum = Taskitem;
					String number = typemoneyselect2;
					Gson gson = new Gson();
					parms = new CheckLibrarySupplementService().getReScanDetail(tasknum, number);
					Log.d(TAG, "获取一个扫描的号" + "=====" + parms.toString());
					// 返回的类型anyType{}需要进行判断
					unneedScanpakgeList.clear();
					if (parms != null) {
						getReScanDetailEntity grsd = gson.fromJson(parms, getReScanDetailEntity.class);
						for (int i = 0; i < grsd.getFilterTagList().size(); i++) {
							unneedScanpakgeList.add(grsd.getFilterTagList().get(i));
						}
						Log.e(TAG, "MissCount" + grsd.getMissCount());
						MissCount = grsd.getMissCount();
						handlernet.sendEmptyMessage(7);

					} else {
						handlernet.sendEmptyMessage(0);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/***
	 * 接口4 上传扫描到的箱子 更新上传发给后台的网络请求 扫到的箱子号和任务号 userId ： 登录人的id taskNum： 盘库单号 moneyid ：
	 * 券别 tagListJson ： 漏扫数据
	 */
	private void updateReScan() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					rfid.close_a20();
					String loginuser = GApplication.user.getYonghuZhanghao();
					String number = Taskitem.trim();
					String monetype = typemoneyselect2;
					Gson gson = new Gson();
					String putJson = gson.toJson(showarrayList);
					Log.e(TAG, number.toString());
					if (loginuser.equals("") || monetype.equals("") || putJson.equals("")) {
						Toast.makeText(ChecklibraryReplenishmentActivity.this, "数据不能为空", 2000).show();
						return;
					} else {

						parms = new CheckLibrarySupplementService().updateReScan(loginuser, number, monetype, putJson);

						// 返回的类型anyType{}需要进行判断
						if (parms != null && !parms.equals("anyType{}")) {
							Log.e(TAG, parms.toString());
							handlernet.sendEmptyMessage(6);
						} else {
							handlernet.sendEmptyMessage(5);
						}
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handlernet.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/****
	 * 验证最后是否还有任务可以进行操作重新请求任务列表
	 */
	public void getCheckLibrarySupplementTaskList() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String number = GApplication.loginname;
					parms = new CheckLibrarySupplementService().getTaskAndMissCount1(number);
					// 返回的类型anyType{}需要进行判断
					if (parms != null && !parms.equals("anyType{}")) {
						Gson gson = new Gson();
						ChelibrarySupplemnetBaseEntity[] csbe = gson.fromJson(parms,
								ChelibrarySupplemnetBaseEntity[].class);
						clsList.clear();
						List<ChelibrarySupplemnetBaseEntity> listoCheck = Arrays.asList(csbe);
						List arrList = new ArrayList(listoCheck);
						for (int i = 0; i < csbe.length; i++) {
							clsList.addAll(arrList);
							countforTask = clsList.get(0).getMISSCOUNT();
							System.out.print("!!!!!!!!!!!" + countforTask);
						}

						if (countforTask == null) {
							manageractivity.getRuning().runding(ChecklibraryReplenishmentActivity.this, "即将跳转");
							Intent intent = new Intent(ChecklibraryReplenishmentActivity.this,
									CheckLibrarySupplementActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						} else {
							handlernet.sendEmptyMessage(8);
						}
					}

				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	/***
	 * 适配器 spinner的
	 * 
	 * @author Administrator
	 *
	 */
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
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.getRuning().remove();
		if (isFinishing()) {
			getRfid().close_a20();
		}
	}
}
