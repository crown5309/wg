package com.heeexy.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heeexy.example.entity.GiftDetailModel;
import com.heeexy.example.entity.KuaiDi100QueryResp;


/**
 * 快递100接口
 * <p>作成日： 2017/01/31 <p/>
 * @author B2B2C商城
 * @Version 1.0
 */
public class KuaiDi100 {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KuaiDi100.class);

	public  KuaiDi100QueryResp getKuiDicContent(String customer,String key,Map<String,String> paramMap) {
		String data = null;
		try {
			String param =JsonUtils.toJsonStr(paramMap);
			logger.info("param_json={}",param);
			String sign = MD5Util.md5Hex(param+key+customer).toUpperCase();
			HashMap<String,String> paramsMap = new HashMap<String,String>();
			paramsMap.put("param",param);
			paramsMap.put("sign",sign);
			paramsMap.put("customer",customer);
			paramsMap.put("order","desc");
			String result=HttpClientUtils.sendHttpPostRequest("http://poll.kuaidi100.com/poll/query.do", paramsMap, data,"map",50000).toString();
			KuaiDi100QueryResp resp= JsonUtils.fromJson(result,KuaiDi100QueryResp.class);

			return resp;
		} catch (Exception e) {
			logger.error("KuaiDi100 getKuiDicContent",e);
		}
		return null;
	}
}
