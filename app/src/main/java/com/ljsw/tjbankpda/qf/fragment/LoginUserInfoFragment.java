package com.ljsw.tjbankpda.qf.fragment;

import com.application.GApplication;
import com.example.pda.R;
import com.ljsw.tjbankpda.qf.application.Mapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginUserInfoFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_qingfendenglu_userinfo, null);
		if (Mapplication.getApplication().user1 != null && Mapplication.getApplication().user2 != null) {
			TextView tvUser1Name = (TextView) view.findViewById(R.id.tv_fg_qingfendenglu_user1);
			TextView tvUser2Name = (TextView) view.findViewById(R.id.tv_fg_qingfendenglu_user2);
			tvUser1Name.setText(Mapplication.getApplication().user1.getLoginUserName()+"");
			tvUser2Name.setText(Mapplication.getApplication().user2.getLoginUserName()+"");
		}else{
			if(null!=GApplication.user){
				if(GApplication.user.getLoginUserId().equals("29")){
				TextView textView1=(TextView)view.findViewById(R.id.textView1);
				TextView textView4=(TextView)view.findViewById(R.id.textView4);
				TextView tvUser1Name = (TextView) view.findViewById(R.id.tv_fg_qingfendenglu_user1);
				TextView tvUser2Name = (TextView) view.findViewById(R.id.tv_fg_qingfendenglu_user2);
				textView1.setText("抵质押品管理员:");
				tvUser1Name.setText("");
				tvUser2Name.setText(""+GApplication.user.getLoginUserName());
				textView4.setText("");
				}
			}
		}
		return view;
	}

}
