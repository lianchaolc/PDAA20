package com.imple.getnumber;

import hdjc.rfid.operator.INotify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetMoneyNum implements INotify {
    // 获取钱捆编号

    public static Handler handler;
    public static List<String> list = new ArrayList<String>(); // 更新显示编号
    public static List<String> list_save = new ArrayList<String>(); // 保存已扫过的编号
    Message m;
    public static int money;

    /**
     * @param number 钞捆编号
     */
    @Override
    public void getNumber(String number) {
        Log.e("!!number!!!!!", number);
        number = number.trim();
        Log.i("number", number);
        if (number == null || !checkNum(number)) {
            return;
        }
        if (number.length() == 24) {

            String
                    checknum = number.substring(9, 10);
            if (checknum.equals("A")) {
                //老版本  020400019A21412032039123

                if (number == null || !checkNum(number)) {
                    Log.i("老版本", number);

                    return;
                } else {
                    Log.e("老版本", "加入");
                    m = handler.obtainMessage();
                    if (!list_save.contains(number)) {
                        m.what = 1;
                        list.add(number);
                        list_save.add(number);
                        moneytoal(); // 算总金额
                        Bundle b = new Bundle();
                        b.putString("money", number);
                        b.putInt("moneyToal", money);
                        m.setData(b);
                        handler.sendMessage(m);
                    }
                }
            } else if (checknum.equals("0") || checknum.equals("1") ||
                    checknum.equals("2") || checknum.equals("3") ||
                    checknum.equals("4") || checknum.equals("5") || checknum.equals("6") || checknum.equals("F")) {//新版本
//新版本 正则

                if (number == null || !checkNumroul(number)) {
                    Log.e("新版规则", "错误");
                    return;
                } else {
                    Log.e("新版规则", "加入");
                    m = handler.obtainMessage();
                    if (!list_save.contains(number)) {
                        m.what = 1;
                        list.add(number);
                        list_save.add(number);
                        moneytoal(); // 算总金额
                        Bundle b = new Bundle();
                        b.putString("money", number);
                        b.putInt("moneyToal", money);
                        m.setData(b);
                        handler.sendMessage(m);
                    }
                }

            } else {

                Log.d("非法标签跳出", "");
                if (number == null) {
                    Log.i("非法标签跳出", number);
                    return;
                }
            }
        }

    }

    // 计算钱捆总金额
    public static void moneytoal() {
        money = 0;
        String moneyNum;
        for (int i = 0; i < list.size(); i++) {
            try {
                Log.e("获得的数据", list.get(i).toString());
                moneyNum = list.get(i).substring(8, 10);
                String moneyNumrule = list.get(i).substring(list.get(i).length() - 1, list.get(i).length());// 新规则名字
                Log.e("获得的字符串最后一位", "" + moneyNumrule + "");

                if (moneyNum.equals("5A")) {
                    money = money + 100000;
                } else if (moneyNum.equals("9A")) {
                    money = money + 50000;
                    // 新增金额
                } else if (moneyNum.equals("6A")) {
                    money = money + 150000;

                } else if (moneyNum.equals("7A")) {
                    money = money + 200000;
                } else if (moneyNum.equals("8A")) {
                    money = money + 250000;
                } else if (moneyNumrule.equals("1")) {
                    money = money + 50000;
                } else if (moneyNumrule.equals("2")) {
                    money = money + 100000;
                } else if (moneyNumrule.equals("3")) {
                    money = money + 150000;
                } else if (moneyNumrule.equals("4")) {
                    money = money + 200000;
                } else if (moneyNumrule.equals("5")) {
                    money = money + 250000;
                } else if (moneyNumrule.equals("6")) {
                    money = money + 300000;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

    }

    private boolean checkNum(String num) {
        String reg = "^[0-9]{9}[A]{1}[0-9]{14}$";
        boolean boo = (num.matches(reg));
        return boo;
    }

    /***
     * 增加了新规则 20211230 修改2022.2.18
     */
    private boolean checkNumroul(String num) {
        String reg = "^[0-9]{8}[A-F]{1}[1-5,F]{1}[0-9,A-C]{1}[0-9]{12}[1-6]{1}$";
        boolean boo = (num.matches(reg));
        return boo;
    }
}
