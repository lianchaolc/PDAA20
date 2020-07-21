package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 2018_11_19
 * 
 * @author Administrator lc 清分人员获取任务数量
 */
public class ClearCollateralTaskListAndCount implements Serializable {

	private int collateralPacketCount;// 装袋 装箱
	private int collateralBaggingCount;

	@Override
	public String toString() {
		return "ClearCollateralTaskListAndCount [collateralPacketCount=" + collateralPacketCount
				+ ", collateralBaggingCount=" + collateralBaggingCount + "]";
	}

	public ClearCollateralTaskListAndCount() {
		super();

	}

	public int getCollateralPacketCount() {
		return collateralPacketCount;
	}

	public void setCollateralPacketCount(int collateralPacketCount) {
		this.collateralPacketCount = collateralPacketCount;
	}

	public int getCollateralBaggingCount() {
		return collateralBaggingCount;
	}

	public void setCollateralBaggingCount(int collateralBaggingCount) {
		this.collateralBaggingCount = collateralBaggingCount;
	}

}
