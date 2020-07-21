package afu.message;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import afu.util.DeviceInfo;

import static afu.util.DeviceInfo.headEndSingle;

/**
 * Created by liu_rui on 2019/11/27.
 */

public class SingleChipModule implements PsamSingleInterface {

	protected static final String TAG = "SingleChipModule";

	private static SingleChipModule singleChipModule;
	private SingleChipPsam singleChipPsamMessage; // 单片机操作对象
//    private static final List<byte[]> writeDataList = new ArrayList<>();

	public static SingleChipModule getInitSingleChipModule() {
		if (singleChipModule == null)
			singleChipModule = new SingleChipModule();
		return singleChipModule;
	}

	private SingleChipModule() {
		singleChipPsamMessage = SingleChipPsam.getInitSingleChipMessage();
		singleChipPsamMessage.setSingleClipPsamByte(DeviceInfo.SinglePSAM.SINGLE_HEAD_END, this);
	}

	@Override
	public void getByte(byte[] bytes) {
//        validData(bytes, true);
		// LR_TODO: 2020/1/11 liu_rui 单片机返回过来的数据
		Log.d(TAG, "接收单片机发送的数据（校验成功）: \t" + DeviceInfo.bytesToHexString(bytes));
	}

	/**
	 * PSAM模块通讯 和 单片机通讯
	 * 
	 * @param bytes 发送的数据
	 */
	public synchronized void setUartWriteMessage(byte[] bytes) {
		byte[] readByte = new byte[bytes.length + 2];
		System.arraycopy(bytes, 0, readByte, 1, bytes.length);
		readByte[0] = headEndSingle[0];
		readByte[bytes.length + 1] = headEndSingle[1];

		uartWriteMessage(readByte);
	}

	/**
	 * 发送数据的方法（本类调用）
	 * 
	 * @param bytes 要发送的数据
	 */
	private void uartWriteMessage(byte[] bytes) {
//        synchronized (writeDataList) {
//            // 判断writeDataList里是否已经有了
//            if (!validData(bytes, false)) {
//                // 打开 5S 超时发送
//                writeDataList.add(bytes);
//                new WriteDataThread(bytes).start();
//            }
		singleChipPsamMessage.setUartWriteMessage(bytes);
//        }
	}

	// ---------------------------------------线程控制开始--------------------------------------------
	public void threadStartMessage() {
		singleChipPsamMessage.threadStartMessage();
	}

	public void threadStopMessage() {
		singleChipPsamMessage.threadStopMessage();
	}

	public void threadOffMessage() {
		singleChipPsamMessage.threadOffMessage();
	}

	public void uartStopMessage() {
		singleChipPsamMessage.uartStopMessage();
	}
	// ---------------------------------------线程控制结尾--------------------------------------------

	// ---------------------------------------------------- 5S 超时发送
//    private class WriteDataThread extends Thread{
//
//        private byte[] aByte;
//
//        WriteDataThread(byte[] aByte){
//            this.aByte = aByte;
//        }
//
//        @Override
//        public void run() {
//            int i = 0;
//            while (true){
////                Log.e(TAG, "run: WriteDataThread : " + i);
//                if (i == 5){
//                    i = 0;
//                    if (validData(aByte, false)){
//                        uartWriteMessage(aByte);
//                    }else {
//                        return;
//                    }
//                }
//                SystemClock.sleep(1000);
//                i++;
//            }
//        }
//
//    }

//    /**
//     * 判断是否已经有该数组
//     * @param bytes 数组
//     * @param isRead 是否为读取到的
//     * @return Boolean true - 有，false - 没有
//     */
//    private boolean validData(byte[] bytes, boolean isRead){
//        for (int i = 0; i < writeDataList.size(); i++) {
//            if (Arrays.equals(bytes, writeDataList.get(i))) {
//                if (isRead)
//                    writeDataList.remove(i);
//                return true;
//            }
//        }
//        return false;
//    }
}
