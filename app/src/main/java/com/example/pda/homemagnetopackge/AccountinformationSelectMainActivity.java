package com.example.pda.homemagnetopackge;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.application.GApplication;
import com.example.app.util.Skip;
import com.example.pda.R;
import com.example.pda.homemagnetopackge.entity.AccontInfoEntity;
import com.example.pda.homemagnetopackge.server.AccountAndPostmanServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ljsw.tjbankpda.db.application.o_Application;
import com.manager.classs.pad.ManagerClass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/***
 * 账户资料查询activity
 * 
 * @author Administrator lianchao 20200419
 * 
 */
public class AccountinformationSelectMainActivity extends Activity implements OnClickListener {
	protected static final String TAG = "AccountinformationSelectMainActivity";
	private ImageView zhql_ruku_back;
	private ListView accountinfoListview;
	private Button btnintent, updata;
	private List<AccontInfoEntity> accountinfoList = new ArrayList<AccontInfoEntity>();
	private List<String> listdata = new ArrayList<String>();
	private DiZhiYaPinXuanZheAdapter dDiZhiYaPinXuanZheAdapter;
	private String undata;// /需要传递的数据
	private ManagerClass manager;
	private OnClickListener OnClick1;
	private List<String> boxZZlist = new ArrayList<String>();// 村周转箱号 跳转后直接进行扫描

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinformation_select_main);
		zhql_ruku_back = (ImageView) findViewById(R.id.zhql_ruku_back);
		zhql_ruku_back.setOnClickListener(this);
		updata = (Button) findViewById(R.id.accountma_nselect_update);
		updata.setOnClickListener(this);
		accountinfoListview = (ListView) findViewById(R.id.zhanghulistview_way);
		btnintent = (Button) findViewById(R.id.btn_accountinfor);
		btnintent.setOnClickListener(this);
		manager = new ManagerClass();
		LoadData();

		OnClick1 = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				manager.getAbnormal().remove();
				netGetData();

			}
		};
	}

	/***
	 * 获取网络数据 http://192.168.1.131:8089/cash/webservice/account
	 * http://192.168.1.131:8089/cash/webservice/cash_sk/sjUnhandleOrderAccountCenterList?arg0=43534
	 */
	private void netGetData() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String conterlernumber = GApplication.user.getYonghuZhanghao();
					Log.e(TAG, "===" + conterlernumber);
					accountinfoList.clear();
					if (!conterlernumber.equals("") || null != conterlernumber) {
						netResult = new AccountAndPostmanServer().sjUnhandleOrderAccountCenterList(conterlernumber);
						Log.e(TAG, "===" + netResult);
						Gson gson = new Gson();
						Type type = new TypeToken<ArrayList<AccontInfoEntity>>() {
						}.getType();

						List<AccontInfoEntity> list1 = gson.fromJson(netResult, type);
						accountinfoList = list1;
						if (!"anytype".equals(netResult)) {
							Log.e(TAG, "===+" + "22222222222");
							handler.sendEmptyMessage(2);
						} else {
							Log.e(TAG, "===" + netResult);
							Log.e(TAG, "===" + "3 失败");
							handler.sendEmptyMessage(3);
						}
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "SocketTimeout异常" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer异常" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception异常" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

	/***
	 * 数据
	 */
	private String netResult;// 网络请求变量

	private void LoadData() {
		// AccontInfoEntity ainentity=new AccontInfoEntity();
		// ainentity.setLinid("xxx1");
		// accountinfoList.add(ainentity);

		btnintent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (accountinfoList != null) {
					undata = "";
					String linnum = "";
					for (int j = 0; j < accountinfoList.size(); j++) {

						if (accountinfoList.get(j).isChecked()) {

							listdata.add(accountinfoList.get(j).getLinenum());
							if (accountinfoList.size() > 1) {// 长度大一个时进行加逗号拼接
								linnum = linnum + accountinfoList.get(j).getLinenum() + ",";
								undata = linnum.substring(0, linnum.length() - 1);

							} else {// 小于 或者等于1 时 直接赋值 去当前的值
								undata = accountinfoList.get(0).getLinenum();
							}
						} else {

						}

					}
					if (undata.equals("") || undata == null) {
						Log.d(TAG, "!!!!!!!!!!!undata" + undata);
						btnintent.setFocusable(false);// 可点击
						Toast.makeText(AccountinformationSelectMainActivity.this, "请选中要做的线路", 400).show();

					} else {
						Log.d(TAG, "!不等！！！！！！！！！！" + "ZZZZZZZ");
						loadMyData1();
					}
				}
			}
		});

		dDiZhiYaPinXuanZheAdapter = new DiZhiYaPinXuanZheAdapter();
		accountinfoListview.setAdapter(dDiZhiYaPinXuanZheAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhql_ruku_back:

			AccountinformationSelectMainActivity.this.finish();

			break;

		case R.id.accountma_nselect_update:
			netGetData();
			dDiZhiYaPinXuanZheAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

	class DiZhiYaPinXuanZheAdapter extends BaseAdapter {

		ViewHolderAdapterWailk view;
		LayoutInflater lf = LayoutInflater.from(AccountinformationSelectMainActivity.this);

		@Override
		public int getCount() {
			return accountinfoList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return accountinfoList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			final ViewHolderAdapterWailk holder;
			if (v == null) {
				holder = new ViewHolderAdapterWailk();
				;
				v = lf.inflate(R.layout.postmansaccountlistview_walkie_chebox_item, null);
				holder.dizhiyapincheboxresult = (TextView) v.findViewById(R.id.postmancountresult1);
				holder.postmancounttv = (TextView) v.findViewById(R.id.postmancounttv);
				holder.mCheckBox = (CheckBox) v.findViewById(R.id.dzyp_chebox);

				v.setTag(holder);
			} else {
				holder = (ViewHolderAdapterWailk) v.getTag();
			}

			// 显示任务的单号 只有一个订单号
			holder.dizhiyapincheboxresult.setText(accountinfoList.get(position).getLinename());
			holder.postmancounttv.setText(accountinfoList.get(position).getCount());
			// 显示是否被选中
			holder.mCheckBox.setChecked(accountinfoList.get(position).isChecked());
			holder.mCheckBox.setOnClickListener(new OnClickListener() {

				// s设置被点击和不被点击的状态
				@Override
				public void onClick(View v) {
					if (holder.mCheckBox.isChecked()) {
						holder.mCheckBox.setChecked(true);
						accountinfoList.get(position).setChecked(true);
					} else {
						holder.mCheckBox.setChecked(false);
						accountinfoList.get(position).setChecked(false);
					}
					// view.mCheckBox.setChecked(view.mCheckBox.isChecked()?false:true);
					// 三木运算符
				}
			});
			return v;
		}

	}

	public static class ViewHolderAdapterWailk {
		public TextView dizhiyapincheboxresult;
		public TextView dizhiyapincheboxtype;
		public TextView postmancounttv;
		public CheckBox mCheckBox;
	}

	class SelectListen implements OnCheckedChangeListener {
		private int position;

		public SelectListen(int position) {
			super();
			this.position = position;

		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		netGetData();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(AccountinformationSelectMainActivity.this, "加载超时,重试?", OnClick1);
				break;
			case 1:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(AccountinformationSelectMainActivity.this, "网络连接失败,重试?", OnClick1);
				break;
			case 2:
				manager.getRuning().remove();

				dDiZhiYaPinXuanZheAdapter = new DiZhiYaPinXuanZheAdapter();
				accountinfoListview.setAdapter(dDiZhiYaPinXuanZheAdapter);

				break;
			case 3:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(AccountinformationSelectMainActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();
					}
				});
				break;
			case 4:
				if (listdata.size() == 0 && undata.equals("")) {

				} else {
					Log.d(TAG, "undata！！！！！！！！！！" + undata);

					Bundle bundle = new Bundle();
					bundle.putSerializable("houduActionlist", (Serializable) boxZZlist);
					bundle.putString("linnum", undata);

					System.out.println("准备跳转页面");
					System.out.print("!!!!!!!bundle" + bundle);
					if (null == bundle && boxZZlist.size() == 0) {

					} else {
						Log.d(TAG, "进项跳转页面的代码");
						Skip.skip(AccountinformationSelectMainActivity.this, AccountInfoChecksActivity.class, bundle,
								0);
					}

				}
				break;
			case 5:
				manager.getRuning().remove();
				manager.getAbnormal().timeout(AccountinformationSelectMainActivity.this, "没有任务", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						manager.getAbnormal().remove();

					}
				});
				break;

			default:
				break;
			}
		}

	};

	// http://192.168.1.131:8089/cash/webservice/cash_sk/sjUnHandleDistributionBoxnumByLinenumsInOrderAccount?arg0=43534&arg1=BC08
	private void loadMyData1() {

		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String conterlernumber = GApplication.user.getYonghuZhanghao();
					Log.e(TAG, "undata----------------" + conterlernumber + undata);
					if (!undata.equals("") || null != undata) {
						String unpdata1 = undata;
						Log.e(TAG, "" + unpdata1);
						netResult = new AccountAndPostmanServer()
								.sjUnHandleDistributionBoxnumByLinenumsInOrderAccount(conterlernumber, undata);
						Gson gson = new Gson();
						Type type = new TypeToken<ArrayList<String>>() {
						}.getType();

						List<String> list = gson.fromJson(netResult, type);
						Log.e(TAG, "===!!!!!!!!===========" + list.size());
						if (!"anytype".equals(netResult)) {
							Log.e(TAG, "===" + "42222222222222222");
							boxZZlist = list;// 赋值给全局的变量进行判断
							for (int i = 0; i < list.size(); i++) {
								Log.e(TAG, "===获取数据" + list.get(i));
							}
							handler.sendEmptyMessage(4);
						} else {
							Log.e(TAG, "===" + netResult);
							Log.e(TAG, "===" + "5 失败");
							handler.sendEmptyMessage(5);
						}
					} else {

						Toast.makeText(AccountinformationSelectMainActivity.this, "请选中要做的线路", 400).show();

					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Log.e(TAG, "SocketTimeout异常" + e);
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.e(TAG, "NullPointer异常" + e);
					handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception异常" + e);
					handler.sendEmptyMessage(1);
				}
			}

		}.start();

	}

}
