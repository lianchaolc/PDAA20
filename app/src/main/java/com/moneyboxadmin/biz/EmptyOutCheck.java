package com.moneyboxadmin.biz;

import android.util.ArraySet;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.entity.Box;
import com.entity.BoxDetail;
import com.entity.BoxInfoByEmply;
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
    Map<String, BoxDetail> hashmap2 = new HashMap<String, BoxDetail>();  // 钞箱状态// 存放钞箱标牌2 钞箱类型/存款箱 / 取款箱子
    HashSet<String> boxStateSet = new HashSet<String>();// 钞箱状态

    private List<BoxDetail> boxDetailslist = new ArrayList<>();
    Set<BoxDetail> hs = new HashSet();
    HashSet<String> typecount = new HashSet<String>();// 与服务端 相同品牌和数量下   存在存款机和取款机数量要求一致

    private List<BoxDetail> listDetail = new ArrayList<>();
    private List<BoxDetail> listDetail2 = new ArrayList<>();
    Map<String, Object> mapsett = new HashMap<>();//名牌和数量ATM数量

    public Map<String, Object> check() {
        hs.clear();
        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {

            hashmap.put(GetBoxDetailListBiz.list.get(i).getBrand(), 0);
            BoxDetail n = new BoxDetail();
            n.setAtmType(GetBoxDetailListBiz.list.get(i).getAtmType());
            hashmap2.put(GetBoxDetailListBiz.list.get(i).getBrand(), n);
            brandSet.add(GetBoxDetailListBiz.list.get(i).getBrand());
        }

        boxDetailslist = Getnumber.list_boxdeatil;
        for (int i = 0; i < Getnumber.list_boxdeatil.size(); i++) {
            String brand = Getnumber.list_boxdeatil.get(i).getBrand();
            String brandstate = Getnumber.list_boxdeatil.get(i).getBoxState();
            String brandsAtmtype = Getnumber.list_boxdeatil.get(i).getAtmType();
//            for (Map.Entry<String, Integer> item : hashmap.entrySet()) {
//                if (item.getKey().equals(brand)) {
//                    int n = item.getValue();
//                    n++;
//                    item.setValue(n);
//                    break;
//                } else {
//                    brandSet.add(brand);
//                }
//            }
            for (Map.Entry<String, BoxDetail> item : hashmap2.entrySet()) {
                BoxDetail n = item.getValue();
                String sATmtype = n.getAtmType();
                if (item.getKey().equals(brand)) {
                    BoxDetail boxDetailoxd = item.getValue();
                    if (brandsAtmtype.equals(boxDetailoxd.getAtmType())) {
//                    n++;
                        int count = 0;
                        count++;
//                        n.setBoxcout("" + count);
                        n.setBrand(brand);
                        n.setBoxState(brandstate);
                        n.setAtmType(brandsAtmtype);
                        item.setValue(n);
                        break;
                    }
                } else {
                    brandSet.add(brand);
                }
            }
        }
//        2021.4.26  lianchao    做分类代码  网络数据和本地数据做对比区分箱子是否符合  网络请求的数据代码
        listDetail.clear();
        BoxDetail boxDetailcun = null;
        BoxDetail boxDetailqu = null;
        ;
        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {

            String brand = GetBoxDetailListBiz.list.get(i).getBrand();
            String num = GetBoxDetailListBiz.list.get(i).getNum();
            Integer intNum = Integer.parseInt(num);


//            进行分类
            for (int j = 0; j < boxDetailslist.size(); j++) {
                int cuncount = 0;
                int qucount = 0;
                String atmtype = "";  ///款钞箱
                if (boxDetailslist.get(j).getBrand().equals(GetBoxDetailListBiz.list.get(i).getBrand())) {
                    if (boxDetailslist.get(j).getAtmType().equals("0")) {//取款钞箱
                        atmtype = "取款钞箱";
                    } else if (boxDetailslist.get(j).getAtmType().equals("1")) { //存款钞箱
                        atmtype = "存款钞箱";
                    }
                    if (GetBoxDetailListBiz.list.get(i).getAtmType().equals(atmtype) &&
                            GetBoxDetailListBiz.list.get(i).getAtmType().equals("存款钞箱")) {
//                        if (boxDetailslist.get(j).getBrand().equals(GetBoxDetailListBiz.list.get(i).getBrand())) {
                        cuncount++;
                        boxDetailcun = new BoxDetail();
                        boxDetailcun.setBrand(boxDetailslist.get(j).getBrand());
                        boxDetailcun.setAtmType(GetBoxDetailListBiz.list.get(i).getAtmType());
                        boxDetailcun.setBoxState(boxDetailslist.get(j).getBoxState());
                        boxDetailcun.setNum(cuncount + "");
//                        private String Num; // 钞箱编号
//                        private String money; // 加钞金额 或者 核心余额
//                        private String atmType; // ATM机类型：取款机|存取一体机
                        mapsett.put(GetBoxDetailListBiz.list.get(i).getAtmType(), cuncount);
                        listDetail.add(boxDetailcun);
//                        }
                    } else if (GetBoxDetailListBiz.list.get(i).getAtmType().equals(atmtype) && atmtype.equals("取款钞箱")) {
                        qucount++;
//                        if(null==listDetail){
                        boxDetailqu = new BoxDetail();
                        boxDetailqu.setBrand(boxDetailslist.get(j).getBrand());
                        boxDetailqu.setAtmType(GetBoxDetailListBiz.list.get(i).getAtmType());
                        boxDetailqu.setBoxState(boxDetailslist.get(j).getBoxState());
                        boxDetailqu.setNum(qucount + "");
                        listDetail.add(boxDetailqu);
//                        }else{
//                            if(listDetail.contains(boxDetailslist.get(j).getBrand())){

//                            }

                    }
                }

//                }
            }

        }

//        在 次分组
        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {

            String brand = GetBoxDetailListBiz.list.get(i).getBrand();
            String num = GetBoxDetailListBiz.list.get(i).getNum();
            Integer intNum = Integer.parseInt(num);
            String str_type = GetBoxDetailListBiz.list.get(i).getAtmType();
            int intcountatm = 0;
            for (int p = 0; p < listDetail.size(); p++) {
                String name = listDetail.get(p).getBrand();
                String strAtmtype = listDetail.get(p).getAtmType();
                String stratmcount = listDetail.get(p).getNum();
                if (brand.equals(name)) {
                    if (str_type.equals(strAtmtype)) {//  cunkuangji qukaungji

                        if (null == listDetail2) {
                            boxDetailqu = new BoxDetail();
                            boxDetailqu.setBrand(name);
                            boxDetailqu.setAtmType(strAtmtype);
                            int add = intcountatm++;
                            boxDetailqu.setNum(add + "");
                            listDetail2.add(boxDetailqu);
                        } else if (listDetail2.contains(name)) {
                            if (listDetail2.contains(listDetail.get(p).getBrand()) && listDetail2.contains(strAtmtype)) {
                                int add1 = intcountatm++;
                                boxDetailqu.setNum(add1 + "");
                                listDetail2.add(boxDetailqu);
                            }
                        }


                    }

                }
            }
        }

        Log.e("EmptyOutCheck", "====" + listDetail2.size() + "");
// 创建一个集合
        List<BoxDetail> list = new ArrayList<>();
        Map<String, Integer> mapAll = new HashMap<>();
        for (int p = 0; p < listDetail.size(); p++) {
            BoxDetail boxDetail = listDetail.get(p);
            String key = boxDetail.getBrand() + "," + boxDetail.getAtmType();
            if (mapAll.get(key) == null) {
                mapAll.put(key, Integer.parseInt(boxDetail.getNum()));
            } else {
                mapAll.put(key, Integer.parseInt(boxDetail.getNum()) + mapAll.get(key));
            }
        }
        Iterator iter = mapAll.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            BoxDetail boxDetail = new BoxDetail();
            String[] arr = key.split(",");
            boxDetail.setBrand(arr[0]);
            boxDetail.setAtmType(arr[1]);
            boxDetail.setNum(value);
            list.add(boxDetail);
        }


        Log.e("EmptyOutCheck", "====" + list.size() + "");


        /**
         * 网络数据代码和分组代码比较去重复
         * */
        int cuncout = 0;
        int countqu = 0;

//        if(GetBoxDetailListBiz.list.size()!=listDetail.size()){
//            str.append("该" + "品牌下钞箱数量不一致！！！；");
//        }else{

        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
            String brand = GetBoxDetailListBiz.list.get(i).getBrand();
            String num = GetBoxDetailListBiz.list.get(i).getNum();
            String GetBizAtmtype = GetBoxDetailListBiz.list.get(i).getAtmType();
            Integer intNum = Integer.parseInt(num);
            Log.e("EmptyOutCheck", "====" + hs.size() + "");
            for (int j = 0; j < list.size(); j++) {
                Log.e("EmptyOutCheck", "====" + listDetail.size() + "");

                String count = list.get(j).getNum();
                Integer intcount = Integer.parseInt(count);
                String strAtmtype = list.get(j).getAtmType();
                String name = list.get(j).getBrand();
                if (name.equals(brand)) {
                    if (GetBizAtmtype.equals(strAtmtype)) {
                        if (intNum != intcount) {
                            brandNumSet.add(brand);
                        }
                        if (brandSet.contains(brand)) {
                            brandSet.remove(brand);
                        }
                    }
                }
            }


        }

//            for (Map.Entry<String, BoxDetail> item1 : hashmap2.entrySet()) {
//                String mbrand = item1.getKey();
//                BoxDetail boxDetail = item1.getValue();
//                String boxdetailnum = boxDetail.getBoxcout();
//                String ATMtype = boxDetail.getAtmType();
//                Integer intMNum = Integer.parseInt(boxdetailnum);
//                if (brand.equals(mbrand)) {
//                    if (intNum != intMNum) {
//                        brandNumSet.add(brand);
//                        // str.append(brand+"品牌数量异常；");
//                        /*
//                         * if(intMNum > intNum) {//扫描到的钞箱多余应出库的钞箱 int a = intMNum-intNum;
//						 * str.append("请删除该品牌的" +a+"个箱子"); } else if(intMNum < intNum){//扫描到的钞箱少余应出库的钞箱
//						 * int a = intNum - intMNum; str.append("请再扫描该品牌的" +a+"个箱子"); } str.append(";");
//						 */
//                    } /*
//                         * else { rightNum +=intMNum;//品牌和数量都对，rightNum增加intMNum }
//						 */
//                }
//                // brandSet包涵所有的品牌数据，brand为库中数据，过滤掉准确的品牌，剩下无效品牌
//                if (brandSet.contains(brand)) {
//                    brandSet.remove(brand);
//                }
//            }
//        }
//        }
        // 该计划下有多少个存取一体机  afdasdf afdasfa dsadfdafafafdafdafasd
        String cqEupCount = GetBoxDetailListBiz.cqEupCount;
        for (int i = 0; i < GetBoxDetailListBiz.list.size(); i++) {
            HashSet<String> saveMoneyBoxSet = new HashSet<String>();// 存款箱类型
            String atmType = GetBoxDetailListBiz.list.get(i).getAtmType();

            int saveMoneyBoxNum = 0;// 存款箱数量
            for (int j = 0; j < Getnumber.list_boxdeatil.size(); j++) {
                String cboxnum = Getnumber.list_boxdeatil.get(j).getNum();
                String scanAtmType = Getnumber.list_boxdeatil.get(j).getAtmType();
                if (null == scanAtmType || scanAtmType.equals("")) {
                    Log.e("EmptyOutCheck", "传递数据null ");
                } else {
                    scanAtmType = scanAtmType.equals("0") ? "取款机" : "存款机";

                    String scanBoxState = Getnumber.list_boxdeatil.get(j).getBoxState();

				/*
                 * if(scanAtmType.equals("存款机")){ saveMoneyBoxNum +=1;
				 */
//                if (atmType.equals("存取一体机")) {
//                    saveMoneyBoxSet.add(cboxnum);
//                    if (scanAtmType.equals("存款机")) {
//                        saveMoneyBoxNum += 1;
//                    }
//                }
                    if (atmType.equals("取款机")) {
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

            }

            if (saveMoneyBoxNum != saveMoneyBoxSet.size() && saveMoneyBoxNum != 0
                    && saveMoneyBoxNum < Integer.parseInt(cqEupCount) && atmType.equals("存取一体机")) {
                String brand = GetBoxDetailListBiz.list.get(i).getBrand();
                strCQ += brand;

                // str.append(brand+"品牌需要的存款箱数量异常;");
            }
        }
        //  钞箱类型不正确(存取类型下的数量与 服务端数据一致)
        if (!typecount.isEmpty()) {

        }
        if (!brandNumSet.isEmpty()) {
            str.append("该" + brandNumSet.toString() + "品牌下钞箱数量异常；");
        }
        if (!brandSet.isEmpty()) {
            str.append(brandSet.toString() + "品牌错误；");
        }
//        if (!strCQ.equals("")) {//因没有存取一体机概念 2021.4.29
//            str.append(strCQ + "品牌需要的存款箱数量异常；");
//        }
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
