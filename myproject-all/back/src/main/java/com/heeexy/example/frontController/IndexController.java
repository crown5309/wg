package com.heeexy.example.frontController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.IndexConfigService;
import com.heeexy.example.util.CommonUtil;

/**
 * 首页配置
 */
@RestController
@RequestMapping("/index")
public class IndexController {
	@Autowired
	private IndexConfigService indexConfigService;

	/**
	 * 首页分类文字及分类图
	 * 
	 * @param appId
	 * @return
	 */
	@RequestMapping("/listIndexAll")
	public JSONObject listIndexAll(String appId) {
		return CommonUtil.successJson(indexConfigService.listIndexAll(appId));
	}

	/**
	 * 热门商品
	 * 
	 * @param appId
	 * @param CLASSID
	 * @return
	 */
	@RequestMapping("/listIndexGoods")
	public JSONObject listIndexGoods(String appId,String classId,int pageNo,int pageSize) {
		return CommonUtil.successJson(indexConfigService.listIndexGoods(appId,classId,pageNo,pageSize));
	}
}
