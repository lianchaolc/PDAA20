package com.ljsw.tjbankpda.db.biz;

import android.os.Handler;

import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.strings.tocase.CaseString;

import hdjc.rfid.operator.INotify;

public class QingLingRuKuSaoMiao implements INotify {
	private Handler handler;
//	private Handler handler1;
	boolean flag = true;
	String str;

	@Override
	public void getNumber(String number) {
		if (number == null || !CaseString.reg(number)) {
			return;
		}
		str = number;
		str = CaseString.getBoxNum2(number);

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

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 抵制押品
	 */
//	@Override
//	public void getNumber1(String number) {
//		System.out.println("--------------" + "抵制押品方法");
//		// TODO Auto-generated method stub
//		if(number==null || !CaseString.reg(number)){
//			return;
//		 }
//		str=CaseString.getBoxNum2(number);
//		
//		System.out.println("--------------" + str);
//		// 过滤
//		if (!o_Application.guolv.contains(str)) {
//			o_Application.guolv.add(str);
//		} else {
//			return;
//		}
//		int aa = -1;
//		for (int i = 0; i < o_Application.qlruku1.getBoxList().size(); i++) {
//			if (o_Application.qlruku1.getBoxList().get(i).equals(str)) {
//				aa = i;
//				break;
//			}
//		}
//		System.out.println("=============扫描完成，aa为：" + aa);
//		if (aa != -1) {
//			// o_Application.DiZhiYaPin_bool=true;
//			o_Application.qlruku1.getBoxList().remove(aa);// 左侧集合删除
//			o_Application.numberlist1.add(str);// 右侧集合添加
//			
//		} else {
//			for (int i = 0; i < o_Application.numberlist1.size(); i++) {
//				if(o_Application.numberlist1.get(i).equals(str)){
//					o_Application.wrong = "";
//					flag = false;
//					break;
//				}
//			}
//			if(flag){
//				o_Application.wrong = str + "-错误箱号";
//			}
//		}
//		flag = true;
//		handler1.sendEmptyMessage(0);	
//	}
//	public void setHandler1(Handler handler1){
//		this.handler1=handler1;
//	}
}
