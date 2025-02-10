package com.share.novel.service.impl;

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

    //获取小说通用方法
    String getNovel(String key,String condition,String sort,Integer current,Integer size,LambdaQueryWrapper<Novel> queryWrapper){
        queryWrapper.eq(Novel::getState, 1);
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
        String result = Result.success("查找成功", map);
        stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        
        return result;
    }

    @Override
    public String add(Novel novel) {
        int a = novelMapper.insert(novel);
        if(a>0)
            return Result.success("添加成功", "");
        return Result.error("添加失败，请重试", "");
    }

    @Override
    public String del(Novel novel) {
        int a = novelMapper.deleteById(novel);
        if(a>0)
            return Result.success("删除成功", "");
        return Result.error("删除失败，请重试", "");
    }

    @Override
    public String index(String condition,String sort,Integer current,Integer size) {

        //先在redis中查询
        String key = sncscs+condition+":"+sort+":"+current+":"+size;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result==null){
            LambdaQueryWrapper<Novel> queryWrapper = new LambdaQueryWrapper<>();
            result = getNovel(key, condition, sort, current, size, queryWrapper);
        }   
        

        return result;
    }

    @Override
    public String classify(String condition,String sort,Integer current,Integer size,Integer labelId) {
        //先在redis中查询
        String key = snclscs+condition+":"+sort+":"+current+":"+size+":"+labelId;
        String result = stringRedisTemplate.opsForValue().get(key);

        if(result==null){
            LambdaQueryWrapper<NovelLabel> novelLabelQueryWrapper = new LambdaQueryWrapper<>();
            novelLabelQueryWrapper.eq(NovelLabel::getLid, labelId);
            List<NovelLabel> list = novelLabelMapper.selectList(novelLabelQueryWrapper);

            if(list.isEmpty())
                return Result.error("该标签下没有小说", "");

            LambdaQueryWrapper<Novel> novelQueryWrapper = new LambdaQueryWrapper<>();
            for (NovelLabel novelLabel : list) {
                novelQueryWrapper.eq(Novel::getId, novelLabel.getNid());
            }

            result = getNovel(key, condition, sort, current, size, novelQueryWrapper);
        }   
        

        return result;
    }
}
