package com.heeexy.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface OrderLogDao {

	void insertOrderLog(JSONObject orderLog);

	void insertOrderLogBatch(@Param("list")List<JSONObject> orderLogList);

}
