package com.heeexy.example.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface GoodsClassDao {

	int countGoodsClass(JSONObject request2Json);

	List<JSONObject> listGoodsClass(JSONObject request2Json);

	void updateGoodsClass(JSONObject requestJson);

	void addGoodsClass(JSONObject requestJson);

	List<JSONObject> getAllGoodsClass(String appid);

}
