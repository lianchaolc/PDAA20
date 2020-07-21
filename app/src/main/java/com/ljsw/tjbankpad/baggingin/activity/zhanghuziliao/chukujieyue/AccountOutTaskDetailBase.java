package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import java.io.Serializable;

/***
 * 2018-11_14
 * 
 * @author Administrator
 *
 */
public class AccountOutTaskDetailBase implements Serializable {
	private String LOCATION; // 放置的位置
	private String STOCKCODE; /// 箱子的号码

	public String getCONTAINERNO() {
		return LOCATION;
	}

	public AccountOutTaskDetailBase(String cONTAINERNO, String sTOCKCODE) {
		super();
		LOCATION = cONTAINERNO;
		STOCKCODE = sTOCKCODE;
	}

	public void setCONTAINERNO(String cONTAINERNO) {
		LOCATION = cONTAINERNO;
	}

	public String getSTOCKCODE() {
		return STOCKCODE;
	}

	public void setSTOCKCODE(String sTOCKCODE) {
		STOCKCODE = sTOCKCODE;
	}

	@Override
	public String toString() {
		return "AccountOutTaskDetailBase [CONTAINERNO=" + LOCATION + ", STOCKCODE=" + STOCKCODE + "]";
	}

}
