package com.ljsw.collateraladministratorsorting.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.collateraladministratorsorting.entity.CleanMangerCheckTaskQingfenEntity;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskListByCollateralBean;
import com.ljsw.collateraladministratorsorting.selecttaskadapter.SelectTaskCollaterAdapter;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpda.main.QingFenLingQu_qf;
import com.ljsw.tjbankpda.main.QinglingWangdianActivity;
import com.ljsw.tjbankpda.main.QinglingZhuangxiangInfoActivity;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QingfenRemwu;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.onTouthImageStyle;
import com.manager.classs.pad.ManagerClass;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * 清分员任务列表界面
 *
 * @author FUHAIQING
 * @time 2021.9.16 15:16
 */
public class CleanMangeCheckTaskQingfenRenwuActivity extends FragmentActivity implements OnTouchListener {
    private static final String TAG = "CMCTQingfenRenwuA";
    /* 定义控件 */
    private RelativeLayout rlTitleQingling;// 请领装箱标题
    private LinearLayout llQingling;// 请领装箱详情
    private ListView lvQingling;// 请领装箱列表
    private RelativeLayout rlTitleShangjiao;// 上缴清分标题
    private LinearLayout llShangjiao;// 上缴清分详情
    private ListView lvShangjiao;// 上缴清分列表
    private Button btnZhuangxiang;// 装箱领款按钮
    private Button btnShangjiao;// 清分领取按钮
    private TextView tvQLNo;// 清分领取任务单号
    private TextView tvSJNo;// 上缴清分任务单号
    private ImageView ivBack;// 返回按钮
    private ImageView ivRefresh;// 刷新按钮
    private BaseAdapter qlAdapter; // 请领适配器
    private BaseAdapter sjAdapter; // 上缴适配器

    /* 定义全局变量 */
    private Table[] RenwuData;
    List<QingfenRemwu> ltQLRenwu = new ArrayList<QingfenRemwu>(); // 请领任务列表
    List<QingfenRemwu> ltSJRenwu = new ArrayList<QingfenRemwu>(); // 上缴任务列表
    private String qlNum;// 请领任务单号
    private String sjNum;// 上缴任务单号
    private int move;
    private ManagerClass manager;// 弹出框

    private String strqlnum;// 请领任务号
    private String strsjnum;//  上缴任务号

    private String strqltype; //  类型
    private String strsjtype;//  类型

    private List<CleanMangerCheckTaskQingfenEntity> CleanltQLRenwulist = new ArrayList<CleanMangerCheckTaskQingfenEntity>(); // 请领任务列表
    private List<CleanMangerCheckTaskQingfenEntity> CleanltSJRenwulist = new ArrayList<CleanMangerCheckTaskQingfenEntity>(); // 上缴任务列表


    private View.OnClickListener OnClick1;
    private Handler okHandle = new Handler() {// 数据获取成功handler
        public void handleMessage(Message msg) {
            if (ltSJRenwu.size() > 0) {
                ltSJRenwu.clear();
            }
            if (ltQLRenwu.size() > 0) {
                ltQLRenwu.clear();
            }
            System.out.println("解析字符串RenwuData:" + RenwuData.toString());
            // 获取请领装箱数据
            if (RenwuData[1].get("renwudan").getValues().size() > 0) {
                System.out.println("获取请领装箱数据");
                qlNum = RenwuData[1].get("renwudan").get(0);
                List<String> ltQl = RenwuData[1].get("xianluming").getValues();
                for (int i = 0; i < ltQl.size(); i++) {
                    ltQLRenwu.add(new QingfenRemwu(RenwuData[1].get("xianluId").get(i),
                            RenwuData[1].get("xianluming").get(i), RenwuData[1].get("wangdianshu").get(i)));
                }
            }
            // 更新adapter
            qlAdapter.notifyDataSetChanged();
            // 获取上缴清分数据
            if (RenwuData[0].get("renwudan").getValues().size() > 0) {
                System.out.println("获取上缴清分数据");
                sjNum = RenwuData[0].get("renwudan").get(0);
                System.out.println("sjNum----->" + sjNum);
                List<String> ltSj = RenwuData[0].get("xianluming").getValues();
                for (int i = 0; i < ltSj.size(); i++) {
                    ltSJRenwu.add(new QingfenRemwu(RenwuData[0].get("xianluId").get(i),
                            RenwuData[0].get("xianluming").get(i), RenwuData[0].get("wangdianshu").get(i),
                            RenwuData[0].get("zhouzhuanxiangshu").get(i)));
                }
            }
            sjAdapter.notifyDataSetChanged();

            if (ltQLRenwu.size() > 0) {
                btnZhuangxiang.setEnabled(true);
                btnZhuangxiang.setBackgroundResource(R.drawable.buttom_selector_bg);
                // 设置任务单号
                tvQLNo.setText(qlNum);
            } else {
                tvQLNo.setText("");
            }
            if (ltSJRenwu.size() > 0) {
                btnShangjiao.setEnabled(true);
                btnShangjiao.setBackgroundResource(R.drawable.buttom_selector_bg);
                tvSJNo.setText(sjNum);
            } else {
                tvSJNo.setText("");
            }

            manager.getRuning().remove();
        }

        ;
    };
    private Handler timeoutHandle = new Handler() {// 连接超时handler
        public void handleMessage(Message msg) {
            manager.getRuning().remove();
            if (msg.what == 0) {
                manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "数据连接超时", new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
//                        getDate();
                        manager.getAbnormal().remove();
                        manager.getRuning().runding(CleanMangeCheckTaskQingfenRenwuActivity.this, "数据加载中...");
                    }
                });
            }
            if (msg.what == 1) {
                manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "网络连接失败", new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        btnZhuangxiang.setEnabled(false);
                        btnZhuangxiang.setBackgroundResource(R.drawable.button_gray);
//                        getDate();
                        manager.getAbnormal().remove();
                        manager.getRuning().runding(CleanMangeCheckTaskQingfenRenwuActivity.this, "数据加载中...");
                    }
                });
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qingfenrenwu);
        manager = new ManagerClass();
        manager.getRuning().runding(this, "数据加载中...");
        Bundle bundle = this.getIntent().getExtras();
        strqlnum = bundle.getString("qlNum");

        strsjnum = bundle.getString("sjNum");

        strqltype = bundle.getString("qltype");
        ; //  类型
        strsjtype = bundle.getString("sjtype");
        ;//  类型
        // 绑定控件
        findView();

        // 设置监听
        ivBack.setOnTouchListener(this);
        ivRefresh.setOnTouchListener(this);
        setListener();
        findListView(); // 绑定listView
        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                getListClearingWorkDetail();
            }
        };
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("调用getDate");
        // 获取数据
//        getDate();
        getListClearingWorkDetail();
    }

    /**
     * 初始化控件
     */
    private void findView() {
        llQingling = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_qingling);
        llShangjiao = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_shangjiao);
        rlTitleQingling = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_qingling_title);
        rlTitleShangjiao = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_shangjiao_title);
        lvQingling = (ListView) findViewById(R.id.lv_qingfenrenwu_qingling);
        lvShangjiao = (ListView) findViewById(R.id.lv_qingfenrenwu_shangjiao);
        btnZhuangxiang = (Button) findViewById(R.id.btn_qingfenrenwu_qingling);
        btnShangjiao = (Button) findViewById(R.id.btn_qingfenrenwu_shangjiao);
        tvQLNo = (TextView) findViewById(R.id.tv_qingfenrenwu_qingling_orderno);
        tvSJNo = (TextView) findViewById(R.id.tv_qingfenrenwu_shangjiao_orderno);
        ivBack = (ImageView) findViewById(R.id.iv_qingfenrenwu_back);
        ivRefresh = (ImageView) findViewById(R.id.iv_qingfenrenwu_refresh);
        btnZhuangxiang.setEnabled(false);
        btnZhuangxiang.setBackgroundResource(R.drawable.button_gray);
        btnShangjiao.setEnabled(false);
        btnShangjiao.setBackgroundResource(R.drawable.button_gray);
        //设置顶部的任号//  上缴、请领
        if(strqlnum==null||strqlnum.equals("")){
            tvQLNo.setText("");
        }else{
            tvQLNo.setText(strqlnum+"");
        }

        if(strsjnum==null||strsjnum.equals("")){
            tvSJNo.setText("");
        }else{
            tvSJNo.setText(strsjnum+"");
        }

    }

    /**
     * listView 绑定适配器
     */
    private void findListView() {
        System.out.println("Adapter绑定");
//        qlAdapter = new QinglingBaseAdapter(ltQLRenwu);
//        sjAdapter = new ShangjiaoBaseAdapter(ltSJRenwu);


        QinglingBaseAdapter qladapter = new QinglingBaseAdapter(CleanltQLRenwulist);
//                    lv_selecttaskbycollate_shangjiao.setAdapter(selectTaskCollaterAdapter);
        lvQingling.setAdapter(qladapter);
        ShangjiaoBaseAdapter sjadapter = new ShangjiaoBaseAdapter(CleanltSJRenwulist);
        lvShangjiao.setAdapter(sjadapter);
        // 绑定Adapter

//        lvShangjiao.setAdapter(sjAdapter);
//        lvQingling.setAdapter(qlAdapter);
        // 设置监听
        lvQingling.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Bundle bundle = new Bundle();
                String xianluId = RenwuData[1].get("xianluId").get(arg2);
                bundle.putString("XianluName", ltQLRenwu.get(arg2).getName());
                bundle.putString("XianluId", xianluId);
                bundle.putString("renwudan", qlNum);

                Skip.skip(CleanMangeCheckTaskQingfenRenwuActivity.this, QinglingWangdianActivity.class, bundle, 0);
            }
        });
    }

    /**
     * 设置监听
     */
    private void setListener() {
        rlTitleQingling.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llShangjiao.setVisibility(View.GONE);
                llQingling.setVisibility(View.VISIBLE);
            }
        });
        rlTitleShangjiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llQingling.setVisibility(View.GONE);
                llShangjiao.setVisibility(View.VISIBLE);
            }
        });
        btnZhuangxiang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putString("qlNum", qlNum); //strqlnum
                // bundle.putString("tvSJNo", tvSJNo.getText().toString());
                bundle.putInt("biaoqian", 0);
                Skip.skip(CleanMangeCheckTaskQingfenRenwuActivity.this, QinglingZhuangxiangInfoActivity.class, bundle, 0);
            }
        });
        btnShangjiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putString("sjNum", strsjnum);
                Skip.skip(CleanMangeCheckTaskQingfenRenwuActivity.this, QingFenLingQu_qf.class, bundle, 0);
            }
        });
    }

    /**
     * 获得请领装箱支行列表数据，上缴清分任务线路
     *
     * @returns
     */
//    private void getDate() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String params;
//                try {
//
//                    // 调用接口获得全部数据
//                    System.out.println("上传的参数（清分员1）=" + Mapplication.getApplication().UserId);
//                    params = new QingfenRenwuService().getQingfenRenwu(Mapplication.getApplication().UserId);
//                    System.out.println("返回的结果:" + params);
//                    // 解析字符串
//                    RenwuData = Table.doParse(params);
//                    Message msg = okHandle.obtainMessage();
//                    msg.obj = RenwuData;
//
//                    okHandle.sendEmptyMessage(0);
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    timeoutHandle.sendEmptyMessage(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    timeoutHandle.sendEmptyMessage(1);
//                }
//            }
//        }).start();
//
//    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        btnZhuangxiang.setEnabled(false);
        btnZhuangxiang.setBackgroundResource(R.drawable.button_gray);
        btnShangjiao.setEnabled(false);
        btnShangjiao.setBackgroundResource(R.drawable.button_gray);

    }

    /**
     * 请领装箱自定义Adapter
     */
    class QinglingBaseAdapter extends BaseAdapter {
        private List<CleanMangerCheckTaskQingfenEntity> lt;
        private ViewHolder vh;

        public QinglingBaseAdapter(List<CleanMangerCheckTaskQingfenEntity> lt) {
            super();
            this.lt = lt;
        }

        @Override
        public int getCount() {
            return lt.size();
        }

        @Override
        public Object getItem(int arg0) {
            return lt.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View v, ViewGroup arg2) {
            LayoutInflater inflater = LayoutInflater.from(CleanMangeCheckTaskQingfenRenwuActivity.this);
            if (v == null) {
                v = inflater.inflate(R.layout.item_qingfenrenwu_qingling, null);
                vh = new ViewHolder();
                vh.tvName = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_xianluName);
                vh.tvCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_orderNumber);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            if (lt.size() == 0) {
                btnZhuangxiang.setEnabled(false);
                btnZhuangxiang.setBackgroundResource(R.drawable.button_gray);
            } else {
                btnZhuangxiang.setEnabled(true);
                btnZhuangxiang.setBackgroundResource(R.drawable.buttom_selector_bg);
            }
            vh.tvName.setText(lt.get(position).getLineName());
            vh.tvCount.setText("" + lt.get(position).getBoxCount());
            return v;
        }

        public class ViewHolder {
            TextView tvName;
            TextView tvCount;
        }
    }

    /**
     * 上缴清分自定义Adapter
     */
    class ShangjiaoBaseAdapter extends BaseAdapter {
        private List<CleanMangerCheckTaskQingfenEntity> lt;
        private ViewHolder vh;

        public ShangjiaoBaseAdapter(List<CleanMangerCheckTaskQingfenEntity> lt) {
            this.lt = lt;
        }

        @Override
        public int getCount() {
            return lt.size();
        }

        @Override
        public Object getItem(int arg0) {
            return lt.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View v, ViewGroup arg2) {
            LayoutInflater inflater = LayoutInflater.from(CleanMangeCheckTaskQingfenRenwuActivity.this);
            if (v == null) {
                v = inflater.inflate(R.layout.item_qingfenrenwu_shangjiao, null);
                vh = new ViewHolder();
                vh.tvName = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_shnagjiao_name);
                vh.tvWangdianCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_shnagjiao_wangdianshuliang);
                vh.tvZhouzhuanxiangCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_shnagjiao_boxCount);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            if (lt.size() == 0) {
                btnShangjiao.setEnabled(false);
                btnShangjiao.setBackgroundResource(R.drawable.button_gray);
            } else {
                btnShangjiao.setEnabled(true);
                btnShangjiao.setBackgroundResource(R.drawable.buttom_selector_bg);
            }
//            vh.tvName.setText(lt.get(position).getName());
//            vh.tvWangdianCount.setText("" + lt.get(position).getWangdianCount());
//            vh.tvZhouzhuanxiangCount.setText("" + lt.get(position).getZhouzhuanxiangCount());
            vh.tvName.setText(lt.get(position).getLineName());
            vh.tvWangdianCount.setText("" + lt.get(position).getOrgCount());
            vh.tvZhouzhuanxiangCount.setText("" + lt.get(position).getBoxCount());
            return v;
        }

        public class ViewHolder {
            TextView tvName;
            TextView tvWangdianCount;
            TextView tvZhouzhuanxiangCount;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.iv_qingfenrenwu_back:
                    new onTouthImageStyle().setFilter(ivBack);
                    break;
                case R.id.iv_qingfenrenwu_refresh:
                    new onTouthImageStyle().setFilter(ivRefresh);
                    break;
                default:
                    break;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // 每移动一次move+1
            move++;
//			System.out.println("=====move:"+move);
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            // 只有在move<canMove的情况下才能执行操作.
            int canMove = 10;
            switch (v.getId()) {
                case R.id.iv_qingfenrenwu_back:
                    new onTouthImageStyle().removeFilter(ivBack);
                    if (move < canMove) {
                        this.finish();
                    }
                    break;
                case R.id.iv_qingfenrenwu_refresh:
                    new onTouthImageStyle().removeFilter(ivRefresh);
                    if (move < canMove) {
//                        getDate();
                        getListClearingWorkDetail();//  刷新数据
                        manager.getRuning().runding(CleanMangeCheckTaskQingfenRenwuActivity.this, "正在刷新中...");
                    }
                    break;
                default:
                    break;
            }
            move = 0;
        }
        if (e.getAction() == MotionEvent.ACTION_CANCEL) {
            switch (v.getId()) {
                case R.id.iv_qingfenrenwu_back:
                    new onTouthImageStyle().setFilter(ivBack);
                    break;
                case R.id.iv_qingfenrenwu_refresh:
                    new onTouthImageStyle().setFilter(ivRefresh);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub‘
        CleanMangeCheckTaskQingfenRenwuActivity.this.finish();
        return super.onKeyDown(keyCode, event);
    }

    /***
     *显示 隐藏任务sj  ql 任务列表
     */

    private String parms;

    private void getListClearingWorkDetail() {

        manager.getRuning().runding(CleanMangeCheckTaskQingfenRenwuActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {

//                    strqlnum = bundle.getString("qlNum");
//
//                    strsjnum = bundle.getString("sjNum");
//
//                    strqltype = bundle.getString("qltype");
//                    ; //  类型
//                    strsjtype = bundle.getString("sjtype");
                    String upnum = "";
                    String gettype = "";
                    if (strqlnum == null||strqlnum.equals("")) {
                        upnum = strsjnum;
                        gettype = strsjtype;
                    } else if (strsjnum == null||strsjnum.equals("")) {
                        upnum = strqlnum;
                        gettype = strqltype;
                    } else {
                        Log.e(TAG, "测试upnum====" + upnum+"+++++++++++++type===="+gettype);

                        handler.sendEmptyMessage(4);
                    }


                    parms = new GetResistCollateralBaggingService().getListClearingWorkDetail(upnum, gettype);
                    Log.e(TAG, "测试" + parms);
                    // 返回的类型anyType{}需要进行判断
                    if (parms != null && !parms.equals("anyType{}")) {
                        Gson gson = new Gson();
                        CleanltQLRenwulist.clear();// 每次进入后清除
                        CleanltSJRenwulist.clear();
                        Type type = new TypeToken<ArrayList<CleanMangerCheckTaskQingfenEntity>>() {
                        }.getType();

                        List<CleanMangerCheckTaskQingfenEntity> listPrint = gson.fromJson(parms,
                                type);
//                        返回结果中任务类型:0是上缴,1是请领
                        if (gettype.equals("1")) { //请领
                            CleanltQLRenwulist = listPrint;
                        } else if (gettype.equals("0")) { //上缴
                            CleanltSJRenwulist = listPrint;
                        }


                        handler.sendEmptyMessage(2);

                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();


    }

    /***
     * 网络返回数据显示
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
//                    selectTaskCollaterAdapter = new SelectTaskCollaterAdapter(AllTypeList, SelectTaskByCollateralActivity.this);
//                    lv_selecttaskbycollate_shangjiao.setAdapter(selectTaskCollaterAdapter);
//
//
//                    qinglingBaseAdapter = new SelectTaskByCollateralActivity.QinglingBaseAdapter(selectTaskBYCollaterallistql);//                    上缴
//                    System.out.println("Adapter绑定");
//                    // 绑定Adapter
//                    lv_qingfenrenwu_qingling.setAdapter(qinglingBaseAdapter);//                    上缴
//                    selectTaskCollaterAdapter.notifyDataSetChanged();
//                    qinglingBaseAdapter.notifyDataSetChanged();
//                    adapter.notifyDataSetChanged();
//                    listview.setAdapter(adapter);
//				new TurnListviewHeight(listview);

                    QinglingBaseAdapter qladapter = new QinglingBaseAdapter(CleanltQLRenwulist);
//                    lv_selecttaskbycollate_shangjiao.setAdapter(selectTaskCollaterAdapter);
                    lvQingling.setAdapter(qladapter);
                    ShangjiaoBaseAdapter sjadapter = new ShangjiaoBaseAdapter(CleanltSJRenwulist);
                    lvShangjiao.setAdapter(sjadapter);

                    qladapter.notifyDataSetChanged();
                    sjadapter.notifyDataSetChanged();

                    rlTitleQingling = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_qingling_title);
                    rlTitleShangjiao = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_shangjiao_title);
                    if(CleanltQLRenwulist.size()>0&&CleanltSJRenwulist.size()<1){
                        llQingling.setVisibility(View.VISIBLE);
                        llShangjiao.setVisibility(View.GONE);
                        rlTitleShangjiao.setVisibility(View.GONE);
                    }else if(CleanltSJRenwulist.size()>0&&CleanltQLRenwulist.size()<1){
                        llShangjiao.setVisibility(View.VISIBLE);
                        llQingling.setVisibility(View.GONE);
                        rlTitleQingling.setVisibility(View.GONE);// 顶部的标题进行隐藏
                    }else{
                        llQingling.setVisibility(View.VISIBLE);
                        llShangjiao.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "没有任务", new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;
                case 4:

                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CleanMangeCheckTaskQingfenRenwuActivity.this, "参数不完整", new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;
                default:
                    break;
            }
        }

    };
}
