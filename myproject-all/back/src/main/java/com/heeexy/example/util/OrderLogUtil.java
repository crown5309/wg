package com.heeexy.example.util;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AddressDao;
import com.heeexy.example.dao.OrderLogDao;

import java.util.ArrayList;
import java.util.List;

public class OrderLogUtil {
    public static void inserOrderLog(String orderIds, String userId, int state, String stateName, JSONObject address, OrderLogDao orderLogDao
    , AddressDao addressDao) {
        List<JSONObject> orderLogList=new ArrayList<JSONObject>();//需要插入的订单日志集合
        JSONObject orderLog=null;
        List<JSONObject> orderAddressList=new ArrayList<JSONObject>();//需要插入的订单日志集合
        JSONObject orderAddress=null;
        for(String s:orderIds.split(",")) {
            orderLog=new JSONObject();
            orderLog.put("state", state);
            orderLog.put("stateName", stateName);
            orderLog.put("userId", userId);
            orderLog.put("orderId", s);
            orderLogList.add(orderLog);
            if(address!=null) {
                orderAddress=new JSONObject();
                orderAddress.put("name", address.get("name"));
                orderAddress.put("province", address.get("province"));
                orderAddress.put("city", address.get("city"));
                orderAddress.put("country", address.get("country"));
                orderAddress.put("detail", address.get("detail"));
                orderAddress.put("userId", address.get("userId"));
                orderAddress.put("telephone", address.get("telephone"));
                orderAddress.put("orderId", s);
                orderAddressList.add(orderAddress);
            }

        }
        //订单日志
        orderLogDao.insertOrderLogBatch(orderLogList);
        //
        if(address!=null) {
            addressDao.insertOrderAddressBatch(orderAddressList);
        }

    }
}
