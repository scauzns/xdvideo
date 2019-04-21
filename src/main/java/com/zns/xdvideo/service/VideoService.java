package com.zns.xdvideo.service;

import com.zns.xdvideo.domain.Video;

import java.util.List;

/**
 * @功能描述：TODO
 * @创建日期: 2019/4/14 22:52
 */
public interface VideoService {
    List<Video> findAll();
    Video findById(int videoId);
    int update(Video video);
    int delete(int id);
    int save(Video video);
}
