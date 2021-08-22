package com.sql.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/12/15.
 * values.put("cashbgcbno", cashbgcbno);
 * values.put("cashbgcbversion", cashbgcbversion);
 * values.put("cashbgcbtype", cashbgcbtype);
 * values.put("cashbgcbstate", cashbgcbstate);
 * values.put("cashbgcbcount", cashbgcbcount);
 * values.put("cashbgcbmoney", cashbgcbmoney);
 * 数据查查询 明细的实体类
 */

public class CashBagCheckEntity implements Serializable {
    public String getCashbgcbno() {
        return cashbgcbno;
    }

    @Override
    public String toString() {
        return "CashBagCheckEntity{" +
                "cashbgcbno='" + cashbgcbno + '\'' +
                ", cashbgcbversion='" + cashbgcbversion + '\'' +
                ", cashbgcbtype='" + cashbgcbtype + '\'' +
                ", cashbgcbstate='" + cashbgcbstate + '\'' +
                ", cashbgcbcount='" + cashbgcbcount + '\'' +
                ", cashbgcbmoney='" + cashbgcbmoney + '\'' +
                '}';
    }

    public void setCashbgcbno(String cashbgcbno) {
        this.cashbgcbno = cashbgcbno;
    }

    public String getCashbgcbversion() {
        return cashbgcbversion;
    }

    public void setCashbgcbversion(String cashbgcbversion) {
        this.cashbgcbversion = cashbgcbversion;
    }

    public String getCashbgcbtype() {
        return cashbgcbtype;
    }

    public void setCashbgcbtype(String cashbgcbtype) {
        this.cashbgcbtype = cashbgcbtype;
    }

    public String getCashbgcbstate() {
        return cashbgcbstate;
    }

    public void setCashbgcbstate(String cashbgcbstate) {
        this.cashbgcbstate = cashbgcbstate;
    }

    public String getCashbgcbcount() {
        return cashbgcbcount;
    }

    public void setCashbgcbcount(String cashbgcbcount) {
        this.cashbgcbcount = cashbgcbcount;
    }

    public String getCashbgcbmoney() {
        return cashbgcbmoney;
    }

    public void setCashbgcbmoney(String cashbgcbmoney) {
        this.cashbgcbmoney = cashbgcbmoney;
    }

    public CashBagCheckEntity() {

    }

    public CashBagCheckEntity(String cashbgcbno, String cashbgcbversion, String cashbgcbtype, String cashbgcbstate, String cashbgcbcount, String cashbgcbmoney) {
        this.cashbgcbno = cashbgcbno;

        this.cashbgcbversion = cashbgcbversion;
        this.cashbgcbtype = cashbgcbtype;
        this.cashbgcbstate = cashbgcbstate;
        this.cashbgcbcount = cashbgcbcount;
        this.cashbgcbmoney = cashbgcbmoney;
    }

    private String cashbgcbno;//  编号
    private String cashbgcbversion;//  版别
    private String cashbgcbtype;//  券别
    private String cashbgcbstate;//  canshu 状态
    private String cashbgcbcount; //  canshu 状态
    private String cashbgcbmoney;//  canshu 状态
}
