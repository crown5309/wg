package com.heeexy.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.IndexConfigService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/index")
public class IndexClassController {
	@Autowired
	private IndexConfigService indexConfigService;

	/**
	 * 首页分类文字及分类图
	 * 
	 * @param appId
	 * @return
	 */
	@RequiresPermissions("index:list")
	@RequestMapping("/listIndexClassAll")
	public JSONObject listIndexClassAll(String appId) {
		return CommonUtil.successJson(indexConfigService.listIndexClassAll(appId));
	}
	
	/**
	 * 首页分类文字及分类图
	 * 
	 * @param appId
	 * @return
	 */
	@RequiresPermissions("index:update")
	@RequestMapping("/listClassAll")
	public JSONObject listClassAll(String appId) {
		return CommonUtil.successJson(indexConfigService.listClassAll(appId));
	}
	/**
	 * 首页分类文字及分类图
	 * 
	 * @param appId
	 * @return
	 */
	@RequiresPermissions("index:update")
	@RequestMapping("/addIndexClass")
	public JSONObject addIndexClass(String json) {
		return CommonUtil.successJson(indexConfigService.addIndexClass(json));
	}
	
	@RequiresPermissions("banner:list")
	@GetMapping("/listBanner")
	public JSONObject listBanner(HttpServletRequest request) {
		return indexConfigService.listBanner(CommonUtil.request2Json(request));
	}
	
	@RequiresPermissions("banner:add")
	@RequestMapping("/addBanner")
	public JSONObject addBanner(HttpServletRequest request) {
		return indexConfigService.addBanner(request);
	}
	
	@RequiresPermissions("banner:update")
	@RequestMapping("/updateBanner")
	public JSONObject updateBanner(HttpServletRequest request) {
		return indexConfigService.updateBanner(request);
	}
	@RequiresPermissions("banner:delete")
	@RequestMapping("/deleteBanner")
	public JSONObject deleteBanner(String  id,String oldImg) {
		return indexConfigService.deleteBanner(id,oldImg);
	}
}
