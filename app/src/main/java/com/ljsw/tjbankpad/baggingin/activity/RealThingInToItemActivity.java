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
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangXianjinFragment;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangZHongkongFragment;
import com.ljsw.tjbankpda.qf.fragment.ZaiRuKuZhuangDai;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

import a20.android_serialport_api.SerialPort;
import a20.cn.uhf.admin.Tools;
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
 * 备用装袋的item
 * 
 */

public class RealThingInToItemActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.realthingintaitem_activity);
		// ================================打开RFID电源控制

		initview();
		LoadData();
	}

	private void LoadData() {

	}

	private void initview() {

	}

	public RealThingInToItemActivity() {
		super();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
