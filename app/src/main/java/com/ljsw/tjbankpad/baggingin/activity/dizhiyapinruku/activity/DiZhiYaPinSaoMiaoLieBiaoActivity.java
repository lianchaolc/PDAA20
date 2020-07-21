package com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity;

import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.DiZhiYaPinGetNumber;
import com.manager.classs.pad.ManagerClass;

/***
 * 抵制押品入库交接
 * 
 * @author Administrator
 * 
 *         已经更改好抵制押品规则能过 2018_12_16
 * 
 *         抵制押品入库交接列表
 */

public class DiZhiYaPinSaoMiaoLieBiaoActivity extends Activity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPinSaoMiaoLieBiaoActivity";
	private TextView dzypliebiao_title; // 抵质押品tittle
	// 变量
	private ListView listleft, listright;
	// 数据源
	private List<String> arraylist = new ArrayList<String>();
	List<String> list = new ArrayList<String>();// 获得的集合数据
	private TextView boxtodizhiyapin_left; // 已经扫描到的号码
	private TextView boxtodizhiyapin_right; // 未扫描到的号码
	// 适配器
	private Button btdzypibtn_sure;// 周转箱拆箱交接// 取消重扫
	private ImageView iv_balcak;
	// 执行扫描的方法
	private ManagerClass manager;
	DiZhiYaPinGetNumber getNum;
	List<String> copylist = new ArrayList<String>();
	List<String> copylistleft = new ArrayList<String>();
	private Button zhuangdai_btn_cancle;

	// 适配器
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private QualitativeWareScanning getnumber;// 抵质押品的新的扫描规则重新截取的字符串长度
	OnClickListener onclickreplace, onClick;
	private String tvtitle;// 传过的值
	private RFID_Device rfid;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinrukusaomiaoliebiao);
		initView();
		// 执行扫描方法
		getnumber = new QualitativeWareScanning();
		getnumber.setHandler(handler);
		Log.e(TAG, "========" + o_Application.qlruku.getZhouzhuanxiang());
		copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();

	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
		// 袋子号的规则 DZ20181211163500662
		System.out.print(copylist.size() + "======");
		getRfid().addNotifly(getnumber);
		o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		boxtodizhiyapin_left.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
		boxtodizhiyapin_right.setText("" + o_Application.numberlist.size());
		listleft.setAdapter(ladapter);
		listright.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btdzypibtn_sure.setEnabled(false);
			btdzypibtn_sure.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			zhuangdai_btn_cancle.setEnabled(false);
			zhuangdai_btn_cancle.setBackgroundResource(R.drawable.button_gray);
		}
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
					btdzypibtn_sure.setEnabled(true);
					btdzypibtn_sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				if (o_Application.numberlist.size() > 0) {
					zhuangdai_btn_cancle.setEnabled(true);
					zhuangdai_btn_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				boxtodizhiyapin_right.setText("" + o_Application.numberlist.size());
				boxtodizhiyapin_left.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				listleft.setAdapter(ladapter);
				listright.setAdapter(radapter);

				break;
			}

		}

	};

	@Override
	protected void onPause() {
		super.onPause();
		manager.getRuning().remove();
		o_Application.wrong = "";
		getRfid().close_a20();
	}

	private void initView() {

		dzypliebiao_title = (TextView) findViewById(R.id.dzypliebiao_title);
		dzypliebiao_title.setText(o_Application.qlruku.getDanhao());

		btdzypibtn_sure = (Button) findViewById(R.id.dpjd_rukubtn_sure);
		btdzypibtn_sure.setOnClickListener(this);
		zhuangdai_btn_cancle = (Button) findViewById(R.id.dpjd_rukubtnliebiao_cancle);
		zhuangdai_btn_cancle.setOnClickListener(this);
		// 左右
		boxtodizhiyapin_left = (TextView) findViewById(R.id.dzypif_lift);
		boxtodizhiyapin_right = (TextView) findViewById(R.id.dzypliebiao_right);

		listleft = (ListView) findViewById(R.id.dzypsmlb_liftlistvieyisao);// 左侧
		listright = (ListView) findViewById(R.id.dzypsmlb_rightlistview);// 右侧

		iv_balcak = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_balcak.setOnClickListener(this);
		listleft.setDividerHeight(0);
		listright.setDividerHeight(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dpjd_rukubtn_sure:
			Intent i = new Intent(DiZhiYaPinSaoMiaoLieBiaoActivity.this, DiZhiYaPinSaoMiaoZhiWenActivity.class);
			if (rfid != null) {
				getRfid().close_a20();
			}
			startActivity(i);
			break;
		case R.id.dpjd_rukubtnliebiao_cancle:
			// 清除集合 重新扫描
			btdzypibtn_sure.setEnabled(false);
			btdzypibtn_sure.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				zhuangdai_btn_cancle.setEnabled(false);
				zhuangdai_btn_cancle.setBackgroundResource(R.drawable.button_gray);
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
			o_Application.guolv.clear();

			handler.sendEmptyMessage(0);
			break;

		case R.id.ql_ruku_back:
			if (rfid != null) {
				getRfid().close_a20();
			}
			finish();
			break;
		default:
			break;
		}
	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinSaoMiaoLieBiaoActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinSaoMiaoLieBiaoActivity.this);

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
			DiZhiYaPinSaoMiaoLieBiaoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
