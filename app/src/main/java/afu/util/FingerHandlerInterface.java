package afu.util;

import android.graphics.Bitmap;

/**
 * @ClassName: FingerHandlerInterface
 * @Description: java类作用描述
 * @Author: yao
 * @Date: 2020/7/17 15:33
 */
public interface FingerHandlerInterface {

    //打开指纹设备成功
    public void openFingerSucceed();

    //没有发现指纹
    public void noFingerHandler();

    //发现指纹, 正在获取特征值
    public void findFinger();

    //指纹特征值有问题
    public void badCharHandler();

    //获取特征值与图像成功
    public void getCharImgSucceed(byte[] charBytes, Bitmap img);

    //获取特征值成功
    public void getCharSucceed(byte[] charBytes);

    //获取图片成功
    public void getImgSucceed(Bitmap img);
}
