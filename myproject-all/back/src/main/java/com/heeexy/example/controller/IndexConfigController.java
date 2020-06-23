package com.heeexy.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.IndexConfigService;
import com.heeexy.example.util.CommonUtil;

/**
 * @author: hxy
 * @description: 首页配置相关Controller
 * @date: 2017/10/24 16:04
 */
@RestController
@RequestMapping("/indexConfig")
public class IndexConfigController {

	@Autowired
	private IndexConfigService indexConfigService;

	/**
	 * 查询首页配置列表
	 */
	@RequiresPermissions("indexConfig:list")
	@GetMapping("/listIndexConfig")
	public Object listIndexConfig(HttpServletRequest request) {
		return indexConfigService.listIndexConfig(CommonUtil.request2Json(request));
	}

	/**
	 * 新增首页配置
	 */
	@RequiresPermissions("indexConfig:add")
	@PostMapping("/addIndexConfig")
	public JSONObject addIndexConfig(HttpServletRequest request) {
		return indexConfigService.addIndexConfig(CommonUtil.request2Json(request));
	}

	/**
	 * 修改首页配置
	 */
	@RequiresPermissions("indexConfig:update")
	@PostMapping("/updateIndexConfig")
	public JSONObject updateIndexConfig(HttpServletRequest request) {
		return indexConfigService.updateIndexConfig(CommonUtil.request2Json(request));
	}
}
