package com.main.pda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import afu.util.PrivilegeActivity;
import com.service.NetService;
import hdjc.rfid.operator.RFID_Device;

import com.application.GApplication;
import com.example.pda.R;
import com.golbal.pda.GolbalUtil;

import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.service.FixationValue;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends PrivilegeActivity implements OnTouchListener {
	// 主界面

	ImageView home; // 主菜单
	ImageView systemset; // 系统设置
	ImageView version; // 版本更新
	BroadcastReceiver broad_net;

	private RFID_Device rfid;

	RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	public static List<Activity> list_version;

	int i = 0;
	private ManagerClass managerClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		managerClass = new ManagerClass();
		// 公共视图文件初始化
		managerClass.getGolbalView().Init(this);

		home = (ImageView) findViewById(R.id.home);
		systemset = (ImageView) findViewById(R.id.systeset);
		version = (ImageView) findViewById(R.id.version);

		// 触摸事件
		home.setOnTouchListener(this);
		systemset.setOnTouchListener(this);
		version.setOnTouchListener(this);

		// 把当前activity放进集合
		GApplication.addActivity(this, "1main");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean txt_namespace = fileIsExists("namespace.txt");
		boolean txt_url = fileIsExists("url.txt");
		if (txt_url == true && txt_namespace == true) {
			inputSDCardNAMESPACE();
			inputSDCardURL();

		} else {

		}

		boolean txt_namespacethree = fileIsExists("namespace_three.txt");
		boolean txt_urlthree = fileIsExists("url_three.txt");
		if (txt_namespacethree == true && txt_urlthree == true) {
			inputSDCardNAMESPACEThree();
			inputSDCardURLThree();
		} else {
			Log.e("地址读取", "地址读取err");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("a");
		return true;
	}

	// 拦截Menu
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		new MenuShow().menu(this);
		MenuShow.pw.showAtLocation(findViewById(R.id.main_box), Gravity.BOTTOM, 0, 0);
		return false;
	}

	@Override
	public void finish() {
		super.finish();
		Thread.currentThread().interrupt();// 中断当前线程.
	}

	// 触摸事件
	@Override
	public boolean onTouch(View view, MotionEvent even) {

		// 当按下的时候
		if (MotionEvent.ACTION_DOWN == even.getAction()) {
			switch (view.getId()) {
			// 版本
//			case R.id.version:
//				version.setImageResource(R.drawable.about_press);
//				break;
			// 主菜单
			case R.id.home:
				home.setImageResource(R.drawable.meun_money_press);
				break;
			// 系统管理
			case R.id.systeset:
				systemset.setImageResource(R.drawable.system_admin_press);
				break;
			}
		}

		// 当移动的时候
		if (MotionEvent.ACTION_MOVE == even.getAction()) {
			GolbalUtil.ismover++;
		}

		// 当松手的的时候
		if (MotionEvent.ACTION_UP == even.getAction()) {
			Log.i("getGetUtil().ismover", new GolbalUtil().ismover + "");
			switch (view.getId()) {
			// 主菜单
			case R.id.home:

				managerClass.getGolbalutil().gotoActivity(MainActivity.this, SystemLogin.class, null,
						GolbalUtil.ismover);
				// }
				home.setImageResource(R.drawable.meun_money);
				break;
			// 系统设置
			case R.id.systeset:
				systemset.setImageResource(R.drawable.system_admin);
				managerClass.getGolbalutil().gotoActivity(MainActivity.this, SystemSet.class, null, 0);
				break;
			// 版本更新
			case R.id.version:
				version.setImageResource(R.drawable.about);
				list_version = new ArrayList<Activity>();
				list_version.add(MainActivity.this);
				managerClass.getGolbalutil().gotoActivity(MainActivity.this,
//						BaggingActivity.class, null, GolbalUtil.ismover);  // 库管 入门式天线之前做的操作 库管员进行装袋操作
						VersionCheck.class, null, GolbalUtil.ismover);
//				managerClass.getGolbalutil().gotoActivity(MainActivity.this,
//						BaggingActivity.class, null, GolbalUtil.ismover);
//						   Intent intent = new Intent(MainActivity.this, BaggingActivitySend.class);
//                startActivity(intent);
				break;
			}

			GolbalUtil.ismover = 0;
		}

		// 当弹起因意外打断的时候
		if (MotionEvent.ACTION_CANCEL == even.getAction()) {
			GolbalUtil.ismover = 0;
			switch (view.getId()) {
			case R.id.version:
				version.setImageResource(R.drawable.about);
				break;
			case R.id.home:
				home.setImageResource(R.drawable.meun_money);
				break;
			case R.id.systeset:
				systemset.setImageResource(R.drawable.system_admin);
				break;
			}

		}

		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = new Intent(this, NetService.class);
//		intent.setAction("network");
		startService(intent);

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}

	/**
	 * 本机读取保存的地址 文件名 需要赋值的控件
	 * 
	 * @param urlname
	 */
	public void inputSDCardNAMESPACE() {
		try {
			System.out.println("-----读取文件-----");
			File file = new File(Environment.getExternalStorageDirectory(), "namespace.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline);
			}
			String getsaveinfo = sb.toString();
			System.out.println("=====读取:" + getsaveinfo);
			FixationValue.NAMESPACE = getsaveinfo;
			System.out.println("命名空间：" + FixationValue.NAMESPACE);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inputSDCardURL() {
		try {
			System.out.println("-----读取文件-----");
			File file = new File(Environment.getExternalStorageDirectory(), "url.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline);
			}
			String getsaveinfo = sb.toString();
			System.out.println("=====读取:" + getsaveinfo);
			FixationValue.URL = getsaveinfo + "/cash_pda";
			String str = getsaveinfo.substring(getsaveinfo.length() - 9, getsaveinfo.length());
			if (str.equals("/cash_pda")) {
				FixationValue.URL2 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_box";
				FixationValue.URL3 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_cm";
				FixationValue.URL4 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_cmanagement";
				FixationValue.URL5 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_kuguanyuan";
			} else {
				FixationValue.URL2 = getsaveinfo + "/cash_box";
				FixationValue.URL3 = getsaveinfo + "/cash_cm";
				FixationValue.URL4 = getsaveinfo + "/cash_cmanagement";
				FixationValue.URL5 = getsaveinfo + "/cash_kuguanyuan";
			}
			System.out.println("地址2：" + FixationValue.URL2);
			System.out.println("地址：" + FixationValue.URL);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 三期
	 * 
	 * @param textname
	 * @return
	 */

	/**
	 * 本机读取保存的地址 文件名 需要赋值的控件
	 * 
	 * @param urlname
	 */
	public void inputSDCardNAMESPACEThree() {
		try {
			System.out.println("-----读取文件-----");
			File file = new File(Environment.getExternalStorageDirectory(), "namespace_three.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline);
			}
			String getsaveinfo = sb.toString();
			System.out.println("=====三期读取:" + getsaveinfo);
			FixationValue.NAMESPACEZH = getsaveinfo;
			System.out.println("命名空间三期：：：：：" + FixationValue.NAMESPACEZH);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inputSDCardURLThree() {
		try {
			System.out.println("-----读取文件-----");
			File file = new File(Environment.getExternalStorageDirectory(), "url_three.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline);
			}
			String getsaveinfo = sb.toString();
			System.out.println("=====读取:" + getsaveinfo);
			FixationValue.URL6 = getsaveinfo + "/pda";
			String str = getsaveinfo.substring(getsaveinfo.length() - 9, getsaveinfo.length());
			if (str.equals("/pda")) {
				FixationValue.URL6 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/pda";
				FixationValue.URL7 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/account";
				FixationValue.URL8 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/ckc";
				FixationValue.URL9 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_cm";
				FixationValue.url11 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/escort";
				FixationValue.URL10 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_cm";
				FixationValue.url15 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cd";
				FixationValue.url16 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/ckc";
				FixationValue.URL17 = getsaveinfo.substring(0, getsaveinfo.length() - 9) + "/cash_sk";

			} else {
				FixationValue.URL6 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/pda";
				FixationValue.URL7 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/account";
				FixationValue.URL8 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/ckc";
				FixationValue.URL9 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/cash_cm";
				FixationValue.url11 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/escort";
				FixationValue.URL10 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/cash_cm";
				FixationValue.url15 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/cd";
				FixationValue.url16 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/ckc";
				FixationValue.URL17 = getsaveinfo.substring(0, getsaveinfo.length() - 0) + "/cash_sk";
			}
			System.out.println("地址2：" + FixationValue.URL6);
			System.out.println("地址7：" + FixationValue.URL7);
			System.out.println("地址8：" + FixationValue.URL8);
			System.out.println("地址9：" + FixationValue.URL9);
			System.out.println("地址10：" + FixationValue.URL10);
			System.out.println("地址15：" + FixationValue.url15);
			System.out.println("地址16：" + FixationValue.url16);
			System.out.println("地址11：" + FixationValue.url11);
			System.out.println("地址17：" + FixationValue.URL17);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean fileIsExists(String textname) {
		try {
			File f = new File(Environment.getExternalStorageDirectory().getPath() + "/" + textname);
			if (!f.exists()) {
				System.out.println("-----文件不存在-----");
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		System.out.println("-----文件存在-----");
		return true;
	}
}
