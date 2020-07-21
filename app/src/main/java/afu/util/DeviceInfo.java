package afu.util;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu_rui on 2019/11/27.
 */

public class DeviceInfo {

	public static final String SINGLE_CHIP_FILE = "/dev/ttyHSL1";
	public static final String MODULE_DEBUG_FILE = "/dev/ttyHSL0";
	public static final String MODULE_433WIRELESS_FILE = "/dev/ttysWK0";
	public static final String MODULE_FINGER_FILE = "/dev/ttysWK1";
	public static final String MODULE_SCAN_FILE = "/dev/ttysWK2";
	public static final String MODULE_RFID_FILE = "/dev/ttysWK3";

	public static final int SINGLE_CHIP_KEY = 9600; // 单片机
	public static final int MODULE_DEBUG_KEY = 115200;
	public static final int MODULE_433WIRELESS_KEY = 9600; // 无线
	public static final int MODULE_FINGER_KEY = 115200; // 指纹
//	public static final int MODULE_FINGER_KEY = 9600;    //指纹
	public static final int MODULE_SCANDEVICE_KEY = 9600; // 扫描头
	public static final int MODULE_RFID_KEY = 115200;

	public static final String RFID_REPEAT = "RFID_REPEAT";
	public static final String RFID_OPEN = "RFID_OPEN";
	public static final String RFID_CLOSE = "RFID_CLOSE";
	public static final String RFID_STOP = "RFID_STOP";
	public static final String RFID_SINGLE = "RFID_SINGLE";
	public static final String RFID_WRITE = "RFID_WRITE";
	public static final String WIRELESS_OPEN = "WIRELESS_OPEN";
	public static final String WIRELESS_CLOSE = "WIRELESS_CLOSE";
	public static final String WIRELESS_SEND = "WIRELESS_SEND";
	public static final String FINGER_OPEN = "FINGER_OPEN";
	public static final String FINGER_CLOSE = "FINGER_CLOSE";
	public static final String RFID_SET = "RFID_SET";
	public static final String SCAN_OPEN = "SCAN_OPEN";
	public static final String SCAN_CLOSE = "SCAN_CLOSE";

	// DEBUG 就是 Micro
	public static final String DEBUG_OPEN = "DEBUG_OPEN";
	public static final String DEBUG_CLOSE = "DEBUG_CLOSE";
	public static final String DEBUG_SEND = "DEBUG_SEND";
	public static final String PSAM_OPEN = "PSAM_OPEN";
	public static final String PSAM_CLOSE = "PSAM_CLOSE";
	public static final String PSAM_SEND = "PSAM_SEND";

	public static final String GPRS_OPEN = "GPRS_OPEN";
	public static final String GPRS_CLOSE = "GPRS_CLOSE";
	public static final String GPS_OPEN = "GPS_OPEN";

	public enum SinglePSAM {
		SINGLE_HEAD_END, PSAM_HEAD_END
	}

	public static final byte[] headEndSingle = new byte[] { 0xEF - 0x100, 0xFE - 0x100 }; // 头部，尾部数据
	public static final byte[] headEndPsam = new byte[] { 0xBC - 0x100, 0xCB - 0x100 }; // PSAM 头部，尾部数据

	public static final byte[][] allData = { new byte[] { 0x05, 0x01, 0x01, 0x00, 0x07 }, // 绿色LED 打开 0
			new byte[] { 0x05, 0x01, 0x02, 0x00, 0x08 }, // 绿色LED 关闭 1
			new byte[] { 0x05, 0x02, 0x01, 0x00, 0x08 }, // 红色LED 打开 2
			new byte[] { 0x05, 0x02, 0x02, 0x00, 0x09 }, // 红色LED 关闭 3
			new byte[] { 0x05, 0x04, 0x01, 0x00, 0x0A }, // RFID EN (和RFID 一起) 打开 4
			new byte[] { 0x05, 0x04, 0x02, 0x00, 0x0B }, // RFID EN (和RFID 一起) 关闭 5
			new byte[] { 0x05, 0x08, 0x01, 0x00, 0x0E }, // 无线模块休眠（不休眠） 6
			new byte[] { 0x05, 0x08, 0x02, 0x00, 0x0F }, // 无线模块休眠（休眠） 7
			new byte[] { 0x05, 0x09, 0x01, 0x00, 0x0F }, // 无线模块电源（高电平开）打开 8
			new byte[] { 0x05, 0x09, 0x02, 0x00, 0x10 }, // 无线模块电源（高电平开）关闭 9
			new byte[] { 0x05, 0x0B, 0x01, 0x00, 0x11 }, // 扫描头电源（高电平开）打开 10
			new byte[] { 0x05, 0x0B, 0x02, 0x00, 0x12 }, // 扫描头电源（高电平开）关闭 11
			new byte[] { 0x05, 0x0C, 0x01, 0x00, 0x12 }, // 摄像头开关(预留)打开 12
			new byte[] { 0x05, 0x0C, 0x02, 0x00, 0x13 }, // 摄像头开关(预留)关闭 13
			new byte[] { 0x05, 0x0D, 0x01, 0x00, 0x13 }, // GPS使能（高电平开）打开 14
			new byte[] { 0x05, 0x0D, 0x02, 0x00, 0x14 }, // GPS使能（高电平开）关闭 15
			new byte[] { 0x05, 0x0E, 0x01, 0x00, 0x14 }, // 蜂鸣器开关（高电平开）打开 16
			new byte[] { 0x05, 0x0E, 0x02, 0x00, 0x15 }, // 蜂鸣器开关（高电平开）关闭 17
			new byte[] { 0x05, 0x0F, 0x01, 0x00, 0x15 }, // 指纹识别模块电源（高电平开）打开 18
			new byte[] { 0x05, 0x0F, 0x02, 0x00, 0x16 }, // 指纹识别模块电源（高电平开）关闭 19

			new byte[] { 0x05, 0x05, 0x01, 0x00, 0x0B }, // STM8GPIO20_5V(5V总电源)(PB0) 打开 20
			new byte[] { 0x05, 0x05, 0x02, 0x00, 0x0C }, // STM8GPIO20_5V(5V总电源)(PB0) 关闭 21
			new byte[] { 0x05, 0x03, 0x01, 0x00, 0x09 }, // 18外挂串口(PB3) 打开 22
			new byte[] { 0x05, 0x03, 0x02, 0x00, 0x0A }, // 18外挂串口(PB3) 关闭 23
	};

	/**
	 * 判断是否是中日韩文字
	 * 
	 * @param c 要判断的字符
	 * @return true或false
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是数字或者是英文字母
	 * 
	 * @param c
	 * @return
	 */
	private static boolean judge(char c) {
		if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否乱码
	 * 
	 * @param strName 字符串
	 * @return
	 */
	public static boolean isMessyCode(String strName) {
		// 去除字符串中的空格 制表符 换行 回车
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		// 去除字符串中的标点符号
		String temp = after.replaceAll("\\p{P}", "");
		// 处理之后转换成字符数组
		char[] ch = temp.trim().toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			// 判断是否是数字或者英文字符
			if (!judge(c)) {
				// 判断是否是中日韩文
				if (!isChinese(c)) {
					// 如果不是数字或者英文字符也不是中日韩文则表示是乱码返回true
					return true;
				}
			}
		}
		// 表示不是乱码 返回false
		return false;
	}

	// 16进制数字：大小写不影响
	private final static char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String bytesToHexString(byte[] bytes) {
		if (bytes == null)
			return "这是个空值 NULL";
		StringBuilder strbuffer = new StringBuilder();
		for (byte b : bytes) {
			strbuffer.append(DeviceInfo.byteToHexString(b)).append(" ");
		}
		return strbuffer.toString();
	}

	/**
	 * 将byte类型数字转成成16进制字符串
	 * 
	 * @explain
	 * @param b 表述范围
	 * @return
	 */
	private static String byteToHexString(byte b) {
		if (127 < b || b < -128)
			return "";
		// 确保n是正整数
		int n = b < 0 ? 256 + b : b;
		return "" + HEXDIGITS[n / 16] + HEXDIGITS[n % 16];
	}
}
