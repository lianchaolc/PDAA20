package com.ljsw.tjbankpad.baggingin.activity.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunMingXiSelectEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuTookinfoAndTrueEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuUserCodeEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuUserEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectTaskEntity;

/**
 * Created by Administrator on 2019/12/19. lianc 押运员获取任务后选择做上缴或者是请领的任务
 * 
 */
public class YayunyaunUserAdapter extends BaseAdapter {
	protected static final String TAG = "YayunyaunUserAdapter";
	private List<YayunSelectRewuUserEntity> walkList;
	private Context mContext;
	private LayoutInflater inflater;

	public YayunyaunUserAdapter(List<YayunSelectRewuUserEntity> yayunmingselectlist, Context mContext) {
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

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderAdapterWailk mVHAWailk = null;
		if (mVHAWailk == null) {
			mVHAWailk = new ViewHolderAdapterWailk();
			convertView = inflater.inflate(R.layout.yayunyuantaskmingxisekectklist_item, null, false);
			TextView yayunorgname = (TextView) convertView.findViewById(R.id.yayunorgname);
			TextView yayuntooktast = (TextView) convertView.findViewById(R.id.yayuntooktast);// 请领显示
			TextView yayunupdatatast = (TextView) convertView.findViewById(R.id.yayunupdatatast);// / 上缴显示
			TextView yayunyuantasktext = (TextView) convertView.findViewById(R.id.yayunyuantasktext);// 显示线路
			Button btnyayunyuantask = (Button) convertView.findViewById(R.id.btnyayunyuantask);
			LinearLayout lintitle = (LinearLayout) convertView.findViewById(R.id.lintitle);
			// TextView yayunyuantaskyuliu=(TextView) convertView
			// .findViewById(R.id.yayunyuantaskyuliu);
			mVHAWailk.yayunorgname = yayunorgname;
			mVHAWailk.yayuntooktast = yayuntooktast;
			mVHAWailk.yayunupdatatast = yayunupdatatast;
			mVHAWailk.yayunyuantasktext = yayunyuantasktext;
			mVHAWailk.btnyayunyuantask = btnyayunyuantask;
			mVHAWailk.lintitle = lintitle;
			// mVHAWailk.yayunyuantaskyuliu=yayunyuantaskyuliu;
			convertView.setTag(convertView);
		} else {
			mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
			convertView.setTag(convertView);
		}

		final YayunSelectRewuUserEntity entity = walkList.get(position);

		if (position == 0) {
			mVHAWailk.yayunyuantasktext.setVisibility(View.VISIBLE);
			mVHAWailk.btnyayunyuantask.setVisibility(View.VISIBLE);
			mVHAWailk.lintitle.setVisibility(View.VISIBLE);
			// mVHAWailk.yayunyuantaskyuliu.setVisibility(View.VISIBLE);
		} else {
			YayunSelectRewuUserEntity lastEntity = walkList.get(position - 1);
			if (entity.getLinename().equals(lastEntity.getLinename())) {
				mVHAWailk.yayunyuantasktext.setVisibility(View.GONE);
				mVHAWailk.btnyayunyuantask.setVisibility(View.GONE);
				mVHAWailk.lintitle.setVisibility(View.GONE);
				// mVHAWailk.yayunyuantaskyuliu.setVisibility(View.GONE);
			} else {
				mVHAWailk.yayunyuantasktext.setVisibility(View.VISIBLE);
				mVHAWailk.btnyayunyuantask.setVisibility(View.VISIBLE);
				mVHAWailk.lintitle.setVisibility(View.VISIBLE);
				// mVHAWailk.yayunyuantaskyuliu.setVisibility(View.VISIBLE);
			}
		}
		mVHAWailk.yayuntooktast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "数据源长度：" + walkList.size(), Toast.LENGTH_SHORT).show();
				Toast.makeText(mContext, "我点的是：" + entity.getCorpname(), Toast.LENGTH_SHORT).show();
				Toast.makeText(mContext, "我点的是getLinename：" + entity.getLinename(), Toast.LENGTH_SHORT).show();
				for (int i = 0; i < walkList.size(); i++) {
					System.out.print(walkList.get(i).getCorpid());
					System.out.print("" + walkList.get(i).getCorpname());
				}

			}
		});

		mVHAWailk.yayunyuantasktext.setText(entity.getLinename());// 显示线路
		mVHAWailk.yayunorgname.setText(entity.getCorpname());// 显示银行下的网点
		if (entity.getFlag().endsWith("0")) {// 上缴
			if ("00".equals(entity.getState())) {
				mVHAWailk.yayuntooktast.setText("待确认");
			} else if ("01".equals(entity.getState())) {
				mVHAWailk.yayunupdatatast.setText("待交接");
			} else if ("00".equals(entity.getState())) {
				mVHAWailk.yayunupdatatast.setText("已交接");
			}
		} else if (entity.getFlag().endsWith("1")) {
			if ("00".equals(entity.getState())) {
				mVHAWailk.yayunupdatatast.setText("待确认");
			} else if ("01".equals(entity.getState())) {
				mVHAWailk.yayunupdatatast.setText("待交接");
			} else if ("00".equals(entity.getState())) {
				mVHAWailk.yayunupdatatast.setText("已交接");
			}

		}
		return convertView;
	}

	class ViewHolderAdapterWailk {
		public TextView yayunorgname;
		public TextView yayuntooktast;
		public TextView yayunupdatatast;
		public TextView yayunyuantasktext;// xians显示线路
		public Button btnyayunyuantask;// 做上缴任务 的确认按钮
		public LinearLayout lintitle;// 标题头
		// public TextView yayunyuantaskyuliu;// 无用站位
	}

}
