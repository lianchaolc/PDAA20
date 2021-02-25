package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.urils;

import android.os.Handler;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.strings.tocase.CaseString;

import hdjc.rfid.operator.INotify;

/**
 * Created by Administrator on 2021/2/7.
 *
 * 清分管理员 扫描周转箱工具类代码 20210208
 */

public class Scanutils_TurnOver implements INotify {

    public boolean isfind = true;
    public Handler handler;

    public void getNumber(String number) {
//        String nowDate = S_application.getApplication().bianhao;
        String newDate = CaseString.getBoxNum2(number);
//        if (!newDate.equals(nowDate)) {
            if (number == null ) {
                return;
            }
            // 十进制转换成字符
//            S_application.getApplication().bianhao = CaseString.getBoxNum2(number);

            System.out.println("--------------" + newDate);
            if (!o_Application.guolv.contains(newDate)) {
                o_Application.guolv.add(newDate);
//	        o_Application.guolv.clear();// 天加到集合后就是错误的号所以以后再也扫描不到了跳出 当前注销0190304
            } else {
                return;
            }
            int aa = -1;
            for (int i = 0; i < o_Application.qlruku.getZhouzhuanxiang().size(); i++) {
                if (o_Application.qlruku.getZhouzhuanxiang().get(i).equals(newDate)) {
                    aa = i;
                    break;
                }
            }
            System.out.println("=============扫描完成，aa为：" + aa);
            if (aa != -1) {
                o_Application.qlruku.getZhouzhuanxiang().remove(aa);// 左侧集合删除
                if (!(o_Application.numberlist.contains(newDate))) {
                    o_Application.numberlist.add(newDate);// 向集合添加数据
                }
//			o_Application.numberlist.add(str);// 右侧集合添加改动
                if (newDate.equals("") || newDate == null) {
                    Log.e("AAAAA", "不做");
                } else {

                }

            } else {
                for (int i = 0; i < o_Application.numberlist.size(); i++) {
                    if (o_Application.numberlist.get(i).equals(newDate)) {
                        o_Application.wrong = "";
                        isfind = false;
                        break;
                    }
                }
                if (isfind) {
                    o_Application.wrong = newDate + "-错误箱号";
                }
            }
            isfind = true;
            handler.sendEmptyMessage(0);
        }

//        System.out.println("=============扫描完成，集合长度：" + S_application.getApplication().rightlist.size());
//			setHandler(handler);
//			Message m = handler.obtainMessage();
//			m.what = 1;
//            isfind = true;
//            handler.sendEmptyMessage(1);
//    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
