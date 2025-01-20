package com.share.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.share.novel.base.Label;

public interface LabelService extends IService<Label> {

    //添加标签
    String addLabel(String name);

    //删除标签
    String delLabel(Integer id);
}
