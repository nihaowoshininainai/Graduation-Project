package com.share.novel.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.share.novel.service.DeepSeekService;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    private final ChatClient chatClient;

    public DeepSeekServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String getRecommend(Map<Integer, List<Integer>> map1, Map<Integer, List<Integer>> map2) {
        String prompt = """
                这是用户喜欢的小说：
                %s,
                这是数据库中所有小说：
                %s，
                根据用户喜欢的小说和数据库中所有小说，推荐给用户6部小说，不够6部就全部推荐出去，输出格式是小说id数组,不要输出其他任何字符。
                """.formatted(map1.toString(), map2.toString());

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public Object getLock(int userId) {
        return DeepSeekService.map.computeIfAbsent(userId, k -> new Object());
    }

}
