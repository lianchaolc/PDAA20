package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.QingLingRuKuSaoMiao;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 抵质押品袋核对
 * 
 * @author Administrator
 *
 */
public class DiZhiYaPinZhuangDaiHeDuiActivity extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "DiZhiYaPinZhuangDaiHeDuiActivity";
	// 组件初始化
	private ListView leftlistview, rightlistview;
	private TextView zhuangcxiang_tvusername2, zhuangcxiang_tvusername1, tvnumber;
	private TextView dizhiyapinleft, dizhiyapinright;
	// 数据源
	private List<String> savelistscan = new ArrayList<String>();
	// 适配器
	private Button dzyphedui_rukubtn_sure, dzyphedui_cancle;
	private ManagerClass manager;
	// 返回值
	private ImageView ql_ruku_back;// 返回键
	List<String> copylist = new ArrayList<String>();

	private String TaskNum = "";// 传的任务
	private QingLingRuKuSaoMiao getnumber;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private LeftAdapter ladapter;
	private RightAdapter radapter;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dizhiyapinzhungdaihedui);
		TaskNum = getIntent().getStringExtra("TaskNum");
		getnumber = new QingLingRuKuSaoMiao();
		getnumber.setHandler(handler);
		copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
		savelistscan.addAll(o_Application.qlruku.getZhouzhuanxiang());
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
		o_Application.qlruku.getZhouzhuanxiang().addAll(copylist);
		if (copylist.equals("") || copylist == null) {
			o_Application.qlruku.getZhouzhuanxiang().addAll(savelistscan);
		}
		getRfid().addNotifly(getnumber);
		o_Application.numberlist.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}
		}.start();
		dizhiyapinright.setText("" + o_Application.numberlist.size());
		dizhiyapinleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		leftlistview.setAdapter(ladapter);
		rightlistview.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			dzyphedui_rukubtn_sure.setEnabled(false);
			dzyphedui_rukubtn_sure.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			dzyphedui_cancle.setEnabled(false);
			dzyphedui_cancle.setBackgroundResource(R.drawable.button_gray);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dzyphedui_rukubtn_sure:
			rfid.stop_a20();// 停止扫描
			Intent i2 = new Intent(DiZhiYaPinZhuangDaiHeDuiActivity.this,
					DiZhiYaPinZhuangXiangWangdianDingDanActivity.class);
			i2.putExtra("TaskNum", TaskNum);

			startActivity(i2);
			break;
		case R.id.ql_ruku_back:
			DiZhiYaPinZhuangDaiHeDuiActivity.this.finish();
			break;
		case R.id.dzypheduidaibtn_cancle:
			// 清除集合 重新扫描
			dzyphedui_rukubtn_sure.setEnabled(false);
			dzyphedui_rukubtn_sure.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				dzyphedui_cancle.setEnabled(false);
				dzyphedui_cancle.setBackgroundResource(R.drawable.button_gray);
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

	private void initView() {
		// 操作人
		zhuangcxiang_tvusername2 = (TextView) findViewById(R.id.dzypheduitv_cao_zuo_ren);
		zhuangcxiang_tvusername2.setText("" + GApplication.user.getLoginUserName());// 操作人员
		leftlistview = (ListView) findViewById(R.id.dzypzhaungxiang_weilistview);
		rightlistview = (ListView) findViewById(R.id.dzypzhangxiang_listvieyisao);
		dizhiyapinright = (TextView) findViewById(R.id.dzypliebiaoright);// 抵质押品以扫右侧
		dizhiyapinleft = (TextView) findViewById(R.id.dzypliebiaoleft);// 抵制押品未扫左侧

		dzyphedui_rukubtn_sure = (Button) findViewById(R.id.dzyphedui_rukubtn_sure);
		dzyphedui_rukubtn_sure.setOnClickListener(this);
		dzyphedui_cancle = (Button) findViewById(R.id.dzypheduidaibtn_cancle);
		dzyphedui_cancle.setOnClickListener(this);
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		tvnumber = (TextView) findViewById(R.id.zhouzhuanxiangnumber);
		tvnumber.setText(o_Application.qlruku.getDanhao() + "");// 任务号

		leftlistview.setDividerHeight(0);
		rightlistview.setDividerHeight(0);
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
					dzyphedui_rukubtn_sure.setEnabled(true);
					dzyphedui_rukubtn_sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					dzyphedui_cancle.setEnabled(true);
					dzyphedui_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());

				dizhiyapinright.setText("" + o_Application.numberlist.size());
				dizhiyapinleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
				Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				leftlistview.setAdapter(ladapter);
				rightlistview.setAdapter(radapter);

				break;
			}

		}

	};

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinZhuangDaiHeDuiActivity.this);

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

	/****
	 * 左右的适配器
	 * 
	 * @author Administrator
	 *
	 */
	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(DiZhiYaPinZhuangDaiHeDuiActivity.this);

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
