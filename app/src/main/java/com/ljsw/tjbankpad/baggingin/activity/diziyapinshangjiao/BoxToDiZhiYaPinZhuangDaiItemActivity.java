package com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao;

import hdjc.rfid.operator.INotify;
import hdjc.rfid.operator.RFID_Device;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.imple.getnumber.BackCleanBox;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinChaiXiangAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinHeDuiAdapter;
import com.ljsw.tjbankpad.baggingin.activity.adapter.WalkieDataCountAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinSaoMiaoZhiWenActivity;
import com.ljsw.tjbankpda.db.activity.QingLingZhuangXiangRuKuSaoMiao_db;
import com.ljsw.tjbankpda.db.activity.RenWuLieBiao_db;
import com.ljsw.tjbankpda.db.activity.ShangJiaoRuKuSaoMiao_db;
import com.ljsw.tjbankpda.db.activity.ShangJiaoRuKuSaoMiao_db.RightHolder;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.DiZhiYaPinGetNumber;
import com.ljsw.tjbankpda.db.biz.QingLingChuRuKuGetNumber;
import com.ljsw.tjbankpda.db.biz.QingLingRuKuSaoMiao;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.main.pda.HomeMenu;
import com.manager.classs.pad.ManagerClass;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 当前的activity 来自周转箱的地址押品拆箱子 得到袋子号 与实物进行核对无误后跳转
 * 
 * @author Administrator
 *
 */
public class BoxToDiZhiYaPinZhuangDaiItemActivity extends Activity implements OnClickListener {
	String TAG = "BoxToDiZhiYaPinZhuangDaiItemActivity";
	private TextView zhouto_caozuoren, dizhiyapinheduitv;
	// listview 显示 周转箱任务列表

	List<String> list = new ArrayList<String>();// 获得的集合数据
	private TextView boxtodizhiyapin_left; // 已经扫描到的号码
	private TextView boxtodizhiyapin_right; // 未扫描到的号码
	// 适配器
	private Button btdzypibtn_sure;// 周转箱拆箱交接
	private ImageView iv_balcak;
	// 执行扫描的方法
	private ManagerClass manager;
	DiZhiYaPinGetNumber getNum;
	List<String> copylist = new ArrayList<String>();
	private String postion;// 获取的上一个页面传过来的值

	private Button zhuangdai_btn_cancle;
	// 适配器
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private ListView listleft, listright;

//	private QingLingRuKuSaoMiao getnumber1;
	private QualitativeWareScanning getnumber1;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
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
		setContentView(R.layout.activity_boxtodizhiyapinitem);

		initView();
		// 执行扫描方法
		getnumber1 = new QualitativeWareScanning();
		getnumber1.setHandler(handler);

		copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();

		onclickreplace = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				manager.getAbnormal().remove();
			}
		};
		onClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				manager.getAbnormal().remove();

			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		o_Application.guolv.clear();
		Log.e("btdzypSaomiao", "===" + o_Application.qlruku.getZhouzhuanxiang().toString());
		o_Application.qlruku.getZhouzhuanxiang().clear();
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
		getRfid().addNotifly(getnumber1);
		o_Application.numberlist.clear();
//		o_Application.numberlist.clear();////
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		boxtodizhiyapin_right.setText("" + o_Application.numberlist.size());
		boxtodizhiyapin_left.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

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
//				     btdzypibtn_sure.setEnabled(false);
//					btdzypibtn_sure.setBackgroundResource(R.drawable.button_gray);
				if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
					btdzypibtn_sure.setEnabled(true);
					btdzypibtn_sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					zhuangdai_btn_cancle.setEnabled(true);
					zhuangdai_btn_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());

				boxtodizhiyapin_right.setText("" + o_Application.numberlist.size());
				boxtodizhiyapin_left.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

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
		// 以扫
		listleft = (ListView) findViewById(R.id.boxtodizhiyapin_weilistview1);
		listright = (ListView) findViewById(R.id.boxtodizhiyapin_yilistvieyisao1);

		boxtodizhiyapin_left = (TextView) findViewById(R.id.boxtodizhiyapin_left);
		boxtodizhiyapin_right = (TextView) findViewById(R.id.boxtodizhiyapin_rigth);

		btdzypibtn_sure = (Button) findViewById(R.id.btdzypibtn_sure);
		btdzypibtn_sure.setOnClickListener(this);
		btdzypibtn_sure.setBackgroundResource(R.drawable.button_gray);
		btdzypibtn_sure.setEnabled(false);

		iv_balcak = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_balcak.setOnClickListener(this);
		dizhiyapinheduitv = (TextView) findViewById(R.id.dzyphedui_number);
		zhuangdai_btn_cancle = (Button) findViewById(R.id.zhuangdai_btn_cancle);
		zhuangdai_btn_cancle.setOnClickListener(this);
//				if(postion!=null||!postion.equals("")){

		dizhiyapinheduitv.setText(o_Application.qlruku.getDanhao() + "");
		listleft.setDividerHeight(0);
		listright.setDividerHeight(0);
//				}
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.getRuning().remove();
		o_Application.wrong = "";
		getRfid().close_a20();
	}

//跳转
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btdzypibtn_sure:// 提交进行交接
			Intent iti = new Intent(BoxToDiZhiYaPinZhuangDaiItemActivity.this, DiZhiYaPinActivity.class);
			iti.putExtra("postion", o_Application.qlruku.getDanhao());
			startActivity(iti);
			break;

		case R.id.zhuangdai_btn_cancle:
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
		default:
			break;
		}
	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(BoxToDiZhiYaPinZhuangDaiItemActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(BoxToDiZhiYaPinZhuangDaiItemActivity.this);

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

}
