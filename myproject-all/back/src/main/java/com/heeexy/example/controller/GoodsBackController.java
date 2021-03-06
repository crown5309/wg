package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.GoodsService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/goods")
public  class GoodsBackController {

	@Autowired
	private GoodsService goodsService;

	/**
	 * 查询商品列表
	 */
	@RequiresPermissions("goods:list")
	@GetMapping("/listgoods")
	public JSONObject listGoods(HttpServletRequest request) {
		return goodsService.listGoods(CommonUtil.request2Json(request));
	}

	/**
	 * 新增商品
	 */
	@RequiresPermissions("goods:add")
	@PostMapping("/addGoods")
	public Object addgoods(HttpServletRequest request) {
		return goodsService.addGoods(CommonUtil.request2Json(request));
	}

	/**
	 * 审核
	 */
	@RequiresPermissions("goods:audit")
	@PostMapping("/auditGoods")
	public JSONObject auditGoods(HttpServletRequest request) {
		return goodsService.auditGoods(CommonUtil.request2Json(request));
	}
	/**
	 * 上架下架
	 */
	@RequiresPermissions("goods:upAndDown")
	@PostMapping("/upAndDownGoods")
	public JSONObject upAndDownGoods(HttpServletRequest request) {
		return goodsService.upAndDownGoods(CommonUtil.request2Json(request));
	}
	/**
	 * 修改
	 */
	@RequiresPermissions("goods:update")
	@PostMapping("/updateGoods")
	public JSONObject updategoods(HttpServletRequest request) {
		return goodsService.updateGoods(CommonUtil.request2Json(request));
	}
}
