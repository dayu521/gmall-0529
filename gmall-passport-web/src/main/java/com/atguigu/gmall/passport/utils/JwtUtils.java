package com.atguigu.gmall.passport.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Map;

public class JwtUtils {
    
    private final static String keyStr = "fdjsalfjdsklajflksdkjfdsklajfdskhfjdslhuienfhds,fdskahj";
    
    public static String createJwtToken(Map<String,Object> body){

        SecretKey secretKey = Keys.hmacShaKeyFor(keyStr.getBytes());

        String compact = Jwts.builder().addClaims(body)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return compact;
    }

    /**
     * 解析令牌
     * @param jwtToken
     * @return
     */
    public static boolean confirmJwtToken(String jwtToken){
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStr.getBytes());

        try {
            Jwt parse = Jwts.parser().setSigningKey(secretKey)
                    .parse(jwtToken);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
