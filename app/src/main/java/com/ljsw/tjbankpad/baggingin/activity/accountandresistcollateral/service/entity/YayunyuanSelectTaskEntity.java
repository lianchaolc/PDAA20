package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/**
 * Copyright 2019 bejson.com 
 */
/**
 * Auto-generated: 2019-12-18 15:0:19
 *
 * 押运员指纹验证成功后查看押运任务显示的实体类
 */
public class YayunyuanSelectTaskEntity implements Serializable {

	public YayunyuanSelectTaskEntity() {
		super();
	}

	@Override
	public String toString() {
		return "YayunyuanSelectTaskEntity [" + (linename != null ? "linename=" + linename + ", " : "")
				+ (qinglingstate != null ? "qinglingstate=" + qinglingstate + ", " : "")
				+ (linenum != null ? "linenum=" + linenum : "") + "]";
	}

	private String linename;;// 押运员做任务的线路名称
	private String qinglingstate; // 线路状态
	private String linenum; // 、线路号

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getLinename() {
		return linename;
	}

	public void setQinglingstate(String qinglingstate) {
		this.qinglingstate = qinglingstate;
	}

	public String getQinglingstate() {
		return qinglingstate;
	}

	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}

	public String getLinenum() {
		return linenum;
	}

}
