package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.DetailList;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.AccountInfomationReturnService;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.entity.AccountInformationToBeReturnedEntity;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 账户资料 待 归还资料列表 账户资料归还 : 待放入货柜的任务列表以及详情
 * 
 * @author Administrator 2018_11_19
 */
public class ZhangHuZiLiaoZhangHuDaiGuiHuanActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "ZhangHuZiLiaoZhangHuDaiGuiHuanActivity";
	// 组件初始化

	private ListView zhzl_guihuanrukudaiguihuan_listview;
	private ImageView iv_black;
	private TextView Accountinfotion_oprater;/// 操作人
//	适配器
	private QingLingAdapter adapter;
//	数据源
	private List<AccountInformationToBeReturnedEntity> guihuanList = new ArrayList<AccountInformationToBeReturnedEntity>();
	private List<String> scanlocallist = new ArrayList<String>();
//  字符串变量
	private String number = ""; // 登录人的账户
	private String netResultwillReturn = "";// 待归还的网络请求
	private String count = "";// 数量

	// 工具类
	private ManagerClass manager;
	private OnClickListener OnClick1;
	private AccountInformationToBeReturnedEntity aitbrreturn = new AccountInformationToBeReturnedEntity();/// 实体类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaoguihuanrukudaiguihuanliebiao);
		initView();
		AccountIntotionwillReturn();// 待归还资料列表 账户资料归还
		adapter = new QingLingAdapter();
		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				AccountIntotionwillReturn();// 网络请求
			}
		};

	}

	private void AccountIntotionwillReturn() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 登录人员账号
					number = GApplication.user.getYonghuZhanghao();
					Log.i(TAG, "账户名称===" + number); /// 网络请求
					netResultwillReturn = new AccountInfomationReturnService().getAccountPutInTaskList(number);
					if (!netResultwillReturn.equals("")) {
						Gson gson = new Gson();
						AccountInformationToBeReturnedEntity[] aitbrentityjson = gson.fromJson(netResultwillReturn,
								AccountInformationToBeReturnedEntity[].class);
						for (int i = 0; i < aitbrentityjson.length; i++) {
							guihuanList = Arrays.asList(aitbrentityjson);

							for (int j = 0; j < guihuanList.get(i).getDetailList().size(); j++) {
								scanlocallist.add(guihuanList.get(i).getDetailList().get(j).getSTOCKCODE());
								/// 存放扫描的锁号码

							}

						}
						Log.d(TAG, "=====" + guihuanList.size());

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
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();

				getData();
				adapter.notifyDataSetChanged();
				zhzl_guihuanrukudaiguihuan_listview.setAdapter(adapter);
//				new TurnListviewHeight(listview);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this, "没有任务",
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

	private void initView() {
		// TODO Auto-generated method stub
		/// 登录人
		Accountinfotion_oprater = (TextView) findViewById(R.id.zhzlguihuanrenwuliebaio_username);
		Accountinfotion_oprater.setText("" + GApplication.user.getLoginUserName());
		iv_black = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_black.setOnClickListener(this);
		zhzl_guihuanrukudaiguihuan_listview = (ListView) findViewById(
				R.id.accountinfotionwill_guihuanrukudaiguihuan_listview);

		zhzl_guihuanrukudaiguihuan_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				o_Application.qlruku = o_Application.qinglingruku.get(arg2);
				Intent intent = new Intent(ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this,
						ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.class);
				intent.putExtra("Tasknumber", o_Application.qinglingruku.get(arg2).getDanhao());

				intent.putExtra("list", (Serializable) guihuanList.get(arg2).getDetailList());
				startActivity(intent);

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this.finish();
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
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			Log.d(TAG, "====" + guihuanList.size());
			return guihuanList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return guihuanList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new ViewHodler();
				arg1 = lf.inflate(R.layout.listview_wangdianmingcheng_item, null);
				view.danhao = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvinfo);
				view.riqi = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvtype);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.danhao.setText(guihuanList.get(arg0).getCVOUN());
			view.riqi.setText(scanlocallist.size() + "");
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi, count;
	}

	/**
	 * 扫描到
	 */
	public void getData() {
		o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
		if (guihuanList.size() == 0) { // 集合中没有数据直接跳出
			return;
		}
		if (guihuanList.get(0).getDetailList().size() > 0) {
			for (int i = 0; i < guihuanList.size(); i++) {
				o_Application.qinglingruku.add(
						new QingLingRuKu(guihuanList.get(i).getCVOUN(), guihuanList.get(i).getCVOUN(), scanlocallist));

			}

		}
	}
}
