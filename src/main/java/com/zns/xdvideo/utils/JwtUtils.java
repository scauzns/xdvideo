package com.zns.xdvideo.utils;

import com.zns.xdvideo.domain.User;
import io.jsonwebtoken.*;

import java.util.Date;

/**
 * jwt工具类,通过一定规范来生成token，然后可以通过解密算法逆向解密token，这样就可以获取用户信息
 * 优点：
 *   1）生产的token可以包含基本信息，比如id、用户昵称、头像等信息，避免再次查库
 *
 *   2）存储在客户端，不占用服务端的内存资源
 *
 * 缺点：
 *  token是经过base64编码，所以可以解码，因此token加密前的对象不应该包含敏感信息
 *   如用户权限，密码等
 */
public class JwtUtils {
    public static final String SUBJECT = "xdclass";

    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7; //过期时间为一周

    public static final String APPSECRET = "xd666";

    public static String geneJsonWebToken(User user){
        if(user == null || user.getId()==null || user.getName()==null || user.getHeadImg()==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT).claim("id", user.getId())
                                                        .claim("name", user.getName())
                                                        .claim("img",user.getHeadImg())
                                                        .setIssuedAt(new Date())  //什么时候发布
                                                        .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                                                        .signWith(SignatureAlgorithm.HS256, APPSECRET)
                                                        .compact();
        return token;
    }

    public static Claims checkJWT(String token){
        final Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {

        }
        return null;
    }
}
