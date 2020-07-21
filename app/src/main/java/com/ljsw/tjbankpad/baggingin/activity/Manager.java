package com.ljsw.tjbankpad.baggingin.activity;

import android.os.Handler;

public class Manager {
	public static final String TAG = "Manager";

	private static Manager mInstance;

	public Handler mHandler;

	public synchronized static Manager getInstance() {
		if (mInstance == null) {
			mInstance = new Manager();
		}
		return mInstance;
	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	public void sendSuccessMessage() {
		mHandler.sendEmptyMessage(0);
	}

	public void sendFailMessage() {
		mHandler.sendEmptyMessage(1);
	}

}
