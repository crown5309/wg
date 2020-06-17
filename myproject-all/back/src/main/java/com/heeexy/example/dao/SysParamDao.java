package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface SysParamDao {

	List<JSONObject> getParamInfo(@Param("appId")String appId ,@Param("code") String code);

}
