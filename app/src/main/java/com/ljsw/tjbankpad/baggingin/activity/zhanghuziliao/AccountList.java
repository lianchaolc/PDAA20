package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import java.io.Serializable;

/***
 * 账户资料装袋实体类
 * 
 * @author Administrator
 *
 */
public class AccountList implements Serializable {
	private String ACCOUNTNUM; // 编号
	private String COMPANYNAME;// 名称
	private String ORGNAM; // 说明

	public String getACCOUNTNUM() {
		return ACCOUNTNUM;
	}

	public void setACCOUNTNUM(String aCCOUNTNUM) {
		ACCOUNTNUM = aCCOUNTNUM;
	}

	public String getCOMPANYNAME() {
		return COMPANYNAME;
	}

	public void setCOMPANYNAME(String cOMPANYNAME) {
		COMPANYNAME = cOMPANYNAME;
	}

	public String getORGNAM() {
		return ORGNAM;
	}

	public void setORGNAM(String oRGNAM) {
		ORGNAM = oRGNAM;
	}

	@Override
	public String toString() {
		return "AccountList [ACCOUNTNUM=" + ACCOUNTNUM + ", COMPANYNAME=" + COMPANYNAME + ", ORGNAM=" + ORGNAM + "]";
	}

	public AccountList(String aCCOUNTNUM, String cOMPANYNAME, String oRGNAM) {
		super();
		ACCOUNTNUM = aCCOUNTNUM;
		COMPANYNAME = cOMPANYNAME;
		ORGNAM = oRGNAM;
	}

}
