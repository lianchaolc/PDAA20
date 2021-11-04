package com.ljsw.pdachecklibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.application.GApplication;
import com.bigkoo.pickerview.TimePickerView;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.collateraladministratorsorting.activity.SelectTaskByCollateralActivity;
import com.ljsw.collateraladministratorsorting.entity.SelectTaskByCollateralEntity;
import com.ljsw.collateraladministratorsorting.selecttaskadapter.SelectTaskCollaterAdapter;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryAdapter;
import com.ljsw.pdachecklibrary.checklibraryservice.CheckLirabryService;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.BaseActivity;
import com.ljsw.tjbankpda.qf.entity.ShangJiaoQingFen_o_qf_Print_Entity;
import com.manager.classs.pad.ManagerClass;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 * 抵质押品库盘查库任务补扫选择任务
 * 角色29
 *2021.8.25
 */
public class CheckLibraryByCollMangerActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CheckLibraryByCollMangerActivity";
    private ListView listviewbycoll_manger;// 显示listview 电调点击跳转下一个页面
    private TextView showControllerUser;//  显示操作人
    private TextView tv_StrartTime;
    private TextView tv_EndTime;
    private Button btn_SearchTime;
    private TimePickerView pvstart;
    private TimePickerView pvTimeEnd;

    private Button checklibrarybydizhi_gtnovebox_update;// g更新
    //    变量
    private List<CheckLibraryEntity> datalist = new ArrayList<>();
    Calendar cal;
    private ManagerClass manager;
    Date d;
    private CheckLibraryAdapter checkLibraryAdapter;
    private View.OnClickListener OnClick1;
    private ImageView btnblack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_library_by_coll_manger);
        manager = new ManagerClass();
        cal = Calendar.getInstance();
        initView();
        LoadData();
//        GetCheckListbaryTask()

        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                GetCheckListbaryTask();
            }
        };

    }

    /***
     * 加载数据
     */
    private void LoadData() {
        datalist.clear();
//        CheckLibraryEntity checkLibraryEntity = new CheckLibraryEntity();
//        checkLibraryEntity.setCheckLibraryCreateTime("2021-11-22");
//        checkLibraryEntity.setChelibraryResutlCounts("34");
//        checkLibraryEntity.setCheLibraryTaskNO("DZ9880500002021041500001");
//        datalist.add(checkLibraryEntity);
//        CheckLibraryEntity checkLibraryEntity1 = new CheckLibraryEntity();
//        checkLibraryEntity1.setCheckLibraryCreateTime("2021-1-2");
//        checkLibraryEntity1.setChelibraryResutlCounts("30");
//        checkLibraryEntity1.setCheLibraryTaskNO("DZ9880500002021041500001");
//        datalist.add(checkLibraryEntity);
        checkLibraryAdapter = new CheckLibraryAdapter(datalist, CheckLibraryByCollMangerActivity.this);
        listviewbycoll_manger.setAdapter(checkLibraryAdapter);
        //跳转
        listviewbycoll_manger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intnetaction = new Intent(CheckLibraryByCollMangerActivity.this, CheackLibraryshowTableActivity.class);
                intnetaction.putExtra("tasknum", datalist.get(position).getId());
                intnetaction.putExtra("taskcount", datalist.get(position).getMissingNum());
                intnetaction.putExtra("createtime", datalist.get(position).getStarttime());
                startActivity(intnetaction);
            }
        });
    }

    private void initView() {
        listviewbycoll_manger = (ListView) findView(R.id.listviewbycoll_manger);
        showControllerUser = (TextView) findViewById(R.id.tv_controller);
        String userName = GApplication.user.getLoginUserName();
        showControllerUser.setText(userName + "");
        btn_SearchTime = (Button) findViewById(R.id.btn_selecttime);
        btn_SearchTime.setOnClickListener(this);
        tv_StrartTime = (TextView) findViewById(R.id.tv_starttime);
        tv_StrartTime.setOnClickListener(this);

        tv_EndTime = (TextView) findViewById(R.id.tv_endtime);
        tv_EndTime.setOnClickListener(this);
        checklibrarybydizhi_gtnovebox_update = (Button) findViewById(R.id.checklibrarybydizhi_gtnovebox_update);
        checklibrarybydizhi_gtnovebox_update.setOnClickListener(this);
        btnblack=(ImageView)findViewById(R.id.checklibrarybydizhi_gtnovebox_back);
        btnblack.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selecttime:
                GetCheckListbaryTask();
                break;
            case R.id.tv_starttime:
                getStartTiem();
                break;
            case R.id.tv_endtime:
                getEndTime();
                break;
            case R.id.checklibrarybydizhi_gtnovebox_update:
                GetCheckListbaryTask();
                break;
            case  R.id.checklibrarybydizhi_gtnovebox_back:
               CheckLibraryByCollMangerActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /***
     * 获取结束时间
     */
    private void getEndTime() {
        pvTimeEnd = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //设置标题
        pvTimeEnd.setTitle("选择日期");
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

//        pvTime.setRange(calendar.get(Calendar.YEAR) - 90, calendar.get(Calendar.YEAR));


        pvTimeEnd.setTime(new Date());
        //设置是否循环
        pvTimeEnd.setCyclic(false);
        //设置是否可以关闭
        pvTimeEnd.setCancelable(true);
        //设置选择监听
        pvTimeEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Log.e("MainActivity", "当前选择时间：" + getTime(date));
                tv_EndTime.setText(getTime(date));
            }
        });
        //显示
        pvTimeEnd.show();
    }

    /***
     * n拿到开始时间
     */
    private void getStartTiem() {
        pvstart = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //设置标题
        pvstart.setTitle("选择日期");
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

//        pvTime.setRange(calendar.get(Calendar.YEAR) - 90, calendar.get(Calendar.YEAR));


        pvstart.setTime(new Date());
        //设置是否循环
        pvstart.setCyclic(false);
        //设置是否可以关闭
        pvstart.setCancelable(true);
        //设置选择监听
        pvstart.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Log.e("MainActivity", "当前选择时间：" + getTime(date));
                tv_StrartTime.setText(getTime(date));
            }
        });
        //显示
        pvstart.show();

    }

    private void getSearchResultbyCheckLibrary() {
        GetCheckListbaryTask();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCheckListbaryTask();

    }

    /***
     * 网络请求数据 获取查库任务数据
     */
    private String CheckLibraryResult = "";
    String begintime = "";
    String endtime = "";

    private void GetCheckListbaryTask() {

        begintime = tv_StrartTime.getText().toString();//        开始时间
        endtime = tv_EndTime.getText().toString();//  结尾时间

        if(begintime.equals("开始时间")&&endtime.equals("结束时间")) {
            begintime = "";
            endtime = "";
            Log.d(TAG, "当前时间为null");
        }else  if(endtime.equals("结束时间")){
            endtime = "";
        }else if(begintime.equals("开始时间")){
            Log.d(TAG,"有时间选择我啥也不做");
            begintime = "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(begintime);
            Date d2 = sdf.parse( endtime);
            if(d2.before(d1)){
                Toast.makeText(CheckLibraryByCollMangerActivity.this,"结尾时间不能早于开始时间",400).show();
                endtime = "";
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        manager.getRuning().runding(CheckLibraryByCollMangerActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    CheckLibraryResult = new CheckLirabryService().getChceckLibraryNum( begintime, endtime,number);
                    Log.e(TAG, "测试==" + CheckLibraryResult);
                    // 返回的类型anyType{}需要进行判断
                    if (CheckLibraryResult != null && !CheckLibraryResult.equals("anyType{}")) {
                        Gson gson = new Gson();
                        datalist.clear();// 每次进入后清除
//                        CheckLibraryEntity CheckLibraryEntity = gson.fromJson(CheckLibraryResult, CheckLibraryEntity.class);

                        Type type = new TypeToken<ArrayList<CheckLibraryEntity>>() {
                        }.getType();
                        List<CheckLibraryEntity> listPrint = gson.fromJson(CheckLibraryResult,
                                type);
//                        Log.e(TAG, "测试===" + CheckLibraryEntity.toString());
                        datalist=listPrint;

                        handler.sendEmptyMessage(2);

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
     * DZ9880500002021010600001
     */

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            manager.getRuning().remove();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryByCollMangerActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryByCollMangerActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
                    checkLibraryAdapter = new CheckLibraryAdapter(datalist, CheckLibraryByCollMangerActivity.this);
                    listviewbycoll_manger.setAdapter(checkLibraryAdapter);
                    checkLibraryAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryByCollMangerActivity.this, "没有任务", new View.OnClickListener() {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.getRuning().remove();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.getRuning().remove();
    }
}
