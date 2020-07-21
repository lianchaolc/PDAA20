package com.ljsw.tjbankpda.db.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;

/**
 * 周转箱交接处理类
 * 
 * @author zhangxuewei
 *
 */
public class ZhouZhuanXiangJiaoJie {
	/**
	 * 库管向押运交接执行方法
	 * 
	 * @param cashBoxNum
	 * @param roleId
	 * @param yayunid
	 * @param authType
	 * @param xianluId
	 * @param jigouId
	 * @param caozuoType
	 * @return
	 * @throws Exception
	 */
	public String SaveAuthLogPeisong(String cashBoxNum, String roleId, String yayunid, String authType, String xianluId,
			String jigouId, String caozuoType) throws Exception {
		String mothedName = "SaveAuthLogPeisong";
		WebParameter[] param = { new WebParameter<String>("arg0", cashBoxNum), new WebParameter<String>("arg1", roleId),
				new WebParameter<String>("arg2", yayunid), new WebParameter<String>("arg3", authType),
				new WebParameter<String>("arg4", xianluId), new WebParameter<String>("arg5", jigouId),
				new WebParameter<String>("arg6", caozuoType) };
		System.out.println("----SaveAuthLogPeisong传参：" + cashBoxNum + "+" + roleId + "+" + authType + "+" + xianluId
				+ "+" + jigouId);
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACE, FixationValue.URL5);
		System.out.println("----------SaveAuthLogPeisong:" + soap);

		String msg = soap.getProperty("msg").toString();
		System.out.println("msg:" + msg);
		msg = msg == null ? "" : msg;
		String[] codes = msg.split("_");
		if (codes[0].trim().equals("00")) {
			return codes[0].trim();
		} else {
			return codes[1].trim();
		}

	}

	public String saveAuthLog(String cashBoxNum, String roleId, String roleId2, String authType, String jihuadan)
			throws Exception {
		String mothedName = "saveAuthLog";
		WebParameter[] param = { new WebParameter<String>("arg0", cashBoxNum), new WebParameter<String>("arg1", roleId),
				new WebParameter<String>("arg2", roleId2), new WebParameter<String>("arg3", authType),
				new WebParameter<String>("arg4", jihuadan) };
		System.out.println("-------saveAuthLog传参：" + cashBoxNum + "-" + roleId + "-" + authType + "-" + jihuadan);
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACE, FixationValue.URL5);
		System.out.println("----------saveAuthLog:" + soap);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		if (code.equals("00")) {
			return code;
		} else {
			return msg;
		}
	}

	/***
	 * 
	 * @param cashBoxNum
	 * @param roleId
	 * @param authType
	 * @param xianluId
	 * @param jigouId
	 * @param strflage//
	 * @return
	 * @throws Exception
	 */

	public String SaveAuthLogShangjiao(String cashBoxNum, String roleId, String authType, String xianluId,
			String jigouId) throws Exception {
		String mothedName = "SaveAuthLogShangjiao";
		Log.i("cashBoxNum", cashBoxNum);
		Log.i("roleId", roleId);
		Log.i("authType", authType);
		Log.i("xianluId", xianluId);
		Log.i("jigouId", jigouId);

		WebParameter[] param = { new WebParameter<String>("arg0", cashBoxNum), new WebParameter<String>("arg1", roleId),
				new WebParameter<String>("arg2", authType), new WebParameter<String>("arg3", xianluId),
				new WebParameter<String>("arg4", jigouId) };
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACE, FixationValue.URL5);
		System.out.println("----------SaveAuthLogShangjiao:" + soap);
		String code = soap.getProperty("code").toString();
		// String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (code.equals("00")) {
			return params;
		}
		return null;
	}

	/***
	 * * 库管员和押运员上缴入库交接周转箱 lianc修改20191227 目的验证 返回00 提交成功 01 需要再次调用一次接口 99 调用失败
	 * 修改当前值值总库上缴入库 多加一个参数
	 */

//	public String SaveAuthLogShangjiaoBYMasterlibrary(String cashBoxNum, String roleId,
//			String authType, String xianluId, String jigouId,String strflage) throws Exception {
//		String mothedName = "SaveAuthLogShangjiao";
//		Log.i("cashBoxNum",cashBoxNum);
//		Log.i("roleId",roleId);
//		Log.i("authType",authType);
//		Log.i("xianluId",xianluId);
//		Log.i("jigouId",jigouId);
//		Log.i("strflage",strflage);
//		WebParameter[] param = { 
//				new WebParameter<String>("arg0", cashBoxNum),
//				new WebParameter<String>("arg1", roleId),
//				new WebParameter<String>("arg2", authType),
//				new WebParameter<String>("arg3", xianluId),
//				new WebParameter<String>("arg4", jigouId),
//				new WebParameter<String>("arg5", strflage) };
//		SoapObject soap = null;
//		soap = WebServiceFromThree.getSoapObject(mothedName, param,
//				FixationValue.NAMESPACE, FixationValue.URL5);
//		System.out.println("----------SaveAuthLogShangjiao:" + soap);
//		String code = soap.getProperty("code").toString();
//		//String msg = soap.getProperty("msg").toString();
//		String params = soap.getProperty("params").toString();
//		String msg = soap.getProperty("msg").toString();
//		Log.i("code",code);
//		Log.i("params",params);
//		Log.i("msg",msg);
////		if (code.equals("00")) {
//			return params;
////		}
////		return code;
//	}
//	
	/// 周转箱 线路名 库管员账户 该人员的组织id flag n

	public String SaveAuthLogShangjiaoBYMasterlibrary(String cashBoxNum, String xianluId, String userzhanghu,
			String jigouId, String strflage) throws Exception {
		String mothedName = "sjCofferIn";
		Log.i("cashBoxNum", cashBoxNum);
		Log.i("xianluId", xianluId);
		Log.i("roleId", userzhanghu);
		Log.i("jigouId", jigouId);
		Log.i("strflage", strflage);
		WebParameter[] param = { new WebParameter<String>("arg0", cashBoxNum),
				new WebParameter<String>("arg1", xianluId), new WebParameter<String>("arg2", userzhanghu),
				new WebParameter<String>("arg3", jigouId), new WebParameter<String>("arg4", strflage) };
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACEZH, FixationValue.URL17);
		System.out.println("----------SaveAuthLogShangjiao:" + soap);
		String code = soap.getProperty("code").toString();
		// String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		String msg = soap.getProperty("msg").toString();
		Log.i("code", code);
		Log.i("params", params);
		Log.i("msg", msg);
//		if (code.equals("00")) {
		return msg;
//		}
//		return code;
	}

}
