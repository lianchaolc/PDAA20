package com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.AccountInfomationReturnService;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.ZhangHuZiLiaoChuRuKuMingXiActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.manager.classs.pad.ManagerClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 账户资料归还
 *
 * @author Administrator 账户资料归还 归还任务列表和详情 2018_11_14lc
 */
public class ZhangHuZiLiaoGuiHuanActivity extends Activity implements OnClickListener {
    protected static final String TAG = "ZhangHuZiLiaoGuiHuanActivity";
    // 组件
    private ImageView ql_ruku_back;
    private Button ql_ruku_update;
    private TextView caozuoren, TaskNumber_tv;
    private ListView guihuanrenwulistview;
    private List<String> checkList = new ArrayList<String>();
    // 适配器
    private QingLingAdapter adapter;
    // 变量
    private String resultparms = "";// 网络请求返回值
    private String taskNumber = "";// 任务编号
    // g工具类
    private ManagerClass manager;
    private OnClickListener OnClick1;
    // 归还任务列表和详情实体类
    private ReturnAccountInformationEntity raientity = new ReturnAccountInformationEntity();
    // 接受数据的集合
    List<ReturnAccountInformationEntity> returntolist = new ArrayList<ReturnAccountInformationEntity>();
    public static ZhangHuZiLiaoGuiHuanActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhanghuziliaoguihuanrenwuliebiao);
        instance = this;
        initView();
        adapter = new QingLingAdapter();
        manager = new ManagerClass();
        OnClick1 = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                getAccountReturnTaskList();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (o_Application.qinglingruku.size() > 0) {
            o_Application.qinglingruku.clear();
        }
        adapter.notifyDataSetChanged();
        guihuanrenwulistview.setAdapter(adapter);
        getAccountReturnTaskList();

        OnClick1 = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                getAccountReturnTaskList();
            }
        };
    }

    private void initView() {
        // TODO Auto-generated method stub
        caozuoren = (TextView) findViewById(R.id.returnaccountinformation_username);
        caozuoren.setText(GApplication.user.getLoginUserName());
        guihuanrenwulistview = (ListView) findViewById(R.id.returnaccountinformation_listview);
        ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
        ql_ruku_update.setOnClickListener(this);
        guihuanrenwulistview.setOnItemClickListener(new OnItemClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                manager.getRuning().runding(ZhangHuZiLiaoGuiHuanActivity.this, "正在开启扫描请稍等...");
                Intent intent = new Intent(ZhangHuZiLiaoGuiHuanActivity.this,
                        ZhangHuZiLiaoGuiHuanJiaojieActivity.class);
//                Log.e(TAG, "====!!!cd===" + o_Application.qinglingruku.size());
//                Log.e(TAG, "=======" + o_Application.qinglingruku.get(arg2));

                if(o_Application.qinglingruku==null){
                    Log.d(TAG,"数据加载未完成------");
                }else{
                o_Application.qlruku = o_Application.qinglingruku.get(arg2);
                Log.e(TAG, "=======" + taskNumber);
                intent.putExtra("taskNumber", o_Application.qinglingruku.get(arg2).getRiqi());
                startActivity(intent);
               }
                manager.getRuning().remove();
            }
        });
        ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
        ql_ruku_back.setOnClickListener(this);

    }

    /***
     * 账户资料归还的网络请求
     */
    private void getAccountReturnTaskList() {
        manager.getRuning().runding(ZhangHuZiLiaoGuiHuanActivity.this, "正在获取数请稍等...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.user.getYonghuZhanghao();

                    resultparms = new AccountInfomationReturnService().getAccountReturnTaskList(number);
                    Log.d(TAG, "网络返回结果=====" + resultparms);
                    if (!resultparms.equals("")) {
                        Gson gson = new Gson();

                        ReturnAccountInformationEntity[] raientity = gson.fromJson(resultparms,
                                ReturnAccountInformationEntity[].class);

                        // 非正常的数据通过数组方式进行接收
                        returntolist = Arrays.asList(raientity);
                        List arrList = new ArrayList(returntolist);
                        for (int i = 0; i < raientity.length; i++) {
                            checkList.addAll(arrList);
                        }

                        if (returntolist.size() < 1) {
                            handler.sendEmptyMessage(3);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } else if (resultparms.equals("失败")) {
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

    private Handler handler = new Handler() {

        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            manager.getRuning().remove();
            switch (msg.what) {

                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(ZhangHuZiLiaoGuiHuanActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(ZhangHuZiLiaoGuiHuanActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
                    getData();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "==========InventoryThread============+" + Thread.currentThread().getName());

                    adapter.notifyDataSetChanged();
                    guihuanrenwulistview.setAdapter(adapter);
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(ZhangHuZiLiaoGuiHuanActivity.this, "没有任务", new OnClickListener() {

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

    /***
     * 请求到的网络数据放到集合进行扫描
     */
    private void getData() {
        o_Application.qinglingruku.clear();
        if (returntolist.get(0).getDetailList().size() > 0) {
            for (int i = 0; i < returntolist.size(); i++) {
                o_Application.qinglingruku.add(new QingLingRuKu(returntolist.get(i).getCOUNT(),
                        returntolist.get(i).getCVOUN(), returntolist.get(i).getDetailList()));

            }
        }
    }

    /***
     * 显示三个列表的适配器
     *
     * @author Administrator
     *
     */

    class QingLingAdapter extends BaseAdapter {
        LayoutInflater lf = LayoutInflater.from(ZhangHuZiLiaoGuiHuanActivity.this);
        ViewHodler view;

        @Override
        public int getCount() {
            return returntolist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return returntolist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                view = new ViewHodler();
                arg1 = lf.inflate(R.layout.listview_walkie_item, null);
                view.danhao = (TextView) arg1.findViewById(R.id.lv_dizhiyapinid);
                view.riqi = (TextView) arg1.findViewById(R.id.dizhiyapinitem);
                view.count = (TextView) arg1.findViewById(R.id.dizhiyapintype);
                arg1.setTag(view);
            } else {
                view = (ViewHodler) arg1.getTag();
            }
            view.danhao.setText((arg0 + 1) + "");
            view.riqi.setText(returntolist.get(arg0).getCVOUN());
            view.count.setText(returntolist.get(arg0).getDetailList().size() + "");
            return arg1;
        }

    }

    public static class ViewHodler {
        TextView danhao, riqi, count;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ql_ruku_back:
                ZhangHuZiLiaoGuiHuanActivity.this.finish();
                break;
            case R.id.ql_ruku_update:
                getAccountReturnTaskList();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.getRuning().remove(); //  去除遮罩
    }
}
