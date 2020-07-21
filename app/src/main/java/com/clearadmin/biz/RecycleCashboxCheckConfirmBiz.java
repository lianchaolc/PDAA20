package com.clearadmin.biz;

import java.net.SocketTimeoutException;

import com.clearadmin.service.RecycleCashboxCheckConfirmService;
import com.golbal.pda.GolbalUtil;

import android.os.Handler;
import android.os.Message;

public class RecycleCashboxCheckConfirmBiz {
	// 回收钞箱清点-确定

	public Handler handler;
	RecycleCashboxCheckConfirmService recycleCashboxCheckConfirm;

	public RecycleCashboxCheckConfirmService getRecycleCashboxCheckConfirm() {
		return recycleCashboxCheckConfirm = recycleCashboxCheckConfirm == null ? new RecycleCashboxCheckConfirmService()
				: recycleCashboxCheckConfirm;
	}

	/**
	 * 
	 * @param cashboxNum:钞箱编号;
	 * @param orderNum:业务单编号;
	 * @param balance1:钞箱余额;
	 * @param balance2:废钞箱余额;
	 * @param userId:用户编号
	 * @return
	 * @throws Exception
	 */
	public void getCheckConfirmResult(String cashboxNum, String orderNum, String balance1, String balance2,
			String userId1, String userId2) {
		if (GolbalUtil.onclicks) {
			GolbalUtil.onclicks = false;
			Thread t = new Thread(new Run(cashboxNum, orderNum, balance1, balance2, userId1, userId2));
			t.start();

		}
	}

	class Run implements Runnable {
		String cashboxNum;
		String orderNum;
		String balance1;
		String balance2;
		String userId1;
		String userId2;
		Message m;

		public Run() {
		};

		public Run(String cashboxNum, String orderNum, String balance1, String balance2, String userId1,
				String userId2) {
			this.cashboxNum = cashboxNum;
			this.orderNum = orderNum;
			this.balance1 = balance1;
			this.balance2 = balance2;
			this.userId1 = userId1;
			this.userId2 = userId2;
			m = handler.obtainMessage();
		};

		@Override
		public void run() {
			try {
				/**
				 * modify by wangmeng 2017-07-28 判断该订单是否已审核，添加限制，返回00，则通过；返回99则表示已审核，不能再清分
				 * 
				 */
				String code = getRecycleCashboxCheckConfirm().checkAtmPlanState(orderNum, cashboxNum);
				if ("01".equals(code)) {// 表示已审核
					m.what = -88;
				} else if ("00".equals(code)) {
					if (getRecycleCashboxCheckConfirm().recycleCashboxCheckConfirm(cashboxNum, orderNum, balance1,
							balance2, userId1, userId2)) {
						m.what = 1;
					} else {
						m.what = 0;
					}
				} else {
					m.what = 0;
				}

			} catch (SocketTimeoutException e) {
				m.what = -4;
			} catch (Exception e) {
				m.what = -1;
			} finally {
				handler.sendMessage(m);
				GolbalUtil.onclicks = true;
			}

		}

	}
}
