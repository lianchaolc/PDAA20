package com.messagebox;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.ljsw.tjbankpda.main.QingfenRenwuActivity;
import com.ljsw.tjbankpda.main.QinglingWangdianActivity;
import com.ljsw.tjbankpda.main.QinglingZhouzhuanxiangluruActivity;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.Box;
import com.ljsw.tjbankpda.qf.entity.QingfenRemwu;
import com.ljsw.tjbankpda.qf.entity.ZhuanxiangTongji;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.BianyiType;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.manager.classs.pad.ManagerClass;

public class ResultMsg {
    private Handler okHandle;
    private Handler okHandleOnlyDZ;//  只有抵质押品提交时候使用
    private Handler timeoutHandle;
    private Handler timeoutHandleOnlyDZ;
    private Handler handler;
    private Handler handler2Button;
    private String dingdanId;// 订单ID
    private String boxNum;// 周转性编号
    private String onceKeyNum;// 一次性锁扣编号

    private String dingdanIdOnlyDZ;
    private String boxNumOnlyDZ;// 周转性编号
    private String onceKeyNumOnlyDZ;// 一次性锁扣编号
    private GolbalView g;

    GolbalView getG() {
        if (g == null) {
            g = new GolbalView();
        }
        return g;
    }

    private View v;

    int i = 0;

    /**
     * 弹出窗口提示信息(一个Button)
     *
     * @param a
     * @param msg
     */
    public void resultmsg(Activity a, String msg, boolean flag) {

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
            }
        });
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息(两个Button)
     *
     * @param a
     * @param msg
     */
    public void resultmsgHas2(Activity a, String msg, boolean flag, final int what) {

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
                handler.sendEmptyMessage(what);

            }
        });
        ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
            }
        });
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息(1个Button)
     *
     * @param a
     * @param msg
     */
    public void resultmsgHas1(Activity a, String msg, boolean flag, final int what) {

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
        ((View) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
                handler.sendEmptyMessage(what);
            }
        });
        ((TextView) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息(两个Button,没有图片,)
     *
     * @param a
     * @param msg
     */
    public void resultmsgHas2(Activity a, String msg) {

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        ((ImageView) v.findViewById(R.id.result_img)).setVisibility(View.GONE);

        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_exit)).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
                handler2Button.sendEmptyMessage(1);
            }
        });
        ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
            }
        });
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 请领周转箱提交请领信息.
     *
     * @param a
     * @param msg
     */
    public void submitZzxInfo(Activity a, final Context context, String msg, boolean flag) {
        okHandle = new Handler() {// 数据获取成功handler
            public void handleMessage(Message msg) {
                ((TextView) v.findViewById(R.id.result_text)).setText("提交成功");
                // 初始化全局变量
                Mapplication.getApplication().boxLtXianjing.clear();
                Mapplication.getApplication().boxLtZhongkong.clear();
                Mapplication.getApplication().boxLtDizhi.clear();
                Mapplication.getApplication().zxLtXianjing.clear();
                Mapplication.getApplication().zxLtZhongkong.clear();
                Mapplication.getApplication().zxTjDizhi = new ZhuanxiangTongji();
                Mapplication.getApplication().ltDizhiNum.clear();
                Mapplication.getApplication().ltZzxNumber.clear();

                ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
                ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.result_exit)).setText("即将返回任务列表...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);
                        remove();
                    }
                }).start();
                Intent intent = new Intent("com.ljsw.pda3.QinglingWangdian");
                context.startActivity(intent);
            }

            ;
        };
        /**
         * 请领周转箱提交请领信息.
         *
         * @param a
         * @param msg
         */


        timeoutHandle = new Handler() {// 连接超时handler
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
//					((TextView)v.findViewById(R.id.result_text)).setText("连接超时");
                    ((TextView) v.findViewById(R.id.result_text)).setText("连接超时");
                    ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
                    ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.result_ok)).setText("继续");
                    ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            getDate();
                        }
                    });
                    ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            remove();
                        }
                    });
                }
                if (msg.what == 1) {
                    ((TextView) v.findViewById(R.id.result_text)).setText("网络连接失败");
                    ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
                    ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.result_ok)).setText("继续");
                    ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            remove();
                        }
                    });
                    ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            getDate();
                        }
                    });
                }
            }

            ;
        };

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                StringBuffer sbBox = new StringBuffer();
                sbBox.append("zhouzhuanxiang:");
                StringBuffer sbKey = new StringBuffer();
                sbKey.append("suokou:");
                StringBuffer sbOrderNum = new StringBuffer();
                sbOrderNum.append("dingdan:");

                // -------------------------------------------------------------------------------------------
                List<Box> lt = Mapplication.getApplication().ltZzxNumber;
                int i = 0;
                List<String> dingdan = new ArrayList<String>();
                for (Box b : lt) {
                    i++;
                    sbBox.append(b.getType());
                    sbKey.append(b.getCount());

                    if (!dingdan.contains(b.getMark())) {
                        sbOrderNum.append(b.getMark());
                        dingdan.add(b.getMark());
                    }

                    if (i < lt.size()) {
                        sbBox.append(",");
                        sbKey.append(",");
                    }
                }
                dingdanId = sbOrderNum + "";
                boxNum = sbBox + "";
                onceKeyNum = sbKey + "";
                String zzxMsg = sbOrderNum + "  " + sbBox + "  " + sbKey;
                System.out.println("周转箱提交数据:\n" + zzxMsg);
                ((TextView) v.findViewById(R.id.result_text)).setText("提交中...");
                ((TextView) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
                ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
                getDate();
            }
        });
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);

    }

    public void submitZzxInfoOnlyDZ(Activity a, final Context context, String msg, boolean flag) {
        okHandleOnlyDZ = new Handler() {// 数据获取成功handler
            public void handleMessage(Message msg) {
                ((TextView) v.findViewById(R.id.result_text)).setText("提交成功");
                // 初始化全局变量
                Mapplication.getApplication().boxLtXianjing.clear();
                Mapplication.getApplication().boxLtZhongkong.clear();
                Mapplication.getApplication().boxLtDizhi.clear();
                Mapplication.getApplication().zxLtXianjing.clear();
                Mapplication.getApplication().zxLtZhongkong.clear();
                Mapplication.getApplication().zxTjDizhi = new ZhuanxiangTongji();
                Mapplication.getApplication().ltDizhiNum.clear();
                Mapplication.getApplication().ltZzxNumber.clear();

                ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
                ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.result_exit)).setText("即将返回任务列表...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);
                        remove();
                    }
                }).start();
                Intent intent;
                intent = new Intent("com.ljsw.pda3.QinglingWangdian");// 订单没做完
//                intent = new Intent("com.ljsw.pda4.CleanMangeCheckTaskQingfenRenwuActivity");//  订单做完了
                context.startActivity(intent);

            }

        };
        timeoutHandleOnlyDZ = new Handler() {// 连接超时handler
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
//					((TextView)v.findViewById(R.id.result_text)).setText("连接超时");
                    ((TextView) v.findViewById(R.id.result_text)).setText("连接超时");
                    ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
                    ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.result_ok)).setText("继续");
                    ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            getDateByDZ();
                        }
                    });
                    ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            remove();
                        }
                    });
                }
                if (msg.what == 1) {
                    ((TextView) v.findViewById(R.id.result_text)).setText("网络连接失败");
                    ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
                    ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.result_ok)).setText("继续");
                    ((TextView) v.findViewById(R.id.result_exit)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            remove();
                        }
                    });
                    ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            getDateByDZ();
                        }
                    });
                }
            }

            ;
        };

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.VISIBLE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.VISIBLE);
        ((TextView) v.findViewById(R.id.result_ok)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                StringBuffer sbBox = new StringBuffer();
                sbBox.append("");
                StringBuffer sbKey = new StringBuffer();
                sbKey.append("");
                StringBuffer sbOrderNum = new StringBuffer();
                sbOrderNum.append("");
                String  dingdana="";
                // -------------------------------------------------------------------------------------------
                List<Box> lt = Mapplication.getApplication().ltZzxNumber;
                int i = 0;
                List<String> dingdan = new ArrayList<String>();
                for (Box b : lt) {
                    i++;
                    sbBox.append(b.getType());
                    sbKey.append(b.getCount());

                    if (!dingdan.contains(b.getMark())) {
                        sbOrderNum.append(b.getMark());
                        dingdan.add(b.getMark());
                    }

                    if (i < lt.size()) {
                        sbBox.append("-");
                        sbKey.append("-");
                        String news = b.getMark();
                        String codedeal=news.toString().replaceAll(",","-");
                        dingdana=codedeal;
                    }else{
                        String news = b.getMark();
                        String codedeal=news.toString().replaceAll(",","-");
                        dingdana=codedeal;
                    }
                }
                dingdanIdOnlyDZ = dingdana + "";
                boxNumOnlyDZ = sbBox + "-";
                onceKeyNumOnlyDZ = sbKey + "-";
                String zzxMsg = dingdanIdOnlyDZ + "  " + sbBox + "  " + sbKey;
                System.out.println("周转箱提交数据:\n" + zzxMsg);
                ((TextView) v.findViewById(R.id.result_text)).setText("提交中...");
                ((TextView) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
                ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
                getDateByDZ();
            }
        });
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息(没有Button)
     *
     * @param a
     * @param msg
     */
    public void resultOnlyMsg(Activity a, String msg, boolean flag) {
        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
        if (flag) {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.sccuss);
        } else {
            ((ImageView) v.findViewById(R.id.result_img)).setImageResource(R.drawable.fail_img);
        }
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息(没有Button)
     *
     * @param a
     * @param msg
     */
    public void resultOnlyMsg(Activity a, String msg) {
        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
        ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.result_exit)).setVisibility(View.GONE);
        ((ImageView) v.findViewById(R.id.result_img)).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);
    }

    /**
     * 弹出窗口提示信息
     *
     * @param a
     * @param msg
     */
    public void resultmsg(Activity a, String msg) {

        if (v == null) {
            v = GolbalView.getLF(a).inflate(R.layout.c_msg_result, null);
        }
        ((ImageView) v.findViewById(R.id.result_img)).setBackgroundResource(R.drawable.sccuss);
        ((TextView) v.findViewById(R.id.result_text)).setText(msg);
        getG().createFloatView(a, v);

    }

    public void remove() {
        getG().removeV(v);
        v = null;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setHandler2Button(Handler handler) {
        this.handler2Button = handler;
    }

    private void getDate() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    boolean flag = new QingfenRenwuService().submitZzxInfo(Mapplication.getApplication().renwudan,
                            Mapplication.getApplication().UserId, boxNum,
							dingdanId,
							onceKeyNum,
                            Mapplication.getApplication().jigouid);
                    if (flag) {
                        okHandle.sendEmptyMessage(0);
                    } else {
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    timeoutHandle.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
//					timeoutHandle.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    /**
     * *arg0=corpId  904050100  目标机构
     * arg1=userId  100029 登陆用户id
     * arg2=clearTaskNum  RW1000020220225113658 清分任务编号
     * arg3=passboxNums ZH000009ZZ-ZH000011ZZ- 周转箱(袋)编号 （要与下面的一次性锁编号的顺序一一对应）
     * arg4=bundleNums 111-2222- 一次性锁扣编号
     * arg5=cvounWds DZXD1211202202250002-  订单编号(清分任务下面的)
     */
    private void getDateByDZ() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    boolean flag = new QingfenRenwuService().submitZzxInfoOnlyDZ(Mapplication.getApplication().jigouid,
                            Mapplication.getApplication().UserId, Mapplication.getApplication().renwudan, boxNumOnlyDZ, onceKeyNumOnlyDZ,
                            dingdanIdOnlyDZ);
                    if (flag) {
                        okHandleOnlyDZ.sendEmptyMessage(0);
                    } else {
                        // 更改
//						timeoutHandle.sendEmptyMessage(1);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    timeoutHandleOnlyDZ.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    timeoutHandleOnlyDZ.sendEmptyMessage(1);
                }
            }
        }).start();
    }


    /**
     * 避免弹出提交成功后出现网络错误
     *
     * @param a
     * @param context
     * @param msg
     * @param flag
     */
    public void submitZzxInfo1(Activity a, final Context context, String msg, boolean flag) {
        okHandle = new Handler() {// 数据获取成功handler
            public void handleMessage(Message msg) {
                ((TextView) v.findViewById(R.id.result_text)).setText("提交成功");
                // 初始化全局变量
                Mapplication.getApplication().boxLtXianjing.clear();
                Mapplication.getApplication().boxLtZhongkong.clear();
                Mapplication.getApplication().boxLtDizhi.clear();
                Mapplication.getApplication().zxLtXianjing.clear();
                Mapplication.getApplication().zxLtZhongkong.clear();
                Mapplication.getApplication().zxTjDizhi = new ZhuanxiangTongji();
                Mapplication.getApplication().ltDizhiNum.clear();
                Mapplication.getApplication().ltZzxNumber.clear();

                ((TextView) v.findViewById(R.id.result_ok)).setVisibility(View.GONE);
                ((View) v.findViewById(R.id.result_sep)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.result_exit)).setText("即将返回任务列表...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("com.ljsw.pda3.QinglingWangdian");
                context.startActivity(intent);
            }

            ;
        };

    }

}
