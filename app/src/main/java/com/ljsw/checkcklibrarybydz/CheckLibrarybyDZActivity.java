package com.ljsw.checkcklibrarybydz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.checkcklibrarybydz.adapter.CheckLibraryDZTaskAdapter;
import com.ljsw.checkcklibrarybydz.bean.CheckeLibraryDZTaskBean;
import com.ljsw.tjbankpad.baggingin.activity.zhanghuziliao.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/***
 * 2022.2.16
 * 给抵质押品做的盘查库页面
 * 需求精确到隔断  避免一次扫描 数据量大造成卡顿
 *
 */
public class CheckLibrarybyDZActivity extends BaseActivity {

    private ImageView checkelibrarydz_back;//  返回
    private TextView checkelibrarydzname;//  设置名称
    private ListView checkelibrarydz__listview;//
    private CheckLibraryDZTaskAdapter ckdztaskadapter;
    //  数据源
    private List<CheckeLibraryDZTaskBean> taskkdatalist=new ArrayList<CheckeLibraryDZTaskBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkclibrarydz);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskkdatalist.clear();
        LoadData();
    }

    private void LoadData() {
        CheckeLibraryDZTaskBean   checkeLibraryDZTaskBean=new CheckeLibraryDZTaskBean();
        checkeLibraryDZTaskBean.setTaskNO("202001");
        checkeLibraryDZTaskBean.setTaskNO("202002");
        CheckeLibraryDZTaskBean   checkeLibraryDZTaskBean2=new CheckeLibraryDZTaskBean();
        checkeLibraryDZTaskBean2.setTaskNO("202001");
        checkeLibraryDZTaskBean2.setTaskNO("202002");
        taskkdatalist.add(checkeLibraryDZTaskBean);
        taskkdatalist.add(checkeLibraryDZTaskBean2);
        ckdztaskadapter=new CheckLibraryDZTaskAdapter(taskkdatalist,CheckLibrarybyDZActivity.this);
        checkelibrarydz__listview.setAdapter(ckdztaskadapter);
    }

    private void initView() {
        checkelibrarydz_back=(ImageView) findViewById(R.id.checkelibrarydz_back);
        checkelibrarydz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLibrarybyDZActivity.this.finish();
            }
        });
        checkelibrarydzname=(TextView) findViewById(R.id.checkelibrarydzname);
        checkelibrarydzname.setText(""+ GApplication.loginname);
        checkelibrarydz__listview=findViewById(R.id.checkelibrarydz__listview);

        checkelibrarydz__listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent   i=new Intent(CheckLibrarybyDZActivity.this,CheckLibraryFaceActivity.class);
                String  action=taskkdatalist.get(position).getTaskNO();
                i.putExtra("task",action);
                startActivity(i);
            }
        });
    }



    @Override
    protected void setContentView() {

    }
}
