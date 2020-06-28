package com.heeexy.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsClassDao;
import com.heeexy.example.dao.IndexConfigDao;
import com.heeexy.example.entity.ClassesInfo;
import com.heeexy.example.service.IndexConfigService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.Constants;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
	@Autowired
	private IndexConfigDao indexConfigDao;
	@Autowired
	private GoodsClassDao goodsClassDao;

	@Override
	public List<JSONObject> listIndexConfig(JSONObject request2Json) {
		// TODO Auto-generated method stubrequest2Json
		String type = request2Json.getString("type");
		String appId = request2Json.getString("appId");
		List<JSONObject> js = null;
		if ("classes".equals(type)) {
			js = indexConfigDao.listIndexConfigClasses(type);
		} else {
			js = indexConfigDao.listIndexConfigBanner(type, appId);
		}
		return js;
	}

	@Override
	public JSONObject addIndexConfig(JSONObject requestJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject updateIndexConfig(JSONObject requestJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject listIndexAll(String appId) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		List<ClassesInfo> listIndexAll = indexConfigDao.listIndexAll(appId);
		// 获取banner
		List<JSONObject> listIndexConfigBanner = indexConfigDao.listIndexConfigBanner("banner", appId);
		json.put("classList", listIndexAll);
		json.put("bannerList", listIndexConfigBanner);
		return json;
	}

	@Override
	public Object listIndexClassAll(String appId) {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		appId = userInfo.getString("appId");
		List<ClassesInfo> listIndexAll = indexConfigDao.listIndexAll(appId);
		// 获取banner
		List<JSONObject> list = null;
		for (ClassesInfo sg : listIndexAll) {
			sg.put("id", sg.get("id"));
			sg.put("label", sg.get("name"));
			list = (List<JSONObject>) sg.get("classsesList");
			for (JSONObject goods : list) {
				// 下单成功减库存
				goods.put("id", goods.get("goodsClassId"));
				goods.put("label", goods.get("className"));

			}
			sg.put("children", list);
		}
		return listIndexAll;
	}

	@Override
	public Object listClassAll(String appId) {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		appId = userInfo.getString("appId");
		List<JSONObject> list=goodsClassDao.getAllGoodsClass(appId);
		List<JSONObject> list1=new ArrayList<JSONObject>();
		JSONObject one=null;
		List<JSONObject> list2=null;
		JSONObject two=null;
		List<JSONObject> list3=null;
		JSONObject three=null;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getInteger("classLevel")==1) {//一级
				one=new JSONObject();
				one.put("id", list.get(i).get("id"));
				one.put("label", list.get(i).get("className"));
				list2=new ArrayList<JSONObject>();
				for(int j=0;j<list.size();j++) {
					if(list.get(j).getInteger("classLevel")==2&&list.get(j).get("parentId").equals(list.get(i).get("id"))) {//二级
						two=new JSONObject();
						two.put("id", list.get(j).get("id"));
						two.put("label", list.get(j).get("className"));
						list3=new ArrayList<JSONObject>();
						for(int k=0;k<list.size();k++) {
							if(list.get(k).getInteger("classLevel")==3&&list.get(k).get("parentId").equals(list.get(j).get("id"))) {//三级
								three=new JSONObject();
								three.put("id", list.get(k).get("id"));
								three.put("label", list.get(k).get("className"));
								list3.add(three);
							}
						}
						two.put("children", list3);
						list2.add(two);
					}
					
				}
				one.put("children", list2);
				list1.add(one);
			}
			
		}
		List<String> checkList=indexConfigDao.listIndexCheck(appId);
		JSONObject js=new JSONObject();
		js.put("list", list1);
		js.put("checkList", checkList);
		return js;
	}

	@Override
	@Transactional
	public Object addIndexClass(String json) {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String appId = userInfo.getString("appId");
		String[] split = json.split(",");
		indexConfigDao.deleteByAppId(appId);
	    indexConfigDao.addIndexClass(appId,split);
		return "";
	}

	@Override
	public JSONObject listBanner(JSONObject request2Json) {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String appId = userInfo.getString("appId");
		request2Json.put("appId", appId);
		CommonUtil.fillPageParam(request2Json);
		int count = indexConfigDao.countBanner(request2Json);
		List<JSONObject> list = indexConfigDao.listBanner(request2Json);
		return CommonUtil.successPage(request2Json, list, count);
	}

}
