package com.ljsw.tjbankpda.main;

import android.os.SystemClock;

import com.application.GApplication;
import com.clearadmin.pda.ClearAddMoneyOutDo;
import com.example.pda.R;
import com.example.pda.homemagnetopackge.AccountinformationSelectMainActivity;
import com.example.pda.homemagnetopackge.HouDuActivity;
import com.ljsw.tjbankpda.db.activity.KuGuanLogin_db;
import com.ljsw.tjbankpda.util.onTouthImageStyle;
import com.ljsw.tjbankpda.yy.activity.YayunLoginSAcitivity;
import com.ljsw.tjbankpda.yy.activity.ZhouzXixinSmiaoActivity;
import com.manager.classs.pad.ManagerClass;
import com.service.FixationValue;
import com.clearadmin.pda.SonLibraryCodeScanActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ZhouzhuanxiangMenu extends Activity implements OnTouchListener {
    private LinearLayout llQingfen;// 清分管理
    private LinearLayout llDiaobo;// 调拨管理
    private LinearLayout llYayun;// 押运管理
    private LinearLayout ll2;
    private LinearLayout ll1;
    private ImageView ivQinfen;
    private ImageView ivShaomiao;
    private ImageView ivDiaobo;
    private ImageView ivYayun;
    private TextView tvQinfen;
    private TextView tvShaomiao;
    private ManagerClass manager;
    private LinearLayout ll_zhanghuziliao_menu_yayun;// 账户资料
    private LinearLayout ll_houduzhangb_menu_yayun;// 后督账包
    private LinearLayout ll_account_menu_yayun;// 账户资料
    private int move = 0;
    private LinearLayout ll_library_menu_yayun; /// 分库业务

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhouzhuanxiao_menu);
        llDiaobo = (LinearLayout) findViewById(R.id.ll_zhouzhuanxiao_menu_diaobo);
        llQingfen = (LinearLayout) findViewById(R.id.ll_zhouzhuanxiao_menu_qingfen);
        llYayun = (LinearLayout) findViewById(R.id.ll_zhouzhuanxiao_menu_yayun);
        ll2 = (LinearLayout) findViewById(R.id.ll_zhouzhuanxiao_menu_2);
        ll1 = (LinearLayout) findViewById(R.id.ll_zhouzhuanxiao_menu_1);
        ivQinfen = (ImageView) findViewById(R.id.iv_zhouzhuanxiao_menu_qingfen);
        ivShaomiao = (ImageView) findViewById(R.id.iv_zhouzhuanxiao_menu_saomiao);
        ivDiaobo = (ImageView) findViewById(R.id.iv_zhouzhuanxiao_menu_diaobo);
        ivYayun = (ImageView) findViewById(R.id.iv_zhouzhuanxiao_menu_yayun);
        tvQinfen = (TextView) findViewById(R.id.tv_zhouzhuanxiao_menu_qingfen);
        tvShaomiao = (TextView) findViewById(R.id.tv_zhouzhuanxiao_menu_saomiao);
//		 后督账包操作  的点击 页面
        ll_zhanghuziliao_menu_yayun = (LinearLayout) findViewById(R.id.ll_postman_menu_yayun);
        ll_zhanghuziliao_menu_yayun.setOnTouchListener(this);
        ll_account_menu_yayun = (LinearLayout) findViewById(R.id.ll_account_menu_yayun);
        ll_account_menu_yayun.setOnTouchListener(this);
        ll_library_menu_yayun = findViewById(R.id.ll_library_menu_yayun);
        ll_library_menu_yayun.setOnTouchListener(this);
        manager = new ManagerClass();

        // 判断登陆用户角色身份，限制功能图标显 这里需要异常处理
        System.out.println("当前用户身份为=" + GApplication.user.getLoginUserId());
        if ("7".equals(GApplication.user.getLoginUserId()) || "17".equals(GApplication.user.getLoginUserId())) { // 如果清分员或者清分管理员
            ll1.setVisibility(View.GONE);


            ll_zhanghuziliao_menu_yayun.setVisibility(View.GONE);
            ll_account_menu_yayun.setVisibility(View.GONE);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ivQinfen.setImageResource(R.drawable.menu_qingfen);
            tvQinfen.setText("清分管理");
            ivShaomiao.setImageResource(R.drawable.saomiao);
            tvShaomiao.setText("周转箱扫描");
            ivQinfen.setOnTouchListener(this);
            ivShaomiao.setOnTouchListener(this);
        } else if ("4".equals(GApplication.user.getLoginUserId())
                || (FixationValue.waibaoQingfenString).equals(GApplication.user.getLoginUserId())) { // 如果是库管员
            if ((FixationValue.waibaoQingfenString).equals(GApplication.user.getLoginUserId())) {
                ll_zhanghuziliao_menu_yayun.setVisibility(View.GONE);
                ll_account_menu_yayun.setVisibility(View.GONE);
                ll_library_menu_yayun.setVisibility(View.GONE);
            } else {
                ll_zhanghuziliao_menu_yayun.setVisibility(View.VISIBLE);
                ll_account_menu_yayun.setVisibility(View.VISIBLE);
                ll_library_menu_yayun.setVisibility(View.GONE);
            }

            llQingfen.setVisibility(View.GONE);
            llYayun.setVisibility(View.GONE);
            ivDiaobo.setOnTouchListener(this);
            ivShaomiao.setOnTouchListener(this);
        } else if ("9".equals(GApplication.user.getLoginUserId())) { // 如果是押运员
            llDiaobo.setVisibility(View.GONE);
            llQingfen.setVisibility(View.GONE);
            ll_zhanghuziliao_menu_yayun.setVisibility(View.GONE);
            ll_account_menu_yayun.setVisibility(View.GONE);
            ll_library_menu_yayun.setVisibility(View.GONE);
            ivYayun.setOnTouchListener(this);
            ivShaomiao.setOnTouchListener(this);
        } else if ("4".equals(GApplication.user.getLoginUserId())) {
            llQingfen.setVisibility(View.GONE);
//			ll_zhanghuziliao_menu_yayun.setVisibility(View.GONE);
            llYayun.setVisibility(View.GONE);
            ll_zhanghuziliao_menu_yayun.setVisibility(View.VISIBLE);
            ll_account_menu_yayun.setVisibility(View.VISIBLE);
            ll_library_menu_yayun.setVisibility(View.GONE);
        }else{
            ll_library_menu_yayun.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                manager.getRuning().remove();
            }
        }).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.iv_zhouzhuanxiao_menu_qingfen:
                    new onTouthImageStyle().setFilter(ivQinfen);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_diaobo:
                    new onTouthImageStyle().setFilter(ivDiaobo);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_yayun:
                    new onTouthImageStyle().setFilter(ivYayun);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_saomiao:
                    new onTouthImageStyle().setFilter(ivShaomiao);
                    break;
                default:
                    break;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            move++;
            System.out.println("QingfenMenu--move:" + move);
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                // 清分管理
                case R.id.iv_zhouzhuanxiao_menu_qingfen:
                    new onTouthImageStyle().removeFilter(ivQinfen);
                    if (move < 10) {
                        manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在开启指纹扫描，请稍等...");
                        Intent intent = new Intent(ZhouzhuanxiangMenu.this, QingfenDengLuAcyivity.class);
                        startActivity(intent);
                    }
                    break;
                // 调拨管理
                case R.id.iv_zhouzhuanxiao_menu_diaobo:
                    new onTouthImageStyle().removeFilter(ivDiaobo);
                    if (move < 10) {
                        manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在开启指纹扫描，请稍等...");
                        Intent intent = new Intent(ZhouzhuanxiangMenu.this, KuGuanLogin_db.class);
                        startActivity(intent);
                    }
                    break;
                // 押运管理
                case R.id.iv_zhouzhuanxiao_menu_yayun:
                    new onTouthImageStyle().removeFilter(ivYayun);
                    if (move < 10) {
                        manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在开启指纹扫描,请稍等...");
                        Intent intent = new Intent(ZhouzhuanxiangMenu.this, YayunLoginSAcitivity.class);
                        startActivity(intent);
                    }
                    break;
                // 周转箱扫描
                case R.id.iv_zhouzhuanxiao_menu_saomiao:
                    new onTouthImageStyle().removeFilter(ivShaomiao);
                    if (move < 10) {
                        manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在开启RFID扫描，请稍后...");
                        Intent intent = new Intent(ZhouzhuanxiangMenu.this, ZhouzXixinSmiaoActivity.class);
                        startActivity(intent);
                    }
                    break;

                // 后督账包
                case R.id.ll_postman_menu_yayun:
                    manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在跳转，请稍后...");
                    Intent intent = new Intent(ZhouzhuanxiangMenu.this, HouDuActivity.class);
                    startActivity(intent);
                    break;

                // 账户资料
                case R.id.ll_account_menu_yayun:
                    manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在跳转，请稍后...");
                    Intent intent2 = new Intent(ZhouzhuanxiangMenu.this, AccountinformationSelectMainActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.ll_library_menu_yayun:
                    manager.getRuning().runding(ZhouzhuanxiangMenu.this, "正在跳转，请稍后...");
                    Intent ll_library = new Intent(ZhouzhuanxiangMenu.this, SonLibraryCodeScanActivity.class);
//                    Intent ll_library = new Intent(ZhouzhuanxiangMenu.this, ClearAddMoneyOutDo.class);
                    startActivity(ll_library);

                default:
                    break;
            }
            move = 0;
        }
        if (e.getAction() == MotionEvent.ACTION_CANCEL) {
            switch (v.getId()) {
                case R.id.iv_zhouzhuanxiao_menu_qingfen:
                    new onTouthImageStyle().removeFilter(ivQinfen);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_diaobo:
                    new onTouthImageStyle().removeFilter(ivDiaobo);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_yayun:
                    new onTouthImageStyle().removeFilter(ivYayun);
                    break;
                case R.id.iv_zhouzhuanxiao_menu_saomiao:
                    new onTouthImageStyle().removeFilter(ivShaomiao);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

}
