package com.fragment.pda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.entity.Box;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.GetPlanWayListBiz;
import com.moneyboxadmin.pda.BoxDetailInfoDo;
import com.moneyboxadmin.pda.MoneyBoxDetial;

/**
 * 2017-8-9
 * 
 * @author wangmeng 为清回收钞箱出库fragment
 *
 */
//出库内容显示
public class NotClearBoxOut_fragment extends Fragment {

	ListView listView;
	Bundle bundle;
	DataListAd adapter;
	private ManagerClass managerClass;
	int boxTatal = 0;

	private ArrayList<String> planNumList = new ArrayList<String>();
	List<Box> showlist = new ArrayList<Box>();

	public GetPlanWayListBiz planwaylist;

	public GetPlanWayListBiz getPlanwaylist() {
		return planwaylist = planwaylist == null ? new GetPlanWayListBiz() : planwaylist;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		managerClass = new ManagerClass();
		// 接收当activity传递 进来的参数
		bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			planNumList = bundle.getStringArrayList("number");
			Log.i("palnNum", planNumList.toString());
			Log.i("palnNum", getPlanwaylist().list_box + "");
		}

		// 组装数据
		tranDataList();

		View view = inflater.inflate(R.layout.notclearboxout_fragment, null);
		// 显示明细中钞箱总数
		TextView box_total = (TextView) view.findViewById(R.id.box_total);
		box_total.setText("钞箱总数:" + boxTatal);

		listView = (ListView) view.findViewById(R.id.list_not_clear);
		if (adapter == null) {
			adapter = new DataListAd();
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void tranDataList() {
		// 以计划编号为键，box为值
		Map<String, Box> dataMap = new HashMap<String, Box>();
		List<Box> list = getPlanwaylist().list_box;
		for (int i = 0; i < list.size(); i++) {
			Box box = list.get(i);
			dataMap.put(box.getPlanNum(), box);
		}

		for (int i = 0; i < planNumList.size(); i++) {
			String plan = planNumList.get(i);
			for (Map.Entry<String, Box> entry : dataMap.entrySet()) {
				if (plan.equals(entry.getKey())) {
					Box box = entry.getValue();
					// 总钞箱数量
					boxTatal += Integer.parseInt(box.getBoxNum());
					showlist.add(box);

				}
			}
		}
	}

	static class ViewHolder {
		TextView planNum;
		TextView lineNum;
		TextView palnType;
		TextView boxNum;
	}

	class DataListAd extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return showlist.size();
		}

		@Override
		public Object getItem(int index) {
			// TODO Auto-generated method stub
			return showlist.get(index);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			View v = view;
			final ViewHolder holder;
			if (v == null) {
				holder = new ViewHolder();
				v = GolbalView.getLF(getActivity()).inflate(R.layout.notclearboxout_list_fragment, null);
				holder.planNum = (TextView) v.findViewById(R.id.sub_addmoney_plannum);
				holder.lineNum = (TextView) v.findViewById(R.id.sub_addmoney_line);
				holder.palnType = (TextView) v.findViewById(R.id.sub_box_type);
				holder.boxNum = (TextView) v.findViewById(R.id.sub_box_number);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			Box box = (Box) getItem(arg0);
			holder.planNum.setText(box.getPlanNum());
			holder.lineNum.setText(box.getWay());
			holder.palnType.setText(box.getType());
			holder.boxNum.setText(box.getBoxNum());
			v.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (MotionEvent.ACTION_UP == arg1.getAction()) {

						holder.planNum.setBackgroundResource(R.color.gray_background);

						Bundle bundle = new Bundle();
						bundle.putString("number", holder.planNum.getText().toString());
						bundle.putString("businName", "未清回收钞箱出库-item");

						managerClass.getGolbalutil().gotoActivity(getActivity(), MoneyBoxDetial.class, bundle,
								managerClass.getGolbalutil().ismover);

						managerClass.getGolbalutil().ismover = 0;
					}
					return true;
				}
			});
			return v;
		}
	}

}
