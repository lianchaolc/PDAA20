package com.ljsw.tjbankpad.baggingin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pda.R;
import com.manager.classs.pad.ManagerClass;

/***
 * 账户资料管理 进入账户层的跳转类
 * 
 * @author Administrator
 *
 */
public class AccountDataActivity extends FragmentActivity implements OnClickListener {
	// 组件声明
	private TextView tv_zhanghuziliao, tv_dizhiyapin;
	// 线性布局
	private LinearLayout Linlyzhanghu; // 账户
	private LinearLayout accountdizhiyapin; // 抵制押品
	private TextView zhanghuziliaoru, zhanghuziliaola;// 账户资料入 账户资料

	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acctivity_accountdata);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		zhanghuziliaoru = (TextView) findViewById(R.id.zhanghuziliaoru);
		zhanghuziliaoru.setOnClickListener(this);
		zhanghuziliaola = (TextView) findViewById(R.id.zhanghuziliaola);
		zhanghuziliaola.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhanghuziliaoru: // 出入库
			Intent ii = new Intent(AccountDataActivity.this, ZHZiLiaoRuKuActivity.class);
			startActivity(ii);
			break;
		case R.id.zhanghuziliaola:// 货位
			Intent ii2 = new Intent(AccountDataActivity.this, ZhangHuZLaActivity.class);
			startActivity(ii2);
			break;
		default:
			break;
		}

	}
}
