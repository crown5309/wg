package com.heeexy.example.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface IndexConfigService {

	List<JSONObject> listIndexConfig(JSONObject request2Json);

	JSONObject addIndexConfig(JSONObject requestJson);

	JSONObject updateIndexConfig(JSONObject requestJson);

	JSONObject listIndexAll(String appId);

	Object listIndexClassAll(String appId);

	Object listClassAll(String appId);

	Object addIndexClass(String json);

	JSONObject listBanner(JSONObject request2Json);

}
