package com.example.pda.homemagnetopackge;

import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.PostmanAccountScannerUtil;
import com.ljsw.tjbankpad.baggingin.activity.chuku.DiZhiYaPinChuKuZhiWenJiaoJie;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ZhangHuZiLiaoGuiHuanJiaojieActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;

/****
 * 获取后端的后督账包编号后实现左右数据对比 成功后进行指纹验证
 * 
 * @author Administrator
 * 
 */
public class PostmansAccountCheckerActivity extends Activity implements OnClickListener {
	public static PostmansAccountCheckerActivity instance = null;
	protected static final String TAG = "PostmansAccountCheckerActivity";
	private TextView shwoTvWorker;
	// private TextView unCheckTv;
	// private TextView checkTv;
	private ListView unCheckeLv;
	private ListView checkLv;
	private Button btnUpdata;
	private Button btnClean;
	private ImageView ivBlack;
	private List<String> intentdatalist = new ArrayList<String>();

//	private List<String> showlist = new ArrayList<String>();
	private ManagerClass manager;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private TextView tvleft;// 待扫
	private TextView tvright;// 以扫到

	private Button btnjiaojie;
	private Button btn_cancle;
	Bundle bundle_biz;// 接收务业标识
	private String netPostmanResult;

	private PostmanAccountScannerUtil Postman;// 后督账包操作自己的一套扫描rfid标签的规则
	private List<String> houduActionlist = new ArrayList<String>();
	private String uPstr = "";
	String intLinmnum = "";// 传过来的线路

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	private LeftAdapter ladapter;
	private RightAdapter radapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postmans_account_checker);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		intentdatalist.clear();
		if (bundle != null) {
			intentdatalist = (List<String>) bundle.getSerializable("houduActionlist");
			intLinmnum = bundle.getString("linnum");

			System.out.println("houduActionlist:    ---" + intentdatalist.size());
			Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + intentdatalist.size());
			if (intentdatalist.size() > 0 && !intLinmnum.equals("")) {
				// for (int i = 0; i < intentdatalist.size(); i++) {

				// o_Application.qinglingruku.add(new QingLingRuKu(null, null,
				// intentdatalist));
				// o_Application.qlruku.getZhouzhuanxiang().addAll(intentdatalist);
				// }

				Postman = new PostmanAccountScannerUtil();
				Postman.setHandler(handler);
			}
		}

		IntView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		o_Application.qinglingruku.clear();
		o_Application.qinglingruku.add(new QingLingRuKu(null, null, intentdatalist));
		o_Application.qlruku = o_Application.qinglingruku.get(0);
		getRfid().addNotifly(Postman);
		// o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		if (o_Application.numberlist.size() > 0) {
			tvright.setText("" + o_Application.numberlist.size());
		} else {
			tvright.setText("" + 0);
		}
		tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		unCheckeLv.setAdapter(ladapter);
		checkLv.setAdapter(radapter);
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

	/***
	 * 組件
	 */
	private void IntView() {
		// TODO Auto-generated method stub
		shwoTvWorker = (TextView) findViewById(R.id.postmanstvusername1);
		shwoTvWorker.setText(GApplication.user.getLoginUserName());
		ivBlack = (ImageView) findViewById(R.id.postmans_checkql_ruku_back);
		ivBlack.setOnClickListener(this);
		btnjiaojie = (Button) findViewById(R.id.btn_postmanaccount_jiaojie);
		btnjiaojie.setOnClickListener(this);
		btn_cancle = (Button) findViewById(R.id.btn_postmanschukuhedui_cancle);
		btn_cancle.setOnClickListener(this);
		unCheckeLv = (ListView) findViewById(R.id.leftdzpostman_listview);
		checkLv = (ListView) findViewById(R.id.rightpostman_listview);
		tvright = (TextView) findViewById(R.id.postmans_tvright);
		tvleft = (TextView) findViewById(R.id.postmans_tvleft);
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 0:

			break;
		case R.id.postmans_checkql_ruku_back:
			if (rfid != null) {
				getRfid().close_a20();
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			PostmansAccountCheckerActivity.this.finish();
			break;
		case R.id.btn_postmanaccount_jiaojie:

			Updata();
			break;

		case R.id.btn_postmanschukuhedui_cancle:
			btnjiaojie.setEnabled(false);
			btnjiaojie.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btn_cancle.setEnabled(false);
				btn_cancle.setBackgroundResource(R.drawable.button_gray);
			}
//			o_Application.numberlist.clear();
//			o_Application.qlruku.getZhouzhuanxiang().clear();
//			if(intentdatalist.size()>0){
//				
//				Log.e(TAG, intentdatalist.size()+"!!!!!!!!!!!!!!!!!!!");
//			o_Application.qlruku.getZhouzhuanxiang().addAll(intentdatalist);
//			
//			}else{
//			Log.e(TAG, intentdatalist.size()+"!!!!");
//						}
//			  把上一个页面传递的值进行进行保存

			List<String> setlistdata = new ArrayList<String>();
			if (o_Application.numberlist.size() > 0) {
				setlistdata.addAll(o_Application.numberlist);
			}
			if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {

				setlistdata.addAll(o_Application.qlruku.getZhouzhuanxiang());
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			if (setlistdata.size() > 0) {
				o_Application.qlruku.getZhouzhuanxiang().addAll(setlistdata);
			} else {
				Log.d(TAG, "------lianc不添加");
			}

			o_Application.guolv.clear();
			handler.sendEmptyMessage(0);
			break;
		default:
			break;
		}
	}

	private void Updata() {
		// 跳转到指纹验证

		// manager.getGolbalutil().gotoActivity(
		// PostmansAccountCheckerActivity.this,
		// PostMangerFingerActivity.class, bundle_biz, 0);
		Intent intetent3 = new Intent(PostmansAccountCheckerActivity.this, PostMangerFingerActivity.class);
		intetent3.putStringArrayListExtra("postmanlist", (ArrayList<String>) houduActionlist);
		intetent3.putExtra("intLinmnum", intLinmnum);

		startActivity(intetent3);
		if (rfid != null) {
			getRfid().close_a20();
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
					houduActionlist = o_Application.numberlist;
				}
				if (o_Application.numberlist.size() > 0) {
					btn_cancle.setEnabled(true);
					btn_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				tvright.setText("" + o_Application.numberlist.size());
				tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				unCheckeLv.setAdapter(ladapter);
				checkLv.setAdapter(radapter);

				break;
			}

		}

	};

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(PostmansAccountCheckerActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(PostmansAccountCheckerActivity.this);

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
		if (rfid != null) {
			getRfid().close_a20();
		}
		o_Application.numberlist.clear();
		o_Application.qlruku.getZhouzhuanxiang().clear();
		PostmansAccountCheckerActivity.this.finish();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (rfid != null) {
			getRfid().close_a20();
		}
		super.onPause();
	}

}
