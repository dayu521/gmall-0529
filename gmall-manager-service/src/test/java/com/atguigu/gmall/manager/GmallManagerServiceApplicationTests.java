package com.atguigu.gmall.manager;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.UserMapper;
import com.atguigu.gmall.manager.sku.SkuAttrValue;
import com.atguigu.gmall.manager.sku.SkuImage;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuSaleAttrValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 1、导入mybatis-plus的starter
 * 2、编写javaBaen。编写mapper接口（继承BaseMapper）
 * 3、@MapperScan("com.atguigu.gmall.manager.mapper")
 *
 * 高级：
 * 	1）、逻辑删除
 * 		1、在application.properties说明逻辑删除的规则
 * 	    2、在javaBean里面加上逻辑删除字段并且用@TableLogic
 * 	    3、自定义一个mybatisplus的配置类，注入逻辑删除插件即可
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerServiceApplicationTests {

	@Autowired
	UserMapper userMapper;

	@Autowired
	BaseCatalog1Mapper mapper;

	@Autowired
	CatalogService catalogService;


	@Autowired
	StringRedisTemplate stringRedisTemplate; //k-v都是string的

	@Autowired  //只需要注入jedis连接池
	JedisPool jedisPool;


	@Test
	public void testJedisPool(){
		Jedis jedis = jedisPool.getResource();
		String djsklajdal = jedis.get("djsklajdal");
		System.out.println(djsklajdal == null);

		String s = jedis.get("sku:77:info");
		System.out.println("null".equals(s));
	}

	//RedisTemplate //k-v都是object的
	//string list set hash zset
	@Test
	public void testRedisTemplate(){

		//1、创建
		//JedisPool jedisPool = new JedisPool();
		//jedisPool.initPool();...
		//2、从池中获取jedis客户端；
		//Jedis jedis = jedisPool.getResource();
		//3、springboot知道我么要用jedis，也自动配合了jedis的连接工厂


		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

		opsForValue.set("hello","world",20, TimeUnit.SECONDS);

		System.out.println("666.////");
		String hello = opsForValue.get("hello");
		System.out.println("返回的是："+hello);
	}


	@Test
	public void testSkuInfoJson(){

		/**
		 *     private Integer skuId;//当前图片对应的skuId
		 private String imgName;//图片的名字
		 private String imgUrl;//图片的url
		 private Integer spuImgId;//图片对应的spu_image表中的id
		 private String isDefault;//是否默认图片
		 */
		List<SkuImage> skuImages = new ArrayList<>();
		skuImages.add(new SkuImage(1,"黑色正面","hei.jpg",111,"0"));
		skuImages.add(new SkuImage(1,"黑色范面","heifan.jpg",112,"1"));


		/**
		 *     private Integer attrId;//平台属性id
		 private Integer valueId;//平台属性值id
		 private Integer skuId;//对应的skuId
		 */
		List<SkuAttrValue> skuAttrValues = new ArrayList<>();
		skuAttrValues.add(new SkuAttrValue(1,1,1));
		skuAttrValues.add(new SkuAttrValue(2,2,1));

		/**
		 *     //sku_id  sale_attr_id  sale_attr_value_id  sale_attr_name  sale_attr_value_name
		 private Integer skuId;//21
		 private Integer saleAttrId;//销售属性的id
		 private String saleAttrName;//销售属性的名字（冗余） ===【颜色】

		 private Integer saleAttrValueId;//销售属性值id
		 private Integer saleAttrValueName;//销售属性值的名字  ====【红色】
		 */
		List<SkuSaleAttrValue> skuSaleAttrValues = new ArrayList<>();
		skuSaleAttrValues.add(new SkuSaleAttrValue(1,1,"颜色",1,"黑色"));
		skuSaleAttrValues.add(new SkuSaleAttrValue(1,2,"版本",1,"6+64GB"));

//		SkuInfo skuInfo = new SkuInfo(51, new BigDecimal("50.99"), "三星 Glaxxxx9", "稳定爆炸..",
//				new BigDecimal("19.99"), 1, 3,
//				"http://xxxx.jpg",
//				skuImages, skuAttrValues, skuSaleAttrValues);

		//String s = JSON.toJSONString(skuInfo);
		//System.out.println(s);


	}


	@Test
	public void testCatalogService(){
		List<BaseCatalog1> allBaseCatalog1 = catalogService.getAllBaseCatalog1();
		log.info("一级分类信息：{}",allBaseCatalog1);

		List<BaseCatalog2> baseCatalog2ByC1id = catalogService.getBaseCatalog2ByC1id(allBaseCatalog1.get(0).getId());
		log.info("{} 的二级分类信息：{}",allBaseCatalog1.get(0),baseCatalog2ByC1id);

		List<BaseCatalog3> baseCatalog3ByC2id = catalogService.getBaseCatalog3ByC2id(baseCatalog2ByC1id.get(0).getId());
		log.info("{} 的三级分类信息：{}",baseCatalog2ByC1id.get(0),baseCatalog3ByC2id);
	}


	@Test
	public void testMapper(){
		BaseCatalog1 baseCatalog1 = new BaseCatalog1();
		baseCatalog1.setName("呵呵");
		mapper.insert(baseCatalog1);

		log.info("成功....，id是{},name是{}",baseCatalog1.getId(),baseCatalog1.getName());
	}


	@Test
	public void testLogicDelete(){
		userMapper.deleteById(2L);
		System.out.println("删除完成...");
		//以后的所有查询默认都是查未删除的
		for (User user : userMapper.selectList(null)) {
			System.out.println(user);
		}
		;
	}

	@Test
	public void contextLoads() {

		for (User user : userMapper.selectList(null)) {
			System.out.println(user);
		}
		;


		System.out.println("========");


		//要让xml生效一定加上mybatis-plus.mapper-locations=classpath:mapper/*.xml
		User user = new User();
		user.setName("Jack");
		user.setAge(20);
		User userByHaha = userMapper.getUserByHaha(user);
		System.out.println(userByHaha);




	}

}
