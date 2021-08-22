package hdjc.rfid.operator;

import a20.cn.uhf.admin.RfidAdmin;
import afu.util.DeviceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.poka.device.DeviceManage;
import com.poka.device.GPIO;
import com.poka.device.ScanDevice;

public class RFID_Device implements IRFID_Device {
	public static final int scanCheck = 0x88;
	private static INotify notifys;
	private DeviceManage device = DeviceManage.getDeviceMObject("", null);
	static String singlecode = null;
	public static final int send_Data = 0x300;
	public static final int fingerIval = 0x224;
	public static final int fingerPraper = 0x222;
	public static final int rdid_a20 = 99;
	public static final int rdid_a20_newthread = 98;
	private RfidAdmin rfid_a20;
	private boolean opanAndColse = true;

	public static final int wireless_data = 0x333;
	public static final int micro_usb_data = 0x444;
	public static final int psam_data = 0x555;
	private static int psamPrimitiveOrder = -100;

	private static final String parameterRFID = "rfid";
	private static final String parameterPSAM = "psam";
	private static final String parameterScan = "scan";
	private static final String parameterFinger = "finger";
	private static final String parameterWireless = "wireless";
	private static final String parameterMicro = "micro";



	final Handler h = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(final Message msg) {

			switch (msg.what) {
			case ScanDevice.SCAN:
				Bundle bundle = msg.getData();
				String code = bundle.getString("code");
				System.out.println("10是否传递回来值:" + code);
                if (code == null || code.equals("")) {
                    break;
                }
				read(code);
				readScan(code);
				break;
			case 12:
				Bundle singlebundle = msg.getData();
				singlecode = singlebundle.getString("code");
				System.out.println("12是否传递回来值:" + singlecode);
				break;
			case fingerPraper:
				System.out.println("正在获取指纹特征值！");
				fingerPressState();
				// ShareUtil.toastShow("正在获取指纹特征值！", theActivity);
				break;
			case send_Data:
				// String luru = "MATCH_FINGER";
				String luru = "ENROLL_FINGER";
				byte[] t = luru.getBytes();
				byte[] temp = new byte[76];
				for (int i = 0; i < t.length; ++i)
					temp[i] = t[i];
				// ShareUtil.isCaiji = false;
				System.out.print(
						"liurui--->指纹--->fingersDevcie.getSerialPort()--->" + device.fingersDevcie.getSerialPort());
				if (device.fingersDevcie.getSerialPort() != null)
					device.fingersDevcie.getSerialPort().serialWrite(temp);
//					else
//						h.sendMessageDelayed(h.obtainMessage(send_Data), 500);
				break;
			case 281:
				String close = "QUIT";
				byte[] tt = close.getBytes();
				byte[] tempclose = new byte[76];
				for (int i = 0; i < tt.length; ++i)
					tempclose[i] = tt[i];
				// ShareUtil.isCaiji = false;
				System.out.println("发送关闭命令");
				device.fingersDevcie.getSerialPort().serialWrite(tempclose);
				break;
			case fingerIval:

				byte[] ival = (byte[]) msg.obj;
				System.out.println("指纹特征值获取成功！");
				fingerIval();
				// ShareUtil.toastShow("指纹特征值获取成功！", theActivity);

				break;
			case rdid_a20:
				Bundle b = msg.getData();
				String number = b.getString("number");
				RFID_Device.this.notify(number);
				break;
			case wireless_data:
			case micro_usb_data:
			case psam_data:
				byte[] bytes = msg.getData().getByteArray("readData");
				if (bytes != null && readData != null) {
					Bundle readDate = new Bundle();
					readDate.putByteArray("READ_DATA", bytes);
					Message message = new Message();
					message.what = msg.what;
					message.setData(readDate);
					readData.sendMessage(message);
				}
				break;

				case rdid_a20_newthread:
					// 加入到子线

//					new Thread(new Runnable() {
//						@Override
//						public void run() {
					Log.d("RFID_Device", "==========InventoryThread============rdid_a20_newthread+" + Thread.currentThread().getName());
					Bundle bnewthread = msg.getData();
					String numbernewthread = bnewthread.getString("number");
					RFID_Device.this.notify(numbernewthread);
//						}
//					}).start();
					break;
			default:

				break;
			}
			return true;
		}
	});

	private Handler readData = null;

	public void setReadDataHandler(Handler readData) {
		this.readData = readData;
	}

	// LR_TODO: 2020/3/27 20:56 liu_rui 本类的打开关闭串口都加了线程，以免程序等待引起崩溃
	// 打开RFID扫描（打开串口，并且监听串口接收到的信息（搜索：codeString = serialPort.scanRead();））
	public void open() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Message mg = Message.obtain();
//				mg.what = scanCheck;
//				mg.obj = DeviceInfo.RFID_REPEAT;
//				DeviceManage.getDeviceMObject(parameterRFID, h);
//				device.getMyHandler().sendMessage(mg);
//			}
//		}).start();
	}

	// 关闭RFID扫描（关闭线程，没有关闭串口，我发现好像没有调用这里的串口）
	public void close() {
//		if (true)     // LR_TODO: 2020/4/6 16:29 liu_rui 我添加的
//			return;
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Message mg = Message.obtain();
//				mg.what = 0x91;
//				mg.obj = DeviceInfo.RFID_CLOSE;
//				DeviceManage.getDeviceMObject(parameterRFID, h);
//				device.getMyHandler().sendMessage(mg);
//			}
//		}).start();
	}

	// 添加通知，欧
	public void addNotifly(INotify notify) {
		RFID_Device.notifys = notify;

	}

	// 循环读取RFID编号
	private void read(String rfidStr) {
		System.out.println("读取到的编号:" + rfidStr);
		notify(rfidStr);//
	}

	/*
	 * 通知外部，调用方得到编号
	 */
	private void notify(String number) {
		Log.d("RFID_Device", "==========InventoryThread============notify+" + Thread.currentThread().getName());

		Log.e("TAG", "liu_rui, notify: " + notifys + number);
		if (notifys != null)
			notifys.getNumber(number);//
	}

	// 打开一维码扫描
	@Override
	public void scanOpen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message mg = Message.obtain();
				mg.obj = DeviceInfo.SCAN_OPEN;
				DeviceManage.getDeviceMObject(parameterScan, h);
				device.getScanHandler().sendMessage(mg);
			}
		}).start();
	}

	// 关闭一维码扫描
	@Override
	public void scanclose() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message mg = Message.obtain();
				mg.obj = DeviceInfo.SCAN_CLOSE;
				DeviceManage.getDeviceMObject(parameterScan, h);
				device.getScanHandler().sendMessage(mg);
			}
		}).start();
	}

	// 单次获取一维码扫描结果数据
	@Override
	public String readScan(String scan) {
		System.out.println("扫描回来的一维码:" + scan);
		notify(scan);
		return scan;
	}

	@Override
	public void fingerOpen() {
		close_a20();
//		managerHint.getRuning().runding(QinglingZhuangxiangActivity.this, "正在开启RIFI,请稍等...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message mg = Message.obtain();
				mg.obj = DeviceInfo.FINGER_OPEN;
				// 先打开线程再添加回调
				DeviceManage.getDeviceMObject(parameterFinger, h);
				device.setLoginAcitvityHandler(h);
				h.sendMessageDelayed(h.obtainMessage(send_Data), 500);
				DeviceManage.getDeviceMObject(parameterFinger, h).getFingerHandler().sendMessage(mg);
			}
		}).start();
	}

	@Override
	public void fingerClose() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
		// 不建议关掉指纹
		Message mg = Message.obtain();
		mg.obj = DeviceInfo.FINGER_CLOSE;
//				System.out.println(DeviceManage.getDeviceMObject(parameterFinger) + "主界面是否为null");
		// 先打开线程再添加回调
		DeviceManage.getDeviceMObject(parameterFinger, h);
		device.setLoginAcitvityHandler(h);
		DeviceManage.getDeviceMObject(parameterFinger, h).getFingerHandler().sendMessage(mg);
		h.sendMessageDelayed(h.obtainMessage(send_Data), 500);
//			}
//		}).start();
	}

	@Override
	public void fingerPressState() {
		notify("正在获取指纹特征值！");
	}

	@Override
	public void fingerIval() {
		notify("获取指纹特征值成功！");
	}

	/**
	 * 多次扫描调用的回调
	 */
	@Override
	public void open_a20() {
		if (opanAndColse) {
			getRfid_a20().setHandler(h);
			getRfid_a20().openRfid();
			Log.i("open", "open");
			opanAndColse = false;
		}

	}

	@Override
	public void close_a20() {
		if (!opanAndColse) {
			getRfid_a20().closeRfid();
			Log.i("colse", "colse");
			opanAndColse = true;
		}

	}

	@Override
	public void stop_a20() {
		getRfid_a20().stopRfid();
	}

	@Override
	public void start_a20() {
		getRfid_a20().startRfid();
	}

	private RfidAdmin getRfid_a20() {
		if (rfid_a20 == null) {
			rfid_a20 = new RfidAdmin();
		}
		return rfid_a20;
	}

	/**
	 * 模块电源，使能是否关闭（当前只有指纹模块）
	 *
	 * @param ID  需要操作的模块 如：GlobalConstant.IO_RFID_POWER
	 * @param val 0 = 关闭，1 = 打开
	 */
	public void setOpenClose(int ID, int val) {
		new GPIO(ID).setGPIO(val);
	}

	/***
	 * 后加入
	 *
	 * @param b
	 */
	public void setVoice(boolean b) {
		// TODO Auto-generated method stub

	}

	/*
	 * 特征值存在 ShareUtil.ivalBack; 采集或者验证的时候直接将此值 作为参数分割传入即可
	 *
	 * 或者分为2个128的字节数组传入
	 */
}
