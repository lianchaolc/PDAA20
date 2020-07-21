package com.ljsw.tjbankpad.baggingin.activity;

import java.util.List;

/***
 * 抵制押品的实体类任务列表
 * 
 * @author Administrator
 *
 */
public class DiZhiYaPinChaiXiang {
	private String count;
	private String clearTaskNum;
	private List<String> boxList;

	public DiZhiYaPinChaiXiang(String count, String clearTaskNum, List<String> boxList) {
		super();
		this.count = count;
		this.clearTaskNum = clearTaskNum;
		this.boxList = boxList;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getClearTaskNum() {
		return clearTaskNum;
	}

	public void setClearTaskNum(String clearTaskNum) {
		this.clearTaskNum = clearTaskNum;
	}

	@Override
	public String toString() {
		return "DiZhiYaPinChaiXiang [count=" + count + ", clearTaskNum=" + clearTaskNum + ", boxList=" + boxList + "]";
	}

	public List<String> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<String> boxList) {
		this.boxList = boxList;
	}

}
