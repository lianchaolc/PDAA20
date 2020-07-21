package com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.SystemClock;
import android.widget.*;
import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.DiZhiYaPinRuKu;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.Skip;
import com.manager.classs.pad.ManagerClass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView.OnItemClickListener;

/***
 * 入库 任务列表选中明细查看详情扫描号
 * 
 * @author Administrator 2018_10_28
 */
public class DiZhiYaPinRuKuActivity extends Activity implements OnClickListener {
	protected static final String TAG = " DiZhiYaPinRuKuActivity";
	private ListView dzypitemListView;
	private DiZhiYaPinAdapter DiZhiYaPinAdapter;
	private List<DiZhiYaPinRuKu> dzyprukuArrayList = new ArrayList<DiZhiYaPinRuKu>();// 数据集合

	DiZhiYaPinRuKu mDiZhiYaPinRuKu = new DiZhiYaPinRuKu();// 抵制押品出库实体类
	private ManagerClass manager;
	private QingLingAdapter adapter;
	private OnClickListener OnClick1;
	private String dzyprkparms;// 获取的到网络请求的结果
	private Button ql_ruku_update;
	private TextView tvusername;// 操作人员
	private ImageView ql_ruku_back;// 返回按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinruku);
		initView();
		adapter = new QingLingAdapter();
		manager = new ManagerClass();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.dizhiyapinruku.size() > 0) {
			o_Application.dizhiyapinruku.clear();
		}
		adapter.notifyDataSetChanged();
		dzypitemListView.setAdapter(adapter);
		getRuKu();
		/*****
		 * 点击获取网络请求
		 */
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getRuKu();
			}
		};

	}

	/****
	 * 开启线程进行扫描网络
	 */
	private void getRuKu() {
		manager.getRuning().runding(DiZhiYaPinRuKuActivity.this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					dzyprkparms = new GetResistCollateralBaggingService().GetResistCollateralBaggingCleanList();
					Log.e(TAG, "测试数据源!!!!!!!!!!!!!!!!!!getRuKu" + dzyprkparms.toString());
					if (!dzyprkparms.equals("")) {
						Gson gson = new Gson();
						DiZhiYaPinRuKu[] mDiZhiYaPinRuKu = gson.fromJson(dzyprkparms, DiZhiYaPinRuKu[].class);
						Log.e(TAG, "run: " + mDiZhiYaPinRuKu[0]);
						Log.e(TAG, "run: " + mDiZhiYaPinRuKu[1]);
						for (int i = 0; i < mDiZhiYaPinRuKu.length; i++) {
							dzyprukuArrayList = Arrays.asList(mDiZhiYaPinRuKu);
						}
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

	private void initView() {
		// TODO Auto-generated method stub
		dzypitemListView = (ListView) findViewById(R.id.dzyp_weilistview);
		dzypitemListView.setAdapter(DiZhiYaPinAdapter);
		tvusername = (TextView) findViewById(R.id.dizhiyapin_tvusername1);
		tvusername.setText("" + GApplication.user.getLoginUserName());
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		// / 点击时条目跳转
		dzypitemListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (dzyprukuArrayList != null && dzyprukuArrayList.get(arg2) != null
						&& dzyprukuArrayList.get(arg2).getBagList() != null) {
					try {
						o_Application.qlruku = o_Application.qinglingruku.get(arg2);
					} catch (Exception e) {
						Log.e("liu_rui", "onItemClick: 条目点击时数组下标越界？？？");
					} finally {
						manager.getRuning().runding(DiZhiYaPinRuKuActivity.this, "扫描功能开启中...");
						Skip.skip(DiZhiYaPinRuKuActivity.this, DiZhiYaPinSaoMiaoLieBiaoActivity.class, null, 0);
						new Thread(new Runnable() {
							@Override
							public void run() {
								SystemClock.sleep(500);
								manager.getRuning().remove();
							}
						}).start();
					}
				} else {
					Toast.makeText(DiZhiYaPinRuKuActivity.this, "请检查数据", 400).show();
				}
			}
		});

	}

	/***
	 * 适配器
	 */
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinRuKuActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return dzyprukuArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dzyprukuArrayList.get(arg0);
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
			view.riqi.setText(dzyprukuArrayList.get(arg0).getCLEARTASKNUM());
			view.count.setText(dzyprukuArrayList.get(arg0).getCOUNT() + "");
			// );
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_update:// 更新网络请求
			getRuKu();

			break;
		case R.id.ql_ruku_back:
			DiZhiYaPinRuKuActivity.this.finish();
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinRuKuActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinRuKuActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				getData();
				adapter.notifyDataSetChanged();
				dzypitemListView.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinRuKuActivity.this, "没有任务", new OnClickListener() {

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

	public void getData() {

		if (dzyprukuArrayList.size() == 0) {
			return;
		} /// 每次进入要清空下集合否则数据重复
		o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
		int listSize = 0;
		for (DiZhiYaPinRuKu diZhiYaPinRuKu : dzyprukuArrayList) {
			if (diZhiYaPinRuKu.getBagList().size() > 0) {
				listSize = 1;
				break;
			}
		}
		if (listSize > 0) {
			for (int i = 0; i < dzyprukuArrayList.size(); i++) {

				o_Application.qinglingruku.add(new QingLingRuKu(dzyprukuArrayList.get(i).getCLEARTASKNUM(),
						dzyprukuArrayList.get(i).getCOUNT() + "", dzyprukuArrayList.get(i).getBagList()));

				System.out.println(o_Application.qinglingruku.size());
				/*
				 * system.out.println("请领装箱入库：" +
				 * rukulist[i].get("zhouzhuanxiang").getvalues()); system.out.println("请领装箱入库："
				 * + rukulist[i].get("riqi").getvalues().get(0)); system.out.println("请领装箱入库：" +
				 * rukulist[i].get("jihuadan").getvalues().get(0));
				 */
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DiZhiYaPinRuKuActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
