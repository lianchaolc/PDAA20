package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan;

import hdjc.rfid.operator.RFID_Device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinSaoMiaoZhiWenActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.activity.AccountInfoInHandoverActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.AccountDataScanningUntil;
import com.ljsw.tjbankpda.db.biz.QingLingRuKuSaoMiao;
import com.manager.classs.pad.ManagerClass;

/****
 * 账户资料归还交接前的单位袋
 * 
 * @author Administrator 18_11_16 lchao
 */
public class ZhangHuZiLiaoGuiHuanJiaojieActivity extends Activity implements OnClickListener {
	public static ZhangHuZiLiaoGuiHuanJiaojieActivity instance = null;
	private static final String TAG = "ZhangHuZiLiaoGuiHuanJiaojieActivity";
	private TextView leftunScantv, rightScantv, TaskNumber, Operater;// 扫描数 , 未扫描描数,任务号,操作人
	private Button btnHandov, btnUnHandov;// 交接 ，未交接
	private ListView listleft;
	private ListView listright;
	private ImageView tv_black;

	private ManagerClass manager;
	private OnClickListener OnClick1;
	private LeftAdapter ladapter;
	private RightAdapter radapter;

//  变量 
	private String TaskNumberStr = ""; // 任务号
//	数据源
	List<String> copylist = new ArrayList<String>();
	List<String> listinttent = new ArrayList<String>();/// 要传输的集合

	private RFID_Device rfid;
	private AccountDataScanningUntil getnumber;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaoguihuanjiaojie);
		TaskNumberStr = getIntent().getStringExtra("taskNumber");
		Log.e(TAG, "任务号===" + TaskNumberStr);
		instance = this;
		initView();
		getnumber = new AccountDataScanningUntil();
		getnumber.setHandler(handler);

		copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
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
		leftunScantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
		rightScantv.setText("" + o_Application.numberlist.size());

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

				rightScantv.setText("" + o_Application.numberlist.size());
				leftunScantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

				Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());

				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				listleft.setAdapter(ladapter);
				listright.setAdapter(radapter);

				break;
			}

		}

	};

	private void initView() {
		// TODO Auto-generated method stub
		// 、操作人
		Operater = (TextView) findViewById(R.id.accountreturnhandover_check_tvusername1);
		Operater.setText(GApplication.user.getLoginUserName());
		btnHandov = (Button) findViewById(R.id.accountintion_btnsure);
		btnHandov.setOnClickListener(this);
		btnUnHandov = (Button) findViewById(R.id.accountintion_btncancle);
		btnUnHandov.setOnClickListener(this);
		TaskNumber = (TextView) findViewById(R.id.accountreturnhandover_check_number);
		TaskNumber.setText(TaskNumberStr);/// 设置任务编号
		tv_black = (ImageView) findViewById(R.id.ql_ruku_back);
		tv_black.setOnClickListener(this);
		leftunScantv = (TextView) findViewById(R.id.accountreturnhandover_check_scnnumber_tvleft);
		rightScantv = (TextView) findViewById(R.id.accountreturnhandover_check_unscannumberright);
		listleft = (ListView) findViewById(R.id.accountreturnhandover_check_leftlistview);
		listright = (ListView) findViewById(R.id.accountreturnhandover_check_rightlistview);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			ZhangHuZiLiaoGuiHuanJiaojieActivity.this.finish();
			if (rfid != null) {
				getRfid().close_a20();
			}
			break;
		case R.id.accountintion_btncancle:
			// 清除集合 重新扫描
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
		case R.id.accountintion_btnsure:// 点击提交
			Intent intent = new Intent(ZhangHuZiLiaoGuiHuanJiaojieActivity.this, DiZhiYaPinSaoMiaoZhiWenActivity.class);/// 需求改动货位管理员》都是库管员
			intent.putExtra("tasknumber", TaskNumberStr);
			listinttent.clear();/// 传送的锁好每次清除
			listinttent.addAll(o_Application.numberlist);
			intent.putExtra("list", (Serializable) listinttent);
			Log.e(TAG, "====" + TaskNumberStr + "==" + listinttent.size());
			if (rfid != null) {
				getRfid().close_a20();
			}
			startActivity(intent);
		default:
			break;
		}
	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoGuiHuanJiaojieActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoGuiHuanJiaojieActivity.this);

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
		if (rfid != null) {
			getRfid().close_a20();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rfid != null) {
				getRfid().close_a20();
			}
			ZhangHuZiLiaoGuiHuanJiaojieActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
