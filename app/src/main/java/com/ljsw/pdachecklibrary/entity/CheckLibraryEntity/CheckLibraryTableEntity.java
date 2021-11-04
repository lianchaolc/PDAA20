package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/8/25.
 */

public class CheckLibraryTableEntity implements Serializable {

    public CheckLibraryTableEntity() {
    }

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }

    public String getFACENUMBER() {
        return FACENUMBER;
    }

    public void setFACENUMBER(String FACENUMBER) {
        this.FACENUMBER = FACENUMBER;
    }

    public String getCABINETNUMBER() {
        return CABINETNUMBER;
    }

    public void setCABINETNUMBER(String CABINETNUMBER) {
        this.CABINETNUMBER = CABINETNUMBER;
    }

    public CheckLibraryTableEntity(int COUNT, String FACENUMBER, String CABINETNUMBER) {
        this.COUNT = COUNT;
        this.FACENUMBER = FACENUMBER;
        this.CABINETNUMBER = CABINETNUMBER;
    }

    private int  COUNT;
    private String FACENUMBER;//  查库的任务号
    private String CABINETNUMBER;//缺失数量


}
