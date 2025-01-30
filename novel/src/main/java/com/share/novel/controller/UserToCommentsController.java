package com.share.novel.controller;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.Comments;
import com.share.novel.service.CommentsService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserToCommentsController {

    @Autowired
    CommentsService commentsService;

    @Autowired
    ControllerUtil controllerUtil;

    //用户举报评论
    public String reportComments(HttpServletRequest httpServletRequest,Comments comments){
        Function<Integer,String> function = uid -> {
            comments.setState(1);
            boolean flag = commentsService.updateById(comments);
            if(flag)
                return Result.success("举报成功", "");
            
            return Result.error("举报失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
