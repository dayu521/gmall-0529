package com.atguigu.gmall.test;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    private String key = "fdsajljfdskljfkdlsajf;d;lasfjkdsafdskhfdashj";
    @Test
    public  void createJwtToken(){
        Map<String,Object> user = new HashMap<>();
        user.put("id","123");
        user.put("userName","张三");

        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        String string = Jwts.builder()
                .addClaims(user)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();


        System.out.println(string);
        //eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEyMyIsInVzZXJOYW1lIjoi5byg5LiJIn0.QVyyuDq8Tsj0YQTjGpdZkcmlj34Pb5eniXecxSGgB1k
    }

    @Test
    public void verf(){
        String s = "eyJhbGciOiJIUzI1NiJ9.ey1JpZCI6IjEyMyIsInVzZXJOYW1lIjoi5byg5LiJIn0.QVyyuDq8Tsj0YQTjGpdZkcmlj34Pb5eniXecxSGgB1k";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

       Jwts.parser().setSigningKey(secretKey)
                .parse(s);



    }
}
