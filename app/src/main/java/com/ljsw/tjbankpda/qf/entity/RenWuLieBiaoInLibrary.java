package com.ljsw.tjbankpda.qf.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 实体类代码 入库上缴任务中管库员或者是外包清分人员登录后 对上缴入库的处理当前显示入库的线路数 20200318 lianc
 * 
 * @author Administrator
 *
 */
public class RenWuLieBiaoInLibrary implements Serializable {

	@Override
	public String toString() {
		return "RenWuLieBiaoInLibrary [" + (boxnums != null ? "boxnums=" + boxnums + ", " : "")
				+ (linename != null ? "linename=" + linename + ", " : "")
				+ (linenum != null ? "linenum=" + linenum : "") + "]";
	}

	public List<String> getBoxnums() {
		return boxnums;
	}

	public void setBoxnums(List<String> boxnums) {
		this.boxnums = boxnums;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getLinenum() {
		return linenum;
	}

	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}

	private List<String> boxnums; // 周转箱
	private String linename; // 线路名称 和线路id
	private String linenum;
}
