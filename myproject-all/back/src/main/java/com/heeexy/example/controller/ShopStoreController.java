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
import com.heeexy.example.service.ShopService;
import com.heeexy.example.util.CommonUtil;

/**
 * @author: wg
 * @description: 商家分类相关Controller
 * @date: 2020/07/23
 */
@RestController
@RequestMapping("/shop")
public  class ShopStoreController {

	@Autowired
	private ShopService shopService;

	/**
	 * 查询商家分类列表
	 */
	@RequiresPermissions("shop:list")
	@GetMapping("/listShop")
	public JSONObject listArticle(HttpServletRequest request) {
		return shopService.listShop(CommonUtil.request2Json(request));
	}

	/**
	 * 新增商家分类
	 */
	@RequiresPermissions("shop:add")
	@PostMapping("/addShop")
	public JSONObject addShop(@RequestBody JSONObject requestJson) {
		return shopService.addShop(requestJson);
	}

	/**
	 * 修改商家分类
	 */
	@RequiresPermissions("shop:update")
	@PostMapping("/updateShop")
	public JSONObject updateShop(@RequestBody JSONObject requestJson) {
		return shopService.updateShop(requestJson);
	}
	/**
	 * 修改商家分类
	 */
	@RequiresPermissions("shop:audit")
	@PostMapping("/auditShop")
	public JSONObject auditShop(@RequestBody JSONObject requestJson) {
		return shopService.updateShop(requestJson);
	}
	
	/**
	 * 删除商家分类
	 */
	@RequiresPermissions("shop:delete")
	@PostMapping("/deleteShop")
	public JSONObject deleteShop(String id) {
		return shopService.deleteShop(id);
	}
}
