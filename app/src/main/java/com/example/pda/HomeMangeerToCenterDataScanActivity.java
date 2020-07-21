package com.example.pda;

import hdjc.rfid.operator.RFID_Device;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ljsw.tjbankpad.baggingin.activity.handover.HomeMangerToCleanHandoverActivity;
import com.ljsw.tjbankpad.baggingin.activity.handover.saomiaoutil.CashtoPackgerUtils;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;
import com.pda.checksupplement.service.CheckLibrarySupplementService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 库管员交接 现金装袋后需要扫描后台的袋数并指纹验证提交
 * 
 * @author Administrator 廉c 201910.28修改加班
 */
public class HomeMangeerToCenterDataScanActivity extends Activity implements OnClickListener {
	protected static final String TAG = "HomeMangeerToCenterDataScanActivity";
	private ImageView ivblacka;
	private TextView homepackger_tvleft;// 标题文字， 装袋的左侧数量
	private ManagerClass manageractivity;
	private OnClickListener OnClick1;
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	// 数据源
	private List<HomeMagerToCenterEntity> pakgerslist = new ArrayList<HomeMagerToCenterEntity>();// 装卡的集合号
	private List<String> packgetsList = new ArrayList<String>();
	// private QualitativeWareScanning getnumber1;// 抵制押品自己的一套扫描rfid标签的规则
	private TextView homepackgerright;// 右侧文字
	private ListView righthometoclean_listview, lefthometoclean_listview;// 右侧listview// 左侧listview
	private Button btn_hometoclean_jiaojie, btn_hometoclean_cancle;// 左侧按钮
	private CashtoPackgerUtils cashtoPackgerUtils;
	private List<String> listinttent = new ArrayList<String>();// 需要传的集合
	private RFID_Device rfid;

	private Table[] mingxilist;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	private ManagerClass manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_mangeer_to_center_data_scan);
		// adapter = new QingLingAdapter();
		manageractivity = new ManagerClass();
//		改
		cashtoPackgerUtils = new CashtoPackgerUtils();
		cashtoPackgerUtils.setHandler(handler);
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();
		initView();
//		loaddata();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manageractivity.getAbnormal().remove();
				loaddata();
			}
		};
	}

	/****
	 * 获取尾零的袋的编号进行扫描
	 */
	String parms = "";//

	private void loaddata() {

		new Thread() {
			@Override
			public void run() {
				super.run();
//					o_Application.qlruku.getZhouzhuanxiang().clear();
				try {
					parms = new CheckLibrarySupplementService().HomemangerGetPackgets_queryUnHandleCdnos();
					// 返回的类型anyType{}需要进行判断
					Log.e(TAG, "parms数据" + parms);
					if (parms != null) {
						if (parms.equals("anyType{}")) {
							return;
						} else {

							List<String> split = Arrays.asList(parms.split(","));
							if (null != o_Application.qlruku.getZhouzhuanxiang()) {
								o_Application.qlruku.getZhouzhuanxiang().clear();
							}
							assert o_Application.qlruku.getZhouzhuanxiang() != null;
							o_Application.qlruku.getZhouzhuanxiang().addAll(split);
							if (null == o_Application.qlruku) {
								handler1.sendEmptyMessage(3);
							} else {
								handler1.sendEmptyMessage(4);
							}
						}
					} else {
						handler1.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler1.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handler1.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handler1.sendEmptyMessage(1);
				}
			}

		}.start();

		// 比如在这里开始查询webservice
//			query( new CallbackClass(){
//				public void callback(String cdnos){
//					//
//				}
//			});
//		}
//	private interface CallbackClass{
//		public void callback(String cdnos);
//	}
//		
//	private void query(CallbackClass c){
//		//webservicve查询
//		//{
//			c.callback("CD10A14190227093020000,CD10014190227093020000");
//		//}
	}

	private void initView() {
		ivblacka = (ImageView) findViewById(R.id.ql_ruku_back);
		ivblacka.setOnClickListener(this);
		homepackger_tvleft = (TextView) findViewById(R.id.homepackger_tvleft);
		// 左侧数量
		homepackgerright = (TextView) findViewById(R.id.homepackgerright);
		lefthometoclean_listview = (ListView) findViewById(R.id.lefthometoclean_listview);
		righthometoclean_listview = (ListView) findViewById(R.id.righthometoclean_listview);
		btn_hometoclean_jiaojie = (Button) findViewById(R.id.btn_hometoclean_jiaojie);
		btn_hometoclean_jiaojie.setFocusable(false);
		btn_hometoclean_jiaojie.setBackgroundResource(R.drawable.button_gray);
		btn_hometoclean_jiaojie.setOnClickListener(this);
		btn_hometoclean_cancle = (Button) findViewById(R.id.btn_hometoclean_cancle);
		btn_hometoclean_cancle.setFocusable(false);
		btn_hometoclean_cancle.setBackgroundResource(R.drawable.button_gray);
		btn_hometoclean_cancle.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		loaddata();
		// TODO Auto-generated method stub
		super.onResume();
		o_Application.guolv.clear();
		List arrList = new ArrayList<String>();

		o_Application.qinglingruku.add(new QingLingRuKu(null, null, arrList));
		o_Application.qlruku = o_Application.qinglingruku.get(0);
		if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {

		} else {

		}
	}

	private Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manageractivity.getRuning().remove();
				manageractivity.getAbnormal().timeout(HomeMangeerToCenterDataScanActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manageractivity.getRuning().remove();
				manageractivity.getAbnormal().timeout(HomeMangeerToCenterDataScanActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manageractivity.getRuning().remove();

//				获取数据成功后进行扫描
//				cashtoPackgerUtils  = new CashtoPackgerUtils(); 
//				cashtoPackgerUtils.setHandler(handler);
				// adapter.notifyDataSetChanged();
				// dzyp_outlistview.setAdapter(adapter);
				// new TurnListviewHeight(listview);

				break;
			case 3:
				manageractivity.getRuning().remove();
				manageractivity.getAbnormal().timeout(HomeMangeerToCenterDataScanActivity.this, "没有数据",
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								manageractivity.getAbnormal().remove();
							}
						});
				break;
			case 4:
				getRfid().addNotifly(cashtoPackgerUtils);
				o_Application.numberlist.clear();
				new Thread() {
					@Override
					public void run() {
						super.run();
						Thread thread = new Thread();
						try {
							thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getRfid().open_a20();
					}
				}.start();

				homepackgerright.setText("" + o_Application.numberlist.size());
				homepackger_tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				lefthometoclean_listview.setAdapter(ladapter);
				righthometoclean_listview.setAdapter(radapter);
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
					btn_hometoclean_jiaojie.setEnabled(false);
					btn_hometoclean_jiaojie.setBackgroundResource(R.drawable.button_gray);
				}
				if (o_Application.numberlist.size() == 0) {
					btn_hometoclean_cancle.setEnabled(false);
					btn_hometoclean_cancle.setBackgroundResource(R.drawable.button_gray);
				}
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			if (rfid != null) {
				getRfid().close_a20();
			}
			HomeMangeerToCenterDataScanActivity.this.finish();
			break;
		case R.id.btn_hometoclean_cancle:

			o_Application.qlruku.getZhouzhuanxiang().clear();
			o_Application.qlruku.getZhouzhuanxiang().addAll(o_Application.numberlist);
			o_Application.numberlist.clear();
			o_Application.guolv.clear();

			if (o_Application.qinglingruku.size() == 0) {

				btn_hometoclean_jiaojie.setEnabled(true);
//				btn_hometoclean_jiaojie
//						.setBackgroundResource(R.drawable.button_gray);
			} else {

				btn_hometoclean_jiaojie.setEnabled(false);
				btn_hometoclean_jiaojie.setBackgroundResource(R.drawable.button_gray);
			}
			if (o_Application.numberlist.size() == 0) {
				btn_hometoclean_cancle.setEnabled(false);
				btn_hometoclean_cancle.setBackgroundResource(R.drawable.button_gray);
			}
			handler.sendEmptyMessage(1);
			break;

		case R.id.btn_hometoclean_jiaojie:
			if (rfid != null) {
				getRfid().close_a20();
			}
//			跳转类指纹交接并
			Intent intent = new Intent(HomeMangeerToCenterDataScanActivity.this,
					HomeMangerToCleanHandoverActivity.class);
			listinttent.clear();// 清空每次
			listinttent.addAll(o_Application.numberlist);
			String str = "";
			for (int i = 0; i < listinttent.size(); i++) {
				str += "," + listinttent.get(i);

			}
			if (null == str || str.equals("")) {
				return;
			} else {

				intent.putExtra("str", str.substring(1));

				if (0 == listinttent.size()) {

					Toast.makeText(HomeMangeerToCenterDataScanActivity.this, "要交接的数据不能为空", 300).show();
				} else {
					startActivity(intent);
				}
				return;
			}

		default:
			break;
		}
	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(HomeMangeerToCenterDataScanActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(HomeMangeerToCenterDataScanActivity.this);

		@Override
		public int getCount() {
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

	/**
	 * 无限刷新集合
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				// 用户进入时不让点击跳转下一个页面
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					if (o_Application.numberlist.size() != 0) {
						btn_hometoclean_jiaojie.setEnabled(true);
						btn_hometoclean_jiaojie.setBackgroundResource(R.drawable.buttom_selector_bg);
					}
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {

					btn_hometoclean_cancle.setEnabled(true);
					btn_hometoclean_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				homepackgerright.setText("" + o_Application.numberlist.size());
				homepackger_tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				lefthometoclean_listview.setAdapter(ladapter);
				righthometoclean_listview.setAdapter(radapter);

				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onDestroy() {

		manageractivity.getRuning().remove();
		if (o_Application.numberlist.size() > 0) {
			o_Application.numberlist.clear();
		}
		if (o_Application.guolv.size() > 0) {
			o_Application.guolv.clear();
		}
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			o_Application.qlruku.getZhouzhuanxiang().clear();
		}

		super.onDestroy();

		if (rfid != null) {
			getRfid().close_a20();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (rfid != null) {
			getRfid().close_a20();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (rfid != null) {
			getRfid().close_a20();
		}
		this.finish();
	}

}
