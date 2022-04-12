package com.ljsw.collateraladministratorsorting.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskByCollateralEntity;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskListByCollateralBean;
import com.ljsw.collateraladministratorsorting.selecttaskadapter.SelectTaskCollaterAdapter;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryLatticeEntity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.diziyapinshangjiao.BoxToDiZhiYaPinZhuangdaiActivity;
import com.ljsw.tjbankpda.main.QingFenLingQu_qf;
import com.ljsw.tjbankpda.main.QingfenRenwuActivity;
import com.ljsw.tjbankpda.main.QinglingZhuangxiangInfoActivity;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.ShangJiaoQingFen_o_qf_Print_Entity;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * 抵质押品管理员
 * 做上缴和请领的操作 listview
 *  条目点击跳转
 *  返回结果中任务类型:0是上缴,1是请领
 */
public class SelectTaskByCollateralActivity extends FragmentActivity implements View.OnClickListener {


    private static final String TAG = "SelectTaskByCollateralActivity";
    private List<SelectTaskListByCollateralBean> AllTypeList = new ArrayList<SelectTaskListByCollateralBean>();// 接受全类型的人数据数据集合
    private List<SelectTaskListByCollateralBean> selectTaskBYCollaterallistsj = new ArrayList<>();//  上缴任务集合
    private List<SelectTaskListByCollateralBean> selectTaskBYCollaterallistql = new ArrayList<>();// 请领取
    private ListView lv_selecttaskbycollate_shangjiao;// 显示上缴任务
    //  显示请领取listview
    private ListView lv_qingfenrenwu_qingling;//显示请领

    private TextView tv_lv_selecttaskbycollate_shangjiao_qingling_orderno;//上缴任务号
    private ImageView iv_selecttaskbycollateral_refresh;//  刷新
    private RelativeLayout rlTitleQingling;// 请领装箱标题
    private RelativeLayout rlTitleShangjiao;// 上缴清分标题

    private LinearLayout llShangjiao;
    private LinearLayout llQingling;

    private String qlNum;// 请领任务单号
    private String sjNum;// 上缴任务单号

    private ImageView iv_selecttaskbycollateral_back;//返回键

    // 适配器

    private QinglingBaseAdapter qinglingBaseAdapter;
    private ShangjiaoBaseAdapter sjBaseAdapter;
    private ManagerClass manager;
    private View.OnClickListener OnClick1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttaskbycollateral);
        manager = new ManagerClass();
        initView();
        setListener();
        loadData();
        findListView();

        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                upDataDZTask();
            }
        };
    }

    private void findListView() {
        System.out.println("Adapter绑定");
        qinglingBaseAdapter = new QinglingBaseAdapter(selectTaskBYCollaterallistql);

        System.out.println("Adapter绑定");
        // 绑定Adapter
        lv_qingfenrenwu_qingling.setAdapter(qinglingBaseAdapter);
    }

    /**
     * 每次进入可以清空在添加数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        upDataDZTask();//  每次进入重新请求数据


    }

    private void loadData() {
//        SelectTaskByCollateralEntity entity = new SelectTaskByCollateralEntity();
//        entity.setId("");
//        entity.setName("任务1");
//        entity.setWangdianCount("4");
//        entity.setZhouzhuanxiangCount("2");
//        SelectTaskByCollateralEntity entity1 = new SelectTaskByCollateralEntity();
//        entity1.setId("2");
//        entity1.setName("任务2");
//        entity1.setWangdianCount("5");
//        entity1.setZhouzhuanxiangCount("5");
//        selectTaskBYCollaterallistsj.add(entity);
//        selectTaskBYCollaterallistsj.add(entity1);
//        selectTaskCollaterAdapter = new SelectTaskCollaterAdapter(selectTaskBYCollaterallistsj, SelectTaskByCollateralActivity.this);
//        lv_selecttaskbycollate_shangjiao.setAdapter(selectTaskCollaterAdapter);
//
//        selectTaskBYCollaterallistql.clear();
//        SelectTaskByCollateralEntity entitysj = new SelectTaskByCollateralEntity();
//        entitysj.setId("1");
//        entitysj.setName("RW1QF6020210812111408");
//        entitysj.setWangdianCount("4");
//        entitysj.setZhouzhuanxiangCount("2");
//        selectTaskBYCollaterallistql.add(entitysj);
    }

    private void initView() {
        iv_selecttaskbycollateral_back = (ImageView) findViewById(R.id.iv_selecttaskbycollateral_back);
        iv_selecttaskbycollateral_back.setOnClickListener(this);
        iv_selecttaskbycollateral_refresh = (ImageView) findViewById(R.id.iv_selecttaskbycollateral_refresh);
        iv_selecttaskbycollateral_refresh.setOnClickListener(this);

        lv_selecttaskbycollate_shangjiao = (ListView) findViewById(R.id.lv_selecttaskbycollate_shangjiao);
        lv_selecttaskbycollate_shangjiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SelectTaskByCollateralActivity.this, SelectTaskByCollateralActivity.class);
                if (AllTypeList == null || AllTypeList.size() == 0) {

                } else {
                    sjNum = AllTypeList.get(position).getClearTaskNum();
                    String sjtype = AllTypeList.get(position).getClearTaskType();
                    Bundle bundle = new Bundle();

                    bundle.putString("sjNum", sjNum);
                    bundle.putString("sjtype", sjtype);
                    bundle.putInt("biaoqian", 0);
//                Skip.skip(SelectTaskByCollateralActivity.this, QinglingZhuangxiangInfoActivity.class, bundle, 0);
                    Skip.skip(SelectTaskByCollateralActivity.this, CleanMangeCheckTaskQingfenRenwuActivity.class, bundle, 0);
                }

//                startActivity(intent);
            }
        });

        rlTitleQingling = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_qingling_titleselecttaskbycollateralql);
        rlTitleShangjiao = (RelativeLayout) findViewById(R.id.rl_qingfenrenwu_qingling_titleselecttaskbycollateralsj);

        llQingling = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_ql);
        llShangjiao = (LinearLayout) findViewById(R.id.ll_qingfenrenwu_sj);
//  请领的组件跳转页面
        lv_qingfenrenwu_qingling = (ListView) findViewById(R.id.lv_qingfenrenwu_qingling);//请领组件
        lv_qingfenrenwu_qingling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qlNum = selectTaskBYCollaterallistql.get(position).getClearTaskNum();
                String qltype = selectTaskBYCollaterallistql.get(position).getClearTaskType();
                Bundle bundle = new Bundle();
                bundle.putString("qlNum", qlNum);//  传递任务单号
                bundle.putString("qltype", qltype);
                Skip.skip(SelectTaskByCollateralActivity.this, CleanMangeCheckTaskQingfenRenwuActivity.class, bundle, 0);

            }
        });


    }

    private void setListener() {
        rlTitleQingling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llShangjiao.setVisibility(View.GONE);
                llQingling.setVisibility(View.VISIBLE);
            }
        });
        rlTitleShangjiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                llQingling.setVisibility(View.GONE);
                llShangjiao.setVisibility(View.VISIBLE);
            }
        });
//        btnZhuangxiang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Bundle bundle = new Bundle();
//                bundle.putString("qlNum", qlNum);//  传递任务单号
//                // bundle.putString("tvSJNo", tvSJNo.getText().toString());
//                bundle.putInt("biaoqian", 0);
//                Skip.skip(SelectTaskByCollateralActivity.this, QinglingZhuangxiangInfoActivity.class, bundle, 0);
//            }
//        });
//        btnShangjiao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
        Bundle bundle = new Bundle();
        bundle.putString("sjNum", sjNum);
//                Skip.skip(SelectTaskByCollateralActivity.this, QingFenLingQu_qf.class, bundle, 0);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_selecttaskbycollateral_back:
                SelectTaskByCollateralActivity.this.finish();
                break;
            case R.id.iv_selecttaskbycollateral_refresh:
                upDataDZTask();
                break;
            default:
                break;
        }
    }

    /***
     * 更新数据
     * 操作
     * 请求数据操作
     *
     * 返回
     * [{"clearTaskNum":"RW0QF0520210916110120","clearTaskType":"0"}]
     */
    private String parms;//  接受网络返回结果

    private void upDataDZTask() {

        manager.getRuning().runding(SelectTaskByCollateralActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    Mapplication.getApplication().UserId = number;
//                    String number="05005";
                    parms = new GetResistCollateralBaggingService().getListClearingWork(number);
                    Log.e(TAG, "测试" + parms);
                    // 返回的类型anyType{}需要进行判断
                    if (parms != null && !parms.equals("anyType{}")) {
                        Gson gson = new Gson();
                        AllTypeList.clear();// 每次进入后清除
                        selectTaskBYCollaterallistql.clear();
                        Type type = new TypeToken<ArrayList<SelectTaskListByCollateralBean>>() {
                        }.getType();

                        List<SelectTaskListByCollateralBean> listPrint = gson.fromJson(parms,
                                type);
//                        返回结果中任务类型:0是上缴,1是请领
                        for (int i = 0; i < listPrint.size(); i++) {
                            if (listPrint.get(i).getClearTaskType().equals("1")) {// 区分和请领
                                SelectTaskListByCollateralBean selectTaskListByCollateralBean = new SelectTaskListByCollateralBean();
                                selectTaskListByCollateralBean.setClearTaskNum(listPrint.get(i).getClearTaskNum());
                                selectTaskListByCollateralBean.setClearTaskType(listPrint.get(i).getClearTaskType());
                                selectTaskBYCollaterallistql.add(selectTaskListByCollateralBean);
                            } else if (listPrint.get(i).getClearTaskType().equals("0")) {//上缴
                                SelectTaskListByCollateralBean selectTaskListByCollateralBean = new SelectTaskListByCollateralBean();
                                selectTaskListByCollateralBean.setClearTaskNum(listPrint.get(i).getClearTaskNum());
                                selectTaskListByCollateralBean.setClearTaskType(listPrint.get(i).getClearTaskType());
                                AllTypeList.add(selectTaskListByCollateralBean);
                            }
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
     * 网络请求的分步骤处理
     */

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            manager.getRuning().remove();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SelectTaskByCollateralActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SelectTaskByCollateralActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
                    sjBaseAdapter = new ShangjiaoBaseAdapter(AllTypeList);
                    lv_selecttaskbycollate_shangjiao.setAdapter(sjBaseAdapter);

                    if (qinglingBaseAdapter == null) {
                        qinglingBaseAdapter = new QinglingBaseAdapter(selectTaskBYCollaterallistql);//                    上缴
                        System.out.println("Adapter绑定");
                        Log.e(TAG, "测试" + selectTaskBYCollaterallistql.size());
                    } else {
                        qinglingBaseAdapter.notifyDataSetChanged();
                        Log.e(TAG, "测试" + selectTaskBYCollaterallistql.size());
                    }

                    // 绑定Adapter
                    lv_qingfenrenwu_qingling.setAdapter(qinglingBaseAdapter);//                    上缴
                    sjBaseAdapter.notifyDataSetChanged();

//                    adapter.notifyDataSetChanged();
//                    listview.setAdapter(adapter);
//				new TurnListviewHeight(listview);
//                    有数据展示逻辑
                    if (selectTaskBYCollaterallistql.size() > 0 && AllTypeList.size() < 1) {
                        llQingling.setVisibility(View.VISIBLE);
                        llShangjiao.setVisibility(View.GONE);
                    } else if (AllTypeList.size() > 0 && selectTaskBYCollaterallistql.size() < 1) {
                        llShangjiao.setVisibility(View.VISIBLE);
                        llQingling.setVisibility(View.GONE);
                    } else {
                        llQingling.setVisibility(View.VISIBLE);
                        llShangjiao.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SelectTaskByCollateralActivity.this, "没有任务", new View.OnClickListener() {

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

    class QinglingBaseAdapter extends BaseAdapter {
        private List<SelectTaskListByCollateralBean> lt;
        private SelectTaskByCollateralActivity.QinglingBaseAdapter.ViewHolder vh;

        public QinglingBaseAdapter(List<SelectTaskListByCollateralBean> lt) {
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
            LayoutInflater inflater = LayoutInflater.from(SelectTaskByCollateralActivity.this);
            if (v == null) {
                v = inflater.inflate(R.layout.item_qingfenrenwu_qingling, null);
                vh = new SelectTaskByCollateralActivity.QinglingBaseAdapter.ViewHolder();
                vh.tvName = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_xianluName);
                vh.tvCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_orderNumber);
                v.setTag(vh);
            } else {
                vh = (SelectTaskByCollateralActivity.QinglingBaseAdapter.ViewHolder) v.getTag();
            }
            vh.tvName.setText(lt.get(position).getClearTaskNum());
            vh.tvCount.setVisibility(View.GONE);
//            vh.tvCount.setText("" + lt.get(position).getClearTaskNum());
            return v;
        }

        public class ViewHolder {
            TextView tvName;
            TextView tvCount;
        }
    }

    class ShangjiaoBaseAdapter extends BaseAdapter {
        private List<SelectTaskListByCollateralBean> lt;
        private SelectTaskByCollateralActivity.ShangjiaoBaseAdapter.ViewHolder vh;

        public ShangjiaoBaseAdapter(List<SelectTaskListByCollateralBean> lt) {
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
            LayoutInflater inflater = LayoutInflater.from(SelectTaskByCollateralActivity.this);
            if (v == null) {
                v = inflater.inflate(R.layout.item_qingfenrenwu_qingling, null);
                vh = new SelectTaskByCollateralActivity.ShangjiaoBaseAdapter.ViewHolder();
                vh.tvName = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_xianluName);
                vh.tvCount = (TextView) v.findViewById(R.id.tv_item_qingfenrenwu_qingling_orderNumber);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            vh.tvName.setText(lt.get(position).getClearTaskNum());
            vh.tvCount.setVisibility(View.GONE);
//            vh.tvCount.setText("" + lt.get(position).getClearTaskNum());
            return v;
        }

        public class ViewHolder {
            TextView tvName;
            TextView tvCount;
        }
    }

}
