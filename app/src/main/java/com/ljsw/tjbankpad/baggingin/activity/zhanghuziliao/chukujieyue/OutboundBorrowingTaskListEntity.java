package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import java.io.Serializable;

/***
 * 出库任务列表
 * 
 * @author Administrator 2018_11_14 lc
 */

public class OutboundBorrowingTaskListEntity implements Serializable {

	private String CVOUN;// 任务号
	private String COUNT; // 数量
	private String FLAG; // 状态 归还还是借阅

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

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

	@Override
	public String toString() {
		return "OutboundBorrowingTaskListEntity [CVOUN=" + CVOUN + ", COUNT=" + COUNT + ", FLAG=" + FLAG + "]";
	}

	public OutboundBorrowingTaskListEntity() {
		super();
	}

}
