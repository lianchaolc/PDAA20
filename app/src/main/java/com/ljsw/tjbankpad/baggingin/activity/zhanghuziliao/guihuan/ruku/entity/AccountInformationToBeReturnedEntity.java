package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 账户资料待归还的实体类
 * 
 * @author Administrator 2018-11_19 lc
 * 
 * 
 */
public class AccountInformationToBeReturnedEntity implements Serializable {

	public AccountInformationToBeReturnedEntity() {
		super();

	}

	private String CVOUN; // 账号
	private String COUNT; /// 归还的数量
	private List<DetailList> detailList; // 报存位置和箱号的结合

	public String getCVOUN() {
		return CVOUN;
	}

	public void setCVOUN(String cVOUN) {
		CVOUN = cVOUN;
	}

	public String getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(String cOUNT) {
		COUNT = cOUNT;
	}

	public List<DetailList> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DetailList> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		return "AccountInformationToBeReturnedEntity [CVOUN=" + CVOUN + ", COUNT=" + COUNT + ", detailList="
				+ detailList + "]";
	}

}
