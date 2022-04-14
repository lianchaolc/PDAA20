package com.main.pda;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.application.GApplication;
import com.clearadmin.pda.SonLibraryCodeScanActivity;
import com.example.app.activity.KuanxiangCaidanActivity;
import com.example.pda.CashtopackageSelectActivity;
import com.example.pda.HomeMangeerToCenterDataScanActivity;
import com.example.pda.HomeMangerAddPackageActivity;
import com.example.pda.R;
import com.example.pda.inventory.InventoryActivity;
import com.golbal.pda.GolbalUtil;
import com.ljsw.collateraladministratorsorting.activity.SelectTaskByCollateralActivity;
import com.ljsw.pdachecklibrary.CheckLibraryByCollMangerActivity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.cash.BaggingActivitySend;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.GetTurnoverboxchecketLineinfoActivity;
import com.ljsw.tjbankpda.main.ZhouzhuanxiangMenu;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.moneyboxadmin.pda.BankDoublePersonLogin;
import com.out.admin.pda.OrderWork;
import com.pda.checksupplement.activity.CheckLibrarySupplementActivity;
import com.service.FixationValue;

public class HomeMenu extends Activity implements OnTouchListener {
    // 功能菜单界面

    ImageView atmboxadmin; // ATM钞箱管理
    ImageView systemmanager; // 系统管理
    ImageView clearmanager; // 清分管理
    ImageView outjoin; // 出库交接
    private ImageView kuanxiangguanli;

    ImageView dizhiyapiniv;// 抵制押品管理
    private ImageView zhouzhuanxiang;
    private ImageView checklibrarysupplementimage;// 查库盘库补录图片
    private ImageView home_menu_img_accountcenter;// 账户中心图片
    private ImageView dizhiyapin;// 抵质押品管理
    private ImageView home_menu_img_cleantohome;// 图片库管员现金装袋后交接接图片
    private ImageView home_menu_img_dizhimnagepancha;//  imageview盘查库
    private ImageView home_menu_img_dizhimnagepanchapc;//盘查库
    private  ImageView  home_menu_img_cleanmangerclean;// 抵质押品清分员29  清分图片

    Bundle bundle = new Bundle();

    LinearLayout latmboxadmin; // ATM钞箱管理
    LinearLayout lsystemmanager; // 系统管理
    LinearLayout lclearmanager; // 清分管理
    LinearLayout loutjoin; // 出库交接
    LinearLayout kxgl; // 款箱管理
    LinearLayout zzxgl;
    LinearLayout ll_look_storage; // 查库服务
    private LinearLayout llaydizhiyapinmanager;// 抵制押品管理
    private LinearLayout home_menu_layout_accountcenter;// 账户资料管理

    private LinearLayout llchecklibrarysupplement; // 查库盘库补录
    private TextView dizhioraccountintiontitle, textViewaccountcenter, textView7;// 抵制押品和账户资料文字显示
    private LinearLayout home_menu_layout_cash_topackege;// 现金装袋a
    private ScrollView home_scrowview;
    private ManagerClass manager;
    private TextView textViewcleantohome;// 现金装袋交接流程 清分->库管
    private LinearLayout home_menu_layout_cleantohome;// 现金装袋后交接线性布局
    private LinearLayout homemangertopacke;// 库管员装袋
    private LinearLayout home_menu_layout_homemanger_watershow;// 库管员水牌处理

    private GolbalUtil getUtil;
    private LinearLayout home_menu_layout_sublibrarytwocode;//  f分库
    private LinearLayout home_menu_layout_cleanmanger;//  清分管理员
    private LinearLayout home_menu_layout_dizhimnagepancha;//  盘查库布局主
    private LinearLayout home_menu_layout_cleanmangerclean;//  清分
    private LinearLayout home_menu_layout_dizhimnagepanchapc;//  盘查库

    public GolbalUtil getGetUtil() {
        if (getUtil == null) {
            getUtil = new GolbalUtil();
        }
        return getUtil;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.y_home_menus);
        manager = new ManagerClass();
        // 全局异常处理
        // CrashHandler.getInstance().init(this);
        home_menu_img_dizhimnagepancha = (ImageView) findViewById(R.id.home_menu_img_dizhimnagepancha);// 29盘查库imaview
        home_menu_img_dizhimnagepancha.setOnTouchListener(this);
        home_scrowview = (ScrollView) findViewById(R.id.home_sv);// 可滑动的
        textViewcleantohome = (TextView) findViewById(R.id.textViewcleantohome); /// 清分员装袋交接
        textViewcleantohome.setText("清分员装袋交接");
        home_menu_img_cleantohome = (ImageView) findViewById(R.id.home_menu_img_cleantohome);
        atmboxadmin = (ImageView) findViewById(R.id.money_box_admins);
        systemmanager = (ImageView) findViewById(R.id.systemanager);
        clearmanager = (ImageView) findViewById(R.id.cleanadmin);
        outjoin = (ImageView) findViewById(R.id.outjoin);
        kuanxiangguanli = (ImageView) findViewById(R.id.kuanxiangguanli);
        zhouzhuanxiang = (ImageView) findViewById(R.id.home_menu_img_zzximg);
        dizhiyapiniv = (ImageView) findViewById(R.id.home_menu_img_dizhiyapiniv);// 抵制押品
        checklibrarysupplementimage = (ImageView) findViewById(R.id.checklibrarysupplementimage);
        dizhioraccountintiontitle = (TextView) findViewById(R.id.textViewdizhiyapin);
        textView7 = (TextView) findViewById(R.id.textView7);// 设置文字
        home_menu_img_accountcenter = (ImageView) findViewById(R.id.home_menu_img_accountcenter);
        home_menu_img_accountcenter.setOnTouchListener(this);

        home_menu_img_cleanmangerclean=findViewById(R.id.home_menu_img_cleanmangerclean);// 清分员清分
        home_menu_img_cleanmangerclean.setOnTouchListener(this);
        textViewaccountcenter = (TextView) findViewById(R.id.textViewaccountcenter);
        home_menu_layout_sublibrarytwocode = findViewById(R.id.home_menu_layout_sublibrarytwocode);
        home_menu_layout_sublibrarytwocode.setOnTouchListener(this);
        home_menu_layout_cleanmanger = findViewById(R.id.home_menu_layout_cleanmanger);
        home_menu_layout_cleanmanger.setOnTouchListener(this);
        home_menu_img_dizhimnagepanchapc=findViewById(R.id.home_menu_img_dizhimnagepanchapc);
        home_menu_img_dizhimnagepanchapc.setOnTouchListener(this);
        if (GApplication.user.getLoginUserId().equals("4")) {
            dizhioraccountintiontitle.setText("档案柜管理");
            textView7.setText("周转箱管理");
        } else if (GApplication.user.getLoginUserId().equals("7")) {
            dizhioraccountintiontitle.setText("现金装袋");// 抵质押品改为现金装袋
            textView7.setText("周转箱清分");
        } else if (GApplication.user.getLoginUserId().equals("9")) {// 押运人员

            textView7.setText("周转箱管理");
        } else if (GApplication.user.getLoginUserId().equals("19")) {// 外包清分
            textView7.setText("周转箱管理");
        }

        zzxgl = (LinearLayout) findViewById(R.id.home_menu_layout_zzx);
        ll_look_storage = (LinearLayout) findViewById(R.id.ll_look_storage);
        latmboxadmin = (LinearLayout) findViewById(R.id.lmoney_box_admins);
        lsystemmanager = (LinearLayout) findViewById(R.id.lsystemanager);
        lclearmanager = (LinearLayout) findViewById(R.id.lcleanadmin);
        loutjoin = (LinearLayout) findViewById(R.id.loutjoin);
        loutjoin = (LinearLayout) findViewById(R.id.loutjoin);
        kxgl = (LinearLayout) findViewById(R.id.zzxgl1);
        home_menu_layout_cleantohome = (LinearLayout) findViewById(R.id.home_menu_layout_cleantohome);// 清分员现金装袋到库管员
        // 盘库和查库
        llchecklibrarysupplement = (LinearLayout) findViewById(R.id.llchecklibrarysupplement);
        home_menu_layout_cash_topackege = (LinearLayout) findViewById(R.id.home_menu_layout_cash_topackege);
        // 账户资料
        home_menu_layout_accountcenter = (LinearLayout) findViewById(R.id.home_menu_layout_accountcenter);
        home_menu_layout_accountcenter.setOnTouchListener(this);
        // 抵制押品
        llaydizhiyapinmanager = (LinearLayout) findViewById(R.id.home_menu_layout_dizhiyapin);
        llaydizhiyapinmanager.setOnTouchListener(this);
        // 现金装袋
        checklibrarysupplementimage.setOnTouchListener(this);
        llchecklibrarysupplement.setOnTouchListener(this);
//		kxgl=(LinearLayout)findViewById(R.id.zzxgl);
        // 抵制押品
        home_menu_layout_dizhimnagepancha = (LinearLayout) findViewById(R.id.home_menu_layout_dizhimnagepancha);// 盘查库
        home_menu_layout_dizhimnagepancha.setOnTouchListener(this);
        //	清分单独拿出来
        home_menu_layout_cleanmangerclean = (LinearLayout) findViewById(R.id.home_menu_layout_cleanmangerclean);
        home_menu_layout_cleanmangerclean.setOnTouchListener(this);
        home_menu_layout_dizhimnagepancha.setOnTouchListener(this);
//抵质押品管库元
        home_menu_layout_dizhimnagepanchapc=findViewById(R.id.home_menu_layout_dizhimnagepanchapc);
        home_menu_layout_dizhimnagepanchapc.setOnTouchListener(this);
        homemangertopacke = (LinearLayout) findViewById(R.id.home_menu_layout_homemangertopacke);
        homemangertopacke.setOnTouchListener(this);
        home_menu_layout_cleantohome.setOnTouchListener(this);
        home_menu_layout_homemanger_watershow = (LinearLayout) findViewById(R.id.home_menu_layout_homemanger_watershow);
        home_menu_layout_homemanger_watershow.setOnTouchListener(this);
        atmboxadmin.setOnTouchListener(this);
        systemmanager.setOnTouchListener(this);
        clearmanager.setOnTouchListener(this);
        outjoin.setOnTouchListener(this);
        kuanxiangguanli.setOnTouchListener(this);
        zhouzhuanxiang.setOnTouchListener(this);
        ll_look_storage.setOnTouchListener(this);
        home_menu_layout_cash_topackege.setOnTouchListener(this);
        dizhiyapiniv.setOnTouchListener(this);
        if (GApplication.user.getLoginUserId() != null) {

            System.out.print("id====" + GApplication.user.getLoginUserId());
            show(Integer.parseInt(GApplication.user.getLoginUserId()));
        }
    }

    // 触摸事件
    @Override
    public boolean onTouch(View view, MotionEvent even) {
        // 按下的时候
        if (MotionEvent.ACTION_DOWN == even.getAction()) {

            switch (view.getId()) {
                // ATM钞箱管理
                case R.id.money_box_admins:
                    atmboxadmin.setImageResource(R.drawable.atmbox_down);
                    break;
                // 系统管理
                case R.id.systemanager:
                    systemmanager.setImageResource(R.drawable.systemadmin_down);
                    break;
                // 清分管理
                case R.id.cleanadmin:
                    clearmanager.setImageResource(R.drawable.clearf_down);
                    break;
                // 出库交接
                case R.id.outjoin:
                    outjoin.setImageResource(R.drawable.warehouse_down);
                    break;
            }

        }

        // 手指松开的时候
        if (MotionEvent.ACTION_UP == even.getAction()) {
            Log.i("getGetUtil().ismover", getGetUtil().ismover + "");
            switch (view.getId()) {

                // ATM钞箱管理
                case R.id.money_box_admins:
                    atmboxadmin.setImageResource(R.drawable.atmbox);
                    // 跳到库管双人登陆页面
                    bundle.putString("user", "库管员");
                    getGetUtil().gotoActivity(HomeMenu.this, BankDoublePersonLogin.class, bundle, getGetUtil().ismover);
//						HomeMenu.this.finish();//关闭当前页面
                    break;
                // 系统管理
                case R.id.systemanager:
                    systemmanager.setImageResource(R.drawable.systemadmin);
                    Log.i("getGetUtil()", getUtil + "");
                    getGetUtil().gotoActivity(HomeMenu.this, SystemManager.class, null, getGetUtil().ismover);
                    break;
                // 清分管理
                case R.id.cleanadmin:
                    clearmanager.setImageResource(R.drawable.clearf);
                    // 跳到清分员交接验证页面
                    bundle.putString("user", "清分员");
                    bundle.putString("where", "清分管理");
                    getGetUtil().gotoActivity(HomeMenu.this, BankDoublePersonLogin.class, bundle, getGetUtil().ismover);
                    break;
                // 出库交接
                case R.id.outjoin:
                    outjoin.setImageResource(R.drawable.warehouse);
                    getGetUtil().gotoActivity(HomeMenu.this, OrderWork.class, null, getGetUtil().ismover);
                    break;
                case R.id.kuanxiangguanli:
                    outjoin.setImageResource(R.drawable.warehouse);
                    getGetUtil().gotoActivity(HomeMenu.this, KuanxiangCaidanActivity.class, null, getGetUtil().ismover);
                    break;
                case R.id.home_menu_img_zzximg:
                    getGetUtil().gotoActivity(HomeMenu.this, ZhouzhuanxiangMenu.class, null, getGetUtil().ismover);
                    break;
                case R.id.ll_look_storage: // 查库服务
                /*
                 * 进入双人指纹验证界面
				 * 
				 * @author zhouKai
				 */
                    bundle.putString("user", "库管员");
                    bundle.putString("taskType", "lookStorageService");
                    getGetUtil().gotoActivity(HomeMenu.this, BankDoublePersonLogin.class, bundle, getGetUtil().ismover);
                    break;
                case R.id.home_menu_img_dizhiyapiniv: // 抵制押品管理图片
                    bundle.putString("user", "清分员");
                    bundle.putString("where", "清分管理");

                    if (GApplication.user.getLoginUserId().equals("4")) {
                        getGetUtil().gotoActivity(HomeMenu.this, DiZhiYaPinKuangJiaActivity.class, null,
                                getGetUtil().ismover);
                    } else if (GApplication.user.getLoginUserId().equals("7")) {
//							现金装袋
                        getGetUtil().gotoActivity(HomeMenu.this, CashtopackageSelectActivity.class, null,
                                getGetUtil().ismover);
                    }

                    break;
                case R.id.home_menu_layout_accountcenter:// 账户中心
                    getGetUtil().gotoActivity(HomeMenu.this, DiZhiYaPinKuangJiaActivity.class, null, getGetUtil().ismover);
                    break;

                case R.id.home_menu_layout_cash_topackege:// 制卡
                    getGetUtil().gotoActivity(HomeMenu.this, BaggingActivitySend.class, null, getGetUtil().ismover);
//			                startActivity(intent);
                    break;

                // case R.id.checklibrarysupplementimage /// 现金业务暂时不清楚待做
                case R.id.checklibrarysupplementimage:
                    getGetUtil().gotoActivity(HomeMenu.this, CheckLibrarySupplementActivity.class, null,
                            getGetUtil().ismover);

                    break;
                /// 制卡尾零和整钞
                case R.id.home_menu_img_accountcenter:
                    getGetUtil().gotoActivity(HomeMenu.this, BaggingActivitySend.class, null, getGetUtil().ismover);
                    break;

                case R.id.home_menu_layout_cleantohome:/// 库管员现金装袋页面跳转
                    getGetUtil().gotoActivity(HomeMenu.this, HomeMangeerToCenterDataScanActivity.class, null,
                            getGetUtil().ismover);
                    break;

                case R.id.home_menu_layout_homemangertopacke:// 库管员处理中银行的钱
                    getGetUtil().gotoActivity(HomeMenu.this, HomeMangerAddPackageActivity.class, null,
                            getGetUtil().ismover);
                    break;
                case R.id.home_menu_layout_homemanger_watershow:// 库管员处理水牌
                    getGetUtil().gotoActivity(HomeMenu.this, InventoryActivity.class, null, getGetUtil().ismover);
                    break;

                case R.id.home_menu_layout_sublibrarytwocode:// 分库二维码扫描
                    getGetUtil().gotoActivity(HomeMenu.this, SonLibraryCodeScanActivity.class, null, getGetUtil().ismover);

                    break;

                case R.id.home_menu_layout_cleanmanger:// q清分管理员核对
                    getGetUtil().gotoActivity(HomeMenu.this, GetTurnoverboxchecketLineinfoActivity.class, null, getGetUtil().ismover);

                    break;

                //补扫
                case R.id.home_menu_img_dizhimnagepancha:
                    getGetUtil().gotoActivity(HomeMenu.this, CheckLibraryByCollMangerActivity.class, null, getGetUtil().ismover);
                    break;
                //盘查库任务

                case R.id.home_menu_img_dizhimnagepanchapc:
                    Bundle  bundle=new Bundle();
                    bundle.putString("pancha","盘查");
                    getGetUtil().gotoActivity(HomeMenu.this, CheckLibraryByCollMangerActivity.class, bundle, getGetUtil().ismover);
                    break;

//清分操          作 抵质押品管理员 清分
                case  R.id.home_menu_img_cleanmangerclean:

                    getGetUtil().gotoActivity(HomeMenu.this , SelectTaskByCollateralActivity.class, null, getGetUtil().ismover);
                    break;


                default:
                    break;

            }


            getGetUtil().ismover = 0;
        }
        // 手指移动的时候
        if (MotionEvent.ACTION_MOVE == even.getAction()) {
            getGetUtil().ismover++;
        }
        // 意外中断事件取消
        if (MotionEvent.ACTION_CANCEL == even.getAction()) {
            switch (view.getId()) {
                // ATM钞箱管理
                case R.id.money_box_admins:
                    atmboxadmin.setImageResource(R.drawable.atmbox);
                    break;
                // 系统管理
                case R.id.systemanager:
                    systemmanager.setImageResource(R.drawable.systemadmin);
                    break;
                // 清分管理
                case R.id.cleanadmin:
                    clearmanager.setImageResource(R.drawable.clearf);
                    break;
                // 出库交接
                case R.id.outjoin:
                    outjoin.setImageResource(R.drawable.warehouse);
                    break;
            }
            getGetUtil().ismover = 0;
        }

        return true;
    }

    /**
     * 根据权限显示图标 传入的角色编号（3456789）
     *
     * @param permission
     */
    public void show(int permission) {
        switch (permission) {
            case FixationValue.examine: // 审核岗
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE); //清分
            /*
             * 清楚查库服务按钮，下同
			 * 
			 * @author zhouKai
			 */
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case FixationValue.warehouse: // 管库员
                latmboxadmin.setVisibility(View.VISIBLE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.VISIBLE);
                zzxgl.setVisibility(View.VISIBLE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务 // 去掉查库 reviseed by zhangxuewei
                llaydizhiyapinmanager.setVisibility(View.VISIBLE);// 抵制押品
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.VISIBLE);
                home_menu_layout_cleantohome.setVisibility(View.VISIBLE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.VISIBLE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case 5: // 加钞人员
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.VISIBLE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case 6: // 网点加钞
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.VISIBLE); // 出库交接
                kxgl.setVisibility(View.VISIBLE);
                zzxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case FixationValue.clearer: // 清分员
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.VISIBLE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.VISIBLE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.VISIBLE);
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.VISIBLE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case FixationValue.supercargo: // 押运员
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.VISIBLE); // 出库交接
                kxgl.setVisibility(View.VISIBLE);
                zzxgl.setVisibility(View.VISIBLE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case FixationValue.waibaoqingfen: // 外包清分员
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.GONE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.VISIBLE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;

            case 27:/// 账户中心
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.GONE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                // 账户中心
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理///
                home_menu_layout_cash_topackege.setVisibility(View.GONE); /////
                home_menu_layout_accountcenter.setVisibility(View.VISIBLE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE); //盘查库任务补扫
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
            case 17:/// 清分管理员
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.GONE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llaydizhiyapinmanager.setVisibility(View.GONE);// 抵制押品
                // 账户中心
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理///
                home_menu_layout_cash_topackege.setVisibility(View.GONE); /////
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmanger.setVisibility(View.GONE); //清分管理员核对数据明细\
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;

            case 29://  王姐角色29 下显示5个图标
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理


                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                // 账户中心
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理///

                home_menu_layout_accountcenter.setVisibility(View.GONE);
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.VISIBLE);//  fenku 二维码扫描
                home_menu_layout_cleanmanger.setVisibility(View.GONE); //清
                llaydizhiyapinmanager.setVisibility(View.GONE);
                lsystemmanager.setVisibility(View.VISIBLE); // 系统管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE); /////制卡
                home_menu_layout_dizhimnagepancha.setVisibility(View.VISIBLE);// 盘查库抵质押品
                home_menu_layout_cleanmangerclean.setVisibility(View.VISIBLE);
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.VISIBLE);
//				清分

                break;
            default:
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                latmboxadmin.setVisibility(View.GONE); // ATM钞箱管理
                lsystemmanager.setVisibility(View.GONE); // 系统管理
                lclearmanager.setVisibility(View.GONE); // 清分管理
                loutjoin.setVisibility(View.GONE); // 出库交接
                kxgl.setVisibility(View.GONE);
                ll_look_storage.setVisibility(View.GONE); // 查库服务
                llchecklibrarysupplement.setVisibility(View.GONE);// 库存管理
                home_menu_layout_cash_topackege.setVisibility(View.GONE);
                zzxgl.setVisibility(View.GONE);
                home_menu_layout_accountcenter.setVisibility(View.GONE);
                llaydizhiyapinmanager.setVisibility(View.GONE); // 档案柜管理
                home_menu_layout_cleantohome.setVisibility(View.GONE);// 装袋（正钞和尾零）
                homemangertopacke.setVisibility(View.GONE);/// 库管员处理中银行的钱
                home_menu_layout_homemanger_watershow.setVisibility(View.GONE);/// 库管员处理水牌
                home_menu_layout_sublibrarytwocode.setVisibility(View.GONE);//  fenku 二维码扫描
                home_menu_layout_cleanmanger.setVisibility(View.GONE); //清分管理员核对数据明细
                home_menu_layout_cleanmangerclean.setVisibility(View.GONE); //  清分单独拿出来
                home_menu_layout_dizhimnagepancha.setVisibility(View.GONE);
                home_menu_layout_dizhimnagepanchapc.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("a");
        return true;
    }

    // 拦截Menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        new MenuShow().menu(this);
        MenuShow.pw.showAtLocation(findViewById(R.id.homemenu_box), Gravity.BOTTOM, 0, 0);
        return false;
    }

}