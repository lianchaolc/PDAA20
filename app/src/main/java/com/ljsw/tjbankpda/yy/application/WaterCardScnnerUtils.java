package com.ljsw.tjbankpda.yy.application;

import hdjc.rfid.operator.INotify;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.strings.tocase.CaseString;

/***
 * 
 * @author Administrator 廉超水牌的扫描规则
 */
public class WaterCardScnnerUtils implements INotify {

	public Handler handler;
	private List<String> list_ishave = new ArrayList<String>(); // 存入已扫描的周转箱
	public static boolean isfind;// 扫描开关,true-开,false- 关
	Bundle bun;

	@Override
	public void getNumber(String number) {
		String nowDate = S_application.getApplication().bianhao;
		String newDate = CaseString.getWaterCard2(number);
		if (!newDate.equals(nowDate)) {
			if (number == null || !CaseString.reg(number)) {
				return;
			}
			// 十进制转换成字符
			S_application.getApplication().bianhao = CaseString.getWaterCard2(number);
			System.out.println("扫描的结果:" + S_application.getApplication().bianhao);
			Message m = handler.obtainMessage();
			bun = new Bundle();
			bun.putString("box", newDate);
			m.setData(bun);
			m.what = 1;
			handler.sendMessage(m);
		}

	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
