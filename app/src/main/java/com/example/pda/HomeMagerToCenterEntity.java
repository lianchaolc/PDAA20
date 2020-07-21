package com.example.pda;

import java.io.Serializable;

/***
 * 库管员登录后获取袋 201910.25
 * 
 * @author Administrator lc
 */
public class HomeMagerToCenterEntity implements Serializable {
	private String cdno;// 钞袋号

	@Override
	public String toString() {
		return "HomeMagerToCenterEntity [" + (cdno != null ? "cdno=" + cdno : "") + "]";
	}

	public String getCdno() {
		return cdno;
	}

	public void setCdno(String cdno) {
		this.cdno = cdno;
	}

}
