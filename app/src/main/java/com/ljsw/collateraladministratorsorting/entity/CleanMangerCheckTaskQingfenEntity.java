package com.ljsw.collateraladministratorsorting.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/9/18.
 * [{"lineName":"津南四线","lineNum":"0020","boxCount":3,"orgCount":2}];
 * lianc
 */

public class CleanMangerCheckTaskQingfenEntity implements Serializable {
    public CleanMangerCheckTaskQingfenEntity(String lineName, String lineNum, String boxCount, String orgCount) {
        this.lineName = lineName;
        this.lineNum = lineNum;
        this.boxCount = boxCount;
        this.orgCount = orgCount;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(String boxCount) {
        this.boxCount = boxCount;
    }

    public String getOrgCount() {
        return orgCount;
    }

    public void setOrgCount(String orgCount) {
        this.orgCount = orgCount;
    }

    public CleanMangerCheckTaskQingfenEntity() {
    }

    private  String lineName;
    private  String lineNum;
    private  String boxCount;
    private  String orgCount;
}
