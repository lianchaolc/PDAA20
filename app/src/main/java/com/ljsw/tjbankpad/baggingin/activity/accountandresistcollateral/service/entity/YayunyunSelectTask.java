package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;
import java.sql.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class YayunyunSelectTask implements Serializable {

	private String LINENAME;
	private String LINENUM;
	private String CORPNAME;
	private String APPTYPE;
	private String TYPE;
	private String APPNUM;

	public String getLINENAME() {
		return LINENAME;
	}

	public void setLINENAME(String lINENAME) {
		LINENAME = lINENAME;
	}

	public String getLINENUM() {
		return LINENUM;
	}

	public void setLINENUM(String lINENUM) {
		LINENUM = lINENUM;
	}

	public String getCORPNAME() {
		return CORPNAME;
	}

	public void setCORPNAME(String cORPNAME) {
		CORPNAME = cORPNAME;
	}

	public String getAPPTYPE() {
		return APPTYPE;
	}

	public void setAPPTYPE(String aPPTYPE) {
		APPTYPE = aPPTYPE;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getAPPNUM() {
		return APPNUM;
	}

	public void setAPPNUM(String aPPNUM) {
		APPNUM = aPPNUM;
	}

	@Override
	public String toString() {
		return "YayunyunSelectTask [" + (LINENAME != null ? "LINENAME=" + LINENAME + ", " : "")
				+ (LINENUM != null ? "LINENUM=" + LINENUM + ", " : "")
				+ (CORPNAME != null ? "CORPNAME=" + CORPNAME + ", " : "")
				+ (APPTYPE != null ? "APPTYPE=" + APPTYPE + ", " : "") + (TYPE != null ? "TYPE=" + TYPE + ", " : "")
				+ (APPNUM != null ? "APPNUM=" + APPNUM : "") + "]";
	}

}
