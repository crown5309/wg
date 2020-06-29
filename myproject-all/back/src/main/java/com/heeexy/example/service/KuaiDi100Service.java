package com.heeexy.example.service;

import com.heeexy.example.entity.KuaiDi100QueryResp;
/**
 * 物流
 * @author Administrator
 *
 */
public interface KuaiDi100Service {

	KuaiDi100QueryResp queryLogicInfo(String comNo, String kuaiDiDangNum, String sendPhone);
}
