package com.ljsw.tjbankpda.main;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.poka.util.ShareUtil;
import cn.poka.util.SharedPreUtil;
import hdjc.rfid.operator.RFID_Device;

import com.example.pda.R;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.entity.Qingfenxianjin;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.Box;
import com.ljsw.tjbankpda.qf.entity.CollateralTurnOver;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.qf.entity.ZhuanxiangTongji;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.NumFormat;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

/**
 * 请领装入量获取
 *
 * @author yuyunheng
 */
public class QinglingZhuangxiangInfoActivity extends FragmentActivity {
    protected static final String TAG = "QinglingZhuangxiangInfoActivity";
    /* 定义控件 */
    private LinearLayout llXianjin;
    private LinearLayout llZhongkong;
    private LinearLayout ll_collateralInfo;// 抵制押品
    private Button btnZhuangxiang;
    private TextView tvTitle;
    private TextView tvDiZhiCount;// 抵质押品数量
    private TextView tvXianjinCount;// 现金数量
    private TextView tvZhongkongCount;// 重控数量
    private ImageView ivBack;
    private Context mContext;
    /* 定义全局变量 */
    private List<Qingfenxianjin> ltXianjin = new ArrayList<Qingfenxianjin>();// 现金集合网络请求
    private List<Box> ltZhongkong = new ArrayList<Box>(); // 中控的数量
    private List<CollateralTurnOver> ltResistcollateral = new ArrayList<CollateralTurnOver>();// 抵制押品装
    private List<String> listResisicollrallist = new ArrayList<String>();// 需要扫描的号
    private String diZhiCount;// 抵质押品数量
    private String diZhibianhao;// 抵质押品编号
    private int totalXianjin;// 现金总数量
    private int totalZhongkong;// 重空凭证总数量
    private int biaoqian;// 标签,0为装箱领款,1为请领装箱明细.
    private Bundle bundle;
    private String orderNum;// 请领任务订单号
    private RFID_Device rfid;

    private RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    private ManagerClass manager;// 弹出框
    private Handler okHandle = new Handler() {// 数据获取成功handler
        public void handleMessage(Message msg) {
            for (int i = 0; i < ltXianjin.size(); i++) {
                View v = LayoutInflater.from(QinglingZhuangxiangInfoActivity.this)
                        .inflate(R.layout.item_qingfenrenwu_qinglingxiangqing, null);
                TextView tvType = (TextView) v.findViewById(R.id.tv_item_qinglingmingzi_type);
                TextView tvCount = (TextView) v.findViewById(R.id.tv_item_qinglingmingzi_count);
                tvType.setText(ltXianjin.get(i).getQuanbie());
                String count = ltXianjin.get(i).getShuliang() + "";
                tvCount.setText(count);
                llXianjin.addView(v);
                btnZhuangxiang.setEnabled(true);
                btnZhuangxiang.setBackgroundResource(R.drawable.buttom_selector_bg);
                mContext = QinglingZhuangxiangInfoActivity.this;
            }
            for (int i = 0; i < ltZhongkong.size(); i++) {
                View v = LayoutInflater.from(QinglingZhuangxiangInfoActivity.this)
                        .inflate(R.layout.item_qingfenrenwu_qinglingxiangqing, null);
                TextView tvType = (TextView) v.findViewById(R.id.tv_item_qinglingmingzi_type);
                TextView tvCount = (TextView) v.findViewById(R.id.tv_item_qinglingmingzi_count);
                tvType.setText(ltZhongkong.get(i).getType());
                tvCount.setText("0" + ltZhongkong.get(i).getCount());
                llZhongkong.addView(v);
                btnZhuangxiang.setEnabled(true);
                btnZhuangxiang.setBackgroundResource(R.drawable.buttom_selector_bg);
            }
//			抵制押品获取数据后并显示
            for (int j = 0; j < ltResistcollateral.size(); j++) {
                View v = LayoutInflater.from(QinglingZhuangxiangInfoActivity.this)
                        .inflate(R.layout.item_qingfenrenwu_qinglingxiangqingchangetext, null);
                TextView tvCount = (TextView) v.findViewById(R.id.tv_item_qinglingmingzi_counttext);
                tvCount.setText(ltResistcollateral.get(j).getDizhibianhao());
                ll_collateralInfo.addView(v);
                btnZhuangxiang.setEnabled(true);
                btnZhuangxiang.setBackgroundResource(R.drawable.buttom_selector_bg);
            }

            tvDiZhiCount.setText(diZhiCount);
            tvXianjinCount.setText(new NumFormat().format(totalXianjin + ""));// 转换功能
            tvZhongkongCount.setText(totalZhongkong + "");

            manager.getRuning().remove();
        }

        ;
    };
    private Handler timeoutHandle = new Handler() {// 连接超时handler
        public void handleMessage(Message msg) {
            manager.getRuning().remove();
            if (msg.what == 0) {
                manager.getAbnormal().timeout(QinglingZhuangxiangInfoActivity.this, "数据连接超时", new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (biaoqian == 1) {
                            getDate();
                        }
                        manager.getAbnormal().remove();
                    }
                });
            }
            if (msg.what == 1) {
                manager.getAbnormal().timeout(QinglingZhuangxiangInfoActivity.this, "网络连接失败", new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        manager.getAbnormal().remove();
                    }
                });
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qingfengrenwu_qinfenxiangqing);

        llXianjin = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_qinglingxiangqing_xianjinInfo);
        llZhongkong = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_qinglingxiangqing_zhongkongInfo);
        ll_collateralInfo = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_qinglingxiangqing_collateralInfo1);
        btnZhuangxiang = (Button) findViewById(R.id.btn_qingfengrenwu_qinglingxiangqing_zhuangxiang);
        btnZhuangxiang.setEnabled(false);
        btnZhuangxiang.setBackgroundResource(R.drawable.button_gray);
        tvTitle = (TextView) findViewById(R.id.tv_qingfengrenwu_qinglingxiangqing_title);
        tvDiZhiCount = (TextView) findViewById(R.id.tv_qingfengrenwu_qinglingxiangqing_sizhi_total);
        tvXianjinCount = (TextView) findViewById(R.id.tv_qingfengrenwu_qinglingxiangqing_xianjing_total);
        tvZhongkongCount = (TextView) findViewById(R.id.tv_qingfengrenwu_qinglingxiangqing_zhongkong_total);
        ivBack = (ImageView) findViewById(R.id.iv_qingfenrenwu_qinglingxiangqing_back);
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                QinglingZhuangxiangInfoActivity.this.finish();
            }
        });
        bundle = super.getIntent().getExtras(); // 任务单号 和 任务种类条目 现金 ，中控，抵制押品
        biaoqian = bundle.getInt("biaoqian", -1);
        orderNum = bundle.getString("qlNum");// orderNum RW1QF2020190114155253
//		201902283020120001,201902283020110001,
        System.out.println("orderNum===:" + orderNum);

        manager = new ManagerClass();
        manager.getRuning().runding(this, "数据加载中...");

        if (biaoqian == 1) {
            getDate(); // 装入量获取 网络请求方法 无论上个页面传什么

            btnZhuangxiang.setOnClickListener(new OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View arg0) {
                    bundle.putString("zongMoney", totalXianjin + ""); // 将现金总数传入bundle
                    bundle.putString("zongZhongkong", totalZhongkong + ""); // 将重空总数传入bundle
                    bundle.putString("zongDizhi", diZhiCount + ""); // 将抵质押品总数传入bundle
                    // 抵制押品 数据放入集合实现扫描20190114
                    o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
                    o_Application.qlruku = null;// 先清空在存放

                    if (ltResistcollateral.size() > 0) {
//						for (int i = 0; i < ltResistcollateral.size(); i++) {
                        // 向集合加入任务号用于扫描
//							Log.e(TAG + "", "！！！AAAA！" + ltResistcollateral.get(i));

                        o_Application.qinglingruku.add(new QingLingRuKu(null, null, listResisicollrallist));
//						}
                        // 存入
                        o_Application.qlruku = o_Application.qinglingruku.get(0);// 添加数据源
                        Log.e(TAG + "", o_Application.qlruku.getZhouzhuanxiang().size()
                                + "===o_Application.qlruku.getZhouzhuanxiang()!!!!");
                        Log.e(TAG + "", Mapplication.getApplication().boxLtDizhi.size()
                                + "====Mapplication.getApplication().boxLtDizhi!!!!");
                    }
                    manager.getRuning().runding(QinglingZhuangxiangInfoActivity.this, "加载中...");
                    if (o_Application.qlruku == null) {
                        Log.d(TAG, "数据为null");
//							更改跳转
                        Skip.skip(QinglingZhuangxiangInfoActivity.this, QinglingZhuangxiangActivity.class, bundle, 0);
                    } else {
                        Log.e(TAG + "长度:", Mapplication.getApplication().boxLtDizhi.size() + "===="
                                + o_Application.qlruku.getZhouzhuanxiang().size());


                        if (o_Application.qlruku.getZhouzhuanxiang().size() != ltResistcollateral.size()) {
                            Log.e(TAG, Mapplication.getApplication().boxremberDizhi1.size() + "！！！");
                            Log.e(TAG, o_Application.qlruku.getZhouzhuanxiang().size() + "！！！");



                            for (int j = 0; j < Mapplication.getApplication().boxremberDizhi1.size(); j++) {
                                for (int i = 0; i < o_Application.qlruku.getZhouzhuanxiang().size(); i++) {
                                    if ((Mapplication.getApplication().boxremberDizhi1.get(j)
                                            .equals(o_Application.qlruku.getZhouzhuanxiang().get(i)))) { // 防止重复

//										Log.e(TAG + "getZhouzhuanxiang().size()!!!!",
//												o_Application.qlruku.getZhouzhuanxiang().size() + "");
                                        continue;
                                    } else {
                                        if (null != Mapplication.getApplication().boxremberDizhi1) {
//                                            for (int x = 0; x < Mapplication.getApplication().boxremberDizhi1.size(); x++) {
//                                                System.out.print("!!!!" + Mapplication.getApplication().boxremberDizhi1.get(x));
//                                            }
//                                            System.out.print("!!!!" + Mapplication.getApplication().boxremberDizhi1.size());
                                        }
                                        if (o_Application.qlruku.getZhouzhuanxiang().contains(Mapplication.getApplication().boxremberDizhi1.get(j))) {

                                        } else {
                                            o_Application.qlruku.getZhouzhuanxiang()
                                                    .add(Mapplication.getApplication().boxremberDizhi1.get(j));// 添加数据源

                                        }

                                        Log.e(TAG + "getZhouzhuanxiang().size()DDDDD",
                                                o_Application.qlruku.getZhouzhuanxiang().size() + "DDD");
                                    }
                                }
                            }
                        }
                        /// 全部扫完重新加入
                        if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
                            if (Mapplication.getApplication().boxLtDizhi.size() > 0) {
                                o_Application.qlruku.getZhouzhuanxiang()
                                        .addAll(Mapplication.getApplication().boxLtDizhi);
                            }
                        }
                        // 清除两个集合
                        Mapplication.getApplication().boxremberDizhi.clear();
                        Mapplication.getApplication().boxremberNumberDizhi.clear();
                        Mapplication.getApplication().boxremberDizhi1.clear();
                    }

//                    Log.e(TAG,"打印时间"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() ));
                    Skip.skip(QinglingZhuangxiangInfoActivity.this, QinglingZhuangxiangActivity.class, bundle, 0);
//				                             装箱页面

                }
            });

        } else if (biaoqian == 0) {
            if (o_Application.qinglingruku != null) {
                o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复lc
            }
            System.out.println("装箱领款");
            btnZhuangxiang.setVisibility(View.GONE);
            tvTitle.setText("装箱领款");
            getTotalData(); // 装箱领款
        }
    }

    /**
     * 抵制押品返回后无数据 activity每次从新走后需要走的方法
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (o_Application.qinglingruku.size() > 0) {
            o_Application.qinglingruku.clear();
        }
        if (o_Application.numberlist.size() > 0) {
            o_Application.numberlist.clear();
//			添加修改
        }
        getRfid().scanclose();

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.getRuning().remove();
    }

    /**
     * 根据订单号获取数据 (装入量获取)
     */
    private void getDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String params;
                try {
                    // 初始化全局变量
                    Mapplication.getApplication().IsDizhiOK = false;
                    Mapplication.getApplication().IsXianjingOK = false;
                    Mapplication.getApplication().IsZhongkongOK = false;
                    Mapplication.getApplication().zxLtXianjing.clear();
                    Mapplication.getApplication().zxLtZhongkong.clear();
                    Mapplication.getApplication().zxTjDizhi = null;// 对象
                    Mapplication.getApplication().boxLtXianjing.clear();
                    Mapplication.getApplication().boxLtZhongkong.clear();
                    Mapplication.getApplication().boxLtDizhi.clear();
                    ltXianjin.clear();
                    ltZhongkong.clear();
                    ltResistcollateral.clear(); // lianchao
                    // 连接服务器 通过编号判断执行那个网络请求
                    if (biaoqian == 0) {
                        params = new QingfenRenwuService().getReceive(orderNum);// 网络接口

//					params = new QingfenRenwuService().getParams(orderNum, "getloadNum");
                        System.out.println("装入量返回信息：" + params);
                        Table[] RenwuData = Table.doParse(params);
                        List<String> ltx = RenwuData[0].get("shuliang").getValues();
                        if (ltx.size() != 0) {
                            for (int i = 0; i < ltx.size(); i++) {
                                ltXianjin.add(new Qingfenxianjin(RenwuData[0].get("quanbie").get(i),
                                        RenwuData[0].get("shuliang").get(i), RenwuData[0].get("quanbieId").get(i),
                                        RenwuData[0].get("quanJiazhi").get(i)));
                            }
                        }
                        List<String> ltz = RenwuData[1].get("shuliang").getValues();
                        if (ltz.size() != 0) {
                            for (int i = 0; i < ltz.size(); i++) {
                                ltZhongkong.add(new Box(RenwuData[1].get("zhongkongtype").get(i),
                                        RenwuData[1].get("shuliang").get(i)));
                            }
                        }
                        /// lian 抵质押品数量解析
                        List<String> ltg = RenwuData[2].get("dizhibianhao").getValues();
                        listResisicollrallist.clear();
                        if (ltg.size() != 0) {
                            for (int i = 0; i < RenwuData.length; i++) {
                                ltResistcollateral.add(new CollateralTurnOver(RenwuData[2].get("dizhibianhao").get(i)));
//						存放数据源到集合中
                                listResisicollrallist.add(ltResistcollateral.get(i).getDizhibianhao());

                            }
                        }
                        for (String str : ltz) {
                            totalZhongkong += Integer.parseInt(str);
                        }
                        totalXianjin = 0; // 重置现金总数
                        for (Qingfenxianjin xianjin : ltXianjin) {
                            // 取出现金价值
                            double jiazhi = Double.parseDouble(xianjin.getQuanJiazhi().trim());
                            // 取出现金数量
                            int num = Integer.parseInt(xianjin.getShuliang().trim());

                            totalXianjin += jiazhi * num;
                        }
                        diZhiCount = RenwuData[2].get("dizhinum").get(0);
                        // 将获取到的数据填充到Mapplication里
                        Mapplication.getApplication().xjType = new String[ltXianjin.size()];
                        Mapplication.getApplication().zkType = new String[ltZhongkong.size()];

                        for (int i = 0; i < ltXianjin.size(); i++) {
                            Mapplication.getApplication().zxLtXianjing
                                    .add(new ZhuanxiangTongji(ltXianjin.get(i).getQuanbie(), 0,
                                            Integer.parseInt(ltXianjin.get(i).getShuliang())));
                            Mapplication.getApplication().xjType[i] = ltXianjin.get(i).getQuanbie();
                        }
                        for (int j = 0; j < ltZhongkong.size(); j++) {
                            Mapplication.getApplication().zxLtZhongkong.add(new ZhuanxiangTongji(
                                    ltZhongkong.get(j).getType(), 0, Integer.parseInt(ltZhongkong.get(j).getCount())));
                            Mapplication.getApplication().zkType[j] = ltZhongkong.get(j).getType();
                        }
                        Mapplication.getApplication().zxTjDizhi = new ZhuanxiangTongji("抵质押品", 0,
                                Integer.parseInt(diZhiCount));
                        Mapplication.getApplication().ltDizhiNum.clear();
                        Mapplication.getApplication().ltZzxNumber.clear();
                        okHandle.sendEmptyMessage(0);
                    } else { // 两个参数设置 bianhao==1 条目点击时走的方法
                        params = new QingfenRenwuService().getParamsbynum(orderNum, "getloadNum");
//						shuliang:2000|quanJiazhi:10.00|quanbieId:0010A|quanbie:十元券别
//						shuliang:1100,100|zhongkongtype:汇票申请,农商行转账支票
//						dizhibianhao:|dizhinum:0
                        System.out.println("装入量返回信息：" + params);
                        Table[] RenwuData = Table.doParse(params);
                        List<String> ltx = RenwuData[0].get("shuliang").getValues();
                        if (ltx.size() != 0) {
                            for (int i = 0; i < ltx.size(); i++) {
                                ltXianjin.add(new Qingfenxianjin(RenwuData[0].get("quanbie").get(i),
                                        RenwuData[0].get("shuliang").get(i), RenwuData[0].get("quanbieId").get(i),
                                        RenwuData[0].get("quanJiazhi").get(i)));
                            }
                        }
                        List<String> ltz = RenwuData[1].get("shuliang").getValues();
                        if (ltz.size() != 0) {
                            for (int i = 0; i < ltz.size(); i++) {
                                ltZhongkong.add(new Box(RenwuData[1].get("zhongkongtype").get(i),
                                        RenwuData[1].get("shuliang").get(i)));
                            }
                        }
                        /// lian 抵制押品数量解析
                        listResisicollrallist.clear();
                        List<String> ltgcollateral = RenwuData[2].get("dizhibianhao").getValues();
                        if (ltgcollateral.size() != 0) {
                            for (int i = 0; i < ltgcollateral.size(); i++) {
                                ltResistcollateral.add(new CollateralTurnOver(RenwuData[2].get("dizhibianhao").get(i)));
                                listResisicollrallist.add(ltResistcollateral.get(i).getDizhibianhao());
                            }

                        }

                        for (String str : ltz) {
                            totalZhongkong += Integer.parseInt(str);
                        }
                        totalXianjin = 0; // 重置现金总数
                        for (Qingfenxianjin xianjin : ltXianjin) {
                            // 取出现金价值
                            double jiazhi = Double.parseDouble(xianjin.getQuanJiazhi().trim());
                            // 取出现金数量
                            int num = Integer.parseInt(xianjin.getShuliang().trim());

                            totalXianjin += jiazhi * num;
                        }
                        diZhiCount = RenwuData[2].get("dizhinum").get(0);
                        // 将获取到的数据填充到Mapplication里
                        Mapplication.getApplication().xjType = new String[ltXianjin.size()];
                        Mapplication.getApplication().zkType = new String[ltZhongkong.size()];
                        Mapplication.getApplication().dzType = new String[ltResistcollateral.size()];

                        for (int i = 0; i < ltXianjin.size(); i++) {
                            Mapplication.getApplication().zxLtXianjing
                                    .add(new ZhuanxiangTongji(ltXianjin.get(i).getQuanbie(), 0,
                                            Integer.parseInt(ltXianjin.get(i).getShuliang())));
                            Mapplication.getApplication().xjType[i] = ltXianjin.get(i).getQuanbie();
                        }
                        for (int j = 0; j < ltZhongkong.size(); j++) {
                            Mapplication.getApplication().zxLtZhongkong.add(new ZhuanxiangTongji(
                                    ltZhongkong.get(j).getType(), 0, Integer.parseInt(ltZhongkong.get(j).getCount())));
                            Mapplication.getApplication().zkType[j] = ltZhongkong.get(j).getType();
                        }
                        Mapplication.getApplication().zxTjDizhi = new ZhuanxiangTongji("抵质押品", 0,
                                Integer.parseInt(diZhiCount));
                        Mapplication.getApplication().ltDizhiNum.clear();
                        Mapplication.getApplication().ltZzxNumber.clear();
                        okHandle.sendEmptyMessage(0);

                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    timeoutHandle.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    timeoutHandle.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    /**
     * biaoqian==0 获得所有订单的数据 (装箱领款) 只是显示不添加数据到集合进行扫描
     * <p>
     * shuliang:|quanJiazhi:|quanbieId:|quanbie: shuliang:|zhongkongtype:
     */
    private void getTotalData() {
        new Thread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                String params;
                o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
                try {
                    Log.d(TAG, "orderNum===" + orderNum);
//					params = new QingfenRenwuService().getParams(orderNum, "getReceive");
                    params = new QingfenRenwuService().getReceive(orderNum + "");

                    System.out.println("装箱领款返回信息：" + params);
                    Table[] RenwuData = Table.doParse(params);
                    List<String> ltx = RenwuData[0].get("shuliang").getValues();
                    for (int i = 0; i < ltx.size(); i++) {
                        ltXianjin.add(new Qingfenxianjin(RenwuData[0].get("quanbie").get(i),
                                RenwuData[0].get("shuliang").get(i), RenwuData[0].get("quanbieId").get(i),
                                RenwuData[0].get("quanJiazhi").get(i)));

                    }
                    List<String> ltz = RenwuData[1].get("shuliang").getValues();
                    if (ltz.size() != 0) {
                        for (int i = 0; i < ltz.size(); i++) {
                            ltZhongkong.add(new Box(RenwuData[1].get("zhongkongtype").get(i),
                                    RenwuData[1].get("shuliang").get(i)));
                        }
                    }

                    for (String str : ltz) {
                        totalZhongkong += Integer.parseInt(str);
                    }
//					lchao添加中地质押品
                    List<String> ldizhi = RenwuData[2].get("dizhibianhao").getValues();
                    if (ltz.size() != 0) {
                        for (int i = 0; i < ldizhi.size(); i++) {
                            ltResistcollateral.add(new CollateralTurnOver(RenwuData[2].get("dizhibianhao").get(i)));
                            listResisicollrallist.add(ltResistcollateral.get(i).getDizhibianhao());
                        }
                    }
                    totalXianjin = 0; // 重置现金总数
                    for (Qingfenxianjin xianjin : ltXianjin) {
                        // 取出现金价值
                        double jiazhi = Double.parseDouble(xianjin.getQuanJiazhi().trim());
                        // 取出现金数量
                        int num = Integer.parseInt(xianjin.getShuliang().trim());
                        totalXianjin += jiazhi * num;
                    }

                    diZhiCount = RenwuData[2].get("dizhinum").get(0);
                    okHandle.sendEmptyMessage(0);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    timeoutHandle.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    timeoutHandle.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (o_Application.qinglingruku.size() > 0) {
                o_Application.qinglingruku.clear();
            }
            QinglingZhuangxiangInfoActivity.this.finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (o_Application.qinglingruku.size() > 0) {
            o_Application.qinglingruku.clear();
            o_Application.numberlist.clear();
        }

    }

}
