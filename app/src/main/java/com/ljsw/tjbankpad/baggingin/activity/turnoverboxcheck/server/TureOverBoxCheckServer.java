package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.server;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;
import com.service.WebService;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Administrator on 2021/2/1.
 * 清分管理获取明细
 */

public class TureOverBoxCheckServer {

    /***
     * 获取线路列表
     *
     * @return
     * @throws Exception
     *
     *                   201910.25 日 制卡接口修改 cd/queryUnHandleCdnos
     */
    public String getLineList(String Canshutype  ,String userid) throws Exception {
        String methodName = "getLineByBoxList";// 接口方法Info
        Log.i("arg0",  Canshutype);
        Log.i("arg1",userid);
        WebParameter[] param = {
                new WebParameter<String>("arg0", Canshutype ),
                new WebParameter<String>("arg1",userid)
        };// 传入参数
        SoapObject soap = null;// 创建返回值接收对象
        soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url18);// 根据路径获得返回值
        String code = soap.getProperty("code").toString().trim();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
        if ("00".equals(code)) {// code=00->成功,code=99->失败
            return params;
        } else {
            return null;
        }
    }

    /***
     * x
     * @param linNO
     * @return
     * @throws Exception
     * @param selectType 1,2,3
     */
    public String getListinfo(String linNO, String selectType) throws Exception {
        String methodName = "selectTurnoverBoxDetails";// 接口方法Info
        Log.i("方法名称---",  "selectTurnoverBoxDetails");
        Log.i("arg0", linNO);
        Log.i("arg1", selectType);
        WebParameter[] param = {
                new WebParameter<String>("arg0", linNO),
                new WebParameter<String>("arg1", selectType)
        };// 传入参数
        SoapObject soap = null;// 创建返回值接收对象
        soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url18);// 根据路径获得返回值
        String code = soap.getProperty("code").toString().trim();
        String msg = soap.getProperty("msg").toString();
        String params = soap.getProperty("params").toString();
        System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
        if ("00".equals(code)) {// code=00->成功,code=99->失败
            return params;
        } else {
            return null;
        }
    }

    /**
     * 提交操作人和线路
     * @param UserZhaohao
     * @param LinNO
     * @return
     * @throws Exception
     * clearingCheck
     */
    public String clearingCheck(String UserZhaohao, String LinNO) throws Exception {
        String methodName = "clearingCheck";// 接口方法
        Log.i("arg0", UserZhaohao);
        Log.i("arg1", LinNO);
        @SuppressWarnings("rawtypes")
        WebParameter[] parameter = {new WebParameter<String>("arg0", UserZhaohao),
                new WebParameter<String>("arg1", LinNO)};// 传入参数
        SoapObject soap = null;// 创建返回值接收对象
        soap = WebServiceFromThree.getSoapObject(methodName, parameter, FixationValue.NAMESPACEZH, FixationValue.url18);
        if (soap.getProperty("code").toString().equals("00")) {
            System.out.print("======" + soap.getProperty("params").toString());
            return soap.getProperty("msg").toString();
        } else {
            return soap.getProperty("msg").toString();
        }
    }
}
