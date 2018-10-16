package com.atguigu.gmall.manager.constant;

/**
 * Redis中所有缓存使用的key的一些规定
 */
public class RedisCacheKeyConst {

    public static final String SKU_INFO_PREFIX = "sku:";
    public static final String SKU_INFO_SUFFIX= ":info";
    public static final Integer SKU_INFO_TIMEOUT= 60*60*24; //setex是秒为单位的
    public static final Integer LOCK_TIMEOUT = 3;//默认锁的超时时间
    public static final String LOCK_SKU_INFO = "gmall:lock:sku";
    public static final Integer SKU_INFO_NULL_TIMEOUT= 60*5; //setex是秒为单位的


}
