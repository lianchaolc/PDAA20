package com.moneyboxadmin.pda;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pda.R;
import com.fragment.pda.NotClearBoxOut_fragment;
import com.golbal.pda.GolbalView;
import com.imple.getnumber.Getnumber;
import com.imple.getnumber.StopNewClearBox;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.GetBoxDetailListBiz;

/**
 * ATM未清回收钞箱出库勾选后界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ResourceAsColor")
public class NotClearBoxDetailInfoDo extends Activity implements OnTouchListener {

	Button box_detail; // 钞箱明细
	Button box_do; // 出库操作
	ArrayList<String> planNum; // 计划编号
	String way; // 路线
	Bundle bundleBussin; // 接收业务
	String busin; // 业务名称
	TextView bizName; // 业务名称显示控件

	public static int isfirst = 0; // 回收钞箱入库是否是首次入库
	LinearLayout fragment_layout; // 装载fragment
	Fragment notclearoutf; // 为清回收钞箱出库
	ImageView back_pre; // 返回上一级

	private GetBoxDetailListBiz boxDetailList;

	GetBoxDetailListBiz getBoxDetailList() {
		return boxDetailList = boxDetailList == null ? new GetBoxDetailListBiz() : boxDetailList;
	}

	private ManagerClass managerClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.y_emptvan_goout_detail);
		managerClass = new ManagerClass();
		managerClass.getRuning().remove();

		// 全局异常处理
		// CrashHandler.getInstance().init(this);

		Log.i("11", "111");
		box_detail = (Button) findViewById(R.id.money_box_detail);
		box_do = (Button) findViewById(R.id.box_do);
		back_pre = (ImageView) findViewById(R.id.back_empty);
		bizName = (TextView) findViewById(R.id.bizname);

		box_detail.setOnTouchListener(this);
		box_do.setOnTouchListener(this);
		back_pre.setOnTouchListener(this);

		// 接收传进来的bundle
		bundleBussin = getIntent().getExtras();
		busin = bundleBussin.getString("businName");
		bizName.setText(busin + "明细");
		box_detail.setText("总钞箱明细");
		planNum = bundleBussin.getStringArrayList("number");

		Log.i("busin", busin);

		// 判断是何种操作
		if ("未清回收钞箱出库".equals(busin)) {
			box_do.setText("出库操作");

		}
		Log.i("22", "22");

		if ("未清回收钞箱出库".equals(busin)) { //
			if (notclearoutf == null) {
				notclearoutf = new NotClearBoxOut_fragment();
			}
			managerClass.getGolbalView().replaceFragment(this, R.id.fragment_address, notclearoutf, bundleBussin);
		}

		// 把当前activity放进集合
		// GApplication.addActivity(this,"0boxd");

	}

	int judgeGetDetailList = 0;

	// 触摸事件
	@Override
	public boolean onTouch(View view, MotionEvent even) {
		// 按下的时候
		if (MotionEvent.ACTION_DOWN == even.getAction()) {
			switch (view.getId()) {
			// 返回
			case R.id.back_empty:
				back_pre.setImageResource(R.drawable.back_cirle_press);
				break;
			// 钞箱明细
			case R.id.money_box_detail:
				box_detail.setBackgroundResource(R.drawable.buttom_select_press);
				break;
			case R.id.box_do:
				// 出库
				box_do.setBackgroundResource(R.drawable.buttom_select_press);
				break;
			}
		}

		// 手指松开的时候
		if (MotionEvent.ACTION_UP == even.getAction()) {
			switch (view.getId()) {
			// 钞箱明细
			case R.id.money_box_detail:
				box_detail.setBackgroundResource(R.drawable.buttom_selector_bg);
				if (isfirst > 0) {
					managerClass.getSureCancel().makeSuerCancel(NotClearBoxDetailInfoDo.this, "首次回收钞箱入库不用获取明细",
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									managerClass.getSureCancel().remove();

								}
							}, true);
				} else {
					managerClass.getGolbalutil().gotoActivity(NotClearBoxDetailInfoDo.this, MoneyBoxDetial.class,
							bundleBussin, managerClass.getGolbalutil().ismover);
				}
				judgeGetDetailList += 1;
				break;
			// 各种操作，出库
			case R.id.box_do:
				// //出库前清空前一次的数据
				if (StopNewClearBox.list.size() > 0 || StopNewClearBox.liststr.size() > 0) {
					try {
						StopNewClearBox.list.clear();
						StopNewClearBox.liststr.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				Log.i("list_StopNewClearBox", StopNewClearBox.list.size() + "");
				box_do.setBackgroundResource(R.drawable.buttom_selector_bg);

				if (judgeGetDetailList == 0) {
					GolbalView.toastShow(NotClearBoxDetailInfoDo.this, "请先获取钞箱明细，再进行出入库。");
				} else {

					/*
					 * managerClass.getGolbalutil().gotoActivity( NotClearBoxDetailInfoDo.this,
					 * NotClearBoxDoDetail.class, bundleBussin,
					 * managerClass.getGolbalutil().ismover);
					 */
					Bundle bundle = new Bundle();
					bundle.putString("business", busin);
					managerClass.getGolbalutil().gotoActivity(NotClearBoxDetailInfoDo.this, BoxAddStop.class, bundle,
							managerClass.getGolbalutil().ismover);
				}

				break;
			// 返回
			case R.id.back_empty:
				back_pre.setImageResource(R.drawable.back_cirle);
				NotClearBoxDetailInfoDo.this.finish();
				break;
			}
			managerClass.getGolbalutil().ismover = 0;
		}
		// 手指移动的时候
		if (MotionEvent.ACTION_MOVE == even.getAction()) {
			managerClass.getGolbalutil().ismover++;
		}
		// 意外中断事件取消
		if (MotionEvent.ACTION_CANCEL == even.getAction()) {
			back_pre.setImageResource(R.drawable.back_cirle_press);

			switch (view.getId()) {
			// 返回
			case R.id.back_empty:
				back_pre.setImageResource(R.drawable.back_cirle);
				break;
			// 钞箱明细
			case R.id.money_box_detail:
				box_detail.setBackgroundResource(R.drawable.buttom_selector_bg);
				break;
			case R.id.box_do:
				// 出库
				box_do.setBackgroundResource(R.drawable.buttom_selector_bg);
				break;
			}
			managerClass.getGolbalutil().ismover = 0;
		}
		return true;
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
