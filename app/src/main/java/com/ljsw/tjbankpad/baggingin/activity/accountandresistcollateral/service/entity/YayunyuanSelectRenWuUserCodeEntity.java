package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;
/***
 * 派工单的实体类   包含上缴派工单和请领的派工单
 * linac
 * 2019。12。25
 */
import java.util.List;

/***
 * 线路名称和线路编号外层实体类
 * 
 * @author Administrator
 *
 */
public class YayunyuanSelectRenWuUserCodeEntity implements Serializable {
	public YayunyuanSelectRenWuUserCodeEntity(String linename, List<YayunyuanSelectRenWuUserBaseEntity> data,
			String linenum) {
		super();
		this.linename = linename;
		this.data = data;
		this.linenum = linenum;
	}

	public YayunyuanSelectRenWuUserCodeEntity() {
		super();
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public List<YayunyuanSelectRenWuUserBaseEntity> getData() {
		return data;
	}

	public void setData(List<YayunyuanSelectRenWuUserBaseEntity> data) {
		this.data = data;
	}

	public String getLinenum() {
		return linenum;
	}

	@Override
	public String toString() {
		return "YayunyuanSelectRenWuUserCodeEntity [" + (linename != null ? "linename=" + linename + ", " : "")
				+ (data != null ? "data=" + data + ", " : "") + (linenum != null ? "linenum=" + linenum : "") + "]";
	}

	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}

	private String linename;
	private List<YayunyuanSelectRenWuUserBaseEntity> data;
	private String linenum;
}
