package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/2/5.
 */

public class OrderList  implements Serializable{
    private String cvoun;

    private String numId;

    private String orderType;

    private String psNum;

    public void setCvoun(String cvoun){
        this.cvoun = cvoun;
    }
    public String getCvoun(){
        return this.cvoun;
    }
    public void setNumId(String numId){
        this.numId = numId;
    }
    public String getNumId(){
        return this.numId;
    }
    public void setOrderType(String orderType){
        this.orderType = orderType;
    }
    public String getOrderType(){
        return this.orderType;
    }
    public void setPsNum(String psNum){
        this.psNum = psNum;
    }
    public String getPsNum(){
        return this.psNum;
    }

}
