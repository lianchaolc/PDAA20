package com.ljsw.tjbankpad.baggingin.activity.adapter;

import java.util.List;

import com.example.pda.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnnerAdapter extends BaseAdapter {
	private List<String> list;
	private Context mContext;

	public SpinnnerAdapter(List<String> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void refersh(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view = LayoutInflater.from(mContext).inflate(R.layout.item_spinner_left, null);
		TextView tv = (TextView) view.findViewById(R.id.spnnertv);
		tv.setText(list.get(i));
		tv.setTextSize(22f);
		return view;
	}

}
