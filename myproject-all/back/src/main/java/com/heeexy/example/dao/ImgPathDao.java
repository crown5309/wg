package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImgPathDao {
    void addImgPath(JSONObject json);

    void updateImgPathBatch(@Param("list")List<JSONObject> list);

    void updateImgPath(JSONObject json);

    int countImgByState(int i);

    List<JSONObject> getImgByState(JSONObject json);

    void deleteBatch(@Param("ids")List<Integer> ids);
}
