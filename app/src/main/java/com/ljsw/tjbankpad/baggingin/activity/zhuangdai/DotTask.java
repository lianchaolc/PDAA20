package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;
import java.util.List;

/***
 * 网点任务实体类
 * 
 * @author Administrator
 *
 */
public class DotTask implements Serializable {

	private String CORPID;// 任务编号
	private String CORPNAME;// 任务名称
	private String COUNT;// 数量

	public DotTask() {
		super();
	}

	public String getCORPID() {
		return CORPID;
	}

	public void setCORPID(String cORPID) {
		CORPID = cORPID;
	}

	public String getCORPNAME() {
		return CORPNAME;
	}

	public void setCORPNAME(String cORPNAME) {
		CORPNAME = cORPNAME;
	}

	public String getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(String cOUNT) {
		COUNT = cOUNT;
	}

	@Override
	public String toString() {
		return "DotTask [CORPID=" + CORPID + ", CORPNAME=" + CORPNAME + ", COUNT=" + COUNT + "]";
	}

	public DotTask(String cORPID, String cORPNAME, String cOUNT) {
		super();
		CORPID = cORPID;
		CORPNAME = cORPNAME;
		COUNT = cOUNT;
	}

}
