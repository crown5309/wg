package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ShopUserDao {
    int countShopUser(JSONObject request2Json);

    List<JSONObject> listShopUser(JSONObject request2Json);

    List<JSONObject> serachShopUser(JSONObject world);

    void changeStoreId(JSONObject j);

    void deleteShopUser(String id);
}
