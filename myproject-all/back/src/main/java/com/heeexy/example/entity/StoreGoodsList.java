package com.heeexy.example.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Set;

/**
 *
 */
public class StoreGoodsList extends JSONObject {
	private List<JSONObject> goodsList;

	public List<JSONObject> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<JSONObject> goodsList) {
		this.goodsList = goodsList;
	}
	
}
