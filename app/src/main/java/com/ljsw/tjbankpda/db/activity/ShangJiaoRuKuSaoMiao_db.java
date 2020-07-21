package com.ljsw.tjbankpda.db.activity;

import hdjc.rfid.operator.RFID_Device;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.example.pda.TailzerotoPackgersActivity;
import com.ljsw.tjbankpad.baggingin.activity.CustomDialog;
import com.ljsw.tjbankpad.baggingin.activity.DiZhiYaPinActivity;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.ljsw.tjbankpda.db.biz.ShangJiaoRuKuSaoMiao;
import com.ljsw.tjbankpda.db.service.ZhouZhuanXiangJiaoJie;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;
import com.service.FixationValue;

/***
 * 扫描验证
 * 
 * @author Administrator 上缴入库调用接口
 *
 */
@SuppressLint("HandlerLeak")
public class ShangJiaoRuKuSaoMiao_db extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "ShangJiaoRuKuSaoMiao_db";// 上缴入库扫描标识
	private ImageView back;
	private TextView topleft, topright, wrong;
	private Button chuku, quxiao;
	private ListView listleft, listright;
	List<String> copylist = new ArrayList<String>();
	private String jiaojieOk;
	private String jiaojieOK1;// 第二次交接
	private LeftAdapter ladapter;
	private RightAdapter radapter;
	private String cashBoxNum;// 提交款箱
	private String userId;// 提交指纹验证人的账户
	private ShangJiaoRuKuSaoMiao getnumber;
	private ManagerClass manager;
	ICleaningManService is = new ICleaningManService();
	OnClickListener onclickreplace, onclickreplace1, onclickreplace2, onclickreplace3;
	private String jigouleibie;
	private RFID_Device rfid;

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	private Dialog successDialog;// 成功弹出框
	private DialogManager dmanager;// 弹出框管理类
	private CustomDialog outDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shangjiaorukusaomiao);
		load();
		cashBoxNum = "";
		userId = "";
		getnumber = new ShangJiaoRuKuSaoMiao();
		getnumber.setHandler(handler);
		copylist.addAll(o_Application.rukumingxi.getKuxiang());
		ladapter = new LeftAdapter();
		radapter = new RightAdapter();
		manager = new ManagerClass();

		onclickreplace = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				manager.getAbnormal().remove();
				Xianlu();
			}
		};
		onclickreplace1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				manager.getAbnormal().remove();
				getJigouLeibie();
			}
		};
		onclickreplace3 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				manager.getAbnormal().remove();
				JiaoJie();
			}
		};
//		onclickreplace2=new View.OnClickListener() {	
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				manager.getAbnormal().remove();
//				
//				ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
//				if("0".equals(jigouleibie)){
//					if (o_Application.rukumingxi.getXianluType() != null
//							&& o_Application.rukumingxi.getXianluType()
//									.equals("1")) {
//						String type="";
//						if (GApplication.user.getLoginUserId().equals(
//								FixationValue.waibaoQingfenString)){
//							type="2L";
//						}else{
//							type="2J";
//						}
//						
//						// 直属上缴(网点缴总库)
//						try {
//							jiaojieOk = jiaojie
//									.SaveAuthLogShangjiaoBYMasterlibrary(cashBoxNum, userId,
//											type, o_Application.rukumingxi
//													.getXianluid(),
//											o_Application.kuguan_db
//													.getOrganizationId(),strflag);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//						if(jiaojieOk.equals("00")){
//							Log.e(TAG,"------提交成功");
//						}else if(jiaojieOk.equals("01")){
//							Skip.skip(ShangJiaoRuKuSaoMiao_db.this, RenWuLieBiao_db.class,
//									null, 0);
//							
//						}else if(jiaojieOk.equals("99")){
//							Log.e(TAG,"------提交失败");
//						}
//							
//						
//					} 
//			}
//				
//				
//				
//			}
//		};

	}

	@Override
	protected void onResume() {
		super.onResume();
//		Xianlu();
		getJigouLeibie();
		o_Application.guolv.clear();
		o_Application.numberlist.clear();
		o_Application.rukumingxi.getKuxiang().clear();
		o_Application.rukumingxi.getKuxiang().addAll(copylist);
		getRfid().addNotifly(getnumber);
		new Thread() {
			@Override
			public void run() {
				super.run();
				getRfid().open_a20();
			}

		}.start();
		topright.setText("" + o_Application.numberlist.size());
		topleft.setText("" + o_Application.rukumingxi.getKuxiang().size());
		listleft.setDividerHeight(0);
		listright.setDividerHeight(0);
		listleft.setAdapter(ladapter);
		listright.setAdapter(radapter);
		ladapter.notifyDataSetChanged();
		radapter.notifyDataSetChanged();
		// new TurnListviewHeight(listleft);
		// new TurnListviewHeight(listright);

		if (o_Application.rukumingxi.getKuxiang().size() > 0) {
			chuku.setEnabled(false);
			chuku.setBackgroundResource(R.drawable.button_gray);
		}
		if (o_Application.numberlist.size() == 0) {
			quxiao.setEnabled(false);
			quxiao.setBackgroundResource(R.drawable.button_gray);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			manager.getAbnormal().remove();
			switch (msg.what) {
			case 0:

				if (o_Application.rukumingxi.getKuxiang().size() == 0) {
					chuku.setEnabled(true);
					chuku.setBackgroundResource(R.drawable.buttom_selector_bg);
				}

				if (o_Application.numberlist.size() > 0) {
					quxiao.setEnabled(true);
					quxiao.setBackgroundResource(R.drawable.buttom_selector_bg);
				}
//				wrong.setText(o_Application.wrong);
				topright.setText("" + o_Application.numberlist.size());
				topleft.setText("" + o_Application.rukumingxi.getKuxiang().size());
				ladapter.notifyDataSetChanged();
				radapter.notifyDataSetChanged();
				listleft.setAdapter(ladapter);
				// new TurnListviewHeight(listleft);
				listright.setAdapter(radapter);
				// new TurnListviewHeight(listright);
				break;
			case 1:
				manager.getRuning().remove();
				Skip.skip(ShangJiaoRuKuSaoMiao_db.this, RenWuLieBiao_db.class, null, 0);
				break;
			case 2:
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "提交超时,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getRuning().remove();
						manager.getAbnormal().remove();
						JiaoJie();
					}
				});
				break;
			case 3:
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "提交失败,重试?", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getRuning().remove();
						manager.getAbnormal().remove();
						JiaoJie();
					}
				});
				break;
			case 4:
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "网络连接失败,重试?", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getRuning().remove();
						manager.getAbnormal().remove();
						JiaoJie();
					}
				});
				break;
			case 5:
				manager.getRuning().remove();
				break;
			case 6:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "连接超时，重新链接？", onclickreplace);
				break;
			case 7:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "获取失败", onclickreplace);
				break;
			case 8:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "信息加载异常", onclickreplace);
				break;
			case 15:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "连接超时，重新链接？", onclickreplace1);
				break;
			case 13:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "获取失败", onclickreplace1);
				break;
			case 14:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "信息加载异常", onclickreplace1);
				break;

			case 16:/// 提交两边信息
				manager.getRuning().remove();
				ShangjiaorukuUpData();

				break;

			case 17:

				manager.getRuning().remove();
//				
				break;

			case 18:
				manager.getRuning().remove();
				Toast.makeText(ShangJiaoRuKuSaoMiao_db.this, "提交失败" + jiaojieOk, 400).show();
//					
				break;
			case 19:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoRuKuSaoMiao_db.this, "提交失败：" + jiaojieOk, onclickreplace3);
				break;
			default:
				break;
			}

		}

	};

	private void ShangjiaorukuUpData() {
		ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();

		successDialog = new Dialog(ShangJiaoRuKuSaoMiao_db.this);
		LayoutInflater inflaterfa = LayoutInflater.from(ShangJiaoRuKuSaoMiao_db.this);
		View vfa = inflaterfa.inflate(R.layout.dialog_successwithtext, null);
		TextView dialog_tv001 = (TextView) vfa.findViewById(R.id.dialog_tv001);
		TextView dialog_faile = (TextView) vfa.findViewById(R.id.failue);
		Button butfa = (Button) vfa.findViewById(R.id.success);
		dialog_tv001.setText("线路下存在有业务但未确认装箱的网点，是否继续上缴入库？");
		butfa.setText("确定");
		dialog_faile.setText("取消");
		successDialog.setCancelable(false);
		successDialog.setContentView(vfa);
		butfa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				successDialog.dismiss();
				strflag = "y";

				if (strflag.equals("y")) {
					new Thread() {
						public void run() {
							super.run();
							ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
//        				if("0".equals(jigouleibie)){
//        					if (o_Application.rukumingxi.getXianluType() != null
//        							&& o_Application.rukumingxi.getXianluType()
//        									.equals("1")) {
//        						String type="";
//        						if (GApplication.user.getLoginUserId().equals(
//        								FixationValue.waibaoQingfenString)){
//        							type="2L";
//        						}else{
//        							type="2J";
//        						}

							// 直属上缴(网点缴总库)
							try {

//        							Log.i("cashBoxNum",cashBoxNum);
//        							Log.i("xianluId",xianluId);
//        							Log.i("roleId",userzhanghu);
//        							Log.i("jigouId",jigouId);
//        							Log.i("strflage",strflage)
								String zhanghu = GApplication.user.getYonghuZhanghao();
								jiaojieOK1 = jiaojie.SaveAuthLogShangjiaoBYMasterlibrary(cashBoxNum,
										o_Application.rukumingxi.getXianluid(), zhanghu,
										o_Application.kuguan_db.getOrganizationId(), strflag);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.e(TAG, "报错：：：：：：：" + e);
							}
							Log.e(TAG, "jiaojieOK1" + jiaojieOK1);
							if (jiaojieOK1.equals("上缴入库成功")) {
								Log.e(TAG, "------提交成功");
								handler.sendEmptyMessage(1);

							} else if (jiaojieOK1.equals("押运员网点交接未完成")) {
//        							Skip.skip(ShangJiaoRuKuSaoMiao_db.this, RenWuLieBiao_db.class,
//        									null, 0);
								handler.sendEmptyMessage(5);

							} else if (jiaojieOK1.equals("99")) {
								Log.e(TAG, "------提交失败");
							}

//        					} 

//        			}

						};
					}.start();
				} else {
					strflag = "y";
				}

			}
		});
		successDialog.setContentView(vfa);
		dialog_faile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strflag = "n";
				successDialog.dismiss();
				Log.e(TAG, "什么也没做");
			}
		});
		successDialog.show();
	}

	String params;// 返回机构的类别

	/**
	 * 通过机构ID获取线路类型
	 */
	public void Xianlu() {
		manager.getRuning().runding(ShangJiaoRuKuSaoMiao_db.this, "获取线路中.....");
		new Thread() {
			public void run() {
				super.run();
				try {
					params = is.getJigouLeibie(o_Application.kuguan_db.getOrganizationId());
					if (params != null || !params.equals("")) {
						// S_application.getApplication().jiaojieType=1;
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

//private String  strflag="n";
//	public void JiaoJie() {
//		getcashBoxNumAnduserId();
//		manager.getRuning().runding(this, "提交中...");
//		new Thread() {
//			@Override
//			public void run() {
//				super.run();
//				ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
//				try {
//					// 上缴总库 2L  或者2J 是调用存储过程中调用那个调用那个节点的存储过程
//					if("0".equals(jigouleibie)){
//						if (o_Application.rukumingxi.getXianluType() != null
//								&& o_Application.rukumingxi.getXianluType()
//										.equals("1")) {
//							String type="";
//							if (GApplication.user.getLoginUserId().equals(
//									FixationValue.waibaoQingfenString)){
//								type="2L";
//							}else{
//								type="2J";
//							}
//							
//							// 直属上缴(网点缴总库)
//							jiaojieOk = jiaojie
//									.SaveAuthLogShangjiaoBYMasterlibrary(cashBoxNum, userId,
//											type, o_Application.rukumingxi
//													.getXianluid(),
//											o_Application.kuguan_db
//													.getOrganizationId(),strflag);
//							Log.e(TAG,"jiaojieOk==="+jiaojieOk);
//							String  sss=jiaojieOk;
//							System.out.print("!!!!!!!!"+sss);
//							if(jiaojieOk.equals("交接完成")){
//								handler.sendEmptyMessage(1);
//								Log.e(TAG,"提交成功");
//							}else if(jiaojieOk.equals("")||jiaojieOk.equals("anyType{}")){
//								Log.e(TAG,"提交2次");
//								handler.sendEmptyMessage(16);
//							}else if(jiaojieOk.equals("押运员网点交接未完成")){
//								handler.sendEmptyMessage(18);
//							}
//								
//							
//						} else {
//							// 分行缴总库
//							String type="";
//							
//							if (GApplication.user.getLoginUserId().equals(
//									FixationValue.waibaoQingfenString)){
//								type="2K";
//							}else{
//								type="2A";
//							}
//							
//							jiaojieOk = jiaojie
//									.SaveAuthLogShangjiao(cashBoxNum, userId,
//											type, o_Application.rukumingxi
//													.getXianluid(),
//											o_Application.kuguan_db
//													.getOrganizationId());
//						}
//
//					} else {
//						jiaojieOk = jiaojie.SaveAuthLogShangjiao(cashBoxNum,
//								userId, "28",
//								o_Application.rukumingxi.getXianluid(),
//								o_Application.kuguan_db.getOrganizationId());
//					}
//					
//					if (jiaojieOk != null) {
////						manager.getRuning().remove();
//						try {
////							manager.getRuning().runding(
////									ShangJiaoRuKuSaoMiao_db.this, jiaojieOk);
//							Thread.sleep(2500);
//							
////							handler.sendEmptyMessage(1);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//
//					} else {
//						handler.sendEmptyMessage(3);
//					}
//				} catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(2);
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(3);
//				} catch (Exception e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(4);
//				}
//			}
//
//		}.start();
//	}

	/***
	 * 交接修改2.0 http://localhost:8080/cashman/webservice/cash_sk/sjCofferIn?
	 * arg0=ZH000002ZZ& arg1=BC02& arg2=0001& arg3=988050000& arg4=n
	 * 
	 */
	private String strflag = "n";

	public void JiaoJie() {
		getcashBoxNumAnduserId();
		manager.getRuning().runding(this, "提交中...");
		new Thread() {
			@Override
			public void run() {
				super.run();
				ZhouZhuanXiangJiaoJie jiaojie = new ZhouZhuanXiangJiaoJie();
				try {
					// 上缴总库 2L 或者2J 是调用存储过程中调用那个调用那个节点的存储过程
					if ("0".equals(jigouleibie)) {

						// 直属上缴(网点缴总库)
						String zhanghu = GApplication.user.getYonghuZhanghao();
						jiaojieOk = jiaojie.SaveAuthLogShangjiaoBYMasterlibrary(cashBoxNum,
								o_Application.rukumingxi.getXianluid(), zhanghu,
								o_Application.kuguan_db.getOrganizationId(), strflag);
						Log.e(TAG, "jiaojieOk===" + jiaojieOk);
						String sss = jiaojieOk;
						System.out.print("!!!!!!!!" + sss);
						if (null != jiaojieOk || !("").equals(jiaojieOk)) {
							if (jiaojieOk.equals("上缴入库成功")) {
								handler.sendEmptyMessage(1);
								Log.e(TAG, "提交成功");
							} else if (jiaojieOk.equals("线路下存在有业务但未确认装箱的网点，是否继续执行上缴入库？")
									|| jiaojieOk.equals("anyType{}")) {
								Log.e(TAG, "提交2次");
								handler.sendEmptyMessage(16);
							} else if (jiaojieOk.equals("押运员网点交接未完成")) {
								handler.sendEmptyMessage(18);
							}
						} else {
							Log.d(TAG, "提交数据不正确");
						}

						if (jiaojieOk != null) {
							manager.getRuning().remove();
							try {
								Thread.sleep(2500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						} else {
							handler.sendEmptyMessage(3);
						}
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				} catch (NullPointerException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}

		}.start();
	}

	/**
	 * 拼接周转箱和交接人账户信息
	 */
	public void getcashBoxNumAnduserId() {
		cashBoxNum = "";
		userId = "";
		int a = 0;
		int b = 0;
		for (int i = 0; i < o_Application.numberlist.size(); i++) {
			if (a == o_Application.numberlist.size() - 1) {
				cashBoxNum += o_Application.numberlist.get(i);
			} else {
				cashBoxNum += o_Application.numberlist.get(i) + ",";
			}
			a++;
		}
		for (int i = 0; i < o_Application.FingerLoginNum.size(); i++) {
			if (b == o_Application.FingerLoginNum.size() - 1) {
				userId += o_Application.FingerLoginNum.get(i);
			} else {
				userId += o_Application.FingerLoginNum.get(i) + "_";
			}
			b++;
		}
		System.out.println("------------上缴入库完成 cashBoxNum：" + cashBoxNum);
		System.out.println("------------上缴入库完成 userId：" + userId);
	}

	public void load() {
		back = (ImageView) findViewById(R.id.sjruku_saomiao_back);
		back.setOnClickListener(this);
		topleft = (TextView) findViewById(R.id.sjruku_saomiao_left_text);
		topright = (TextView) findViewById(R.id.sjruku_saomiao_right_text);
		chuku = (Button) findViewById(R.id.sjruku_saomiao_chuku);
		chuku.setOnClickListener(this);
		quxiao = (Button) findViewById(R.id.sjruku_saomiao_quxiao);
		quxiao.setOnClickListener(this);
		listleft = (ListView) findViewById(R.id.sjruku_saomiao_list_left);
		listright = (ListView) findViewById(R.id.sjruku_saomiao_list_right);
		wrong = (TextView) findViewById(R.id.shangjiaoruku_tishi_cuowu);
	}

	/**
	 * 获取机构类别
	 */
	public void getJigouLeibie() {
		manager.getRuning().runding(ShangJiaoRuKuSaoMiao_db.this, "获取机构类别中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					jigouleibie = is.getJigouLeibie(o_Application.kuguan_db.getOrganizationId());
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sjruku_saomiao_back:
			ShangJiaoRuKuSaoMiao_db.this.finish();
			break;
		case R.id.sjruku_saomiao_chuku:
			// 出库跳转
			new Thread() {
				@Override
				public void run() {
					super.run();
					getRfid().close_a20();
				}

			}.start();
			JiaoJie();
			break;

		case R.id.sjruku_saomiao_quxiao:
			// 清除集合 重新扫描
			o_Application.rukumingxi.getKuxiang().clear();
			o_Application.numberlist.clear();
			o_Application.guolv.clear();
			o_Application.rukumingxi.getKuxiang().addAll(copylist);
			o_Application.wrong = "";
			if (o_Application.rukumingxi.getKuxiang().size() > 0) {
				chuku.setEnabled(false);
				chuku.setBackgroundResource(R.drawable.button_gray);
			}
			if (o_Application.numberlist.size() == 0) {
				quxiao.setEnabled(false);
				quxiao.setBackgroundResource(R.drawable.button_gray);
			}
			handler.sendEmptyMessage(0);
			break;
		default:
			break;
		}

	}

	class LeftAdapter extends BaseAdapter {
		LeftHolder lh;
		LayoutInflater lf = LayoutInflater.from(ShangJiaoRuKuSaoMiao_db.this);

		@Override
		public int getCount() {
			return o_Application.rukumingxi.getKuxiang().size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.rukumingxi.getKuxiang().get(arg0);
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
			lh.tv.setText(o_Application.rukumingxi.getKuxiang().get(arg0));
			return arg1;
		}

	}

	public static class LeftHolder {
		TextView tv;
	}

	class RightAdapter extends BaseAdapter {
		RightHolder rh;
		LayoutInflater lf = LayoutInflater.from(ShangJiaoRuKuSaoMiao_db.this);

		@Override
		public int getCount() {
			return o_Application.numberlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return o_Application.numberlist.get(arg0);
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
			rh.tv.setText(o_Application.numberlist.get(arg0));
			return arg1;
		}

	}

	public static class RightHolder {
		TextView tv;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.getRuning().remove();
		if (o_Application.rukumingxi.getKuxiang().size() > 0) {
			o_Application.rukumingxi.getKuxiang().clear();
		}
		if (o_Application.numberlist.size() > 0) {
			o_Application.numberlist.clear();
		}
		if (o_Application.guolv.size() > 0) {
			o_Application.guolv.clear();
		}
		if (o_Application.rukumingxi.getKuxiang().size() == 0) {
			o_Application.rukumingxi.getKuxiang().addAll(copylist);
		} else {
			o_Application.rukumingxi.getKuxiang().clear();
			o_Application.rukumingxi.getKuxiang().addAll(copylist);
		}
		copylist.clear();
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.getRuning().remove();
		getRfid().close_a20();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ShangJiaoRuKuSaoMiao_db.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
