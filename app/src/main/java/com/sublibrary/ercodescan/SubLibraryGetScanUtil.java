package com.sublibrary.ercodescan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hdjc.rfid.operator.INotify;

/**
 * Created by Administrator on 2020/12/23.
 * <p>
 * 分库下配合打印机的工具类主要作用   调用二维码串口扫描  拿到二维信息并且处理验证
 */

public class SubLibraryGetScanUtil implements INotify {
    // 获取钱捆编号

    public static Handler handler;
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
        if (number == null || number.equals("")) {
            return;
        } else {
            m = handler.obtainMessage();
            m.what = 1;
            Bundle b = new Bundle();
            b.putString("money", number);
            b.putInt("moneyToal", money);
            m.setData(b);
                handler.sendMessage(m);

        }


    }


    private boolean checkNum(String num) {
        String reg = "^[0-9]{9}[A-Z]{2}[0-9]{14}$";
        boolean boo = (num.matches(reg));
        return boo;
    }
}
