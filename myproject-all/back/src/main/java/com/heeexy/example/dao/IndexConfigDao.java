package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.entity.ClassesInfo;

public interface IndexConfigDao {

	List<JSONObject> listIndexConfigClasses(String type);

	List<JSONObject> listIndexConfigBanner(@Param("type")String type,@Param("appId") String appId);

	List<ClassesInfo> listIndexAll(String appId);

}
