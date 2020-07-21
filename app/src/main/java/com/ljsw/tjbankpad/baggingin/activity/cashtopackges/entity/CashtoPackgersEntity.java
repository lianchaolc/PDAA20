package com.ljsw.tjbankpad.baggingin.activity.cashtopackges.entity;

import java.io.Serializable;

/***
 * 实体类现金装袋
 * 
 * @author Administrator 廉超 20190428
 */
public class CashtoPackgersEntity implements Serializable {

	private String moneyid;// 现金id
	private String moneytype;// 现金类型
	private String parvalue; // 现金的面值
	private String bagmoney;// 总数值

	public String getParvalue() {
		return parvalue;
	}

	public void setParvalue(String parvalue) {
		this.parvalue = parvalue;
	}

	public String getBagmoney() {
		return bagmoney;
	}

	public void setBagmoney(String bagmoney) {
		this.bagmoney = bagmoney;
	}

	@Override
	public String toString() {
		return "CashtoPackgersEntity [" + (moneyid != null ? "moneyid=" + moneyid + ", " : "")
				+ (moneytype != null ? "moneytype=" + moneytype + ", " : "") + "parvalue=" + parvalue + ", bagmoney="
				+ bagmoney + "]";
	}

	public String getMoneyid() {
		return moneyid;
	}

	public void setMoneyid(String moneyid) {
		this.moneyid = moneyid;
	}

	public String getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}

}
