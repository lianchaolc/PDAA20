package com.fragment.pda;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.entity.BoxDetail;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.GetBoxDetailListBiz;

public class MoneyAndBoxNum_fragment extends Fragment {

	private GetBoxDetailListBiz getBoxDetailList;

	public GetBoxDetailListBiz getGetBoxDetailList() {
		return getBoxDetailList = getBoxDetailList == null ? new GetBoxDetailListBiz() : getBoxDetailList;
	}

	ListView listview;
	String planNum; // 计划编号
	String bizName; // 业务名称
	LinearLayout biaoti; // 标题栏
	LinearLayout notdata; // 无数据时提示
	OnClickListener clickreplace;
	Button btn_notdata;
	TextView boxNum; // 钞箱编号或数量

	Ad ad;
	private ManagerClass managerClass;

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		managerClass = new ManagerClass();
		// 重试单击事件
		clickreplace = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				managerClass.getAbnormal().remove();
				managerClass.getRuning().runding(getActivity(), "正在获取钞箱明细...");
				// 连接超时后确定再次请求数据
				getGetBoxDetailList().getBoxDetailList(planNum, bizName);

			}
		};
		bizName = getArguments().getString("businName");
		/** begin modify by wangmeng */
		if ("未清回收钞箱出库".equals(bizName)) {
			ArrayList<String> planNumList = getArguments().getStringArrayList("number");
			planNum = planNumList.get(0) + ",";
			for (int i = 1; i < planNumList.size(); i++) {
				planNum += planNumList.get(i) + ",";
			}
			planNum = planNum.substring(0, planNum.length() - 1);
		} else {
			planNum = getArguments().getString("number");
		}
		/** end modify by wangmeng */
		Log.i("bizName111111111111", bizName);
		Log.i("planNum111111111111", planNum);

		// hand通知操作
		getGetBoxDetailList().hand_detail = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 移除获取钞箱明细提示
				managerClass.getRuning().remove();
				super.handleMessage(msg);
				if (msg.what == 1) { // 有数据
					notdata.setVisibility(View.GONE);
					if (ad == null) {
						ad = new Ad();
						listview.setAdapter(ad);
					} else {
						ad.notifyDataSetChanged();
					}

				} else if (msg.what == 0) { // 没数据

					notdata.setVisibility(View.VISIBLE);

				} else if (msg.what == -1) { // 连接超时
					notdata.setVisibility(View.GONE);
					managerClass.getAbnormal().timeout(getActivity(), "连接超时，要重试吗？", clickreplace);
				}
			}
		};
		Log.i("planNum", planNum);

		/**
		 * modify by wangmeng 动态添加标题
		 */

		View view = inflater.inflate(R.layout.money_and_boxnum_fragment, null);
		if ("空钞箱出库".equals(bizName)) {
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.linearlayout_head);
			// 设置权重
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
			// fragment中原有的TextView（品牌信息和钞箱编号）
			TextView child1 = (TextView) layout.getChildAt(0);
			TextView child2 = (TextView) layout.getChildAt(1);
			child1.setLayoutParams(lp);
			child2.setLayoutParams(lp);
			// 动态新增TextView（ATM类型）
			TextView tv = new TextView(getActivity());
			tv.setGravity(Gravity.CENTER);
			tv.setText("ATM类型");
			tv.setTextColor(Color.parseColor("#bf2929"));
			tv.setLayoutParams(lp);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
			/* tv.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT); */
			tv.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
			layout.addView(tv);
		}

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		listview = (ListView) getActivity().findViewById(R.id.listview_boxdetial);
		notdata = (LinearLayout) getActivity().findViewById(R.id.layout_notdata);
		boxNum = (TextView) getActivity().findViewById(R.id.boxnum_detail);

		if ("空钞箱出库".equals(bizName)) {
			boxNum.setText("钞箱数量");
		} else {
			boxNum.setText("钞箱编号");
		}

		if (ad == null) {
			// 提示获取钞箱明细
			managerClass.getRuning().runding(getActivity(), "正在获取钞箱明细...");
			// 开始请求数据
			getGetBoxDetailList().getBoxDetailList(planNum, bizName);
		} else {
			ad.notifyDataSetChanged();
		}
	}

	// 适配器
	class Ad extends BaseAdapter {

		@Override
		public int getCount() {
			return getGetBoxDetailList().list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return getGetBoxDetailList().list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View v = arg1;
			ViewHolder holder = new ViewHolder();
			if (v == null) {
				v = GolbalView.getLF(getActivity()).inflate(R.layout.boxinformation_item, null);
				holder.brand = (TextView) v.findViewById(R.id.brand);
				holder.num = (TextView) v.findViewById(R.id.box_num);
				if ("空钞箱出库".equals(bizName)) {
					TextView tv = (TextView) v.findViewById(R.id.atm_type);
					tv.setVisibility(View.VISIBLE);
					holder.atmType = (TextView) tv;
				}

				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			BoxDetail box = (BoxDetail) getItem(arg0);

			holder.brand.setText(box.getBrand());
			holder.num.setText(box.getNum());
			if ("空钞箱出库".equals(bizName)) {
				holder.atmType.setText(box.getAtmType());
			}

			return v;
		}

	}

	static class ViewHolder {
		TextView brand;
		TextView num;
		TextView atmType;
	}

}
