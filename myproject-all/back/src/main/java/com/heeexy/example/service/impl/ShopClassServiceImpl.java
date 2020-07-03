package com.heeexy.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ShopClassDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.ShopClassService;
import com.heeexy.example.util.CommonUtil;
@Service
public class ShopClassServiceImpl extends BaseService implements ShopClassService {
	@Autowired
	private ShopClassDao shopClassDao;
	@Override
	public JSONObject listShopClass(JSONObject request2Json) {
		// TODO Auto-generated method stub
		CommonUtil.fillPageParam(request2Json);
		getAppId(request2Json);
		int count = shopClassDao.countShopClass(request2Json);
		List<JSONObject> list = shopClassDao.listShopClass(request2Json);
		JSONObject successPage = CommonUtil.successPage(request2Json, list, count);
		return successPage;
	}

	@Override
	public JSONObject addShopClass(JSONObject requestJson) {
		// TODO Auto-generated method stub
		getAppId(requestJson);
		shopClassDao.addShopClass(requestJson);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject updateShopClass(JSONObject requestJson) {
		// TODO Auto-generated method stub
		shopClassDao.updateShopClass(requestJson);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject deleteShopClass(String id) {
		// TODO Auto-generated method stub
		shopClassDao.deleteShopClass(id);
		return CommonUtil.successJson();
	}

}
