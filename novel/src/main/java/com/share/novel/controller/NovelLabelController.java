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
    ControllerUtil controllerUtil;

    @Value("${novellabel.nid.lid}")
    String nnl;

    //添加标签
    @PostMapping("/addLabel")
    public String add(HttpServletRequest httpServletRequest,@RequestBody List<NovelLabel> novelLabels){
        Function<Integer,String> function = uid -> {
            boolean flag = novelLabelService.saveBatch(novelLabels);
            int nid = novelLabels.get(0).getNid();
            if(flag){

                //删除小说标签缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(nnl+nid+"*"));

                return Result.success("添加标签成功", "");
            }
            else
                return Result.error("添加失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }

    //删除标签
    @PostMapping("/delLabel")
    public String del(HttpServletRequest httpServletRequest,@RequestBody List<NovelLabel> novelLabels){
        Function<Integer,String> function = uid -> {
            boolean flag = novelLabelService.removeBatchByIds(novelLabels);
            int nid = novelLabels.get(0).getNid();
            if(flag){

                //删除小说标签缓存
                stringRedisTemplate.delete(stringRedisTemplate.keys(nnl+nid+"*"));

                return Result.success("移除标签成功", "");
            }
            else
                return Result.error("移除失败，请重试", "");
        };
        Integer uid = null;

        return controllerUtil.sameFunc(httpServletRequest, uid, function);
    }
}
