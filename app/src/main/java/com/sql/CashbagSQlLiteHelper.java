package com.sql;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static cn.poka.util.CrashHandler.TAG;

/**
 * Created by Administrator on 2020/12/15.
 * <p>
 * 钞袋  的sql 代码h
 */

public class CashbagSQlLiteHelper extends SQLiteOpenHelper {
    public static final String TAG = "CashbagSQlLiteHelper";
    public static final String DB_NAME01 = "cashbg_db";//数据库名字
//    public static final String DB_NAMEINFo = "cashbaginfo_db";//  数据库详细
    public static String TABLE_NAME01 = "cashbg";// 表名
    public static String FIELD_ID = "cashbgno";// 列名

//    public static String cashbginfo = "cashbginfo";// 表名
//    public static String cashbgno = "cashbgno";// 表名
//    public static String cashbgcbno = "cashbgcbno";//  表明 钞袋编号
//    public static String cashbgcbversion = "cashbgcbversion";//  表明 banbie
//    public static String cashbgcbtype = "cashbgcbtype";//  表明 券别
//    public static String cashbgcbstate = "cashbgcbstate";//  表明 残损
//    public static String cashbgcbcount = "cashbgcbcount";//  表明 数量
//    public static String cashbgcbmoney = "cashbgcbmoney";//  表明 钱数


    private static final int DB_VERSION = 1;   // 数据库版本

    public CashbagSQlLiteHelper(Context context) {
        super(context, DB_NAME01, null, DB_VERSION);
    }
//    public CashbagSQlLiteHelper(Context context,String ss) {
//        super(context, DB_NAMEINFo, null, DB_VERSION);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME01 + "(" + FIELD_ID + " integer primary key autoincrement );";
//        String sqlinfo = "CREATE TABLE " + cashbginfo + "(" + cashbgcbno + " integer primary key autoincrement, " +
//                cashbgcbversion + "text not null," + cashbgcbtype + "text not null,"+cashbgcbstate+"text not null,"+
//                cashbgcbcount+"text not null,"+cashbgcbmoney+"text not null);";
        try {
            db.execSQL(sql);
//            db.execSQL(sqlinfo);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + TABLE_NAME01 + "Error" + e.toString());
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
