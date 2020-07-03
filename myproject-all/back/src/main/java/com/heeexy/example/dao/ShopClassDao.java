package com.heeexy.example.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface ShopClassDao {

	int countShopClass(JSONObject request2Json);

	List<JSONObject> listShopClass(JSONObject request2Json);

	void addShopClass(JSONObject requestJson);

	void updateShopClass(JSONObject requestJson);

	void deleteShopClass(String id);

}
