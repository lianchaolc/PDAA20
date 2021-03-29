package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinChaiXiang;
import com.ljsw.tjbankpad.baggingin.activity.cash.entity.BaggingForCashEntity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.server.TureOverBoxCheckServer;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnNoverBoxEntity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnoverBoxchecketLineinfoEntity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnverboxcheckineinfoadapter.TurnoverBoxCheckLinadapter;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.BaseActivity;
import com.ljsw.tjbankpda.qf.entity.User;
import com.manager.classs.pad.ManagerClass;

import java.net.SocketTimeoutException;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 2021128
 *
 * 周转箱子  核对
 * 1获取线路明细  传操作人
 * 2点击线路获取周转箱 按钮传递  提交
 * 页面3   线路获取明细页面
 *
 */
public class GetTurnoverboxchecketLineinfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "GetTurnoverboxchecketLineinfoActivity";
    private ListView getturnListview;
    private TurnoverBoxCheckLinadapter turnoverBoxCheckLinadapter;//  适配器
    private Button upd;
    private ImageView iv_black;
    private List<TurnNoverBoxEntity> lininfolist = new ArrayList<TurnNoverBoxEntity>();
    private ManagerClass manageryuan;
    private View.OnClickListener OnClick1;
    private ManagerClass manager;
    private TextView gtnovebox_user;// 操作人
    private JsonArray baggingarray = new JsonArray();


    private Button gtnovebox_update;//  更新
    private ImageView black;
    private CheckBox ck_dizhiyapin;//  抵质押品 check
    private CheckBox ck_zongkong;//  中控
    private CheckBox ck_xianjin;//  现金

    private String ckxianjin = "";
    private String ckzhongkong = "";
    private String ckdizhiyapin = "";
    private List<String> listtypestr = new ArrayList<>();
    private Map<String, String> mapstr = new HashMap<>();
    private Button btn_seclect;
    private TextView tv_boxcount;
    private TextView tv_netpoint;
    private String selectType = "";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(GetTurnoverboxchecketLineinfoActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(GetTurnoverboxchecketLineinfoActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:

                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    break;

                case 3:

                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
//                     设置数据元
                    turnoverBoxCheckLinadapter = new TurnoverBoxCheckLinadapter(lininfolist, GetTurnoverboxchecketLineinfoActivity.this);
                    getturnListview.setAdapter(turnoverBoxCheckLinadapter);
                    if (null != turnoverBoxCheckLinadapter) {
                        turnoverBoxCheckLinadapter.notifyDataSetChanged();
                    }


                    break;

                case 7:
                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, " 请选择一个类型做为筛选条件 ", Toast.LENGTH_SHORT).show();
                default:


                    break;


            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnoverboxchecklin);
        InitView();
        manageryuan = new ManagerClass();
        loadData();
//        LoadData1();
        manager = new ManagerClass();
        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                LoadData1();
            }
        };
    }

    private String reusltCardnumber = "";
    TurnNoverBoxEntity[] baggingForCashEntity = null;

    private void LoadData1() {
        selectType = "";
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    lininfolist.clear();

                    if (ckxianjin.equals("") && ckzhongkong.equals("") && ckdizhiyapin.equals("")) {
                        handler.sendEmptyMessage(7);
                        return;
                    } else {
                        String Canshutype = "";
                        for (String s : mapstr.keySet()) {
                            System.out.println("key : " + s + " value : " + mapstr.get(s));
                            if (("").equals(mapstr.get(s))) {

                            } else {
                                Canshutype = Canshutype + mapstr.get(s) + ",";
                            }
                        }
                        String Canshutype1 = Canshutype.substring(0, Canshutype.length() - 1);
                        String userid = GApplication.user.getYonghuZhanghao();// 获取账号名称
                        selectType = Canshutype1;
                        reusltCardnumber = new TureOverBoxCheckServer().getLineList(Canshutype1, userid);
                        Log.e(TAG, "测试=====" + reusltCardnumber.toString());
                        if (!reusltCardnumber.equals("[]") || reusltCardnumber == null || !reusltCardnumber.equals("") || !reusltCardnumber.equals("anyType{}")) {
                            Gson gson = new Gson();
                            Log.e(TAG, "测试数据源=====" + reusltCardnumber.toString());

                            baggingForCashEntity = gson.fromJson(reusltCardnumber, TurnNoverBoxEntity[].class);
                            List<TurnNoverBoxEntity> listoCheck = Arrays.asList(baggingForCashEntity);
                            List arrList = new ArrayList(listoCheck);
                            lininfolist.addAll(arrList);
                            if (null == lininfolist) {
                                handler.sendEmptyMessage(3);
                            } else {
                                handler.sendEmptyMessage(6);
                            }
                        } else {
                            handler.sendEmptyMessage(3);
                        }

                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e(TAG, "**===" + e);
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(TAG, "**===" + e);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "***===" + e);
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadData();
    }

    String linno = "";
    String boxstr = "";

    private void loadData() {
        gtnovebox_user = (TextView) findView(R.id.gtnovebox_user);
//        gtnovebox_user.setText("" + GApplication.user.getLoginUserName());

        getturnListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                linno = lininfolist.get(position).getLineNum();
                boxstr = lininfolist.get(position).getPassBoxNums();
                Intent intent = new Intent
                        (GetTurnoverboxchecketLineinfoActivity.this,
                                turnoverboxcheckActivity.class);
                intent.putExtra("linno", linno);
                intent.putExtra("boxstr", boxstr);
                intent.putExtra("selectType", selectType);
                startActivity(intent);


            }
        });
    }

    /***
     * 组件初始化
     */

    private void InitView() {
        tv_boxcount = findViewById(R.id.tv_001);
        tv_boxcount.setText("");
        tv_netpoint = findViewById(R.id.tv_002);
        tv_netpoint.setText("");
        btn_seclect = (Button) findViewById(R.id.btn_seclect);
        btn_seclect.setOnClickListener(this);
        getturnListview = (ListView) findViewById(R.id.listview_gtnovebox);
        gtnovebox_update = (Button) findViewById(R.id.gtnovebox_update);
        gtnovebox_update.setOnClickListener(this);
        black = (ImageView) findViewById(R.id.gtnovebox_back);
        black.setOnClickListener(this);
        ck_dizhiyapin = (CheckBox) findViewById(R.id.ck_dizhiyapin);
        ck_dizhiyapin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, "\n" +
                            "您选中了" + "抵质押品", Toast.LENGTH_SHORT).show();
                    ckdizhiyapin = "ckdizhiyapin";
                    mapstr.put(ckdizhiyapin, "3");
                } else {
                    ckdizhiyapin = "";
//                    mapstr.put(ckdizhiyapin, "");
                    mapstr.remove("ckdizhiyapin");
                }
            }
        });


        ck_zongkong = (CheckBox) findViewById(R.id.ck_zongkong);
        ck_zongkong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, "\n" +
                            "您选中了中控", Toast.LENGTH_SHORT).show();
                    ckzhongkong = "ckzhongkong";
                    mapstr.put(ckzhongkong, "2");
                } else {
                    ckzhongkong = "";
                    mapstr.remove("ckzhongkong");
                }
            }
        });
        ck_xianjin = (CheckBox) findViewById(R.id.ck_xianjin);
        ck_xianjin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(GetTurnoverboxchecketLineinfoActivity.this, "\n" +
                            "您选中现金", Toast.LENGTH_SHORT).show();
                    ckxianjin = "ckxianjin";
                    mapstr.put(ckxianjin, "1");
                } else {
                    ckxianjin = "";
                    mapstr.put(ckxianjin, "");
                    mapstr.remove("ckxianjin");
                }
            }
        });
    }

    @Override
    protected void setContentView() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.gtnovebox_back:
                GetTurnoverboxchecketLineinfoActivity.this.finish();
                break;

            case R.id.btn_seclect:
                LoadData1();
                break;
            case R.id.gtnovebox_update:
                LoadData1();
                break;
        }
    }
}



