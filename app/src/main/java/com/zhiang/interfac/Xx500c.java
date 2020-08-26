package com.zhiang.interfac;

import com.za.finger.ZAandroid;


public class Xx500c {
	/**
	 * 操作成功
	 */
	public final int PS_OK = 0;
	/**
	 * 数据收包错误
	 */
	public final int PS_COMM_ERR = 1;
	/**
	 * 传感器上无手指
	 */
	public final int PS_NO_FINGER = 2;
	/**
	 * 录入指纹不成功
	 */
	public final int PS_GET_IMG_ERR = 3;
	/**
	 * 指纹太干、太淡，生不成特征
	 */
	public final int PS_FP_TOO_DRY = 4;
	/**
	 * 指纹太湿、太糊，生不成特征
	 */
	public final int PS_FP_TOO_WET = 5;
	/**
	 * 指纹图像太乱生不成特征
	 */
	public final int PS_FP_DISORDER = 6;
	/**
	 * 指纹太小或特征点太少生不成特征
	 */
	public final int PS_LITTLE_FEATURE = 7;
	/**
	 * 指纹不匹配
	 */
	public final int PS_NOT_MATCH = 8;
	/**
	 * 没搜索到指纹
	 */
	public final int PS_NOT_SEARCHED = 9;
	/**
	 * 特征合并失败
	 */
	public final int PS_MERGE_ERR = 10;
	/**
	 * 访问指纹库地址序号超出范围
	 */
	public final int PS_ADDRESS_OVER = 11;
	/**
	 * 指纹库读模板出错或无效
	 */
	public final int PS_READ_ERR = 12;
	/**
	 * 上传特征失败
	 */
	public final int PS_UP_TEMP_ERR = 13;
	/**
	 * 模块不能接收后续数据包
	 */
	public final int PS_RECV_ERR = 14;
	/**
	 * 上传图像失败
	 */
	public final int PS_UP_IMG_ERR = 15;
	/**
	 * 删除模板失败
	 */
	public final int PS_DEL_TEMP_ERR = 16;
	/**
	 * 清空指纹库失败
	 */
	public final int PS_CLEAR_TEMP_ERR = 17;
	/**
	 * 不能进入低功耗状态
	 */
	public final int PS_SLEEP_ERR = 18;
	/**
	 * 口令不正确
	 */
	public final int PS_INVALID_PASSWORD = 19;
	/**
	 * 系统复位失败
	 */
	public final int PS_RESET_ERR = 20;
	/**
	 * 缓冲区内没有有效原始图像不能生成图像
	 */
	public final int PS_INVALID_IMAGE = 21;
	/**
	 * 在线升级失败
	 */
	public final int PS_HANGOVER_UNREMOVE = 22;
	/**
	 * 残留指纹或两次采集之间手指没有移动过
	 */
	public final int PS_HAND_MOVE = 23;
	/**
	 * 写Flash出错
	 */
	public final int PS_FLASH_ERROR = 24;

	public final int CHAR_BUFFER_A = 0xf0;
	public final int CHAR_BUFFER_B = 0xf1;

	public final int FLASH_CHECK = 0xf2;
	public final int FLASH_PK_MARK = 0xf3;
	public final int FLASH_PK_LENGTH = 0xf4;
	public final int FLASH_CODE_LENGTH = 0xf5;
	public final int FLASH_FAIL = 0xf6;
	/**
	 * 未定义错误
	 */
	public final int PS_DON_ERROR = 0x19;
	/**
	 * 无效寄存器号
	 */
	public final int PS_SAVE_ID = 0x1a;
	/**
	 * 寄存器设定内容错误号
	 */
	public final int PS_SAVEID_ERROR = 0x1b;
	/**
	 * 记事本页码指定错误
	 */
	public final int PS_BOOK_ERROR = 0x1c;
	/**
	 * 端口操作失败
	 */
	public final int PS_IP_ERROR = 0x1d;
	/**
	 * 自动注册enroll失败
	 */
	public final int PS_REGISTER_ERROR = 0x1e;
	/**
	 * 指纹库满
	 */
	public final int PS_FG_EMPTY = 0x1f;
	/**
	 * 指纹设备未连接
	 */
	public final int PS_NO_CONNECT = -1;
	/**
	 * 通讯错误
	 */
	public final int PS_IM_ERROR = -2;

	public ZAandroid a6 = new ZAandroid();
	private int nAddr = 0xffffffff;
	public int[] iTempletLength = { 0, 0 };
	public int iTempletLengths = 2304;
	public int[] iScore = { 0, 0 };
	String temp="";

	public int FunPS_GetImage() {
		return a6.ZAZGetImage(nAddr);
	}

	public int FunPS_UpImage(byte[] pImageData) {
		return a6.ZAZUpImage(nAddr, pImageData, iTempletLength);
	}

	public int FunPS_GenChar(int iBufferID) {
		return a6.ZAZGenChar(nAddr, iBufferID);
	}

	public int FunPS_UpChar(int iBufferID, byte[] pImageData) {
		return a6.ZAZUpChar(nAddr, iBufferID, pImageData, iTempletLength);
	}

	public int FunPS_DownChar(int iBufferID, byte[] pImageData) {
		return a6.ZAZDownChar(nAddr, iBufferID, pImageData, iTempletLengths);
	}
/**
 * iScore的值不传如何定义*/
	public int FunPS_Match(int iBufferID) {
		return a6.ZAZMatch(nAddr, iScore);
	}

	public String FunPS_ErrorStr(int status) {
		
		switch (status) {
		case 0:
			temp="操作成功";
			break;
		case 1:
			temp="数据收包错误";
			break;
		case 2:
			temp="传感器上无手指";
			break;
		case 3:
			temp="录入指纹不成功";
			break;
		case 4:
			temp="指纹太干、太淡，生不成特征";
			break;
		case 5:
			temp="指纹太湿、太糊，生不成特征";
			break;
		case 6:
			temp="指纹图像太乱生不成特征";
			break;
		case 7:
			temp="指纹太小或特征点太少生不成特征";
			break;
		case 8:
			temp="指纹不匹配";
			break;
		case 9:
			temp="没搜索到指纹";
			break;
		case 10:
			temp="特征合并失败";
			break;
		case 11:
			temp="访问指纹库地址序号超出范围";
			break;
		case 12:
			temp="指纹库读模板出错或无效";
			break;
		case 13:
			temp="上传特征失败";
			break;
		case 14:
			temp="模块不能接收后续数据包";
			break;
		case 15:
			temp="上传图像失败";
			break;
		case 16:
			temp="删除模板失败";
			break;
		case 17:
			temp="清空指纹库失败";
			break;
		case 18:
			temp="不能进入低功耗状态";
			break;
		case 19:
			temp="口令不正确";
			break;
		case 20:
			temp="系统复位失败";
			break;
		case 21:
			temp="缓冲区内没有有效原始图像不能生成图像";
			break;
		case 22:
			temp="在线升级失败";
			break;
		case 23:
			temp="残留指纹或两次采集之间手指没有移动过";
			break;
		case 24:
			temp="写Flash出错";
			break;

		default:
			break;
		}
		return temp;
	}

	// public int FunPS_GetDeviceNumber(byte[] pImageData){
	// return a6.ZAZGetImage(nAddr);
	// }
	//
	// public int FunPS_GetDevInfo(){
	// return a6.ZAZGetImage(nAddr);
	// }

	public int FunPS_RegModel() {
		return a6.ZAZRegModule(nAddr);
	}
}
