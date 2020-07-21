package com.ljsw.tjbankpda.db.biz;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.strings.tocase.CaseString;

import android.os.Handler;
import hdjc.rfid.operator.INotify;

/**
 * lc 2019-3-27 账户资料扫描工具了 之前的代码是周转箱和盘查库的规则在 账户资料中不能使用
 * 
 * @author Administrator 仿写清零入库扫描
 *
 */
public class AccountDataScanningUntil implements INotify {
	private Handler handler;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
		if (number == null) {
			return;
		}
		String numbercode = number.substring(0, 4);
		if (numbercode.equals("9072")) {
			if (number.length() > 17) {
				str = number;
				str = CaseString.getAccountDataScan(number);

				System.out.println("--------------" + str);
				// 过滤
				if (!o_Application.guolv.contains(str)) {
					o_Application.guolv.add(str);
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
		}
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}