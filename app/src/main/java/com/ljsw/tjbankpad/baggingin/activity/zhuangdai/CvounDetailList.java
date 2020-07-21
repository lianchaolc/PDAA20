package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;
import java.util.List;

/***
 * 银行网点 订单选择
 * 
 * @author Administrator 11-5 实体类
 */
public class CvounDetailList implements Serializable {
	private String cvoun;
	private List<String> detailList;

	@Override
	public String toString() {
		return "BankPointBase [cvoun=" + cvoun + ", detailList=" + detailList + "]";
	}

	public CvounDetailList(String cvoun, List<String> detailList) {
		super();
		this.cvoun = cvoun;
		this.detailList = detailList;
	}

	public List<String> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<String> detailList) {
		this.detailList = detailList;
	}

	public String getCvoun() {
		return cvoun;
	}

	public void setCvoun(String cvoun) {
		this.cvoun = cvoun;
	}

}
