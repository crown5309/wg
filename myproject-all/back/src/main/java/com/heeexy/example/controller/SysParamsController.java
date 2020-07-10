package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.SysParamsService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/param")
public  class SysParamsController {

	@Autowired
	private SysParamsService sysParamsService;

	/**
	 * 查询列表
	 */
	@RequiresPermissions("sysParams:list")
	@GetMapping("/listSysParams")
	public JSONObject listSysParams(HttpServletRequest request) {
		return sysParamsService.listSysParams(CommonUtil.request2Json(request));
	}

	/**
	 * 新增
	 */
	@RequiresPermissions("sysParams:add")
	@PostMapping("/addSysParams")
	public Object addSysParams(HttpServletRequest request) {
		return sysParamsService.addSysParams(CommonUtil.request2Json(request));
	}

	/**
	 * 修改
	 */
	@RequiresPermissions("sysParams:update")
	@PostMapping("/updateSysParams")
	public JSONObject updateSysParams(HttpServletRequest request) {
		return sysParamsService.updateSysParams(CommonUtil.request2Json(request));
	}
	/**
	 * 删除
	 */
	@RequiresPermissions("sysParams:delete")
	@PostMapping("/deleteSysParams")
	public JSONObject deleteSysParams(HttpServletRequest request) {
		return sysParamsService.deleteSysParams(CommonUtil.request2Json(request));
	}
}
