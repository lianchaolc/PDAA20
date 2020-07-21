package com.pda.checksupplement.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.manager.classs.pad.ManagerClass;
import com.pda.checksupplement.entity.ChelibrarySupplemnetBaseEntity;
import com.pda.checksupplement.service.CheckLibrarySupplementService;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/***
 * 库存管理
 * 
 * @author Administrator 库管员 查库盘库查库补录 lc 20181_11_29 1 改动 2019_3_6
 */
public class CheckLibrarySupplementActivity extends Activity implements OnClickListener {
	protected static final String TAG = "CheckLibrarySupplementActivity";
	// 组件
	private TextView CheckLSoprater;// 查库人， 后退
	private ListView CheckLS_listview; // listview
	private ImageView CheckLSBlack;// 更新请求数据
	private Button CheckLs_ql_ruku_update; /// 盘库查库更新
	// 数据源
	private List<ChelibrarySupplemnetBaseEntity> clsList = new ArrayList<ChelibrarySupplemnetBaseEntity>();
	// 实体类
	ChelibrarySupplemnetBaseEntity csbe = new ChelibrarySupplemnetBaseEntity();
	// 适配器
	private QingLingAdapter adapter;
	private ManagerClass manager;

	private OnClickListener OnClick1;
	// 变量
	private String parms;// 接受网络返回结果
	private String accrossTaskItem;// 传任务；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acctivity_checklibrary_supplement);
		initView();
		adapter = new QingLingAdapter();
		manager = new ManagerClass();

	}

	private void initView() {
		CheckLs_ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		CheckLs_ql_ruku_update.setOnClickListener(this);// 更新
		CheckLSBlack = (ImageView) findViewById(R.id.ql_ruku_back);// 返回
		CheckLSBlack.setOnClickListener(this);

		CheckLSoprater = (TextView) findViewById(R.id.checkls_tvusername);
		CheckLSoprater.setText(GApplication.user.getLoginUserName());
		CheckLS_listview = (ListView) findViewById(R.id.cls_listview);
		CheckLS_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				manager.getRuning().runding(CheckLibrarySupplementActivity.this, "扫描功能开启中...");
				Intent mIntent = new Intent(CheckLibrarySupplementActivity.this,
						ChecklibraryReplenishmentActivity.class);
				accrossTaskItem = clsList.get(arg2).getTASKNUM();
				Log.e(TAG, "=====" + accrossTaskItem);
				mIntent.putExtra("Taskitem", accrossTaskItem); //
				startActivity(mIntent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
//		adapter.notifyDataSetChanged();
//		CheckLS_listview.setAdapter(adapter);
		clsList.clear();
		getCheckLibrarySupplementTaskList();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getCheckLibrarySupplementTaskList();
			}

		};
	}

	/***
	 * 网络请求获取任务列表
	 */
	String countforTask = "0";

	public void getCheckLibrarySupplementTaskList() {
		manager.getRuning().runding(CheckLibrarySupplementActivity.this, "数据加载中...");
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
							handler.sendEmptyMessage(3);
						} else {
							handler.sendEmptyMessage(2);
						}

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
				manager.getAbnormal().timeout(CheckLibrarySupplementActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(CheckLibrarySupplementActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				adapter.notifyDataSetChanged();
				CheckLS_listview.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(CheckLibrarySupplementActivity.this, "没有任务", new OnClickListener() {
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
		LayoutInflater lf = LayoutInflater.from(CheckLibrarySupplementActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return clsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return clsList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_checklibrarysupplement_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.checklibrarysupplement_id);
				view.count = (TextView) arg1.findViewById(R.id.checklibrarysupplement_tv);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText(clsList.get(arg0).getTASKNUM() + "");

			view.count.setText(clsList.get(arg0).getMISSCOUNT());
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_update:
			getCheckLibrarySupplementTaskList();
			break;
		case R.id.ql_ruku_back:
			CheckLibrarySupplementActivity.this.finish();

			break;
		default:
			break;
		}

	}

}
