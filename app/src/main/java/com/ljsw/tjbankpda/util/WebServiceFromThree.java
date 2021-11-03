package com.ljsw.tjbankpda.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.service.FixationValue;

/**
 * Webservice解析方法（3期使用） 参数： 1.webservice命名空间 2.调用的方法名 3.webservice路径 4.参数数组
 * 
 * @author Administrator
 * 
 */
public class WebServiceFromThree {
	private static final String TAG = "WebServiceFromThree";

	/**
	 * 
	 * @param methodName
	 * @param parameter
	 * @param namespace
	 * @param path       包括（cash_cm，cash_cmanagement，cash_kuguanyuan）
	 * @return
	 * @throws Exception
	 */
	public static SoapObject getSoapObject(String methodName, WebParameter[] parameter, String namespace, String path)

			throws Exception {
		// 创建Soap对象，并指定命名空间和方法名
		SoapObject request = new SoapObject(namespace, methodName);

		// 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值

		if (parameter != null) {
			for (WebParameter webParameter : parameter) {
				request.addProperty(webParameter.getName(), webParameter.getValue());
			}
		}
		// 指定版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		// 设置Bodyout属性
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);

		envelope.encodingStyle = "UTF-8";

		// 创建HTTPtransports对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(path, 20000);

		// 调用webservice
		String pacth = path + "/" + methodName;
		System.out.println("test--" + pacth);

		ht.call(pacth, envelope);

		// 返回结果集
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		return soapObject;
	}


	/***
	 * 20210916 清分管理员复查合计的URL19 公共类
	 * @param methodName
	 * @param parameter
	 * @return
	 * @throws Exception
	 *
	 * 角色29
	 */
	public static SoapObject getSoapObjectDZLiabraryMangerbyThree(String methodName,  WebParameter[] parameter, String namespace, String path)
			throws Exception {
		// 创建Soap对象，并指定命名空间和方法名
		// 地址空间发生了改变
		SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
		// 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值

		if (parameter != null) {
			for (WebParameter webParameter : parameter) {
				request.addProperty(webParameter.getName(), webParameter.getValue());
			}
		}
		// 指定版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;// 后加入

		// 设置Bodyout属性
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		envelope.encodingStyle = "UTF-8";

		// 创建HTTPtransports对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(FixationValue.url19, 20000);

		// 调用webservice
		String patch = FixationValue.url19 + "/" + methodName;
		Log.e(TAG, "----------访问的地址：" + patch);
		ht.call(patch, envelope);
		// 返回结果集z

		SoapObject soapObject = (SoapObject) envelope.getResponse();
		Log.e(TAG, "----------soapObject：" + soapObject);
		return soapObject;
	}

}
