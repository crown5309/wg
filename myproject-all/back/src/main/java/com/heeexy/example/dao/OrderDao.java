package com.heeexy.example.dao;

import java.util.List;

import com.heeexy.example.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface OrderDao {

	void insertOrderBatch(@Param("list")List<JSONObject> orderList);

	void insertOrder(JSONObject order);

    List<OrderInfo> getAllInfoOrderByOrderIds(@Param("orderIds")String[] orderIds,@Param("userId") String userId);

    void updateOrderState(@Param("ids")String[] split,@Param("out_trade_no") String did,@Param("state") int state,@Param("addressId")  String addressId);

	void updateByOutTradeNo(@Param("out_trade_no")String out_trade_no,@Param("state") int state);

	List<String> getOrderIdsByOutTradeNo(String out_trade_no);

	List<OrderInfo> getOrderInfoByState(@Param("state")String state,@Param("userId")String userId,@Param("pageNo") int pageNO,@Param("pageSize") int pageSize,@Param("storeId") String storeId,@Param("type") String type);

	List<OrderInfo>  getOrderInfoByNoPay(int i);

	void cancelOrderState(@Param("orderId")String orderId,@Param("state") int state);

    int countOrder(JSONObject request2Json);

	List<OrderInfo> getOrderInfoList(JSONObject request2Json);

	void updateOrderStateByOrderId(@Param("orderId")String orderId,@Param("state") String state);
}
