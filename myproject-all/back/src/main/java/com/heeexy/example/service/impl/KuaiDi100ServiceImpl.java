package com.heeexy.example.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.heeexy.example.entity.KuaiDi100QueryResp;
import com.heeexy.example.service.KuaiDi100Service;
import com.heeexy.example.util.KuaiDi100;



/**
 * 物流查询
 * @author Administrator
 *
 */
@Service
public class KuaiDi100ServiceImpl implements KuaiDi100Service {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KuaiDi100ServiceImpl.class);
    @Value("${kuai_dai_100_key:tlCldJnE5568}")
	private String key;
	@Value("${kuai_dai_100_customer:30BDA7DB7E1AA0D890E7941228A96CD9}")
	private String customer;
		
	
	@Override
	public JSONObject queryLogicInfo(String comNo, String kuaiDiDangNum, String sendPhone) {
		logger.info("《《《《《《《开始调用KuaiDi100ServiceImpl.queryLogicInfo,comNo={},kuaiDiDangNum={},sendPhone={}",comNo,kuaiDiDangNum,sendPhone);
		KuaiDi100 kd100=new KuaiDi100();
		Map<String,String> params=new HashMap<String,String>();
		params.put("com", comNo);
		params.put("num", kuaiDiDangNum);
		if(!StringUtils.isEmpty(sendPhone)) {
		  params.put("phone", sendPhone);
		}
		KuaiDi100QueryResp resp=kd100.getKuiDicContent(customer, key, params);

		logger.info("《《《《《《《kuaidi100返回信息为={}",resp);
		if(CollectionUtils.isEmpty(resp.getData())){
			return CommonUtil.successJson(resp.getMessage());
		}
		return CommonUtil.successJson(resp);
	}

}
