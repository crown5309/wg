package com.heeexy.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.ImgPathDao;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.ImgPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.heeexy.example.dao.JingDongDao;
import com.heeexy.example.service.JingDongService;
@Service
public class JingDongServiceImpl implements JingDongService {
	@Autowired
	private JingDongDao jingDongDao;
	@Autowired
	private ImgPathDao imgPathDao;

	@Value("${imgServerUrl}")
	private String imgServerUrl;
	private static  Map<String,Object> map=new HashMap<>();
	static {
		map.put("filter_1","平板电视");
		map.put("filter_2","冰箱");
		map.put("filter_3","洗衣机");
		map.put("filter_4","油烟机");
		map.put("filter_5","空调");
		map.put("filter_6","燃气灶");
		map.put("filter_7","集成灶");
		map.put("filter_8","燃气热水器");
		map.put("filter_9","电热水器");
		map.put("filter_10","冷柜/冰吧");
		map.put("filter_11","厨房小电");
		map.put("filter_12","生活电器");
	}
	@Override
	public Object getProduct(int pageNo, int pageSize, String keyword, String type) {
		// TODO Auto-generated method stub
		pageNo=(pageNo-1)*pageSize;
		List<Map<String, Object>> product = jingDongDao.getProduct(pageNo,pageSize,keyword,type);
		for(Map<String, Object> m:product) {
			String text=m.get("title")+"前台价："+m.get("price")+"推广价："+m.get("jingdong_price")+m.get("jingdong_link");
			m.put("text", text);
		}
		return product;
	}

	@Override
	public Object getBanner() {
		List<Map<String, Object>> banner = jingDongDao.getBanner();
		return banner;
	}

	@Override
	public JSONObject listgoods(JSONObject request2Json) {
		CommonUtil.fillPageParam(request2Json);
		int count = jingDongDao.countGoods(request2Json);
		List<JSONObject> list = jingDongDao.listGoods(request2Json);
		for(JSONObject js:list){
			js.put("filterName",map.get(js.getString("type")));
		}
		return CommonUtil.successPage(request2Json, list, count);
	}

	@Override
	public JSONObject addgoods(JSONObject requestJson) {
		jingDongDao.addgoods(requestJson);
		String classImgUrl = requestJson.getString("img_url");
		ImgPathUtils.updateImgPath(classImgUrl,imgPathDao,1,imgServerUrl);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject updategoods(JSONObject requestJson) {
		jingDongDao.updategoods(requestJson);
		String classImgUrl = requestJson.getString("img_url");
		ImgPathUtils.updateImgPath(classImgUrl,imgPathDao,1,imgServerUrl);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject deletegoods(int id) {
		jingDongDao.deletegoods(id);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject listMyBanner(JSONObject request2Json) {
		CommonUtil.fillPageParam(request2Json);
		int count = jingDongDao.countMyBanner(request2Json);
		List<JSONObject> list = jingDongDao.listMyBanner(request2Json);
		for(JSONObject js:list){
			js.put("filterName",map.get(js.getString("type")));
		}
		return CommonUtil.successPage(request2Json, list, count);
	}

	@Override
	public JSONObject addMyBanner(JSONObject requestJson) {
		jingDongDao.addMyBanner(requestJson);
		String classImgUrl = requestJson.getString("img_url");
		ImgPathUtils.updateImgPath(classImgUrl,imgPathDao,1,imgServerUrl);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject updateMyBanner(JSONObject requestJson) {
		jingDongDao.updateMyBanner(requestJson);
		String classImgUrl = requestJson.getString("img_url");
		ImgPathUtils.updateImgPath(classImgUrl,imgPathDao,1,imgServerUrl);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject deleteMyBanner(int id) {
		jingDongDao.deleteMyBanner(id);
		return CommonUtil.successJson();
	}

}
