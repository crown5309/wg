package com.heeexy.example.entity;

import java.util.List;


public class KuaiDi100QueryResp {
	private String message;
	/**
	 * 包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态
	 */
	private String state;
	/**
	 * 忽略
	 */
	private String status;
	/**
	 * 忽略
	 */
	private String condition;
    /**
     * 忽略
     */
	private String ischeck;
	/**
	 * 快递公司编码,一律用小写字母
	 */
	private String com;
    /**
     * 订单号
     */
	private String nu;
	/**
	* 最新查询结果，数组，包含多项，全量，倒序（即时间最新的在最前），每项都是对象，对象包含字段请展开
	*/
	private List<KuaiDi100LgicInfo> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public List<KuaiDi100LgicInfo> getData() {
		return data;
	}

	public void setData(List<KuaiDi100LgicInfo> data) {
		this.data = data;
	}
	

}
