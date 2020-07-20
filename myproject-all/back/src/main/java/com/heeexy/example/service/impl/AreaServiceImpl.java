package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.redis.RedisClient;
import com.heeexy.example.dao.ArticleDao;
import com.heeexy.example.dao.OrderLogDao;
import com.heeexy.example.service.AreaService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.OrderLogUtil;
import com.heeexy.example.util.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hxy
 * @date: 2017/10/24 16:07
 */
@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private OrderLogDao orderLogDao;
	/**
	 * 新增文章
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONObject addArticle(JSONObject jsonObject) {
		articleDao.addArticle(jsonObject);
		Session session = SecurityUtils.getSubject().getSession(); JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		String userId =userInfo.getString("userId");
		OrderLogUtil.inserOrderLog(jsonObject.getString("orderId"), userId,3,"发货成功",null,orderLogDao,null);
		return CommonUtil.successJson();
	}

	/**
	 * 文章列表
	 */
	@Override
	public JSONObject listArticle(JSONObject jsonObject) {
		Object areaList = redisClient.getJsonObject("areaList", List.class);
		List<JSONObject> list1=new ArrayList<JSONObject>();
		if(areaList==null){
			List<JSONObject> list = articleDao.listArticle(jsonObject);
			JSONObject one=null;
			List<JSONObject> list2=null;
			JSONObject two=null;
			List<JSONObject> list3=null;
			JSONObject three=null;
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getInteger("levelType")==1) {//一级
					one=new JSONObject();
					one.put("value", list.get(i).get("name"));
					one.put("label", list.get(i).get("name"));
					list2=new ArrayList<JSONObject>();
					for(int j=0;j<list.size();j++) {
						if(list.get(j).getInteger("levelType")==2&&list.get(j).get("parentId").equals(list.get(i).get("id"))) {//二级
							two=new JSONObject();
							two.put("value", list.get(j).get("name"));
							two.put("label", list.get(j).get("name"));
							list3=new ArrayList<JSONObject>();
							for(int k=0;k<list.size();k++) {
								if(list.get(k).getInteger("levelType")==3&&list.get(k).get("parentId").equals(list.get(j).get("id"))) {//三级
									three=new JSONObject();
									three.put("value", list.get(k).get("name"));
									three.put("label", list.get(k).get("name"));
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
			redisClient.setJsonString("areaList",list1);
		}else{
			list1=(List<JSONObject>)areaList;
		}

		return CommonUtil.successJson(list1);
	}

	/**
	 * 更新文章
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONObject updateArticle(JSONObject jsonObject) {
		articleDao.updateArticle(jsonObject);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject listWuLiu(JSONObject request2Json) {
		List<JSONObject> list=articleDao.listWuLiu();
		return CommonUtil.successJson(list);
	}
}
