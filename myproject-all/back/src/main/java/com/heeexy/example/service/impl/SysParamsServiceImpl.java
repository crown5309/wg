package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.LoginDao;
import com.heeexy.example.dao.ShopDao;
import com.heeexy.example.dao.SysParamDao;
import com.heeexy.example.dao.UserDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.SysParamsService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysParamsServiceImpl extends BaseService  implements SysParamsService {
    @Autowired
    private SysParamDao sysParamDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginDao loginDao;
    @Override
    public JSONObject listSysParams(JSONObject request2Json) {
        CommonUtil.fillPageParam(request2Json);
        getAppId(request2Json);
        int count = sysParamDao.countParams(request2Json);
        List<JSONObject> list = sysParamDao.listParams(request2Json);
        JSONObject successPage = CommonUtil.successPage(request2Json, list, count);
        return successPage;
    }

    @Override
    @Transactional
    public Object addSysParams(JSONObject request2Json) {
        getAppId(request2Json);
        String code = request2Json.getString("code");
        String appId = request2Json.getString("appId");
        String valueByCode = sysParamDao.getValueByCode(appId, code);
        if(!StringTools.isNullOrEmpty(valueByCode)){
            return CommonUtil.errorJson("该code已存在");
        }
        String username = request2Json.getString("username");
        if("sys_param".equals(code)&&username==null){
            String myName = request2Json.getString("myName");
            int count=loginDao.getExitsUser(myName);
            if(count>0){
                return CommonUtil.errorJson("用户名已存在");
            }
            //添加商户
            JSONObject shopJson=new JSONObject();
            shopJson.put("storeName",request2Json.getString("value"));
            shopJson.put("state",2);
            shopJson.put("appId",appId);
            Integer storeId = shopDao.addShop(shopJson);
            //添加用户对应平台管理员
            JSONObject userJson=new JSONObject();

            userJson.put("username", myName);
            userJson.put("password",request2Json.getString("password"));
            userJson.put("roleId","1");
            userJson.put("appId",appId);
            userJson.put("storeId",storeId);
            userDao.addUser(userJson);
        }
        sysParamDao.addSysParams(request2Json);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateSysParams(JSONObject request2Json) {
        getAppId(request2Json);
        sysParamDao.updateSysParams(request2Json);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional
    public JSONObject deleteSysParams(JSONObject request2Json) {
        sysParamDao.deleteSysParamsById(request2Json.getIntValue("id"));
        int parentId = request2Json.getIntValue("parentId");
        if(parentId !=0){
            sysParamDao.deleteSysParamsParentId(parentId);
        }
        return CommonUtil.successJson();
    }
}
