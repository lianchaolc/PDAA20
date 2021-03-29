package com.sublibrary.ercodescan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/12/24.
 * <p>
 * 分库下打印机手动录入钞袋信息代码实体类
 */

public class LibraryCodeScan implements Serializable {

    private String cabinetNumber;

    private String comment;

    private String customerName;

    private String faceNumber;

    private String gridNumber;

    private int id;

    private String inOperatorCode;

    private String inOperatorName;

    public IntoDate getIntoDate() {
        return intoDate;
    }

    public void setIntoDate(IntoDate intoDate) {
        this.intoDate = intoDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    private IntoDate intoDate;

    private String itemCode;

    private String itemName;

    private String itemType;

    private String itemTypeName;

    private String location;

    private String org;

    private String orgName;

    private OutData outDate;
//    private String outDate;
    public OutData getOutDate() {
        return outDate;
    }

    public void setOutDate(OutData outDate) {
        this.outDate = outDate;
    }

    private String outOperatorCode;

    private String outOperatorName;

    private String partitionNumber;

    private String realCabinetNumber;

    private String realFaceNumber;

    private String realGridNumber;

    private String realLocation;

    private String stockCode;

    private String stockState;
    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setFaceNumber(String faceNumber) {
        this.faceNumber = faceNumber;
    }

    public String getFaceNumber() {
        return faceNumber;
    }

    public void setGridNumber(String gridNumber) {
        this.gridNumber = gridNumber;
    }

    public String getGridNumber() {
        return gridNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setInOperatorCode(String inOperatorCode) {
        this.inOperatorCode = inOperatorCode;
    }

    public String getInOperatorCode() {
        return inOperatorCode;
    }

    public void setInOperatorName(String inOperatorName) {
        this.inOperatorName = inOperatorName;
    }

    public String getInOperatorName() {
        return inOperatorName;
    }



    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrg() {
        return org;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

//    public void setOutDate(String outDate) {
//        this.outDate = outDate;
//    }
//
//    public String getOutDate() {
//        return outDate;
//    }

    public void setOutOperatorCode(String outOperatorCode) {
        this.outOperatorCode = outOperatorCode;
    }

    public String getOutOperatorCode() {
        return outOperatorCode;
    }

    public void setOutOperatorName(String outOperatorName) {
        this.outOperatorName = outOperatorName;
    }

    public String getOutOperatorName() {
        return outOperatorName;
    }

    public void setPartitionNumber(String partitionNumber) {
        this.partitionNumber = partitionNumber;
    }

    public String getPartitionNumber() {
        return partitionNumber;
    }

    public void setRealCabinetNumber(String realCabinetNumber) {
        this.realCabinetNumber = realCabinetNumber;
    }

    public String getRealCabinetNumber() {
        return realCabinetNumber;
    }

    public void setRealFaceNumber(String realFaceNumber) {
        this.realFaceNumber = realFaceNumber;
    }

    public String getRealFaceNumber() {
        return realFaceNumber;
    }

    public void setRealGridNumber(String realGridNumber) {
        this.realGridNumber = realGridNumber;
    }

    public String getRealGridNumber() {
        return realGridNumber;
    }

    public void setRealLocation(String realLocation) {
        this.realLocation = realLocation;
    }

    public String getRealLocation() {
        return realLocation;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getStockState() {
        return stockState;
    }

}
