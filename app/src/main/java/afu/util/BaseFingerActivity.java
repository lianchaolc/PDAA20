package afu.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

        fingerUtil = new FingerUtil(this);

        fingerUtil.openFinger();
    }

    @Override
    protected void onDestroy() {
        fingerUtil.closeFinger();
        super.onDestroy();
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
