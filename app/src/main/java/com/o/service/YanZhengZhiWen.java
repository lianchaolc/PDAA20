package com.o.service;

import org.ksoap2.serialization.SoapObject;

import android.os.Handler;
import android.util.Log;

import com.entity.WebParameter;
import com.example.app.entity.User;
import com.service.WebService;

public class YanZhengZhiWen {

	public User yanzhengfinger(String corpId, String roleId, byte[] cValue) throws Exception {

		Log.i("yanzhengyayunb","corpId="+corpId);
		Log.i("yanzhengyayunb","roleId="+roleId);
		Log.i("yanzhengyayunb","cValue="+cValue);
		User user = null;
		String mothedName = "checkFingerprint";
		WebParameter[] param = {
				new WebParameter<String>("arg0", corpId),// 机构
				new WebParameter<String>("arg1", roleId), // 角色
				new WebParameter<byte[]>("arg2", cValue) };
		SoapObject soap = WebService.getSoapObject(mothedName, param);
		System.out.println("YanZhengZhiWen-------------传参：" + corpId + "===" + roleId);
		System.out.println("YanZhengZhiWen-------------" + soap);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if ("00".equals(code)) {
			String[] split = params.split(";");
			user = new User();
			user.setUserzhanghu(split[0]);
			user.setUsername(split[1]);
			if(null!=split[2]){
				user.setJigouid(split[2]);//  2022.1.14 网点信息录入时需要加上没有机构id
			}

		} else {
			return null;
		}

		return user;

	}
}
