package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.ShopService;
import com.heeexy.example.service.ShopUserService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wg
 * @description: 店员相关Controller
 * @date: 2020/07/23
 */
@RestController
@RequestMapping("/shop")
public class ShopUserController {

	@Autowired
	private ShopUserService shopUserService;

	/**
	 * 查询店员列表
	 */
	@RequiresPermissions("shopUser:list")
	@GetMapping("/listShopUser")
	public JSONObject listShopUser(HttpServletRequest request) {
		return shopUserService.listShopUser(CommonUtil.request2Json(request));
	}
	/**
	 * 搜索
	 */
	@RequestMapping("/serachShopUser")
	public JSONObject serachShopUser(String world) {
		return shopUserService.serachShopUser(world);
	}

	/**
	 * 新增店员
	 */
	@RequiresPermissions("shopUser:add")
	@PostMapping("/addShopUser")
	public JSONObject addShopUser(String userIds) {
		return shopUserService.addShopUser(userIds);
	}

	/**
	 * 修改店员
	 */
	@RequiresPermissions("shopUser:update")
	@PostMapping("/updateShopUser")
	public JSONObject updateShopUser(@RequestBody JSONObject requestJson) {
		return shopUserService.updateShopUser(requestJson);
	}
	
	/**
	 * 删除店员
	 */
	@RequiresPermissions("shopUser:delete")
	@PostMapping("/deleteShopUser")
	public JSONObject deleteShopUser(String id) {
		return shopUserService.deleteShopUser(id);
	}
}
