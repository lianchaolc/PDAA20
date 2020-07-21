package com.strings.tocase;

import java.io.ByteArrayOutputStream;

import android.util.Log;

public class CaseString {

	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";
	public static boolean isOk = false;

	/**
	 * 转化字符串为十六进制编码
	 * 
	 * @param s
	 * @return
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str.toUpperCase();
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 转化为钞箱编号
	 * 
	 * @param number
	 * @return
	 */
	public static String getBoxNum(String number) {

		if (number.length() < 8) {
			return "";
		}

		if (number.length() >= 14) {
			String jiewei = number.substring(10, 14);
			System.out.println("打印：" + jiewei);
			if ("9090".equals(jiewei.trim()) || "7588".equals(jiewei.trim())) {
				return "";
			}
		}

		String num = number.substring(0, 8);

		Log.i("转化传进来的", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 8));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return sb.toString();
	}

	/**
	 * 周转箱扫描
	 * 
	 * @param number
	 * @return 907220190326101429086000 ZH201903
	 */
	public static String getBoxNum2(String number) {
		if (number.length() < 14) {
			return "";
		}

		String num = number.substring(0, 14);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 10));
			sb.append((char) Integer.parseInt(num.substring(10, 12)));
			sb.append((char) Integer.parseInt(num.substring(12, 14)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();

	}

	/***
	 * 账户资料的过滤的规则 lc 2019_3_27 总的有效长度为21位 907220190326101429086000
	 * ZH20190327100720030
	 */
	public static String getAccountDataScan(String number) {
		if (number.length() < 14) {
			return "错误标签";
		}
		if (number.length() < 20) {
			return "标签长度不够";
		}

		String num = number.substring(0, 21);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 21));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();

	}

	/**
	 * 款箱扫描
	 * 
	 * @param number
	 * @return
	 */
	public static String getBoxNum3(String number) {
		if (number.length() < 14) {
			return "";
		}

		if (number.length() >= 14) {
			String jiewei = number.substring(10, 14);
			System.out.println("打印：" + jiewei);
			if (!"7588".equals(jiewei.trim())) {
				return "";
			}
		}

		String num = number.substring(0, 14);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 10));
			sb.append((char) Integer.parseInt(num.substring(10, 12)));
			sb.append((char) Integer.parseInt(num.substring(12, 14)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();
		// return num.toString();

	}

	/**
	 * 返回ATM编号
	 * 
	 * @param number
	 * @return
	 */
	public static String getATMNum(String number) {
		if (number.length() < 10) {
			return "";
		}
		String num = number.substring(0, 10);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 10));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来getATMNum:" + sb.toString());
		return sb.toString();

	}

	/**
	 * 得到10进制的编号
	 * 
	 * @param str
	 * @return
	 */
	public static String getNum(String str) {
		String s = str.substring(0, 2);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			sb.append((int) s.charAt(i));
		}
		return sb.append(str.substring(2, str.length())).toString();
	}

	/**
	 * 编号正则表达式
	 * 
	 * @param str 编号
	 * @return
	 */
	public static boolean reg(String str) {
		if (str.length() < 8) {
			return false;
		}
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) (Integer.parseInt(str.substring(0, 2))));
			sb.append((char) (Integer.parseInt(str.substring(2, 4))));
		} catch (Exception e) {
			return false;
		}

		sb.append(str.substring(4, str.length()));
		String r = "^[A-Z]{2}[0-9]{20}$";
		System.out.println(sb + "----" + sb.length());

		return sb.toString().matches(r);

	}

	/**
	 * 抵质押品扫描 lchao 修改下抵制押品扫描规则 去掉9072【ZH】 直接拼接16位字符串
	 * 
	 * @param number
	 * @return DZ2019
	 */
	public static String getBoxNumbycollateral(String number) {
//		if (number.length() < 14) {
//			return "";
//		}
		if (number == null || number.length() < 19) {
			Log.i("转化传进来的2", "标签不合法");
			return "";
		}
		String num = number.substring(0, 21);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 21));
			// sb.append((char) Integer.parseInt(num.substring(10, 12)));
			// sb.append((char) Integer.parseInt(num.substring(12, 14)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();
		// ZH20190404162251797
	}

	/**
	 * 
	 * 盘查库的过滤规则 将rfid转化为钞袋号
	 * 
	 * @param number
	 * @return
	 */
	public static String turnToBanknoteBagNum(String number) {
		StringBuffer sb = new StringBuffer();

		if ("0".equals(number.substring(4, 5)) && "0".equals(number.substring(5, 6))) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number.substring(4, 21));
		} else if ("0".equals(number.substring(4, 5)) && "1".equals(number.substring(5, 6))) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 7);
			sb.append((char) Integer.parseInt(number.substring(7, 9)));
			sb.append(number.substring(9, 22));
		} else if ("1".equals(number.substring(4, 5)) && "0".equals(number.substring(5, 6))) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 6);
			sb.append((char) Integer.parseInt(number.substring(6, 8)));
			sb.append(number.substring(8, 22));
		} else if ("1".equals(number.substring(4, 5)) && "1".equals(number.substring(5, 6))) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 6);
			sb.append((char) Integer.parseInt(number.substring(6, 8)));
			sb.append((char) Integer.parseInt(number.substring(8, 10)));
			sb.append(number.substring(10, 23));
		}
		System.out.print("sb转换====" + sb.toString());
		return sb.toString();
	}

	/***
	 * 实现 现金装袋转化24位号
	 * 
	 * packgetsList.add("676810A14190227093020000");
	 * packgetsList.add("676800991419022709302000");
	 * 
	 * 修改201910.24 修改 6768000554190227093020 无字母 // 676810155641902270930200 有字母
	 * CD000554190227093020 6768000554190227093020 4在显示钞袋号： 000 尾零和无字母 001 尾零和带字母
	 * CD115A419022709302000000 676810155641902270930200 101 不尾零 包含尾零和不尾零的状态
	 */
	/**
	 * 获取钞袋号 中的是否尾零 将rfid转化为钞袋号 * 例： 676801556419022709302000
	 * *转化后：CD015A4190227093020
	 * 
	 * @return
	 */
	public static String turnToPackgerHomeManagertoClean(String number) {

		if (number.startsWith("CD"))
			return number;
		StringBuffer sb = new StringBuffer();

		String sign = number.substring(5, 6);
		if ("0".equals(sign)) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number.substring(4, 21));
		} else if ("1".equals(sign)) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 7);
			sb.append((char) Integer.parseInt(number.substring(7, 9)));
			sb.append(number.substring(9, 22));
		} else if ("2".equals(sign)) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 6);
			sb.append((char) Integer.parseInt(number.substring(6, 8)));
			sb.append(number.substring(8, 22));
		} else if ("3".equals(sign)) {
			sb.append((char) Integer.parseInt(number.substring(0, 2)));
			sb.append((char) Integer.parseInt(number.substring(2, 4)));
			sb.append(number, 4, 6);
			sb.append((char) Integer.parseInt(number.substring(6, 8)));
			sb.append((char) Integer.parseInt(number.substring(8, 10)));
			sb.append(number.substring(10, 23));
		}

		return sb.toString();
	}

	/***
	 * lianchao 20200318 修改扫描规则 现阶段规则如： 只存在ZH000001ZZ 和ZH000001ZB 结尾 上缴入库的规则
	 */
	public static String getBoxNum5(String number) {
		if (number.length() < 14) {
			return "";
		}

		String num = number.substring(0, 14);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 10));
			sb.append((char) Integer.parseInt(num.substring(10, 12)));
			sb.append((char) Integer.parseInt(num.substring(12, 14)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();

	}

	/**
	 * lianchao 20200327 水牌扫描签
	 * 
	 * @param number
	 * @return SP10000001 整 SP00000001 尾零
	 */
	public static String getWaterCard2(String number) {
		if (number.length() < 11) {
			return "";
		}

		String num = number.substring(0, 11);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 11));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();

	}

	/***
	 * 后督账包复选 长度10 与周装箱一致 最后以ZZ结尾
	 */

	public static String getPostmanAccount(String number) {
		if (number.length() < 14) {
			return "";
		}

		String num = number.substring(0, 14);
		Log.i("转化传进来的2", num);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append((char) Integer.parseInt(num.substring(0, 2)));
			sb.append((char) Integer.parseInt(num.substring(2, 4)));
			sb.append(num.substring(4, 10));
			sb.append((char) Integer.parseInt(num.substring(10, 12)));
			sb.append((char) Integer.parseInt(num.substring(12, 14)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("转化传进来3的", sb.toString());
		System.out.println("转化传进来3的:" + sb.toString());
		return sb.toString();

	}

}
