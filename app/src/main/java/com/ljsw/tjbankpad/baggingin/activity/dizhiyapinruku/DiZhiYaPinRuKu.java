package com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku;

import java.io.Serializable;
import java.util.List;

/***
 * 抵制押品入库实体类
 * 
 * @author Administrator 2018_10_28
 */
public class DiZhiYaPinRuKu implements Serializable {
	private List<String> bagList;
	private int COUNT; /// 箱子数量
	private String CLEARTASKNUM; // 箱子号

	public List<String> getBagList() {
		return bagList;
	}

	public void setBagList(List<String> bagList) {
		this.bagList = bagList;
	}

	public int getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(int cOUNT) {
		COUNT = cOUNT;
	}

	public String getCLEARTASKNUM() {
		return CLEARTASKNUM;
	}

	public void setCLEARTASKNUM(String cLEARTASKNUM) {
		CLEARTASKNUM = cLEARTASKNUM;
	}

	@Override
	public String toString() {
		return "DiZhiYaPinRuKu [bagList=" + bagList + ", COUNT=" + COUNT + ", CLEARTASKNUM=" + CLEARTASKNUM
				+ ", getBagList()=" + getBagList() + ", getCOUNT()=" + getCOUNT() + ", getCLEARTASKNUM()="
				+ getCLEARTASKNUM() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
