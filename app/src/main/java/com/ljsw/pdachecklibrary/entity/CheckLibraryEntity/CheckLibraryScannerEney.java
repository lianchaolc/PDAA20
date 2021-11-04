package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.io.Serializable;

/**
 *
 * 抵质押品获取扫描标签数据
 * Created by Administrator on 2021/9/6.
 */

public class CheckLibraryScannerEney  implements Serializable{
    public CheckLibraryScannerEney(String PARTITIONNUMBER, String STOCKCODE) {
        this.PARTITIONNUMBER = PARTITIONNUMBER;
        this.STOCKCODE = STOCKCODE;
    }

    public String getPARTITIONNUMBER() {
        return PARTITIONNUMBER;
    }

    public void setPARTITIONNUMBER(String PARTITIONNUMBER) {
        this.PARTITIONNUMBER = PARTITIONNUMBER;
    }

    public String getSTOCKCODE() {
        return STOCKCODE;
    }

    public void setSTOCKCODE(String STOCKCODE) {
        this.STOCKCODE = STOCKCODE;
    }



    public CheckLibraryScannerEney() {
    }
    private String PARTITIONNUMBER ;
    private  String   STOCKCODE;
}
