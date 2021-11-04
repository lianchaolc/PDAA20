package com.ljsw.collateraladministratorsorting.loginuser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.GApplication;
import com.example.pda.R;

/**
 * Created by Administrator on 2021/9/26.
 *
 * 设定顶部标题
 */
//

public class LoginControInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_qingfenershijiu_userinfo, null);
        if  (GApplication.loginUsername!= null||!GApplication.loginname.equals("")) {
            TextView tvUser1Name = (TextView) view.findViewById(R.id.tv_fg_qingfenershijiuuser);
            tvUser1Name.setText( GApplication.loginUsername);
            TextView fg_qingfenershijiu=view.findViewById(R.id.fg_qingfenershijiuorg);
        }
        return view;
    }
}
