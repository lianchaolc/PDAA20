package com.ljsw.checkcklibrarybydz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.checkcklibrarybydz.bean.CheckLibrayDZScannerUpBean;
import com.ljsw.checkcklibrarybydz.bean.CheckeLibraryDZTaskBean;

import java.util.List;

/**
 * Created by Administrator on 2022/2/16.
 * 扫描的并提交的适配器
 */

public class CheckLibraryDZScannerUpAdapter extends BaseAdapter {
    private List<CheckLibrayDZScannerUpBean> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryDZScannerUpAdapter(List<CheckLibrayDZScannerUpBean> walkList, Context mContext) {
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
        CheckLibraryDZScannerUpAdapter.ViewHolderAdapterCheckLibraryAdapter mVHAWailk = null;
        if (mVHAWailk == null) {
            mVHAWailk = new CheckLibraryDZScannerUpAdapter.ViewHolderAdapterCheckLibraryAdapter();
            convertView = inflater.inflate(R.layout.checklibrarybydzscanneruplv, null, false);
            TextView clsudzno = (TextView) convertView.findViewById(R.id.clsudzno);
            TextView clsudznostate = (TextView) convertView.findViewById(R.id.clsudznostate);
            mVHAWailk.clsudzno = clsudzno;
            mVHAWailk.clsudznostate = clsudznostate;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.clsudzno.setText(walkList.get(position).getScannnerNO());
        if (walkList.get(position).getNoState().isEmpty()) {
            mVHAWailk.clsudznostate.setText("未扫描");
        } else {
            mVHAWailk.clsudznostate.setText("已扫描");
        }

        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView clsudzno;//  bi编号
        public TextView clsudznostate;// 状态
    }
}
