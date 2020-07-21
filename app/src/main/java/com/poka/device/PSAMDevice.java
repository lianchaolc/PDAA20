package com.poka.device;

import afu.message.PsamSingleInterface;
import afu.message.SingleChipPsam;
import afu.util.DeviceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import hdjc.rfid.operator.RFID_Device;

import java.nio.ByteBuffer;

import static afu.util.DeviceInfo.headEndPsam;

public class PSAMDevice implements PsamSingleInterface {

	// /* XXX */ 表示这类命令的类型
	// XXX【某某某】 表示名称
	// 某某某【XXX】 表示 响应数据
	/* 通用命令详解 */
	// 控制辅助输出端口,控制LED闪烁 【通过INI引脚可以输出高低电平信号】
	public static final int LED_ON_OFF = 0x14;
	// 读取模块信息 【返回模块名称和版本的ASCII码信息】
	public static final int READ_MODEULE_INFO = 0x15;
	// 激活A型卡 【卡片序列号】
	public static final int A_ACTIVATE = 0x16;
	// B型卡激活，通用命令-RC523 【此命令可以读取B型卡卡号(特定模块支持)】
	public static final int B_ACTIVATE = 0x17; // 待定
	// CPU卡激活 【激活CPU卡ISO14443-4】
	public static final int CPU_ACTIVITE = 0x18;
	// CPU 卡数据透传 【数据透传命令，支持自定义开发CPU卡】
	public static final int CPU_PASSTHROUGH = 0x19;
	// SAM卡复位指令 【复位SAM卡获取复位信息】
	public static final int SAM_RESET = 0x1A;
	// SAM卡透传指令 【数据透传命令，支持SAM卡自由操作】
	public static final int SAM_PASSTHROUGH = 0x1B;

	/* 非接触SM7国密卡命令详解 */
	// 认证 【获取卡片操作权限 】
	public static final int AUTHENTICATION = 0xF0;
	// 读数据块 【读取数据块信息 】
	public static final int READ_BLOCK_DATA = 0xF1;
	// 写数据块 【对数据块进行写入 】
	public static final int WRITE_BLOCK_DATA = 0xF2;
	// 修改数据块权限 【修改数据块的操作权限 】
	public static final int UPDATE_BLOCK_DATA = 0xF3;
	// 修改密钥 【修改卡片的密钥 】
	public static final int UPDATE_KEY = 0xF4;

	/* 接触式PSAM卡命令详解 */
	// 初始化PSAM卡 【将PSAM卡设置成与SM7通讯的初始状态 】
	public static final int PSAM_INIT = 0xF5;
	// 回收PSAM卡 【对PSAM卡进行重置 】
	public static final int PSAM_RECYCLE = 0xF6;
	// 外部认证 【外部认证获取PSAM卡操作权限 】
	public static final int AUTHENTICATION_EXTERNAL = 0xE0;
	// 内部认证 【数据加解密 】
	public static final int AUTHENTICATION_INTERIOR = 0xE1;
	// 创建目录 【在当前目录下创建应用目录和密钥文件，并写入主控密钥】
	public static final int CREATE_DIRECTORY = 0xE2;
	// 选择目录或文件 【选择PSAM卡内的目录或者文件 】
	public static final int CHOOSE_DIRECTORY_OR_FILE = 0xE3;
	// 创建二进制文件 【在当前目录下创建二进制数据文件 】
	public static final int CREATE_BINARY_FILE = 0xE4;
	// 擦除目录 【外部认证获取操作权限后擦除当前目录 】
	public static final int ERASE_DIRECTORY = 0xE5;
	// 创建密钥文件 【创建密钥文件 并写入一个密钥 】
	public static final int CREATE_KEY_FILE = 0xE6;
	// 增加或修改密钥 【在密钥文件中增加或修改密钥 】
	public static final int ADD_UPDATE_KEY = 0xE7;
	// 写二进制文件 【更新二进制文件数据 】
	public static final int WRITE_BINARY_FILE = 0xE8;
	// 读二进制文件 【读取二进制文件数据 】
	public static final int READ_BINARY_FILE = 0xE9;
	// 通用加密计算初始化 【PSAM卡用分散代码计算过程密钥 】
	public static final int ENCRYPTION_CALCULATION_INIT = 0xEA;
	// 通用加密计算 【利用过程密钥计算动态密文 】
	public static final int ENCRYPTION_CALCULATION = 0xEB;
	// 获取随机数 【PSAM卡获取随机数 】
	public static final int GET_RANDOM_NUMBER = 0xEC;
	// SM7 认证初始化 【SM7 国密卡三重认证时返回密文 1 】
	public static final int SM7_AUTHENTICATION_INIT = 0xED;
	// SM7 认证 【SM7 国密卡三重认证时返回初始密码流 】
	public static final int SM7_AUTHENTICATION = 0xEE;
	// SM7 安全计算 【SM7 国密卡认证成功后获取密码流加密 】
	public static final int SM7_SAFETY_CALCULATION = 0xEF;

	/* 非接触Mifare卡命令详解 */
	// 读取单块数据
	public static final int READ_SINGLE_DATA = 0x21;
	// 写入单块数据
	public static final int WRITE_SINGLE_DATA = 0x22;
	// 修改扇区密钥
	public static final int UPDATE_SECTORS_KEY = 0x23;

	private SingleChipPsam singleChipPsamMessage;

	// ---------------------------------------------初始化 Micro
	// USB---------------------------------------------
	public PSAMDevice() {
		openSerialPort();
	}

	/**
	 * ---------------------------------------------串口发送数据---------------------------------------------
	 * 向 PSAM 发送数据
	 * 
	 * @param primitiveOrder 命令（上面的一长串）
	 * @param bytes          byte[]（发送的数据） 如：0x04 0x01 0x15 —— 0xE5 0x04 ：表示总长度为 4
	 *                       0x01 ：地址 0x15 ：primitiveOrder（命令） —— ：bytes（表示没有数据）
	 *                       0xE5 ：校验位
	 */
	void psamSend(int primitiveOrder, byte... bytes) {
		int byteSize = 0;
		if (bytes != null)
			byteSize = bytes.length;

		if (byteSize + 4 <= 32) {
			byte[] writeDataPSAM = new byte[byteSize + 4]; // 字节总长度数字 + 地址 + 命令 + byte[]的长度（数据长度） + 校验位

			// LR_TODO: 2020/2/18 liu_rui PSAM模块包头有问题
			writeDataPSAM[0] = (byte) (byteSize + 4);
			writeDataPSAM[1] = 0x01;
			writeDataPSAM[2] = getPrimitiveOrder(primitiveOrder);
			if (byteSize > 0) {
				System.arraycopy(bytes, 0, writeDataPSAM, 3, byteSize);
			}
			writeDataPSAM[byteSize + 3] = 0xE5 - 0x100;
			// 把数据加上包头包尾 然后发送
			uartWriteMessage(writeDataPSAM);
		}
	}

	/**
	 * 发送数据的方法（本类调用）
	 * 
	 * @param bytes 要发送的数据
	 */
	private void uartWriteMessage(byte[] bytes) {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + 2);
//        byteBuffer.put(headEndPsam[0]);
//        byteBuffer.put(bytes);
//        byteBuffer.put(headEndPsam[1]);
//        byte[] byts = byteBuffer.array();

		byte[] readByte = new byte[bytes.length + 2];
		System.arraycopy(bytes, 0, readByte, 1, bytes.length);
		readByte[0] = headEndPsam[0];
		readByte[bytes.length + 1] = headEndPsam[1];

		singleChipPsamMessage.setUartWriteMessage(readByte);
	}

	// 0xFF 内 转 byte
	private byte getPrimitiveOrder(int primitiveOrder) {
		byte order = (byte) primitiveOrder;

		if (primitiveOrder >= 128) {
			order = (byte) (~primitiveOrder ^ 0xff);
		}
		return order;
	}

	// ---------------------------------------------回调线程---------------------------------------------

	private Handler psamHandler;

	public void setHandler(Handler handler) {
		this.psamHandler = handler;
	}

	// ---------------------------------------------串口接收数据---------------------------------------------
	@Override
	public void getByte(byte[] bytes) {
		// LR_TODO: 2020/1/11 liu_rui PSAM返回过来的数据
		Message msg = Message.obtain();
		Bundle bundle = new Bundle();
		bundle.putByteArray("readData", bytes);
		msg.setData(bundle);
		msg.what = RFID_Device.psam_data;
		if (psamHandler != null) {
			psamHandler.sendMessage(msg);
		}
		Log.e("TAG", "接收PSAM发送的数据（校验成功）: \t" + DeviceInfo.bytesToHexString(bytes));
	}

	// ---------------------------------------------打开串口---------------------------------------------
	void openSerialPort() {
		if (singleChipPsamMessage != null)
			return;
		singleChipPsamMessage = SingleChipPsam.getInitSingleChipMessage();
		singleChipPsamMessage.setSingleClipPsamByte(DeviceInfo.SinglePSAM.PSAM_HEAD_END, this);
		// LR_TODO: 2020/3/19 15:40 liu_rui PSAM的电源和使能开关
//        PowerIO = GPIO.getGPIO(GlobalConstant.IO_RFID_POWER);
//        enableIO = GPIO.getGPIO(GlobalConstant.IO_RFID_ENABLE);
	}

	void closePSAM() {
		Log.e("TAG", "closePSAM: PSAM模块不用关闭");
	}
}
