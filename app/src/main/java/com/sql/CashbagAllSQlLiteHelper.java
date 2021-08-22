package com.sql;
/***
 * baocun 保存 第三张表的sql 实现版别 和钱数代码
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.util.Log;

/**
 * Created by Administrator on 2020/12/17.
 */

public class CashbagAllSQlLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "CashbagInfoSQlLiteHelper";
    private static final String DB_NAMEINFo = "allmoney_db";//  数据库详细

    public static String TABLE_NAME2 = "tableallmoney";// 表名

    public static String cashbginfomoney = "cashbginfomoney";//字段名称
    public static String money_no = "money_no";//  字段名称 钞袋编号
    public static String money_couts = "money_couts";//  字段名称 数量
    public static String money_all = "money_all";//  字段名称 钱数
    private static final int DB_VERSION = 2;   // 数据库版本

    public CashbagAllSQlLiteHelper(Context context) {
        super(context, DB_NAMEINFo, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlinfo =
                "CREATE TABLE "
                        + TABLE_NAME2 + "(" + cashbginfomoney
                        + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
                        + money_no + " text,"
                        + money_couts + " text" + ")";

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
