package com.pda.checksupplement.entity;

import java.io.Serializable;

/***
 * 1. 查/盘库补录任务列表 实体类
 * 
 * @author Administrator lc 2018_11_29
 */
public class ChelibrarySupplemnetBaseEntity implements Serializable {
	private String TASKNUM; // 任务数量
	private String MISSCOUNT; // 未扫到的数量

	public String getTASKNUM() {
		return TASKNUM;
	}

	public void setTASKNUM(String tASKNUM) {
		TASKNUM = tASKNUM;
	}

	public String getMISSCOUNT() {
		return MISSCOUNT;
	}

	public void setMISSCOUNT(String mISSCOUNT) {
		MISSCOUNT = mISSCOUNT;
	}

	@Override
	public String toString() {
		return "ChelibrarySupplemnetBaseEntity [" + (TASKNUM != null ? "TASKNUM=" + TASKNUM + ", " : "")
				+ (MISSCOUNT != null ? "MISSCOUNT=" + MISSCOUNT : "") + "]";
	}

}
