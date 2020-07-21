package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import java.io.Serializable;
import java.util.List;

public class AccountCenterBagginBase implements Serializable {
	private String undoSum; // 没做的总数
	private List<String> undoAccountNumList; // 没做的数量
	private List<AccountList> accountList;// 要装的集合

	public String getUndoSum() {
		return undoSum;
	}

	public void setUndoSum(String undoSum) {
		this.undoSum = undoSum;
	}

	public List<AccountList> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<AccountList> accountList) {
		this.accountList = accountList;
	}

	public List<String> getUndoAccountNumList() {
		return undoAccountNumList;
	}

	public void setUndoAccountNumList(List<String> undoAccountNumList) {
		this.undoAccountNumList = undoAccountNumList;
	}

	@Override
	public String toString() {
		return "AccountCenterBagginBase [undoSum=" + undoSum + ", accountList=" + accountList + ", undoAccountNumList="
				+ undoAccountNumList + "]";
	}

	public AccountCenterBagginBase() {
		super();
	}

}
