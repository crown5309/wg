package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.AreaService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: hxy
 * @description: 文章相关Controller
 * @date: 2017/10/24 16:04
 */
@RestController
@RequestMapping("/area")
public class ArticleController {

	@Autowired
	private AreaService areaService;

	/**
	 * 查询文章列表
	 */
	@GetMapping("/listArea")
	public JSONObject listArticle(HttpServletRequest request) {
		return areaService.listArticle(CommonUtil.request2Json(request));
	}
	/**
	 * 查询文章列表
	 */
	@GetMapping("/listWuLiu")
	public JSONObject listWuLiu(HttpServletRequest request) {
		return areaService.listWuLiu(CommonUtil.request2Json(request));
	}

	/**
	 * 新增文章
	 */
	@PostMapping("/updateOrder")
	public JSONObject addArticle(@RequestBody JSONObject requestJson) {
		return areaService.addArticle(requestJson);
	}

	/**
	 * 修改文章
	 */
	@PostMapping("/updateArea")
	public JSONObject updateArea(@RequestBody JSONObject requestJson) {
		return areaService.updateArticle(requestJson);
	}
}
