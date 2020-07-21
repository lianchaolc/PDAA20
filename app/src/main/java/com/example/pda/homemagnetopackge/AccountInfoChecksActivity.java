package com.example.pda.homemagnetopackge;

import java.util.ArrayList;
import java.util.List;

import hdjc.rfid.operator.RFID_Device;

import com.application.GApplication;
import com.example.pda.R;
import com.example.pda.R.layout;
import com.example.pda.homemagnetopackge.PostmansAccountCheckerActivity.LeftAdapter;
import com.example.pda.homemagnetopackge.PostmansAccountCheckerActivity.LeftHolder;
import com.example.pda.homemagnetopackge.PostmansAccountCheckerActivity.RightAdapter;
import com.example.pda.homemagnetopackge.PostmansAccountCheckerActivity.RightHolder;
import com.ljsw.tjbankpad.baggingin.activity.PostmanAccountScannerUtil;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpad.baggingin.activity.chuku.DiZhiYaPinChuKuItemHeDuiActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.manager.classs.pad.ManagerClass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 账户资料对比类 获取后台的数据源进行对比 20200424 lianchao
 * 
 * @author Administrator
 * 
 */
public class AccountInfoChecksActivity extends Activity implements OnClickListener {
	protected static final String TAG = "AccountInfoChecksActivity";
	private ImageView accountcheck_back;// / 返回
	private TextView accountchecktvleft;// 左侧未扫描
	private TextView accountchecktvright;// 预测扫描
	private Button btn_accountcheck_jiaojie;
	private Button btn_accountcheck_cancle;
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private ListView leftaccountcheck_listview;
	private ListView rightaccountcheck_listview;
	private RFID_Device rfid;
	private ManagerClass manager;
	private List<String> copylistheduichuku = new ArrayList<String>();
	private String linnumIntent;// 需要传的线路号

	private PostmanAccountScannerUtil PostmanAccountScannerUtil; // 账户资料与后督账包操作操作用了同一个操作规则不对
	private List<String> houduActionlist = new ArrayList<String>();
	private TextView actvusername1;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_account_info_checks);
		InitView();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		copylistheduichuku.clear();
		if (bundle != null) {
			copylistheduichuku = (List<String>) bundle.getSerializable("houduActionlist");
			linnumIntent = bundle.getString("linnum");
			Log.d(TAG, "!!!!!!!!" + copylistheduichuku.size());
			for (int i = 0; i < copylistheduichuku.size(); i++) {
				Log.d(TAG, "!!!!!copylistheduichuku==" + copylistheduichuku.get(i));
			}
			Log.d(TAG, "!!!!!linnumIntent==" + linnumIntent);
			if (copylistheduichuku.size() > 0 && linnumIntent != null) {

				PostmanAccountScannerUtil = new PostmanAccountScannerUtil();
				PostmanAccountScannerUtil.setHandler(handler);
			}
		}
//		copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();
	}

	private void InitView() {
		accountcheck_back = (ImageView) findViewById(R.id.accountcheck_back);
		accountcheck_back.setOnClickListener(this);
		accountchecktvright = (TextView) findViewById(R.id.accountchecktvright);
		accountchecktvleft = (TextView) findViewById(R.id.accountchecktvleft);
		btn_accountcheck_jiaojie = (Button) findViewById(R.id.btn_accountcheck_jiaojie);
		btn_accountcheck_jiaojie.setOnClickListener(this);
		btn_accountcheck_cancle = (Button) findViewById(R.id.btn_accountcheck_cancle);
		btn_accountcheck_cancle.setOnClickListener(this);
		leftaccountcheck_listview = (ListView) findViewById(R.id.leftaccountcheck_listview);
		rightaccountcheck_listview = (ListView) findViewById(R.id.rightaccountcheck_listview);

		actvusername1 = (TextView) findViewById(R.id.actvusername1);
		actvusername1.setText(GApplication.user.getLoginUserName());
	}

	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		o_Application.qinglingruku.clear();
		o_Application.qinglingruku.add(new QingLingRuKu(null, null, copylistheduichuku));
		o_Application.qlruku = o_Application.qinglingruku.get(0);
//		copylistheduichuku.clear();
		if (copylistheduichuku == null) {
			copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		}
		getRfid().addNotifly(PostmanAccountScannerUtil);
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		if (o_Application.numberlist.size() > 0) {
			accountchecktvright.setText("" + o_Application.numberlist.size());
		} else {
			accountchecktvright.setText("" + 0);
		}
		accountchecktvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		leftaccountcheck_listview.setAdapter(ladapter);
		rightaccountcheck_listview.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btn_accountcheck_jiaojie.setEnabled(false);
			btn_accountcheck_jiaojie.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			btn_accountcheck_cancle.setEnabled(false);
			btn_accountcheck_cancle.setBackgroundResource(R.drawable.button_gray);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.accountcheck_back:
			if (rfid != null) {
				rfid.close();
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			AccountInfoChecksActivity.this.finish();
			break;
		case R.id.btn_accountcheck_jiaojie:
			Intent intetent3 = new Intent(AccountInfoChecksActivity.this,
					AccountCenterByHomemangerFingerActivity.class);
			intetent3.putStringArrayListExtra("postmanlist", (ArrayList<String>) houduActionlist);
			intetent3.putExtra("intLinmnum", linnumIntent);
			startActivity(intetent3);
			if (rfid != null) {
				getRfid().close_a20();
			}
			break;
		case R.id.btn_accountcheck_cancle:
			btn_accountcheck_jiaojie.setEnabled(false);
			btn_accountcheck_jiaojie.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btn_accountcheck_cancle.setEnabled(false);
				btn_accountcheck_cancle.setBackgroundResource(R.drawable.button_gray);
			}
			List<String> setlistdata = new ArrayList<String>();
			if (o_Application.numberlist.size() > 0) {
				setlistdata.addAll(o_Application.numberlist);
			}
			if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {

				setlistdata.addAll(o_Application.qlruku.getZhouzhuanxiang());
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();

//			if(copylistheduichuku.size()>0){

			Log.e(TAG, copylistheduichuku.size() + "!!!!!!!!!!!!!!!!!!!");
			if (setlistdata.size() > 0) {
				o_Application.qlruku.getZhouzhuanxiang().addAll(setlistdata);
				Log.d(TAG, "----lianc我的数据长度是大于0可以添加" + copylistheduichuku.size() + "!!!!!!!!!!!");
			} else {
				Log.d(TAG, "------lianc不添加");
			}

//			}else{
			Log.e(TAG, copylistheduichuku.size() + "!!!!");
//						}
			o_Application.guolv.clear();
			handler.sendEmptyMessage(0);

			break;

		default:
			break;
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
					btn_accountcheck_jiaojie.setEnabled(true);
					btn_accountcheck_jiaojie.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					btn_accountcheck_cancle.setEnabled(true);
					btn_accountcheck_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
					houduActionlist = o_Application.numberlist;
				}

				accountchecktvright.setText("" + o_Application.numberlist.size());
				accountchecktvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				leftaccountcheck_listview.setAdapter(ladapter);
				rightaccountcheck_listview.setAdapter(radapter);

				break;
			}

		}

	};

	/***
	 * 左右的数据进行对比
	 * 
	 * @author Administrator
	 *
	 */
	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(AccountInfoChecksActivity.this);

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
				arg1 = lf.inflate(layout.adapter_dizhisaomiao_left, null);
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
		LayoutInflater lf = LayoutInflater.from(AccountInfoChecksActivity.this);

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
				arg1 = lf.inflate(layout.adapter_dizhisaomiao_right, null);
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
		if (rfid != null) {
			rfid.close();
		}
		o_Application.numberlist.clear();
		o_Application.qlruku.getZhouzhuanxiang().clear();
		AccountInfoChecksActivity.this.finish();
		if (o_Application.guolv.size() > 0) {
			o_Application.guolv.clear();
		}
		getRfid().close_a20();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (rfid != null) {
			getRfid().close_a20();
		}
		if (rfid != null) {
			rfid.close();
		}
		o_Application.numberlist.clear();
		o_Application.qlruku.getZhouzhuanxiang().clear();
		AccountInfoChecksActivity.this.finish();
	}

}
