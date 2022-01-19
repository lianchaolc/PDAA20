package com.messagebox;

import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.ljsw.tjbankpda.yy.application.S_application;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SureCancelButton {

	GolbalView g;

	GolbalView getG() {
		if (g == null) {
			g = new GolbalView();
		}
		return g;
	}

	View v;
	View v3;
	View v4;

	/**
	 * 
	 * @param a
	 * @param msg
	 * @param click 确定事件
	 * @param can   true为只显示确定 ，false为显示确定和取消
	 */
	public void makeSuerCancel(Activity a, String msg, OnClickListener click, boolean can) {

		if (v == null) {
			v = GolbalView.getLF(a).inflate(R.layout.suer_cancel_button, null);
		}
		TextView text = (TextView) v.findViewById(R.id.suercanceltext);
		text.setText(msg+"");
		Button suer = (Button) v.findViewById(R.id.suerbtn);
		Button cancel = (Button) v.findViewById(R.id.cancelbtn);

		if (can) {
			cancel.setVisibility(View.GONE);
			suer.setVisibility(View.VISIBLE);
			Log.i("取消", "取消");
		} else {
			suer.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					remove();
				}
			});
		}
		getG().createFloatView(a, v);
		suer.setOnClickListener(click);

	}

	/**
	 * author zhangXuewei
	 * 
	 * @param a
	 * @param msg
	 * @param click 确定事件
	 * @param can   true为只显示确定 ，false为显示确定和取消
	 */
	public void makeSuerCancel2(Activity a, String msg, OnClickListener click, boolean can) {

		if (v == null) {
			v = GolbalView.getLF(a).inflate(R.layout.suer_cancel_button, null);
		}
		TextView text = (TextView) v.findViewById(R.id.suercanceltext);
		text.setText(msg);
		Button suer = (Button) v.findViewById(R.id.suerbtn);
		Button cancel = (Button) v.findViewById(R.id.cancelbtn);
		cancel.setText("重新录入");
		if (can) {
			cancel.setVisibility(View.GONE);
			suer.setVisibility(View.VISIBLE);
			Log.i("取消", "取消");
		} else {
			suer.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					remove();
				}
			});
		}
		getG().createFloatView(a, v);
		suer.setOnClickListener(click);

	}

	public void remove() {
		getG().removeV(v);
	}

	public void makeSuerCancel3(Activity a, String msg,String msg2,String msg3, OnClickListener click, boolean can) {

		if (v3 == null) {
			v3 = GolbalView.getLF(a).inflate(R.layout.big_suer_cancel_buttonver3, null);
		}
		TextView text = (TextView) v3.findViewById(R.id.big_suercanceltext_v3);
		text.setText("指纹识别姓名:"+msg);
		TextView  textViewuser=v3.findViewById(R.id.tv_user_v3);
		textViewuser.setText("押运员编号:"+msg3);


		Button suer = (Button) v3.findViewById(R.id.big_suerbtn_v3);
		Button cancel = (Button) v3.findViewById(R.id.big_cancelbtn_v3);
		cancel.setText("重新录入");
		if (can) {
			cancel.setVisibility(View.GONE);
			suer.setVisibility(View.VISIBLE);
			Log.i("取消", "取消");
		} else {
			suer.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					remove3();
				}
			});
		}
		getG().createFloatView(a, v3);
		suer.setOnClickListener(click);

	}
	public void remove3() {
		getG().removeV(v3);
	}
	/***
	 * 再次确认后的按钮
	 * @param a
	 * @param msg
	 * @param click
	 * @param can
	 */

	public void makeSuerCancel4(Activity a, String msg,String msg1,String msg2, OnClickListener click, boolean can) {

		if (v4 == null) {
			v4 = GolbalView.getLF(a).inflate(R.layout.suer_cancel_buttonvfoure, null);
		}
		TextView text = (TextView) v4.findViewById(R.id.suercanceltextvfoure);
		text.setText(msg);
		TextView textView =v4.findViewById(R.id.suercanceltextuservfoure);
		textView.setText("绑定账号:"+msg1);
		TextView textView0 =v4.findViewById(R.id.suercanceltextvfoure);
		textView0.setText("绑定人员:"+msg);

		TextView textView1=v4.findViewById(R.id.suercanceltextidvfoure);
		textView1.setText(msg2);
		Button suer = (Button) v4.findViewById(R.id.suerbtnvfoure);
		Button cancel = (Button) v4.findViewById(R.id.cancelbtnvfoure);
		cancel.setText("取消");
		if (can) {
			cancel.setVisibility(View.GONE);
			suer.setVisibility(View.VISIBLE);
			Log.i("取消", "取消");
		} else {
			suer.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					remove4();
				}
			});
		}
		getG().createFloatView(a, v4);
		suer.setOnClickListener(click);

	}
	public void remove4() {
		getG().removeV(v4);
	}
}
