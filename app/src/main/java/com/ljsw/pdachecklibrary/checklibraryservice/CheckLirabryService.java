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

        String methodName = "listMissingCollateralCheckStock";

        Log.e(TAG, "方法名称 :" + "listMissingCollateralCheckStock");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + statrTime);
        Log.i(TAG, "参数3" + endTime);
        WebParameter[] param = {
                new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", statrTime),
                new WebParameter<String>("arg2", endTime)};
        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        if (code.equals("00")) {
            System.out.println("返回 : " + msg);
            System.out.println("返回 : " + params);
            return params;
        }
        return null;
    }
    /***1
     *  通过时间查询查库任务降序方式
     * @return
     * @throws Exception
     */
    public String getChceckLibraryNumbyPC(String ControllerUser, String statrTime, String endTime,String canshu) throws Exception {

        String methodName = "listMissingCollateralCheckStock";

        Log.e(TAG, "方法名称 :" + "listMissingCollateralCheckStock");
        Log.i(TAG, "参数1" + ControllerUser);
        Log.i(TAG, "参数2" + statrTime);
        Log.i(TAG, "参数3" + endTime);
        Log.i(TAG, "参数4" + canshu);

        WebParameter[] param = {
                new WebParameter<String>("arg0", ControllerUser),
                new WebParameter<String>("arg1", statrTime),
                new WebParameter<String>("arg2", endTime),
                new WebParameter<String>("arg3", canshu)};
        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        if (code.equals("00")) {
            System.out.println("返回 : " + msg);
            System.out.println("返回 : " + params);
            return params;
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
    public String CheckLibrarybyTable( String ChecklibaryraTasknum,String ControllerUser) throws Exception {
        String methodName = "countListMissingCollateralByTask";
        Log.e(TAG, "方法名称 :" + "countListMissingCollateralByTask");
        Log.i(TAG, "参数2==" + ControllerUser);
        Log.i(TAG, "参数1==" + ChecklibaryraTasknum);
        WebParameter[] param = {
                new WebParameter<String>("arg0", ChecklibaryraTasknum),
                new WebParameter<String>("arg1", ControllerUser),
        };
        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        System.out.println("返回 : " + code);
        System.out.println("返回 : " + msg);
        System.out.println("返回 : " + params);
        if (code.equals("00")) {
            System.out.println("返回 : " + params);
            return params;
        }

        return null;
    }


    /*****3
     * 获取时间和数量数量后 拿到每个lattice  格子
     * @param
     * @para
     * @param
     * @return
     * @throws Exception-
     */
    public String CheckLibrarylattice(String ChecklibaryraTasknum, String uplattice,String ControllerUser) throws Exception {
        String methodName = "countListMissingCollateralByTaskAndFaceNumber";
        Log.e(TAG, "方法名称 :" + "countListMissingCollateralByTaskAndFaceNumber");

        Log.i(TAG, "参数1=" + ChecklibaryraTasknum);
        Log.i(TAG, "参数2=" + uplattice);
        Log.i(TAG, "参数3=" + ControllerUser);
        WebParameter[] param = {
                new WebParameter<String>("arg0", ChecklibaryraTasknum),
                new WebParameter<String>("arg1", uplattice),
                new WebParameter<String>("arg2", ControllerUser),
        };
        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();

        if (code.equals("00")) {
            System.out.println("返回 : " + params);
            return params;
        }

        return null;

    }


    /***4
     * 获取隔断数据
     * @param ControllerUser
     * @param upDZNo
     * @return
     * @throws Exception
     *
     * http://361de15631.wicp.vip/cash/webservice/pda/listMissingCollateralByTaskAndFaceGridNumber?arg0=DZ9880500002021041500001&arg1=G1-A&arg2=13&arg3=0001
     */
    public String listMissingCollateralByTaskAndFaceGridNumber( String upDZNo,String latticeNo,String geduan,String ControllerUser) throws Exception {

        String methodName = "listMissingCollateralByTaskAndFaceGridNumber";
        Log.e(TAG, "方法名称 :" + "listMissingCollateralByTaskAndFaceGridNumber");
        Log.i(TAG, "参数2" + upDZNo);
        Log.i(TAG, "参数2" + latticeNo);
        Log.i(TAG, "参数3" + geduan);
        Log.i(TAG, "参数4" + ControllerUser);
        WebParameter[] param = {new WebParameter<String>("arg0", upDZNo),
                new WebParameter<String>("arg1", latticeNo),
                new WebParameter<String>("arg2", geduan),
                new WebParameter<String>("arg3", ControllerUser),
        };
        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        if (code.equals("00")) {
            System.out.println("返回 : " + params);
            return params;
        }

        return null;
    }
    /**
     * 5
     * 提交数据
     */

    public String upChecklibraryScanData(String Taskno, String upDZNo ,String ControllerUser) throws Exception {

        String methodName = "recordMissCollateral";
        Log.e(TAG, "方法名称 :" + "recordMissCollateral");
        Log.d(TAG, "参数0" + Taskno);
        Log.i(TAG, "参数1" + upDZNo);
        Log.i(TAG, "参数2" + ControllerUser);

        WebParameter[] param = {
                new WebParameter<String>("arg0", Taskno),
                new WebParameter<String>("arg1", upDZNo),
                new WebParameter<String>("arg2", ControllerUser),
        };


        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        System.out.println("返回 code: " + code);
        System.out.println("返回 msg: " + msg);
        System.out.println("返回 params: " + params);
        if(code.equals("00")) {
            return code;
        }else{
            return null;
        }
    }
    /***
     * j结束任务需要确定
     */
//    cash/webservicea/finishTask?arg0=DZ9880300002022040700003&arg1=0001
    public String finishTask(String Taskno,String ControllerUser) throws Exception {

        String methodName = "finishTask";
        Log.e(TAG, "方法名称 :" + "finishTask");
        Log.d(TAG, "参数0" + Taskno);
        Log.i(TAG, "参数1" + ControllerUser);

        WebParameter[] param = {
                new WebParameter<String>("arg0", Taskno),
                new WebParameter<String>("arg1", ControllerUser),
        };


        SoapObject soap = null;
        soap = WebService.getSoapObjectZH(methodName, param);
        System.out.println("[soap:----]" + soap);
        String code = soap.getProperty("code").toString();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        System.out.println("返回 code: " + code);
        System.out.println("返回 msg: " + msg);
        System.out.println("返回 params: " + params);
        if(code.equals("00")) {
            return code;
        }else{
            return null;
        }
    }

}
