package com.ljsw.tjbankpad.baggingin.activity.zhuangdai;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.strings.tocase.CaseString;

import java.text.SimpleDateFormat;
import java.util.Date;

import hdjc.rfid.operator.INotify;


/**
 * Created by Administrator on 2021/8/18.
 */

public class QualitativeWareScanningbyDiZhinewThread implements INotify {

    private static final String TAG ="QualitativeWareScanningbyDiZhinewThread" ;
    private Handler handler;
    boolean flag = true;
    String str;

    @SuppressLint("LongLogTag")
    @Override
    public void getNumber(final String number) {
                Log.d(TAG, "==========InventoryThread============getNumber+" + Thread.currentThread().getName());
                if (number == null) {
                    return;
                }
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
                    o_Application.qlruku.getZhouzhuanxiang().remove(aa);// 左侧集合删除
                    o_Application.numberlist.add(str);// 右侧集合添加
//                    handler.sendEmptyMessage(10086);

                    if (str.equals("") || str == null) {
                        Log.e("AAAAA", "不做");
                        return;
                    } else {
                        // 向扫面到集合添加0415 不加入这段代码切换左右一已扫描会就丢失数据2021.8.17
                        Mapplication.getApplication().boxremberNumberDizhi.clear();// 全局变量先清空在和原数据源进行对比
                        Log.e("QualitativeWareScanningbyDiZhi", "对!!boxremberNumberDizhi.size()"+
                                Mapplication.getApplication().boxremberNumberDizhi.size());
                        if (!(Mapplication.getApplication().boxremberNumberDizhi.contains(str))) {
					for (int i = 0; i < Mapplication.getApplication().boxremberDizhi.size(); i++) {
                            for (int j = 0; j < o_Application.numberlist.size(); j++) {
                                if (Mapplication.getApplication().boxremberDizhi.contains(o_Application.numberlist.get(j))){

                                    Log.e("QualitativeWareScanningbyDiZhi", "对比数据w位置a"+j);
							if (!(Mapplication.getApplication().boxremberNumberDizhi
									.contains(Mapplication.getApplication().boxremberDizhi.get(i)))) {

                                    if (!(Mapplication.getApplication().boxremberNumberDizhi
                                            .contains(o_Application.numberlist.get(j)))) {
                                        Mapplication.getApplication().boxremberNumberDizhi
                                                .add(o_Application.numberlist.get(j));// 用存放扫描时一半未扫描和一半已经扫描的数据
//									Log.e("QualitativeWareScanningbyDiZhi", "对比数据位置B"+i);
                                    }
                                    if (!(Mapplication.getApplication().boxremberDizhi1
                                            .contains(o_Application.numberlist.get(j)))) {
                                        Mapplication.getApplication().boxremberDizhi1.add(o_Application.numberlist.get(j));// 用存放扫描时一半未扫描和一半已经扫描的数据
                                        Log.e("QualitativeWareScanningbyDiZhi", "对比数据C"+j);
                                    }

							}
                                }

//                                Log.e(TAG,"打印时间循环结束"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS") .format(new Date() ));
                            }
                        }
                    }
                    Log.e("AAA", "boxremberNumberDizhi不做:::" + Mapplication.getApplication().boxremberNumberDizhi.size());
                    Log.e("AAAAA", "不做::::::" + Mapplication.getApplication().boxremberDizhi1.size());

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
                handler.sendEmptyMessage(0);
                handler.sendEmptyMessage(1);
            }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    public  void  stoHander(Handler handler){
        this.handler = handler;
        handler.removeCallbacksAndMessages(null);
    }

}
