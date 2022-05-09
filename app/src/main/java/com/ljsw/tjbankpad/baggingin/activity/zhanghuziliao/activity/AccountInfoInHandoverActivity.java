package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.activity;

import hdjc.rfid.operator.RFID_Device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.InAccountCenterFingerActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.AccountDataScanningUntil;
import com.manager.classs.pad.ManagerClass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 1 账户资料入库库交接 账户资料交接入库的第一个页面：查询未交接数量和数据集合 》库管员
 * 
 * @author Administrator 2018_11_13
 */
public class AccountInfoInHandoverActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "AccountInfoInHandoverActivity";

	// 数据源
	private ImageView ql_ruku_back; // 返回
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private ListView listleft, listright;
	private TextView unScantv, Scantv;
	private Button btnHandov, btnUnHandov;// 交接 ，伟未交接
	// 提示
	private ManagerClass manager;
	private OnClickListener OnClick1;
	// 实体类
	List<String> copylist = new ArrayList<String>();
	List<String> listinttent = new ArrayList<String>();

	private RFID_Device rfid;
	private AccountDataScanningUntil getnumber;// 账户资料的工具了

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaozhuandairuku);
		getnumber = new AccountDataScanningUntil();
		getnumber.setHandler(handler);
		initView();
		;
		if(null==o_Application.qlruku.getZhouzhuanxiang()){

		}else{
			copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
		}

		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		Log.e(TAG, "========" + o_Application.qlruku.getZhouzhuanxiang().toString());
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
		getRfid().addNotifly(getnumber);
		o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		unScantv.setText("" + o_Application.numberlist.size());
		Scantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		listleft.setAdapter(ladapter);
		listright.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btnHandov.setEnabled(false);
			btnHandov.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			btnUnHandov.setEnabled(false);
			btnUnHandov.setBackgroundResource(R.drawable.button_gray);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				// 用户进入时不让点击跳转下一个页面

				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					btnHandov.setEnabled(true);
					btnHandov.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					btnUnHandov.setEnabled(true);
					btnUnHandov.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());
				unScantv.setText("" + o_Application.numberlist.size());
				Scantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				listleft.setAdapter(ladapter);
				listright.setAdapter(radapter);
				break;
			}

		}

	};

	private void initView() {
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		// TODO Auto-generated method stub
		listright = (ListView) findViewById(R.id.zhanghuziliao_ruku_listview_right);
		listleft = (ListView) findViewById(R.id.zhanghuziliao_ruku_listview_left);

		unScantv = (TextView) findViewById(R.id.zhziruku_rightunscan);// 未扫描
		Scantv = (TextView) findViewById(R.id.zhziruku_leftscan);// 扫描
		btnHandov = (Button) findViewById(R.id.zhziliaojiejei_rukubtn_sure);
		btnHandov.setOnClickListener(this);
		btnUnHandov = (Button) findViewById(R.id.zhanghuziliaobtn_cancle);
		btnUnHandov.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			AccountInfoInHandoverActivity.this.finish();
			if (rfid != null) {
				getRfid().close_a20();
			}
			break;
		case R.id.zhanghuziliaobtn_cancle:
			// 清除集合 重新扫描
			btnHandov.setEnabled(false);
			btnHandov.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btnUnHandov.setEnabled(false);
				btnUnHandov.setBackgroundResource(R.drawable.button_gray);
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
			o_Application.guolv.clear();

			handler.sendEmptyMessage(0);
			break;

		case R.id.zhziliaojiejei_rukubtn_sure:
			if (o_Application.numberlist.size() != 0 || o_Application.numberlist.equals(null)) {
				Intent intent = new Intent(AccountInfoInHandoverActivity.this, InAccountCenterFingerActivity.class);
				Log.e(TAG, o_Application.numberlist.size() + "======");
				if (rfid != null) {// 关闭扫描
					getRfid().close_a20();
				}
				listinttent.clear();// 清空每次
				listinttent.addAll(o_Application.numberlist);
				intent.putExtra("list", (Serializable) listinttent);
				startActivity(intent);
			}

			break;
		default:
			break;
		}

	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(AccountInfoInHandoverActivity.this);

		@Override
		public int getCount() {
			return o_Application.qlruku.getZhouzhuanxiang().size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.qlruku.getZhouzhuanxiang().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				lh = new LeftHolder();
				arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_left, null);
				lh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_left_text);
				arg1.setTag(lh);
			} else {
				lh = (LeftHolder) arg1.getTag();
			}
			lh.tv.setText(o_Application.qlruku.getZhouzhuanxiang().get(arg0));
			return arg1;
		}

	}

	public static class LeftHolder {
		TextView tv;
	}

	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(AccountInfoInHandoverActivity.this);

		@Override
		public int getCount() {
			Log.e("o_Application.numberlist.size()", "测试" + o_Application.numberlist.size());
			return o_Application.numberlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.numberlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				rh = new RightHolder();
				arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_right, null);
				rh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_right_text);
				arg1.setTag(rh);
			} else {
				rh = (RightHolder) arg1.getTag();
			}
			rh.tv.setText(o_Application.numberlist.get(arg0));
			return arg1;
		}

	}

	public static class RightHolder {
		TextView tv;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.getRuning().remove();
		if (o_Application.numberlist.size() > 0) {
			o_Application.numberlist.clear();
		}
		if (o_Application.guolv.size() > 0) {
			o_Application.guolv.clear();
		}
		getRfid().close_a20();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rfid != null) {
				getRfid().close_a20();
			}
			AccountInfoInHandoverActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
