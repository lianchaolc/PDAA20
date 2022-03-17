package com.ljsw.pdachecklibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryUpDataAdapter;
import com.ljsw.pdachecklibrary.checklibraryservice.CheckLirabryService;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryScanEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryScannerEney;
import com.ljsw.pdachecklibrary.utilsscanbychecklibrary.UtilsScanbyCheckLibrary;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanningbyDiZhi;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.OutLibraryToAccountCenterFingerActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.entity.DetailList;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.manager.classs.pad.ManagerClass;
import com.strings.tocase.CaseString;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import a20.cn.uhf.admin.RfidAdmin;
import hdjc.rfid.operator.RFID_Device;

/***
 * 数据提交扫描页面提交数据
 * 2021.8.26
 * 已经放在子线程中了
 */
public class CheckLibraryUpdataActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = "CheckLibraryUpdataActivity";
    private List<CheckLibraryScanEntity> scanlist = new ArrayList<>();
    private List<CheckLibraryScannerEney> intlist = new ArrayList<>();
    private ListView listview_checklibraryupata;
    private Button btn_updatastop;// 停止扫描
    private Button checlibraryupdatabtn;//  提交数据
    private CheckLibraryUpDataAdapter upDataAdapter;
    private Button check_libraryupdata_update;// 更新

    private UtilsScanbyCheckLibrary UtilsScan;
    private ManagerClass manager;

    private ImageView check_libraryupdata_back;//返回
    private RFID_Device rfid;
    private Context mContext;
    private View.OnClickListener OnClick1;
    private RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    private String strlocationpoion;// 格子编号
    String TaskNo="";//  传递的任务号
    Dialog dialogfa;
    Dialog dialogSuccess;
    private TextView tv_checklibraryupdatapointno;// 显示格子号
    private TextView tv_checklibraryupdatauser;

    private String  geduanno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_library_updata);
        strlocationpoion = getIntent().getStringExtra("intenttable");
        intlist.clear();
        intlist = (List<CheckLibraryScannerEney>) getIntent().getSerializableExtra("unScanlist");
        TaskNo=getIntent().getStringExtra("taskNo");
        geduanno=  getIntent().getStringExtra("geduan");
//        RfidAdmin.booleanstrnewThread = true;
        RfidAdmin r=new RfidAdmin();
        r.booleanstrnewThread=true;
        UtilsScan = new UtilsScanbyCheckLibrary();
        UtilsScan.setHandler(handler);
        manager = new ManagerClass();
        initView();
        loadData();
        Loadlocaldata();
    }

    private void loadData() {
        scanlist.clear();
        for (int i=0; i<intlist.size(); i++){
            CheckLibraryScanEntity   entity =new CheckLibraryScanEntity();
            entity.setDZNo(intlist.get(i).getSTOCKCODE());
            entity.setDZPOin(intlist.get(i).getPARTITIONNUMBER());
            scanlist.add(entity);
        }
    }

    private void Loadlocaldata() {

        upDataAdapter = new CheckLibraryUpDataAdapter(scanlist, CheckLibraryUpdataActivity.this);
        listview_checklibraryupata.setAdapter(upDataAdapter);

    }

    private void initView() {
        listview_checklibraryupata = (ListView) findViewById(R.id.listview_checklibraryupata);// 组件listvie
        btn_updatastop = (Button) findViewById(R.id.btn_updatastop);// 停止扫描
        btn_updatastop.setOnClickListener(this);
        checlibraryupdatabtn = findViewById(R.id.checlibraryupdatabtn);// 提交数据
        checlibraryupdatabtn.setOnClickListener(this);

        check_libraryupdata_update = findViewById(R.id.check_libraryupdata_update);
        check_libraryupdata_update.setOnClickListener(this);
        check_libraryupdata_back = (ImageView) findViewById(R.id.check_libraryupdata_back);//  返回
        check_libraryupdata_back.setOnClickListener(this);
        tv_checklibraryupdatapointno=(TextView) findViewById(R.id.tv_checklibraryupdatapointno);
        tv_checklibraryupdatapointno.setText(strlocationpoion+"-"+geduanno);

        tv_checklibraryupdatauser=findViewById(R.id.tv_checklibraryupdatauser);
        tv_checklibraryupdatauser.setText(""+GApplication.loginname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_updatastop:
                ControllerRfid();

                break;
            case R.id.checlibraryupdatabtn:
                if(btn_updatastop.getText().equals("开始扫描")){

                    getRfid().close_a20();
                    UpDataCheckLibraryResutlt();
                }else{

                    Toast.makeText(CheckLibraryUpdataActivity.this,"请停止扫描在提交",
                            400).show();
                }
//                }

                break;
            case R.id.check_libraryupdata_update:// 更新

                break;

            case R.id.check_libraryupdata_back://  返回
                BlackAction();
            default:
                break;
        }
    }

    /***
     * 回退 操作 关闭页面关闭rfid 扫描
     */
    private void BlackAction() {
        CheckLibraryUpdataActivity.this.finish();
        if (rfid != null) {
            getRfid().close_a20();
        }
    }

    private String Undatastr="";
    private void UpDataCheckLibraryResutlt() {
        manager.getRuning().runding(CheckLibraryUpdataActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    GetUpdata();
                    if(TaskNo==null||strupdata==null||TaskNo.equals("")||strupdata.equals("")){
                        Log.e(TAG, "测试" + strupdata+"==="+strupdata);
                        handler.sendEmptyMessage(4);
                    }else{


                    Undatastr = new CheckLirabryService().upChecklibraryScanData(TaskNo,strupdata, number);
                    Log.e(TAG, "测试" + Undatastr);
                    // 返回的类型anyType{}需要进行判断
//                    unScanlist.clear();
                    if (Undatastr != null && !Undatastr.equals("anyType{}")) {
                        Gson gson = new Gson();
//                        unScanlist.clear();// 每次进入后清除
//                        Type type = new TypeToken<ArrayList<String>>() {
//                        }.getType();
//                        List<String> listPrint = gson.fromJson(UnScanResult,
//                                type);
//                        unScanlist = listPrint;

                        handler.sendEmptyMessage(5);

                    } else {
                        handler.sendEmptyMessage(6);
                    }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(6);
                }
            }

        }.start();


    }

    /***
     * 拿到上传的数据
     */
    String  strupdata="";
    private void GetUpdata() {
        String newupstr="";

        for(int j=0; j<scanlist.size(); j++){
            if(null!=scanlist.get(j).getDZState()){

            if(scanlist.get(j).getDZState().equals("1")){
                newupstr=scanlist.get(j).getDZNo()+","+newupstr;


            }
            }else {
                Log.d(TAG, "去除重复");
            }
        }
        strupdata=newupstr.substring(0,newupstr.length()-1);
        Log.d(TAG, "查看字符串"+strupdata);
    }

    /****
     * 关闭扫描
     */
    private void ControllerRfid() {
        if(rfid!=null){



        }
        if(btn_updatastop.getText().equals("暂停")){
            btn_updatastop.setText("开始扫描");
            rfid.close_a20();
            return;
        }else if (btn_updatastop.getText().equals("开始扫描")){
            rfid.open_a20();
            btn_updatastop.setText("暂停");
            getRfid().addNotifly(UtilsScan);
            new Thread() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    super.run();
                    getRfid().open_a20();
                    try {
                        Log.d(TAG, "线程启动======Qingling zhuangxiang");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d(TAG, "线程启动异常" + e);
                    }
                }
            }.start();

        }

    }

    @Override
    protected void onResume() {
//        RfidAdmin.booleanstrnewThread = true;
        loadData();
        super.onResume();
        getRfid().addNotifly(UtilsScan);
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                getRfid().open_a20();
                try {
                    Log.d(TAG, "线程启动======Qingling zhuangxiang");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d(TAG, "线程启动异常" + e);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 776:
                    new Thread(new Runnable() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void run() {
                            Log.d("DIZHIfragment", "======InventoryThread====+handler0=" + Thread.currentThread().getName());
                            // 循环扫描后网络请求后的结果为0时 将抵质邀您的状态设置为 完成
                            Bundle bnewthread = msg.getData();

                            String numbernewthread = bnewthread.getString("number");

                            String str = CaseString.getBoxNumbycollateral(numbernewthread);
                            if (null == str || str.equals("")) {
                                Log.d(TAG, "不对比");
                            } else {
                                Log.d(TAG, "对比数据");

//                                for (int i = 0; i < scanlist.size(); i++) {
//                                    if (scanlist.get(i).getDZNo().equals(str)) {
//                                        scanlist.get(i).setDZState("1");
//                                        Log.d(TAG, "空" + scanlist.get(i).getDZNo());
//                                    }
//                                }

                                for (int k=0;k<scanlist.size(); k++)
                                {

                                    if(o_Application.numberlist.contains(scanlist.get(k).getDZNo())){
                                        scanlist.get(k).setDZState("1");
                                    }

                                }
                            }
                        }
                    }).start();

                    break;

                case 777:// 这里特别主注意 子线程传递数据
                    Log.d("DIZHIfragment", "======InventoryThread====+handler1" + Thread.currentThread().getName());

                if(upDataAdapter!=null){
                    upDataAdapter.notifyDataSetChanged();
                }else{
                    upDataAdapter = new CheckLibraryUpDataAdapter(scanlist, CheckLibraryUpdataActivity.this);
                    listview_checklibraryupata.setAdapter(upDataAdapter);
                }


                    break;

                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryUpdataActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryUpdataActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();

//				new TurnListviewHeight(listview);
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryUpdataActivity.this, "没有任务",
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    manager.getAbnormal().remove();
                                }
                            });
                    break;
                case 4:
                    manager.getRuning().remove();
                    Toast.makeText(CheckLibraryUpdataActivity.this,"传递数据不正确，请从上一个面重做",Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    manager.getRuning().remove();
//                    Toast.makeText(CheckLibraryUpdataActivity.this,"提交数据成功",Toast.LENGTH_SHORT).show();
                    dialogSuccess = new Dialog(CheckLibraryUpdataActivity.this);
                    LayoutInflater inflaterSuccess = LayoutInflater.from(CheckLibraryUpdataActivity.this);
                    View vsuccewss = inflaterSuccess.inflate(R.layout.dialog_success, null);
                    Button butsu = (Button) vsuccewss.findViewById(R.id.success);
                    butsu.setText("提交成功");
                    dialogSuccess.setCancelable(false);
                    dialogSuccess.setContentView(vsuccewss);
                    if (butsu != null) {
                        butsu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                CheckLibraryUpdataActivity.this.finish();
                                dialogSuccess.dismiss();
                            }

                        });
                    }
                    if (!isFinishing()) {
                        dialogSuccess.show();
                    }


                    break;
                case 6:
                    manager.getRuning().remove();
                    dialogfa = new Dialog(CheckLibraryUpdataActivity.this);
                    LayoutInflater inflaterfa = LayoutInflater.from(CheckLibraryUpdataActivity.this);
                    View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
                    Button butfa = (Button) vfa.findViewById(R.id.success);
                    butfa.setText("失败了请重新尝试");
                    dialogfa.setCancelable(false);
                    dialogfa.setContentView(vfa);
                    if (butfa != null) {
                        butfa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                dialogfa.dismiss();
                            }

                        });
                    }
                    if (!isFinishing()) {
                        dialogfa.show();
                    }
                    break;
                default:
            }

        }

    };

    @Override
    public void onPause() {
        manager.getRuning().remove();
        super.onPause();
        if (rfid != null) {
            getRfid().close_a20();
        }
    }

    @Override
    public void onDestroy() {
        manager.getRuning().remove();
        // TODO Auto-generated method stub
        super.onDestroy();
        if (rfid != null) {
            getRfid().close_a20();

        }
        RfidAdmin r=new RfidAdmin();
        r.booleanstrnewThread=false;
//        RfidAdmin.booleanstrnewThread = false;
    }
}
