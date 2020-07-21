package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import java.io.Serializable;

/***
 * 抵质押品上缴装箱的实体类
 * 
 * @author Administrator lc 2018-11-23
 */
public class ScanZZandLockNum implements Serializable {
	private String boxnum;/// 周转箱
	private String bundleNum;// 一次性锁号码

	@Override
	public String toString() {
		return "ScanZZandLockNum [" + (boxnum != null ? "boxnum=" + boxnum + ", " : "")
				+ (bundleNum != null ? "bundleNum=" + bundleNum : "") + "]";
	}

	public ScanZZandLockNum() {
		super();
	}

	public String getBoxnum() {
		return boxnum;
	}

	public void setBoxnum(String boxnum) {
		this.boxnum = boxnum;
	}

	public String getBundleNum() {
		return bundleNum;
	}

	public void setBundleNum(String bundleNum) {
		this.bundleNum = bundleNum;
	}

}
