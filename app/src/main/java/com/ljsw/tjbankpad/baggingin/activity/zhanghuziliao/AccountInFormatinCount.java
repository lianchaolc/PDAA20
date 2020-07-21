package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

/***
 * 实体类 2018_11_8
 * 
 * @author Administrator 获取线路实体类
 */
public class AccountInFormatinCount {
	private int count;// 获取数量

	public AccountInFormatinCount(int count) {
		super();
		this.count = count;
	}

	@Override
	public String toString() {
		return "AccountInFormatinCount [count=" + count + "]";
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
