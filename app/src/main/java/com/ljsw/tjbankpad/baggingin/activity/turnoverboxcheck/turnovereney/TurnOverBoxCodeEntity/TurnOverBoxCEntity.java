package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity;

import java.util.List;

/**
 * Created by Administrator on 2021/2/5.
 */

public class TurnOverBoxCEntity {
    private String psNum;

    private String psType;

    private String org;

    private String tonod;

    private String state;

    private String stateDate;

    private String orgName;

    private String toNodName;

    private List<OrderList> orderList;

    private List<ClearList> clearList;

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getPsNum() {
        return this.psNum;
    }

    public void setPsType(String psType) {
        this.psType = psType;
    }

    public String getPsType() {
        return this.psType;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrg() {
        return this.org;
    }

    public void setTonod(String tonod) {
        this.tonod = tonod;
    }

    public String getTonod() {
        return this.tonod;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setStateDate(String stateDate) {
        this.stateDate = stateDate;
    }

    public String getStateDate() {
        return this.stateDate;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setToNodName(String toNodName) {
        this.toNodName = toNodName;
    }

    public String getToNodName() {
        return this.toNodName;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

    public List<OrderList> getOrderList() {
        return this.orderList;
    }

    public void setClearList(List<ClearList> clearList) {
        this.clearList = clearList;
    }

    public List<ClearList> getClearList() {
        return this.clearList;
    }
}
