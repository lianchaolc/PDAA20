package com.ljsw.tjbankpad.baggingin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.manager.classs.pad.ManagerClass;

/***
 * 账户拉额操作
 * 
 * @author Administrator
 *
 */
public class ZhangHuZLaActivity extends FragmentActivity implements OnClickListener {
	// 组件声明
	private TextView zhuanghu_username;
	private ListView zhzzlzhzl_listview;
	private Button zhzilabtn_sure;
	// 线性布局
	private LinearLayout Linlyzhanghu; // 账户
	private LinearLayout accountdizhiyapin; // 抵制押品
	private TextView zhanghuziliaoru, zhanghuziliaola;// 账户资料入 账户资料

	// 数据源
	private List<String> zhzllsit = new ArrayList<String>();

	// 适配器
	private WalkieDataCountAdapter ZhangHuZLaadapter;

	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuzla);

		initView();
		loadData();
	}

	/***
	 * 添加数据
	 */
	private void loadData() {
		zhzllsit = new ArrayList<String>();
		zhzllsit.add("Rk20180607001");
		zhzllsit.add("Rk20180607002");
		zhzllsit.add("Rk20180607003");
		zhzllsit.add("Rk20180607004");
		zhzllsit.add("Rk20180607005");
		ZhangHuZLaadapter = new WalkieDataCountAdapter(zhzllsit, ZhangHuZLaActivity.this);
		zhzzlzhzl_listview.setAdapter(ZhangHuZLaadapter);
	}

	/**
	 * 组件初始化
	 */
	private void initView() {
		// TODO Auto-generated method stub
		zhuanghu_username = (TextView) findViewById(R.id.zhuanghu_username);
		zhuanghu_username.setText("王五X");
		zhzzlzhzl_listview = (ListView) findViewById(R.id.zhzl_listview);
		zhzilabtn_sure = (Button) findViewById(R.id.zhzilabtn_sure);
		zhzilabtn_sure.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.zhzilabtn_sure:
			Intent zhuanghu = new Intent(ZhangHuZLaActivity.this, ZhangHuZiLiaoRuKuActivity.class);
			startActivity(zhuanghu);
			ZhangHuZLaActivity.this.finish();
			break;

		}

	}
}
