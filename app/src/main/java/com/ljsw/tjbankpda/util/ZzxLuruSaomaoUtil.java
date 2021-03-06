package com.ljsw.tjbankpda.util;

import java.util.ArrayList;
import java.util.List;

import com.strings.tocase.CaseString;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import hdjc.rfid.operator.INotify;

public class ZzxLuruSaomaoUtil implements INotify {
	private Handler handler;
	private String nowNumber = "";
	private String newNumber;
	private Bundle bundle;

	@Override
	public void getNumber(String number) {
		if (number == null) {
			return;
		}
		System.out.println("扫到的标签为=" + number + "/" + CaseString.getBoxNum2(number));
		newNumber = number;

		if (nowNumber.equals(newNumber)) {
			return;
		}
		if ("Ѐ".equals(CaseString.getBoxNum2(newNumber))) {
			return;
		}
		if (number.length() < 14) {
			return;
		}

		// 如果包含就跳出如果不包含添加

		nowNumber = newNumber;
		bundle = new Bundle();
		Message m = new Message();
		String boxNum = CaseString.getBoxNum2(number);
		bundle.putString("Num", boxNum);
		m.setData(bundle);
		m.what = 1;
		handler.sendMessage(m);

	}

	/**
	 * 重置
	 */
	public void zhikong() {
		nowNumber = "";
	}

	public void setHand(Handler handler) {
		System.out.println("!!!!!!!!!!!测试线程卡顿");
		this.handler = handler;
	}

}
