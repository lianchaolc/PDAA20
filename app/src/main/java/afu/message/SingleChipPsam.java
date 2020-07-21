package afu.message;

import android.os.SystemClock;
import android.util.Log;

import afu.util.DeviceInfo;

import static afu.util.DeviceInfo.SINGLE_CHIP_FILE;
import static afu.util.DeviceInfo.SINGLE_CHIP_KEY;
import static afu.util.DeviceInfo.headEndPsam;
import static afu.util.DeviceInfo.headEndSingle;

/**
 * Created by liu_rui on 2019/11/27.
 */

public class SingleChipPsam implements SingleChipPsamInterface {

	private static final String TAG = "SingleChipMessage";
	private static SingleChipPsam chipMessage; // 单片机操作对象

	private PsamSingleInterface singleClipByte, psamByte;
	private UARTImplement uartImplement;

	public static SingleChipPsam getInitSingleChipMessage() {
		if (chipMessage == null)
			chipMessage = new SingleChipPsam();
		return chipMessage;
	}

	/**
	 * 数据返回回调
	 * 
	 * @param singlePSAM         类型
	 * @param singleClipPsamByte 回调
	 */
	public void setSingleClipPsamByte(DeviceInfo.SinglePSAM singlePSAM, PsamSingleInterface singleClipPsamByte) {
		switch (singlePSAM) {
		case PSAM_HEAD_END:
			psamByte = singleClipPsamByte;
			break;
		case SINGLE_HEAD_END:
			singleClipByte = singleClipPsamByte;
			break;
		}
	}

	private SingleChipPsam() {
		// 开启串口重置 byteInspect
		byteInspect = ByteInspect.HEAD;
		// 打开串口
		uartImplement = new UARTImplement(this);
		uartImplement.uartStartMessage(SINGLE_CHIP_FILE, SINGLE_CHIP_KEY);
	}

	/**
	 * PSAM模块通讯 和 单片机通讯
	 * 
	 * @param bytes 发送的数据
	 */
	@Override
	public void setUartWriteMessage(byte[] bytes) {
		synchronized (this) {
//            Log.d(TAG, "正在发送: setUartWriteMessage\t" + DeviceInfo.bytesToHexString(bytes));
			uartImplement.uartWriteMessage(bytes);
			SystemClock.sleep(150); // 延时 150 ms 以免发送给单片机两个命令却只返回一个
		}
	}

	// ---------------------------------------线程控制开始--------------------------------------------
	@Override
	public void threadStartMessage() {
		uartImplement.threadStartMessage();
	}

	@Override
	public void threadStopMessage() {
		uartImplement.threadStopMessage();
	}

	@Override
	public void threadOffMessage() {
		uartImplement.threadOffMessage();
	}

	@Override
	public void uartStopMessage() {
		uartImplement.uartStopMessage();
	}
	// ---------------------------------------线程控制结尾--------------------------------------------

	// ----------------------------------------------------单片机校验
	enum ByteInspect {
		HEAD, END, INSPECT
	}

	private ByteInspect byteInspect;
	private int flagBit = 0; // 下标
	private byte[] allBytes; // 分两次发送保存的数据（第一次的）

	@Override
	public void messageInspect(byte[] by) {
		byte[] bytes = new byte[by.length + flagBit];
		if (flagBit != 0)
			System.arraycopy(allBytes, 0, bytes, 0, flagBit);
		System.arraycopy(by, 0, bytes, flagBit, by.length);

//        Log.d(TAG, "run: 接收到的数据\t" + DeviceInfo.bytesToHexString(bytes));
		byte[] readMessage = null;
		for (int i = 0; i < bytes.length; i++) {
			switch (byteInspect) {
			case HEAD:
				if (bytes[i] == headEndSingle[0] || bytes[i] == headEndPsam[0]) {
					readMessage = new byte[] { bytes[i] }; // 以免数组的结尾为校验开头而丢掉开头
					flagBit = 1;
					byteInspect = ByteInspect.END;
				}
				break;
			case END:
				if (readMessage == null || readMessage.length == 0) {
					byteInspect = ByteInspect.HEAD;
					i--;
					continue;
				}
				// 第二个校验
				if (flagBit == 1) {
					if (bytes[i] > 0) {
						byte head = readMessage[0];
						readMessage = new byte[bytes[i] + 2];
						readMessage[0] = head;
					} else {
						byteInspect = ByteInspect.HEAD;
						i--;
						break;
					}
				}
				// 尾部校验
				if (flagBit == readMessage.length - 1
						&& (bytes[i] == headEndSingle[1] || (bytes[i] == headEndPsam[1] && parityBit(readMessage)))) {
					byteInspect = ByteInspect.INSPECT;
					readMessage[flagBit] = bytes[i]; // 添加数组内容
					i--;
					break;
				}
				if (flagBit >= readMessage.length - 1) { // 一旦尾部不正确则进入
					i -= flagBit;
					readMessage = null;
					byteInspect = ByteInspect.HEAD;
				} else {
					readMessage[flagBit] = bytes[i]; // 添加数组内容
					flagBit++;
				}
				break;
			case INSPECT:
				if (readMessage == null || readMessage.length == 0) {
					byteInspect = ByteInspect.HEAD;
					i--;
					continue;
				}
				if (readMessage[0] == headEndSingle[0]) {
					singleClipByte.getByte(readMessage);
				} else if (readMessage[0] == headEndPsam[0]) {
					psamByte.getByte(readMessage);
				}

				byteInspect = ByteInspect.HEAD;
				readMessage = null;
				flagBit = 0;
				break;
			}
		}
		byteInspect = ByteInspect.HEAD; // 重置
//        Log.e(TAG, "多余的 : messageInspect: " + readMessage + " flagBit: " + flagBit);
		if (readMessage != null) {
			allBytes = new byte[flagBit];
			System.arraycopy(readMessage, 0, allBytes, 0, flagBit);
		} else {
			flagBit = 0;
			allBytes = new byte[flagBit];
		}
	}

	private boolean parityBit(byte[] bytes) {
		int parity = 0;
		// 排除 第一个（头） 和 最后二个（校验位和尾）
		for (int i = 1; i < bytes.length - 2; i++) {
			parity += bytes[i];
		}
		byte a = (byte) parity;
		Log.e(TAG, "校验: \t" + ~a + "\t" + bytes[bytes.length - 2] + "\t" + (bytes[bytes.length - 2] == ~a));
		return bytes[bytes.length - 2] == ~a;
	}
}
