package com.clearadmin.biz;

/**
 * Created by Administrator on 2021/5/11.
 */

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 选ATM 的服务类
 * lianchao
 * 2021.5.11
 */
public class SelectATMByBlackMoneyBoxCleanService {

    /**
     * 返回 ATM列表 方法
     *
     * @param box
     * @return
     * @throws Exception
     *
     * 可以修改url  和参数
     */
    public String ReturnATMSelectList(String Userid, String orginid, String ATMyuliu) throws Exception {
        String methodName = "ReturnATMSelectList";// 以后会改是后台上的某些字段
        Log.i("arg0", Userid);
        Log.i("arg1", orginid);
        Log.i("arg2", ATMyuliu);
        List<String> list = new ArrayList<String>();
        WebParameter[] parameter = {
                new WebParameter<String>("arg0", Userid),
                new WebParameter<String>("arg1", orginid),
                new WebParameter<String>("arg2", ATMyuliu)};
        SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
        Log.i("arg0", soap + "");
        if (soap.getProperty("code").toString().equals("00")) {
            System.out.print("======" + soap.getProperty("params").toString());
            return soap.getProperty("params").toString();
        } else {
            return null;
        }
    }
}
