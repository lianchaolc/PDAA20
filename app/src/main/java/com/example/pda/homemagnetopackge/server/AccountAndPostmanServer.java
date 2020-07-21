package com.example.pda.homemagnetopackge.server;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

/**
 * 
 * 账户资料和后督账包提交的服务类 外包清分服务工具类
 * 
 * @author Administrator lianchao 20200420
 * 
 */
public class AccountAndPostmanServer {

	/***
	 * 2.1
	 * 
	 * 
	 * 账户中心获取线路 sjUnhandleOrderAccountCenterList 未完成
	 */

	public String sjUnhandleOrderAccountCenterList(String userId) throws Exception {
		// sjUnhandleOrderAccountCenterList
		String methodName = "sjUnhandleOrderAccountCenterList";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		// Log.i("arg0", strboxs);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 2.2. 获取账户资料的周转箱
	 * 
	 * @param userId
	 * @param strboxs
	 * @return
	 * @throws Exception http://192.168.1.131:8089/cash/webservice/cash_sk/
	 *                   sjUnHandleDistributionBoxnumByLinenumsInOrderAccount
	 *                   ?arg0=43534&arg1=BC02
	 */
	// sjUnHandleDistributionBoxnumByLinenumsInOrderAccount
	public String sjUnHandleDistributionBoxnumByLinenumsInOrderAccount(String userId, String strboxs) throws Exception {
		String methodName = "sjUnHandleDistributionBoxnumByLinenumsInOrderAccount";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		Log.i("arg1", strboxs);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId),
				new WebParameter<String>("arg1", strboxs) };
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 2.3 账户资料入库交接 20200420
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * 
	 *                   arg0= 账户中心人员 arg1=登录人员总行库管员账号 arg2= [ZH000001ZB,ZH000002ZB]
	 *                   周转箱的集合 arg3=[bc001,bc002]
	 * 
	 */
	public String AccountCenterByHomemanger(String usernumber, String postmanaccounnumber, String BoxNum, String linnum)
			throws Exception {
//		                      handleSJUnHandleInOrderAccount
		String methodName = "handleSJUnHandleInOrderAccount";// 以后会改是后台上的某些字段
		Log.i("arg0", usernumber);
		Log.i("arg1", postmanaccounnumber);
		Log.i("arg2", BoxNum);
		Log.i("arg3", linnum);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", usernumber),
				new WebParameter<String>("arg1", postmanaccounnumber), new WebParameter<String>("arg2", BoxNum),
				new WebParameter<String>("arg3", linnum)

		};
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			// 返回提交成功或者失败
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/**********************************************************************************************************/
	/***
	 * 1 后督账目包查询线路 20200422 http://192.168.1.131:8089/cash/webservice/cash_sk/
	 * sjUnhandleOrderFileCenterList?arg0=43534
	 */

	public String PostmandataSelect(String userId) throws Exception {

		String methodName = "sjUnhandleOrderFileCenterList";
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId) };
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/**
	 * 2
	 * 
	 * @param userId
	 * @return
	 * @throws Exception http://192.168.1.131:8089/cash/webservice/cash_sk/
	 *                   sjUnHandleDistributionBoxnumByLinenums?arg0=43534&arg1=BC02
	 *                   http://192.168.1.131:8089/cash/webservice/cash_sk/
	 *                   sjUnHandleDistributionBoxnumByLinenumsInFileCenter
	 *                   ?arg0=43534&arg1=BC02
	 */

	public String PostmanZZData(String usernumber, String strdata) throws Exception {
		String methodName = "sjUnHandleDistributionBoxnumByLinenumsInFileCenter";
		Log.i("arg0", usernumber);
		Log.i("arg1", strdata);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", usernumber),
				new WebParameter<String>("arg1", strdata) };
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 3 外包清分 后督账包操作 人员 指纹登录成功 或者账户登录成功后 提交数据代码
	 * 
	 * http://localhost:8080/cashman/webservice/cash_sk/
	 * handleSJUnHandleInOrderAccount?
	 * arg0=666666&arg1=43534&arg2=ZH000001ZZ&arg3=BC02
	 */
	public String PostmanUpData(String usernumber, String postmanaccounnumber, String BoxNum, String linnum)
			throws Exception {
		String methodName = "handleSJUnHandleInFileCenter";
		Log.i("arg0", usernumber);
		Log.i("arg1", postmanaccounnumber);
		Log.i("arg2", BoxNum);
		Log.i("arg3", linnum);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", usernumber),
				new WebParameter<String>("arg1", postmanaccounnumber), new WebParameter<String>("arg2", BoxNum),
				new WebParameter<String>("arg3", linnum)

		};
		SoapObject soap = WebService.getSoapObjectAccountAndPostman(methodName, parameter);

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			// 返回提交成功或者失败
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

}
