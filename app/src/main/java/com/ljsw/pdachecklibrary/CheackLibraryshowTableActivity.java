package com.ljsw.pdachecklibrary;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryAdapter;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryTableAdapter;
import com.ljsw.pdachecklibrary.checklibraryservice.CheckLirabryService;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryTableEntity;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinKuangJiaActivity;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinSaoMiaoZhiWenActivity;
import com.manager.classs.pad.ManagerClass;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/****
 * 展示 档案柜的每个柜子的缺失的数量
 */
public class CheackLibraryshowTableActivity extends AppCompatActivity {
    private static final String TAG = "CheackLibraryshowTableActivity";
    private ImageView check_librarytable_back;
    private TextView tv_controllerchecklibryuser;//  操作人
    private TextView cheachlibrary_taskno;//  r任务号
    private TextView cheachlibrary_losscounts;//缺失数量
    private TextView cheachlibrary_creattime;//  任务时间
    private ListView listview_showtable;
    private Button check_librarytable_update;// 更新

    private String cteatetime;
    private String taskNo;
    private String count;
    private List<CheckLibraryTableEntity> tablbelist = new ArrayList<CheckLibraryTableEntity>();//数据源
    private CheckLibraryTableAdapter checkLibraryTableAdapter;
    private ManagerClass manager;
    private View.OnClickListener OnClick1;
    private View.OnClickListener OnClick2;
    String str = "";
    private Button cheachlibrary_finishtask;
    private Dialog dialogforreturnaccountinten;
    private  String  pancha="";
    private  TextView check_librarytable_textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheack_libraryshow_table);
        taskNo = getIntent().getStringExtra("tasknum");
        count = getIntent().getStringExtra("taskcount");
        cteatetime = getIntent().getStringExtra("createtime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        pancha= getIntent().getStringExtra("pancha");


        String s = sdf.format(new Date());
        str = cteatetime.substring(0, 9);
        try {
            Date date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        manager = new ManagerClass();
        initView();
        loadData();
        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                GetCheckListbaryTask();
            }
        };
        OnClick2 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                FinashTask();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCheckListbaryTask();
        Log.d(TAG, "taskNo" + taskNo);
        Log.d(TAG, "count" + count);
        Log.d(TAG, "cteatetime" + cteatetime);

    }

    private void loadData() {
        tablbelist.clear();
        listview_showtable = (ListView) findViewById(R.id.listview_showtable);
        checkLibraryTableAdapter = new CheckLibraryTableAdapter(tablbelist, CheackLibraryshowTableActivity.this);
        listview_showtable.setAdapter(checkLibraryTableAdapter);
        listview_showtable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intenttable = new Intent(CheackLibraryshowTableActivity.this, CheckLibraryLatticeActivity.class);
                String intTableNo = taskNo;
                String lattice = tablbelist.get(position).getCABINETNUMBER() + "-" + tablbelist.get(position).getFACENUMBER();
                intenttable.putExtra("Tableno", intTableNo);
                intenttable.putExtra("Lattice", lattice);
                startActivity(intenttable);
            }
        });
    }

    private void initView() {
        check_librarytable_back = (ImageView) findViewById(R.id.check_librarytable_back);
        check_librarytable_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheackLibraryshowTableActivity.this.finish();
            }
        });

        tv_controllerchecklibryuser = (TextView) findViewById(R.id.tv_controllerchecklibryuser);
        tv_controllerchecklibryuser.setText("" + GApplication.loginUsername);
        cheachlibrary_taskno = (TextView) findViewById(R.id.cheachlibrary_taskno);// 任务号
        cheachlibrary_taskno.setText(taskNo);
        cheachlibrary_losscounts = findViewById(R.id.cheachlibrary_losscounts);// 缺失数量
        cheachlibrary_losscounts.setText(count);
        cheachlibrary_creattime = findViewById(R.id.cheachlibrary_creattime);
        cheachlibrary_creattime.setText(str + "");
        check_librarytable_update = findViewById(R.id.check_librarytable_update);
        check_librarytable_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCheckListbaryTask();
            }
        });
//        点击这个就是结束任务

        cheachlibrary_finishtask = findViewById(R.id.cheachlibrary_finishtask);
        if(null==pancha||pancha.equals("")){
         cheachlibrary_finishtask.setVisibility(View.GONE);
        }else if (pancha.equals("盘查")){
            cheachlibrary_finishtask.setVisibility(View.VISIBLE);
        }
        check_librarytable_textView1=(TextView)findViewById(R.id.check_librarytable_textView1);
        if(null==pancha||pancha.equals("")){
            check_librarytable_textView1.setText("补扫");
        }else if (pancha.equals("盘查")){
            check_librarytable_textView1.setText("盘查库");
        }
        cheachlibrary_finishtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinashTask();
            }
        });

    }

    /***
     * 结束当前任务只有盘查库存在
     */
    private void FinashTask() {
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String ControllerNo = GApplication.loginname;
                    Log.e(TAG, " ControllerNo" + ControllerNo);
                    Log.e(TAG, "taskNo" + taskNo);
                    ReturnNetResutl = new CheckLirabryService().finishTask(taskNo, ControllerNo);
                    Log.e(TAG, "测试" + ReturnNetResutl);
                    // 返回的类型anyType{}需要进行判断
                    if (ReturnNetResutl != null && !ReturnNetResutl.equals("anyType{}")) {
                        Gson gson = new Gson();
                        if (tablbelist == null || tablbelist.size() == 0) {
                            handler.sendEmptyMessage(4);
                        } else {
                            handler.sendEmptyMessage(6);
                        }


                    } else {
                        handler.sendEmptyMessage(5);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(5);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                }
            }

        }.start();
    }


    /***
     * 获取格子的未知和缺失数量的
     *
     * http://361de15631.wicp.vip/cash/webservice/pda/countListMissingCollateralByTask?arg1=DZ9880500002021010600001&arg2=100029
     */

    private String ReturnNetResutl = "";

    private void GetCheckListbaryTask() {


        manager.getRuning().runding(CheackLibraryshowTableActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String ControllerNo = GApplication.loginname;
                    String strTable = taskNo;
                    Log.e(TAG, " ControllerNo" + ControllerNo);
                    Log.e(TAG, "strTable" + strTable);
                    ReturnNetResutl = new CheckLirabryService().CheckLibrarybyTable(strTable, ControllerNo);
                    Log.e(TAG, "测试" + ReturnNetResutl);
                    // 返回的类型anyType{}需要进行判断
                    if (ReturnNetResutl != null && !ReturnNetResutl.equals("anyType{}")) {
                        Gson gson = new Gson();
                        tablbelist.clear();// 每次进入后清除
                        Type type = new TypeToken<ArrayList<CheckLibraryTableEntity>>() {
                        }.getType();
                        List<CheckLibraryTableEntity> listPrint = gson.fromJson(ReturnNetResutl,
                                type);
                        tablbelist = listPrint;
                        if (tablbelist == null || tablbelist.size() == 0) {
                            handler.sendEmptyMessage(3);
                        } else {
                            handler.sendEmptyMessage(2);
                        }


                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();
    }

    /***
     * 网络请求数据处理结果
     */

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheackLibraryshowTableActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheackLibraryshowTableActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
                    checkLibraryTableAdapter = new CheckLibraryTableAdapter(tablbelist, CheackLibraryshowTableActivity.this);
                    listview_showtable.setAdapter(checkLibraryTableAdapter);
                    checkLibraryTableAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheackLibraryshowTableActivity.this, "没有任务", new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;
                case 4:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheackLibraryshowTableActivity.this, "加载超时,重试?", OnClick2);
                    break;
                case 5:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheackLibraryshowTableActivity.this, "网络连接失败,重试?", OnClick2);
                    break;
                case 6:
//                    提交成功跳转首页

                       dialogforreturnaccountinten = new Dialog(CheackLibraryshowTableActivity.this);
                    LayoutInflater inflaterforreturnaccountinten = LayoutInflater
                            .from(CheackLibraryshowTableActivity.this);
                    View vforreturnaccountinten = inflaterforreturnaccountinten.inflate(R.layout.dialog_success, null);
                    Button butforreturnaccountinten = (Button) vforreturnaccountinten.findViewById(R.id.success);
                    butforreturnaccountinten.setText("提交成功");
                    dialogforreturnaccountinten.setCancelable(false);
                    dialogforreturnaccountinten.setContentView(vforreturnaccountinten);
                    if (butforreturnaccountinten != null) {
                        butforreturnaccountinten.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                dialogforreturnaccountinten.dismiss();
                                // 跳转下一个页面

                                CheackLibraryshowTableActivity.this.finish();
                            }

                        });
                    }
                    if (!isFinishing()) {
                        dialogforreturnaccountinten.show();
                    }

                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.getRuning().remove();
    }
}
