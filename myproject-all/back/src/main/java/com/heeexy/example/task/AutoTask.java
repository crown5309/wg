package com.heeexy.example.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import com.alibaba.fastjson.JSONObject;
//import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.OrderDao;
import com.heeexy.example.entity.OrderInfo;

@EnableScheduling
@Component
public class AutoTask {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoTask.class);
	@Autowired
	private OrderDao orderDao;
//	@Autowired
	//private GoodsDao goodsDao;
	@Scheduled(cron = "0 0/10 * * * ?") // 订单取消
	private void process() {
		log.info("*************取消订单定时任务start***************");
		List<OrderInfo> list = orderDao.getOrderInfoByNoPay(1);
	//	List<JSONObject> goodsList = null;
		long chaTime = 1000 * 60 * 60 * 1;//10分为支付取消订单
		for (OrderInfo sg : list) {// 取消订单
			if (new Date().getTime() - sg.getDate("createTime").getTime() > chaTime) {
				orderDao.cancelOrderState(sg.getString("orderId"), 0);
			//	goodsList = (List<JSONObject>) sg.get("goodsList");
//				for (JSONObject goods : goodsList) {
//					goodsDao.addSkuCount(goods.getString("goodsId"),goods.getString("count"));
//				}
			}

			
		}
		log.info("*************取消订单定时任务结束***************");
	}
}
