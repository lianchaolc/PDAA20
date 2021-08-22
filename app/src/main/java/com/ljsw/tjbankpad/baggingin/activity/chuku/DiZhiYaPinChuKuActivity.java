package com.ljsw.tjbankpad.baggingin.activity.chuku;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.DetailList;
import com.ljsw.tjbankpad.baggingin.activity.chuku.entity.LocationManagerCode;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.manager.classs.pad.ManagerClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 抵制押品出库 选中出库的任务列表 查看相对的任务
 *
 * @author Administrator 请领
 *
 *         货位管库员查询抵质押品请领清分任务以及数量和明细信息
 */
public class DiZhiYaPinChuKuActivity extends Activity implements OnClickListener {
    protected static final String TAG = "DiZhiYaPinChuKuActivity";
    private ListView dzyp_outlistview; // 抵制押品出库的任务列表
    private List<LocationManagerCode> arraycleanList = new ArrayList<LocationManagerCode>();// 数据源
    private List<DetailList> detailist = new ArrayList<DetailList>();// 出入下一页面显示位置的集合
    private List<String> getcopyboxlist = new ArrayList<String>();// 存放箱子号码进行核对
    private ImageView ql_ruku_back;// 返回
    private Button ql_ruku_update;
    private TextView operatorleft, operatorright;
    private String postion;
    private ManagerClass manageractivity;
    private QingLingAdapter adapter;
    private OnClickListener OnClick1;
    private String getResult;// 获取网络结果
    private LocationManagerCode lmcode;// 实体类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizhiyapinchuku);
        initView();

        adapter = new QingLingAdapter();
        manageractivity = new ManagerClass();
        getChuKu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (o_Application.qinglingruku.size() > 0) {
            o_Application.qinglingruku.clear();
        }
        adapter.notifyDataSetChanged();
        dzyp_outlistview.setAdapter(adapter);
        getChuKu();
        S_application.wrong = "huitui";

        OnClick1 = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manageractivity.getAbnormal().remove();
                getChuKu();
            }
        };
    }

    /**
     * 货位管理员员查看任务
     */
    private void getChuKu() {
        manageractivity.getRuning().runding(DiZhiYaPinChuKuActivity.this, "数据加载中...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    getResult = new GetResistCollateralBaggingService().LocalMangeSelectCollateral();
                    if (!getResult.equals("") || getResult == null) {
                        Gson gson = new Gson();
                        LocationManagerCode[] lmcode = gson.fromJson(getResult, LocationManagerCode[].class);
                        for (int i = 0; i < lmcode.length; i++) {
                            arraycleanList = Arrays.asList(lmcode);
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(DiZhiYaPinChuKuActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(DiZhiYaPinChuKuActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manageractivity.getRuning().remove();
                    getData();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    dzyp_outlistview.setAdapter(adapter);
                    // new TurnListviewHeight(listview);
                    break;
                case 3:
                    manageractivity.getRuning().remove();
                    manageractivity.getAbnormal().timeout(DiZhiYaPinChuKuActivity.this, "没有任务", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manageractivity.getAbnormal().remove();
                        }
                    });
                    break;
                default:
                    break;
            }
        }

    };

    private void initView() {

        dzyp_outlistview = (ListView) findViewById(R.id.dzyp_outlistview);
        ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
        ql_ruku_back.setOnClickListener(this);

        // 更新
        ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
        ql_ruku_update.setOnClickListener(this);
        try {
            operatorleft = (TextView) findViewById(R.id.resistthecollateraloperator);
            operatorleft.setText(GApplication.user.getLoginUserName());// 操作人
        } catch (Exception e) {
            // TODO: handle exception
        }

        // listview 跳转
        dzyp_outlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                o_Application.qlruku = o_Application.qinglingruku.get(arg2);
                Intent i = new Intent(DiZhiYaPinChuKuActivity.this, DiZhiYaPinChuKuItemActivity.class);
                detailist.clear();// 每次进入都清空
                detailist.addAll(arraycleanList.get(arg2).getDetailList());
                getcopyboxlist.clear();
                for (int j = 0; j < arraycleanList.get(arg2).getDetailList().size(); j++) {
                    getcopyboxlist.add(arraycleanList.get(arg2).getDetailList().get(j).getSTOCKCODE());
                }
                i.putExtra("position", o_Application.qinglingruku.get(arg2).getDanhao());
                i.putExtra("list", (Serializable) detailist);

                // 关闭扫描

                startActivity(i);
            }
        });

    }

    /***
     * 适配器
     *
     * @author Administrator
     *
     */
    class QingLingAdapter extends BaseAdapter {
        LayoutInflater lf = LayoutInflater.from(DiZhiYaPinChuKuActivity.this);
        ViewHodler view;

        @Override
        public int getCount() {
            return arraycleanList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arraycleanList.get(arg0);
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
            view.riqi.setText(arraycleanList.get(arg0).getCLEARTASKNUM());
            view.count.setText(arraycleanList.get(arg0).getDetailList().size() + "");
            return arg1;
        }

    }

    public static class ViewHodler {
        TextView danhao, riqi, count;
    }

    /***
     * 全局的集合添加数据用于扫描数据源对比
     */
    public void getData() {
        if (arraycleanList.isEmpty()) {
            return;
        }
        o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
        if (arraycleanList.get(0).getDetailList().size() > 0) {
            for (int i = 0; i < arraycleanList.size(); i++) {
                for (int j = 0; j < arraycleanList.get(i).getDetailList().size(); j++) {
                    detailist.clear();
                }
                o_Application.qinglingruku.add(new QingLingRuKu(arraycleanList.get(i).getCLEARTASKNUM() + "",
                        arraycleanList.get(i).getCOUNT(), getcopyboxlist));

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ql_ruku_update:
                if (o_Application.qinglingruku.size() > 0) {
                    o_Application.qinglingruku.clear();
                }
                getChuKu();
                break;
            case R.id.ql_ruku_back:
                DiZhiYaPinChuKuActivity.this.finish();
                break;
            default:
                break;
        }

    }
}
