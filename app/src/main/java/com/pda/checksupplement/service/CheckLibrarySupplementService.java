package com.pda.checksupplement.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

/***
 * 库管员查库盘库任务 网络请求
 * 
 * @author Administrator lc 2018_11_29
 */
public class CheckLibrarySupplementService {
	private static final String TAG = "CheckLibrarySupplementService";

	/***
	 * 1 获取盘库和查库的列表
	 * 
	 * @throws Exception getTaskAndMissCount
	 */
	public String getTaskAndMissCount1(String number) throws Exception {
		String methodName = "getTaskAndMissCount";
		Log.i("arg0", number);
//		List<String> list=new ArrayList<String>();
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + number) };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
		Log.e("soap", "getTaskAndMissCount====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 2 需要扫面的券别网络获取数据
	 */

	public String GetTypeOfCoupon(String taskNum) throws Exception {
		String methodName = "getMoneyType";
		Log.i("arg0", taskNum);
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + taskNum) };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
		Log.e("soap", "getMoneyType====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 2 获取要扫描的的号
	 * 
	 * @param number
	 * @return
	 * @throws Exception
	 */
//	public  String getReScanDetail(String number) throws Exception{
//		String  methodName="getReScanDetail";
//		Log.i("arg0", number);
//		List<String> list=new ArrayList<String>();
//		WebParameter[] parameter = { new WebParameter<String>("arg0", ""+number) };
//		SoapObject soap=WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
//		Log.e("soap","getTaskAndErrorCount===="+soap.toString());
//		if(soap.getProperty("code").toString().equals("00")){
//			return soap.getProperty("params").toString();
//		}else{
//			return null;
//		}
//	}

	/**
	 * 4 查库车：当日任务的补扫功能
	 *
	 * @param taskNum     任务号
	 * @param tagListJson 补扫到的标签 loginuser 做事的人
	 * @throws Exception
	 */

	public String updateReScan(String userId, String taskNum, String moneytype, String tagListJson) throws Exception {
		String methodName = "updateReScan";
		Log.i("arg0", userId);
		Log.i("arg1", taskNum);
		Log.i("arg2", moneytype);
		Log.i("arg3", tagListJson);
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + userId),
				new WebParameter<String>("arg1", "" + taskNum), new WebParameter<String>("arg2", "" + moneytype),
				new WebParameter<String>("arg3", "" + tagListJson) };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
		Log.e(TAG, "updateReScan====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {
			Log.e(TAG, soap.getProperty("code").toString());
			return soap.getProperty("code").toString();
		} else {
			return null;
		}

	}

	/****
	 * 3 获取要扫那些标签
	 * 
	 * @param userid
	 * @param moneytype
	 * @return
	 * @throws Exception
	 */

	public String getReScanDetail(String userid, String moneytype) throws Exception {
		String methodName = "getReScanDetail";
		Log.i("arg0", userid);
		Log.i("arg1", moneytype);
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + userid),
				new WebParameter<String>("arg1", "" + moneytype), };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
		Log.e(TAG, "getReScanDetail====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {

			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 库管员 扫描钞袋进行数据提交 获取后天传的袋子号码显示 lc 201910.25
	 * 
	 * @throws Exception
	 * 
	 *                   返回msg
	 */

	public String HomemangerGetPackgets_queryUnHandleCdnos() throws Exception {
		String methodName = "queryUnHandleCdnos";
		WebParameter[] parameter = {};
		SoapObject soap = WebService.getSoapObjectCD(methodName, parameter);
		if (soap.getProperty("code").toString().equals("00")) {
			Log.d(TAG, "" + soap.getProperty("code"));
			return soap.getProperty("msg").toString(); /// 华徐冰后台反的有问题
		} else {
			return null;
		}
	}
}
