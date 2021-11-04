package com.ljsw.pdachecklibrary.adapterbycheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;
import com.ljsw.tjbankpad.baggingin.activity.adapter.DiZhiYaPinAdapter;

import java.util.List;

/**
 * Created by Administrator on 2021/8/25.
 */

public class CheckLibraryAdapter extends BaseAdapter{

    private List<CheckLibraryEntity> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryAdapter(List<CheckLibraryEntity> walkList, Context mContext) {
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
            mVHAWailk = new CheckLibraryAdapter.ViewHolderAdapterCheckLibraryAdapter();
            convertView = inflater.inflate(R.layout.checkibraryshowlistview_item, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.lv_checklibrarytaskno);
            TextView lv_checklibrarytaskcounts = (TextView) convertView.findViewById(R.id.lv_checklibrarytaskcounts);
            TextView lv_checklibrarycreatetime = (TextView) convertView.findViewById(R.id.lv_checklibrarycreatetime);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            mVHAWailk.lv_checklibrarytaskcounts = lv_checklibrarytaskcounts;
            mVHAWailk.lv_checklibrarycreatetime = lv_checklibrarycreatetime;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (CheckLibraryAdapter.ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.lv_checklibrarytaskno.setText(walkList.get(position).getId());
        mVHAWailk.lv_checklibrarytaskcounts.setText(walkList.get(position).getMissingNum());
        mVHAWailk.lv_checklibrarycreatetime.setText(walkList.get(position).getStarttime()+"");
        return convertView;
    }

    class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
        public TextView lv_checklibrarytaskcounts;
        public TextView lv_checklibrarycreatetime;
    }

}
