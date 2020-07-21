package com.example.pda;

import java.io.Serializable;

/***
 * 尾零接受数据的实体类
 * 
 * @author Administrator ckc/getMoneyEditionList
 *         [{"MONEYTYPE":"纸100元（5套）","MEID":"01","EDITION":2015},
 *         {"MONEYTYPE":"纸100元（5套）","MEID":"02","EDITION":2005}
 *         ,{"MONEYTYPE":"纸100元（5套）","MEID":"03","EDITION":1999} 生成的袋好标签
 *         {"MONEYTYPE":"纸100元（5套）","MEID":"01","PARVALUE":100.00,"EDITION":2015}
 *         20191022 lc
 * 
 *         {"MONEYTYPE":"纸100元（5套）","MEID":"01","PARVALUE":100.00,"LOSSVALUE":50.00,"EDITION":2015},{"MONEYTYPE":"纸100元（5套）","MEID":"02","PARVALUE":100.00,"LOSSVALUE":50.00,"EDITION":2005},{"MONEYTYPE":"纸100元（5套）","MEID":"03","PARVALUE":100.00,"LOSSVALUE":50.00,"EDITION":1999},{"MONEYTYPE":"纸50元（5套）","MEID":"04","PARVALUE":50.00,"LOSSVALUE":25.00,"EDITION":2005},{"MONEYTYPE":"纸50元（5套）","MEID":"05","PARVALUE":50.00,"LOSSVALUE":25.00,"EDITION":1999},{"MONEYTYPE":"纸20元（5套）","MEID":"06","PARVALUE":20.00,"LOSSVALUE":10.00,"EDITION":2005},{"MONEYTYPE":"纸20元（5套）","MEID":"07","PARVALUE":20.00,"LOSSVALUE":10.00,"EDITION":1999},{"MONEYTYPE":"纸10元（5套）","MEID":"08","PARVALUE":10.00,"LOSSVALUE":5.00,"EDITION":2005},{"MONEYTYPE":"纸10元（5套）","MEID":"09","PARVALUE":10.00,"LOSSVALUE":5.00,"EDITION":1999},{"MONEYTYPE":"纸5元（5套）","MEID":"0A","PARVALUE":5.00,"LOSSVALUE":2.50,"EDITION":2005},{"MONEYTYPE":"纸5元（5套）","MEID":"0B","PARVALUE":5.00,"LOSSVALUE":2.50,"EDITION":1999},{"MONEYTYPE":"纸1元（5套）","MEID":"0C","PARVALUE":1.00,"LOSSVALUE":0.50,"EDITION":1999},{"MONEYTYPE":"纸100元（4套）","MEID":"0D","PARVALUE":100.00,"LOSSVALUE":50.00,"EDITION":1990},{"MONEYTYPE":"纸100元（4套）","MEID":"0E","PARVALUE":100.00,"LOSSVALUE":50.00,"EDITION":1980},{"MONEYTYPE":"纸50元（4套）","MEID":"0F","PARVALUE":50.00,"LOSSVALUE":25.00,"EDITION":1990},{"MONEYTYPE":"纸50元（4套）","MEID":"10","PARVALUE":50.00,"LOSSVALUE":25.00,"EDITION":1980},{"MONEYTYPE":"纸10元（4套）","MEID":"11","PARVALUE":10.00,"LOSSVALUE":5.00,"EDITION":1980},{"MONEYTYPE":"纸5元（4套）","MEID":"12","PARVALUE":5.00,"LOSSVALUE":2.50,"EDITION":1980},{"MONEYTYPE":"纸2元（4套）","MEID":"13","PARVALUE":2.00,"LOSSVALUE":1.00,"EDITION":1990},{"MONEYTYPE":"纸2元（4套）","MEID":"14","PARVALUE":2.00,"LOSSVALUE":1.00,"EDITION":1980},{"MONEYTYPE":"纸1元（4套）","MEID":"16","PARVALUE":1.00,"LOSSVALUE":0.50,"EDITION":1996},{"MONEYTYPE":"纸1元（4套）","MEID":"17","PARVALUE":1.00,"LOSSVALUE":0.50,"EDITION":1990},{"MONEYTYPE":"纸1元（4套）","MEID":"15","PARVALUE":1.00,"LOSSVALUE":0.50,"EDITION":1980},{"MONEYTYPE":"纸5角（4套）","MEID":"18","PARVALUE":0.50,"LOSSVALUE":0.25,"EDITION":1980},{"MONEYTYPE":"纸2角（4套）","MEID":"19","PARVALUE":0.20,"LOSSVALUE":0.10,"EDITION":1980},{"MONEYTYPE":"纸1角（4套）","MEID":"1A","PARVALUE":0.10,"LOSSVALUE":0.05,"EDITION":1980},{"MONEYTYPE":"硬五角（5套）","MEID":"1B","PARVALUE":0.50,"LOSSVALUE":0.25,"EDITION":0},{"MONEYTYPE":"硬一角（5套）","MEID":"1C","PARVALUE":0.10,"LOSSVALUE":0.05,"EDITION":0}]
 *         201910.23
 * 
 *         10.29 改 "MONEYTYPE": "纸100元（5套）", "BAGMONEY": 2000000.00, "MEID":
 *         "01", "PARVALUE": 100.00, "LOSSVALUE": 50.00, "EDITION": 2015
 *
 * 
 */

public class TailZerotoEntity implements Serializable {

	public String getBAGMONEY() {
		return BAGMONEY;
	}

	public void setBAGMONEY(String bAGMONEY) {
		BAGMONEY = bAGMONEY;
	}

	private String MONEYTYPE; // 纸币名称
	private String MEID; // 办别的id
	private String PARVALUE; /// 面值的金额
	private String EDITION; // 版别年份
	private String LOSSVALUE;// 半残值
	private String BAGMONEY;// 总的钱数

	public String getLOSSVALUE() {
		return LOSSVALUE;
	}

	public void setLOSSVALUE(String lOSSVALUE) {
		LOSSVALUE = lOSSVALUE;
	}

	public String getMONEYTYPE() {
		return MONEYTYPE;
	}

	public void setMONEYTYPE(String mONEYTYPE) {
		MONEYTYPE = mONEYTYPE;
	}

	public String getMEID() {
		return MEID;
	}

	public void setMEID(String mEID) {
		MEID = mEID;
	}

	public String getPARVALUE() {
		return PARVALUE;
	}

	public void setPARVALUE(String pARVALUE) {
		PARVALUE = pARVALUE;
	}

	public String getEDITION() {
		return EDITION;
	}

	public void setEDITION(String eDITION) {
		EDITION = eDITION;
	}

	@Override
	public String toString() {
		return "TailZerotoEntity [" + (MONEYTYPE != null ? "MONEYTYPE=" + MONEYTYPE + ", " : "")
				+ (MEID != null ? "MEID=" + MEID + ", " : "") + (PARVALUE != null ? "PARVALUE=" + PARVALUE + ", " : "")
				+ (EDITION != null ? "EDITION=" + EDITION + ", " : "")
				+ (LOSSVALUE != null ? "LOSSVALUE=" + LOSSVALUE + ", " : "")
				+ (BAGMONEY != null ? "BAGMONEY=" + BAGMONEY : "") + "]";
	}

}
