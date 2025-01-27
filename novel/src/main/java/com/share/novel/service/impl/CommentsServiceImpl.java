package com.share.novel.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Comments;
import com.share.novel.mapper.CommentsMapper;
import com.share.novel.service.CommentsService;

@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper,Comments> implements CommentsService {

}
