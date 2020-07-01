package com.heeexy.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsClassDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.goodsClassService;
import com.heeexy.example.util.CommonUtil;

@Service
public class goodsClassServiceImpl extends BaseService implements goodsClassService {
	@Autowired
	private GoodsClassDao goodsClassDao;

	@Override
	public JSONObject listGoodsClass(JSONObject request2Json) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		getAppId(request2Json);
		CommonUtil.fillPageParam(request2Json);
		int count = goodsClassDao.countGoodsClass(request2Json);
		List<JSONObject> list = goodsClassDao.listGoodsClass(request2Json);
		return CommonUtil.successPage(request2Json, list, count);

	}

	@Override
	public JSONObject addGoodsClass(JSONObject requestJson) {
		getAppId(requestJson);
		goodsClassDao.addGoodsClass(requestJson);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject updateGoodsClass(JSONObject requestJson) {
		// TODO Auto-generated method stub
		goodsClassDao.updateGoodsClass(requestJson);
		return CommonUtil.successJson();
	}

	@Override
	public Object getAllGoodsClass(String appid) {
		// TODO Auto-generated method stub
		List<JSONObject> list=goodsClassDao.getAllGoodsClass(appid);
		List<JSONObject> list1=new ArrayList<JSONObject>();
		JSONObject one=null;
		List<JSONObject> list2=null;
		JSONObject two=null;
		List<JSONObject> list3=null;
		JSONObject three=null;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getInteger("classLevel")==1) {//一级
				one=new JSONObject();
				one.put("screenId", list.get(i).get("id"));
				one.put("screenName", list.get(i).get("className"));
				one.put("showImageUrl", list.get(i).get("classImgUrl"));
				list2=new ArrayList<JSONObject>();
				for(int j=0;j<list.size();j++) {
					if(list.get(j).getInteger("classLevel")==2&&list.get(j).get("parentId").equals(list.get(i).get("id"))) {//二级
						two=new JSONObject();
						two.put("screenId", list.get(j).get("id"));
						two.put("screenName", list.get(j).get("className"));
						two.put("showImageUrl", list.get(j).get("classImgUrl"));
						list3=new ArrayList<JSONObject>();
						for(int k=0;k<list.size();k++) {
							if(list.get(k).getInteger("classLevel")==3&&list.get(k).get("parentId").equals(list.get(j).get("id"))) {//三级
								three=new JSONObject();
								three.put("screenId", list.get(k).get("id"));
								three.put("screenName", list.get(k).get("className"));
								three.put("showImageUrl", list.get(k).get("classImgUrl"));
								list3.add(three);
							}
						}
						two.put("childrenScreenArray", list3);
						list2.add(two);
					}
					
				}
				one.put("childrenScreenArray", list2);
				list1.add(one);
			}
			
		}
		return list1;
	}

}
