package com.ljsw.tjbankpad.baggingin.activity;

import android.os.Handler;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.strings.tocase.CaseString;

import hdjc.rfid.operator.INotify;

/***
 * lc 2018_12-16 抵质押品的号码改变了书写规则需要更改
 * 
 * @author Administrator eg DZ20181211123123
 */
public class QualitativeWareScanning implements INotify {
	private Handler handler;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
		if (number == null) {
			return;
		}

		String numbercode = number.substring(0, 4);
		if (numbercode.equals("6890")) {
			if (number.length() > 17) {
				str = number;
				str = CaseString.getBoxNumbycollateral(number);

				System.out.println("--------------" + str);
				// 过滤
				if (!o_Application.guolv.contains(str)) {
					o_Application.guolv.add(str);
					o_Application.guolv.clear();// 天加到集合后就是错误的号所以以后再也扫描不到了跳出
												// 当前注销0190304
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
					// if(!(Mapplication.getApplication().boxremberDizhi1.contains(str))){
					// Mapplication.getApplication().boxremberDizhi1.add(str);
					// }

					if (str.equals("") || str == null) {
						// if(Mapplication.getApplication().boxremberNumberDizhi.contains(str));
						Log.e("AAAAA", "不做");
					} else {

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
				System.out.println("--------------" + str);
			} else {

			}
		}
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
