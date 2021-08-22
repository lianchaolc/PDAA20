package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;

import java.util.List;

/**
 * qingfen
 * Created by Administrator on 2021/4/30.
 * 清分适配器  lianchao  2021.5.12
 */

public class SelectATMBlackMoneBoxCountAdapter extends BaseAdapter {

    public SelectATMBlackMoneBoxCountAdapter(Context mContext, List<String> selectATmblackmonecoutnlist) {
        this.mContext = mContext;
        this.selectATmblackmonecoutnlist = selectATmblackmonecoutnlist;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private Context mContext;
    private List<String> selectATmblackmonecoutnlist;
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return selectATmblackmonecoutnlist.size();

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
        selectATMBlackMoneBoxCountView View = null;
        if (View == null) {
            View = new selectATMBlackMoneBoxCountView();
            convertView = inflater.inflate(R.layout.listview_selecatmblackclenmoney_item, null, false);
            TextView lvselectatm_id = (TextView) convertView.findViewById(R.id.lvselectatm_id);
            TextView lvselectatm_name = (TextView) convertView.findViewById(R.id.lvselectatm_name);
            TextView lvselectatm_atm = (TextView) convertView.findViewById(R.id.lvselectatm_atm);
            View.lvselectatm_id = lvselectatm_id;
            View.lvselectatm_name = lvselectatm_name;
            View.lvselectatm_atm = lvselectatm_atm;
            convertView.setTag(convertView);
        } else {
            View = (SelectATMBlackMoneBoxCountAdapter.selectATMBlackMoneBoxCountView) convertView.getTag();
            convertView.setTag(convertView);
        }
        View.lvselectatm_id.setText(position + 1 + "");
        View.lvselectatm_name.setText(selectATmblackmonecoutnlist.get(position) + "");
        View.lvselectatm_atm.setText(selectATmblackmonecoutnlist.get(position) + "");
        return convertView;


    }

    class selectATMBlackMoneBoxCountView {
        public TextView lvselectatm_id;
        public TextView lvselectatm_name;
        public TextView lvselectatm_atm;
    }
}
