package hdjc.rfid.operator;

import android.content.Context;

public interface IRFID_Device {
	// 多次扫描RFID接口
	public void addNotifly(INotify notify);

	// 一维码接口
	public void scanOpen();// 打开一维码扫描

	public void scanclose();// 关闭一维码扫描

	public String readScan(String scanCodeing);// 获取扫描到的一维码

	// 指纹接口
	public void fingerOpen();// 打开指纹

	public void fingerClose();// 关闭指纹

	public void fingerPressState();// 是否按压手指

	public void fingerIval();// 获取指纹特征值

	public void open_a20(); // 打开RFID

	public void close_a20(); // 关闭RFID

	public void stop_a20(); // 暂停扫描

	public void start_a20(); // 开始扫描

}
