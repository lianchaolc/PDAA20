package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 获取任务进行显示的实体类
 * 
 * @author Administrator
 * 
 */
public class YayunSelectRewuUserCodeEntity implements Serializable {
	private String linename;
	private List<YayunSelectRewuUserEntity> data;

	public List<YayunSelectRewuUserEntity> getData() {
		return data;
	}

	public YayunSelectRewuUserCodeEntity(List<YayunSelectRewuUserEntity> data, String linename) {
		super();
		this.data = data;
		this.linename = linename;
	}

	public YayunSelectRewuUserCodeEntity() {
		super();
	}

	public void setData(List<YayunSelectRewuUserEntity> data) {
		this.data = data;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	@Override
	public String toString() {
		return "YayunSelectRewuUserCodeEntity [" + (data != null ? "data=" + data + ", " : "")
				+ (linename != null ? "linename=" + linename : "") + "]";
	}

}
