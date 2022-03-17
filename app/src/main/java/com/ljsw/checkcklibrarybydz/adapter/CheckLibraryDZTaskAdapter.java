package com.ljsw.checkcklibrarybydz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.checkcklibrarybydz.bean.CheckeLibraryDZTaskBean;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryAdapter;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;

import java.util.List;

/**
 * Created by Administrator on 2022/2/16.
 */

public class CheckLibraryDZTaskAdapter extends BaseAdapter {
    private List<CheckeLibraryDZTaskBean> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryDZTaskAdapter(List<CheckeLibraryDZTaskBean> walkList, Context mContext) {
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
        CheckLibraryDZTaskAdapter.ViewHolderAdapterCheckLibraryAdapter mVHAWailk = null;
        if (mVHAWailk == null) {
            mVHAWailk = new CheckLibraryDZTaskAdapter.ViewHolderAdapterCheckLibraryAdapter();
            convertView = inflater.inflate(R.layout.checklibrarybydztasklv, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.lv_checklibrarytaskno);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.lv_checklibrarytaskno.setText(walkList.get(position).getTaskNO());
        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
    }
}
