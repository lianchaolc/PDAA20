package com.ljsw.tjbankpda.qf.fragment;

import java.util.List;
import java.util.Map;

import com.example.pda.R;
import com.ljsw.tjbankpda.db.entity.Qingfenxianjin;
import com.ljsw.tjbankpda.qf.application.Mapplication;
import com.ljsw.tjbankpda.qf.entity.QuanbieXinxi;
import com.ljsw.tjbankpda.qf.fragment.QinglingZhuangxiangXianjinFragment.QuanbieBaseAdapter;
import com.ljsw.tjbankpda.util.MySpinner;
import com.manager.classs.pad.ManagerClass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/****
 * 
 * @author Administrator 2018_09_17 在装箱入库 lc
 */
@SuppressLint("ValidFragment")
public class ZaiRuKuZhuangDai extends Fragment {
	public ZaiRuKuZhuangDai(Map<String, QuanbieXinxi> quanbieXinxi) {

	}

	public ZaiRuKuZhuangDai() {
	}

	// 定义组件
	private TextView bc_textviewidtype, bc_tvspcoinorpaper, bc_tvwkindof, zrkztv;// 券别 券种 纸币和硬币
	private EditText ed_zrkzin, ettest001;// 用户输入的钱数

	private List<Qingfenxianjin> ltQuanbie = Mapplication.getApplication().boxLtXianjing;// 存放券别信息
	private Map<String, QuanbieXinxi> quanbieXinxi; // 券别信息
	private ManagerClass manager;
	private MySpinner spinner;
//	private String[] str_juanbie=Mapplication.getApplication().xjType;
	private String[] str_juanbie = { "纸100元（5套）", "纸50元（5套）", "纸10元（5套）" };
	private String[] str_juanzhong = { "残损", "完整" };
	private String[] str_coinorpaper = { "纸", "硬币" };

	private String etInWanyuan;// 用户输入的钱数

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.ab_zairukuzhuangdai_fragment, null);
		bc_textviewidtype = (TextView) root.findViewById(R.id.bc_tvwkindof);
		bc_tvwkindof = (TextView) root.findViewById(R.id.bc_textviewidtype);
		bc_tvspcoinorpaper = (TextView) root.findViewById(R.id.bc_tvspcoinorpaper);

		zrkztv = (TextView) root.findViewById(R.id.zrkztv);

		ed_zrkzin = (EditText) root.findViewById(R.id.ed_zairukuzhuangxiang);

		;
		manager = new ManagerClass();
		bc_textviewidtype.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				spinner = new MySpinner(getActivity(), bc_textviewidtype, bc_textviewidtype);
				spinner.setSpinnerHeight(bc_textviewidtype.getHeight() * 2);
				spinner.setList(getActivity(), str_juanzhong);
				spinner.showPopupWindow(bc_textviewidtype);
				spinner.setList(getActivity(), str_juanzhong, 40);
			}
		});

		bc_tvwkindof.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				spinner = new MySpinner(getActivity(), bc_tvwkindof, bc_tvwkindof);
				spinner.setSpinnerHeight(bc_tvwkindof.getHeight() * 2);

				spinner.setList(getActivity(), str_juanbie);
				spinner.showPopupWindow(bc_tvwkindof);
				spinner.setList(getActivity(), str_juanbie, 40);
			}
		});
		bc_tvspcoinorpaper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				spinner = new MySpinner(getActivity(), bc_tvspcoinorpaper, bc_tvspcoinorpaper);
				spinner.setSpinnerHeight(bc_tvspcoinorpaper.getHeight() * 2);
				spinner.setList(getActivity(), str_coinorpaper);
				spinner.showPopupWindow(bc_tvspcoinorpaper);
				spinner.setList(getActivity(), str_coinorpaper, 40);
			}
		});

		etInWanyuan = ed_zrkzin.getText().toString();
		ed_zrkzin.getText().toString().trim();
		Log.e("ZA111", "=====!" + ed_zrkzin.toString() + "====" + zrkztv.toString() + "===###"
				+ ed_zrkzin.getText().toString().trim());
//	getEDStrign();
		return root;
	}

	/***
	 * 获取用户输入的值
	 * 
	 * @return
	 */
	public String getEDStrign() {
		Log.e("ZA", "======" + ed_zrkzin.getText().toString());
		if (!ed_zrkzin.getText().toString().equals("") || !ed_zrkzin.getText().toString().equals(null)) {
			etInWanyuan = ed_zrkzin.getText().toString();
		}
		return etInWanyuan;
	}
}
