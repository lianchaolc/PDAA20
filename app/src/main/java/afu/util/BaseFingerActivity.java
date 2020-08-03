package afu.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @ClassName: BaseFingerActivity
 * @Description: java类作用描述
 * @Author: yao
 * @Date: 2020/7/20 11:26
 */
public abstract class BaseFingerActivity extends Activity implements FingerHandlerInterface{

    public FingerUtil fingerUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("baseFingerActivity", "onCreate -- openFinger");
        fingerUtil = new FingerUtil(this);

        fingerUtil.openFinger();
    }

    @Override
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
    }

    @Override
    protected void onStop() {
        Log.e("baseFingerActivity", "onStop -- closeFinger");
        fingerUtil.closeFinger();
        super.onStop();
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
