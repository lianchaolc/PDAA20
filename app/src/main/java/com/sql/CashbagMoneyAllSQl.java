package com.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sql.entity.CashBagCheckEntity;
import com.sql.entity.CashMoneyInfoEntity;

import java.util.ArrayList;
import java.util.List;

import static com.sql.CashbagAllSQlLiteHelper.TABLE_NAME2;
import static com.sql.CashbagAllSQlLiteHelper.money_all;
import static com.sql.CashbagAllSQlLiteHelper.money_couts;
import static com.sql.CashbagInfoSQlLiteHelper.TABLE_NAME1;
import static com.sql.CashbagInfoSQlLiteHelper.cashbginfono;
import static com.sql.CashbagInfoSQlLiteHelper.cmoney;
import static com.sql.CashbagInfoSQlLiteHelper.couts;
import static com.sql.CashbagInfoSQlLiteHelper.state;
import static com.sql.CashbagInfoSQlLiteHelper.type;
import static com.sql.CashbagInfoSQlLiteHelper.version;

/**
 * Created by Administrator on 2020/12/15.
 */

public class CashbagMoneyAllSQl {

    CashbagAllSQlLiteHelper helperinfoallmoney;
    SQLiteDatabase sqLiteDatabaseinfo;

    public CashbagMoneyAllSQl(Context context) {
        helperinfoallmoney = new CashbagAllSQlLiteHelper(context);
        sqLiteDatabaseinfo = helperinfoallmoney.getReadableDatabase();
    }

//    public static String TABLE_NAME1 = "tableallmoney";// 表名
//
//    public static String cashbginfomoney = "cashbginfomoney";//字段名称
//    public static String money_no = "money_no";//  字段名称 钞袋编号
//    public static String money_couts = "money_couts";//  字段名称 数量
//    public static String money_all = "money_all";//  字段名称 钱数

    /***
     * 明细 数据的添加
     * @param name
     */
    public void addinfo(String arg1, String arg2) {
        SQLiteDatabase db = helperinfoallmoney.getWritableDatabase();
        List<CashMoneyInfoEntity> selectCashMoneyInfoEntitylist = new ArrayList<>();

        if (db.isOpen()) {

            Cursor cursor = db.query(TABLE_NAME2, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                CashMoneyInfoEntity CashMoneyInfoEntity = new CashMoneyInfoEntity();
                CashMoneyInfoEntity.setMoney_couts(cursor.getString(cursor.getColumnIndex(money_couts)));
                CashMoneyInfoEntity.setMoney_all(cursor.getString(cursor.getColumnIndex(money_all)));
                selectCashMoneyInfoEntitylist.add(CashMoneyInfoEntity);
            }
                if (selectCashMoneyInfoEntitylist.contains(arg1)) {

                    ContentValues values = new ContentValues();
                    values.put(money_all, arg2);//key为字段名，value为值
                    db.update(TABLE_NAME2, values, "money_all=?", new String[]{money_all});

                    db.close();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(money_couts, arg1);
                    values.put(money_all, arg2);
                    db.insert(TABLE_NAME2, null, values);//返回新添记录的行号，与主键id无
                }

//            ContentValues values = new ContentValues();
//            values.clear();


//            values.put(money_couts, arg1);
//            values.put(money_all, arg2);
//            //不允许插入一个空值,如果contentvalue,一般第二个参
//            db.insert(TABLE_NAME1, null, values);//通过组拼完成的添加的操作
        }
        db.close();
    }

    /***'
     * 查询所有的
     */
    List<CashMoneyInfoEntity> selectlist = new ArrayList<>();

    public List<CashMoneyInfoEntity> selectinfo() {
        selectlist.clear();
        SQLiteDatabase db = helperinfoallmoney.getWritableDatabase();
        SQLiteDatabase db1 = helperinfoallmoney.getReadableDatabase();

        if (db1.isOpen()) {

            Cursor cursor = db1.query(TABLE_NAME2, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                CashMoneyInfoEntity CashMoneyInfoEntity = new CashMoneyInfoEntity();
                CashMoneyInfoEntity.setMoney_couts(cursor.getString(cursor.getColumnIndex(money_couts)));
                CashMoneyInfoEntity.setMoney_all(cursor.getString(cursor.getColumnIndex(money_all)));
                selectlist.add(CashMoneyInfoEntity);
            }
            cursor.close();
            db.close();
        }
        return selectlist;
    }
//    public static String no = "no";//  表明 钞袋编号
//    public static String version = "version";//  表明 banbie
//    public static String type = "type";//  表明 券别
//    public static String state = "state";//  表明 残损
//    public static String couts = "moneycouts";//  表明 数量
//    public static String money = "cmoney";//  表明 钱数

    /***
     * 删除
     * @param
     * 版别
     * 券别
     * 状态
     *
     *     public static String money_no = "money_no";//  字段名称 钞袋编号
    public static String money_couts = "money_couts";//  字段名称 数量
    public static String money_all = "money_all";//  字段名称 钱数
     */
    public void deleteinfo(String cashbgcbversion, String cashbgcbtype, String cashbgcbstate) {
        SQLiteDatabase db = helperinfoallmoney.getWritableDatabase();
        selectlist.clear();
        boolean sss = false;
//        SQLiteDatabase db = helperinfo.getReadableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME2, "version=?and type=? and state=?",
                    new String[]{cashbgcbversion, cashbgcbtype, cashbgcbstate});
            Cursor cursor = db.query(TABLE_NAME1, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                CashMoneyInfoEntity CashMoneyInfoEntity = new CashMoneyInfoEntity();
                CashMoneyInfoEntity.setMoney_couts(cursor.getString(cursor.getColumnIndex(money_couts)));
                CashMoneyInfoEntity.setMoney_all(cursor.getString(cursor.getColumnIndex(money_all)));
                selectlist.add(CashMoneyInfoEntity);

            }
            db.close();
        }
    }

    /***
     * 删除 数据库 中的所有数据
     * delete from TableName;  //清空数据
     * update sqlite_sequence SET seq = 0 where name ='TableName';//自增长ID为0
     */

    public void deleteinfoall() {
        SQLiteDatabase db = helperinfoallmoney.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("delete from " + TABLE_NAME2);
            db.close();


        }
    }
}
