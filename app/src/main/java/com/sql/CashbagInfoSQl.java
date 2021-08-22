package com.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sql.entity.CashBagCheckEntity;

import java.util.ArrayList;
import java.util.List;

import static com.sql.CashbagInfoSQlLiteHelper.TABLE_NAME1;
import static com.sql.CashbagInfoSQlLiteHelper.cashbginfono;
import static com.sql.CashbagInfoSQlLiteHelper.cmoney;
import static com.sql.CashbagInfoSQlLiteHelper.couts;
import static com.sql.CashbagInfoSQlLiteHelper.no;
import static com.sql.CashbagInfoSQlLiteHelper.state;
import static com.sql.CashbagInfoSQlLiteHelper.type;
import static com.sql.CashbagInfoSQlLiteHelper.version;

/**
 * Created by Administrator on 2020/12/15.
 */

public class CashbagInfoSQl {

    CashbagInfoSQlLiteHelper helperinfo;
    SQLiteDatabase sqLiteDatabaseinfo;

    public CashbagInfoSQl(Context context) {
        helperinfo = new CashbagInfoSQlLiteHelper(context);
        sqLiteDatabaseinfo = helperinfo.getReadableDatabase();
    }


    /***
     * 明细 数据的添加
     public static String cashbgcbno = "cashbgcbno";//  表明 钞袋编号
     public static String cashbgcbversion = "cashbgcbversion";//  表明 banbie
     public static String cashbgcbtype = "cashbgcbtype";//  表明 券别
     public static String cashbgcbstate = "cashbgcbstate";//  表明 残损
     public static String cashbgcbcount = "cashbgcbcount";//  表明 数量
     public static String cashbgcbmoney = "cashbgcbmoney";//  表明 钱数

     int count = Db.update("UPDATE BO_ACT_DPM_MONTHDMDSUMDET SET MONTHDMD =333"

     + " WHERE CAMPAIGN = ? AND DEMANDID = ? ",

     "201901", "bindid");
     * @param name
     */
    List<CashBagCheckEntity> selectlist1 = new ArrayList<>();
    public void addinfo(String cashbgcbno1, String cashbgcbversion1, String cashbgcbtype1,
                        String cashbgcbstate, String cashbgcbcount, String cashbgcbmoney) {
        SQLiteDatabase db = helperinfo.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.clear();
//            values.put(cashbginfono, cashbgcbno1);
//            values.put(version, cashbgcbversion1);
//            values.put(type, cashbgcbtype1);
//            values.put(state, cashbgcbstate);
//            values.put(couts, cashbgcbcount);
//            values.put(cmoney, cashbgcbmoney);
//            //不允许插入一个空值,如果contentvalue,一般第二个参
//            db.insert(TABLE_NAME1, null, values);//通过组拼完成的添加的操作


            Cursor cursor = db.query(TABLE_NAME1, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                CashBagCheckEntity CashBagCheckEntity = new CashBagCheckEntity();
                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex(cashbginfono)));
                CashBagCheckEntity.setCashbgcbversion(cursor.getString(cursor.getColumnIndex(version)));
                CashBagCheckEntity.setCashbgcbtype(cursor.getString(cursor.getColumnIndex(type)));
                CashBagCheckEntity.setCashbgcbstate(cursor.getString(cursor.getColumnIndex(state)));
                CashBagCheckEntity.setCashbgcbcount(cursor.getString(cursor.getColumnIndex(couts)));
                CashBagCheckEntity.setCashbgcbmoney(cursor.getString(cursor.getColumnIndex(cmoney)));
                selectlist1.add(CashBagCheckEntity);
            }


            if (selectlist1.size()==0) {
                values.put(cashbginfono, cashbgcbno1);
                values.put(version, cashbgcbversion1);
                values.put(type, cashbgcbtype1);
                values.put(state, cashbgcbstate);
                values.put(couts, cashbgcbcount);
                values.put(cmoney, cashbgcbmoney);
                db.insert(TABLE_NAME1, null, values);//返回新添记录的行号，与主键id无
            } else {
                for (int i = 0; i < selectlist1.size(); i++) {


                    if (selectlist1.get(i).getCashbgcbversion().contains(cashbgcbversion1) &&
                            selectlist1.get(i).getCashbgcbtype().contains(cashbgcbtype1)
                            && selectlist1.get(i).getCashbgcbstate().contains(cashbgcbstate)) {
                        db.update(TABLE_NAME1, values, "cmoney=? ", new String[]{cashbgcbmoney});
                    }else{
//                        values.put(cashbginfono, cashbgcbno1);
//                        values.put(version, cashbgcbversion1);
//                        values.put(type, cashbgcbtype1);
//                        values.put(state, cashbgcbstate);
//                        values.put(couts, cashbgcbcount);
//                        values.put(cmoney, cashbgcbmoney);
//                        db.insert(TABLE_NAME1,null,values);
                    }
                }

            }

        }
        db.close();
    }

    /***'
     * 查询所有的
     */
    List<CashBagCheckEntity> selectlist = new ArrayList<>();

    public List<CashBagCheckEntity> selectinfo() {
        selectlist.clear();
        SQLiteDatabase db = helperinfo.getWritableDatabase();
        SQLiteDatabase db1 = helperinfo.getReadableDatabase();

        if (db1.isOpen()) {

            Cursor cursor = db1.query(TABLE_NAME1, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Log.e("11111111", cursor.getString(cursor.getColumnIndex(type)));
                Log.e("11111111", cursor.getString(cursor.getColumnIndex(state)));
                CashBagCheckEntity CashBagCheckEntity = new CashBagCheckEntity();
                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex(cashbginfono)));
                CashBagCheckEntity.setCashbgcbversion(cursor.getString(cursor.getColumnIndex(version)));
                CashBagCheckEntity.setCashbgcbtype(cursor.getString(cursor.getColumnIndex(type)));
                CashBagCheckEntity.setCashbgcbstate(cursor.getString(cursor.getColumnIndex(state)));
                CashBagCheckEntity.setCashbgcbcount(cursor.getString(cursor.getColumnIndex(couts)));
                CashBagCheckEntity.setCashbgcbmoney(cursor.getString(cursor.getColumnIndex(cmoney)));
                selectlist.add(CashBagCheckEntity);
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
     */
    public void deleteinfo(String cashbgcbversion, String cashbgcbtype, String cashbgcbstate) {
        SQLiteDatabase db = helperinfo.getWritableDatabase();
        boolean sss = false;
//        SQLiteDatabase db = helperinfo.getReadableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME1, "version=?and type=? and state=?",
                    new String[]{cashbgcbversion, cashbgcbtype, cashbgcbstate});
//+type+"="+cashbgcbtype+state+"="+cashbgcbstate'
//            db.execSQL("delete from "+TABLE_NAME1+"WHERE"+ "version=" +"2005");
            Cursor cursor = db.query(TABLE_NAME1, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Log.e("11111111", cursor.getString(cursor.getColumnIndex(type)));
                Log.e("11111111", cursor.getString(cursor.getColumnIndex(state)));
                CashBagCheckEntity CashBagCheckEntity = new CashBagCheckEntity();
                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex(cashbginfono)));
                CashBagCheckEntity.setCashbgcbversion(cursor.getString(cursor.getColumnIndex(version)));
                CashBagCheckEntity.setCashbgcbtype(cursor.getString(cursor.getColumnIndex(type)));
                CashBagCheckEntity.setCashbgcbstate(cursor.getString(cursor.getColumnIndex(state)));
                CashBagCheckEntity.setCashbgcbcount(cursor.getString(cursor.getColumnIndex(couts)));
                CashBagCheckEntity.setCashbgcbmoney(cursor.getString(cursor.getColumnIndex(cmoney)));
                selectlist.add(CashBagCheckEntity);

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
        SQLiteDatabase db = helperinfo.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("delete from " + TABLE_NAME1);
//            db.delete("cashbginfo",null,null);
            db.close();


        }
    }
}
