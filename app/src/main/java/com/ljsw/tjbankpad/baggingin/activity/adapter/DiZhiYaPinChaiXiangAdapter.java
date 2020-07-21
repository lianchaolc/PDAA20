package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;

/**
 * Created by Administrator on 2018/6/26. 地址押品拆箱的适配器
 */

public class DiZhiYaPinChaiXiangAdapter extends BaseAdapter {
	private List<DiZhiYaPinChaiXiang> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public DiZhiYaPinChaiXiangAdapter(List<DiZhiYaPinChaiXiang> walkList, Context mContext) {
		this.walkList = walkList;
		this.mContext = mContext;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		Log.e("", "!!!!!!!!!" + walkList.size() + walkList.get(0).getClearTaskNum());

		return walkList.size();

	}

	@Override
	public Object getItem(int position) {
		Log.e("", "!!!!!!!!!" + walkList.size() + walkList.get(position).getClearTaskNum());
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
			convertView = inflater.inflate(R.layout.listview_walkie_item, null, false);
			TextView dizhiyapinid = (TextView) convertView.findViewById(R.id.lv_dizhiyapinid);
			TextView dizhiyapinitem = (TextView) convertView.findViewById(R.id.dizhiyapinitem);
			TextView dizhiyapintype = (TextView) convertView.findViewById(R.id.dizhiyapintype);
			mVHAWailk.dizhiyapinid = dizhiyapinid;
			mVHAWailk.dizhiyapinitem = dizhiyapinitem;
			mVHAWailk.dizhiyapintype = dizhiyapintype;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
		mVHAWailk.dizhiyapinid.setText(position + 1 + "");
		mVHAWailk.dizhiyapinitem.setText(walkList.get(position).getClearTaskNum());
		mVHAWailk.dizhiyapintype.setText(walkList.get(position).getCount());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView dizhiyapinid;
		public TextView dizhiyapinitem;
		public TextView dizhiyapintype;
	}

}
