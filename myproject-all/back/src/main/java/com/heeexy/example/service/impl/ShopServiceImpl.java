package com.heeexy.example.service.impl;

import java.util.List;

import com.heeexy.example.dao.UserDao;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ShopClassDao;
import com.heeexy.example.dao.ShopDao;
import com.heeexy.example.service.AreaService;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.ShopService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public  class ShopServiceImpl extends BaseService implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopClassDao shopClassDao;
	@Autowired
	private UserDao userDao;
	@Override
	public JSONObject listShop(JSONObject request2Json) {
		// TODO Auto-generated method stub
		CommonUtil.fillPageParam(request2Json);
		getAppId(request2Json);
		int count = shopDao.countShop(request2Json);
		List<JSONObject> list = shopDao.listShop(request2Json);
		//1 待审核 2.审核通过   3拒绝
		for(JSONObject k:list) {
			k.put("createTime", DateUtil.format(k.getDate("createTime"), DateUtil.DATE_TIME));
			k.put("auditFlag", true);
			if(k.getIntValue("state")==1) {
				k.put("stateName", "待审核");
				k.put("auditFlag", false);
			}else if(k.getIntValue("state")==2) {
				k.put("stateName", "审核通过");
			}else if(k.getIntValue("state")==3) {
				k.put("stateName", "拒绝通过");
			}else {
				k.put("stateName", "状态码错误");
			}
			k.put("area", k.getString("areaIdOne")+k.getString("areaIdTwo")+k.getString("areaIdThree")+k.getString("areaInfo"));
		}
		JSONObject successPage = CommonUtil.successPage(request2Json, list, count);
		successPage.getJSONObject("info").put("areaList", areaService.listArticle(request2Json).get("info"));
		request2Json.put("offSet", 0);
		request2Json.put("pageRow", 10000);
		successPage.getJSONObject("info").put("shopClassList", shopClassDao.listShopClass(request2Json));
		return successPage;
	}

	@Override
	public JSONObject addShop(JSONObject requestJson) {
		// TODO Auto-generated method stub
		getAppId(requestJson);
		shopDao.addShop(requestJson);
		return CommonUtil.successJson();
	}

	@Override
	@Transactional
	public JSONObject updateShop(JSONObject requestJson) {
		// TODO Auto-generated method stub
		shopDao.updateShop(requestJson);
		if(requestJson.getIntValue("state")==2){
			getAppId(requestJson);
			String roleId=userDao.getRoleIdByJson(requestJson);
			if(StringTools.isNullOrEmpty(roleId)){
				return CommonUtil.errorJson("请先添加商家角色");
			}
			requestJson.put("weixin_role_id",roleId);
			userDao.updateStoreRole(requestJson);
		}
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject deleteShop(String id) {
		// TODO Auto-generated method stub
		shopDao.deleteShop(id);
		return CommonUtil.successJson();
	}

}

