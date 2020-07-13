package com.heeexy.example.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface ShopDao {

	int countShop(JSONObject request2Json);

	List<JSONObject> listShop(JSONObject request2Json);

	String addShop(JSONObject requestJson);

	void updateShop(JSONObject requestJson);

	void deleteShop(String id);

}
