package com.atguigu.gmall.passport;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallPassportServiceApplicationTests {

	@Test
	public void contextLoads() throws UnsupportedEncodingException {


		//e10adc3949ba59abbe56e057f20f883e
		//e10adc3949ba59abbe56e057f20f883e
		String s = DigestUtils.md5Hex("123456");
		System.out.println(s);

		//$1$nEW3h.Ao$K/X5WETV53cmsTgrk3mfi.
		//$1$Mk.dZ1ke$kCASkC1rtv2n8DsQQLp.n.

//		String crypt = Md5Crypt.md5Crypt("123456".getBytes());
//		System.out.println(crypt);

	}

}
