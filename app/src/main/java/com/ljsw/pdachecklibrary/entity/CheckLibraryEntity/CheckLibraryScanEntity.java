package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/8/26.
 档案柜扫描

 */


public class CheckLibraryScanEntity implements Serializable {
    public CheckLibraryScanEntity() {
    }

    public CheckLibraryScanEntity(String DZNo, String DZState, String DZPOin) {
        this.DZNo = DZNo;
        this.DZState = DZState;
        this.DZPOin = DZPOin;
    }

    private String DZNo;

    public String getDZNo() {
        return DZNo;
    }

    public void setDZNo(String DZNo) {
        this.DZNo = DZNo;
    }

    public String getDZState() {
        return DZState;
    }

    public void setDZState(String DZState) {
        this.DZState = DZState;
    }

    public String getDZPOin() {
        return DZPOin;
    }

    public void setDZPOin(String DZPOin) {
        this.DZPOin = DZPOin;
    }

    private String DZState;
    private String DZPOin;//  隔断
}
