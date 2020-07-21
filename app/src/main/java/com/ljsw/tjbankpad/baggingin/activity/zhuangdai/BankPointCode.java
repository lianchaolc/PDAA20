package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;
import java.util.List;

/***
 * 网点订单选择实体类类
 * 
 * @author Administrator
 *
 */

public class BankPointCode implements Serializable {

	private String corpName;/// 银行网点
	private String count; // 数量
	private List<String> cvounList;// 任务集合
	private List<CvounDetailList> cvounDetailList; /// 扫描时需要的任务集合
	private boolean isChecked = false;

	public BankPointCode(String corpName, String count, List<String> cvounList, List<CvounDetailList> cvounDetailList,
			boolean isChecked) {
		super();
		this.corpName = corpName;
		this.count = count;
		this.cvounList = cvounList;
		this.cvounDetailList = cvounDetailList;
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public BankPointCode() {
		super();
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<String> getCvounList() {
		return cvounList;
	}

	public void setCvounList(List<String> cvounList) {
		this.cvounList = cvounList;
	}

	public List<CvounDetailList> getCvounDetailList() {
		return cvounDetailList;
	}

	public void setCvounDetailList(List<CvounDetailList> cvounDetailList) {
		this.cvounDetailList = cvounDetailList;
	}

	@Override
	public String toString() {
		return "BankPointCode [corpName=" + corpName + ", count=" + count + ", cvounList=" + cvounList
				+ ", cvounDetailList=" + cvounDetailList + "]";
	}

	public BankPointCode(String corpName, String count, List<String> cvounList, List<CvounDetailList> cvounDetailList) {
		super();
		this.corpName = corpName;
		this.count = count;
		this.cvounList = cvounList;
		this.cvounDetailList = cvounDetailList;
	}

}
