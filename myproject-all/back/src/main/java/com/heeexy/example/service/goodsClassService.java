package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface goodsClassService {

	JSONObject listGoodsClass(JSONObject request2Json);

	JSONObject addGoodsClass(JSONObject requestJson);

	JSONObject updateGoodsClass(JSONObject requestJson);

	Object getAllGoodsClass(String appid);

}
