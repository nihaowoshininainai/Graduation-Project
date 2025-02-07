package com.share.novel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.service.NovelService;

@RestController
@RequestMapping("/api")
public class ShowController {

    @Autowired
    NovelService novelService;

    //首页小说请求
    @GetMapping("/index")
    String show(@RequestParam(required = false) String condition,@RequestParam(required = false) String sort,@RequestParam("current") Integer current,@RequestParam("size") Integer size){
        
        return novelService.index(condition, sort, current, size);
    }
}
