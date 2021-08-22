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
import static com.sql.CashbagSQlLiteHelper.DB_NAME01;
import static com.sql.CashbagSQlLiteHelper.FIELD_ID;
import static com.sql.CashbagSQlLiteHelper.TABLE_NAME01;

/**
 * Created by Administrator on 2020/12/15.
 * 数据库的操作类代码
 * <p>
 * https://www.cnblogs.com/amosli/p/3784998.html
 */
//  删除钞袋的列表的sql

public class CashbagSQl {


    CashbagSQlLiteHelper helper;
    SQLiteDatabase sqLiteDatabase;

//    CashbagInfoSQlLiteHelper helperinfo;
//    SQLiteDatabase sqLiteDatabaseinfo;

    public CashbagSQl(Context context) {
        helper = new CashbagSQlLiteHelper(context);
        sqLiteDatabase = helper.getReadableDatabase();
    }

//    public CashbagSQl(Context context, String ss) {
//        helperinfo = new CashbagInfoSQlLiteHelper(context);
//        sqLiteDatabaseinfo = helperinfo.getReadableDatabase();
//    }

    /**
     * execSQL()方法可以执行insert，update，delete语句
     * 实现对数据库的 增，删，改 功能
     * sql为操作语句 ， bindArgs为操作传递参数
     **/
    public boolean updateSQLite(String sql, Object[] bindArgs) {
        boolean isSuccess = false;
        try {
            sqLiteDatabase.execSQL(sql, bindArgs);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            Log.i("TAG:", "数据插入数据库中状态：" + isSuccess);
        }
        return isSuccess;
    }


    public void add(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
//        if (db.isOpen()) {
//            ContentValues values = new ContentValues();
//            values.put("cashbgno", name);
//            //不允许插入一个空值,如果contentvalue,一般第二个参
//            db.insert("cashbg", null, values);//通过组拼完成的添加的操作
//        }
//        db.close();
        List<String> resultcadailist = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME01, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String cdno = (cursor.getString(cursor.getColumnIndex(FIELD_ID)));
            resultcadailist.add(cdno);
        }
        if (resultcadailist.contains(name)) {
            ContentValues values = new ContentValues();
            values.put(FIELD_ID, name);//key为字段名，value为值
            db.update(TABLE_NAME01, values, FIELD_ID+"=?", new String[]{name});
        } else {
            ContentValues values = new ContentValues();
            values.put(FIELD_ID, name);
            db.insert(TABLE_NAME01, null, values);//返回新添记录的行号，与主键id无
        }
        db.close();

    }

    public List<String> select() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<String> persons = null;
        if (db.isOpen()) {
            persons = new ArrayList<String>();

            Cursor cursor = db.query("cashbg", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
//                person.setName(cursor.getString(cursor.getColumnIndex("name")));
//                person.setAge(cursor.getInt(cursor.getColumnIndex("age")));
//                persons.add(person);
                persons.add(cursor.getString(cursor.getColumnIndex("cashbgno")));
            }
            cursor.close();
            db.close();
        }
        return persons;
    }

    public void delete(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("cashbg", "cashbgno=?", new String[]{name});
            db.close();
        }
    }


    /***
     * 删除所有
     *
     */

    public void delete() {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {

            db.execSQL("delete from cashbg");
            db.close();
        }
    }
    /***
     * 更新数据库的钞袋的数据
     */
    public  long    updata(String cdno) {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("cashbgno", cdno);//key为字段名，value为值
//        if (db.isOpen()) {
//            db.update("cashbg", "cashbgno=?", new String[]{values})
//
//            db.close();
//        }
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cashbgno",cdno);
        long result = db.update("cashbg", values, "cashbgno=?", new String[]{cdno});
        db.close();
        return result;

    }
}



//
//    /***
//     * 明细 数据的添加
//     *     public static String cashbginfo = "cashbginfo";// 表名
//     public static String cashbgno = "cashbgno";// 表名
//     public static String cashbgcbno = "cashbgcbno";//  表明 钞袋编号
//     public static String cashbgcbversion = "cashbgcbversion";//  表明 banbie
//     public static String cashbgcbtype = "cashbgcbtype";//  表明 券别
//     public static String cashbgcbstate = "cashbgcbstate";//  表明 残损
//     public static String cashbgcbcount = "cashbgcbcount";//  表明 数量
//     public static String cashbgcbmoney = "cashbgcbmoney";//  表明 钱数
//     * @param name
//     */
//    public void addinfo(String cashbgcbno, String cashbgcbversion, String cashbgcbtype,
//                        String cashbgcbstate, String cashbgcbcount, String cashbgcbmoney) {
//        SQLiteDatabase db = helperinfo.getWritableDatabase();
//        if (db.isOpen()) {
//            ContentValues values = new ContentValues();
//            values.put("cashbgcbno", cashbgcbno);
//            values.put("cashbgcbversion", cashbgcbversion);
//            values.put("cashbgcbtype", cashbgcbtype);
//            values.put("cashbgcbstate", cashbgcbstate);
//            values.put("cashbgcbcount", cashbgcbcount);
//            values.put("cashbgcbmoney", cashbgcbmoney);
//            //不允许插入一个空值,如果contentvalue,一般第二个参
//            db.insert("cashbginfo", null, values);//通过组拼完成的添加的操作
//        }
//        db.close();
//    }
//
//    /***'
//     * 查询所有的
//     */
//    List<CashBagCheckEntity> selectlist = new ArrayList<>();
//
//    public List<CashBagCheckEntity> selectinfo(String name) {
//        selectlist.clear();
//        SQLiteDatabase db = helper.getWritableDatabase();
//        SQLiteDatabase db1 = helper.getReadableDatabase();
//
//        if (db1.isOpen()) {
//
//            Cursor cursor = db1.query("cashbginfo", null, null, null, null, null, null);
//
//            while (cursor.moveToNext()) {
//                CashBagCheckEntity CashBagCheckEntity = new CashBagCheckEntity();
//                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex("cashbgno")));
//                CashBagCheckEntity.setCashbgcbversion(cursor.getString(cursor.getColumnIndex("cashbgcbversion")));
//                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex("cashbgcbtype")));
//                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex("cashbgcbstate")));
//                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex("cashbgcbcount")));
//                CashBagCheckEntity.setCashbgcbno(cursor.getString(cursor.getColumnIndex("cashbgcbmoney")));
//                selectlist.add(CashBagCheckEntity);
//            }
//            cursor.close();
//            db.close();
//        }
//        return selectlist;
//    }
//
//    /***
//     * 删除
//     * @param
//     * banbie banbie
//     * banbie
//     * 版别
//     * 券别
//     * 状态
//     */
//    public void deleteinfo(String cashbgcbversion, String cashbgcbtype, String cashbgcbstate) {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        if (db.isOpen()) {
//            db.delete("cashbginfo", "cashbgcbversion=?and cashbgcbtype=? and cashbgcbtype=?",
//                    new String[]{cashbgcbversion,cashbgcbtype,cashbgcbstate});
//            db.close();
//        }
//    }
//}
