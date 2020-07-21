package com.ljsw.tjbankpad.baggingin.activity.cashtopackges.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

/***
 * 现金装袋服务类 20190426
 * 
 * @author Administrator 廉超
 * 
 */
public class CashToPackgersService {
	/***
	 * 获取所有的算别信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAllMoneyInfo() throws Exception {
		String methodName = "getAllMoneyInfo";// 以后会改是后台上的某些字段
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = {};
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCount(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {

			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 清分人员指纹交接提交数据（华徐冰写非尾零状态 下的现金装袋数据提交）
	 * 
	 * @throws Exception
	 */
	public String updateCashClearingWork(String cleanuserone, String cleanmangerother, String cardid, String array)
			throws Exception {
//		String methodName = "updateCashClearingWork";// 以后会改是后台上的某些字段

		String methodName = "bag";// 华徐冰 修改新接口
		Log.i("arg0", cardid);
		Log.i("arg1", cleanmangerother);
		Log.i("arg2", cleanuserone);

		@SuppressWarnings("rawtypes")

		WebParameter[] parameter = { new WebParameter<String>("arg0", cardid),
				new WebParameter<String>("arg1", cleanmangerother), new WebParameter<String>("arg2", cleanuserone),
				new WebParameter<String>("arg3", ""), };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCountAA(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {

			return soap.getProperty("msg").toString();
		}

	}

	public String updateCashClearingWorkweiling(String cardid, String cleanmangerother, String cleanuserone,
			String array) throws Exception {
//		String methodName = "updateCashClearingWork";// 以后会改是后台上的某些字段

		String methodName = "bag";// 华徐冰 修改新接口
		Log.i("arg0", cardid);
		Log.i("arg1", cleanmangerother);
		Log.i("arg2", cleanuserone);
		Log.i("arg3", array);
		@SuppressWarnings("rawtypes")

		WebParameter[] parameter = { new WebParameter<String>("arg0", cardid),
				new WebParameter<String>("arg1", cleanmangerother), new WebParameter<String>("arg2", cleanuserone),
				new WebParameter<String>("arg3", array), };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCountAA(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {

			return soap.getProperty("msg").toString();
		}

	}

	/****
	 * 2019.10.21
	 * 
	 * @return
	 * @throws Exception
	 * 
	 *                   获取数据尾零装袋的数据请求因原接口缺少版别字段所以新接口 获区 券别信息
	 */

	public String TailCoupon_getMoneyEditionList() throws Exception {
		String methodName = "getMoneyEditionList";// 以后会改是后台上的某些字段
		WebParameter[] parameter = {};
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCountdizhi(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {

			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 尾零数据提交
	 */
	public String TailzertoPacakge_fillTailZero(String packgeid, String array) throws Exception {
		String methodName = "fillTailZero";// 以后会改是后台上的某些字段
		Log.i("arg0", packgeid);
		Log.i("arg1", array);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", packgeid),
				new WebParameter<String>("arg1", array), };
		SoapObject soap = WebService.getSoapObjectCD(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {

			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 清分人员指纹交接提交数据 抵质押品
	 * 
	 * @throws Exception
	 */
	public String updateCashClearingWork(String cleanuserone, String cleanmangerother, String cardid) throws Exception {
		String methodName = "updateCashClearingWork";// 以后会改是后台上的某些字段
		Log.i("arg0", cleanuserone);
		Log.i("arg1", cleanmangerother);
		Log.i("arg2", cardid);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", cleanuserone),
				new WebParameter<String>("arg1", cleanmangerother), new WebParameter<String>("arg2", cardid), };
		SoapObject soap = WebService.getSoapObjectZHTaskAndErrorCountdizhi(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {

			return soap.getProperty("msg").toString();
		}

	}
}
