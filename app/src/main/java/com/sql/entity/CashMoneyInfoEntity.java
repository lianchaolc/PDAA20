package com.sql.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/12/17.
 *
 //    public static String money_no = "money_no";//  字段名称 钞袋编号
 //    public static String money_couts = "money_couts";//  字段名称 数量
 //    public static String money_all = "money_all";//  字段名称 钱数
 */
public class CashMoneyInfoEntity  implements Serializable {

    public String getMoney_no() {
        return money_no;
    }

    public void setMoney_no(String money_no) {
        this.money_no = money_no;
    }

    public String getMoney_couts() {
        return money_couts;
    }

    public void setMoney_couts(String money_couts) {
        this.money_couts = money_couts;
    }

    public String getMoney_all() {
        return money_all;
    }

    public void setMoney_all(String money_all) {
        this.money_all = money_all;
    }

    @Override
    public String toString() {
        return "CashMoneyInfoEntity{" +
                "money_no='" + money_no + '\'' +
                ", money_couts='" + money_couts + '\'' +
                ", money_all='" + money_all + '\'' +
                '}';
    }

    private String  money_no;// id

    public CashMoneyInfoEntity() {
    }

    private String money_couts;// s
    private  String money_all; //

    public CashMoneyInfoEntity(String money_no, String money_couts, String money_all) {
        this.money_no = money_no;
        this.money_couts = money_couts;
        this.money_all = money_all;
    }
}
