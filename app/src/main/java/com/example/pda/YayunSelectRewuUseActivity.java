package com.example.pda;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import cn.poka.util.ShareUtil;

import com.application.GApplication;
import com.golbal.pda.GolbalUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRenwuUserDispatchList;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectRewuUserEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunSelectTaskEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunyuanSelectRenWuUserBaseEntity;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunyuanSelectRenWuUserCodeEntity;
import com.ljsw.tjbankpda.main.ZhouzhuanxiangMenu;
import com.ljsw.tjbankpda.qf.entity.YaYunLb;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.TableQuzhi;
import com.ljsw.tjbankpda.yy.activity.YaYunRwMingxiActivity;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;
import com.service.NetService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * ql必须做 上缴选择做重点
 * 
 * @author Administrator
 * 
 * 
 *         .SaveAuthLogYayun( S_application.getApplication().s_zzxShangjiao,
 *         S_application.getApplication().s_zzxQingling,
 *         S_application.getApplication().s_userWangdian,
 *         S_application.getApplication().s_userYayun,
 *         S_application.getApplication().s_yayunXianluId, qlJiaojieType,
 *         sjJiaojieType);
 * 
 */
public class YayunSelectRewuUseActivity extends FragmentActivity implements View.OnClickListener {

	protected static final String TAG = "YayunSelectRewuUseActivity";
	private ListView listviewshowselect;// 显示用的组件
	private List<YayunSelectTaskEntity> YayunSelectRewuMinglist = new ArrayList<YayunSelectTaskEntity>();

	private TextView ysrwmxtvtitle, tvtextViewcount;// 线路名称//
	private ImageView ac_yayun_selecrenwuminguserxiblack;
	private ManagerClass manager;
	private View.OnClickListener OnClick1;
	private Dialog dialogfa;// 失败
	private DialogManager dialogManager;
	private ICleaningManService is = new ICleaningManService();// 要修改的网络接口服务类
	private YayunSelectTaskEntity yayunSelectTaskEntity = new YayunSelectTaskEntity();// 押运任务选取实体类
	private YayunSelectRewuUserEntity YayunSelectRewuUserEntity = new YayunSelectRewuUserEntity();
	private List<YayunSelectRewuUserEntity> YayunSelectRewuUserList = new ArrayList<YayunSelectRewuUserEntity>();

	private List<YayunSelectRewuUserEntity> mDatas = new ArrayList<YayunSelectRewuUserEntity>();
	// 两个同时存在先做
	private boolean network = true; // 是否有网络
	private Context mContext;
	private YayunThread yt;

	// old
	Table[] tables;
	private TextView yy_XianluName, // 线路名
			jigoushuliangView; // 机构数量显示

	View.OnClickListener onclickreplace;
	View.OnClickListener onclickreplace1;
	View.OnClickListener onclickreplace2;
	private String sjPgdan, // 上缴派工单
			qlPgdan, // 请领派工单
			xianluming, // 线路名称
			xianluType, // 线路类别
			pdate;// 配送日期

	private String biaoshi;
	private String jigouleibie;

	private List<String> qlZzxlist = new ArrayList<String>(); // 请领周转箱集合
	private List<String> sjZzxlist = new ArrayList<String>(); // 上缴周转箱集合
	private YaYunLb YaYunLb; // 被选中的网点
	private YayunyuanSelectRenWuUserBaseEntity YaYunLb1; // 被选中的网点
	private ManagerClass managerClass;
	private List<YaYunLb> list = new ArrayList<YaYunLb>();
	private YayunSelectRenwuUserDispatchList yysrwudispatchlist = new YayunSelectRenwuUserDispatchList();// 派工单实体类
	private List<YayunSelectRenwuUserDispatchList> disoatchlist = new ArrayList<YayunSelectRenwuUserDispatchList>();// 派工单集合
	private List<String> disoatchstrlist = new ArrayList<String>();// 派工单集合
	String params;// 返回机构的类别
	LiebiaoAdapter liebiaoadapter;

	private List<YayunyuanSelectRenWuUserCodeEntity> yyysrwycodelist = new ArrayList<YayunyuanSelectRenWuUserCodeEntity>();// 更改数据结构后的外层list
	private List<YayunyuanSelectRenWuUserBaseEntity> yyysrwybaselist = new ArrayList<YayunyuanSelectRenWuUserBaseEntity>();// 更改数据结构后的外层list
	private String netPonitCorpid = "";// / 网点cord
	String linename = "";

	private Button yyy_took_update;// 更新

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yayun_select_rewu_ming_xiuser);
		manager = new ManagerClass();
		mContext = YayunSelectRewuUseActivity.this;
		managerClass = new ManagerClass();
		InitView();
		YayunSelectRewuMinglist = (List<YayunSelectTaskEntity>) getIntent().getSerializableExtra("yayunlist");
		dialogManager = new DialogManager(YayunSelectRewuUseActivity.this);
		// yayunrenwu
		// 接收网络状态广播后，发出handler通知
		NetService.handnet = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					network = true;
				} else {
					network = false;
				}
			}
		};
		// 手动判断是否有网络
		if (NetService.info != null && !NetService.info.isConnectedOrConnecting()) {
			network = false;
		} else if (NetService.info == null) {
			network = false;
		}
		onclickreplace2 = new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				managerClass.getAbnormal().remove();
				Xianlu();
			}

			/***
			 * 获取线路
			 */
		};
		onclickreplace1 = new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				managerClass.getAbnormal().remove();
				getJigouLeibie();
			}
			/***
			 * 获取交接类别
			 */

		};

		// 重试单击事件
		onclickreplace = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NetService.handnet = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						if (msg.what == 1) {
							network = true;
						} else {
							network = false;
						}
					}
				};
				// 手动判断是否有网络
				if (NetService.info != null && !NetService.info.isConnectedOrConnecting()) {
					network = false;
				} else if (NetService.info == null) {
					network = false;
				}
				managerClass.getAbnormal().remove();
				managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "正在获取数据...");
				yt = new YayunThread();
				yt.start();
				if ("qitiao".equals(biaoshi)) {
					managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "正在获取数据...");
					HuoquZzxThread ht = new HuoquZzxThread();
					ht.start();
				}

			}
		};

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:// 显示数据
					managerClass.getRuning().remove();

					String str = msg.obj.toString();
					tables = Table.doParse(str);
					System.out.println("str:" + str);
					// 获取线路id
					S_application.getApplication().s_yayunXianluId = TableQuzhi.getZhi(tables[0], "xianluId");
					// 获取线路名称
					xianluming = TableQuzhi.getZhi(tables[0], "xianluming");
					xianluming = xianluming == null ? "无" : xianluming;
					// 获取线路类型
					xianluType = TableQuzhi.getZhi(tables[0], "xianluType");
					// 获取上缴派工单
					sjPgdan = TableQuzhi.getZhi(tables[0], "sjpaigongdan");
					System.out.println("sjPgdan:" + sjPgdan);
					S_application.getApplication().sjpaigongdan = sjPgdan;
					// 获取请领派工单
					qlPgdan = TableQuzhi.getZhi(tables[0], "qlpaigongdan");
					System.out.println("qlPgdan:" + qlPgdan);
					S_application.getApplication().s_qlpaigongdan = qlPgdan;
					// 获取配送日期
					pdate = TableQuzhi.getZhi(tables[0], "peisongriqi");
					pdate = pdate == null ? "" : pdate;
					Log.e(TAG, "我是获取的数据：" + "日期：=" + pdate + "请领派工单=" + qlPgdan + "上缴派工单：" + sjPgdan + "xianluType："
							+ xianluType + "xianluming:===" + "==" + xianluming);
//					yy_XianluName.setText(xianluming);
					getData();
					managerClass.getRuning().remove();
					// 获取交接类型
					if (!"".equals(xianluType) && null != xianluType && xianluType.equals("0")) { // 如果是普通线路
						if (jigouleibie.equals("0")) {
							S_application.getApplication().jiaojieType = 1; // 总库押运
						} else {
							S_application.getApplication().jiaojieType = 2; // 分库押运
						}
					} else { // 如果是直属线路
						S_application.getApplication().jiaojieType = 3;
						System.out.println("交接类型:" + S_application.getApplication().jiaojieType);
					}
					break;
				case -1:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "信息加载异常", onclickreplace);
					break;
				case 0:
					// managerClass.getRuning().remove();
					break;
				case 3:// 跳转下一个页面
					managerClass.getRuning().remove();
					Bundle bundle = new Bundle();
					bundle.putSerializable("qllist", (Serializable) qlZzxlist);
					bundle.putSerializable("sjlist", (Serializable) sjZzxlist);
					bundle.putSerializable("YaYunLb", YaYunLb1);
					bundle.putString("pdate", pdate);
					bundle.putString("xianluType", xianluType);
					bundle.putString("XianLu", xianluming);
					System.out.println("准备跳转页面");
					System.out.print("!!!!!!!" + bundle);
					if (null != bundle) {
						Skip.skip(YayunSelectRewuUseActivity.this, YaYunRwMingxiActivity.class, bundle, 0);
					} else {
						Log.e(TAG, "bundle 是空的");
					}

					break;
				case -4:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "连接超时，重新链接？", onclickreplace);
					break;
				case 5:
					managerClass.getRuning().remove();
					// 获取交接类型
					if (!"".equals(xianluType) && null != xianluType && xianluType.equals("0")) { // 如果是普通线路

						if (params != null && params.equals("0")) {
							S_application.getApplication().jiaojieType = 1; // 总库押运
						} else {
							S_application.getApplication().jiaojieType = 2; // 分库押运
						}
					} else { // 如果是直属线路
						S_application.getApplication().jiaojieType = 3;
						System.out.println("交接类型:" + S_application.getApplication().jiaojieType);
					}
					break;
				case 6:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "连接超时，重新链接？", onclickreplace2);
					break;
				case 7:
					managerClass.getRuning().remove();
					// managerClass.getAbnormal().timeout(YayunRwLbSActivity.this,
					// "获取失败", onclickreplace2);
					break;
				case 8:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "信息加载异常", onclickreplace2);
					break;
				case 15:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "连接超时，重新链接？", onclickreplace2);
					break;
				case 13:
					managerClass.getRuning().remove();
					// managerClass.getAbnormal().timeout(YayunRwLbSActivity.this,
					// "获取失败", onclickreplace2);
					break;
				case 14:
					managerClass.getRuning().remove();
					managerClass.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "信息加载异常", onclickreplace2);
					break;

				case 50:
					liebiaoadapter.notifyDataSetChanged();
					liebiaoadapter = new LiebiaoAdapter();
					listviewshowselect.setAdapter(liebiaoadapter);
					Log.e(TAG, "我在点击前！！！！changdu     " + yyysrwybaselist.size());
//					tvtextViewcount.setText(mDatas.size() + "条");
					liebiaoadapter.notifyDataSetChanged();
					break;

				case 51:
					manager.getRuning().remove();
					manager.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "网络连接失败,重试?",
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									manager.getAbnormal().remove();
									GetHadTask();
								}
							});
					break;

				case 52:
					ShareUtil.toastShow("没有查询到数据", YayunSelectRewuUseActivity.this);
					break;
				case 53:
					yyysrwybaselist.clear();
					liebiaoadapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}

		};
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getJigouLeibie();
		System.out.print("获得押运员=======" + S_application.getApplication().s_userYayun);

	}

	/***
	 * 现在已经有任务了进行查询 并显示数据
	 * 
	 * 一个派工单下一个线
	 */
	private String linname = "";

	private void GetHadTask() {

		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.e(TAG, "----LoadDataNow" + GApplication.use.getUserzhanghu());
					// 用户账号
					String userZhanghu = GApplication.use.getUserzhanghu();
					;
					String netresultClean = new AccountAndResistCollateralService()
							.queryJobOrderDetailHandleStatusByEscort(userZhanghu);// 返回数据源是msg+params
					// 需要分开进行解决
					// netresultClean测试数据源
					// [{"linename":"北辰支行中心库一线","flag":"0","corpid":"904050100","state":"01","corpname":"小淀支行","linenum":"BC01"}],
					// {"sj":"BC0120191218174053"}

					// 思路规则 同一条线路下可以有上缴和请领两个派工单 也可以只有一个上缴或者是请领 2 可以给你每个网点下都加入派工单
					// 无论上缴还是请领
					Log.e(TAG, "返回=============netresultClean=====" + netresultClean);
					if (null == netresultClean) {

						handler.sendEmptyMessage(52);
					} else if (netresultClean.equals("该押运员没有正在执行的派工单任务")) {
						handler.sendEmptyMessage(53);
						/// 这里是跳转领取任务页面还是在当前页面不显示数据 只有页面
					} else {
						String strnew = netresultClean.substring(0, netresultClean.indexOf("*"));
						String Dispatch = netresultClean.substring(strnew.length() + 1, netresultClean.length());
						Log.e(TAG, "返回==================" + Dispatch);
						Log.e(TAG, "返回==================" + strnew);
						Gson gson = new Gson();
						// 解析派工单
						disoatchlist.clear();
						yyysrwycodelist.clear();
						YayunSelectRenwuUserDispatchList jrb = gson.fromJson(Dispatch,
								YayunSelectRenwuUserDispatchList.class);
						Log.e(TAG, "获取派工单集合=====!!!!!" + jrb.getQl() + "======" + jrb.getSj());
						YayunSelectRenwuUserDispatchList yyselectdispath = new YayunSelectRenwuUserDispatchList();
						yyselectdispath.setQl(jrb.getQl());
						yyselectdispath.setSj(jrb.getSj());
						disoatchlist.add(yyselectdispath);

						for (int i = 0; i < disoatchlist.size(); i++) {
							Log.e(TAG, "第一次获取派工单集合的请领和上缴=====!!!!!" + jrb.getQl() + "======" + jrb.getSj());
							Log.e(TAG, "第一次获取派工单集合的请领和上缴=====!!!!!" + disoatchlist.get(i).getQl() + "======"
									+ disoatchlist.get(i).getSj());
						}
						// 为每条线路下的 =DispatchList.get(i)= 派工单

						if (!strnew.equals("") || strnew != null || !strnew.equals("anyType{}")) {
							// 数据返回
							Log.e(TAG, "netresultClean测试数据源" + strnew.toString());
							YayunSelectRewuUserList.clear();
							linname = "";
							list.clear();// 每次进入需要清除数据
							Type type = new TypeToken<ArrayList<YayunyuanSelectRenWuUserCodeEntity>>() {
							}.getType();

							List<YayunyuanSelectRenWuUserCodeEntity> list = gson.fromJson(strnew, type);

							yyysrwycodelist = list;
							//
							// // 将数据整合变成分组目的是适配时显示数据和隐藏//获取线路 去重线路
							// List<String> strList=new ArrayList<String>();
							// List<String> netPointList=new ArrayList<String>();
							// for(int i=0;i<list.size();i++){
							// String name=list.get(i).getLinename();
							// if(!strList.contains(name)){
							// strList.add(name);
							// }
							// //// 网点去除重复
							// // if(!netPointList.contains(netPotnt)){
							// // netPointList.add(netPotnt);
							// // }
							//
							//
							// }
							//
							// List<YayunSelectRewuUserCodeEntity> bigList=new
							// ArrayList<YayunSelectRewuUserCodeEntity>();
							// // disoatchlist 派工单长度=== 线路的长度 每条线路下要具有派工单 上缴、请领或者都存在
							// for(int i=0;i<strList.size();i++){
							// String str=strList.get(i);// 获取线路名称
							// for (int j = 0; j < netPointList.size(); j++) {
							// String strnetpoint=netPointList.get(i);// 获取网点
							// }
							//
							//
							// YayunSelectRewuUserCodeEntity rewuUserCodeEntity=new
							// YayunSelectRewuUserCodeEntity();
							// List<YayunSelectRewuUserEntity> smallList=new
							// ArrayList<YayunSelectRewuUserEntity>();
							// for(YayunSelectRewuUserEntity rewuUserEntity:list){
							// if(rewuUserEntity.getLinename().equals(str)){
							// // 如果存在上缴任务
							// // if()
							// if(null!=disoatchlist&&disoatchlist.size()>0){//
							// 设置派工单的长度
							//
							// if(rewuUserEntity.getFlag().equals("0")){// 设置上缴和请领
							// // 派工到哪添加到实体类
							// rewuUserEntity.setQl(disoatchlist.get(i).getQl());
							// }else{
							// rewuUserEntity.setSj(disoatchlist.get(i).getSj());
							// }
							// //
							//
							// }
							// smallList.add(rewuUserEntity);
							// }
							// }
							// rewuUserCodeEntity.setLinename(str);
							// rewuUserCodeEntity.setData(smallList);
							// bigList.add(rewuUserCodeEntity);
							// // }
							// }
							// 将数据重新分散掉

							// for(YayunSelectRewuUserCodeEntity bigEntity:bigList){
							// List<YayunSelectRewuUserEntity>
							// smallList=bigEntity.getData();
							// for(YayunSelectRewuUserEntity smallEntity:smallList){
							// mDatas.add(smallEntity);
							// }
							// }

							// 打印
							Log.e(TAG, "查看长度派工单---" + disoatchlist.size());
							yyysrwybaselist.clear();
							List<YayunyuanSelectRenWuUserBaseEntity> baselist2 = new ArrayList<YayunyuanSelectRenWuUserBaseEntity>();
							for (YayunyuanSelectRenWuUserCodeEntity codeEntity : yyysrwycodelist) {
								// 拿到base集合
								List<YayunyuanSelectRenWuUserBaseEntity> baselist1 = codeEntity.getData();
								for (YayunyuanSelectRenWuUserBaseEntity entity : baselist1) {
									entity.setLinename(codeEntity.getLinename());
									entity.setLinenum(codeEntity.getLinenum());
									// 当前农行的业务逻辑不可删除
									// 派工单说明： 1两种情况一种派工单实体的集合长度为2 线路1 下所有网点是sj
									// 线路2下所有网点为ql总是互斥
									// 2线路只有一条 派工单实体集合为长度为1 线路1 下网点1 有上缴没请领，网点2
									// 只有请领请领没上交网点3既有上缴也有请领 （网点1和网点3的情况共用一个上缴派工单）
									// 3 上缴05到01 状态是由pc端操作完成pda无法控制
									// 4 请领有任务时肯定有派工单 上缴一个列表只有一个是不为00
									// 状态那么就有派工单其它状态均有派工单
									baselist2.add(entity);
								}
							}

							for (int i = 0; i < baselist2.size(); i++) {
								Log.e(TAG, baselist2.size() + "=====baselist2长度");
								Log.e(TAG, baselist2.get(i) + "=====baselist2长度");
							}
							for (YayunyuanSelectRenWuUserBaseEntity baseentity : baselist2) {
								// 上缴：00待确认 01待交接 02/04已交接 /// 线路和派工单两条
								if (linename == null || linename.equals("")) {
									linename = baseentity.getLinename();// / 线路1
								} else {
									if (linename.equals(baseentity.getLinename())) {
										Log.e(TAG, "linenname" + linename);
									} else {
										// linename=baseentity.getLinename();// 长度为2
										// 的情况//// 线路2

									}
								}

								if (null != disoatchlist && yyysrwycodelist.size() > 1 && disoatchlist.size() > 1) {

									// if((!baseentity.getSjstate().equals("00"))||baseentity.getSjstate().equals("01")||
									// baseentity.getSjstate().equals("02")||baseentity.getSjstate().equals("04")){
									// for (int i = 0; i < disoatchlist.size(); i++)
									// {
									// baseentity.setSj(disoatchlist.get(i).getSj());
									// }
									// }else
									// if(baseentity.getSjstate().equals("00")){
									//
									// Log.e(TAG,"当前没有派工单或者没生成");
									// }
									// // 请领： 05待交接 00/04已交接
									// if(baseentity.getQlstate().equals("05")||baseentity.getQlstate().equals("00")||
									// baseentity.getQlstate().equals("04")){
									// for (int i = 0; i < disoatchlist.size(); i++)
									// {
									// baseentity.setQl(disoatchlist.get(i).getQl());
									// }
									// }
									if (linename != null || !linename.equals("")) {
										if (linename.equals(baseentity.getLinename())) {// 长度是一
											if ((!baseentity.getSjstate().equals("00"))
													|| baseentity.getSjstate().equals("01")
													|| baseentity.getSjstate().equals("02")
													|| baseentity.getSjstate().equals("04")) {

												for (int i = 0; i < disoatchlist.size(); i++) {
													baseentity.setSj(disoatchlist.get(i).getSj());
												}
											} else if (baseentity.getSjstate().equals("00")) {

												Log.e(TAG, "当前没有派工单或者没生成");
											}

											// 请领： 05待交接 00/04已交接
											if (baseentity.getQlstate().equals("05")
													|| baseentity.getQlstate().equals("00")
													|| baseentity.getQlstate().equals("04")) {
												for (int i = 0; i < disoatchlist.size(); i++) {
													baseentity.setQl(disoatchlist.get(i).getQl());
												}
											}
										} else {// / 两个线路名不相等
											if ((!baseentity.getSjstate().equals("00"))
													|| baseentity.getSjstate().equals("01")
													|| baseentity.getSjstate().equals("02")
													|| baseentity.getSjstate().equals("04")) {
												for (int i = 0; i < disoatchlist.size(); i++) {
													baseentity.setSj(disoatchlist.get(i).getSj());
												}
											} else if (baseentity.getSjstate().equals("00")) {

												Log.e(TAG, "当前没有派工单或者没生成");
											}

											// 请领： 05待交接 00/04已交接
											if (baseentity.getQlstate().equals("05")
													|| baseentity.getQlstate().equals("00")
													|| baseentity.getQlstate().equals("04")) {
												for (int i = 0; i < disoatchlist.size(); i++) {
													baseentity.setQl(disoatchlist.get(i).getQl());
												}
											}
										}
									}
									// 当前只有一条线路和一条派工单的实体
								} else if (disoatchlist.size() == 1 && yyysrwycodelist.size() == 1
										&& null != disoatchlist) {
									// for (int i = 0; i < disoatchlist.size(); i++)
									// {
									// 请领： 05待交接 00/04已交接
									if (null != baseentity.getQlstate()) {
										if (baseentity.getQlstate().equals("05") || baseentity.getQlstate().equals("00")
												|| baseentity.getQlstate().equals("04")) {
											for (int i = 0; i < disoatchlist.size(); i++) {
												baseentity.setQl(disoatchlist.get(i).getQl());
												Log.e(TAG, "1111111111111！！！！！！！！" + disoatchlist.get(i).getQl());
											}
										} else {
											Log.e(TAG, ".getQlstate()" + "不符合派工单的条件");
										}

									} else {
										Log.e(TAG, ".getQlstate()" + "没有派工单");
									}
									// sj
									if (null != baseentity.getSjstate()) {
										if ((!baseentity.getSjstate().equals("00"))
												|| baseentity.getSjstate().equals("01")
												|| baseentity.getSjstate().equals("02")
												|| baseentity.getSjstate().equals("04")) {
											for (int i = 0; i < disoatchlist.size(); i++) {
												baseentity.setSj(disoatchlist.get(i).getSj());
												Log.e(TAG, "1111111111111！！！！！！！！" + disoatchlist.get(i).getSj());
											}
										} else if (baseentity.getSjstate().equals("00")) {

											Log.e(TAG, "当前没有派工单或者没生成");
										}
									}
								}
								yyysrwybaselist.add(baseentity);

							}

							// disoatchlist
							for (YayunSelectRenwuUserDispatchList strentity01 : disoatchlist) {
								Log.e(TAG, "请领" + strentity01.getQl());
								Log.e(TAG, "上缴" + strentity01.getSj());

							}

							for (YayunyuanSelectRenWuUserBaseEntity yayunyuan : yyysrwybaselist) {
								Log.i(TAG, "派工单ql" + yayunyuan.getQl());
								Log.i(TAG, "派工单sj----a--" + yayunyuan.getSj());
								Log.i(TAG, "派工单sj----b--" + yayunyuan.getCorpname());
								Log.i(TAG, "派工单sj----c-" + "：：：：：：：" + yyysrwybaselist.size());
							}

							if (null != yyysrwybaselist && yyysrwybaselist.size() > 0) {
								Log.e(TAG, "yyysrwybaselist----" + yyysrwybaselist.size());
								handler.sendEmptyMessage(50);
							} else {
								Log.e(TAG, "网路请求失败了");

								// handler.sendEmptyMessage(3);
							}

						} else {
							Log.e(TAG, "网路请无数据;");
							handler.sendEmptyMessage(52);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e);
					handler.sendEmptyMessage(51);
				}
			}

		}.start();
	}

	@Override
	protected void onResume() {
		if (mDatas.size() > 0) {
			mDatas.clear();
		}
		try {
			GetHadTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		OnClick1 = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
			}
		};
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void Xianlu() {
		managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "获取线路中.....");
		new Thread() {
			public void run() {
				super.run();
				try {
					params = is.getJigouLeibie(S_application.getApplication().s_yayunJigouId);
					if (params != null || !params.equals("")) {
						// S_application.getApplication().jiaojieType=1;
						Log.d(TAG, "获取线路下 的数据::::" + params);
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(7);
					}
				} catch (SocketTimeoutException e) {
					handler.sendEmptyMessage(6);
				} catch (NullPointerException e) {
					handler.sendEmptyMessage(7);
				} catch (Exception e) {
					handler.sendEmptyMessage(8);
				}

			};
		}.start();
	}

	/***
	 * 点击条目进行跳转
	 */
	private void InitView() {
		System.out.print("获得押运员=======GApplication.use.getUserzhanghu()" + GApplication.use.getUserzhanghu());
		yyy_took_update = (Button) findViewById(R.id.yyy_took_update);
		yyy_took_update.setOnClickListener(this);
		liebiaoadapter = new LiebiaoAdapter();
		// yayunyaunUserAdapter = new YayunyaunUserAdapter();
		listviewshowselect = (ListView) findViewById(R.id.listviewshowselect1);
		ysrwmxtvtitle = (TextView) findViewById(R.id.textView1);// 线路名称
		ac_yayun_selecrenwuminguserxiblack = (ImageView) findViewById(R.id.ac_yayun_selecrenwuminguserxiblack);
		ac_yayun_selecrenwuminguserxiblack.setOnClickListener(this);
		jigoushuliangView = (TextView) this.findViewById(R.id.textView5);

		listviewshowselect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// S_application.getApplication().wangdianJigouId="904050100";
				Log.e(TAG, "我在点击！！！！" + arg2);

				Log.e(TAG, "我在点击！！！！" + yyysrwybaselist.get(arg2));
				Log.e(TAG, "我在点击！！！sj！" + yyysrwybaselist.get(arg2).getSj());
				Log.e(TAG, "我在点击！！！ql    " + yyysrwybaselist.get(arg2).getQl());
				Log.e(TAG, "我在点击！！！！" + yyysrwybaselist.get(arg2).getLinename());
				Log.e(TAG, "我在点击！！！！" + yyysrwybaselist.get(arg2).getLinenum());
				Log.e(TAG, "我在点击！！！！" + yyysrwybaselist.get(arg2));
				// biaoshi = "qitiao";
				Log.e(TAG, "getSjappdate！" + yyysrwybaselist.get(arg2).getSjappdate());
				// biaoshi = "qitiao";
				/// 判断点击事件上是否应该跳转

				if (null == yyysrwybaselist.get(arg2).getQlstate()
						|| ("").equals(yyysrwybaselist.get(arg2).getQlstate())
								&& null != yyysrwybaselist.get(arg2).getSjstate()) {
					if (yyysrwybaselist.get(arg2).getSjstate().equals("04")
							|| yyysrwybaselist.get(arg2).getSjstate().equals("00")) {
						if (yyysrwybaselist.get(arg2).getSjstate().equals("00")) {
							ShareUtil.toastShow("任务状态为待确认不允许做", YayunSelectRewuUseActivity.this);
//						Toast.makeText(YayunSelectRewuUseActivity.this, "任务状态为待确认不允许做", 100).show();
						} else if (yyysrwybaselist.get(arg2).getSjstate().equals("04")) {
//						Toast.makeText(YayunSelectRewuUseActivity.this, "任务状态为已经交接不允许做", 100).show();
							ShareUtil.toastShow("任务状态为已经交接不允许做", YayunSelectRewuUseActivity.this);
						}

					} else {
						YaYunLb1 = yyysrwybaselist.get(arg2);

						sjPgdan = yyysrwybaselist.get(arg2).getSj();
						qlPgdan = yyysrwybaselist.get(arg2).getQl();
						System.out.print("sjPgdan---" + sjPgdan + "!!!!!!!!!!!!!!!!!!!!!!!qlPgdan===" + qlPgdan);
						netPonitCorpid = yyysrwybaselist.get(arg2).getCorpid();
						// // 选中的机构id等于所到达网点的机构id
						S_application.getApplication().sjpaigongdan = sjPgdan;
						// 获取请领派工单
						System.out.println("qlPgdan:" + qlPgdan + "sjPgdan:" + sjPgdan);
						S_application.getApplication().s_qlpaigongdan = qlPgdan;
						S_application.getApplication().wangdianJigouId = yyysrwybaselist.get(arg2).getCorpid();
						// 设置线路id
						S_application.getApplication().s_yayunXianluId = yyysrwybaselist.get(arg2).getLinenum();
						System.out.println("选中的机构ID-->" + S_application.getApplication().wangdianJigouId);
						managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "正在获取数据...");
						if (null == YaYunLb1 || "null".equals(YaYunLb1)) {
							Log.e(TAG, "数据源是空的不能做！");
						} else {
							HuoquZzxThread ht = new HuoquZzxThread();
							ht.start();
						}

					}
				} else {
					YaYunLb1 = yyysrwybaselist.get(arg2);

					sjPgdan = yyysrwybaselist.get(arg2).getSj();
					qlPgdan = yyysrwybaselist.get(arg2).getQl();
					System.out.print("sjPgdan---" + sjPgdan + "!!!!!!!!!!!!!!!!!!!!!!!qlPgdan===" + qlPgdan);
					netPonitCorpid = yyysrwybaselist.get(arg2).getCorpid();
					// // 选中的机构id等于所到达网点的机构id
					S_application.getApplication().sjpaigongdan = sjPgdan;
					// 获取请领派工单
					System.out.println("qlPgdan:" + qlPgdan + "sjPgdan:" + sjPgdan);
					S_application.getApplication().s_qlpaigongdan = qlPgdan;
					S_application.getApplication().wangdianJigouId = yyysrwybaselist.get(arg2).getCorpid();
					// 设置线路id
					S_application.getApplication().s_yayunXianluId = yyysrwybaselist.get(arg2).getLinenum();
					System.out.println("选中的机构ID-->" + S_application.getApplication().wangdianJigouId);
					managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "正在获取数据...");
					if (null == YaYunLb1 || "null".equals(YaYunLb1)) {
						Log.e(TAG, "数据源是空的不能做！");
					} else {
						HuoquZzxThread ht = new HuoquZzxThread();
						ht.start();
					}
				}
				// 选中的机构id等于所到达网点的机构id
				// S_application.getApplication().wangdianJigouId =
				// YaYunLb.getJigouId();
				// S_application.getApplication().wangdianJigouId = "904050100";
				// System.out.println("选中的机构ID-->" +
				// S_application.getApplication().wangdianJigouId);
				// managerClass.getRuning().runding(YayunSelectRewuUseActivity.this,
				// "正在获取数据...");
				// HuoquZzxThread ht = new HuoquZzxThread();
				// ht.start();

			}
		});
		Log.e(TAG, "跳出点击事件!!!!!!!!!!!!");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ac_yayun_selecrenwuminguserxiblack:
			YayunSelectRewuUseActivity.this.finish();
			break;

		case R.id.yyy_took_update:// 更新代码
			GetHadTask();
			break;
		default:
			break;
		}

	}

	/***
	 * 创建网络 请求确定领取任务 lianc 20191126 弹出提示
	 */
	// private void TrueTookTask() {
	// new Thread() {
	// @Override
	// public void run() {
	// super.run();
	// try {
	// // 用户账号
	// String usernname = "";
	// String qinglingnumber = "";
	// String shangjiaonumber = "";
	// String netresultClean = new AccountAndResistCollateralService()
	// .GetTookTaskTrue(usernname, qinglingnumber);
	// if (!netresultClean.equals("") || netresultClean != null) {
	// Gson gson = new Gson();
	// Log.e(TAG, "测试数据源" + netresultClean.toString());
	//
	// YayunyunSelectTask[] yayunyunSelectTask = gson
	// .fromJson(netresultClean,
	// YayunyunSelectTask[].class);
	// for (int i = 0; i < yayunyunSelectTask.length; i++) {
	// }
	// handler.sendEmptyMessage(9);
	// } else {
	// handler.sendEmptyMessage(3);
	// }
	// } catch (SocketTimeoutException e) {
	// e.printStackTrace();
	// Log.e("", "**===" + e);
	// handler.sendEmptyMessage(51);
	// } catch (NullPointerException e) {
	// e.printStackTrace();
	// Log.e("TAG", "**===" + e);
	// handler.sendEmptyMessage(51);
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e("TAG", "***===" + e);
	// handler.sendEmptyMessage(51);
	// }
	// }
	//
	// }.start();
	//
	// }

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				// getData();
				// adapter.notifyDataSetChanged();
				// dzypitemListView.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuUseActivity.this, "没有任务", new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;

			case 4:
				dialogfa = new Dialog(YayunSelectRewuUseActivity.this);
				LayoutInflater cashtopackgerinflaterfa = LayoutInflater.from(YayunSelectRewuUseActivity.this);
				View vfa = cashtopackgerinflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("领取任务成功");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
					}
				});
				dialogfa.show();
				break;

			default:
				break;
			}
		}

	};

	/****
	 * 获取数据<押运>
	 */
	private void getData() {
		if (list.size() > 0) {
			list.clear();
		}

		for (int i = 0; i < tables[1].get("Jigouming").getValues().size(); i++) {
			YaYunLb yy = new YaYunLb();
			yy.setJigouId(tables[1].get("JigouId").getValues().get(i));
			yy.setName(tables[1].get("Jigouming").getValues().get(i));
			String str = tables[1].get("renwuType").getValues().get(i);
			String[] st = str.split("_");
			yy.setJigouId(tables[1].get("JigouId").getValues().get(i));
			yy.setQlrwState(st[0]);
			yy.setSjrwState(st[1]);

			list.add(yy);
			Log.e(TAG, "listlistlistlistlist+list.size()" + list.size());
		}

		// liebiaoadapter.notifyDataSetChanged();
		jigoushuliangView.setText("任务的数量：" + list.size() + "");
	}

	private void getJigouLeibie() {
		managerClass.getRuning().runding(YayunSelectRewuUseActivity.this, "获取机构类别中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					/**
					 * 取消押运员机构限制后，根据押运员的userId查询出其所领取的任务的机构ID， 以此机构id作为查询机构类别的参数。
					 * 
					 * @author zhouKai
					 * @date 2016-7-11 下午1:21:03
					 */
					// jigouleibie =
					// is.getJigouLeibie(o_Application.kuguan_db.getOrganizationId());
					// Log.e(TAG,"======getJigouLeibie==="+GApplication.use.getUserzhanghu());
//					S_application.getApplication().s_userYayun
//					jigouleibie = is.getJigouLeibie( GApplication.use.getUserzhanghu());
					jigouleibie = is.getJigouLeibie(S_application.getApplication().s_userYayun);
					Log.d(TAG, "获取机构的类别：：：：：：" + jigouleibie);
					if (null != jigouleibie || !"".equals(jigouleibie)) {
						handler.sendEmptyMessage(5);
					} else {
						handler.sendEmptyMessage(13);
					}
				} catch (SocketTimeoutException e) {
					handler.sendEmptyMessage(15);
					e.printStackTrace();
				} catch (NullPointerException e) {
					handler.sendEmptyMessage(13);
					e.printStackTrace();
				} catch (Exception e) {
					handler.sendEmptyMessage(14);
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 获取数据
	 * 
	 * @author Administrator
	 */
	class YayunThread extends Thread {
		Message m;

		public YayunThread() {
			super();
			m = handler.obtainMessage();
		}

		@Override
		public void run() {
			super.run();
			try {
				// 修改： 11004041 之前登录时存账户现在没登录需要手动存个假数据
				System.out.println("BBB-0000ID :" + S_application.getApplication().s_yayunJigouId);
				// System.out.println("BBB-0000 :" +
				// GApplication.use.getUserzhanghu());
				String str = is.getYayunyuanRenwu(GApplication.use.getUserzhanghu(),
						S_application.getApplication().s_yayunJigouId);
				Log.d(TAG, "获取数据str：：：：：：：：" + str);
				if (!"".equals(str) && null != str) {
					m.obj = str;
					// m.what = 1; 修改
					System.out.println("不走吗?");
				} else {
					System.out.println("走吗1");
					m.what = 0;
				}
			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				e.printStackTrace();
				m.what = -4;
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("走吗12");
				m.what = 0;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				m.what = -1;
			} finally {
				handler.sendMessage(m);
				GolbalUtil.onclicks = true;
			}
		}
	}

	/**
	 * 押运员周转箱获取
	 * 
	 * @author Administrator
	 */
	class HuoquZzxThread extends Thread {
		Message m;

		public HuoquZzxThread() {
			super();
			m = handler.obtainMessage();
		}

		@Override
		public void run() {
			super.run();

			try {
				System.out.println("机构id=" + S_application.getApplication().wangdianJigouId);
				System.out.println("sj=" + sjPgdan);
				System.out.println("ql=" + qlPgdan);// 小淀904050100

				System.out.print("!!!!" + YaYunLb1.getSj() + "!!!!!!!!!!!!!" + YaYunLb1.getQl());
				// String str =
				// is.getYayunyuanZhouzhuangxiang(S_application.getApplication().wangdianJigouId,
				// sjPgdan, qlPgdan);
				String str = is.getYayunyuanZhouzhuangxiang(netPonitCorpid, sjPgdan, qlPgdan);
				if (!"".equals(str) && null != str) {
					Table[] tables2 = Table.doParse(str);
					sjZzxlist = TableQuzhi.getList(tables2[0], "shangjiaoZhouzhuangxiang");
					qlZzxlist = TableQuzhi.getList(tables2[1], "qinglingZhouzhuangxiang");
					Log.e(TAG, "sjZzxlist==" + sjZzxlist.size() + "====---qlZzxlist" + qlZzxlist.size());
					// 新增 上缴和请领的周转箱不能同时为null
					m.what = 3;
				}

			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				e.printStackTrace();
				m.what = -4;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				m.what = -1;
			} finally {
				handler.sendMessage(m);
				GolbalUtil.onclicks = true;
			}
		}
	}

	@Override
	protected void onDestroy() {
//		if (S_application.getApplication().s_userYayun != null) {
//			S_application.getApplication().s_userYayun = null;
//			GApplication.user.setYonghuZhanghao("");
//		}

		super.onDestroy();
	}

	/***
	 * 适配器放数据
	 * 
	 * @author Administrator
	 * 
	 */
	class LiebiaoAdapter extends BaseAdapter {

		protected static final String TAG = "LiebiaoAdapter";

		@Override
		public int getCount() {
			Log.e(TAG, "--yyysrwybaselist------" + yyysrwybaselist.size());
			return yyysrwybaselist.size();

		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unused")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderAdapterWailk mVHAWailk = null;
			if (mVHAWailk == null) {
				mVHAWailk = new ViewHolderAdapterWailk();
				convertView = LayoutInflater.from(YayunSelectRewuUseActivity.this)
						.inflate(R.layout.yayunyuantaskmingxisekectklist_item, null);
				TextView yayunorgname = (TextView) convertView.findViewById(R.id.yayunorgname);
				TextView yayuntooktast = (TextView) convertView.findViewById(R.id.yayuntooktast);// 请领显示
				TextView yayunupdatatast = (TextView) convertView.findViewById(R.id.yayunupdatatast);// / 上缴显示
				TextView yayunyuantasktext = (TextView) convertView.findViewById(R.id.yayunyuantasktext);// 显示线路
				LinearLayout lintitle = (LinearLayout) convertView.findViewById(R.id.lintitle);
				mVHAWailk.yayunorgname = yayunorgname;
				mVHAWailk.yayuntooktast = yayuntooktast;
				mVHAWailk.yayunupdatatast = yayunupdatatast;
				mVHAWailk.yayunyuantasktext = yayunyuantasktext;
				mVHAWailk.lintitle = lintitle;
				convertView.setTag(convertView);
			} else {
				mVHAWailk = (ViewHolderAdapterWailk) convertView.getTag();
				convertView.setTag(convertView);
			}

			YayunyuanSelectRenWuUserBaseEntity entity = yyysrwybaselist.get(position);

			if (position == 0) {
				mVHAWailk.yayunyuantasktext.setVisibility(View.VISIBLE);
				mVHAWailk.lintitle.setVisibility(View.VISIBLE);
			} else {
				YayunyuanSelectRenWuUserBaseEntity lastEntity = yyysrwybaselist.get(position - 1);
				if (entity.getLinename().equals(lastEntity.getLinename())) {
					mVHAWailk.yayunyuantasktext.setVisibility(View.GONE);
					mVHAWailk.lintitle.setVisibility(View.GONE);
				} else {
					mVHAWailk.yayunyuantasktext.setVisibility(View.VISIBLE);
					mVHAWailk.lintitle.setVisibility(View.VISIBLE);
				}
			}
			mVHAWailk.yayunyuantasktext.setText(entity.getLinename());// 显示线路
			mVHAWailk.yayunorgname.setText(entity.getCorpname());// 显示银行下的网点
			Log.e(TAG, "sjposition!!!!!!" + entity);
			Log.e(TAG, "sj!!!!!!" + entity.getSjstate());
			if ("00".equals(entity.getSjstate())) {// 上缴：00待确认 01待交接 02/04已交接
				mVHAWailk.yayunupdatatast.setText("待确认");
				mVHAWailk.yayunupdatatast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else if ("01".equals(entity.getSjstate())) {
				mVHAWailk.yayunupdatatast.setText("待交接");
				mVHAWailk.yayunupdatatast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else if ("02".equals(entity.getSjstate())) {
				mVHAWailk.yayunupdatatast.setText("已交接");
				mVHAWailk.yayunupdatatast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else if ("04".equals(entity.getSjstate())) {
				mVHAWailk.yayunupdatatast.setText("已交接");
				mVHAWailk.yayunupdatatast.setBackgroundResource(R.drawable.buttom_selector_bg);
			}

			if ("05".equals(entity.getQlstate())) {
				mVHAWailk.yayuntooktast.setText("待交接");
				mVHAWailk.yayuntooktast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else if ("04".equals(entity.getQlstate())) {
				mVHAWailk.yayuntooktast.setText("已交接");
				mVHAWailk.yayuntooktast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else if ("00".equals(entity.getQlstate())) {
				mVHAWailk.yayuntooktast.setText("已交接");
				mVHAWailk.yayuntooktast.setBackgroundResource(R.drawable.buttom_selector_bg);
			} else {
			}
			return convertView;
		}

	}

	static class ViewHolderAdapterWailk {
		public TextView yayunorgname;
		public TextView yayuntooktast;
		public TextView yayunupdatatast;
		public TextView yayunyuantasktext;// xians显示线路
		public LinearLayout lintitle;// 标题头
		public TextView yayunyuantaskyuliu;// 无用站位
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Skip.skip(YayunSelectRewuUseActivity.this, ZhouzhuanxiangMenu.class, null, 0);
			YayunSelectRewuUseActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
