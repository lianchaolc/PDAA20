package com.loginsystem.biz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.hjq.permissions.Permission;


/**
 * 权限工具类
 *
 * @author sangf
 * @date 2019/7/19
 */

public class PermissionUtil {

    /**
     * 8.0及以上应用安装权限
     */
    public static final String REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES";
    /**
     * 6.0及以上悬浮窗权限
     */
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    /**
     * 拍照权限
     */
    public static final String CAMERA = "android.permission.CAMERA";
    /**
     * 读取联系人
     */
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    /**
     * 写入联系人
     */
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    /**
     * 访问账户列表
     */
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    /**
     * 获取精确位置
     */
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    /**
     * 获取粗略位置
     */
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    /**
     * 读取电话状态
     */
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    /**
     * 拨打电话
     */
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    /**
     * 读取外部存储
     */
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    /**
     * 写入外部存储
     */
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {

        // 联系人
        public static final String[] CONTACTS = new String[]{
                Permission.READ_CONTACTS,
                Permission.WRITE_CONTACTS,
                Permission.GET_ACCOUNTS};

        // 位置
        public static final String[] LOCATION = new String[]{
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION};

        // 存储
        public static final String[] STORAGE = new String[]{
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * 拨打电话 requestCode 1客服 2普通叫车 3代人叫车 4叫车人
     */
//    public static void callPhone(Activity activity, String phoneNumber, int requestCode) {
//        if (XXPermissions.isHasPermission(LocationApplication.getContext(), PermissionUtil.CALL_PHONE)) {
//            // 允许了拨打电话权限，可以拨打电话
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
//            if (ActivityCompat.checkSelfPermission(LocationApplication.getContext(),
//                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // 没有运行拨打电话权限，不往下执行
//                return;
//            }
//            // 动态判断拨打电话权限
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            LocationApplication.getContext().startActivity(intent);
//        } else {
//            // 没有权限，显示申请拨打电话权限，弹框
//            ActivityCompat.requestPermissions(activity, new String[]{CALL_PHONE}, requestCode);
//        }
//    }

    /**
     * 定位，获取精确位置
     */
//    public static void accessFineLocation(Activity activity, int requestCode) {
//        if (XXPermissions.isHasPermission(LocationApplication.getContext(), PermissionUtil.ACCESS_FINE_LOCATION)) {
//            // 允许了获取精确位置权限，跳到手机系统，打开位置服务页面
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            // 动态判断获取精确位置权限
//            LocationApplication.getContext().startActivity(intent);
//        } else {
//            // 没有权限，显示获取精确位置权限，弹框
//            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, requestCode);
//        }
//    }

}
