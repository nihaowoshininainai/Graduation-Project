package com.share.novel.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        stringRedisTemplate.execute((RedisCallback<Void>) connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build();
            try (var cursor = connection.keyCommands().scan(scanOptions)) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
            }
            return null;
        });
        return keys;
    }

    public void deleteByPattern(String pattern) {
        Set<String> keys = scanKeys(pattern);
        if (!keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }
}
