package afu.message;

import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by liu_rui on 2019/11/30.
 */

public class UARTImplement implements UARTInterface {

	private final static String TAG = "UARTImplement";

	private SerialPort singleChipPort;
	private InputStream inputStream;
	private OutputStream outputStream;

	private ReadThread readThread;
	private SingleChipPsamInterface singleChipPsamInterface;

	UARTImplement(SingleChipPsamInterface singleChipPsamInterface) {
		this.singleChipPsamInterface = singleChipPsamInterface;
	}

	// ----------------------------------------------------打开线程（只会打开一个，再次调用会启用线程数据接收（暂停线程后调用））
	@Override
	public void threadStartMessage() {
		if (readThread == null) {
			readThread = new ReadThread();
			readThread.start(); // 打开线程
		}
		readThread.startThread(); // 打开数据读取
	}

	// ----------------------------------------------------暂停线程（线程还在运行）
	@Override
	public void threadStopMessage() {
		if (readThread != null)
			readThread.stopThread(); // 关闭数据读取（线程还在运行）
	}

	// ----------------------------------------------------关闭线程（线程不运行）
	@Override
	public void threadOffMessage() {
		if (readThread != null)
			readThread.offThread(); // 关闭线程（读取）
	}

	// ----------------------------------------------------打开串口（同时打开线程）
	@Override
	public void uartStartMessage(String uartFileName, int uartBaud) {
		if (singleChipPort == null) {
			for (int i = 0; i < 2; i++) { // 打开第一次串口后关闭是保证只有一个串口
				try {
					this.singleChipPort = new SerialPort(new File(uartFileName), uartBaud, 0);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (i == 0 && singleChipPort != null) {
					this.singleChipPort.close();
				}
				Log.i(TAG, "DeviceManage---->init---->" + uartFileName);
			}
		}

		if (singleChipPort == null) {
			Log.e(TAG, "uartStartMessage: 串口打开失败");
			return;
		}
		inputStream = singleChipPort.getInputStream();
		outputStream = singleChipPort.getOutputStream();
		threadStartMessage();
	}

	// ----------------------------------------------------向串口发送数据
	@Override
	public void uartWriteMessage(byte[] bytes) {
		if (outputStream == null)
			return;
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			uartStopMessage();
			Log.e(TAG, "writeMessage : " + e);
		}
	}

	// ----------------------------------------------------关闭串口和线程
	@Override
	public void uartStopMessage() {
		try {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (this.singleChipPort != null)
				this.singleChipPort.close();
			if (readThread != null)
				readThread.offThread();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			inputStream = null;
			outputStream = null;
			singleChipPort = null;
			readThread = null;
		}
	}

//    //----------------------------------------------------非空校验
//    public String setError(){
//        if (readThread == null)
//            return "readThread: NULL";
//        if (singleChipPort == null)
//            return "singleChipPort: NULL";
//        if (inputStream == null)
//            return "inputStream: NULL";
//        if (outputStream == null) {
//            return "outputStream: NULL";
//        }
//        return "没问题";
//    }

	// ----------------------------------------------------串口读取线程
	class ReadThread extends Thread {

		private Boolean threadSignal = false;
		private Boolean isOff;

		ReadThread() {
		}

		private synchronized void startThread() {
			threadSignal = true;
		}

		private synchronized void stopThread() {
			threadSignal = false;
		}

		private synchronized void offThread() {
			isOff = false;
		}

		@Override
		public void run() {
			super.run();

			isOff = true;
			while (isOff) {
				synchronized (this) {
					if (isInterrupted() || inputStream == null)
						break;
					if (!threadSignal) {
						continue;
					}
				}
				try {
					byte[] buffer = new byte[128];
					int size = inputStream.read(buffer);
					if (size > 0) {
						byte[] read = new byte[size];

						System.arraycopy(buffer, 0, read, 0, size); // 读取到的数值拷贝到数组中

						singleChipPsamInterface.messageInspect(read); // 接收到的串口数据
					}
				} catch (Exception e) {
					uartStopMessage();
					Log.e(TAG, "run: 关闭 ttyHSL0 串口后还读取串口数据报错");
					return;
				}
				SystemClock.sleep(50);
			}
			Log.i("ThreadManage", "RFIDThread------------------------->end");
		}
	}
}
