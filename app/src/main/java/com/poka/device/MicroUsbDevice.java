package com.poka.device;

import afu.util.DeviceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android_serialport_api.SerialPort;
import hdjc.rfid.operator.RFID_Device;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MicroUsbDevice {

	private SerialPort microUsbSerialPort;
	private MicroUsbReadThread microUsbRead;

	private InputStream inputStream;
	private OutputStream outputStream;

	// ---------------------------------------------回调线程---------------------------------------------
	private Handler microHandler;

	public Handler getHandler() {
		return microHandler;
	}

	public void setHandler(Handler handler) {
		this.microHandler = handler;
	}

	// ---------------------------------------------初始化 Micro
	// USB---------------------------------------------
	MicroUsbDevice() {
		openSerialPort();
	}

	// ---------------------------------------------打开串口---------------------------------------------
	private void openSerialPort() {
		if (microUsbSerialPort != null)
			return;
		Log.i("MicroUsbDevice", "init()--->MicroUsbDevice()");
		for (int i = 0; i < 2; i++) {
			File microUsbFile = new File(DeviceInfo.MODULE_DEBUG_FILE);//
			try {
				microUsbSerialPort = new SerialPort(microUsbFile, DeviceInfo.MODULE_DEBUG_KEY, 0);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
			if (i == 0 && microUsbSerialPort != null) {
				this.microUsbSerialPort.close();
				Log.i("MicroUsbDevice", "MicroUsbSerialPort.close()");
			}
			Log.i("MicroUsbDevice", "DeviceManage---->init---->MicroUsbSerialPort");
		}

		// LR_TODO: 2020/3/19 15:40 liu_rui Micro USB的电源和使能开关
//        PowerIO = GPIO.getGPIO(GlobalConstant.IO_RFID_POWER);
//        enableIO = GPIO.getGPIO(GlobalConstant.IO_RFID_ENABLE);
	}

	// ---------------------------------------------打开串口并监听数据---------------------------------------------
	void openMicroUsb() {
		Log.d("liu_rui", "openMicroUsb: MicroUsb" + microUsbSerialPort);
		if (microUsbSerialPort == null)
			return;
		openSerialPort();

		inputStream = microUsbSerialPort.getInputStream();
		outputStream = microUsbSerialPort.getOutputStream();

		if (microUsbRead == null && inputStream != null) {
			microUsbRead = new MicroUsbReadThread();
			microUsbRead.start();
		}
	}

	// ---------------------------------------------串口发送数据---------------------------------------------
	void microUsbSend(byte[] bytes) {
		if (outputStream == null)
			return;
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			closeMicroUsb();
			Log.e("liu_rui", "run: MicroUSB 串口关闭后发送问题");
		}
	}

	// ---------------------------------------------串口接收数据---------------------------------------------
	class MicroUsbReadThread extends Thread {

		private boolean readBool;

		MicroUsbReadThread() {
			readBool = true;
		}

		void stopReadBool() {
			readBool = false;
		}

		@Override
		public void run() {
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
					closeMicroUsb();
					Log.e("liu_rui", "run: MicroUSB 串口关闭后读取问题");
					Log.e("TAG", "ReadThread : NegativeArraySizeException");
					return;
				}
				SystemClock.sleep(50);
			}
			Log.i("ThreadManage", "RFIDThread------------------------->end");
		}
	}

	private void handlerCallback(int what, byte[] data) {
		Message msg = Message.obtain();
		Bundle bundle = new Bundle();
		bundle.putByteArray("readData", data);
		msg.setData(bundle);
		msg.what = RFID_Device.micro_usb_data;
		if (microHandler != null) {
			microHandler.sendMessage(msg);
		}
	}

	void closeMicroUsb() {
		Log.d("liu_rui", "closeMicroUsb: MicroUsb");
		try {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (microUsbSerialPort != null)
				microUsbSerialPort.close();

			if (microUsbRead != null) {
				microUsbRead.stopReadBool();
				microUsbRead = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			inputStream = null;
			outputStream = null;
			microUsbSerialPort = null;
			microUsbRead = null;
		}
	}
}
