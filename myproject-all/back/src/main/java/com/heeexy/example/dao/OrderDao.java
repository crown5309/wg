package com.heeexy.example.dao;

import java.util.List;

import com.heeexy.example.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface OrderDao {

	void insertOrderBatch(@Param("list")List<JSONObject> orderList);

	void insertOrder(JSONObject order);

    List<OrderInfo> getAllInfoOrderByOrderIds(String[] orderIds);

	void updateOrderState(@Param("ids")String[] split,@Param("out_trade_no") String did,@Param("state") int state);

	void updateByOutTradeNo(@Param("out_trade_no")String out_trade_no,@Param("state") int state);

	List<String> getOrderIdsByOutTradeNo(String out_trade_no);

	List<JSONObject> getOrderInfoByState(@Param("state")String state,@Param("userId")String userId);
}
