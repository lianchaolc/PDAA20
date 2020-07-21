package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 获取任务进行显示的实体类
 * 
 * @author Administrator
 * 
 */
public class YayunSelectRewuUserEntity implements Serializable {
//	private String linename; // 线路名称
//	private String flag; // 是否有请领
//	private String corpid; // 机构id
//	private String state; // 是否有上缴
//	private String corpname; // 银行名称
//	private String linenum;// 线路名称
	private String linename;

	public YayunSelectRewuUserEntity(String linename, String flag, String corpid, String state, String corpname,
			String linenum, String sj, String ql) {
		super();
		this.linename = linename;
		this.flag = flag;
		this.corpid = corpid;
		this.state = state;
		this.corpname = corpname;
		this.linenum = linenum;
		this.sj = sj;
		this.ql = ql;
	}

	private String flag;
	private String corpid;
	private String state;
	private String corpname;
	private String linenum;
	private String sj;
	private String ql;

	public String getAppdate() {
		return appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	private String appdate;// 派工单的日期

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getQl() {
		return ql;
	}

	public void setQl(String ql) {
		this.ql = ql;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

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

	public String getLinenum() {
		return linenum;
	}

	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}

	public YayunSelectRewuUserEntity() {
		super();
	}

	@Override
	public String toString() {
		return "YayunSelectRewuUserEntity [" + (linename != null ? "linename=" + linename + ", " : "")
				+ (flag != null ? "flag=" + flag + ", " : "") + (corpid != null ? "corpid=" + corpid + ", " : "")
				+ (state != null ? "state=" + state + ", " : "")
				+ (corpname != null ? "corpname=" + corpname + ", " : "")
				+ (linenum != null ? "linenum=" + linenum : "") + "]";
	}

	public YayunSelectRewuUserEntity(String linename, String flag, String corpid, String state, String corpname,
			String linenum) {
		super();
		this.linename = linename;
		this.flag = flag;
		this.corpid = corpid;
		this.state = state;
		this.corpname = corpname;
		this.linenum = linenum;
	}

}
