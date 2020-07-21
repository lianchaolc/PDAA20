package com.ljsw.tjbankpad.baggingin.activity.handover.saomiaoutil;

import hdjc.rfid.operator.INotify;
import android.os.Handler;
import android.util.Log;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.strings.tocase.CaseString;

/****
 * homemangerTocentersaca 库管员扫描装袋的规则 到清分的规则（单独外面的交接）扫描袋号的一个提交
 * 
 * @author Administrator CD000554190227093020 6768000554190227093020 4在显示钞袋号：
 * 
 */
public class CashtoPackgerUtils implements INotify {
	private Handler handler;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
		if (number == null) {
			return;
		}
		str = number;

		String charfistfour = number.substring(0, 4);
		if (charfistfour.equals("6768")) { // 查看是否满足6768
			str = CaseString.turnToPackgerHomeManagertoClean(number);// 盘查库
			if (o_Application.numberlist.contains(str)) {
				return;
			} else {
			}

			System.out.println("--------------" + str);
		}
		System.out.println("--------------" + str);
		// 过滤
		if (!o_Application.guolv.contains(str)) {
			o_Application.guolv.add(str);
//	        o_Application.guolv.clear();// 天加到集合后就是错误的号所以以后再也扫描不到了跳出 当前注销0190304
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
			if (!(o_Application.numberlist.contains(str))) {
				o_Application.numberlist.add(str);// 向集合添加数据
			}
//			o_Application.numberlist.add(str);// 右侧集合添加改动
			if (str.equals("") || str == null) {
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
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
