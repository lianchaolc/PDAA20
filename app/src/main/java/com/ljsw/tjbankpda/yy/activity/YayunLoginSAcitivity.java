package com.ljsw.tjbankpda.yy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.app.entity.User;
import com.example.pda.R;
import com.example.pda.YayunSelectRewuUseActivity;
import com.example.pda.YayunSelectTaskActivity;
import com.golbal.pda.GolbalUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRenwuUserDispatchList;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuUserEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunyuanSelectRenWuUserBaseEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunyuanSelectRenWuUserCodeEntity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.YaYunLb;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.IPdaOfBoxOperateService;
import com.manager.classs.pad.ManagerClass;
import com.poka.device.ShareUtil;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import afu.util.BaseFingerActivity;

/**
 * 押运员单人登录页面
 * 登录的时候 指纹放在Ga
 * 账号登录用的 o_Application.yayunyuan
 *
 * @author Administrator
 */
public class YayunLoginSAcitivity extends BaseFingerActivity {

    protected static final String TAG = "YayunLoginSAcitivity";
    private TextView top, fname, bottom;// 顶部提示 指纹对应人员姓名 底部提示
    private ImageView finger;// 指纹图片
    public User result_user;
    private int fingerCount;// 验证指纹失败的次数

    private String userid = "";// 用户的id全局

    private Intent intent;
    private ManagerClass managerClass;
    private ManagerClass managerClassbutn;

    public Handler handler;
    private boolean isFlag = true;

    private boolean selectTast = false;// 标识当前是否有任务

    @SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yayun_login_s);

        initView();

        intent = new Intent();
        managerClass = new ManagerClass();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (managerClass == null) {
                    managerClass = new ManagerClass();
                }

                super.handleMessage(msg);
                managerClass.getRuning().remove();
                isFlag = true;
                switch (msg.what) {
                    case 1:// 验证成功跳转

                        finger.setImageBitmap(ShareUtil.finger_gather);
                        fname.setText(result_user.getUsername());
                        top.setText("指纹验证成功");

                        System.out.print("!!!" + result_user.getUserid() + ":::"
                                + result_user.getUsername() + "!!!" + result_user.getUserzhanghu());
                        managerClass.getSureCancel().makeSuerCancel3(
                                YayunLoginSAcitivity.this, "" + result_user.getUsername(), "", result_user.getUserzhanghu(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "正在查询,请稍等...");
                                        Log.d(TAG, "===GApplication.use" + GApplication.use.getUserzhanghu());
                                        S_application.s_userYayun = result_user
                                                .getUserzhanghu();
                                        S_application.s_userYayunName=result_user.getUsername();
                                        getescortselectTask();

                                    }


                                }, false);
                        break;
                    case -1:
                        // top.setText("验证异常，请重按");
                        S_application.s_userYayun = null;// 修改20200320
                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
                            intent.setClass(YayunLoginSAcitivity.this,
                                    YayunDenglu.class);
                            YayunLoginSAcitivity.this.startActivityForResult(
                                    intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;
                    case -4:

                        top.setText("验证超时，请重按");
                        break;
                    case 0:

                        fingerCount++;
                        top.setText("验证失败" + fingerCount + "次，请重按");
                        if (fingerCount >= ShareUtil.three) {
                            // 跳用户登录
                            intent.setClass(YayunLoginSAcitivity.this,
                                    YayunDenglu.class);
                            YayunLoginSAcitivity.this.startActivityForResult(
                                    intent, 1);
                            top.setText("");
                            fingerCount = 0;
                        }
                        break;
                    case 10:

                    case 11:
                        //					Skip.skip(YayunLoginSAcitivity.this, YayunRwLbSActivity.class,null, 0);  // 修改以前的接口
                        Skip.skip(YayunLoginSAcitivity.this,
                                YayunSelectRewuUseActivity.class, null, 0);
                        break;


                    case 9:
                        Skip.skip(YayunLoginSAcitivity.this,
                                YayunSelectTaskActivity.class, null, 0);
//								YayunRwLbSActivity.class, null, 0);
                        break;
                    case 12:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "网络请求失败",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "网络请求失败");
                                        managerClass.getRuning().remove();
                                    }
                                }, false);
                        break;
                    case 13:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "服务器没数据",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "网络请求失败");
                                        managerClass.getRuning().remove();
                                    }


                                }, false);
                        break;
                    case 14:
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "网络请求失败",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "连接超时");
                                        managerClass.getRuning().remove();
                                    }
                                }, false);
                        break;


                    case 15:
                        if(null!=managerClassbutn){

                        }else{
                            managerClassbutn = new ManagerClass();
                        }
                        managerClass.getRuning().remove();
                        if(managerClassbutn.getSureCancel()!=null){
                            managerClassbutn.getSureCancel().remove4();
                        }
                        managerClassbutn.getSureCancel().makeSuerCancel4(
                                YayunLoginSAcitivity.this,
                                S_application.s_userYayunName,
                                S_application.s_userYayun ,
                                "本次领取任务线路：" + linename,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
//                                                "即将请稍后...");
                                        Log.d(TAG, "===GApplication.use" + GApplication.use.getUserzhanghu());
                                        managerClass.getSureCancel().remove4();
                                        managerClassbutn.getSureCancel().remove4();

                                        Skip.skip(YayunLoginSAcitivity.this,
                                                YayunSelectRewuUseActivity.class, null, 0);

                                    }


                                }, false);
                        //移除上一一个遮罩
                        managerClass.getSureCancel().remove3();
                        managerClassbutn.getSureCancel().remove3();
                        break;

                    case 17:
                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                "正在查询...");
                        GetHadTask();
                        break;

                    case 18:   ///  w网络请求失败查询是否有任务
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "网络请求失败",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "网络请求失败");
                                        managerClass.getRuning().remove();
                                        getescortselectTask();
                                    }
                                }, false);
                        break;
                    case 19:   ///  w网络请求失败查询是否有任务
                        managerClass.getSureCancel().makeSuerCancel(
                                YayunLoginSAcitivity.this, "服务器链接超时",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getRuning().runding(YayunLoginSAcitivity.this,
                                                "服务器链接超时");
                                        managerClass.getRuning().remove();
                                        getescortselectTask();
                                    }
                                }, false);
                        break;

                    default:
                        managerClass.getRuning().remove();

                        break;
                }
            }
        };
    }

    public void initView() {
        fname = (TextView) findViewById(R.id.yy_fname);
        finger = (ImageView) this.findViewById(R.id.yy_image);
        top = (TextView) this.findViewById(R.id.yy_top);
        bottom = (TextView) this.findViewById(R.id.ll_yayun_bottom);
    }

    @Override
    public void onStop() {
        super.onStop();
        isFlag = false;
        managerClass.getRuning().remove();
    }

    @Override
    public void openFingerSucceed() {
        fingerUtil.getFingerCharAndImg();
    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        ShareUtil.ivalBack = charBytes;
        ShareUtil.finger_gather = img;

        if (isFlag) {
            top.setText("正在验证指纹...");
            isFlag = false;
            // 开始调用服务器验证指纹
            CheckFingerThread cf = new CheckFingerThread();
            cf.start();
        }
    }

    @Override
    public void findFinger() {
        top.setText("正在获取指纹特征值");
    }

    @Override
    public void badCharHandler() {
        top.setText("获取指纹特征值失败,请重试");
    }

    /**
     * 指纹验证线程
     *
     * @author Administrator
     */
    class CheckFingerThread extends Thread {
        Message m;

        public CheckFingerThread() {
            super();
            m = handler.obtainMessage();
        }

        @Override
        public void run() {
            super.run();
            IPdaOfBoxOperateService yz = new IPdaOfBoxOperateService();
            try {
                // 押运员的机构id默认 等于最开始登录用户的机构id
                S_application.s_yayunJigouId = GApplication.user
                        .getOrganizationId();
                System.out.println("GApplication.user.getLoginUserId():" + 9);
                Log.d(TAG, "=====+sjigou" + S_application.s_yayunJigouId + "");
                Log.d(TAG, "=====userid" + GApplication.user.getLoginUserId() + "");
                Log.d(TAG, "=====ivalBack" + ShareUtil.ivalBack + "");

                result_user = yz.checkFingerprint(
                        S_application.s_yayunJigouId,
                        GApplication.user.getLoginUserId(), ShareUtil.ivalBack);
                System.out.println("result_user============"
                        + result_user);
                System.out.println("yyl============"
                        + GApplication.user.getLoginUserId());
                System.out.println("yyl============"
                        + GApplication.user.getOrganizationId());
                if (result_user != null) {// 验证成功

                    Log.d(TAG, result_user + ":::::::::::::::------");
                    GApplication.use = result_user;

                    Log.d(TAG, GApplication.use.getUsername() + ":::::覆盖后");
                    Log.d(TAG, GApplication.use.getUserzhanghu() + ":::::覆盖后");
                    Log.d(TAG, S_application.s_userYayun + ":::::");
                    Log.d(TAG, result_user.getUsername() + ":::::");

                    m.what = 1;
                } else {
                    m.what = 0;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                m.what = -4;// 超时验证
            } catch (Exception e) {
                e.printStackTrace();
                m.what = -1;// 验证异常
            } finally {
                handler.sendMessage(m);
                GolbalUtil.onclicks = true;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            String isOk = bundle.getString("isOk");
            if (isOk.equals("success")) {
                fname.setText(GApplication.use.getUsername());//  返回系统登录人员lianchao 20201.3.31jilu  无修改
                finger.setImageResource(R.drawable.result_isok);
                bottom.setText("验证成功!");
                if (bundle.getString("name") != null
                        && !bundle.getString("name").equals("")) {
                    S_application.s_userYayun = bundle.getString("name");
                    S_application.s_userYayunName=bundle.getString("username");
                }
                // 跳转下一个页面
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            getescortselectTask();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    /***
     * 押运员是否领取任务 用户的id 押运员当前没有任务需要走
     * 修改于20191126
     * lianc
     * 201912.3 建议只判断当前是否有任务
     */

    private boolean getescortselectTask() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    System.out.println(GApplication.user.getLoginUserId() + "---" +
                            GApplication.user.getLoginUserName() + "--" +
                            GApplication.user.getOrganizationId() + "--"
                    );

                    Log.e(TAG, "获取押运员数据S_application.getApplication().s_userYayun" + S_application.s_userYayun);
                    userid = S_application.s_userYayun;
                    Log.e(TAG, "userid----" + userid);
                    if (null == userid || userid.equals("")) {
                        Log.e(TAG, "userid是===" + userid);
                    } else {
                        Log.e(TAG, "userid不为null===" + userid);
                        String
                                netresultClean = new AccountAndResistCollateralService()
                                .getescortselectTask(userid);
                        Log.e(TAG, "测试数据源----" + netresultClean + "---" + netresultClean);
                        selectTast = true;
                        if (netresultClean.equals("1")) {
                            Skip.skip(YayunLoginSAcitivity.this,
                                    YayunSelectTaskActivity.class, null, 0);//  无任务押运员身上是否有任务   有任务：0 无任务：1
                            Log.e(TAG, "任务----");
                            managerClass.getSureCancel().remove3();
                        } else if (netresultClean.equals("0")) {
                            Log.e(TAG, "跳转任务----");
                            if(managerClass.getRuning()!=null){
                                managerClass.getRuning().remove();
                            }


                            handler.sendEmptyMessage(17);

                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e("", "**===" + e);

//                    managerClass.getSureCancel().makeSuerCancel(
//                            YayunLoginSAcitivity.this, "网络请求失败",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
//                                            "连接超时");
//                                    managerClass.getRuning().remove();
//                                }
//                            }, false);
                    handler.sendEmptyMessage(19);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("TAG", "**===" + e);
                    selectTast = false;
//                    managerClass = new ManagerClass();
//                    managerClass.getSureCancel().makeSuerCancel(
//                            YayunLoginSAcitivity.this, "服务器没数据",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
//                                            "网络请求失败");
//                                    managerClass.getRuning().remove();
//                                }
//
//
//                            }, false);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "***===" + e);
//                    managerClass = new ManagerClass();
//                    managerClass.getSureCancel().makeSuerCancel(
//                            YayunLoginSAcitivity.this, "网络请求失败",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    managerClass.getRuning().runding(YayunLoginSAcitivity.this,
//                                            "网络请求失败");
//                                    managerClass.getRuning().remove();
//                                }
//                            }, false);
                    handler.sendEmptyMessage(18);

                }
            }

        }.start();
        return selectTast;
    }


    /***
     * 押运员有任务了显示所选线路
     */
    private String linname = "";
    private List<YayunSelectRenwuUserDispatchList> disoatchlist = new ArrayList<YayunSelectRenwuUserDispatchList>();// 派工单集合
    private List<YayunyuanSelectRenWuUserCodeEntity> yyysrwycodelist = new ArrayList<YayunyuanSelectRenWuUserCodeEntity>();// 更改数据结构后的外层list
    private List<YayunSelectRewuUserEntity> YayunSelectRewuUserList = new ArrayList<YayunSelectRewuUserEntity>();
    private List<YaYunLb> list = new ArrayList<YaYunLb>();
    private List<YayunyuanSelectRenWuUserBaseEntity> yyysrwybaselist = new ArrayList<YayunyuanSelectRenWuUserBaseEntity>();// 更改数据结构后的外层list
    String linename = "";

    private void GetHadTask() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.e(TAG, "----LoadDataNow" + userid);
//                    // 用户账号
//                    String userZhanghu = GApplication.use.getUserzhanghu();
                    ;
                    String netresultClean = new AccountAndResistCollateralService()
                            .queryJobOrderDetailHandleStatusByEscort(userid);// 返回数据源是msg+params
                    // 需要分开进行解决
                    // netresultClean测试数据源
                    // [{"linename":"北辰支行中心库一线","flag":"0","corpid":"904050100","state":"01","corpname":"小淀支行","linenum":"BC01"}],
                    // {"sj":"BC0120191218174053"}

                    // 思路规则 同一条线路下可以有上缴和请领两个派工单 也可以只有一个上缴或者是请领 2 可以给你每个网点下都加入派工单
                    // 无论上缴还是请领
                    Log.e(TAG, "返回=============netresultClean=====" + netresultClean);
                    if (null == netresultClean) {

                        handler.sendEmptyMessage(52);
                    } else if (netresultClean.equals("该押运员没有正在执行的派工单任务")) {
                        handler.sendEmptyMessage(53);
                        /// 这里是跳转领取任务页面还是在当前页面不显示数据 只有页面
                    } else {
                        String strnew = netresultClean.substring(0, netresultClean.indexOf("*"));
                        String Dispatch = netresultClean.substring(strnew.length() + 1, netresultClean.length());
                        Log.e(TAG, "返回==================" + Dispatch);
                        Log.e(TAG, "返回==================" + strnew);
                        Gson gson = new Gson();
                        // 解析派工单
                        disoatchlist.clear();
                        yyysrwycodelist.clear();
                        YayunSelectRenwuUserDispatchList jrb = gson.fromJson(Dispatch,
                                YayunSelectRenwuUserDispatchList.class);
                        Log.e(TAG, "获取派工单集合=====!!!!!" + jrb.getQl() + "======" + jrb.getSj());
                        YayunSelectRenwuUserDispatchList yyselectdispath = new YayunSelectRenwuUserDispatchList();
                        yyselectdispath.setQl(jrb.getQl());
                        yyselectdispath.setSj(jrb.getSj());
                        disoatchlist.add(yyselectdispath);

                        for (int i = 0; i < disoatchlist.size(); i++) {
                            Log.e(TAG, "第一次获取派工单集合的请领和上缴=====!!!!!" + jrb.getQl() + "======" + jrb.getSj());
                            Log.e(TAG, "第一次获取派工单集合的请领和上缴=====!!!!!" + disoatchlist.get(i).getQl() + "======"
                                    + disoatchlist.get(i).getSj());
                        }
                        // 为每条线路下的 =DispatchList.get(i)= 派工单

                        if (!strnew.equals("") || strnew != null || !strnew.equals("anyType{}")) {
                            // 数据返回
                            Log.e(TAG, "netresultClean测试数据源" + strnew.toString());
                            YayunSelectRewuUserList.clear();
                            linname = "";
                            list.clear();// 每次进入需要清除数据
                            Type type = new TypeToken<ArrayList<YayunyuanSelectRenWuUserCodeEntity>>() {
                            }.getType();

                            List<YayunyuanSelectRenWuUserCodeEntity> list = gson.fromJson(strnew, type);

                            yyysrwycodelist = list;

                            // 打印
                            Log.e(TAG, "查看长度派工单---" + disoatchlist.size());
                            yyysrwybaselist.clear();
                            List<YayunyuanSelectRenWuUserBaseEntity> baselist2 = new ArrayList<YayunyuanSelectRenWuUserBaseEntity>();
                            for (YayunyuanSelectRenWuUserCodeEntity codeEntity : yyysrwycodelist) {
                                // 拿到base集合
                                List<YayunyuanSelectRenWuUserBaseEntity> baselist1 = codeEntity.getData();
                                for (YayunyuanSelectRenWuUserBaseEntity entity : baselist1) {
                                    entity.setLinename(codeEntity.getLinename());
                                    entity.setLinenum(codeEntity.getLinenum());
                                    // 当前农行的业务逻辑不可删除
                                    // 派工单说明： 1两种情况一种派工单实体的集合长度为2 线路1 下所有网点是sj
                                    // 线路2下所有网点为ql总是互斥
                                    // 2线路只有一条 派工单实体集合为长度为1 线路1 下网点1 有上缴没请领，网点2
                                    // 只有请领请领没上交网点3既有上缴也有请领 （网点1和网点3的情况共用一个上缴派工单）
                                    // 3 上缴05到01 状态是由pc端操作完成pda无法控制
                                    // 4 请领有任务时肯定有派工单 上缴一个列表只有一个是不为00
                                    // 状态那么就有派工单其它状态均有派工单
                                    baselist2.add(entity);
                                }
                            }

                            for (int i = 0; i < baselist2.size(); i++) {
                                Log.e(TAG, baselist2.size() + "=====baselist2长度");
                                Log.e(TAG, baselist2.get(i) + "=====baselist2长度");
                            }
                            for (YayunyuanSelectRenWuUserBaseEntity baseentity : baselist2) {
                                // 上缴：00待确认 01待交接 02/04已交接 /// 线路和派工单两条
                                if (linename == null || linename.equals("")) {
                                    linename = baseentity.getLinename();// / 线路1
                                } else {
                                    if (linename.equals(baseentity.getLinename())) {
                                        Log.e(TAG, "linenname" + linename);
                                    } else {
                                        // linename=baseentity.getLinename();// 长度为2
                                        // 的情况//// 线路2

                                    }
                                }
                                handler.sendEmptyMessage(15);
                                if (null != disoatchlist && yyysrwycodelist.size() > 1 && disoatchlist.size() > 1) {

                                    if (linename != null || !linename.equals("")) {
                                        if (linename.equals(baseentity.getLinename())) {// 长度是一
                                            if ((!baseentity.getSjstate().equals("00"))
                                                    || baseentity.getSjstate().equals("01")
                                                    || baseentity.getSjstate().equals("02")
                                                    || baseentity.getSjstate().equals("04")) {

                                                for (int i = 0; i < disoatchlist.size(); i++) {
                                                    baseentity.setSj(disoatchlist.get(i).getSj());
                                                }
                                            } else if (baseentity.getSjstate().equals("00")) {

                                                Log.e(TAG, "当前没有派工单或者没生成");
                                            }

                                            // 请领： 05待交接 00/04已交接
                                            if (baseentity.getQlstate().equals("05")
                                                    || baseentity.getQlstate().equals("00")
                                                    || baseentity.getQlstate().equals("04")) {
                                                for (int i = 0; i < disoatchlist.size(); i++) {
                                                    baseentity.setQl(disoatchlist.get(i).getQl());
                                                }
                                            }
                                        } else {// / 两个线路名不相等
                                            if ((!baseentity.getSjstate().equals("00"))
                                                    || baseentity.getSjstate().equals("01")
                                                    || baseentity.getSjstate().equals("02")
                                                    || baseentity.getSjstate().equals("04")) {
                                                for (int i = 0; i < disoatchlist.size(); i++) {
                                                    baseentity.setSj(disoatchlist.get(i).getSj());
                                                }
                                            } else if (baseentity.getSjstate().equals("00")) {

                                                Log.e(TAG, "当前没有派工单或者没生成");
                                            }

                                            // 请领： 05待交接 00/04已交接
                                            if (baseentity.getQlstate().equals("05")
                                                    || baseentity.getQlstate().equals("00")
                                                    || baseentity.getQlstate().equals("04")) {
                                                for (int i = 0; i < disoatchlist.size(); i++) {
                                                    baseentity.setQl(disoatchlist.get(i).getQl());
                                                }
                                            }
                                        }
                                    }
                                    // 当前只有一条线路和一条派工单的实体
                                } else if (disoatchlist.size() == 1 && yyysrwycodelist.size() == 1
                                        && null != disoatchlist) {
                                    // for (int i = 0; i < disoatchlist.size(); i++)
                                    // {
                                    // 请领： 05待交接 00/04已交接
                                    if (null != baseentity.getQlstate()) {
                                        if (baseentity.getQlstate().equals("05") || baseentity.getQlstate().equals("00")
                                                || baseentity.getQlstate().equals("04")) {
                                            for (int i = 0; i < disoatchlist.size(); i++) {
                                                baseentity.setQl(disoatchlist.get(i).getQl());
                                                Log.e(TAG, "1111111111111！！！！！！！！" + disoatchlist.get(i).getQl());
                                            }
                                        } else {
                                            Log.e(TAG, ".getQlstate()" + "不符合派工单的条件");
                                        }

                                    } else {
                                        Log.e(TAG, ".getQlstate()" + "没有派工单");
                                    }
                                    // sj
                                    if (null != baseentity.getSjstate()) {
                                        if ((!baseentity.getSjstate().equals("00"))
                                                || baseentity.getSjstate().equals("01")
                                                || baseentity.getSjstate().equals("02")
                                                || baseentity.getSjstate().equals("04")) {
                                            for (int i = 0; i < disoatchlist.size(); i++) {
                                                baseentity.setSj(disoatchlist.get(i).getSj());
                                                Log.e(TAG, "1111111111111！！！！！！！！" + disoatchlist.get(i).getSj());
                                            }
                                        } else if (baseentity.getSjstate().equals("00")) {

                                            Log.e(TAG, "当前没有派工单或者没生成");
                                        }
                                    }
                                }
                                yyysrwybaselist.add(baseentity);

                            }

                            // disoatchlist
                            for (YayunSelectRenwuUserDispatchList strentity01 : disoatchlist) {
                                Log.e(TAG, "请领" + strentity01.getQl());
                                Log.e(TAG, "上缴" + strentity01.getSj());

                            }

                            for (YayunyuanSelectRenWuUserBaseEntity yayunyuan : yyysrwybaselist) {
                                Log.i(TAG, "派工单ql" + yayunyuan.getQl());
                                Log.i(TAG, "派工单sj----a--" + yayunyuan.getSj());
                                Log.i(TAG, "派工单sj----b--" + yayunyuan.getCorpname());
                                Log.i(TAG, "派工单sj----c-" + "：：：：：：：" + yyysrwybaselist.size());
                            }

                            if (null != yyysrwybaselist && yyysrwybaselist.size() > 0) {
                                Log.e(TAG, "yyysrwybaselist----" + yyysrwybaselist.size());
                            } else {
                                Log.e(TAG, "网路请求失败了");

                                // handler.sendEmptyMessage(3);
                            }

                        } else {
                            Log.e(TAG, "网路请无数据;");
                            handler.sendEmptyMessage(52);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "***===" + e);
                    handler.sendEmptyMessage(51);
                }
            }

        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "押运员登录");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        managerClassbutn = new ManagerClass();
        managerClass.getRuning().remove();
        managerClass.getSureCancel().remove4();
        managerClassbutn.getSureCancel().remove4();

        managerClassbutn.getSureCancel().remove3();
        managerClass.getSureCancel().remove3();

    }
}
