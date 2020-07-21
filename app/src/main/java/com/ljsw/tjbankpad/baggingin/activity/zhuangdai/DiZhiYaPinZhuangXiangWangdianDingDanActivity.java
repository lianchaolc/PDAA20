package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinZhuangXiangLieBiaoAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.LocationManagerCode;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity.ViewHodler;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPinZhuangXiangTActivity.QingLingAdapter;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.yy.application.S_application;
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
 * 网点点定点核对11-5
 * 
 * @author Administrator
 *
 */
public class DiZhiYaPinZhuangXiangWangdianDingDanActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPinZhuangXiangWangdianDingDanActivity";
	// /组件初始化
	private ListView RenWulistview;
	private TextView bianhaonumber, wangdiancaozuoren;// 网点操作人
	// 数据源
	private List<DotTask> renwuList = new ArrayList<DotTask>();
	// 适配器
	private ImageView ql_ruku_back;// 返回按钮
	private QingLingAdapter wangdiandapter;// 适配器
	private ManagerClass manager;// 提示
	private OnClickListener OnClick1;
	private String parms;// 获取任务
	private String TaskNum = "";// 传的任务号
	private DotTask dottask = new DotTask();// 实体类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinzhungxiangliebiao);
		Intent intent = getIntent();
		TaskNum = intent.getStringExtra("TaskNum");
		initView();
		wangdiandapter = new QingLingAdapter();
		manager = new ManagerClass();
		loadData();

	}

	private void loadData() {
		// TODO Auto-generated method stub

		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String number = o_Application.qlruku.getDanhao();
					parms = new GetResistCollateralBaggingService().getPleaseLeadClearWorkCorpListAndCvounCount(number);
					Log.e(TAG, "网络请求结果" + parms.toString());
					if (!parms.equals("")) {
						Gson gson = new Gson();
						DotTask[] DotTask = gson.fromJson(parms, DotTask[].class);
						for (int i = 0; i < DotTask.length; i++) {
							renwuList = Arrays.asList(DotTask);
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

	@Override
	protected void onResume() {
		super.onResume();
		if (o_Application.qinglingruku.size() > 0) {
			o_Application.qinglingruku.clear();
		}
		wangdiandapter.notifyDataSetChanged();
		RenWulistview.setAdapter(wangdiandapter);
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();

			}
		};
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangWangdianDingDanActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangWangdianDingDanActivity.this, "网络连接失败,重试?",
						OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
//				getData();
				wangdiandapter.notifyDataSetChanged();
				RenWulistview.setAdapter(wangdiandapter);
//				new TurnListviewHeight(listview);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinZhuangXiangWangdianDingDanActivity.this, "没有任务",
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
	 * 跳转
	 */
	private void initView() {
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		bianhaonumber = (TextView) findViewById(R.id.bianhaonumber);
		bianhaonumber.setText(o_Application.qlruku.getDanhao());
		wangdiancaozuoren = (TextView) findViewById(R.id.wangdian_tvusername1);// 设置人员名称
		wangdiancaozuoren.setText("" + GApplication.user.getLoginUserName());// 设置计划编号
//		bianhaonumber.setText(o_Application.qlruku.getDanhao() + "");

		RenWulistview = (ListView) findViewById(R.id.dzyprenwuliebaio_listvieyisao);

		RenWulistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent it = new Intent(DiZhiYaPinZhuangXiangWangdianDingDanActivity.this,
						DiZhiYaPinWangDianDiangDanXuanZheActivity.class);

				Log.e(TAG, "传值" + o_Application.qlruku.getDanhao() + "==" + renwuList.get(arg2).getCORPID() + "==!!!!!"
						+ TaskNum);
				it.putExtra("position", TaskNum);// 任务编号
				it.putExtra("banknumber", renwuList.get(arg2).getCORPID());// 银行id
				it.putExtra("bankname", renwuList.get(arg2).getCORPNAME());// 银行网点
				/// 传两个参数 任务编号和银行网点编号
				startActivity(it);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.ql_ruku_back:
			DiZhiYaPinZhuangXiangWangdianDingDanActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/***
	 * 显示三个列表的适配器
	 * 
	 * @author Administrator
	 *
	 */

	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinZhuangXiangWangdianDingDanActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return renwuList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return renwuList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_pointsetlect_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.lv_dizhiyapinid);
				view.riqi = (TextView) arg1.findViewById(R.id.dizhiyapinitem);
				view.count = (TextView) arg1.findViewById(R.id.dizhiyapintype);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText((arg0 + 1) + "");
			view.riqi.setText(renwuList.get(arg0).getCORPNAME());
			view.count.setText(renwuList.get(arg0).getCOUNT() + "");
//			);
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

}
