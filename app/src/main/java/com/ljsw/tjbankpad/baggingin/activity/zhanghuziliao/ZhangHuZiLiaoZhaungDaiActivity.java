package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import a20.cn.uhf.admin.Tools;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.handheld.UHF.UhfManager;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpad.baggingin.activity.adapter.AutoComperAdapter;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;
import com.strings.tocase.CaseString;

/**
 * 账户资料装袋 2018-10-17
 * 
 * @author Administrator 2018_11_9 有问题 用户选中第二个人时不能只能选中一个 后台有问题需要改
 */
public class ZhangHuZiLiaoZhaungDaiActivity extends Activity implements OnClickListener {
	protected static final String TAG = "ZhangHuZiLiaoZhaungDaiActivity";
	private TextView caozuoren;// 操作人
	private TextView zhanghuziliaoshuliang;
	private Button btn_chongsao, btnPacking;// 查询按钮
	private AutoCompleteTextView etnumber;
	private TextView tvorg, tvChongsao;// 搜索
	private TextView tvuser; // 开户人
	private TextView tvsaomiaobaohao, search;// 扫描包号码
	private ImageView ql_ruku_back;
	private String number = "";
	private String netResult = "";
	private OnClickListener OnClick1;
	private ManagerClass manager;

	private long lastClickTime = 0;/// 限制点击的时间间隔
//实体类
	private AccountCenterBagginBase acbagginbase = new AccountCenterBagginBase();/// 实体类
//	数据源
	private List<String> acbaggnumberList = new ArrayList<String>(); // 放置列表显示号
	private Map<String, Object> map = new LinkedHashMap<String, Object>();// 通过key value 保存要显示的条目
	private Map<String, Object> mapvalue = new LinkedHashMap<String, Object>();
//	变量
	private String undobaggingnum = "";// 待装袋数量
	private String bankorg = ""; // 开户行机构
	private String Accountholder = "";// 开户人
	private String Packetbag = ""; // 已扫到的封包号

	private AutoComperAdapter Autoadapter;
	private UhfManager NSCmanager;
	private String str;// 接收到的转化的字符串
	private powercontrol rFidPowerControl;// yang
	private DialogManager dmanager;// 弹出框管理类
	private String upsaomiao = ""; // 锁号码
	private String selectNumber = ""; // 选中的号码
	// dialog显示
	private Dialog dialogSuccess;
	private Dialog dialogUnSunccess;

	// 显示跳转提示
	private ManagerClass manageractivity;// /activity提示

	// 输入时需要的正则表达式
	private String Regular = "^[A-Z]+$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaozhuangdai);
		initView();
		etnumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence mCharSequences = new CharSequence() {

					@Override
					public CharSequence subSequence(int start, int end) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public int length() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public char charAt(int index) {
						// TODO Auto-generated method stub
						return 0;
					}
				};
				mCharSequences = " ";
				etnumber.append(mCharSequences);
				etnumber.setAdapter(Autoadapter);

			}
		});
//	
		// ================================打开RFID电源控制
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		dmanager = new DialogManager(ZhangHuZiLiaoZhaungDaiActivity.this);

		initView();
		NSCmanager = UhfManager.getInstance();
		if (NSCmanager == null) {
			showBigToast("串口初始化失败", 500);
			return;
		}
		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();

			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		S_application.wrong = "huitui";
		getAccountTurnOverLineCount();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getAccountTurnOverLineCount();
			}
		};
	}

	/****
	 * 网络获取任务
	 */
	public void getAccountTurnOverLineCount() {
		new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				super.run();
				try {
					// 账号
					number = "" + GApplication.user.getYonghuZhanghao();
					Log.i(TAG, "账户名称" + number); /// 网络请求
					netResult = new AccountInformationService().getAccountBaggingDetailList(number);
					if (!netResult.equals("")) {
						Gson gson = new Gson();

						acbagginbase = gson.fromJson(netResult, AccountCenterBagginBase.class);
						undobaggingnum = acbagginbase.getUndoSum();
						acbaggnumberList.clear();
						// 获取下拉数据

						for (int m = 0; m < acbagginbase.getUndoAccountNumList().size(); m++) {
							acbaggnumberList.add(" " + acbagginbase.getUndoAccountNumList().get(m));
						}
						// 得到textview 显示

						map.clear();
						mapvalue.clear();
						for (int j = 0; j < acbagginbase.getAccountList().size(); j++) {
							Log.e(TAG, "key====" + acbagginbase.getAccountList().get(j).getACCOUNTNUM() + "value------"
									+ acbagginbase.getAccountList().get(j).getCOMPANYNAME());
							map.put(acbagginbase.getAccountList().get(j).getACCOUNTNUM(),
									acbagginbase.getAccountList().get(j).getCOMPANYNAME());
							mapvalue.put(acbagginbase.getAccountList().get(j).getACCOUNTNUM(),
									acbagginbase.getAccountList().get(j).getORGNAM());

						}

						/// 加入适配器筛选的适配器
						Autoadapter = new AutoComperAdapter(ZhangHuZiLiaoZhaungDaiActivity.this,
								android.R.layout.simple_dropdown_item_1line, acbaggnumberList, AutoComperAdapter.ALL);
						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(3);
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
	 * 向后台传输数据
	 */
	public void getupdatabagging() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 账号
					number = "" + GApplication.user.getYonghuZhanghao();
					Log.i(TAG, "账户名称" + number); /// 网络请求
					netResult = new AccountInformationService().updatabagging(selectNumber, upsaomiao,
							GApplication.user.getYonghuZhanghao());
					if (netResult.equals("00")) {
						manager.getResultmsg().resultmsg(ZhangHuZiLiaoZhaungDaiActivity.this, "提交成功", true);
//							handler.sendEmptyMessage(4);// 提交成功
					} else {
//							handler.sendEmptyMessage(5);// 提交失败
						manager.getResultmsg().resultmsg(ZhangHuZiLiaoZhaungDaiActivity.this, "提交失败", false);
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
	 * 网络请求成功获取后的显示
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhaungDaiActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhaungDaiActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
//					bankorg="";  // 开户行机构 
//				    Accountholder="" ;// 开户人

				zhanghuziliaoshuliang.setText("" + undobaggingnum);
				etnumber.setAdapter(Autoadapter);
				tvuser.setText("");

				// 用户输入空得字符时能狗自匹配出数据

//						if(Regular.matches(etnumber.getText().toString().trim())){
				etnumber

						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								for (Map.Entry<String, Object> entry : map.entrySet()) {
									Log.e(TAG, "key==" + acbaggnumberList.get(arg2) + "key==" + entry.getKey()
											+ "value==" + entry.getValue());
									// 在组件获取值 进行哪来和集合进行对比
									if (etnumber.getText().toString().trim().equals(entry.getKey())
											&& (!etnumber.getText().toString().trim().equals(""))) {
										selectNumber = etnumber.getText().toString().trim();// 得到值传给后台变量
										Log.e(TAG, "selectNumber!!" + selectNumber);
										Log.e(TAG, "key!!" + acbaggnumberList.get(arg2) + "key!!==" + entry.getKey()
												+ "value!!" + entry.getValue());
										tvuser.setText(entry.getValue() + "");

									}
								}
								for (Map.Entry<String, Object> entryvalue : mapvalue.entrySet()) {
									if (etnumber.getText().toString().trim().equals(entryvalue.getKey())) {
										tvorg.setText(entryvalue.getValue() + "");
										return;
									}
								}
							}
						});
//						}	

				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhaungDaiActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:// 提交成功

//						dialogSuccess = new Dialog(ZhangHuZiLiaoZhaungDaiActivity.this);
//		            	LayoutInflater inflater = LayoutInflater.from(ZhangHuZiLiaoZhaungDaiActivity.this);
//		            	View v = inflater.inflate(R.layout.dialog_success, null);
//		            	Button but = (Button)v.findViewById(R.id.success);
//		            	but.setText("装袋成功");
//		            	dialogSuccess.setCancelable(false);
//		            	dialogSuccess.setContentView(v);
//		            	if(but!=null){
//		            		but.setOnClickListener(new View.OnClickListener() {
//		            			@Override
//		            			public void onClick(View arg0) {				
//		            				dialogSuccess.dismiss();
//		            				getAccountTurnOverLineCount();
//		         	                //提交成功后清除
//		            				tvsaomiaobaohao.setText("");
//		            				tvuser.setText("");
//		            				tvorg.setText("");
//		            				etnumber.setText("");
//		         	            	Log.e(TAG,"+++++++++++"+zhanghuziliaoshuliang);
//		            			}
//		            		});
//		            	}
//		            	dialogSuccess.show();
				break;

			case 5:// 提交失败
				dialogUnSunccess = new Dialog(ZhangHuZiLiaoZhaungDaiActivity.this);
				LayoutInflater inflaterunSuccess = LayoutInflater.from(ZhangHuZiLiaoZhaungDaiActivity.this);
				View vunSuccess = inflaterunSuccess.inflate(R.layout.dialog_success, null);
				Button butunSuccess = (Button) vunSuccess.findViewById(R.id.success);
				butunSuccess.setText("装袋结果：" + netResult);
				dialogUnSunccess.setCancelable(false);
				dialogUnSunccess.setContentView(vunSuccess);
				if (butunSuccess != null) {
					butunSuccess.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							dialogUnSunccess.dismiss();
//		            				 getRenWuInfo();
							// 提交成功后清除
							tvsaomiaobaohao.setText("");
						}
					});
				}
				dialogUnSunccess.show();

				break;

			default:
				break;
			}
		}

	};

	private void initView() {
		// TODO Auto-generated method stub
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);

		zhanghuziliaoshuliang = (TextView) findViewById(R.id.zhzl_number);
		zhanghuziliaoshuliang.setText(undobaggingnum + "");

		tvorg = (TextView) findViewById(R.id.zhzl_org_tv);

		tvuser = (TextView) findViewById(R.id.openuser);
		caozuoren = (TextView) findViewById(R.id.zhzl_tv_username);
		caozuoren.setText("" + GApplication.user.getLoginUserName());

		etnumber = (AutoCompleteTextView) findViewById(R.id.accountcentatctv);// 输入的自动提示
		etnumber.setThreshold(1);
		// 扫描到包号码
		tvsaomiaobaohao = (TextView) findViewById(R.id.tv_saonumber);
		tvsaomiaobaohao.setText("");
		btn_chongsao = (Button) findViewById(R.id.zhzl_btn_saomiao);
		btn_chongsao.setOnClickListener(this);
		// 装箱按钮
		btnPacking = (Button) findViewById(R.id.zhzl_zhuangxiang_btn);
		btnPacking.setOnClickListener(this);
		tvChongsao = (TextView) findViewById(R.id.tv_zhzl_sousuo);
		tvChongsao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		byte[] accessPassword = Tools.HexString2Bytes("00000000");
		switch (v.getId()) {

		case R.id.ql_ruku_back: // /关闭页面
			ZhangHuZiLiaoZhaungDaiActivity.this.finish();
			break;
		case R.id.tv_zhzl_sousuo: // 做搜素操作 将数据显示到输入的可选框上

//			setdata();
			etnumber.setAdapter(Autoadapter);
			break;
		case R.id.zhzl_btn_saomiao:
			if (acbaggnumberList.size() == 0) {
				showBigToast("请检查账户资料号", 500);
				return;
			} else if (tvuser.getText().equals("")) {
				showBigToast("请检查账户资料号", 500);
				return;
			} else {
				tvsaomiaobaohao.setText("");
				if (accessPassword.length != 4) {
					showBigToast("密码为4个字节", 500);
					return;
				}

				byte[] data = NSCmanager.readFrom6C(1, 2, 6, accessPassword);
				if (data != null && data.length > 1) {
					String dataStr = Tools.Bytes2HexString(data, data.length);
					if (dataStr == null || !CaseString.reg(dataStr)) {
						return;
					}
					str = dataStr;
					str = CaseString.getBoxNum2(str);
					tvsaomiaobaohao.setText(str);
					showBigToast(str, 500);
					upsaomiao = str;
					break;
				} else {
					if (data != null) {
						showBigToast("扫描失败重试", 500);
						return;
					}
				}
				btn_chongsao.setText("重扫");
			}

			btn_chongsao.setText("重扫");
			break;
		case R.id.zhzl_zhuangxiang_btn:
			long now = System.currentTimeMillis();
			if (now - lastClickTime > 1000) {
				lastClickTime = now;
				getupdatabagging();

			} else {
				showBigToast("您提交的太快了", 400);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 拆分集合
	 * 
	 * private String bankorg=""; // 开户行机构 private String Accountholder="" ;// 开户人
	 * private String Packetbag="" ; // 已扫到的封包号
	 */

	/**
	 * 自定义 toast， 增大字体
	 *
	 * @param info     提示信息
	 * @param duration 显示时间，0：短时间，1：长时间
	 */
	public void showBigToast(String info, int duration) {
		Toast toast = new Toast(ZhangHuZiLiaoZhaungDaiActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(ZhangHuZiLiaoZhaungDaiActivity.this);
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
	 * 获取数据放到适配器上
	 */
//    public  void   setdata(){
//    	etnumber.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				CharSequence mCharSequences=new CharSequence()
//		          {
//		             
//		            @Override
//		            public CharSequence subSequence(int start, int end)
//		            {
//		              // TODO Auto-generated method stub
//		              return null;
//		            }
//		             
//		            @Override
//		            public int length()
//		            {
//		              // TODO Auto-generated method stub
//		              return 0;
//		            }
//		             
//		            @Override
//		            public char charAt(int index)
//		            {
//		              // TODO Auto-generated method stub
//		              return 0;
//		            }
//		          };          
//		          mCharSequences=" ";
//		          etnumber.append(mCharSequences);
//		         
//				
//			}
//		});
//	
//			

//    }

}
