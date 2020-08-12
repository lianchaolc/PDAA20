package afu.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;


import com.example.pda.R;
import com.za.finger.ZAandroid;
import com.zhiang.interfac.ZA_finger;

import java.util.HashMap;
import java.util.Iterator;


public class FingerUtil {

    private String tag = "FingerUtil";

    private Handler mHandler = new Handler(); //handle thread message

    private int IMG_SIZE = 0;// 同参数：（0:256x288 1:256x360）
    private String tempImgPath = "/mnt/sdcard/temp.bmp";

    private FingerHandlerInterface fingerHandlerInterface;

    int DEV_ADDR = 0xffffffff;

    /**
     * 设置特征长度
     */
    private int charLen = 512;

    ZAandroid a6 = new ZAandroid();
    private ZA_finger mFPower = new ZA_finger();

    Activity activity;

    public FingerUtil(Activity activity) {
        this.fingerHandlerInterface = (FingerHandlerInterface) activity;

        this.activity = activity;
    }

    public void openFinger() {

        mFPower.finger_power_on();
        mFPower.card_power_on();

        int fd = getrwusbdevices();
        Log.e(tag, "zhw === open fd: " + fd);

        int defiCom = 11;//ttyMT1
        int defDeviceType = 1;
        int defiBaud = 6;
        int status = a6.ZAZOpenDeviceEx(fd, defDeviceType, defiCom, defiBaud, 0, 0);

        a6.ZAZSetImageSize(IMG_SIZE);

        Log.e(tag, " open status: " + status);
        if (status == 1) {
            fingerHandlerInterface.openFingerSucceed();
        } else {
            //"打开设备失败";
        }
    }

    public void closeFinger() {
        mHandler.removeCallbacksAndMessages(null);

        int status = a6.ZAZCloseDeviceEx();
        mFPower.finger_power_off();

        Log.e(tag, " close status: " + status);
    }

    public void getFingerImg() {
        mHandler.removeCallbacksAndMessages(null);

        //run get finger image task
        mHandler.postDelayed(getFPImageTask, 0);
    }

    public void getFingerChar() {
        mHandler.removeCallbacksAndMessages(null);

        //run get finger char task
        mHandler.postDelayed(getCharTask, 0);
    }

    public void getFingerCharAndImg() {
        mHandler.removeCallbacksAndMessages(null);

        //run get finger char task
        mHandler.postDelayed(getCharImgTask, 0);
    }

    /**
     * get finger image task
     */
    private Runnable getFPImageTask = new Runnable() {
        public void run() {

            int nRet = a6.ZAZGetImage(DEV_ADDR);
            if (nRet == 0) {
                int[] len = {0, 0};
                byte[] Image = new byte[256 * 360];
                a6.ZAZUpImage(DEV_ADDR, Image, len);
                a6.ZAZImgData2BMP(Image, tempImgPath);
                //temp = "获取图像成功";

                Bitmap bmpDefaultPic = BitmapFactory.decodeFile(tempImgPath, null);

                fingerHandlerInterface.getImgSucceed(bmpDefaultPic);

                //识别成功后, 开启下一次识别
                mHandler.postDelayed(getFPImageTask, 1000);
            } else if (nRet == a6.PS_NO_FINGER || nRet == -2) {
                //temp = "正在读取指纹中 ";
                mHandler.postDelayed(getFPImageTask, 100);
            } else if (nRet == a6.PS_GET_IMG_ERR) {
                //temp = "图像获取中";
                fingerHandlerInterface.findFinger();
                mHandler.postDelayed(getFPImageTask, 100);
            } else {
                //temp = "通讯异常";
            }
        }
    };


    /**
     * get finger char
     */
    private Runnable getCharTask = new Runnable() {
        @Override
        public void run() {

            int nRet = a6.ZAZGetImage(DEV_ADDR);

            if (nRet == 0) {
                nRet = a6.ZAZGenChar(DEV_ADDR, a6.CHAR_BUFFER_A);
                if (nRet == a6.PS_OK) {
                    nRet = a6.ZAZSetCharLen(charLen);
                    int[] iTempletLength = {0, 0};

                    byte[] pTemplet = new byte[512];
                    nRet = a6.ZAZUpChar(DEV_ADDR, a6.CHAR_BUFFER_A, pTemplet, iTempletLength);
                    if (nRet == a6.PS_OK) {

                        fingerHandlerInterface.getCharSucceed(pTemplet);
                    }

                    //识别成功后, 开启下一次识别
                    mHandler.postDelayed(getCharTask, 1000);
                } else {
                    //temp = "特征太差，请重新录入";
                    fingerHandlerInterface.badCharHandler();
                    mHandler.postDelayed(getCharTask, 1000);
                }
            } else if (nRet == a6.PS_NO_FINGER || nRet == -2) {
                //temp = "正在读取指纹中";
                mHandler.postDelayed(getCharTask, 10);
            } else if (nRet == a6.PS_GET_IMG_ERR) {
                //temp = "图像获取中";
                fingerHandlerInterface.findFinger();
                mHandler.postDelayed(getCharTask, 10);
            } else {
               // temp = "通讯异常";
            }
        }
    };

    private Runnable getCharImgTask = new Runnable() {
        @Override
        public void run() {
            int nRet = a6.ZAZGetImage(DEV_ADDR);

            Log.e(tag, "ZAZGetImage: " + nRet);

            if (nRet == 0) {

                /* 识别出图片耗时太久, 使用默认指纹图片代替
                int[] len = {0, 0};
                byte[] Image = new byte[256 * 360];
                a6.ZAZUpImage(DEV_ADDR, Image, len);
                a6.ZAZImgData2BMP(Image, tempImgPath);

                Bitmap bmpDefaultPic = BitmapFactory.decodeFile(tempImgPath, null);*/

                nRet = a6.ZAZGenChar(DEV_ADDR, a6.CHAR_BUFFER_A);

                Log.e(tag, "ZAZGenChar: " + nRet);
                if (nRet == a6.PS_OK) {
                    nRet = a6.ZAZSetCharLen(charLen);
                    int[] iTempletLength = {0, 0};

                    byte[] pTemplet = new byte[512];
                    nRet = a6.ZAZUpChar(DEV_ADDR, a6.CHAR_BUFFER_A, pTemplet, iTempletLength);

                    Log.e(tag, "ZAZUpChar: " + nRet);
                    if (nRet == a6.PS_OK) {

                        Bitmap bmpDefaultPic = BitmapFactory.decodeResource(activity.getResources(), R.drawable.finger_bmp);
                        Log.e(tag, "pTemplet.length: " + pTemplet.length);
                        fingerHandlerInterface.getCharImgSucceed(pTemplet, bmpDefaultPic);
                    }

                    //识别成功后, 开启下一次识别
                    mHandler.postDelayed(getCharImgTask, 1000);
                } else {
                    //temp = "特征太差，请重新录入";
                    fingerHandlerInterface.badCharHandler();
                    mHandler.postDelayed(getCharImgTask, 1000);
                }
            } else if (nRet == a6.PS_NO_FINGER || nRet == -2) {
                //temp = "正在读取指纹中";
                mHandler.postDelayed(getCharImgTask, 10);
            } else if (nRet == a6.PS_GET_IMG_ERR) {
                //temp = "图像获取中";
                fingerHandlerInterface.findFinger();
                mHandler.postDelayed(getCharImgTask, 10);
            } else {
                //temp = "通讯异常";
            }
        }
    };


    @SuppressLint("NewApi")
    public int getrwusbdevices() {
        // get FileDescriptor by Android USB Host API
        UsbManager mUsbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(activity, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        BroadcastReceiver mUsbReceiver = null;
        activity.registerReceiver(mUsbReceiver, filter);
        Log.i(tag, "zhw 060");
        int fd = -1;
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Log.i(tag,
                    device.getDeviceName() + " "
                            + Integer.toHexString(device.getVendorId()) + " "
                            + Integer.toHexString(device.getProductId()));
            if ((device.getVendorId() == 0x2109)
                    && (0x7638 == device.getProductId())) {
                Log.d(tag, " get FileDescriptor ");
                mUsbManager.requestPermission(device, mPermissionIntent);
                while (!mUsbManager.hasPermission(device)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
                if (mUsbManager.hasPermission(device)) {
                    if (mUsbManager.openDevice(device) != null) {
                        fd = mUsbManager.openDevice(device).getFileDescriptor();
                        Log.d(tag, " get FileDescriptor fd " + fd);
                        return fd;
                    } else
                        Log.e(tag, "UsbManager openDevice failed");
                    mUsbManager.openDevice(device).close();
                }
                break;
            }
        }
        return 0;
    }
}
