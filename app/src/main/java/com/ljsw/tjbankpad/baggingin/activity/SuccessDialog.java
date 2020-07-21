package com.ljsw.tjbankpad.baggingin.activity;

import com.example.pda.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @Package com.ljsw.dialog
 * @Description: TODO(提交成功Dialog)
 * @author 郑鸿博
 * @date 2015-11-30 下午2:12:16
 * @version V1.0
 */
public class SuccessDialog {
	public SuccessDialog(Context context) {
		super();
		this.context = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();// 获取屏幕分辨率
	}

	private Context context;
	private Dialog dialog;
	private Display display;
	private LinearLayout success_layout;
	private TextView dialog_success;
	private TextView dialog_failure;

	public SuccessDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);
		success_layout = (LinearLayout) view.findViewById(R.id.success_layout);
		dialog_success = (TextView) view.findViewById(R.id.dialog_success1);
		dialog_failure = (TextView) view.findViewById(R.id.dialog_failure1);
		// 添加Dialog样式
		dialog = new Dialog(context, R.style.DeleteDialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域不关闭Dialog
		// 调整dialog背景大小
//		success_layout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
//				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		return this;
	}

	public SuccessDialog success() {
		dialog_success.setVisibility(View.VISIBLE);
		dialog_failure.setVisibility(View.GONE);
		return this;
	}

	public SuccessDialog failure() {
		dialog_failure.setVisibility(View.VISIBLE);
		dialog_success.setVisibility(View.GONE);
		return this;
	}

	public TextView addTextView() {
		TextView tv = null;
		try {
			success_layout.removeViewAt(2);
		} catch (Exception e) {
			// TODO: handle exception
		}
		success_layout.addView(tv = new TextView(context));
		return tv;
	}

	public LinearLayout getrootView() {
		return success_layout;
	}

	public void dismiss() {
		dialog.dismiss();
	}

	/**
	 * 确定事件
	 * 
	 * @param text
	 * @param listener
	 * @return
	 */
	public SuccessDialog setFailure(final OnClickListener listener) {
		dialog_failure.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	/**
	 * 确定事件
	 * 
	 * @param text
	 * @param listener
	 * @return
	 */
	public SuccessDialog setSuccess(final OnClickListener listener) {
		dialog_success.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public void setTextSize(int size) {
		dialog_failure.setTextSize(size);
	}

	public SuccessDialog setTitle(String title) {
		dialog_failure.setText(title);
		return this;
	}

	public SuccessDialog setSuTitle(String title) {
		dialog_success.setText(title);
		return this;
	}

	public void show() {
		dialog.show();
	}
}
