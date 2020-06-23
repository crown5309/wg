package com.heeexy.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.IndexConfigDao;
import com.heeexy.example.entity.ClassesInfo;
import com.heeexy.example.service.IndexConfigService;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
	@Autowired
	private IndexConfigDao indexConfigDao;
	@Override
	public List<JSONObject> listIndexConfig(JSONObject request2Json) {
		// TODO Auto-generated method stubrequest2Json
		String type=request2Json.getString("type");
		List<JSONObject> js=null;
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

	@Override
	public JSONObject listIndexAll(String appId) {
		// TODO Auto-generated method stub
		JSONObject json=new JSONObject();
		List<ClassesInfo> listIndexAll = indexConfigDao.listIndexAll(appId);
		//获取banner
		List<JSONObject> listIndexConfigBanner = indexConfigDao.listIndexConfigBanner("banner");
		json.put("classList", listIndexAll);
		json.put("bannerList", listIndexConfigBanner);
		return json;
	}

}
