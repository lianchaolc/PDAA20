package com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinChaiXiangAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinSaoMiaoLieBiaoActivity;
import com.ljsw.tjbankpda.db.activity.QingFenJiHuaMingXi_db;
import com.ljsw.tjbankpda.db.activity.QingLingZhuangXiangRuKu_db;
import com.ljsw.tjbankpda.db.activity.RenWuLieBiao_db;
import com.ljsw.tjbankpda.db.activity.QingLingZhuangXiangRuKu_db.ViewHodler;
//import com.ljsw.tjbankpda.db.activity.QingLingZhuangXiangRuKu_db.QingLingAdapter;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.service.KuGuanRenWuLieBiao;
import com.ljsw.tjbankpda.db.service.QingLingZhuangXiangRuKu;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.pda.BankDoublePersonLogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.BaseAdapter;

/***
 * 当前的activity 来自周转箱的地址押品拆箱子 把抵制押品拿出来
 * 
 * @author Administratorlchao
 *
 */
@SuppressLint("HandlerLeak")
public class BoxToDiZhiYaPinZhuangdaiActivity extends Activity implements OnClickListener {
	protected static final String TAG = "BoxToDiZhiYaPinZhuangdaiActivity";
	// 操作人员
	private TextView zhouto_username;

	// listview 显示 周转箱任务列表
	private ListView listview;
	// 数据源
	private List<String> arraylist = new ArrayList<String>();
	private List<DiZhiYaPinChaiXiang> openboxtopackges = new ArrayList<DiZhiYaPinChaiXiang>();
	private ImageView ql_ruku_back;// 返回按钮
	private ManagerClass manager;
	private QingLingAdapter adapter;
	private Table[] rukulist;
//	适配器
	private DiZhiYaPinChaiXiangAdapter mDiZhiYaPinChaiXiangAdapter;

	private String reuwuliebiao;// 网络请求
	private String parms;
	private OnClickListener OnClick1;

	private Button ql_ruku_update;// 更新
	Bundle bundleIntent;
	Bundle bundle;
	String where; // 标识是从清分管理进入
	public static String userid1; // 角色ID
	public static String userid2; // 角色ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acctivity_boxtodizhiyapin);
		initView();
		adapter = new QingLingAdapter();
		manager = new ManagerClass();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);
		openboxtopackges.clear();
		getRuKu();
		S_application.wrong = "huitui";
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getRuKu();
			}
		};
	}

	private void initView() {
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);
		zhouto_username = (TextView) findViewById(R.id.zhouto_tvusername1);
		zhouto_username.setText("" + GApplication.user.getLoginUserName());// 操作人员

		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);

		listview = (ListView) findViewById(R.id.zhouzhuanxiang_dizhiyapin_weilistview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				manager.getRuning().runding(BoxToDiZhiYaPinZhuangdaiActivity.this, "扫描功能开启中...");
				o_Application.qlruku = o_Application.qinglingruku.get(arg2);
				Skip.skip(BoxToDiZhiYaPinZhuangdaiActivity.this, BoxToDiZhiYaPinZhuangDaiItemActivity.class, null, 0);

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_back: // 关闭当前页面
			BoxToDiZhiYaPinZhuangdaiActivity.this.finish();
			break;
		case R.id.ql_ruku_update:
			getRuKu();
			break;
		default:
			break;
		}
	}

	/**
	 * 获得任务列表信息 抵制押品拆箱装袋网络请求
	 */

	public void getRuKu() {
		manager.getRuning().runding(BoxToDiZhiYaPinZhuangdaiActivity.this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String number = GApplication.loginname;
					parms = new GetResistCollateralBaggingService().GetResistCollateralBaggingList(number);
					Log.e(TAG, "测试" + parms);
					// 返回的类型anyType{}需要进行判断
					if (parms != null && !parms.equals("anyType{}")) {
						Gson gson = new Gson();
						openboxtopackges.clear();// 每次进入后清除
						DiZhiYaPinChaiXiang DiZhiYaPinChaiXiang = gson.fromJson(parms, DiZhiYaPinChaiXiang.class);
						Log.e(TAG, "测试===" + DiZhiYaPinChaiXiang.toString());
						openboxtopackges.add(DiZhiYaPinChaiXiang);
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(BoxToDiZhiYaPinZhuangdaiActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(BoxToDiZhiYaPinZhuangdaiActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				getData();
				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);
//				new TurnListviewHeight(listview);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(BoxToDiZhiYaPinZhuangdaiActivity.this, "没有任务", new OnClickListener() {

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
	 * 显示三个列表的适配器
	 * 
	 * @author Administrator
	 *
	 */

	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(BoxToDiZhiYaPinZhuangdaiActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return o_Application.qinglingruku.size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.qinglingruku.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_walkie_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.lv_dizhiyapinid);
				view.riqi = (TextView) arg1.findViewById(R.id.dizhiyapinitem);
				view.count = (TextView) arg1.findViewById(R.id.dizhiyapintype);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText((arg0 + 1) + "");
			view.riqi.setText(o_Application.qinglingruku.get(arg0).getDanhao());
			view.count.setText(o_Application.qinglingruku.get(arg0).getZhouzhuanxiang().size() + "");
//			);
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	public void getData() {
		o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
		if (openboxtopackges.get(0).getBoxList().size() > 0) {
			for (int i = 0; i < openboxtopackges.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu(openboxtopackges.get(i).getClearTaskNum(),
						openboxtopackges.get(i).getCount(), openboxtopackges.get(i).getBoxList()));

			}

//			
//		if (rukulist[0].get("count").getValues().size() > 0) {
//			System.out.println(rukulist.length);
//			for (int i = 0; i < rukulist.length; i++) {
//						o_Application.qinglingruku.add(new QingLingRuKu(rukulist[i]
//								.get("clearTaskNum").getValues().get(0), 
//						null, rukulist[i].get(
//								"boxList").getValues()));
//				System.out.println(o_Application.qinglingruku.size());
			/*
			 * system.out.println("请领装箱入库：" +
			 * rukulist[i].get("zhouzhuanxiang").getvalues()); system.out.println("请领装箱入库："
			 * + rukulist[i].get("riqi").getvalues().get(0)); system.out.println("请领装箱入库：" +
			 * rukulist[i].get("jihuadan").getvalues().get(0));
			 */
//			}
//			Log.e("","测试"+o_Application.qinglingruku.toString());
		}

	}

}
