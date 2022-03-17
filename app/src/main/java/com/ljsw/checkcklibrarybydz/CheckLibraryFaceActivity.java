package com.ljsw.checkcklibrarybydz;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.pda.R;
import com.ljsw.checkcklibrarybydz.adapter.CheckLibraryDZFaceAdapter;
import com.ljsw.checkcklibrarybydz.bean.CheckLibraryFaceBean;
import com.online.update.biz.LoadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取任务后拿到某个大柜子
 */
public class CheckLibraryFaceActivity extends Activity implements View.OnClickListener {
    // 组件
    private ImageView checkelibrarydzface_back;
    private ListView listviewface;
    //    变量
    private List<CheckLibraryFaceBean> listchecklibraryface = new ArrayList<CheckLibraryFaceBean>();

    private CheckLibraryDZFaceAdapter faceadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_library_face);
        String intents = getIntent().getStringExtra("task");
        initView();
    }

    private void initView() {
        checkelibrarydzface_back = (ImageView) findViewById(R.id.checkelibrarydzface_back);
        checkelibrarydzface_back.setOnClickListener(this);
        listviewface = (ListView) findViewById(R.id.listviewlibraryface);

        listviewface.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    private void LoadData() {
        listchecklibraryface.clear();
        CheckLibraryFaceBean  clbean=new CheckLibraryFaceBean();
        clbean.setLossCounts("10");
        clbean.setTableNo("face001");
        CheckLibraryFaceBean  clbean2=new CheckLibraryFaceBean();
        clbean2.setLossCounts("40");
        clbean2.setTableNo("face002");
        listchecklibraryface.add(clbean);
        listchecklibraryface.add(clbean2);
        faceadapter=new   CheckLibraryDZFaceAdapter(listchecklibraryface,CheckLibraryFaceActivity.this);
        listviewface.setAdapter(faceadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkelibrarydzface_back:
                CheckLibraryFaceActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
