package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ShopUserDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.ShopUserService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShopUserServiceImpl extends BaseService implements ShopUserService {
    @Autowired
    private ShopUserDao shopUserDao;
    @Override
    public JSONObject listShopUser(JSONObject request2Json) {
        CommonUtil.fillPageParam(request2Json);
        getAppId(request2Json);
        int count = shopUserDao.countShopUser(request2Json);
        List<JSONObject> list = shopUserDao.listShopUser(request2Json);
        JSONObject successPage = CommonUtil.successPage(request2Json, list, count);
        return successPage;
    }

    @Override
    public JSONObject addShopUser(String userIds) {
        String[] array = userIds.split(",");
        JSONObject j=new JSONObject();
        getAppId(j);
        j.put("array",array);
        shopUserDao.changeStoreId(j);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateShopUser(JSONObject requestJson) {
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject deleteShopUser(String id) {
        shopUserDao.deleteShopUser(id);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject serachShopUser(String world) {
        JSONObject j=new JSONObject();
        getAppId(j);
        j.put("world",world);
        List<JSONObject> list=shopUserDao.serachShopUser(j);
        return CommonUtil.successJson(list);
    }
}
