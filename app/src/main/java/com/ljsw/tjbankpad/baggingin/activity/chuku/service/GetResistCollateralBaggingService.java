package com.ljsw.tjbankpad.baggingin.activity.chuku.service;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.entity.BoxDetail;
import com.entity.WebParameter;
import com.service.WebService;

import android.util.Log;

/***
 * 抵质押品装袋 网络请求服务类
 * 
 * @author Administrator lc 2018_10_22
 *
 */
public class GetResistCollateralBaggingService {

	/***
	 * 获取抵质押品的装袋计划列表
	 * 
	 * @throws Exception
	 */
	public String GetResistCollateralBaggingList(String number) throws Exception {

		String methodName = "getClearWorkBox";
		Log.i("arg0", number);
		List<String> list = new ArrayList<String>();
		WebParameter[] parameter = { new WebParameter<String>("arg0", "" + number) };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.e("soap", "GetResistCollateralBaggingService====" + soap.toString());
		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 抵质押品 装袋核对列表
	 * 
	 */
	public String GetResistOutCollateraPlanlList(String number) throws Exception {
		String methodName = "getClearWorkBoxList";// 以后会改
		Log.i("arg0", number);
		List<String> list = new ArrayList<String>();
		WebParameter[] parameter = { new WebParameter<String>("arg0", number) };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap + "");
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/****
	 * 抵制押品输入编号
	 * 
	 * @throws Exception
	 * 
	 */
	public String SendBoxBagSubmission(String box) throws Exception {
		String methodName = "getClearWorkDetailList";// 以后会改是后台上的某些字段
		Log.i("arg0", box);
		List<String> list = new ArrayList<String>();
		WebParameter[] parameter = { new WebParameter<String>("arg0", box) };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap + "");
		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 抵质押品装袋
	 * 
	 * @throws Exception
	 */

	public String GetResistzhuangdai(String number, String bag, String username) throws Exception {
		String methodName = "updateBagging";// 以后会改是后台上的某些字段
		Log.i("arg0", number);
		Log.i("arg1", bag);
		Log.i("arg2", username);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", number), new WebParameter<String>("arg1", bag),
				new WebParameter<String>("arg2", username) };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 出库 抵质押品获取清分的袋数和名称
	 * http://192.168.1.138:8081/cash/webservice/pda/getClearWorkBag
	 */
	public String GetResistCollateralBaggingCleanList() throws Exception {
		String methodName = "getClearWorkBag";// 以后会改是后台上的某些字段
		SoapObject soap = WebService.getSoapObjectZH(methodName, null);// 参数的值为null
		Log.i("arg0", soap + "");
		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * 抵质押品入库指纹交接成功后提交
	 */

	public String GetUpdata(String clearTaskNum, String userId) throws Exception {
		String methodName = "updateCollateralTurnOverFromClearToWarehouse";//
		Log.i("arg0", clearTaskNum);
		Log.i("arg1", userId);
//		
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", clearTaskNum),
				new WebParameter<String>("arg1", userId), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}
	}

	/***
	 * 抵质押品 货位管库员查询抵质押品请领清分任务以及数量和明细信息
	 * 
	 * @throws Exception
	 */

	public String LocalMangeSelectCollateral() throws Exception {
		String methodName = "getPleaseLeadClearWorkBag";
		SoapObject soap = WebService.getSoapObjectZH(methodName, null);
		Log.i("arg0", soap + "");
		if (soap.getProperty("code").toString().equals("00")) {
			return soap.getProperty("params").toString();
		} else {
			return null;
		}
	}

	/***
	 * * @param clearTaskNum 清分任务
	 * 
	 * @param warehouseUserId 货柜管理员id
	 * @param clearUserId1    清分员1
	 * @param clearUserId2    清分员2
	 * @return
	 * @throws Exception
	 */

	public String getResultfingerprint(String clearTaskNum, String warehouseUserId, String clearUserId1,
			String clearUserId2) throws Exception {
		String methodName = "updateCollateralPleaseLeadFromWarehouseToClear";//
		Log.i("arg0", clearTaskNum);
		Log.i("arg1", warehouseUserId + "");
		Log.i("arg2", clearUserId1);
		Log.i("arg3", clearUserId2);
//		
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", clearTaskNum),
				new WebParameter<String>("arg1", warehouseUserId), new WebParameter<String>("arg2", clearUserId1),
				new WebParameter<String>("arg3", clearUserId2), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 抵质押品押品 装箱 清分员发起 装箱操作
	 * 
	 * @param clearUserId2 清分员 2011-11-5
	 * @throws Exception
	 */

	public String CleanMantoBoxList(String cleannumber) throws Exception {
		String methodName = "getPleaseLeadClearWorkBagList";//
		Log.i("arg0", cleannumber);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", cleannumber), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/***
	 * 查询任务
	 * 
	 * @throws Exception
	 * 
	 */
	public String getPleaseLeadClearWorkCorpListAndCvounCount(String Tasknum) throws Exception {
		String methodName = "getPleaseLeadClearWorkCorpListAndCvounCount";//
		Log.i("arg0", Tasknum);
		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", Tasknum), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/****
	 * 根据 网点编号 和清分任务， 查找定单总数和订单列表和对应的抵质押品
	 * getPleaseLeadClearWorkCvounAndCollateralList
	 * 
	 * @throws Exception
	 * 
	 */

	public String getPleaseLeadClearWorkCvounAndCollateralList(String Postion, String BankNumber) throws Exception {
		String methodName = "getPleaseLeadClearWorkCvounAndCollateralList";//
		Log.i("arg0", Postion);
		Log.i("arg1", BankNumber);

		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", Postion),
				new WebParameter<String>("arg1", BankNumber), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("params").toString();
		} else {
			return soap.getProperty("msg").toString();
		}

	}

	/**
	 * 抵质押品装箱
	 * 
	 * clearTaskNum 清分任务 corpId 订单所属机构号 userId 装箱人员 boxnumlistjson 周转箱集合的json
	 * cvounlistJson 订单集合的json
	 * 
	 * @throws Exception
	 * 
	 */
	public String updateCollateralPacket(String clearTaskNum, String corpId, String userId, String boxnumListJson,
			String cvounListJson) throws Exception {
		String methodName = "updateCollateralPacket";//
		Log.i("arg0", clearTaskNum);
		Log.i("arg1", corpId);
		Log.i("arg2", userId);
		Log.i("arg3", boxnumListJson);
		Log.i("arg4", cvounListJson);

		@SuppressWarnings("rawtypes")
		WebParameter[] parameter = { new WebParameter<String>("arg0", clearTaskNum),
				new WebParameter<String>("arg1", corpId), new WebParameter<String>("arg2", userId),
				new WebParameter<String>("arg3", boxnumListJson), new WebParameter<String>("arg4", cvounListJson), };
		SoapObject soap = WebService.getSoapObjectZH(methodName, parameter);
		Log.i("arg0", soap.getProperty("code").toString());

		if (soap.getProperty("code").toString().equals("00")) {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("code").toString();
		} else {
			System.out.print("======" + soap.getProperty("params").toString());
			return soap.getProperty("msg").toString();
		}

	}

}
