package com.heeexy.example.service;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

	Object submitOrder(String goodsId, String count, String cartIds);

	Object pay(String orderIds, String addressId,String remarks,HttpServletRequest request);

	Object getOrderInfo(String orderIds,String addressId);

	void updateByOutTradeNo(String out_trade_no, int i);
}
