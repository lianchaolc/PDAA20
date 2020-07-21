package com.ljsw.tjbankpad.baggingin.activity;

import android.os.Handler;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.strings.tocase.CaseString;

import hdjc.rfid.operator.INotify;

/***
 * 网点选择中的fragrenment 中的抵质押品的扫描 lc 20190417
 * 
 * @author Administrator
 *
 */
public class QualitativeWareScanningbyDiZhi implements INotify {
	private Handler handler;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
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
			// o_Application.DiZhiYaPin_bool=true;
			o_Application.qlruku.getZhouzhuanxiang().remove(aa);// 左侧集合删除
			o_Application.numberlist.add(str);// 右侧集合添加

			if (str.equals("") || str == null) {
				Log.e("AAAAA", "不做");
			} else {
				// 向扫面到集合添加0415
				Mapplication.getApplication().boxremberNumberDizhi.clear();// 全局变量先清空在和原数据源进行对比
				if (!(Mapplication.getApplication().boxremberNumberDizhi.contains(str))) {
					for (int i = 0; i < Mapplication.getApplication().boxremberDizhi.size(); i++) {
						for (int j = 0; j < o_Application.numberlist.size(); j++) {
							if (Mapplication.getApplication().boxremberDizhi.get(i)
									.equals(o_Application.numberlist.get(j)))
								;
							if (!(Mapplication.getApplication().boxremberNumberDizhi
									.contains(Mapplication.getApplication().boxremberDizhi.get(i)))) {

								if (!(Mapplication.getApplication().boxremberNumberDizhi
										.contains(o_Application.numberlist.get(j)))) {
									Mapplication.getApplication().boxremberNumberDizhi
											.add(o_Application.numberlist.get(j));// 用存放扫描时一半未扫描和一半已经扫描的数据
								}
								if (!(Mapplication.getApplication().boxremberDizhi1
										.contains(o_Application.numberlist.get(j)))) {
									Mapplication.getApplication().boxremberDizhi1.add(o_Application.numberlist.get(j));// 用存放扫描时一半未扫描和一半已经扫描的数据
								}

							}
						}

					}
				}
				Log.e("AAAAA", "不做::::::" + Mapplication.getApplication().boxremberNumberDizhi.size());
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
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
