package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.entity.StoreGoodsList;

public interface CartDao {

	List<StoreGoodsList> getStoreList(@Param("cartIds")String[] cartIds,@Param("userId") String userId);

	JSONObject getCartByGoodsId(@Param("goodsId")String goodsId, @Param("userId")String userId);

	void updateCartCountById(@Param("id")String id, @Param("count")int count);

	void insert(JSONObject cart);

	void deleteMyCartById(String id);

	List<JSONObject> getMyCartList(@Param("pageNo")Integer pageNo, @Param("pageSize")Integer pageSize,@Param("userId") String userId);

	void deleteBatch(@Param("ids")String[] split);

	void deleteMyCartByGoodsId(@Param("goodsId")String goodsId, @Param("userId")String userId);

}
