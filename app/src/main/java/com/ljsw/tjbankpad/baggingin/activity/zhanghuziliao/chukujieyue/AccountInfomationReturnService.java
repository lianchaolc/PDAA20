package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.entity.WebParameter;
import com.service.WebService;

/***
 * * @author Administrator 账户资料待归还的服务类 2018_11_13
 */
public class AccountInfomationReturnService {
	/***
	 * 账户资料入库交接页面数据查询 ，获取账户资料未交接的数量和数据集合
	 * 
	 * @param userId
	 * @return
	 * @throws Exception 1
	 */
	public String getAccountUnHandoverCountAndList(String userId) throws Exception {
		String methodName = "getAccountUnHandoverCountAndList";// 以后会改是后台上的某些字段
		Log.i("arg0", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", userId), };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 2 上缴账户资料交接 账户中心人员》抵制押品 管理员
	 * 
	 * @param stockCodeListJson
	 * @param usrId
	 * @return
	 * @throws Exception
	 * 
	 */
	public String updateAccountTurnOverFromCenterToWarehouser(String stockCodeListJson, String usrId) throws Exception {
		String methodName = "updateAccountTurnOverFromCenterToWarehouse";// 以后会改是后台上的某些字段
		Log.i("arg0====", stockCodeListJson);
		Log.i("arg1======", usrId);

		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", stockCodeListJson),
				new WebParameter<String>("arg1", usrId),

		};
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 账户资料出库借阅 出库任务列表
	 * 
	 * @param stockCodeListJson
	 * @param usrId
	 * @return
	 * @throws Exception 3
	 */
	public String getAccountOutTaskList(String usrId) throws Exception {
		// getAccountOutTaskList
		String methodName = "getAccountOutTaskList";// 以后会改是后台上的某些字段
		Log.i("arg0", usrId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", usrId), };

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
	 * 4
	 * 
	 * @param cvoun
	 * @return
	 * @throws Exception 4 账户资料出库借阅
	 */
	public String getAccountOutTaskListDetail(String cvoun) throws Exception {
		// getAccountOutTaskDetail
		String methodName = "getAccountOutTaskDetail";// 以后会改是后台上的某些字段
		Log.i("arg0", cvoun);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", cvoun), };

		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			System.out.print("======" + soap.getProperty("msg").toString());
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 5 账户资料出库借阅 交接 管库员 》账户中心人员
	 * 
	 * @param cvoun
	 * @param userId
	 * @return
	 * @throws Exception
	 */

	public String updateAccountOutFromWarehouserToCenter(String covun, String userId, String operator)
			throws Exception {
		// updateAccountOutFromWarehouseToCenter
		String methodName = "updateAccountOutFromWarehouseToCenter";// 以后会改是后台上的某些字段
		Log.i("arg0", covun);
		Log.i("arg1", userId);
		Log.i("arg2", operator);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", covun), new WebParameter<String>("arg1", userId),
				new WebParameter<String>("arg2", operator), };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 6 /账户资料归还 归还任务列表和详情
	 * 
	 * @param userId
	 * @return
	 * @throws Exception 6-1
	 */
	public String getAccountReturnTaskList(String userId) throws Exception {
		String methodName = "getAccountReturnTaskList";// 以后会改是后台上的某些字段
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
	 * 账户资料归还： 交接（账户中心人员 -> 管库员)
	 * 
	 * @param cvoun             订单号
	 * @param stockCodeListJson 扫描到的袋号数据
	 * @param userId            管库员id
	 * @return
	 */

	public String updateAccountReturnFromCenterToWareHouse(String cvoun, String stockCodeListJson, String userId)
			throws Exception {
		String methodName = "updateAccountReturnFromCenterToWarehouse";// 以后会改是后台上的某些字段
		Log.i("arg0", cvoun);
		Log.i("arg1", stockCodeListJson);
		Log.i("arg2", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", cvoun),
				new WebParameter<String>("arg1", stockCodeListJson), new WebParameter<String>("arg2", userId), };
		SoapObject soap = WebService.getSoapObjectZHAccountCenter(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * /** 账户资料归还 : 待放入货柜的任务列表以及详情
	 * 
	 * @param userId 管库员id
	 * @return
	 * @throws Exception
	 * 
	 *                   7
	 */

	public String getAccountPutInTaskList(String userId) throws Exception {
		String methodName = "getAccountPutInTaskList";// 以后会改是后台上的某些字段
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
	 * 库管员交接 现金装袋后需要扫描后台的袋数并指纹验证提交 lianc
	 * 
	 * @param userId
	 * @return
	 * @throws Exception stockCodeListJson, userId
	 */

	public String updataHomeManger_handle(String stockCodeListJson, String userId) throws Exception {
		String methodName = "handle";// 以后会改是后台上的某些字段
		Log.i("arg0", stockCodeListJson);
		Log.i("arg1", userId);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", stockCodeListJson),
				new WebParameter<String>("arg1", userId) };
		SoapObject soap = WebService.getSoapObjectCD(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

}
