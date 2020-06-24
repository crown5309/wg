package com.heeexy.example.service;

public interface CartService {

	Object addToMyCart(String goodsId, int count);

	Object updateCartCountById(String id, int count);

	Object getMyCartList(Integer pageNo, Integer pageSize);

	Object deleteMyCartById(String id);

}
