package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.entity.ClassesInfo;

public interface IndexConfigDao {

	List<JSONObject> listIndexConfigClasses(@Param("type")String type,@Param("appId") String appId);

	List<JSONObject> listIndexConfigBanner(@Param("type")String type,@Param("appId") String appId);

	List<ClassesInfo> listIndexAll(String appId);

	List<String> listIndexCheck(String appId);

	void addIndexClass(@Param("appId")String appId, @Param("list")String[] parseArray);

	void deleteByAppId(String appId);

	int countBanner(JSONObject request2Json);

	List<JSONObject> listBanner(JSONObject request2Json);

	void addBanner(JSONObject userInfo);

	void updateBanner(JSONObject userInfo);

	void deleteBanner(String id);

}
