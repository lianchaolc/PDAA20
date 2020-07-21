package com.ljsw.tjbankpad.baggingin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pda.R;
import com.manager.classs.pad.ManagerClass;

/***
 * 抵制押品和账户封包的
 * 
 * @author Administrator
 *
 */
public class AccountInfoActivity extends FragmentActivity implements OnClickListener {
	// 组件声明
	private TextView tv_zhanghuziliao, tv_dizhiyapin;
	// 线性布局
	private LinearLayout Linlyzhanghu; // 账户
	private LinearLayout accountdizhiyapin; // 抵制押品
	private TextView zhanghuziliaoru, zhanghuziliaola;// 账户资料入 账户资料啦

	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinfo);

		initView();
	}

	/***
	 * 实现思路进行点击实现隐藏
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tv_zhanghuziliao = (TextView) findViewById(R.id.zhanghuziliaomannager);
		tv_zhanghuziliao.setOnClickListener(this);
		tv_dizhiyapin = (TextView) findViewById(R.id.dizhiyapinmannager);
		tv_dizhiyapin.setOnClickListener(this);
		// 账户资料和抵制押品
		Linlyzhanghu = (LinearLayout) findViewById(R.id.aalayzhanghu);
		Linlyzhanghu.setOnClickListener(this);

		accountdizhiyapin = (LinearLayout) findViewById(R.id.aalaydizhiyapin);
		accountdizhiyapin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aalayzhanghu:
			Intent ii = new Intent(AccountInfoActivity.this, AccountDataActivity.class);
			startActivity(ii);
			break;
		case R.id.aalaydizhiyapin: // 地址押品页面暂无 仿照上缴请领

			break;

		default:
			break;
		}

	}
}
