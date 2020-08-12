package com.zhiang.interfac;

import com.za.finger.ZAandroid;


public class Xx500c {
	/**
	 * �����ɹ�
	 */
	public final int PS_OK = 0;
	/**
	 * �����հ�����
	 */
	public final int PS_COMM_ERR = 1;
	/**
	 * ������������ָ
	 */
	public final int PS_NO_FINGER = 2;
	/**
	 * ¼��ָ�Ʋ��ɹ�
	 */
	public final int PS_GET_IMG_ERR = 3;
	/**
	 * ָ��̫�ɡ�̫��������������
	 */
	public final int PS_FP_TOO_DRY = 4;
	/**
	 * ָ��̫ʪ��̫��������������
	 */
	public final int PS_FP_TOO_WET = 5;
	/**
	 * ָ��ͼ��̫������������
	 */
	public final int PS_FP_DISORDER = 6;
	/**
	 * ָ��̫С��������̫������������
	 */
	public final int PS_LITTLE_FEATURE = 7;
	/**
	 * ָ�Ʋ�ƥ��
	 */
	public final int PS_NOT_MATCH = 8;
	/**
	 * û������ָ��
	 */
	public final int PS_NOT_SEARCHED = 9;
	/**
	 * �����ϲ�ʧ��
	 */
	public final int PS_MERGE_ERR = 10;
	/**
	 * ����ָ�ƿ��ַ��ų�����Χ
	 */
	public final int PS_ADDRESS_OVER = 11;
	/**
	 * ָ�ƿ��ģ��������Ч
	 */
	public final int PS_READ_ERR = 12;
	/**
	 * �ϴ�����ʧ��
	 */
	public final int PS_UP_TEMP_ERR = 13;
	/**
	 * ģ�鲻�ܽ��պ������ݰ�
	 */
	public final int PS_RECV_ERR = 14;
	/**
	 * �ϴ�ͼ��ʧ��
	 */
	public final int PS_UP_IMG_ERR = 15;
	/**
	 * ɾ��ģ��ʧ��
	 */
	public final int PS_DEL_TEMP_ERR = 16;
	/**
	 * ���ָ�ƿ�ʧ��
	 */
	public final int PS_CLEAR_TEMP_ERR = 17;
	/**
	 * ���ܽ���͹���״̬
	 */
	public final int PS_SLEEP_ERR = 18;
	/**
	 * �����ȷ
	 */
	public final int PS_INVALID_PASSWORD = 19;
	/**
	 * ϵͳ��λʧ��
	 */
	public final int PS_RESET_ERR = 20;
	/**
	 * ��������û����Чԭʼͼ��������ͼ��
	 */
	public final int PS_INVALID_IMAGE = 21;
	/**
	 * ��������ʧ��
	 */
	public final int PS_HANGOVER_UNREMOVE = 22;
	/**
	 * ����ָ�ƻ����βɼ�֮����ָû���ƶ���
	 */
	public final int PS_HAND_MOVE = 23;
	/**
	 * дFlash����
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
	 * δ�������
	 */
	public final int PS_DON_ERROR = 0x19;
	/**
	 * ��Ч�Ĵ�����
	 */
	public final int PS_SAVE_ID = 0x1a;
	/**
	 * �Ĵ����趨���ݴ����
	 */
	public final int PS_SAVEID_ERROR = 0x1b;
	/**
	 * ���±�ҳ��ָ������
	 */
	public final int PS_BOOK_ERROR = 0x1c;
	/**
	 * �˿ڲ���ʧ��
	 */
	public final int PS_IP_ERROR = 0x1d;
	/**
	 * �Զ�ע��enrollʧ��
	 */
	public final int PS_REGISTER_ERROR = 0x1e;
	/**
	 * ָ�ƿ���
	 */
	public final int PS_FG_EMPTY = 0x1f;
	/**
	 * ָ���豸δ����
	 */
	public final int PS_NO_CONNECT = -1;
	/**
	 * ͨѶ����
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
 * iScore��ֵ������ζ���*/
	public int FunPS_Match(int iBufferID) {
		return a6.ZAZMatch(nAddr, iScore);
	}

	public String FunPS_ErrorStr(int status) {
		
		switch (status) {
		case 0:
			temp="�����ɹ�";
			break;
		case 1:
			temp="�����հ�����";
			break;
		case 2:
			temp="������������ָ";
			break;
		case 3:
			temp="¼��ָ�Ʋ��ɹ�";
			break;
		case 4:
			temp="ָ��̫�ɡ�̫��������������";
			break;
		case 5:
			temp="ָ��̫ʪ��̫��������������";
			break;
		case 6:
			temp="ָ��ͼ��̫������������";
			break;
		case 7:
			temp="ָ��̫С��������̫������������";
			break;
		case 8:
			temp="ָ�Ʋ�ƥ��";
			break;
		case 9:
			temp="û������ָ��";
			break;
		case 10:
			temp="�����ϲ�ʧ��";
			break;
		case 11:
			temp="����ָ�ƿ��ַ��ų�����Χ";
			break;
		case 12:
			temp="ָ�ƿ��ģ��������Ч";
			break;
		case 13:
			temp="�ϴ�����ʧ��";
			break;
		case 14:
			temp="ģ�鲻�ܽ��պ������ݰ�";
			break;
		case 15:
			temp="�ϴ�ͼ��ʧ��";
			break;
		case 16:
			temp="ɾ��ģ��ʧ��";
			break;
		case 17:
			temp="���ָ�ƿ�ʧ��";
			break;
		case 18:
			temp="���ܽ���͹���״̬";
			break;
		case 19:
			temp="�����ȷ";
			break;
		case 20:
			temp="ϵͳ��λʧ��";
			break;
		case 21:
			temp="��������û����Чԭʼͼ��������ͼ��";
			break;
		case 22:
			temp="��������ʧ��";
			break;
		case 23:
			temp="����ָ�ƻ����βɼ�֮����ָû���ƶ���";
			break;
		case 24:
			temp="дFlash����";
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
