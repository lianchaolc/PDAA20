package com.ljsw.checkcklibrarybydz.bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.Serializable;

/**
 * Created by Administrator on 2022/2/17.
 */
//实体类代码扫描  需要扫描的编号和 扫描的状态
public class CheckLibrayDZScannerUpBean implements Serializable {
    public CheckLibrayDZScannerUpBean() {
    }

    public String getScannnerNO() {
        return ScannnerNO;
    }

    public void setScannnerNO(String scannnerNO) {
        ScannnerNO = scannnerNO;
    }

    public String getNoState() {
        return NoState;
    }

    public void setNoState(String noState) {
        NoState = noState;
    }

    public CheckLibrayDZScannerUpBean(String scannnerNO, String noState) {
        ScannnerNO = scannnerNO;
        NoState = noState;

    }

    private String ScannnerNO;
    private String NoState;

}
