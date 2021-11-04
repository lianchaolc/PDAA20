package com.ljsw.pdachecklibrary.entity.CheckLibraryEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2021/8/25.
 */
//档案柜查库任务补扫列表
public class CheckLibraryEntity implements Serializable {

    public CheckLibraryEntity() {
    }

    public CheckLibraryEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMissingNum() {
        return missingNum;
    }

    public void setMissingNum(String missingNum) {
        this.missingNum = missingNum;
    }

    public String  getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public CheckLibraryEntity(String id, String missingNum, String  starttime) {
        this.id = id;
        this.missingNum = missingNum;
        this.starttime = starttime;
    }

    private String id;//  查库的任务号
    private String missingNum;//缺失数量
    private String  starttime;// 创建查库时间

}
