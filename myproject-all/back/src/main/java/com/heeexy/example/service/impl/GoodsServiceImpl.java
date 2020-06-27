package com.heeexy.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.UserDao;
import com.heeexy.example.service.GoodsService;
import com.heeexy.example.util.CommonUtil;
@Service
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsDao goodsDao;
	@Override
	public Object addGoods(JSONObject request2Json) {
		// TODO Auto-generated method stub
		goodsDao.addGoods(request2Json);
		return CommonUtil.successJson();
	}
	@Override
	public Object getAllGoodsByState(String state, int pageSize,int pageNo,String appId,String classId,String goodsName, String type, String priceFlag) {
		// TODO Auto-generated method stub
		pageNo=pageSize*(pageNo-1);
		List<JSONObject> list=goodsDao.getAllGoodsByState(state,pageSize,pageNo,appId,classId,goodsName,type,priceFlag);
		String[] split =null;
		for(int i=0;i<list.size();i++) {
			split = list.get(i).getString("bannerUrl").split(",");
			list.get(i).put("bannerUrl",split);
			list.get(i).put("imageurl",split[0]);
		list.get(i).put("isSelect",false);
		}
		return list;
	}
	@Override
	public Object updateGoodsByIds(String state, String ids) {
		String[] split = ids.split(",");
		// TODO Auto-generated method stub
		goodsDao.updateGoodsByIds(state,split);
		return CommonUtil.successJson();
	}
	@Override
	public Object getGoodsById(String goodsId) {
		// TODO Auto-generated method stub
		JSONObject goodsById = goodsDao.getGoodsById(goodsId);
		goodsById.put("bannerUrl",goodsById.getString("bannerUrl").split(","));
		goodsById.put("detailUrl",goodsById.getString("detailUrl").split(","));
		return goodsById;
	}

}
