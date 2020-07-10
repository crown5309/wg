package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.SysParamDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.SysParamsService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysParamsServiceImpl extends BaseService  implements SysParamsService {
    @Autowired
    private SysParamDao sysParamDao;
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
    public Object addSysParams(JSONObject request2Json) {
        getAppId(request2Json);
        String valueByCode = sysParamDao.getValueByCode(request2Json.getString("appId"), request2Json.getString("code"));
        if(!StringTools.isNullOrEmpty(valueByCode)){
            return CommonUtil.errorJson("该code已存在");
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
