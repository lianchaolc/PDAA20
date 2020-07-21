package com.poka.device;

import afu.util.SoundUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import cn.pda.serialport.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ScanDevice extends Thread {

	private static String TAG = ScanDevice.class.getSimpleName();

	private SerialPort mSerialPort;
	private InputStream is;
	private OutputStream os;
	/* serialport parameter */
	public static int port = 0;
	private int baudrate = 9600;
	// private int baudrate = 4800;
	private int flags = 0;

	private Handler handler;

	public final static int SCAN = 1001; // messege recv mode

	private Timer mTimer;

	/**
	 * if throw exception, serialport initialize fail.
	 *
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ScanDevice(Handler handler) throws SecurityException, IOException {
		this.handler = handler;
		mSerialPort = new SerialPort(port, baudrate, flags);
		if (port == 0) {
			mSerialPort.scaner_poweron();
		}
//		mSerialPort.rfid_poweron();
		is = mSerialPort.getInputStream();
		os = mSerialPort.getOutputStream();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/** clear useless data **/
		byte[] temp = new byte[1024];
		is.read(temp);
	}

	@Override
	public void run() {
		try {
			int size = 0;
			byte[] buffer = new byte[1024];
			int available = 0;
			while (!isInterrupted()) {
				available = is.available();
//				int sumLen = 0;
//				byte[] tempBuffer = new byte[1024];
//				while (available > 0) {
//					Log.e(TAG, "available = " + available);
//					size = is.read(buffer);
//					if (size > 0) {
//						String byteStr = Tools.Bytes2HexString(buffer, size);
//						Log.e(TAG, "byte = " + byteStr);
//						if (byteStr.endsWith("0D")) {
//							System.arraycopy(buffer, 0, tempBuffer, sumLen, size);
//							sumLen = sumLen + size;
//							Log.e(TAG, "sumLen = " + sumLen);
//							Log.e(TAG, "tempBuffer = " + tempBuffer.length);
//							if (sumLen > 12) {
//								sendMessege(tempBuffer, sumLen, SCAN);
//							} else {
//								sendMessege(buffer, size, SCAN);
//							}
//							available = is.available();
//						} else {
//							System.arraycopy(buffer, 0, tempBuffer, sumLen, 12);
//							sumLen = sumLen + size;
//							available = is.available();
//							while (available == 0) {
//								available = is.available();
//							}
//						}
//					}
//				}
				if (available > 0) {
					Log.e(TAG, "available = " + available);
					size = is.read(buffer);
					if (size > 0) {
						sendMessege(buffer, size, SCAN);
						stopScan();
					}
					Thread.sleep(50);
				}
			}
		} catch (Exception e) {
			// 返回错误信息
			e.printStackTrace();
		}
		super.run();
	}

	private void sendMessege(byte[] data, int dataLen, int mode) {
		try {
//            String dataStr = new String(data, 0, dataLen);
			String dataStr = new String(data, 0, dataLen, "GBK");
			Bundle bundle = new Bundle();
			bundle.putString("data", dataStr);
			byte[] dataBytes = new byte[dataLen];
			System.arraycopy(data, 0, dataBytes, 0, dataLen);
			bundle.putByteArray("dataBytes", dataBytes);
			Message msg = new Message();
			msg.what = mode;
			msg.setData(bundle);
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scan() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mSerialPort.scaner_trig_stat() == true) {
			Log.e(TAG, "scan reset ");
			mSerialPort.scaner_trigoff();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mSerialPort.scaner_trigon();
		Log.e(TAG, "scan start ");
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mSerialPort.scaner_trigoff();
				Log.e(TAG, "scan terminate ");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(SCAN);
			}
		}, 3000);
	}

	public void stopScan() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mSerialPort.scaner_trig_stat() == true) {
			Log.e(TAG, "scan reset ");
			mSerialPort.scaner_trigoff();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private KeyReceiver keyReceiver;
	private boolean mRegisterFlag = false;

	void openReceiver() {
		if (SoundUtil.mContext == null)
			return;
		// 注册按键广播接收者
		keyReceiver = new KeyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.rfid.FUN_KEY");
		filter.addAction("android.intent.action.FUN_KEY");
		SoundUtil.mContext.registerReceiver(keyReceiver, filter);
		mRegisterFlag = true;
	}

	/**
	 * 按键广播接收者 用于接受按键广播 触发扫描
	 */
	private boolean mIsPressed = false;

	private class KeyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int keyCode = intent.getIntExtra("keyCode", 0);
			// 为兼容早期版本机器
			if (keyCode == 0) {
				keyCode = intent.getIntExtra("keycode", 0);
			}
			boolean keyDown = intent.getBooleanExtra("keydown", false);
			if (keyDown && !mIsPressed) {
				// 根据需要在对应的按键的键值中开启扫描,
				switch (keyCode) {
				case KeyEvent.KEYCODE_F1:

				case KeyEvent.KEYCODE_F2:

				case KeyEvent.KEYCODE_F3:

				case KeyEvent.KEYCODE_F4:

				case KeyEvent.KEYCODE_F5:

				default:
					// 开启扫描
					mIsPressed = true;
					scan();
					break;
				}
			} else {
				mIsPressed = false;
			}
		}
	}

	public void close() {
		// 注销广播接收者
		if (mRegisterFlag) {
			SoundUtil.mContext.unregisterReceiver(keyReceiver);
			mRegisterFlag = false;
		}
		if (mSerialPort != null) {
			if (port == 0) {
				mSerialPort.scaner_poweroff();
			}
//            mSerialPort.rfid_poweroff();
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mSerialPort.close(port);
		}
	}

}
