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
import com.heeexy.example.service.ShopClassService;
import com.heeexy.example.util.CommonUtil;

/**
 * @author: wg
 * @description: 商家相关Controller
 * @date: 2020/07/23
 */
@RestController
@RequestMapping("/shopClass")
public class ShopStoreClassController {

	@Autowired
	private ShopClassService shopClassService;

	/**
	 * 查询商家列表
	 */
	@RequiresPermissions("shop_class:list")
	@GetMapping("/listShopClass")
	public JSONObject listArticle(HttpServletRequest request) {
		return shopClassService.listShopClass(CommonUtil.request2Json(request));
	}

	/**
	 * 新增商家
	 */
	@RequiresPermissions("shop_class:add")
	@PostMapping("/addShopClass")
	public JSONObject addShopClass(@RequestBody JSONObject requestJson) {
		return shopClassService.addShopClass(requestJson);
	}

	/**
	 * 修改商家
	 */
	@RequiresPermissions("shop_class:update")
	@PostMapping("/updateShopClass")
	public JSONObject updateShopClass(@RequestBody JSONObject requestJson) {
		return shopClassService.updateShopClass(requestJson);
	}
	
	/**
	 * 删除商家
	 */
	@RequiresPermissions("shop_class:delete")
	@PostMapping("/deleteShopClass")
	public JSONObject deleteShopClass(String id) {
		return shopClassService.deleteShopClass(id);
	}
}
