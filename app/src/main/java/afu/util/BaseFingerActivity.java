package afu.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * 2020-8-6
 * 特殊情况: 网点双人指纹(KuanXiangJiaoJieActivity)验证完成, 跳转下一个页面验证指纹时, 不能在onStop中关闭指纹
 * Activity生命周期原因, 先执行下个页面的 onCrete - onResume, 再执行本页面的onStop
 * 下个页面会在 onCreate 中打开指纹, 如果本页面onStop执行关闭指纹, 将导致下个页面指纹逻辑崩溃.
 *
 * 另外,不能在onPause中执行关闭指纹, 启用指纹模块, 需要申请USB权限, 弹框提醒, 会导致当前指纹页面回调 onPause
 * 汉德霍尔设备暂无USB权限弹框, 但是依旧会导致当前页面回调 onPause.
 *
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
