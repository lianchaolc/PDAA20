package com.clearadmin.pda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.clearadmin.biz.AddMoneyConfirmBiz;
import com.clearadmin.biz.GetAddMoneySumBiz;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handheld.UHF.UhfManager;
import com.imple.getnumber.AddMoneygetNum;
import com.imple.getnumber.GetMoneyNum;
import com.ljsw.tjbankpad.baggingin.activity.chuku.service.GetResistCollateralBaggingService;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.DiZhiYaPinRuKu;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.AccountInfomationReturnService;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.OutboundBorrowingTaskListEntity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.ZhangHuZiLiaoChuKuJieYueActivity;
import com.manager.classs.pad.ManagerClass;
import com.moneyboxadmin.pda.BankDoublePersonLogin;
import com.sublibrary.ercodescan.SubLibraryGetScanUtil;
import com.sublibrary.ercodescan.SubLibraryScanTwoServer;
import com.sublibrary.ercodescan.entity.LibraryCodeScan;

import org.w3c.dom.Text;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import a20.cn.uhf.admin.Tools;
import afu.util.SoundUtil;
import afu.util.Util;
import hdjc.rfid.operator.RFID_Device;


/***
 * 分库二维码扫描获钞袋的信息（编号） 和货位的信息等  复值  当前信息 写入rfid 标签   和读取验证成功
 * 2  cunzai
 *
 * DZ202021001093539471
 *
 * 一次扫描完成二维码有时间间隔 大约5s
 */
public class SonLibraryCodeScanActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SonLibraryCodeScanActivity";
    private TextView sonlibraytvshwo, netreusltcode;//  扫描二维码
    private Button btnupcode;// 网络请求获取数据按钮
    private TextView makeandrreadcard;//
    private boolean first; // 只执行一次
    private TextView cardresult;

    private int toal;//  总钱数
    // 筛选数据
    private String StrintCode;//传递的号码


    AddMoneygetNum scanbox = new AddMoneygetNum(); // 钞箱编号扫描类 ///rfid
    GetMoneyNum scanMoneyNum = new GetMoneyNum(); // 钱捆编号扫描类
    SubLibraryGetScanUtil scanUtil = new SubLibraryGetScanUtil();//  新二维码扫描数据20201223

    // 筛选数据
    private AddMoneygetNum addMoneygetNum;

    AddMoneygetNum getAddMoneygetNum() {
        return addMoneygetNum = addMoneygetNum == null ? new AddMoneygetNum() : addMoneygetNum;
    }

    // 获取钱捆编号
    private GetMoneyNum getMoneyNum;

    GetMoneyNum getGetMoneyNum() {
        return getMoneyNum = getMoneyNum == null ? new GetMoneyNum() : getMoneyNum;
    }

    //
    private AddMoneyConfirmBiz addMoneyConfirm;

    AddMoneyConfirmBiz getAddMoneyConfirm() {
        return addMoneyConfirm = addMoneyConfirm == null ? new AddMoneyConfirmBiz() : addMoneyConfirm;
    }

    private GetAddMoneySumBiz getAddMoneySum;

    GetAddMoneySumBiz getGetAddMoneySum() {
        return getAddMoneySum = getAddMoneySum == null ? new GetAddMoneySumBiz() : getAddMoneySum;
    }

    private SubLibraryGetScanUtil subLibraryGetScanUtil;

    SubLibraryGetScanUtil getScantwo1() {
        return subLibraryGetScanUtil = subLibraryGetScanUtil == null ? new SubLibraryGetScanUtil() : subLibraryGetScanUtil;
    }

    SubLibraryScanTwoServer SubLibraryScanTwoServer;

    SubLibraryScanTwoServer SubLibraryScanTwoServer() {
        return SubLibraryScanTwoServer = SubLibraryScanTwoServer == null ? new SubLibraryScanTwoServer() : SubLibraryScanTwoServer;
    }

    private ManagerClass managerClass;
    private RFID_Device rfid;

    RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    Bundle b;
    private ManagerClass manager;
    private UhfManager UhfManager;
    private Button librarytwocode_update;
    private ImageView iv_twoblack;
    View.OnClickListener onclickreplace, onClick;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁止休睡眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_son_library_code_scan);
        managerClass = new ManagerClass();
        managerClass.getGolbalView().Init(this);
        UhfManager = UhfManager.getInstance();
        if (UhfManager == null) {
            Toast.makeText(getApplicationContext(), "串口初始化失败", 400).show();
            return;
        }
        initView();
        btnupcode = (Button) findViewById(R.id.btnupcode);// 获取网络数据按钮
        btnupcode.setOnClickListener(this);
        sonlibraytvshwo = findViewById(R.id.sonlibraytvshwo);
        sonlibraytvshwo.setOnClickListener(this);

        makeandrreadcard = (TextView) findViewById(R.id.makeandrreadcard);
        Util.initSoundPool(this);//  声音位置
        manager = new ManagerClass();
        onClick = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                manager.getAbnormal().remove();
                NetInfoTwocode();
            }
        };
        getRfid().stop_a20();
        getScantwo1().handler = new Handler() {
            Bundle bundlemoney;

            @SuppressLint("LongLogTag")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        netreusltcode.setText("");
                        netreuslttype.setText("");//  类型find
                        netreusltcontroller.setText("");// 机构
                        netreusltbianno.setText("");
                        netreusltcoutrsname.setText("");
                        netreusltnum.setText("");
                        //扫描到一二维码, 语音提示
                        Util.play(1, 0);
                        Log.e(TAG, "!!!!!!!!!!!!!!!" + msg.getData());
                        bundlemoney = msg.getData();
                        if (bundlemoney != null) {
                            // 接收统计扫描钞捆的总金额
                            StrintCode = bundlemoney.getString("money");//  获取上个工具类扫描的值
                        }

                        // 当前扫描总金额
                        sonlibraytvshwo.setText(StrintCode);
                        if (null == StrintCode || StrintCode.equals("")) {
                        } else {
                            sonlibraymadekar.setFocusable(true);
//                            sonlibraymadekar.setBackgroundResource(R.drawable.buttom_select_press);
                            sonlibraymadekar.setVisibility(View.VISIBLE);
                        }

                        getRfid().addNotifly(scanUtil);
                        // 打开二维码扫描
                        managerClass.getRfid().scanOpen();
                        break;
                }
            }
        };

        getGetAddMoneySum().handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                managerClass.getRuning().remove();

                switch (msg.what) {
                    case 1:
                        // 当前钞箱金额
                        Double moneyToal = Double.parseDouble(GetAddMoneySumBiz.box.getMoney());
                        netreusltcode.setText((moneyToal / 10000) + "万");

                        // 钞箱编号和品牌
                        netreusltcode.setText(GetAddMoneySumBiz.box.getNum() + "    " + GetAddMoneySumBiz.box.getBrand());

                        // 增加二维码接口通知
                        getRfid().addNotifly(scanMoneyNum);
                        // 打开二维码扫描
                        SoundUtil.mContext = SonLibraryCodeScanActivity.this;
                        managerClass.getRfid().scanOpen();

                        break;
                    case 0:
                        managerClass.getSureCancel().makeSuerCancel(SonLibraryCodeScanActivity.this, "没有这个钞箱的记录",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                        // 开始扫描
                                        getRfid().start_a20();
                                    }
                                }, true);
                        break;
                    case -1:
                        managerClass.getAbnormal().timeout(SonLibraryCodeScanActivity.this, "连接异常!", null);
                        break;
                    case -4:
                        managerClass.getAbnormal().timeout(SonLibraryCodeScanActivity.this, "连接超时!", null);
                        break;
                }
            }
        };
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SonLibraryCodeScanActivity.this, "加载超时,重试?", onClick);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SonLibraryCodeScanActivity.this, "网络连接失败,重试?", onClick);
                    break;
                case 2:

                    netreusltcode.setText(LibraryCodeScanlist.get(0).getStockCode());
                    netreuslttype.setText(LibraryCodeScanlist.get(0).getItemTypeName());//  类型find
                    netreusltcontroller.setText(LibraryCodeScanlist.get(0).getOrgName());// 机构
                    netreusltbianno.setText(LibraryCodeScanlist.get(0).getItemCode());

                    netreusltcoutrsname.setText(LibraryCodeScanlist.get(0).getCustomerName());
                    netreusltnum.setText(
                            LibraryCodeScanlist.get(0).getCabinetNumber() + "-" + LibraryCodeScanlist.get(0).getFaceNumber() + "-" + LibraryCodeScanlist.get(0).getGridNumber() + "-" + LibraryCodeScanlist.get(0).getPartitionNumber());

                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(SonLibraryCodeScanActivity.this, "没有任务", new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;

                case 4:
                    Toast.makeText(SonLibraryCodeScanActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(SonLibraryCodeScanActivity.this, "无当前二维码结果请联系管理员", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    manager.getRuning().remove();
                    manager.getAbnormal().remove();
                    break;
            }
        }

    };

    private Button sonlibrayreader, sonlibraymadekar;//

    private TextView netreuslttype;//  类型find
    private TextView netreusltcontroller;// 机构
    private TextView netreusltbianno, netreusltcoutrsname;
    private TextView netreusltnum, netreusltnum2;//  柜子  柜面号码
    private TextView netreusltnum3, netreusltnum4;//  柜子  柜面号码

    private void initView() {
        cardresult = findViewById(R.id.cardresult);//读卡
        sonlibraymadekar = findViewById(R.id.sonlibraymadekar);
        sonlibraymadekar.setFocusable(false);
        sonlibraymadekar.setOnClickListener(this);
        sonlibrayreader = findViewById(R.id.sonlibrayreader);
        sonlibrayreader.setOnClickListener(this);

        //  daihao
        netreusltcode = (TextView) findViewById(R.id.netreusltcode);//袋子号码
        netreuslttype = (TextView) findViewById(R.id.netreuslttype);
        netreusltcontroller = (TextView) findViewById(R.id.netreusltcontroller);
        netreusltbianno = (TextView) findViewById(R.id.netreusltbianno);
        netreusltcoutrsname = (TextView) findViewById(R.id.netreusltcoutrsname);
        netreusltnum = (TextView) findViewById(R.id.netreusltnum);
        netreusltnum2 = (TextView) findViewById(R.id.netreusltnum2);
        netreusltnum3 = (TextView) findViewById(R.id.netreusltnum3);
        netreusltnum4 = (TextView) findViewById(R.id.netreusltnum4);
        librarytwocode_update = (Button) findViewById(R.id.librarytwocode_update);
        librarytwocode_update.setOnClickListener(this);
        iv_twoblack = findViewById(R.id.librarytwocode_back);
        iv_twoblack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        byte[] accessPassword = Tools.HexString2Bytes("00000000");
        switch (v.getId()) {

            case R.id.sonlibraytvshwo:

                break;


            case R.id.btnupcode://网络请求数据和返现
                NetInfoTwocode();

                break;


            case R.id.sonlibraymadekar: ///  制卡  和转换卡的名称
                makeandrreadcard.setText("制卡结果");
                LibraryMakeCard();

                break;

            case R.id.sonlibrayreader: //  duka
                makeandrreadcard.setText("读卡结果");
                LibrayrReadCard();

                break;


            case R.id.librarytwocode_update:
                NetInfoTwocode();
                break;

            case R.id.librarytwocode_back:

                getRfid().close_a20();
                /**
                 * 2015-7-13 增
                 */
                getRfid().scanclose();
                SonLibraryCodeScanActivity.this.finish();

                break;
        }
    }

    //读卡
    private void LibrayrReadCard() {
        byte[] accessPassword = Tools.HexString2Bytes("00000000");
        if (accessPassword.length != 4) {
            Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
            return;
        }

        // 读取数据区数据
        // 目前只操作EPC
        byte[] data = UhfManager.readFrom6C(1, 2, 6, accessPassword);

        if (data != null && data.length > 1) {
            String dataStr = Tools.Bytes2HexString(data, data.length);
//					Toast.makeText(getApplicationContext(),dataStr, 0).show();
//            ReaderCardTranfance(dataStr);
            cardresult.setText(dataStr);
        } else {
            if (data != null) {
                cardresult.append("读数据失败，错误码：" + (data[0] & 0xff) + "\n");
                return;
            }
            cardresult.append("读数据失败，返回为空" + "\n");
        }

    }

    //截取前两位后把多余的0 去掉
    private void ReaderCardTranfance(String dataStr) {
        String jiequstr = dataStr.substring(0, 4);
        ;

        String Assiistr = jiequstr.substring(0, 2);
        int a = Integer.parseInt(Assiistr);
        String Assiistr2 = jiequstr.substring(2, 4);
        int a2 = Integer.parseInt(Assiistr2);
        char c2 = (char) a;
        char c3 = (char) a2;

        StringBuffer buf = new StringBuffer();
        buf.append(c2);
        buf.append(c3);
        String jiequstr2 = dataStr.substring(4, 21);
        buf.append(jiequstr2);

//        String code_str =  (char) a +(char) a2 + jiequstr2;

        cardresult.setText(buf);

    }

    //制卡
    private void LibraryMakeCard() {
        byte[] accessPassword = Tools.HexString2Bytes("00000000");
//转换
        trsformastCode();


        for (int i = 0; i < 5; i++) {

            if (accessPassword.length != 4) {
                Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer sb = new StringBuffer();
            String editWrite = transfcode;
            if (editWrite.length() < 24) {
                sb.append(editWrite);
                for (int j = 0; j < 24 - editWrite.length(); j++) {
                    sb.append("0");
                }
            } else {
                sb.append(editWrite.substring(0, 24));
            }
            String writeData = sb.toString();
            Log.i("writeData.length()", writeData.length() + "");
            Log.i("writeData", writeData);

            if (writeData.length() % 4 != 0) {
                Toast.makeText(getApplicationContext(), "写入数据的长度以字为单位，1word = 2bytes", Toast.LENGTH_SHORT).show();
            }
            byte[] dataBytes = Tools.HexString2Bytes(writeData);
            boolean writeFlag = UhfManager.writeTo6C(accessPassword, 1, 2, dataBytes.length / 2, dataBytes);

            if (writeFlag) {
                cardresult.setText("写数据成功！" + "\n");
                break;
            } else {
                cardresult.setText("写数据失败！" + "\n");
                continue;
            }

        }
    }

    /***
     * zhuanghuan    erweima zifu
     */
    private String transfcode;

    private void trsformastCode() {
        String code_str;
        String code_sub = StrintCode.substring(0, 2);
        String code_app = StrintCode.substring(2, StrintCode.length());
        if (code_sub.equals("DZ")) {
            code_str = "6890" + code_app;

        } else {

            Toast.makeText(SonLibraryCodeScanActivity.this, "请检查读取的信息 " + code_sub, Toast.LENGTH_LONG).show();
            return;
        }
        transfcode = code_str;

    }

    //  数据员代码list集合
    List<LibraryCodeScan> LibraryCodeScanlist = new ArrayList<>();

    public void NetInfoTwocode() {
////                        // 开启线程，获取钞箱的信息
        new Thread() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    // 账号
                    if (null == StrintCode || StrintCode.equals("")) {
//                        StrintCode = "DZ20200922145910285";
                        handler.sendEmptyMessage(4);
                        return;
                    } else {


                        String netReult = new SubLibraryScanTwoServer().GetTwoInfoSum(GApplication.user.getYonghuZhanghao(), StrintCode);
                        Log.d(TAG, "netResultTask===" + netReult); // /网络请求

                        if (netReult != null && !netReult.equals("anyType{}")) {
                            Gson gson = new Gson();
                            LibraryCodeScanlist.clear();
                            LibraryCodeScanlist = gson.fromJson(netReult, new TypeToken<List<LibraryCodeScan>>() {
                            }.getType());

                            if (LibraryCodeScanlist.size() > 0) {
                                handler.sendEmptyMessage(2);
                            } else {
                                handler.sendEmptyMessage(5);//  无返回结果网络返回成功
                            }

                        } else {
                            handler.sendEmptyMessage(5);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "异常====" + e);
                    handler.sendEmptyMessage(1);
                }
                manager.getRuning().remove();
            }

        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 开启RFID扫描，只代码在同一生命周期里面只执行一次
        if (first) {
            first = false;
            // 添加钞箱通知
            managerClass.getRfid().addNotifly(scanbox);
            // 打开扫描，进行钞箱扫描
            getRfid().open_a20();
        }
        getRfid().addNotifly(scanUtil);
        // 打开二维码扫描
        managerClass.getRfid().scanOpen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 关闭扫描
        getRfid().close_a20();
        /**
         * 2015-7-13 增
         */
        getRfid().scanclose();
        // modify by wangmeng 2017-8-29
    }

}
