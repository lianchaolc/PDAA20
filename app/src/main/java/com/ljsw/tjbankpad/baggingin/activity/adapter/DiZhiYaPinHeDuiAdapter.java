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
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYapinItemhedui;

/**
 * Created by Administrator on 2018/6/26. 列表核对
 * 
 */

public class DiZhiYaPinHeDuiAdapter extends BaseAdapter {
	private List<String> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public DiZhiYaPinHeDuiAdapter(List<String> walkList, Context mContext) {
		this.walkList = walkList;
		this.mContext = mContext;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return walkList.size();

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
		mVHAWailk.dizhiyapinitem.setText(walkList.get(position).toString());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView dizhiyapinid;
		public TextView dizhiyapinitem;
		public TextView dizhiyapintype;
	}

}
