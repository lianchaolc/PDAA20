package com.service;

import com.application.GApplication;
import com.golbal.pda.GolbalUtil;
import com.golbal.pda.GolbalView;
import com.main.pda.SystemLogin;
import com.messagebox.Abnormal;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class NetService extends Service {

	ConnectivityManager connectivityManager;
	public static NetworkInfo info;
	Bundle bundle;
	int type = -1;
	public static Handler handnet;
	Message m;

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(broadcastreceiver, mFilter);

//		ConnectivityManager conne = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (conne != null) {
//			info = conne.getActiveNetworkInfo();
//		}else
//			info = null;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastreceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static void setSendMsg(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null)
			return;
		info = connectivityManager.getActiveNetworkInfo();

		if (info != null) {
			if (info.isConnectedOrConnecting()) { // 有网络
				// 发送通知
				handnet.sendEmptyMessage(1);
				return;
			}
		}
		handnet.sendEmptyMessage(-1);
	}

	/**
	 * 网络每次发生变化，系统会发现一个ConnectivityManager.CONNECTIVITY_ACTION广播 通过这个广播监测网络状态，及网络类型
	 */

	private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			int netType = 0;
			if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
						Context.CONNECTIVITY_SERVICE);
				if (connectivityManager != null)
					info = connectivityManager.getActiveNetworkInfo();
				else
					info = null;

				if (info != null) {
					if (info.isConnectedOrConnecting()) { // 有网络
						if (SystemLogin.current) { // 如果是登录当前界面
							Log.i("info1", info + "");
							// 发送通知
							sendmsg(1);
						}

						ConnectivityManager connMgr = (ConnectivityManager) context
								.getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
						if (networkInfo == null) {
							sendmsg(-99);// 当前无网路
						}
						int nType = networkInfo.getType();
						if (nType == ConnectivityManager.TYPE_WIFI) {
							netType = 1;// wifi
							GApplication.netState="11";
						} else if (nType == ConnectivityManager.TYPE_MOBILE) {
							int nSubType = networkInfo.getSubtype();
							TelephonyManager mTelephony = (TelephonyManager) context
									.getSystemService(Context.TELEPHONY_SERVICE);
							if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
									&& !mTelephony.isNetworkRoaming()) {
								netType = 2;// 3G
								GApplication.netState="13";
							} else {
								netType = 3;// 2G
								GApplication.netState="13";
							}
						}else if(networkInfo.getType()==ConnectivityManager.TYPE_ETHERNET){//以太网 的网络类型 9
							GApplication.netState="11";
						}
					}
				} else {
					if (SystemLogin.current) { // 如果是登录当前界面
						Log.i("info-1", info + "");
						sendmsg(-1);
						new GolbalView().toastShow(context, "当前网络连接失败");
					}
				}


//				ConnectivityManager connMgr = (ConnectivityManager) context
//						.getSystemService(Context.CONNECTIVITY_SERVICE);
//				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//				int nType = networkInfo.getType();
//				if(netType==ConnectivityManager.TYPE_ETHERNET){//以太网 的网络类型
//					GApplication.netState="11";
//				}



			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void sendmsg(int i) {
		m = handnet.obtainMessage();
		m.what = i;
		if(null==handnet){}else{
			handnet.sendMessage(m);
		}

	}
}
