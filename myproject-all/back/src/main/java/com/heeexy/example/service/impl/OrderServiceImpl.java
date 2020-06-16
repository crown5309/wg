package com.heeexy.example.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CartDao;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.OrderDao;
import com.heeexy.example.dao.OrderGoodsDao;
import com.heeexy.example.entity.StoreGoodsList;
import com.heeexy.example.service.OrderService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.OrderIdFactory;
import com.heeexy.example.util.StringTools;
import com.heeexy.example.util.UUIDUtil;
import com.heeexy.example.util.constants.Constants;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private CartDao cartDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;

	@Override
	@Transactional
	public Object submitOrder(String goodsId, String goodsCount, String cartIds) {

		// 从session获取用户信息

		/* Session session = SecurityUtils.getSubject().getSession(); JSONObject
		 userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);*/

		String userId ="11";// userInfo.getString("userId");
		//订单id
		String orderId = OrderIdFactory.getOrderIdByUUIdAndDate();
		List<String> orderIds=new ArrayList<String>();//返回订单列表id
		// TODO Auto-generated method stub
		if (StringTools.isNullOrEmpty(goodsId) && !StringTools.isNullOrEmpty(cartIds)) {// 从购物车取信息
			orderId = OrderIdFactory.getOrderIdByUUIdAndDate();
			orderIds.add(orderId);
			List<StoreGoodsList> list=cartDao.getStoreList(cartIds);
			List<JSONObject> goodsList=null;
			List<JSONObject> orderList=new ArrayList<JSONObject>();//需要插入的订单集合
			JSONObject order=null;
			List<JSONObject> orderGoodList=new ArrayList<JSONObject>();//需要插入的商品集合
			JSONObject orderGoods=null;
			Map<String,BigDecimal> map=null;
			BigDecimal totalPay=null;//总价
			BigDecimal discountPay=null;//优惠价
			BigDecimal practicePay=null;//实际价
			for(StoreGoodsList sg:list) {//订单拆分对应商家
				goodsList =(List<JSONObject>) sg.get("goodsList");
				 totalPay=new BigDecimal(0);//总价
			     discountPay=new BigDecimal(0);//优惠价
				 practicePay=new BigDecimal(0);//实际价
				 for(JSONObject goods:goodsList) {
					 if(goods.getIntValue("skuStore")<goods.getIntValue("count")) {
							return CommonUtil.errorJson(goods.getString("goodsName")+"库存不足");
						}
					 map=new HashMap<String, BigDecimal>();
					 orderGoods = new JSONObject();
					 setOrderGoods(orderId, 0, goods, orderGoods);
					 getPrice(map,goods,goods.getIntValue("count"));
					 totalPay= totalPay.add(map.get("totalPay"));
					 discountPay= totalPay.add(map.get("discountPay"));
					 practicePay= totalPay.add(map.get("practicePay"));
					 orderGoodList.add(orderGoods);
				 }
				 // 订单表保存
				 order= new JSONObject();
				setOrder(userId, orderId, order, totalPay, discountPay, practicePay);
				orderList.add(order);
				 
			}
			//订单表批量保存
			orderDao.insertOrderBatch(orderList);
			// 商品订单表批量保存
			orderGoodsDao.insertGoodsBatch(orderGoodList);
			// 批量扣减库存
			String counts=goodsDao.updateGoodsStoreSku(orderGoodList);
		} else {
			int count=0;
			if(!StringTools.isNullOrEmpty(goodsCount)) {
				count=Integer.parseInt(goodsCount);
			}else {
				return CommonUtil.errorJson("购买数出错");
			}
			if (count <= 0) {
				return CommonUtil.errorJson("购买数不能小于0");
			}
			JSONObject goods = goodsDao.getGoodsById(goodsId);
			if(goods.getIntValue("skuStore")<count) {
				return CommonUtil.errorJson("库存不足");
			}
			//获取订单总价，优惠价，实付金额
			Map<String,BigDecimal> map=new HashMap<String, BigDecimal>();
			getPrice(map,goods,count);
			// 商品订单表保存
			JSONObject orderGoods = new JSONObject();
			setOrderGoods(orderId, count, goods, orderGoods);
			orderGoodsDao.insertOrderGoods(orderGoods);
			// 订单表保存
			JSONObject order = new JSONObject();
			BigDecimal totalPay = map.get("totalPay");
			BigDecimal discountPay = map.get("discountPay");
			BigDecimal practicePay = map.get("practicePay");
			setOrder(userId, orderId, order, totalPay, discountPay, practicePay);
			orderDao.insertOrder(order);
			//扣减库存
			int updateGoodStoreCount = goodsDao.updateGoodStoreCount( goods.getString("goodsId"), count);
			if(updateGoodStoreCount<0) {
				throw new RuntimeException("库存不足");
			}
		}
		return orderIds;
	}

	private void setOrder(String userId, String orderId, JSONObject order, BigDecimal totalPay,
			BigDecimal discountPay, BigDecimal practicePay) {
		order.put("orderId", orderId);
		order.put("userId", userId);
		order.put("totalPay", totalPay);
		order.put("discountPay", discountPay);
		order.put("practicePay", practicePay);
		//1 待支付 2.待发货 3.待收货 4.交易失败 5.交易完成 6.已取消
		order.put("state","1");
	}

	private void setOrderGoods(String orderId, int count, JSONObject goods, JSONObject orderGoods) {
		String uuid = UUIDUtil.uuid();
		orderGoods.put("orderGoodsId", uuid);
		if(count==0) {
			orderGoods.put("count", goods.get("count"));
		}else {
			orderGoods.put("count", count);
		}
		orderGoods.put("storeId", goods.get("storeId"));
		orderGoods.put("orderId", orderId);
		orderGoods.put("goodsId", goods.get("goodsId"));
		orderGoods.put("price", goods.get("price"));
	}

	private void getPrice(Map<String, BigDecimal> map, JSONObject goods,int count) {
		// TODO Auto-generated method stub
		BigDecimal totalPay=new BigDecimal(goods.getString("price")).multiply(new BigDecimal(count));//总价
		BigDecimal discountPay=new BigDecimal(0);//优惠价
		BigDecimal practicePay=totalPay.subtract(discountPay);//实际价
		map.put("totalPay", totalPay);
		map.put("discountPay", discountPay);
		map.put("practicePay", practicePay);
	}

}
