package com.heeexy.example.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.entity.ClassesInfo;

public interface IndexConfigDao {

	List<JSONObject> listIndexConfigClasses(String type);

	List<JSONObject> listIndexConfigBanner(String type);

	List<ClassesInfo> listIndexAll(String appId);

}
