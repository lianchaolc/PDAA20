package com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 入账户中的请求数据获取号的集合 实体类
 * 
 * @author Administrator 2018_11_8
 */
@SuppressWarnings("serial")
public class GetAccountTurnOverLineListEntity implements Serializable {

//	private  String  LINENAME;//   银行下的线路
////	private String count ; // 数量
//	private List<String>  detailList;// 周转箱的集合
//	private String PSLINE;// 标识某个线路
////    private String LINENAME;

	private String LINENAME;
	private String count;
	private List<String> detailList;
	private String PSLINE;

	public GetAccountTurnOverLineListEntity(String lINENAME, List<String> detailList, String pSLINE, String count) {
		super();
		LINENAME = lINENAME;
		this.detailList = detailList;
		PSLINE = pSLINE;
		this.count = count;
	}

	@Override
	public String toString() {
		return "GetAccountTurnOverLineListEntity [LINENAME=" + LINENAME + ", detailList=" + detailList + ", PSLINE="
				+ PSLINE + ", count=" + count + "]";
	}

//    private List<String> detailList;
//    private String PSLINE;
	public GetAccountTurnOverLineListEntity() {
		super();

	}

	public String getLINENAME() {
		return LINENAME;
	}

	public void setLINENAME(String lINENAME) {
		LINENAME = lINENAME;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<String> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<String> detailList) {
		this.detailList = detailList;
	}

	public String getPSLINE() {
		return PSLINE;
	}

	public void setPSLINE(String pSLINE) {
		PSLINE = pSLINE;
	}

}
