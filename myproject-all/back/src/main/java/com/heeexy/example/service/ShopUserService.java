package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface ShopUserService {
    JSONObject listShopUser(JSONObject request2Json);

    JSONObject addShopUser(String userIds);

    JSONObject updateShopUser(JSONObject requestJson);

    JSONObject deleteShopUser(String id);

    JSONObject serachShopUser(String world);
}
