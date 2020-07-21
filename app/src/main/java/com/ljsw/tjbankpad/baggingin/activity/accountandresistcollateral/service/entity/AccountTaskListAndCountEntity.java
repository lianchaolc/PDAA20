package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/**
 * 账户资料任务数量的实体类
 * 
 * @author Administrator 2018_11_19
 */
public class AccountTaskListAndCountEntity implements Serializable {
	private int inCenterCount; // 进账户中心
	private int baggingCount; // 包数量

	public AccountTaskListAndCountEntity() {
		super();
	}

	public int getInCenterCount() {
		return inCenterCount;
	}

	public void setInCenterCount(int inCenterCount) {
		this.inCenterCount = inCenterCount;
	}

	public int getBaggingCount() {
		return baggingCount;
	}

	public void setBaggingCount(int baggingCount) {
		this.baggingCount = baggingCount;
	}

	@Override
	public String toString() {
		return "AccountTaskListAndCountEntity [inCenterCount=" + inCenterCount + ", baggingCount=" + baggingCount + "]";
	}

}
