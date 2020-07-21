package com.ljsw.tjbankpda.qf.entity;

import java.io.Serializable;

import com.ljsw.tjbankpda.util.Column;

/**
 * 2019_1_9抵制押品的实体类
 * 
 * @author Administrator
 *
 */
public class CollateralTurnOver implements Serializable {

	public CollateralTurnOver(String dizhibianhao, String dizhinum) {
		super();
		this.dizhibianhao = dizhibianhao;
		this.dizhinum = dizhinum;
	}

	public CollateralTurnOver(String dizhibianhao) {
		super();
		this.dizhibianhao = dizhibianhao;

	}

	private String dizhibianhao; // 抵制数量
	private String dizhinum;// 抵制号

	@Override
	public String toString() {
		return "CollateralTurnOver [" + (dizhibianhao != null ? "dizhibianhao=" + dizhibianhao + ", " : "")
				+ (dizhinum != null ? "dizhinum=" + dizhinum : "") + "]";
	}

	public String getDizhibianhao() {
		return dizhibianhao;
	}

	public void setDizhibianhao(String dizhibianhao) {
		this.dizhibianhao = dizhibianhao;
	}

	public String getDizhinum() {
		return dizhinum;
	}

	public void setDizhinum(String dizhinum) {
		this.dizhinum = dizhinum;
	}

	public CollateralTurnOver() {
		super();
	}

}
