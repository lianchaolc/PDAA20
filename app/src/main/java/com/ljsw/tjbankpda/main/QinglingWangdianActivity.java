package com.ljsw.tjbankpda.main;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ljsw.tjbankpda.qf.entity.OrderInfo;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.manager.classs.pad.ManagerClass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 请领装箱网点界面
 * 
 * @time 2015-06-19 17:38
 * @author FUHAIQING 选中要装那些类型的物品 复选框（现金/重空/抵质/）
 */
public class QinglingWangdianActivity extends FragmentActivity {
	protected static final String TAG = "QinglingWangdianActivity";
	/* 定义控件变量 */
	private ListView lvWangdian;
	private WangdianBaseAdapter wa;
	private TextView tvXianluName;
	private ImageView ivBack;

	/* 定义全局变量 */
	private String XianluName;// 线路名称
	private String XianluId;// 线路Id
	private String renwudan;// 任务单
	private Dialog dialogfa;// 失败
//	现金重空抵质押品

	private String collateral = "";
	private String collateralstate = "3";// 判断能否跳转下一个页面

	List<OrderInfo> ltOrder = new ArrayList<OrderInfo>();// 订单列表
	private List<List<Integer>> ltCurrent_Order = new ArrayList<List<Integer>>();// 网点显示状态列表
	private List<Integer> ltCurrent_WD = new ArrayList<Integer>();
	private List<Integer> ltCurrent_OrderCount = new ArrayList<Integer>();// 已选择的订单数量

	private ManagerClass manager;// 弹出框
	private Table[] RenwuData;

	private String requestResult = "";// 接受后台返回结果不让勾选的结果
	private String Taskid = "";// 任务单号
	private List<String> checklist = new ArrayList<String>();// 接受后台传过来的字符串实物没有在的情况
	private Handler okHandle = new Handler() {// 数据获取成功handler

		public void handleMessage(Message msg) {
			// 初始化列表
			ltOrder.clear();
			ltCurrent_Order.clear();
			ltCurrent_WD.clear();
			ltCurrent_OrderCount.clear();

			// 绑定Adapter
			List<String> ltw = RenwuData[0].get("wangdianming").getValues();
			if (ltw.size() > 0) {
				for (int i = 0; i < ltw.size(); i++) {
					String str = RenwuData[0].get("dingdanhao").get(i);
					List<String> lt = new ArrayList<String>();
					if (!"-1".equals(str)) {
						String[] orderNum = str.split("_");// 201901143020110004_DYXD12112019011402_201901143020120004
						for (int j = 0; j < orderNum.length; j++) {
							lt.add(orderNum[j]);

						}
					}
					ltOrder.add(new OrderInfo(RenwuData[0].get("wangdianming").get(i), lt,
							RenwuData[0].get("jigouid").get(i)));

				}
			}
			// 根据ltOrder添加标识
			for (int i = 0; i < ltOrder.size(); i++) {
				List<Integer> ltt = new ArrayList<Integer>();
				for (int j = 0; j < ltOrder.get(i).getLtOrder().size(); j++) {
					ltt.add(1);
				}
				ltCurrent_Order.add(ltt);
				if (i == 0) {
					ltCurrent_WD.add(1);
				} else {
					ltCurrent_WD.add(0);
				}
				ltCurrent_OrderCount.add(ltOrder.get(i).getLtOrder().size());
			}

			wa.notifyDataSetInvalidated(); // 更新数据
			manager.getRuning().remove();
		};
	};
	private Handler timeoutHandle = new Handler() {// 连接超时handler
		public void handleMessage(Message msg) {
			manager.getRuning().remove();
			if (msg.what == 0) {
				manager.getAbnormal().timeout(QinglingWangdianActivity.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						getDate();
						manager.getAbnormal().remove();
						manager.getRuning().runding(QinglingWangdianActivity.this, "数据加载中...");
					}
				});
			}
			if (msg.what == 1) {
				manager.getAbnormal().timeout(QinglingWangdianActivity.this, "网络连接失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						getDate();
						manager.getAbnormal().remove();
						manager.getRuning().runding(QinglingWangdianActivity.this, "数据加载中...");
					}
				});
			}
			if (msg.what == 2) {
				dialogfa = new Dialog(QinglingWangdianActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(QinglingWangdianActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("请领取抵质押品");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				if (butfa != null) {
					butfa.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							dialogfa.dismiss();
						}

					});
				}
				dialogfa.show();

			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_qingfenrenwu_qinling_wangdian);
		lvWangdian = (ListView) findViewById(R.id.lv_qingfenrenwu_qingling_wangdian_Info);
		tvXianluName = (TextView) findViewById(R.id.tv_qingfenrenwu_qingling_wangdian_zhihangName);
		ivBack = (ImageView) findViewById(R.id.iv_qingfenrenwu_qingling_wangdian_back);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				QinglingWangdianActivity.this.finish();
			}
		});

		manager = new ManagerClass();
		manager.getRuning().runding(this, "数据加载中...");

		// 获得线路名称
		Bundle bundle = super.getIntent().getExtras();
		XianluName = bundle.getString("XianluName");
		XianluId = bundle.getString("XianluId");
		renwudan = bundle.getString("renwudan");
		tvXianluName.setText(XianluName);

		findListView(); // 绑定适配器
		Taskid = renwudan;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getDate();
		getUncheckCovuns();
	}

	/**
	 * 绑定listView
	 */
	private void findListView() {
		wa = new WangdianBaseAdapter(ltOrder);
		lvWangdian.setAdapter(wa);
	}

	/**
	 * 获取数据
	 */
	private void getDate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String params;
				try {

					// 访问服务器0
					System.out.println("xianluId==" + XianluId + "renwudan==" + renwudan);
					params = new QingfenRenwuService().getQinglingMingxi(XianluId, renwudan);
					RenwuData = Table.doParse(params);
					okHandle.sendEmptyMessage(0);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	/***
	 * 获取检查抵制押品是否应该通过
	 */
	private String checkIfHasGotCollateral() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String params;
				try {

					// 访问服务器0
					System.out.println("collateral=" + collateral); // collateral
					params = new QingfenRenwuService().checkIfHasGotCollateral(collateral);
//					RenwuData=Table.doParse(params);
					if (params.equals("99")) {
						;// 不成功
						collateralstate = "2";
					} else {
						collateralstate = "3";

					}

				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(1);
				}
			}
		}).start();
		return collateralstate;
	}

	/**
	 * 网点Adapter
	 */
	class WangdianBaseAdapter extends BaseAdapter {
		private List<OrderInfo> lt;
		private ViewHolder vh;

		public WangdianBaseAdapter(List<OrderInfo> lt) {
			super();
			this.lt = lt;
		}

		@Override
		public int getCount() {
			return lt.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lt.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			System.out.println("-----wa更新-----");
			LayoutInflater inflater = LayoutInflater.from(QinglingWangdianActivity.this);
			if (v == null) {
				v = inflater.inflate(R.layout.item_qingfengrenwu_qingfen_wangdian, null);
				vh = new ViewHolder();
				vh.tvWangdianName = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_wangdain_name);
				vh.cbAllSelect = (CheckBox) v.findViewById(R.id.cb_item_qingfenrenwu_qingling_wangdain_allcheck);
				vh.tvSelectCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_wangdain_orderCount);
				vh.btnOk = (Button) v.findViewById(R.id.btn_item_qingfenrenwu_qingling_wangdain_queding);
				vh.lvOrderNo = (ListView) v.findViewById(R.id.lv_item_qingfenrenwu_qingling_wangdain_orderNo);
				vh.rlTitle = (RelativeLayout) v.findViewById(R.id.rl_item_qingfenrenwu_qingling_wangdain_title);
				vh.llInfo = (LinearLayout) v.findViewById(R.id.ll_item_qingfenrenwu_qingling_wangdain_order);
				v.setTag(vh);
			} else {
				vh = (ViewHolder) v.getTag();
			}
			// 设置网点名字
			vh.tvWangdianName.setText(lt.get(arg0).getName());
			// 遍历计算已选择的订单数量
			int a = 0;// a为过渡使用的变量
			for (int i = 0; i < ltCurrent_Order.get(arg0).size(); i++) {
				if (ltCurrent_Order.get(arg0).get(i) == 1) {
					a++;
				}
			}
			ltCurrent_OrderCount.set(arg0, a);
			// 判断是否有选中的订单,如果没有,确认按钮为不可点击状态
			if (a == 0) {
				vh.btnOk.setEnabled(false);
				vh.btnOk.setBackgroundResource(R.drawable.button_gray);
			} else {
				vh.btnOk.setEnabled(true);
				vh.btnOk.setBackgroundResource(R.drawable.buttom_selector_bg);
			}
			// 如果已选择的订单数量与全部订单数量不相等,则全选单选框为false,相等为true
			if (ltCurrent_Order.get(arg0).size() == ltCurrent_OrderCount.get(arg0)) {
				vh.cbAllSelect.setChecked(true);
			} else {
				vh.cbAllSelect.setChecked(false);
			}
			// 显示已选择的订单数量
			vh.tvSelectCount.setText("已选：" + ltCurrent_OrderCount.get(arg0) + "个订单");
			vh.rlTitle.setOnClickListener(new OpenOnClickListener(arg0));// 给网点标题栏添加点击事件
			vh.cbAllSelect.setOnCheckedChangeListener(new SelectAllListener(arg0));
			vh.btnOk.setOnClickListener(new OKOnClickListener(arg0));// 确认按钮监听
			// 绑定订单列表Adapter
			vh.lvOrderNo.setAdapter(new WandianOrderBaseAdapter(lt.get(arg0).getLtOrder(), arg0));
			// 根据标识来设置当前选中网点,选中的网点则标识为1.
			if (ltCurrent_WD.get(arg0) == 1) {
				vh.llInfo.setVisibility(View.VISIBLE);
			} else {
				vh.llInfo.setVisibility(View.GONE);
			}
			new TurnListviewHeight(vh.lvOrderNo);
			return v;
		}

		public class ViewHolder {
			CheckBox cbAllSelect;// 全选checkbox
			TextView tvWangdianName;// 网点名称
			TextView tvSelectCount;// 已选择订单的数量
			ListView lvOrderNo;// 订单列表
			Button btnOk;// 确定按钮
			RelativeLayout rlTitle;// 网点标题布局
			LinearLayout llInfo;// 网点内容布局
		}

		/**
		 * 全选按钮监听
		 */
		class SelectAllListener implements OnCheckedChangeListener {
			private int position;

			public SelectAllListener(int position) {
				super();
				this.position = position;
			}

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				System.out.println("++++点击全选,当前状态为" + arg1 + ",网点名为=" + ltOrder.get(position).getName());
				if (arg1) {
					for (int i = 0; i < ltCurrent_Order.get(position).size(); i++) {
						ltCurrent_Order.get(position).set(i, 1);

					}
				} else {
					int checkCount = 0;
					for (int i = 0; i < ltCurrent_Order.get(position).size(); i++) {
						if (ltCurrent_Order.get(position).get(i) == 1) {
							checkCount++;
						}
					}
					if (checkCount == ltCurrent_Order.get(position).size()) {
						for (int i = 0; i < ltCurrent_Order.get(position).size(); i++) {
							ltCurrent_Order.get(position).set(i, 0);
						}
					}
				}
				System.out.println("test--" + ltCurrent_Order.toString());
				wa.notifyDataSetChanged();
			}
		}

		/**
		 * 网点订单内容展示 一次只会显示一个网点的订单号
		 */
		class OpenOnClickListener implements OnClickListener {
			private int positon;

			public OpenOnClickListener(int positon) {
				super();
				this.positon = positon;
			}

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < ltCurrent_WD.size(); i++) {
					if (i == positon) {
						ltCurrent_WD.set(i, 1);
					} else {
						ltCurrent_WD.set(i, 0);
					}
				}
				wa.notifyDataSetChanged();
			}
		}

		/**
		 * 确认按钮监听 跳转到请领装箱明细界面,同时传递订单号和标签
		 */

		private boolean check = false;
		Bundle bundle = new Bundle();
		String qlNumSbstr = "";

		class OKOnClickListener implements OnClickListener {
			private int positon;

			public OKOnClickListener(int positon) {
				super();
				this.positon = positon;
			}

			@Override
			public void onClick(View arg0) {
				checkIfHasGotCollateral();
				bundle.putInt("biaoqian", 1);
				StringBuffer qlNumSb = new StringBuffer();
				StringBuffer collateral01 = new StringBuffer();
				for (int i = 0; i < ltCurrent_Order.get(positon).size(); i++) {
					if (ltCurrent_Order.get(positon).get(i) == 1) {
						qlNumSb.append(ltOrder.get(positon).getLtOrder().get(i) + ",");
					}
					Log.e(TAG, "ltCurrent_Order.get(positon).size()====" + ltCurrent_Order.get(positon).get(i));
					// 需要对选中的订单号进行拼接+，+如果大于18位+，如果小于18位直接拼接
					if (ltOrder.get(positon).getLtOrder().get(i).toString().contains("DYXD")
							&& ltCurrent_Order.get(positon).get(i) == 1) {
						collateral01 = collateral01.append(ltOrder.get(positon).getLtOrder().get(i) + ",");
						if (collateral01.length() > 19) {
							collateral = collateral01.toString();
							System.out.print("**collateral==" + collateral);
							Log.d("", "》19" + collateral);

						} else {
							collateral = (ltOrder.get(positon).getLtOrder().get(i).toString());
							Log.d("", "《19" + collateral);
							System.out.print("!!collateral=====" + collateral);
						}
						// 拼接字符串并做网络请求
					}
				}
				qlNumSbstr = qlNumSb.toString();

				bundle.putString("jigouid", ltOrder.get(positon).getJigouid());
				System.out.println("机构id=" + ltOrder.get(positon).getJigouid());
				bundle.putString("renwudan", renwudan);
				bundle.putString("qlNum", qlNumSb + "");
				System.out.println("请领订单=" + qlNumSb);
				Log.d("", "" + "collateralstate===" + collateralstate);
				for (int j = 0; j < ltOrder.get(positon).getLtOrder().size(); j++) {
					Log.e("TAG", "ltOrder.get(positon).getLtOrder()===" + ltOrder.get(positon).getLtOrder());
					for (int i = 0; i < checklist.size(); i++) {
						Log.e("TAG", "checklist===" + checklist.get(i));

					}

					if (checklist.contains(ltOrder.get(positon).getLtOrder().get(j))
							&& ltCurrent_Order.get(positon).get(j) == 1) {
						/// caozuo
						Log.e("TAG", "" + ltOrder.get(positon).getLtOrder().get(j));
//						Toast.makeText(QinglingWangdianActivity.this, "请检查是否有返回没有完成交接的订单号"+ltOrder.get(positon).getLtOrder().get(j), 1000).show();
						timeoutHandle.sendEmptyMessage(2);
						check = false;
						return;
					} else {
						check = true;
						Log.i("TAG", "我这不包含的没勾选");

					}

				}
				if (check) {
					JumpAc();
				}
				Log.d("", "!!!!" + "collateralstate===" + collateralstate);
			}

		}

		public void JumpAc() {
			Log.d("", "" + "booleancollateral===" + collateralstate);
			if (!qlNumSbstr.toString().contains("DYXD")) {
				Skip.skip(QinglingWangdianActivity.this, QinglingZhuangxiangInfoActivity.class, bundle, 0);
				collateralstate = "1";
			} else if (collateralstate.equals("3")) {
				Skip.skip(QinglingWangdianActivity.this, QinglingZhuangxiangInfoActivity.class, bundle, 0);
				collateralstate = "1";
			} else if ((!collateralstate.equals("2")) && qlNumSbstr.toString().contains("DYXD")) {
				System.out.println("请检查抵制押品状态");
				Toast.makeText(QinglingWangdianActivity.this, "请检查抵制押品状态", 1000).show();
			}
		}

		/**
		 * 网点订单号Adapter
		 */
		class WandianOrderBaseAdapter extends BaseAdapter {
			private List<String> lt;
			private int position;

			public WandianOrderBaseAdapter(List<String> lt, int position) {
				super();
				this.lt = lt;
				this.position = position;
			}

			@Override
			public int getCount() {
				System.out.print("!!!!!!!!!!!!" + lt.size());
				return lt.size();
			}

			@Override
			public Object getItem(int arg0) {
				return lt.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				LayoutInflater inflater = LayoutInflater.from(QinglingWangdianActivity.this);
				View v = inflater.inflate(R.layout.item_qingfenrenwu_qiangling_wangdian_orderno, null);
				TextView smtext = (TextView) v.findViewById(R.id.smtext);
				CheckBox cbOrder = (CheckBox) v.findViewById(R.id.cb_item_qingfenrenwu_qingling_wangdian_orderno);
				cbOrder.setOnCheckedChangeListener(new SelectListener(position, arg0));
//				System.out.println("醉了...看不到了"+lt.get(arg0));
//				cbOrder.setText(lt.get(arg0));
				smtext.setText(lt.get(arg0));
				if (ltCurrent_Order.get(position).get(arg0) == 1) {
					cbOrder.setChecked(true);
				} else {
					cbOrder.setChecked(false);
				}

				return v;
			}

			class SelectListener implements OnCheckedChangeListener {
				private int position;
				private int current;

				public SelectListener(int position, int current) {
					super();
					this.position = position;
					this.current = current;
				}

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {
						System.out.println("订单CheckBox设置为true");
						ltCurrent_Order.get(position).set(current, 1);
					} else {
						System.out.println("订单CheckBox设置为false");
						ltCurrent_Order.get(position).set(current, 0);

					}
					System.out.println("test--" + ltCurrent_Order.toString());
					wa.notifyDataSetChanged();
					// updateAll();
				}
			}
		}

		/**
		 * 更新网点信息
		 * 
		 * @param position
		 */
		private void updateAll() {
			wa.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			QinglingWangdianActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/****
	 * 新增需求 防止没有任务的（zk xj dz） 被点击 需要后台发送数据检查哪些是可以点击 那个点击后提示没有实物 参数 corouid ：任务号
	 * 地址押品装箱时，返回没有完成交接的订单号 需求更改 当默认全不选 现在改为提交跳转的时候去对比发现没有实物的时候进行提示20190412
	 */

	public void getUncheckCovuns() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.e(TAG, "renwudan====：" + renwudan);
					requestResult = new QingfenRenwuService().getUncheckCovuns(renwudan);
					Log.e(TAG, "测试数据源：" + requestResult);
					checklist.clear();// 每次进入清空
					if (requestResult != null && !requestResult.equals("anyType{}")) {
						checklist = new Gson().fromJson(requestResult, new TypeToken<List<Object>>() {
						}.getType());
						for (int i = 0; i < checklist.size(); i++) {
							Log.e(TAG, "++++++=" + checklist.get(i));
						}

					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(1);
				}
			}

		}.start();

	}

}
