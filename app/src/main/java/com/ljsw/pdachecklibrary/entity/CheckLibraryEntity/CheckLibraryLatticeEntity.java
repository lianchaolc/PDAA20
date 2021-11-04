package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/8/26.
 * 展示格子缺失数量
 * 2021.8.26
 */

public class CheckLibraryLatticeEntity implements Serializable{

    public CheckLibraryLatticeEntity() {
    }

    public CheckLibraryLatticeEntity(String GRIDNUMBER, String COUNT) {
        this.GRIDNUMBER = GRIDNUMBER;
        this.COUNT = COUNT;
    }

    public String getGRIDNUMBER() {
        return GRIDNUMBER;
    }

    public void setGRIDNUMBER(String GRIDNUMBER) {
        this.GRIDNUMBER = GRIDNUMBER;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    private String GRIDNUMBER;//  格子号
    private String COUNT;//缺失数量
}
