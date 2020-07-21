package com.pda.checksupplement.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 
 * 库管员 2018_11_29 lc
 * 
 * @author Administrator 查库和盘库的实体类
 */
public class ChecklibraryReplenisBaseEntity implements Serializable {
	private String UNKNOWNCOUNT; // 不知道的数量
	private List<String> MISSLIST; // 漏扫的数量
	private String TASKNUM;// 任务编号
	private String TYPE; /// 类型
	private String MISSCOUNT; // 漏的数量
	private List<String> UNKNOWNLIST; /// 漏的集合数据

	public ChecklibraryReplenisBaseEntity() {
		super();
	}

	@Override
	public String toString() {
		return "ChecklibraryReplenisBaseEntity [UNKNOWNCOUNT=" + UNKNOWNCOUNT + ", "
				+ (MISSLIST != null ? "MISSLIST=" + MISSLIST + ", " : "")
				+ (TASKNUM != null ? "TASKNUM=" + TASKNUM + ", " : "") + (TYPE != null ? "TYPE=" + TYPE + ", " : "")
				+ "MISSCOUNT=" + MISSCOUNT + ", " + (UNKNOWNLIST != null ? "UNKNOWNLIST=" + UNKNOWNLIST : "") + "]";
	}

	public String getUNKNOWNCOUNT() {
		return UNKNOWNCOUNT;
	}

	public void setUNKNOWNCOUNT(String uNKNOWNCOUNT) {
		UNKNOWNCOUNT = uNKNOWNCOUNT;
	}

	public List<String> getMISSLIST() {
		return MISSLIST;
	}

	public void setMISSLIST(List<String> mISSLIST) {
		MISSLIST = mISSLIST;
	}

	public String getTASKNUM() {
		return TASKNUM;
	}

	public void setTASKNUM(String tASKNUM) {
		TASKNUM = tASKNUM;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getMISSCOUNT() {
		return MISSCOUNT;
	}

	public void setMISSCOUNT(String mISSCOUNT) {
		MISSCOUNT = mISSCOUNT;
	}

	public List<String> getUNKNOWNLIST() {
		return UNKNOWNLIST;
	}

	public void setUNKNOWNLIST(List<String> uNKNOWNLIST) {
		UNKNOWNLIST = uNKNOWNLIST;
	}

}
