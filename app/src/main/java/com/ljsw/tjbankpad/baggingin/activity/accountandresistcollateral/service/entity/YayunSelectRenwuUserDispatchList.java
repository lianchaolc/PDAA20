package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 接口新增派工的实体类
 * 
 * @author Administrator lianc 2019.12.23.
 *
 */
public class YayunSelectRenwuUserDispatchList implements Serializable {

	@Override
	public String toString() {
		return "YayunSelectRenwuUserDispatchList [" + (sj != null ? "sj=" + sj + ", " : "")
				+ (ql != null ? "ql=" + ql : "") + "]";
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getQl() {
		return ql;
	}

	public YayunSelectRenwuUserDispatchList() {
		super();
	}

	public void setQl(String ql) {
		this.ql = ql;
	}

	private String ql;// 请领

	public YayunSelectRenwuUserDispatchList(String ql, String sj) {
		super();
		this.ql = ql;
		this.sj = sj;
	}

	private String sj; // 上缴
}