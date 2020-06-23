package com.heeexy.example.frontController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.IndexConfigService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/index")
public class IndexConfigController {
	@Autowired
	private IndexConfigService indexConfigService;

	/**
	 * 首页分类文字及分类图
	 * 
	 * @param appId
	 * @return
	 */
	@GetMapping("/listIndexAll")
	public JSONObject listIndexAll(String appId) {
		return CommonUtil.successJson(indexConfigService.listIndexAll(appId));
	}
}
