package com.heeexy.example.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * @author: hxy
 * @description: MyBatis的一对多JSON返回对象
 * <p>
 * 处理嵌套查询结果时，MyBatis会根据bean定义的属性类型来初始化嵌套的成员变量，
 * 主要看其是不是Collection
 * 如果这里不定义，那么嵌套返回结果里就只能返回一对一的结果，而不是一对多的
 * <p>
 * 参见MyBatis  DefaultResultSetHandler.instantiateCollectionPropertyIfAppropriate()
 * @date: 2017/10/24 10:17
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
