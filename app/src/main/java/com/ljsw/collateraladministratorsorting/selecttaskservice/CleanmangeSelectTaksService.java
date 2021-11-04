package com.ljsw.collateraladministratorsorting.selecttaskservice;


import com.ljsw.tjbankpda.util.WebServiceFromThree;
import com.service.FixationValue;
import com.example.app.entity.WebParameter;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by Administrator on 2021/8/31.
 * <p>
 * 抵质押品清分员清分
 */

public class CleanmangeSelectTaksService {
    private static final String TAG = "CleanmangeSelectTaksService";

    /***1
     *  登录人操作人登录账号查询数据抵质押品的任务List
     *  返回所有的上缴和请领的任务号  实质是把 原来的一个接口做分成两个来做
     *    * 获取清分员任务
     * @return
     * @throws Exception
     *
     */
    public String getChceckLibraryNum(String ControllerUser) throws Exception {
//    public String getQingfenRenwu(String userId) throws Exception {   //原方法
        String methodName = "getChceckLibraryNum";// 接口方法
     WebParameter[] param = { new WebParameter<String>("arg0", ControllerUser) };// 传入参数
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
    /***
     * submitShangjiaobyDZ 100029
     * 老接口
     * 不能用于新的接口这里提交的只有抵质押品的数据   农商行王姐专用
     * 2021.9.27
     */

    public boolean submitShangjiaobyDZ(String renwudan, String peisongId, String user, String xianjin, String zhongkong,
                                       String dizhi) throws Exception {
        String methodName = "setQingfenContrast";// 接口方法
        System.out.println("user:" + user);
        WebParameter[] param = { new WebParameter<String>("arg0", renwudan), // 任务单号
                new WebParameter<String>("arg1", peisongId), // 配送单ID
                new WebParameter<String>("arg2", user), // 登录用户帐号
//				new WebParameter<String>("arg3", xianjin), // 现金数据
//				new WebParameter<String>("arg4", zhongkong), // 重空凭证
//				new WebParameter<String>("arg5", dizhi)
        };// 抵质押品
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

}
