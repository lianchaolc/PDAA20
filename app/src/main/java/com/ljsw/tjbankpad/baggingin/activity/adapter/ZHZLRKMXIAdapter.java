package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.example.pda.R;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ZHZLRKMXIAdapter extends BaseAdapter {
	private List<String> ZHZLRKList;
	private Context mContext;
	private LayoutInflater inflater;

	public ZHZLRKMXIAdapter(List<String> ZHZLRKList, Context mContext) {
		this.ZHZLRKList = ZHZLRKList;
		this.mContext = mContext;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ZHZLRKList.size();

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
			convertView = inflater.inflate(R.layout.listview_zxzlrkminxi_item, null, false);
			TextView lvzxzlruid = (TextView) convertView.findViewById(R.id.lvzxzlruid);
			TextView lvzxzlruitemnumber = (TextView) convertView.findViewById(R.id.lvzxzlruitemnumber);
			TextView lvzxzlruitemlocation = (TextView) convertView.findViewById(R.id.lvzxzlruitemlocation);
			mVHAWailk.lvzxzlruid = lvzxzlruid;
			mVHAWailk.lvzxzlruitemnumber = lvzxzlruitemnumber;
			mVHAWailk.lvzxzlruitemlocation = lvzxzlruitemlocation;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
		mVHAWailk.lvzxzlruid.setText((position + 1) + "");
		mVHAWailk.lvzxzlruitemnumber.setText("DZ00000" + (position + 1));
		mVHAWailk.lvzxzlruitemlocation.setText(ZHZLRKList.get(position).toString());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView lvzxzlruid;
		private TextView lvzxzlruitemnumber;// 编号
		private TextView lvzxzlruitemlocation; // 位置
	}

}
