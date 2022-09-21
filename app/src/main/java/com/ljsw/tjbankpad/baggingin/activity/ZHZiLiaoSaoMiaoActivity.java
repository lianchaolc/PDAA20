//package com.ljsw.tjbankpad.baggingin.activity;
//
//import com.example.pda.R;
//import com.manager.classs.pad.ManagerClass;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
///***
// * 账户资料扫描页面
// *
// * @author Administrator
// *
// */
//public class ZHZiLiaoSaoMiaoActivity extends Activity implements OnClickListener {
//
//	private Button btnpostion;
//	private TextView tvmnu;
//	private Button btn_weizhi;
//	private ManagerClass managerclass = new ManagerClass();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_zhziliaosaomiao);
//
//		initView();
//		loadData();
//	}
//
//	private void loadData() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		btnpostion = (Button) findViewById(R.id.saomiao_btn);// 扫描
//		btnpostion.setOnClickListener(this);
//		tvmnu = (TextView) findViewById(R.id.saomiaocunfagnweizhi);
//
//		btn_weizhi = (Button) findViewById(R.id.saomiaoresult_btn);
//		btn_weizhi.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.saomiao_btn:
//			tvmnu.setText("A区5号货柜三层301");
//
//			break;
//		case R.id.saomiaoresult_btn:
//			// 弹出dialog
//			Dialog dialog;
//			managerclass.getResultmsg().submitZzxInfo(ZHZiLiaoSaoMiaoActivity.this, ZHZiLiaoSaoMiaoActivity.this,
//					"封包FB000003已放入货柜", true);
//
//			break;
//		default:
//			break;
//		}
//	}
//
//}
