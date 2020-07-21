package com.poka.device;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import afu.util.DeviceInfo;
import android.content.Context;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import android_serialport_api.SerialPort;

import cn.poka.util.ShareUtil;

public class DeviceManage {
	public static DeviceManage dM;
	public FingerDevcie fingersDevcie;
	private RFIDDevice fRIDDevice;
	private ScanDevice scanDevice;

	private static Handler mHandler;
	public static int interval;
	public static Context context;
	private String codeString = null;

	public String getCode() {
		return codeString;
	}

	public void setCode(String code) {
		this.codeString = code;
	}

	// 设备线程
	public static ScanDeviceManageThread scanThread;
	public static FingerDevcieManageThread fingerThread;

	/**
	 * 单例模式
	 */
	private DeviceManage() {

	}

	public static DeviceManage getDeviceMObject(String str, Handler handler) {
		Log.i("DeviceManage", "getDeviceMObject---->Str == " + str);
		if (dM == null) {
			Log.i("DeviceManage", "getDeviceMObject---->dM==null");
			dM = new DeviceManage();
			if (str.equals(""))
				return dM;
		}
		if (handler != null)
			mHandler = handler;

		if (str.equals("scan")) {
			try {
				scanThread = dM.new ScanDeviceManageThread();
				scanThread.start();
				Log.i("ThreadManage", "ScanDeviceManageThread------------------------->start");
			} catch (IOException e) {
				// 出现异常
				Log.i("ThreadManage", "初始化串口失败");
			}
		}
		if (fingerThread == null && str.equals("finger")) {
			fingerThread = dM.new FingerDevcieManageThread();
			fingerThread.start();
			Log.i("ThreadManage", "FingerDevcieManageThread------------------------->start");
		}
		try {
//			Thread.sleep(1500);
			SystemClock.sleep(500);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dM;
	}

	private void destroyScan() {

		Log.d("liu_rui",
				"destroy: scanDevice" + scanDevice + "\tscanHandler: " + scanHandler + "\tscanThread: " + scanThread);
		if (scanDevice != null) {
			scanDevice.stopScan();
			scanDevice.interrupt();
			scanDevice.close();
			scanDevice = null;
		}
		if (scanHandler != null) {
			scanHandler.removeCallbacksAndMessages(null);
			scanHandler.getLooper().quit();
			scanHandler = null;
		}
		scanThread = null;

		if (BeepDevice.BD != null) {
			BeepDevice.BD.closeBeeperThread();
		}
	}

	/**
	 * 设置主线程Handler
	 * 
	 * @param handler
	 */

	public void setMainHandler(Handler handler) {
		mHandler = handler;
		if (fRIDDevice != null) {
			fRIDDevice.setHandler(handler);
		}
	}

	public void setLoginAcitvityHandler(Handler handler) {
		Log.e("liu_rui", "指纹的回调: " + fingersDevcie);
		if (fingersDevcie != null) {
			fingersDevcie.setHandler(handler);
		}
	}

	public void setContext(Context con) {
		context = con;
	}

	private Handler scanHandler;

	public Handler getScanHandler() {
		Log.i("DeviceMsg", "getScanHandler()    =   " + scanHandler);
		return scanHandler;
	}

	public void setScanHandler(Handler scanHandler) {
		this.scanHandler = scanHandler;
	}

	class ScanDeviceManageThread extends Thread {

		public ScanDeviceManageThread() throws IOException {
			scanDevice = new ScanDevice(mHandler);
		}

		@Override
		public void run() {
			super.run();

			//
			Looper.prepare();
			Log.i("DeviceMsg", "NEW  ScanHANDLER ");
			scanHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {

					String otherScanState = null;
					otherScanState = (String) msg.obj;
					Log.i("DeviceMsg", "otherScanState " + otherScanState + " msg.what= " + msg.what);
					if (DeviceInfo.SCAN_OPEN.equals(otherScanState)) {
						if (scanDevice != null) {
							scanDevice.start();
							scanDevice.openReceiver();
							Log.i("DeviceManage", "DeviceInfo.SCAN_OPEN--->isOff  ");
						}
					}
					if (DeviceInfo.SCAN_CLOSE.equals(otherScanState)) {
						if (scanDevice != null) {
							scanDevice.stopScan();
							scanDevice.interrupt();
							scanDevice.close();
							scanDevice = null;
						}
						Message msgMessage = Message.obtain();
						msgMessage.what = msg.what;
						msgMessage.obj = DeviceInfo.SCAN_CLOSE;
						if (mHandler != null) {
							mHandler.sendMessage(msgMessage);
							Log.i("DeviceManage", "DeviceInfo.SCAN_CLOSE");
						}

					}
				}
			};
			Looper.loop();
			scanDevice = null;
			Log.i("ThreadManage", "ScanDeviceManageThread------------------------->end");
		}
	}

	private Handler fingersHandler;

	public Handler getFingerHandler() {
		return fingersHandler;
	}

	public void setFingerHandler(Handler fingerHandler) {
		this.fingersHandler = fingerHandler;
	}

	class FingerDevcieManageThread extends Thread {
		FingerDevcieManageThread() {
			fingersDevcie = new FingerDevcie();
		}

		@Override
		public void run() {
			super.run();
			Looper.prepare();
			fingersHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String otherFingerState = null;
					otherFingerState = (String) msg.obj;
					Log.i("DeviceMsg", "otherFingerState " + otherFingerState + " msg.what= " + msg.what);
					if (DeviceInfo.FINGER_OPEN.equals(otherFingerState)) {
						if (fingersDevcie != null) {
							fingersDevcie.open();
						}
					}
					if (DeviceInfo.FINGER_CLOSE.equals(otherFingerState)) {
						if (fingersDevcie != null) {
							fingersDevcie.close();
						}
					}
				}
			};
			Looper.loop();
			fingersDevcie = null;
			Log.i("ThreadManage", "FingerDevcieManageThread------------------------->end");
		}
	}

}
