package com.ljsw.pdachecklibrary.adapterbycheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryTableEntity;

import java.util.List;

/**
 * Created by Administrator on 2021/8/25.
 * 格子的适配器
 */

public class CheckLibraryTableAdapter extends BaseAdapter{

    private List<CheckLibraryTableEntity> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryTableAdapter(List<CheckLibraryTableEntity> walkList, Context mContext) {
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
     ViewHolderAdapterCheckLibraryAdapter mVHAWailk = null;
        if (mVHAWailk == null) {
            mVHAWailk = new CheckLibraryTableAdapter.ViewHolderAdapterCheckLibraryAdapter();// 这个布局不要改
            convertView = inflater.inflate(R.layout.listview_wangdianmingcheng_item, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.accountinfotion_listitem_tvinfo);
            TextView lv_checklibrarytaskcounts = (TextView) convertView.findViewById(R.id.accountinfotion_listitem_tvtype);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            mVHAWailk.lv_checklibrarytaskcounts = lv_checklibrarytaskcounts;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (CheckLibraryTableAdapter.ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        //面柜子
        mVHAWailk.lv_checklibrarytaskno.setText(walkList.get(position).getCABINETNUMBER()+"-"+walkList.get(position).getFACENUMBER());
        mVHAWailk.lv_checklibrarytaskcounts.setText(walkList.get(position).getCOUNT()+"");
        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
        public TextView lv_checklibrarytaskcounts;
    }

}
