package com.zns.xdvideo.service.impl;

import com.zns.xdvideo.domain.Video;
import com.zns.xdvideo.mapper.VideoMapper;
import com.zns.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @功能描述：TODO
 * @创建日期: 2019/4/14 22:52
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> findAll() {
        return videoMapper.selectByExample(null);
    }

    @Override
    public Video findById(int videoId) {
        return videoMapper.selectByPrimaryKey(videoId);
    }

    @Override
    public int update(Video video) {
        return videoMapper.updateByPrimaryKeySelective(video);
    }

    @Override
    public int delete(int id) {
        return videoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int save(Video video) {
        return videoMapper.insertSelective(video);
    }
}
