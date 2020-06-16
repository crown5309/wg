package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface OrderGoodsDao {

	void insertGoodsBatch(@Param("list")List<JSONObject> orderGoodList);

	void insertOrderGoods(JSONObject orderGoods);

}
