package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface GoodsService {

	Object addGoods(JSONObject request2Json);

	Object getAllGoodsByState(String state, int pageSize,int pageNo,String appId, String classId);

	Object updateGoodsByIds(String state, String ids);

	Object getGoodsById(String goodsId);

}
