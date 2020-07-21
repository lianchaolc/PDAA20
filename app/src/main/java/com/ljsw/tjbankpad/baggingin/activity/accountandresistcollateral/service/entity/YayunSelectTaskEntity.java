package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 押运员获取线路列表 lianc 201911.26
 * 
 * @author Administrator
 *         :xianluId:CC01|xianluming:存储一线|xianluType:1|qlpaigongdan:|sjpaigongdan:CC0120190718153459|peisongriqi:2019-07-18
 *         押运员任务列表接口调用成功 仿写
 */
public class YayunSelectTaskEntity implements Serializable {
	public YayunSelectTaskEntity() {
		super();
	}

	@Override
	public String toString() {
		return "YayunSelectTaskEntity [" + (xianluId != null ? "xianluId=" + xianluId + ", " : "")
				+ (xianliming != null ? "xianliming=" + xianliming + ", " : "")
				+ (xianluType != null ? "xianluType=" + xianluType + ", " : "")
				+ (qlpaigongdan != null ? "qlpaigongdan=" + qlpaigongdan + ", " : "")
				+ (sjpaigongdan != null ? "sjpaigongdan=" + sjpaigongdan + ", " : "")
				+ (peisongriqi != null ? "peisongriqi=" + peisongriqi : "") + "]";
	}

	public String getXianluId() {
		return xianluId;
	}

	public void setXianluId(String xianluId) {
		this.xianluId = xianluId;
	}

	public String getXianliming() {
		return xianliming;
	}

	public void setXianliming(String xianliming) {
		this.xianliming = xianliming;
	}

	public String getXianluType() {
		return xianluType;
	}

	public void setXianluType(String xianluType) {
		this.xianluType = xianluType;
	}

	public String getQlpaigongdan() {
		return qlpaigongdan;
	}

	public void setQlpaigongdan(String qlpaigongdan) {
		this.qlpaigongdan = qlpaigongdan;
	}

	public String getSjpaigongdan() {
		return sjpaigongdan;
	}

	public void setSjpaigongdan(String sjpaigongdan) {
		this.sjpaigongdan = sjpaigongdan;
	}

	public String getPeisongriqi() {
		return peisongriqi;
	}

	public void setPeisongriqi(String peisongriqi) {
		this.peisongriqi = peisongriqi;
	}

	private String xianluId;
	private String xianliming;
	private String xianluType;
	private String qlpaigongdan;
	private String sjpaigongdan;
	private String peisongriqi;

}
