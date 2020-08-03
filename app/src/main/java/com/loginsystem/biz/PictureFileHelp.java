package com.loginsystem.biz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 拍照或者是选择相册帮助类
 *
 * @author YSir
 * @date 2018/5/5
 */
public class PictureFileHelp {

    public static final int PERMISSION_CODE_SELECT_PICTURE = 10001;
    public static final int PERMISSION_CODE_TAKE_PHOTO = 10000;

    public static final int CODE_SELECT_PICTURE = 1001;
    public static final int CODE_TAKE_PHOTO = 1003;
    public static final int CODE_CROP_PICTURE = 1004;
    /**
     * 图片路径
     */
    private static final String BASE_PICTURE = Environment.getExternalStorageDirectory() + "/rihui_ecar_images/";


    private static final String[] PICTURE_PERMISSIONS_SAVE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static void requestPermissionSave(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, PICTURE_PERMISSIONS_SAVE, requestCode);
    }

    private static void selectPictureTrue(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CODE_SELECT_PICTURE);
    }

    /**
     * 是否请求权限
     */
    public static boolean shouldGetPermission(Activity activity) {
        boolean bool = true;
        for (String str : PICTURE_PERMISSIONS_SAVE) {
            bool = ActivityCompat.checkSelfPermission(activity, str) == PackageManager.PERMISSION_GRANTED;
            if (!bool) {
                break;
            }
        }
        return bool;
    }

    /**
     * 先选择相册
     */
    public static void selectPicture(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!shouldGetPermission(activity)) {
                requestPermissionSave(activity, PERMISSION_CODE_SELECT_PICTURE);
            } else {
                selectPictureTrue(activity);
            }
        } else {
            selectPictureTrue(activity);
        }
    }


    /**
     * 选择拍照
     *
     * @param activity 上下文
     * @param fileName 拍照之后的文件名
     */
    public static void takePhoto(Activity activity, String fileName) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!shouldGetPermission(activity)) {
                requestPermissionSave(activity, PERMISSION_CODE_TAKE_PHOTO);
            } else {
                takePhoto(activity, initFile(activity, fileName));
            }
        } else {
            takePhoto(activity, initFile(activity, fileName));
        }
    }

    private static void takePhoto(Activity activity, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, CODE_TAKE_PHOTO);
    }

    private static void takePhoto(Activity activity, Uri uri, int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, code);
    }

    /**
     * 上传完之后清空文件夹
     */
    public static void delAllFile() {
        File file = new File(BASE_PICTURE);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (String str : tempList) {
            temp = new File(BASE_PICTURE + str);
            if (temp.isFile()) {
                temp.delete();
            }
        }
    }

    /**
     * 将指定路径的图片文件转为bitmap并进行压缩
     *
     * @param name 文件名
     * @return bitmap
     */
    public static Bitmap getBitmap(String name) {
        String url = getPhotoUrl(name);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(url);
    }

    /**
     * 将指定路径的图片文件转为bitmap并进行压缩
     *
     * @param name 文件名
     * @return bitmap
     */
    public static Bitmap getBitmapJpeg(String name) {
        String url = getPhotoUrlJpeg(name);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(url);
    }

    public static Bitmap getBitmap(Activity activity, String name) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(getUri(activity, name).getPath());
    }

    /**
     * 将指定路径的图片文件转为bitmap并进行压缩
     *
     * @return bitmap
     */
    public static Bitmap getBitmapStream(InputStream uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        return BitmapFactory.decodeStream(uri, null, options);
    }

    public static void saveBitmap(Bitmap bitmap, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(createFileByName(filename));
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapHalf(Bitmap bitmap, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(createFileByName(filename));
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static File getFileByNameJpeg(String name) {
        return new File(BASE_PICTURE + name + ".jpeg");
    }

    /**
     * 图片的缩放方法
     *
     * @param bitmap ：源图片资源
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > 200) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / 200;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPhotoUrl(String name) {
        return BASE_PICTURE + name + ".jpeg";
    }

    public static String getPhotoUrlJpeg(String name) {
        return BASE_PICTURE + name + ".jpeg";
    }

    /**
     * 拍照图片保存的文件
     *
     * @param name 文件名
     * @return 路径
     */
    public static Uri initFile(Activity activity, String name) {
        Uri uri;
        File file = new File(BASE_PICTURE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileSave = new File(file, name + ".jpeg");
        try {
            if (!fileSave.exists()) {
                fileSave.createNewFile();
            } else {
                fileSave.delete();
                fileSave.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            // 适配android7.0 ，不能直接访问原路径
            // 需要对intent 授权
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".FileProvider", fileSave);
        } else {
            uri = Uri.fromFile(fileSave);
        }
        return uri;
    }

    /**
     * 拍照图片保存的文件
     *
     * @param name 文件名
     * @return 路径
     */
    public static Uri initFileNew(Activity activity, String name) {
        Uri uri;
        File file = new File(BASE_PICTURE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileSave = new File(file, name + ".jpeg");
        try {
            if (!fileSave.exists()) {
                fileSave.createNewFile();
            } else {
                fileSave.delete();
                fileSave.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        uri = Uri.fromFile(fileSave);
        return uri;
    }

    public static Uri getUri(Activity activity, String name) {
        Uri uri;
        File fileSave = new File(BASE_PICTURE + name + ".jpeg");
        if (Build.VERSION.SDK_INT >= 24) {
            // 适配android7.0 ，不能直接访问原路径
            // 需要对intent 授权
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".FileProvider", fileSave);
        } else {
            uri = Uri.fromFile(fileSave);
        }
        return uri;
    }

    public static File createFileByName(String name) {
        File file = new File(BASE_PICTURE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileSave = new File(file, name + ".jpeg");
        try {
            if (!fileSave.exists()) {
                fileSave.createNewFile();
            } else {
                fileSave.delete();
                fileSave.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSave;
    }

    private static String path_ = "";

    public static File createFile() {
        File file = new File(BASE_PICTURE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileSave = new File(file, path_ + ".jpeg");
        try {
            if (!fileSave.exists()) {
                fileSave.createNewFile();
            } else {
                fileSave.delete();
                fileSave.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSave;
    }
}
