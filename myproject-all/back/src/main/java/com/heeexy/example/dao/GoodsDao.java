package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface GoodsDao {

	void addGoods(JSONObject request2Json);

	List<JSONObject> getAllGoodsByState(@Param("state")String state,@Param("pageSize") int pageSize,@Param("pageNo") int pageNo,@Param("appId")String appId,@Param("classId")String classId,@Param("goodsName") String goodsName, @Param("type")String type,@Param("priceFlag") String priceFlag);

	void updateGoodsByIds(@Param("state")String state, @Param("ids")String[] ids);

	JSONObject getGoodsById(String goodsId);

	String updateGoodsStoreSku(List<JSONObject> orderGoodList);

	int updateGoodStoreCount(@Param("goodsId")String goodsId,@Param("count") int count);

	void updateCountById(@Param("goodsId")String goodsId,@Param("count") String count);

	void addSkuCount(@Param("goodsId")String goodsId,@Param("count") String count);

	int countGoods(JSONObject request2Json);

	List<JSONObject> listGoods(JSONObject request2Json);

	List<JSONObject> listIndexGoods(@Param("appId")String appId,@Param("classId") String classId, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

}
