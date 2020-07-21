package com.example.pda.homemagnetopackge.entity;

import java.io.Serializable;

/***
 * 后督账包所需要的实体类 lainchao 202020222
 * 
 * @author Administrator
 * 
 */
public class HouDulineEntity implements Serializable {

	@Override
	public String toString() {
		return "HouDulineEntity [" + (linenum != null ? "linenum=" + linenum + ", " : "")
				+ (linename != null ? "linename=" + linename + ", " : "")
				+ (count != null ? "count=" + count + ", " : "")
				+ (linstate != null ? "linstate=" + linstate + ", " : "") + "isChecked=" + isChecked + "]";
	}

	public String getLinstate() {
		return linstate;
	}

	public void setLinstate(String linstate) {
		this.linstate = linstate;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	private String linenum;// 线路的id

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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	private String linename; // 线路名称
	private String count; // 线路周转箱数量
	private String linstate;// 线路的状态
	private boolean isChecked = false;
}
