package com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.SystemClock;
import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.LocationManagerCode;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.entity.GetAccountTurnOverLineListEntity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.AccountInformationService;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.manager.classs.pad.ManagerClass;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 入账户中心
 * 
 * @author Administrator
 *
 */
public class RuZhangHuZhongXinActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "RuZhangHuZhongXinActivity";
	// 组件
	private ListView libiaoListview;/// 数据列表
	private ImageView ql_ruku_back;// 返回按钮
	private List<GetAccountTurnOverLineListEntity> lineList = new ArrayList<GetAccountTurnOverLineListEntity>();
	private List<GetAccountTurnOverLineListEntity> GetAccountlineList = new ArrayList<GetAccountTurnOverLineListEntity>();
	// 实体类

	private ManagerClass manager;// 提示
	private String netresult;
	private QingLingAdapter adapter;// 适配器
	private OnClickListener OnClick1;
	private TextView ora;// 操作人

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruzhanghuzhongxin);
		initView();
		adapter = new QingLingAdapter();
		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getAccountTurnOverLineList();
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
		libiaoListview.setAdapter(adapter);
		lineList.clear();
		getAccountTurnOverLineList();

		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getAccountTurnOverLineList();
			}
		};
	}

	/***
	 * 网络请求 查询待做的线路列表以及具体周转箱号以及数量
	 */
	private void getAccountTurnOverLineList() {
//		manager.getRuning().runding(RuZhangHuZhongXinActivity.this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = GApplication.user.getYonghuZhanghao();
					netresult = new AccountInformationService().getAccountTurnOverLineList(number);
					Log.i(TAG, "测试数据源" + netresult.toString());
					if (!netresult.equals("")) {
						Gson gson = new Gson();
						GetAccountTurnOverLineListEntity[] gatolle = gson.fromJson(netresult,
								GetAccountTurnOverLineListEntity[].class);

						lineList.clear();
						// 可以预防出现数据个是不支持的异常
						GetAccountlineList = Arrays.asList(gatolle);
						List arrList = new ArrayList(GetAccountlineList);
						for (int i = 0; i < gatolle.length; i++) {
							lineList.addAll(Arrays.asList(gatolle));
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

	/***
	 * 组件
	 */
	private void initView() {
		ora = (TextView) findViewById(R.id.accountoravuser);
		// TODO Auto-generated method stub
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ora.setText(GApplication.user.getLoginUserName());
		ql_ruku_back.setOnClickListener(this);
		libiaoListview = (ListView) findViewById(R.id.ruzhanghuzhongxin_listview);

		libiaoListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				o_Application.qlruku = o_Application.qinglingruku.get(arg2);
				manager.getRuning().runding(RuZhangHuZhongXinActivity.this, "扫描功能开启中...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(1000);
						manager.getRuning().remove();
					}
				}).start();
				Intent i = new Intent(RuZhangHuZhongXinActivity.this, ZhouZhuanXiangHeDuiActivity.class);
				i.putExtra("lin", o_Application.qinglingruku.get(arg2).getRiqi());
				startActivity(i);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			RuZhangHuZhongXinActivity.this.finish();

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
				manager.getAbnormal().timeout(RuZhangHuZhongXinActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(RuZhangHuZhongXinActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				getData();
				adapter.notifyDataSetChanged();
				libiaoListview.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(RuZhangHuZhongXinActivity.this, "没有任务", new OnClickListener() {

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
		o_Application.dizhiyapinruku.clear();/// 每次进入要清空下集合否则数据重复
		if (lineList.size() == 0) {
			return;
		}
		if (lineList.get(0).getDetailList().size() > 0) {
			System.out.println(lineList.size() + "===============");
			for (int i = 0; i < lineList.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu(lineList.get(i).getLINENAME(),
						lineList.get(i).getPSLINE() + "", lineList.get(i).getDetailList()));

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

	/***
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(RuZhangHuZhongXinActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return lineList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lineList.get(arg0);
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
			view.danhao.setText(lineList.get(arg0).getPSLINE() + "");// 显示编号
			view.riqi.setText(lineList.get(arg0).getLINENAME()); // 线路名
			view.count.setText(lineList.get(arg0).getDetailList().size() + "");// 数量
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

}
