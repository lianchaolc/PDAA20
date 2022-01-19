package com.example.pda.homemagnetopackge;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.application.GApplication;
import com.example.app.util.Skip;
import com.example.pda.R;
import com.example.pda.homemagnetopackge.entity.HouDulineEntity;
import com.example.pda.homemagnetopackge.server.AccountAndPostmanServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manager.classs.pad.ManagerClass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/***
 * 库管员后督账包操作 由称为文件凭证
 *
 * @author Administrator 20200422 lianchao
 *
 */
public class HouDuActivity extends Activity implements OnClickListener {
    private static final String TAG = "HouDuActivity";
    private ListView houdulistview_way;
    private List<HouDulineEntity> houdulinlist = new ArrayList<HouDulineEntity>();

    private List<String> houduActionlist = new ArrayList<String>();
    private DiZhiYaPinXuanZheAdapter dDiZhiYaPinXuanZheAdapter;
    private Button houduzhangbaobtn;
    private ManagerClass manager;

    private String netResult = "";// 网络返回的结果
    private OnClickListener OnClick1;
    private String undata = "";

    private List<String> boxZZlist = new ArrayList<String>();// 村周转箱号 跳转后直接进行扫描
    private ImageView houduactivityql_ruku_back;
    private Button huo_du_update;
    private String lineandtime;// 保存线路和时间
    private String pinjie = ""; // 新拼接的字符串线路和时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hou_du);
        manager = new ManagerClass();
        initView();
        OnClick1 = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                loadMyData();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyData();
    }

    /***
     * 获取数据
     */
    private void loadMyData() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String conterlernumber = GApplication.user.getYonghuZhanghao();
                    Log.e(TAG, "===" + conterlernumber);
                    houdulinlist.clear();
                    if (!conterlernumber.equals("") || null != conterlernumber) {
                        netResult = new AccountAndPostmanServer().PostmandataSelect(conterlernumber);
                        Log.e(TAG, "===" + netResult);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<HouDulineEntity>>() {
                        }.getType();

                        List<HouDulineEntity> list = gson.fromJson(netResult, type);
                        houdulinlist = list;
                        if (houdulinlist.size() > 0) {
                            handler.sendEmptyMessage(2);
                        } else {
                            Log.e(TAG, "==失败=" + netResult);
                            handler.sendEmptyMessage(3);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SocketTimeout异常" + e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(TAG, "NullPointer异常" + e);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception异常" + e);
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();

    }

    /***
     * 组件初始化
     */
    private void initView() {
        // TODO Auto-generated method stub
        houdulistview_way = (ListView) findViewById(R.id.houdulistview_way);
        houduactivityql_ruku_back = (ImageView) findViewById(R.id.houduactivityql_ruku_back);
        houduactivityql_ruku_back.setOnClickListener(this);
        houduzhangbaobtn = (Button) findViewById(R.id.houduzhangbaobtn);
        houduzhangbaobtn.setFocusable(false);
        huo_du_update = (Button) findViewById(R.id.huo_du_update);
        huo_du_update.setOnClickListener(this);
        houduzhangbaobtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (houdulinlist.size() > 0) {
                    undata = "";
                    String linetime = "";
                    String linnum = "";
                    pinjie = "";
                    List<HouDulineEntity> houdulinlist1 = new ArrayList<HouDulineEntity>();
                    houdulinlist1 = houdulinlist;
                    for (int j = 0; j < houdulinlist1.size(); j++) {

                        if (houdulinlist1.get(j).isChecked()) {

                            houduActionlist.add(houdulinlist1.get(j).getLinenum());
                            if (houdulinlist1.size() > 1) {// 长度大一个时进行加逗号拼接
                                linnum = linnum + houdulinlist1.get(j).getLinenum() + ",";
                                undata = linnum.substring(0, linnum.length() - 1);
                                String linetimesplite = houdulinlist1.get(j).getStatedate();
                                String linnumsplite = houdulinlist1.get(j).getLinenum();
                                pinjie = pinjie + linnumsplite + "," + linetimesplite + ";";
                            } else {// 小于 或者等于1 时 直接赋值 去当前的值
                                undata = houdulinlist1.get(0).getLinenum();
                                String linetimesplite = houdulinlist1.get(0).getStatedate();
                                String linnumsplite = houdulinlist1.get(j).getLinenum();
                                pinjie = linnumsplite + "," + linetimesplite + ";";
                            }
                        } else {

                        }

                    }


                    if (undata == null || undata.equals("") || null == pinjie || pinjie.equals("")) {
                        Log.d(TAG, "!!!!!!!!!!!undata" + undata);
                        houduzhangbaobtn.setFocusable(false);// 可点击
                        Toast.makeText(HouDuActivity.this, "请选中要做的线路", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "!不等！！！！！！！！！！" + "ZZZZZZZ");
                        loadMyData1();
                    }

                    Log.d(TAG, "!!!!!!!!!!!长度" + houduActionlist.size());

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.houduactivityql_ruku_back:
                HouDuActivity.this.finish();

                break;
            case R.id.huo_du_update:
                loadMyData();
                if (dDiZhiYaPinXuanZheAdapter == null) {
                    dDiZhiYaPinXuanZheAdapter = new DiZhiYaPinXuanZheAdapter();
                    houdulistview_way.setAdapter(dDiZhiYaPinXuanZheAdapter);
                } else {
                    dDiZhiYaPinXuanZheAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }

    }

    class DiZhiYaPinXuanZheAdapter extends BaseAdapter {

        ViewHolderAdapterWailk view;
        LayoutInflater lf = LayoutInflater.from(HouDuActivity.this);

        @Override
        public int getCount() {
            return houdulinlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return houdulinlist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final ViewHolderAdapterWailk holder;
            if (v == null) {
                holder = new ViewHolderAdapterWailk();
                ;
                v = lf.inflate(R.layout.postmansaccountlistview_walkie_chebox_item, null);
                holder.dizhiyapincheboxresult = (TextView) v.findViewById(R.id.postmancountresult1);
                holder.postmancounttv = (TextView) v.findViewById(R.id.postmancounttv);
                holder.mCheckBox = (CheckBox) v.findViewById(R.id.dzyp_chebox);
                holder.postmanstatetimetv = (TextView) v.findViewById(R.id.postmanstatetimetv);// 显示时间

                v.setTag(holder);
            } else {
                holder = (ViewHolderAdapterWailk) v.getTag();
            }
            holder.postmancounttv.setVisibility(View.VISIBLE);
            holder.postmanstatetimetv.setText(houdulinlist.get(position).getStatedate() + "");

            // 显示任务的单号 只有一个订单号
            holder.dizhiyapincheboxresult.setText(houdulinlist.get(position).getLinename() + "");
            holder.postmancounttv.setText(houdulinlist.get(position).getCount());
            // 显示是否被选中
            holder.mCheckBox.setChecked(houdulinlist.get(position).isChecked());
            holder.mCheckBox.setOnClickListener(new OnClickListener() {

                // s设置被点击和不被点击的状态
                @Override
                public void onClick(View v) {
                    if (holder.mCheckBox.isChecked()) {
                        holder.mCheckBox.setChecked(true);
                        houdulinlist.get(position).setChecked(true);
                    } else {
                        holder.mCheckBox.setChecked(false);
                        houdulinlist.get(position).setChecked(false);
                    }
                    // view.mCheckBox.setChecked(view.mCheckBox.isChecked()?false:true);
                    // 三木运算符
                }
            });
            return v;
        }

    }

    public static class ViewHolderAdapterWailk {
        public TextView dizhiyapincheboxresult;
        public TextView postmancounttv;
        public CheckBox mCheckBox;
        public TextView postmanstatetimetv;// 显示日期
    }

    class SelectListen implements OnCheckedChangeListener {
        private int position;

        public SelectListen(int position) {
            super();
            this.position = position;

        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(HouDuActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(HouDuActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();

                    dDiZhiYaPinXuanZheAdapter = new DiZhiYaPinXuanZheAdapter();
                    houdulistview_way.setAdapter(dDiZhiYaPinXuanZheAdapter);

                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(HouDuActivity.this, "没有任务", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;
                case 4:
                    if (boxZZlist.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("houduActionlist", (Serializable) boxZZlist);
                        bundle.putString("linnum", undata);
                        bundle.putString("lineandtime", pinjie);
                        System.out.println("准备跳转页面");
                        System.out.print("!!!!!!!" + bundle);
                        if (null == bundle && boxZZlist.size() == 0) {
                            Log.d(TAG, "空值！！！！！！！！！！");
                        } else {
                            Skip.skip(HouDuActivity.this, PostmansAccountCheckerActivity.class, bundle, 0);
                        }
                    }
                    break;
                case 5:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(HouDuActivity.this, "没有任务", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();

                        }
                    });
                    break;
                case 6:
                    if (null != netResult || !netResult.equals("")) {
                        Toast.makeText(HouDuActivity.this, "" + netResult, 500).show();
                    }
                    break;
                default:
                    break;
            }
        }

    };


    /***
     * 获取周转箱通过线路编号
     */
    private void loadMyData1() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String conterlernumber = GApplication.user.getYonghuZhanghao();
                    netResult = new AccountAndPostmanServer().PostmanZZData(conterlernumber, undata, pinjie);
                    Log.e(TAG, "===" + netResult);
                    if (null != netResult || !netResult.equals("")) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();

                        List<String> list = gson.fromJson(netResult, type);
                        Log.e(TAG, "===!!!!!!!!===========" + list.size());
                        if (!"anytype".equals(netResult)) {
                            Log.e(TAG, "===" + "42222222222222222");
                            boxZZlist = list;// 赋值给全局的变量进行判断
                            handler.sendEmptyMessage(4);
                        } else {
                            Log.e(TAG, "===" + netResult);
                            Log.e(TAG, "===" + "5 失败");
                            handler.sendEmptyMessage(5);
                        }
                    } else {

                        Toast.makeText(HouDuActivity.this, "请选中要做的线路", 400).show();

                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SocketTimeout异常" + e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e(TAG, "NullPointer异常" + e);
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception异常" + e);
                    handler.sendEmptyMessage(1);
                }
            }

        }.start();

    }

}
