package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;
import java.util.Date;

public class YayunyuanSelectRenWuUserBaseEntity implements Serializable {

	@Override
	public String toString() {
		return "YayunyuanSelectRenWuUserBaseEntity [" + (sjstate != null ? "sjstate=" + sjstate + ", " : "")
				+ (corpid != null ? "corpid=" + corpid + ", " : "")
				+ (corpname != null ? "corpname=" + corpname + ", " : "")
				+ (qlstate != null ? "qlstate=" + qlstate : "") + "]";
	}

	public YayunyuanSelectRenWuUserBaseEntity() {
		super();
	}

	public String getSjappdate() {
		return sjappdate;
	}

	public void setSjappdate(String sjappdate) {
		this.sjappdate = sjappdate;
	}

	public String getSjstate() {
		return sjstate;
	}

	public void setSjstate(String sjstate) {
		this.sjstate = sjstate;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getQlstate() {
		return qlstate;
	}

	public void setQlstate(String qlstate) {
		this.qlstate = qlstate;
	}

	public String getQlappdate() {
		return qlappdate;
	}

	public void setQlappdate(String qlappdate) {
		this.qlappdate = qlappdate;
	}

	private String sjappdate;
	private String sjstate;
	private String corpid;
	private String corpname;
	private String qlstate;
	private String qlappdate;
	private String sj;
	private String ql;
	private String linenum;// 线路编号

	public String getLinenum() {
		return linenum;
	}

	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	private String linename;// 线路名称

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

}
