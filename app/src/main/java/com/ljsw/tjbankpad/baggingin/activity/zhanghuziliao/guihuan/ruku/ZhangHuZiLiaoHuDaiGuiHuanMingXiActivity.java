package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku;

import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.entity.DetailList;
import com.manager.classs.pad.ManagerClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 待归还资料明细
 * 
 * @author Administrator 因后期都有袋好标签可能用不到
 */
public class ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity extends Activity implements OnClickListener {
	private static final String TAG = "ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity";
	private ListView zhzl_rukumingxi_listview;
	private ImageView iv_black;
	private List<DetailList> guihuanListinfo = new ArrayList<DetailList>();

	private AccountInfotionReturnList airl = new AccountInfotionReturnList();// 实体类
	private TextView Operator;// 操作人
	private TextView TaskNumber;// 任务编号
	private Button outhandover, ql_ruku_update;// 交接，更新
	// activity
	private ManagerClass manager;
	private OnClickListener OnClick1;
	private QingLingAdapter adapter;

	// 变量
	private String number = "";// 传的号
	private String netResult = ""; // 获取的网络结果

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaorukudaiguihuanmingxi);
		Intent intent = getIntent();
		number = intent.getStringExtra("Tasknumber");
		guihuanListinfo = (List<DetailList>) getIntent().getSerializableExtra("list");
		adapter = new QingLingAdapter();
		manager = new ManagerClass();

//			Log.e(TAG,	"*****"+guihuanListinfo.get(0).getLOCATION());
//			Log.e(TAG,	"*****"+guihuanListinfo.get(0).getSTOCKCODE());
		initView();
		manager = new ManagerClass();

		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
//					getRenturnList();
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		zhzl_rukumingxi_listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/***
	 * 网络请求成功获取后的显示
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.this, "没有任务",
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
		zhzl_rukumingxi_listview = (ListView) findViewById(R.id.accountinfo_returnlist_listview);
		zhzl_rukumingxi_listview.setAdapter(adapter);
		iv_black = (ImageView) findViewById(R.id.ql_ruku_back);// 返回按键
		iv_black.setOnClickListener(this);
		// 更新按键
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);
		Operator = (TextView) findViewById(R.id.accountinfo_returnlist_oraperet);
		Operator.setText(GApplication.user.getLoginUserName());
		TaskNumber = (TextView) findViewById(R.id.accountinfo_returnlist_number);
		TaskNumber.setText("" + number);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.this.finish();
			break;
		case R.id.ql_ruku_update:// 网络情求更新

			break;
		default:
			break;
		}
	}

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 */
	//
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoHuDaiGuiHuanMingXiActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return guihuanListinfo.size();
		}

		@Override
		public Object getItem(int arg0) {
			return guihuanListinfo.get(arg0);
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
			view.danhao.setText(guihuanListinfo.get(arg0).getSTOCKCODE());
			view.riqi.setText(guihuanListinfo.get(arg0).getLOCATION());
			return arg1;
		}

	}

	public static class ViewHodler {
		TextView danhao, riqi;
	}
}
