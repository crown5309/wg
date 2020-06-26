package com.heeexy.example.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.CartDao;
import com.heeexy.example.service.CartService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.Constants;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartDao cartDao;

	@Override
	public Object addToMyCart(String goodsId, int count) {

		// TODO Auto-generated method stub
		// 获取购物车是否存在该商品
		String userId = getUserId();
		JSONObject goods = cartDao.getCartByGoodsId(goodsId,userId);
		if(goods!=null) {
			 cartDao.updateCartCountById(goods.getString("id"),count);
		}else {
			JSONObject cart=new JSONObject();
			cart.put("goodsId", goodsId);
			cart.put("count", count);
			cart.put("userId", userId);
			cartDao.insert(cart);
		}
		return CommonUtil.successJson();
	}

	@Override
	public Object updateCartCountById(String id, int count) {
		// TODO Auto-generated method stub
		 cartDao.updateCartCountById(id,count);
		return CommonUtil.successJson();
	}

	@Override
	public Object getMyCartList(Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo=(pageNo-1)*pageSize;
		List<JSONObject> list=cartDao.getMyCartList(pageNo,pageSize,"1");
		
		for(JSONObject j:list) {
			j.put("bannerUrl",j.getString("bannerUrl").split(","));
		}
		return CommonUtil.successJson(list);
	}

	@Override
	public Object deleteMyCartById(String id) {
		// TODO Auto-generated method stub
		 cartDao.deleteMyCartById(id);
		return CommonUtil.successJson();
	}

	private String getUserId() {
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String userId = userInfo.getString("userId");
		return userId;
	}
}
