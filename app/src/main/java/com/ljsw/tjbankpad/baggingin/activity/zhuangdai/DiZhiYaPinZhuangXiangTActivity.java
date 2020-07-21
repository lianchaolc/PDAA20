package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.LocationManagerCode;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangDaiItemActivity;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;

import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 抵质押品装箱 清分员操作
 * 
 * @author Administrator 清分员登陆后查看是否有自己的相关任务
 */
public class DiZhiYaPinZhuangXiangTActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPinZhuangXiangTActivity";
	// 组件
	private ImageView upData;// 更新数据
	private TextView tv_caozuoren, tv_caozuoren2;// 操作人员1 操作人员2
	private ListView dzyp_zhuangxiang_listview;// listview列表
	private ImageView ql_ruku_back; // 返回按钮
	private Button ql_ruku_update;
	private QingLingAdapter dzypzxadapter;// 适配器
	private List<DiZhiYaPinZhuangXiangEnTity> dzypzdlist = new ArrayList<DiZhiYaPinZhuangXiangEnTity>();// 数据集合
	private ManagerClass manager;// 提示
	// 数据源
	private String resultparms;// 获取网络
	private OnClickListener OnClick1;
	private String TaskNum = "";// 任务编号
	private DiZhiYaPinZhuangXiangEnTity dzypzxet = new DiZhiYaPinZhuangXiangEnTity();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinzhungxiang);
		initView();
		getZhuangXiang();// 获取网络信息
		dzypzxadapter = new QingLingAdapter();
		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getZhuangXiang();
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		dzypzxadapter.notifyDataSetChanged();
		dzyp_zhuangxiang_listview.setAdapter(dzypzxadapter);
		dzypzdlist.clear();
		getZhuangXiang();

		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getZhuangXiang();
			}
		};
	}

	private void initView() {
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back); // /返回
		ql_ruku_back.setOnClickListener(this);
		tv_caozuoren = (TextView) findViewById(R.id.zd_tvusername1);// 操作人1
		tv_caozuoren.setText("" + GApplication.user.getLoginUserName());
		dzyp_zhuangxiang_listview = (ListView) findViewById(R.id.dzyp_zhuangxiang_listview);
		// TODO Auto-generated method stub
		dzyp_zhuangxiang_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//						 2  还没交接 3  已经拿到任务
				System.out.print("state===" + dzypzdlist.get(arg2).getState());
				if (dzypzdlist.get(arg2).getState().equals("2")) {
					showBigToast("请领取抵质押品", 500);
				} else if (dzypzdlist.get(arg2).getState().equals("3")) {
					manager.getRuning().runding(DiZhiYaPinZhuangXiangTActivity.this, "扫描功能开启中...");
					o_Application.qlruku = o_Application.qinglingruku.get(arg2);
					Intent intent = new Intent(DiZhiYaPinZhuangXiangTActivity.this,
							DiZhiYaPinZhuangDaiHeDuiActivity.class);
					intent.putExtra("TaskNum", TaskNum);
					startActivity(intent);
				}
			}
		});
	}

	/***
	 * 网络请求抵制押品装箱
	 */
	public void getZhuangXiang() {
//		manager.getRuning()
//				.runding(QingLingZhuangXiangRuKu_db.this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String number = GApplication.user.getYonghuZhanghao();
					resultparms = new GetResistCollateralBaggingService().CleanMantoBoxList(number);
					if (resultparms != null && !resultparms.equals("anyType{}")) {
						Gson gson = new Gson();
						DiZhiYaPinZhuangXiangEnTity dzypzxet = gson.fromJson(resultparms,
								DiZhiYaPinZhuangXiangEnTity.class);
						dzypzdlist.clear();
						dzypzdlist.add(dzypzxet);// 添加数据
						Log.e(TAG, "测试长度===" + dzypzdlist.size());

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_back:// 关闭当前页面
			DiZhiYaPinZhuangXiangTActivity.this.finish();
			break;

		case R.id.ql_ruku_update:
			getZhuangXiang();// 刷新数据
			break;
		default:
			break;
		}

	}

	/***
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinZhuangXiangTActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return dzypzdlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dzypzdlist.get(arg0);
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
			view.riqi.setText(dzypzdlist.get(arg0).getClearTaskNum());
			view.count.setText(dzypzdlist.get(arg0).getBagList().size() + "");
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	/***
	 * 全局的集合添加数据用于扫描数据源对比
	 */
	public void getData() {
		o_Application.qinglingruku.clear();
		if (dzypzdlist.get(0).getBagList().size() > 0) {
			for (int i = 0; i < dzypzdlist.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu(dzypzdlist.get(i).getClearTaskNum() + "",
						dzypzdlist.get(i).getCount(), dzypzdlist.get(i).getBagList()));
				TaskNum = dzypzdlist.get(i).getClearTaskNum();// 需要传的任务编号
			}
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangTActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangTActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				getData();
				dzypzxadapter.notifyDataSetChanged();
				dzyp_zhuangxiang_listview.setAdapter(dzypzxadapter);
				new TurnListviewHeight(dzyp_zhuangxiang_listview);// 放开
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangTActivity.this, "没有任务", new OnClickListener() {

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

	/**
	 * 自定义 toast， 增大字体
	 *
	 * @param info     提示信息
	 * @param duration 显示时间，0：短时间，1：长时间
	 */
	public void showBigToast(String info, int duration) {
//        Toast toast = new Toast(kf.getActivity());

		Toast toast = new Toast(DiZhiYaPinZhuangXiangTActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(DiZhiYaPinZhuangXiangTActivity.this);
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

}
