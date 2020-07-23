package com.heeexy.example.util;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ImgPathDao;

import java.util.ArrayList;
import java.util.List;

public class ImgPathUtils {
    public static void updateImgPath(String imgPaths, ImgPathDao imgPathDao,int state,String imgServerUrl){
        String[] imgPathArray = imgPaths.split(",");
        List<JSONObject> list =new ArrayList<JSONObject>();
        JSONObject imgJson=null;
        for(String imgPath:imgPathArray){
            imgPath=imgPath.replaceAll(imgServerUrl+"/img/","");
            imgJson=new JSONObject();
            imgJson.put("imgId",imgPath);
            imgJson.put("state",state);
            list.add(imgJson);
        }
        imgPathDao.updateImgPathBatch(list);
    }
}
