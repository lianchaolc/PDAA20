package com.ljsw.collateraladministratorsorting.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/9/17.
 * <p>
 * 获取任务实体类
 * 2021.9.16
 * SelectTaskByCollateralEntity
 * [{"clearTaskNum":"RW0QF0520210916110120","clearTaskType":"0"}]
 */

public class SelectTaskListByCollateralBean implements Serializable {

    public SelectTaskListByCollateralBean(String clearTaskNum, String clearTaskType) {
        this.clearTaskNum = clearTaskNum;
        this.clearTaskType = clearTaskType;
    }

    public SelectTaskListByCollateralBean() {
    }

    @Override
    public String toString() {
        return "SelectTaskListByCollateralBean{" +
                "clearTaskNum='" + clearTaskNum + '\'' +
                ", clearTaskType='" + clearTaskType + '\'' +
                '}';
    }

    public String getClearTaskNum() {
        return clearTaskNum;
    }

    public void setClearTaskNum(String clearTaskNum) {
        this.clearTaskNum = clearTaskNum;
    }

    public String getClearTaskType() {
        return clearTaskType;
    }

    public void setClearTaskType(String clearTaskType) {
        this.clearTaskType = clearTaskType;
    }

    private String clearTaskNum;// 任务号
    private String clearTaskType;//   任务类型
}
