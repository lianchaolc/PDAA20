package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.util.List;

/**
 * Created by Administrator on 2022/3/28.
 */

public class CheckLibraryScannerBean {
    private int UNSCANNED;

    private int SCANNED;

    private int TOTAL;

    private List<CheckLibraryScannerEney> list;

    public void setUNSCANNED(int UNSCANNED){
        this.UNSCANNED = UNSCANNED;
    }
    public int getUNSCANNED(){
        return this.UNSCANNED;
    }
    public void setSCANNED(int SCANNED){
        this.SCANNED = SCANNED;
    }
    public int getSCANNED(){
        return this.SCANNED;
    }
    public void setTOTAL(int TOTAL){
        this.TOTAL = TOTAL;
    }
    public int getTOTAL(){
        return this.TOTAL;
    }
    public void setList(List<CheckLibraryScannerEney> list){
        this.list = list;
    }
    public List<CheckLibraryScannerEney> getList(){
        return this.list;
    }

}
