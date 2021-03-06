package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface SysParamDao {

	List<JSONObject> getParamInfo(@Param("appId")String appId ,@Param("code") String code);

	String getValueByCode(@Param("appId")String appId ,@Param("code") String code);

	int countParams(JSONObject request2Json);

	List<JSONObject> listParams(JSONObject request2Json);

	void addSysParams(JSONObject request2Json);

	void updateSysParams(JSONObject request2Json);

	void deleteSysParamsById(int id);

	void deleteSysParamsParentId(int parentId);
}
