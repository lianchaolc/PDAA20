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
import com.manager.classs.pad.ManagerClass;

public class ZhangHuZiLiaoRuKuActivity extends FragmentActivity implements OnClickListener {
	// 组件声明
	private TextView zhanghuziliaoruku;//
	private Button btnMingxi;
	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaoruku);

		initView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub

	}

	private void initView() {
		// TODO Auto-generated method stub
		zhanghuziliaoruku = (TextView) findViewById(R.id.zhanghuziliaoruku);
		zhanghuziliaoruku.setText("20180607002");
		btnMingxi = (Button) findViewById(R.id.zhzlrkmingxi);
		btnMingxi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhzlrkmingxi:
			Intent iimixi = new Intent(ZhangHuZiLiaoRuKuActivity.this, ZhangHuZLRuKuMingXiItemActivity.class);
			startActivity(iimixi);
			break;

		default:
			break;
		}

	}
}
