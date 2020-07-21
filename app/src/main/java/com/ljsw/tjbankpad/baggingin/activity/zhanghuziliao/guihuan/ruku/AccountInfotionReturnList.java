package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku;

import java.io.Serializable;
import java.util.List;

/***
 * 账户资料归还列表实体类
 * 
 * @author Administrator 2918-11-13 暂时只看到这个
 */
public class AccountInfotionReturnList implements Serializable {
	private String CVOUN;// 归还的账户资料 编号

	private String COUNT;// 归还的数量
	private List<String> detailList;// 账户资料的集合

	public AccountInfotionReturnList() {
		super();

	}

	@Override
	public String toString() {
		return "AccountInfotionReturnList [CVOUN=" + CVOUN + ", COUNT=" + COUNT + ", detailList=" + detailList + "]";
	}

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

	public List<String> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<String> detailList) {
		this.detailList = detailList;
	}

}
