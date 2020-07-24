package com.ljsw.tjbankpda.util;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class HxbSpinner extends MySpinner {
	private static final int fontSize = 30;
	private List<String> list;
	private String chooseText;
	private SpinnerAdapter spinnerAdapter;

	public HxbSpinner(final Activity context, View parent, final TextView tv, List<String> list) {
		super(context, parent, tv);
		this.list = list;
		spinner_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				chooseText = HxbSpinner.this.list.get(arg2);
				tv.setText(chooseText);

				HxbSpinner.this.dismiss();
			}

		});
		// 设置选项
		spinnerAdapter = new SpinnerAdapter(context, list, fontSize);
		spinner_list.setAdapter(spinnerAdapter);
	}

	public String getChooseText() {
		return chooseText;
	}

	public void clear() {
		chooseText = null;
		list.clear();
		spinnerAdapter.notifyDataSetChanged();
	}

}
