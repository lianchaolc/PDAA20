package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity;

import java.util.Date;
import java.util.List;

/**
 * @author success
 * @date 2021-02-03 17:18
 */
public class LineBoxVo {
    private String psNum; // 配送单号
    private String psType; // 配送单类型
    private String org; // 申请机构编号
    private String tonod; // 对方机构编号
    private String state; // 配送单状态
    private Date stateDate; // 状态日期
    private String linenum; // 线路编号
    private String psline; // 配送单线路
    private String orgName; // 申请网点名称
    private String toNodName; // 对方网点名称
    private String beginOrg; // 起止申请机构号


    private List<ClearingCheckVO> orderList;

    private List<ClearingCheckVO> clearList;


    public String getPsNum() {
        return psNum;
    }

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getPsType() {
        return psType;
    }

    public void setPsType(String psType) {
        this.psType = psType;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getTonod() {
        return tonod;
    }

    public void setTonod(String tonod) {
        this.tonod = tonod;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public String getLinenum() {
        return linenum;
    }

    public void setLinenum(String linenum) {
        this.linenum = linenum;
    }

    public String getPsline() {
        return psline;
    }

    public void setPsline(String psline) {
        this.psline = psline;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getToNodName() {
        return toNodName;
    }

    public void setToNodName(String toNodName) {
        this.toNodName = toNodName;
    }

    public String getBeginOrg() {
        return beginOrg;
    }

    public void setBeginOrg(String beginOrg) {
        this.beginOrg = beginOrg;
    }

    public List<ClearingCheckVO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<ClearingCheckVO> orderList) {
        this.orderList = orderList;
    }

    public List<ClearingCheckVO> getClearList() {
        return clearList;
    }

    public void setClearList(List<ClearingCheckVO> clearList) {
        this.clearList = clearList;
    }

}
