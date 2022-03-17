package com.moneyboxadmin.biz;

import java.io.Serializable;

/**
 * Created by Administrator on 2022/3/4.
 */

/***
 * 主要网点人员登录时存放网点的信息 之前没有网点人员登录的实体类2022.3.4
 */
public class LoginInfoByNetPont implements Serializable {

    private String loginUserName; // 用户名
    private String organizationName; // 机构名称
    private String organizationId; // 机构id
    private String loginUserId; // 登陆用户Id
    private String yonghuZhanghao;
    private String xiaozu;// 所属小组

    public String getYonghuZhanghao() {
        return yonghuZhanghao;
    }

    public void setYonghuZhanghao(String yonghuZhanghao) {
        this.yonghuZhanghao = yonghuZhanghao;
    }

    public String getXiaozu() {
        return xiaozu;
    }

    public void setXiaozu(String xiaozu) {
        this.xiaozu = xiaozu;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
