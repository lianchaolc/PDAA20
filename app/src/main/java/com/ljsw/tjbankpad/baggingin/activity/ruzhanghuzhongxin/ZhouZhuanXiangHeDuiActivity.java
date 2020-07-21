package com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin;

import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinSaoMiaoZhiWenActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.activity.AccountInfoInHandoverActivity;
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
 * 如账户中心 周转箱核对
 * 
 * @author Administrator
 *
 */
public class ZhouZhuanXiangHeDuiActivity extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "ZhouZhuanXiangHeDuiActivity";
	private ListView boxtodizhiyapin_right;// 右侧 已经扫描到的数据集合
	private ListView boxtodizhiyapin_left;/// 左侧网络请求到的数据
	private Button btn_zhouzhuanxainghedui_sure, btn_cancle;/// 确定取消

	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private QingLingRuKuSaoMiao getnumber1;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onClick;
	private RFID_Device rfid;
	private ImageView iv_black;
	List<String> copylistheduichuku = new ArrayList<String>();// 接受需要扫的数据集合
	private TextView zznumber;// 周转箱编号
	private String linnum = "";
	private TextView tvright;
	private TextView tvleft;
	private TextView zz_operater;// 操作人

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhouzhuanxianghedui_zhanghuzhongxin);
		initView();

		Intent inte = getIntent();
		linnum = inte.getStringExtra("lin");// 获取线路号

		getnumber1 = new QingLingRuKuSaoMiao();
		getnumber1.setHandler(handler);
		copylistheduichuku.addAll(o_Application.qlruku.getZhouzhuanxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
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

		tvright.setText("" + o_Application.numberlist.size());
		tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

		boxtodizhiyapin_left.setAdapter(ladapter);
		boxtodizhiyapin_right.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
			btn_zhouzhuanxainghedui_sure.setEnabled(false);
			btn_zhouzhuanxainghedui_sure.setBackgroundResource(R.drawable.button_gray);
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
					btn_zhouzhuanxainghedui_sure.setEnabled(true);
					btn_zhouzhuanxainghedui_sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				// 扫描到有值得时候可以清除
				if (o_Application.numberlist.size() > 0) {
					btn_cancle.setEnabled(true);
					btn_cancle.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				tvright.setText("" + o_Application.numberlist.size());
				tvleft.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());

				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				boxtodizhiyapin_left.setAdapter(ladapter);
				boxtodizhiyapin_right.setAdapter(radapter);
				break;
			}

		}

	};

	private void initView() {
		// TODO Auto-generated method stub
		// 操作人
		zz_operater = (TextView) findViewById(R.id.zz_operater);
		zz_operater.setText(GApplication.user.getLoginUserName());
		btn_zhouzhuanxainghedui_sure = (Button) findViewById(R.id.btn_zhouzhuanxainghedui_sure);
		btn_cancle = (Button) findViewById(R.id.btn_zhouzhuanxainghedui_cancle);
		btn_zhouzhuanxainghedui_sure.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);

		boxtodizhiyapin_right = (ListView) findViewById(R.id.boxtoaccountcenter_rightlv);
		boxtodizhiyapin_left = (ListView) findViewById(R.id.boxtoaccountcenter_leftlv);
		iv_black = (ImageView) findViewById(R.id.ql_ruku_back);
		iv_black.setOnClickListener(this);

		tvleft = (TextView) findViewById(R.id.zzacountcenter_tvleft);
		tvright = (TextView) findViewById(R.id.zzacountcenter_tvright);
		zznumber = (TextView) findViewById(R.id.zhouzhaunxiangbianhao);
		zznumber.setText(o_Application.qlruku.getDanhao());
		boxtodizhiyapin_right.setDividerHeight(0);
		boxtodizhiyapin_left.setDividerHeight(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_zhouzhuanxainghedui_sure:
			Intent i2 = new Intent(ZhouZhuanXiangHeDuiActivity.this, InAccountCenterFingerActivity.class);// 账户中心的指纹验证
//			跳转后关闭页面
			if (rfid != null) {
				rfid.close_a20();
			}
			startActivity(i2);

			break;
		case R.id.btn_zhouzhuanxainghedui_cancle:
			btn_zhouzhuanxainghedui_sure.setEnabled(false);
			btn_zhouzhuanxainghedui_sure.setBackgroundResource(R.drawable.button_gray);

			if (o_Application.numberlist.size() == 0) {
				btn_cancle.setEnabled(false);
				btn_cancle.setBackgroundResource(R.drawable.button_gray);
			}
			o_Application.numberlist.clear();
			o_Application.qlruku.getZhouzhuanxiang().clear();
			o_Application.qlruku.getZhouzhuanxiang().addAll(copylistheduichuku);
			o_Application.guolv.clear();

			handler.sendEmptyMessage(0);
			break;
		case R.id.ql_ruku_back:// 关闭当前页面

			if (rfid != null) {
				getRfid().close_a20();
			}
			ZhouZhuanXiangHeDuiActivity.this.finish();
			break;
		default:
			break;
		}

	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(ZhouZhuanXiangHeDuiActivity.this);

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
		LayoutInflater lf = LayoutInflater.from(ZhouZhuanXiangHeDuiActivity.this);

		@Override
		public int getCount() {
			Log.e(TAG, "测试" + o_Application.numberlist.size());
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rfid != null) {
				getRfid().close_a20();
			}
			ZhouZhuanXiangHeDuiActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
