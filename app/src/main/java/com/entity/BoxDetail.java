package com.entity;

/**
 * @ClassName: BoxDetail
 * @Description: TODO xxxxxxxx
 * @author liuchang
 * @date 2017-5-5 下午4:00:19
 */

public class BoxDetail {
	private String brand; // 钞箱品牌 或者 业务单编号
	private String Num; // 钞箱编号
	private String money; // 加钞金额 或者 核心余额
	private String atmType; // ATM机类型：取款机|存取一体机
	private String boxState;// 钞箱状态

	public String getAtmType() {
		return atmType;
	}

	public void setAtmType(String atmType) {
		this.atmType = atmType;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = num;
	}

	public BoxDetail() {
	};

	public BoxDetail(String brand, String Num) {
		this.brand = brand;
		this.Num = Num;
	}

	public String getBoxState() {
		return boxState;
	}

	public void setBoxState(String boxState) {
		this.boxState = boxState;
	}

}
