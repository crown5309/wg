package com.heeexy.example.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.dao.UserDao;
import com.heeexy.example.service.GoodsService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
import com.heeexy.example.util.constants.Constants;
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
	@Override
	public JSONObject addGoods(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JSONObject listGoods(JSONObject request2Json) {
		// TODO Auto-generated method stub
		CommonUtil.fillPageParam(request2Json);
		getAppId(request2Json);
		int count = goodsDao.countGoods(request2Json);
		List<JSONObject> list = goodsDao.listGoods(request2Json);
		for(JSONObject j:list) {
			getStateName(j);
			j.put("createTime", DateUtil.format(j.getDate("createTime"), DateUtil.DATE_TIME));
		}
		return CommonUtil.successPage(request2Json, list, count);
	}
	@Override
	public JSONObject updateGoods(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		goodsDao.updateGoods(jsonObject);
		return CommonUtil.successJson();
	}
	private void getAppId(JSONObject request2Json) {
		Session session = SecurityUtils.getSubject().getSession(); JSONObject
		userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		if(!"admin".equals(userInfo.getString("username"))) {
			String appId =userInfo.getString("appId");
			request2Json.put("appId", appId);
		}else {
			request2Json.put("appId", "");
		}
		
	}
	private void getStateName(JSONObject json) {
		//0 待审核 1审核通过  2 上架 3下架 4拒绝
		Integer state = json.getInteger("state");
		if(state==0) {
			json.put("stateName", "待审核");
		}else if(state==1) {
			json.put("stateName", "审核通过");
		}else if(state==2) {
			json.put("stateName", "已上架");
		}else if(state==3) {
			json.put("stateName", "已下架");
		}else if(state==4) {
			json.put("stateName", "拒绝通过");
		}
		
	}
}
