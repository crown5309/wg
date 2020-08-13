package com.heeexy.example.frontController;



import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.heeexy.example.service.JingDongService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kemp on 2018/8/15.
 */
@Controller
@RequestMapping("/index")
public class MyController {
	@Autowired
	private JingDongService jingDongService;
	@RequestMapping("/")
    public String test(){ 
        return "index";
    }
    @RequestMapping("getProduct")
    @ResponseBody
    public Object getProduct(int pageNo,int pageSize,String keyword,String type){ 
        return jingDongService.getProduct(pageNo,pageSize,keyword,type);
    }
    /**
     * 查询商品
     */
    @GetMapping("/listgoods")
    @ResponseBody
    public JSONObject listgoods(HttpServletRequest request) {
        return jingDongService.listgoods(CommonUtil.request2Json(request));
    }

    /**
     * 新增商品
     */
    @RequestMapping("/addgoods")
    @ResponseBody
    public JSONObject addgoods(@RequestBody JSONObject requestJson) {
        return jingDongService.addgoods(requestJson);
    }

    /**
     * 修改商品
     */
    @RequestMapping("/updategoods")
    @ResponseBody
    public JSONObject updategoods(@RequestBody JSONObject requestJson) {
        return jingDongService.updategoods(requestJson);
    }
    /**
     * 删除商品
     */
    @RequestMapping("/deletegoods")
    @ResponseBody
    public JSONObject deletegoods(int id) {
        return jingDongService.deletegoods(id);
    }
    @RequestMapping("getBanner")
    @ResponseBody
    public Object getBanner(){
        return jingDongService.getBanner();
    }
    /**
     * 查询banner
     */
    @GetMapping("/listMyBanner")
    @ResponseBody
    public JSONObject listMyBanner(HttpServletRequest request) {
        return jingDongService.listMyBanner(CommonUtil.request2Json(request));
    }

    /**
     * 新增banner
     */
    @RequestMapping("/addMyBanner")
    @ResponseBody
    public JSONObject addMyBanner(@RequestBody JSONObject requestJson) {
        return jingDongService.addMyBanner(requestJson);
    }

    /**
     * 修改banner
     */
    @RequestMapping("/updateMyBanner")
    @ResponseBody
    public JSONObject updateMyBanner(@RequestBody JSONObject requestJson) {
        return jingDongService.updateMyBanner(requestJson);
    }
    /**
     * 删除banner
     */
    @RequestMapping("/deleteMyBanner")
    @ResponseBody
    public JSONObject deleteMyBanner(int id) {
        return jingDongService.deleteMyBanner(id);
    }
}