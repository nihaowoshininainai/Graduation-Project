package com.share.novel.service;

import java.util.List;
import java.util.Map;

public interface DeepSeekService {

    /**
     *根据用户喜欢获取用户推荐，小说id为键，标签id数组为值
     * @param map1 用户喜欢
     * @param map2 数据库所有小说
     * @return 推荐的小说id数组
     */
    public String getRecommend(Map<Integer,List<Integer>> map1, Map<Integer,List<Integer>> map2);
}
