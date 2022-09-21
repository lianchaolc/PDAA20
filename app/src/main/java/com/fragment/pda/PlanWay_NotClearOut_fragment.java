package com.fragment.pda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.entity.Box;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.loginsystem.biz.SystemLoginBiz;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.biz.GetEmptyRecycleCashboxInListBiz;
import com.moneyboxadmin.biz.GetPlanWayListBiz;
import com.moneyboxadmin.pda.NotClearBoxDetailInfoDo;
import com.moneyboxadmin.pda.PlanWay.PlanFragment;

public class PlanWay_NotClearOut_fragment extends Fragment implements PlanFragment {
	// 显示路线的fragment
	Bundle bundle_listitem;
	String busin; // 业务名称
	String type; // 业务类别 (在行，离行)
	ListView listview;
	OnClickListener clickReplace; // 重试事件
	TextView planNum;
	TextView way;
	WayAd wayad;
	LinearLayout notdata;
	Button btnNotData;
	private ManagerClass managerClass;
	private ArrayList<String> planNumList = new ArrayList<String>();
	View view;// 整体界面布局
	RelativeLayout relayout_head;// 将要添加界面元素
	TextView tip;// 选择提示
	int selectedNum = 0;// 已经选择的数量

	// 系统登陆
	private SystemLoginBiz systemLogin;

	SystemLoginBiz getSystemLogin() {
		return systemLogin = systemLogin == null ? new SystemLoginBiz() : systemLogin;
	}

	// 获取路线
	private GetPlanWayListBiz planwaylist;

	GetPlanWayListBiz getPlanwaylist() {
		return planwaylist = planwaylist == null ? new GetPlanWayListBiz() : planwaylist;
	}

	private GetEmptyRecycleCashboxInListBiz emptyRecycleCashboxInList;

	GetEmptyRecycleCashboxInListBiz getEmptyRecycleCashboxInList() {
		return emptyRecycleCashboxInList = emptyRecycleCashboxInList == null ? new GetEmptyRecycleCashboxInListBiz()
				: emptyRecycleCashboxInList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		managerClass = new ManagerClass();
		// 接收上一个页面传递进来的参数
		busin = getArguments().getString("business");

		view = inflater.inflate(R.layout.planway_fragment, null);
		relayout_head = (RelativeLayout) view.findViewById(R.id.head_notclear);
		// 为信息列表设置垂直滚动条
		setListViewVerticalScrollBar(relayout_head);

		// 未清回收钞箱出库获取 重试单击事件
		clickReplace = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				managerClass.getAbnormal().remove();
				managerClass.getRuning().runding(getActivity(), "正在获取路线...");
				// 参数机构ID
				getPlanwaylist().getBoxList(busin, GApplication.user.getOrganizationId());
			}
		};
		// 未清回收钞箱出库列表
		getPlanwaylist().hand_way = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.i("移除下", "移除下");
				// 移除提示
				managerClass.getRuning().remove();
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					packagingDataToShow();
					// 添加确定按钮
					addSureButton(view, relayout_head);
					notdata.setVisibility(View.GONE);
					// listview加载数据
					if (wayad == null) {
						wayad = new WayAd();
						listview.setAdapter(wayad);
					} else {
						wayad.notifyDataSetChanged();
					}
					break;
				case -1:
					managerClass.getAbnormal().timeout(getActivity(), "连接异常，重新连接？", clickReplace);
					notdata.setVisibility(View.GONE);
					break;
				case 0:
					// 没有数据
					notdata.setVisibility(View.VISIBLE);
					break;
				case -4:
					managerClass.getAbnormal().timeout(getActivity(), "连接超时，重新连接？", clickReplace);
					notdata.setVisibility(View.GONE);
					break;
				}
			}
		};

		return view;

	}

	private ArrayList<Map<String, ArrayList<Box>>> showList;

	public void packagingDataToShow() {
		List<Box> list = getPlanwaylist().list_box;
		Map<String, ArrayList<Box>> showMap = new ConcurrentHashMap<String, ArrayList<Box>>();
		// 遍历真个list，获取showMap
		for (Box box : list) {
			String lineNume = box.getWay().trim();
			String planNum = box.getPlanNum();

			ArrayList<Box> sub_list = new ArrayList<Box>();

			Box sub_Box = new Box();
			sub_Box.setPlanNum(planNum);
			sub_Box.setState("false");// 自己保存checkbox的状态，初始化为false

			if (showMap.isEmpty()) {
				sub_list.add(sub_Box);
				showMap.put(lineNume, sub_list);
			} else {
				// 组装数据，已线路为键，计划单号的list为值
				if (showMap.containsKey(lineNume)) {
					// 如果howMap中包涵该线路，则在该线路中添加该计划
					showMap.get(lineNume).add(sub_Box);
				} else {
					// 如果不包含，则以该线路为键，计划单号放入新的list中，组装map
					sub_list.add(sub_Box);
					showMap.put(lineNume, sub_list);
				}
			}

		}
		showList = new ArrayList<Map<String, ArrayList<Box>>>();
		// 遍历showMap，组装showList
		for (Map.Entry<String, ArrayList<Box>> entry : showMap.entrySet()) {
			Map<String, ArrayList<Box>> subMap = new HashMap<String, ArrayList<Box>>();
			subMap.put(entry.getKey(), entry.getValue());
			showList.add(subMap);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		planNum = (TextView) getActivity().findViewById(R.id.plan_num_fra);
		way = (TextView) getActivity().findViewById(R.id.plan_content_fra);
		listview = (ListView) getActivity().findViewById(R.id.listview_way);
		notdata = (LinearLayout) getActivity().findViewById(R.id.way_notdata);
		// 隐藏掉刷新按钮，省去刷新后，勾选的数据不清空
		ImageView refresh = (ImageView) getActivity().findViewById(R.id.refresh);
		refresh.setVisibility(View.GONE);

		planNum.setText("加钞路线");
		way.setText("计划数");

		if (wayad == null) {
			// 操作友好提示
			Log.i("aaaaaa", "aaaaaaaaa");
			managerClass.getRuning().runding(getActivity(), "正在获取路线...");
			// 获取路线业务数据，参数机构ID
			getPlanwaylist().getBoxList(busin, GApplication.user.getOrganizationId());

		} else {
			listview.setAdapter(wayad);
		}

	}

	// 适配器
	class WayAd extends BaseAdapter {
		@Override
		public int getCount() {

			return showList.size();// getPlanwaylist().list_box.size();
		}

		@Override
		public Object getItem(int arg0) {

			return showList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			View v = view;
			final ViewHolder holder;
			if (v == null) {
				holder = new ViewHolder();
				v = GolbalView.getLF(getActivity()).inflate(R.layout.c_way_notclearout, null);
				holder.planNum = (TextView) v.findViewById(R.id.bin_number);
				holder.planContent = (TextView) v.findViewById(R.id.line_num);
				holder.isfirst = (TextView) v.findViewById(R.id.isfirst);
				holder.checkBox = (CheckBox) v.findViewById(R.id.box_notclear);

				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
				holder.checkBox.getTag(arg0);
			}
			// 获取数据
			Map<String, ArrayList<Box>> subMap = (Map<String, ArrayList<Box>>) getItem(arg0);
			Set set = subMap.keySet();
			Iterator iter = set.iterator();
			String linenum = "";
			while (iter.hasNext()) {
				linenum = (String) iter.next();
			}
			final ArrayList<Box> planList = subMap.get(linenum);

			holder.planNum.setText(linenum);
			holder.planContent.setText(planList.size() + "");

			/* add by wangmeng 2017-8-3 */
			getPlanNumByButton(holder, planList);

			v.setOnTouchListener(new OnTouchListener() {
				// 触摸事件
				@Override
				public boolean onTouch(View view, MotionEvent even) {
					CheckBox box = (CheckBox) view.findViewById(R.id.box_notclear);

					if (MotionEvent.ACTION_DOWN == even.getAction()) {
						holder.planNum.setBackgroundResource(R.color.bleu_pressdown);
						holder.planContent.setBackgroundResource(R.color.bleu_pressdown);
					}

					if (MotionEvent.ACTION_UP == even.getAction()) {

						/* add by wangmeng 2017-8-3 */
						if (holder.checkBox.isChecked()) {
							// 如果已选中了，再次点击则取消
							holder.checkBox.setChecked(false);
						} else {
							holder.checkBox.setChecked(true);
						}

						getPlanNumByButton(holder, planList);

						holder.planNum.setBackgroundResource(R.color.gray_background);
						holder.planContent.setBackgroundResource(R.color.gray_background);

						managerClass.getGolbalutil().ismover = 0;
					}

					if (MotionEvent.ACTION_MOVE == even.getAction()) {
						managerClass.getGolbalutil().ismover++;
					}

					if (MotionEvent.ACTION_CANCEL == even.getAction()) {
						holder.planNum.setBackgroundResource(R.color.gray_background);
						holder.planContent.setBackgroundResource(R.color.gray_background);
						managerClass.getGolbalutil().ismover = 0;
					}

					return true;
				}
			});

			return v;
		}

	}

	static class ViewHolder {
		TextView planNum;
		TextView isfirst;
		TextView planContent;
		CheckBox checkBox;

	}

	@Override
	public void replace() {
		managerClass.getRuning().runding(getActivity(), "正在刷新...");
		// 获取路线业务数据，参数机构ID
		Log.i("busin", busin);
		tip.setVisibility(View.GONE);
		selectedNum = 0;
		planNumList.clear();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		planNumList.clear();
	}

	/* add by wangmeng 2017-8-3 */
	public void getPlanNumByButton(ViewHolder holder, final ArrayList<Box> planList) {

		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {// 选中了，则将plannum放入集合
					if ("false".equals(planList.get(0).getState())) {
						selectedNum += 1;
						for (Box box : planList) {
							box.setState("true");
							planNumList.add(box.getPlanNum());
						}

					}
				} else {// 从集合中删除
					if ("true".equals(planList.get(0).getState())) {
						selectedNum -= 1;
						for (Box box : planList) {
							box.setState("false");
							planNumList.remove(box.getPlanNum());
						}
					}

				}
				// Toast.makeText(getActivity(),planNumList.toString(),1000).show();
				tip.setText(getTipInfo());
			}
		});

		if ("false".equals(planList.get(0).getState())) {
			holder.checkBox.setChecked(false);

		} else {
			holder.checkBox.setChecked(true);

		}
	}

	public void addSureButton(View view, RelativeLayout relayout_head) {
		// 设置宽度和高度
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		// 设置LinearLayout在底部的属性
		LinearLayout linear = new LinearLayout(getActivity());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		linear.setLayoutParams(params);
		// linear.setGravity(Gravity.CENTER_HORIZONTAL);

		tip = new TextView(getActivity());
		tip.setText(getTipInfo());
		tip.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
		tip.setTextColor(getResources().getColor(R.color.gray));
		tip.setHeight(70);
		tip.setWidth(300);
		// 创建button
		final Button sure = new Button(getActivity());
		sure.setId(20170807);
		sure.setText("确认");
		sure.setTextColor(getResources().getColor(R.color.white));
		sure.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
		sure.setHeight(70);
		sure.setWidth(200);
		sure.setBackgroundResource(R.drawable.buttom_selector_bg);

		linear.addView(sure);
		linear.addView(tip);
		relayout_head.addView(linear);

		sure.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent even) {
				// 按下的时候
				if (MotionEvent.ACTION_DOWN == even.getAction()) {
					sure.setBackgroundResource(R.drawable.buttom_select_press);
				}

				// 手指松开的时候
				if (MotionEvent.ACTION_UP == even.getAction()) {
					sure.setBackgroundResource(R.drawable.buttom_selector_bg);
					if (planNumList.size() > 0) {// 线路选择不为空 BC01202203086
						// 把当前点击的项的编号、路线、业务、放进bundle
						bundle_listitem = new Bundle();
						bundle_listitem.putStringArrayList("number", planNumList);
						bundle_listitem.putString("businName", busin);

						selectedNum = 0;
						tip.setText("已选 ：0/" + showList.size());

						managerClass.getGolbalutil().gotoActivity(getActivity(), NotClearBoxDetailInfoDo.class,
								bundle_listitem, managerClass.getGolbalutil().ismover);
						getActivity().finish();
					} else {
						Toast.makeText(getActivity(), "请选择出库线路", Toast.LENGTH_LONG).show();
					}
					// Toast.makeText(getActivity(),"list:"+planNumList.toString()
					// ,Toast.LENGTH_LONG).show();
					managerClass.getGolbalutil().ismover = 0;
				}
				// 手指移动的时候
				if (MotionEvent.ACTION_MOVE == even.getAction()) {
					managerClass.getGolbalutil().ismover++;
				}
				// 意外中断事件取消
				if (MotionEvent.ACTION_CANCEL == even.getAction()) {
					managerClass.getGolbalutil().ismover = 0;
					sure.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
				return true;
			}
		});
	}

	public void setListViewVerticalScrollBar(RelativeLayout relayout_head) {
		// 信息列表
		ListView dataList = (ListView) relayout_head.getChildAt(1);
		RelativeLayout.LayoutParams lv_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 470);
		dataList.setLayoutParams(lv_params);
		dataList.setVerticalScrollBarEnabled(true);
	}

	public String getTipInfo() {
		String str = "已选 ：" + selectedNum + "/" + showList.size();
		return str;
	}
}
