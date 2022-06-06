package com.ljsw.tjbankpda.db.activity;

import java.net.SocketTimeoutException;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pda.R;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.entity.QingLingChuRuKu;
import com.ljsw.tjbankpda.db.service.PeiSongChuRuKu;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

@SuppressLint("HandlerLeak")
public class PeiSongChuKuXianLu_db extends FragmentActivity implements OnClickListener, OnItemClickListener {
    private ImageView back;
    private Button update;
    private ListView listview;
    // List<PeiSongChuKu> list = new ArrayList<PeiSongChuKu>();
    private ManagerClass manager;
    private String parms;
    private Table[] churukulist;
    private OnClickListener OnClick1;
    private PeiSongAdapter adapter;
    private long lastClickTime = 0;/// 限制点击的时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_peisongchukuxianlu);
        load();
        adapter = new PeiSongAdapter();
        listview.setAdapter(adapter);
        manager = new ManagerClass();
        OnClick1 = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getPeiSongChuKu();
                manager.getAbnormal().remove();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        listview.requestLayout();
        adapter.notifyDataSetChanged();

        getPeiSongChuKu();
    }

    public void getPeiSongChuKu() {
        manager.getRuning().runding(this, "数据加载中...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    // 1.入库操作，2出库操作
                    parms = new PeiSongChuRuKu().getPeisongChuku(o_Application.kuguan_db.getOrganizationId(), "2");
                    if (!parms.equals("")) {
                        churukulist = Table.doParse(parms);
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
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(PeiSongChuKuXianLu_db.this, "连接超时,重试?", OnClick1);
                    listview.setEnabled(true);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(PeiSongChuKuXianLu_db.this, "网络连接失败,重试?", OnClick1);
                    listview.setEnabled(true);
                    break;
                case 2:
                    manager.getRuning().remove();
//                    PeiSongChuKuXianLu_db.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                    getData();
//                        }
//                    });
                    listview.setEnabled(true);
                    if (null == o_Application.qingling_churuku || o_Application.qingling_churuku.size() == 0) {
                        handler.sendEmptyMessage(7);
                    } else if (o_Application.qingling_churuku.size() >= 1) {
                        listview.requestLayout();
                        adapter.notifyDataSetChanged();
                    }
//				listview.setAdapter(adapter);
//				new TurnListviewHeight(listview);
                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(PeiSongChuKuXianLu_db.this, "获取任务失败", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();

                        }
                    });
                    listview.setEnabled(true);
                    break;
                case 7:
                    Toast.makeText(PeiSongChuKuXianLu_db.this, "数据未加载完成请等待!", 400).show();
                    if (null == o_Application.qingling_churuku || o_Application.qingling_churuku.size() == 0) {
                        getPeiSongChuKu();//  空的重新拿数据
                    } else if (o_Application.qingling_churuku.size() >= 1) {
                        listview.requestLayout();
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public void load() {
        back = (ImageView) findViewById(R.id.peisong_back);
        back.setOnClickListener(this);
        update = (Button) findViewById(R.id.peisong_update);
        update.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.peisong_listView1);
        listview.setOnItemClickListener(this);
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
//                        manager.getRuning().runding(PeiSongChuKuXianLu_db.this, "数据加载中...");
                        break;
                    case MotionEvent.ACTION_UP:
                        manager.getRuning().remove();
                        break;
                }
                return false;
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {//停止

                } else if (scrollState == SCROLL_STATE_FLING) {//手指抛出

                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {// 正在滚动

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    boolean Scrollstate = true;

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.peisong_back:
                listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {//停止
                            Scrollstate = true;
                            PeiSongChuKuXianLu_db.this.finish();
                        } else if (scrollState == SCROLL_STATE_FLING) {//手指抛出
                            Scrollstate = true;
                            PeiSongChuKuXianLu_db.this.finish();

                        } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {// 正在滚动
                            Toast.makeText(PeiSongChuKuXianLu_db.this, "等待列表停止滑动在返回", 1000).show();
                            Scrollstate = false;
                        } else {
                            Scrollstate = true;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });
                if (Scrollstate) {
                    PeiSongChuKuXianLu_db.this.finish();
                }


                break;
            case R.id.peisong_update:
                // 功能为实现
                o_Application.qingling_churuku.clear();
                listview.setEnabled(false);
                getPeiSongChuKu();

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        o_Application.qinglingchuruku = o_Application.qingling_churuku.get(arg2);
        manager.getRuning().runding(this, "开启扫描功能中...");
        if (o_Application.qinglingchuruku.getCaozuotype().equals("1")) {
            Skip.skip(this, PeiSongJiaoJie_db.class, null, 0);
        } else {
            Skip.skip(this, PeiSongSaoMiao_db.class, null, 0);
        }

    }

    class PeiSongAdapter extends BaseAdapter {
        LayoutInflater lf = LayoutInflater.from(PeiSongChuKuXianLu_db.this);
        ViewHolder view;

        @Override
        public int getCount() {
            return o_Application.qingling_churuku.size();
        }

        @Override
        public Object getItem(int arg0) {
            return o_Application.qingling_churuku.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = lf.inflate(R.layout.adapter_peisongxianlu, null);
                view = new ViewHolder();
                view.qingling = (TextView) arg1.findViewById(R.id.adapter_peisong_qingling);
                view.shangjiao = (TextView) arg1.findViewById(R.id.adapter_peisong_shangjiao);
                view.xianlu = (TextView) arg1.findViewById(R.id.adapter_peisong_xianlu);
                view.count = (TextView) arg1.findViewById(R.id.adapter_peisong_count);
                arg1.setTag(view);
            } else {
                view = (ViewHolder) arg1.getTag();
            }
            if (
                    o_Application.qingling_churuku.size()==0) {
                handler.sendEmptyMessage(7);

            } else {
                if ("1".equals(o_Application.qingling_churuku.get(arg0).getCaozuotype())) {
                    view.qingling.setText("无");
                    view.shangjiao.setText("有");
                } else if ("2".equals(o_Application.qingling_churuku.get(arg0).getCaozuotype())) {
                    view.qingling.setText("有");
                    view.shangjiao.setText("无");
                } else if ("3".equals(o_Application.qingling_churuku.get(arg0).getCaozuotype())) {
                    view.qingling.setText("有");
                    view.shangjiao.setText("有");
                }

                view.xianlu.setText(o_Application.qingling_churuku.get(arg0).getXianluming());
                System.out.println("周转箱数量:=" + o_Application.qingling_churuku.get(arg0).getKuxiang().size());
                view.count.setText(o_Application.qingling_churuku.get(arg0).getKuxiang().size() + "");
            }
            return arg1;
        }
    }

    public static class ViewHolder {
        TextView qingling, shangjiao, xianlu, count;
    }

    public void getData() {
        o_Application.qingling_churuku.clear();
        if (churukulist[0].get("xianlumingcheng").getValues().size() > 0) {
            for (int i = 0; i < churukulist.length; i++) {
                o_Application.qingling_churuku
                        .add(new QingLingChuRuKu(churukulist[i].get("xianlumingcheng").getValues().get(0),
                                churukulist[i].get("xianluId").getValues().get(0),
                                churukulist[i].get("zhouzhuangxiang").getValues(),
                                churukulist[i].get("xianluType").getValues().get(0),
                                churukulist[i].get("caozuoleibie").getValues().get(0)));
                // params=xianluId:|xianlumingcheng:|xianluType:|zhouzhuangxiang
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.getRuning().remove();
        o_Application.qingling_churuku.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE) {//停止
                        PeiSongChuKuXianLu_db.this.finish();
                    } else if (scrollState == SCROLL_STATE_FLING) {//手指抛出
                        PeiSongChuKuXianLu_db.this.finish();

                    } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {// 正在滚动
                        Scrollstate = true;
                        Toast.makeText(PeiSongChuKuXianLu_db.this, "等待列表停止滑动在返回", 500).show();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

        }
        if (Scrollstate) {
            PeiSongChuKuXianLu_db.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
