package com.example.pda;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import com.application.GApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpad.baggingin.activity.DialogManager;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.AccountAndResistCollateralService;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.YayunMingXiSelectEntity;
import com.ljsw.tjbankpad.baggingin.activity.adapter.YayunyaunTaskMingXiAdapter;
import com.manager.classs.pad.ManagerClass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity.*;
import com.ljsw.tjbankpda.yy.application.S_application;

/****
 * 押运员要领取任务明细进入后点击按钮确认领取任务 lianc
 * 
 * @author Administrator state 只跟上缴有关
 *         [{"corpid":"904030100","state":"00","corpname":"青光支行"}]
 *         http://192.168
 *         .1.113:8081/cash/webservice/escort/getnotastinfo?arg0=11009998
 *         &arg1=BC02
 * 
 */
public class YayunSelectRewuTookinfoAndTrueictivity extends Activity implements View.OnClickListener {

	protected static final String TAG = "YayunSelectRewuMingXictivity";
	private ListView listviewshow;// 显示用的组件
	private Button btn_tooktasktrue;// 确认领取任务按钮
	private YayunyaunTaskMingXiAdapter yYayunyaunTaskMingXiAdapter;
	private TextView ysrwmxtvtitle;// 线路名称
	private ImageView ysrwmxivblack;
	private ManagerClass manager;
	private View.OnClickListener OnClick1;
	private Dialog dialogfa, dialogsuccess;// 失败
	private DialogManager dialogManager;
	private ManagerClass managerclass = new ManagerClass();
	private List<YayunMingXiSelectEntity> yayunmingselectlist = new ArrayList<YayunMingXiSelectEntity>();
	private String linname;// 获取线路名称
	private Context mContext;
	private YayunSelectRewuTookinfoAndTrueEntity YayunSelectRewuTookinfoAndTrueEntity = new YayunSelectRewuTookinfoAndTrueEntity();
	private List<YayunSelectRewuTookinfoAndTrueEntity> selectrwtooinforandtruelist = new ArrayList<YayunSelectRewuTookinfoAndTrueEntity>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yayun_select_rewu_ming_xictivity);
		Log.e(TAG, "S_application.getApplication().s_userYayun=====" + S_application.getApplication().s_userYayun);
		manager = new ManagerClass();
		mContext = YayunSelectRewuTookinfoAndTrueictivity.this;
		InitView();
		linname = getIntent().getStringExtra("linname");
		dialogManager = new DialogManager(YayunSelectRewuTookinfoAndTrueictivity.this);
	}

	@Override
	protected void onResume() {

		OnClick1 = new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				GetResultTookTask();
			}
		};
		// TODO Auto-generated method stub
		super.onResume();
		if (null != yayunmingselectlist) {
			yayunmingselectlist.clear();
		}

		if (null != linname && (!linname.equals(""))) {
			GetResultTookTask();
		} else {
			Log.i(TAG, "上一个页面没传线路名称不做网路请求");
		}
	}

	/****
	 * 当用户进入下一个页面时查询是否有请领任务
	 */
	private void GetResultTookTask() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Log.e(TAG, "----S_application.getApplication().s_userYayun=========="
							+ S_application.getApplication().s_userYayun);
					Log.e(TAG, "----linname==========" + linname);
					// 用户账号
//					String userZhanghu = GApplication.use.getUserzhanghu();
//					;

					String userZhanghu = S_application.getApplication().s_userYayun;
					if (null != userZhanghu && null != linname) {
						String netresultClean = new AccountAndResistCollateralService().GetTookTask(userZhanghu,
								linname);
						Log.e(TAG, "返回原数据" + netresultClean.toString());
						if (!netresultClean.equals("") || netresultClean != null) {
							// 数据返回
							Gson gson = new Gson();
							Log.e(TAG, "测试数据源" + netresultClean.toString());

							Type type = new TypeToken<ArrayList<YayunSelectRewuTookinfoAndTrueEntity>>() {
							}.getType();

							List<YayunSelectRewuTookinfoAndTrueEntity> list = gson.fromJson(netresultClean, type);
							selectrwtooinforandtruelist = list;
							Log.e(TAG, "----YayunyuanSelectTaskEntitylist" + selectrwtooinforandtruelist.size());
							if (null != list && list.size() > 0) {
								handler.sendEmptyMessage(5);
							} else {

								handler.sendEmptyMessage(3);
							}

						} else {
							handler.sendEmptyMessage(3);
						}
					} else {
						Log.e(TAG, "***===" + null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "***===" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();
	}

	/***
	 * 组件初始化
	 */
	private void InitView() {
		listviewshow = (ListView) findViewById(R.id.listviewshow);
		btn_tooktasktrue = (Button) findViewById(R.id.btn_tooktasktrue);
		btn_tooktasktrue.setOnClickListener(this);
		ysrwmxtvtitle = (TextView) findViewById(R.id.textView1);// 线路名称
		ysrwmxivblack = (ImageView) findViewById(R.id.ac_yayun_selecrenwumingxiblack);
		ysrwmxivblack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_tooktasktrue:
			TrueTookTask();// 提交并更新数据
			Dialog dialog;

			break;
		case R.id.ac_yayun_selecrenwumingxiblack:
			YayunSelectRewuTookinfoAndTrueictivity.this.finish();
			break;

		default:
			break;
		}

	}

	/***
	 * 创建网络 请求确定领取任务 lianc 20191126 弹出提示
	 */
	private void TrueTookTask() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 用户账号
					Log.e(TAG + "GApplication.use.getUserzhanghu()", "测试数据源" + GApplication.use.getUserzhanghu() + "==="
							+ S_application.getApplication().s_userYayun);
					String usernname = S_application.getApplication().s_userYayun;
					String netresultClean = new AccountAndResistCollateralService().GetTookTaskTrue(usernname, linname);
					if (!netresultClean.equals("") || netresultClean != null
							|| netresultClean.equals("返回原数据该线路上缴派工单已被交接")) {
						Log.e(TAG + "true", "测试数据源" + netresultClean.toString());
						handler.sendEmptyMessage(6);
					} else {
						handler.sendEmptyMessage(4);
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e("", "**===" + e);
					handler.sendEmptyMessage(0);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e("TAG", "**===" + e);
					handler.sendEmptyMessage(4);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("TAG", "***===" + e);
					handler.sendEmptyMessage(1);// 实际运行此处解开

				}
			}

		}.start();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuTookinfoAndTrueictivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuTookinfoAndTrueictivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();
				// getData();
				// adapter.notifyDataSetChanged();
				// dzypitemListView.setAdapter(adapter);
				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(YayunSelectRewuTookinfoAndTrueictivity.this, "没有任务",
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								manager.getAbnormal().remove();
							}
						});
				break;

			case 4:
				manager.getRuning().remove();
				dialogfa = new Dialog(YayunSelectRewuTookinfoAndTrueictivity.this);
				LayoutInflater cashtopackgerinflaterfa = LayoutInflater
						.from(YayunSelectRewuTookinfoAndTrueictivity.this);
				View vfa = cashtopackgerinflaterfa.inflate(R.layout.dialog_success, null);
				Button butfa = (Button) vfa.findViewById(R.id.success);
				butfa.setText("领取任务失败");
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

			case 6:
				manager.getRuning().remove();
				dialogsuccess = new Dialog(YayunSelectRewuTookinfoAndTrueictivity.this);

				LayoutInflater cashtopackgerinflatersuccess = LayoutInflater
						.from(YayunSelectRewuTookinfoAndTrueictivity.this);
				View vsuccess = cashtopackgerinflatersuccess.inflate(R.layout.dialog_success, null);
				Button butsuccess = (Button) vsuccess.findViewById(R.id.success);
				butsuccess.setText("领取任务成功");
				dialogsuccess.setCancelable(false);
				dialogsuccess.setContentView(vsuccess);
				butsuccess.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialogsuccess.dismiss();
						Intent i = new Intent(YayunSelectRewuTookinfoAndTrueictivity.this,
								YayunSelectRewuUseActivity.class);
						startActivity(i);
					}
				});
				dialogsuccess.show();

				break;
			case 5:
				yYayunyaunTaskMingXiAdapter = new YayunyaunTaskMingXiAdapter(selectrwtooinforandtruelist, mContext);
				listviewshow.setAdapter(yYayunyaunTaskMingXiAdapter);
				break;
			default:
				break;
			}
		}

	};

}
