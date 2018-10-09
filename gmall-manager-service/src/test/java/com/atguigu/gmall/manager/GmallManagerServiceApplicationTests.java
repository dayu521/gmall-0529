package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
