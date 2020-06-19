package com.heeexy.example.frontController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.UserService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.WxUtils;

@RestController
@RequestMapping("/user")
public class UserLoginController {
	@Autowired
	private UserService userService;
    @GetMapping("/auth")
    public Object auth(String code,String appid) {
    	
    	return userService.getWeiXinUserInfo(code,appid);
    }
    @GetMapping("/auth/phone")
    public Object authPhone(String encryptedData, String session_key, String iv) {
        try {
            String result = WxUtils.wxDecrypt(encryptedData, session_key, iv);
            JSONObject json = JSONObject.parseObject(result);
            if (json.containsKey("phoneNumber")) {
                String phone = json.getString("phoneNumber");
                String appid = json.getJSONObject("watermark").getString("appid");
                if (StringUtils.isNoneBlank(phone)) {
                	userService.updateMobile(appid, phone);
                	 return CommonUtil.successJson("成功");
                } else {
                    return CommonUtil.errorJson("失败！用户未绑定手机号");
                }
            } else {
               
                return CommonUtil.errorJson( "获取失败！");
            }
        } catch (Exception e) {
        	return CommonUtil.errorJson( "获取失败！");
        }
    }
}
