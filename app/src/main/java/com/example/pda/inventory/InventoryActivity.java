package com.example.pda.inventory;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import hdjc.rfid.operator.RFID_Device;

import com.application.GApplication;
import com.example.pda.R;
import com.example.pda.TailZerotoEntity;
import com.example.pda.homemagnetopackge.HomeMangerByTailzerotoPackgersActivity;
import com.golbal.pda.GolbalUtil;
import com.google.gson.Gson;
import com.ljsw.tjbankpad.baggingin.activity.cash.entity.BaggingForCashEntity;
import com.ljsw.tjbankpad.baggingin.activity.cash.service.BaggingForCashService;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.entity.CashtoPackgersEntity;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.service.CashToPackgersService;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity.TailPackgerEntityQuanbieXinxi;
import com.ljsw.tjbankpad.baggingin.activity.cashtopackges.tail.entity.TailZerotoPackgerTianJiaXianJin;
import com.ljsw.tjbankpda.util.HxbSpinner;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.NumFormat;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.ljsw.tjbankpda.yy.application.S_application;
import com.ljsw.tjbankpda.yy.application.WaterCardScnnerUtils;
import com.ljsw.tjbankpda.yy.application.ZzxSaomiaoUtil;
import com.ljsw.tjbankpda.yy.service.ICleaningManService;
import com.manager.classs.pad.ManagerClass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 20200323 ????????????????????????
 * 
 * @author Administrator lianc
 * 
 *         ????????? ?????????????????????
 * 
 * 
 */
public class InventoryActivity extends Activity implements OnClickListener {
	protected static final String TAG = "InventoryActivity";
	// ??????
	private TextView etpackgercount, tv_zhenchaomoney;// ???????????? ????????????
	private Button btn_scanner, inventory_btn_add;// ??????, ??????
	private ImageView ql_ruku_back;
	private LinearLayout lin_zhenghchao, inven_zhengchao_quanbie_spinner_layout;// ?????? ,??????
	private TextView inven_zhengchao_quanbiespinner_text;// ???????????????
	private HxbSpinner couopnSpinner;
	private HxbSpinner editionSpinner;
	private MySpinner spinner, spinner1;
	private Button btn_zhengchaoupodata, btn_zhengchaoclean;// ????????????????????????
	private String netresultClean;// ???????????????????????????
	private List<CashtoPackgersEntity> cashtopacgerslist = new ArrayList<CashtoPackgersEntity>();// ?????????
	private List<String> cashtopackgertype = new ArrayList<String>();// ????????????
	private List<String> cashtopackgersid = new ArrayList<String>();// ??????id
	private List<String> cashtopackgercount = new ArrayList<String>();// ??????id
	private List<String> cahstopackgermeid = new ArrayList<String>();// ?????????????????????
	private String[] str_juanzhong = { "??????", "??????" };// ??????
	private String[] str_paper = { "??????", "??????" };// ??????????????????
	private String[] array2 = cashtopackgersid.toArray(new String[0]);// /????????????id
	private String[] array3 = cashtopackgertype.toArray(new String[0]);// /???????????????????????????

	private Map<String, List<Map<String, String>>> moneyEditionMap = new HashMap<String, List<Map<String, String>>>();
	// ????????????????????????
	private Map<String, List<Map<String, String>>> moneyEditionMapstr = new HashMap<String, List<Map<String, String>>>();
	private Map<String, List<String>> moneyEditionMapliststr = new HashMap<String, List<String>>();
	private SpinnerAdapter mSpinnerAdapter, mSpinnerAdapterquanzhong, mspinnbanbie, mSpinnerPaper;// ?????????
	private Spinner spinner_quanbie, spinnerversion, spinner_falseall, spinner_paper; // ??????,??????,??????

	private String moneyType, moneyid;// ???????????????
	private String cansunwanczheng;
	private String paperfalse;// ???????????????

	private String TailZeromeid = "";// ?????????????????????meoid ??????meid ???????????????
	private String ByTailZeromeidtoparval = "";// ???????????????

	private String ByTailZeromeidtolostvalue = "";// ???????????????
	private String showtvcash_banbie;// ?????????????????????????????????
	private String shwotvreadcash_qunabeibu;
	private String isupdatameid = "";// / ????????????????????????meid????????????
	private String cansunid;//

	private EditText etpackgerzhengchaocount;// ??????????????????

	private TextView tvzhengchaoquanbei, tv_zhengchao_banbie, tv_zhenchaowanzhegncansuny;// ??????,?????????????????????
	private TextView tv_zhenchaopaperfalse, tv_zhenchaopackgercount, tv_zhenchaoallcounts;// ????????????,?????????
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ??????
	private LinearLayout lin_weiling;// ??????
	private LinearLayout watercar_tailzeroto_spinner_layout, watercar_tailzerotbanbie_spinner_layout,
			water_taizero_spinner_layout_zhuangtai
//			water_taizero_spinner_layout_paper
	;// ??????,??????, ??????
	private TextView watercar_tailzeroto_spinner_textquanbie, watercar_tailzeroto_banbie_spinner_text_banbie,
			water_taizero_spinner_text_zhuangtai_tv, water_taizero_spinner_text_paper_tv;// ???????????????
	private EditText inventory_et_tailzeroet;// ????????????
	private Button watercar_tailzero_xianjin_tianjia;// ????????????

	private Button btn_inventory_weiling_clean;// ??????????????????
	private Button btn_inventory_weilingupdata;// ????????????????????????
	private TextView tv_weilingquanbei;// ??????????????????
	private String param;// ?????? ????????????
	private List<TailZerotoEntity> TailZerotoEntitylist = new ArrayList<TailZerotoEntity>();// ??????????????????
	// ?????????????????????
	private int isok = -1; // ?????????????????????
	private String[] str_zhuangtai;
	private String[] str_paperfalse;
	private String zhuangtai;// / ???????????????
	private String str_select_paperfalse;// ?????????????????????????????????
	private double heji_xianjin = 0;
	private String delete = "0";// ???????????????

	private String updataresult;// ?????????????????????
	private List<TailPackgerEntityQuanbieXinxi> quanbieListxin = new ArrayList<TailPackgerEntityQuanbieXinxi>(); // ??????????????????????????????
	private String juanbie;// ????????????
	private List<TailZerotoPackgerTianJiaXianJin> xianjinlisttinajia = new ArrayList<TailZerotoPackgerTianJiaXianJin>();
	private ListView watercar_tailzerotopackgeslistview;
	private XianJinAdapter adapter;
	private String setstate = ""; // ????????????stat??????
	private TextView tailzero_heji;
	private Dialog dialogfa;// ??????
	private Dialog dialogforreturnaccountinten;
	// ??????
	private static final String defaultOptions = "?????????";
	public static HomeMangerByTailzerotoPackgersActivity instance = null;
	SaomiaoThread stm;
	private Handler handl;
	private WaterCardScnnerUtils wcsm;
	private TextView tv_showreaderresult;// ?????????
	Table[] tables;

	private ManagerClass manager;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (null != S_application.getApplication().bianhao) {
					// managerClass.getRuning().runding(ZhouzXixinSmiaoActivity.this,"???????????????...");
					stm = new SaomiaoThread();
					stm.start();
				}
				break;
			case 4:
				// / ??????????????????monetype
				spinnerselect();
				// ????????????
				break;
			case 998:// ??????????????????dialog
				dialogfa = new Dialog(InventoryActivity.this);
				LayoutInflater inflaterfa = LayoutInflater.from(InventoryActivity.this);
				View vfa = inflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("????????????+???????????????????????????");
				dialogfa.setCancelable(false);
				dialogfa.setContentView(vfa);
				butfa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogfa.dismiss();
					}

				});
				dialogfa.show();
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		wcsm = new WaterCardScnnerUtils();
		wcsm.setHandler(handler);
		InitView();

		LoadData();
		manager = new ManagerClass();
		adapter = new XianJinAdapter();
		handl = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// managerClass.getRuning().remove();
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:// ??????????????????
					Log.e(TAG,
							"S_application.getApplication().bianhao=====---" + S_application.getApplication().bianhao);
					tv_showreaderresult.setText(S_application.getApplication().bianhao);

					if (null != S_application.getApplication().bianhao
							|| S_application.getApplication().bianhao.equals("")) {
						String str_bianhao = S_application.getApplication().bianhao.substring(0, 3);
						Log.e(TAG, "str_bianhao---" + str_bianhao);
						if (str_bianhao.equals("SP1")) {
							Log.e(TAG, "bianhao=====---" + S_application.getApplication().bianhao);
							lin_zhenghchao.setVisibility(View.VISIBLE);
							lin_weiling.setVisibility(View.GONE);

							getClearCollateralTaskListAndCount1();// ?????????????????????????????? ??????
						} else if (str_bianhao.equals("SP0")) {
							Log.e(TAG, "bianhao********" + S_application.getApplication().bianhao);
							lin_zhenghchao.setVisibility(View.GONE);
							lin_weiling.setVisibility(View.VISIBLE);
							getSpinnerData();// ????????????????????????
						} else {
							lin_zhenghchao.setVisibility(View.GONE);
							lin_weiling.setVisibility(View.GONE);

							Log.e(TAG, "?????????");

						}
//						if (S_application.getApplication().c("")) {
//							
//						} else {
//						
//
//						}

					}
					manager.getRfid().close_a20();
					// S_application.getApplication().bianhao="";
					// List<String> jigous=tables[0].get("jigou").getValues();
					// if(jigous.size()<=0){
					// tv_showreaderresult.setText("");
					// }else{
					// tv_showreaderresult.setText(jigous.get(0));
					// }
					//
					// List<String> xianlus=tables[0].get("xianlu").getValues();
					// if(xianlus.size()<=0){
					// tv_showreaderresult.setText("");
					// }else{
					// tv_showreaderresult.setText(xianlus.get(0));
					// }

					break;
				case -1:
					// managerClass.getAbnormal().timeout(ZhouzXixinSmiaoActivity.this,"??????????????????",
					// onclickreplace);
					break;
				case -4:
					// managerClass.getAbnormal().timeout(ZhouzXixinSmiaoActivity.this,"??????????????????????????????",
					// onclickreplace);
					break;

				default:
					break;
				}
			}
		};
	}

	/***
	 * ????????????????????????
	 */
	private void LoadData() {
		// TODO Auto-generated method stub

	}

	private void InitView() {
		// TODO Auto-generated method stub
		ql_ruku_back = (ImageView) findViewById(R.id.ql_ruku_back);
		ql_ruku_back.setOnClickListener(this);
		btn_scanner = (Button) findViewById(R.id.btn_scanner);// / ??????
		btn_scanner.setOnClickListener(this);
		lin_zhenghchao = (LinearLayout) findViewById(R.id.lin_zhenghchao001);// ????????????
		lin_zhenghchao.setVisibility(View.GONE);
		lin_weiling = (LinearLayout) findViewById(R.id.lin_weiling);// ????????????
		lin_zhenghchao.setVisibility(View.GONE);
		tv_showreaderresult = (TextView) findViewById(R.id.tv_showreaderresult);

		// inven_zhengchao_quanbie_spinner_layout = (LinearLayout)
		// findViewById(R.id.inven_zhengchao_quanbie_spinner_layout);
		// inven_zhengchao_quanbie_spinner_layout.setOnClickListener(this);
		btn_zhengchaoupodata = (Button) findViewById(R.id.btn_zhengchaoupodata);
		btn_zhengchaoupodata.setOnClickListener(this);
		btn_zhengchaoclean = (Button) findViewById(R.id.btn_zhengchaoclean);
		btn_zhengchaoclean.setOnClickListener(this);
		spinner_quanbie = (Spinner) findViewById(R.id.spinner_quanbie);// ??????
		spinnerversion = (Spinner) findViewById(R.id.spinnerversion);// ??????
		spinner_falseall = (Spinner) findViewById(R.id.spinnerfalseall);// ??????
		inventory_btn_add = (Button) findViewById(R.id.inventory_btn_add);
		inventory_btn_add.setOnClickListener(this);
		spinner_paper = (Spinner) findViewById(R.id.spinnerpaper);// ??????????????????
		tv_zhenchaomoney = (TextView) findViewById(R.id.tv_zhenchaomoney);
		etpackgerzhengchaocount = (EditText) findViewById(R.id.etpackgerzhengchaocount);// ???????????????
		tvzhengchaoquanbei = (TextView) findViewById(R.id.tvzhengchaoquanbei);// ????????????
		tv_zhengchao_banbie = (TextView) findViewById(R.id.tv_zhengchao_banbie);// ????????????
		tv_zhenchaowanzhegncansuny = (TextView) findViewById(R.id.tv_zhenchaowanzhegncansun1);// ????????????
		tv_zhenchaopaperfalse = (TextView) findViewById(R.id.tv_zhenchaoshowpaperfalse);
		tv_zhenchaopackgercount = (TextView) findViewById(R.id.tv_zhenchaopackgercount);
		tv_zhenchaoallcounts = (TextView) findViewById(R.id.tv_zhenchaoallcounts);
		// ????????????

		btn_inventory_weiling_clean = (Button) findViewById(R.id.btn_inventory_weiling_clean);// ????????????
		btn_inventory_weiling_clean.setOnClickListener(this);
		btn_inventory_weilingupdata = (Button) findViewById(R.id.btn_inventory_weilingupdata);
		btn_inventory_weilingupdata.setOnClickListener(this);

		watercar_tailzeroto_spinner_layout = (LinearLayout) findViewById(R.id.watercar_tailzeroto_spinner_layout);//
		watercar_tailzeroto_spinner_layout.setOnClickListener(this);
		watercar_tailzeroto_spinner_textquanbie = (TextView) findViewById(R.id.watercar_tailzeroto_spinner_textquanbie);
		watercar_tailzerotbanbie_spinner_layout = (LinearLayout) findViewById(
				R.id.watercar_tailzerotbanbie_spinner_layout);
		watercar_tailzerotbanbie_spinner_layout.setOnClickListener(this);
		watercar_tailzeroto_banbie_spinner_text_banbie = (TextView) findViewById(
				R.id.watercar_tailzeroto_banbie_spinner_text_banbie);
		water_taizero_spinner_layout_zhuangtai = (LinearLayout) findViewById(
				R.id.water_taizero_spinner_layout_zhuangtai); // /
																// ??????????????????
		water_taizero_spinner_layout_zhuangtai.setOnClickListener(this);
		water_taizero_spinner_text_zhuangtai_tv = (TextView) findViewById(R.id.water_taizero_spinner_text_zhuangtai_tv);

//		water_taizero_spinner_layout_paper = (LinearLayout) findViewById(R.id.water_taizero_spinner_layout_paper);
//		water_taizero_spinner_layout_paper.setOnClickListener(this);
//		water_taizero_spinner_text_paper_tv = (TextView) findViewById(R.id.water_taizero_spinner_text_paper_tv);

		inventory_et_tailzeroet = (EditText) findViewById(R.id.inventory_et_tailzeroet);// ???????????????
		watercar_tailzero_xianjin_tianjia = (Button) findViewById(R.id.watercar_tailzero_xianjin_tianjia);
		watercar_tailzero_xianjin_tianjia.setOnClickListener(this);
		watercar_tailzerotopackgeslistview = (ListView) findViewById(R.id.watercar_tailzerotopackgeslistview);
		tailzero_heji = (TextView) findViewById(R.id.tailzero_heji);// /??????

//		  ????????????

	}

	class SaomiaoThread extends Thread {
		Message m;

		public SaomiaoThread() {
			super();
			m = handl.obtainMessage();
		}

		@Override
		public void run() {
			super.run();
			ICleaningManService is = new ICleaningManService();
			try {
				tables = Table.doParse(is.zhouzhuangxiangXinxi(S_application.getApplication().bianhao));

				if (tables.length > 0) {
					m.what = 1;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				m.what = -4;
			} catch (Exception e) {
				e.printStackTrace();
				m.what = -1;
			} finally {
				handl.sendMessage(m);
				GolbalUtil.onclicks = true;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// S_application.getApplication().bianhao="";
		manager.getRfid().addNotifly(wcsm);
		manager.getRfid().open_a20();
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.getRfid().close_a20();
		S_application.getApplication().bianhao = "";
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.inven_zhengchao_quanbie_spinner_layout:
		// break;
		case R.id.btn_zhengchaoupodata: // / ??????

			ZHengchaoUpdata();

			break;
		case R.id.btn_zhengchaoclean:
			ZhengChaoClean();
			break;
		case R.id.inventory_btn_add:// ??????????????????
			Pattern pattern = Pattern.compile("[0-9]+");

			String str_input = etpackgerzhengchaocount.getText().toString().trim();
			Matcher isNum = pattern.matcher(str_input);
			if (null != str_input || !str_input.equals("")) {
				if (isNum.matches()) {
					tv_zhenchaopackgercount.setText("" + etpackgerzhengchaocount.getText());
					if (null != moneyType) {
						tvzhengchaoquanbei.setText("" + moneyType);// ????????????
					} else {
						showBigToast("?????????", 400);
					}

					if (null != moneyid || !moneyid.equals("")) {
						tv_zhengchao_banbie.setText(moneyid + "");
					}
					if (null != cansunwanczheng || !cansunwanczheng.equals("")) {// ??????????????????
						tv_zhenchaowanzhegncansuny.setText(cansunwanczheng + "");
					}
//			????????????????????????
					double i = Double.parseDouble(tv_zhenchaomoney.getText().toString().trim());
//			int i = Integer.parseInt(tv_zhenchaomoney.getText().toString().trim()); 
					double f = Double.parseDouble(str_input);

					tv_zhenchaoallcounts.setText((i * f) + "??????");// ???????????????
				} else {

					manager.getResultmsg().resultmsg(this, "????????????", false);
					return;
				}
			} else {
				manager.getResultmsg().resultmsg(this, "????????????", false);
				return;
			}
			break;

		// ////////////////////////////////////////////////// ??????????????????
		case R.id.btn_inventory_weiling_clean:

			break;

		case R.id.btn_inventory_weilingupdata:
			UpdataWeilingData();
			break;

		case R.id.watercar_tailzeroto_spinner_layout:// ?????????????????????
			List<String> couponList = new ArrayList<String>();
			for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
				String coupon = item.getQuanbieName();
				if (!couponList.contains(coupon)) {
					couponList.add(coupon);
				}
			}
			couopnSpinner = new HxbSpinner(this, watercar_tailzeroto_spinner_layout,
					watercar_tailzeroto_spinner_textquanbie, couponList);
			couopnSpinner.setSpinnerHeight(watercar_tailzeroto_spinner_layout.getHeight() * 2);
			couopnSpinner.showPopupWindow(watercar_tailzeroto_spinner_layout);
			if (editionSpinner != null) {
				editionSpinner.clear();
				if (watercar_tailzeroto_spinner_textquanbie != null)
					watercar_tailzeroto_spinner_textquanbie.setText(defaultOptions);
			}

			break;
		case R.id.watercar_tailzerotbanbie_spinner_layout:
			if (null == couopnSpinner || couopnSpinner.equals("")) {
				showBigToast("????????????", 100);
			} else {

				List<String> editionList = new ArrayList<String>();
				String coupon = couopnSpinner.getChooseText();
				if (null != coupon) {
					for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
						if (coupon.equals(item.getQuanbieName())) {
							editionList.add(item.getEDITION());
						}
					}
				}
				editionSpinner = new HxbSpinner(this, watercar_tailzerotbanbie_spinner_layout,
						watercar_tailzeroto_banbie_spinner_text_banbie, editionList);
				editionSpinner.setSpinnerHeight(watercar_tailzerotbanbie_spinner_layout.getHeight() * 2);
				editionSpinner.showPopupWindow(watercar_tailzerotbanbie_spinner_layout);
			}
			break;
		case R.id.water_taizero_spinner_layout_zhuangtai:
			spinner = new MySpinner(this, water_taizero_spinner_layout_zhuangtai,
					water_taizero_spinner_text_zhuangtai_tv);
			spinner.setSpinnerHeight(water_taizero_spinner_layout_zhuangtai.getHeight() * 2);
			spinner.setList(this, str_zhuangtai);
			spinner.showPopupWindow(water_taizero_spinner_layout_zhuangtai);
			spinner.setList(this, str_zhuangtai, 40);

			break;

//		case R.id.water_taizero_spinner_layout_paper:
//			spinner1 = new MySpinner(this, water_taizero_spinner_layout_paper,
//					water_taizero_spinner_text_paper_tv);
//			spinner1.setSpinnerHeight(water_taizero_spinner_layout_paper
//					.getHeight() * 2);
//			spinner1.setList(this, str_paperfalse);
//			spinner1.showPopupWindow(water_taizero_spinner_layout_paper);
//			spinner1.setList(this, str_paperfalse, 40);
//			break;
		case R.id.watercar_tailzero_xianjin_tianjia:
			if (null == couopnSpinner || null == couopnSpinner.getChooseText() || null == editionSpinner
					|| null == editionSpinner.getChooseText()) {
				Log.d(TAG, "????????????????????????????????????");
				showBigToast("?????????????????????????????????", 400);
				btn_inventory_weilingupdata.setEnabled(false);
				btn_inventory_weilingupdata.setBackgroundResource(R.drawable.buttom_selector_bg);
				return;
			} else {

				addXianJin();
				btn_inventory_weilingupdata.setEnabled(true);
				btn_inventory_weilingupdata.setBackgroundResource(R.drawable.buttom_selector_bg);
			}
			break;

		case R.id.btn_scanner:
			S_application.getApplication().bianhao = null;
			manager.getRfid().addNotifly(wcsm);
			manager.getRfid().open_a20();
			break;

		case R.id.ql_ruku_back:
			InventoryActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/***
	 * ??????????????????
	 */
	private void UpdataWeilingData() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// ????????????
					netresultClean = new CashToPackgersService().TailzertoPacakge_fillTailZero(null, null);
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

	/***
	 * ????????????
	 */
	private void ZhengChaoClean() {
		tv_zhengchao_banbie.setText("");
		tvzhengchaoquanbei.setText("");
		tv_zhenchaopackgercount.setText("");
		tv_zhenchaowanzhegncansuny.setText("");
		tv_zhenchaoallcounts.setText("");
	}

	/***
	 * ??????????????????
	 */
	String str_result = "";

	private void ZHengchaoUpdata() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// ????????????

					netresultClean = new CashToPackgersService().updateCashClearingWork(inentorwraterTailyzero,
							inentorwraterTailyzero, inentorwraterTailyzero, inentorwraterTailyzero);
//				if(str_result)
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

	/***
	 * ??????????????????
	 */

	BaggingForCashEntity[] baggingForCashEntity;

	public void getClearCollateralTaskListAndCount1() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// ????????????
					netresultClean = new CashToPackgersService().TailCoupon_getMoneyEditionList();

					Log.d(TAG, "-----" + netresultClean);
					cashtopacgerslist.clear();
					if (!netresultClean.equals("")) {
						Gson gson = new Gson();

						baggingForCashEntity = gson.fromJson(netresultClean, BaggingForCashEntity[].class);

						// ????????????????????????????????????
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
								Log.e(TAG, "??????" + moneyEditionMapstr.get(item.getEDITION()));
								listmapedition.add(baggmap);

							} else {
								List<Map<String, String>> list = new ArrayList<Map<String, String>>();
								list.add(baggmap);
								moneyEditionMapstr.put(item.getEDITION(), list);
							}
							cahstopackgermeid.add(item.getMEID());
						}
						// ???????????? "???100??????5??????
						Set<String> moneySet = moneyEditionMap.keySet();
						array2 = new String[moneySet.size()];
						moneySet.toArray(array2);
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

	/***
	 * spinner ?????????????????????????????????????????? ????????????
	 */
	public void spinnerselect() {

		if (0 != (array2.length)) {
			mSpinnerAdapter = new SpinnerAdapter(InventoryActivity.this, android.R.layout.simple_spinner_dropdown_item,
					array2);
			mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_quanbie.setAdapter(mSpinnerAdapter);
			spinner_quanbie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

					moneyType = array2[pos];
					getversioncode(moneyType);
					// ???????????????????????????
					String title = "";
					// readqumbei = "";// ????????????????????????null
					// intentvalue = "";
					// readcash_qunabeibu.setText("");
					// cashreadmoney.setText("");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

					Log.e(TAG, "??????????????????");

				}
			});

			// ????????????

		}
	}

	private class SpinnerAdapterquanzhong extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

		public SpinnerAdapterquanzhong(final Context context, final int textViewResourceId, final String[] objects) {
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
			// ????????????spinner??????????????? ???????????????
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

	// ?????????
	class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

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
			// ????????????spinner??????????????? ???????????????
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLUE);
			tv.setTextSize(40);
			return convertView;
		}
	}

	/***
	 * ??????????????????
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
				Log.e(TAG, "??????????????????????????????" + item.getMONEYTYPE() + monestr);
				Log.e(TAG, "!!!!!" + moneytypeweiling);

				if (listmap.contains(item.getEDITION())) {

				} else {
					if (monestr.equals("0")) {
						listmap.add("-");// // ????????????deition
					} else {
						listmap.add(monestr + "");// // ????????????deition
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

		array3 = listmap.toArray(new String[0]);
		;

		mspinnbanbie = new SpinnerAdapter(InventoryActivity.this, android.R.layout.simple_spinner_dropdown_item,
				array3);
		mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerversion.setAdapter(mspinnbanbie);
		spinnerversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				moneyid = array3[pos];
				Log.e(TAG, "moneyid???????????????????????????" + moneyid);
				;
				if (null != moneyType && null != moneyid) {
					selectmeid(moneyType, moneyid);// /??????meid?????? ???????????????
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// ????????????

		mSpinnerAdapterquanzhong = new SpinnerAdapter(InventoryActivity.this,
				android.R.layout.simple_spinner_dropdown_item, str_juanzhong);
		mSpinnerAdapterquanzhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_falseall.setAdapter(mSpinnerAdapterquanzhong);
		spinner_falseall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Log.d(TAG, str_juanzhong[pos] + "");
				cansunwanczheng = str_juanzhong[pos];
				Log.d(TAG, "cansunwanczheng===" + cansunwanczheng);
				SetAllFalseState(cansunwanczheng);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback

			}
		});

		// ????????????????????????
		mSpinnerPaper = new SpinnerAdapter(InventoryActivity.this, android.R.layout.simple_spinner_dropdown_item,
				str_paper);
		mSpinnerPaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_paper.setAdapter(mSpinnerPaper);
		spinner_paper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Log.d(TAG, str_juanzhong[pos] + "");
				paperfalse = str_paper[pos];
				Log.d(TAG, "paperfalse===" + paperfalse);
				// SetpaperFalse(paperfalse);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback

			}
		});

	}

	/***
	 * ??????????????????????????? meid??????
	 * 
	 * @param monetype
	 * @param moneyid
	 * @return
	 */
	public String selectmeid(String monetype, String moneyid) {
		if (null != monetype && monetype.equals("") || null != moneyid && moneyid.equals("")) {
			showBigToast("??????" + monetype + "??????" + moneyid + "????????????", 400);
		} else {
			Log.e(TAG, "meid============??????" + monetype + "===??????====" + moneyid);
			for (Entry<String, List<Map<String, String>>> item : moneyEditionMap.entrySet()) {
				if (monetype.equals(item.getKey())) {
					for (Map<String, String> map : item.getValue()) {
						if (moneyid.equals(map.get("edition"))) {
							Log.e(TAG, "edition==========" + moneyid + (map.get("edition")));
							Log.e(TAG, "meid======" + map.get("meid"));
							TailZeromeid = map.get("meid");
							// PARVALUE

							ByTailZeromeidtolostvalue = map.get("bagmoney");
							handler.sendEmptyMessage(8);// ?????????????????????????????????
							;
							Log.e(TAG, "bagmoney======" + map.get("bagmoney"));
							break;
						} else {
							Log.e(TAG, "bagmoney=====" + map.get("bagmoney"));
							ByTailZeromeidtolostvalue = map.get("bagmoney");
							handler.sendEmptyMessage(8);// ?????????????????????????????????
							TailZeromeid = null;
						}
					}
				}
			}

		}
		// ???????????????
		return TailZeromeid;
	}

	public void showBigToast(String info, int duration) {
		Toast toast = new Toast(InventoryActivity.this);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
		TextView tv = new TextView(InventoryActivity.this);
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
	 * ????????????
	 * 
	 * @param cansunwanczheng
	 */
	private void SetAllFalseState(String cansunwanczheng) {
		// TODO Auto-generated method stub
		if (cansunwanczheng.equals("??????")) {
			cansunid = "2";
			if (null != moneyType && null != moneyid) {
				Log.d(TAG, "=======" + moneyType + moneyid);
			} else {
			}
			if (!ByTailZeromeidtolostvalue.equals("")) {
				double intbytailzermcan = Double.parseDouble(ByTailZeromeidtolostvalue);
				double result = (intbytailzermcan * (0.0001));
				tv_zhenchaomoney.setText(result + "");
			}

			// tvMoneyCoun.setText(""+ByTailZeromeidtoparval);
		} else if (cansunwanczheng.equals("??????")) {
			cansunid = "4";
			if (null != moneyType && null != moneyid) {
				Log.d(TAG, "=======" + moneyType + moneyid);
			} else {
			}

		} else {
			showBigToast("?????????????????????", 1000);
		}

	}

	/**
	 * ??????????????????
	 */

	// public void SetpaperFalse(String paper) {
	// Log.d(TAG, "=====paper==" + paper + paper);
	//
	// }
	// /////////////////////////////////////////////??????
	/****
	 * ??????????????????????????????
	 */
	public void getSpinnerData() {
		manager.getRuning().runding(InventoryActivity.this, "???????????????...");
		// ??????????????????
		Thread thread = new Thread(new GetQuanbieXinxi());
		thread.start();
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @author yuyunheng
	 */
	private class GetQuanbieXinxi implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = create.obtainMessage();
			try {
				param = new CashToPackgersService().TailCoupon_getMoneyEditionList();
				Gson gson = new Gson();
				TailZerotoEntity[] mTailZerotoEntity = gson.fromJson(param, TailZerotoEntity[].class);
				for (int i = 0; i < mTailZerotoEntity.length; i++) {
					TailZerotoEntitylist = Arrays.asList(mTailZerotoEntity);
				}
				if (TailZerotoEntitylist != null && !TailZerotoEntitylist.equals("")) {
					msg.obj = param;
					msg.what = 1;
				} else {
					msg.what = 3; // ????????????????????????
				}
			} catch (SocketTimeoutException ee) {
				// TODO: handle exception
				msg.what = 2;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 3; // ????????????????????????
			}

			create.sendMessage(msg); // ????????????
		}

	}

	/***
	 * ???????????????????????? ????????????
	 */
	private List<String> quanbieIds = new ArrayList<String>();
	private List<String> quanbieNames = new ArrayList<String>();;
	private List<String> quanjiazhis = new ArrayList<String>();;
	private List<String> canshunjiazhis = new ArrayList<String>();;

	private Map<String, String> mapbanbie = new HashMap<String, String>();
	private Map<String, Map<String, String>> maplistquanbie = new HashMap<String, Map<String, String>>();// ??????
	private Map<String, Map<String, String>> mapbanbieid = new HashMap<String, Map<String, String>>(); // ????????????
	private Map<String, String> mapjiazhi = new HashMap<String, String>(); // ????????????

	private Handler create = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			manager.getRuning().remove(); // ????????????
			switch (msg.what) {
			case 1: // ????????????
				// ??????????????????
				str_zhuangtai = new String[] { "?????????", "?????????", "?????????" };
//				str_paperfalse = new String[] { "??????", "??????" };
				String quanbieXinxi = msg.obj.toString();
				System.out.println("???????????????????????????" + quanbieXinxi);
				Table[] table = Table.doParse(quanbieXinxi);

				for (int i = 0; i < TailZerotoEntitylist.size(); i++) {
					Map<String, String> val = new HashMap<String, String>();

					val.put("moneytype", TailZerotoEntitylist.get(i).getMONEYTYPE());
					val.put("meid", TailZerotoEntitylist.get(i).getMEID());
					val.put("parvalue", TailZerotoEntitylist.get(i).getPARVALUE());
					val.put("eidtion", TailZerotoEntitylist.get(i).getEDITION());
					val.put("lossvalue", TailZerotoEntitylist.get(i).getLOSSVALUE());
					mapbanbieid.put(TailZerotoEntitylist.get(i).getMEID(), val);
				}
				// ?????? banbielist
				for (Entry<String, Map<String, String>> str : mapbanbieid.entrySet()) {
					TailPackgerEntityQuanbieXinxi tpexx = new TailPackgerEntityQuanbieXinxi();
					Map<String, String> val = str.getValue();
					tpexx.setCanshunJiazhi(val.get("lossvalue"));
					tpexx.setEDITION(val.get("eidtion"));
					tpexx.setQuanbieId(val.get("meid"));
					tpexx.setQuanbieName(val.get("moneytype"));
					tpexx.setQuanJiazhi(val.get("parvalue"));
					quanbieListxin.add(tpexx);
				}
				break;

			case 2: // ????????????
				manager.getAbnormal().timeout(InventoryActivity.this, "??????????????????", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // ??????????????????
					}
				});
				break;

			case 3: // ????????????
				manager.getAbnormal().timeout(InventoryActivity.this, "????????????????????????", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // ??????????????????
					}
				});
				break;

			}
		}
	};

	/**
	 * ????????????
	 * 
	 * @author lc
	 * 
	 */
	String moneyversion = "";

	public void addXianJin() {
		String xjcount = inventory_et_tailzeroet.getText().toString().trim();

		if (null == couopnSpinner.getChooseText()) {

			manager.getResultmsg().resultmsg(this, "?????????????????????", false);
			return;
		} else {
			juanbie = couopnSpinner.getChooseText();
			moneyversion = editionSpinner.getChooseText();
		}

		if (juanbie.equals(defaultOptions)) {
			manager.getResultmsg().resultmsg(this, "?????????????????????", false);
			return;
		}
		if (null == moneyversion) {

			manager.getResultmsg().resultmsg(this, "???????????????", false);
		} else {

			if (moneyversion.equals(defaultOptions)) {
				manager.getResultmsg().resultmsg(this, "???????????????", false);
				return;
			}
		}

		zhuangtai = water_taizero_spinner_text_zhuangtai_tv.getText().toString();
		if (TextUtils.isEmpty(zhuangtai) || zhuangtai.equals(defaultOptions)) {
			manager.getResultmsg().resultmsg(this, "???????????????", false);
			return;
		}
//		str_select_paperfalse = water_taizero_spinner_text_paper_tv.getText()
//				.toString().trim();
//		if (TextUtils.isEmpty(str_select_paperfalse)
//				|| str_select_paperfalse.equals(defaultOptions)) {
//			manager.getResultmsg().resultmsg(this, "????????????????????????", false);
//			return;
//		}

		if (TextUtils.isEmpty(xjcount)) {
			manager.getResultmsg().resultmsg(this, "???????????????", false);
			return;
		}
		// ???????????????????????????????????????id
		if (!"".equals(zhuangtai) && null != zhuangtai) {
			if (zhuangtai.equals("?????????")) {
				zhuangtai = "0";
			} else if (zhuangtai.equals("?????????")) {
				zhuangtai = "2";
			} else if (zhuangtai.equals("?????????")) {
				zhuangtai = "1";
			}
		}
		TailPackgerEntityQuanbieXinxi choose = InventoryActivity.this.getTailPackgerEntityQuanbieXinxi(juanbie,
				moneyversion);

		if (choose != null) {
			System.out.println("????????????????????????-->" + zhuangtai);
			TailZerotoPackgerTianJiaXianJin model = this.chooseExists(choose.getQuanbieId(), zhuangtai);
			if (model != null) {
				model.setCount(String.valueOf(Integer.parseInt(xjcount) + Integer.parseInt(model.getCount())));
			} else {
				model = new TailZerotoPackgerTianJiaXianJin();
				model.setCanshunJiazhi(choose.getCanshunJiazhi());
				model.setCash_type(choose.getQuanbieId());
				model.setCashbanbie(moneyversion);
				model.setCount(xjcount);
				model.setJuanbie(juanbie);
				model.setQuanJiazhi(choose.getQuanJiazhi());
				model.setZhuangtai(zhuangtai);
				xianjinlisttinajia.add(model);
			}
		} else {
			int listcount = Integer.parseInt(xianjinlisttinajia.get(isok).getCount());
			int xjshuliang = Integer.parseInt(xjcount);
			int count = listcount + xjshuliang;
			xianjinlisttinajia.get(isok).setCount(count + "");
			isok = -1;
		}

		Heji();
		watercar_tailzerotopackgeslistview.setAdapter(adapter);
		new TurnListviewHeight(watercar_tailzerotopackgeslistview);
		System.out.println("?????????????????????----->");
		for (int i = 0; i < xianjinlisttinajia.size(); i++) {
			System.out
					.println(xianjinlisttinajia.get(i).getJuanbie() + "----->>" + xianjinlisttinajia.get(i).getCount());
		}
	}

	/**
	 * ???????????????????????????????????????????????????
	 * 
	 * @param moneyType
	 * @param edition
	 * @return
	 */
	private TailPackgerEntityQuanbieXinxi getTailPackgerEntityQuanbieXinxi(String moneyType, String edition) {
		if ((null == edition || null == moneyType) || moneyType.equals("") || edition.equals("")) {

			manager.getResultmsg().resultmsg(this, "???????????????", false);

		} else {
			if (moneyType.equals("")) {
				showBigToast("?????????" + moneyType, 100);
			} else if (edition.equals("") || null == edition) {
				showBigToast("?????????" + edition, 100);
			}

			for (TailPackgerEntityQuanbieXinxi item : quanbieListxin) {
				if (moneyType.equals(item.getQuanbieName()) && edition.equals(item.getEDITION()))
					return item;
			}
		}
		return null;
	}

	/**
	 * ??????
	 */
	public void Heji() {

		heji_xianjin = 0;
		if (xianjinlisttinajia.size() != 0) {

			for (int i = 0; i < xianjinlisttinajia.size(); i++) {
				int count = Integer.parseInt(xianjinlisttinajia.get(i).getCount().trim());
				double quanJiazhi = Double.parseDouble(xianjinlisttinajia.get(i).getQuanJiazhi().trim());
				double canshuJiazhi = Double.parseDouble(xianjinlisttinajia.get(i).getCanshunJiazhi().trim());
				int zhuangtai = Integer.parseInt(xianjinlisttinajia.get(i).getZhuangtai().trim());

				switch (zhuangtai) {
				case 0: // ??????
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 1: // ??????
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 2: // ??????
					heji_xianjin += (double) (canshuJiazhi * count);
					break;
				}
			}
		}
		tailzero_heji.setText(new NumFormat().format(heji_xianjin + ""));
	}

	/**
	 * ?????????????????????
	 * 
	 * @param meid
	 * @return
	 */
	private TailZerotoPackgerTianJiaXianJin chooseExists(String meid, String state) {
		for (TailZerotoPackgerTianJiaXianJin item : xianjinlisttinajia) {
			if (meid.equals(item.getCash_type()) && state.equals(item.getZhuangtai())) {
				return item;
			}
		}
		return null;
	}

	class XianJinAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(InventoryActivity.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return xianjinlisttinajia.size();
		}

		@Override
		public Object getItem(int arg0) {
			return xianjinlisttinajia.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = lf.inflate(R.layout.adapter_tailzerot_packger_tianjia, null);
				view = new ViewHodler();
				view.banbie = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv0);
				view.juanbie = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv1);
				view.zhuangtai = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv2);
				view.shuliang = (TextView) arg1.findViewById(R.id.tailzerotada_title_tv3);
				view.shanchu = (Button) arg1.findViewById(R.id.tailzerotada_title_button);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			TailZerotoPackgerTianJiaXianJin iObj = xianjinlisttinajia.get(position);
			view.banbie.setText(iObj.getCashbanbie());// ??????
			view.juanbie.setText(iObj.getJuanbie());

			String state = iObj.getZhuangtai();
			if (state.equals("0")) {
				view.zhuangtai.setText("??????");
				setstate = "2";
			} else if (state.equals("1")) {
				view.zhuangtai.setText("??????");
				setstate = "4";
			} else if (state.equals("2")) {
				view.zhuangtai.setText("??????");
				setstate = "4";
			}
			// view.zhuangtai.setText(xianjinlist.get(position).getZhuangtai());
			view.shuliang.setText(xianjinlisttinajia.get(position).getCount());
			view.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					xianjinlisttinajia.remove(position);
					delete = "2"; // ??????????????????
					Heji();
					adapter.notifyDataSetChanged();
					new TurnListviewHeight(watercar_tailzerotopackgeslistview);
					// if (xianjinlist.size() > 0) {
					// qingdfen_ok.setEnabled(true);
					// qingdfen_ok
					// .setBackgroundResource(R.drawable.buttom_selector_bg);
					// lin_lloutgo.setVisibility(View.GONE);
					//
					// } else if (xianjinlist.size() == 0) {
					// qingdfen_ok.setEnabled(false);
					// qingdfen_ok
					// .setBackgroundResource(R.drawable.button_gray);
					// lin_lloutgo.setVisibility(View.GONE);
					//
					// Log.e("???????????????", "???????????????");
					//
					// }
				}
			});
			return arg1;
		}
	}

	public static class ViewHodler {
		TextView juanbie, zhuangtai, shuliang, banbie;
		Button shanchu;
	}

	/***
	 * ?????????????????????
	 * 
	 * 
	 */
	String inentorwraterTailyzero = "";

	public void updataTailzero() throws Exception {
		String userid = GApplication.use.getUserzhanghu();
		String watercard = S_application.getApplication().bianhao;
		Log.d(TAG, "userid" + userid);
		String watertailzero = "";
		JSONArray array = new JSONArray();
		try {
			// Log.e(TAG,"Tasknumber======"+Tasknumber);
			for (TailZerotoPackgerTianJiaXianJin item : xianjinlisttinajia) {
				JSONObject json = new JSONObject();
				json.put("meid", item.getCash_type());
				json.put("total", item.getCount());
				json.put("lossstate", item.getZhuangtai());

				double amount = 0.0;
				int count = Integer.parseInt(item.getCount());
				if ("1".equals(item.getZhuangtai())) {// ?????????
					amount = count * Double.parseDouble(item.getCanshunJiazhi());
				} else if ("0".equals(item.getZhuangtai())) {
					amount = count * Double.parseDouble(item.getQuanJiazhi());
				} else if ("2".equals(item.getZhuangtai())) {
					amount = (count * Double.parseDouble(item.getQuanJiazhi()) * 0.5);
				}
				json.put("amount", amount);
				array.put(json);
			}
			// Log.e(TAG,"======"+cvoun+"===="+returnaccountinteninfolist.size()+"==="+userId);
			// ??????????????????null ?????????????????? ?????????dialog??????
			if (array.length() == 0) {
				handler.sendEmptyMessage(998);
			} else {

				inentorwraterTailyzero = new BaggingForCashService().upDataWaterCardTailZero(userid, watercard, null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(TAG, "????????????????????????" + inentorwraterTailyzero);

	}

}