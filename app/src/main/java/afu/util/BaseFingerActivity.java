package afu.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

public abstract class BaseFingerActivity extends Activity implements FingerHandlerInterface{

    public FingerUtil fingerUtil;

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("baseFingerActivity", "onResume -- openFinger");
        fingerUtil = new FingerUtil(this);

        fingerUtil.openFinger();
    }

    /*@Override
    protected void onRestart() {
        Log.e("baseFingerActivity", "onRestart -- openFinger");
        fingerUtil = new FingerUtil(this);
        fingerUtil.openFinger();
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("baseFingerActivity", "onNewIntent -- openFinger");
        fingerUtil = new FingerUtil(this);
        fingerUtil.openFinger();
        super.onNewIntent(intent);
    }*/

    @Override
    protected void onPause() {
        Log.e("baseFingerActivity", "onPause -- closeFinger");
        fingerUtil.closeFinger();
        super.onPause();
    }

    //没有发现指纹
    public void noFingerHandler(){}

    //指纹特征值有问题
    public void badCharHandler(){}

    //获取特征值与图像成功
    public void getCharImgSucceed(byte[] charBytes, Bitmap img){}

    //获取特征值成功
    public void getCharSucceed(byte[] charBytes){}

    //获取图片成功
    public void getImgSucceed(Bitmap img){}
}
