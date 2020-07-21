package com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.entity;

import java.io.Serializable;

/****
 * 账户资料交接入库的第一个页面：查询待做的线路数
 * 
 * @author Administrator1
 *
 */
@SuppressWarnings("serial")
public class AccountCenterInEntity implements Serializable {
	private String count; // 任务数

	public AccountCenterInEntity() {
		super();
	}

	@Override
	public String toString() {
		return "AccountCenterInEntity [count=" + count + "]";
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

}
