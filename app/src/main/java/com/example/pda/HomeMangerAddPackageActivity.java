package com.example.pda;

import com.example.pda.homemagnetopackge.HomeMangerByTailzerotoPackgersActivity;
import com.example.pda.homemagnetopackge.HomeMangerToAddCashToPackgersActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/****
 * 20200320 lianc
 * 
 * @author Administrator 库管员装工行的袋功能
 * 
 */
public class HomeMangerAddPackageActivity extends Activity implements OnClickListener {

	private TextView tv_zhengtopackger, tv_zoretopackger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_manger_add_package);
		InitView();
	}

	private void InitView() {
		tv_zhengtopackger = (TextView) findViewById(R.id.tv_zhengtopackger);
		tv_zhengtopackger.setText("库管员整钞装袋");
		tv_zhengtopackger.setOnClickListener(this);
		tv_zoretopackger = (TextView) findViewById(R.id.tv_zoretopackger);
		tv_zoretopackger.setText("库管员尾零装袋");
		tv_zoretopackger.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_zhengtopackger:
			Intent i = new Intent(HomeMangerAddPackageActivity.this, HomeMangerToAddCashToPackgersActivity.class);
			startActivity(i);
			break;
		case R.id.tv_zoretopackger:
			Intent i1 = new Intent(HomeMangerAddPackageActivity.this, HomeMangerByTailzerotoPackgersActivity.class);
			startActivity(i1);
			break;
		default:
			break;
		}

	}

}
