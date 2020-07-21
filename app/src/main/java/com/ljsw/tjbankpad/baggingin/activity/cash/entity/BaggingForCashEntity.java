package com.ljsw.tjbankpad.baggingin.activity.cash.entity;

import java.io.Serializable;

/***
 * 2019_4_18
 * 
 * @author Administrator lc 现金过门市天线时 接受券别的实体类
 *
 *         2019.10.25 修改 加入版别 修改实体类
 */
public class BaggingForCashEntity implements Serializable {
//	private  String MONEYID;// 钱的id
//	private String  MONEYTYPE;//钱的类型

	@Override
	public String toString() {
		return "BaggingForCashEntity [" + (MONEYTYPE != null ? "MONEYTYPE=" + MONEYTYPE + ", " : "")
				+ (MEID != null ? "MEID=" + MEID + ", " : "") + (PARVALUE != null ? "PARVALUE=" + PARVALUE + ", " : "")
				+ (EDITION != null ? "EDITION=" + EDITION + ", " : "")
				+ (LOSSVALUE != null ? "LOSSVALUE=" + LOSSVALUE + ", " : "")
				+ (BAGMONEY != null ? "BAGMONEY=" + BAGMONEY : "") + "]";
	}

	public String getMONEYTYPE() {
		return MONEYTYPE;
	}

	public void setMONEYTYPE(String mONEYTYPE) {
		MONEYTYPE = mONEYTYPE;
	}

	public String getMEID() {
		return MEID;
	}

	public void setMEID(String mEID) {
		MEID = mEID;
	}

	public String getPARVALUE() {
		return PARVALUE;
	}

	public void setPARVALUE(String pARVALUE) {
		PARVALUE = pARVALUE;
	}

	public String getEDITION() {
		return EDITION;
	}

	public void setEDITION(String eDITION) {
		EDITION = eDITION;
	}

	public String getLOSSVALUE() {
		return LOSSVALUE;
	}

	public void setLOSSVALUE(String lOSSVALUE) {
		LOSSVALUE = lOSSVALUE;
	}

	private String MONEYTYPE; // 纸币名称
	private String MEID; // 版别的id
	private String PARVALUE; /// 面值的金额
	private String EDITION; // 版别年份
	private String LOSSVALUE;// 半残值
	private String BAGMONEY;// 总钱数
//	@Override
//	public String toString() {
//		return "BaggingForCashEntity ["
//				+ (MONEYTYPE != null ? "MONEYTYPE=" + MONEYTYPE + ", " : "")
//				+ (MONEYID != null ? "MONEYID=" + MONEYID : "") + "]";
//	}
//	public String getMONEYTYPE() {
//		return MONEYTYPE;
//	}
//	public void setMONEYTYPE(String mONEYTYPE) {
//		MONEYTYPE = mONEYTYPE;
//	}
//	public String getMONEYID() {
//		return MONEYID;
//	}
//	public void setMONEYID(String mONEYID) {
//		MONEYID = mONEYID;
//	}
//	

	public String getBAGMONEY() {
		return BAGMONEY;
	}

	public void setBAGMONEY(String bAGMONEY) {
		BAGMONEY = bAGMONEY;
	}
}
