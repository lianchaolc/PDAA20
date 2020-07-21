package com.poka.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android_serialport_api.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import afu.util.DeviceInfo;
import hdjc.rfid.operator.RFID_Device;
import poka_global_constant.GlobalConstant;

public class WirelessDevice {

	private GPIO PowerIO;

	private SerialPort wirelessCanSerialPort;
	private WirelessReadThread wirelessRead;

	private InputStream inputStream;
	private OutputStream outputStream;

	// ---------------------------------------------回调线程---------------------------------------------
	private Handler wirelessHandler;

	public Handler getHandler() {
		return wirelessHandler;
	}

	public void setHandler(Handler handler) {
		this.wirelessHandler = handler;
	}

	// ---------------------------------------------初始化无线---------------------------------------------
	WirelessDevice() {
		openSerialPort();
		// LR_TODO: 2020/3/19 15:40 liu_rui 433无线的电源和使能开关
		PowerIO = new GPIO(GlobalConstant.IO_WIRELESS_POWER);
	}

	// ---------------------------------------------打开串口---------------------------------------------
	private void openSerialPort() {
		if (wirelessCanSerialPort != null)
			return;
		Log.i("liu_rui", "openSerialPort()--->WirelessDevice()");
		for (int i = 0; i < 2; i++) {
			File wirelessFile = new File(DeviceInfo.MODULE_433WIRELESS_FILE);//
			try {
				wirelessCanSerialPort = new SerialPort(wirelessFile, DeviceInfo.MODULE_433WIRELESS_KEY, 0);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
			if (i == 0) {
				this.wirelessCanSerialPort.close();
				Log.i("WirelessDevice", "WirelessSerialPort.close()");
			}
			Log.i("WirelessDevice", "DeviceManage---->init---->WirelessSerialPort");
		}
	}

	// ---------------------------------------------打开串口并监听数据---------------------------------------------
	void openWireless() {
		openSerialPort();

		PowerIO.setGPIO(GlobalConstant.ENABLE_IO); // 打开电源和使能

		inputStream = wirelessCanSerialPort.getInputStream();
		outputStream = wirelessCanSerialPort.getOutputStream();

		if (wirelessRead == null && inputStream != null) {
			wirelessRead = new WirelessReadThread();
			wirelessRead.start();
		}
	}

	// ---------------------------------------------串口发送数据---------------------------------------------
	void wirelessSend(byte[] bytes) {
		if (outputStream == null)
			return;
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------串口接收数据---------------------------------------------
	class WirelessReadThread extends Thread {

		private boolean readBool;

		WirelessReadThread() {
			readBool = true;
		}

		void stopReadBool() {
			readBool = false;
		}

		@Override
		public void run() {
			// 读取
			while (readBool) {
				synchronized (this) {
					if (isInterrupted() || inputStream == null)
						break;
				}
				try {
					byte[] buffer = new byte[128];
					int size = inputStream.read(buffer);
					if (size > 0) {
						byte[] read = new byte[size];

						System.arraycopy(buffer, 0, read, 0, size); // 读取到的数值拷贝到数组中

						handlerCallback(0, read); // 接收到的串口数据
					}
				} catch (Exception e) {
					closeWireless();
					Log.e("TAG", "ReadThread : NegativeArraySizeException");
					return;
				}
				SystemClock.sleep(50);
			}
		}
	}
//    class WirelessSendThread extends Thread{
//
//        WirelessSendThread() {
//        }
//
//        @Override
//        public void run() {
//            // 发送
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true){
//                        Log.e("DeviceManage", "run: 433正在发送");
//                        wirelessSend(new byte[]{2, 3, 5, 7, 56, 45});
//                        SystemClock.sleep(1000);
//                    }
//                }
//            }).start();
//        }
//    }

	private void handlerCallback(int what, byte[] data) {
		Message msg = Message.obtain();

		Bundle bundle = new Bundle();
		bundle.putByteArray("readData", data);
		msg.setData(bundle);
		msg.what = RFID_Device.wireless_data;
		if (wirelessHandler != null) {
			wirelessHandler.sendMessage(msg);
		}
	}

	void closeWireless() {
		Log.d("liu_rui", "closeWireless: 关闭无线");
		try {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (wirelessCanSerialPort != null)
				wirelessCanSerialPort.close();

			if (wirelessRead != null)
				wirelessRead.stopReadBool();
		} catch (IOException e) {
			Log.e("liu_rui", "closeWireless: 无线关闭出错，可以不管");
		} finally {
			PowerIO.setGPIO(GlobalConstant.DISABLE_IO);
			inputStream = null;
			outputStream = null;
			wirelessCanSerialPort = null;
			wirelessRead = null;
			wirelessHandler = null;
		}
	}
}
