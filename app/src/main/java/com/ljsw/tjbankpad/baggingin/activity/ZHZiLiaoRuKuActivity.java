package com.ljsw.tjbankpad.baggingin.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.manager.classs.pad.ManagerClass;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 账户资料入库 成功或跳出dialgo 提示
 * 
 * @author Administrator
 * 
 */
public class ZHZiLiaoRuKuActivity extends Activity implements OnClickListener {
	private ListView dzypinlistview, dzypinlistvieyisao; // 扫描到和未扫描到的里石头viewer
	private Button dpjdbtn_sure;
	private Button btn_cancle;
	private TextView tvrukunumber;
	private List<String> listweisao = new ArrayList<String>();
	private List<String> listyisao = new ArrayList<String>();
	private WalkieDataCountAdapter weiWalkieDataCountAdapter;
	private WalkieDataCountAdapter yimWalkieDataCountAdapter;
	private TextView dzypif_wei;
	private TextView dzypif_yisao;
	private ManagerClass managerclass = new ManagerClass();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapininfo);

		initView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		listweisao.add("FB000001");
		listyisao.add("FB000003");
		listyisao.add("FB000002");
		listyisao.add("FB000004");
		listyisao.add("FB000005");
		listyisao.add("FB000006");
	}

	private void initView() {
		// TODO Auto-generated method stub
		dzypinlistview = (ListView) findViewById(R.id.dzypinlistview);

		dzypinlistvieyisao = (ListView) findViewById(R.id.dzypinlistvieyisao);
		weiWalkieDataCountAdapter = new WalkieDataCountAdapter(listweisao, ZHZiLiaoRuKuActivity.this);
		yimWalkieDataCountAdapter = new WalkieDataCountAdapter(listyisao, ZHZiLiaoRuKuActivity.this);
		dzypinlistview.setAdapter(weiWalkieDataCountAdapter);
		dzypinlistvieyisao.setAdapter(yimWalkieDataCountAdapter);

		dpjdbtn_sure = (Button) findViewById(R.id.dpjdbtn_sure);
		dpjdbtn_sure.setOnClickListener(this);
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		dzypif_wei = (TextView) findViewById(R.id.dzypif_wei);// 未扫到
		dzypif_yisao = (TextView) findViewById(R.id.dzypif_yisao);// 未扫到
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dpjdbtn_sure:
			dzypif_wei.setText("0");
			dzypif_yisao.setText("6");
			// 弹出dialog
			Dialog dialog;
			managerclass.getResultmsg().submitZzxInfo(ZHZiLiaoRuKuActivity.this, ZHZiLiaoRuKuActivity.this, "入库完成",
					true);

			break;

		default:
			break;
		}
	}

}
