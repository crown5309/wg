package com.heeexy.example.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AddressDao;
import com.heeexy.example.dao.CartDao;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.OrderDao;
import com.heeexy.example.dao.OrderGoodsDao;
import com.heeexy.example.dao.SysParamDao;
import com.heeexy.example.entity.OrderInfo;
import com.heeexy.example.entity.StoreGoodsList;
import com.heeexy.example.service.OrderService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.HttpClientUtils;
import com.heeexy.example.util.IPUtil;
import com.heeexy.example.util.OrderIdFactory;
import com.heeexy.example.util.PayCommonUtil;
import com.heeexy.example.util.StringTools;
import com.heeexy.example.util.UUIDUtil;
import com.heeexy.example.util.XMLUtil;
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
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private SysParamDao sysParamDao;
	@Value("${wei_xin_notify_url}")
	private String base;

	@Override
	@Transactional
	public Object submitOrder(String goodsId, String goodsCount, String cartIds) {
		// 从session获取用户信息
		 Session session = SecurityUtils.getSubject().getSession(); JSONObject
		 userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String userId =userInfo.getString("userId");
		//订单id
		String orderId = OrderIdFactory.getOrderIdByUUIdAndDate();
		List<String> orderIds=new ArrayList<String>();//返回订单列表id
		// TODO Auto-generated method stub
		if (StringTools.isNullOrEmpty(goodsId) && !StringTools.isNullOrEmpty(cartIds)) {// 从购物车取信息
			orderId = OrderIdFactory.getOrderIdByUUIdAndDate();
			orderIds.add(orderId);
			String[] split = cartIds.split(",");
			List<StoreGoodsList> list=cartDao.getStoreList(split);
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
					 if(goods.getIntValue("state")!=2){
						 return CommonUtil.errorJson(goods.getString("goodsName")+"商品未上架");
					 }
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
			for(JSONObject o:orderGoodList){
				int updateGoodStoreCount = goodsDao.updateGoodStoreCount( o.getString("goodsId"), o.getInteger("count"));
				if(updateGoodStoreCount<0) {
					throw new RuntimeException(o.getString("goodsName")+"库存不足");
				}
			}
			goodsDao.updateGoodsStoreSku(orderGoodList);
		} else {
			orderIds.add(orderId);
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

	@Override
	public Object pay(String orderIds, String addressId,String remarks,HttpServletRequest request) {
		JSONObject info = getOrderInfoByIds(orderIds);
		// 支付
        //得到openid
		 Session session = SecurityUtils.getSubject().getSession(); JSONObject
		 userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        String openid = userInfo.getString("openId");
        String appid = userInfo.getString("appId");
        //得到小程序传过来的价格，注意这里的价格必须为整数，1代表1分，所以传过来的值必须*100；
        BigDecimal fee = info.getBigDecimal("practicePay").multiply(new BigDecimal(100));
        //订单编号
        String did =OrderIdFactory.getOrderIdByUUIdAndDate();
         //获取商户相关信息
        List<JSONObject> sysParam=sysParamDao.getParamInfo(appid,"sys_param");
        String mch_id="";//商户号
        String key="";//商户密钥
        String title="";
        for(JSONObject js:sysParam) {
        	if("name".equals(js.getString("code"))) {
        		title=js.getString("value");
        		continue;
        	}
        	if("mch_id".equals(js.getString("code"))) {
        		title=js.getString("value");
        		continue;
        	}
        	if("key".equals(js.getString("code"))) {
        		title=js.getString("value");
        		continue;
        	}
        }
        //时间戳
        String times = System.currentTimeMillis() + "";
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", times);//时间戳
        packageParams.put("body", title);//支付主体
        packageParams.put("out_trade_no", did);//编号
        packageParams.put("total_fee", fee);//价格
        String ip = IPUtil.getIp(request);
        int j = ip.indexOf(",");
		if (j > 0) {
			ip = ip.substring(0, j).trim();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		packageParams.put("spbill_create_ip", ip);
        packageParams.put("notify_url", base+"/weixin/callback");//支付返回地址，
        packageParams.put("trade_type", "JSAPI");//这个api有，固定的
        packageParams.put("openid", openid);//openid
        //获取sign
        String sign = PayCommonUtil.createSign("UTF-8", packageParams,key);//最后这个是自己设置的32位密钥
        packageParams.put("sign", sign);
        //转成XML
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        //得到含有prepay_id的XML
        String resXml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML);
        System.out.println(resXml);
        //解析XML存入Map
        Map map = null;
		try {
			map = XMLUtil.doXMLParse(resXml);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(map);
        String return_code = (String) map.get("return_code");
        String result_code = (String) map.get("result_code()");
        if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
        	//更新订单信息1 待支付 2.待发货 3.待收货 4.交易失败 5.交易完成 6.下单成功 7下单失败 8支付成功 9支付失败 10退款中 11退款完成  12退货中 
        	orderDao.updateOrderState(orderIds.split(","),did,6);
        	//得到prepay_id
            String prepay_id = (String) map.get("prepay_id");
            SortedMap<Object, Object> packageP = new TreeMap<Object, Object>();
            packageP.put("appId", appid);//
            packageP.put("nonceStr", times);//时间戳
            packageP.put("package", "prepay_id=" + prepay_id);//必须把package写成 "prepay_id="+prepay_id这种形式
            packageP.put("signType", "MD5");//paySign加密
            packageP.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
            //得到paySign
            String paySign = PayCommonUtil.createSign("UTF-8", packageP, key);
            packageP.put("paySign", paySign);
            return CommonUtil.successJson(packageP);
        }
        
        //将packageP数据返回给小程序
        //更新订单信息
        orderDao.updateOrderState(orderIds.split(","),did,7);
		return CommonUtil.errorJson("下单失败");
	}

	private JSONObject getOrderInfoByIds(String orderIds) {
		JSONObject info = new JSONObject();
		String[] split = orderIds.split(",");
		List<OrderInfo> OrderInfoList=orderDao.getAllInfoOrderByOrderIds(split);
		List<JSONObject> goodsList=null;
		BigDecimal totalPay=new BigDecimal(0);//总价
		BigDecimal discountPay=new BigDecimal(0);//优惠价
		BigDecimal practicePay=new BigDecimal(0);//实际价
		for(OrderInfo sg:OrderInfoList){
			goodsList =(List<JSONObject>) sg.get("goodsList");
			totalPay= totalPay.add(sg.getBigDecimal("totalPay"));
			discountPay= totalPay.add(sg.getBigDecimal("discountPay"));
			practicePay= totalPay.add(sg.getBigDecimal("practicePay"));
			for(JSONObject goods:goodsList) {
				if(goods.getIntValue("state")!=2){
					return CommonUtil.errorJson(goods.getString("goodsName")+"商品未上架");
				}
				if(goods.getBigDecimal("price").compareTo(goods.getBigDecimal("orderPrice"))!=0){
					return CommonUtil.errorJson(goods.getString("goodsName")+"价格有变");
				}
			}
		}
		info.put("totalPay",totalPay);
		info.put("discountPay",discountPay);
		info.put("practicePay",practicePay);
		info.put("OrderInfoList", OrderInfoList);
		return info;
	}

	@Override
	public Object getOrderInfo(String orderIds) {
		JSONObject info = getOrderInfoByIds(orderIds);
		// 从session获取用户信息
		Session session = SecurityUtils.getSubject().getSession(); JSONObject
		userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String userId =userInfo.getString("userId");
		JSONObject address=addressDao.getaddressDefault(userId);
		info.put("address", address);
		return info;
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
		orderGoods.put("goodsName",  goods.get("goodsName"));
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

	@Override
	public void updateByOutTradeNo(String out_trade_no, int state) {
		// TODO Auto-generated method stub
	     orderDao.updateByOutTradeNo(out_trade_no,state);
	}

}
