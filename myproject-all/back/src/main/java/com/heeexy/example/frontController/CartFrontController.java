package com.heeexy.example.frontController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.CartService;

@RestController
@RequestMapping("/front")
public class CartFrontController {

	@Autowired
	private CartService cartService;

	
	@RequestMapping("/addToMyCart")
	public Object addToMyCart(String goodsId,int count) {
		return  cartService.addToMyCart(goodsId,count);
	}
	
	@RequestMapping("/updateCartCountById")
	public Object updateCartCountById(String id,int count) {
		return  cartService.updateCartCountById(id,count);
	}

	@RequestMapping("/getMyCartList")
	public Object getMyCartList(Integer pageNo,Integer pageSize) {
		return  cartService.getMyCartList(pageNo,pageSize);
	}
	@RequestMapping("/deleteMyCartById")
	public Object deleteMyCartById(String id) {
		return  cartService.deleteMyCartById(id);
	}
}
