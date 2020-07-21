package com.pda.checksupplement.entity;

import java.io.Serializable;

/***
 * 2019_3_8
 * 
 * @author Administrator 获取券别实体类
 */
public class GetTypeOfCouponEntity implements Serializable {
	private String MONEYTYPE;
	private String MONEYID;

	// 券别类型
	/// 券别的id
	public void setMONEYTYPE(String MONEYTYPE) {
		this.MONEYTYPE = MONEYTYPE;
	}

	public String getMONEYTYPE() {
		return MONEYTYPE;
	}

	public void setMONEYID(String MONEYID) {
		this.MONEYID = MONEYID;
	}

	public String getMONEYID() {
		return MONEYID;
	}

	@Override
	public String toString() {
		return "GetTypeOfCouponEntity [" + (MONEYTYPE != null ? "MONEYTYPE=" + MONEYTYPE + ", " : "")
				+ (MONEYID != null ? "MONEYID=" + MONEYID : "") + "]";
	}
}
