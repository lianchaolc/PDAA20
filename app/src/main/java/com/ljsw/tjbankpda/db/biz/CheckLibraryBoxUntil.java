package com.ljsw.tjbankpda.db.biz;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.pda.checksupplement.activity.ChecklibraryReplenishmentActivity;
import com.strings.tocase.CaseString;

import hdjc.rfid.operator.INotify;

/****
 * 盘查库过滤的工具类
 * 
 * @author Administrator 廉chao2019——312
 */
public class CheckLibraryBoxUntil implements INotify {
	private Handler handler;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
		if (number == null || !CaseString.reg(number)) {
			return;
		}
		str = number;

		// /查库车扫描标签号和周转箱的不同有新规则 所判断处理
		String charfistfour = number.substring(0, 4);
		if (charfistfour.equals("6768")) { // 查看是否满足6768
			str = CaseString.turnToBanknoteBagNum(number);// 盘查库
			if (o_Application.numberlist.contains(str)) {
				return;
			} else {
				if (!(o_Application.numberlist.contains(str))) {
					o_Application.numberlist.add(str);// 向集合添加数据
				}
			}

			System.out.println("--------------" + str);

		}
		handler.sendEmptyMessage(0);
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
