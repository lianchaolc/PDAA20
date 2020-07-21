package afu.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PrivilegeActivity extends Activity {

	private AlertDialog mDialog;
	private final int CAMERA_REQ_CODE = 110;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 23) {
			initData();
		}
	}

	private void initData() {
		// 判断当前是否已经获得了该权限
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) this,
					new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, CAMERA_REQ_CODE);
		} else {
			// 未获取权限，跳转悬浮窗权限设置界面
//            boolean bool = canDrawOverlays();
			if (!canDrawOverlays()) {
				showDialog(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivityForResult(intent, 1);
					}
				}, "界面上层显示权限");
			}
		}
	}

	// 反射判断是否打开悬浮窗方法
	private boolean canDrawOverlays() {
		try {
			Class<?> classBook = Class.forName("android.provider.Settings");
//            Class classBook = Settings.class;
			Method methodBook = classBook.getDeclaredMethod("canDrawOverlays", Context.class);
			methodBook.setAccessible(true);
			Object objectBook = classBook.newInstance();
			return (Boolean) methodBook.invoke(objectBook, this);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			// 应用设置页面返回后再次判断权限
			initData();
		}
		Log.e("TAGliurui", "onActivityResult: " + requestCode);
	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if (requestCode == CAMERA_REQ_CODE) {
			boolean bool = true;
			for (int i = 0; i < permissions.length; i++) {
				// 已授权
				if (grantResults[i] == 0)
					continue;

				bool = false;
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
					// 选择禁止
					showDialog(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.dismiss();
							}
							ActivityCompat.requestPermissions((Activity) PrivilegeActivity.this,
									new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, CAMERA_REQ_CODE);
						}
					}, "存储权限");
				} else {
					// 选择禁止并勾选禁止后不再询问
					showDialog(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.dismiss();
							}
							Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							Uri uri = Uri.fromParts("package", getPackageName(), null);
							intent.setData(uri);
							// 调起应用设置页面
							startActivityForResult(intent, 2);
						}
					}, "存储权限");
				}
			}
			if (bool)
				initData();
		}
	}

	private void showDialog(DialogInterface.OnClickListener onClickListener, String str) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("授权");
		builder.setMessage("需要允许" + str + "才可使用本程序");
		builder.setPositiveButton("去授权", onClickListener);
		mDialog = builder.create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}