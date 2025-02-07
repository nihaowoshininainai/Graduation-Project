package com.share.novel.utils;

public class RandomUtil {
    //为存入redis中的缓存设定不一样的时间
    public static long getRandom(){
        return (long)(Math.random()*24+1);
    }
}
