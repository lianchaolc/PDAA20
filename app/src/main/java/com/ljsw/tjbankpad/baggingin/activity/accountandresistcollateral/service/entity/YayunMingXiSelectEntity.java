package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;

/***
 * 押运员领取任务 明细
 * 
 * @author Administrator lianc 1 上缴2 请领 1.1 待交接 已交接 待确认 待确定 空 2.1 待交接 已交接 待确认
 *         待确定 空
 */
public class YayunMingXiSelectEntity implements Serializable {
	private String upstate;// 上缴状态
	private String tookstate;// 请领状态
	private String orgname;// 机构名称

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getUpstate() {
		return upstate;
	}

	public void setUpstate(String upstate) {
		this.upstate = upstate;
	}

	public String getTookstate() {
		return tookstate;
	}

	public void setTookstate(String tookstate) {
		this.tookstate = tookstate;
	}

	public YayunMingXiSelectEntity() {
		super();
	}

	public YayunMingXiSelectEntity(String upstate, String tookstate) {
		super();
		this.upstate = upstate;
		this.tookstate = tookstate;
	}

	@Override
	public String toString() {
		return "YayunMingXiSelectEntity [" + (upstate != null ? "upstate=" + upstate + ", " : "")
				+ (tookstate != null ? "tookstate=" + tookstate : "") + "]";
	}

}
