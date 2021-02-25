package com.sublibrary.ercodescan;

import com.entity.BoxDetail;
import com.entity.WebParameter;
import com.service.WebService;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Administrator on 2020/12/23.
 *
 *二维码扫描的服务类代码2021.1.8 以前
 *
 */

public class SubLibraryScanTwoServer {

    /***
     *
     * @param userid
     * @param coderesult
     * @throws Exception
     */
    public String GetTwoInfoSum(String userid, String coderesult ) throws Exception {
        String methodName = "selectTwoCode";
        WebParameter[] param = {
                new WebParameter<String>("arg0", userid),
                new WebParameter<String>("arg1", coderesult)
             };
        SoapObject soap = WebService.getSoapObjectZH(methodName, param);
        String code = soap.getProperty("code").toString();
        String params = soap.getProperty("params").toString();
        String  message=soap.getProperty("msg").toString();
        if ("00".equals(code) && !params.equals("anyType{}")) {
            return  params;
        }else{
            return message;
        }


    }
}
