package com.share.novel.service.impl;

import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service
public class DeepSeekService {
    public String chat(String words){
        HttpResponse<String> response = Unirest.post("https://api.deepseek.com/chat/completions")
          .header("Content-Type", "application/json")
          .header("Accept", "application/json")
          .header("Authorization", "Bearer sk-4a213a952d8c4f5990a2e12edc4cb942")
          .body("{\n  \"messages\": [\n    {\n      \"content\": \"You are a helpful assistant\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \""+words+"\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \"deepseek-chat\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}")
          .asString();

          return response.getBody();
        
}}

