package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.goodsClassService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/goods")
public class GoodsClassController {

	@Autowired
	private goodsClassService goodsClassService;

	/**
	 * 查询商品分类列表
	 */
	@RequiresPermissions("goodsClass:list")
	@GetMapping("/listgoodsClass")
	public JSONObject listgoodsClass(HttpServletRequest request) {
		return goodsClassService.listGoodsClass(CommonUtil.request2Json(request));
	}

	/**
	 * 新增商品分类
	 */
	@RequiresPermissions("goodsClass:add")
	@PostMapping("/addgoodsClass")
	public JSONObject addgoodsClass(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "className,showOrder");
		return goodsClassService.addGoodsClass(requestJson);
	}

	/**
	 * 修改商品分类
	 */
	@RequiresPermissions("goodsClass:update")
	@PostMapping("/updategoodsClass")
	public JSONObject updategoodsClass(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson,"className,showOrder");
		return goodsClassService.updateGoodsClass(requestJson);
	}
}
