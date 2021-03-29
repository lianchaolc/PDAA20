package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2021/2/5.
 *
 * 清分管理复核的实体类
 * 陈晨给的
 */

public class ClearList implements Serializable {
    private static final long serialVersionUID = -6586479692185825283L;

    private String cvoun;

    private String tckt;

    private String flag;

    private String flagName;

    private String num;

    private BigDecimal amt;

    private String strAmt;

    private BigDecimal amtb;

    private String strAmtb;

    private String cshcd;

    private String dtlType;

    private String dtlName;

    private BigDecimal dtlNums;

    private BigDecimal dtlStartNo;

    private BigDecimal dtlEndNo;

    private String numId;

    private String clearResult;

    private String clearResultName;

    private String orderType;

    private String psNum;

    private String passBoxNum;

    private String bundleNum;

    private Date stateDate;

    public String getCvoun() {
        return cvoun;
    }

    public void setCvoun(String cvoun) {
        this.cvoun = cvoun;
    }

    public String getTckt() {
        return tckt;
    }

    public void setTckt(String tckt) {
        this.tckt = tckt;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
        if("0".equals(flag))
            this.flagName = "完整券";
        else if("1".equals(flag))
            this.flagName = "全损券";
        else if("2".equals(flag))
            this.flagName = "半损券";
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
        this.strAmt = this.formatMoney(amt);
    }

    public String getStrAmt() {
        return strAmt;
    }

    public void setStrAmt(String strAmt) {
        this.strAmt = strAmt;
    }

    public BigDecimal getAmtb() {
        return amtb;
    }

    public void setAmtb(BigDecimal amtb) {
        this.amtb = amtb;
        this.strAmtb = this.formatMoney(amtb);
    }

    public String getDtlName() {
        return dtlName;
    }

    public void setDtlName(String dtlName) {
        this.dtlName = dtlName;
    }

    public String getStrAmtb() {
        return strAmtb;
    }

    public void setStrAmtb(String strAmtb) {
        this.strAmtb = strAmtb;
    }

    public String getCshcd() {
        return cshcd;
    }

    public void setCshcd(String cshcd) {
        this.cshcd = cshcd;
    }

    public String getDtlType() {
        return dtlType;
    }

    public void setDtlType(String dtlType) {
        this.dtlType = dtlType;
    }

    public BigDecimal getDtlNums() {
        return dtlNums;
    }

    public void setDtlNums(BigDecimal dtlNums) {
        this.dtlNums = dtlNums;
    }

    public BigDecimal getDtlStartNo() {
        return dtlStartNo;
    }

    public void setDtlStartNo(BigDecimal dtlStartNo) {
        this.dtlStartNo = dtlStartNo;
    }

    public BigDecimal getDtlEndNo() {
        return dtlEndNo;
    }

    public void setDtlEndNo(BigDecimal dtlEndNo) {
        this.dtlEndNo = dtlEndNo;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public String getClearResult() {
        return clearResult;
    }

    public void setClearResult(String clearResult) {
        this.clearResult = clearResult;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {

        this.orderType = orderType;
    }

    public String getPsNum() {
        return psNum;
    }

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getPassBoxNum() {
        return passBoxNum;
    }

    public void setPassBoxNum(String passBoxNum) {
        this.passBoxNum = passBoxNum;
    }

    public String getBundleNum() {
        return bundleNum;
    }

    public void setBundleNum(String bundleNum) {
        this.bundleNum = bundleNum;
    }


    public String getClearResultName() {
        return clearResultName;
    }

    public void setClearResultName(String clearResultName) {
        this.clearResultName = clearResultName;
    }


    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    /**
     * 格式化金额
     * @return
     */
    public String formatMoney(BigDecimal bibd) {
        int len = 2;
        if (bibd == null) {
            return "";
        } else {
            NumberFormat formater = null;
            double num = Double.parseDouble(bibd.toString());
            if (len == 0) {
                formater = new DecimalFormat("###,###");

            } else {
                StringBuffer buff = new StringBuffer();
                buff.append("###,###.");
                for (int i = 0; i < len; i++) {
                    buff.append("#");
                }
                formater = new DecimalFormat(buff.toString());
            }
            String result = formater.format(num);
            if (result.indexOf(".") == -1) {
                result = result + ".00";
            }
            return result;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }else{
            if(obj instanceof ClearList){
                ClearList vo = (ClearList) obj;
                if("1".equals(vo.getOrderType())){
                    return vo.getPsNum().equals(this.getPsNum()) &&
                            vo.getTckt().equals(this.getTckt()) &&
                            //20200106 新需求
                            // 		上缴清分提交不再限制残损类型，只核对券别和金额
                            //		上缴清分对账不再限制残损类型，只核对券别和金额
                            // 录入现金订单明细、现金订单清分都不区别残损状态，都默认为完整券
//								vo.getFlag().equals(this.getFlag()) &&
                            vo.getAmt().equals(this.getAmt()) &&
                            vo.getNum().equals(this.getNum()) ;

                }else if("2".equals(vo.getOrderType())){
                    if(vo.getPsNum().equals(this.getPsNum()) &&
                            vo.getDtlType().equals(this.getDtlType()) &&
                            vo.getDtlNums().equals(this.getDtlNums()) &&
                            vo.getDtlStartNo().equals(this.getDtlStartNo()) &&
                            vo.getDtlEndNo().equals(this.getDtlEndNo()))
                        return true;
                    else
                        return false;
                }else if("3".equals(vo.getOrderType())){
                    if(vo.getPsNum().equals(this.getPsNum()) &&
                            vo.getNumId().equals(this.getNumId()))
                        return true;
                    else
                        return false;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

}
