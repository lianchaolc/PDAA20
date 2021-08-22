package com.moneyboxadmin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.BoxDetail;
import com.entity.BoxInfoByEmply;
import com.entity.WebParameter;
import com.service.WebService;

public class GetemptyCashBoxOutDetailService {
    // 空钞箱出库明细  2021.4.23

    /**
     * @param planNum
     * @return
     * @throws Exception
     */
    public Map<String, Object> getemptyCashBoxOutDetail(String planNum) throws Exception {

        String methodName = "getemptyCashBoxOutDetail";
        Log.i("planNum", planNum);
        WebParameter[] parameter = {new WebParameter<String>("arg0", planNum),};
        Log.i("a1111", "11111");
        SoapObject soapObject = WebService.getSoapObject(methodName, parameter);
        Log.i("a222", "2222");
        String state = soapObject.getProperty("msg").toString();
        Log.i("a33333", "33333");
        String params = soapObject.getPropertyAsString("params");
        Log.i("a4444444", "444444444");
        List<BoxDetail> list = null;
        Log.i("a5555555555", "55555555");
        Log.i("soapObject", soapObject + "");
        Log.i("params", params);
        Log.i("state", state);

        String cqEupCount = "";
        if ("成功".equals(state) && !params.equals("anyType{}")) {
            /**
             * MODIFY BY WANGMeng,添加查询该计划下有多少个存取一体机 2017-8-8
             */
            String[] array = params.split("wm");
            String listStr = array[0];
            cqEupCount = array[1];// 该计划下有多少个存取一体机

            String[] listArr = listStr.split("\r\n");
            list = new ArrayList<BoxDetail>();
            for (int i = 0; i < listArr.length; i++) {
                BoxDetail box = new BoxDetail();
                String[] arr = listArr[i].split(";");
                box.setBrand(arr[0]);
                box.setNum(arr[1]);// 钞箱数量
                String tmpStr = arr[2];
                if (tmpStr != null && tmpStr != "") {
                    box.setAtmType(tmpStr.equals("0") ? "存取一体机" : "取款机");// crstype
                }

                list.add(box);
            }

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("count", cqEupCount);
        return map;
    }

    /***
     *空钞箱出库明细  net请求2021.4.26
     * https://www.cnblogs.com/lanlanren/p/9283377.html
     */
    public Map<String, Object> getemptyCashBoxOutDetailinfo(String planNum) throws Exception {

        String methodName = "getemptyCashBoxOutDetail";
        Log.i("planNum", planNum);
        WebParameter[] parameter = {new WebParameter<String>("arg0", planNum),};
        Log.i("a1111", "11111");
        SoapObject soapObject = WebService.getSoapObject(methodName, parameter);
        Log.i("a222", "2222");
        String state = soapObject.getProperty("msg").toString();
        Log.i("a33333", "33333");
        String params = soapObject.getPropertyAsString("params");
        Log.i("a4444444", "444444444");
        List<BoxDetail> list = null;
        Log.i("a5555555555", "55555555");
        Log.i("soapObject", soapObject + "");
        Log.i("params", params);
        Log.i("state", state);
        List<BoxDetail> BoxDetaillist=null;
        String cqEupCount = "";
       List<BoxInfoByEmply>        boxInfoByEmplyList = new ArrayList<BoxInfoByEmply>();
        if ("成功".equals(state) && !params.equals("anyType{}")) {
            String[] array = params.split("_##_");

            if (null != array && array.length > 0) {

            } else {

            }
            BoxDetaillist = new ArrayList<BoxDetail>();
            for (int x = 0; x < array.length; x++) {
                BoxInfoByEmply boxInfoByEmply = new BoxInfoByEmply();
                String info = array[x];
                int index = info.indexOf(";");//5

                String brandname = info.substring(0, index);
                boxInfoByEmply.setEmplybradname(brandname);

                String setBrandtype = info.substring(info.indexOf(";", index + 1) + 1);
                String changebrarndtype = setBrandtype.substring(0, 1);
//                int xtype4 = info.indexOf(';');
//                int xtype2 = info.indexOf((info.indexOf(';')));
//                int xtype = info.indexOf((info.indexOf(';')) + 1);
//                int ytype = info.lastIndexOf(';');
                String Brandcount = info.substring(info.indexOf(';') + 1, info.lastIndexOf(';'));
                String Brandcount2 = Brandcount.substring(0, Brandcount.lastIndexOf(';'));
                boxInfoByEmply.setBrandcount(changebrarndtype); // 类型
                boxInfoByEmply.setBrandtype(Brandcount2); // shuliang
                boxInfoByEmplyList.add(boxInfoByEmply);

                BoxDetail box = new BoxDetail();
                box.setAtmType(Brandcount2);//写反了
                box.setBoxState("");
                box.setNum(changebrarndtype); //写反了
                box.setBrand(brandname);
                BoxDetaillist.add(box);
            }


        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", BoxDetaillist);
        map.put("count", cqEupCount);
        return map;
    }
}
