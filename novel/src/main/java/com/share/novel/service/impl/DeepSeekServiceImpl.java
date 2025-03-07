package com.share.novel.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.share.novel.service.DeepSeekService;


@Service
public class DeepSeekServiceImpl implements DeepSeekService {
    public static final String WRITE_ME_HAIKU_ABOUT_CAT = """
        Write me Haiku about cat,
        haiku should start with the word cat obligatory""";

    private final AiClient aiClient;

    public DeepSeekServiceImpl(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    // 构造函数

    @Override
    public String getRecommend(Map<Integer,List<Integer>> map1, Map<Integer,List<Integer>> map2) {
        String a = """
                这是用户喜欢的小说：
                {map1},
                这是数据库中所有小说：
                {map2}，
                根据用户喜欢的小说和数据库中所有小说，推荐给用户2部小说，输出格式是小说id数组,不要输出其他任何字符。
                """;
        PromptTemplate promptTemplate = new PromptTemplate(a);
        promptTemplate.add("map1", map1.toString());
        promptTemplate.add("map2", map2.toString());

        return aiClient.generate(promptTemplate.create().getContents());
    }
}
