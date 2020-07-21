package com.example.pda;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunyuanSelectTaskEntity;
import com.ljsw.tjbankpda.main.ZhouzhuanxiangMenu;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 判断押运员是否需要领取任务列表
 * 
 * @author Administrator 20191126 判断押运员当前是没有任务 需要选择任务列表 listview 的条目点击事件
 * 
 * 
 */
public class YayunSelectTaskActivity extends Activity implements OnClickListener {
	protected static final String TAG = "YayunSelectTaskActivity";
	private ListView mlistview; // listview 组件shiyong
	private LiebiaoAdapter liebiaoadapter;
	private ManagerClass manager;
	private OnClickListener OnClick1;
	private ImageView yayun_backS1, yyrw_update;// 返回 和刷新
	private List<YayunyuanSelectTaskEntity> YayunyuanSelectTaskEntitylist = new ArrayList<YayunyuanSelectTaskEntity>();; // 显示列表数据的集合

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yayun_select_task);
		Log.e(TAG,
				"----GApplication.use.getUserzhanghu()" + GApplication.use.getUserzhanghu() + "系统的账号"
						+ GApplication.user.getYonghuZhanghao() + "" + "S_application.getApplication().s_userYayun押运员"
						+ S_application.getApplication().s_userYayun);

		liebiaoadapter = new LiebiaoAdapter();
		manager = new ManagerClass();
		loadData();
		LoadDataNow();
		initview();

	}

	/***
	 * 获取押运员无任务获取的线路数据
	 */
	private void LoadDataNow() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.e(TAG, "----GApplication.use.getUserzhanghu()" + GApplication.use.getUserzhanghu());
					// 用户账号
//					String userZhanghu = GApplication.use.getUserzhanghu();
					String userZhanghu = S_application.getApplication().s_userYayun;
					String netresultClean = new AccountAndResistCollateralService().getnotastlindata(userZhanghu);
					Log.e(TAG, "返回原数据" + netresultClean.toString());
					if (!netresultClean.equals("") || netresultClean != null
							|| !netresultClean.equals("返回原数据该押运员没有正在执行的派工单任务")) {
						// 数据返回
						Gson gson = new Gson();
						Log.e(TAG, "测试数据源" + netresultClean.toString());
						YayunyuanSelectTaskEntitylist.clear();
						Type type = new TypeToken<ArrayList<YayunyuanSelectTaskEntity>>() {
						}.getType();

						List<YayunyuanSelectTaskEntity> list = gson.fromJson(netresultClean, type);
						for (int i = 0; i < list.size(); i++) {
							Log.e(TAG, "测试数据源" + list.get(i).getLinename());
							Log.e(TAG, "测试数据源" + list.get(i).getQinglingstate());
						}
						YayunyuanSelectTaskEntitylist = list;
						Log.d(TAG, "----YayunyuanSelectTaskEntitylist" + YayunyuanSelectTaskEntitylist.size());

						if (null != YayunyuanSelectTaskEntitylist && YayunyuanSelectTaskEntitylist.size() > 0) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(3);
						}
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	private void initview() {
		yyrw_update = (ImageView) findViewById(R.id.yyrw_update);
		yyrw_update.setOnClickListener(this);
		yayun_backS1 = (ImageView) findViewById(R.id.yayun_backS1);
		yayun_backS1.setOnClickListener(this);
		mlistview = (ListView) findViewById(R.id.yyselectlistView);
		mlistview.setAdapter(liebiaoadapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(YayunSelectTaskActivity.this, YayunSelectRewuTookinfoAndTrueictivity.class);
				intent.putExtra("linname", YayunyuanSelectTaskEntitylist.get(arg2).getLinenum());
				startActivity(intent);

			}

		});
	}

	/**
	 * 只要获取任务后的状态改变
	 */
	public void loadData() {

		liebiaoadapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mlistview.setAdapter(liebiaoadapter);
		liebiaoadapter.notifyDataSetChanged();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yayun_backS1:
			YayunSelectTaskActivity.this.finish();
			break;
		case R.id.yyrw_update:
			// 刷新数据
			LoadDataNow();
			break;
		default:
			break;
		}
	}

	class LiebiaoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return YayunyuanSelectTaskEntitylist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return YayunyuanSelectTaskEntitylist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("NewApi")
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			KongjianEntity entity;
			if (arg1 == null) {
				entity = new KongjianEntity();
				arg1 = LayoutInflater.from(YayunSelectTaskActivity.this).inflate(R.layout.listview_yayunyuan_item,
						null);
				entity.textViewline = (TextView) arg1.findViewById(R.id.yayunyuanlinenum);
				entity.textViewname = (TextView) arg1.findViewById(R.id.yayunyuanlinename);
				entity.yayunyuanlineturefalsetask = (TextView) arg1.findViewById(R.id.yayunyuanlineturefalsetask);
				arg1.setTag(entity);
			} else {
				entity = (KongjianEntity) arg1.getTag();
			}
			entity.textViewline.setText(YayunyuanSelectTaskEntitylist.get(arg0).getLinenum());
			entity.textViewname.setText(YayunyuanSelectTaskEntitylist.get(arg0).getLinename());
			if (YayunyuanSelectTaskEntitylist.get(arg0).getQinglingstate().equals("n")) {
				entity.yayunyuanlineturefalsetask.setText("无");

			} else {
				entity.yayunyuanlineturefalsetask.setText("有");
			}
			return arg1;
		}
	}

	/*
	 * listView 控件实体类
	 */
	static class KongjianEntity {
		public TextView textViewline;// 线路
		public TextView textViewname; // 名称
		public TextView yayunyuanlineturefalsetask;
	}

	/***
	 * 领取任务改变数据库状态
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectTaskActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectTaskActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				// getData();
				// adapter.notifyDataSetChanged();
				// dzypitemListView.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectTaskActivity.this, "没有查询到数据", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:
				// 获取网络数据后适配到listview并可以点击进入下一个领取页面
				mlistview.setAdapter(liebiaoadapter);
				liebiaoadapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
//		S_application.getApplication().s_userYayun=null; 
		super.onPause();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (S_application.getApplication().s_userYayun != null) {
//				S_application.getApplication().s_userYayun = null;
//			}
			Skip.skip(YayunSelectTaskActivity.this, ZhouzhuanxiangMenu.class, null, 0);
			YayunSelectTaskActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
