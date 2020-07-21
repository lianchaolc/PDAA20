package com.ljsw.tjbankpda.db.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;

public class KuGuanRenWuLieBiao {
	public String getStoremanTaskList(String corpId, String jiaoseId) throws Exception {
		String mothedName = "getStoremanTaskList";

		Log.i("KuGuanRenWuLieBiao", corpId);
		Log.i("KuGuanRenWuLieBiao", jiaoseId);

		WebParameter[] param = { new WebParameter<String>("arg0", corpId), new WebParameter<String>("arg1", jiaoseId) };
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACE, FixationValue.URL5);
		System.out.println("----------getStoremanTaskList:" + soap);
		String code = soap.getProperty("code").toString();
		String params = soap.getProperty("params").toString();
		if (code.equals("00")) {
			return params;
		}
		return null;
	}

	/***
	 * ���Ա ��¼���Ͻ�������������
	 * 
	 * @throws Throwable lianc
	 */
	public String getTasInLibrary(String userloginzhaohao) throws Throwable {

		String mothedName = "sjCofferInLines";
		System.out.println("KuGuanRenWuLieBiao" + "---arg0======" + userloginzhaohao);
		WebParameter[] param = { new WebParameter<String>("arg0", userloginzhaohao) };
		SoapObject soap = null;
		soap = WebServiceFromThree.getSoapObject(mothedName, param, FixationValue.NAMESPACEZH, FixationValue.URL17);
		System.out.println("----------getTasInLibrary:" + soap);
		String code = soap.getProperty("code").toString();
		String params = soap.getProperty("params").toString();
		System.out.print("params=" + params);

		if (code.equals("00")) {
			return params;
		}
		return null;
	}

}
