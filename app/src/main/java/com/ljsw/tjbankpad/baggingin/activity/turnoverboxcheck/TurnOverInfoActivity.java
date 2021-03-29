package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.server.TureOverBoxCheckServer;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnNoverBoxEntity;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.ClearList;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.ClearingCheckVO;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.LineBoxVo;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.OrderList;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.ResultVo;
import com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney.TurnOverBoxCodeEntity.TurnOverBoxCEntity;
import com.ljsw.tjbankpda.main.QinglingZhuangxiangInfoActivity;
import com.manager.classs.pad.ManagerClass;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 查看明细页面
 */
public class TurnOverInfoActivity extends AppCompatActivity {
    private static final String TAG = "TurnOverInfoActivity";
    private LinearLayout linlayturnoverinfo;// 获取名细的布局
    private LinearLayout linlayturnoverinfoone;// 获取名细的布局
    private LinearLayout turn_over_info_layout;
    private String linno;
    private ManagerClass manageryuan;
    private List<LineBoxVo> infolist = new ArrayList<LineBoxVo>();
    private List<String> infolistcheck = new ArrayList<String>();

    Set hashSet = new HashSet();

    private TurnOverBoxCEntity[] turnOverboxcentity = null;

    private LineBoxVo[] lineBoxVos = null;
    private TextView turn_title_tvinfo;


    private TextView turn_title_pgdan;

    private TextView kaishijian;

    private TextView tvpsType;//  配送单类型 01  清零、上缴
    private TextView tvstate;
    private Button toi_update;
    private ImageView toi_back;

    List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();

    private TextView tvpeisongdan2, tvpeisongdan1, tvpeisongdan3, tvpeisongdan4;

    String xj1 = "";
    String zk1 = "";
    String dz1 = "";
    String box1 = "";
    private  String selectType;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(TurnOverInfoActivity.this, "加载超时,重试?", null);
                    break;
                case 1:
                    manageryuan.getRuning().remove();
                    manageryuan.getAbnormal().timeout(TurnOverInfoActivity.this, "网络连接失败,重试?", null);
                    break;
                case 2:

                    Toast.makeText(TurnOverInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    break;

                case 3:

                    Toast.makeText(TurnOverInfoActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    break;


                case 6:
                    String psnum = "";
                    turn_title_tvinfo = findViewById(R.id.turn_title_tvinfo);
                    turn_title_pgdan = findViewById(R.id.tvpaigongdanw);
                    kaishijian = findViewById(R.id.kaishijian);
                    List<ClearingCheckVO> inforcashlistOrder = new ArrayList<ClearingCheckVO>();
                    List<ClearingCheckVO> inforcashlistClear = new ArrayList<ClearingCheckVO>();

                    inforcashlistOrder.clear();
                    inforcashlistClear.clear();
                    List<ClearList> inforcashlistssschange = new ArrayList<ClearList>();
                    List<ClearingCheckVO> inforcashlistdizhi = new ArrayList<ClearingCheckVO>();
                    infolistcheck.clear();
                    hashSet.clear();
                    for (int i = 0; i < infolist.size(); i++) {
                        turn_title_tvinfo.setText(infolist.get(i).getOrgName());
                        turn_title_pgdan.setText(infolist.get(i).getOrg());
                        if (infolist.get(i).getPsType().equals("0")) {
                            tvpsType.setText("上缴");
                        } else if (infolist.get(i).getPsType().equals("1")) {
                            tvpsType.setText("请领");
                        }
                        if (infolist.get(i).getState().equals("01")) {
                            tvstate.setText("已装箱");
                        } else if (infolist.get(i).getState().equals("02")) {
                            tvstate.setText("入总库");

                        } else if (infolist.get(i).getState().equals("03")) {
                            tvstate.setText("押运分库");

                        } else if (infolist.get(i).getState().equals("04")) {
                            tvstate.setText("入分库");

                        } else if (infolist.get(i).getState().equals("05")) {
                            tvstate.setText("押运网点");

                        } else if (infolist.get(i).getState().equals("00")) {
                            tvstate.setText("已完成");
                        }

//                        String oldPs = infolist.get(i).getPsNum(); //上次循环的psNum
//                        if (i == infolist.size() - 1) {  //是否最后一次循环
//                            for (int k = 0; k < infolist.get(i).getClearList().size(); k++) {
//                                System.out.println("配送单'" + infolist.get(i).getPsNum() + "'箱子信息:  " + infolist.get(i).getClearList().get(k).getPassBoxNum());
//                            }
//                        } else if (!infolist.get(i + 1).getPsNum().equals(oldPs)) {  //下一次循环如果psNum不一致，证明是一条新的配送单信息。即打印当前配送单所对应的箱子信息
//                            for (int k = 0; k < infolist.get(i).getClearList().size(); k++) {
//                                System.out.println("配送单'" + infolist.get(i).getPsNum() + "'箱子信息:  " + infolist.get(i).getClearList().get(k).getPassBoxNum());
//                            }
//                        }
//
//                        System.out.println("配送单信息编号:   " + infolist.get(i).getPsNum());
//                        System.out.println("配送单网点:   " + infolist.get(i).getToNodName());
//                        inforcashlistOrder.clear();
//                        inforcashlistClear.clear();
//                        inforcashlistOrder = infolist.get(i).getOrderList();
//                        inforcashlistClear = infolist.get(i).getClearList(); //


//  去掉重复的配送单   还原集合
                        if (hashSet == null) {
                            hashSet.add(infolist.get(i).getPsNum());//  添加数据
                        } else {
                            if (hashSet.contains(infolist.get(i).getPsNum())) {

                            } else {
                                hashSet.add(infolist.get(i).getPsNum());  //  添加数据
                            }

                        }

//                        Log.e(TAG,"!!!!!!!!!!!!!"+hashSet.size());
//                        tvpeisongdan2.setText("11111"+hashSet.size());
//                        String xj = "";
//                        String zk = "";
//                        String dz = "";
//                        String box = "";
//
//
//                        for (int b = 0; b < infolist.get(i).getOrderList().size(); b++) {
//                            if (infolist.get(i).getPsNum().equals(infolist.get(i).getOrderList().get(b).getPsNum())) {
//
//                            } else {
//
//
//                            }
//
//                            if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("3"))) {
//                                ClearingCheckVO ol3 = new ClearingCheckVO();
//                                ol3.setNumId(infolist.get(i).getOrderList().get(b).getNumId());
//                                ol3.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                inforcashlistdizhi.add(ol3);
//                                dz = dz + infolist.get(i).getOrderList().get(b).getNumId() + ",";
//                            } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("2"))) {
//                                ClearingCheckVO ol2 = new ClearingCheckVO();
//                                ol2.setDtlEndNo(infolist.get(i).getOrderList().get(b).getDtlEndNo());
//                                ol2.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                inforcashlistdizhi.add(ol2);
//                                zk = zk + infolist.get(i).getOrderList().get(b).getDtlEndNo() + ",";
//                            } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("1"))) {
//                                ClearingCheckVO ol1 = new ClearingCheckVO();
//                                ol1.setAmt(infolist.get(i).getOrderList().get(b).getAmt());
//                                ol1.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                inforcashlistdizhi.add(ol1);
//                                xj = xj + infolist.get(i).getOrderList().get(b).getAmt() + "," + "\n\r";
//                            }
//                        }
//
////huoqu peisong danhao
//                        for (int b = 0; b < infolist.get(i).getClearList().size(); b++) {
//                            if (infolist.get(i).getClearList().get(b).getPsNum().equals((infolist.get(i).getPsNum()))) {
//                                ClearList clearList = new ClearList();
//                                clearList.setPsNum(infolist.get(i).getClearList().get(b).getPsNum());
//                                inforcashlistssschange.add(clearList);
//                                box = box + infolist.get(i).getClearList().get(b).getPassBoxNum() + "\n\r";
//
//                            }
//                        }

                    }
                    String xj = "";
                    String zk = "";
                    String dz = "";
                    String box = "";


                        Iterator iterator = hashSet.iterator();
                        while (iterator.hasNext()) {
                         String stitle=iterator.next().toString();
                                     xj = "";
                                     zk = "";
                                     dz = "";
                                     box = "";
                            Set hashSetbox = new HashSet();
                            for (int i = 0; i < infolist.size(); i++) {
                            for (int b = 0; b < infolist.get(i).getOrderList().size(); b++) {
                                String  nstrtitle=infolist.get(i).getOrderList().get(b).getPsNum();
                                if (stitle.equals(nstrtitle)) {

                                    if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("3"))) {
                                        ClearingCheckVO ol3 = new ClearingCheckVO();
//                                        ol3.setNumId(infolist.get(i).getOrderList().get(b).getNumId());
                                        ol3.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
                                        inforcashlistdizhi.add(ol3);
//                                        if(i%2==0){
                                            dz = dz + infolist.get(i).getOrderList().get(b).getNumId() + ","+ "\n\r";;
//                                        }else{
//                                           dz = dz + infolist.get(i).getOrderList().get(b).getNumId() + ","+"\n\r";
//                                        }
                                    } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("2"))) {
                                        ClearingCheckVO ol2 = new ClearingCheckVO();
//                                        ol2.setDtlEndNo(infolist.get(i).getOrderList().get(b).getDtlEndNo());
                                        ol2.setOrderType(infolist.get(i).getOrderList().get(b).getDtlName()+":"+infolist.get(i).getOrderList().get(b).getDtlNums());
                                        inforcashlistdizhi.add(ol2);
//                                        +infolist.get(i).getOrderList().get(b).getDtlName()
                                        zk = zk + infolist.get(i).getOrderList().get(b).getDtlName()+":"+infolist.get(i).getOrderList().get(b).getDtlNums();
                                    } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("1"))) {
                                        ClearingCheckVO ol1 = new ClearingCheckVO();
//                                        ol1.setAmt(infolist.get(i).getOrderList().get(b).getAmt());
                                        ol1.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
                                        inforcashlistdizhi.add(ol1);
                                        xj = xj + infolist.get(i).getOrderList().get(b).getTcktName() + ":"+infolist.get(i).getOrderList().get(b).getAmt() +","  +
 "\n\r";
                                    }


                                    for (int a = 0; a < infolist.get(i).getClearList().size(); a++) {
                                  String      ctitlestr=infolist.get(i).getClearList().get(a).getPsNum();
                            if (ctitlestr.equals((stitle))) {
                                ClearList clearList = new ClearList();
                                clearList.setPsNum(infolist.get(i).getClearList().get(a).getPsNum());
                                inforcashlistssschange.add(clearList);
                                box = box + infolist.get(i).getClearList().get(a).getPassBoxNum() + "\n\r";
                                if(hashSetbox==null){
                                    hashSetbox.add(infolist.get(i).getClearList().get(a).getPassBoxNum() + "\n\r") ;
                                }else{
                                    if( hashSetbox.contains(infolist.get(i).getClearList().get(a).getPassBoxNum())){}else{
                                    hashSetbox.add(infolist.get(i).getClearList().get(a).getPassBoxNum() + "\n\r") ;
                                    }
                                }

                            }
                        }

                                }else{



                                    if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("3"))) {
//                                        ClearingCheckVO ol3 = new ClearingCheckVO();
//                                        ol3.setNumId(infolist.get(i).getOrderList().get(b).getNumId());
//                                        ol3.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                        inforcashlistdizhi.add(ol3);
//                                        dz = dz + infolist.get(i).getOrderList().get(b).getNumId() + ",";
//                                    } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("2"))) {
//                                        ClearingCheckVO ol2 = new ClearingCheckVO();
//                                        ol2.setDtlEndNo(infolist.get(i).getOrderList().get(b).getDtlEndNo());
//                                        ol2.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                        inforcashlistdizhi.add(ol2);
//                                        zk = zk + infolist.get(i).getOrderList().get(b).getDtlEndNo() + ",";
//                                    } else if (infolist.get(i).getOrderList().get(b).getOrderType().equals(("1"))) {
//                                        ClearingCheckVO ol1 = new ClearingCheckVO();
//                                        ol1.setAmt(infolist.get(i).getOrderList().get(b).getAmt());
//                                        ol1.setOrderType(infolist.get(i).getOrderList().get(b).getOrderType());
//                                        inforcashlistdizhi.add(ol1);
//                                        xj = xj + infolist.get(i).getOrderList().get(b).getAmt() + "," + "\n\r";
                                    }
//
                                }
                            }
                            }
                            linlayturnoverinfoone = (LinearLayout) findViewById(R.id.linlayturnoverinfoone);
                            View vonone = LayoutInflater.from(TurnOverInfoActivity.this)
                                    .inflate(R.layout.turn_over_info_layoutone, null);
                            TextView tvCountone = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonecvoun); //  增加内容
                            TextView qf_zhouzhuan_saomiao_peisongdan = vonone.findViewById(R.id.qf_zhouzhuan_saomiao_peisongdan);
                            TextView turnovercheckinfotvtitleinfoonenumid = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonenumid); //  增加内容

                            TextView turnovercheckinfotvtitleinfooneordertype = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfooneordertype); //  增加内容
                            TextView turnovercheckinfotvtitleinfoonepsnum = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonepsnum); //  增加内容
                            qf_zhouzhuan_saomiao_peisongdan.setText("配送单："+stitle);
                            if(null==zk||zk.equals("")){
                                tvCountone.setText("无");
                            }else{
                                tvCountone.setText(zk);
                            }

                            if(null==xj||xj.equals("")){
                                turnovercheckinfotvtitleinfoonenumid.setText("无");
                            }else{
                                turnovercheckinfotvtitleinfoonenumid.setText(xj);
                            }

                            if(null==dz||dz.equals("")){
                                turnovercheckinfotvtitleinfooneordertype.setText("无");
                            }else{
                                turnovercheckinfotvtitleinfooneordertype.setText(dz);
                            }
                            Iterator iterator1 = hashSetbox.iterator();//
                            String stitle1="";
                            while (iterator1.hasNext()) {
                                 stitle1 = stitle1+iterator1.next().toString();
                                turnovercheckinfotvtitleinfoonepsnum.setText(stitle1);
                            }
                            linlayturnoverinfoone
                                    .addView(vonone);
                        }



//                    linlayturnoverinfoone = (LinearLayout) findViewById(R.id.linlayturnoverinfoone);
//                    View vonone = LayoutInflater.from(TurnOverInfoActivity.this)
//                            .inflate(R.layout.turn_over_info_layoutone, null);
//                    TextView tvTypeone = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonecvoun);// 增加头部
//                    TextView tvCountone = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonecvoun); //  增加内容
//
//                    TextView turnovercheckinfotvtitleonenumid = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonenumid);// 增加头部
//                    TextView turnovercheckinfotvtitleinfoonenumid = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonenumid); //  增加内容
//
//                    TextView turnovercheckinfotvtitleoneordertype = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleoneordertype);// 增加头部
//                    TextView turnovercheckinfotvtitleinfooneordertype = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfooneordertype); //  增加内容
//                    TextView turnovercheckinfotvtitleonepsnum = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonepsnum);// 增加头部
//                    TextView turnovercheckinfotvtitleinfoonepsnum = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonepsnum); //  增加内容
//                    for (int x = 0; x < inforcashlistdizhi.size(); x++) {
//                        boxstr = boxstr + inforcashlistdizhi.get(x).getNumId() + ",";
//                        if(inforcashlistdizhi.get(x).getOrderType().equals("1")){
//                            psstr1= psstr1 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            tvCountone.setText("1:" + psstr1);
//                        }else if(inforcashlistdizhi.get(x).getOrderType().equals("2")){
//                            psstr2= psstr2 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            turnovercheckinfotvtitleonenumid.setText("2:" + psstr2);
//                        }else if(inforcashlistdizhi.get(x).getOrderType().equals("3"))
//                        {
//                            psstr3= psstr3 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            turnovercheckinfotvtitleoneordertype.setText("3:" + psstr2);
//                        }
//
//                    }
//                    tvTypeone.setText("订单编号:");
//
////                    tvCountone.setText("周转箱:" + boxstr);
////                    turnovercheckinfotvtitleonenumid.setText("封包编号:");
////                    turnovercheckinfotvtitleoneordertype.setText("订单类型:");
//
//                    turnovercheckinfotvtitleonepsnum.setText("配送单号:");
//                    turnovercheckinfotvtitleinfoonepsnum.setText("乱写1：" + psstr);
//                    linlayturnoverinfoone
//                            .addView(vonone);
//         循序一次为一个配送单    ----外层
//                    tvstate.setText("已完成" + inforcashlistdizhi.size() + "|||||" + inflistzhongkong.size() + "=" + inforcashlistxianjin.size());

//                            linlayturnoverinfo = (LinearLayout) findViewById(R.id.linlayturnoverinfo);
//                            View v = LayoutInflater.from(TurnOverInfoActivity.this)
//                                    .inflate(R.layout.turn_over_info_layout, null);
//
//                            TextView tvType = (TextView) v.findViewById(R.id.turnovercheckinfotvtitle);// 增加头部
//                            TextView tvCount = (TextView) v.findViewById(R.id.turnovercheckinfotvtitleinfo); //  增加内容
//
//                            TextView turnovercheckinfotvtitlenumid = v.findViewById(R.id.turnovercheckinfotvtitlenumid);
//                            TextView turnovercheckinfotvtitleinfonumid = v.findViewById(R.id.turnovercheckinfotvtitleinfonumid);
//
//                            TextView turnovercheckinfotvtitleordertype = v.findViewById(R.id.turnovercheckinfotvtitleordertype);
//                            TextView turnovercheckinfotvtitleinfoordertype = v.findViewById(R.id.turnovercheckinfotvtitleinfoordertype);
//                            TextView turnovercheckinfotvtitleorderpsnum = v.findViewById(R.id.turnovercheckinfotvtitleorderpsnum);
//                            TextView turnovercheckinfotvtitleinfoordersnum = v.findViewById(R.id.turnovercheckinfotvtitleinfoordersnum);
//                            String sss = "";
//                    if ("3".equals(infolist.get(i).getOrderList().get(0).getOrderType())) {
//                                OrderList orderList = new OrderList();
//                                orderList.setCvoun(infolist.get(i).getClearList().get(j).getPsNum());
//                                orderList.setNumId(infolist.get(i).getClearList().get(j).getPassBoxNum());
//                                orderList.setOrderType(infolist.get(i).getClearList().get(j).getBundleNum());
//                                orderList.setPsNum(infolist.get(i).getClearList().get(j).getPsNum());
//
//                                inforcashlist.add(orderList);
//                    } else if ("2".equals(infolist.get(i).getOrderList().get(0).getOrderType())) {
//                                OrderList orderList = new OrderList();
//                                orderList.setCvoun(infolist.get(i).getClearList().get(j).getPsNum());
//                                orderList.setNumId(infolist.get(i).getClearList().get(j).getPassBoxNum());
//                                orderList.setOrderType(infolist.get(i).getClearList().get(j).getBundleNum());
//                                orderList.setPsNum(infolist.get(i).getClearList().get(j).getPsNum());

//                    } else if ("1".equals(infolist.get(i).getOrderList().get(0).getOrderType())) {
//                                    OrderList orderList = new OrderList();
//                                    orderList.setCvoun(infolist.get(i).getClearList().get(j).getPsNum());
//                                    orderList.setNumId(infolist.get(i).getClearList().get(j).getPassBoxNum());
//                                    orderList.setOrderType(infolist.get(i).getClearList().get(j).getBundleNum());
//                                    orderList.setPsNum(infolist.get(i).getClearList().get(j).getPsNum());
//                            tvType.setText("配送单号:");
//                            tvCount.setText(infolist.get(i).getClearList().get(j).getPsNum());
//                            turnovercheckinfotvtitlenumid.setText("箱号:");
//                            turnovercheckinfotvtitleinfonumid.setText(infolist.get(i).getClearList().get(j).getPassBoxNum());
//                            turnovercheckinfotvtitleordertype.setText("锁扣号：");
//                            turnovercheckinfotvtitleinfoordertype.setText(infolist.get(i).getClearList().get(j).getBundleNum());
//                            turnovercheckinfotvtitleorderpsnum.setText("开始时间:");
//                            turnovercheckinfotvtitleinfoordersnum.setText(infolist.get(i).getClearList().get(j).getStateDate());
//                            linlayturnoverinfo.addView(v);


//                    }

//                        xianjin


//            }   配送单是关键分组
//                    String boxstr = "";
//                    String psstr = "";
//                    String psstr1 = "";
//                    String psstr2 = "";String psstr3 = "";


//                    linlayturnoverinfoone = (LinearLayout) findViewById(R.id.linlayturnoverinfoone);
//                    View vonone = LayoutInflater.from(TurnOverInfoActivity.this)
//                            .inflate(R.layout.turn_over_info_layoutone, null);
//                    TextView tvTypeone = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonecvoun);// 增加头部
//                    TextView tvCountone = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonecvoun); //  增加内容
//
//                    TextView turnovercheckinfotvtitleonenumid = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonenumid);// 增加头部
//                    TextView turnovercheckinfotvtitleinfoonenumid = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonenumid); //  增加内容
//
//                    TextView turnovercheckinfotvtitleoneordertype = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleoneordertype);// 增加头部
//                    TextView turnovercheckinfotvtitleinfooneordertype = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfooneordertype); //  增加内容
//                    TextView turnovercheckinfotvtitleonepsnum = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleonepsnum);// 增加头部
//                    TextView turnovercheckinfotvtitleinfoonepsnum = (TextView) vonone.findViewById(R.id.turnovercheckinfotvtitleinfoonepsnum); //  增加内容
//                    for (int x = 0; x < inforcashlistdizhi.size(); x++) {
//                        boxstr = boxstr + inforcashlistdizhi.get(x).getNumId() + ",";
//                        if(inforcashlistdizhi.get(x).getOrderType().equals("1")){
//                            psstr1= psstr1 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            tvCountone.setText("1:" + psstr1);
//                        }else if(inforcashlistdizhi.get(x).getOrderType().equals("2")){
//                            psstr2= psstr2 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            turnovercheckinfotvtitleonenumid.setText("2:" + psstr2);
//                        }else if(inforcashlistdizhi.get(x).getOrderType().equals("3"))
//                        {
//                            psstr3= psstr3 + inforcashlistdizhi.get(x).getNumId() + ",";
//                            turnovercheckinfotvtitleoneordertype.setText("3:" + psstr2);
//                        }
//
//                    }
//                    tvTypeone.setText("订单编号:");
//
////                    tvCountone.setText("周转箱:" + boxstr);
////                    turnovercheckinfotvtitleonenumid.setText("封包编号:");
////                    turnovercheckinfotvtitleoneordertype.setText("订单类型:");
//
//                    turnovercheckinfotvtitleonepsnum.setText("配送单号:");
//                    turnovercheckinfotvtitleinfoonepsnum.setText("乱写1：" + psstr);
//                    linlayturnoverinfoone
//                            .addView(vonone);
                    break;
                default:


                    break;


            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_over_info);
        linno = getIntent().getStringExtra("LineNo");
        selectType=getIntent().getStringExtra("selectType");
        manageryuan = new ManagerClass();
        initView();
    }

    private void initView() {
        turn_title_tvinfo = findViewById(R.id.turn_title_tvinfo);
        turn_title_pgdan = findViewById(R.id.tvpaigongdanw);
        kaishijian = findViewById(R.id.kaishijian);
        tvpsType = findViewById(R.id.tvpsType);//  配送单类型
        tvstate = findViewById(R.id.tvstate);
        toi_update = findViewById(R.id.toi_update);
        toi_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                infolist.clear();
//                linlayturnoverinfoone.
                LoadData1();
            }
        });
        toi_back = findViewById(R.id.toi_back);
        toi_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOverInfoActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData1();
    }

    private String reusltCardnumber;

    private void LoadData1() {

        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    infolist.clear();
                    reusltCardnumber = new TureOverBoxCheckServer().getListinfo(linno,selectType);
//                    String s =     "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000015\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000029\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000032\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY13000016\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000179ZZ\\\",\\\"bundleNum\\\":\\\"\\\"},{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000180ZZ\\\",\\\"bundleNum\\\":\\\"\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"100\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"百元券别\\\",\\\"tcktNum\\\":\\\"100.00\\\"},{\\\"tckt\\\":\\\"0050A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"200\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"五十元券别\\\",\\\"tcktNum\\\":\\\"50.00\\\"},{\\\"tckt\\\":\\\"0005B\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"伍角券别\\\",\\\"tcktNum\\\":\\\"0.50\\\"},{\\\"tckt\\\":\\\"0005A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":5000.00,\\\"strAmt\\\":\\\"5,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"五元券别\\\",\\\"tcktNum\\\":\\\"5.00\\\"},{\\\"tckt\\\":\\\"0010A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"十元券别\\\",\\\"tcktNum\\\":\\\"10.00\\\"},{\\\"tckt\\\":\\\"0001A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"4500\\\",\\\"amt\\\":4500.00,\\\"strAmt\\\":\\\"4,500.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"壹元券别\\\",\\\"tcktNum\\\":\\\"1.00\\\"},{\\\"tckt\\\":\\\"0002C\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"12500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"tcktName\\\":\\\"二分券别\\\",\\\"tcktNum\\\":\\\"0.02\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlName\\\":\\\"农商行转账支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"003\\\",\\\"dtlName\\\":\\\"汇票申请\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10101,\\\"dtlEndNo\\\":10200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"004\\\",\\\"dtlName\\\":\\\"信汇\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000101,\\\"dtlEndNo\\\":10000200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"604\\\",\\\"dtlName\\\":\\\"津城通卡２０００\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000201,\\\"dtlEndNo\\\":10000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"002\\\",\\\"dtlName\\\":\\\"2006版农商行现金支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":20000201,\\\"dtlEndNo\\\":20000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000004\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000005\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000007\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000009\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000011\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000003\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000013\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"400\\\",\\\"amt\\\":40000.00,\\\"strAmt\\\":\\\"40,000.00\\\",\\\"amtb\\\":1.00,\\\"strAmtb\\\":\\\"1.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"百元券别\\\",\\\"tcktNum\\\":\\\"100.00\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102001010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlName\\\":\\\"农商行转账支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000018\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000028\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY13000017\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]}]\"}";


//ao重要！！！！！

////                               "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\\\"
//                    String s = "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000015\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000029\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000032\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY13000016\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000179ZZ\\\",\\\"bundleNum\\\":\\\"\\\"},{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000180ZZ\\\",\\\"bundleNum\\\":\\\"\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"100\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"百元券别\\\",\\\"tcktNum\\\":\\\"100.00\\\"},{\\\"tckt\\\":\\\"0050A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"200\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"五十元券别\\\",\\\"tcktNum\\\":\\\"50.00\\\"},{\\\"tckt\\\":\\\"0005B\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"伍角券别\\\",\\\"tcktNum\\\":\\\"0.50\\\"},{\\\"tckt\\\":\\\"0005A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":5000.00,\\\"strAmt\\\":\\\"5,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"五元券别\\\",\\\"tcktNum\\\":\\\"5.00\\\"},{\\\"tckt\\\":\\\"0010A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"十元券别\\\",\\\"tcktNum\\\":\\\"10.00\\\"},{\\\"tckt\\\":\\\"0001A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"4500\\\",\\\"amt\\\":4500.00,\\\"strAmt\\\":\\\"4,500.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"壹元券别\\\",\\\"tcktNum\\\":\\\"1.00\\\"},{\\\"tckt\\\":\\\"0002C\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"12500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"tcktName\\\":\\\"二分券别\\\",\\\"tcktNum\\\":\\\"0.02\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlName\\\":\\\"农商行转账支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"003\\\",\\\"dtlName\\\":\\\"汇票申请\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10101,\\\"dtlEndNo\\\":10200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"004\\\",\\\"dtlName\\\":\\\"信汇\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000101,\\\"dtlEndNo\\\":10000200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"604\\\",\\\"dtlName\\\":\\\"津城通卡２０００\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000201,\\\"dtlEndNo\\\":10000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"002\\\",\\\"dtlName\\\":\\\"2006版农商行现金支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":20000201,\\\"dtlEndNo\\\":20000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000004\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000005\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000007\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000009\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000011\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000003\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000013\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"400\\\",\\\"amt\\\":40000.00,\\\"strAmt\\\":\\\"40,000.00\\\",\\\"amtb\\\":1.00,\\\"strAmtb\\\":\\\"1.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"202102001010011001\\\",\\\"tcktName\\\":\\\"百元券别\\\",\\\"tcktNum\\\":\\\"100.00\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102001010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlName\\\":\\\"农商行转账支票\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000018\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000028\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY13000017\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]}]\"}";




//                    String s = "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\"psNum\":\"90405010020210205113956\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\"\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"cvoun\":\"DZXD12112021020301\",\"numId\":\"1211DY13000014\",\"orderType\":\"3\",\"psNum\":\"90405010020210205113956\"},{\"cvoun\":\"DZXD12112021020301\",\"numId\":\"1211DY13000015\",\"orderType\":\"3\",\"psNum\":\"90405010020210205113956\"},{\"cvoun\":\"DZXD12112021020302\",\"numId\":\"1211DY12000029\",\"orderType\":\"3\",\"psNum\":\"90405010020210205113956\"},{\"cvoun\":\"DZXD12112021020302\",\"numId\":\"1211DY12000032\",\"orderType\":\"3\",\"psNum\":\"90405010020210205113956\"},{\"cvoun\":\"DZXD12112021020302\",\"numId\":\"1211DY13000016\",\"orderType\":\"3\",\"psNum\":\"90405010020210205113956\"}],\"clearList\":[{\"psNum\":\"90405010020210205113956\",\"passBoxNum\":\"ZH000179ZZ\",\"bundleNum\":\"\"},{\"psNum\":\"90405010020210205113956\",\"passBoxNum\":\"ZH000180ZZ\",\"bundleNum\":\"\"}]},{\"psNum\":\"90405010020210208095442\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"tckt\":\"0100A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"100\",\"amt\":10000.00,\"strAmt\":\"10,000.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"百元券别\",\"tcktNum\":\"100.00\"},{\"tckt\":\"0050A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"200\",\"amt\":10000.00,\"strAmt\":\"10,000.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"五十元券别\",\"tcktNum\":\"50.00\"},{\"tckt\":\"0005B\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"500\",\"amt\":250.00,\"strAmt\":\"250.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"伍角券别\",\"tcktNum\":\"0.50\"},{\"tckt\":\"0005A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"1000\",\"amt\":5000.00,\"strAmt\":\"5,000.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"五元券别\",\"tcktNum\":\"5.00\"},{\"tckt\":\"0010A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"1000\",\"amt\":10000.00,\"strAmt\":\"10,000.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"十元券别\",\"tcktNum\":\"10.00\"},{\"tckt\":\"0001A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"4500\",\"amt\":4500.00,\"strAmt\":\"4,500.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"壹元券别\",\"tcktNum\":\"1.00\"},{\"tckt\":\"0002C\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"12500\",\"amt\":250.00,\"strAmt\":\"250.00\",\"amtb\":0.00,\"strAmtb\":\"0.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102108010011001\",\"tcktName\":\"二分券别\",\"tcktNum\":\"0.02\"}],\"clearList\":[{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000030ZZ\",\"bundleNum\":\"123\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000037ZZ\",\"bundleNum\":\"12317\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000038ZZ\",\"bundleNum\":\"12318\"}]},{\"psNum\":\"90405010020210208095442\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"cvoun\":\"202102108010011002\",\"num\":\"100\",\"dtlType\":\"001\",\"dtlName\":\"农商行转账支票\",\"dtlNums\":100,\"dtlStartNo\":10001,\"dtlEndNo\":10100,\"orderType\":\"2\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"202102108010011002\",\"num\":\"100\",\"dtlType\":\"003\",\"dtlName\":\"汇票申请\",\"dtlNums\":100,\"dtlStartNo\":10101,\"dtlEndNo\":10200,\"orderType\":\"2\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"202102108010011002\",\"num\":\"100\",\"dtlType\":\"004\",\"dtlName\":\"信汇\",\"dtlNums\":100,\"dtlStartNo\":10000101,\"dtlEndNo\":10000200,\"orderType\":\"2\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"202102108010011002\",\"num\":\"100\",\"dtlType\":\"604\",\"dtlName\":\"津城通卡２０００\",\"dtlNums\":100,\"dtlStartNo\":10000201,\"dtlEndNo\":10000300,\"orderType\":\"2\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"202102108010011002\",\"num\":\"100\",\"dtlType\":\"002\",\"dtlName\":\"2006版农商行现金支票\",\"dtlNums\":100,\"dtlStartNo\":20000201,\"dtlEndNo\":20000300,\"orderType\":\"2\",\"psNum\":\"90405010020210208095442\"}],\"clearList\":[{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000030ZZ\",\"bundleNum\":\"123\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000037ZZ\",\"bundleNum\":\"12317\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000038ZZ\",\"bundleNum\":\"12318\"}]},{\"psNum\":\"90405010020210208095442\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000004\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000005\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000007\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000009\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000011\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY11000012\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY12000003\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY12000012\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY12000013\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"},{\"cvoun\":\"DZXD12112021020801\",\"numId\":\"1211DY12000014\",\"orderType\":\"3\",\"psNum\":\"90405010020210208095442\"}],\"clearList\":[{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000030ZZ\",\"bundleNum\":\"123\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000037ZZ\",\"bundleNum\":\"12317\"},{\"psNum\":\"90405010020210208095442\",\"passBoxNum\":\"ZH000038ZZ\",\"bundleNum\":\"12318\"}]},{\"psNum\":\"90405010020210222141300\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"tckt\":\"0100A\",\"flag\":\"0\",\"flagName\":\"完整券\",\"num\":\"400\",\"amt\":40000.00,\"strAmt\":\"40,000.00\",\"amtb\":1.00,\"strAmtb\":\"1.00\",\"cshcd\":\"\",\"orderType\":\"1\",\"psNum\":\"202102001010011001\",\"tcktName\":\"百元券别\",\"tcktNum\":\"100.00\"}],\"clearList\":[{\"psNum\":\"90405010020210222141300\",\"passBoxNum\":\"ZH000089ZZ\",\"bundleNum\":\"1\"}]},{\"psNum\":\"90405010020210222141300\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"cvoun\":\"202102001010011002\",\"num\":\"100\",\"dtlType\":\"001\",\"dtlName\":\"农商行转账支票\",\"dtlNums\":100,\"dtlStartNo\":10001,\"dtlEndNo\":10100,\"orderType\":\"2\",\"psNum\":\"90405010020210222141300\"}],\"clearList\":[{\"psNum\":\"90405010020210222141300\",\"passBoxNum\":\"ZH000089ZZ\",\"bundleNum\":\"1\"}]},{\"psNum\":\"90405010020210222141300\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\",\"orderList\":[{\"cvoun\":\"DZXD12112021022201\",\"numId\":\"1211DY12000018\",\"orderType\":\"3\",\"psNum\":\"90405010020210222141300\"},{\"cvoun\":\"DZXD12112021022201\",\"numId\":\"1211DY12000028\",\"orderType\":\"3\",\"psNum\":\"90405010020210222141300\"},{\"cvoun\":\"DZXD12112021022201\",\"numId\":\"1211DY13000017\",\"orderType\":\"3\",\"psNum\":\"90405010020210222141300\"}],\"clearList\":[{\"psNum\":\"90405010020210222141300\",\"passBoxNum\":\"ZH000089ZZ\",\"bundleNum\":\"1\"}]}]\"}";
//
//                    String s =   "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\"psNum\":\"90405010020210205113956\",\"psType\":\"1\",\"org\":\"904050100\",\"tonod\":\"988050000\",\"state\":\"01\",\"orgName\":\"小淀支行\",\"toNodName\":\"总行现金管理中心\"}]\"}";


//   guaobao jibiede chaun!!!!!!!!
//                    String s = "{\"code\":\"00\",\"msg\":\"成功\",\"params\":\"[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020301\\\",\\\"numId\\\":\\\"1211DY13000015\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000029\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY12000032\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020302\\\",\\\"numId\\\":\\\"1211DY13000016\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210205113956\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000179ZZ\\\",\\\"bundleNum\\\":\\\"\\\"},{\\\"psNum\\\":\\\"90405010020210205113956\\\",\\\"passBoxNum\\\":\\\"ZH000180ZZ\\\",\\\"bundleNum\\\":\\\"\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"100\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0050A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"200\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0005B\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0005A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":5000.00,\\\"strAmt\\\":\\\"5,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0010A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"1000\\\",\\\"amt\\\":10000.00,\\\"strAmt\\\":\\\"10,000.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0001A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"4500\\\",\\\"amt\\\":4500.00,\\\"strAmt\\\":\\\"4,500.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"tckt\\\":\\\"0002C\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"12500\\\",\\\"amt\\\":250.00,\\\"strAmt\\\":\\\"250.00\\\",\\\"amtb\\\":0.00,\\\"strAmtb\\\":\\\"0.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"003\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10101,\\\"dtlEndNo\\\":10200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"004\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000101,\\\"dtlEndNo\\\":10000200,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"604\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10000201,\\\"dtlEndNo\\\":10000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"202102108010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"002\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":20000201,\\\"dtlEndNo\\\":20000300,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000004\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000005\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000007\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000009\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000011\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY11000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000003\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000012\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000013\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"},{\\\"cvoun\\\":\\\"DZXD12112021020801\\\",\\\"numId\\\":\\\"1211DY12000014\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210208095442\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000030ZZ\\\",\\\"bundleNum\\\":\\\"123\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000037ZZ\\\",\\\"bundleNum\\\":\\\"12317\\\"},{\\\"psNum\\\":\\\"90405010020210208095442\\\",\\\"passBoxNum\\\":\\\"ZH000038ZZ\\\",\\\"bundleNum\\\":\\\"12318\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"tckt\\\":\\\"0100A\\\",\\\"flag\\\":\\\"0\\\",\\\"flagName\\\":\\\"完整券\\\",\\\"num\\\":\\\"400\\\",\\\"amt\\\":40000.00,\\\"strAmt\\\":\\\"40,000.00\\\",\\\"amtb\\\":1.00,\\\"strAmtb\\\":\\\"1.00\\\",\\\"cshcd\\\":\\\"\\\",\\\"orderType\\\":\\\"1\\\",\\\"psNum\\\":\\\"202102001010011001\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"202102001010011002\\\",\\\"num\\\":\\\"100\\\",\\\"dtlType\\\":\\\"001\\\",\\\"dtlNums\\\":100,\\\"dtlStartNo\\\":10001,\\\"dtlEndNo\\\":10100,\\\"orderType\\\":\\\"2\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]},{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"psType\\\":\\\"1\\\",\\\"org\\\":\\\"904050100\\\",\\\"tonod\\\":\\\"988050000\\\",\\\"state\\\":\\\"01\\\",\\\"orgName\\\":\\\"小淀支行\\\",\\\"toNodName\\\":\\\"总行现金管理中心\\\",\\\"orderList\\\":[{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000018\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY12000028\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"},{\\\"cvoun\\\":\\\"DZXD12112021022201\\\",\\\"numId\\\":\\\"1211DY13000017\\\",\\\"orderType\\\":\\\"3\\\",\\\"psNum\\\":\\\"90405010020210222141300\\\"}],\\\"clearList\\\":[{\\\"psNum\\\":\\\"90405010020210222141300\\\",\\\"passBoxNum\\\":\\\"ZH000089ZZ\\\",\\\"bundleNum\\\":\\\"1\\\"}]}]\"}";

//                    reusltCardnumber = s;
                    Log.e(TAG, "TurnOverInfoActivity测试=====" + reusltCardnumber);
                    if (!reusltCardnumber.equals("") || reusltCardnumber == null) {
                        Gson gson = new Gson();
//                        Log.e(TAG, "TurnOverInfoActivity测试数据源=====" +  s.toString());
//
//                        lineBoxVos = gson.fromJson( s, LineBoxVo[].class);
//                        List<LineBoxVo> listoCheck = Arrays.asList(lineBoxVos);
//                        List arrList = new ArrayList(listoCheck);
//
//                        infolist.addAll(arrList);

                        /**
                         * 解析串  解析结果为 ResultVo对应的3个属性
                         */
//                        ResultVo resultVo = gson.fromJson(reusltCardnumber, ResultVo.class);

                        /**
                         * 是否需要判断返回码根据自身业务
                         */
//                        if ("00".equals(resultVo.getCode())) {
//
//                        }

                        /**
                         * 获取Params 也就是上面json串中的数据
                         */
                        List<LineBoxVo> lineBoxVo = gson.fromJson(reusltCardnumber, new TypeToken<List<LineBoxVo>>() {
                        }.getType());
                        infolist.clear();
                        infolist = lineBoxVo;

                        handler.sendEmptyMessage(6);
                    } else {
                        handler.sendEmptyMessage(3);
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
}
