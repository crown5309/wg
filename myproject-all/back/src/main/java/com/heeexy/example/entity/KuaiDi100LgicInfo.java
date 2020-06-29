package com.heeexy.example.entity;

/**
 * @author xutb
 * @2019年9月29日
 * @Version 1.0
 */
public class KuaiDi100LgicInfo {
	/**
	 *  时间，原始格式
	 */
	private String time;
	/**
	 * 格式化后时间
	 */
	private String ftime;
	
	/**
	 * 快内容
	 */
	private String context;


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	
}
