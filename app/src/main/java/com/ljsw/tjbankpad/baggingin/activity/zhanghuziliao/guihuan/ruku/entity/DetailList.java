package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.entity;

import java.io.Serializable;

/**
 * 2018_11_19
 * 
 * @author Administrator lc 账户资料待归还的实体类内层
 */
public class DetailList implements Serializable {
	private String LOCATION; // 存放位置
	private String STOCKCODE; // 周装箱的号

	public DetailList(String lOCATION, String sTOCKCODE) {
		super();
		LOCATION = lOCATION;
		STOCKCODE = sTOCKCODE;
	}

	public DetailList() {
		super();
	}

	@Override
	public String toString() {
		return "DetailListEntity [LOCATION=" + LOCATION + ", STOCKCODE=" + STOCKCODE + "]";
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}

	public String getSTOCKCODE() {
		return STOCKCODE;
	}

	public void setSTOCKCODE(String sTOCKCODE) {
		STOCKCODE = sTOCKCODE;
	}

}
