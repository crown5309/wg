package com.heeexy.example.frontController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.OrderService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/front")
public class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * 提交订单
	 * @param goodsId-商品id
	 * @param count-商品数量
	 * @param cartIds -购物车id集合
	 * @return
	 */
	@RequestMapping("/submitOrder")
	public Object submitOrder(String goodsId,String count,String cartIds) {
		return  CommonUtil.successJson(orderService.submitOrder(goodsId,count,cartIds));
	}
}
