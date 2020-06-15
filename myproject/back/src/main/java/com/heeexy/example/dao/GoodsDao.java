package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface GoodsDao {

	void addGoods(JSONObject request2Json);

	List<JSONObject> getAllGoodsByState(@Param("state")String state,@Param("pageSize") int pageSize,@Param("pageNo") int pageNo,@Param("appId")String appId,@Param("classId")String classId);

	void updateGoodsByIds(@Param("state")String state, @Param("ids")String[] ids);

	JSONObject getGoodsById(String goodsId);

}
