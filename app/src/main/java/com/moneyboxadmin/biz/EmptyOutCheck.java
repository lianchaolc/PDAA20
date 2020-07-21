package com.moneyboxadmin.biz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.entity.BoxDetail;
import com.imple.getnumber.Getnumber;

public class EmptyOutCheck {
	/**
	 * modify by wangmeng 2017-7-19 空钞箱出库检测
	 */
	int rightNum = 0;// 扫描到正确钞箱的数量
	StringBuffer str = new StringBuffer();
	Map<String, Object> resMap = new HashMap<String, Object>();
	// 记录每种钞箱品牌的数量
	Map<String, Integer> hashmap = new HashMap<String, Integer>();
	HashSet<String> brandSet = new HashSet<String>();// 品牌
	HashSet<String> brandNumSet = new HashSet<String>();// 品牌数量

	// HashSet<String> receiveMoneyBoxSet = new HashSet<String>();//取款箱类型
	String strCQ = "";

	HashSet<String> boxStateSet = new HashSet<String>();// 钞箱状态

	public Map<String, Object> check() {
		for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
			hashmap.put(GetBoxDetailListBiz.list.get(i).getBrand(), 0);
			brandSet.add(GetBoxDetailListBiz.list.get(i).getBrand());
		}
		for (int i = 0; i < Getnumber.list_boxdeatil.size(); i++) {
			String brand = Getnumber.list_boxdeatil.get(i).getBrand();
			for (Map.Entry<String, Integer> item : hashmap.entrySet()) {
				if (item.getKey().equals(brand)) {
					int n = item.getValue();
					n++;
					item.setValue(n);
					break;
				} else {
					brandSet.add(brand);
				}
			}
		}

		for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
			String brand = GetBoxDetailListBiz.list.get(i).getBrand();
			String num = GetBoxDetailListBiz.list.get(i).getNum();
			Integer intNum = Integer.parseInt(num);

			for (Map.Entry<String, Integer> item : hashmap.entrySet()) {
				String mbrand = item.getKey();
				String mnum = item.getValue().toString();
				Integer intMNum = Integer.parseInt(mnum);

				if (brand.equals(mbrand)) {
					if (intNum != intMNum) {
						brandNumSet.add(brand);
						// str.append(brand+"品牌数量异常；");
						/*
						 * if(intMNum > intNum) {//扫描到的钞箱多余应出库的钞箱 int a = intMNum-intNum;
						 * str.append("请删除该品牌的" +a+"个箱子"); } else if(intMNum < intNum){//扫描到的钞箱少余应出库的钞箱
						 * int a = intNum - intMNum; str.append("请再扫描该品牌的" +a+"个箱子"); } str.append(";");
						 */
					} /*
						 * else { rightNum +=intMNum;//品牌和数量都对，rightNum增加intMNum }
						 */
				}
				// brandSet包涵所有的品牌数据，brand为库中数据，过滤掉准确的品牌，剩下无效品牌
				if (brandSet.contains(brand)) {
					brandSet.remove(brand);
				}
			}
		}
		// 该计划下有多少个存取一体机
		String cqEupCount = GetBoxDetailListBiz.cqEupCount;
		for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
			HashSet<String> saveMoneyBoxSet = new HashSet<String>();// 存款箱类型
			String atmType = GetBoxDetailListBiz.list.get(i).getAtmType();

			int saveMoneyBoxNum = 0;// 存款箱数量
			for (int j = 0; j < Getnumber.list_boxdeatil.size(); j++) {
				String cboxnum = Getnumber.list_boxdeatil.get(j).getNum();
				String scanAtmType = Getnumber.list_boxdeatil.get(j).getAtmType();
				scanAtmType = scanAtmType.equals("0") ? "取款机" : "存款机";

				String scanBoxState = Getnumber.list_boxdeatil.get(j).getBoxState();

				/*
				 * if(scanAtmType.equals("存款机")){ saveMoneyBoxNum +=1;
				 */
				if (atmType.equals("存取一体机")) {
					saveMoneyBoxSet.add(cboxnum);
					if (scanAtmType.equals("存款机")) {
						saveMoneyBoxNum += 1;
					}
				}
				/* } */

				// 判断钞箱状态是否为00
				if (!"00".equals(scanBoxState.trim())) {
					// str.append(cboxnum+"该钞箱状态不正确，请删除；");
					boxStateSet.add(cboxnum);
				}
			}

			if (saveMoneyBoxNum != saveMoneyBoxSet.size() && saveMoneyBoxNum != 0
					&& saveMoneyBoxNum < Integer.parseInt(cqEupCount) && atmType.equals("存取一体机")) {
				String brand = GetBoxDetailListBiz.list.get(i).getBrand();
				strCQ += brand;

				// str.append(brand+"品牌需要的存款箱数量异常;");
			}
		}
		if (!brandNumSet.isEmpty()) {
			str.append("该" + brandNumSet.toString() + "品牌下钞箱数量异常；");
		}
		if (!brandSet.isEmpty()) {
			str.append(brandSet.toString() + "品牌错误；");
		}
		if (!strCQ.equals("")) {
			str.append(strCQ + "品牌需要的存款箱数量异常；");
		}
		/*
		 * if(!saveMoneyBoxSet.isEmpty()){
		 * str.append(saveMoneyBoxSet.toString()+"钞箱不是存款机；"); }
		 */

		if (!boxStateSet.isEmpty()) {
			str.append(boxStateSet.toString() + "钞箱状态异常；");
		}

		resMap.put("code", str.length());
		resMap.put("msg", str);

		return resMap;
	}

	boolean issame = true;
	StringBuffer sb = new StringBuffer();

	// 移除没有的品牌
	public void remove() {
		for (int i = 0; i < Getnumber.list_boxdeatil.size(); i++) {
			String gbrand = Getnumber.list_boxdeatil.get(i).getBrand();
			for (int j = 0; j < GetBoxDetailListBiz.list.size(); j++) {
				String brand = Getnumber.list_boxdeatil.get(i).getBrand();
				if (gbrand.equals(brand)) {
					issame = false;
				}
			}
			if (issame) {
				sb.append(i);
				issame = true;
			}
		}

		for (int i = 0; i < sb.length(); i++) {
			Getnumber.list_boxdeatil.remove(Integer.parseInt(sb.toString().charAt(i) + ""));
		}

	}

	/**
	 * 检测是否存在无效钞箱
	 * 
	 * @return
	 */
	public String checkBox(List<BoxDetail> list) {
		StringBuffer sb = new StringBuffer();
		/**
		 * modify by wangmeng 2017-07-20 返回00，表示list中为有效钞箱，品牌、状态都对 返回99，表示list是空的，不做任何操作
		 * 返回cboxnums，为要显示有问的钞箱
		 */
		if (list == null || list.size() <= 0) {
			return "99";
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getBrand().equals("无效钞箱")) {
				sb.append(list.get(i).getNum() + ",");
			}
		}
		if (sb.length() != 0) {
			String cboxnums = sb.substring(0, sb.length() - 1).toString();
			if (!"".equals(cboxnums) && cboxnums != null) {// 存在无效钞箱
				return cboxnums;
			} else {
				return "00";
			}
		} else {
			return "00";
		}

	}

}
