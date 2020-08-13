package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface JingDongService {

	Object getProduct(int pageNo, int pageSize, String keyword, String type);

    Object getBanner();

    JSONObject listgoods(JSONObject request2Json);

    JSONObject addgoods(JSONObject requestJson);

    JSONObject updategoods(JSONObject requestJson);

    JSONObject deletegoods(int id);

    JSONObject listMyBanner(JSONObject request2Json);

    JSONObject addMyBanner(JSONObject requestJson);

    JSONObject updateMyBanner(JSONObject requestJson);

    JSONObject deleteMyBanner(int id);
}
