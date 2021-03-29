package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnverboxcheckineinfoadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnNoverBoxEntity;

import java.util.List;

/**
 * Created by Administrator on 2021/1/28.
 */
//生成的实体类代码 清分管理员获取线路列表和周转箱列表代码
//        适配器代码
public class TurnoverBoxCheckLinadapter extends BaseAdapter {
    private List<TurnNoverBoxEntity> walkList;
    private Context mContext;
    private LayoutInflater inflater;

    public TurnoverBoxCheckLinadapter(List<TurnNoverBoxEntity> walkList, Context mContext) {
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
        TurnoverBoxCheckLinadapter.TurnoverBoxCheckLinadapterViewHolder mVHAWailk = null;
        if (mVHAWailk == null) {
            mVHAWailk = new TurnoverBoxCheckLinadapterViewHolder();
            convertView = inflater.inflate(R.layout.turnnoverchecklinelistview_item, null, false);
            TextView dizhiyapinid = (TextView) convertView.findViewById(R.id.tvlinname);
            TextView dizhiyapinitem = (TextView) convertView.findViewById(R.id.tvlinecounts);
            mVHAWailk.dizhiyapinid = dizhiyapinid;
            mVHAWailk.dizhiyapinitem = dizhiyapinitem;
            convertView.setTag(convertView);
        } else {
            mVHAWailk = (TurnoverBoxCheckLinadapterViewHolder) convertView.getTag();
            convertView.setTag(convertView);
        }
        mVHAWailk.dizhiyapinid.setText(walkList.get(position).getLineName());
        mVHAWailk.dizhiyapinitem.setText(walkList.get(position).getBoxCount() + "");
        return convertView;
    }

    class TurnoverBoxCheckLinadapterViewHolder {
        public TextView dizhiyapinid;
        public TextView dizhiyapinitem;
        public TextView dizhiyapintype;
    }
}
