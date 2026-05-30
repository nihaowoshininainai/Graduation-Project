package com.share.novel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Novel;
import com.share.novel.base.NovelLabel;
import com.share.novel.mapper.NovelLabelMapper;
import com.share.novel.mapper.NovelMapper;
import com.share.novel.service.NovelService;
import com.share.novel.utils.RandomUtil;
import com.share.novel.utils.Result;

@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper,Novel> implements NovelService {

    @Autowired
    NovelMapper novelMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    NovelLabelMapper novelLabelMapper;

    @Value("${show.novels.content.sort.current.size}")
    String sncscs;

    @Value("${show.novels.classify.lableId.sort.current.size}")
    String snclscs;

    @Value("${show.search.name}")
    String ssn;

    Result<?> getNovel(String redisKey,String condition,String sort,Integer current,Integer size,LambdaQueryWrapper<Novel> queryWrapper,String name){

        queryWrapper.in(Novel::getState, Arrays.asList(1,2));
        if(!name.equals(""))
            queryWrapper.and(q -> q.like(Novel::getTitle, name));
        SFunction<Novel, ?> orderBy = null;
        if (condition.equals(Condition.hot.name()))
            orderBy = Novel::getViews;
        else
            orderBy = Novel::getCreateTime;
        if(sort.equals(Sort.desc.name())){
            queryWrapper.orderByDesc(orderBy);
        }
        else
            queryWrapper.orderByAsc(orderBy);
        Page<Novel> page = new Page<>(current,size);
        IPage<Novel> iPage = novelMapper.selectPage(page, queryWrapper);
        Map<String,Object> map = Map.of("pages",iPage.getPages(),"novels",iPage.getRecords());
        Result<?> result = Result.success("查找成功", map);
        stringRedisTemplate.opsForValue().set(redisKey, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);

        return result;
    }

    Result<?> getNovel(String redisKey,String condition,String sort,Integer current,Integer size,LambdaQueryWrapper<Novel> queryWrapper){
        return getNovel(redisKey, condition, sort, current, size, queryWrapper, "");
    }

    @Override
    public Result<?> add(Novel novel) {
        int a = novelMapper.insert(novel);
        if(a>0){
            return Result.success("添加成功", novel.getId());
        }
        return Result.error("添加失败，请重试", "");
    }

    @Override
    public Result<?> del(Novel novel) {
        int a = novelMapper.deleteById(novel);
        if(a>0)
            return Result.success("删除成功", "");
        return Result.error("删除失败，请重试", "");
    }

    @Override
    public Result<?> index(String condition,String sort,Integer current,Integer size) {

        String redisKey = sncscs+condition+":"+sort+":"+current+":"+size;
        String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);

        if(cachedJson != null){
            return Result.fromJson(cachedJson);
        }

        LambdaQueryWrapper<Novel> queryWrapper = new LambdaQueryWrapper<>();
        return getNovel(redisKey, condition, sort, current, size, queryWrapper);
    }

    @Override
    public Result<?> classify(String condition,String sort,Integer current,Integer size,Integer labelId) {
        String redisKey = snclscs+condition+":"+sort+":"+current+":"+size+":"+labelId;
        String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);

        if(cachedJson != null){
            return Result.fromJson(cachedJson);
        }

        LambdaQueryWrapper<NovelLabel> novelLabelQueryWrapper = new LambdaQueryWrapper<>();
        novelLabelQueryWrapper.eq(NovelLabel::getLid, labelId);
        List<NovelLabel> list = novelLabelMapper.selectList(novelLabelQueryWrapper);

        if(list.isEmpty())
            return Result.error("该标签下没有小说", "");

        LambdaQueryWrapper<Novel> novelQueryWrapper = new LambdaQueryWrapper<>();
        List<Integer> nids = new ArrayList<>();
        for (NovelLabel novelLabel : list) {
            nids.add(novelLabel.getNid());
        }
        novelQueryWrapper.in(Novel::getId, nids);

        return getNovel(redisKey, condition, sort, current, size, novelQueryWrapper);
    }

    @Override
    public Result<?> search(String condition,String sort,Integer current,Integer size,String keyword) {
        String redisKey = ssn+condition+":"+sort+":"+current+":"+size+":"+keyword;
        String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);

        if(cachedJson != null){
            return Result.fromJson(cachedJson);
        }

        LambdaQueryWrapper<Novel> queryWrapper = new LambdaQueryWrapper<>();
        return getNovel(redisKey, condition, sort, current, size, queryWrapper, keyword);
    }
}
