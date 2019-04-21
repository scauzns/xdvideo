package com.zns.xdvideo.service.impl;

import com.zns.xdvideo.config.WechatConfig;
import com.zns.xdvideo.domain.User;
import com.zns.xdvideo.domain.UserExample;
import com.zns.xdvideo.mapper.UserMapper;
import com.zns.xdvideo.service.UserService;
import com.zns.xdvideo.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WechatConfig wechatConfig;
    @Override
    public User saveWeChatUser(String code) {
        //获取access_token
        String accessTokenUrl = String.format(WechatConfig.getOpenAccessTokenUrl(), wechatConfig.getOpenAppid(),wechatConfig.getOpenAppSecret(),code);
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        if(baseMap==null || baseMap.isEmpty()){
            return null;
        }
        String accessToken = (String)baseMap.get("access_token");
        String openId  = (String) baseMap.get("openid");

        //查找数据库用户是否存在
        UserExample example = new UserExample();
        example.createCriteria().andOpenidEqualTo(openId);
        List<User> list = userMapper.selectByExample(example);
        if(list!=null && list.size()!=0){
            return list.get(0);
        }

        //获取用户基本信息
        String userInfoUrl = String.format(WechatConfig.getOpenUserInfoUrl(),accessToken, openId);
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoUrl);
        if(baseUserMap==null || baseUserMap.isEmpty()){
            return null;
        }
        String nickname = (String) baseUserMap.get("nickname");
        Double sexTemp = (Double) baseUserMap.get("sex");
        //普通用户性别，1为男性，2为女性
        int sex = sexTemp.intValue();
        String province = (String)baseUserMap.get("province");
        String city = (String)baseUserMap.get("city");
        String country = (String)baseUserMap.get("country");
        String headimgurl = (String)baseUserMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();
        try {
            //解决乱码
            nickname = new String(nickname.getBytes("ISO-8859-1"),"UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex((byte) sex);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return user;
    }
}
