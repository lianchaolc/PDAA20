package com.online.update.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.OrderDetail;
import com.entity.WebParameter;
import com.service.FixationValue;
import com.service.WebService;

public class PDAVersionInfoPathService {
	// 返回配置文件路径
	public String getpath() throws Exception {

		String methodName = "getPDA3VersionInfoPath";
		WebParameter[] param = new WebParameter[0];
		System.out.println("获取配置文件路径");

		SoapObject soap = WebService.getSoapObject(methodName, param);
		System.out.println("" + soap);
		String params = soap.getProperty("params").toString();
		return params.toString();

		// LR_TODO: 2020/6/12 16:22 liu_rui 测试更新下载代码
//		return "http://test.afuiot.com:8080/upload/versionInfo.xml";
	}

	/***
	 * 配合上线有线网络状态下的更新    将原有的getPDAVersionInfoPath 这个方法 改成有线的方式
	 * @return
	 * @throws Exception
	 */
	public  String getpathBYWiFi() throws Exception {

		String methodName = "getPDAVersionInfoPath";
		WebParameter[] param = new WebParameter[0];
		System.out.println("获取配置文件路径getpathBYWiFi");

		SoapObject soap = WebService.getSoapObject(methodName, param);
		System.out.println("" + soap);
		String params = soap.getProperty("params").toString();
		return params.toString();

		// LR_TODO: 2020/6/12 16:22 liu_rui 测试更新下载代码
//		return "http://test.afuiot.com:8080/upload/versionInfo.xml";
	}

}
