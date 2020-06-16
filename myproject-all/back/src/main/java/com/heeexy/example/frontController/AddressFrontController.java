package com.heeexy.example.frontController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.AddressService;
import com.heeexy.example.util.CommonUtil;

@RestController
@RequestMapping("/front")
public class AddressFrontController {

	@Autowired
	private AddressService addresService;

	
	@RequestMapping("/addAddress")
	public Object addAddress(HttpServletRequest request) {
		return  CommonUtil.successJson(addresService.addAddress(CommonUtil.request2Json(request)));
	}
	
	@RequestMapping("/getAddressList")
	public Object getAddressList() {
		return  CommonUtil.successJson(addresService.getAddressList());
	}

	@RequestMapping("/getAddressById")
	public Object getAddressById(String addressId) {
		return  CommonUtil.successJson(addresService.getaddressById(addressId));
	}
	
}
