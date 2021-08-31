package com.ljsw.pdachecklibrary.checklibraryservice;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Administrator on 2021/8/27.
 * <p>
 * 服务获取数据 2021.8.27
 */

public class CheckLirabryService {

    public static String TAG = "CheckLirabryService";

    /***1
     *  通过时间查询查库任务降序方式
     * @return
     * @throws Exception
     */
    public String getChceckLibraryNum(String ControllerUser, String statrTime, String endTime) throws Exception {

        String methodName = "getChceckLibraryNum";

        Log.e(TAG, "方法名称 :" + "getChceckLibraryNum");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + statrTime);
        Log.i(TAG, "参数3" + endTime);
        WebParameter[] param = {
                new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", statrTime),
                new WebParameter<String>("arg2", endTime)};
        SoapObject soap = null;
        soap = WebService.getSoapObject2(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        // String params = soap.getProperty("params").toString();
        if (code.equals("00")) {
            System.out.println("返回 : " + msg);
            return code;
        }
        return null;
    }


    /*****2
     * 获取时间和数量数量后 拿到每个table  柜子
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
    public String CheckLibrary(String ControllerUser, String ChecklibaryraTasknum) throws Exception {
        String methodName = "saveBoxSendOutEarly";
        Log.e(TAG, "方法名称 :" + "saveBoxSendOutEarly");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + ChecklibaryraTasknum);
        WebParameter[] param = {new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", ChecklibaryraTasknum),
        };


        SoapObject soap = null;
        soap = WebService.getSoapObject2(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        // String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("msg").toString();

        return code + "_" + params;

    }


    /*****3
     * 获取时间和数量数量后 拿到每个lattice  格子
     * @param
     * @para
     * @param
     * @return
     * @throws Exception
     */
    public String CheckLibrarylattice(String ControllerUser, String ChecklibaryraTasknum) throws Exception {
        String methodName = "saveBoxSendOutEarly";
        Log.e(TAG, "方法名称 :" + "saveBoxSendOutEarly");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + ChecklibaryraTasknum);
        WebParameter[] param = {new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", ChecklibaryraTasknum),
        };


        SoapObject soap = null;
        soap = WebService.getSoapObject2(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        // String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("msg").toString();

        return code + "_" + params;

    }

    /**
     * 4
     * 提交数据
     */

    public String upChecklibraryScanData(String ControllerUser, String upDZNo) throws Exception {

        String methodName = "upChecklibraryScanData";
        Log.e(TAG, "方法名称 :" + "upChecklibraryScanData");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + upDZNo);
        WebParameter[] param = {new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", upDZNo),
        };


        SoapObject soap = null;
        soap = WebService.getSoapObject2(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        // String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("msg").toString();

        return code;
    }


}
