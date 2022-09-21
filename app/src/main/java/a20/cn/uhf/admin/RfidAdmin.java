package a20.cn.uhf.admin;

import com.handheld.UHF.UhfManager;
import com.poka.device.GPIO;
import hdjc.rfid.operator.RFID_Device;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.ListView;
import a20.android_serialport_api.SerialPort;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android_rfid_control.powercontrol;
import poka_global_constant.GlobalConstant;


public class RfidAdmin {

	private Context context;
	private boolean runFlag = true;
	private boolean startFlag = false;
	// private boolean connectFlag = false;
	private UhfManager manager;
	private SerialPort uhfSerialPort;// ����Ƶ����
	private static final String TAG = "MainActivityRFID";
	public Handler hand = null;
	Bundle bundle;
	public       boolean   booleanstrnewThread=false;// 判断走那个扫描
	public RfidAdmin() {
		if (facilityGPIO == null)
			facilityGPIO = new GPIO(GlobalConstant.IO_RFID_POWER);
	};

	public RfidAdmin(Context context) {
		this.context = context;
		if (facilityGPIO == null)
			facilityGPIO = new GPIO(GlobalConstant.IO_RFID_POWER);
	};

	private GPIO facilityGPIO = null;
//	private powercontrol rFidPowerControl;//yang

	public void setHandler(Handler hand) {
		if (this.hand == null) {
			this.hand = hand;
		}
	}

	public void openRfid() {
		startFlag = true;
		runFlag = true;

//		rFidPowerControl=new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		//===================================
		facilityGPIO.setGPIO(GlobalConstant.ENABLE_IO);
		/*
		 * ��ȡreaderʱ���д��ڵĳ�ʼ������ ��readerΪnull���򴮿ڳ�ʼ��ʧ��
		 * 
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			return;
		}

		Thread thread = new InventoryThread();
		thread.start();
		Log.i("扫描线程启动", "扫描线程启动");
		// ��ʼ��������
		// Util.initSoundPool(context);
	}

//	public void stopRfid(){
//		startFlag = false;
//	}
//
//	public void startRfid(){
//		startFlag = true ;
//	}
//
//	public void closeRfid(){
//		runFlag = false;
//		startFlag = false;
//		if(reader != null){
//			manager.close();
//			//�˳�ʱ�ر�
//			reader.close();
//		}
//		//=========================�ص�RFID
//		rFidPowerControl.rfidPowerctlSetSleep(1);
//		rFidPowerControl.rfidPowerctlClose();
//	}
	/***
	 * 抵制押品
	 */

	public void stopRfid() {
		startFlag = false;
		booleanstrnewThread=false;
	}

	public void startRfid() {
		startFlag = true;
	}

	public void closeRfid() {
		Log.d("liu_rui", "closeRfid: 关闭RFID循环扫描");
		runFlag = false;
		startFlag = false;
		if (manager != null) {
			manager.close();
		}
		// =========================�ص�RFID
//		rFidPowerControl.rfidPowerctlSetSleep(1);
//		rFidPowerControl.rfidPowerctlClose();
		facilityGPIO.setGPIO(GlobalConstant.DISABLE_IO);
	}

	/**
	 * �̴��߳�
	 * 
	 * @author Administrator
	 *
	 */
	class InventoryThread extends Thread {
		private List<byte[]> epcList;

		@Override
		public void run() {
			super.run();
			try {
				while (runFlag) {
					if (startFlag) {
						Log.d(TAG, "==========InventoryThread============rfidadm" + Thread.currentThread().getName());
//					manager.stopInventoryMulti()
						epcList = manager.inventoryRealTime();

						if (epcList != null && !epcList.isEmpty()) {
							// ������ʾ��
							// Util.play(1, 0);
							for (byte[] epc : epcList) {
								String epcStr = Tools.Bytes2HexString(epc, epc.length);
								Log.i("A20扫描的编号------->", epcStr);
								Message m = Message.obtain();
								if (bundle == null) {
									bundle = new Bundle();
								}
								m.what = RFID_Device.rdid_a20;
								if(booleanstrnewThread){ //开启子线程fangfa2021.8.18
									m.what = RFID_Device.rdid_a20_newthread;
									Log.d(TAG, "==========InventoryThread============开启子线程"+Thread.currentThread().getName());
								}else{

								}
								bundle.putString("number", epcStr);
								m.setData(bundle);
								Log.i("hand", hand + "");
								hand.sendMessage(m);


							}
						}
						epcList = null;
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} catch (Exception e) {
				booleanstrnewThread=false;
				Log.e(TAG, "run: RfidAdmin 串口关闭后获取数据异常");
			}
		}
	}

}
