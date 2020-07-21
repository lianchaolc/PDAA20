package com.ljsw.tjbankpad.baggingin.activity.chuku.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 抵制押品实体类
 * 
 * @author Administrator
 *
 */
public class LocationManagerCode implements Serializable {

	private String COUNT; // 数量
	private String CLEARTASKNUM; // 任务号
	private List<DetailList> detailList; /// 任务号中的集合

	public LocationManagerCode(String COUNT, String cLEARTASKNUM, List<DetailList> detailList) {
		super();
		COUNT = COUNT;
		CLEARTASKNUM = cLEARTASKNUM;
		this.detailList = detailList;
	}

	public String getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(String cOUNT) {
		COUNT = cOUNT;
	}

	public String getCLEARTASKNUM() {
		return CLEARTASKNUM;
	}

	public void setCLEARTASKNUM(String cLEARTASKNUM) {
		CLEARTASKNUM = cLEARTASKNUM;
	}

	public List<DetailList> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DetailList> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		return "LocationManagerCode [COUNT=" + COUNT + ", CLEARTASKNUM=" + CLEARTASKNUM + ", detailList=" + detailList
				+ "]";
	}

}
