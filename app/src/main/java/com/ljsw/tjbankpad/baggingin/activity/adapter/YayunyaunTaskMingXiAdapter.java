package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuTookinfoAndTrueEntity;

/**
 * Created by Administrator on 2019/11/26. lianc
 * 
 */

public class YayunyaunTaskMingXiAdapter extends BaseAdapter {
	private List<YayunSelectRewuTookinfoAndTrueEntity> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public YayunyaunTaskMingXiAdapter(List<YayunSelectRewuTookinfoAndTrueEntity> yayunmingselectlist,
			Context mContext) {
		this.walkList = yayunmingselectlist;
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
			convertView = inflater.inflate(R.layout.yyytmxselectlistitem, null, false);
			TextView yyyyayunorgname = (TextView) convertView.findViewById(R.id.yyyyayunorgname);
			TextView yyytooktast = (TextView) convertView.findViewById(R.id.yyytooktast);
			TextView yyyupdatatast = (TextView) convertView.findViewById(R.id.yyyupdatatast);
			mVHAWailk.yyyyayunorgname = yyyyayunorgname;
			mVHAWailk.yyytooktast = yyytooktast;
			mVHAWailk.yyyupdatatast = yyyupdatatast;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}
		mVHAWailk.yyyyayunorgname.setText(walkList.get(position).getCorpname());

		System.out.print("=---=-------" + walkList.get(position).getState());
//            if(walkList.get(position).getState().equals("00")){
//            	 mVHAWailk.yyytooktast.setText("待交接");
//            }else if(walkList.get(position).getState().equals("01")){
//            	 mVHAWailk.yyytooktast.setText("待交接");
//            }else if(walkList.get(position).getState().equals("02")||
//            		walkList.get(position).getState().equals("04")){
//            	 mVHAWailk.yyytooktast.setText("已交接");
//            }else{
		mVHAWailk.yyytooktast.setText("-");
//            }

		if (walkList.get(position).getState().equals("00")) {
			mVHAWailk.yyyupdatatast.setText("待确认");
		} else if (walkList.get(position).getState().equals("01")) {
			mVHAWailk.yyyupdatatast.setText("待交接");
		} else if (walkList.get(position).getState().equals("02") || walkList.get(position).getState().equals("04")) {
			mVHAWailk.yyyupdatatast.setText("已交接");
		} else {
			mVHAWailk.yyyupdatatast.setText("-");
		}
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView yyyyayunorgname;
		public TextView yyytooktast;
		public TextView yyyupdatatast;
	}

}
