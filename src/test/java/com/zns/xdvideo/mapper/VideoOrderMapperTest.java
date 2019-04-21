package com.zns.xdvideo.mapper;

import com.zns.xdvideo.domain.VideoOrder;
import com.zns.xdvideo.domain.VideoOrderExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoOrderMapperTest {

    @Autowired
    private VideoOrderMapper videoOrderMapper;
    @Test
    public void updateByExampleSelective() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setState(1);
        VideoOrderExample example = new VideoOrderExample();
        example.createCriteria().andOutTradeNoEqualTo("799335376366450a8dd246df580cb764")
                .andStateEqualTo(0).andDelEqualTo(0).andIdEqualTo(13); //还没支付，并且未删除的订单
        int rows = videoOrderMapper.updateByExampleSelective(videoOrder, example);
        System.out.println(rows);
    }
}