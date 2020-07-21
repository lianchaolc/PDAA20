package com.ljsw.tjbankpad.baggingin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.manager.classs.pad.ManagerClass;

/***
 * 实现展开一个列表 通过ExpandableListView
 * 
 * @author Administrator
 *
 */
public class ZHZiLiaoHeDuiActivity extends FragmentActivity implements OnClickListener {
	// 定义控件
	private TextView tv_zhanghuziliao, tv_dizhiyapin;
	private WalkieDataCountAdapter wdcadapter;
	private ManagerClass managerclass = new ManagerClass();
	private ListView dzyplistview;
	private Button btnSure;
	private List<String> dzyplist = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapin);

		initView();
		loadData();
	}

	private void loadData() {
		dzyplist.add("RK20180607001");
		dzyplist.add("RK20180607002");
		dzyplist.add("RK20180607003");
		dzyplist.add("RK20180607004");
		wdcadapter = new WalkieDataCountAdapter(dzyplist, ZHZiLiaoHeDuiActivity.this);
		dzyplistview.setAdapter(wdcadapter);
	}

	private void initView() {
		dzyplistview = (ListView) findViewById(R.id.dizhiyapin_listview);
		dzyplistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}
		});

		btnSure = (Button) findViewById(R.id.btn_sure);
		btnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			Intent ii = new Intent(ZHZiLiaoHeDuiActivity.this, ZHZiLiaoHeDuiActivity.class);
			startActivity(ii);
			break;

		default:
			break;
		}

	}

}
