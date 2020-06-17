package com.heeexy.example.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface AddressDao {

	void addAddress(JSONObject request2Json);

	void updateAddress(JSONObject request2Json);

	List<JSONObject> getAddressList(Integer integer);

	Object getaddressById(String addressId);

	void updateAddressIsDefault(JSONObject request2Json);

	JSONObject getaddressDefault(String userId);

}
