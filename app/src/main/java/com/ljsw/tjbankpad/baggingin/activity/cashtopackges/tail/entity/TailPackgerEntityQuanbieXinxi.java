package com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity;

import java.io.Serializable;

/***
 * 尾零数据的添加存的实体类
 * 
 * @author Administrator lc
 *
 */
public class TailPackgerEntityQuanbieXinxi implements Serializable {
	@Override
	public String toString() {
		return "TailPackgerEntityQuanbieXinxi [" + (quanbieId != null ? "quanbieId=" + quanbieId + ", " : "")
				+ (quanbieName != null ? "quanbieName=" + quanbieName + ", " : "")
				+ (quanJiazhi != null ? "quanJiazhi=" + quanJiazhi + ", " : "")
				+ (canshunJiazhi != null ? "canshunJiazhi=" + canshunJiazhi + ", " : "")
				+ (EDITION != null ? "EDITION=" + EDITION : "") + "]";
	}

	private String quanbieId; // 券别id
	private String quanbieName; // 券别名称
	private String quanJiazhi; // 全额价值
	private String canshunJiazhi; // 残损价值
	private String EDITION;// 版别

	public String getEDITION() {
		return EDITION;
	}

	public void setEDITION(String eDITION) {
		EDITION = eDITION;
	}

	public String getQuanbieId() {
		return quanbieId;
	}

	public void setQuanbieId(String quanbieId) {
		this.quanbieId = quanbieId;
	}

	public String getQuanbieName() {
		return quanbieName;
	}

	public void setQuanbieName(String quanbieName) {
		this.quanbieName = quanbieName;
	}

	public String getQuanJiazhi() {
		return quanJiazhi;
	}

	public void setQuanJiazhi(String quanJiazhi) {
		this.quanJiazhi = quanJiazhi;
	}

	public String getCanshunJiazhi() {
		return canshunJiazhi;
	}

	public void setCanshunJiazhi(String canshunJiazhi) {
		this.canshunJiazhi = canshunJiazhi;
	}

}
