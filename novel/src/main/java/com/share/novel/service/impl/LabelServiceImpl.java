package com.share.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Label;
import com.share.novel.mapper.LabelMapper;
import com.share.novel.service.LabelService;
import com.share.novel.utils.Result;

@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper,Label> implements LabelService {

    @Autowired
    LabelMapper labelMapper;

    @Override
    public Result<?> addLabel(String name) {
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        boolean flag = labelMapper.exists(queryWrapper);
        if(flag)
            return Result.error("已存在该标签，请勿重复添加", "");
        Label label = new Label();
        label.setName(name);
        int a = labelMapper.insert(label);
        if(a>0)
            return Result.success("成功添加标签", "");
        return Result.error("添加失败，请重试", "");
    }

    @Override
    public Result<?> delLabel(Integer id) {
        int d = labelMapper.deleteById(id);
        if(d>0)
            return Result.success("成功删除", "");
        return Result.error("删除失败，请重试", "");
    }

}
