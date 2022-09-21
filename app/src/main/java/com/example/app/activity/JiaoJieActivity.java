package com.example.app.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.app.entity.KuanXiangBox;
import com.example.app.entity.KuanXiangChuRu;
import com.example.app.entity.S_box;
import com.example.app.util.Skip;
import com.example.pda.R;
import com.manager.classs.pad.ManagerClass;
import com.o.service.KuanxiangjiaojieService;

/**
 * 款箱交接列表显示
 *
 * @author Administrator
 */
public class JiaoJieActivity extends Activity {
    private ListView listWanshou; // 晚收
    private ListView listZaosong; // 早送
    private ImageView replace; // 刷新
    private LinearLayout layoutWanshou;
    private LinearLayout layoutZaosong;
    private TextView luxianName, zwangdianNum, zkuanxiangNum, wwangdianNum, wkuanxiangNum;
    private String state;// 早送状态 0 不需要 1 需要
    int zwcounnt = 0;
    int zkcount = 0; // 早款箱数
    int wkcount = 0; // 晚款箱数
    //	int zwangdian =0;//早网点数
//	int wwangdian =0;//晚网点数
    KuanxiangjiaojieService ks = new KuanxiangjiaojieService();
    private ManagerClass manager;
    // 获取早送申请列表
    List<S_box> slist = new ArrayList<S_box>();
    // 创建早出信息集合
    private List<KuanXiangBox> zlist = new ArrayList<KuanXiangBox>();
    // 创建晚入信息集合
    private List<KuanXiangBox> wlist = new ArrayList<KuanXiangBox>();
    private zaosongAdapter zaoAdapter;
    private wanshouAdapter wanAdapter;
    String netPoint = "";
    String netPointUser = "";
    String cardDrvier = "";
    Intent intentA;
    public static final int REQUEST_CODE_B = 0;
    public final int REQUEST_CODE_C = 2;
    private String Bindfailshow;//  绑定失败
    private long lastClickTime = 0;/// 限制点击的时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jiaojie);
        // 禁止休睡眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        load();
        intentA = getIntent();
        manager = new ManagerClass();
        zaoAdapter = new zaosongAdapter();
        wanAdapter = new wanshouAdapter();
        ShijianChuli shijianchuli = new ShijianChuli();
        replace.setOnClickListener(shijianchuli);
        layoutWanshou.setOnClickListener(shijianchuli);
        listWanshou.setOnItemClickListener(shijianchuli);
        layoutZaosong.setOnClickListener(shijianchuli);
        listZaosong.setOnItemClickListener(shijianchuli);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager == null) {
            manager = new ManagerClass();
        }
        if (GApplication.boxHandoverList != null) {
            GApplication.boxHandoverList.clear();
        }
        if (zlist != null) {
            zlist.clear();
        }
        if (wlist != null) {
            wlist.clear();
        }
        zwangdianNum.setText("0");
        zkuanxiangNum.setText("0");
        wwangdianNum.setText("0");// 反了 凑合用
        wkuanxiangNum.setText("0");
        wanAdapter.notifyDataSetChanged();
        zaoAdapter.notifyDataSetChanged();
        zkcount = 0;
        wkcount = 0;

        if (null == netPoint || netPoint.equals("")) {
            System.out.println("这是是null 第一次进入?");
            // 绑定信息成功
            getLineNum();
        }
        if (null == GApplication.kxc) {
            System.out.println("走了获取线路吗?");
            getLineNum();// 获取线路
        } else {
            getInfo();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_B) {
            Log.e("onActivityResult", "" + data);
            if (null == data) {
                Log.e("", "我是空的" + data);
                Toast.makeText(JiaoJieActivity.this, "信息缺失请重新录入信息", 500).show();
            } else {
                String sumText = data.getStringExtra("netpoincollect");
                String netpointleft = data.getStringExtra("netpointleft");
                String netpointright = data.getStringExtra("netpointright");
                System.out.println("-------------" + sumText);
                System.out.println("-------------" + netpointleft);
                System.out.println("-------------" + netpointright);

                if (sumText.equals("账号验证成功")) {
                    netPoint = netpointleft;
                    netPointUser = netpointright;
                    cardDrvier = GApplication.loginname;
                    getLastReviceBind();
                }
            }
        }
    }

    /*
     * 控件初始化
     */
    private void load() {
        listWanshou = (ListView) findViewById(R.id.wanshou_listView1);
        listZaosong = (ListView) findViewById(R.id.zaochu_listView2);
        replace = (ImageView) findViewById(R.id.jiaojie_shuaxin);
        luxianName = (TextView) findViewById(R.id.luxianName);
        zwangdianNum = (TextView) findViewById(R.id.zaosongjiaojie_wangdiancount);
        zkuanxiangNum = (TextView) findViewById(R.id.zaosongjiaojie_kuanxiangcount);
        wwangdianNum = (TextView) findViewById(R.id.wanrujiaojie_wangdiancount);
        wkuanxiangNum = (TextView) findViewById(R.id.wanrujiaojie_kuanxiangcount);
        layoutWanshou = (LinearLayout) findViewById(R.id.wanshou_jiaojie);
        layoutZaosong = (LinearLayout) findViewById(R.id.zaosong_jiaojie);
    }

    /**
     * 事件处理内部类
     *
     * @author yuyunheng
     */
    private class ShijianChuli implements OnClickListener, OnItemClickListener {

        /**
         * 单机事件
         */
        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.wanshou_jiaojie:
                    listWanshou.setVisibility(View.VISIBLE);
                    listZaosong.setVisibility(View.GONE);
                    Toast.makeText(JiaoJieActivity.this, "网点人员绑定才能看到任务", 1000).show();
                    break;
                case R.id.zaosong_jiaojie:
                    listWanshou.setVisibility(View.GONE);
                    listZaosong.setVisibility(View.VISIBLE);
                    break;
                case R.id.jiaojie_shuaxin:
                    long now = System.currentTimeMillis();
                    if (now - lastClickTime > 1000) {
                        lastClickTime = now;

                    if (null == GApplication.kxc) {
                        System.out.println("走了获取线路吗?");
                        getLineNum();// 获取线路
                    } else {
                        getInfo();
                    }

                    } else {
                        Toast.makeText(JiaoJieActivity.this, "您点击的太快了请勿重复刷新", 300).show();
//                        return;

                    }
                    break;
            }
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int checkId, long arg3) {
            switch (arg0.getId()) {
                case R.id.zaochu_listView2:// 早送交接列表点击
                    manager.getRuning().runding(JiaoJieActivity.this, "数据加载中...");
                    GApplication.sk = zlist.get(checkId);

                    ZaoSongJiaojie();
                    break;
                case R.id.wanshou_listView1:// 晚收交接列表点击
                    manager.getRuning().runding(JiaoJieActivity.this, "数据加载中...");

                    GApplication.sk = wlist.get(checkId);
                    // 需呀判断是否需呀早送
                    ShiFouZaoSong();
                    break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            manager.getRuning().remove();
            switch (msg.what) {
                // 0 早送异常 重新拿数据 1 查询是否早送出错 重新跑
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "数据加载失败", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSongJiaojie();
                        }
                    });
                    break;
                case 1:
                    manager.getRuning().remove();
                    // 是否早送异步 得到状态后决定启动哪个异步 失败重头跑起
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "数据加载失败", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ShiFouZaoSong();
                        }
                    });
                    break;
                case 2:
                    if (state.equals("0")) { // 晚收列表中
                        BuZaoSong();// 不早送 跳转晚收
                    } else if (state.equals("1")) {
//                        ZaoSong();// 早送申请  2022.5.11 需求早送申请 不要了 所以当我不管你是什么值我都走不早送
                        BuZaoSong();// 不早送 跳转晚收
                    }
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "加载超时,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ShiFouZaoSong();
                        }
                    });
                    break;
                case 4:
                    manager.getRuning().remove();
                    // 有了刷新键这里暂时取消提示
                /*
                 * manager.getAbnormal().timeout(JiaoJieActivity.this, "未能获取任务,重试?", new
				 * View.OnClickListener() {
				 * 
				 * @Override public void onClick(View arg0) { manager.getAbnormal().remove();
				 * getInfo(); } });
				 */
                    break;
                case 5:
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "加载超时,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getRuning().remove();
                            manager.getAbnormal().remove();
                            getInfo();
                        }
                    });
                    break;
                case 6:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getInfo();
                        }
                    });
                    break;
                case 7:
                    Log.d("jiaojie","7是否空这判断"+GApplication.boxHandoverList);
                    if (zkcount != 0) {
                        zkcount = 0;
                    }
                    if (wkcount != 0) {
                        wkcount = 0;
                    }
                    zlist.clear();
                    wlist.clear();
                    // 以上是刷新时起作用
                    Log.d("jiaojie","7是否空这判 GApplication.kxc)断"+ GApplication.kxc);

                    if (null != GApplication.kxc) {
                        luxianName.setText(GApplication.kxc.getChaochexianlu());
                    }
                    Log.d("jiaojie","7是否空这判 GApplication.GApplication.boxHandoverList)断"+ GApplication.boxHandoverList.size());
                    if (GApplication.boxHandoverList != null&&GApplication.boxHandoverList.size()>0) {// 交接列表不为空
                        for (int i = 0; i < GApplication.boxHandoverList.size();i++) {
                            if (GApplication.boxHandoverList.get(i).getType().equals("0")) {
                                zlist.add(GApplication.boxHandoverList.get(i));// 添加早送集合
                                zkcount += Integer.parseInt(GApplication.boxHandoverList.get(i).getBoxNum());
                            } else {
                                wlist.add(GApplication.boxHandoverList.get(i));// 添加晚收集合
                                wkcount += Integer.parseInt(GApplication.boxHandoverList.get(i).getBoxNum());
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listZaosong.setAdapter(zaoAdapter);
                    wanAdapter.notifyDataSetChanged();
                    zaoAdapter.notifyDataSetChanged();
                    listWanshou.setAdapter(wanAdapter);
                    zwangdianNum.setText("" + zkcount); // 早送网点总数
                    zkuanxiangNum.setText("" + zlist.size());// 早送款箱总数
                    wwangdianNum.setText("" + wkcount);// 反了 凑合用
                    wkuanxiangNum.setText("" + wlist.size());
                    manager.getRuning().remove();
                    break;
                case 8:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "数据加载超时,重试?", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSongJiaojie();
                        }
                    });
                    break;
                case 9:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSongJiaojie();
                        }
                    });
                    break;
                case 10:
                    System.out.println("KKKK--" + 10);
                    GApplication.jiaojiestate = 1; // 早送交接跳转晚收款箱页面
                    manager.getRuning().remove();
                    Skip.skip(JiaoJieActivity.this, WanShouXiangActivity.class, null, 0);
                    break;
                case 11:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ShiFouZaoSong();
                        }
                    });
                    break;
                case 12:
                    // System.out.println("KKKK--"+ 12);
                    GApplication.jiaojiestate = 2; // 晚收交接页面中 跳转晚收页面(不早送.直接进晚收交接页面)
                    System.out.println("晚收交接页面中  跳转晚收页面:" + GApplication.jiaojiestate);
                    Skip.skip(JiaoJieActivity.this, WanShouXiangActivity.class, null, 0);
                    break;
                case 13:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "获取失败,重试?", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            BuZaoSong();
                        }
                    });
                    break;
                case 14:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "数据加载超时,重试?", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            BuZaoSong();
                        }
                    });
                    break;
                case 15:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            BuZaoSong();
                        }
                    });
                    break;
                case 16:
                    manager.getRuning().remove();
                    if (null != GApplication.kxc) {
                        luxianName.setText(GApplication.kxc.getChaochexianlu());
                    }
                    getInfo();// 展示数据
                    break;
                case 17:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "加载超时,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getLineNum();
                        }
                    });
                    break;
                case 18:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "获取失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getLineNum();
                        }
                    });
                    break;
                case 19:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getLineNum();
                        }
                    });
                    break;
                case 20:
                    GApplication.jiaojiestate = 3; // 晚收交接 中跳转早送申请页面 然后再跳晚收交接页面
                    manager.getRuning().remove();
                    Skip.skip(JiaoJieActivity.this, ZaoSongshenqingActivity.class, null, 0);
                    break;
                case 21:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "请求失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSong();
                        }
                    });
                    break;
                case 22:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "连接超时,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSong();
                        }
                    });
                    break;
                case 23:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            ZaoSong();
                        }
                    });
                    break;
            /*
             * case 24: manager.getRuning().remove(); break;
			 */
                case 25:
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "即将进入网点人员信息录入?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            Intent intent = new Intent(JiaoJieActivity.this, KuanXiangNetPointCollectActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_B);
                            manager.getRuning().runding(JiaoJieActivity.this, "即将自动跳转");

                        }
                    });

                    break;

                case 26:
                    manager.getAbnormal().timeout(JiaoJieActivity.this, Bindfailshow, new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
//                    Toast.makeText(JiaoJieActivity.this, "" + Bindfailshow, 500).show();
//                    Toast.makeText(JiaoJieActivity.this, "" , 500).show();
                    manager.getRuning().remove();
                    break;
                case 27:
                    manager.getRuning().remove();
                    getLastReviceBind();//  如果没有返回线路信息
                    break;
                case 28:
                    Toast.makeText(JiaoJieActivity.this, "网点人员信息为空请重新登录", 500).show();
                    break;
                case 29:
                    Toast.makeText(JiaoJieActivity.this, "押运员信息为空请重新登录", 500).show();
                    break;
                case 30://绑定信息成功
                    break;


                case 31:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "连接超时,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getLastReviceBind();
                        }
                    });
                    break;
                case 32:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(JiaoJieActivity.this, "网络连接失败,重试?", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                            getLastReviceBind();
                        }
                    });
                    break;
                default:
                    manager.getRuning().remove();
                    break;
            }
        }

    };

    public void ZaoSongJiaojie() {
        manager.getRuning().runding(JiaoJieActivity.this, "数据加载中...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {// 获取款箱交接明细
                    GApplication.zaolist = ks.getBoxHandoverDetail(GApplication.sk.getNetId(),
                            GApplication.sk.getType());
                    if (GApplication.zaolist != null) {
                        handler.sendEmptyMessage(10);
                    } else {
                        handler.sendEmptyMessage(8);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(8);
                } catch (Exception e) {
                    handler.sendEmptyMessage(9);
                    e.printStackTrace();
                }
            }

        }.start();
    }

    /**
     * 调用判断是否早送
     *
     * @author lenovo
     */
    public void ShiFouZaoSong() {
        manager.getRuning().runding(JiaoJieActivity.this, "数据加载中...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {// 该机构是否需要早送申请，如果需要PDA进入早送申请页面，反之进入交接页面
// 等于1  早送  否则为0
                    state = ks.getApplicationByCorpId(GApplication.sk.getNetId());
                    System.out.println("早送申请的标识:" + state);
                    if (state != null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    manager.getRuning().remove();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(3);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(11);
                }
            }

        }.start();
    }

    public void BuZaoSong() {
        manager.getRuning().runding(JiaoJieActivity.this, "数据获取中...");
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {// 获取款箱交接明细 晚收交接的
                    GApplication.zaolist = ks.getBoxHandoverDetail(GApplication.sk.getNetId(),
                            GApplication.sk.getType());
                    if (GApplication.zaolist != null) {
                        handler.sendEmptyMessage(12);
                    } else {
                        handler.sendEmptyMessage(13);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(14);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(13);
                } catch (Exception e) {
                    handler.sendEmptyMessage(15);
                }
            }

        }.start();
    }

    public void ZaoSong() {
        manager.getRuning().runding(JiaoJieActivity.this, "数据获取中...");
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {// 获取早送申请列表
                    GApplication.slist = ks.getBoxApplicationList(GApplication.sk.getNetId());
                    GApplication.jigouid = GApplication.sk.getNetId();
                    /*
                     * GApplication.zaolist = ks.getBoxHandoverDetail( GApplication.sk.getNetId(),
					 * state);
					 */
                    if (GApplication.slist != null) {

                        handler.sendEmptyMessage(20);
                    } else {
                        handler.sendEmptyMessage(21);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(22);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(21);
                } catch (Exception e) {
                    handler.sendEmptyMessage(23);
                }
            }

        }.start();
    }

    /**
     * 款箱早出数据配置
     *
     * @author yuyunheng
     */
    private class zaosongAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return zlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return zlist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            KongjianEntity entity;
            if (arg1 == null) {
                entity = new KongjianEntity();
                arg1 = LayoutInflater.from(JiaoJieActivity.this).inflate(R.layout.listview_chuangxiangchuru, null);
                entity.textView1 = (TextView) arg1.findViewById(R.id.adapter_kname);
                entity.textView2 = (TextView) arg1.findViewById(R.id.adapter_kuanxiang);
                entity.textView3 = (TextView) arg1.findViewById(R.id.adapter_wangdian);
                arg1.setTag(entity);
            } else {
                entity = (KongjianEntity) arg1.getTag();
            }
            entity.textView1.setText(zlist.get(arg0).getNetName());
            entity.textView2.setText("" + zlist.get(arg0).getBoxNum());
            entity.textView3.setText("" + 1);
            return arg1;
        }

    }

    /**
     * 款箱晚入数据配置
     */
    private class wanshouAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return wlist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            KongjianEntity entity;
            if (arg1 == null) {
                entity = new KongjianEntity();
                arg1 = LayoutInflater.from(JiaoJieActivity.this).inflate(R.layout.listview_chuangxiangchuru, null);
                entity.textView1 = (TextView) arg1.findViewById(R.id.adapter_kname);
                entity.textView2 = (TextView) arg1.findViewById(R.id.adapter_kuanxiang);
                entity.textView3 = (TextView) arg1.findViewById(R.id.adapter_wangdian);
                arg1.setTag(entity);
            } else {
                entity = (KongjianEntity) arg1.getTag();
            }
            entity.textView1.setText(wlist.get(arg0).getNetName());
            entity.textView2.setText("" + wlist.get(arg0).getBoxNum());
            entity.textView3.setText("" + 1);
            return arg1;
        }

    }

    /*
     * listView 控件实体类
     */
    private class KongjianEntity {
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
    }

    public void getInfo() {
        if(manager.getRuning()!=null){manager.getRuning().remove();}
        manager.getRuning().runding(JiaoJieActivity.this, "数据加载中...");
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {// 获取款箱交接列表 0是早送列表 1是晚收列表
                    Log.d("jiaojie","早送列表 "+GApplication.kxc.getXianlubianhao());
                    GApplication.boxHandoverList = ks.getBoxHandoverList(GApplication.kxc.getXianlubianhao());
                    Log.d("jiaojie",""+GApplication.boxHandoverList);
//                    manager.getRuning().remove();
                    Log.d("jiaojie","是否空"+GApplication.boxHandoverList);
                    if (GApplication.boxHandoverList != null) {
                        Log.d("jiaojie","是否空这判断"+GApplication.boxHandoverList);
                        handler.sendEmptyMessage(7);
                    } else {
                        handler.sendEmptyMessage(4);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(5);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(4);
                } catch (Exception e) {
                    e.printStackTrace();
                    manager.getRuning().remove();
                    handler.sendEmptyMessage(6);
                }
            }

        }.start();
    }

    public void getLineNum() {
//        manager.getRuning().runding(JiaoJieActivity.this, "获取线路中...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                KuanxiangjiaojieService ks = new KuanxiangjiaojieService();
                try {// 押运人员获取路线信息
                    System.out.println("---AA--获取线路编号：" + GApplication.loginname);
                    GApplication.kxc = ks.getLineByUserId(GApplication.loginname);
                    if (GApplication.kxc != null) {
                        handler.sendEmptyMessage(16);
                    } else {
                        if(wlist.size()>0){//  晚收有数据进入录入信息页面
                            Log.d("jiaojie","返回的晚收数据值大于0 证明有数据");
                        }else if (netPoint.equals("")||null==netPoint){//  没有录入过信息
                            handler.sendEmptyMessage(25);// m没数据绑定把
                            Log.d("jiaojie","录入信息了"+netPoint);
                        }

                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(17);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(18);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(19);
                }
            }

        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.getAbnormal().remove();
        manager.getRuning().remove();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            JiaoJieActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.getAbnormal().remove();
        manager.getRuning().remove();
        handler.removeCallbacksAndMessages(null);
    }

    /***
     * 网点人员1  网点人员2  押韵员3
     * @return
     * 2022.1.13 验证和关系网点/押运员关系绑定
     */
    public void getLastReviceBind() {


        if (null == netPoint || netPoint.equals("") || netPointUser.equals("")) {
            handler.sendEmptyMessage(28);// 网点人员为空请重新录入信息

        } else if (cardDrvier.equals("")) {
            handler.sendEmptyMessage(29);//  登录押运员为空请重新登录
        } else {
            manager.getRuning().runding(JiaoJieActivity.this, "关系绑定中...");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    KuanxiangjiaojieService ks = new KuanxiangjiaojieService();
                    try {// 押运人员绑定信息
                        String resultcode = ks.receiveLineTask(netPoint, netPointUser, cardDrvier);

                        if (resultcode.equals("00")) {
                            getLineNum();
                        } else {
                            Bindfailshow = resultcode;
                            if (null != Bindfailshow || !Bindfailshow.equals("")) {
                                handler.sendEmptyMessage(26);
                            }

                        }

                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(31);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(27);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(32);
                    }
                }

            }.start();
        }
    }


}
