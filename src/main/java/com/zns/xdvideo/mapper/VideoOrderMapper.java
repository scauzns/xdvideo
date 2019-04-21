package com.zns.xdvideo.mapper;

import com.zns.xdvideo.domain.VideoOrder;
import com.zns.xdvideo.domain.VideoOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VideoOrderMapper {
    long countByExample(VideoOrderExample example);

    int deleteByExample(VideoOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VideoOrder record);

    int insertSelective(VideoOrder record);

    List<VideoOrder> selectByExample(VideoOrderExample example);

    VideoOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VideoOrder record, @Param("example") VideoOrderExample example);

    int updateByExample(@Param("record") VideoOrder record, @Param("example") VideoOrderExample example);

    int updateByPrimaryKeySelective(VideoOrder record);

    int updateByPrimaryKey(VideoOrder record);
}