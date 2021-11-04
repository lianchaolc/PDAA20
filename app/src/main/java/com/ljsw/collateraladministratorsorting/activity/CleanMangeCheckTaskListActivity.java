package com.ljsw.collateraladministratorsorting.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pda.R;

/***
 *DZ押品管理员
 * 选择任务
 *
 * 当前页面作废
 */
public class CleanMangeCheckTaskListActivity extends AppCompatActivity implements View.OnClickListener {


    private Button cleanmange_update;//  更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_mange_check_task_list);
        initView();

    }

    private void initView() {
        cleanmange_update = (Button) findViewById(R.id.cleanmange_update);
        cleanmange_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
