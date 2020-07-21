package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 
 * lianc
 * 
 * @author Administrator 20191219 押運員选中一条线路后进入当前（确定按钮前的显示） 需要查询一下上缴的状态并提交
 */
public class YayunSelectRewuTookinfoAndTrueEntity implements Serializable {

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	private String state;// 状态

	public YayunSelectRewuTookinfoAndTrueEntity() {
		super();
	}

	@Override
	public String toString() {
		return "YayunSelectRewuTookinfoAndTrueEntity [" + (state != null ? "state=" + state + ", " : "")
				+ (corpname != null ? "corpname=" + corpname + ", " : "") + (corpid != null ? "corpid=" + corpid : "")
				+ "]";
	}

	private String corpname;// 名称
	private String corpid;// 机构的id
}
