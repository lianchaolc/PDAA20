package com.ljsw.tjbankpad.baggingin.activity;

import com.example.pda.R;

import com.manager.classs.pad.ManagerClass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * 账户资料封包
 * 
 * @author Administrator
 *
 */
public class AccountPackageActivity extends FragmentActivity implements OnClickListener {
	private EditText ap_et_number;// 用户输入的号
	private String apa_stretnumer;
	private Button up_dataResult, apk_clean_btn, apk_tijiao_btn;// 更新数据
	// 用户输入
	private LinearLayout apk_intonumber;
	private TextView apkresultnum, textViewaccpack;// 用于跳转页面

	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountpackage_activity);

		initView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		ap_et_number = (EditText) findViewById(R.id.ap_et_number);
		apa_stretnumer = ap_et_number.getText().toString();
		up_dataResult = (Button) findViewById(R.id.ql_ruku_update);
		up_dataResult.setOnClickListener(this);
		apk_clean_btn = (Button) findViewById(R.id.apk_clean_btn);
		apk_clean_btn.setOnClickListener(this);

		apk_tijiao_btn = (Button) findViewById(R.id.apk_tijiao_btn);
		apk_tijiao_btn.setOnClickListener(this);
		apkresultnum = (TextView) findViewById(R.id.apkresultnum);
		textViewaccpack = (TextView) findViewById(R.id.ql_ruku_back);
		textViewaccpack.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_update:// 更新数据或者页面的数据

			break;

		case R.id.apk_clean_btn:

			break;
		case R.id.apk_tijiao_btn:
			// 弹出dialog
			Dialog dialog;
			managerclass.getResultmsg().submitZzxInfo1(AccountPackageActivity.this, AccountPackageActivity.this, "封包成功",
					true);

			apkresultnum.setText("FB000001");
			break;

		case R.id.ql_ruku_back:// 跳转页面
			Intent ii = new Intent(AccountPackageActivity.this, AccountInfoActivity.class);
			startActivity(ii);
			break;

		default:
			break;
		}

	}
}
