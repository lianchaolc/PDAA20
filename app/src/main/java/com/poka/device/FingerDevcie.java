package com.poka.device;

import afu.util.DeviceInfo;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass.Device;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.*;
import android.util.Log;
import android_serialport_api.SerialPort;
import com.example.app.activity.KuanXiangJiaoJieActivity;
import com.example.app.activity.KuguanDengluActivity;
import com.ljsw.tjbankpad.baggingin.activity.chuku.DiZhiYaPinChuKuZhiWenJiaoJie;
import com.ljsw.tjbankpda.db.activity.KuGuanLogin_db;
import com.ljsw.tjbankpda.db.activity.QingLingChuKuJiaoJie_db;
import com.ljsw.tjbankpda.main.QingfenDengLuAcyivity;
import com.ljsw.tjbankpda.yy.activity.YyrwJiaojieActivity;
import com.moneyboxadmin.pda.BankDoublePersonLogin;
import com.moneyboxadmin.pda.LihangJiachaoRenyuanJiaojie;
import poka_global_constant.GlobalConstant;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FingerDevcie extends Device {
//	private ElectricalAmperage electricalAmperage;
	private GPIO fingerPowerIO;
	private GPIO GPRSPowerIO;
	private FingerThread fingerThread;
	private Handler handler;
	private SerialPort serialPort;
	private FingerHandlerThread fingerHandlerThread;
	private static final String tag = "FingerDevcie";
	public static Handler fingerHandle;
	private Handler hand;

//	public ElectricalAmperage getElectricalAmperage() {
//		return electricalAmperage;
//	}
//
//	public void setElectricalAmperage(ElectricalAmperage electricalAmperage) {
//		this.electricalAmperage = electricalAmperage;
//	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	FingerDevcie() {
		super();
		fingerPowerIO = new GPIO(GlobalConstant.IO_AS602_POWER);
		fingerPowerIO.setGPIO(GlobalConstant.ENABLE_IO);
		fingerPowerIO.setGPIO(71);
		setFingerInit();
//		if (electricalAmperage == null) {
//			electricalAmperage = new ElectricalAmperage(null);
//		}
	}

	private void setFingerInit() {
		if (serialPort != null)
			return;
		File rFidFile = new File(DeviceInfo.MODULE_FINGER_FILE);
		try {
			this.serialPort = new SerialPort(rFidFile, DeviceInfo.MODULE_FINGER_KEY, 0);
		} catch (SecurityException | IOException e) {
			Log.e("FingerDevcie", "setFingerInit: " + e.getStackTrace());
		}
		if (fingerThread == null) { // ���ر�ָ��ģ��ע������
			fingerThread = new FingerThread();
			fingerThread.start();
			Log.i("ThreadManage", "fingerThread------------------------->start");
		}
		if (fingerHandlerThread == null) { // ���ر�ָ��ģ��ע������
			fingerHandlerThread = new FingerHandlerThread();
			fingerHandlerThread.start();
			Log.i("ThreadManage", "fingerHandlerThread------------------------->start");
		}
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void open() {
		try {
//			fingerPowerIO.setGPIO(GlobalConstant.ENABLE_IO);    // 打开就没关闭过
//			fingerPowerIO.setGPIO(71);
			System.out.println("fingerPower------------------up!");
			if (serialPort == null)
				setFingerInit();
			fingerThread.startThread();
			System.out.println("fingerThread---------------start!");
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public void fingerStop() {
		fingerThread.stopThread();
	}

	public void close() {
//		if (electricalAmperage != null) {
//			electricalAmperage.close();
//		}
		if (fingerThread != null) {
			fingerThread.stopThread();
			fingerThread.destoryThread();
			fingerThread = null;
		}
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
		if (fingerHandlerThread != null) {
			fingerHandle.removeCallbacksAndMessages(null);
			fingerHandle.getLooper().quit();
			fingerHandlerThread = null;
		}
		if (fingerPowerIO != null) {
			fingerPowerIO.setGPIO(GlobalConstant.DISABLE_IO);
			fingerPowerIO.setGPIO(70);
		}
	}

	private HashMap<String, Object> getFingerDataMap(byte[] data) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		switch (data[0]) {
		case 0:
			System.out.println("MD5...");
			hashMap.put("tag", "MD5");
			break;
		case 1:
			System.out.println("IVAL...");
			hashMap.put("tag", "IVAL");
			break;
		case 2:
			System.out.println("IMG0...");
			hashMap.put("tag", "IMG0");
			break;
		case 3:
			System.out.println("IMG1...");
			hashMap.put("tag", "IMG1");
			break;
		default:
			break;
		}
		ByteArrayInputStream bytes = new ByteArrayInputStream(data, 1, data.length - 1);
		hashMap.put("data", bytes);
		return hashMap;
	}

	class FingerHandlerThread extends Thread {
		@SuppressLint("HandlerLeak")
        @Override
		public void run() {
			super.run();
			Looper.prepare();
			fingerHandle = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// ------------------------->Ч����ݿ���ײ�ָ����ݶԱ�
					if (msg.what == 0x225) {

//						for (int i = 0; i < 3; i++) {
//							byte[] fingerData = (byte[]) msg.obj;
//							int result = SerialPort
//									.fingerVerify(fingerData, fingerData);
//							Log.d(tag, "fingerData result = " + result);
//							Message mg = Message.obtain();							
//							if ("MATCH_SUCCESS".equals(result.trim())) {
//								if (handler != null) {
//									mg.obj = result;
//									handler.sendMessage(mg);
//									break;
//								}
//							} else if ("MATCH_FAILED".equals(result.trim())) {
//								if (handler != null) {
//									mg.obj = result;
//									handler.sendMessage(mg);
//									break;
//								}
//							} else if ("VERIFY_ERROR".equals(result.trim())) {
//								continue;
//							} else if ("SENDOVER".equals(result.trim())) {
//								break;
//							} else if ("SENDNULL".equals(result.trim())) {
//								break;
//							}
//						}
					}
					// ------------------------->end
					Bundle bundle = msg.getData();
					if (bundle.getBoolean("pressing")) {
						if (handler != null) {
							Message mg = Message.obtain();
							mg.what = 0x222;
							mg.obj = "ready";
							handler.sendMessage(mg);
							Log.i("FingerDevcie", "---------->ready");
						}
						return;
					}
					byte[] fingerData = bundle.getByteArray("fingerData");
					System.out.println("fingerData[0] = " + fingerData[0]);

					switch (fingerData[0]) {
					case 0:
						System.out.println("MD5...");
						break;
					case 1:
						System.out.println("IVAL...");
						if (handler != null) {
							Message mg = Message.obtain();
							mg.what = 0x224;
							mg.obj = GlobalConstant.subBytes(fingerData, 1, 256);
							byte[] ival = (byte[]) msg.obj;

//							ShareUtil.ivalBack = ival;
//							System.out.println("***************************9"+ShareUtil.ivalBack);
							handler.sendMessage(mg);
							Log.i("liu_rui", "mg.obj=b1---------->IVAL---right");
							Log.i("FingerDevcie", "mg.obj=b1---------->IVAL---right");
						}
						break;
					case 2:
						Log.i("liu_rui", "mg.obj=b1---------->IMG0---right");
						System.out.println("IMG0...");
						int len = fingerData.length - 1;
						int[] colors = new int[len];
						for (int i = 0; i < len; i++) {
							byte a = fingerData[i + 1];
							colors[i] = (0xff << 24) + (a << 16) + (a << 8) + (a);
						}
						System.out.println("image 8bit to ARGB-8888 finish!");

						Bitmap img = Bitmap.createBitmap(colors, 76, 100, Config.ARGB_8888);
						Bitmap resizeBitmap = Bitmap.createScaledBitmap(img, 190, 250, true);
						Matrix matrix = new Matrix();
						int width = resizeBitmap.getWidth();
						int height = resizeBitmap.getHeight();
						float matrix_values1[] = { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
						float matrix_values2[] = { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
						matrix.setValues(matrix_values1);
						matrix.postScale(1, -1);
						Bitmap b = Bitmap.createBitmap(resizeBitmap, 0, 0, width, height, matrix, true);

						Matrix m = new Matrix();
						m.setValues(matrix_values2);
						m.postRotate(180);
						Bitmap b1 = Bitmap.createBitmap(b, 0, 0, width, height, m, true);

						ShareUtil.finger_gather = b1;
						Log.e("TAG", "chao----- 显示图片" + b1);

						if (LihangJiachaoRenyuanJiaojie.state == 1) {
							ShareUtil.finger_bitmap_right = b1;
						} else {
							ShareUtil.finger_bitmap_left = b1;
						}

						if (BankDoublePersonLogin.firstSuccess) {
							ShareUtil.finger_bitmap_right = b1;
						} else {
							ShareUtil.finger_bitmap_left = b1;
						}
						if (KuguanDengluActivity.firstSuccess) {
							ShareUtil.finger_bitmap_right = b1;
						} else {
							ShareUtil.finger_bitmap_left = b1;
						}
						if (KuanXiangJiaoJieActivity.firstSuccess) {
							ShareUtil.w_finger_bitmap_right = b1;
						} else {
							ShareUtil.w_finger_bitmap_left = b1;
						}
						if (KuGuanLogin_db.firstSuccess) {
							ShareUtil.finger_kuguandenglu_right = b1;
						} else {
							ShareUtil.finger_kuguandenglu_left = b1;
						}
						if (QingLingChuKuJiaoJie_db.firstSuccess) {
							ShareUtil.finger_jiaojie_qingfen_right = b1;
						} else {
							ShareUtil.finger_jiaojie_qingfen_left = b1;
						}
						if (QingfenDengLuAcyivity.firstSuccess) {
							ShareUtil.finger_qingfen_denglu_right = b1;
						} else {
							ShareUtil.finger_qingfen_denglu_left = b1;
						}
						if (YyrwJiaojieActivity.firstSuccess) {
							ShareUtil.finger_wangdian_right = b1;
						} else {
							ShareUtil.finger_wangdian_left = b1;
						}

						// 抵制押品的清分员lc 出现右手的特征值 图样不出现问题
						if (DiZhiYaPinChuKuZhiWenJiaoJie.firstSuccess) {
							ShareUtil.finger_bitmap_right = b1;
						} else {
							ShareUtil.finger_bitmap_left = b1;
						}

						b1 = null;
						if (handler != null) {
							Message mg = Message.obtain();
//							mg.what = MainActivityT.fingerBitmap;
							mg.obj = b1;
							handler.sendMessage(mg);
							Log.i("b1", b1 + "");
							Log.i("FingerDevcie", "mg.obj=b1---------->>bitmap---right");
						}
						b = null;
						resizeBitmap = null;
						img = null;

						break;
					case 3:
						System.out.println("IMG1...");
						break;
					case 4:
						System.out.println("image err!");
						break;
//					default:
//						System.out.println("Black IMG...");
//						int _len = fingerData.length;
//						int[] _colors = new int[_len];
//						for (int i = 0; i < _len; i++) {
//							byte a = fingerData[i];
//							_colors[i] = (0xff << 24) + (a << 16) + (a << 8)
//									+ (a);
//						}
//						System.out.println("image 8bit to ARGB-8888 finish!");
//						Bitmap _img = Bitmap.createBitmap(_colors, 76, 100,
//								Config.ARGB_8888);
//						System.out.println("create img!");
//						if (handler != null) {
//							Message mg = Message.obtain();
//							mg.what = LoginActivity.fingerBitmap;
//							mg.obj = _img;
//							handler.sendMessage(mg);
//							Log.i("FingerDevcie",
//									"mg.obj=_img---------->bitmap---error");
//						}
//						break;
					}
				}
			};
			Looper.loop();
			Log.i("ThreadManage", "fingerHandlerThread------------------------->end");
		}
	}

	class FingerThread extends Thread {

		private Boolean threadSignal = false;
		private Boolean isOff;

		public void startThread() {
			threadSignal = true;
		}

		public void stopThread() {
			threadSignal = false;
		}

		public void destoryThread() {
			isOff = false;
		}

		@Override
		public void run() {
			super.run();
			System.out.println("finger thread............start!");
			isOff = true;
			HashMap<String, Object> fingerDataHashMap = null;
			while (isOff) {
				synchronized (this) {
					if (!threadSignal) {
						continue;
					}
				}
				System.out.println("serialPort.isFingerPressed()");
				if (1 != serialPort.isFingerPressed()) {
					System.out.println("SSSSSSSSSSSSSSSSSSS" + serialPort.isFingerPressed());
					System.out.println("finger pressing err!");
					continue;
				} else {
					System.out.println("WB_finger is pressing!!!");
					Message _msg = Message.obtain();
					Bundle _bundle = new Bundle();
					_bundle.putBoolean("pressing", true);
					_msg.setData(_bundle);
					fingerHandle.sendMessage(_msg);// send AS602 OK!
				}

				byte[] fingerDataBytes = serialPort.fingerRead(SerialPort.IMG0);// 改
				System.out.println("fingerDataBytes--------->" + fingerDataBytes + "");
				System.out.println("fingerDataBytes--------->liu_rui------>" + fingerDataBytes[0]);
				System.out.println("fingerRead OK!");
				// ������Ҫȥ��200ms,���͹���в��ܱ�ready����

				fingerDataHashMap = getFingerDataMap(fingerDataBytes); // ********
				Message msg = Message.obtain();
				Bundle bundle = new Bundle();// ********
				bundle.putByteArray("fingerData", fingerDataBytes); // ********
				msg.setData(bundle);// ********
				if (fingerDataBytes[0] == 2)// ********
					fingerHandle.sendMessage(msg);// ********
				else if (fingerDataBytes[0] == 4) {
					String luru = "ENROLL_FINGER";
					byte[] temp = luru.getBytes();
					if (serialPort != null)
						serialPort.serialWrite(temp);
					SystemClock.sleep(1000);
					continue;
				}

				byte[] fingerData = serialPort.fingerReadIval();
				Log.i(tag, "fingerData.length1111 = " + fingerData.length);
				Log.i(tag, "fingerData[0] = " + fingerData[0]);
				Bundle extras = new Bundle();
				extras.putByteArray("fingerData", fingerData);
				msg = Message.obtain();
				msg.setData(extras);

				if (fingerData[0] == 1) {
					fingerHandle.sendMessage(msg);
				} else if (fingerData[0] == 4) {
					String luru = "ENROLL_FINGER";
					byte[] temp = luru.getBytes();
					if (serialPort != null)
						serialPort.serialWrite(temp);
					SystemClock.sleep(1000);
					continue;
				}

				byte[] fingerValueCodeing = fingerData;
				byte[] fingerValueImage = fingerDataBytes; // 改
				byte[] fingerValue = new byte[256];
				byte[] fingerImg = new byte[256]; // 改

				System.arraycopy(fingerValueCodeing, 1, fingerValue, 0, 256);
				System.arraycopy(fingerValueImage, 1, fingerImg, 0, 256); // 改

				ShareUtil.ivalBack = fingerValue;
				ShareUtil.fingerImg = fingerImg; // 改

				System.out.println("***************************10" + ShareUtil.ivalBack);
//				System.out.println("isCaiji = " + ShareUtil.isCaiji);
//				System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>send!");
//				Log.i("FingerDevice", " **********************************");
				if (true) {
//					System.out.println("Send  Yanzhen  Verify2  ");
//					if(fingerData == null || fingerData.length < 257){
//					String strs = "123456";
//						byte[] xx = new byte[255];
//						byte[] yy =strs.getBytes();
//						for (int i = 0; i < yy.length; i++) {
//									xx[i] = yy[i];
//						}
					System.out.println("����ֵ�·�");
//						serialPort.fingerVerify2(ShareUtil.ivalBack);
//						serialPort.serialWrite(ShareUtil.ivalBack);
//						System.out.println("����ֵ�·�000"+ShareUtil.ivalBack);
//					}else{
//						serialPort.fingerVerify2(GlobalConstant
//							.subBytes(fingerData, 1, 256));
//					}
					try {
//							sleep(1500);
						byte[] buf = new byte[280];
						serialPort.serialRead(buf, 257);
						String res = new String(buf);
						System.out.println("Yanzhen  Verify2  res = " + res);
						System.out.println("��ȡ�ַ�" + res.substring(0, 13));
						if (res.substring(0, 13).endsWith("MATCH_SUCCESS")) {
							Message mg = Message.obtain();
							mg.what = 389;
//								mg.obj = ShareUtil.names;
//								handler.sendMessage(mg);
						} else {
							Message mg = Message.obtain();
							mg.what = 391;
//								handler.sendMessage(mg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			Log.i("ThreadManage", "fingerThread------------------------->end");
			System.out.println("finger thread.............off!");

		}
	}
}
