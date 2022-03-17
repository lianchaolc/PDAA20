package com.ljsw.checkcklibrarybydz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.checkcklibrarybydz.bean.CheckLibraryFaceBean;
import com.ljsw.checkcklibrarybydz.bean.CheckeLibraryDZTaskBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2022/2/16.
 */

public class CheckLibraryDZFaceAdapter extends BaseAdapter{
    private List<CheckLibraryFaceBean> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryDZFaceAdapter(List<CheckLibraryFaceBean> walkList, Context mContext) {
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
        CheckLibraryDZFaceAdapter.ViewHolderAdapterCheckLibraryAdapter mVHAWailk = null;
        if (mVHAWailk == null) {
            mVHAWailk = new CheckLibraryDZFaceAdapter.ViewHolderAdapterCheckLibraryAdapter();
            convertView = inflater.inflate(R.layout.activity_checkclibraryfacedz, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.checklibrary_taskbydztextview);
            TextView lv_checklibrarytaskcount = (TextView) convertView.findViewById(R.id.checklibrary_countsbydztextview);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            mVHAWailk.lv_checklibrarytaskcount = lv_checklibrarytaskcount;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (CheckLibraryDZFaceAdapter.ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.lv_checklibrarytaskno.setText(walkList.get(position).getTableNo());
        mVHAWailk.lv_checklibrarytaskcount.setText(walkList.get(position).getLossCounts());
        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
        public TextView lv_checklibrarytaskcount;
    }
}
