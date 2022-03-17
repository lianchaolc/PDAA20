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

    public String getORGNUM() {
        return ORGNUM;
    }

    public void setORGNUM(String ORGNUM) {
        this.ORGNUM = ORGNUM;
    }

    public String getLINENAME() {
        return LINENAME;
    }

    public void setLINENAME(String LINENAME) {
        this.LINENAME = LINENAME;
    }

    public String getLINENUM() {
        return LINENUM;
    }

    public void setLINENUM(String LINENUM) {
        this.LINENUM = LINENUM;
    }

    private String ORGNUM;
    private String LINENAME;;

    private String LINENUM;

    public CleanMangerCheckTaskQingfenEntity(String lineName, String lineNum, String boxCount, String orgCount, String ORGNUM, String LINENAME, String LINENUM) {
        this.lineName = lineName;
        this.lineNum = lineNum;
        this.boxCount = boxCount;
        this.orgCount = orgCount;
        this.ORGNUM = ORGNUM;
        this.LINENAME = LINENAME;
        this.LINENUM = LINENUM;
    }
    //    [{"ORGNUM":1,"LINENAME":"西青六线","LINENUM":"0029"}]
}
