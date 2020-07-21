package com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity;

import java.io.Serializable;

/****
 * 2019.10.21
 * 
 * @author Administrator lianc 创建一个实体类接受尾零入库的券别数据
 */
public class TailPackgeEntity implements Serializable {
	private String MONEYTYPE;// 面值
	private String MEID;// 面值的id
	private int EDITION; // 版别

	@Override
	public String toString() {
		return "TailPackgeEntity [" + (MONEYTYPE != null ? "MONEYTYPE=" + MONEYTYPE + ", " : "")
				+ (MEID != null ? "MEID=" + MEID + ", " : "") + "EDITION=" + EDITION + "]";
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

	public int getEDITION() {
		return EDITION;
	}

	public void setEDITION(int eDITION) {
		EDITION = eDITION;
	}

}
