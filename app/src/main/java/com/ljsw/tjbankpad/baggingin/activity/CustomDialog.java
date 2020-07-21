package com.ljsw.tjbankpad.baggingin.activity;

import com.example.pda.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/****
 * 抵制押品装袋的dialog
 * 
 * @author Administrator
 *
 */
public class CustomDialog {
	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private Button btn_neg;
	private Button btn_pos;
	private Display display;
	private boolean showTitle = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	public CustomDialog(Context context) {
		try {
			this.context = context;
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			display = windowManager.getDefaultDisplay();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CustomDialog builder() {
		try {
			// 获取Dialog布局
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);

			// 获取自定义Dialog布局中的控件
			txt_title = (TextView) view.findViewById(R.id.txt_title);
			txt_title.setVisibility(View.GONE);
			btn_neg = (Button) view.findViewById(R.id.btn_neg);
			btn_neg.setVisibility(View.GONE);
			btn_pos = (Button) view.findViewById(R.id.btn_pos);
			btn_pos.setVisibility(View.GONE);

			// 定义Dialog布局和参数
			dialog = new Dialog(context, R.style.AlertDialogStyle);
			dialog.setContentView(view);

			// 调整dialog背景大小
			Window window = dialog == null ? null : dialog.getWindow();
			if (dialog != null && window != null) {
				WindowManager.LayoutParams attr = window.getAttributes();
				if (attr != null) {
					attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
					attr.gravity = Gravity.CENTER_VERTICAL;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public CustomDialog setTitle(String title) {
		try {
			showTitle = true;
			if ("".equals(title)) {
				txt_title.setText("标题");
			} else {
				txt_title.setText(title);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	public CustomDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public CustomDialog setPositiveButton(String text, final OnClickListener listener) {
		try {
			showPosBtn = true;
			if ("".equals(text)) {
				btn_pos.setText("确定");
			} else {
				btn_pos.setText(text);
			}
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(v);
					dialog.dismiss();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	public CustomDialog setNegativeButton(String text, final OnClickListener listener) {
		try {
			showNegBtn = true;
			if ("".equals(text)) {
				btn_neg.setText("取消");
			} else {
				btn_neg.setText(text);
			}
			btn_neg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(v);
					dialog.dismiss();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	private void setLayout() {
		try {

			if (showTitle) {
				txt_title.setVisibility(View.VISIBLE);
			}

			if (!showPosBtn && !showNegBtn) {
				btn_pos.setText("确定");
				btn_pos.setVisibility(View.VISIBLE);
				btn_pos.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

			if (showPosBtn && showNegBtn) {
				btn_pos.setVisibility(View.VISIBLE);
				btn_neg.setVisibility(View.VISIBLE);
			}

			if (showPosBtn && !showNegBtn) {
				btn_pos.setVisibility(View.VISIBLE);
			}

			if (!showPosBtn && showNegBtn) {
				btn_neg.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void dismiss() {
		dialog.dismiss();
	}

	public void show() {
		try {
			setLayout();
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
