package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    /*生成JWT令牌*/
    @Test
    public void testGenerateJwt(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",1);
        dataMap.put("username","admin");

        String jwt = Jwts.builder() //生成JWT令牌的方法
                .signWith(SignatureAlgorithm.HS256,"aXRoZWltYQo=")  //指定加密算法、密钥
                .addClaims(dataMap) //添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000)) //设置过期时间
                .compact(); //生成令牌
        System.out.println(jwt);
    }

    /*生成JWT令牌*/
    @Test
    public void testParseJWT(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc4MzM0OTM3MH0.-X-lBdvYneaoNWGcvsW4Fk8jgQQqbWKRxd0SN8cgxuE";
        Claims claims = Jwts.parser() //解析令牌的方法
                .setSigningKey("aXRoZWltYQo=") //指定密钥
                .parseClaimsJws(token) //解析令牌
                .getBody(); //获取自定义信息
        System.out.println(claims);
    }

    /*业务逻辑是为了拿到自定义信息，因此问题就成了怎么从令牌中截取出自定义信息
    * 令牌被两个.分割成三部分
    * 第一部分：签名算法的base编码
    * 第二部分：自定义信息，用一个HashMap封装
    * 第三部分：设置的过期时间
    * 首先用一个变量token接收令牌
    * 然后调用Jwts中的parser()方法进行令牌解析
    * 其次是调用setSignKey指定密钥
    * 然后调用parseClaimsJwt()解析token
    * 最后调用getBody()获取自定义的信息*/
}
