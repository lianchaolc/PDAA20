package com.clearadmin.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

public class RecycleCashboxCheckConfirmService {
    /**
     * @param orderNum 判断该订单是否已审核，添加限制，返回00，则通过；返回01则表示已审核，不能再清分
     * @return
     * @throws Exception
     * @author wangmeng
     * @since 2017-7-28
     */
    public String checkAtmPlanState(String orderNum, String cashboxNum) throws Exception {
        String methodName = "checkAtmPlanState";
        WebParameter[] param = {new WebParameter<String>("arg0", orderNum),
                new WebParameter<String>("arg1", cashboxNum),};
        SoapObject soap = WebService.getSoapObject(methodName, param);
        String code = soap.getProperty("code").toString();

        return code;
    }

    // 回收钞箱清点-确定

    /**
     * @param cashboxNum:钞箱编号;
     * @param orderNum:业务单编号;
     * @param balance1:钞箱余额;
     * @param balance2:废钞箱余额;
     * @param userId:用户编号
     * @return
     * @throws Exception
     */
    public boolean recycleCashboxCheckConfirm(String cashboxNum, String orderNum, String balance1, String balance2,
                                              String userId1, String userId2) throws Exception {
        String methodName = "updateAndRecycleCashboxCheckConfirm";
        Log.e("RecycleCashboxCheckConfirmService", "cashboxNum" + cashboxNum);
        Log.e("RecycleCashboxCheckConfirmService", "orderNum" + orderNum);
        Log.e("RecycleCashboxCheckConfirmService", "balance1" + balance1);
        Log.e("RecycleCashboxCheckConfirmService", "balance2+" + balance1);
        Log.e("RecycleCashboxCheckConfirmService", "userId1" + userId2);
        Log.e("RecycleCashboxCheckConfirmService", "userId2" + userId1);
        WebParameter[] param = {new WebParameter<String>("arg0", cashboxNum),
                new WebParameter<String>("arg1", orderNum), new WebParameter<String>("arg2", balance1),
                new WebParameter<String>("arg3", balance2), new WebParameter<String>("arg4", userId1),
                new WebParameter<String>("arg5", userId2),};
        SoapObject soap = WebService.getSoapObject(methodName, param);
        String code = soap.getProperty("code").toString();
        Log.i("回收钞箱清点-确定", soap + "");
        Log.i("code", code);
        if (code.equals("00")) {
            return true;
        }

        return false;

    }
}
