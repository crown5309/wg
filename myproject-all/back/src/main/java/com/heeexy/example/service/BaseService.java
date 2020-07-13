package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

public abstract class BaseService {
    public void getAppId(JSONObject request2Json) {
        Session session = SecurityUtils.getSubject().getSession(); JSONObject
                userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        String appId =userInfo.getString("appId");
        request2Json.put("appId", appId);
        request2Json.put("storeId", userInfo.getString("storeId"));
        request2Json.put("userId", userInfo.getString("userId"));
        String username = userInfo.getString("username");
        if("root".equals(username)){
            request2Json.put("username",username );
        }else{
            request2Json.put("username",null );
        }


    }
}

