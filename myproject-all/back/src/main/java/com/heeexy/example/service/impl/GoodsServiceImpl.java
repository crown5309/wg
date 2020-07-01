package com.heeexy.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GoodsClassDao;
import com.heeexy.example.dao.GoodsDao;
import com.heeexy.example.service.BaseService;
import com.heeexy.example.service.GoodsService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
@Service
public class GoodsServiceImpl  extends BaseService implements GoodsService{
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsClassDao goodsClassDao;
	@Override
	public Object addGoods(JSONObject request2Json) {
		// TODO Auto-generated method stub
		getAppId(request2Json);
		goodsDao.addGoods(request2Json);
		return CommonUtil.successJson();
	}
	@Override
	public Object getAllGoodsByState(String state, int pageSize,int pageNo,String appId,String classId,String goodsName, String type, String priceFlag) {
		// TODO Auto-generated method stub
		pageNo=pageSize*(pageNo-1);
		List<JSONObject> list=goodsDao.getAllGoodsByState(state,pageSize,pageNo,appId,classId,goodsName,type,priceFlag);
		String[] split =null;
		for(int i=0;i<list.size();i++) {
			split = list.get(i).getString("bannerUrl").split(",");
			list.get(i).put("bannerUrl",split);
			list.get(i).put("imageurl",split[0]);
		list.get(i).put("isSelect",false);
		}
		return list;
	}
	@Override
	public Object updateGoodsByIds(String state, String ids) {
		String[] split = ids.split(",");
		// TODO Auto-generated method stub
		goodsDao.updateGoodsByIds(state,split);
		return CommonUtil.successJson();
	}
	@Override
	public Object getGoodsById(String goodsId) {
		// TODO Auto-generated method stub
		JSONObject goodsById = goodsDao.getGoodsById(goodsId);
		goodsById.put("bannerUrl",goodsById.getString("bannerUrl").split(","));
		goodsById.put("detailUrl",goodsById.getString("detailUrl").split(","));
		return goodsById;
	}
	@Override
	public JSONObject addGoods(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JSONObject listGoods(JSONObject request2Json) {
		// TODO Auto-generated method stub
		CommonUtil.fillPageParam(request2Json);
		getAppId(request2Json);
		int count = goodsDao.countGoods(request2Json);
		List<JSONObject> list = goodsDao.listGoods(request2Json);
		for(JSONObject j:list) {
			getStateName(j);
			j.put("createTime", DateUtil.format(j.getDate("createTime"), DateUtil.DATE_TIME));
		}
		//商品分类
		JSONObject successPage = CommonUtil.successPage(request2Json, list, count);
		successPage.getJSONObject("info").put("goodsClassList", getGoodsClass(request2Json.getString("appId")));
		return successPage;
	}
	@Override
	public JSONObject updateGoods(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		goodsDao.updateGoods(jsonObject);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject auditGoods(JSONObject request2Json) {
		JSONObject goods = goodsDao.getGoodsById(request2Json.getString("id"));
		int state = goods.getIntValue("state");
		int state1 = request2Json.getIntValue("state");
		String name="上架";
		if(!(state1 ==1||state1 ==4)){
			return CommonUtil.errorJson("状态传递错误");
		}
		if(state!=0){
			return CommonUtil.errorJson("该状态下商品不能审核");
		}

		goodsDao.updateGoods(request2Json);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject upAndDownGoods(JSONObject request2Json) {
		JSONObject goods = goodsDao.getGoodsById(request2Json.getString("id"));
		int state = goods.getIntValue("state");
		int state1 = request2Json.getIntValue("state");
		String name="上架";
		if(state1 ==2){
			name="上架";
		}else if(state1==3){
			name="下架";
		}else {
			return CommonUtil.errorJson("状态传递错误");
		}
		if(state==0||state==4){
			return CommonUtil.errorJson("该状态下商品不能"+name);
		}

		goodsDao.updateGoods(request2Json);
		return CommonUtil.successJson();
	}


	private void getStateName(JSONObject json) {
		//0 待审核 1审核通过  2 上架 3下架 4拒绝
		Integer state = json.getInteger("state");
		if(state==0) {
			json.put("stateName", "待审核");
		}else if(state==1) {
			json.put("stateName", "审核通过");
		}else if(state==2) {
			json.put("stateName", "已上架");
		}else if(state==3) {
			json.put("stateName", "已下架");
		}else if(state==4) {
			json.put("stateName", "拒绝通过");
		}
		
	}
	private List<JSONObject> getGoodsClass(String appId){
		List<JSONObject> list=goodsClassDao.getAllGoodsClass(appId);
		List<JSONObject> list1=new ArrayList<JSONObject>();
		JSONObject one=null;
		List<JSONObject> list2=null;
		JSONObject two=null;
		List<JSONObject> list3=null;
		JSONObject three=null;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getInteger("classLevel")==1) {//一级
				one=new JSONObject();
				one.put("value", list.get(i).get("id"));
				one.put("label", list.get(i).get("className"));
				list2=new ArrayList<JSONObject>();
				for(int j=0;j<list.size();j++) {
					if(list.get(j).getInteger("classLevel")==2&&list.get(j).get("parentId").equals(list.get(i).get("id"))) {//二级
						two=new JSONObject();
						two.put("value", list.get(j).get("id"));
						two.put("label", list.get(j).get("className"));
						list3=new ArrayList<JSONObject>();
						for(int k=0;k<list.size();k++) {
							if(list.get(k).getInteger("classLevel")==3&&list.get(k).get("parentId").equals(list.get(j).get("id"))) {//三级
								three=new JSONObject();
								three.put("value", list.get(k).get("id"));
								three.put("label", list.get(k).get("className"));
								list3.add(three);
							}
						}
						two.put("children", list3);
						list2.add(two);
					}
					
				}
				one.put("children", list2);
				list1.add(one);
			}
			
		}
		return list1;
	}
}
