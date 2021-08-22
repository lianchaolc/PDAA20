package com.ljsw.tjbankpda.qf.fragment;

import a20.cn.uhf.admin.RfidAdmin;
import hdjc.rfid.operator.RFID_Device;

import java.util.ArrayList;
import java.util.List;

import com.example.pda.R;
import com.ljsw.tjbankpad.baggingin.activity.QualitativeWareScanningbyDiZhi;
import com.ljsw.tjbankpad.baggingin.activity.zhuangdai.QualitativeWareScanningbyDiZhinewThread;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.DiZhiYaPinGetNumber;
import com.ljsw.tjbankpda.main.QinglingZhuangxiangActivity;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.ZhuanxiangTongji;
import com.ljsw.tjbankpda.util.DizhiYapinSaomiaoUtil;
import com.ljsw.tjbankpda.util.MessageDialog;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.manager.classs.pad.ManagerClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 抵制押品fragment
 *
 * @author Administrator 请领装箱 抵制押品o_Application.qlruku
 */
public class QinglingZhuangxiangDizhiFragment extends Fragment implements OnTouchListener {
    protected static final String TAG = "QinglingZhuangxiangDizhiFragment";
    private ListView lvInfo, leftlistview, rightlistview;//
    private TextView lefttv, right;

    private List<String> boxLtDizhi = Mapplication.getApplication().boxLtDizhi;// 存放抵质押品信息
    private List<String> boxLtNumber = Mapplication.getApplication().boxLtNumber;
    private List<String> boxLtRember = Mapplication.getApplication().boxremberDizhi;
    private ManagerClass manager = new ManagerClass();
    private List<String> copylist = new ArrayList<String>();
    // 数据源
    private List<String> checkList = new ArrayList<String>();// 存放那些 扫一半的集合情况
    private QualitativeWareScanningbyDiZhi getnumber;// 这里涉及到扫描数据保存问题所以用新的扫描规则

    private QualitativeWareScanningbyDiZhinewThread getnumberthrea;// 这里涉及到扫描数据保存问题所以用新的扫描规则
    private LeftAdapter ladapter;
    private RightAdapter radapter;
    private RFID_Device rfid;
    private Context mContext;

    private RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fg_qingling_zhuangxiang_dizhi, null);
        QinglingZhuangxiangDizhiFragment.this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		getnumber = new QualitativeWareScanningbyDiZhi();
//		getnumber.setHandler(handler);
        getnumberthrea = new QualitativeWareScanningbyDiZhinewThread();
        getnumberthrea.setHandler(handler);
        copylist.clear();// 出现添加多次 上一个页面传空值的处理方法
        o_Application.numberlist.clear();// 每次进入清除
        // 1 上一个（请领装箱info）页面正常传过来有值的时候
        if (o_Application.qlruku == null) {
            Log.d(TAG, "当数据源为null");
        } else {
            if ((o_Application.qlruku.getZhouzhuanxiang().size() != 0)) {
                copylist.addAll(o_Application.qlruku.getZhouzhuanxiang());
                Mapplication.getApplication().boxLtDizhi.clear();
                Mapplication.getApplication().boxLtDizhi.addAll(o_Application.qlruku.getZhouzhuanxiang());// 记录单条处理数据
                Mapplication.getApplication().boxremberDizhi.addAll(o_Application.qlruku.getZhouzhuanxiang());// 保存数据0415
                Log.d(TAG, "长度BBB" + copylist + "size" + Mapplication.getApplication().boxremberNumberDizhi.size());
                if (Mapplication.getApplication().boxremberDizhi1.size() != 0) {// 201904015 扫一半的情况
                    o_Application.numberlist.clear();
                    o_Application.numberlist.addAll(Mapplication.getApplication().boxremberDizhi1);
                }
                // 2不返回上一个页面只在现金中控和抵质押品三个页面切换造成无数据 想法 o_Application.numberlist 的数据用一个变量接在放在里面
            } else {
                Log.d(TAG, "长度cccc" + Mapplication.getApplication().boxremberDizhi.size());
                if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
                    o_Application.numberlist.clear();
                    o_Application.numberlist.addAll(Mapplication.getApplication().boxremberNumberDizhi);
                    Log.d(TAG, "长度cccc" + copylist);
                }
            }
            Log.d(TAG, "boxremberDizhi长度AAAA" + Mapplication.getApplication().boxremberDizhi.size());
            ladapter = new LeftAdapter();
            radapter = new RightAdapter();
            ladapter.notifyDataSetChanged();
            radapter.notifyDataSetChanged();
            manager = new ManagerClass();
            leftlistview = (ListView) v.findViewById(R.id.pleasepackstatisticleft);
            rightlistview = (ListView) v.findViewById(R.id.pleasepackstatisticright);
            right = (TextView) v.findViewById(R.id.pleasepackstatistic_hadscan);
            lefttv = (TextView) v.findViewById(R.id.pleasepackstatisticsneedscan);
            mContext = QinglingZhuangxiangDizhiFragment.this.getActivity();
        }
        return v;

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResume() {
        super.onResume();
        RfidAdmin.booleanstrnewThread=true;
        // 如果请领装箱详细信息（QinglingZhuangxiangInfoActivity） 没有传值到集合就直接return出去不做扫描操作
        if (Mapplication.getApplication().boxremberDizhi.size() == 0) {
            Log.d(TAG, "数据为null 不进行扫描");
            return;
        } else {
            Setdata();

        }
        ladapter.notifyDataSetChanged();
        radapter.notifyDataSetChanged();


    }

    public void Setdata() {
//		 已经网络获取后并且扫描的直接显示已经扫到
        System.out.print("copylist.size()======" + copylist.size());
//		getRfid().addNotifly(getnumber);
        getRfid().addNotifly(getnumberthrea);
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                getRfid().open_a20();
                try {
                    Log.d(TAG, "线程启动======Qingling zhuangxiang");
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d(TAG, "线程启动异常" + e);
                }
            }
        }.start();
        lefttv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
        right.setText("" + o_Application.numberlist.size());
        // 此处进行添加避面重复的方法
        leftlistview.setAdapter(ladapter);
        rightlistview.setAdapter(radapter);
        ladapter.notifyDataSetChanged();
        radapter.notifyDataSetChanged();
    }

    /**
     * 无限刷新集合
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                    Log.d("DIZHIfragment", "======InventoryThread====+handler0=" + Thread.currentThread().getName());
                    // 循环扫描后网络请求后的结果为0时 将抵质邀您的状态设置为 完成
                    if (o_Application.qlruku.getZhouzhuanxiang().size() == 0) {
                        Mapplication.getApplication().IsDizhiOK = true;
                    }


                    Mapplication.getApplication().zxTjDizhi.setWeiZhuang(o_Application.qlruku.getZhouzhuanxiang().size());
                    Mapplication.getApplication().zxTjDizhi.setYiZhuang(o_Application.numberlist.size());
                    /// 后加入20190415防止数据过多
                    if (o_Application.numberlist.size() == 0) {
                        o_Application.numberlist.clear();
                        if (Mapplication.getApplication().boxremberNumberDizhi.size() != 0) {// 20190405
                            o_Application.numberlist.addAll(Mapplication.getApplication().boxremberNumberDizhi);
                        }
                    }
//                    if (Mapplication.getApplication().boxLtNumber.contains(o_Application.numberlist)) {

//                    } else {

//                        Mapplication.getApplication().boxLtNumber.addAll(o_Application.numberlist);// 添加扫描到的签

//                    }
//                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                    break;
                case 10086:// 这里特别主注意 子线程传递数据
//					ladapter.notifyDataSetChanged();
//					radapter.notifyDataSetChanged();
//					break;
                case 1:// 这里特别主注意 子线程传递数据
                    Log.d("DIZHIfragment", "======InventoryThread====+handler1" + Thread.currentThread().getName());
                    right.setText("" + o_Application.numberlist.size());
                    lefttv.setText("" + o_Application.qlruku.getZhouzhuanxiang().size());
                    leftlistview.setAdapter(ladapter);
                    rightlistview.setAdapter(radapter);
                    ladapter.notifyDataSetChanged();
                    radapter.notifyDataSetChanged();
					break;
            }

        }

    };

    @Override
    public void onPause() {
        super.onPause();
        if (rfid != null) {
            getRfid().close_a20();
            getRfid().stop_a20();


            RfidAdmin r = new RfidAdmin();
            r.closeRfid();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (rfid != null) {
            getRfid().close_a20();

        }
        RfidAdmin.booleanstrnewThread=false;
    }

    /****
     * 原始版输入时代码20190416
     */
//	class DizhiBaseAdapter extends BaseAdapter{
//		private ViewHolder vh;
//
//		@Override
//		public int getCount() {
//
//			return ltDizhi.size();
//
//		}
//		@Override
//		public Object getItem(int arg0) {
//			return ltDizhi.get(arg0);
//		}
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//		@Override
//		public View getView(int arg0, View v, ViewGroup arg2) {
//			LayoutInflater inflater=LayoutInflater.from(getActivity());
//			if(v==null){
//				v=inflater.inflate(R.layout.item_qingling_zhuangxiang, null);
//				vh=new ViewHolder();
//				vh.tvType=(TextView)v.findViewById(R.id.tv_item_qingling_zhuangxiang_type);
//				vh.tvCount=(TextView)v.findViewById(R.id.tv_item_qingling_zhuangxiang_count);
//				vh.tvDel=(TextView)v.findViewById(R.id.tv_item_qingling_zhuangxiang_delete);
//				v.setTag(vh);
//			}else{
//				vh=(ViewHolder) v.getTag();
//			}
//			vh.tvType.setText(ltDizhi.get(arg0));
//			vh.tvCount.setVisibility(View.GONE);
//			vh.tvDel.setOnClickListener(new QuanbieDelListener(arg0));
//			return v;
//		}
//
//		public class ViewHolder{
//			TextView tvType;
//			TextView tvCount;
//			TextView tvDel;
//		}
//		class QuanbieDelListener implements OnClickListener{
//			private int position;
//
//			public QuanbieDelListener(int position) {
//				super();
//				this.position = position;
//			}
//			@Override
//			public void onClick(View arg0) {
//				int weizhuang=Mapplication.getApplication().zxTjDizhi.getWeiZhuang();
//				int yizhuang=Mapplication.getApplication().zxTjDizhi.getYiZhuang();
//				weizhuang++;
//				yizhuang--;
//				Mapplication.getApplication().zxTjDizhi.setWeiZhuang(weizhuang);
//				Mapplication.getApplication().zxTjDizhi.setYiZhuang(yizhuang);
//				ltDizhi.remove(position);
//				da.notifyDataSetChanged();
//				new TurnListviewHeight(lvInfo);
//				Mapplication.getApplication().IsDizhiOK=false;
//				updateInfo();
//			}
//		}
//	}
//	private void updateInfo() {
//		Mapplication.getApplication().ltDizhiNum=ltDizhi;
//		ZhuanxiangTongji dizhi=Mapplication.getApplication().zxTjDizhi;
//		int totalDzCount=dizhi.getWeiZhuang()+dizhi.getYiZhuang();
//		int yzdzCount=ltDizhi.size();
//		dizhi.setYiZhuang(yzdzCount);
//		dizhi.setWeiZhuang(totalDzCount-yzdzCount);
//		Mapplication.getApplication().zxTjDizhi=dizhi;
//		Mapplication.getApplication().boxLtDizhi=ltDizhi;
//	}

    /***
     * 适配器
     */

    class LeftAdapter extends BaseAdapter {
        LeftHolder lh;
        LayoutInflater lf = LayoutInflater.from(QinglingZhuangxiangDizhiFragment.this.getActivity());

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
        private List<String> rightData;
        RightHolder rh;
        LayoutInflater lf = LayoutInflater.from(QinglingZhuangxiangDizhiFragment.this.getActivity());

        public RightAdapter() {
            this.rightData = o_Application.numberlist;
            this.lf = lf;
        }

        public RightAdapter(List<String> rightData, LayoutInflater lf) {
            this.rightData = o_Application.numberlist;
            this.lf = lf;
        }

        public void setData(List<String> rightData) {
            if (rightData != null) {
                this.rightData = o_Application.numberlist;
                notifyDataSetChanged();
            }

        }

        @Override
        public int getCount() {
//			Log.e("o_Application.numberlist.size()", "测试长度：：：：：" + o_Application.numberlist.size());
//			return o_Application.numberlist.size();
            return null == rightData ? 0 : rightData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return rightData.get(arg0);
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
            rh.tv.setText(rightData.get(arg0));
            return arg1;
        }

    }

    public static class RightHolder {
        TextView tv;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == KeyEvent.KEYCODE_BACK) {
            if (rfid != null) {
                getRfid().close_a20();
//                QualitativeWareScanningbyDiZhi qualitativeWareScanningbyDiZhi = new QualitativeWareScanningbyDiZhi();
//                qualitativeWareScanningbyDiZhi.stoHander(handler);
            }
            QinglingZhuangxiangDizhiFragment.this.getActivity().finish();
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        getRfid().close_a20();
//        QualitativeWareScanningbyDiZhi qualitativeWareScanningbyDiZhi = new QualitativeWareScanningbyDiZhi();
//        qualitativeWareScanningbyDiZhi.stoHander(handler);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rfid != null) {
            getRfid().close_a20();
            getRfid().stop_a20();
        }
        RfidAdmin r = new RfidAdmin();
        r.closeRfid();
//        QualitativeWareScanningbyDiZhi qualitativeWareScanningbyDiZhi = new QualitativeWareScanningbyDiZhi();
//        qualitativeWareScanningbyDiZhi.stoHander(handler);
        System.gc();
    }
}
