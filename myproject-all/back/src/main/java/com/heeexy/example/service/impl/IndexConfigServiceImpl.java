package com.heeexy.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.IndexConfigDao;
import com.heeexy.example.service.IndexConfigService;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
	@Autowired
	private IndexConfigDao indexConfigDao;
	@Override
	public JSONObject listIndexConfig(JSONObject request2Json) {
		// TODO Auto-generated method stubrequest2Json
		String type=request2Json.getString("type");
		JSONObject js=null;
		if("classes".equals(type)) {
			js=indexConfigDao.listIndexConfigClasses(type);
		}else {
			js=indexConfigDao.listIndexConfigBanner(type);
		}
		return js;
	}

	@Override
	public JSONObject addIndexConfig(JSONObject requestJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject updateIndexConfig(JSONObject requestJson) {
		// TODO Auto-generated method stub
		return null;
	}

}
