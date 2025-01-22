package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Novel;
import com.share.novel.service.NovelService;
import com.share.novel.utils.ControllerUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserToNovelController {

    @Autowired
    ControllerUtil controllerUtil;

    @Autowired
    NovelService novelService;

    @PostMapping("/addNovel")
    public String addNovel(HttpServletRequest httpServletRequest,@RequestBody Novel novel){
        Function<Integer,String> function = uid -> {
            novel.setUid(uid);
            novel.setCreateTime();
            String result = novelService.add(novel);

            return result;
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
