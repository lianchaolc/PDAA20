package com.main.pda;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pda.R;

/***
 通过输入密码修改配置地址
 2021.7.20
 */
public class InputPWBychangAdressActivity extends Activity implements View.OnClickListener {
    private TextView showerror;
    private EditText input_passwordchange; // 输入
    private Button loginaction;
    String localpw;
    private String pwdstr = "money";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pwbychang_adress);
        initView();
    }

    private void initView() {
        showerror = (TextView) findViewById(R.id.showerror);
        input_passwordchange = (EditText) findViewById(R.id.input_passwordchange);
        loginaction = (Button) findViewById(R.id.loginaction);

        loginaction.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        localpw = input_passwordchange.getText().toString().trim();
        switch (v.getId()) {
            case R.id.loginaction:
                if (null == localpw || localpw.equals("")) {
                    showerror.setText("输入不能为空");
                } else if (localpw.equals(pwdstr)) {
//                    Toast.makeText(InputPWBychangAdressActivity.this,"1111",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(InputPWBychangAdressActivity.this,Service_Address.class);
                    startActivity(i);
                } else if (localpw.length()>8) {
                    showerror.setText("输入长度不正确");
                } else {
//                    Toast.makeText(InputPWBychangAdressActivity.this,"输入密码不正确",Toast.LENGTH_SHORT).show();
                    showerror.setText("输入密码不正确");
                }
                break;
        }
    }
}
