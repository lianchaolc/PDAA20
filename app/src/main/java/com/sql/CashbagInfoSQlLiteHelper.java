package com.sql;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2020/12/15.
 * 创建 一个录入明细表  包含  id  自增（两种方式是插入还是到了2*意义）hou 重新开始
 * 2   钞袋   券别 版别 残损  数量  整钞/尾零
 * 3 提交时候注意有个对比  本地和后台要数据一致才可以提交
 * 4 录入时候也需要和后台返给的数据做对比
 * 5 写入sql 后需要先查询后添加
 *
 * 6.1 总表 需要累加的sql
 * 删除时候注意输入的
 */

public class CashbagInfoSQlLiteHelper extends SQLiteOpenHelper {


    private static final String DB_NAMEINFo = "infob_db";//  数据库详细
    private static final String TAG = "CashbagInfoSQlLiteHelper";

    public static String TABLE_NAME1 = "tablecdinfoc";// 表名

    public static String cashbginfono = "cashbginfono";//字段名称
    public static String no = "no";//  字段名称 钞袋编号
    public static String version = "version";//  表明 banbie
    public static String type = "type";//  字段名称 券别
    public static String state = "state";//  字段名称 残损
    public static String couts = "moneycouts";//  字段名称 数量
    public static String cmoney = "money";//  字段名称 钱数
    private static final int DB_VERSION = 2;   // 数据库版本

    public CashbagInfoSQlLiteHelper(Context context) {
        super(context, DB_NAMEINFo, null, DB_VERSION);
    }

    //    public CashbagInfoSQlLiteHelper(Context context,String ss) {
//        super(context, DB_NAMEINFo, null, DB_VERSION);
//    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sqlinfo = "CREATE TABLE " + cashbginfo + "(" + no + " integer primary key autoincrement, " +
//                version + "text not null," + type + "text not null,"+state+"text not null,"+
//                couts+"text not null,"+money+"text not null)";
//        String sqlinfo = "CREATE TABLE " + TABLE_NAME + "(" + no + " integer primary key autoincrement, " +
//                version + "text not null," + type + "text not null,"+state+"text not null,"+
//                couts+"text not null,"+money+"text not null" +
//                ")";

        String sqlinfo =
//                = "CREATE TABLE " + TABLE_NAME1 + "(" + no + " integer primary key autoincrement ,
// "+version +"VARCHAR(100))";
                "CREATE TABLE "
                        + TABLE_NAME1 + "(" + no
                        + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
                        + cashbginfono + " text,"
                        + version + " text,"
                        + type + " text,"
                        + state + " text,"
                        + couts + " text ,"
                        + cmoney + " text" + ")";

        try {
            db.execSQL(sqlinfo);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + "Error" + e.toString());
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
