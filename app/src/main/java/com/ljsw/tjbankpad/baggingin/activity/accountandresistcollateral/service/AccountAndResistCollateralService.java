package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.example.app.entity.User;
import com.service.WebService;

/**
 * 账户资料和抵制押品获取任务数量的服务类
 * 
 * @author Administrator 2018_11_19
 * 
 * 
 *         *
 */
public class AccountAndResistCollateralService {

	/**
	 * 3 获取账户资料任务数量服务
	 */

	public String getAccountTaskListAndCount(String userId) throws Exception {
		String methodName = "getAccountTaskListAndCount";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 库管员获取的任务列表数量
	 * 
	 * 管库员查看档案柜任务列表（包括抵质押品和账户资料）
	 *
	 * @param userId 管库员 2
	 */
	public String getWarehouseTaskListAndCount(String userId) throws Exception {
//	                        getWarehouseTaskListAndCount
		String methodName = "getWarehouseTaskListAndCount";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 1 清分人员获取抵质押品的任务数量
	 * 
	 */
	public String getClearCollateralTaskListAndCount(String userId) throws Exception {
		// getClearCollateralTaskListAndCount
		String methodName = "getClearCollateralTaskListAndCount";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", "=====" + soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {

			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 押运员是否领取任务
	 * 
	 * @param userId
	 * @return
	 * @throws Exception 如果ip后面的链接名不符合会报错 XmlPullParser
	 *                   解析不到END_DOCUMENT，XmlPullParserException: Unexpected token
	 *                   (position:TEXT
	 */
//	getescortselectTask
	public String getescortselectTask(String userId) throws Exception {
		System.out.println("验证是否有上缴的任务需要押运员去请领");
		System.out.println(userId + "====userId");
		String mothedName = "getescortselectTask";
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("msg").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 领取任务成功后更改数据库的表状态
	 */

	public String getTaskTruebyYayunyuan(String userid) {

		System.out.println(userid + "====userId");
		String mothedName = "getescortselectTask";
		Log.i("arg0", userid);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", userid), };
//		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
//		String code = soap.getProperty("code").toString();
//		String msg = soap.getProperty("msg").toString();
//		String params = soap.getProperty("params").toString();
//		if (soap.getProperty("code").toString().equals("00")) {
//			System.out.print("======" + soap.getProperty("params").toString());
//			return soap.getProperty("params").toString();
//		} else {
//			
//			return soap.getProperty("msg").toString();
//		}
		return null;

	}

	/***
	 * 押运员获取线路信息 lianc 没有做的上缴或者请领任务 接口5
	 * 
	 * @throws Exception
	 */
	public String queryJobOrderDetailHandleStatusByEscort(String userZhanghu) throws Exception {
		System.out.println(userZhanghu + "====userZhanghu");
		String mothedName = "queryJobOrderDetailHandleStatusByEscort";
		Log.i("arg0", userZhanghu);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", userZhanghu), };
		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("msg").toString());
			System.out.print("======" + soap.getProperty("code").toString());
			System.out.print("======" + soap.getProperty("params").toString());
//			 这里后台的返回码错误华徐冰
			return soap.getProperty("msg").toString() + "*" + soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 查看是否有上交任务 3 根据线路编号查询上缴配送详细信息 lianc
	 */
	public String GetTookTask(String userZhanghu, String linnum) throws Exception {
		System.out.println(userZhanghu + "====userZhanghu");
		System.out.println(linnum + "====linnum");
		String mothedName = "getnotastinfo";
		Log.i("arg0", userZhanghu);
		Log.i("arg0", linnum);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", userZhanghu),
				new WebParameter<String>("arg1", linnum), };
		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("msg").toString());
			System.out.print("======" + soap.getProperty("code").toString());
			System.out.print("======" + soap.getProperty("params").toString());
//			 这里后台的返回码错误华徐冰
			return soap.getProperty("msg").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 押运员确定领取任务 lianc 确定领取任务
	 * 
	 * @throws Exception
	 */
	public String GetTookTaskTrue(String usernume, String linname) throws Exception {
		String mothedName = "receiveSJJobOrder";
		Log.i("receiveSJJobOrder", "receiveSJJobOrder");
		Log.i("arg0", usernume);
		Log.i("arg1", linname);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", usernume), new WebParameter<String>("arg1", linname),

		};
		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("msg").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 接口2 的数据
	 * 
	 * @param usernume
	 * @param linname
	 * @return
	 * @throws Exception
	 */

	public String getnotastlindata(String usernume) throws Exception {
		String mothedName = "getnotastlindata";
		Log.i("arg0", usernume);
		@SuppressWarnings("rawtypes")
		WebParameter[] param = { new WebParameter<String>("arg0", usernume),

		};
		SoapObject soap = WebService.getSoapObjectZHyayun(mothedName, param);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("msg").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

}
