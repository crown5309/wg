package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

public interface IndexConfigDao {

	JSONObject listIndexConfigClasses(String type);

	JSONObject listIndexConfigBanner(String type);

}
