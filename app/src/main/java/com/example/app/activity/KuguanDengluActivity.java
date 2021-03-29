package com.example.app.activity;

import java.net.SocketTimeoutException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.app.entity.User;
import com.example.app.util.Skip;
import com.example.pda.R;
import com.manager.classs.pad.ManagerClass;
import com.o.service.YanZhengZhiWen;
import com.poka.device.ShareUtil;
import com.service.FixationValue;

import afu.util.BaseFingerActivity;

/**
 * 库管员验证指纹页面
 *
 * @author Administrator
 */
public class KuguanDengluActivity extends BaseFingerActivity {

    private TextView name_left, name_right, bottomtext, texttop;
    private ImageView img_left, img_right;
    private User result_user;
    private String fname_left, fname_right;

    public static String userid1; // 角色ID
    public static String userid2; // 角色ID
    public static boolean firstSuccess = false; // 第一位是否已成功验证指纹
    String f1 = null; // 第一个按手指的人
    String f2 = null; // 第二个按手指的人
    int one = 0;// 统计第一位验证指纹人的次数
    int two = 0;// 统计第二位验证指纹人的次数

    private ManagerClass manager;
    OnClickListener OnClick;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_kuguandenglu);
        load();
        manager = new ManagerClass();

        OnClick = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                yanzhengFinger();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("fname_left" + fname_left);
        if (fname_left == null) {
            firstSuccess = false;
        }

        /**
         * SM修改部分
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String flag = bundle.getString("FLAG");

            if (flag.equals("kuguanone")) {
                img_left.setImageResource(R.drawable.sccuss);
                fname_left = GApplication.use.getUsername();
                name_left.setText(fname_left);
                f1 = "1";
                one = 0;
                firstSuccess = true;
                bottomtext.setText("请第二位库管员按压手指...");
                texttop.setText("第一位验证成功");

            }
            if (flag.equals("kuguantwo")) {
                img_right.setImageResource(R.drawable.sccuss);
                fname_right = GApplication.use.getUsername();
                fname_left = bundle.getString("name1");
                if (fname_left != null && GApplication.map != null) {
                    img_left.setImageBitmap(GApplication.map);
                } else {
                    img_left.setImageResource(R.drawable.sccuss);
                }
                name_left.setText(fname_left);
                name_right.setText(fname_right);
                f1 = "1";
                f2 = "2";
                texttop.setText("第二位验证成功");
                bottomtext.setText("验证成功！");
                if (fname_left.equals(fname_right)) {
                    Toast.makeText(KuguanDengluActivity.this, "请两个库管员进行验证！", Toast.LENGTH_SHORT).show();
                } else {
                    fname_right = null;
                    fname_left = null;
                    firstSuccess = false;
                    if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {
                        manager.getRuning().runding(this, "验证完成,数据加载中...");
                        Skip.skip(KuguanDengluActivity.this, KuanxiangChuruActivity.class, null, 0);
                        manager.getRuning().remove();
                    }
                }
            }
        }
    }


    /**
     * 控件初始化
     */
    private void load() {
        img_left = (ImageView) findViewById(R.id.finger_left);
        img_right = (ImageView) findViewById(R.id.finger_right);
        name_left = (TextView) findViewById(R.id.login_name1);
        name_right = (TextView) findViewById(R.id.login_name2);
        bottomtext = (TextView) findViewById(R.id.login_bottom_show);
        texttop = (TextView) findViewById(R.id.resultmsg);
    }


    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void findFinger() {
        texttop.setText("正在获取指纹特征值");
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        super.getCharImgSucceed(charBytes, img);

        ShareUtil.ivalBack = charBytes;
        if (!firstSuccess && !"1".equals(f1)) {
            ShareUtil.finger_bitmap_left = img;
        } else {
            ShareUtil.finger_bitmap_right = img;
        }
        yanzhengFinger();
    }

    // 根据交接类型获取返回角色ID
    public String getUserID(String type) {
        // 空钞箱交接
        if (type.equals("01")) {
            return FixationValue.clearer + "";
            // 未清回收钞箱交接
        } else if (type.equals("03")) {
            return FixationValue.clearer + "";
            // 网点人员加钞钞箱交接
        } else if (type.equals("04")) {
            return FixationValue.webuser + "";
        }
        return null;

    }

    // 验证指纹
    public void yanzhengFinger() {
        Thread t1;
        t1 = new Thread() {
            @Override
            public void run() {
                super.run();
                YanZhengZhiWen yz = new YanZhengZhiWen();
                try {
                    result_user = yz.yanzhengfinger(GApplication.loginJidouId, "4", ShareUtil.ivalBack);
                    if (result_user != null) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (SocketTimeoutException e) {
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(5);
                }
            }
        };
        t1.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // OneAsyncTask
                    manager.getAbnormal().timeout(KuguanDengluActivity.this, "获取数据失败", OnClick);
                    break;
                case 1:
                    if (!firstSuccess && !"1".equals(f1)) {
                        img_left.setImageBitmap(ShareUtil.finger_bitmap_left);
                        fname_left = result_user.getUsername();
                        ShareUtil.zhiwenid_left = result_user.getUserzhanghu();
                        name_left.setText(fname_left);
                        f1 = "1";

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < ShareUtil.ivalBack.length; i++) {
                            sb.append(ShareUtil.ivalBack[i]);
                        }
                        firstSuccess = true;
                        bottomtext.setText("请第二位库管员按压手指...");
                        texttop.setText("第一位验证成功");
                    // 第二位验证
                    } else {
                        fname_right = result_user.getUsername();
                        if (fname_left != null && fname_left.equals(fname_right)) {
                            texttop.setText("验证失败");
                            handler.sendEmptyMessage(2);
                        } else {
                            texttop.setText("");
                            img_right.setImageBitmap(ShareUtil.finger_bitmap_right);
                            ShareUtil.zhiwenid_right = result_user.getUserzhanghu();

                            name_right.setText(fname_right);
                            f2 = "2";
                            bottomtext.setText("验证成功！");
                            if (GApplication.kuguan1 == null) {
                                GApplication.kuguan1 = new User();
                            }
                            if (GApplication.kuguan2 == null) {
                                GApplication.kuguan2 = new User();
                            }
                            fname_left = null;
                            fname_right = null;
                            firstSuccess = false;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("走起:222222" + "f1是什么" + f1 + "f2是什么" + f2);
                            if (null != f1 && null != f2 && f1.equals("1") && f2.equals("2")) {
                                manager.getRuning().runding(KuguanDengluActivity.this, "验证完成，数据加载中...");
                                Skip.skip(KuguanDengluActivity.this, KuanxiangChuruActivity.class, null, 0);
                        }

                        }
                    }
                    break;
                case 2:
                    if (!firstSuccess) {// 失败就累加次数
                        one++;
                        texttop.setText("验证失败" + one + "次");
                    } else {
                        two++;
                        texttop.setText("验证失败" + two + "次");
                    }
                    if (one >= FixationValue.PRESS) {
                        /*
                         * Modify begin by zhangxuewei, @2017-01-22 此处新增关闭指纹录入接口,
                         */
                        //getRfid().close_a20();
                        // Modify End
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguanone");
                        ShareUtil.ivalBack = null;
                        ShareUtil.finger_bitmap_left = null;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    } else if (two >= FixationValue.PRESS) {
                        /*
                         * Modify begin by zhangxuewei, @2017-01-22 此处新增关闭指纹录入接口,
                         */
                        //getRfid().close_a20();
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguantwo");
                        bundle2.putString("name1", name_left.getText().toString());
                        System.out.println("您是什么账号?:" + name_left.getText().toString());
                        if (ShareUtil.finger_bitmap_left != null)
                            GApplication.map = ShareUtil.finger_bitmap_left;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        f1 = "1";
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    }
                    break;
                case 3:
                    texttop.setText("验证超时");
                    if (!firstSuccess) {// 失败就累加次数
                        one++;
                        texttop.setText("验证失败" + one + "次");
                    } else {
                        two++;
                        texttop.setText("验证失败" + two + "次");
                    }
                    if (one >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguanone");
                        ShareUtil.ivalBack = null;
                        ShareUtil.finger_bitmap_left = null;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    } else if (two >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguantwo");
                        bundle2.putString("name1", name_left.getText().toString());
                        System.out.println("您是什么账号?:" + name_left.getText().toString());
                        if (ShareUtil.finger_bitmap_left != null)
                            GApplication.map = ShareUtil.finger_bitmap_left;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        f1 = "1";
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    }

                    break;
                case 4:
                    texttop.setText("验证失败");
                    if (!firstSuccess) {// 失败就累加次数
                        one++;
                        texttop.setText("验证失败" + one + "次");
                    } else {
                        two++;
                        texttop.setText("验证失败" + two + "次");
                    }
                    if (one >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguanone");
                        ShareUtil.ivalBack = null;
                        ShareUtil.finger_bitmap_left = null;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    } else if (two >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguantwo");
                        bundle2.putString("name1", name_left.getText().toString());
                        System.out.println("您是什么账号?:" + name_left.getText().toString());
                        if (ShareUtil.finger_bitmap_left != null)
                            GApplication.map = ShareUtil.finger_bitmap_left;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        f1 = "1";
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    }
                    break;
                case 5:
                    texttop.setText("验证异常");
                    if (!firstSuccess) {// 失败就累加次数
                        one++;
                        texttop.setText("验证失败" + one + "次");
                    } else {
                        two++;
                        texttop.setText("验证失败" + two + "次");
                    }
                    if (one >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguanone");
                        ShareUtil.ivalBack = null;
                        ShareUtil.finger_bitmap_left = null;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    } else if (two >= FixationValue.PRESS) {
                        Intent intent = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FLAG", "kuguantwo");
                        bundle2.putString("name1", name_left.getText().toString());
                        System.out.println("您是什么账号?:" + name_left.getText().toString());
                        if (ShareUtil.finger_bitmap_left != null)
                            GApplication.map = ShareUtil.finger_bitmap_left;
                        intent.putExtras(bundle2);
                        texttop.setText("");
                        f1 = "1";
                        Skip.skip(KuguanDengluActivity.this, KuguanCheckFingerActivity.class, bundle2, 0);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            firstSuccess = false;
            f1 = null;
            f2 = null;
            if (ShareUtil.zhiwenid_left != null) {
                ShareUtil.zhiwenid_left = null;
            }
            if (ShareUtil.zhiwenid_right != null) {
                ShareUtil.zhiwenid_right = null;
            }
            if (GApplication.kuguan1 != null) {
                GApplication.kuguan1 = null;
            }
            if (GApplication.kuguan2 != null) {
                GApplication.kuguan2 = null;
            }
            Skip.skip(KuguanDengluActivity.this, KuanxiangCaidanActivity.class, null, 0);
            KuguanDengluActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("kuguandenglu", "onStop");

        manager.getRuning().remove();
        if (fname_left == null || fname_right == null) {
            KuguanDengluActivity.this.finish();
        }
    }
}
