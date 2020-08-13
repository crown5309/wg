package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface JingDongDao {

	List<Map<String,Object>> getProduct(int pageNo, int pageSize, String keyword, String type);


    List<Map<String, Object>> getBanner();

    int countGoods(JSONObject request2Json);

    List<JSONObject> listGoods(JSONObject request2Json);

    void addgoods(JSONObject requestJson);

    void updategoods(JSONObject requestJson);

    void deletegoods(int id);

    int countMyBanner(JSONObject request2Json);

    List<JSONObject> listMyBanner(JSONObject request2Json);

    void addMyBanner(JSONObject requestJson);

    void updateMyBanner(JSONObject requestJson);

    void deleteMyBanner(int id);
}
