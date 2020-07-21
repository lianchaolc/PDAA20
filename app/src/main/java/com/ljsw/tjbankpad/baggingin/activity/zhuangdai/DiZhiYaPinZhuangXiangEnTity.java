package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;
import java.util.List;

/***
 * 抵制押品装箱的实体类
 * 
 * @author Administrator
 *
 */
public class DiZhiYaPinZhuangXiangEnTity implements Serializable {

	private String count; // 数量
	private String state;// 2 还没交接 3 已经拿到任务
	private String clearTaskNum; /// 任务号
	private List<String> bagList;// 周转箱

	public List<String> getBagList() {
		return bagList;
	}

	public void setBagList(List<String> bagList) {
		this.bagList = bagList;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getClearTaskNum() {
		return clearTaskNum;
	}

	public void setClearTaskNum(String clearTaskNum) {
		this.clearTaskNum = clearTaskNum;
	}

	@Override
	public String toString() {
		return "DiZhiYaPinZhuangXiangEnTity [bagList=" + bagList + ", count=" + count + ", state=" + state
				+ ", clearTaskNum=" + clearTaskNum + "]";
	}

	public DiZhiYaPinZhuangXiangEnTity(String count, String state, String clearTaskNum, List<String> bagList) {
		super();
		this.count = count;
		this.state = state;
		this.clearTaskNum = clearTaskNum;
		this.bagList = bagList;
	}

	public DiZhiYaPinZhuangXiangEnTity() {
		super();

	}

}
