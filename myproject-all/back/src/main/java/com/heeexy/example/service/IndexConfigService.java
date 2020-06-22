package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface IndexConfigService {

	JSONObject listIndexConfig(JSONObject request2Json);

	JSONObject addIndexConfig(JSONObject requestJson);

	JSONObject updateIndexConfig(JSONObject requestJson);

}
