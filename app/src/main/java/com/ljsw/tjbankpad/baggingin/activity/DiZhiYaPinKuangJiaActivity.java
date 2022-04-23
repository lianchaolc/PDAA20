package com.ljsw.tjbankpad.baggingin.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;

import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.AccountTaskListAndCountEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.ClearCollateralTaskListAndCount;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.WarehouseTaskListAndCount;
import com.ljsw.tjbankpad.baggingin.activity.chuku.DiZhiYaPinChuKuActivity;
import com.ljsw.tjbankpad.baggingin.activity.dizhiyapinruku.activity.DiZhiYaPinRuKuActivity;
import com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.RuZhangHuZhongXinActivity;
import com.ljsw.tjbankpad.baggingin.activity.ruzhanghuzhongxin.entity.AccountCenterInEntity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.AccountInfoInHandoverEntity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.activity.AccountInfoInHandoverActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.AccountInfomationReturnService;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.chukujieyue.ZhangHuZiLiaoChuKuJieYueActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ZhangHuZiLiaoGuiHuanActivity;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.guihuan.ruku.ZhangHuZiLiaoZhangHuDaiGuiHuanActivity;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.qf.entity.QingLingRuKu;
import com.ljsw.tjbankpda.util.Skip;
import com.main.pda.SystemLogin;
import com.manager.classs.pad.ManagerClass;
import com.service.FixationValue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

/***
 * 抵制押品最外层框架 条目点击
 * 
 * @author Administrator
 * 
 */
public class DiZhiYaPinKuangJiaActivity extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "DiZhiYaPinKuangJiaActivity";
	private RelativeLayout relazhaungdai;
	private RelativeLayout relazhuangxiang;
	private RelativeLayout relachuku;
	private RelativeLayout relayruku;
	private RelativeLayout zhanghuziliaozhuangdai;// z账户资料装袋
	private RelativeLayout zhanghuziliaoruku;
	private RelativeLayout zhanghuziliaochukujieyue;// 借阅
	private RelativeLayout zhanghuziliaoguihuan; // 归还

	private RelativeLayout zhaunghuziliaoguihuandaiguihuanziliaoruku;// 待归还资料入库

	private RelativeLayout ruzhanghuzhongxin;// 入账户中心
	private List<String> rukulist = new ArrayList<String>();
	private Bundle getBundle;
	private String parms = "";
	private String account = "";

	Bundle bundleIntent;
	Bundle bundle;
	private String permission;
	private String number;// 传入的登录人员
	private String netResultcenter;
	private OnClickListener OnClick1;
	private ManagerClass manager;
//	组件
	private TextView tvAccountcent, tvTitleItem;// z账户中心
	private TextView accountintioninhome_number, accountinfotiontobagging;// 入库的任务数量,账户资料装袋
	private TextView returntothelistofmaterialstobereturned_tv, resistthecollateral, resistcollateralinhous;// 账户资料待归还任务数量,抵制押品出库,抵制押品入库
	private TextView accountintionoutboundborrowing, returnofaccountinformation_tv; // 账户资料出库借阅,账户资料归还, 归还入库待归还资料列表

	private TextView resistcollateralpacking, resistpledgebagging_tv;// 抵制押品装箱 ，抵制押品装袋
	private Button ql_ruku_update; // 更新
	private ImageView ql_ruku_back; // 返回
	private String netresult = ""; // 网络请求结果
	private String netresultgetCount = "";// 如账户中心和账户资料装袋网络请求结果
	private String netresultClean;// 清分人员的抵制押品
	// 实体类
	private AccountCenterInEntity mAccountCenterInEntity = new AccountCenterInEntity();
	// 获取账户中心任务数量
	private AccountTaskListAndCountEntity atlace = new AccountTaskListAndCountEntity();
	// 库管员
	private WarehouseTaskListAndCount wtlac = new WarehouseTaskListAndCount();// 库管员的实体类
	// 请分员—— 抵制押品装袋装箱
	private ClearCollateralTaskListAndCount mClearCollateralTaskListAndCount = new ClearCollateralTaskListAndCount();// 抵制押品实体类

	// 账户资料入库实体类
	private List<AccountInfoInHandoverEntity> arraylist = new ArrayList<AccountInfoInHandoverEntity>();
	// 如账户中心交接集合添加到扫描中
	private AccountInfoInHandoverEntity aiihEntity = new AccountInfoInHandoverEntity();
	public static DiZhiYaPinKuangJiaActivity instance = null;

	private ManagerClass managerClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kuangjia);try {
			permission = GApplication.user.getLoginUserId();
		}catch (Exception e){
			System.out.print("DiZhiYaPinKuangJiaActivity"+e);
		}

		initView();
		instance = this;
//		账户资料交接入库的第一个页面：查询待做的线路数

		manager = new ManagerClass();
		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
//					getAccountTurnOverLineCount();// 网络请求
			}
		};

		managerClass = new ManagerClass();
		// 公共视图文件初始化
		managerClass.getGolbalView().Init(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GApplication.user.getLoginUserId();
		GApplication.user.getLoginUserName();
		GApplication.user.getOrganizationId();
		// 进入时判断不同用户进行网络请求
		if (GApplication.user.getLoginUserId().equals("7")) {// 清分员
			tvTitleItem.setText("抵制押品");
			getClearCollateralTaskListAndCount();// 获取任务数量
		} else if (GApplication.user.getLoginUserId().equals("4")) {// 库管员
			// 获取的账户中心
			managerClass.getRuning().runding(DiZhiYaPinKuangJiaActivity.this, "正在获取数据...");
			getWarehouseTaskListAndCount();// 管库员查看档案柜任务列表（包括抵质押品和账户资料）
			tvTitleItem.setText("档案柜");
		} else if (GApplication.user.getLoginUserId().equals("27")) {
			tvTitleItem.setText("账户中心");
			// 账户资料装袋和如账户中心网络请求方法
			getIncenterandAccountintiontopackges();
			;
		}

		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
//				getAccountTurnOverLineCount();// 网络请求
			}
		};
	}

	/***
	 * 清分人员 获取抵制押品的任务数量
	 */
	private void getClearCollateralTaskListAndCount() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = GApplication.user.getYonghuZhanghao();
					netresultClean = new AccountAndResistCollateralService().getClearCollateralTaskListAndCount(number);
					if (!netresultClean.equals("")) {
						Gson gson = new Gson();
						Log.e(TAG, "测试数据源" + netresultClean.toString());
						mClearCollateralTaskListAndCount = gson.fromJson(netresultClean,
								ClearCollateralTaskListAndCount.class);

						handler.sendEmptyMessage(9);
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

	/**
	 * //账户资料装袋和如账户中心网络请求方法
	 */
	private void getIncenterandAccountintiontopackges() {
		new Thread() {
			@SuppressLint("LongLogTag")
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = GApplication.user.getYonghuZhanghao();
					netresultgetCount = new AccountAndResistCollateralService().getAccountTaskListAndCount(number);
					Log.e(TAG, "测试数据源" + netresultgetCount.toString());
					if (netresultgetCount != null && !netresultgetCount.equals("anyType{}")) {
						Gson gson = new Gson();
						atlace = gson.fromJson(netresultgetCount, AccountTaskListAndCountEntity.class);
						atlace.getBaggingCount();// 账户资料的数量
						atlace.getInCenterCount();// 入账户中心的数量
						Log.e(TAG, "网络请求====" + netresult.toString());
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e); /// 网络请求
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/***
	 * 入库交接的网络请求——库管员
	 */
	private void getAccountUnHandoverCountAndList() {
		new Thread() {
			@SuppressLint("LongLogTag")
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = GApplication.user.getYonghuZhanghao();
					Log.i(TAG, "网络请求====" + number);
					netresult = new AccountInfomationReturnService().getAccountUnHandoverCountAndList(number);
					if (netresult != null && !netresult.equals("anyType{}")) {
						Gson gson = new Gson();
						aiihEntity = gson.fromJson(netresult, AccountInfoInHandoverEntity.class);
						arraylist.clear();
						arraylist.add(aiihEntity);
						handler.sendEmptyMessage(4);
					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e); /// 网络请求
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/***
	 * 管库员获取任务数量
	 */
	private void getWarehouseTaskListAndCount() {
		new Thread() {
			@SuppressLint("LongLogTag")
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					String number = GApplication.user.getYonghuZhanghao();
					Log.e(TAG, "网络请求====" + number);

					netresult = new AccountAndResistCollateralService().getWarehouseTaskListAndCount(number);
					Log.e(TAG, "管库员获取数据源" + netresult.toString());
					if (netresult != null && !netresult.equals("anyType{}")) {
						Gson gson = new Gson();
						wtlac = gson.fromJson(netresult, WarehouseTaskListAndCount.class);
						wtlac.getAccountInCount();
						wtlac.getAccountOutCount();
						wtlac.getAccountPutInCount();
						wtlac.getAccountReturnCount();
						wtlac.getCollateralInCount();// 抵制押品入库
						wtlac.getCollateralOutCount();// 抵制押品出库
						Log.e(TAG, "网络请求====" + wtlac.getAccountInCount());
						handler.sendEmptyMessage(6);

					} else {
						handler.sendEmptyMessage(3);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "**===" + e); /// 网络请求
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e); /// 网络请求
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	private void initView() {

//		标题
		tvTitleItem = (TextView) findViewById(R.id.dz_frametextView1);
		// TODO Auto-generated method stub
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);

		GApplication.user.getLoginUserName();
		Log.i(TAG, "id=" + GApplication.user.getLoginUserId());
		Log.i(TAG, "name=" + GApplication.user.getLoginUserName());
		Log.i(TAG, "YonghuZhanghao=" + GApplication.user.getYonghuZhanghao());
		Log.i(TAG, "YonghuZhanghao()===" + GApplication.loginjueseid);// 获取角色的方法
		relazhaungdai = (RelativeLayout) findViewById(R.id.zhuangdai);
		relazhaungdai.setOnClickListener(this);
		relachuku = (RelativeLayout) findViewById(R.id.outroom);
		relachuku.setOnClickListener(this);
		relazhuangxiang = (RelativeLayout) findViewById(R.id.zhuangxiang);
		relazhuangxiang.setOnClickListener(this);
		relayruku = (RelativeLayout) findViewById(R.id.ruku);
		relayruku.setOnClickListener(this);

		zhanghuziliaozhuangdai = (RelativeLayout) findViewById(R.id.zhanghuziliaozhuangdai);
		zhanghuziliaozhuangdai.setOnClickListener(this);
		zhanghuziliaoruku = (RelativeLayout) findViewById(R.id.zhanghuziliaoruku);
		zhanghuziliaoruku.setOnClickListener(this);

		zhanghuziliaochukujieyue = (RelativeLayout) findViewById(R.id.zhanghuziliaochukujieyue);
		zhanghuziliaochukujieyue.setOnClickListener(this);

		zhanghuziliaoguihuan = (RelativeLayout) findViewById(R.id.zhanghuziliaoguihuan);
		zhanghuziliaoguihuan.setOnClickListener(this);

		zhaunghuziliaoguihuandaiguihuanziliaoruku = (RelativeLayout) findViewById(R.id.zhanghuziliaoguihuanrukudairuku);
		zhaunghuziliaoguihuandaiguihuanziliaoruku.setOnClickListener(this);

		ruzhanghuzhongxin = (RelativeLayout) findViewById(R.id.ruzhanghuzhongxin);
		ruzhanghuzhongxin.setOnClickListener(this);
		tvAccountcent = (TextView) findViewById(R.id.account_center_wangdian);// 账户中心任务条数
		accountinfotiontobagging = (TextView) findViewById(R.id.accountinfotiontobagging);// 账户资料装袋
		accountintioninhome_number = (TextView) findViewById(R.id.accountintionminhome_number_tv); // 账户中心入库的任务数量

		returntothelistofmaterialstobereturned_tv = (TextView) findViewById(
				R.id.returntothelistofmaterialstobereturned_tv);// 待归还资料列表
		resistthecollateral = (TextView) findViewById(R.id.resistthecollateral);// 抵制押品出库
		resistcollateralinhous = (TextView) findViewById(R.id.resistcollateralinhous);// 抵制押品入库
		accountintionoutboundborrowing = (TextView) findViewById(R.id.accountintionoutboundborrowing);// 账户资料出库借阅
		returnofaccountinformation_tv = (TextView) findViewById(R.id.returnofaccountinformation_tv);// 账户资料归还
		// 抵制押品
		resistpledgebagging_tv = (TextView) findViewById(R.id.resistpledgebagging_tv); // 装袋
		resistcollateralpacking = (TextView) findViewById(R.id.resistcollateralpacking);// 装箱
		ql_ruku_update = (Button) findViewById(R.id.ql_ruku_update);
		ql_ruku_update.setOnClickListener(this);
		if (GApplication.user.getLoginUserId().equals("26")) {

		}

		if (GApplication.user.getLoginUserId() != null) {

			System.out.print("id====" + GApplication.user.getLoginUserId());
			show(parseInt(GApplication.user.getLoginUserId()));
		}

	}

	private void show(int permission) {
		switch (permission) {
		// locationmanger 货位管理员 新增

		case FixationValue.clearer:

			relazhaungdai.setVisibility(View.VISIBLE);
			relazhuangxiang.setVisibility(View.VISIBLE);
			relachuku.setVisibility(View.GONE); //
			relayruku.setVisibility(View.GONE);
			zhanghuziliaozhuangdai.setVisibility(View.GONE);
			zhanghuziliaoruku.setVisibility(View.GONE);
			zhanghuziliaochukujieyue.setVisibility(View.GONE);
			zhanghuziliaoguihuan.setVisibility(View.GONE);
			zhaunghuziliaoguihuandaiguihuanziliaoruku.setVisibility(View.GONE);
			ruzhanghuzhongxin.setVisibility(View.GONE);
			/*
			 * 清楚查库服务按钮，下同
			 * 
			 * @author zhouKai
			 */

			break;
		case FixationValue.locationmanger:
			relazhaungdai.setVisibility(View.GONE);
			relazhuangxiang.setVisibility(View.GONE);
			relachuku.setVisibility(View.VISIBLE);
			relayruku.setVisibility(View.VISIBLE);
			zhanghuziliaozhuangdai.setVisibility(View.GONE); //
			zhanghuziliaoruku.setVisibility(View.VISIBLE);
			zhanghuziliaochukujieyue.setVisibility(View.VISIBLE);
			zhanghuziliaoguihuan.setVisibility(View.VISIBLE);//
			zhaunghuziliaoguihuandaiguihuanziliaoruku.setVisibility(View.GONE);
			ruzhanghuzhongxin.setVisibility(View.GONE);
			break;
		case FixationValue.AccountCenter:
			// 如账户中心的显示隐藏
			zhanghuziliaozhuangdai.setVisibility(View.GONE); // 已经在pc端完成
			ruzhanghuzhongxin.setVisibility(View.VISIBLE);
			relazhaungdai.setVisibility(View.GONE);
			relazhuangxiang.setVisibility(View.GONE);
			relachuku.setVisibility(View.GONE);
			relayruku.setVisibility(View.GONE);
			zhanghuziliaoruku.setVisibility(View.GONE);
			zhanghuziliaochukujieyue.setVisibility(View.GONE);
			zhanghuziliaoguihuan.setVisibility(View.GONE);//
			zhaunghuziliaoguihuandaiguihuanziliaoruku.setVisibility(View.GONE);

			break;
		case FixationValue.warehouse:
			zhanghuziliaozhuangdai.setVisibility(View.GONE); //
			ruzhanghuzhongxin.setVisibility(View.GONE);
			relazhaungdai.setVisibility(View.GONE);
			relazhuangxiang.setVisibility(View.GONE);
			relachuku.setVisibility(View.VISIBLE);
			relayruku.setVisibility(View.VISIBLE);
			zhanghuziliaoruku.setVisibility(View.VISIBLE);
			zhanghuziliaochukujieyue.setVisibility(View.VISIBLE);
			zhanghuziliaoguihuan.setVisibility(View.VISIBLE);//
			zhaunghuziliaoguihuandaiguihuanziliaoruku.setVisibility(View.GONE);
			break;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.zhuangdai: //装袋暂时合并暂时先不用了注释掉
//			Log.i("planNum", "" + GApplication.user.getLoginUserId());
//			Intent i4 = new Intent(DiZhiYaPinKuangJiaActivity.this,
//					BoxToDiZhiYaPinZhuangdaiActivity.class);
//			startActivity(i4);
//			break;
		case R.id.outroom: // 抵制押品 出库
			Intent i2 = new Intent(DiZhiYaPinKuangJiaActivity.this, DiZhiYaPinChuKuActivity.class);
			startActivity(i2);

			break;

		case R.id.ruku: // 抵制押品入库
			Intent i3 = new Intent(DiZhiYaPinKuangJiaActivity.this, DiZhiYaPinRuKuActivity.class);
			startActivity(i3);
			break;
//		case R.id.zhuangxiang: //抵制押品装箱 清分员    最新已经在别的页面完成
//			System.out.print("===========" + GApplication.user.getLoginUserId());
//			System.out.print( GApplication.user.getYonghuZhanghao());
//		
//			Log.i("测试",GApplication.user.getYonghuZhanghao());
//			GApplication.user.getLoginUserName();
//			Intent i = new Intent(DiZhiYaPinKuangJiaActivity.this,
//					DiZhiYaPinZhuangXiangTActivity.class);
//			startActivity(i);
//			break;
//		case R.id.zhanghuziliaozhuangdai: // 账户资料装袋// 当前页面废弃改为pc端执行
//			Intent i5 = new Intent(DiZhiYaPinKuangJiaActivity.this,
//					ZhangHuZiLiaoZhaungDaiActivity.class);
//			startActivity(i5);
//			break;
		case R.id.zhanghuziliaoruku: // 账户资料入库交接
			if (o_Application.qinglingruku.size() == 0 || o_Application.qinglingruku == null) {
				Toast.makeText(DiZhiYaPinKuangJiaActivity.this, "没有任务请刷新", 500).show();
				return;
			} else {
				Intent i6 = new Intent(DiZhiYaPinKuangJiaActivity.this, AccountInfoInHandoverActivity.class);
				startActivity(i6);

			}
			break;
		case R.id.zhanghuziliaochukujieyue: // 账户资料出库借阅
			Log.e("", "===" + "ZhangHuZiLiaoChuKuJieYueActivity");
			Intent i7 = new Intent(DiZhiYaPinKuangJiaActivity.this, ZhangHuZiLiaoChuKuJieYueActivity.class);
			Log.e("", "===" + "ZhangHuZiLiaoChuKuJieYueActivity");
			startActivity(i7);
			break;
		case R.id.zhanghuziliaoguihuan: // 账户资料归还
			int  return_int=0;
			return_int=wtlac.getAccountReturnCount();

			if(return_int==0){
				Toast.makeText(DiZhiYaPinKuangJiaActivity.this, "没有任务请刷新", 500).show();
			}else{

			Intent i8 = new Intent(DiZhiYaPinKuangJiaActivity.this, ZhangHuZiLiaoGuiHuanActivity.class);
			Log.e("", "===" + "ZhangHuZiLiaoChuKuJieYueActivity");

			startActivity(i8);
			}
			break;
		// 账户资料待归还
		case R.id.zhanghuziliaoguihuanrukudairuku: // 账户资料待归还
			String netcoutn=returnofaccountinformation_tv.getText().toString().trim();
		     int  newcount =parseInt(netcoutn);
			if(newcount>0){
			Intent i9 = new Intent(DiZhiYaPinKuangJiaActivity.this, ZhangHuZiLiaoZhangHuDaiGuiHuanActivity.class);
			Log.e("", "===" + "ZhangHuZiLiaoChuKuJieYueActivity");
			startActivity(i9);
			}else{
				Toast.makeText(DiZhiYaPinKuangJiaActivity.this, "没有任务请刷新", 500).show();
			}
			break;
		// /账户中心入账中心
		case R.id.ruzhanghuzhongxin:
			if (atlace.getInCenterCount() == 0) {
				Toast.makeText(DiZhiYaPinKuangJiaActivity.this, "没有任务", 500).show();

			} else {
				Skip.skip(this, RuZhangHuZhongXinActivity.class, null, 0);
				Log.e("", "===" + "ZhangHuZiLiaoChuKuJieYueActivity");

			}

		case R.id.ql_ruku_update:
//			getAccountTurnOverLineCount();// 网络请求
			if (o_Application.qinglingruku.size() > 0) {
				o_Application.qinglingruku.clear();
			}
			if (GApplication.user.getLoginUserId().equals("7")) {// 清分员

				getClearCollateralTaskListAndCount();// 获取任务数量

			} else if (GApplication.user.getLoginUserId().equals("4")) {// 库管员
				getWarehouseTaskListAndCount();// 管库员查看档案柜任务列表（包括抵质押品和账户资料）
				getAccountUnHandoverCountAndList();
			} else if (GApplication.user.getLoginUserId().equals("27")) {
//				getAccountTurnOverLineCount();// 网络请求账户资料交接入库的第一个页面：查询待做的线路数
				// 账户资料装袋和如账户中心网络请求方法
				getIncenterandAccountintiontopackges();
			}
			break;

		case R.id.ql_ruku_back:
			DiZhiYaPinKuangJiaActivity.this.finish();
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
			managerClass.getRuning().remove();

			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinKuangJiaActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				managerClass.getAbnormal().remove();
				manager.getAbnormal().timeout(DiZhiYaPinKuangJiaActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				if (mAccountCenterInEntity.getCount() == null) {
					tvAccountcent.setText(account);
				} else {
					tvAccountcent.setText(mAccountCenterInEntity.getCount());
				}

				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(DiZhiYaPinKuangJiaActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:
				accountintioninhome_number.setText(aiihEntity.getCount());
				getData();
				break;

			case 5:// 账户中心
				// 账户资料的数量
				// 入账户中心的数量
				tvAccountcent.setText("" + atlace.getInCenterCount());
				accountinfotiontobagging.setText("" + atlace.getBaggingCount());
				break;

			case 6: // 库管员
				wtlac.getAccountInCount();
				Log.e(TAG, "==账户资料入库==" + wtlac.getAccountInCount());

				resistthecollateral.setText("" + wtlac.getCollateralOutCount());// 抵制押品出库
				resistcollateralinhous.setText("" + wtlac.getCollateralInCount());// 抵制押品入库
				accountintioninhome_number.setText("" + wtlac.getAccountInCount());// 账户资料入库
				accountintionoutboundborrowing.setText("" + wtlac.getAccountOutCount());// 账户资料出库借阅
				returnofaccountinformation_tv.setText("" + wtlac.getAccountReturnCount());// 账户资料归还
				returntothelistofmaterialstobereturned_tv.setText("" + wtlac.getAccountPutInCount());// 待归还
				// 当入库交接网络请求大于0时证明有任务 进行网络请求
				if (wtlac.getAccountInCount() != 0) {
					getAccountUnHandoverCountAndList();// 入库交接的网络请求——库管员
				}
				managerClass.getRuning().remove();
				managerClass.getAbnormal().remove();
				break;
			case 9:
//				mClearCollateralTaskListAndCount.getCollateralBaggingCount()

				resistpledgebagging_tv.setText("" + mClearCollateralTaskListAndCount.getCollateralBaggingCount());
				resistcollateralpacking.setText("" + mClearCollateralTaskListAndCount.getCollateralPacketCount());// 装箱
				break;
			default:
				break;
			}
		}

	};

	/***
	 * 集合添加数据账户资料入库交接
	 */

	public void getData() {
		o_Application.qinglingruku.clear();/// 每次进入要清空下集合否则数据重复
		if (arraylist.size() > 0) {
			for (int i = 0; i < arraylist.size(); i++) {
				o_Application.qinglingruku.add(new QingLingRuKu(null, null, arraylist.get(i).getStockCodeList()));
			}
			o_Application.qlruku = o_Application.qinglingruku.get(0);// 这里只选中第一条
			Log.e(TAG, "====" + o_Application.qlruku.getZhouzhuanxiang() + "====");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		managerClass.getRuning().remove();
	}
}
