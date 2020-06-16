package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface AddressService {

	Object addAddress(JSONObject request2Json);

	Object getAddressList();

	Object getaddressById(String addressId);

}
