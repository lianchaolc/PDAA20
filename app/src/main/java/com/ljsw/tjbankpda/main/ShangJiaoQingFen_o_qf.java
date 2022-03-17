package com.ljsw.tjbankpda.main;

import hdjc.rfid.operator.RFID_Device;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.collateraladministratorsorting.activity.SelectTaskByCollateralActivity;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.entity.ShangJiaoQingFen_o_qf_Print_Entity;
import com.ljsw.tjbankpda.qf.entity.TianJiaXianJin;
import com.ljsw.tjbankpda.qf.entity.TianJiaZhongKong;
import com.ljsw.tjbankpda.qf.entity.ZhongkongXinxi;
import com.ljsw.tjbankpda.qf.service.QingfenRenwuService;
import com.ljsw.tjbankpda.util.BianyiType;
import com.ljsw.tjbankpda.util.BiaohaoJiequ;
import com.ljsw.tjbankpda.util.MySpinner;
import com.ljsw.tjbankpda.util.NumFormat;
import com.ljsw.tjbankpda.util.ShangjiaoDizhiSaomiaoUtil;
import com.ljsw.tjbankpda.util.Skip;
import com.ljsw.tjbankpda.util.StringGetNum;
import com.ljsw.tjbankpda.util.Table;
import com.ljsw.tjbankpda.util.TurnListviewHeight;
import com.manager.classs.pad.ManagerClass;

/**
 * 清分员清分activity9001
 * 
 * @author yuyunheng 2018_12_11lc 实现 现金布局 的显示和隐藏 抵制押品能不能点击 现金字段为null 现金为0
 *         抵质押品为null 没有抵质押品课通过此进行判断 思路：：：：通过两个订单号进行判断集合中数据是否>0 有值 显示
 *         没有值的时候进行现金的隐藏和抵质押品的不让点击 2019_0508 行方要求中空前加入前面数字后面文字显示
 *         2019_06_04数据库返回字段更改修改 001；农商行转账
 */
public class ShangJiaoQingFen_o_qf extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "ShangJiaoQingFen_o_qf";
	private LinearLayout spinner_layout_juanbie, spinner_layout_zhuangtai, alllinear, // 总的大布局
			spinner_layout_pingzheng, lin_cash, lin_impotan, lin_lloutgo, shangjiaoqingfen_lin_cash;// 现金模块的显示隐藏，重要凭证
	private TextView spinner_text_juanbie, spinner_text_zhuangtai, xianjin_heji, zhongkong_yiqingdian,
			zhongkong_zhonglei, dizhi_heji, peisongdan, ResistcollateralCoaunt;// 抵制押品数量//
	private MySpinner spinner;
	private String[] str_zhuangtai;
	private String juanbie, zhuangtai, pingzheng;// 接收spinner所选信息
	private Button tianjia_xianjin, tianjia_zhongkong, tianjia_dizhi, openSaomiao, qingdfen_ok, btn_print;// 打印抵制押品
	private EditText xianjin_count, pingzhengbianhao, pingzheng_haoduan, dizhi_bianhao;
	private ListView xianjin_listview, pingzheng_listview, dizhi_listview;// 现金,重空,抵质的ListView
	private ImageView back;
	private String orderNum;

	private List<QuanbieXinxi> quanbieList = new ArrayList<QuanbieXinxi>(); // 创建券别信息集合
	private List<ZhongkongXinxi> zhongkongList = new ArrayList<ZhongkongXinxi>(); // 创建重空信息集合
	private List<ZhongkongXinxi> lzhongkongList = new ArrayList<ZhongkongXinxi>();// xinlc 001,农商行支票
	String xianjingMsg;// 现金msg
	String zhongkongMsg;// 重空凭证msg
	String dizhiMsg;// 抵质押品msg
	String zhongkongSubmit; // 重空提交msg

	String  newdizhiMSg;
	private StringGetNum getnum = new StringGetNum(); // 截取数字工具类
	private BiaohaoJiequ jiequ = new BiaohaoJiequ(); // 截取编号工具类

	boolean isCanOpenSaomiao = true;
	String psdId = "";
	private boolean booleancahsOnclick = false;// 判断添加是否可以点击
	private boolean booleanimportanOnclick = false;// 判断重要中控凭证
	private String delete = "0";// 是否删除过
	XianJinAdapter adapter;
	ZhongKongAdapter zkadapter;
	DiZhiAdapter dzadapter;

	private String peisongId;// 上页面传单号
	private static List<String> staprintlist = new ArrayList<String>();

	private int isok = -1;
	private int zkok = -1;
	private double heji_xianjin = 0;
	private int dzok = -1;
	private int heji_dizhi;
	private ShangjiaoDizhiSaomiaoUtil dizhiSaomiaoUtil = new ShangjiaoDizhiSaomiaoUtil();
	private RFID_Device rfid;

	List<TianJiaXianJin> xianjinlist = new ArrayList<TianJiaXianJin>();
	List<TianJiaZhongKong> zhongkonglist = new ArrayList<TianJiaZhongKong>(); // 券别信息集合
	List<String> dizhilist = new ArrayList<String>();
	List<String> dizhilistnumlist = new ArrayList<String>();
	private List<String> cash = new ArrayList<String>();// 判断布局显示现金
	private List<String> Commoditycollaterallist = new ArrayList<String>(); // 抵质押品
	private List<String> importlist = new ArrayList<String>();// 重要凭证
	private Table[] shangjiaoMark; // 对比信息
	private Table[] shangjiaoRenwu; // 上缴登记信息
	private Table[] subimtMark; // 提交信息

	String dizhishunum = "0";// 获取抵制押品要打印的数量

	private RFID_Device getRfid() {
		if (rfid == null) {
			rfid = new RFID_Device();
		}
		return rfid;
	}

	private ManagerClass manager;// 弹出框

	Table[] table3 = new Table[10];// 声明一个数据
//	打印机20200825
    private List<String>  printsacelist= new ArrayList<String>();//  接受打印机数据的集合
	private List<Map<String,String>>  printsacelistmap= new ArrayList<Map<String,String>>();//  接受打印机数据的集合
	private String userNumber;//登录人的账号
	private  String  printcode;// 获得打印机的牌子
	private LinearLayout  shangjiaoqingfen_spinner_printinfo_layout,print_spinner_layout;//   ,二次进入spinner需要隐藏
	private  TextView   shangjiaoqingfen_printinfo_spinner_text;
	private  String param;


	private  Map<String,String>   printmapnetrestult=new HashMap<String, String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangjiaoqingfen_qf);
		peisongId = super.getIntent().getExtras().getString("peisongId");
		System.out.println("====" + peisongId);// 90405010020181221173307
		manager = new ManagerClass();
		manager.getResultmsg().setHandler(submitHandler);
		manager.getResultmsg().setHandler2Button(isSubmitHandler);
		dizhiSaomiaoUtil.setHand(handler);

		adapter = new XianJinAdapter();
		zkadapter = new ZhongKongAdapter();
		dzadapter = new DiZhiAdapter();

		orderNum = super.getIntent().getExtras().getString("orderNum");
		userNumber=GApplication.user.getYonghuZhanghao();// 获取账号名称
		load();
		bangdingList();
		getDate(); // 获取券别等级信息

	}

	/*
	 * listView 绑定
	 */
	private void bangdingList() {
		zkadapter = new ZhongKongAdapter();
		pingzheng_listview.setAdapter(zkadapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getRfid().scanclose();
		manager.getRuning().remove();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(rfid!=null){
			rfid.close();
		}
	}

	public void load() {
		shangjiaoqingfen_lin_cash = (LinearLayout) findViewById(R.id.shangjiaoqingfen_lin_cash);
		shangjiaoqingfen_lin_cash.setVisibility(View.GONE);
		alllinear = (LinearLayout) findViewById(R.id.alllinear);
		alllinear.setVisibility(View.GONE); // 加载时显示大布局

		lin_lloutgo = (LinearLayout) findViewById(R.id.lin_llout);// 抵制押品显示
		lin_lloutgo.setOnClickListener(this);
		lin_cash = (LinearLayout) findViewById(R.id.lin_cash);// 现金布局
		lin_impotan = (LinearLayout) findViewById(R.id.impotan);// 重要空白凭证
		peisongdan = (TextView) findViewById(R.id.zhouzhuan_hedui_left_text);
		spinner_layout_juanbie = (LinearLayout) findViewById(R.id.shangjiaoqingfen_spinner_layout);
		spinner_layout_juanbie.setOnClickListener(this);
		spinner_text_juanbie = (TextView) findViewById(R.id.shangjiaoqingfen_spinner_text);
		spinner_text_zhuangtai = (TextView) findViewById(R.id.shangjiaoqingfen_spinner_text_zhuangtai);
		spinner_text_zhuangtai.setText("完整券");// 经过修改下拉框改为只是完整券其它位置不动
//		spinner_layout_zhuangtai = (LinearLayout) findViewById(R.id.shangjiaoqingfen_spinner_layout_zhuangtai);
//		spinner_layout_zhuangtai.setOnClickListener(this);
		tianjia_xianjin = (Button) findViewById(R.id.shangjiaoqingfen_xianjin_tianjia);
		tianjia_xianjin.setOnClickListener(this);
		xianjin_count = (EditText) findViewById(R.id.shangjiaoqingfen_edit);
		xianjin_listview = (ListView) findViewById(R.id.qf_shangjiaoqingfen_xianjin_listView1);
		back = (ImageView) findViewById(R.id.shangjiaoqingfen_back);
		back.setOnClickListener(this);
		xianjin_heji = (TextView) findViewById(R.id.shangjiaoqingfen_heji);
		// 重空清点数量
		zhongkong_yiqingdian = (TextView) findViewById(R.id.shangjiaoqingfen_zhongkong_yiqingdian);
		pingzheng_listview = (ListView) findViewById(R.id.qf_shangjiaoqingfen_zhongkong_listView1);
//		pingzheng_listview.setAdapter(adapter);// 出现bug 中控显示条目
		pingzhengbianhao = (EditText) findViewById(R.id.shangjiaoqingfen_zhongkong_edit);
		// 重空种类spinner显示
		zhongkong_zhonglei = (TextView) findViewById(R.id.shangjiaoqingfen_zhongkong_spinner_text);
		spinner_layout_pingzheng = (LinearLayout) findViewById(R.id.shangjiaoqingfen_spinner_zhongkong_layout);
		spinner_layout_pingzheng.setOnClickListener(this);
		pingzheng_haoduan = (EditText) findViewById(R.id.shangjiaoqingfen_haoduan_edit);
		tianjia_zhongkong = (Button) findViewById(R.id.shangjiaoqingfen_zhongkong_tianjia);
		tianjia_zhongkong.setOnClickListener(this);
		xianjin_heji.setText("");
		peisongdan.setText(peisongId);
//		tianjia_zhongkong.setBackgroundColor(R.drawable.buttom_selector_bg);
//		tianjia_zhongkong.setBackgroundColor(R.drawable.buttom_select_press);
		// 抵质押品
//		dizhi_heji = (TextView) findViewById(R.id.shangjiaoqingfen_dizhiyapin_text);
//		tianjia_dizhi = (Button) findViewById(R.id.shangjiaoqingfen_dizhiyapin_tianjia);
//		tianjia_dizhi.setOnClickListener(this);
//		openSaomiao = (Button) findViewById(R.id.shangjiaoqingfen_dizhiyapin_tianjia_saomiao);
//		openSaomiao.setOnClickListener(this);
//		dizhi_bianhao = (EditText) findViewById(R.id.shangjiaoqingfen_dizhiyapin_edit);
		dizhi_listview = (ListView) findViewById(R.id.qf_shangjiaoqingfen_dizhiyapin_listView1);
		// 显示网络请求到的地址押品列表
		// 完成清分功能

		qingdfen_ok = (Button) findViewById(R.id.shangjiaoqingfen_qingdianwancheng);
		qingdfen_ok.setEnabled(false);
		qingdfen_ok.setBackgroundResource(R.drawable.button_gray);
		qingdfen_ok.setOnClickListener(this);
		// 更改 获取抵制押品清分数量
		ResistcollateralCoaunt = (TextView) findViewById(R.id.textViewdizhiyapincoount);
		ResistcollateralCoaunt.setText("");
		// 打印按钮第一次进入没打印时进行显示
		btn_print = (Button) findViewById(R.id.btn_print);
		btn_print.setOnClickListener(this);
		btn_print.setVisibility(View.VISIBLE);

		shangjiaoqingfen_spinner_printinfo_layout=(LinearLayout) findViewById(R.id.shangjiaoqingfen_spinner_printinfo_layout);
		shangjiaoqingfen_spinner_printinfo_layout.setOnClickListener(this);
		shangjiaoqingfen_printinfo_spinner_text=(TextView) findViewById(R.id.shangjiaoqingfen_printinfo_spinner_text)	;
		print_spinner_layout   =(LinearLayout) findViewById(R.id.print_spinner_layout);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 选择劵别
		case R.id.shangjiaoqingfen_spinner_layout:
			spinner = new MySpinner(this, spinner_layout_juanbie, spinner_text_juanbie);
			spinner.setSpinnerHeight(spinner_layout_juanbie.getHeight() * 2);
			spinner.setListXJ(this, quanbieList);
			spinner.showPopupWindow(spinner_layout_juanbie);
			spinner.setListXJ(this, quanbieList, 40);

			break;
//		case R.id.shangjiaoqingfen_spinner_layout_zhuangtai:
//			spinner = new MySpinner(this, spinner_layout_zhuangtai,
//					spinner_text_zhuangtai);
//			spinner.setSpinnerHeight(spinner_layout_zhuangtai.getHeight() * 2);
//			spinner.setList(this, str_zhuangtai);
//			spinner.showPopupWindow(spinner_layout_zhuangtai);
//			spinner.setList(this, str_zhuangtai, 40);

//			break;
		case R.id.shangjiaoqingfen_xianjin_tianjia:
			addXianJin();
			if (xianjinlist.size() > 0 && importlist.size() == 0) {
				qingdfen_ok.setEnabled(true);
				qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);
				lin_lloutgo.setVisibility(View.GONE);// 添加后隐藏
			} else if (xianjinlist.size() > 0 && zhongkonglist.size() != 0) {
				qingdfen_ok.setEnabled(true);
				qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);
				lin_lloutgo.setVisibility(View.GONE);

			}
			break;
		case R.id.shangjiaoqingfen_spinner_zhongkong_layout:
			for (int i = 0; i < lzhongkongList.size(); i++) {
				Log.e(TAG, "数据：" + lzhongkongList.get(i));
			}

			spinner = new MySpinner(this, spinner_layout_pingzheng, zhongkong_zhonglei);
			spinner.setSpinnerHeight(spinner_layout_pingzheng.getHeight() * 2);
			spinner.setListZK(this, lzhongkongList);
			spinner.showPopupWindow(spinner_layout_pingzheng);
			spinner.setListZK(this, lzhongkongList, 40);
			break;

		case R.id.shangjiaoqingfen_zhongkong_tianjia:
			addZhongKong();
			if (zhongkongList.size() > 0) {
				qingdfen_ok.setEnabled(true);
				qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);
				lin_lloutgo.setVisibility(View.GONE);
				// 现金中控同事存在
			} else if (xianjinlist.size() > 0 && zhongkonglist.size() != 0) {
				qingdfen_ok.setEnabled(true);
				qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);
				lin_lloutgo.setVisibility(View.GONE);

			}
			break;
//		case R.id.shangjiaoqingfen_dizhiyapin_tianjia:
//			// 抵质押品添加
//			addDizhiYapin();
//			break;

//		case R.id.shangjiaoqingfen_dizhiyapin_tianjia_saomiao:
//			// 开启扫描功能
//			System.out.println(isCanOpenSaomiao + "");
//			if (isCanOpenSaomiao == true) {
//				getRfid().addNotifly(dizhiSaomiaoUtil);
//				getRfid().scanOpen();
//				isCanOpenSaomiao = false;
//				System.out.println("--------关闭扫描");
//				openSaomiao.setText("关闭扫描");
//			} else {
//				getRfid().scanclose();
//				isCanOpenSaomiao = true;
//				openSaomiao.setText("开启扫描");
//				System.out.println("--------开启扫描");
//			}

//			break;

		case R.id.btn_print:// 点击打印的时候
			// 如果抵质押品的数量为0 不允许被点击
			int dizhishunumInt = Integer.parseInt(dizhishunum);
			if (dizhishunumInt > 0) {
//				getprintinfo();
				getUpdata_printinfo();
			} else {
				// 设置不可点击和改变颜色状态
				qingdfen_ok.setEnabled(false);
				qingdfen_ok.setBackgroundResource(R.drawable.button_gray);
			}
			break;
		case R.id.shangjiaoqingfen_qingdianwancheng:
			// 清点完成
			manager.getResultmsg().resultmsgHas2(this, "是否提交数据?");
			break;
		case R.id.shangjiaoqingfen_back:
			ShangJiaoQingFen_o_qf.this.finish();
			break;
			case R.id.shangjiaoqingfen_spinner_printinfo_layout://打印机spinner数据
				spinner = new MySpinner(this, shangjiaoqingfen_spinner_printinfo_layout,
						shangjiaoqingfen_printinfo_spinner_text);
				spinner.setSpinnerHeight(shangjiaoqingfen_spinner_printinfo_layout.getHeight() * 4);
				spinner.setListPrint(this, printsacelist);
				spinner.showPopupWindow(shangjiaoqingfen_spinner_printinfo_layout);
				spinner.setListPrint(this, printsacelist, 40);
		default:
			break;
		}
	}

	/**
	 * 添加现金
	 * 
	 * @author shimao
	 */
	public void addXianJin() {
		String xjcount = xianjin_count.getText().toString();
		juanbie = spinner_text_juanbie.getText().toString();
		System.out.println("juanbie--->>>>>:" + juanbie);
		if (!"".equals(juanbie) && null != juanbie) {
			// 遍历卷别集合 找出与名称对应的券别id
			for (int i = 0; i < this.quanbieList.size(); i++) {
				if (juanbie.equals(quanbieList.get(i).getQuanbieName())) {
					juanbie = quanbieList.get(i).getQuanbieId();
				}
			}

		}
		// 将残损状态名称改为残损状态id
		zhuangtai = spinner_text_zhuangtai.getText().toString();
		if (!"".equals(zhuangtai) && null != zhuangtai) {
			if (zhuangtai.equals("完整券")) {
				zhuangtai = "0";
			} else if (zhuangtai.equals("半损券")) {
				zhuangtai = "2";
			} else if (zhuangtai.equals("全损券")) {
				zhuangtai = "1";
			}
		}
		if (juanbie.equals("请选择")) {
			manager.getResultmsg().resultmsg(this, "请选择券别类型", false);
		} else {
			if (TextUtils.isEmpty(xjcount)) {
				manager.getResultmsg().resultmsg(this, "请输入数量", false);
			} else {
				if (zhuangtai.equals("请选择")) {
					manager.getResultmsg().resultmsg(this, "请选择状态", false);
				} else {
					for (int i = 0; i < xianjinlist.size(); i++) {
						if (xianjinlist.get(i).getJuanbie().equals(juanbie)
								&& xianjinlist.get(i).getZhuangtai().equals(zhuangtai)) {
							isok = i;
						}
					}

					// 根据券别名称 查询该券别的id 全额价值 残损价值
					String quanJiazhi = "";
					String canshuJiazhi = "";
					for (int i = 0; i < quanbieList.size(); i++) {
						if (juanbie.equals(quanbieList.get(i).getQuanbieId())) {
							quanJiazhi = quanbieList.get(i).getQuanJiazhi();
							canshuJiazhi = quanbieList.get(i).getCanshunJiazhi();
						}
					}

					if (isok == -1) {
						System.out.println("现金集合开始添加-->");
						xianjinlist.add(new TianJiaXianJin(juanbie, quanJiazhi, canshuJiazhi, zhuangtai, xjcount));
					} else {
						int listcount = Integer.parseInt(xianjinlist.get(isok).getCount());
						int xjshuliang = Integer.parseInt(xjcount);
						int count = listcount + xjshuliang;
						xianjinlist.get(isok).setCount(count + "");
						isok = -1;
					}

					Heji();
					xianjin_listview.setAdapter(adapter);
					new TurnListviewHeight(xianjin_listview);
					System.out.println("现金集合的数量----->");
					for (int i = 0; i < xianjinlist.size(); i++) {
						System.out.println(xianjinlist.get(i).getJuanbie() + "----->>" + xianjinlist.get(i).getCount());
					}

				}
			}
		}
	}

	/**
	 * 重空添加 SM
	 */
	@SuppressLint("WrongConstant")
	public void addZhongKong() {
		String zhongkongQishihao = pingzhengbianhao.getText().toString(); // 重空起始号
		if ("^(?![1-9]+$)[0-9A-Za-z]{8,16}$".matches(zhongkongQishihao)) { // ^\d{m,n}$
			Toast.makeText(ShangJiaoQingFen_o_qf.this, "长度不符合规范", 400).show();
			return;
		} else if (zhongkongQishihao.equals("") || zhongkongQishihao == null) {
			return;
		} else if (zhongkongQishihao.length() > 8) {
			Log.d(TAG, "数据长度不符合规范");
			Toast.makeText(ShangJiaoQingFen_o_qf.this, "长度不符合规范", 400).show();
			return;
		}
		int weiShuzi = Integer.parseInt(zhongkongQishihao); // 获取号段末尾的数字 // lchao// 获取当前输入值
		String weishuziString = getnum.getStringNum(zhongkongQishihao); // 获取号段末尾的数字
		String shouZimu = getnum.getChar(zhongkongQishihao, weishuziString); // 获取号段前面的字母
		String lianxuShu = pingzheng_haoduan.getText().toString(); // 连续数
		pingzheng = "";// 每次赋值都要放置空
		pingzheng = zhongkong_zhonglei.getText().toString();
		if (TextUtils.isEmpty(zhongkongQishihao) && TextUtils.isEmpty(lianxuShu) && pingzheng.equals("请选择")) {
			manager.getResultmsg().resultmsg(this, "重空添加信息不可为空", false);
		} else {
			int countNum = 0; // 已清点数据
			if (zhongkonglist.size() == 0) {
				// 将信息添加金号段集合
				addZhongkong(zhongkongQishihao, weiShuzi, shouZimu, pingzheng, lianxuShu);
			} else {
				int lianxushu = Integer.parseInt(lianxuShu);
				int weishuzi1 = weiShuzi + lianxushu;// 获取当前的结果值

				int size = zhongkonglist.size();
				for (int i = 0; i < size; i++) {
					TianJiaZhongKong zk = zhongkonglist.get(i);
					// 首先比较前面的号段字母是否相同
					String zimu = zk.getHaoZimu();
					if (shouZimu.equals(zimu)) {
						// 如果号段前面字母相同 检查输入的号码的范围是否已经被录入
						int shuzi = zk.getHaoShuzi(); // 取出该号段的末尾数字
						int count = zk.getXianxushu(); // 取出连续数量
						int endShuzi = shuzi + count - 1; // 计算出结尾号段
//                         编号
						if (weiShuzi >= shuzi && weiShuzi <= endShuzi) {
							// 如果输入的在起始号段在 有些号段之间 提示用户不能添加并且不再同一号段// 1 其实好端
							for (int j = 0; j < zhongkonglist.size(); j++) {
								if (pingzheng.equals(zhongkonglist.get(j).getZhongleiName())) {
									manager.getResultmsg().resultmsg(this, "凭证编号或号段重复", false);
									zhongkongQishihao = "";
									weiShuzi = 0;
									shouZimu = "";
									pingzheng = "";
									lianxuShu = "";
									endShuzi = 0;
									return;
								}
//							比较计算后的号段
							}
						} else {
							for (int k = 0; k < zhongkonglist.size(); k++) {
								int hadendnum = Integer.parseInt(zhongkonglist.get(k).getJieshuHao());// 已有输入结束
								int hadstartnum = Integer.parseInt(zhongkonglist.get(k).getKaishiHao());// 已有输入开始
//							第二次	获取的结果数大于 对比集合开始号小于结束号

								if (((weishuzi1 <= hadstartnum) && (weishuzi1 <= hadendnum))
										&& ((weiShuzi <= hadendnum) && weiShuzi <= hadstartnum)) {
									Log.d(TAG, "符合标准");
//									          1100   
								} else if (((weishuzi1 >= hadstartnum) && (weishuzi1 >= hadendnum))
										&& ((weiShuzi >= hadendnum) && weiShuzi >= hadstartnum)) {
									Log.d(TAG, "符合标准");
								} else {
									manager.getResultmsg().resultmsg(this, "凭证编号或号段重复", false);
									zhongkongQishihao = "";
									weiShuzi = 0;
									shouZimu = "";
									pingzheng = "";
									lianxuShu = "";
									endShuzi = 0;
									return;

								}
							}
						}

					}

					if (i == zhongkonglist.size() - 1) {
						// 遍历完成 未发现不合格 将号段添加
						addZhongkong(zhongkongQishihao, weiShuzi, shouZimu, pingzheng, lianxuShu);
					}
				}
			}
			for (TianJiaZhongKong zk : zhongkonglist) {
				countNum += zk.getXianxushu();
			}

			zhongkong_yiqingdian.setText("" + countNum);
			pingzheng_listview.setAdapter(zkadapter);
			new TurnListviewHeight(pingzheng_listview);
//			lianchao    添加完成后全部清空
			zhongkongQishihao = "";
			weiShuzi = 0;
			shouZimu = "";
			pingzheng = "";
			lianxuShu = "";

		}
	}

	/*
	 * 添加重空集合信息
	 */
	private void addZhongkong(String zhongkongQishihao, int weiShuzi, String shouZimu, String pingzheng,
			String lianxuShu) {

		Log.d(TAG, "参数 六个====" + zhongkongQishihao + weiShuzi + shouZimu + pingzheng + lianxuShu);
		int num = Integer.parseInt(lianxuShu.trim()); // 将连续数量转换成int
		String zhongkongId = "";
		String pingzhengzhongkongname = pingzheng.substring(pingzheng.indexOf(",") + 1);
		System.out.println("!!!!!!!" + pingzhengzhongkongname);
		pingzheng = pingzhengzhongkongname;// 截取逗号后字符串
		for (ZhongkongXinxi zk : zhongkongList) {
			if (zk.getZhongkongName().equals(pingzheng)) {
				zhongkongId = zk.getZhongkongId();
			}
		}

		String jieshuHao = getnum.jisuanweiHao(zhongkongQishihao, num);
		System.out.println("结束号段为");
		// 创建 添加重空对象
		TianJiaZhongKong tjzk = new TianJiaZhongKong(zhongkongQishihao, shouZimu, weiShuzi, zhongkongId, num, pingzheng,
				jieshuHao);
		// 将对象添加进入集合
		Log.d(TAG, "tjzk===" + tjzk);
		this.zhongkonglist.add(tjzk);
		Log.d(TAG, "=zhongkonglist.size===" + zhongkonglist.size());
	}

	/**
	 * 抵制押品添加 SM
	 */
	public void addDizhiYapin() {
		String dz_bianhao = dizhi_bianhao.getText().toString();
		if (TextUtils.isEmpty(dz_bianhao)) {
			// manager.getResultmsg().resultmsg(this, "请输入抵质押品编号", false);
		} else {
			for (int i = 0; i < dizhilist.size(); i++) {
				if (dizhilist.get(i).equals(dz_bianhao)) {
					dzok = i;
				} else {
					dzok = -1;
				}
			}
			if (dzok == -1) {
				dizhilist.add(dz_bianhao);
			} else {
				manager.getResultmsg().resultmsg(this, "抵质押品编号重复", false);
			}
			// dzadapter.notifyDataSetChanged();
			dizhi_listview.setAdapter(dzadapter);
			new TurnListviewHeight(dizhi_listview);
//			dizhi_heji.setText("" + dizhilist.size());
		}
	}

	/**
	 * 合计
	 */
	public void Heji() {

		heji_xianjin = 0;
		if (xianjinlist.size() != 0) {

			for (int i = 0; i < xianjinlist.size(); i++) {
				int count = Integer.parseInt(xianjinlist.get(i).getCount().trim());
				double quanJiazhi = Double.parseDouble(xianjinlist.get(i).getQuanJiazhi().trim());
				double canshuJiazhi = Double.parseDouble(xianjinlist.get(i).getCanshunJiazhi().trim());
				int zhuangtai = Integer.parseInt(xianjinlist.get(i).getZhuangtai().trim());

				switch (zhuangtai) {
				case 0: // 全额
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 1: // 全损
					heji_xianjin += (double) (quanJiazhi * count);
					break;
				case 2: // 半损
					heji_xianjin += (double) (canshuJiazhi * count);
					break;
				}
			}
		}
		xianjin_heji.setText(new NumFormat().format(heji_xianjin + ""));
	}

	class XianJinAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(ShangJiaoQingFen_o_qf.this);
		ViewHodler view;

		@Override
		public int getCount() {
			return xianjinlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return xianjinlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = lf.inflate(R.layout.adapter_shangjiaoqingfen_tianjia, null);
				view = new ViewHodler();
				view.juanbie = (TextView) arg1.findViewById(R.id.sj_qf_title_tv1);
				view.zhuangtai = (TextView) arg1.findViewById(R.id.sj_qf_title_tv2);
				view.shuliang = (TextView) arg1.findViewById(R.id.sj_qf_title_tv3);
				view.shanchu = (Button) arg1.findViewById(R.id.sj_qf_title_button);
				arg1.setTag(view);
			} else {
				view = (ViewHodler) arg1.getTag();
			}
			view.juanbie.setText(xianjinlist.get(position).getJuanbie());
			view.zhuangtai.setText(xianjinlist.get(position).getZhuangtai());
			view.shuliang.setText(xianjinlist.get(position).getCount());
			view.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					xianjinlist.remove(position);
					delete = "2"; // 现金被删除过
					Heji();
					adapter.notifyDataSetChanged();
					new TurnListviewHeight(xianjin_listview);
					if (xianjinlist.size() > 0 && zhongkonglist.size() > 0) {
						qingdfen_ok.setEnabled(true);
						qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);
						lin_lloutgo.setVisibility(View.GONE);

					} else if (xianjinlist.size() == 0) {
						qingdfen_ok.setEnabled(false);
						qingdfen_ok.setBackgroundResource(R.drawable.button_gray);
						lin_lloutgo.setVisibility(View.GONE);

						Log.e("数据不匹配", "数据不匹配");

					}
				}
			});
			return arg1;
		}
	}

	public static class ViewHodler {
		TextView juanbie, zhuangtai, shuliang;
		Button shanchu;
	}

	class ZhongKongAdapter extends BaseAdapter {
		LayoutInflater lf = LayoutInflater.from(ShangJiaoQingFen_o_qf.this);
		ZhongKongHolder view;

		@Override
		public int getCount() {
			return zhongkonglist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return zhongkonglist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = lf.inflate(R.layout.adapter_shangjiaoqingfen_tianjia2, null);
				view = new ZhongKongHolder();
				view.bianhao = (TextView) arg1.findViewById(R.id.sj_qf_title_zk_tv1);
				view.zhonglei = (TextView) arg1.findViewById(R.id.sj_qf_title_zk_tv2);
				view.haoduan = (TextView) arg1.findViewById(R.id.sj_qf_title_zk_tv3);
				view.shanchu = (Button) arg1.findViewById(R.id.sj_qf_title_zk_button);
				arg1.setTag(view);
			} else {
				view = (ZhongKongHolder) arg1.getTag();
			}
			view.bianhao.setText(zhongkonglist.get(position).getKaishiHao());
			view.zhonglei.setText(zhongkonglist.get(position).getZhongleiId());
			view.haoduan.setText(zhongkonglist.get(position).getXianxushu() + "");
			view.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					zhongkonglist.remove(position);

					zhongkong_yiqingdian.setText("" + zhongkonglist.size());
					zkadapter.notifyDataSetChanged();
					new TurnListviewHeight(pingzheng_listview);
					if (xianjinlist.size() > 0 && zhongkonglist.size() > 0) {
						qingdfen_ok.setEnabled(true);
						qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);

					} else if (zhongkonglist.size() == 0) {
						qingdfen_ok.setEnabled(false);
						qingdfen_ok.setBackgroundResource(R.drawable.button_gray);

					}
				}
			});
			return arg1;
		}

	}

	public static class ZhongKongHolder {
		TextView bianhao, zhonglei, haoduan;
		Button shanchu;
	};

	class DiZhiAdapter extends BaseAdapter {
		DiZhiHolder view;
		LayoutInflater lf = LayoutInflater.from(ShangJiaoQingFen_o_qf.this);

		@Override
		public int getCount() {
			return dizhilist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dizhilist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				view = new DiZhiHolder();
				arg1 = lf.inflate(R.layout.adapter_shangjiaoqingfen_tianjia3, null);
				view.bianhao = (TextView) arg1.findViewById(R.id.adapter_shangjiaoqingfen_tianjia_dizhiyapin);
//				view.shanchu = (Button) arg1
//						.findViewById(R.id.adapter_shangjiaoqingfen_shanchu_dizhiyapin);
				arg1.setTag(view);
			} else {
				view = (DiZhiHolder) arg1.getTag();
			}
			view.bianhao.setText(dizhilist.get(position));
//			view.shanchu.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					dizhilist.remove(position);
//					dzadapter.notifyDataSetChanged();
//					dizhi_listview.setAdapter(dzadapter);
//					new TurnListviewHeight(dizhi_listview);
//					dizhi_heji.setText("" + dizhilist.size());
//				}
//			});
			return arg1;
		}

	}

	public static class DiZhiHolder {
		TextView bianhao;
		Button shanchu;
	}

	/**
	 * 获取券别数据
	 */
	public void getSpinnerData() {
		manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
		// 获取券别信息
		Thread thread = new Thread(new GetQuanbieXinxi());
		thread.start();
	}

	/**
	 * 获取重空
	 */
	private void getZhongkong() {
		manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
		// 获取重空信息
		Thread thread = new Thread(new GetZhongkong());
		thread.start();
	}

	/**
	 * 从服务器获取券别信息
	 * 
	 * @author yuyunheng
	 */
	private class GetQuanbieXinxi implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = create.obtainMessage();
			try {
				QingfenRenwuService service = new QingfenRenwuService();
				String quanbieXinxi = service.getQuanbieList();
				if (quanbieXinxi != null && !quanbieXinxi.equals("")) {
					msg.obj = quanbieXinxi;
					msg.what = 1;
				} else {
					msg.what = 3; // 获取券别信息失败
				}
			} catch (SocketTimeoutException ee) {
				// TODO: handle exception
				msg.what = 2;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 3; // 获取券别信息失败
			}

			create.sendMessage(msg); // 发送消息
		}

	}

	/**
	 * 获取重空信息
	 * 
	 * @author yuyunheng
	 * 
	 */
	private class GetZhongkong implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = create.obtainMessage();
			try {
				QingfenRenwuService service = new QingfenRenwuService();
				String zhongkongxinxi = service.getZhongkongList();
				if (zhongkongxinxi != null && !zhongkongxinxi.equals("")) {
					msg.obj = zhongkongxinxi;
					msg.what = 4;
				} else {
					msg.what = 6; // 获取券别信息失败
				}
			} catch (SocketTimeoutException ee) {
				// TODO: handle exception
				msg.what = 5;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 6; // 获取券别信息失败
			}

			create.sendMessage(msg); // 发送消息
		}

	}
	/***
	 * 新增
	 * 获取打印机代码类型
	 * 20200707
	 *
	 */
	private class GetPrintInfo implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = create.obtainMessage();
			try {
				QingfenRenwuService service = new QingfenRenwuService();
				String PrintInfo = service.getPrinInfo(userNumber);
				if (null!=PrintInfo && !PrintInfo.equals("")) {
					Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<ShangJiaoQingFen_o_qf_Print_Entity>>() {
					}.getType();

					List<ShangJiaoQingFen_o_qf_Print_Entity> listPrint = gson.fromJson(PrintInfo,
							type);

					printsacelist.clear();
//					printsacelist = listPrint;
					printsacelistmap.clear();
					for (int i = 0; i < listPrint.size(); i++) {
//						Map<String,String> printmap=new HashMap<String, String>();
//						printmap.put(listPrint.get(i).getCode(), listPrint.get(i).getId());
						printmapnetrestult.put(listPrint.get(i).getCode(), listPrint.get(i).getId());
//						printsacelistmap.add(printmap);
						printsacelist.add(listPrint.get(i).getCode());
					}
//					msg.obj = PrintInfo;
					if(printsacelist.size()>0){
						Log.d(TAG,"获取数据后我什么也没做等着数据放到组件上");
//						msg.what = 7; //
					}

				} else {
					msg.what = 8; //
				}
			} catch (SocketTimeoutException ee) {
				// TODO: handle exception
				msg.what = 5;
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 8; // 获取券别信息失败
			}

			create.sendMessage(msg); // 发送消息
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			ShangJiaoQingFen_o_qf.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 提交数据
	 * 
	 */
	private void submit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("调用存储过程的方法------->开始");
				for (int i = 0; i < xianjinlist.size(); i++) {
					System.out.println("数量------->>>>:" + xianjinlist.get(i).getCount());
				}
				togerDate();// 拼接数据
				try {
					System.out.println("任务单号:" + orderNum);
					System.out.println("清分员帐号:" + GApplication.user.getLoginUserName());
					System.out.println("清分员帐号:" + GApplication.user.getLoginUserName());
					System.out.println("现金-->" + xianjingMsg);
					System.out.println("------------------------------数据上传");
					System.out.println("ShangJiaoQingFen_o_qf:" + GApplication.user);
					/*
					 * revised by zhangxuewei 后台传值name 改为ID 是否需要符合
					 */
					boolean isOk = new QingfenRenwuService().submitShangjiao(orderNum, peisongId,
							(GApplication.getApplication().app_hash.get("login_username")).toString(), xianjingMsg,
							zhongkongSubmit, dizhiMsg);
					if (isOk) {
						System.out.println("------------------------------上传成功");
						okHandle.sendEmptyMessage(0);
					} else {
						okHandle.sendEmptyMessage(2);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					System.out.println("上传超时");
					timeoutHandle.sendEmptyMessage(00);
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("没有任务");
					timeoutHandle.sendEmptyMessage(12);

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("上传失败");
					timeoutHandle.sendEmptyMessage(10);
				}
			}
		}).start();
	}

	/****
	 * 单独提交抵质押品的数据 100029 王姐
	 * 这里只提交抵质押品其它几项 不提交需要判断
	 */
	public void submitByDZcollateralHandOverClearResultAndCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("调用存储过程的方法------->开始");
				togerDate();// 拼接数据
				try {
					System.out.println("任务单号:" + orderNum);
					System.out.println("清分员帐号:" + GApplication.user.getLoginUserName());
					System.out.println("清分员帐号:" + GApplication.user.getLoginUserName());
					System.out.println("现金-->" + xianjingMsg);
					System.out.println("------------------------------数据上传");
					System.out.println("ShangJiaoQingFen_o_qf:" + GApplication.user);
					/*
					 * revised by zhangxuewei 后台传值name 改为ID 是否需要符合1
					 */
					boolean isOk = new QingfenRenwuService().collateralHandOverClearResultAndCheck(orderNum, peisongId,
							(GApplication.getApplication().app_hash.get("login_username")).toString(), newdizhiMSg);
					if (isOk) {
						System.out.println("------------------------------上传成功");
						okHandle.sendEmptyMessage(0);
					} else {
						okHandle.sendEmptyMessage(2);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					System.out.println("上传超时");
					timeoutHandle.sendEmptyMessage(00);
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("没有任务");
					timeoutHandle.sendEmptyMessage(12);

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("上传失败");
					timeoutHandle.sendEmptyMessage(10);
				}
			}
		}).start();

	}





	/**
	 * 获取清分登记信息
	 */
	private void getDate() {
		manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String param;
				List<String> dizhishulist = new ArrayList<String>();
				Log.d(TAG, "!!!!！！！！！！！！！！！！！！！！！");
				try {
					Log.d(TAG, "!!!!" + peisongId);
//					shuliang:|canshun:|quanbieId:|quanbie:1330
					param = new QingfenRenwuService().isCanSubmitlc(peisongId);
					shangjiaoRenwu = Table.doParse(param);
					table3 = Table.doParse(param);
//					 中空
//					 shuliang:|canshun:|quanbieId:|quanbie:
//						 zkJieshu:7400400|zkKaishi:7400101|leibieName:农商行转账支票|leibie:001
//						 dizhishu:0|daihao:
					// 抵制押品的数据
//					[quanbie:|canshun:|shuliang:|quanbieId:|, leibie:|leibieName:|zkKaishi:|zkJieshu:|, daihao:|dizhishu:3|]
					// 现金的数据
//					[quanbie:百元券别|canshun:0|shuliang:500|quanbieId:0100A|, leibie:|leibieName:|zkKaishi:|zkJieshu:|, daihao:|dizhishu:0|]

					for (int i = 0; i < table3.length; i++) {
						dizhishulist = table3[2].get("dizhishu").getValues();
						cash = table3[0].get("shuliang").getValues();
						;
						Commoditycollaterallist = table3[2].get("dizhishu").getValues();
						;
						importlist = table3[1].get("zkKaishi").getValues();// 重要的中控凭证
						dizhilist = table3[2].get("daihao").getValues();

					}

					Log.e(TAG, "***" + cash.size() + Commoditycollaterallist.size() + importlist.size()
							+ dizhilist.size());
					for (int i = 0; i < dizhishulist.size(); i++) {
						dizhishunum = dizhishulist.get(i);// 获取抵制押品的数量进行判断
						Log.d(TAG, "====***" + dizhishunum);
					}

					// 将集合中数量和 集合拿出来放到页面显示
//					dizhilistnumlist.addAll(shangjiaoRenwu[2]);
					shangjiaoMark = shangjiaoRenwu;

					okHandle.sendEmptyMessage(1);

					// 第二次进入页面时获取的数据
					if (dizhilist.size() > 0) {
						okHandle.sendEmptyMessage(4);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(01);
				} catch (NullPointerException e) {
					e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
					timeoutHandle.sendEmptyMessage(11);
				}
			}
		}).start();
	}

	/**
	 * 判断当前清分的数据是否符合系统上的数据
	 * 
	 * @param RenwuData
	 * @param xianjinlistNew
	 * @param zhongkonglistNew
	 * @param dizhilistNew
	 * @return
	 */
	private boolean isEquals(Table[] RenwuData, List<TianJiaXianJin> xianjinlistNew,
			List<TianJiaZhongKong> zhongkonglistNew, List<String> dizhilistNew) {
		System.out.println("进入比对方法");
		System.out.println("现金清点集合长度：" + xianjinlistNew.size());
		System.out.println("重空清点集合长度：" + zhongkonglistNew.size());
		System.out.println("抵质押品清点集合长度：" + dizhilistNew.size());

		// 判断三种情况 1 有现金 没有抵质押品 2没现金有地址押品 3 现金和抵制押品同时存在 4中控自己存在 5中控和现金6中控和抵制和现金
		if (xianjinlistNew.size() > 0) {
			// 解析数据,获得现金明细,判断现金券别,残损,数量是否相同.
			List<String> x_quanbie = RenwuData[0].get("quanbieId").getValues();
			System.out.println("登记券别集合长度：" + x_quanbie.size());
			List<String> x_canshun = RenwuData[0].get("canshun").getValues();
			System.out.println("登记残损集合长度：" + x_canshun.size());
			List<String> x_shuliang = RenwuData[0].get("shuliang").getValues();
			System.out.println("登记数量集合长度：" + x_shuliang.size());
			if (x_quanbie.size() != xianjinlist.size()) {
				System.out.println("现金集合长度与登记集合长度不匹配");
				return false;
			} else {
				for (int i = 0; i < xianjinlist.size(); i++) {
					boolean flag1 = false;
					for (int j = 0; j < x_quanbie.size(); j++) {
						// 如果当清点现金的券别等于登记信息的券别，并且清点现金的残损状态等于登记信息的残损状态
						if (xianjinlist.get(i).getJuanbie().equals(x_quanbie.get(j))
//								&& xianjinlist.get(i).getZhuangtai()
//										.equals(x_canshun.get(j))) {
						) {// 修改状态lianc2019016
							flag1 = true; // 如果找到了该卷别和该状态 把flag1设置为true
							if (!(xianjinlist.get(i).getCount().equals(x_shuliang.get(j)))) { // 判断金钱的数量是否与等级的想匹配
								System.out.println("现金清点不匹配1");
								return false;
							}
						}
					}
					if (!flag1) {
						// 如果没有找到那状态和该券别 表示清点数据不相符
						System.out.println("现金清点不匹配2");
						return false;
					}
				}

			}
		}
		// 新输入的集合数据 比对的思路 先比较类型 在比较号段的区间
		if (zhongkonglistNew.size() > 0) { // 网络请求到数据
			// 解析数据,获得重空凭证明细,判断重空类型,数量是否相同. 网络请求穿的集合参数shangjiaoRenwu
//			283
			List<String> z_leibie = RenwuData[1].get("leibie").getValues();
//			个人网银二代
			List<String> z_leibieName = RenwuData[1].get("leibieName").getValues();
//			100001[100200]// 上一个的集合
			List<String> z_beginHao = RenwuData[1].get("zkKaishi").getValues();
//			100100[100200]上一个
			List<String> z_jieshu = RenwuData[1].get("zkJieshu").getValues();

			if (z_leibie.size() != zhongkonglist.size()) {
				System.out.println("重空集合长度与登记集合长度不匹配");
				return false;
			} else {

				// 解析数据,获得抵质押品明细,判断抵质编号,数量是否相同.

				for (int i = 0; i < zhongkonglist.size(); i++) {
					TianJiaZhongKong tj = zhongkonglist.get(i);
					for (int j = 0; j < z_leibie.size(); j++) {
						String typeId = tj.getZhongleiId(); // 取出录入的 类别id
						String bg = tj.getKaishiHao(); // 取出该类别的开始号段
						// String ed = tj.getHaoZimu(); // 取出结束号段
						String ed = tj.getJieshuHao();

						String djtypeId = z_leibie.get(j);// 取出登记的 类别id
						String djbg = z_beginHao.get(j);// 取出登记的 类别开始号段
						String djed = z_jieshu.get(j); // 取出登记的结束号段
//						1 判断网络获取数据和本地数据做对比2 第二次输入的时候拼接字符串 和输入的做对比（清分结果不正常时使用）两次结果一致时提交数据
//						if (typeId.equals(djtypeId) ) {
						Log.e(TAG, "typeId" + typeId + "====--====" + "djtypeId" + djtypeId);
						Log.e(TAG, "bg" + bg + "=====-------===" + djbg + "djbg");
						int s = Integer.parseInt(ed);
						int s1 = Integer.parseInt(bg);
						int s2 = Integer.parseInt(djed);
						int s3 = Integer.parseInt(djbg);
						Log.e(TAG, "djed::::" + (djed));
						Log.e(TAG, "result00000::::" + (s1 - s));
						Log.e(TAG, "result00000::::" + (s1 - s));
						Log.e(TAG, "result1111::::" + (s3 - s2));
						Log.e(TAG, "ed" + ed + "==========----===" + ed + "ed");

						if (typeId.equals(djtypeId) && bg.equals(djbg)) {
//							// 如果类别相同 开始号段也相同 则比较结尾号段
							if (!ed.equals(djed)) {
								System.out.println("尾号不相同");
								return false;
							} else {
								break;
							}
						}
//
						if (j == z_leibie.size() - 1) {
							System.out.println("类别与开始号匹配不成功");
							return false;
						}

					}
				}
			}

		}
		if (zhongkonglistNew.size() > 0 && xianjinlistNew.size() > 0) {
			// 解析数据,获得重空凭证明细,判断重空类型,数量是否相同.
			List<String> z_leibie = RenwuData[1].get("leibie").getValues();
			List<String> z_leibieName = RenwuData[1].get("leibieName").getValues();
			List<String> z_beginHao = RenwuData[1].get("zkKaishi").getValues();
			List<String> z_jieshu = RenwuData[1].get("zkJieshu").getValues();

			if (z_leibie.size() != zhongkonglist.size()) {
				System.out.println("重空集合长度与登记集合长度不匹配");
				return false;
			} else {
				// 解析数据,获得抵质押品明细,判断抵质编号,数量是否相同.

				for (int i = 0; i < zhongkonglist.size(); i++) {
					TianJiaZhongKong tj = zhongkonglist.get(i);
					for (int j = 0; j < z_leibie.size(); j++) {
						String typeId = tj.getZhongleiId(); // 取出录入的 类别id
						String bg = tj.getKaishiHao(); // 取出该类别的开始号段
//									String ed = tj.getHaoZimu(); // 取出结束号段
						String ed = tj.getJieshuHao();
						String djtypeId = z_leibie.get(j);// 取出登记的 类别id
						String djbg = z_beginHao.get(j);// 取出登记的 类别开始号段
						String djed = z_jieshu.get(j); // 取出登记的结束号段
						if (typeId.equals(djtypeId) && bg.equals(djbg)) {
							// 如果类别相同 开始号段也相同 则比较结尾号段
							if (!ed.equals(djed)) {
								System.out.println("尾号不相同");
								return false;
							} else {
								break;
							}
						}

						if (j == z_leibie.size() - 1) {
							System.out.println("类别与开始号匹配不成功");
							return false;
						}

					}
				}
			}

			// 解析数据,获得现金明细,判断现金券别,残损,数量是否相同.
			List<String> x_quanbie = RenwuData[0].get("quanbieId").getValues();
			System.out.println("登记券别集合长度：" + x_quanbie.size());
			List<String> x_canshun = RenwuData[0].get("canshun").getValues();
			System.out.println("登记残损集合长度：" + x_canshun.size());
			List<String> x_shuliang = RenwuData[0].get("shuliang").getValues();
			System.out.println("登记数量集合长度：" + x_shuliang.size());
			if (x_quanbie.size() != xianjinlist.size()) {
				System.out.println("现金集合长度与登记集合长度不匹配");
				return false;
			} else {
				for (int i = 0; i < xianjinlist.size(); i++) {
					boolean flag1 = false;
					for (int j = 0; j < x_quanbie.size(); j++) {
						// 如果当清点现金的券别等于登记信息的券别，并且清点现金的残损状态等于登记信息的残损状态
						if (xianjinlist.get(i).getJuanbie().equals(x_quanbie.get(j))
								&& xianjinlist.get(i).getZhuangtai().equals(x_canshun.get(j))) {
							flag1 = true; // 如果找到了该卷别和该状态 把flag1设置为true
							if (!(xianjinlist.get(i).getCount().equals(x_shuliang.get(j)))) { // 判断金钱的数量是否与等级的想匹配
								System.out.println("现金清点不匹配1");
								return false;
							}
						}
					}
					if (!flag1) {
						// 如果没有找到那状态和该券别 表示清点数据不相符
						System.out.println("现金清点不匹配2");
						return false;
					}
				}

			}
			return true;
		}

		return true;
	}

	private void togerDate() {
		for (int i = 0; i < xianjinlist.size(); i++) {
			System.out.println("数量:" + xianjinlist.get(i).getCount());
		}

		// 拼接现金字符串
		StringBuffer sbX_quanbie = new StringBuffer();
		StringBuffer sbX_cansun = new StringBuffer();
		StringBuffer sbX_count = new StringBuffer();
		sbX_quanbie.append("quanbieId:");
		sbX_cansun.append("canshun:");
		sbX_count.append("shuliang:");
		int flag = 0;
		for (TianJiaXianJin xianjin : xianjinlist) {
			flag++;
			sbX_quanbie.append(xianjin.getJuanbie());
			sbX_cansun.append(xianjin.getZhuangtai());
			sbX_count.append(xianjin.getCount());
			System.out.println("现金数量count--->" + xianjin.getCount());
			if (flag < xianjinlist.size()) {
				sbX_quanbie.append(BianyiType.douhao);
				sbX_cansun.append(BianyiType.douhao);
				sbX_count.append(BianyiType.douhao);
			}
		}
		// 拼接重空凭证字符串
		StringBuffer sbZ_leibie = new StringBuffer();
		StringBuffer sbZ_leibieSubmit = new StringBuffer();
		StringBuffer sbZ_leibieName = new StringBuffer();
		StringBuffer sbZ_kaishishu = new StringBuffer();
		StringBuffer sbZ_jieshushu = new StringBuffer();
		StringBuffer sbZ_bianhao = new StringBuffer();
		StringBuffer sbZ_shuliang = new StringBuffer();

		sbZ_leibie.append("leibie:"); // 本地对比数据字符串
		sbZ_leibieName.append("leibieName:");
		sbZ_kaishishu.append("zkKaishi:");
		sbZ_jieshushu.append("zkJieshu:");

		sbZ_leibieSubmit.append("zhongkongtype:");// 上传服务器的字符
		sbZ_bianhao.append("bianhao:");
		sbZ_shuliang.append("shuliang:");
		flag = 0;
//		循环添加，
		for (TianJiaZhongKong zhongkong : zhongkonglist) {
			flag++;
			sbZ_leibie.append(zhongkong.getZhongleiId());
			sbZ_leibieName.append(zhongkong.getZhongleiName());
			sbZ_kaishishu.append(zhongkong.getKaishiHao());
			sbZ_jieshushu.append(zhongkong.getJieshuHao());
			sbZ_leibieSubmit.append(zhongkong.getZhongleiId());
			sbZ_bianhao.append(zhongkong.getKaishiHao());
			sbZ_shuliang.append(zhongkong.getXianxushu());
			if (flag < zhongkonglist.size()) {
				sbZ_leibie.append(BianyiType.douhao);
				sbZ_bianhao.append(BianyiType.douhao);
				sbZ_shuliang.append(BianyiType.douhao);

				sbZ_leibieSubmit.append(BianyiType.douhao);// lchao 超出长度8位后台插入失败
				sbZ_kaishishu.append(BianyiType.douhao);
				sbZ_jieshushu.append(BianyiType.douhao);

			}
		}
		// 拼接抵质押品字符串
		StringBuffer sbD = new StringBuffer();
		sbD.append("dizhibianhao:");
		flag = 0;
		for (String dizhi : dizhilist) {
			flag++;
			sbD.append(dizhi);
			if (flag < dizhilist.size()) {
				sbD.append(BianyiType.xiahuaxian);
			}
		}
		//  这里单纯复制上面内容预防修修改
		StringBuffer sbD2 = new StringBuffer();
//		sbD2.append("dizhibianhao:");
		flag = 0;
		for (String dizhi : dizhilist) {
			flag++;
			sbD2.append(dizhi);
			if (flag < dizhilist.size()) {
				sbD2.append(BianyiType.xiahuaxian);
			}
		}
		newdizhiMSg="asdqwbianhao:"+sbD2;
		xianjingMsg = sbX_quanbie + BianyiType.fenge + sbX_cansun + BianyiType.fenge + sbX_count;// 现金msg
		zhongkongMsg = sbZ_leibie + BianyiType.fenge + sbZ_leibieName + BianyiType.fenge + sbZ_kaishishu
				+ BianyiType.fenge + sbZ_jieshushu; // 重空msg
		zhongkongSubmit = sbZ_leibieSubmit + BianyiType.fenge + sbZ_bianhao + BianyiType.fenge + sbZ_shuliang;// 重空凭证提交msg

//		zhongkongMsg = sbZ_leibieSubmit+ BianyiType.fenge + sbZ_leibieName
//				+ BianyiType.fenge + sbZ_kaishishu + BianyiType.fenge
//				+ sbZ_jieshushu; // 重空msg
//		zhongkongSubmit =sbZ_leibie + BianyiType.fenge + sbZ_bianhao
//				+ BianyiType.fenge + sbZ_shuliang;// 重空凭证提交msg
		dizhiMsg = "dizhishu:" + dizhilist.size() + BianyiType.fenge + sbD;// 抵质押品msg
		System.out.println("我是现金:" + xianjingMsg);
		System.out.println("我是中控:" + zhongkongMsg);
		System.out.println("我是地址押品:" + dizhiMsg);
		String str = xianjingMsg + BianyiType.jianduan + zhongkongMsg + BianyiType.jianduan + dizhiMsg;
		String subStr = xianjingMsg + BianyiType.jianduan + zhongkongSubmit + BianyiType.jianduan + dizhiMsg;
		shangjiaoMark = Table.doParse(str);
		subimtMark = Table.doParse(subStr);

		System.out.println("比对信息为：--------------------" + shangjiaoMark);
		System.out.println("比提交信息为：--------------------" + subimtMark);
	}

	/**
	 * 判断数组里面是否含有某个值
	 * 
	 * @param arr         数组
	 * @param targetValue 要查询的值
	 * @return 有-返回true,没有-返回false;
	 */
	private boolean useLoop(String[] arr, String targetValue) {
		for (String s : arr) {
			if (s.equals(targetValue))
				return true;
		}
		return false;
	}

	private Handler okHandle = new Handler() {// 数据上传成功handler
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				manager.getRuning().remove();
				System.out.println("------------------------------页面跳转");
				if(dizhishunum.equals("0") ){
				Skip.skip(ShangJiaoQingFen_o_qf.this, QingFenJinDu_qf.class, null, 0);
				}else {
					Skip.skip(ShangJiaoQingFen_o_qf.this, SelectTaskByCollateralActivity.class, null, 0);
					//2022.1.25 这里添加后跳转抵质数大于且不为0时
				}
			}
			if (msg.what == 1) {
				getPritInfoHander();//  每次进入都会获取到打印机的数据
				load();
				alllinear.setVisibility(View.VISIBLE); // 加载时显示
				ResistcollateralCoaunt.setText(dizhishunum);// 设置抵制押品数量显示
//				 如果 现金=0  抵质！=0 中控=0 三个种类
				Log.e("TAG", "***" + cash.size() + Commoditycollaterallist.size() + importlist.size() + "");
				if (cash.size() == 0 && (!dizhishunum.equals("0")) && importlist.size() == 0) {
					lin_cash.setVisibility(View.GONE);
					lin_impotan.setVisibility(View.GONE);
					btn_print.setEnabled(true);
					btn_print.setVisibility(View.VISIBLE);// 通过网络请求判断布局是显示还是隐藏
//				如果 现金=0  抵质=0 中！=0
				} else if (cash.size() == 0 && dizhishunum.equals("0") && importlist.size() > 0) {
					lin_cash.setVisibility(View.GONE);
					lin_impotan.setVisibility(View.VISIBLE);
					lin_lloutgo.setVisibility(View.GONE);
					btn_print.setEnabled(false);
					btn_print.setVisibility(btn_print.GONE);
					print_spinner_layout.setVisibility(print_spinner_layout.GONE);
//					现金！=0 抵制=0 中控=0
				} else if (cash.size() > 0 && dizhishunum.equals("0") && importlist.size() == 0) {
					lin_lloutgo.setVisibility(View.GONE);
					lin_cash.setVisibility(View.VISIBLE);
					lin_impotan.setVisibility(View.GONE);
					btn_print.setEnabled(true);
					btn_print.setVisibility(View.GONE);
					print_spinner_layout.setVisibility(print_spinner_layout.GONE);
//					如果 现金！=0  抵质=0 中!=0	
				} else if (cash.size() >= 0 && dizhishunum.equals("0") && importlist.size() > 0) {
					lin_lloutgo.setVisibility(View.GONE);
					lin_cash.setVisibility(View.VISIBLE);
					lin_impotan.setVisibility(View.VISIBLE);
					btn_print.setEnabled(false);
					btn_print.setVisibility(View.GONE);
					print_spinner_layout.setVisibility(print_spinner_layout.GONE);
//					如果 现金=0  抵质！0 中！0
				} else if (cash.size() == 0 || cash == null && !dizhishunum.equals("0") && importlist.size() > 0) {
					lin_lloutgo.setVisibility(View.GONE);
					lin_cash.setVisibility(View.GONE);
					lin_impotan.setVisibility(View.VISIBLE);
					btn_print.setEnabled(true);
					btn_print.setVisibility(View.VISIBLE);
					print_spinner_layout.setVisibility(print_spinner_layout.VISIBLE);
//					抵制为0  现金！=0  中空白！=！0
				} else if (dizhishunum.equals("0") && cash.size() > 0 && importlist.size() > 0) {
					btn_print.setEnabled(true);
					lin_lloutgo.setVisibility(View.GONE);
					lin_cash.setVisibility(View.VISIBLE);
					btn_print.setVisibility(View.GONE);
					lin_impotan.setVisibility(View.VISIBLE);
					print_spinner_layout.setVisibility(print_spinner_layout.GONE);
					// 抵质押品和现金》0 中控=0
				} else if ((!dizhishunum.equals("0")) && cash.size() > 0 && importlist.size() == 0) {
					btn_print.setEnabled(true);
					lin_cash.setVisibility(View.VISIBLE);
					btn_print.setVisibility(View.VISIBLE);
					lin_impotan.setVisibility(View.GONE);
					lin_lloutgo.setVisibility(View.VISIBLE);
					print_spinner_layout.setVisibility(print_spinner_layout.VISIBLE);
//			全部是0
				} else if (cash.size() == 0 && (dizhishunum.equals(0)) && importlist.size() == 0) {
					lin_lloutgo.setVisibility(View.GONE);
					btn_print.setVisibility(View.GONE);
					lin_cash.setVisibility(View.GONE);
					btn_print.setEnabled(false);
					print_spinner_layout.setVisibility(print_spinner_layout.GONE);
//					 做一次刷新
//					全显示
				} else if (cash.size() > 0 && (!dizhishunum.equals(0)) && importlist.size() > 0) {
					lin_cash.setVisibility(View.VISIBLE);
					btn_print.setEnabled(true);
					btn_print.setVisibility(View.VISIBLE);
					lin_lloutgo.setVisibility(View.VISIBLE);
					lin_impotan.setVisibility(View.VISIBLE);
					print_spinner_layout.setVisibility(print_spinner_layout.VISIBLE);
				}

				getSpinnerData(); // 获取成功以后继续 获取券别信息

				System.out.println("你好");
			}
			if (msg.what == 2) {
				alllinear.setVisibility(View.VISIBLE);
				System.out.println("aaaaaaaaaaaaaaaaaaaaaa == 2");
				manager.getRuning().remove();
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "提交失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "提交中...");

						if(cash.size()>0||importlist.size()>0){
								submit();

						}else if(!dizhishunum.equals("0")){
							submitByDZcollateralHandOverClearResultAndCheck();
						}
					}
				});
			}
			// 抵质押品打印时提交数据内容
			if (msg.what == 3) {
				load();
				alllinear.setVisibility(View.VISIBLE);
				dizhi_listview.setAdapter(dzadapter);
				new TurnListviewHeight(dizhi_listview);
				// 判断抵制押品数量》0 的操作 抵制押品的集合中有数据

//					点击了打印的按钮   打印完成后
				if (Commoditycollaterallist.size() > 0 && dizhilist.size() > 0) {
					btn_print.setEnabled(false);
					btn_print.setVisibility(View.GONE);
					qingdfen_ok.setEnabled(true);
					qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);

//						    抵制押品数量有只但是显示集合为0  点击打印时  第一次进入
				} else if (Commoditycollaterallist.size() > 0 && dizhilist.size() == 0) {
					qingdfen_ok.setEnabled(false);
					qingdfen_ok.setBackgroundResource(R.drawable.button_gray);
					btn_print.setEnabled(true);
					btn_print.setVisibility(View.VISIBLE);
				}
			}
			/// 当用户再次进入当当前页面时做的网络请求
			if (msg.what == 4) {
				load();
				alllinear.setVisibility(View.VISIBLE);

				ResistcollateralCoaunt.setText(dizhishunum);// 设置抵制押品数量显示
				dizhi_listview.setAdapter(dzadapter);
				new TurnListviewHeight(dizhi_listview);
				// 判断抵制押品数量》0 的操作 抵制押品的集合中有数据
				if (Commoditycollaterallist.size() > 0 && dizhilist.size() > 0) {
					btn_print.setEnabled(false);
					btn_print.setVisibility(View.GONE);
					qingdfen_ok.setEnabled(true);
					qingdfen_ok.setBackgroundResource(R.drawable.buttom_selector_bg);

				}
			}

		};
	};

	/**
	 * 数据提交Handler
	 */
	private Handler submitHandler = new Handler() {
		public void handleMessage(Message msg) {
			manager.getResultmsg().remove();
			if (msg.what == 0) {
				System.out.println("------------------------------数据匹配");
				manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "提交中...");
				// 添加新的数据
				System.out.println("清空数据了");
				// addXianJin();
				// addZhongKong();
				// addDizhiYapin();
				// 必须现金|中控|抵制押品 3者为空时不能进行提交操作 zhongkonglist xianjinlist
				// dizhilist
				if (zhongkonglist.size() == 0 && xianjinlist.size() == 0 && dizhilist.size() == 0) {
					Toast.makeText(ShangJiaoQingFen_o_qf.this, "有现金|重空|抵制押品三者不能同时为空", Toast.LENGTH_SHORT).show();
					manager.getRuning().remove();
				} else {
					if((cash.size()>0||importlist.size()>0)){// 这里单独把抵质押品数据踢出去
						submit();
					}else if(!dizhishunum.equals("0")){
						submitByDZcollateralHandOverClearResultAndCheck();
					}
				}
			}
			if (msg.what == 1) {
				togerDate();
				shangjiaoRenwu = shangjiaoMark;
				xianjinlist.clear();
				zhongkonglist.clear();
				dizhilist.clear();
				xianjin_listview.setAdapter(adapter);
				new TurnListviewHeight(xianjin_listview);
				pingzheng_listview.setAdapter(zkadapter);
				new TurnListviewHeight(pingzheng_listview);
				dizhi_listview.setAdapter(dzadapter);
				new TurnListviewHeight(dizhi_listview);
				xianjin_heji.setText("0");
//				dizhi_heji.setText("0");
				zhongkong_yiqingdian.setText("0");
				spinner_text_juanbie.setText("请选择");
				xianjin_count.setText("");
//				spinner_text_zhuangtai.setText("请选择");
				pingzhengbianhao.setText("");
				zhongkong_zhonglei.setText("请选择");
				pingzheng_haoduan.setText("");
//				dizhi_bianhao.setText("");
				qingdfen_ok.setEnabled(false);
				qingdfen_ok.setBackgroundResource(R.drawable.button_gray);
			}
		};
	};
	private Handler timeoutHandle = new Handler() {// 连接超时handler
		public void handleMessage(Message msg) {
			manager.getRuning().remove();
			if (msg.what == 00) {

				System.out.println("我是提交超时的时候---");
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "提交中...");
//						submit();
						if((cash.size()>0||importlist.size()>0)){// 这里单独把抵质押品数据踢出去
							submit();
						}else if(!dizhishunum.equals("0")){
							submitByDZcollateralHandOverClearResultAndCheck();
						}
					}
				});
			}
			if (msg.what == 10) {

				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "网络连接失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "提交中...");
//						submit();
						if((cash.size()>0||importlist.size()>0)){// 这里单独把抵质押品数据踢出去
							submit();
						}else if(!dizhishunum.equals("0")){
							submitByDZcollateralHandOverClearResultAndCheck();
						}

					}
				});
			}
			if (msg.what == 01) {
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						manager.getAbnormal().remove();
						manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
						getDate();
					}
				});
			}
			if (msg.what == 11) {
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "网络连接失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						manager.getAbnormal().remove();
						manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
						getDate();
					}
				});
			}

			if (msg.what == 12) {
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "没有任务", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
//								manager.getRuning().runding(
//										ShangJiaoQingFen_o_qf.this, "数据加载中...");
//								getDate();
					}
				});
			}
			if(msg.what == 13){
			manager.getResultmsg().resultmsg(ShangJiaoQingFen_o_qf.this,
					"请选择打印设备！", false);

		}
			if(msg.what == 14){
			Toast.makeText(getApplicationContext(),"返回不正常"+param,
					Toast.LENGTH_SHORT).show();


		}


		};
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String dizhiNum = msg.getData().getString("Num");
			for (int i = 0; i < dizhilist.size(); i++) {
				if (dizhilist.get(i).equals(dizhiNum)) {
					dzok = i;
				} else {
					dzok = -1;
				}
			}
			if (dzok == -1) {
				dizhilist.add(dizhiNum);
			} else {
				manager.getResultmsg().resultmsg(ShangJiaoQingFen_o_qf.this, "抵质押品编号重复", false);
			}
			// dzadapter.notifyDataSetChanged();
			dizhi_listview.setAdapter(dzadapter);
			new TurnListviewHeight(dizhi_listview);
//			dizhi_heji.setText("" + dizhilist.size());
		}
	};

	/**
	 * 清分对比 handler
	 */
	private Handler isSubmitHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// 如果什么都没记录
			if (xianjinlist.size() == 0 && zhongkonglist.size() == 0 && dizhilist.size() == 0) {
				manager.getResultmsg().resultmsgHas1(ShangJiaoQingFen_o_qf.this, "请输入清分信息", false, 1);
			} else {

				manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "比对中...");
				boolean isCanSubmit = isEquals(shangjiaoRenwu, xianjinlist, zhongkonglist, dizhilist);
				manager.getRuning().remove();
				if (isCanSubmit) {
					manager.getResultmsg().resultmsgHas2(ShangJiaoQingFen_o_qf.this, "清分数据匹配,是否提交", true, 0);
				} else {
					shangjiaoRenwu = shangjiaoMark;
					manager.getResultmsg().resultmsgHas1(ShangJiaoQingFen_o_qf.this, "清分数据不匹配,请复清", false, 1);
				}
			}
		}
	};

	// ------————————————————————————————————————————————————————————————————————————————————————————————————————————
	/*
	 * 页面初始化 Handler
	 */

	private Handler create = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			manager.getRuning().remove(); // 关闭弹窗
			switch (msg.what) {
			case 1: // 正常获取
				// 设置券别信息
				str_zhuangtai = new String[] { "完整券", "半损券", "全损券" };
				String quanbieXinxi = msg.obj.toString();
				System.out.println("获取的券别信息为：" + quanbieXinxi);

				Table[] table = Table.doParse(quanbieXinxi);

				List<String> quanbieIds = table[0].get("quanbieId").getValues();
				List<String> quanbieNames = table[0].get("quanbieName").getValues();
				List<String> quanjiazhis = table[0].get("quanjiazhi").getValues();
				List<String> canshunjiazhis = table[0].get("canshunjiazhi").getValues();
				System.out.print("获取的残损状态" + canshunjiazhis);
				// 将券别信息添加进入集合中
				for (int i = 0; i < quanbieIds.size(); i++) {
					QuanbieXinxi xinxi = new QuanbieXinxi();
					xinxi.setCanshunJiazhi(canshunjiazhis.get(i));
					xinxi.setQuanbieId(quanbieIds.get(i));
					xinxi.setQuanbieName(quanbieNames.get(i));
					xinxi.setQuanJiazhi(quanjiazhis.get(i));
					quanbieList.add(xinxi);
				}
				getZhongkong(); // 继续获取重空
				break;

			case 2: // 连接超时
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // 点击重新获取
					}
				});
				break;

			case 3: // 获取失败
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "券别信息获取失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getSpinnerData(); // 点击重新获取
					}
				});
				break;
//				******
			case 4: // 重空信息获取成功

				String zhongkongXinxi = msg.obj.toString();
				Table[] table2 = Table.doParse(zhongkongXinxi);
				List<String> zhongkongIdList = table2[0].get("zhongkongId").getValues();
				List<String> zhongkongNameList = table2[0].get("zhongkongName").getValues();
				for (int i = 0; i < zhongkongIdList.size(); i++) {
					String zhongkongId = zhongkongIdList.get(i);
					String zhongkongName = zhongkongNameList.get(i);
					Log.e(TAG, "**********AAA===" + zhongkongIdList.get(i));
					Log.e(TAG, "**********BBBB===" + zhongkongNameList.get(i));
					zhongkongList.add(new ZhongkongXinxi(zhongkongId, zhongkongName));

//					lzhongkongList.add(new ZhongkongXinxi(zhongkongId, zhongkongIdList.get(i)+","+zhongkongNameList.get(i)));
					lzhongkongList.add(new ZhongkongXinxi(null, zhongkongNameList.get(i)));// 后台修改sql后造成返回格式 直接001,农商行支票
				}
				for (int i = 0; i < lzhongkongList.size(); i++) {
					Log.e(TAG, "**********FFF" + lzhongkongList.get(i));
				}

				// str_pingzheng = new String[] { "存折", "银行卡", "支票", "网银盾" };
				break;
			case 5: // 重空连接超时
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "数据连接超时", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getZhongkong(); // 点击重新获取
					}
				});
				break;
			case 6: // 重空获取失败
				manager.getAbnormal().timeout(ShangJiaoQingFen_o_qf.this, "重空信息获取失败", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
						getZhongkong(); // 点击重新获取
					}
				});
				break;

			}
		}
	};

//	/***
//	 * 获取抵制押品需要打印的详细信息
//	 */
//
//	private void getprintinfo() {
////		manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				String param;
//
//				List<String> daihaolist = new ArrayList<String>();
//				Log.d(TAG, "==peisongId===" + peisongId + "===psdId==" + psdId);
//				// 配送单传值
//				if (psdId.equals("")) {
//					psdId = peisongId;
//				}
//
//				try {
////					shuliang:|canshun:|quanbieId:|quanbie:1330
//					param = new QingfenRenwuService().isPrintbagnumberlist(peisongId);
//					Log.d(TAG, "_____======" + param);
//					// 加入判断为null时候操作
//					shangjiaoRenwu = Table.doParse(param);
//
//					Table[] table4 = Table.doParse(param);
//					for (int i = 0; i < table4.length; i++) {
//
//						daihaolist = table4[0].get("daihao").getValues();
//					}
//					for (int i = 0; i < daihaolist.size(); i++) {
//						dizhilist.add(daihaolist.get(i));
//					}
//					okHandle.sendEmptyMessage(3);
//				} catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					timeoutHandle.sendEmptyMessage(01);
//				} catch (NullPointerException e) {
//					e.printStackTrace();
////					timeoutHandle.sendEmptyMessage(12);
//				} catch (Exception e) {
//					e.printStackTrace();
//					timeoutHandle.sendEmptyMessage(11);
//				}
//			}
//		}).start();
//
//	}
	/***
	 * 获取抵制押品需要打印的详细信息
	 * printcode
	 * 修改20200707  打印增加打印机的编号
	 */
	private String  printroobort =""; // 组件上的文字打印机id
	private String  seleckPrinrResult="";// 循环拿到结果 存keyvalyue 中的数据value
	private void getUpdata_printinfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> daihaolist=new ArrayList<String>();
				Log.d(TAG,"==peisongId==="+peisongId+"===psdId=="+psdId);
				//配送单传值
				if(psdId.equals("")){
					psdId=peisongId;
				}
				printroobort	=shangjiaoqingfen_printinfo_spinner_text.getText().toString();
				seleckPrinrResult="";
				for(String key : printmapnetrestult.keySet()){
					Log.d(TAG,"-----------key-----"+key+"printroobort"+printroobort);
					if(printroobort.equals(key)){
						seleckPrinrResult = printmapnetrestult.get(key);
					}

					Log.d(TAG,"-----------seleckPrinrResult-----"+seleckPrinrResult);
				}
				if(null==seleckPrinrResult||printroobort.equals("请选择")||seleckPrinrResult.equals("")){
					timeoutHandle.sendEmptyMessage(13);
					return;
				}else{


					Log.d(TAG,"-------===printroobort="+printroobort+"peisongId-------:"+peisongId);
					try {
//					shuliang:|canshun:|quanbieId:|quanbie:1330
//					param = new QingfenRenwuService().isPrintbagnumberlist(peisongId);
						param = new QingfenRenwuService().isPrintbagnumberlist(peisongId,seleckPrinrResult);
						Log.d(TAG,"_____======"+param);
						if(param.equals("anyType{}")){
							timeoutHandle.sendEmptyMessage(14);
						}else{
							// 加入判断为null时候操作
							shangjiaoRenwu = Table.doParse(param);

							Table[] table4=Table.doParse(param);
							for (int i = 0; i < table4.length; i++) {

								daihaolist = table4[0].get("daihao")
										.getValues();
							}
							for (int i = 0; i < daihaolist.size(); i++) {
								dizhilist.add(daihaolist.get(i));
							}
							okHandle.sendEmptyMessage(3);
						}
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						timeoutHandle.sendEmptyMessage(01);
					} catch (NullPointerException e) {
						e.printStackTrace();
//					timeoutHandle.sendEmptyMessage(12);
					} catch (Exception e) {
						e.printStackTrace();
						timeoutHandle.sendEmptyMessage(11);
					}
				}
			}
		}).start();




	}

	// 适配器
	class SpinnerAdapter extends ArrayAdapter<ShangJiaoQingFen_o_qf_Print_Entity> {
		Context context;
		List<ShangJiaoQingFen_o_qf_Print_Entity> items = new ArrayList<ShangJiaoQingFen_o_qf_Print_Entity>() {};
		public SpinnerAdapter(Context context, int textViewResourceId,
							  List<ShangJiaoQingFen_o_qf_Print_Entity> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}
		@Override
		public View getDropDownView(int position, View convertView,
									ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(
						android.R.layout.simple_spinner_item, parent, false);
			}
			// 此处设置spinner的字体大小 和文字颜色
			TextView tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			tv.setText(items.get(position).getCode());
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLUE);
			tv.setTextSize(40);
			return convertView;
		}
	}
	/***
	 * 20200706获取打印机信息
	 * lianchao
	 */

	/**
	 * 获取打印机信息
	 */
	private void getPritInfoHander() {
		manager.getRuning().runding(ShangJiaoQingFen_o_qf.this, "数据加载中...");
		// 获取打印机信息
		Thread thread = new Thread(new GetPrintInfo());
		thread.start();

	}

}
