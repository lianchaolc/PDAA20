package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.pda.R;

/**
 * Created by Administrator on 2018/6/26.
 */

public class WalkieDataCountWithDeleteButtonAdapter extends BaseAdapter {
	private List<String> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public WalkieDataCountWithDeleteButtonAdapter(List<String> walkList, Context mContext) {
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
			convertView = inflater.inflate(R.layout.listview_withdeletebtn_item, null, false);
			TextView intercomid = (TextView) convertView.findViewById(R.id.liebaiodizhiyapinitem);
			Button liebaiodizhiyapintype = (Button) convertView.findViewById(R.id.liebaiodizhiyapintype);
			mVHAWailk.intercomid = intercomid;
			mVHAWailk.liebaiodizhiyapintype = liebaiodizhiyapintype;

			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
		mVHAWailk.intercomid.setText(walkList.get(position).toString());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView intercomid;
		private Button liebaiodizhiyapintype;// 删除按钮
	}

	public void refersh(List<String> walkList) {
		this.walkList = walkList;
		System.out.print("更新");
		notifyDataSetChanged();
	}

}
