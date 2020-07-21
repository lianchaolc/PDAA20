package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangDaiHeDuiActivity.RightHolder;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.manager.classs.pad.ManagerClass;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

/***
 * 抵制押品 中 网点点单选择带有checkbox的
 * 
 * @author Administrator
 * 
 */
public class DiZhiYaPinWangDianDiangDanXuanZheActivity extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "DiZhiYaPinWangDianDiangDanXuanZheActivity";
	private TextView tvwangdianmigncheng, tvdiangdanshuliang;// 编号
	private ListView listview;
	private DiZhiYaPinXuanZheAdapter dDiZhiYaPinXuanZheAdapter;
	private Button btn_dizhiyapin_xuanzhe;// 确定
	private ImageView ql_ruku_back;
	private String position;// 任务编号
	private String number;// 银行网点编号

	private String resultparms; //
	private BankPointCode bBankPointCode = new BankPointCode();
	private OnClickListener OnClick1;
	private TextView Task_Number;// 任务编号
	private TextView dzyp_pointname;// 网点名称
	private TextView dzyo_pointnmb;// 订单数量；
	private TextView Operator;// 操作人
	private TextView updata;
	// 数据源
	private int count = 0;
	private ManagerClass manager;// 提示
	private List<BankPointCode> xuanzheList = new ArrayList<BankPointCode>();

	private List<CvounDetailList> nmbandtaskitem = new ArrayList<CvounDetailList>();// 获取扫描的号的集合
	private List<String> tasknumber = new ArrayList<String>();
	private List<CvounDetailList> listcvounList = new ArrayList<CvounDetailList>();// string
																					// 类型的字符串
	private List<String> renwulist = new ArrayList<String>(); // 传送任务集合

	// {"corpName":"小淀支行","count":"1","cvounList":["DZXD12112018103101"],"cvounDetailList":[{"detailList":["ZH000025ZZ"],"cvoun":"DZXD12112018103101"}]}
	private List<String> CvounDetailListitem = new ArrayList<String>();// 获取集合中的数据添加到集合
	// chebox
	private int isFlag = 1;// 标识(1)全选或者(0)单个点击
	private List<Integer> clist = new ArrayList<Integer>();// 控制cheBox的状态

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinwangdiandingdanxuanzhe);
		Intent intent = getIntent();
		// 接受数据
		position = intent.getStringExtra("position");// 任务编号
		number = intent.getStringExtra("banknumber");// 银行网点
		Log.e(TAG, "position" + position + "==" + number);
		getWangDianTask();
		initView();
		dDiZhiYaPinXuanZheAdapter = new DiZhiYaPinXuanZheAdapter();
		manager = new ManagerClass();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		btn_dizhiyapin_xuanzhe.setEnabled(false);
		btn_dizhiyapin_xuanzhe.setBackgroundResource(R.drawable.button_gray);
		getWangDianTask();

		dDiZhiYaPinXuanZheAdapter.notifyDataSetChanged();
		listview.setAdapter(dDiZhiYaPinXuanZheAdapter);
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				getWangDianTask();
			}
		};
	}

	/****
	 * 网络获取任务
	 */
	public void getWangDianTask() {
		// manager.getRuning()
		// .runding(QingLingZhuangXiangRuKu_db.this, "数据加载中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.i(TAG, "清分任务网点编号=======" + position + number);
					resultparms = new GetResistCollateralBaggingService()
							.getPleaseLeadClearWorkCvounAndCollateralList(position, number);
					if (resultparms != null && !resultparms.equals("anyType{}")) {
						Log.e(TAG, "测试长度=============" + resultparms);
						Gson gson = new Gson();
						BankPointCode bBankPointCode = gson.fromJson(resultparms, BankPointCode.class);
						xuanzheList.clear();
						xuanzheList.add(bBankPointCode);
						// 添加数据
						Log.e(TAG, "测试长度===" + xuanzheList.size());
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
		btn_dizhiyapin_xuanzhe = (Button) findViewById(R.id.btn_dizhiyapin_xuanzhe);
		btn_dizhiyapin_xuanzhe.setOnClickListener(this);
		btn_dizhiyapin_xuanzhe.setBackgroundResource(R.drawable.button_gray);
		btn_dizhiyapin_xuanzhe.setEnabled(false);
		listview = (ListView) findViewById(R.id.dzyprenwuliebiao_listview);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);// 返回按钮
		ql_ruku_back.setOnClickListener(this);
		Task_Number = (TextView) findViewById(R.id.task_number);// 任务编号
		Task_Number.setText("" + position);
		dzyp_pointname = (TextView) findViewById(R.id.dzyp_pointname);
		dzyo_pointnmb = (TextView) findViewById(R.id.dzyppointnumber);// 订单数量
		Operator = (TextView) findViewById(R.id.netpointtvusername);
		Operator.setText("" + GApplication.user.getLoginUserName());// 操作人

		updata = (TextView) findViewById(R.id.ql_ruku_update);
		updata.setOnClickListener(this);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinWangDianDiangDanXuanZheActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinWangDianDiangDanXuanZheActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				getData();
				dDiZhiYaPinXuanZheAdapter.notifyDataSetChanged();
				listview.setAdapter(dDiZhiYaPinXuanZheAdapter);
				dDiZhiYaPinXuanZheAdapter.notifyDataSetChanged();
				// new TurnListviewHeight(listview);

				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinWangDianDiangDanXuanZheActivity.this, "没有任务",
						new OnClickListener() {

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
	 * 数据源添加数据
	 */
	private void getData() {

		dzyp_pointname.setText(xuanzheList.get(0).getCorpName());// 网点名称
		dzyo_pointnmb.setText(xuanzheList.get(0).getCount()); // 网点号
		if (xuanzheList.isEmpty()) {
			return;
		}
		// 添加数据到扫描的静态集合
		o_Application.qinglingruku.clear();
		if (xuanzheList.size() > 0) {
			for (int i = 0; i < xuanzheList.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu(xuanzheList.get(i).getCorpName() + "",
						xuanzheList.get(i).getCount(), CvounDetailListitem));
				Log.e("size", "=====" + CvounDetailListitem.size());
				o_Application.qlruku = o_Application.qinglingruku.get(i);
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_dizhiyapin_xuanzhe:
			listcvounList.clear();
			for (BankPointCode bpc : xuanzheList) {
				if (bpc.isChecked() == true) {// 如果是被选择中状态
					listcvounList.addAll(bpc.getCvounDetailList());

				} else {// 未选中的状态
				}
			}
			// /json数据分离 数据集合的值
			tasknumber.clear();
			nmbandtaskitem.clear();
			for (int i = 0; i < xuanzheList.size(); i++) {
				for (int j = 0; j < listcvounList.size(); j++) {
					if (xuanzheList.get(i).isChecked() == true) {// 如果是被选择中状态
						// Log.e("TAG1",xuanzheList.get(i).getCvounList().get(j));//
						// 获取的任务号DZCC88052018112001
						Log.e("TAG1", "===" + i + j);//
						// 获取的任务号DZCC88052018112001
						tasknumber.add(xuanzheList.get(i).getCvounDetailList().get(j).getCvoun());// 获取cvounlist数据
						nmbandtaskitem.add(xuanzheList.get(i).getCvounDetailList().get(j));// 获取集合cvoundetail集合
						renwulist.add(xuanzheList.get(i).getCvounList().get(j));
					}
				}
			}

			// 对比covnlist的里面两个值
			CvounDetailListitem.clear();
			for (int i = 0; i < tasknumber.size(); i++) {// [DZCC88052018112001]
				for (int x = 0; x < listcvounList.size(); x++) {// [BankPointBase
																// [cvoun=DZCC88052018112001,
																// detailList=[ZH000001ZZ,
																// ZH000030ZZ]]]
					renwulist.add(nmbandtaskitem.get(i).getDetailList().get(x));
					if (listcvounList.get(i).getCvoun().contains(tasknumber.get(i))) {
						CvounDetailListitem.addAll(listcvounList.get(i).getDetailList());// 全部的锁号码
						break;
					}
				}
			}
			getData();

			// 点击跳转页面
			Intent i = new Intent(DiZhiYaPinWangDianDiangDanXuanZheActivity.this, DiZhiYaPInZhuangXiangActivity.class);
			i.putExtra("list", (Serializable) (tasknumber));// 传送个订单号
			i.putExtra("banknumber", number);
			i.putExtra("position", position);
			startActivity(i);
			break;
		case R.id.ql_ruku_back:// 回退
			DiZhiYaPinWangDianDiangDanXuanZheActivity.this.finish();
			break;

		case R.id.ql_ruku_update:
			getWangDianTask();// 获取网点订单任务
			break;
		default:
			break;

		}
	}

	class DiZhiYaPinXuanZheAdapter extends BaseAdapter {
		ViewHolderAdapterWailk view;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinWangDianDiangDanXuanZheActivity.this);

		@Override
		public int getCount() {
			return xuanzheList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return xuanzheList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				view = new ViewHolderAdapterWailk();
				convertView = lf.inflate(R.layout.listview_walkie_chebox_item, null);
				//
				TextView dizhiyapincheboxresult = (TextView) convertView.findViewById(R.id.dizhiyapincheboxresult);

				CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.dzyp_chebox);
				view.mCheckBox = mCheckBox;
				view.dizhiyapincheboxresult = dizhiyapincheboxresult;

				view.mCheckBox = mCheckBox;

				convertView.setTag(convertView);

			} else {
				// view = (ViewHolderAdapterWailk) convertView.getTag();

			}
			Log.e(TAG, "======" + xuanzheList.get(position).getCount());
			// 显示任务的单号 只有一个订单号
			view.dizhiyapincheboxresult.setText(xuanzheList.get(position).getCvounList().get(0));
			// view.dizhiyapincheboxresult.setText(tasknumber.get(position));
			// 显示是否被选中
			view.mCheckBox.setChecked(xuanzheList.get(position).isChecked());

			// 原版的代码选中和未选中的逻辑
			// view.mCheckBox.setOnCheckedChangeListener(new
			// OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView, boolean
			// isChecked) {
			// // TODO Auto-generated method stub
			// if(isChecked){
			//
			// }
			//
			// }
			// });
			view.mCheckBox.setOnClickListener(new OnClickListener() {

				// s设置被点击和不被点击的状态
				@Override
				public void onClick(View v) {
					if (view.mCheckBox.isChecked()) {
						view.mCheckBox.setChecked(true);
						xuanzheList.get(position).setChecked(true);
						btn_dizhiyapin_xuanzhe.setEnabled(true);
						btn_dizhiyapin_xuanzhe.setBackgroundResource(R.drawable.buttom_selector_bg);
					} else {

						view.mCheckBox.setChecked(false);
						xuanzheList.get(position).setChecked(false);
						btn_dizhiyapin_xuanzhe.setEnabled(false);
						btn_dizhiyapin_xuanzhe.setBackgroundResource(R.drawable.button_gray);

					}
					// view.mCheckBox.setChecked(view.mCheckBox.isChecked()?false:true);
					// 三木运算符
				}
			});
			return convertView;
		}

	}

	public static class ViewHolderAdapterWailk {
		public TextView dizhiyapincheboxresult;
		public TextView dizhiyapincheboxtype;
		public CheckBox mCheckBox;
	}

	class SelectListen implements OnCheckedChangeListener {
		private int position;

		public SelectListen(int position) {
			super();
			this.position = position;

		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		}

	}

}
