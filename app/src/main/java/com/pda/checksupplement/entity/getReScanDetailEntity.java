package com.pda.checksupplement.entity;

import java.io.Serializable;
import java.util.List;

/***
 * 盘库扫描实体类 19——3——8
 * 
 * @author Administrator
 * 
 */
public class getReScanDetailEntity implements Serializable {
	private String missCount; // 数量
	private List<String> filterTagList; // / 要扫的标签集合

	@Override
	public String toString() {
		return "getReScanDetailEntity [missCount=" + missCount + ", "
				+ (filterTagList != null ? "filterTagList=" + filterTagList : "") + "]";
	}

	public String getMissCount() {
		return missCount;
	}

	public void setMissCount(String missCount) {
		this.missCount = missCount;
	}

	public List<String> getFilterTagList() {
		return filterTagList;
	}

	public void setFilterTagList(List<String> filterTagList) {
		this.filterTagList = filterTagList;
	}

}
