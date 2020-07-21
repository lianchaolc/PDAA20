package com.ljsw.tjbankpad.baggingin.activity.cash.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;
import com.service.WebService;

/****
 * 现金过门市天线时请求券别的服务类 lc 20190418
 * 
 * @author Administrator
 *
 */
public class BaggingForCashService {

	/***
	 * 获取过查库车所有的券别
	 * 
	 * @return
	 * @throws Exception
	 * 
	 *                   201910.25 日 制卡接口修改 cd/queryUnHandleCdnos
	 */
	public String getAllMoneyType_getMoneyEditionList() throws Exception {
		String methodName = "getMoneyEditionList";// 接口方法
		WebParameter[] param = {};// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url16);// 根据路径获得返回值
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
	 * 制卡
	 * 
	 * 券别 残损 是否尾零
	 * 
	 * @throws Exception TailZeromeid,cansunid, TailZerorecive
	 * 
	 *                   网络请求不知道报错新增了一个方法 getSoapObjectMakeCardby
	 */

	public String generateTag(String TailZeromeid, String cansunid, String TailZerorecive) throws Exception {
		String methodName = "generateTag";// 接口方法
		Log.i("arg0", TailZeromeid);
		Log.i("arg1", cansunid);
		Log.i("arg2", TailZerorecive);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", TailZeromeid),
				new WebParameter<String>("arg1", cansunid), new WebParameter<String>("arg2", TailZerorecive) };// 传入参数

//		SoapObject soap = null;// 创建返回值接收对象
//		soap = WebServiceFromThree.getSoapObject(methodName, param,FixationValue.NAMESPACEZH, 
//				FixationValue.url13);// 根据路径获得返回值
//			SoapObject soap = WebServiceFromThree.getSoapObjectMakeCardby(methodName,param);
//		    SoapObject soap = WebService.getSoapObjectCD(methodName, param);
		SoapObject soap = WebService.getSoapObjectMakeCardby(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

//		Log.i("arg0", soap.getProperty("code").toString());
//		String code = soap.getProperty("code").toString().trim();
//		String msg = soap.getProperty("msg").toString();
//		String params = soap.getProperty("params").toString();
//		System.out.println("result--code=" + code + "/msg=" + msg + "/params="+ params);
//		if ("00".equals(code)) {// code=00->成功,code=99->失败
//			return params;
//		} else {
//			return null;
//		}
	}

	/***
	 * 提交制卡成功后的数据 create 接口：cd/create 参数： arg1：钞袋编号 arg2: 制卡人
	 */
	public String updata_create(String CDcardid, String madecaruser) throws Exception {
		String methodName = "create";// 接口方法
		Log.i("arg0", CDcardid);
		Log.i("arg1", madecaruser);
		WebParameter[] param = {

				new WebParameter<String>("arg0", "" + CDcardid), new WebParameter<String>("arg1", "" + madecaruser) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url15);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return code;
		} else {
			return null;
		}
	}

	/***
	 * 
	 * 水牌处理尾零数据提交
	 * 
	 * @throws Exception
	 * 
	 */
	public String upDataWaterCardTailZero(String username, String quanbei, String sss) throws Exception {
		String methodName = "upDataWaterCardTailZero";// 接口方法
		Log.i("arg0", username);
		Log.i("arg1", quanbei);
		WebParameter[] param = {

				new WebParameter<String>("arg0", "" + username), new WebParameter<String>("arg1", "" + quanbei) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url15);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return code;
		} else {
			return null;
		}

	}

	/***
	 * 
	 * 水牌处理整袋数据提交
	 * 
	 * @throws Exception
	 * 
	 */
	public String Watercardwholenote(String username, String quanbei, String strjson) throws Exception {
		String methodName = "upDataWaterCardTailZero";// 接口方法
		Log.i("arg0", username);
		Log.i("arg1", quanbei);
		Log.i("arg2", strjson);

		WebParameter[] param = {

				new WebParameter<String>("arg0", "" + username), new WebParameter<String>("arg1", "" + quanbei) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.url15);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return code;
		} else {
			return null;
		}

	}

}
