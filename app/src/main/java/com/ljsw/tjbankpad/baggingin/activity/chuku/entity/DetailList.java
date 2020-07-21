package com.ljsw.tjbankpad.baggingin.activity.chuku.entity;

import java.io.Serializable;

/***
 * 抵制押品货位管理员领取任务实体类
 * 
 * @author Administrator 2018_10_31
 */
public class DetailList implements Serializable {

	private String CLEARTASKNUM; // 清分任务号
	private String STOCKCODE; // 周转箱号
	private String ITEMLOCATION; // 位置

	public String getCLEARTASKNUM() {
		return CLEARTASKNUM;
	}

	public void setCLEARTASKNUM(String cLEARTASKNUM) {
		CLEARTASKNUM = cLEARTASKNUM;
	}

	public String getSTOCKCODE() {
		return STOCKCODE;
	}

	public void setSTOCKCODE(String sTOCKCODE) {
		STOCKCODE = sTOCKCODE;
	}

	public String getITEMLOCATION() {
		return ITEMLOCATION;
	}

	public void setITEMLOCATION(String iTEMLOCATION) {
		ITEMLOCATION = iTEMLOCATION;
	}

	@Override
	public String toString() {
		return "LocationManagerBase [CLEARTASKNUM=" + CLEARTASKNUM + ", STOCKCODE=" + STOCKCODE + ", ITEMLOCATION="
				+ ITEMLOCATION + "]";
	}

}
