package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity;

import java.nio.channels.ClosedSelectorException;
import java.util.List;

/**
 * @author success
 * @date 2020-12-11 11:31
 */
public class ResultVo {
    private String code;

    private String msg;

    private String params;

//    private List<detail> data;
//
//    private List<ResultDetailVo> orderList;

    static class detail {
        private String machineno;

        private String staff;

        public String getMachineno() {
            return machineno;
        }

        public void setMachineno(String machineno) {
            this.machineno = machineno;
        }

        public String getStaff() {
            return staff;
        }

        public void setStaff(String staff) {
            this.staff = staff;
        }
    }

    static class orderArrayList {
        private String tckt;

        public String getTckt() {
            return tckt;
        }

        public void setTckt(String tckt) {
            this.tckt = tckt;
        }
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public List<detail> getData() {
//        return data;
//    }
//
//    public void setData(List<detail> data) {
//        this.data = data;
//    }
//
//    public List<ResultDetailVo> getOrderList() {
//        return orderList;
//    }
//
//    public void setOrderList(List<ResultDetailVo> orderList) {
//        this.orderList = orderList;
//    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
