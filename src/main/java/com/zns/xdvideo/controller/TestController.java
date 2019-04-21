package com.zns.xdvideo.controller;

import com.zns.xdvideo.config.WechatConfig;
import com.zns.xdvideo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用的Controller
 */
@RestController
public class TestController {

    @Autowired
    private WechatConfig wechatConfig;

    @GetMapping("test")
    public Object test(){
        String str = wechatConfig.getAppid() + " " + wechatConfig.getAppsecret();
        return str;
    }

    @GetMapping("testUrl")
    public JsonData test2(){
        String str1 = wechatConfig.getOpenAppid() + "," + wechatConfig.getOpenAppSecret()+","
                    +wechatConfig.getOpenRedirectUrl() + "," +wechatConfig.getOpenQrcodeUrl();
        return JsonData.buildSuccess(str1);
    }
}
