package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: hxy
 * @date: 2017/10/24 16:06
 */
public interface AreaService {
	/**
	 * 新增
	 */
	JSONObject addArticle(JSONObject jsonObject);

	/**
	 * 列表
	 */
	JSONObject listArticle(JSONObject jsonObject);

	/**
	 * 更新
	 */
	JSONObject updateArticle(JSONObject jsonObject);

	JSONObject listWuLiu(JSONObject request2Json);
}
