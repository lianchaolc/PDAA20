package com.sql;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.Cash_TailZeroAllctivity;
import com.example.pda.R;
import com.google.gson.Gson;
import com.handheld.UHF.UhfManager;
import com.ljsw.tjbankpad.baggingin.activity.cash.entity.BaggingForCashEntity;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.entity.CashtoPackgersEntity;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.service.CashToPackgersService;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity.TailPackgerEntityQuanbieXinxi;
import com.ljsw.tjbankpda.util.HxbSpinner;
import com.ljsw.tjbankpda.util.MySpinner;
import com.manager.classs.pad.ManagerClass;
import com.sql.entity.CashBagCheckEntity;
import com.sql.entity.CashMoneyInfoEntity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import a20.cn.uhf.admin.Tools;
import hdjc.rfid.operator.RFID_Device;
import poka_global_constant.GlobalConstant;

/***
 * 现金装袋   整钞  录入本地数据库
 *
 * @author Administrator】
 *
 */
public class CashBagCheckZhengChaoActivity extends FragmentActivity implements OnClickListener {
    protected static final String TAG = "CashToPackgersActivity";
    // 组件
    private TextView tvMoneyCoun, tvCardid;// 钱的总数
    private OnClickListener OnClick1;
    private TextView resistcollateralpacking, readcash_qunabeibu, tv_redcashcansun, cashreadmoney;// 读取后截取字符串券别
    private TextView cash_banbie;// 显示版别
    private Button ql_ruku_update, btn_cashmadecard, cashtoredadupdata_btn, cashreadcard_btn; // 更新 // 读卡
    private ImageView ql_ruku_back; // 返回
    private String netresultClean = ""; // 网络请求结果
    private ManagerClass manager;
    private Spinner sp_quanbei, sp_cansun, sp_banbie;// spinner 残损和券别
    private EditText et_weiling;// 对尾零数据接收
    private ListView cashtopackelistview;// 尾零列表
    // /声明变量
    public static CashBagCheckZhengChaoActivity instance = null;
    private String monetype;
    private String moneyid;
    private String cansunwanczheng;// 残损完整
    private String cansunid = "";
    private String moneyCount = "";
    private String readcount = "";// d\读的金额
    private String readcansun = "";// 残损
    private String readqumbei = "";// 读取券别
    private String readmeid = "";// 对比meid 的变量
    private SpinnerAdapter mSpinnerAdapter, mSpinnerAdapterquanzhong, mspinnbanbie;// 适配器
    private CashtoPackgersEntity cashtoPackgersEntity;// 实体类
    private List<CashtoPackgersEntity> cashtopacgerslist = new ArrayList<CashtoPackgersEntity>();// 数据源
    private List<String> cashtopackgertype = new ArrayList<String>();// 券别类型
    private List<String> cashtopackgersid = new ArrayList<String>();// 券别id
    private List<String> cashtopackgercount = new ArrayList<String>();// 券别id
    private List<String> cahstopackgermeid = new ArrayList<String>();// 数据的唯一标识
    private String[] str_juanzhong = {"完整", "残损"};// 残损
    private String[] array2 = cashtopackgersid.toArray(new String[0]);// /接受券别id
    private String[] array3 = cashtopackgertype.toArray(new String[0]);// /接受券别的对应类型

    private UhfManager newsendmanager;
    //	private powercontrol rFidPowerControl;// yang
    private String intentvalue = "";// 指纹验上传数据库所要的值
    private String epc = "";
    private Map<String, List<Map<String, String>>> moneyEditionMap = new HashMap<String, List<Map<String, String>>>();
    // 存放一个数据集合
    private Map<String, List<Map<String, String>>> moneyEditionMapstr = new HashMap<String, List<Map<String, String>>>();
    private Map<String, List<String>> moneyEditionMapliststr = new HashMap<String, List<String>>();

    private String TailZeromeid = "";// 查找返回数据的meoid 通过meid 去查询价值
    private String ByTailZeromeidtoparval = "";// 完整状态下

    private String ByTailZeromeidtolostvalue = "";// 残损状态下
    private String showtvcash_banbie;// 读卡后返显示版别和券别
    private String shwotvreadcash_qunabeibu;
    private String isupdatameid = "";/// 对比能否提交数据meid唯一标识

    private RFID_Device rfid; // 串口
    private String[] str_zhuangtai = new String[]{"完整", "全损"};

    private RFID_Device getRfid() {
        if (rfid == null) {
            rfid = new RFID_Device();
        }
        return rfid;
    }

    private HashMap<String, String> str_meidandquanbie_map = new HashMap<String, String>();//存版别和meid  移植过来   存放meid券别


    private List<String> quanbielist = new ArrayList<>();///  获取 的集合是网获取  并分类的下     券别 集合
    ;;//   获取 版别  的集合
    ///  新增

    private HxbSpinner couopnSpinner;
    private HxbSpinner editionSpinner;
    private MySpinner spinner;

    private static final String defaultOptions = "请选择";
    private List<BaggingForCashEntity> quanbieListxin = new ArrayList<BaggingForCashEntity>(); // 创建券别信息集合新版
    private Dialog dialogfa;// 失败
    private Button btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.acctivity_cashbag_zhenghcao_bag_ers);
        initView();
        instance = this;
        manager = new ManagerClass();
        OnClick1 = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manager.getAbnormal().remove();
                initView();// 组件初始化
            }
        };

        ///  数据库准初始化代码
        CashbagInfoSQlLiteHelper helperinfo = new CashbagInfoSQlLiteHelper(this);
//        CashbagSQlLiteHelper dbHelper1 = new CashbagSQlLiteHelper(this, "cashbaginfo_db");
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
        SQLiteDatabase sqliteDatabase = helperinfo.getWritableDatabase();

        final CashbagInfoSQl CashbagInfoSQl = new CashbagInfoSQl(CashBagCheckZhengChaoActivity.this);
        CashbagMoneyAllSQl cashbagMoneyAllSQl = new CashbagMoneyAllSQl(CashBagCheckZhengChaoActivity.this); //   钞袋的券别和qianshu

    }

    @Override
    protected void onResume() {
        super.onResume();
//        GApplication.user.getLoginUserId();
//        GApplication.user.getLoginUserName();
//        GApplication.user.getOrganizationId();
        getClearCollateralTaskListAndCount1();// 网络请求接口
    }

    private LinearLayout zhenghcao_sp_qunabeibulaylin;//  quanbie
    private LinearLayout zhenghcao_banbie_lin;//  banbie
    private LinearLayout zhenghcaostate_lin;//  wanzheng cansun

    private TextView zhenghcao_sp_qunabeibutv, zhenghcao_spbanbie_v, zhenghcao_state_v;// 券别 版别  残损状态 设置

    private void initView() {
        zhenghcao_sp_qunabeibulaylin = findViewById(R.id.zhenghcao_sp_qunabeibulaylin);//
        zhenghcao_sp_qunabeibulaylin.setOnClickListener(this);
        zhenghcao_banbie_lin = findViewById(R.id.zhenghcao_banbie_lin);
        zhenghcao_banbie_lin.setOnClickListener(this);
        zhenghcaostate_lin = findViewById(R.id.zhenghcaostate_lin);
        zhenghcaostate_lin.setOnClickListener(this);
        zhenghcao_sp_qunabeibutv = findViewById(R.id.zhenghcao_sp_qunabeibutv);//
        zhenghcao_spbanbie_v = findViewById(R.id.zhenghcao_spbanbie_v);
        //  banbie
        zhenghcao_state_v = findViewById(R.id.zhenghcao_state_v);

//        sp_quanbei = (Spinner) findViewById(R.id.zhenghcao_sp_qunabeibu);// 设置券别
//
//        sp_cansun = (Spinner) findViewById(R.id.zhenghcao_sp_cansun);// 设置残损
//        sp_banbie = (Spinner) findViewById(R.id.zhenghcao_sp_banbie);// 设置版别
        // 标题
        ql_ruku_back = (ImageView) findViewById(R.id.zhenghcao_ql_ruku_back);
        ql_ruku_back.setOnClickListener(this);

//        GApplication.user.getLoginUserName();
        // 抵质押品
        tvMoneyCoun = (TextView) findViewById(R.id.zhenghcao_tv_monecount);
        cashreadcard_btn = (Button) findViewById(R.id.zhenghcao_cashreadcard_btn);
        cashreadcard_btn.setOnClickListener(this);
        tvCardid = (TextView) findViewById(R.id.zhenghcao_tv_bucard);// 显示卡号
        readcash_qunabeibu = (TextView) findViewById(R.id.zhenghcao_cash_qunabeibu);// 券别
        tv_redcashcansun = (TextView) findViewById(R.id.zhenghcao_cashcansun);
        cashreadmoney = (TextView) findViewById(R.id.zhenghcao_cashreadmoney);
        ql_ruku_update = (Button) findViewById(R.id.zhenghcao_ql_ruku_update);
        ql_ruku_update.setOnClickListener(this);
        cashtoredadupdata_btn = (Button) findViewById(R.id.zhenghcao_cashtoredadupdata_btn);
        cashtoredadupdata_btn.setOnClickListener(this);
        // 显示版别tv
        cash_banbie = (TextView) findViewById(R.id.zhenghcao_cash_banbie);
        cash_banbie.setText("");

        /// 新增加
//        zhenghcao_sp_qunabeibulaylin =
        btn = findViewById(R.id.zhenghcao_cashreadinset_btn);
        btn.setOnClickListener(this);
    }

    /***
     * \ 获取券别版别信息 lc 201910.27
     */
    BaggingForCashEntity[] baggingForCashEntity;

    public void getClearCollateralTaskListAndCount1() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    // 用户账号
                    netresultClean = new CashToPackgersService().TailCoupon_getMoneyEditionList();
                    cashtopacgerslist.clear();
                    if (!netresultClean.equals("")) {
                        Gson gson = new Gson();

                        baggingForCashEntity = gson.fromJson(netresultClean, BaggingForCashEntity[].class);

                        // 向集合中分别添加券别券种
                        for (BaggingForCashEntity item : baggingForCashEntity) {
                            String moneytypeweiling = item.getMONEYTYPE();
                            String monestr = item.getEDITION();
                            Map<String, String> baggmap = new HashMap<String, String>();
                            baggmap.put("edition", item.getEDITION());
                            baggmap.put("lossvalue", item.getLOSSVALUE());
                            baggmap.put("meid", item.getMEID());
                            baggmap.put("paryvalue", item.getPARVALUE());
                            baggmap.put("bagmoney", item.getBAGMONEY());

                            if (moneyEditionMap.containsKey(item.getMONEYTYPE())) {
                                List<Map<String, String>> listmap = moneyEditionMap.get(moneytypeweiling);
                                listmap.add(baggmap);
                            } else {
                                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                                list.add(baggmap);
                                moneyEditionMap.put(moneytypeweiling, list);
                            }

                            if (moneyEditionMapstr.containsValue(item.getEDITION())) {
                                List<Map<String, String>> listmapedition = moneyEditionMapstr.get(item.getEDITION());
                                Log.e(TAG, "找张" + moneyEditionMapstr.get(item.getEDITION()));
                                listmapedition.add(baggmap);

                            } else {
                                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                                list.add(baggmap);
                                moneyEditionMapstr.put(item.getEDITION(), list);
                            }
                            cahstopackgermeid.add(item.getMEID());
                            str_meidandquanbie_map.put(item.getMEID(), item.getMONEYTYPE());//存放meid标识和版别集合202008
                        }


                        // 文字版别 "纸100元（5套）
                        Set<String> moneySet = moneyEditionMap.keySet();
                        array2 = new String[moneySet.size()];
                        moneySet.toArray(array2);
                        quanbielist.clear();
                        quanbielist = new ArrayList<String>(moneySet);
                        // 版别"EDITION":2015

//						改动
//						Set<String> moneysetbanbie = moneyEditionMapstr
//								.keySet();
//						array3 = new String[moneysetbanbie.size()];

//						Log.e(TAG, "AAAAA" + array3.length);
//						// moneysetbanbie.toArray(ar)
//						moneysetbanbie.toArray(array3);
                        // 获取钱的价值

                        handler.sendEmptyMessage(4);
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

    @Override
    public void onClick(View v) {
        if (newsendmanager == null) {
            loadRfid();
        }
        final CashbagInfoSQl CashbagInfoSQl = new CashbagInfoSQl(CashBagCheckZhengChaoActivity.this);
        CashbagMoneyAllSQl cashbagMoneyAllSQl = new CashbagMoneyAllSQl(CashBagCheckZhengChaoActivity.this); //   钞袋的券别和qianshu
//        CashbagSQl cashbagSQl = new CashbagSQl(CashBagCheckZhengChaoActivity.this);//  钞袋编号
        switch (v.getId()) {

            case R.id.zhenghcao_cashtoredadupdata_btn:// 读卡

//                if ((!(intentvalue.equals(""))) && (readmeid.equals(TailZeromeid))) {// 非空判断&&readmeid.equals()
                // 后台数据的券别和当前读取券别要求一致
                Log.e(TAG, "!!!!!!!!!!!!!!!" + readqumbei + moneyid);
                // if("完整".equals(cansunwanczheng)){
                Intent intent = new Intent(CashBagCheckZhengChaoActivity.this, CashBagCheckActivity.class);
//                    intent.putExtra("cardid", intentvalue);
                startActivity(intent);

//                } else {
                if (intentvalue.equals("")) {
                    showBigToast("请读卡!", 1000);
                    return;
                }
                showBigToast("读到的券别或版别和所选不一致请重试", 1000);
//                    return;
//                }
                break;
            case R.id.zhenghcao_ql_ruku_back:

//                cashbagSQl.delete();
                CashbagInfoSQl.deleteinfoall();
                cashbagMoneyAllSQl.deleteinfoall();
                CashBagCheckZhengChaoActivity.this.finish();

                Log.e(TAG, "执行代码sql+" +
                        "cashbagSQlCashbagInfoSQl+cashbagMoneyAllSQl");
                break;
            case R.id.zhenghcao_ql_ruku_update:
                getClearCollateralTaskListAndCount1();
                break;
            case R.id.zhenghcao_cashreadcard_btn:// 读卡   代码
                if (null !=  spinner.getChooseText()|| !spinner.getChooseText().equals("")) {
                    getdata(spinner.getChooseText());
                }

                readmeid = "";
                tv_redcashcansun.setText("");// 三个参数全部设置为null
                readcash_qunabeibu.setText("");
                cashreadmoney.setText("");
                tvCardid.setText("");// 每次重新读取后都要设置为null
                if (isFastClick() != true) {

                } else {
                    // 长按后
//                    if (null == monetype || null == cansunwanczheng || null == readcash_qunabeibu || monetype.equals("") || cansunwanczheng.equals("") || cansunwanczheng.equals("")) {
//                        showBigToast("选择券别，版别，完损状态", 400);
//                        tvCardid.setText("");
//                    } else {
                    if (1 == 1) {
                        cash_banbie.setText("");// 版别显示
                        tvCardid.setText(""); /// 卡号显示
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        final byte[] accessPassword = Tools.HexString2Bytes("00000000");
                        if (accessPassword.length != 4) {
                            showBigToast("密码为4个字节", 1000);
                            return;
                        }
                        // 目前只操作EPC
                        byte[] data = newsendmanager.readFrom6C(1, 2, 6, accessPassword);
                        Log.i(TAG, "data========" + data);

                        if (data != null && data.length > 1) {
                            Log.i(TAG, "data========" + data.length);
                            String dataStr = Tools.Bytes2HexString(data, data.length);
                            // 钞袋编号规则 ：CD 01 5A 4 190227092020
                            // 钞袋 券别是否含有字母，券别 券种 时间（0代表5是否为字母1代表A是否有字母）
                            // 写入规则修改版
                            // 写入的均为不尾零的钞袋 包含有字母和无字母两种
                            // 676810014191027180357000,6768110A4191027180357000

                            String cdcode = dataStr.substring(0, 4);
                            StringBuffer sb = new StringBuffer();
                            if (dataStr.length() > 18 && dataStr.length() <= 24) {
                                if (cdcode.equals("6768")) {// 无字母
                                    if ("1".equals(dataStr.substring(4, 5)) && "0".equals(dataStr.substring(5, 6))) {
                                        readcansun = (dataStr.substring(8, 9));// 获取参数的值
                                        if (readcansun.equals("4") || readcansun.equals("2")) {
                                            Log.i(TAG, "readcansun*********" + readcansun);

                                            readmeid = (dataStr.substring(6, 8)).toString();
                                            sb.append(dataStr.substring(0, 24));

                                            Log.e(TAG, "***************" + readqumbei);
                                        }
                                        // 有字母6768110A4191027180357000lc
                                        // 6768110654191027180357000
                                    } else if ("1".equals(dataStr.substring(4, 5)) && "1".equals(dataStr.substring(5, 6))) {
                                        sb.append((dataStr.substring(0, 2)));
                                        sb.append((dataStr.substring(2, 4)));
                                        sb.append(dataStr.substring(4, 7));
                                        sb.append(Integer.parseInt(dataStr.substring(7, 9)));
                                        Log.i(TAG, sb + "!!!!!");
                                        //
                                        readcansun = (dataStr.substring(9, 10));// 获取参数的值
                                        if (readcansun.equals("4") || readcansun.equals("2")) {
                                            Log.i(TAG, "readcansun*********" + readcansun);
                                            Log.i(TAG, "dataStr========" + dataStr);

//									  截取位数字符串  并转码ASSII
                                            String strcode = (dataStr.substring(6, 7)).toString();
                                            Log.i(TAG, "唯一标识meid***************" + strcode);
                                            String strcodeAssii = (dataStr.substring(7, 9)).toString();
                                            Log.i(TAG, "唯一标识meid***************" + strcodeAssii);
                                            StringBuffer sbchar = new StringBuffer();
                                            sbchar.append((char) Integer.parseInt(strcodeAssii.substring(0, 2)));

                                            Log.i(TAG, "唯一标识meid***************" + sbchar);
                                            readmeid = (strcode + sbchar);

                                            Log.i(TAG, "唯一标识meid***************" + readmeid);
                                            sb.append(dataStr.substring(9, 24));
                                        } else {
                                            readcansun = "";
                                            sb = new StringBuffer("");// 清空字符串拼接
                                        }

                                    }
                                    sb.toString();
                                    Log.i(TAG, "唯一标识meid***************" + sb.toString());
//                                    cashbagSQl = new CashbagSQl(CashBagCheckZhengChaoActivity.this);//  钞袋编号
                                    if (cahstopackgermeid.contains(readmeid)) {// 包含在券别种类中发送其它情况均为非法券别
                                        tvCardid.append(sb + "\n");
                                        intentvalue = sb.toString();
                                        Log.i(TAG, "成功了" + sb);
                                        getcashinfo(readmeid);/// 逆向查询 给meid 查询monety 和moneedtion
                                        handler.sendEmptyMessage(6);
//                                        cashbagSQl = new CashbagSQl(CashBagCheckZhengChaoActivity.this);//  钞袋编号
//
//                                        cashbagSQl.add(sb + "");// 添加钞袋  的sql   方法
                                    } else {
                                        tvCardid.setText("" + sb);
                                        handler.sendEmptyMessage(7);


                                    }

                                    break;
                                }
                            }

                        } else {
                            if (data != null) {
                                tvCardid.append("读数据失败，错误码：" + (data[0] & 0xff) + "\n");
                                return;
                            }
                        }
//                        cashbagSQl.add("6");
                    }
                }
                break;

            case R.id.zhenghcao_sp_qunabeibulaylin:// quanbie

                couopnSpinner = new HxbSpinner(this, zhenghcao_sp_qunabeibulaylin, zhenghcao_sp_qunabeibutv, quanbielist);
                couopnSpinner.setSpinnerHeight(zhenghcao_sp_qunabeibulaylin.getHeight() * 2);
                couopnSpinner.showPopupWindow(zhenghcao_sp_qunabeibulaylin);
                if (editionSpinner != null) {
                    editionSpinner.clear();
                    if (zhenghcao_sp_qunabeibutv != null)
                        zhenghcao_sp_qunabeibutv.setText(defaultOptions);
                }
                if (null == couopnSpinner.getChooseText() || couopnSpinner.getChooseText().equals("")) {
                    showBigToast("请选择版别", 400);
                } else {
                    getversioncode(zhenghcao_sp_qunabeibutv.getText().toString());
                }

                break;

            case R.id.zhenghcao_banbie_lin:
                List<String> editionList = new ArrayList<String>();
                String coupon = couopnSpinner.getChooseText();//   券别
                if (null != coupon) {
                    getversioncode(coupon);
//                    List<CashtoPackgersEntity> cashtopacgerslist
//                    for (BaggingForCashEntity item : ) {
//                        if (coupon.equals(item.getMONEYTYPE())) {
//                            editionList.add(item.getEDITION());
//                        }
//                    }
                }
                editionSpinner = new HxbSpinner(this, zhenghcao_banbie_lin, zhenghcao_spbanbie_v,
                        listmap);
                editionSpinner.setSpinnerHeight(zhenghcao_banbie_lin.getHeight() * 2);
                editionSpinner.showPopupWindow(zhenghcao_banbie_lin);

                break;

            case R.id.zhenghcaostate_lin:
                spinner = new MySpinner(this, zhenghcaostate_lin, zhenghcao_state_v);
                spinner.setSpinnerHeight(zhenghcaostate_lin.getHeight() * 2);
                spinner.setList(this, str_zhuangtai);
                spinner.showPopupWindow(zhenghcaostate_lin);
                spinner.setList(this, str_zhuangtai, 40);
                String coupon1 = spinner.getChooseText();//   券别
                if (null != coupon1) {
                    getdata(coupon1);
                }
                break;


            case R.id.zhenghcao_cashreadinset_btn://   插入数据 做查询操作
//                final CashbagInfoSQl CashbagInfoSQl = new CashbagInfoSQl(CashBagCheckZhengChaoActivity.this);
//                final CashbagMoneyAllSQl cashbagMoneyAllSQl = new CashbagMoneyAllSQl(CashBagCheckZhengChaoActivity.this); //   钞袋的券别和qianshu
//                final   CashbagSQl cashbagSQl = new CashbagSQl(CashBagCheckZhengChaoActivity.this);//  钞袋编号
//                List<String> persons =   cashbagSQl.select();
                List<CashBagCheckEntity> selectlist1 = CashbagInfoSQl.selectinfo();
                List<CashMoneyInfoEntity> selectlist = cashbagMoneyAllSQl.selectinfo();

                Log.e(TAG, "查询数据员" + selectlist1.size() + selectlist.size() + "");
                break;
            default:
                break;
        }
    }

    /***
     * 网络请求成功获取后的显示
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CashBagCheckZhengChaoActivity.this, "加载超时,重试?", OnClick1);
                    break;
                case 1:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CashBagCheckZhengChaoActivity.this, "网络连接失败,重试?", OnClick1);
                    break;
                case 2:
                    manager.getRuning().remove();

                    break;
                case 3:
                    manager.getRuning().remove();
                    manager.getAbnormal().timeout(CashBagCheckZhengChaoActivity.this, "没有任务", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            manager.getAbnormal().remove();
                        }
                    });
                    break;

                case 4:
                    // / 选取文字版别monetype
                    spinnerselect();
                    // 设置金额

                    break;
                case 5:

                    break;
                case 6:
                    // / 网路获数据成功后写卡成功后返现卡的信息

                    if (readcansun.equals("4")) {
                        tv_redcashcansun.setText("残损");
                    } else if (readcansun.equals("2")) {
                        tv_redcashcansun.setText("完整");
                    } else {
                        tv_redcashcansun.setText(" ");
                        showBigToast("残损状态不正确", 1000);
                        break;
                    }

                    break;

                case 7:
                    readcash_qunabeibu.setText("非法券别");
                    break;

                case 8:
                    if (!ByTailZeromeidtolostvalue.equals("") && cansunid.equals("4")) {
                        double intbytailzermcan = Double.parseDouble(ByTailZeromeidtolostvalue);
                        double result = (intbytailzermcan * (0.0001));
                        tvMoneyCoun.setText("" + result);
                    } else if (!ByTailZeromeidtolostvalue.equals("") && cansunid.equals("2")) {
                        double intbytailzermcan = Double.parseDouble(ByTailZeromeidtolostvalue);
                        double result = (intbytailzermcan * (0.0001));
                        tvMoneyCoun.setText("" + result);

                    }
                    //  增加明细的sql 代码
                    CashbagInfoSQl infoSQl = new CashbagInfoSQl(CashBagCheckZhengChaoActivity.this);
                    infoSQl.addinfo("", monetype, moneyid, readcansun, "zhengchao", tvMoneyCoun.toString() + "");
                    CashbagMoneyAllSQl cashbagMoneyAllSQl = new CashbagMoneyAllSQl(CashBagCheckZhengChaoActivity.this); //
                    cashbagMoneyAllSQl.addinfo(monetype, tvMoneyCoun + "");

                    Log.e(TAG, "执行代码添加  版别钱数sql" + cashbagMoneyAllSQl.selectlist);
                    break;

                case 9:// 读卡后显示版别和券别

                    break;
                default:
                    break;
            }
        }

    };

    /****
     * 设置残损状态呢 lc 201910.29 private String ByTailZeromeidtoparval="";// 完整状态下
     * private String ByTailZeromeidtolostvalue="";// 残损状态下
     */
    private void getdata(String cansunwanczhegn) {
        if (cansunwanczhegn.equals("完整")) {
            cansunid = "2";

            if (null != monetype && null != moneyid) {
                Log.d(TAG, "=======" + monetype + moneyid);
            } else {
            }
            if (!ByTailZeromeidtolostvalue.equals("")) {
                double intbytailzermcan = Double.parseDouble(ByTailZeromeidtolostvalue);
                double result = (intbytailzermcan * (0.00001));
                tvMoneyCoun.setText("" + result);
            }

            // tvMoneyCoun.setText(""+ByTailZeromeidtoparval);
        } else if (cansunwanczhegn.equals("残损")) {
            cansunid = "4";
            if (null != monetype && null != moneyid) {
                Log.d(TAG, "=======" + monetype + moneyid);

				selectmeid(monetype, moneyid);
            } else {

            }

        } else {
            tv_redcashcansun.setText(" ");
            tvMoneyCoun.setText("");
            showBigToast("残损状态不正确", 1000);
        }

    }

    // 适配器
    class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            // 此处设置spinner的字体大小 和文字颜色
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLUE);
            tv.setTextSize(40);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLUE);
            tv.setTextSize(30);
            return convertView;
        }

        private class SpinnerAdapterquanzhong extends ArrayAdapter<String> {
            Context context;
            String[] items = new String[]{};

            public SpinnerAdapterquanzhong(final Context context, final int textViewResourceId,
                                           final String[] objects) {
                super(context, textViewResourceId, objects);
                this.items = objects;
                this.context = context;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                }
                // 此处设置spinner的字体大小 和文字颜色
                TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                tv.setText(items[position]);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(Color.BLUE);
                tv.setTextSize(40);
                return convertView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                tv.setText(items[position]);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(Color.BLUE);
                tv.setTextSize(30);
                return convertView;
            }

        }
    }

    public static final int MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;

    }

    @Override
    protected void onPause() {
        getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.DISABLE_IO);
        super.onPause();
    }

    public void loadRfid() {
//		// 串口加载文件
//		rFidPowerControl = new powercontrol();
//		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
//		rFidPowerControl.rfidPowerctlSetSleep(0);
//		// ===================================
        getRfid().setOpenClose(GlobalConstant.IO_RFID_POWER, GlobalConstant.ENABLE_IO);

        epc = getIntent().getStringExtra("epc");
        /*
         * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
        newsendmanager = UhfManager.getInstance();
        if (newsendmanager == null) {
            showBigToast("串口初始化失败", 1000);
            return;
        }
    }

    public void showBigToast(String info, int duration) {
        Toast toast = new Toast(CashBagCheckZhengChaoActivity.this);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
        TextView tv = new TextView(CashBagCheckZhengChaoActivity.this);
        tv.setBackgroundResource(R.drawable.bg_toast);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(25, 5, 25, 5);
        tv.setTextColor(Color.WHITE);
        tv.setText(info);
        tv.setTextSize(30);
        toast.setView(tv);
        toast.setDuration(duration);
        toast.show();

    }

    /***
     * 通过券别和版别获取 meid的值
     *
     * @param monetype
     * @param moneyid
     * @return
     */
    public String selectmeid(String monetype, String moneyid) {

        if (null != monetype && monetype.equals("") || null != moneyid && moneyid.equals("")) {
            showBigToast("券别" + monetype + "版别" + moneyid + "不能为空", 400);
        } else {
            Log.e(TAG, "meid============券别" + monetype + "===版别====" + moneyid);
            for (Map.Entry<String, List<Map<String, String>>> item : moneyEditionMap.entrySet()) {
                if (monetype.equals(item.getKey())) {
                    for (Map<String, String> map : item.getValue()) {
                        if (moneyid.equals(map.get("edition"))) {
                            Log.e(TAG, "edition==========" + moneyid + (map.get("edition")));
                            Log.e(TAG, "meid======" + map.get("meid"));
                            TailZeromeid = map.get("meid");
                            // PARVALUE

                            ByTailZeromeidtolostvalue = map.get("bagmoney");
                            handler.sendEmptyMessage(8);// 发送消息显示总万元钱数
                            ;
                            Log.e(TAG, "bagmoney======" + map.get("bagmoney"));
                            break;
                        } else {
                            Log.e(TAG, "bagmoney=====" + map.get("bagmoney"));
                            ByTailZeromeidtolostvalue = map.get("bagmoney");
                            handler.sendEmptyMessage(8);// 发送消息显示总万元钱数
                            TailZeromeid = null;
                        }
                    }
                }
            }

        }
        // 没找到号码
        return TailZeromeid;

    }

    /****
     * 返现数据用户点击读卡后获取的卡号信息进行显示 lc 201910.29 baggmap.put("edition",
     * item.getEDITION()); baggmap.put("lossvalue", item.getLOSSVALUE());
     * baggmap.put("meid", item.getMEID()); baggmap.put("paryvalue",
     * item.getPARVALUE()); baggmap.put("bagmoney", item.getBAGMONEY());
     *
     *
     */
    public void getcashinfo(String readmeid) {
        for (Map.Entry<String, List<Map<String, String>>> item : moneyEditionMap.entrySet()) {
            for (Map<String, String> map : item.getValue()) {
                if (readmeid.equals(map.get("meid"))) {
                    Log.e(TAG, "！！！！！！！！！！！！！！！！！！！！！！！！！" + map.get("meid"));
                    shwotvreadcash_qunabeibu = map.get("moneytype");// 读卡后返显示版别和券别EDITION
                    showtvcash_banbie = map.get("edition");
                    for (Map.Entry<String, String> vo : str_meidandquanbie_map.entrySet()) {
//							    vo.getKey();
//					            vo.getValue();
                        Log.d(TAG, "---------------------readmeid-" + readmeid);
                        Log.d(TAG, "----------" + vo.getKey());
                        Log.d(TAG, "----------" + vo.getValue());
                        if ((vo.getKey().equals(readmeid))) {
                            readcash_qunabeibu.setText(vo.getValue());
                            Log.d(TAG, "---------------------readmeid-" + readmeid);
                            Log.d(TAG, "------------------AAAAAA2----" + str_meidandquanbie_map.get(readmeid));
                        }
                    }
//					if (readmeid.equals("01")) {
//						readcash_qunabeibu.setText("纸100元（5套）");
//					} else if (readmeid.equals("02")) {
//						readcash_qunabeibu.setText("纸100元（5套）");
//					} else if (readmeid.equals("03")) {
//						readcash_qunabeibu.setText("纸100元（5套）");
//					} else if (readmeid.equals("04")) {
//						readcash_qunabeibu.setText(" 纸50元（5套）");
//					} else if (readmeid.equals("05")) {
//						readcash_qunabeibu.setText(" 纸50元（5套）");
//					} else if (readmeid.equals("06")) {
//						readcash_qunabeibu.setText(" 纸20元（5套）");
//					} else if (readmeid.equals("07")) {
//						readcash_qunabeibu.setText(" 纸20元（5套）");
//					} else if (readmeid.equals("08")) {
//						readcash_qunabeibu.setText("纸10元（5套）");
//					} else if (readmeid.equals("09")) {
//
//						readcash_qunabeibu.setText("纸10元（5套）");
//					} else if (readmeid.equals("0A")) {
//						readcash_qunabeibu.setText("纸5元（5套）");
//					} else if (readmeid.equals("0B")) {
//						readcash_qunabeibu.setText("纸5元（5套）");
//					} else if (readmeid.equals("0C")) {
//						readcash_qunabeibu.setText("纸1元（5套）");
//					} else if (readmeid.equals("0D")) {
//						readcash_qunabeibu.setText("纸100元（4套）");
//					} else if (readmeid.equals("0E")) {
//						readcash_qunabeibu.setText("纸100元（4套）");
//					} else if (readmeid.equals("0F")) {
//						readcash_qunabeibu.setText("纸50元（4套）");
//					} else if (readmeid.equals("10")) {
//						readcash_qunabeibu.setText("纸50元（4套）");
//					} else if (readmeid.equals("11")) {
//						readcash_qunabeibu.setText("纸10元（4套）");
//					} else if (readmeid.equals("12")) {
//						readcash_qunabeibu.setText("纸5元（4套）");
//					} else if (readmeid.equals("13")) {
//						readcash_qunabeibu.setText("纸2元（4套）");
//					} else if (readmeid.equals("14")) {
//						readcash_qunabeibu.setText("纸2元（4套）");
//					} else if (readmeid.equals("16")) {
//						readcash_qunabeibu.setText("纸1元（4套）");
//					} else if (readmeid.equals("17")) {
//						readcash_qunabeibu.setText("纸1元（4套）");
//					} else if (readmeid.equals("15")) {
//						readcash_qunabeibu.setText("纸1元（4套）");
//					} else if (readmeid.equals("18")) {
//						readcash_qunabeibu.setText("纸5角（4套）");
//					} else if (readmeid.equals("19")) {
//						readcash_qunabeibu.setText("纸2角（4套）");
//					} else if (readmeid.equals("1A")) {
//						readcash_qunabeibu.setText("纸1角（4套）");
//					} else if (readmeid.equals("1B")) {
//						readcash_qunabeibu.setText("硬五角（5套）");
//					} else if (readmeid.equals("1C")) {
//						readcash_qunabeibu.setText("硬一角（5套）");
//					}

                    double intbytailzermcan = Double.parseDouble(map.get("bagmoney"));
                    double result = (intbytailzermcan * (0.0001));
                    cashreadmoney.setText(result + ""); // 读取后的金额
                    cash_banbie.setText(showtvcash_banbie + ""); /// 读取版别
                    break;
                } else {
                }
            }
        }

    }

    /***
     * 获取版别信息
     */
    private List<String> listmap = new ArrayList<String>();

    private void getversioncode(String monetype1) {

        listmap.clear();

        Map<String, List<String>> moneyEditionMapliststr = new HashMap<String, List<String>>();
        for (BaggingForCashEntity item : baggingForCashEntity) {

            String moneytypeweiling = item.getMONEYTYPE();
            String monestr = item.getEDITION();

            if (moneyEditionMapliststr.containsKey(item.getMONEYTYPE())) {
                listmap = moneyEditionMapliststr.get(item.getMONEYTYPE());
                Log.e(TAG, "！！！！！！！！！！" + item.getMONEYTYPE() + monestr);
                Log.e(TAG, "!!!!!" + moneytypeweiling);

                if (listmap.contains(item.getEDITION())) {

                } else {
                    if (monestr.equals("0")) {
                        listmap.add("-");//// 获取三个deition
                    } else {
                        listmap.add(monestr + "");//// 获取三个deition
                    }

                }
            } else {

                List<String> list = new ArrayList<String>();
                if (list.contains(monestr)) {
                } else {
                    list.add(monestr);
                }

                Log.e(TAG, "listmap!!!!!" + list.size());

                moneyEditionMapliststr.put(monetype1, list);

            }
        }
        Log.e(TAG, "listmap!!!!!" + listmap.size());

//        array3 = listmap.toArray(new String[0]);


//        mspinnbanbie = new SpinnerAdapter(CashBagCheckZhengChaoActivity.this, android.R.layout.simple_spinner_dropdown_item,
//                array3);
//        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_banbie.setAdapter(mspinnbanbie);
//        sp_banbie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//
//                moneyid = array3[pos];
//                Log.e(TAG, "moneyid版别：：：：：：：" + moneyid);
//                ;
//                if (null != monetype && null != moneyid) {
//                    selectmeid(monetype, moneyid);/// 查找meid代码 改变位置了
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });

        // 设置残损

//        mSpinnerAdapterquanzhong = new SpinnerAdapter(CashBagCheckZhengChaoActivity.this,
//                android.R.layout.simple_spinner_dropdown_item, str_juanzhong);
//        mSpinnerAdapterquanzhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_cansun.setAdapter(mSpinnerAdapterquanzhong);
//        sp_cansun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                Log.d(TAG, str_juanzhong[pos] + "");
//                cansunwanczheng = str_juanzhong[pos];
//                getdata(cansunwanczheng);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Another interface callback
//
//            }
//        });

    }

    /***
     * spinner 下拉选择框的数据显示作为参数
     */
    public void spinnerselect() {
        if (0 != (array2.length)) {
//            mSpinnerAdapter = new SpinnerAdapter(CashBagCheckZhengChaoActivity.this,
//                    android.R.layout.simple_spinner_dropdown_item, array2);
//            mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_quanbei.setAdapter(mSpinnerAdapter);
//            sp_quanbei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//
//                    monetype = array2[pos];
//                    getversioncode(monetype);
//                    // 将元转换为万元结尾
//                    String title = "";
//                    readqumbei = "";// 每次选中都全部为null
//                    intentvalue = "";
//                    readcash_qunabeibu.setText("");
//                    cashreadmoney.setText("");
//                    tvCardid.setText("");
//                    tv_redcashcansun.setText(" ");
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                    Log.e(TAG, "不选中中执行");
//
//                }
//            });

            // 设置版别

        }
    }

}
