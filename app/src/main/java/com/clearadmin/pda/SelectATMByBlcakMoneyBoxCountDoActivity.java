package com.clearadmin.pda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.application.GApplication;
import com.clearadmin.biz.SelectATMByBlackMoneyBoxCleanService;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.adapter.SelectATMBlackMoneBoxCountAdapter;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.GetTurnoverboxchecketLineinfoActivity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.server.TureOverBoxCheckServer;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnNoverBoxEntity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnverboxcheckineinfoadapter.TurnoverBoxCheckLinadapter;
import com.manager.classs.pad.ManagerClass;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 *对比的是ATM 中 钞箱所有钱数
 *
 * 当前页面是列表选择 atm
 */
public class SelectATMByBlcakMoneyBoxCountDoActivity extends
        Activity implements View.OnClickListener {
    private static final String TAG = "SelectATMByBlcakMoneyBoxCountDoActivity";
    private ListView recycleviewbybox;//  组件
    private ImageView select_atm_imageView1;//返回
    private SelectATMBlackMoneBoxCountAdapter adapter;
    private ImageView select_atm_countback;//  返回
    private List<String> sflist = new ArrayList<String>();//实体类数据源
    //  服务
    private SelectATMByBlackMoneyBoxCleanService selectATMService;
    private ManagerClass manageryuan;
    private View.OnClickListener OnClick1;
    private ManagerClass manager;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(SelectATMByBlcakMoneyBoxCountDoActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(SelectATMByBlcakMoneyBoxCountDoActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:

                    Toast.makeText(SelectATMByBlcakMoneyBoxCountDoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    break;

                case 3:

                    Toast.makeText(SelectATMByBlcakMoneyBoxCountDoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
//                     设置数据元


                    break;

                case 7:
                    Toast.makeText(SelectATMByBlcakMoneyBoxCountDoActivity.this, " 参数为空 ", Toast.LENGTH_SHORT).show();
                default:
                    break;


            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_atmby_blcak_money_box_count_do);
        initView();
        manageryuan = new ManagerClass();
        manager = new ManagerClass();
        LoadData();

        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                LoadData();
            }
        };
    }

    /***
     * 网络请求数据
     */
    private void LoadData() {
        sflist.add("1");
        sflist.add("2");
        sflist.add("3");
        sflist.add("4");

        adapter = new SelectATMBlackMoneBoxCountAdapter(SelectATMByBlcakMoneyBoxCountDoActivity.this, sflist);
        recycleviewbybox.setAdapter(adapter);
        recycleviewbybox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent istart = new Intent(SelectATMByBlcakMoneyBoxCountDoActivity.this, ClearAddMoneyOutDo.class);
                istart.putExtra("", "");
                startActivity(istart);
            }
        });

//        new Thread() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    // 用户账号
//                    String Userid = "";
//                    String orginid = "";
//                    String ATMyuliu = "";
//                    if (Userid.equals("") && Userid.equals("") && Userid.equals("")) {
//                        handler.sendEmptyMessage(7);
//                        return;
//                    } else {
//                        String Canshutype = "";
////                        for (String s : mapstr.keySet()) {
////                            System.out.println("key : " + s + " value : " + mapstr.get(s));
////                            if (("").equals(mapstr.get(s))) {
////
////                            } else {
////                                Canshutype = Canshutype + mapstr.get(s) + ",";
////                            }
////                        }
////                        String userid = GApplication.user.getYonghuZhanghao();// 获取账号名称
//                        String netResutl = new SelectATMByBlackMoneyBoxCleanService().ReturnATMSelectList("", "", "");
//                        Log.e(TAG, "测试=====" + netResutl.toString());
//                        if (!netResutl.equals("[]") || netResutl == null || !netResutl.equals("") || !netResutl.equals("anyType{}")) {
//                            Gson gson = new Gson();
//
////                            baggingForCashEntity = gson.fromJson(netResutl, TurnNoverBoxEntity[].class);
////                            List<TurnNoverBoxEntity> listoCheck = Arrays.asList(baggingForCashEntity);
////                            List arrList = new ArrayList(listoCheck);
////                            lininfolist.addAll(arrList);
//                            if (null == null) {
//                                handler.sendEmptyMessage(3);
//                            } else {
//                                handler.sendEmptyMessage(6);
//                            }
//                        } else {
//                            handler.sendEmptyMessage(3);
//                        }
//
//                    }
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "**===" + e);
//                    handler.sendEmptyMessage(0);
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "**===" + e);
//                    handler.sendEmptyMessage(3);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "***===" + e);
//                    handler.sendEmptyMessage(1);
//                }
//            }
//
//        }.start();

    }

    private void initView() {
        recycleviewbybox = (ListView) findViewById(R.id.recycleviewbybox);
        select_atm_imageView1 = (ImageView) findViewById(R.id.select_atm_imageView1);
        select_atm_imageView1.setOnClickListener(this);
        select_atm_countback = findViewById(R.id.select_atm_countback);
        select_atm_countback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_atm_imageView1:
                break;

            case R.id.select_atm_countback:
                SelectATMByBlcakMoneyBoxCountDoActivity.this.finish();
                break;

        }
    }
}
