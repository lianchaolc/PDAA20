package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.AccountDataScanningUntil;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;

/***
 * 账户资料出库交接
 * 
 * @author Administrator 2018-11_15 账户资料出库借阅 交接 管库员 》账户中心人员 lc
 */
public class ZhangHuZiLiaoChuKuJiaoJieActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "ZhangHuZiLiaoChuKuJiaoJieActivity";
	// 组件
	private TextView Accountdatadeliverytype, AccountdatadeliveryTaskNumber, Orerater;// 借阅类型, 任务号,操作人
	private TextView unScantv, scanTv;// 未扫描, 扫描
	private ListView leftlistview, rightlistview;// 出库交接入库交接
	private Button btn_Sure, btn_Cancle, btn_Blcak;// 提交和 取消
	private ImageView iv_black;
	// 数据源

	List<String> copylistheduichuku = new ArrayList<String>();// 箱子号
	// 适配器

	private LeftAdapter ladapter;
	private RightAdapter radapter;
	/// 变量
	private ManagerClass manager;
	private AccountDataScanningUntil getnumber1; // 账户资料的扫描工具类 符合账户资料的规则
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private String accrossCvoun = "";// 任务号
	private String FLAG = ""; // 任务类型

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	public static ZhangHuZiLiaoChuKuJiaoJieActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanghuziliaochukujiaojiel);
		Intent mIntent = getIntent();
		Bundle  bundle=this.getIntent().getExtras();
		accrossCvoun = mIntent.getStringExtra("cvoun");
		FLAG = mIntent.getStringExtra("FLAG");
		instance = this;
		getnumber1 = new AccountDataScanningUntil();
		getnumber1.setHandler(handler);
		if (o_Application.qlruku.getZhouzhuanxiang().isEmpty()) {
			copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		}
		copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		Log.e("btdzypSaomiao", "===" + o_Application.qlruku.getZhouzhuanxiang().toString());
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylistheduichuku);
		getRfid().addNotifly(getnumber1);
		o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();

		scanTv.setText("" + o_Application.numberlist.size());
		unScantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		leftlistview.setAdapter(ladapter);
		rightlistview.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btn_Sure.setEnabled(false);
			btn_Sure.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			btn_Cancle.setEnabled(false);
			btn_Cancle.setBackgroundResource(R.drawable.button_gray);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		Orerater = (TextView) findViewById(R.id.accountintionouthous_orerater);
		Orerater.setText("" + GApplication.user.getLoginUserName());
		Accountdatadeliverytype = (TextView) findViewById(R.id.accountdatadeliverycheckup_type_tv);
		Accountdatadeliverytype.setText(FLAG);// 类型

		AccountdatadeliveryTaskNumber = (TextView) findViewById(R.id.accountdatadeliverycheckup_tasknumber_tv);
		AccountdatadeliveryTaskNumber.setText(accrossCvoun);// 任务号
		unScantv = (TextView) findViewById(R.id.accountdatadeliverycheckup_unscan_tv);
		scanTv = (TextView) findViewById(R.id.accountdatadeliverycheckup_scan_tv);

		leftlistview = (ListView) findViewById(R.id.accountdatadeliverycheckup_unscanlistview);
		rightlistview = (ListView) findViewById(R.id.accountdatadeliverycheckup_scanlistview);

		iv_black = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_black.setOnClickListener(this);
		btn_Sure = (Button) findViewById(R.id.accountdatadeliverycheckup_surebtn);
		btn_Sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntentZhanghuziliaojiaojie = new Intent(ZhangHuZiLiaoChuKuJiaoJieActivity.this,
						OutLibraryToAccountCenterFingerActivity.class);
				if (rfid != null) {
					getRfid().close_a20();
				}
				mIntentZhanghuziliaojiaojie.putExtra("cvoun", accrossCvoun);
				startActivity(mIntentZhanghuziliaojiaojie);

			}
		});

		btn_Cancle = (Button) findViewById(R.id.accountdatadeliverycheckup_cancle_btn);
		btn_Cancle.setOnClickListener(this);

	}

	/**
	 * 无限刷新集合
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 用户进入时不让点击跳转下一个页面
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					btn_Sure.setEnabled(true);
					btn_Sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					btn_Cancle.setEnabled(true);
					btn_Cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());
				scanTv.setText("" + o_Application.numberlist.size());
				unScantv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				leftlistview.setAdapter(ladapter);
				rightlistview.setAdapter(radapter);
				break;
			}

		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ql_ruku_back:
			if (rfid != null) {
				getRfid().close_a20();
			}

			ZhangHuZiLiaoChuKuJiaoJieActivity.this.finish();

			break;
		case R.id.accountdatadeliverycheckup_cancle_btn:
			btn_Sure.setEnabled(false);
			btn_Sure.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btn_Cancle.setEnabled(false);
				btn_Cancle.setBackgroundResource(R.drawable.button_gray);
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			o_Application.qlruku.getZhouzhuanxiang().addAll(copylistheduichuku);
			o_Application.guolv.clear();
			handler.sendEmptyMessage(0);

			break;
		default:
			break;
		}

	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoChuKuJiaoJieActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoChuKuJiaoJieActivity.this);

		@Override
		public int getCount() {
			Log.e("TAGo_Application.numberlist.size()", "测试" + o_Application.numberlist.size());
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

	/***
	 * 结束页面
	 */

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (rfid != null) {
			getRfid().close_a20();
		}
		ZhangHuZiLiaoChuKuJiaoJieActivity.this.finish();
	}
}
