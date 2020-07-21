package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan;

import java.io.Serializable;
import java.util.List;

/***
 * 账户资料归还 归还任务列表和详情
 * 
 * @author Administrator 2018-11-14 lc
 *
 */
public class ReturnAccountInformationEntity implements Serializable {

	private String CVOUN; // 归还任务号
	private String COUNT; // 数量
	private List<String> detailList; // 集合中的箱子号

	public ReturnAccountInformationEntity() {
		super();
	}

	@Override
	public String toString() {
		return "ReturnAccountInformationEntity [CVOUN=" + CVOUN + ", COUNT=" + COUNT + ", detailList=" + detailList
				+ "]";
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
