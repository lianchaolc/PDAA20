package com.ljsw.tjbankpad.baggingin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entity.BoxDetail;
import com.example.pda.R;
import com.example.pda.R.layout;
import com.example.pda.R.menu;
import com.golbal.pda.GolbalView;
import com.imple.getnumber.StopNewClearBox;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangXianjinFragment;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangZHongkongFragment;
import com.ljsw.tjbankpda.qf.fragment.ZaiRuKuZhuangDai;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

import a20.android_serialport_api.SerialPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android_rfid_control.powercontrol;

/***
 * 当前的页面视为了装袋时 时生成标签19位 暂定 用户选择券别和券种 lc 2018-9-13 * @author Administrator
 * 
 */

public class RealThingInToActivity extends Activity implements OnClickListener {

	private ListView rtitlistview;
	List<String> itemlist = new ArrayList();// 存放任务的集合
	private WalkieDataCountAdapter mRTItAdapter;
	private TextView reth_tviewcount;// 扫描到数量
	private TextView reth_tvqueshi, reth_tvfeufa, tethcuowu, textViewrthjump;// 用于页面跳转
	private Button tlthyuli_btn;// 清空

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.realthinginta_activity);
		// ================================打开RFID电源控制

		initview();
		LoadData();
	}

	private void LoadData() {
		for (int i = 0; i < 1; i++) {
			itemlist.add("现金袋子200万");
			itemlist.add("现金袋子100万");
			itemlist.add("现金袋子100万");
			itemlist.add("现金袋子100万");
			itemlist.add("现金袋子50万");
			itemlist.add("现金袋子200万");
			itemlist.add("现金袋子200万");
		}

	}

	private void initview() {
		tlthyuli_btn = (Button) findViewById(R.id.tlthyuli_btn);
		tlthyuli_btn.setOnClickListener(this);
		textViewrthjump = (TextView) findViewById(R.id.textViewrthjump);
		textViewrthjump.setOnClickListener(this);
	}

	public RealThingInToActivity() {
		super();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tlthyuli_btn:

			break;
		case R.id.textViewrthjump:
			Intent ii = new Intent(RealThingInToActivity.this, AccountPackageActivity.class);
			startActivity(ii);
			break;
		default:
			break;
		}

	}

}
