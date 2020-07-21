package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import java.io.Serializable;
import java.util.List;

/***
 * 2018-11_13
 * 
 * @author Administrator 外层实体类 账户资料出库借阅
 */
public class AccountOutTaskDetailCode implements Serializable {

	private int count; /// 数量
	private List<AccountOutTaskDetailBase> list; // 数据集合
//	private List<String> list;  //数据集合

	@Override
	public String toString() {
		return "AccountOutTaskDetailCode [count=" + count + ", list=" + list + "]";
	}

	public AccountOutTaskDetailCode() {
		super();

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<AccountOutTaskDetailBase> getList() {
		return list;
	}

	public void setList(List<AccountOutTaskDetailBase> list) {
		this.list = list;
	}

}
