package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
/**
 * 物流
 * @author Administrator
 *
 */
public interface KuaiDi100Service {

	JSONObject queryLogicInfo(String comNo, String kuaiDiDangNum, String sendPhone);
}
