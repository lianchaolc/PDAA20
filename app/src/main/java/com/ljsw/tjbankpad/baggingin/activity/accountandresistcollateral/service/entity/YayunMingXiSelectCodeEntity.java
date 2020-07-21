package com.ljsw.tjbankpad.baggingin.activity.accountandresistcollateral.service.entity;

import java.io.Serializable;
import java.util.List;

public class YayunMingXiSelectCodeEntity {
	private List<YayunMingXiSelectEntity> data;

	public YayunMingXiSelectCodeEntity(List<YayunMingXiSelectEntity> data) {
		super();
		this.data = data;
	}

	@Override
	public String toString() {
		return "YayunMingXiSelectCodeEntity [" + (data != null ? "data=" + data : "") + "]";
	}

	public List<YayunMingXiSelectEntity> getData() {
		return data;
	}

	public void setData(List<YayunMingXiSelectEntity> data) {
		this.data = data;
	}

}
