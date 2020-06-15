package com.heeexy.example.frontController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.goodsClassService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/front")
public class GoodsFrontClassController {

	@Autowired
	private goodsClassService goodsClassService;

	/**
	 * 查询商品分类列表
	 */
	@RequestMapping("/getAllGoodsClass")
	public Object getAllGoodsClass(String appId) {
		return  CommonUtil.successJson(goodsClassService.getAllGoodsClass(appId));
	}
}
