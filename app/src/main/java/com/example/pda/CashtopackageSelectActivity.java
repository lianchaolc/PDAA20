package com.example.pda;

import com.ljsw.tjbankpad.baggingin.activity.CashToPackgersActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 新增三期尾零选项現金裝袋代码
 * 
 * @author Administrator lc 創建時間 20191010
 * 
 */
public class CashtopackageSelectActivity extends Activity implements OnClickListener {
	private TextView tvweilingtopackger;// 尾零
	private TextView tvzhengchaotopackger;// 正钞
	private ImageView ql_ruku_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cashtopackageselect);
		intitView();
	}

	private void intitView() {
		// TODO Auto-generated method stub
		tvweilingtopackger = (TextView) findViewById(R.id.tvweilingtopackger);
		tvweilingtopackger.setOnClickListener(this);
		tvzhengchaotopackger = (TextView) findViewById(R.id.tvzhengchaotopackger);
		tvzhengchaotopackger.setOnClickListener(this);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvweilingtopackger:
			Intent iw = new Intent(CashtopackageSelectActivity.this, TailzerotoPackgersActivity.class);
			startActivity(iw);
			break;
		case R.id.ql_ruku_back:
			CashtopackageSelectActivity.this.finish();
			break;
		case R.id.tvzhengchaotopackger:
			Intent iz = new Intent(CashtopackageSelectActivity.this, CashToPackgersActivity.class);
			startActivity(iz);
			break;
		default:
			break;
		}
	}

}
