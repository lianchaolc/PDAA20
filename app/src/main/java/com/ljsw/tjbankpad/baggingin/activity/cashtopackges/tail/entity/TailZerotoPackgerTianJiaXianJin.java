package com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity;

import java.io.Serializable;

/****
 * 仿写实体类 201910.22 lc
 * 
 * @author Administrator
 *
 */

public class TailZerotoPackgerTianJiaXianJin implements Serializable {

	public TailZerotoPackgerTianJiaXianJin() {
		super();
	}

	public String getJuanbie() {
		return juanbie;
	}

	public void setJuanbie(String juanbie) {
		this.juanbie = juanbie;
	}

	public String getZhuangtai() {
		return zhuangtai;
	}

	public void setZhuangtai(String zhuangtai) {
		this.zhuangtai = zhuangtai;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
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

	public String getCashbanbie() {
		return cashbanbie;
	}

	public void setCashbanbie(String cashbanbie) {
		this.cashbanbie = cashbanbie;
	}

	public String getCash_type() {
		return cash_type;
	}

	public void setCash_type(String cash_type) {
		this.cash_type = cash_type;
	}

	private String juanbie; // 券别id

	public TailZerotoPackgerTianJiaXianJin(String juanbie, String zhuangtai, String count, String quanJiazhi,
			String canshunJiazhi, String cashbanbie, String cash_type) {
		super();

	}

	private String zhuangtai; // 状态
	private String count; // 数量
	private String quanJiazhi; // 全额价值
	private String canshunJiazhi; // 残损价值
	private String cashbanbie; // 版别
	private String cash_type;// 预留

}
