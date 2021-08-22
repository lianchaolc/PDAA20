package com.entity;

/**
 * Created by Administrator on 2021/4/25.
 */

import java.io.Serializable;

/***
 * 空钞箱出库数据员实体类代码
 * 2021.4.21
 */
public class BoxInfoByEmply implements Serializable {
    public BoxInfoByEmply() {

    }

    public String getEmplybradname() {
        return emplybradname;
    }

    public void setEmplybradname(String emplybradname) {
        this.emplybradname = emplybradname;
    }

    public String getBrandtype() {
        return brandtype;
    }

    public void setBrandtype(String brandtype) {
        this.brandtype = brandtype;
    }

    public String getBrandcount() {
        return brandcount;
    }

    public void setBrandcount(String brandcount) {
        this.brandcount = brandcount;
    }

    public BoxInfoByEmply(String emplybradname, String brandtype, String brandcount) {

        this.emplybradname = emplybradname;
        this.brandtype = brandtype;
        this.brandcount = brandcount;
    }

    @Override
    public String toString() {
        return "BoxInfoByEmply{" +
                "emplybradname='" + emplybradname + '\'' +
                ", brandtype='" + brandtype + '\'' +
                ", brandcount='" + brandcount + '\'' +
                '}';
    }

    private String emplybradname; // 名称
    private String brandtype; //存款钞箱;1;_##_大源ATM;取款钞箱;1;_##_大源ATM2;取款钞箱
    private String brandcount;  //  钞箱数量

}
