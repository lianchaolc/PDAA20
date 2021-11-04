package com.ljsw.pdachecklibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryLatticeAdapter;
import com.ljsw.pdachecklibrary.adapterbycheck.CheckLibraryTableAdapter;
import com.ljsw.pdachecklibrary.checklibraryservice.CheckLirabryService;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryLatticeEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryScanEntity;
import com.ljsw.pdachecklibrary.entity.CheckLibraryEntity.CheckLibraryScannerEney;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.BaseActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.manager.classs.pad.ManagerClass;

import org.dom4j.Text;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/***
 *上一个页面柜子传递数据到此
 * 展示格子和格子缺失数量
 */
public class CheckLibraryLatticeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CLLattice";
    private TextView tv_checklibrarylatticeuser;//  操作人
    private TextView tv_showtableno;//  展示柜子号
    private TextView losscount_lattice;// 缺失数量
    private String tableno;
    private ListView showlistview;//
    private List<CheckLibraryLatticeEntity> latticeEntityList = new ArrayList<>();
    private CheckLibraryLatticeAdapter latticeAdapter;
    private List<String> unScanlist = new ArrayList<>();// 网络获取数据需要扫描的地址押品
    private List<CheckLibraryScanEntity> scanlist = new ArrayList<>();

    private ImageView check_librarytable_back;// 返回
    private Button check_librarytable_update;//  更新
    private ManagerClass manager;
    private View.OnClickListener OnClick1;
    private String intenttable;
    private String Lattice;
    List<CheckLibraryScannerEney> listPrint =new ArrayList<>();//  传递到下一页面的数据源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_library_lattice);
        intenttable = getIntent().getStringExtra("Tableno");
        Lattice = getIntent().getStringExtra("Lattice");

        Log.e(TAG, "intenttable==" + intenttable);
        Log.e(TAG, "Lattice===" + Lattice);
        manager = new ManagerClass();
        initView();
        LoadData();
        OnClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                UpdataAction();
            }
        };
    }

    Intent intent;
    String strgeduanno;
    private void LoadData() {
        showlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(CheckLibraryLatticeActivity.this, CheckLibraryUpdataActivity.class);
//拿到点击条目下的隔断号
                 strgeduanno = latticeEntityList.get(position).getGRIDNUMBER();

                if (null == strgeduanno || strgeduanno.equals("")) {

                } else {
                    GetScanData(strgeduanno);
                    Log.e(TAG, "长度" + unScanlist.size());

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdataAction();
    }


    private void initView() {
        tv_checklibrarylatticeuser = (TextView) findViewById(R.id.tv_checklibrarylatticeuser);
        tv_checklibrarylatticeuser.setText(GApplication.loginUsername);
        tv_showtableno = (TextView) findViewById(R.id.tv_showtableno);//  展示下柜子号
        tv_showtableno.setText("" + Lattice);
        showlistview = (ListView) findViewById(R.id.showlistview);
        check_librarytable_back = findViewById(R.id.check_librarytable_back);
        check_librarytable_back.setOnClickListener(this);
        //  返回
//        更新
        check_librarytable_update = (Button) findViewById(R.id.check_librarylattice_update);
        check_librarytable_update.setOnClickListener(this);


    }

    @Override
    protected void setContentView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_librarytable_back://   返回时候关闭页面
                CheckLibraryLatticeActivity.this.finish();
                break;

            case R.id.check_librarylattice_update://更新操作
                UpdataAction();
                break;
            default:
                break;
        }


    }

    /**
     * 8
     * 更新后操作
     */
    private String CheckLibraryResult;

    private void UpdataAction() {
        manager.getRuning().runding(CheckLibraryLatticeActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    String Taskno = intenttable;
                    String uplattice = Lattice;
                    CheckLibraryResult = new CheckLirabryService().CheckLibrarylattice(Taskno, uplattice, number);
                    Log.e(TAG, "测试" + CheckLibraryResult);
                    // 返回的类型anyType{}需要进行判断
                    if (CheckLibraryResult != null && !CheckLibraryResult.equals("anyType{}")) {
                        Gson gson = new Gson();
                        latticeEntityList.clear();// 每次进入后清除
                        Type type = new TypeToken<ArrayList<CheckLibraryLatticeEntity>>() {
                        }.getType();
                        List<CheckLibraryLatticeEntity> listPrint = gson.fromJson(CheckLibraryResult,
                                type);
                        latticeEntityList = listPrint;

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
     */

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            manager.getRuning().remove();
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryLatticeActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryLatticeActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();
                    latticeAdapter = new CheckLibraryLatticeAdapter(latticeEntityList,
                            CheckLibraryLatticeActivity.this, Lattice);
                    showlistview.setAdapter(latticeAdapter);
                    latticeAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CheckLibraryLatticeActivity.this, "没有任务", new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;
                case 4:
                    manager.getRuning().remove();
                    o_Application.qinglingruku.add(new QingLingRuKu(null,
                            null, unScanlist));
                    o_Application.qlruku = o_Application.qinglingruku.get(0);
                    intent.putExtra("unScanlist", ( Serializable) listPrint);
                    intent.putExtra("intenttable", intenttable);
                    String Taskno = intenttable;// 传递的任务号
                    intent.putExtra("taskNo", Taskno);
                    intent.putExtra("geduan", strgeduanno);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }


    };

    @Override
    protected void onDestroy() {
        manager.getRuning().remove();
        super.onDestroy();
        manager.getRuning().remove();
    }

    @Override
    protected void onPause() {
        manager.getRuning().remove();
        super.onPause();
        manager.getRuning().remove();
    }

    private String UnScanResult;

    /****
     * 获取扫描的抵质押品数据
     */
    private void GetScanData(final String strgeduanno) {
        manager.getRuning().runding(CheckLibraryLatticeActivity.this, "数据加载中...");
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    String number = GApplication.loginname;
                    String Taskno = intenttable;
                    String uplattice = Lattice;
                    String upgeduan = strgeduanno;
                    Log.d(TAG, "==" + number + "==" + Taskno + "==" + uplattice);
                    UnScanResult = new CheckLirabryService().listMissingCollateralByTaskAndFaceGridNumber(Taskno, uplattice, upgeduan, number);
                    Log.e(TAG, "测试" + UnScanResult);
                    // 返回的类型anyType{}需要进行判断
                    unScanlist.clear();
                    if (UnScanResult != null && !UnScanResult.equals("anyType{}")) {
                        Gson gson = new Gson();
                        unScanlist.clear();// 每次进入后清除
                        listPrint.clear();
                        Type type = new TypeToken<ArrayList<CheckLibraryScannerEney>>() {
                        }.getType();
                        listPrint = gson.fromJson(UnScanResult,
                                type);


                        CheckDZUnScanList(listPrint);

                        handler.sendEmptyMessage(4);

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
     * 把要扫描的数据和隔断分开
     */
    private List<CheckLibraryScannerEney> CheckDZUnScanList(List<CheckLibraryScannerEney>  listPrint) {


        if(listPrint!=null&&listPrint.size()>0){

            for (int i=0; i<listPrint.size(); i++){
                unScanlist.add(listPrint.get(i).getSTOCKCODE());
            }
        }
        return null;
    }
}
