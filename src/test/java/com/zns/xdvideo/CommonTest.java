package com.zns.xdvideo;

import com.zns.xdvideo.domain.User;
import com.zns.xdvideo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;

/**
 * @功能描述：TODO
 * @创建日期: 2019/4/15 18:41
 */
public class CommonTest {

    @Test
    public void produceToken(){
        User user = new User();
        user.setId(99);
        user.setName("xdddd");
        user.setHeadImg("www.baidu.com");
        String s = JwtUtils.geneJsonWebToken(user);
        System.out.println(s);
    }

    @Test
    public void result(){
       String token = "1eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjk5LCJuYW1lIjoieGRkZGQiLCJpbWciOiJ3d3cuYmFpZHUuY29tIiwiaWF0IjoxNTU1MzI0OTc5LCJleHAiOjE1NTU5Mjk3Nzl9.WlHre0LvCh_SutUdBAOLEPuL6TBq_9E9iz7JzPWv7s0";
        Claims claims = JwtUtils.checkJWT(token);
        if(claims!=null){
            Integer userId = (Integer) claims.get("id");
            String name = (String) claims.get("name");
            String img = (String) claims.get("img");
            System.out.println(userId + " " + name + " " + img);
        }else{
            System.out.println("Token 被篡改！");
        }
    }
}
