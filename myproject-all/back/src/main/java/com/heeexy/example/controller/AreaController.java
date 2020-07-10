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
public class AreaController {

	@Autowired
	private AreaService areaService;

	/**
	 * 查询文章列表
	 */
	@GetMapping("/listArea")
	public JSONObject listArea(HttpServletRequest request) {
		return areaService.listArticle(CommonUtil.request2Json(request));
	}
	/**
	 * 查询物流列表
	 */
	@RequestMapping("/listWuLiu")
	public JSONObject listWuLiu(HttpServletRequest request) {
		return areaService.listWuLiu(CommonUtil.request2Json(request));
	}

	/**
	 *
	 */
	@PostMapping("/updateOrder")
	public JSONObject updateOrder(@RequestBody JSONObject requestJson) {
		return areaService.addArticle(requestJson);
	}
	/**
	 *
	 */
	@PostMapping("/updateOrderByOrderId")
	public JSONObject updateOrder(String logisticsNo,String logisticsType,String orderId) {
		JSONObject requestJson=new JSONObject();
		requestJson.put("logisticsNo", logisticsNo);
		requestJson.put("logisticsType", logisticsType);
		requestJson.put("orderId", orderId);
		return areaService.addArticle(requestJson);
	}

	/**
	 * 修改收货地址
	 */
	@PostMapping("/updateArea")
	public JSONObject updateArea(@RequestBody JSONObject requestJson) {
		return areaService.updateArticle(requestJson);
	}
}
