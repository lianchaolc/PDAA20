package com.moneyboxadmin.biz;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.application.GApplication;
import com.entity.BoxDetail;
import com.entity.BoxInfoByEmply;
import com.golbal.pda.GolbalUtil;
import com.moneyboxadmin.service.GetBoxDetailListService;
import com.moneyboxadmin.service.GetEmptyRecycleCashboxInDetailService;
import com.moneyboxadmin.service.GetemptyCashBoxOutDetailService;

//空钞箱出库,ATM加钞出库，钞箱装钞入库，回收钞箱入库钞箱明细,已清回收钞箱明细
public class GetBoxDetailListBiz {

	public static List<BoxDetail> list = null; // 箱子明细集合
	public static List<BoxInfoByEmply> boxInfoByEmplylist=null;//  空钞箱集合

	public Handler hand_detail;
	public static int boxCount = 0; // 空钞箱出库数量
	// 判断是否已经经过钞箱加钞操作,00表示所有钞箱都已通过,03表示有未完成的钞箱
	public String checkTask = "AA";

	public static String cqEupCount = "";// 该计划下有多少个存取一体机

	// 工具类
	private GolbalUtil getUtil;

	GolbalUtil getGetUtil() {
		return getUtil = getUtil == null ? new GolbalUtil() : getUtil;
	}

	// 已清回收钞箱明细2021.4.26
	GetEmptyRecycleCashboxInDetailService getEmptyRecycleCashboxInDetail;

	GetEmptyRecycleCashboxInDetailService getGetEmptyRecycleCashboxInDetail() {
		return getEmptyRecycleCashboxInDetail = getEmptyRecycleCashboxInDetail == null
				? new GetEmptyRecycleCashboxInDetailService()
				: getEmptyRecycleCashboxInDetail;
	}

	// 空钞箱出库存明细
	GetemptyCashBoxOutDetailService emptyCashBoxOutDetail;

	GetemptyCashBoxOutDetailService getEmptyCashBoxOutDetail() {
		return emptyCashBoxOutDetail = emptyCashBoxOutDetail == null ? new GetemptyCashBoxOutDetailService()
				: emptyCashBoxOutDetail;
	}

	// ATM加钞出库，钞箱装钞入库，回收钞箱入库钞箱明细
	GetBoxDetailListService boxDetailList;

	GetBoxDetailListService getBoxDetailList() {
		return boxDetailList = boxDetailList == null ? new GetBoxDetailListService() : boxDetailList;
	}

	/**
	 * 
	 * @param plan    业务编号
	 * @param bizName 根据业务名称传入不同的值
	 */
	public void getBoxDetailList(String plan, String bizName) {
		Log.i("plan", plan);
		Log.i("bizName明细", bizName);
		String type = "";

		if (getGetUtil().onclicks) {
			if (bizName.equals("ATM加钞出库")) {
				type = "1";
			} else if (bizName.equals("钞箱装钞入库")) {
				type = "2";
			} else if (bizName.equals("回收钞箱入库")) {
				type = "3";
			} else if (bizName.equals("未清回收钞箱出库") || bizName.equals("未清回收钞箱出库-item")) {
				type = "4";
			}
			AsyncTaskBoxDetail asyn = new AsyncTaskBoxDetail(plan, bizName, type);
			asyn.execute();
			getGetUtil().onclicks = false;
		}

	}

	/**
	 * 开始获取数据
	 * 
	 * @author Administrator
	 *
	 */
	private class AsyncTaskBoxDetail extends AsyncTask {
		String planNum;
		String bizName;
		String type;
		Message m;

		public AsyncTaskBoxDetail() {
		};

		public AsyncTaskBoxDetail(String planNum, String bizName, String type) {
			this.planNum = planNum;
			this.bizName = bizName;
			this.type = type;
			m = hand_detail.obtainMessage();

		};

		// 取消任务后的操作
		@Override
		protected void onCancelled() {
			super.onCancelled();
			getGetUtil().onclicks = true;
			// m.what=-1;
			hand_detail.sendMessage(m);
		}

		// 异步完成后的操作
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			getGetUtil().onclicks = true;
			if (list == null) {
				m.what = 0;
			} else {
				m.what = 1;

			}
			hand_detail.sendMessage(m);
		}

		// 异步后台操作
		@Override
		protected Object doInBackground(Object... arg0) {
			Log.i("00000", bizName);
			try {
				// 已清回收钞箱入库明细
				if (bizName.equals("已清回收钞箱入库")) {
					Log.i("111", bizName);
					list = getGetEmptyRecycleCashboxInDetail().getEmptyRecycleCashboxInList(planNum);
					boxCount = list.size();

					// 空钞箱出库明细
				} else if ("空钞箱出库".equals(bizName)) {

//					Map<String, Object> map = getEmptyCashBoxOutDetail().getemptyCashBoxOutDetail(planNum);
					Map<String, Object> map=getEmptyCashBoxOutDetail().getemptyCashBoxOutDetailinfo(planNum);
					list = (ArrayList<BoxDetail>) map.get("list");
//					boxInfoByEmplylist = (ArrayList<BoxInfoByEmply>) map.get("list");
					cqEupCount = (String) map.get("count");
					boxCount = 0;
					// 获取总钞箱数量
					for (int i = 0; i < list.size(); i++) {
						boxCount = boxCount + Integer.parseInt(list.get(i).getNum()); // 箱子数量
//						boxCount = boxCount +Integer.parseInt(boxInfoByEmplylist.get(i).getBrandcount());
					}

					Log.i("222", bizName);
					//
				} else {
					list = getBoxDetailList().getCashBoxDetail(planNum, type);  //java.lang.NullPointerException: Attempt to invoke interface method 'int java.util.List.size()' on a null object reference
					boxCount = list.size(); // 箱子数量
					Log.i("333", bizName);
				}

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				m.what = -4;
				this.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
				m.what = -1;
				this.cancel(true);
			}
			return null;
		}
	}

	/**
	 * modeify by wangmeng 2017-08-02 如果该任务未进行钞箱加钞，要阻止该任务进行下去
	 */
	public void checkTaskState(String planNum) {
		AsyncTaskCheckTask asyn = new AsyncTaskCheckTask(planNum);
		asyn.execute();
	}

	private class AsyncTaskCheckTask extends AsyncTask {
		String planNum;
		Message m;

		public AsyncTaskCheckTask() {
		};

		public AsyncTaskCheckTask(String planNum) {
			this.planNum = planNum;
			m = new Message();
			// m = hand_detail.obtainMessage();
		};

		// 异步完成后的操作
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			getGetUtil().onclicks = true;
			if (checkTask.equals("AA") || checkTask.equals("99")) {
				m.what = -2;
			} else if (checkTask.equals("00")) {
				m.what = 11;
			} else if (checkTask.equals("03")) {
				m.what = 12;
			}
			hand_detail.sendMessage(m);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			Log.i("--poka--", "判断是否已经经过钞箱加钞操作");
			try {
				checkTask = getBoxDetailList().checkTaskState(planNum);
				Bundle bundle = new Bundle();
				bundle.putString("checkTask", checkTask);
				m.setData(bundle);
				Log.i("--poka--", "查询返回结果为：" + checkTask);
			} catch (Exception e) {
				e.printStackTrace();
				m.what = -1;
				this.cancel(true);
				Log.i("--poka--", "error：" + e.getMessage());
			}
			return null;
		}

	}
}
