package com.service;

public class FixationValue {

    // 地址空间
    public static String NAMESPACE = "http://service.timer.cashman.poka.cn/";
    // 地址空间 赵辉
    public static String NAMESPACEZH = "http://service.pda.cashman.poka.cn";
    // http://192.168.1.110:9080/webcash0908/webservice
// 	public static String url = "http://192.168.1.113:8080/CashWebServices/webservice";
// 	public static String url = "http://192.168.1.132:8088/CashWebServices/webservice";
// 	public static String url_zh = "http://192.168.1.100:8081/cash/webservice";
//	public static String url_zxw ="http://192.168.1.114:8089/cash";
//	public static String url_zh = "http://192.168.1.123:8080/cash/webservice";// 更改了
//	public static String url_zh = "http://192.168.1.138:8080/cash/webservice";
//	自己本机

//	public static String url_zh = "http://192.168.1.131:8089/cashman/webservice";//上线需要改
//	public static String url_zh = "http://192.168.1.103:8089/cashman/webservice";//上线需要改
//	public static String url_zh = "http://172.16.3.1:8888/cashman/webservice";   //邱工二期及以前
//	public static String url_zh = "http://192.168.1.182:8080/cash/webservice";
//	public static String url_zh = "http://192.168.1.131:8088/Cash20201124/webservice";//
public static String url_zh = "http://361de15631.wicp.vip/webcash/webservice";  //  gaohogn
//    	public static String url_zh = "http://3212g13a06.zicp.vip/cashman/webservice";//
//    public static String url_zh = "http://192.168.1.145:8081/cashman/webservice/";//

//	http://192.168.1.131:8088/Cash20201124/webservice/ckc/getMoneyEditionList
//	public static String url_zls="http://192.168.1.161:8081/cash/webservice";//  昝老师的url
    // 此处修改138:8081/

    public static String url = "http://192.168.1.132:8089/CashWebServices/webservice";
    // 	public static String url = "http://192.168.1.131:8080/CashWebServices/webservice";
// 	public static String url = "http://172.16.3.1:8888/cashman/webservice"; /// 邱工 三期上线地址
    // webservice地址
    public static String URL = url + "/cash_pdaHDHE";// ATM钞箱模块以及 登录、指纹验证接口 对应 IPdaOperateService.java接口//改  7./22
    //	public static String URL = url + "/cash_pda";// ATM钞箱模块以及 登录、指纹验证接口 对应 IPdaOperateService.java接口
    public static String URL2 = url + "/cash_boxHDHE";// 款箱模块（早送、晚收） 对应 IPdaOperateService.java接口	//改  7./22
    //	public static String URL2 = url + "/cash_box";// 款箱模块（早送、晚收） 对应 IPdaOperateService.java接口	//改  7./22
    public static String URL3 = url + "/cash_cm"; // 对应请领上缴模块中的 清分员操作 对应 ICleaningManService.java

    public static String URL4 = url + "/cash_cmanagement";// 对应请领上缴模块中的 清分管理员操作 对应ICleaningManagementService.java

    public static String URL5 = url + "/cash_kuguanyuan";// 对应请领上缴模块中的 押运员操作 对应IKuGuanYuanService.java

    public static String URL6 = url_zh + "/pda";// 对应请领上缴模块中的 押运员操作 对应GetResistCollateralBaggingService.java
    public static String URL7 = url_zh + "/account";// 对应证账户资料账户中心入库 AccountInformationService
    public static String URL8 = url_zh + "/ckc"; // 查库车查库和盘库
    public static String URL9 = url_zh + "/cash_cm"; // 查询上缴清分接口
    public static String URL10 = url_zh + "/cash_cm"; // 查询上缴清分接口
    public static String url11 = url_zh + "/escort";// 押运员查询自己是否有任务
    public static String URL17 = url_zh + "/cash_sk";// 库管员和外包清分人员提交 抵制账户资料现金等等 20200318

// 	public static  String  url12="http://192.168.1.182:8080/cash/webservice"+"/cd";//  钞袋下的数据提交华徐冰后台
// 	public static String  url13="http://192.168.1.182:8080/cash/webservice"+"/ckc";
// 	public static  String  url12="http://192.168.41.6:8080/cashman/webservice"+"/cd";//  钞袋下的数据提交华徐冰后台

    public static String url15 = url_zh + "/cd";
    public static String url16 = url_zh + "/ckc";
    public static String url18 = url_zh + "/clearingCheck"; ///  清分管理员核对和url

    public static  String url19= url_zh+"/cash_csk";//  抵质押品管库员 2021.9.16
    public static  String url20= url+"/cash_pda";//  离行下的账号密码登录前获取plnumid

    // 钞箱操作类型参数

    // 钞箱操作类型参数
    public static final String TYPE_EMPTYBOXOUT = "02"; // 空钞箱出库
    public static final String TYPE_ATM = "04"; // ATM加钞出库
    public static final String TYPE_PUTIN = "03"; // 钞箱装钞入库
    public static final String TYPE_BACKBOX = "05"; // 回收钞箱入库
    public static final String TYPE_ADDMONEY = "03"; // 钞箱加钞
    /**
     * ADD BY WANGMENG 2017-07-11 按照整条线路进行出库
     */
    public static final String TYPE_NOTCLEAROUT = "00"; // 未清回收钞箱出库
    public static final int AccountCenter = 27;// 账户中心管理员
    public static final int locationmanger = 26; // 货位管理ID //改动
    public static final int supercargo = 9; // 押运员角色ID 原来是8
    public static final int warehouse = 4; // 库管员角色ID
    public static final int clearer = 7; // 清分员角色ID
    public static final int webuser = 5; // 网点人员角色ID 原来这个ID是6，因为后来要求作出修改，把6改成了5，实为加钞人员，只改了值，名称固定不变
    public static final int examine = 3; // 审核员角色ID
    public static final int waibaoqingfen = 19; // 外包清分员
    public static final String waibaoQingfenString = waibaoqingfen + "";
    public static final int cleanmanger = 29; // 抵质押品管库员管库
    public static int PRESS = 3; // 指纹验证次数


//	http://3212g13a06.zicp.vip/cashman/webservice/pda/selectTwoCode?arg0=43534&arg1=DZ20200922145910285
}
