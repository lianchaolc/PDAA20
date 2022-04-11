package com.ljsw.pdachecklibrary.adapterbycheck;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryScanEntity;

import java.util.List;

/**
 * Created by Administrator on 2021/8/25.
 * 最后扫描更新适配器
 */

public class CheckLibraryUpDataAdapter extends BaseAdapter {

    private List<CheckLibraryScanEntity> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public CheckLibraryUpDataAdapter(List<CheckLibraryScanEntity> walkList, Context mContext) {
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
            mVHAWailk = new CheckLibraryUpDataAdapter.ViewHolderAdapterCheckLibraryAdapter();// 这个布局不要改
            convertView = inflater.inflate(R.layout.listview_checklibraryupdata_item, null, false);
            TextView lv_checklibrarytaskno = (TextView) convertView.findViewById(R.id.lvchecklibraryid);
            TextView lv_checklibrarytaskcounts = (TextView) convertView.findViewById(R.id.lvchecklibrarynumber);
            TextView accountinfotion_listitem_tvtype = (TextView) convertView.findViewById(R.id.lvchecklibrarystate);
            mVHAWailk.lv_checklibrarytaskno = lv_checklibrarytaskno;
            mVHAWailk.lv_checklibrarytaskcounts = lv_checklibrarytaskcounts;
            mVHAWailk.accountinfotion_listitem_tvtype = accountinfotion_listitem_tvtype;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (CheckLibraryUpDataAdapter.ViewHolderAdapterCheckLibraryAdapter) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.lv_checklibrarytaskno.setText(walkList.get(position).getDZPOin());
        mVHAWailk.lv_checklibrarytaskcounts.setText(walkList.get(position).getDZNo());
        if (null == walkList.get(position).getDZState() || walkList.get(position).getDZState().equals("")) {
//            mVHAWailk.accountinfotion_listitem_tvtype.setText("");
        } else {
            mVHAWailk.accountinfotion_listitem_tvtype.setText("已扫描");
        }

        return convertView;
    }

  static   class ViewHolderAdapterCheckLibraryAdapter {
        public TextView lv_checklibrarytaskno;
        public TextView lv_checklibrarytaskcounts;
        public TextView accountinfotion_listitem_tvtype;
    }

}
