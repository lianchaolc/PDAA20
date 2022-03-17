package com.ljsw.checkcklibrarybydz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2022/2/16.
 * 创建实体类
 */

public class CheckLibraryFaceBean implements Serializable {
    public CheckLibraryFaceBean(String tableNo, String lossCounts) {
        TableNo = tableNo;
        LossCounts = lossCounts;
    }

    private String TableNo;

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getLossCounts() {
        return LossCounts;
    }

    public void setLossCounts(String lossCounts) {
        LossCounts = lossCounts;
    }

    public CheckLibraryFaceBean() {
    }

    private String LossCounts;

}
