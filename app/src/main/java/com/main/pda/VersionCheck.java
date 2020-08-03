package com.main.pda;

import hdjc.rfid.operator.RFID_Device;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.os.*;
import com.application.GApplication;
import com.clearadmin.pda.ClearAddMoneyOutDo;
import com.example.pda.BuildConfig;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.hjq.permissions.XXPermissions;
import com.loginsystem.biz.PermissionUtil;
import com.loginsystem.biz.PictureFileHelp;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.Loading;
import com.messagebox.Runing;
import com.online.update.biz.GetPDA;
import com.online.update.biz.Online;
import com.online.update.biz.VersionInfo;
import com.service.FixationValue;
import com.sql.SQL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class VersionCheck extends Activity {
	private static final int REQ_CODE_PERMISSION = 0x1111;//  新增200200727
	TextView versionNum, tvscan;
	Button btn;
	private GetPDA Pda;
	private ManagerClass managerClass;
	Loading on = new Loading();
	Handler handler = null; // 获取版本号
	public static boolean stopupdate = true;
	private Runing runing;
	SharedPreferences sharepre;
	String space; // 空间
	String webservice; // webservice地址
	private boolean install;

	GetPDA getPda() {
		if (Pda == null) {
			Pda = new GetPDA();
		}
		return Pda;
	}

	Online online = new Online();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 禁止休睡眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c_version_check);
		btn = (Button) findViewById(R.id.checkupdate);
		versionNum = (TextView) findViewById(R.id.version);
		tvscan = (TextView) findViewById(R.id.tvscan);
//		tvscan.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent  intent  =new  Intent(VersionCheck.this, WaiBusaomiaoactivity.class);
//				startActivity(intent);
//			}
//		});
		runing = new Runing();
		versionNum.setText("当前版本" + getVersion());
		managerClass = new ManagerClass();
		MainActivity.list_version.add(this);

		// 更新按钮
		btn.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent even) {
				switch (even.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btn.setBackgroundResource(R.drawable.gray_btn_bg_press);
					break;
				case MotionEvent.ACTION_UP:
					btn.setBackgroundResource(R.drawable.gray_btn_bg);

//					runing.runding(VersionCheck.this, "此功能暂未开发");
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							SystemClock.sleep(150anyType0);
//							runing.remove();
//						}
//					}).start();
//					if (false){
					// LR_TODO: 2020/4/3 16:18 liu_rui 版本更新暂未开发所以删掉
					runing.runding(VersionCheck.this, "正在获取新版本");
					getPda().getpath(handler, VersionCheck.this);
//					}
					break;
				case MotionEvent.ACTION_CANCEL:
					btn.setBackgroundResource(R.drawable.gray_btn_bg);
					break;

				}

				return true;
			}
		});

		// 通知安装新版本
		final Handler h_load = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 取消计时器
				new Online().t.cancel();
				if (msg.what == 100) {
					if (!install) {
						install = true;
						Timer t = new Timer();
						t.schedule(new TimerTask() {
							@Override
							public void run() {
								installAPK();
							}
						}, 2500);
					}
				}
			}

		};

		// 获取版本号并更新
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				runing.remove();
				switch (msg.what) {
				case -1:
					managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "获取版本号失败,是否重新获取？",
							new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									managerClass.getSureCancel().remove();
									runing.runding(VersionCheck.this, "正在获取新版本");
									getPda().getpath(handler, VersionCheck.this);
								}
							}, false);
					break;
				case 99:
					managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "发现新版本，是否现在更新？",
							new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									managerClass.getSureCancel().remove();
									on.loading(VersionCheck.this, h_load);
								}
							}, false);
					break;
				case 44:
					GolbalView.toastShow(VersionCheck.this, "目前已是最高版本");
					break;
				}

			}

		};

	}

	// 安装应用程序
	public void installAPK() {
		String paths = Environment.getExternalStorageDirectory() + "/PDA_Version" + "/" + VersionInfo.APKNAME;
//		String paths = Environment.getExternalStorageDirectory()+ "/" +VersionInfo.APKNAME;
		// String paths =
		// Environment.getExternalStorageDirectory()+"/PDA_Version"+"/PDAA20.apk";
		File file = new File(paths);
		Uri uri;
		if (file.exists()) {
			if (XXPermissions.isHasPermission(VersionCheck.this, PermissionUtil.Group.STORAGE)) {
//				if (Build.VERSION.SDK_INT >= 24) {
////				uri = FileProvider.getUriForFile(VersionCheck.this, VersionCheck.this.getPackageName() + ".FileProvider", file);
////					installapktest();
//					File file2= new File(paths);
//					Uri apkUri = FileProvider.getUriForFile(VersionCheck.this, "com.example.pda.FileProvider", file2);//在AndroidManifest中的android:authorities值
//					Intent install = new Intent(Intent.ACTION_VIEW);
//					install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
//					install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//					VersionCheck.this.startActivity(install);
//				}else{
//
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//					try {
//						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//						startActivity(intent);
//						VersionCheck.this.finish();
//						exit();
//					} catch (Exception e) {
//						e.getCause();
//						System.out.println(e.getMessage());
//					}
//				}


//			    File dir = new File(paths);
//				File file1 = new File(dir, "PDA.apk");
				if(!file.exists()){
					try {
						file.createNewFile(); //创建文件
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					Log.i("gdchent","文件存在");
				}
				Intent intent = new Intent(Intent.ACTION_VIEW);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
					//小于7.0
					intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} else {
					//大于7.0
					// 声明需要的临时权限
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					// 第二个参数，即第一步中配置的authorities
					String packageName = VersionCheck.this.getApplication().getPackageName();
					Log.i("gdchent","package:"+packageName);
					Log.i("gdchent","ab_path:"+file.getAbsolutePath());
					Uri contentUri = FileProvider.getUriForFile(VersionCheck.this,  "com.example.pda.FileProvider", file);
//					FileProvider.getUriForFile(VersionCheck.this,"com.example.pda.FileProvider",file1);//在AndroidManifest中的andr
					intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
				}
				VersionCheck.this.startActivity(intent);
			} else {
				PictureFileHelp.requestPermissionSave(VersionCheck.this, REQ_CODE_PERMISSION);
				XXPermissions.gotoPermissionSettings(this);

			}


		}


	}


	/***
	 * 7.0 设备安装
	 */
	private void install1(Activity activity, String name) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		// 打开安装包方式
		intent.setDataAndType(getUri1(activity, name), "application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	/***
	 * 转url
	 */
	private Uri getUri1(Activity activity, String name) {
		Uri uri;
		// 获取下载apk，存储地址
//		File fileSave = new File(getDownloadPath() + File.separator + String.valueOf(BuildConfig.APPLICATION_ID) + ".apk");
		File fileSave = new File(getDownloadPath()+"/PDA_Version" + "/"+File.separator+String.valueOf(BuildConfig.APPLICATION_ID)+".apk");
		if (Build.VERSION.SDK_INT >= 24) {
			// 适配android7.0 ，不能直接访问原路径
			// 需要对intent 授权
			uri = FileProvider.getUriForFile(activity, "com.example.pda.FileProvider", fileSave);
		} else {
			Log.d("----------------------",fileSave+"");
			uri = Uri.fromFile(fileSave);
		}
		return uri;
	}


	/***
	 * /**
	 * 获取下载apk，存储地址
	 *
	 * @return
	 */
	private String getDownloadPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}


	public void installapktest() {
		install1(VersionCheck.this, "PDA.apk");
	}




	public void exit() {
		for (int i = 0; i < MainActivity.list_version.size(); i++) {
			MainActivity.list_version.get(i).finish();
		}
	}

	public String getVersion() {
		String versioncode = "";
		PackageManager packageManager = VersionCheck.this.getPackageManager();
		PackageInfo info;
		try {
			info = packageManager.getPackageInfo(VersionCheck.this.getPackageName(), 0);
			// 当前版本号
			versioncode = info.versionName;
			Log.i("versioncode当前版本号", versioncode + "");
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}

		return versioncode;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		stopupdate = false;
		online.t.cancel();
		on.remove();
	}

	@Override
	protected void onStart() {
		super.onStart();
		stopupdate = true;
	}

}
