package com.share.novel.controller;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.share.novel.base.NovelLabel;
import com.share.novel.service.NovelLabelService;
import com.share.novel.utils.ControllerUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class NovelLabelController {

    @Autowired
    NovelLabelService novelLabelService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ControllerUtil controllerUtil;

    @Value("${novellabel.nid.lid}")
    String nnl;

    @PostMapping("/addLabel")
    public Result<?> add(HttpServletRequest httpServletRequest,@RequestBody List<NovelLabel> novelLabels){
        Function<Integer,Result<?>> function = uid -> {
            boolean flag = novelLabelService.saveBatch(novelLabels);
            int nid = novelLabels.get(0).getNid();
            if(flag){
                redisUtil.deleteByPattern(nnl+nid+"*");
                return Result.success("添加标签成功", "");
            }
            else
                return Result.error("添加失败，请重试", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }

    @PostMapping("/delLabel")
    public Result<?> del(HttpServletRequest httpServletRequest,@RequestBody List<NovelLabel> novelLabels){
        Function<Integer,Result<?>> function = uid -> {
            boolean flag = novelLabelService.removeBatchByIds(novelLabels);
            int nid = novelLabels.get(0).getNid();
            if(flag){
                redisUtil.deleteByPattern(nnl+nid+"*");
                return Result.success("移除标签成功", "");
            }
            else
                return Result.error("移除失败，请重试", "");
        };

        return controllerUtil.sameFunc(httpServletRequest, function);
    }
}
