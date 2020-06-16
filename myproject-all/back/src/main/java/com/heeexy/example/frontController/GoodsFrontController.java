package com.heeexy.example.frontController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.GoodsService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/front")
public class GoodsFrontController {

	@Autowired
	private GoodsService goodservice;

	
	@RequestMapping("/addGoods")
	public Object addGoods(HttpServletRequest request) {
		return  CommonUtil.successJson(goodservice.addGoods(CommonUtil.request2Json(request)));
	}
	
	
	@RequestMapping("/getAllGoodsByState")
	public Object getAllGoodsClass(String state,int pageSize,int pageNo,String appId,String classId) {
		return  CommonUtil.successJson(goodservice.getAllGoodsByState(state,pageSize,pageNo,appId,classId));
	}

	@RequestMapping("/getGoodsById")
	public Object getGoodsById(String goodsId) {
		return  CommonUtil.successJson(goodservice.getGoodsById(goodsId));
	}
	
	@RequestMapping("/updateGoodsByIds")
	public Object updateGoodsByIds(String state,String ids) {
		return  CommonUtil.successJson(goodservice.updateGoodsByIds(state,ids));
	}
}
