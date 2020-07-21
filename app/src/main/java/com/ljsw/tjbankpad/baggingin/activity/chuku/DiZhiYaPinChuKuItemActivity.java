package com.ljsw.tjbankpad.baggingin.activity.chuku;

import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.DetailList;
import com.manager.classs.pad.ManagerClass;

import android.content.Intent;
import android.os.Bundle;
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

/****
 * 抵制押品 由任务进入后查看位置在哪
 * 
 * @author Administrator 货位管理员
 */

public class DiZhiYaPinChuKuItemActivity extends FragmentActivity implements OnClickListener {
	public static final String TAG = "DiZhiYaPinChuKuItemActivity";
	private ManagerClass manager;
	private TextView operatorleft;// 操作人
	private ImageView btn_black; // 查看结果后返回
	private ListView listview_chuku; // 出库
	private TextView dizhiyapincount;// 抵制押品数量
	// 适配器
	List<DetailList> DetailListlist = new ArrayList<DetailList>(); // 数据源集合
	private List<String> arraylist = new ArrayList();// 数据集合
	private TextView tvdizhiyapintitle;
	private QingLingAdapter adapter;
	private String postion;
	private Button dizhiyapin_btn_chuku;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinchukuitem);
		Intent i2 = getIntent();
		postion = i2.getStringExtra("position");
		DetailListlist = (List<DetailList>) getIntent().getSerializableExtra("list");
		initView();
		adapter = new QingLingAdapter();
		manager = new ManagerClass();
		Log.e(TAG, "postion+DetailListlist长度===" + DetailListlist.size() + "===" + postion);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		listview_chuku.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listview_chuku.setAdapter(adapter);
		arraylist.clear();

	}

	private void initView() {
		// TODO Auto-generated method stub
		operatorleft = (TextView) findViewById(R.id.dizhilocationtvorpter);

		operatorleft.setText("" + GApplication.user.getLoginUserName());

		btn_black = (ImageView) findViewById(R.id.diziypchuku_iv_ql_ruku_back);
		btn_black.setOnClickListener(this);
		listview_chuku = (ListView) findViewById(R.id.listview_chuku);
		tvdizhiyapintitle = (TextView) findViewById(R.id.tvdizhiyapintitle);
		tvdizhiyapintitle.setText(postion);
		dizhiyapin_btn_chuku = (Button) findViewById(R.id.dizhiyapin_btn_chuku);
		dizhiyapin_btn_chuku.setOnClickListener(this);
		dizhiyapincount = (TextView) findViewById(R.id.dizhiyapincpount);
		dizhiyapincount.setText(DetailListlist.size() + "");

	}

	/***
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class QingLingAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinChuKuItemActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return DetailListlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return DetailListlist.get(arg0);
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
				view.riqi = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvinfo);
				view.count = (TextView) arg1.findViewById(R.id.accountinfotion_listitem_tvtype);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.riqi.setText(DetailListlist.get(arg0).getSTOCKCODE());
			view.count.setText(DetailListlist.get(arg0).getITEMLOCATION() + "");
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
		case R.id.dizhiyapin_btn_chuku:
			Intent i = new Intent(DiZhiYaPinChuKuItemActivity.this, DiZhiYaPinChuKuItemHeDuiActivity.class);
			startActivity(i);
			// 关闭当前页面
			break;
		case R.id.diziypchuku_iv_ql_ruku_back:
			DiZhiYaPinChuKuItemActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
