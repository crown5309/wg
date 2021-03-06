package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

	Object submitOrder(String goodsId, String count, String cartIds);

	Object pay(String orderIds, String addressId,String remarks,HttpServletRequest request);

	Object getOrderInfo(String orderIds,String addressId,String type);

	void updateByOutTradeNo(String out_trade_no, int i);

	Object getOrderInfoList(JSONObject request2Json);

	Object getOrderInfoByState(String state,int pageNo,int pageSize, String type);

	Object updateOrderState(String orderId, String state);
}
