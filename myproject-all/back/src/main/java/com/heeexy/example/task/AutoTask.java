package com.heeexy.example.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.ImgPathDao;
import com.heeexy.example.dao.OrderLogDao;
import com.heeexy.example.util.OrderLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.heeexy.example.dao.OrderDao;
import com.heeexy.example.entity.OrderInfo;

/**
 * 定时任务
 */
@EnableScheduling
@Component
public class AutoTask {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoTask.class);
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private OrderLogDao orderLogDao;
	@Autowired
	private ImgPathDao imgPathDao;
	@Value("${imgPath}")
	private String imgPath;
	@Scheduled(cron = "0 0/30 * * * ?") // 订单取消
	private void process() {
		log.info("*************取消订单自动取消定时任务start***************");
		List<OrderInfo> list = null;
		List<JSONObject> goodsList=null;
		JSONObject json=new JSONObject();
		json.put("pageNo", 0);
		json.put("pageSize", 100);
		json.put("state", 1);
		int count = orderDao.countOrderByState(1);
		int rate = (int) Math.ceil(count /json.getDoubleValue("pageSize"));
		for (int i = 0; i < rate; i++) {
			json.put("pageNo",i*json.getIntValue("pageSize"));
			list=orderDao.getOrderInfoByNoPay(json);
			long chaTime = 3000 * 60 * 60 * 1;// 60分x24x14为支付取消订单
			for (OrderInfo sg : list) {// 取消订单
				if (new Date().getTime() - sg.getDate("createTime").getTime() > chaTime) {
					orderDao.cancelOrderState(sg.getString("orderId"), 0);
					goodsList = (List<JSONObject>) sg.get("goodsList");
					for (JSONObject goods : goodsList) {
						goodsDao.addSkuCount(goods.getString("goodsId"), goods.getString("count"));
						OrderLogUtil.inserOrderLog(sg.getString("orderId"), "系统自动",0,"取消订单",null,orderLogDao,null);
					}
				}

			}
			
		}
		log.info("*************取消订单自动取消定时任务结束***************");
	}

	@Scheduled(cron = "59 59 23 * * ? ") // 订单自动收货 14天自动 每晚23:59:59 执行
	private void processUpdateState() {
		log.info("*************取消订单自动收货定时任务start***************");
		List<OrderInfo> list = null;
		JSONObject json=new JSONObject();
		json.put("pageNo", 0);
		json.put("pageSize", 100);
		json.put("state", 3);
		int count = orderDao.countOrderByState(3);
		int rate = (int) Math.ceil(count /json.getDoubleValue("pageSize"));
		for (int i = 0; i < rate; i++) {
			json.put("pageNo",i*json.getIntValue("pageSize"));
			list=orderDao.getOrderInfoByNoPay(json);
			long chaTime = 24 * 14 * 6000 * 60 * 60 * 1;// 60分x24x14为自动收货
			for (OrderInfo sg : list) {// 自动收货
				if (new Date().getTime() - sg.getDate("sendOutGoodsTime").getTime() > chaTime) {
					orderDao.cancelOrderState(sg.getString("orderId"), 5);
					OrderLogUtil.inserOrderLog(sg.getString("orderId"), "系统自动",5,"收货成功",null,orderLogDao,null);
				}

			}
			
		}
		log.info("*************取消订单自动收货定时任务结束***************");
	}

	/**
	 * 图片资源删除定时任务
	 */
	@Scheduled(cron = "0 0 3 * * ? ")
	private void deleteAllImg() {
		File file = null;
		List<JSONObject> list = null;
		JSONObject json=new JSONObject();
		json.put("pageNo", 0);
		json.put("pageSize", 100);
		json.put("state", 0);
		int count = imgPathDao.countImgByState(1);
		int rate = (int) Math.ceil(count /json.getDoubleValue("pageSize"));
		for (int i = 0; i < rate; i++) {
			json.put("pageNo",i*json.getIntValue("pageSize"));
			list=imgPathDao.getImgByState(json);
			for (JSONObject sg : list) {
				String img = imgPath+sg.getString("img_id");
				file=new File(img);// 读取
				if(file.delete()) {
					imgPathDao.deleteByImgId(sg.getIntValue("id"));
				}
			}

		}

	}
}
