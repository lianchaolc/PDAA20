package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import java.io.Serializable;
import java.util.List;

/***
 * 实体类
 * 
 * @author Administrator 账户资料入库交接实体类 2018_11_13
 */
public class AccountInfoInHandoverEntity implements Serializable {
	private String count;// 账户箱号的数量
	private List<String> stockCodeList;// 装有的箱子号

	public AccountInfoInHandoverEntity() {
		super();
	}

	@Override
	public String toString() {
		return "AccountInfoInHandover [count=" + count + ", stockCodeList=" + stockCodeList + "]";
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<String> getStockCodeList() {
		return stockCodeList;
	}

	public void setStockCodeList(List<String> stockCodeList) {
		this.stockCodeList = stockCodeList;
	}

}
