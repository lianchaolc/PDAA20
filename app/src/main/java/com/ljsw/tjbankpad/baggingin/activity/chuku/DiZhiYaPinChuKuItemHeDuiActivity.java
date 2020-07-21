package com.ljsw.tjbankpad.baggingin.activity.chuku;

import android.view.KeyEvent;
import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.ZhangHuZiLiaoChuKuJiaoJieActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.QingLingRuKuSaoMiao;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.pda.BankDoublePersonLogin;
import com.moneyboxadmin.pda.BoxAddStop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/****
 * 核对抵制押品 是否扫全核对
 * 
 * @author Administrator 抵质押品核对
 */

public class DiZhiYaPinChuKuItemHeDuiActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPinChuKuItemJiaojieActivity";
	private ManagerClass managerclass = new ManagerClass();
	private ImageView iv_black; // 查看结果后返回
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private ListView listleft, listright;
	private TextView dzypihd_tv;
	private TextView tvcaozuoren;// 操作人
	List<String> copylistheduichuku = new ArrayList<String>();// 出库核对
	private String postion;// 上个页面的传值
	private ManagerClass manager;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private TextView tvleft;// 待扫
	private TextView tvright;// 以扫到

	private Button btnjiaojie;
	private Button btn_cancle;
	Bundle bundle_biz;// 接收务业标识

	private QualitativeWareScanning getnumber1;// 抵制押品自己的一套扫描rfid标签的规则

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinchukuitemhedui);
//		获取值
		Intent i2 = getIntent();
		postion = i2.getStringExtra("position");
		initView();
		getnumber1 = new QualitativeWareScanning();
		getnumber1.setHandler(handler);
		copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();
		bundle_biz = new Bundle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		Log.e("btdzypSaomiao", "===" + o_Application.qlruku.getZhouzhuanxiang().toString());
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylistheduichuku);
		manager.getRfid().addNotifly(getnumber1);
		o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				manager.getRfid().open_a20();
			}
		}.start();

		tvright.setText("" + o_Application.numberlist.size());
		tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		listleft.setAdapter(ladapter);
		listright.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btnjiaojie.setEnabled(false);
			btnjiaojie.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			btn_cancle.setEnabled(false);
			btn_cancle.setBackgroundResource(R.drawable.button_gray);
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
					btnjiaojie.setEnabled(true);
					btnjiaojie.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					btn_cancle.setEnabled(true);
					btn_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				tvright.setText("" + o_Application.numberlist.size());
				tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

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
		iv_black = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_black.setOnClickListener(this);
		listright = (ListView) findViewById(R.id.rightdz_listview);
		listleft = (ListView) findViewById(R.id.leftdz_listview);
		dzypihd_tv = (TextView) findViewById(R.id.dzypihd_tvcaozuoren);
		dzypihd_tv.setText(o_Application.qlruku.getDanhao());

		tvleft = (TextView) findViewById(R.id.tvleft);
		tvright = (TextView) findViewById(R.id.tvright);

		btnjiaojie = (Button) findViewById(R.id.btn_chukuhedui_jiaojie);
		btnjiaojie.setOnClickListener(this);
		btn_cancle = (Button) findViewById(R.id.btn_chukuhedui_cancle);
		btn_cancle.setOnClickListener(this);

		tvcaozuoren = (TextView) findViewById(R.id.dizhiyapinchukutvusername1);
		tvcaozuoren.setText(GApplication.user.getLoginUserName());// 操作人
		listleft.setDividerHeight(0);
		listright.setDividerHeight(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btn_chukuhedui_jiaojie:// 跳转请分员交接
			bundle_biz.putString("type", "");
			bundle_biz.putString("clearjoin", "清分员交接");
			if (manager.getRfid() != null) {
				manager.getRfid().close_a20();
			}
			manager.getGolbalutil().gotoActivity(DiZhiYaPinChuKuItemHeDuiActivity.this,
					DiZhiYaPinChuKuZhiWenJiaoJie.class, bundle_biz, 0);

			break;
		case R.id.btn_chukuhedui_cancle:
			btnjiaojie.setEnabled(false);
			btnjiaojie.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btn_cancle.setEnabled(false);
				btn_cancle.setBackgroundResource(R.drawable.button_gray);
			}
			setCleanList();
			handler.sendEmptyMessage(0);
			break;

		case R.id.ql_ruku_back: // 关闭当前页面 随时关闭扫描
			if (manager.getRfid() != null) {
				manager.getRfid().close_a20();
			}
			setCleanList();
			DiZhiYaPinChuKuItemHeDuiActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setCleanList();
//			//不执行父类点击事件
//			return true;
		}
		// 继续执行父类其他点击事件
		return super.onKeyUp(keyCode, event);
	}

	// LR_TODO: 2020/4/16 10:50 liu_rui 清空列表
	private void setCleanList() {
		o_Application.numberlist.clear();
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylistheduichuku);
		o_Application.guolv.clear();
	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinChuKuItemHeDuiActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinChuKuItemHeDuiActivity.this);

		@Override
		public int getCount() {
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
		manager.getRfid().close_a20();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (manager.getRfid() != null) {
			manager.getRfid().close_a20();
		}
		DiZhiYaPinChuKuItemHeDuiActivity.this.finish();
	}

}
