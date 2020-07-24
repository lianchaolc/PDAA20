package com.main.pda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.application.GApplication;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.BaseActivity;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.MenuShow;
import com.messagebox.SureCancelButton;
import com.poka.device.ShareUtil;
import com.systemadmin.biz.FingerGatherBiz;
import com.systemadmin.biz.GetNaneNumByUseridBiz;

import java.util.ArrayList;
import java.util.List;

import afu.util.BaseFingerActivity;

/***
 * 用户登陆后采集自己的指纹
 */
public class FingerGatherEditeSeltDataActivity extends BaseFingerActivity implements View.OnTouchListener {
    private static final String TAG = "FingerGatherEditeSeltDataActivity";
    String fingerNumberText; // 编号
    TextView spinnertext; // 手指文本框
    TextView fingerUserName; // 姓名
    TextView fingerUserNumber; // 编号输入框
    ImageView back; // 返回
    ListView listview;
    Button btn_getname; // 确定
    View popuv; // 下拉列表视图
    ImageView finger_image; // 指纹
    FingerGatherEditeSeltDataActivity.Ad ad;
    PopupWindow mpop = null;
    View.OnClickListener nameclick;
    View.OnClickListener fingerclick;
    TextView resultText;
    ImageView success;
    ImageView fail;
    int count = 0; // 按压手指次数
    String finger_num; // 手指编号
    byte[] firstFinger = null;
    boolean is = false;

    private ManagerClass managerClass;

    List<String> list_finger = new ArrayList<String>(); // 手指集合
    TextView editedataselfname;
    private TextView edtself_finger_username;//   人员名称

    private GetNaneNumByUseridBiz NaneNumByUserid;

    GetNaneNumByUseridBiz getNaneNumByUserid() {
        return NaneNumByUserid = NaneNumByUserid == null ? new GetNaneNumByUseridBiz() : NaneNumByUserid;
    }

    FingerGatherBiz fingerbiz;

    FingerGatherBiz getFingerbiz() {
        return fingerbiz = fingerbiz == null ? new FingerGatherBiz() : fingerbiz;
    }

    SureCancelButton suercancel;

    SureCancelButton getSuercancel() {
        if (suercancel == null) {
            suercancel = new SureCancelButton();
        }
        return suercancel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_gather_edite_selt_data);
        managerClass = new ManagerClass();
        initView();
    }

    private void initView() {
        spinnertext = (TextView) findViewById(R.id.edtself_spinnertext);
        fingerUserNumber = (TextView) findViewById(R.id.edtself_finger_usernumber);
        resultText = (TextView) findViewById(R.id.edtself_fingerGather_text);
        success = (ImageView) findViewById(R.id.edtself_finger_success);
        fail = (ImageView) findViewById(R.id.edtself_finger_fail);
        fingerUserName = (TextView) findViewById(R.id.edtself_finger_username);
        btn_getname = (Button) findViewById(R.id.edtself_finger_getname);
        back = (ImageView) findViewById(R.id.edtself_finger_back);
        finger_image = (ImageView) findViewById(R.id.edtself_finger_images);

        if (null != GApplication.user.getLoginUserName()) {
            fingerUserNumber.setText(GApplication.user.getYonghuZhanghao());
            fingerUserName.setText(GApplication.user.getLoginUserName());
        } else {
            Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!空的");

        }
        fingerNumberText=fingerUserNumber.getText().toString();
        spinnertext.setOnTouchListener(this);
        btn_getname.setOnTouchListener(this);
        back.setOnTouchListener(this);
        fingerInit();
        // 重新采集事件
        fingerclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerClass.getAbnormal().remove();
                managerClass.getRuning().runding(FingerGatherEditeSeltDataActivity.this, "请等待...");
                // 指纹采集:参数 userId:用户ID finger:手指 cValue:特征值1 cValue:特征值2
                getFingerbiz().fingerprintInput(fingerNumberText, fingerInt(), firstFinger, ShareUtil.ivalBack);
            }
        };

        // 采集通知
        getFingerbiz().hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                managerClass.getRuning().remove();
                Log.i("aaa", "进来了");
                count = 0;
                if (msg.what == 1) { // 成功
                    resultText.setText("采集成功");
//                    fingerUserName.setText("");
//                    fingerUserNumber.setText("");
                    success.setVisibility(View.VISIBLE);
                    fail.setVisibility(View.GONE);
                } else if (msg.what == 0) { // 失败
                    resultText.setText("采集失败");
                    success.setVisibility(View.GONE);
                    fail.setVisibility(View.VISIBLE);
                } else if (msg.what == -1) { // 超时
                    managerClass.getAbnormal().timeout(FingerGatherEditeSeltDataActivity.this, "连接超时!", fingerclick);
                }
            }

        };

        // 获取姓名重试单击事件
        nameclick = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                managerClass.getAbnormal().remove();
                // 开始线程获取姓名
                managerClass.getRuning().runding(FingerGatherEditeSeltDataActivity.this, "正在获取姓名...");
                getNaneNumByUserid().getNameById(fingerNumberText);
            }
        };

        // 获取姓名通知handler
        getNaneNumByUserid().handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                managerClass.getRuning().remove();
                super.handleMessage(msg);
                if (msg.what == 1) {
                    resultText.setVisibility(View.VISIBLE);
                    resultText.setText("请按压手指...");

                    fingerUtil.getFingerCharAndImg();

                    Bundle bundle = msg.getData();
                    String name = bundle.getString("fingerName");
                    Log.i("name", name);
                    fingerUserName.setText(name);
                } else if (msg.what == 0) {
                    AlertDialog.Builder dlog = new AlertDialog.Builder(FingerGatherEditeSeltDataActivity.this);
                    dlog.setMessage("找不到与该编号对应的姓名");
                    dlog.setPositiveButton("确定", null);
                    dlog.show();
                } else if (msg.what == -1) {
                    managerClass.getAbnormal().timeout(FingerGatherEditeSeltDataActivity.this, "连接超时!", nameclick);
                }
            }

        };


        // 手指下拉列表
        popuv = GolbalView.getLF(FingerGatherEditeSeltDataActivity.this).inflate(R.layout.spinner_popu, null);
        mpop = new PopupWindow(popuv, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        listview = (ListView) popuv.findViewById(R.id.listview_finger);
        ad = new FingerGatherEditeSeltDataActivity.Ad();
//        fingerNumberText="43534";
//        getname();
//        success.setVisibility(View.GONE);
//        fail.setVisibility(View.GONE);
//        resultText.setText("");
        // 加载显示手指列表
        spinnertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list_finger.size(); i++) {
                    Log.i("list_finger", list_finger.get(i));
                }
                mpop.showAsDropDown(v);
                listview.setAdapter(ad);
                btn_getname.setBackgroundResource(R.drawable.buttom_selector_bg);

            }
        });
    }


    void fingerInit() {
        if (list_finger.size() <= 0) {
            // list_finger.add("请选择手指");
            list_finger.add("右手大拇指");
            list_finger.add("右手食指");
            list_finger.add("右手中指");
            list_finger.add("右手无名指");
            list_finger.add("右手小拇指");
            list_finger.add("左手大拇指");
            list_finger.add("左手食指");
            list_finger.add("左手中指");
            list_finger.add("左手无名指");
            list_finger.add("左手小拇指");
        }
    }

    // 拦截Menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        new MenuShow().menu(this);
        MenuShow.pw.showAtLocation(findViewById(R.id.finger_box), Gravity.BOTTOM, 0, 0);
        return false;
    }

    // 根据编号获取姓名
    public void getname() {
        finger_image.setImageBitmap(ShareUtil.finger_gather);// 2020-05-25 这行删除
        // 获取编号fingerUserName
        fingerNumberText = fingerUserNumber.getText() + "";
        if (fingerNumberText.equals("")) {
            Toast.makeText(FingerGatherEditeSeltDataActivity.this, "请输入编号", Toast.LENGTH_SHORT).show();
        } else {
            // 开始线程获取姓名
            managerClass.getRuning().runding(FingerGatherEditeSeltDataActivity.this, "正在获取姓名...");
            getNaneNumByUserid().getNameById(fingerNumberText);
        }

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 按下的时候
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            switch (v.getId()) {
                // 获取姓名 确定
                case R.id.edtself_finger_getname:
                    btn_getname.setBackgroundResource(R.drawable.buttom_select_press);
                    break;
                // 获取手指
                case R.id.edtself_spinnertext:
                    spinnertext.setBackgroundResource(R.color.gray_msg_bg);
                    break;
                // 返回
                case R.id.edtself_finger_back:
                    back.setImageResource(R.drawable.back_cirle_press);
                    break;
            }
        }

        // 手指松开的时候
        if (MotionEvent.ACTION_UP == event.getAction()) {
            switch (v.getId()) {
//                 获取姓名 确定
                case R.id.edtself_finger_getname:
                    btn_getname.setBackgroundResource(R.drawable.buttom_selector_bg);
                    getname();
                    success.setVisibility(View.GONE);
                    fail.setVisibility(View.GONE);
//                    resultText.setText("");
                    break;
                // 获取手指
                case R.id.edtself_spinnertext:
                    spinnertext.setBackgroundResource(R.color.transparency);
                    mpop.showAsDropDown(v);
                    listview.setAdapter(ad);
                    break;
                // 返回
                case R.id.edtself_finger_back:
                    back.setImageResource(R.drawable.back_cirle);
                    this.finish();
                    break;
            }

        }
        // 手指移动的时候
        if (MotionEvent.ACTION_MOVE == event.getAction()) {

        }
        // 意外中断事件取消
        if (MotionEvent.ACTION_CANCEL == event.getAction()) {
            switch (v.getId()) {
                // 获取姓名 确定
                case R.id.edtself_finger_getname:
                    btn_getname.setBackgroundResource(R.drawable.buttom_selector_bg);
                    break;
                // 获取手指
                case R.id.edtself_spinnertext:
                    spinnertext.setBackgroundResource(R.color.transparency);
                    break;
                // 返回
                case R.id.edtself_finger_back:
                    back.setImageResource(R.drawable.back_cirle);
                    break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ShareUtil.finger_gather = null;
        ShareUtil.ivalBack = null;
        this.finish();
    }


    @Override
    public void openFingerSucceed() {

    }

    @Override
    public void getCharImgSucceed(byte[] charBytes, Bitmap img) {
        ShareUtil.finger_gather = img;
        ShareUtil.ivalBack = charBytes;

        //验证是否选择了手指和编号是否填写
        if(check()){
            return;
        }

        finger_image.setImageBitmap(ShareUtil.finger_gather);
        if (count == 0) {
            count++;
            finger_num = spinnertext.getText().toString();
            firstFinger = ShareUtil.ivalBack;
            resultText.setText("请再次按压手指");
            fail.setVisibility(View.GONE);
            success.setVisibility(View.GONE);
        } else {
            resultText.setText("获取成功");
            // 指纹采集:参数 userId:用户ID finger:手指 cValue:特征值1 cValue:特征值2
            managerClass.getRuning().runding(FingerGatherEditeSeltDataActivity.this, "请等待...");
            getFingerbiz().fingerprintInput(fingerNumberText, fingerInt(), firstFinger, ShareUtil.ivalBack);
        }
    }

    @Override
    public void findFinger() {
        resultText.setText("正在获取指纹特征值");
    }

    class Ad extends BaseAdapter {
        @Override
        public int getCount() {
            return list_finger.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list_finger.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public View getView(int arg0, View content, ViewGroup arg2) {
            View v = content;
            final FingerGatherEditeSeltDataActivity.ViewHolder viewholder; // 内容类
            if (v == null) {
                v = GolbalView.getLF(FingerGatherEditeSeltDataActivity.this).inflate(R.layout.finger_dorplist_tem, null);
                viewholder = new FingerGatherEditeSeltDataActivity.ViewHolder();
                viewholder.finger = (TextView) v.findViewById(R.id.fingertext);
                v.setTag(viewholder);
            } else {
                viewholder = (FingerGatherEditeSeltDataActivity.ViewHolder) v.getTag();
            }

            viewholder.finger.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent even) {
                    if (MotionEvent.ACTION_DOWN == even.getAction()) {
                        viewholder.finger.setBackgroundResource(R.color.bleu_pressdown);
                    }

                    if (MotionEvent.ACTION_UP == even.getAction()) {
                        viewholder.finger.setBackgroundResource(R.color.white);
                        spinnertext.setText(viewholder.finger.getText());
                        mpop.dismiss();

                    }
                    if (MotionEvent.ACTION_CANCEL == even.getAction()) {
                        viewholder.finger.setBackgroundResource(R.color.white);
                        managerClass.getGolbalutil().ismover = 0;
                    }
                    return true;
                }
            });
            String fingerValue = getItem(arg0).toString();
            viewholder.finger.setText(fingerValue);

            return v;
        }

    }

    class ViewHolder {
        TextView finger;
    }

    public String fingerInt() {
        String finger = spinnertext.getText().toString();
        String num = "0";
        if (finger.equals("右手大拇指")) {
            num = "0";
        } else if (finger.equals("右手食指")) {
            num = "1";
        } else if (finger.equals("右手中指")) {
            num = "2";
        } else if (finger.equals("右手无名指")) {
            num = "3";
        } else if (finger.equals("右手小拇指")) {
            num = "4";
        } else if (finger.equals("左手大拇指")) {
            num = "5";
        } else if (finger.equals("左手食指")) {
            num = "6";
        } else if (finger.equals("左手中指")) {
            num = "7";
        } else if (finger.equals("左手无名指")) {
            num = "8";
        } else if (finger.equals("左手小拇指")) {
            num = "9";
        }

        return num;
    }



    // 手指验证、编号验证
    public boolean check() {
        is = false;
        fingerNumberText = fingerUserNumber.getText() + "";
        if (fingerNumberText == null || fingerNumberText.equals("")) {
            Toast.makeText(this, "请输入编号", Toast.LENGTH_SHORT).show();
            is = true;
        }

        if (count > 0) {
            if (!finger_num.equals(spinnertext.getText().toString())) {
                getSuercancel().makeSuerCancel(this, "2次输入指纹的手指不一致", new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Log.i("aaaa", "aaaa");
                        count = 0;
                        finger_num = "";
                        getSuercancel().remove();
                        resultText.setText("请按压手指");
                    }
                }, true);
                is = true;
            }
        }
        return is;
    }
}
