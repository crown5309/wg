package com.heeexy.example.util;

import java.util.Date;
import java.util.UUID;


public class OrderIdFactory {
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
    public static String getOrderIdByUUIdAndDate()
    {
    	int value = UUID.randomUUID().toString().hashCode(); 
		if (value < 0) {
			value = -value;
		}
		//0代表前面补充0，10代表长度，d代表正整数
		String orderId = String.format("%010d", value);
		return DateUtil.format(new Date(), "yyyyMMddHHmmss")+orderId;
    }
public static void main(String[] args) {
	
}
}
