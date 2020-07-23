package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ImgPathDao;
import com.heeexy.example.service.ImgPathService;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImgPathServiceImpl implements ImgPathService {
    @Autowired
    private ImgPathDao imgPathDao;
    @Override
    @Transactional
    public void addImgPath(String path,String oldImg,String imgServerUrl) {
        JSONObject json=new JSONObject();
        if(!StringTools.isNullOrEmpty(path)){
            json.put("imgId",path);
            json.put("state",0);
            imgPathDao.addImgPath(json);
        }
        if(!StringTools.isNullOrEmpty(oldImg)){
            oldImg=oldImg.replaceAll(imgServerUrl+"/img/","");
            json.put("imgId",oldImg);
            json.put("state",0);
            imgPathDao.updateImgPath(json);
        }
    }
}
