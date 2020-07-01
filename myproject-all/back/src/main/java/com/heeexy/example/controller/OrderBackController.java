package com.heeexy.example.controller;

import com.heeexy.example.service.OrderService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.XMLUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderBackController {
	@Autowired
	private OrderService orderService;
	@RequiresPermissions("order:list")
	@RequestMapping("/getOrderInfoList")
	public Object getOrderInfoList(HttpServletRequest request) {
		return  orderService.getOrderInfoList(CommonUtil.request2Json(request));
	}
}
