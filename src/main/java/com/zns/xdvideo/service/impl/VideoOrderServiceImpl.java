package com.zns.xdvideo.service.impl;

import com.zns.xdvideo.config.WechatConfig;
import com.zns.xdvideo.domain.User;
import com.zns.xdvideo.domain.Video;
import com.zns.xdvideo.domain.VideoOrder;
import com.zns.xdvideo.domain.VideoOrderExample;
import com.zns.xdvideo.dto.VideoOrderDto;
import com.zns.xdvideo.mapper.UserMapper;
import com.zns.xdvideo.mapper.VideoMapper;
import com.zns.xdvideo.mapper.VideoOrderMapper;
import com.zns.xdvideo.service.VideoOrderService;
import com.zns.xdvideo.utils.CommonUtils;
import com.zns.xdvideo.utils.HttpUtils;
import com.zns.xdvideo.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @功能描述：TODO
 * @创建日期: 2019/4/17 16:05
 */
@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoOrderMapper videoOrderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private WechatConfig weChatConfig;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception {
        //查找视频信息
        Video video = videoMapper.selectByPrimaryKey(videoOrderDto.getVideoId());
        //查找用户信息
        User user = userMapper.selectByPrimaryKey(videoOrderDto.getUserId());
        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg("xxxx");
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());  //使用UUID生成订单流水号
        videoOrderMapper.insert(videoOrder);

        //统一下单，获取code_url
        String codeUrl = unifiedOrder(videoOrder);
        return codeUrl;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {
        VideoOrderExample example = new VideoOrderExample();
        example.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        List<VideoOrder> videoOrders = videoOrderMapper.selectByExample(example);
        if(videoOrders != null && videoOrders.size()>0){
            return videoOrders.get(0);
        }
        return null;
    }

    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        VideoOrderExample example = new VideoOrderExample();
        example.createCriteria().andOutTradeNoEqualTo(videoOrder.getOutTradeNo())
                                .andStateEqualTo(0).andDelEqualTo(0); //还没支付，并且未删除的订单
        return videoOrderMapper.updateByExampleSelective(videoOrder, example);
    }

    /**
     * 统一下单方法
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //int i = 1/0;   //模拟异常
        //生成签名
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppid());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str",CommonUtils.generateUUID());
        params.put("body",videoOrder.getVideoTitle());
        params.put("out_trade_no",videoOrder.getOutTradeNo());
        params.put("total_fee",videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip",videoOrder.getIp());
        params.put("notify_url",weChatConfig.getPayCallbackUrl());
        params.put("trade_type","NATIVE");

        //sign签名,传送的sign参数不参与签名，将生成的签名与该sign值作校验
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign",sign);

        //map转xml
        String payXml = WXPayUtil.mapToXml(params);

        System.out.println(payXml);
        //统一下单
        String orderStr = HttpUtils.doPost(WechatConfig.getUnifiedOrderUrl(),payXml,4000);
        if(null == orderStr) {
            return null;
        }

        Map<String, String> unifiedOrderMap =  WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if(unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }

        return null;
    }
}
