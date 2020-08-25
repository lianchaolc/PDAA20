package com.ljsw.tjbankpda.qf.entity;

import java.io.Serializable;

/****
 * 20200706
 * @author Administrator
 * lianchao
 *
 *接受打印机的实体类
 */
public class ShangJiaoQingFen_o_qf_Print_Entity  implements Serializable{
	private String  code;// 打印机名称
	private String corpid;/// 
    private String id; //打印机的id
    private String ip; // ip地址
    private String port;// 端口号
    private String type; /// 类型
    public ShangJiaoQingFen_o_qf_Print_Entity() {
		super();
	}
	@Override
	public String toString() {
		return "ShangJiaoQingFen_o_qf_Print_Entity ["
				+ (code != null ? "code=" + code + ", " : "")
				+ (corpid != null ? "corpid=" + corpid + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (ip != null ? "ip=" + ip + ", " : "")
				+ (port != null ? "port=" + port + ", " : "")
				+ (type != null ? "type=" + type : "") + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCorpid() {
		return corpid;
	}
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
