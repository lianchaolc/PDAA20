package com.ljsw.tjbankpda.qf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.example.app.entity.WebParameter;
import com.ljsw.tjbankpda.qf.entity.TianJiaXianJin;
import com.ljsw.tjbankpda.qf.entity.TianJiaZhongKong;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;
import com.service.WebService;

/**
 * 清分员任务数据获取Service类
 * 
 * @time 2015-06-18 15:16
 * @author FUHAIQING
 */
public class QingfenRenwuService {

	private static final String TAG ="QingfenRenwuService" ;

	/**
	 * 获取清分员任务
	 * 
	 * @param userId 用户ID
	 * @return 获取成功返回数据,失败则返回null
	 * @throws Exception
	 */
	public String getQingfenRenwu(String userId) throws Exception {
		String methodName = "getQingfenRenwu";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", userId) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		// String
		// params="renwudan:SJQF002|xianluming:东青支行一线,东青支行二线,东青支行三线|wangdianshu:2,2,2|zhouzhuanxiangshu:6,4,5|\nrenwudan:QLZX001|xianluming:西青支行一线,西青支行二线|wangdianshu:2,2|";
		System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 清分员根据线路获取该线路下网点以及网点下的订单列表
	 * 
	 * @param lineId   线路id
	 * @param renwudan 任务单号
	 * @return
	 * @throws Exception
	 */
	public String getQinglingMingxi(String lineId, String renwudan) throws Exception {
		String methodName = "getQinglingMingxi";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", lineId), new WebParameter<String>("arg1", renwudan) };
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("result--code=" + code + "/msg=" + msg + "/params=" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 检查订单是否已经取到抵质押品、 检查订单是否已经取到抵质押品
	 *
	 * @param dingdanId 订单号
	 * @return
	 * 
	 *         lchao Cash, heavy, collateral
	 */
	public String checkIfHasGotCollateral(String collateral) throws Exception {
		// checkIfHasGotCollateral
		String methodName = "checkIfHasGotCollateral";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", collateral) };
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);// 根据路径获得返回值
		String code = soap.getProperty("code").toString().trim();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("msg:" + msg + " params:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return code;
		} else {
			return msg;
		}
	}

	/**
	 * 只有一个传入参数的接口使用连接类
	 * 
	 * @param inputParams 传入参数
	 * @param methodName  方法名
	 * @return 获取成功返回数据,失败则返回null
	 * @throws Exception
	 */
	public String getParams(String inputParams, String methodName) throws Exception {
		Log.e(TAG,"inputParams"+inputParams);
		WebParameter[] param = { new WebParameter<String>("arg0", inputParams) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();

		System.out.println("msg:" + msg + " params:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 清分员上缴清分获取订单所有周转箱
	 * 
	 * @param taskId 订单号
	 * @return 获取成功返回数据,失败则返回null
	 * @throws Exception
	 */
	public String getQfZhouzhuanxiangID(String taskId) throws Exception {
		String methodName = "getQfZhouzhuanxiangID";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", taskId) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 请领装箱提交
	 * 
	 * @param renwudan   任务单
	 * @param userid     清分员id
	 * @param boxNum     周转箱编号
	 * @param dingdianID 订单id
	 * @param onceKeyNum 一次性锁扣编号
	 * @param jigouid    机构id
	 * @return
	 * @throws Exception
	 */
	public boolean submitZzxInfo(String renwudan, String userid, String boxNum, String dingdianID, String onceKeyNum,
			String jigouid) throws Exception {
		Log.e(TAG,"老方法----");
		System.out.println("renwudan=" + renwudan);
		System.out.println("userid=" + userid);
		System.out.println("boxNum=" + boxNum);
		System.out.println("dingdianID=" + dingdianID);
		System.out.println("onceKeyNum=" + onceKeyNum);
		System.out.println("jigouid=" + jigouid);
		String methodName = "setZhuangxiangSubmit";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", renwudan), new WebParameter<String>("arg1", userid),
				new WebParameter<String>("arg2", boxNum), new WebParameter<String>("arg3", dingdianID),
				new WebParameter<String>("arg4", onceKeyNum), new WebParameter<String>("arg5", jigouid) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		System.out.println("code=" + code);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 请领装箱提交只有抵质押品的情况 100029
	 */

	/**
	 * 请领装箱提交
	 *arg0=corpId  904050100  目标机构
	 arg1=userId  100029 登陆用户id
	 arg2=clearTaskNum  RW1000020220225113658 清分任务编号
	 arg3=passboxNums ZH000009ZZ-ZH000011ZZ- 周转箱(袋)编号 （要与下面的一次性锁编号的顺序一一对应）
	 arg4=bundleNums 111-2222- 一次性锁扣编号
	 arg5=cvounWds DZXD1211202202250002-  订单编号(清分任务下面的)

	 * @return
	 * @throws Exception
	 */
	public boolean submitZzxInfoOnlyDZ(String cordid, String userId, String clearTaskNum, String passboxNums, String bundleNums,
								 String cvounWds) throws Exception {
		Log.e(TAG,"新方法");
		System.out.println("cordid=" + cordid);
		System.out.println("userid=" + userId);
		System.out.println("clearTaskNum=" + clearTaskNum);
		System.out.println("passboxNums=" + passboxNums);
		System.out.println("bundleNums=" + bundleNums);
		System.out.println("cvounWds=" + cvounWds);
		String methodName = "setZhuangxiangSubmit";// 接口方法
		WebParameter[] param = {
				new WebParameter<String>("arg0", cordid),
				new WebParameter<String>("arg1", userId),
				new WebParameter<String>("arg2", clearTaskNum),
				new WebParameter<String>("arg3", passboxNums),
				new WebParameter<String>("arg4", bundleNums),
				new WebParameter<String>("arg5", cvounWds) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
//		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.url19);// 根据路径获得返回值  使用清分管理员角色
		soap = WebServiceFromThree.getSoapObjectDZLiabraryMangerbyThree(methodName, param, FixationValue.NAMESPACE, FixationValue.url19);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		System.out.println("code=" + code);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param peisongId
	 * @return
	 * @throws Exception
	 */
	public String isCanSubmit(String peisongId) throws Exception {
		String methodName = "getQingfendengji";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", peisongId) };// 配送单ID
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 配送单的单号
	 * 
	 * @param peisongId
	 * @return
	 * @throws Exception lc 2018-12-11
	 */
	public String isCanSubmitlc(String peisongId) throws Exception {
		String methodName = "getQingfendengji";// 接口方法90405010020181228064844
		WebParameter[] param = { new WebParameter<String>("arg0", peisongId) };// 配送单ID
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 需要打印出抵制押品已打印袋子号列表
	 * 
	 * @param peisongId
	 * @return
	 * @throws Exception
	 */

	public String isPrintbagnumberlist(String psdId) throws Exception {
		String methodName = "savePrint";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", psdId) };// 配送单ID
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 将清点的数据提交至服务器，服务器对数据审核，来判断清分员是否需要复清
	 * 
	 * @param peisongId 配送单ID
	 * @param xianjin   现金msg
	 * @param zhongkong 重空凭证msg
	 * @param dizhi     抵质押品msg
	 * @return boolean 00为成功,返回true,99为失败,返回false
	 * @throws Exception
	 */
	public boolean submitShangjiao(String renwudan, String peisongId, String user, String xianjin, String zhongkong,
			String dizhi) throws Exception {
		String methodName = "setQingfenContrast";// 接口方法
		System.out.println("user:" + user);
		WebParameter[] param = { new WebParameter<String>("arg0", renwudan), // 任务单号
				new WebParameter<String>("arg1", peisongId), // 配送单ID
				new WebParameter<String>("arg2", user), // 登录用户帐号
				new WebParameter<String>("arg3", xianjin), // 现金数据
				new WebParameter<String>("arg4", zhongkong), // 重空凭证
				new WebParameter<String>("arg5", dizhi) };// 抵质押品
		SoapObject soap = null;// 创建返回值接收对象

		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);
		// 根据路径获得返回值
		System.out.println("soap--->" + soap);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			System.out.println("msg--->true-->" + msg);
			return true;
		} else {
			System.out.println("msg--->flase-->" + msg);
			return false;
		}
	}

	/**
	 * 获取清分员小组
	 * 
	 * @param userId 新添接口
	 * @return
	 * @throws Exception
	 */
	public String getQingfenXiaozu(String userId) throws Exception {
		String methodName = "getQingfenXiaozu";// 接口方法
		System.out.print("userId="+userId);
		WebParameter[] param = { new WebParameter<String>("arg0", userId) };// 清分员用户id
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 查询周转箱是否可用
	 * 
	 * @param zzxId
	 * @return
	 * @throws Exception
	 */
	public String getZzxShifoukeyong(String zzxId) throws Exception {
		String methodName = "getZzxShifoukeyong";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", zzxId) };// 周转箱id
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) { // yes: 可用 no1:已被使用 no2: 无效周转箱 null：周转箱为空
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 查询券别信息
	 * 
	 * @param zzxId
	 * @return
	 * @throws Exception
	 */
	public String getQuanbieList() throws Exception {
		String methodName = "getQuanbieList";// 接口方法

		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, null, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {
			return params;
		} else {
			return null;
		}
	}

	/**
	 * 获取重空信息集合
	 * 
	 * @param zzxId
	 * @return
	 * @throws Exception
	 */
	public String getZhongkongList() throws Exception {
		String methodName = "getZhongkongList";// 接口方法

		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, null, FixationValue.NAMESPACE, FixationValue.URL3);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 添加现金中控和抵制押品获取数据
	 * 
	 * @throws Exception 1
	 */
	public String getReceive(String taskId) throws Exception {
		String methodName = "getReceive";// 接口方法
		System.out.println("taskId-->:" + taskId);
		SoapObject soap = null;// 创建返回值接收对象
		WebParameter[] param = { new WebParameter<String>("arg0", taskId) };// 周转箱id
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);
		// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 向后台传送订单号 三个或几个单 查询
	 * 
	 * @param inputParams
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public String getParamsbynum(String inputParams, String methodName) throws Exception {
//	     String methodName ="getloadNum";// 接口方法
		WebParameter[] param = { new WebParameter<String>("arg0", inputParams) };// 传入参数
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();

		System.out.println("msg:" + msg + " params:" + params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 向后台传送订单号
	 * 
	 * @param Taskid
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUncheckCovuns(String Taskid) throws Exception {
		String methodName = "getUncheckCovuns";// 接口方法
		System.out.println("taskId-->:" + Taskid);
		SoapObject soap = null;// 创建返回值接收对象
		WebParameter[] param = { new WebParameter<String>("arg0", Taskid) };// 周转箱id
		soap = WebServiceFromThree.getSoapObject(methodName, param, FixationValue.NAMESPACEZH, FixationValue.URL9);
		// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {
			return params;
		} else {
			return null;
		}
	}

	/***
	 * 获取打印机的的数据类型  20200607
	 * lianchao
	 * 	arg0 : 外包清分人员id
	 * 此处代码返回应放在params 但是在messge 中
	 * 202008011放开
	 */
	public String  getPrinInfo(String cleanId) throws Exception{
		String methodName ="collateralPrinterList";// 接口方法
		System.out.println("taskId-->:"+cleanId);
		SoapObject soap = null;// 创建返回值接收对象
		WebParameter[] param = { new WebParameter<String>("arg0", cleanId) };//周转箱id
		soap = WebServiceFromThree.getSoapObject(methodName, param,
				FixationValue.NAMESPACEZH, FixationValue.URL9);
		// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		if ("00".equals(code)) {
			return msg;
		} else {
			return null;
		}
	}
	/***
	 * 需要打印出抵制押品已打印袋子号列表
	 * @param peisongId
	 * @return
	 * @throws Exception
	 *
	 * 派工单和打印机的id
	 * 20200201
	 */

	public String isPrintbagnumberlist(String psdId,String printroobortid) throws Exception {
		String methodName = "savePrint";// 接口方法
		Log.d(TAG,"_____======"+"arg0="+psdId);
		Log.d(TAG,"_____===arg1==="+printroobortid);
		System.out.println("arg0="+psdId);
		System.out.println("arg1="+printroobortid);
		WebParameter[] param = {
				new WebParameter<String>("arg0", psdId),// 配送单ID
				new WebParameter<String>("arg1", printroobortid)};
		SoapObject soap = null;// 创建返回值接收对象
		soap = WebServiceFromThree.getSoapObject(methodName, param,
				FixationValue.NAMESPACEZH, FixationValue.URL9);// 根据路径获得返回值
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:"+params);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return params;
		} else {
			return msg;
		}
	}
	//http://localhost:8089/cash/webservice/cash_csk/collateralHandOverClearResultAndCheck?arg0=RW0000020211013102604&arg1=90202010020211013102356&arg2=100029&arg3=asdqwbianhao:1085DY000152_1085DY000153

//2021.10.13 抵质押品打印新接口   100029  抵质押品管库员 归王姐所有

	/***
	 *
	 * @param RW0000020211013102604
	 * @param 90202010020211013102356
	 * @param 100029
	 * @param asdqwbianhao
	 * @return
	 * @throws Exception
	 */
	public boolean collateralHandOverClearResultAndCheck(String taskno,String psno,String controlleruserno,String dznos ) throws Exception {
		String methodName = "collateralHandOverClearResultAndCheck";// 接口方法
		Log.d(TAG, "======" + "arg0=" + taskno);
		Log.d(TAG, "===arg1===" + psno);
		Log.d(TAG, "======" + "arg2=" + controlleruserno);
		Log.d(TAG, "===arg3===" + dznos);
		WebParameter[] param = {
				new WebParameter<String>("arg0", taskno),// 配送单ID
				new WebParameter<String>("arg1", psno),
				new WebParameter<String>("arg2", controlleruserno),// 配送单ID
				new WebParameter<String>("arg3", dznos)};
		SoapObject soap = null;// 创建返回值接收对象
//		soap = WebService.getSoapObjectDZLiabraryManger(methodName, param,
//				FixationValue.NAMESPACEZH, FixationValue.url19);// 根据路径获得返回值
		soap = WebServiceFromThree.getSoapObjectDZLiabraryMangerbyThree(methodName, param,
				FixationValue.NAMESPACEZH, FixationValue.url19);
		String code = soap.getProperty("code").toString();
		String msg = soap.getProperty("msg").toString();
		String params = soap.getProperty("params").toString();
		System.out.println("params-->:" + params);
		System.out.println("code=" + code);
		if ("00".equals(code)) {// code=00->成功,code=99->失败
			return true;
		} else {
			return false;
		}
	}
}
