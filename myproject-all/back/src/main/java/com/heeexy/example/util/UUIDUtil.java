package com.heeexy.example.util;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * <p>做成日： 2017/01/31 <p/>
 * @author B2B2C商城
 * @Version 1.0
 */
public class UUIDUtil{
    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }

    /**
     * Activiti ID 生成
     */
    public String getNextId() {
        return UUIDUtil.uuid();
    }

    public Serializable generateId(Session session) {
        return UUIDUtil.uuid();
    }
}
