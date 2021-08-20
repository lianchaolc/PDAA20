package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.DiZhiYaPinRuKu;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;

/***
 * 出库任务列表 账户资料出库借阅
 * 
 * @author Administrator 2018_11-14 lc 库管员》 账户中心
 */
public class ZhangHuZiLiaoChuKuJieYueActivity extends Activity implements OnClickListener {
	private static final String TAG = "ZhangHuZiLiaoChuKuJieYueActivity";
	private TextView caozuorenyaun;
	private TextView caozuoren;
	private ListView zhanghuziliaorenwuliebiao;
	private ImageView ivblack;
	private Button ql_ruku_update;// 更新请求
	/// 变量
	private String userZhangHu = "";// 传输的登录人员
	private String netResultTask = "";// 网络返回的任务列表
	// 页面点击
	private ManagerClass manager;
	private OnClickListener OnClick1;
	// 实体类
	private OutboundBorrowingTaskListEntity obbtle = new OutboundBorrowingTaskListEntity();
	// 数据源接受
	private List<OutboundBorrowingTaskListEntity> Outboundtasklist = new ArrayList<OutboundBorrowingTaskListEntity>();// 获取任务列表
	// 适配器
	private QingLingAdapter adapter;
	public static ZhangHuZiLiaoChuKuJieYueActivity instance = null;
	private ManagerClass managerClass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaochukujieyue);
		instance = this;
		initView();
		adapter = new QingLingAdapter();

		manager = new ManagerClass();
		managerClass = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getOutboundBorrowingTaskList();// 网络请求
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		getOutboundBorrowingTaskList();// 网络请求();
//		adapter.notifyDataSetChanged();
//		zhanghuziliaorenwuliebiao.setAdapter(adapter);


		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getOutboundBorrowingTaskList();// 网络请求();
			}
		};
	}

	/***
	 * 出库借阅任务列表
	 */

	public void getOutboundBorrowingTaskList() {
		new Thread() {
			@SuppressLint("LongLogTag")
			@Override
			public void run() {
				super.run();
				try {
					// 账号
					managerClass.getRuning().runding(ZhangHuZiLiaoChuKuJieYueActivity.this, "正在登录...");
					userZhangHu = GApplication.user.getYonghuZhanghao();
					Log.i(TAG, "账户名称===" + userZhangHu); // /网络请求
					netResultTask = new AccountInfomationReturnService().getAccountOutTaskList(userZhangHu);
					Outboundtasklist.clear();
					Log.d(TAG, "netResultTask===" + netResultTask); // /网络请求
					if (netResultTask != null && !netResultTask.equals("anyType{}")) {
						Gson gson = new Gson();
						OutboundBorrowingTaskListEntity[] obbtlelenth = gson.fromJson(netResultTask,
								OutboundBorrowingTaskListEntity[].class);

						List<OutboundBorrowingTaskListEntity> listobtlentity = Arrays.asList(obbtlelenth);
						List arrList = new ArrayList(listobtlentity);
						Outboundtasklist.clear();
						Outboundtasklist.addAll(arrList);
						if(null==Outboundtasklist){
							handler.sendEmptyMessage(3);
						}else{
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
					Log.e(TAG, "异常====" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	private void initView() {
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);// 更新代码
		ql_ruku_update.setOnClickListener(this);
		caozuorenyaun = (TextView) findViewById(R.id.accountoutbound_operater); // 操作人员
		caozuorenyaun.setText(GApplication.user.getLoginUserName());
		ivblack = (ImageView) findViewById(R.id.ql_ruku_back);
		ivblack.setOnClickListener(this);
		zhanghuziliaorenwuliebiao = (ListView) findViewById(R.id.zhanghuziliaorenwuliebiao);
		zhanghuziliaorenwuliebiao.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("LongLogTag")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent mIntent = new Intent(ZhangHuZiLiaoChuKuJieYueActivity.this,
						ZhangHuZiLiaoChuRuKuMingXiActivity.class);
				if(Outboundtasklist.get(arg2).getCVOUN().equals("")||Outboundtasklist.get(arg2).getFLAG().equals("")){
					Toast.makeText(ZhangHuZiLiaoChuKuJieYueActivity.this,"数据不正确",Toast.LENGTH_SHORT).show();
				}else{
					Log.i(TAG, "账户名称===" + Outboundtasklist.get(arg2).getCVOUN()+"!!!!!!!!!!!"+Outboundtasklist.get(arg2).getFLAG()); // /网络请求
					mIntent.putExtra("cvoun", Outboundtasklist.get(arg2).getCVOUN());// 传值的cvoun号
					mIntent.putExtra("FLAG", Outboundtasklist.get(arg2).getFLAG());// 类型
					startActivity(mIntent);
				}


			}
		});
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			managerClass.getRuning().remove();
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuKuJieYueActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuKuJieYueActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:

				manager.getRuning().remove();
				zhanghuziliaorenwuliebiao.setAdapter(adapter);
				new TurnListviewHeight(zhanghuziliaorenwuliebiao);// 放开
				adapter.notifyDataSetChanged();
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoChuKuJieYueActivity.this, "没有任务", new OnClickListener() {

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

	/****
	 * 添加数据
	 */

	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoChuKuJieYueActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return Outboundtasklist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return Outboundtasklist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_accountintionouthous_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.zhziliao_id);
				view.riqi = (TextView) arg1.findViewById(R.id.zhziliao_item);
				view.count = (TextView) arg1.findViewById(R.id.zhziliao_type_tv);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText(Outboundtasklist.get(arg0).getCVOUN() + "");
			view.riqi.setText(Outboundtasklist.get(arg0).getCOUNT());
			view.count.setText(Outboundtasklist.get(arg0).getFLAG() + "");
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
		case R.id.ql_ruku_back:
			ZhangHuZiLiaoChuKuJieYueActivity.this.finish();

			break;
		case R.id.ql_ruku_update:
			getOutboundBorrowingTaskList();
			break;
		default:
			break;
		}
	}

}
