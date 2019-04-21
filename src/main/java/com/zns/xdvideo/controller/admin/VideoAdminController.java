package com.zns.xdvideo.controller.admin;

import com.zns.xdvideo.domain.Video;
import com.zns.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @功能描述：TODO
 * @创建日期: 2019/4/15 11:09
 */
@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @DeleteMapping("del_by_id")
    public Object delById(@RequestParam(value = "video_id")int videoId){

        return videoService.delete(videoId);
    }


    @PutMapping("update_by_id")
    public Object update(@RequestBody Video video){
        return videoService.update(video);
    }




    @PostMapping("save")
    public Object save(@RequestBody Video video){
        return videoService.save(video);
    }
}
