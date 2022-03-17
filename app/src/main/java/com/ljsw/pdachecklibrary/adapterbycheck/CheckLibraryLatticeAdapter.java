package com.ljsw.pdachecklibrary.adapterbycheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.pdachecklibrary.CheckLibraryLatticeActivity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryLatticeEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryTableEntity;

import java.util.List;

/**
 * Created by Administrator on 2021/8/25.
 * 柜子的适配器 中格子
 */

public class CheckLibraryLatticeAdapter extends BaseAdapter{

    private List<CheckLibraryLatticeEntity> walkList;
    private Context mContext;
    private String strtable;
    private LayoutInflater inflater;

    public CheckLibraryLatticeAdapter(List<CheckLibraryLatticeEntity> walkList, Context mContext) {
        this.walkList = walkList;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public CheckLibraryLatticeAdapter(List<CheckLibraryLatticeEntity> walkList, Context mContext,String table) {
        this.walkList = walkList;
        this.mContext = mContext;
        this.strtable = table;
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
            mVHAWailk = new CheckLibraryLatticeAdapter.ViewHolderAdapterCheckLibraryAdapter();// 这个布局不要改
            convertView = inflater.inflate(R.layout.listview_wangdianmingcheng_item, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.accountinfotion_listitem_tvinfo);
            TextView lv_checklibrarytaskcounts = (TextView) convertView.findViewById(R.id.accountinfotion_listitem_tvtype);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            mVHAWailk.lv_checklibrarytaskcounts = lv_checklibrarytaskcounts;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (CheckLibraryLatticeAdapter.ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.lv_checklibrarytaskno.setText(strtable+"-"+walkList.get(position).getGRIDNUMBER());
        mVHAWailk.lv_checklibrarytaskcounts.setText(walkList.get(position).getCOUNT()+"");
        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
        public TextView lv_checklibrarytaskcounts;
    }

}
