package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface ShopService {

	JSONObject listShop(JSONObject request2Json);

	JSONObject addShop(JSONObject requestJson);

	JSONObject updateShop(JSONObject requestJson);

	JSONObject deleteShop(String id);

}
