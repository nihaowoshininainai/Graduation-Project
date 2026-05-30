package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Label;
import com.share.novel.utils.Result;

public interface LabelService extends IService<Label> {

    Result<?> addLabel(String name);

    Result<?> delLabel(Integer id);
}
