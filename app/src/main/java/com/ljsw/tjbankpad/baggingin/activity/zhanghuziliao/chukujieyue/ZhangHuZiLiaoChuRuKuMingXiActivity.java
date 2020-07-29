package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/***
 * 账户资料出入库明细
 * 
 * @author Administrator
 *
 */
public class ZhangHuZiLiaoChuRuKuMingXiActivity extends Activity implements OnClickListener {
	// 组件
	private static final String TAG = "ZhangHuZiLiaoChuRuKuMingXiActivity";
	private ImageView ivblack;
	private TextView zhzlCK_caozuoyuan;
	private TextView About_type, AboutTastNumber, About_Counts; // 任务类型，任务的号
	// 数据源
	private List<AccountOutTaskDetailCode> aotdcodelist = new ArrayList<AccountOutTaskDetailCode>();// json
																									// 外层集合
	private List<AccountOutTaskDetailBase> aotdcbaselist = new ArrayList<AccountOutTaskDetailBase>();// 内层显示集合
	private List<String> strlist = new ArrayList<String>();;// 扫描时数据添加的集合
	private Button btn_black, btn_jiaojie; // 查看结果后返回
	private ListView zhzl_listview_chuku; // 出库
	private String accrossCvoun;// 账户资料借阅传过来的字符串
	private String FLAG;// 账户资料传的类型
	private String netresult;// 网落得请求结果
	private String counts; // 当前页面的数量

	private ManagerClass manager;// activity dialog

	private AccountOutTaskDetailCode aotdc = new AccountOutTaskDetailCode();

	private OnClickListener OnClick1;
	private QingLingAdapter adapter;// 适配器
	public static ZhangHuZiLiaoChuRuKuMingXiActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaochukumingxi);
		Intent mIntent = getIntent();
		accrossCvoun = mIntent.getStringExtra("cvoun");
		FLAG = mIntent.getStringExtra("FLAG");
		Log.e(TAG, "accrossCvoun===" + accrossCvoun);

		instance = this;
		initView();

		adapter = new QingLingAdapter();
		getAccountOutTaskDetail();// 4 账户资料出库借阅
		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {

			@Override
			public void onClick(View v) {
				getAccountOutTaskDetail();

			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		adapter.notifyDataSetChanged();
		zhzl_listview_chuku.setAdapter(adapter);
//	aotdcbaselist.clear();
		getAccountOutTaskDetail();
		S_application.wrong = "huitui";
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getAccountOutTaskDetail();
			}
		};
	}

	private void getAccountOutTaskDetail() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = accrossCvoun;

					netresult = new AccountInfomationReturnService().getAccountOutTaskListDetail(number);

					Log.e(TAG, "**===" + netresult);
					if (netresult != null && !netresult.equals("anyType{}")) {
						Gson gson = new Gson();
						aotdc = gson.fromJson(netresult, AccountOutTaskDetailCode.class);
						Log.e(TAG, "**===" + aotdc);
						counts = aotdc.getCount() + "";// 获取数量
						aotdcodelist.add(aotdc);// 外层添加数据

						aotdcbaselist.clear();// 避免数据重复
						for (int i = 0; i < aotdc.getList().size(); i++) {// 显示数据集合
							aotdcbaselist.add(aotdc.getList().get(i));
							Log.e(TAG, "**===i" + aotdcbaselist.get(i).getCONTAINERNO());
							Log.e(TAG, "**===i" + aotdcbaselist.get(i).getSTOCKCODE());
							Log.e(TAG, "**===i" + aotdc.getList().toString());
						}
						/// 添加扫描的数据
						strlist.clear();
						for (int j = 0; j < aotdcbaselist.size(); j++) {
							strlist.add(aotdcbaselist.get(j).getSTOCKCODE());
							Log.e(TAG, "**j===" + aotdcbaselist.get(j).getSTOCKCODE());
							Log.e(TAG, "**j===" + aotdcbaselist.get(j).getCONTAINERNO());
						}

						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e); /// 网络请求
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/***
	 * 网络请求成功获取后的显示
	 */
	@SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuRuKuMingXiActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuRuKuMingXiActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				About_Counts.setText(counts);

				getData();
				adapter.notifyDataSetChanged();
				zhzl_listview_chuku.setAdapter(adapter);
				new TurnListviewHeight(zhzl_listview_chuku);// 放开
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuRuKuMingXiActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			default:
				break;
			}
		}

	};

	/***
	 * 账户资料出库借阅 向集合中添加锁号码
	 */

	private void getData() {
		o_Application.qinglingruku.clear();
		if (aotdcodelist.get(0).getList().size() > 0) {
			for (int i = 0; i < aotdcodelist.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu("", null, strlist));
				o_Application.qlruku = o_Application.qinglingruku.get(i);
			}
		}

	}

	private void initView() {
		zhzlCK_caozuoyuan = (TextView) findViewById(R.id.aioutbounddetails_user);
		zhzlCK_caozuoyuan.setText(GApplication.user.getLoginUserName());
		About_Counts = (TextView) findViewById(R.id.aioutbounddetails_counts);
		About_Counts.setText("0");
		ivblack = (ImageView) findViewById(R.id.ql_ruku_back);
		ivblack.setOnClickListener(this);
		AboutTastNumber = (TextView) findViewById(R.id.aioutbounddetails_number);// 任务编号
		AboutTastNumber.setText("" + accrossCvoun);
		About_type = (TextView) findViewById(R.id.aioutbounddetails_type);// 任务类型
		About_type.setText(FLAG);
		zhzl_listview_chuku = (ListView) findViewById(R.id.zhzl_listview_chuku);

		btn_jiaojie = (Button) findViewById(R.id.dizhiyapin_btn_hover);
		btn_jiaojie.setOnClickListener(this);
	}

	/***
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoChuRuKuMingXiActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return aotdcbaselist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return aotdcbaselist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_wangdianmingcheng_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvinfo);
				view.riqi = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvtype);
//				view.count = (TextView) arg1.findViewById(R.id.dizhiyapintype);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText(aotdcbaselist.get(arg0).getSTOCKCODE());
			view.riqi.setText(aotdcbaselist.get(arg0).getCONTAINERNO());
//			view.count.setText(aotdcbaselist.get(arg0).getCONTAINERNO());
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			ZhangHuZiLiaoChuRuKuMingXiActivity.this.finish();
			break;
		case R.id.dizhiyapin_btn_hover:// 交接跳转

			Intent mIntent = new Intent(ZhangHuZiLiaoChuRuKuMingXiActivity.this,
					ZhangHuZiLiaoChuKuJiaoJieActivity.class);
			mIntent.putExtra("cvoun", accrossCvoun);
			mIntent.putExtra("FLAG", FLAG);
			startActivity(mIntent);

			break;

		default:
			ZhangHuZiLiaoChuRuKuMingXiActivity.this.finish();
			break;
		}
	}

}
