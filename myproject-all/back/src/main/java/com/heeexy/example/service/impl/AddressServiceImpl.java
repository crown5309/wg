package com.heeexy.example.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.AddressDao;
import com.heeexy.example.service.AddressService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.Constants;

@Service
public class AddressServiceImpl  implements AddressService	 {
	@Autowired
	private AddressDao addressDao;

	@Override
	@Transactional
	public Object addAddress(JSONObject request2Json) {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		request2Json.put("userId",  userInfo.getInteger("userId"));
		if("0".equals(request2Json.getString("id"))) {
			addressDao.addAddress(request2Json);
		}else {
			if(request2Json.getIntValue("is_default")==1) {
				addressDao.updateAddressIsDefault(request2Json);
			}
			addressDao.updateAddress(request2Json);
			
		}
		return CommonUtil.successJson();
	}

	@Override
	public Object getAddressList() {
		// TODO Auto-generated method stub
		Session session = SecurityUtils.getSubject().getSession();
		JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
		List<JSONObject> lsit=addressDao.getAddressList( userInfo.getInteger("userId"));
		return lsit;
	}

	@Override
	public Object getaddressById(String addressId) {
		// TODO Auto-generated method stub
		return addressDao.getaddressById(addressId);
	}

	@Override
	public Object deleteAddressById(String addressId) {
		// TODO Auto-generated method stub
		addressDao.deleteAddressById(addressId);
		return CommonUtil.successJson();
	}

}
