//package com.ljsw.tjbankpad.baggingin.activity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.example.pda.R;
//import com.ljsw.tjbankpad.baggingin.activity.adapter.ZHZLRKMXIAdapter;
//import com.manager.classs.pad.ManagerClass;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class ZhangHuZLRuKuMingXiItemActivity extends FragmentActivity implements OnClickListener {
//	// 组件
//	private ManagerClass managerclass = new ManagerClass();
//	private ListView zhzrkmxiListview;// listview
//	private Button Btnsuren, zhanghuiziliaomingxisaomiaobtn;
//
//	private TextView tv;
/////数据源
//	private List<String> maplist = new ArrayList<String>();
//	// 适配器
//	private ZHZLRKMXIAdapter mZHZLRKMXIAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_zhanghuziliaorukumingxiitem);
//
//		initView();
//		loadData();
//	}
//
//	private void loadData() {
//		maplist.add("FB000001");
//		maplist.add("FB000002");
//		maplist.add("FB000003");
//		maplist.add("FB000004");
//
//		mZHZLRKMXIAdapter = new ZHZLRKMXIAdapter(maplist, ZhangHuZLRuKuMingXiItemActivity.this);
//		zhzrkmxiListview.setAdapter(mZHZLRKMXIAdapter);
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		zhzrkmxiListview = (ListView) findViewById(R.id.hzlrkmxilistview);
//		Btnsuren = (Button) findViewById(R.id.zhzirkmingxibtn);
//		Btnsuren.setOnClickListener(this);
//		zhanghuiziliaomingxisaomiaobtn = (Button) findViewById(R.id.zhanghuiziliaomingxisaomiaobtn);
//		zhanghuiziliaomingxisaomiaobtn.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.hzlrkmxilistview:// 查看明细的网络请求
//
//			break;
//		case R.id.zhanghuiziliaomingxisaomiaobtn:// 进行扫描
//			Intent iw = new Intent(ZhangHuZLRuKuMingXiItemActivity.this, ZHZiLiaoSaoMiaoActivity.class);
//			startActivity(iw);
//			break;
//		}
//
//	}
//}
