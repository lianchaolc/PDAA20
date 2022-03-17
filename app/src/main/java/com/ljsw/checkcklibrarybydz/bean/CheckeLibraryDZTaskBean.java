package com.ljsw.checkcklibrarybydz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2022/2/16.
 * 抵质押品实体类
 */

public class CheckeLibraryDZTaskBean implements Serializable {
    public CheckeLibraryDZTaskBean(String taskNO, String tasktime) {
        TaskNO = taskNO;
        Tasktime = tasktime;
    }

    public String getTaskNO() {
        return TaskNO;
    }

    public void setTaskNO(String taskNO) {
        TaskNO = taskNO;
    }

    public String getTasktime() {
        return Tasktime;
    }

    public void setTasktime(String tasktime) {
        Tasktime = tasktime;
    }

    public CheckeLibraryDZTaskBean() {
    }

    @Override
    public String toString() {
        return "CheckeLibraryDZTaskBean{" +
                "TaskNO='" + TaskNO + '\'' +
                ", Tasktime='" + Tasktime + '\'' +
                '}';
    }

    private String  TaskNO;
    private  String  Tasktime;

}
