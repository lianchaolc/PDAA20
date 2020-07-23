package afu.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.za.finger.FingerHelper;
import com.za.finger.IUsbConnState;

import cn.pda.serialport.SerialPort;


public class FingerUtil {

    private FingerHelper mFingerHelper;  //option finger

    private int statues = 0;

    private long startTime = 0L;
    private long endTime = 0L;

    private String tag = "FingerUtil";

    private Handler mHandler = new Handler(); //handle thread message
    SerialPort serialPort = new SerialPort();

    private final int IMAGE_SIZE = 256 * 288;//image size
    private String tempImgPath = "/mnt/sdcard/temp.bmp";

    private FingerHandlerInterface fingerHandlerInterface;

    private int fpCharBuffer = 0;

    public FingerUtil(Activity activity) {
        this.fingerHandlerInterface = (FingerHandlerInterface) activity;
        mFingerHelper = new FingerHelper(activity, usbConnstate);
    }

    //IUsbConnState is to receive usb finger connect state
    private IUsbConnState usbConnstate = new IUsbConnState() {
        @Override
        public void onUsbConnected() {

            //connect finger device
            statues = mFingerHelper.connectFingerDev();
            if (statues == mFingerHelper.CONNECT_OK) {
                fingerHandlerInterface.openFingerSucceed();
            } else {

            }

        }

        @Override
        public void onUsbPermissionDenied() {

        }

        @Override
        public void onDeviceNotFound() {

        }
    };

    public void openFinger() {
        serialPort.power3v3on();
        mFingerHelper.init();
    }

    public void closeFinger() {

        mHandler.removeCallbacksAndMessages(null);
        if (serialPort != null) {
            serialPort.power3v3off();
        }
        mFingerHelper.close();
    }

    public void getFingerImg() {
        mHandler.removeCallbacksAndMessages(null);

        startTime = System.currentTimeMillis();
        endTime = startTime;
        //run get finger image task
        mHandler.postDelayed(getFPImageTask, 0);
    }

    public void getFingerChar() {
        mHandler.removeCallbacksAndMessages(null);

        startTime = System.currentTimeMillis();
        endTime = startTime;
        //run get finger char task
        mHandler.postDelayed(getCharTask, 0);
    }

    public void getFingerCharAndImg() {
        mHandler.removeCallbacksAndMessages(null);

        startTime = System.currentTimeMillis();
        endTime = startTime;
        //run get finger char task
        mHandler.postDelayed(getCharImgTask, 0);
    }

    public void getTwoFingerCharAndImg() {
        mHandler.removeCallbacksAndMessages(null);

        startTime = System.currentTimeMillis();
        endTime = startTime;
        //run get finger char task
        mHandler.postDelayed(twoFingerTask, 0);
    }

    /**
     * get finger image task
     */
    private Runnable getFPImageTask = new Runnable() {
        @Override
        public void run() {
            statues = mFingerHelper.getImage();
            //find finger
            if (statues == mFingerHelper.PS_OK) {

                fingerHandlerInterface.findFinger();

                int[] recvLen = {0, 0};
                byte[] imageByte = new byte[IMAGE_SIZE];//256*288
                mFingerHelper.uploadImage(imageByte, recvLen);
                //switch to bmp
                mFingerHelper.imageData2BMP(imageByte, tempImgPath);

                //将识别的指纹图片保存进 ShareUtil.finger_gather
                Bitmap bitmap = BitmapFactory.decodeFile(tempImgPath, null);

                fingerHandlerInterface.getImgSucceed(bitmap);

                mHandler.postDelayed(getFPImageTask, 1000);

            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                fingerHandlerInterface.noFingerHandler();
                mHandler.postDelayed(getFPImageTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                mHandler.postDelayed(getFPImageTask, 100);
            } else {
                //temp = res.getString(R.string.dev_error);
            }
        }
    };


    /**
     * get finger char
     */
    private Runnable getCharTask = new Runnable() {
        @Override
        public void run() {

            statues = mFingerHelper.getImage();
            //find finger
            if (statues == mFingerHelper.PS_OK) {

                fingerHandlerInterface.findFinger();

                //gen char to bufferA
                statues = mFingerHelper.genChar(mFingerHelper.CHAR_BUFFER_A);
                if (statues == mFingerHelper.PS_OK) {
                    int[] iCharLen = {0, 0};
                    byte[] charBytes = new byte[512];
                    //upload char
                    statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iCharLen);
                    if (statues == mFingerHelper.PS_OK) {
                        //upload success
                        fingerHandlerInterface.getCharSucceed(charBytes);
                    }

                    mHandler.postDelayed(getCharTask, 1000);
                } else {
                    //char is bad quickly
                    fingerHandlerInterface.badCharHandler();
                    mHandler.postDelayed(getCharTask, 100);
                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                fingerHandlerInterface.noFingerHandler();
                mHandler.postDelayed(getCharTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                mHandler.postDelayed(getCharTask, 100);
            } else {
                //temp = res.getString(R.string.dev_error);
            }
        }
    };

    private Runnable getCharImgTask = new Runnable() {
        @Override
        public void run() {
            statues = mFingerHelper.getImage();
            //find finger
            if (statues == mFingerHelper.PS_OK) {

                fingerHandlerInterface.findFinger();

                int[] recvLen = {0, 0};
                byte[] imageByte = new byte[IMAGE_SIZE];//256*288
                mFingerHelper.uploadImage(imageByte, recvLen);
                //switch to bmp
                mFingerHelper.imageData2BMP(imageByte, tempImgPath);
                Bitmap bitmap = BitmapFactory.decodeFile(tempImgPath, null);

                //gen char to bufferA
                statues = mFingerHelper.genChar(mFingerHelper.CHAR_BUFFER_A);
                if (statues == mFingerHelper.PS_OK) {
                    int[] iCharLen = {0, 0};
                    byte[] charBytes = new byte[512];

                    //upload char
                    statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iCharLen);
                    if (statues == mFingerHelper.PS_OK) {
                        //upload success

                        fingerHandlerInterface.getCharImgSucceed(charBytes, bitmap);
                    }

                    //获取下一枚指纹信息, 避免页面控制
                    mHandler.postDelayed(getCharImgTask, 1000);
                } else {
                    //char is bad quickly
                    fingerHandlerInterface.badCharHandler();
                    mHandler.postDelayed(getCharImgTask, 100);
                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                fingerHandlerInterface.noFingerHandler();
                mHandler.postDelayed(getCharImgTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                mHandler.postDelayed(getCharImgTask, 100);
            } else {
                //temp = res.getString(R.string.dev_error);
            }
        }
    };


    private Runnable twoFingerTask = new Runnable() {
        @Override
        public void run() {

            statues = mFingerHelper.getImage();
            //find finger
            if (statues == mFingerHelper.PS_OK) {

                fingerHandlerInterface.findFinger();

                int[] recvLen = {0, 0};
                byte[] imageByte = new byte[IMAGE_SIZE];//256*288
                mFingerHelper.uploadImage(imageByte, recvLen);
                //switch to bmp
                mFingerHelper.imageData2BMP(imageByte, tempImgPath);

                //将识别的指纹图片保存进 ShareUtil.finger_gather
                Bitmap bitmap = BitmapFactory.decodeFile(tempImgPath, null);


                if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_A) {
                    //gen char to bufferA
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {

                        int[] iCharLen = {0, 0};
                        byte[] charBytes = new byte[512];
                        //upload char
                        statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iCharLen);
                        if (statues == mFingerHelper.PS_OK) {
                            //upload success
                            fingerHandlerInterface.getCharImgSucceed(charBytes, bitmap);
                        }

                        mHandler.postDelayed(twoFingerTask, 1000);
                        fpCharBuffer = mFingerHelper.CHAR_BUFFER_B;
                    } else {
                        //char is bad quickly
                        fingerHandlerInterface.badCharHandler();
                        mHandler.postDelayed(twoFingerTask, 100);
                    }
                } else if (fpCharBuffer == mFingerHelper.CHAR_BUFFER_B) { //second finger
                    //gen char to bufferB
                    statues = mFingerHelper.genChar(fpCharBuffer);
                    if (statues == mFingerHelper.PS_OK) {

                        int[] iCharLen = {0, 0};
                        byte[] charBytes = new byte[512];
                        //upload char
                        statues = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_B, charBytes, iCharLen);
                        if (statues == mFingerHelper.PS_OK) {
                            //upload success
                            fingerHandlerInterface.getCharImgSucceed(charBytes, bitmap);
                        }

                        mHandler.postDelayed(twoFingerTask, 1000);
                    } else {
                        //char is bad quickly
                        fingerHandlerInterface.badCharHandler();
                        mHandler.postDelayed(twoFingerTask, 100);
                    }
                }
            } else if (statues == mFingerHelper.PS_NO_FINGER) {
                fingerHandlerInterface.noFingerHandler();

                mHandler.postDelayed(twoFingerTask, 100);
            } else if (statues == mFingerHelper.PS_GET_IMG_ERR) {
                mHandler.postDelayed(twoFingerTask, 100);
            } else {
                //temp = res.getString(R.string.dev_error);
            }
        }
    };

}
