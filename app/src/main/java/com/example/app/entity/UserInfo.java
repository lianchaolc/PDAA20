package com.example.app.entity;

import java.io.Serializable;

/**
 * 款箱或者押运任务交接时使用 lianc 猜测
 * 
 * @author Administrator
 *
 */
public class UserInfo implements Serializable {

	private String nameZhanghao;
	private String pwd;

	public String getNameZhanghao() {
		return nameZhanghao;
	}

	public void setNameZhanghao(String nameZhanghao) {
		this.nameZhanghao = nameZhanghao;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public UserInfo(String nameZhanghao, String pwd) {
		super();
		this.nameZhanghao = nameZhanghao;
		this.pwd = pwd;
	}

	public UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
