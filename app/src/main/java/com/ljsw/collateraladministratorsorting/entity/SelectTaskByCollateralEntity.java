package com.ljsw.collateraladministratorsorting.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/8/31.
 *
 * 抵质押品清分的任务实体类
 */

public class SelectTaskByCollateralEntity implements Serializable {

    public SelectTaskByCollateralEntity(String id, String name, String wangdianCount, String zhouzhuanxiangCount) {
        this.id = id;
        this.name = name;
        this.wangdianCount = wangdianCount;
        this.zhouzhuanxiangCount = zhouzhuanxiangCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWangdianCount() {
        return wangdianCount;
    }

    public void setWangdianCount(String wangdianCount) {
        this.wangdianCount = wangdianCount;
    }

    public String getZhouzhuanxiangCount() {
        return zhouzhuanxiangCount;
    }

    public void setZhouzhuanxiangCount(String zhouzhuanxiangCount) {
        this.zhouzhuanxiangCount = zhouzhuanxiangCount;
    }

    public SelectTaskByCollateralEntity() {
    }

    private String id;// 支行id
    private String name;// 支行名称
    private String wangdianCount;// 网点数量
    private String zhouzhuanxiangCount;// 周转箱数量
}
