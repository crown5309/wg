package com.heeexy.example.frontController;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heeexy.example.service.OrderService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.XMLUtil;

@RestController
@RequestMapping("/front")
public class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * 提交订单
	 * @param goodsId-商品id
	 * @param count-商品数量
	 * @param cartIds -购物车id集合
	 * @return
	 */
	@RequestMapping("/submitOrder")
	public Object submitOrder(String goodsId,String count,String cartIds) {
		return  CommonUtil.successJson(orderService.submitOrder(goodsId,count,cartIds));
	}
	/**
	 * 获取支付信息
	 * @param orderIds
	 * @return
	 */
	@RequestMapping("/getOrderInfo")
	public Object getOrderInfo(String orderIds) {
		return  CommonUtil.successJson(orderService.getOrderInfo(orderIds));
	}
	/**
	 * 支付
	 * @param orderIds
	 * @param addressId
	 * @param remarks
	 * @return
	 */
	@RequestMapping("/pay")
	public Object pay(String orderIds,String addressId,String remarks,HttpServletRequest request) {
		return  orderService.pay(orderIds,addressId,remarks,request);
	}
	 /**
     * 微信小程序支付成功回调函数
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/weixin/callback")
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
 
        Map map = XMLUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
        	//订单支付金额
			String total_fee =(String) map.get("total_fee");
			String out_trade_no =(String) map.get("out_trade_no"); 
			orderService.updateByOutTradeNo(out_trade_no,2);//
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
 
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

}
