package com.ljsw.tjbankpda.yy.application;

import java.util.ArrayList;
import java.util.List;

import com.entity.SystemUser;

import android.app.Application;

/**
 * 2021.7.27   未知 扫描类application
 * 当前类为辅助类  不存登录信息只存 交接时用户信息
 */
public class S_application {
    public static S_application s_application;
    public static String wrong;// 扫面的中间变量 | 密码提示信息
    public static String bianhao;// 扫面的周转箱中间变量
    public static String bianhaobyDZyapinguanliyuan;// 扫面的周转箱中间变量
//	public static String yayunOrizatuion;

    public static List<String> leftlist = new ArrayList<String>();// 左款箱码集合
    public static List<String> rightlist = new ArrayList<String>();// 右款箱码集合

    public static List<String> qfjhdlist = new ArrayList<String>();// 清分计划单
    public static List<String> qfrqlist = new ArrayList<String>();// 清分日期

    public static List<String> ywcjhdlist = new ArrayList<String>();// 任务单号 备注:清分已完成任务单列表信息 包括 任务单号，负责小组，该小组所装周转箱编号
    public static List<String> ywcjhdzmlist = new ArrayList<String>();// 小组组名 备注:清分已完成任务单列表信息
    public static List<String> ywcjhdzidlist = new ArrayList<String>();// 小组id 备注:清分已完成任务单列表信息
    public static List<String> ywcjhdzzbhlist = new ArrayList<String>();// 周转箱编号 备注:清分已完成任务单列表信息

    public static List<String> wwcjhdzmlist = new ArrayList<String>();// 小组组名 备注 :清分未完成任务单列表信息
    public static List<String> wwcjhdzidlist = new ArrayList<String>();// 小组ID 备注 :清分未完成任务单列表信息
    public static List<String> wwcjhdlist = new ArrayList<String>();// 任务单号 备注 :清分未完成任务单列表信息
    // 指纹验证用
    public static SystemUser left_user;
    public static SystemUser right_user;

    public static String wangdianJigouId;// 网点人员验证指纹时需要的机构Id 可变的
    public static String sjpaigongdan; // 押运员上缴派工单
    public static String jhdId;// 计划单号

    public static String s_zzxShangjiao; // 上缴周转箱
    public static String s_zzxQingling; // 请领周转箱
    public static String s_userWangdian; // 网点或库管员id
    public static String s_userYayunName; // 押运员名称
    public static String s_userYayun; // 押运员Id
    public static String s_userguankuyaun; // 管库员Id l c
    public static String s_useraccountuserid; // Id l c


    //	账户中心账户中心人员 启用与2021.7.27
    public static String s_zhanghuzhonginguanliyaun; // 账户中心管理员lc
    public static String s_zhanghuzhonginguanliId; // 账户中心机构id lc
    public static String s_zhanghuzhongxinguanliname; // 账户中心管理员
    //交接时候启用的管库员变量
    public static String s_librarycenthander_yaun; // 管库员lc
    public static String s_librarycenthander_Id; // 管库员机构id lc
    public static String s_librarycenthander_name; // 管库员名称

    // 押运员 账号和密码非系统登录认证局部业务操作变量
    public static String s_userbycar_yaun; // 押运员管理员lc
    public static String s_userbycar_yaun_Id; // 押运员机构id lc
    public static String userbycar_yaun_name; //押运员名称
    public static String userbycar_yaun_pwd; //押运员密码

    public static String s_yayunJigouId; // 押运员机构Id
    public static String s_qlpaigongdan; // 请领派工单
    public static int jiaojieType; // 交接类型 1.总行押运与分库库管 2分行押运为网点人员 3总行押运与网点人员
    public static String s_yayunXianluId; // 押运线路id
// 交接的 抵质押品管库员
    public static String s_librarydz_yaun; // 管库员lc
    public static String s_librarydzr_Id; // 管库员机构id lc
    public static String s_librarydz_name; // 管库员名称
    public S_application() {
    }

    public static S_application getApplication() {
        if (s_application == null) {
            s_application = new S_application();
        }
        return s_application;
    }
}
