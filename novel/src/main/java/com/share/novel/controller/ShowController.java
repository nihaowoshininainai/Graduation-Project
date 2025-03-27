package com.share.novel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Collection;
import com.share.novel.base.Comments;
import com.share.novel.base.Novel;
import com.share.novel.base.NovelLabel;
import com.share.novel.base.User;
import com.share.novel.service.CollectionService;
import com.share.novel.service.CommentsService;
import com.share.novel.service.DeepSeekService;
import com.share.novel.service.LabelService;
import com.share.novel.service.NovelLabelService;
import com.share.novel.service.NovelService;
import com.share.novel.service.UserService;
import com.share.novel.utils.RandomUtil;
import com.share.novel.utils.Result;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RestController
@RequestMapping("/api")
public class ShowController {

    @Autowired
    NovelService novelService;

    @Autowired
    NovelLabelService novelLabelService;

    @Autowired
    UserService userService;

    @Autowired
    LabelService labelService;

    @Autowired
    CollectionService collectionService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DeepSeekService deepseek;

    @Value("${label.all}")
    String la;

    @Value("${user.id}")
    String ui;

    @Value("${show.novels.uid}")
    String snu;

    @Value("${show.novels.like.uid}")
    String snlu;

    @Value("${show.novel.nid}")
    String snn;

    @Value("${show.comments.nid}")
    String scn;

    @Value("${show.novel.like.uid}")
    String snl;

    @Value("${show.weigui.user.uid}")
    String swuu;

    @Value("${show.recommend.user.uid}")
    String sruu;

    // 首页小说请求
    @GetMapping("/index")
    String show(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size) {

        return novelService.index(condition, sort, current, size);
    }

    // 分类小说请求
    @GetMapping("/classify")
    String classify(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size,
            @RequestParam("labelId") Integer labelId) {

        return novelService.classify(condition, sort, current, size, labelId);
    }

    // 搜索小说
    @GetMapping("/search")
    String search(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size,
            @RequestParam("name") String name) {
        return novelService.search(condition, sort, current, size, name);
    }

    // 根据id查询用户名
    @GetMapping("/showUname")
    String showUname(@RequestParam("id") Integer id) {

        return userService.getUname(id);
    }

    // 获取标签
    @GetMapping("/labels")
    public String getLabel() {
        // 查询redis中是否存有缓存
        String result = stringRedisTemplate.opsForValue().get(la);

        if (result == null) {
            result = Result.success("查询成功", labelService.list());
            stringRedisTemplate.opsForValue().set(la, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    // 根据用户名查询用户id
    @GetMapping("/showUid")
    String showUid(@RequestParam("uname") String uname) {
        return userService.getUid(uname);
    }

    // 根据用户id查询用户信息
    @GetMapping("/showUser")
    String showUser(@RequestParam("uid") Integer uid) {
        // 查询redis中是否存有缓存
        String result = stringRedisTemplate.opsForValue().get(ui + uid);

        if (result == null) {
            User user = userService.getById(uid);
            user.setPwd("");
            result = Result.success("查询成功", user);
            stringRedisTemplate.opsForValue().set(ui + uid, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    // 根据用户id查询上传的小说
    @GetMapping("/showNovels")
    String showNovels(@RequestParam("uid") Integer uid) {
        // 查询redis中是否存有缓存
        String result = stringRedisTemplate.opsForValue().get(snu + uid);

        if (result == null) {
            LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Novel::getUid, uid);
            result = Result.success("查询成功", novelService.list(wrapper));
            stringRedisTemplate.opsForValue().set(snu + uid, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    // 获取用户收藏
    @GetMapping("/showCollections")
    String showNovelsLike(@RequestParam("uid") Integer uid) {
        // 查询redis中是否存有缓存
        String key = snlu + uid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if (result == null) {
            List<Collection> collections = collectionService.getCollectionListByUserId(uid);
            if (collections.isEmpty()) {
                result = Result.success("查询成功", "");
                stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
            } else {
                List<Integer> ids = new ArrayList<>();
                for (Collection collection : collections) {
                    ids.add(collection.getNid());
                }
                LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(Novel::getId, ids);
                result = Result.success("查询成功", novelService.list(wrapper));
            }
        }

        return result;

    }

    // 获取小说信息
    @GetMapping("/showNovel")
    String showNovel(@RequestParam("nid") Integer nid) {
        // 查询redis中是否存有缓存
        String result = stringRedisTemplate.opsForValue().get(snn + nid);

        if (result == null) {
            Novel novel = novelService.getById(nid);
            result = Result.success("查询成功", novel);
            stringRedisTemplate.opsForValue().set(snn + nid, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    // 获取小说评论
    @GetMapping("/showComments")
    String showComments(@RequestParam("nid") Integer nid) {
        // 查询redis中是否存有缓存
        String key = scn + nid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if (result == null) {
            LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comments::getNid, nid).and(w -> w.eq(Comments::getState, 0).or().eq(Comments::getState, 1));

            result = Result.success("查询成功", commentsService.list(wrapper));
            stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        return result;
    }

    // 获取小说点赞
    @GetMapping("/showLike")
    String showLike(@RequestParam("nid") Integer nid, @RequestParam("uid") Integer uid) {
        // 查询redis中是否存有缓存
        String key = snl + uid;
        String result = stringRedisTemplate.opsForValue().get(key);

        if (result == null) {
            LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Collection::getUid, uid);
            List<Collection> collections = collectionService.list(wrapper);
            List<Integer> ids = new ArrayList<>();
            for (Collection collection : collections) {
                ids.add(collection.getNid());
            }

            result = Result.success("查询成功", ids);
            stringRedisTemplate.opsForValue().set(key, result, RandomUtil.getRandom(), TimeUnit.HOURS);
        }

        JSONObject resultObject = JSONUtil.parseObj(result);
        List<Integer> ids = resultObject.getJSONArray("data").toList(Integer.class);

        return Result.success("查询成功", ids.contains(nid));

    }

    // 获取用户推荐
    @GetMapping("/showRecommend")
    String showRecommend(@RequestParam("uid") Integer uid) {
        // 查询redis中是否存有缓存
        String key = sruu + uid;
        String result = stringRedisTemplate.opsForValue().get(key);

        synchronized (deepseek.getLock(uid)) {
            result = stringRedisTemplate.opsForValue().get(key);
            if (result == null) {
                LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Collection::getUid, uid);

                List<Collection> collections = collectionService.list(wrapper);

                LambdaQueryWrapper<Novel> novelWrapper = new LambdaQueryWrapper<>();
                novelWrapper.eq(Novel::getState, 1).or().eq(Novel::getState, 2);
                List<Novel> allNovles = novelService.list(novelWrapper);

                Map<Integer, List<Integer>> map1 = new HashMap<>();
                Map<Integer, List<Integer>> map2 = new HashMap<>();

                for (Collection collection : collections) {
                    LambdaQueryWrapper<NovelLabel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(NovelLabel::getNid, collection.getNid());
                    List<NovelLabel> novelLabels = novelLabelService.list(lambdaQueryWrapper);
                    List<Integer> list = new ArrayList<>();
                    for (NovelLabel novelLabel : novelLabels) {
                        list.add(novelLabel.getLid());
                    }
                    map1.put(collection.getNid(), list);
                }

                for (Novel novel : allNovles) {
                    LambdaQueryWrapper<NovelLabel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(NovelLabel::getNid, novel.getId());
                    List<NovelLabel> novelLabels = novelLabelService.list(lambdaQueryWrapper);
                    List<Integer> list = new ArrayList<>();
                    for (NovelLabel novelLabel : novelLabels) {
                        list.add(novelLabel.getLid());
                    }
                    map2.put(novel.getId(), list);
                }
                if (map1.isEmpty()) {
                    return Result.error("请先收藏小说", "");
                }
                result = Result.success("查询成功",
                        novelService.listByIds(JSONUtil.toList(deepseek.getRecommend(map1, map2), Integer.class)));
                stringRedisTemplate.opsForValue().set(key, result, 1, TimeUnit.DAYS);
            }
        }

        return result;
    }

    /*
     * 以下为管理端数据展示
     */
    // 获取被举报的文章信息
    @GetMapping("/getJvbao")
    String showNovelJvbao() {
        LambdaQueryWrapper<Novel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Novel::getState, 2);
        return Result.success("查询成功", novelService.list(lambdaQueryWrapper));
    }

    // 获取待审核的文章信息
    @GetMapping("/getShenhe")
    String showNovleShenhe() {
        LambdaQueryWrapper<Novel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Novel::getState, 0);
        return Result.success("查询成功", novelService.list(lambdaQueryWrapper));
    }

    // 获取违规超过三次的用户信息
    @GetMapping("/getUsers")
    String showUsers() {
        Set<String> keys = stringRedisTemplate.keys(swuu.concat("*"));
        Set<Integer> uids = new HashSet<>();
        if (keys.isEmpty())
            return Result.success("查询成功", "");
        else {
            for (String key : keys) {
                if (Integer.parseInt(stringRedisTemplate.opsForValue().get(key)) >= 3)
                    uids.add(Integer.parseInt(key.substring(swuu.length())));
            }
        }

        return Result.success("查询成功", userService.listByIds(uids));
    }

    // 获取被举报评论
    @GetMapping("getJvbaoComments")
    String getJvbaoComments() {
        LambdaQueryWrapper<Comments> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comments::getState, 1);

        return Result.success("查询成功", commentsService.list(lambdaQueryWrapper));
    }

}