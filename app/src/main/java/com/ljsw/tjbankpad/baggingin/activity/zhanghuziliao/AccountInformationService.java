package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

/***
 * 账户资料服务类1
 * 
 * @author Administrator 2018—11—9廉超
 */

public class AccountInformationService {

	/***
	 * 账户资料交接入库的第一个页面：查询待做的线路数
	 * 
	 * @throws Exception
	 */
	public String getAccountTurnOverLineCount(String number) throws Exception {
		String methodName = "getAccountTurnOverLineCount";
		Log.i("arg0", number);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + number) };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);// 账户中心的url 更改了
		Log.e("soap", "getAccountTurnOverLineCount====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			System.out.print("======" + soap.getProperty("msg").toString());
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 账户资料交接入库的第二个页面：查询待做的线路列表以及具体周转箱号以及数量
	 * 
	 * @param number
	 * @return
	 * @throws Exception
	 */

	public String getAccountTurnOverLineList(String number) throws Exception {
		String methodName = "getAccountTurnOverLineList";// 以后会改是后台上的某些字段
		Log.i("arg0", number);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", number), };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();// 返回集合内容
		} else {
			return soap.getProperty("params").toString();
		}
	}

	/***
	 * 上缴账户资料（押运->账户中心）
	 * 
	 * @param userId
	 * @param lineNum
	 * @param corpId
	 * @return
	 * @throws Exception
	 */

	public String getupdateAccountTurnOverFromEscortToCenter(String userId, String lineNum) throws Exception {
		String methodName = "updateAccountTurnOverFromEscortToCenter";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		Log.i("arg1", lineNum);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId),
				new WebParameter<String>("arg1", lineNum), };

		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======!!!!!!!" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();// 返回集合内容
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 账户资料装袋 获取列表
	 */
	public String getAccountBaggingDetailList(String userId) throws Exception {
		String methodName = "getAccountBaggingDetailList";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };

		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();// 返回集合内容
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 向后台传输数据 accountNum baggingNum userId
	 */

	public String updatabagging(String accountNum, String baggingNum, String userId) throws Exception {
		String methodName = "updateBagging";// 以后会改是后台上的某些字段
		Log.i("arg0", accountNum);
		Log.i("arg1", baggingNum);
		Log.i("arg2", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", accountNum),
				new WebParameter<String>("arg1", baggingNum), new WebParameter<String>("arg2", userId), };

		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();// 返回集合内容
		} else {
			return soap.getProperty("msg").toString();
		}

	}
}
