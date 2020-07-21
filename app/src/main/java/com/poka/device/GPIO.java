package com.poka.device;

import afu.message.SingleChipModule;
import afu.util.DeviceInfo;
import android.os.RemoteException;
import android.util.Log;
import poka_global_constant.GlobalConstant;

public class GPIO {
//    private static GPIO getgpio;
	private int ioID;

	/**
	 * 那个设备
	 * 
	 * @param typeId
	 */
	public GPIO(int typeId) {
		ioID = typeId * 10;
	}

	/**
	 * 打开对应的设备的开关（供电）
	 * 
	 * @param val
	 */
	public void setGPIO(int val) {
		// LR_TODO: 2020/1/11 liu_rui 设备开关未做处理
		int gpioGetVal = ioID + val;
		Log.i("模块电源和使能", "setGPIO: 模块：" + gpioGetVal / 10 + "\t是否打开：" + gpioGetVal % 10);
	}
}
