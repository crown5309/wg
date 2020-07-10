package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface SysParamsService {
    JSONObject listSysParams(JSONObject request2Json);

    Object addSysParams(JSONObject request2Json);

    JSONObject updateSysParams(JSONObject request2Json);

    JSONObject deleteSysParams(JSONObject request2Json);
}
