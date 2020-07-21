package com.ljsw.tjbankpad.baggingin.activity;

import com.example.pda.R;

import a20.android_serialport_api.SerialPort;
import a20.cn.uhf.admin.Tools;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android_rfid_control.powercontrol;
import com.handheld.UHF.UhfManager;

/***
 * 扫描获取箱子类型 并写入数据
 * 
 * @author Administrator
 * 
 */
public class ReadBoxTypeActivity extends Activity implements OnClickListener {
	private static final String TAG = "ReadBoxTypeActivity";
	private RadioGroup rg;
	private RadioButton rb_Xianjin, rb_ZhongKong, rb_DiZhiYaPin;
	private Button rbtbtn_saomiao, rbtbtn_tijao;
	private TextView rbt_tvsettype; // 是那种类型 现金中空 抵制押品
	private EditText rbt_Et;// 接受用户输入
	private TextView rb_tvsaomiaoresult, tv_clean;// 扫描到的箱子

	// 串口需要
	private powercontrol rFidPowerControl;// yang
	private UhfManager manager;
	private SerialPort uhfSerialPort;// 超高频串口
	String epc = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readboxtype);
		// ================================打开RFID电源控制
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		epc = getIntent().getStringExtra("epc");
		initview();
		LoadData();
		LoadGpiO();
		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}

	}

	private void LoadGpiO() {
		// TODO Auto-generated method stub
		rFidPowerControl = new powercontrol();
		rFidPowerControl.openrfidPowerctl("/dev/rfidPowerctl");
		rFidPowerControl.rfidPowerctlSetSleep(0);
		// ===================================

		epc = getIntent().getStringExtra("epc");
		/*
		 * 获取reader时，有串口的初始化操作 若reader为null，则串口初始化失败
		 */
		manager = UhfManager.getInstance();
		if (manager == null) {
			Toast.makeText(getApplicationContext(), "串口初始化失败", 0).show();
			return;
		}

	}

	private void LoadData() {

	}

	private void initview() {
		// TODO Auto-generated method stub
		rg = (RadioGroup) findViewById(R.id.rg_zzx_num);
		rb_Xianjin = (RadioButton) findViewById(R.id.rb_cash);
		rb_ZhongKong = (RadioButton) findViewById(R.id.rb_zhongyaozhongkong);
		rb_DiZhiYaPin = (RadioButton) findViewById(R.id.dizhiyapin);
		rg.setOnCheckedChangeListener(new MyRadioButtonListener());
		rbt_tvsettype = (TextView) findViewById(R.id.rbt_tv);// 获取数据后的类型
		rb_tvsaomiaoresult = (TextView) findViewById(R.id.rb_tvsaomiaoresult);
		rbt_tvsettype.setOnClickListener(this);
		rbtbtn_saomiao = (Button) findViewById(R.id.btn_saomiao);
		rbtbtn_saomiao.setOnClickListener(this);
		rbtbtn_tijao = (Button) findViewById(R.id.btn_tijiao);
		rbtbtn_tijao.setOnClickListener(this);
		tv_clean = (TextView) findViewById(R.id.tv_clean);
		tv_clean.setOnClickListener(this);
		rbt_Et = (EditText) findViewById(R.id.et_into);
		rbt_Et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (count > 0) {
					setview();
				}
			}

			private void setview() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (rbt_Et.getText().length() != 0) {
					tv_clean.setVisibility(View.VISIBLE);
				} else {
					tv_clean.setVisibility(View.INVISIBLE);
				}
			}
		});
		/** 焦点变化监听 **/
		rbt_Et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (rbt_Et.getText().length() != 0) {
					// 删除图标显示
					tv_clean.setVisibility(View.VISIBLE);
				} else {
					// 删除图标隐藏
					tv_clean.setVisibility(View.INVISIBLE);
				}
				if (arg1) {
					// 得到焦点
				} else {
					// 失去焦点，删除图标隐藏
					tv_clean.setVisibility(View.INVISIBLE);
				}
			}
		});

		// 删除图标的点击监听事件
		tv_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 执行清空EditText数据
				rbt_Et.setText("");
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 当读取不到数据时 检查驱动可能没有完全加载
		case R.id.btn_saomiao:
			final byte[] accessPassword = Tools.HexString2Bytes("00000000");

			if (accessPassword.length != 4) {
				Toast.makeText(getApplicationContext(), "密码为4个字节", Toast.LENGTH_SHORT).show();
				return;
			}
			// 目前只操作EPC
			byte[] data = manager.readFrom6C(1, 2, 6, accessPassword);

			if (data != null && data.length > 1) {
				String dataStr = Tools.Bytes2HexString(data, data.length);
				Toast.makeText(getApplicationContext(), dataStr, 0).show();
//				editReadData.append("读数据：" + dataStr + "\n");

				String xianjin = dataStr.substring(5, 7);
				Log.e("读取数据成功", "dataStr==" + dataStr);
				Log.e("读取数据成功", "xianjin==" + xianjin);
				if (xianjin.equals("40")) {
					rb_tvsaomiaoresult.setText("xx箱子");
				} else if (xianjin.equals("75")) {
					rb_tvsaomiaoresult.setText("xy箱子");
				} else {
					rb_tvsaomiaoresult.setText("箱子");
				}
				break;
			} else {
				Toast.makeText(getApplicationContext(), "读数据失败", Toast.LENGTH_SHORT).show();
				if (data != null) {
					Log.e("", "==" + (data[0] & 0xff));
					return;
				}
				Log.e("", "==" + "读数据失败，返回为空" + "\n");
			}

			break;
		case R.id.btn_tijiao:

			break;

		case R.id.tv_clean:// 清除edtext为null
			rbt_Et.setText("" + "");
			break;
		}

	}

	class MyRadioButtonListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// 选中状态改变时被触发
			switch (checkedId) {
			case R.id.rb_cash:

				rbt_tvsettype.setText("万元");
				break;

			case R.id.rb_zhongyaozhongkong:
				// 当用户选择
				rbt_tvsettype.setText("数量");
				break;
			case R.id.dizhiyapin:
				// 当用户选择
				rbt_tvsettype.setText("数量");
				break;
			}
		}
	}

}
