package com.heeexy.example.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 *  * 订单编码码生成器，生成32位数字编码，  * @生成规则 1位单号类型+17位时间戳+14位(用户id加密&随机数)  *
 * Date:2017年9月8日上午10:05:19  * @author jiwengjian  
 */
public class OrderCodeFactory {
	 /**
     * 根据UUID生成订单号
     * @return
     */
    public static String getOrderIdByUUId()
    {
    	int value = UUID.randomUUID().toString().hashCode(); 
		if (value < 0) {
			value = -value;
		}
		//0代表前面补充0，10代表长度，d代表正整数
		String orderId = String.format("%010d", value);
		return orderId;
    }
public static void main(String[] args) {
	System.out.println((DateUtil.format(new Date(), "yyyyMMddHHmmss")+getOrderIdByUUId()).length());
}
}
