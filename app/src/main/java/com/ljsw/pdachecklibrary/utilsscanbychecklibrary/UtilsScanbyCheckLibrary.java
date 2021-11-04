package com.ljsw.pdachecklibrary.utilsscanbychecklibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.strings.tocase.CaseString;

import java.text.SimpleDateFormat;
import java.util.Date;

import hdjc.rfid.operator.INotify;
import hdjc.rfid.operator.RFID_Device;

/**
 * Created by Administrator on 2021/8/26.
 * 查库工具类
 */

public class UtilsScanbyCheckLibrary implements INotify {
    private static final String TAG = "UtilsScanbyCheckLibrary";
    private Handler handler;
    boolean flag = true;
    String str;
    Bundle bundle;
    @SuppressLint("LongLogTag")
    @Override
    public void getNumber(final String number) {

        if (number == null) {
            return;
        }
        str = number;
        str = CaseString.getBoxNumbycollateral(number);

        System.out.println("--------------" + str);
        // 过滤
        if (!o_Application.guolv.contains(str)) {
            o_Application.guolv.add(str);
            o_Application.guolv.clear();// 天加到集合后就是错误的号所以以后再也扫描不到了跳出 当前注销0190304
        } else {
            return;
        }
        int aa = -1;
        for (int i = 0; i < o_Application.qlruku.getZhouzhuanxiang().size(); i++) {
            if (o_Application.qlruku.getZhouzhuanxiang().get(i).equals(str)) {
                aa = i;
                break;
            }
        }
        System.out.println("=============扫描完成，aa为：" + aa);
        if (aa != -1) {
//             o_Application.DiZhiYaPin_bool=true;
            o_Application.qlruku.getZhouzhuanxiang().remove(aa);// 左侧集合删除
            o_Application.numberlist.add(str);// 右侧集合添加
            if (bundle == null) {
                bundle = new Bundle();
            }
            Message m = Message.obtain();
            bundle.putString("number", str);
            m.setData(bundle);

            if (str.equals("") || str == null) {
                Log.e("AAAAA", "不做");
                return;
            } else {
                // 向扫面到集合添加0415 不加入这段代码切换左右一已扫描会就丢失数据2021.8.17

            }


        } else {
            for (int i = 0; i < o_Application.numberlist.size(); i++) {
                if (o_Application.numberlist.get(i).equals(str)) {
                    o_Application.wrong = "";
                    flag = false;
                    break;
                }
            }
            if (flag) {
                o_Application.wrong = str + "-错误箱号";
            }
        }
        flag = true;

        handler.sendEmptyMessage(776);
        handler.sendEmptyMessage(777);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void stoHander(Handler handler) {
        this.handler = handler;
        handler.removeCallbacksAndMessages(null);
    }
}
