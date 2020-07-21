package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 库管员 查看任务数量
 * 
 * @author Administrator 2018_11_19 lc
 */
public class WarehouseTaskListAndCount implements Serializable {
	private int accountReturnCount;
	private int collateralInCount;
	private int accountOutCount;
	private int collateralOutCount;
	private int accountInCount;
	private int accountPutInCount;

	public WarehouseTaskListAndCount() {
		super();
	}

	@Override
	public String toString() {
		return "WarehouseTaskListAndCount [accountReturnCount=" + accountReturnCount + ", collateralInCount="
				+ collateralInCount + ", accountOutCount=" + accountOutCount + ", collateralOutCount="
				+ collateralOutCount + ", accountInCount=" + accountInCount + ", accountPutInCount=" + accountPutInCount
				+ "]";
	}

	public int getAccountReturnCount() {
		return accountReturnCount;
	}

	public void setAccountReturnCount(int accountReturnCount) {
		this.accountReturnCount = accountReturnCount;
	}

	public int getCollateralInCount() {
		return collateralInCount;
	}

	public void setCollateralInCount(int collateralInCount) {
		this.collateralInCount = collateralInCount;
	}

	public int getAccountOutCount() {
		return accountOutCount;
	}

	public void setAccountOutCount(int accountOutCount) {
		this.accountOutCount = accountOutCount;
	}

	public int getCollateralOutCount() {
		return collateralOutCount;
	}

	public void setCollateralOutCount(int collateralOutCount) {
		this.collateralOutCount = collateralOutCount;
	}

	public int getAccountInCount() {
		return accountInCount;
	}

	public void setAccountInCount(int accountInCount) {
		this.accountInCount = accountInCount;
	}

	public int getAccountPutInCount() {
		return accountPutInCount;
	}

	public void setAccountPutInCount(int accountPutInCount) {
		this.accountPutInCount = accountPutInCount;
	}

}
