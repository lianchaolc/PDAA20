package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2021/2/5.
 * 生成的实体类代码 清分管理员获取线路列表和周转箱列表代码
 */
//[{"boxCount":2,"boxNums":[],"clearTime":null,"clearingGroupId":"","clearingGroupName":"","lineName":"北辰支行中心库一线","lineNum":"BC01","nodName":"","orderSum":"","orderType":"3","org":"","orgName":"","passBoxNums":"ZH000179ZZ,ZH000180ZZ","psNum":"","psType":"","result":"","strOrderSum":"","toNod":""},{"boxCount":2,"boxNums":[],"clearTime":null,"clearingGroupId":"","clearingGroupName":"","lineName":"总库到分库二","lineNum":"ZF02","nodName":"","orderSum":"","orderType":"1","org":"","orgName":"","passBoxNums":"ZH000176ZZ,ZH000177ZZ","psNum":"","psType":"","result":"","strOrderSum":"","toNod":""}]
public class TurnNoverBoxEntity implements Serializable {

    public TurnNoverBoxEntity() {
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public List<BoxNums> getBoxNums() {
        return boxNums;
    }

    public void setBoxNums(List<BoxNums> boxNums) {
        this.boxNums = boxNums;
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public String getClearingGroupId() {
        return clearingGroupId;
    }

    public void setClearingGroupId(String clearingGroupId) {
        this.clearingGroupId = clearingGroupId;
    }

    public String getClearingGroupName() {
        return clearingGroupName;
    }

    public void setClearingGroupName(String clearingGroupName) {
        this.clearingGroupName = clearingGroupName;
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

    public String getNodName() {
        return nodName;
    }

    public void setNodName(String nodName) {
        this.nodName = nodName;
    }

    public String getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(String orderSum) {
        this.orderSum = orderSum;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPassBoxNums() {
        return passBoxNums;
    }

    public void setPassBoxNums(String passBoxNums) {
        this.passBoxNums = passBoxNums;
    }

    public String getPsNum() {
        return psNum;
    }

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getPsType() {
        return psType;
    }

    public void setPsType(String psType) {
        this.psType = psType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStrOrderSum() {
        return strOrderSum;
    }

    public void setStrOrderSum(String strOrderSum) {
        this.strOrderSum = strOrderSum;
    }

    public String getToNod() {
        return toNod;
    }

    public void setToNod(String toNod) {
        this.toNod = toNod;
    }

    public TurnNoverBoxEntity(int boxCount, List<BoxNums> boxNums, String clearTime, String clearingGroupId, String clearingGroupName, String lineName, String lineNum, String nodName, String orderSum, String orderType, String org, String orgName, String passBoxNums, String psNum, String psType, String result, String strOrderSum, String toNod) {
        this.boxCount = boxCount;
        this.boxNums = boxNums;
        this.clearTime = clearTime;
        this.clearingGroupId = clearingGroupId;
        this.clearingGroupName = clearingGroupName;
        this.lineName = lineName;
        this.lineNum = lineNum;
        this.nodName = nodName;
        this.orderSum = orderSum;

        this.orderType = orderType;
        this.org = org;
        this.orgName = orgName;
        this.passBoxNums = passBoxNums;
        this.psNum = psNum;
        this.psType = psType;
        this.result = result;
        this.strOrderSum = strOrderSum;
        this.toNod = toNod;
    }

    private int boxCount;

    private List<BoxNums> boxNums ;

    private String clearTime;

    private String clearingGroupId;

    private String clearingGroupName;

    private String lineName;

    private String lineNum;

    private String nodName;

    private String orderSum;

    private String orderType;

    private String org;

    private String orgName;

    private String passBoxNums;

    private String psNum;

    private String psType;

    private String result;

    private String strOrderSum;

    private String toNod;

}
