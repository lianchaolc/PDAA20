package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.HomeMangeerToCenterDataScanActivity;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanning;
import com.ljsw.tjbankpad.baggingin.activity.cash.BaggingActivitySend;
import com.ljsw.tjbankpad.baggingin.activity.cash.service.BaggingForCashService;
import com.ljsw.tjbankpad.baggingin.activity.handover.HomeMangerToCleanHandoverActivity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.server.TureOverBoxCheckServer;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.urils.Scanutils_TurnOver;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.DiZhiYaPInZhuangXiangActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.yy.application.SaomiaoUtil;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.pda.BoxDoDetail;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hdjc.rfid.operator.RFID_Device;

/**
 * 周转箱检查核对扫描代码
 */
public class turnoverboxcheckActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "turnoverboxcheckActivity";
    private Scanutils_TurnOver getnumber;// 抵质押品的新的扫描规则重新截取的字符串长度
    private Button turncheckbox_updatejiaojie;

    private ImageView turncheckbox_back;//  按钮返回

    private TextView homepackger_tvlefe;  //  未扫描代码

    private TextView turncheckbox_right; //  已经扫描
    private ListView turncheckboxlefthometoclean_listview;

    private ListView turncheckboxrighthometoclean_listview;

    private Button turncheckbox_updata_info; //  交接

    private Button turncheckbox_info;//  明细
    private ManagerClass manageryuan;
//    private View.OnClickListener OnClick1;


    private LeftAdapter ladapter;
    private RightAdapter radapter;
    private ManagerClass manager;
    private ManagerClass manageractivity;
    private RFID_Device rfid;
    private List<String> boxUNscanlist = new ArrayList<>();
    private List<String> boxUNscanlist2 = new ArrayList<>();
    String linno = "";

    private RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    private Dialog dialog;// 成功
    private Dialog dialogfa;// 失败
    private Button turncheckbox_cancle1;
    private View.OnClickListener OnClick1;
    private String selectType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnoverboxcheck);
        linno = getIntent().getStringExtra("linno");
        String boxstr = getIntent().getStringExtra("boxstr");
         selectType=getIntent().getStringExtra("selectType");
//          处理字符串并拆分
        String a[] = boxstr.split(",");
        ArrayList b = new ArrayList();
        for (int i = 0; i < a.length; i++) {
            boxUNscanlist.add(a[i]);
            boxUNscanlist2.add(a[i]);
        }
        o_Application.qinglingruku.clear();
        o_Application.qinglingruku.add(new QingLingRuKu(null,
                null, boxUNscanlist));

        o_Application.qlruku = o_Application.qinglingruku.get(0);
//        boxUNscanlist2.addAll(o_Application.qlruku.getZhouzhuanxiang());
        ladapter = new LeftAdapter();
        radapter = new RightAdapter();
        manageryuan = new ManagerClass();
        manageractivity = new ManagerClass();
        manager = new ManagerClass();
        turncheckbox_updatejiaojie = (Button) findViewById(R.id.turncheckbox_updatejiaojie);
        turncheckbox_updatejiaojie.setOnClickListener(this);
        turncheckbox_back = (ImageView) findViewById(R.id.turncheckbox_back);
        turncheckbox_back.setOnClickListener(this);
//        未扫描
        homepackger_tvlefe = (TextView) findViewById(R.id.turncheckbox_tvleft);
//  已经扫描
        turncheckbox_right = (TextView) findViewById(R.id.turncheckbox_right);

        turncheckboxlefthometoclean_listview = findViewById(R.id.turncheckboxlefthometoclean_listview);
        turncheckboxrighthometoclean_listview = findViewById(R.id.turncheckboxrighthometoclean_listview);
        turncheckbox_info = (Button) findViewById(R.id.turncheckbox_info);
        turncheckbox_info.setOnClickListener(this);
        turncheckbox_updata_info = findViewById(R.id.turncheckbox_updata_info);
        turncheckbox_updata_info.setOnClickListener(this);
        getnumber = new Scanutils_TurnOver();
        getnumber.setHandler(handler);
        turncheckbox_cancle1 = findViewById(R.id.turncheckbox_cancle1);
        turncheckbox_cancle1.setOnClickListener(this);
        turncheckbox_cancle1.setEnabled(true);

        /*****
         * 点击获取网络请求
         */
        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manageryuan.getAbnormal().remove();
                UpDataInfoActopn();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        o_Application.guolv.clear();
        System.out.print(boxUNscanlist2.size() + "======");
        o_Application.qlruku.getZhouzhuanxiang().clear();
        if(0==o_Application.qlruku.getZhouzhuanxiang().size()){
            o_Application.qlruku.getZhouzhuanxiang().addAll(boxUNscanlist2);
//            o_Application.qlruku = o_Application.qinglingruku.get(0);
        }
        getRfid().addNotifly(getnumber);
        o_Application.numberlist.clear();
        new Thread() {
            @Override
            public void run() {
                super.run();
            getRfid().open_a20();
            }
        }.start();
        homepackger_tvlefe.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
        turncheckbox_right.setText("" + o_Application.numberlist.size());
        turncheckboxlefthometoclean_listview.setAdapter(ladapter);
        turncheckboxrighthometoclean_listview.setAdapter(radapter);
        ladapter.notifyDataSetChanged();
        radapter.notifyDataSetChanged();
        if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
            turncheckbox_updata_info.setEnabled(false);
            turncheckbox_updata_info.setBackgroundResource(R.drawable.button_gray);
        }
        if (o_Application.numberlist.size() == 0) {
            turncheckbox_cancle1.setEnabled(true);
            turncheckbox_cancle1.setBackgroundResource(R.drawable.button_gray);

        }
    }

    /***
     * 提交数据代码
     */

    private String reusltCardnumber = "";// wagnluo daima

    private void UpDataInfoActopn() {

        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    String UserId = GApplication.user.getLoginUserId();
                    String linNO = linno;
                    if (UserId.equals("") || linNO.equals("")) {
                        handler.sendEmptyMessage(3);
                    } else {
                        reusltCardnumber = new TureOverBoxCheckServer().clearingCheck(GApplication.user.getLoginUserId(), linno);
                        if (reusltCardnumber.equals("成功")) {
                            Gson gson = new Gson();
                            Log.e(TAG, "测试数据源=====" + reusltCardnumber.toString());
                            handler.sendEmptyMessage(6);
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


    class LeftAdapter extends BaseAdapter {
        LeftHolder lh;
        LayoutInflater lf = LayoutInflater.from(turnoverboxcheckActivity.this);

        @Override
        public int getCount() {
            return o_Application.qlruku.getZhouzhuanxiang().size();
        }

        @Override
        public Object getItem(int arg0) {
            return o_Application.qlruku.getZhouzhuanxiang().get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                lh = new LeftHolder();
                arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_left, null);
                lh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_left_text);
                arg1.setTag(lh);
            } else {
                lh = (LeftHolder) arg1.getTag();
            }
            lh.tv.setText(o_Application.qlruku.getZhouzhuanxiang().get(arg0));
            return arg1;
        }

    }

    public static class LeftHolder {
        TextView tv;
    }

    class RightAdapter extends BaseAdapter {
        RightHolder rh;
        LayoutInflater lf = LayoutInflater.from(turnoverboxcheckActivity.this);

        @Override
        public int getCount() {
            return o_Application.numberlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return o_Application.numberlist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                rh = new RightHolder();
                arg1 = lf.inflate(R.layout.adapter_dizhisaomiao_right, null);
                rh.tv = (TextView) arg1.findViewById(R.id.adapter_dizhisaomiao_right_text);
                arg1.setTag(rh);
            } else {
                rh = (RightHolder) arg1.getTag();
            }
            rh.tv.setText(o_Application.numberlist.get(arg0));
            return arg1;
        }

    }

    public static class RightHolder {
        TextView tv;
    }

    @Override
    protected void onDestroy() {

        manageractivity.getRuning().remove();
        if (o_Application.numberlist.size() > 0) {
            o_Application.numberlist.clear();
        }
        if (o_Application.guolv.size() > 0) {
            o_Application.guolv.clear();
        }
        if (null == o_Application.qlruku) {

        } else {
            if (null != o_Application.qlruku.getZhouzhuanxiang() && o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
                o_Application.qlruku.getZhouzhuanxiang().clear();
            }
        }


        super.onDestroy();

        if (rfid != null) {
            getRfid().close_a20();
        }

        handler1.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rfid != null) {
            getRfid().close_a20();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (rfid != null) {
            getRfid().close_a20();
        }
        this.finish();
    }

    private Handler handler = new Handler() {
        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    turncheckbox_cancle1.setFocusable(true);
                    if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
                        getRfid().stop_a20();

                        turncheckbox_cancle1.setBackgroundResource(R.drawable.buttom_selector_bg);
                        turncheckbox_cancle1.setFocusable(true);
                    }

                    if (null != o_Application.numberlist) {
                        turncheckbox_cancle1.setFocusable(true);
                        turncheckbox_cancle1.setBackgroundResource(R.drawable.buttom_selector_bg);
                    }
                    // 扫描到有值得时候可以清除
                    if (o_Application.numberlist.size() > 0) {
                        turncheckbox_cancle1.setFocusable(true);
                        turncheckbox_cancle1.setBackgroundResource(R.drawable.buttom_selector_bg);
                    }
                    Log.e(TAG, "扫描到=== " + o_Application.numberlist.size());
                    turncheckbox_right.setText("" + o_Application.numberlist.size());
                    homepackger_tvlefe.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
                    Log.e(TAG, "网络===" + o_Application.qlruku.getZhouzhuanxiang().size());
                    ladapter.notifyDataSetChanged();
                    radapter.notifyDataSetChanged();
                    turncheckboxlefthometoclean_listview.setAdapter(ladapter);
                    turncheckboxrighthometoclean_listview.setAdapter(radapter);
                    // 判断扫描周转箱是否可以点击
                    if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
                        turncheckbox_updata_info.setEnabled(true);
                        turncheckbox_updata_info.setBackgroundResource(R.drawable.buttom_selector_bg);
                    }

                    break;

                case 3:

                    Toast.makeText(turnoverboxcheckActivity.this, "参数不正确!!!", Toast.LENGTH_SHORT).show();
                    break;

                case 6:
                    dialog = new Dialog(turnoverboxcheckActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(turnoverboxcheckActivity.this);
                    View v = inflater.inflate(R.layout.dialog_success, null);
                    Button but = (Button) v.findViewById(R.id.success);
                    but.setText("提交成功");
                    dialog.setCancelable(false);
                    dialog.setContentView(v);

                    but.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();
                            Intent i = new Intent(turnoverboxcheckActivity.this, GetTurnoverboxchecketLineinfoActivity.class);
                            startActivity(i);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            turnoverboxcheckActivity.this.finish();

                        }
                    });

                    dialog.show();
                    break;
                case 7:
                    dialog = new Dialog(turnoverboxcheckActivity.this);
                    LayoutInflater inflaterfaile = LayoutInflater.from(turnoverboxcheckActivity.this);
                    View vfaile = inflaterfaile.inflate(R.layout.dialog_success, null);
                    Button butfaile = (Button) vfaile.findViewById(R.id.success);
                    butfaile.setText("提交成功");
                    dialog.setCancelable(false);
                    dialog.setContentView(vfaile);

                    butfaile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                    break;
                default:
                    break;
            }
        }

    };

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(turnoverboxcheckActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(turnoverboxcheckActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manageractivity.getRuning().remove();

//				获取数据成功后进行扫描
//				cashtoPackgerUtils  = new CashtoPackgerUtils();
//				cashtoPackgerUtils.setHandler(handler);
                    // adapter.notifyDataSetChanged();
                    // dzyp_outlistview.setAdapter(adapter);
                    // new TurnListviewHeight(listview);

                    break;
                case 3:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(turnoverboxcheckActivity.this, "没有数据",
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    manageractivity.getAbnormal().remove();
                                }
                            });
                    break;
                case 4:
                    getnumber = new Scanutils_TurnOver();
                    getnumber.setHandler(handler);
                    manager.getRfid().addNotifly(getnumber);
                    o_Application.numberlist.clear();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Thread thread = new Thread();
                            try {
                                thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            manager.getRfid().open_a20();
                        }
                    }.start();
                    homepackger_tvlefe.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
                    turncheckbox_right.setText("" + o_Application.numberlist.size());
                    turncheckboxlefthometoclean_listview.setAdapter(ladapter);
                    turncheckboxrighthometoclean_listview.setAdapter(radapter);
                    ladapter.notifyDataSetChanged();
                    radapter.notifyDataSetChanged();
                    if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
                        turncheckbox_updata_info.setEnabled(false);
                        turncheckbox_updata_info.setBackgroundResource(R.drawable.button_gray);
                    }
                    if (o_Application.numberlist.size() == 0) {
                        turncheckbox_cancle1.setEnabled(true);
                        turncheckbox_cancle1.setBackgroundResource(R.drawable.button_gray);

                    }
                    ladapter.notifyDataSetChanged();
                    radapter.notifyDataSetChanged();
                    if (o_Application.qlruku.getZhouzhuanxiang().size() > 0) {
//                        btn_hometoclean_jiaojie.setEnabled(false);
//                        btn_hometoclean_jiaojie.setBackgroundResource(R.drawable.button_gray);
                    }
                    if (o_Application.numberlist.size() == 0) {
//                        btn_hometoclean_cancle.setEnabled(false);
//                        btn_hometoclean_cancle.setBackgroundResource(R.drawable.button_gray);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.turncheckbox_cancle1:

                turncheckbox_updata_info.setEnabled(false);
                turncheckbox_updata_info.setBackgroundResource(R.drawable.button_gray);

                if (o_Application.numberlist.size() == 0) {
                    turncheckbox_cancle1.setEnabled(false);
                    turncheckbox_cancle1.setBackgroundResource(R.drawable.button_gray);
                }
                setCleanList();

                handler1.sendEmptyMessage(4);
                handler.sendEmptyMessage(0);
                break;


            case R.id.turncheckbox_back:
                if (rfid != null) {
                    getRfid().close_a20();
                }
                turnoverboxcheckActivity.this.finish();
                break;
            case R.id.turncheckbox_updata_info:

                UpDataInfoActopn();


                break;

            case R.id.turncheckbox_updatejiaojie:
                UpDataInfoActopn();
                break;

            case R.id.turncheckbox_info:// 明细页面
                Intent i = new Intent(turnoverboxcheckActivity.this, TurnOverInfoActivity.class);
                i.putExtra("LineNo", linno);
                i.putExtra("selectType", selectType);
                startActivity(i);
            default:
                break;
        }
    }

    private void setCleanList() {
        o_Application.guolv.clear();
        o_Application.numberlist.clear();
        o_Application.qlruku.getZhouzhuanxiang().clear();
        o_Application.qlruku.getZhouzhuanxiang().addAll(boxUNscanlist2);
        o_Application.guolv.clear();

    }
}
