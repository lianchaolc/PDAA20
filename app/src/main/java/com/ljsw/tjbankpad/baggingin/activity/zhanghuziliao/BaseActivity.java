package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by SPC on 2017/4/10.
 */
public abstract class BaseActivity extends FragmentActivity {

	/**
	 * 加载框的文字说明
	 */

	private boolean isSetStatusBar = true;
	private String mProgressMessage = "请稍后...";

	/**
	 * 全局的加载框对象，已经完成初始化
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 它的功能是启用窗体的扩展特

		setContentView();
	}

	protected abstract void setContentView();

	@Override
	protected void onStart() {
		super.onStart();

	}

	/***
	 * 防止用户快速点击
	 */
	private boolean fastClick() {
		long lastClick = 0;
		if (System.currentTimeMillis() - lastClick <= 1000) {
			return false;
		}
		lastClick = System.currentTimeMillis();
		return true;
	}

	/***
	 * 携带数据跳转页面
	 */
	public void startActivity(Class<?> aClass, Class<?> clz) {
		startActivity(clz, null);
	}

	/**
	 * 是否设置沉浸状态栏
	 *
	 * @param isSetStatusBar
	 */
	public void setSteepStatusBar(boolean isSetStatusBar) {
		this.isSetStatusBar = isSetStatusBar;
	}// 减少类型的转换 findviewbyid

	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}
}
