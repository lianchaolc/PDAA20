package com.ljsw.tjbankpad.baggingin.activity;

import android.content.Context;

/**
 * @Package com.ljsw.util
 * @Description: TODO(提示框Dialog管理类)
 * @author 郑鸿博
 * @date 2015-11-30 上午10:43:43
 * @version V1.0
 */
public class DialogManager {

	public DialogManager(Context context) {
		super();
		this.context = context;
	}

	private Context context;

	private SuccessDialog successDialog;

	public SuccessDialog getSuccessDialog() {
		if (successDialog == null) {
			successDialog = new SuccessDialog(context).builder();
		}
		return successDialog;
	}

}
