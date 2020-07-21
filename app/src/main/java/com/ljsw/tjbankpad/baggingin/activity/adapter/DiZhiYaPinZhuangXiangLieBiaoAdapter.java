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

/**
 * Created by Administrator on 2018/6/26. 2 个textview
 */

public class DiZhiYaPinZhuangXiangLieBiaoAdapter extends BaseAdapter {
	private List<String> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public DiZhiYaPinZhuangXiangLieBiaoAdapter(List<String> walkList, Context mContext) {
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
			convertView = inflater.inflate(R.layout.listview_wangdianmingcheng_item, null, false);
			TextView dizhiyapinitem = (TextView) convertView.findViewById(R.id.liebaiodizhiyapinitem);
			TextView dizhiyapintype = (TextView) convertView.findViewById(R.id.liebaiodizhiyapintype);
			mVHAWailk.dizhiyapinitem = dizhiyapinitem;
			mVHAWailk.dizhiyapintype = dizhiyapintype;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
//            mVHAWailk.dizhiyapinid.setText(position+1+"");
		mVHAWailk.dizhiyapinitem.setText(walkList.get(position).toString());
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView dizhiyapinitem;
		public TextView dizhiyapintype;
	}

	public void refersh(List<String> walkList) {
		this.walkList = walkList;
		System.out.print("更新");
		notifyDataSetChanged();
	}

}
