package com.ljsw.tjbankpad.baggingin.activity.entity;

import java.io.Serializable;

/***
 * 
 * @author Administrator
 *
 */
public class UndoPacketList implements Serializable {
	private String PLANNO;
	private String CORPID;
	private String BAGGINGNUM;
	private String STATE;
	private String CLEARTASKNUM;
	private String GROUPID;
	private String ITEMSTATE;
	private String ITEMNAME;
	private String CLEARTASKTYPE;
	private String CUSTOMERNAME;
	private String CVOUN;
	private String LINENUM;
	private int ID;
	private String COMMENT;
	private String PACKETNUM;
	private String USERID;

	public void setPLANNO(String PLANNO) {
		this.PLANNO = PLANNO;
	}

	public String getPLANNO() {
		return PLANNO;
	}

	public void setCORPID(String CORPID) {
		this.CORPID = CORPID;
	}

	public String getCORPID() {
		return CORPID;
	}

	public void setBAGGINGNUM(String BAGGINGNUM) {
		this.BAGGINGNUM = BAGGINGNUM;
	}

	public String getBAGGINGNUM() {
		return BAGGINGNUM;
	}

	public void setSTATE(String STATE) {
		this.STATE = STATE;
	}

	public String getSTATE() {
		return STATE;
	}

	public void setCLEARTASKNUM(String CLEARTASKNUM) {
		this.CLEARTASKNUM = CLEARTASKNUM;
	}

	public String getCLEARTASKNUM() {
		return CLEARTASKNUM;
	}

	public void setGROUPID(String GROUPID) {
		this.GROUPID = GROUPID;
	}

	public String getGROUPID() {
		return GROUPID;
	}

	public void setITEMSTATE(String ITEMSTATE) {
		this.ITEMSTATE = ITEMSTATE;
	}

	public String getITEMSTATE() {
		return ITEMSTATE;
	}

	public void setITEMNAME(String ITEMNAME) {
		this.ITEMNAME = ITEMNAME;
	}

	public String getITEMNAME() {
		return ITEMNAME;
	}

	public void setCLEARTASKTYPE(String CLEARTASKTYPE) {
		this.CLEARTASKTYPE = CLEARTASKTYPE;
	}

	public String getCLEARTASKTYPE() {
		return CLEARTASKTYPE;
	}

	public void setCUSTOMERNAME(String CUSTOMERNAME) {
		this.CUSTOMERNAME = CUSTOMERNAME;
	}

	public String getCUSTOMERNAME() {
		return CUSTOMERNAME;
	}

	public void setCVOUN(String CVOUN) {
		this.CVOUN = CVOUN;
	}

	public String getCVOUN() {
		return CVOUN;
	}

	public void setLINENUM(String LINENUM) {
		this.LINENUM = LINENUM;
	}

	public String getLINENUM() {
		return LINENUM;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public void setCOMMENT(String COMMENT) {
		this.COMMENT = COMMENT;
	}

	public String getCOMMENT() {
		return COMMENT;
	}

	public void setPACKETNUM(String PACKETNUM) {
		this.PACKETNUM = PACKETNUM;
	}

	public String getPACKETNUM() {
		return PACKETNUM;
	}

	public void setUSERID(String USERID) {
		this.USERID = USERID;
	}

	public String getUSERID() {
		return USERID;
	}

}
