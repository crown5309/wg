package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface ShopClassService {

	JSONObject listShopClass(JSONObject request2Json);

	JSONObject addShopClass(JSONObject requestJson);

	JSONObject updateShopClass(JSONObject requestJson);

	JSONObject deleteShopClass(String id);

}
