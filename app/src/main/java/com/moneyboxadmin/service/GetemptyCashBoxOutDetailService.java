package com.moneyboxadmin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.BoxDetail;
import com.entity.WebParameter;
import com.service.WebService;

public class GetemptyCashBoxOutDetailService {
	// 空钞箱出库明细

	/**
	 * 
	 * @param planNum
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getemptyCashBoxOutDetail(String planNum) throws Exception {

		String methodName = "getemptyCashBoxOutDetail";
		Log.i("planNum", planNum);
		WebParameter[] parameter = { new WebParameter<String>("arg0", planNum), };
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

}
