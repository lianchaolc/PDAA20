package com.ljsw.collateraladministratorsorting.selecttaskadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskByCollateralEntity;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskListByCollateralBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 * 适配器
 */

public class SelectTaskCollaterAdapter extends BaseAdapter {
	private List<SelectTaskListByCollateralBean> SelectTaskByCollateraList;
	private Context mContext;
	private LayoutInflater inflater;

	public SelectTaskCollaterAdapter(List<SelectTaskListByCollateralBean> SelectTaskByCollateraList, Context mContext) {
		this.SelectTaskByCollateraList = SelectTaskByCollateraList;
		this.mContext = mContext;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return SelectTaskByCollateraList.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderAdapterWailk mVHAWailk = null;
		if (mVHAWailk == null) {
			mVHAWailk = new ViewHolderAdapterWailk();
			//  这里布局不要改动
			convertView = inflater.inflate(R.layout.item_selecttaskcollater, null, false);
			TextView dizhiyapinid = (TextView) convertView.findViewById(R.id.tv_selecttaskcollater);
			TextView dizhiyapinitem = (TextView) convertView.findViewById(R.id.tv_selecttaskcollaterName);
//			TextView dizhiyapintype = (TextView) convertView.findViewById(R.id.dizhiyapintype);
			mVHAWailk.dizhiyapinid = dizhiyapinid;
			mVHAWailk.dizhiyapinitem = dizhiyapinitem;
//			mVHAWailk.dizhiyapintype = dizhiyapintype;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
		mVHAWailk.dizhiyapinid.setText(SelectTaskByCollateraList.get(position).getClearTaskNum());
		mVHAWailk.dizhiyapinitem.setText(SelectTaskByCollateraList.get(position).getClearTaskType());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView dizhiyapinid;
		public TextView dizhiyapinitem;
		public TextView dizhiyapintype;
	}

}
