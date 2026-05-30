package com.share.novel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Admin;
import com.share.novel.base.Collection;
import com.share.novel.base.Comments;
import com.share.novel.base.Novel;
import com.share.novel.base.NovelLabel;
import com.share.novel.base.User;
import com.share.novel.service.AdminService;
import com.share.novel.service.CollectionService;
import com.share.novel.service.CommentsService;
import com.share.novel.service.DeepSeekService;
import com.share.novel.service.LabelService;
import com.share.novel.service.NovelLabelService;
import com.share.novel.service.NovelService;
import com.share.novel.service.UserService;
import com.share.novel.utils.RandomUtil;
import com.share.novel.utils.RedisUtil;
import com.share.novel.utils.Result;

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
    RedisUtil redisUtil;

    @Autowired
    DeepSeekService deepseek;

    @Autowired
    AdminService adminService;

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

    @GetMapping("/index")
    Result<?> show(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size) {

        return novelService.index(condition, sort, current, size);
    }

    @GetMapping("/classify")
    Result<?> classify(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size,
            @RequestParam("labelId") Integer labelId) {

        return novelService.classify(condition, sort, current, size, labelId);
    }

    @GetMapping("/search")
    Result<?> search(@RequestParam(required = false) String condition, @RequestParam(required = false) String sort,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size,
            @RequestParam("name") String name) {
        return novelService.search(condition, sort, current, size, name);
    }

    @GetMapping("/showUname")
    Result<?> showUname(@RequestParam("id") Integer id) {

        return userService.getUname(id);
    }

    @GetMapping("/labels")
    public Result<?> getLabel() {
        String cachedJson = stringRedisTemplate.opsForValue().get(la);

        if (cachedJson == null) {
            Result<?> result = Result.success("查询成功", labelService.list());
            stringRedisTemplate.opsForValue().set(la, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
            return result;
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showUid")
    Result<?> showUid(@RequestParam("uname") String uname) {
        return userService.getUid(uname);
    }

    @GetMapping("/showUser")
    Result<?> showUser(@RequestParam("uid") Integer uid) {
        String cachedJson = stringRedisTemplate.opsForValue().get(ui + uid);

        if (cachedJson == null) {
            User user = userService.getById(uid);
            user.setPwd("");
            Result<?> result = Result.success("查询成功", user);
            stringRedisTemplate.opsForValue().set(ui + uid, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
            return result;
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showNovels")
    Result<?> showNovels(@RequestParam("uid") Integer uid) {
        String cachedJson = stringRedisTemplate.opsForValue().get(snu + uid);

        if (cachedJson == null) {
            LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Novel::getUid, uid);
            Result<?> result = Result.success("查询成功", novelService.list(wrapper));
            stringRedisTemplate.opsForValue().set(snu + uid, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
            return result;
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showCollections")
    Result<?> showNovelsLike(@RequestParam("uid") Integer uid) {
        String key = snlu + uid;
        String cachedJson = stringRedisTemplate.opsForValue().get(key);

        if (cachedJson == null) {
            List<Collection> collections = collectionService.getCollectionListByUserId(uid);
            if (collections.isEmpty()) {
                Result<?> result = Result.success("查询成功", "");
                stringRedisTemplate.opsForValue().set(key, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
                return result;
            } else {
                List<Integer> ids = new ArrayList<>();
                for (Collection collection : collections) {
                    ids.add(collection.getNid());
                }
                LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(Novel::getId, ids);
                Result<?> result = Result.success("查询成功", novelService.list(wrapper));
                stringRedisTemplate.opsForValue().set(key, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
                return result;
            }
        }

        return Result.fromJson(cachedJson);

    }

    @GetMapping("/showNovel")
    Result<?> showNovel(@RequestParam("nid") Integer nid) {
        String cachedJson = stringRedisTemplate.opsForValue().get(snn + nid);

        if (cachedJson == null) {
            Novel novel = novelService.getById(nid);
            Result<?> result = Result.success("查询成功", novel);
            stringRedisTemplate.opsForValue().set(snn + nid, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
            return result;
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showComments")
    Result<?> showComments(@RequestParam("nid") Integer nid) {
        String key = scn + nid;
        String cachedJson = stringRedisTemplate.opsForValue().get(key);

        if (cachedJson == null) {
            LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comments::getNid, nid).and(w -> w.eq(Comments::getState, 0).or().eq(Comments::getState, 1));

            Result<?> result = Result.success("查询成功", commentsService.list(wrapper));
            stringRedisTemplate.opsForValue().set(key, result.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
            return result;
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showLike")
    Result<?> showLike(@RequestParam("nid") Integer nid, @RequestParam("uid") Integer uid) {
        String key = snl + uid;
        String cachedJson = stringRedisTemplate.opsForValue().get(key);
        Result<?> cachedResult;

        if (cachedJson == null) {
            LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Collection::getUid, uid);
            List<Collection> collections = collectionService.list(wrapper);
            List<Integer> ids = new ArrayList<>();
            for (Collection collection : collections) {
                ids.add(collection.getNid());
            }

            cachedResult = Result.success("查询成功", ids);
            stringRedisTemplate.opsForValue().set(key, cachedResult.toJson(), RandomUtil.getRandom(), TimeUnit.HOURS);
        } else {
            cachedResult = Result.fromJson(cachedJson);
        }

        List<Integer> ids = JSONUtil.toList(JSONUtil.toJsonStr(cachedResult.getData()), Integer.class);

        return Result.success("查询成功", ids.contains(nid));

    }

    @GetMapping("/showRecommend")
    Result<?> showRecommend(@RequestParam("uid") Integer uid) {
        String key = sruu + uid;
        String cachedJson = stringRedisTemplate.opsForValue().get(key);

        synchronized (deepseek.getLock(uid)) {
            cachedJson = stringRedisTemplate.opsForValue().get(key);
            if (cachedJson == null) {
                LambdaQueryWrapper<Collection> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Collection::getUid, uid);

                List<Collection> collections = collectionService.list(wrapper);

                LambdaQueryWrapper<Novel> novelWrapper = new LambdaQueryWrapper<>();
                novelWrapper.eq(Novel::getState, 1).or().eq(Novel::getState, 2);
                List<Novel> allNovles = novelService.list(novelWrapper);

                Map<Integer, List<Integer>> map1 = new HashMap<>();
                Map<Integer, List<Integer>> map2 = new HashMap<>();

                if (!collections.isEmpty()) {
                    List<Integer> collectionNids = collections.stream().map(Collection::getNid).toList();
                    LambdaQueryWrapper<NovelLabel> nlWrapper1 = new LambdaQueryWrapper<>();
                    nlWrapper1.in(NovelLabel::getNid, collectionNids);
                    List<NovelLabel> allNovelLabels1 = novelLabelService.list(nlWrapper1);
                    map1 = allNovelLabels1.stream()
                            .collect(Collectors.groupingBy(
                                    NovelLabel::getNid,
                                    Collectors.mapping(NovelLabel::getLid, Collectors.toList())));
                    for (Integer nid : collectionNids) {
                        map1.putIfAbsent(nid, new ArrayList<>());
                    }
                }

                if (!allNovles.isEmpty()) {
                    List<Integer> novelIds = allNovles.stream().map(Novel::getId).toList();
                    LambdaQueryWrapper<NovelLabel> nlWrapper2 = new LambdaQueryWrapper<>();
                    nlWrapper2.in(NovelLabel::getNid, novelIds);
                    List<NovelLabel> allNovelLabels2 = novelLabelService.list(nlWrapper2);
                    map2 = allNovelLabels2.stream()
                            .collect(Collectors.groupingBy(
                                    NovelLabel::getNid,
                                    Collectors.mapping(NovelLabel::getLid, Collectors.toList())));
                    for (Integer nid : novelIds) {
                        map2.putIfAbsent(nid, new ArrayList<>());
                    }
                }
                if (map1.isEmpty()) {
                    return Result.error("请先收藏小说", "");
                }
                Result<?> result = Result.success("查询成功",
                        novelService.listByIds(JSONUtil.toList(deepseek.getRecommend(map1, map2), Integer.class)));
                stringRedisTemplate.opsForValue().set(key, result.toJson(), 1, TimeUnit.DAYS);
                return result;
            }
        }

        return Result.fromJson(cachedJson);
    }

    @GetMapping("/showUserComments")
    Result<?> showUserComments(@RequestParam("uid") Integer uid) {

        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comments::getUid, uid);

        return Result.success("查询成功", commentsService.list(wrapper));
    }

    @GetMapping("/getJvbao")
    Result<?> showNovelJvbao() {
        LambdaQueryWrapper<Novel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Novel::getState, 2);
        return Result.success("查询成功", novelService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getShenhe")
    Result<?> showNovleShenhe() {
        LambdaQueryWrapper<Novel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Novel::getState, 0);
        return Result.success("查询成功", novelService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getBanUsers")
    Result<?> showBanUsers() {
        Set<String> keys = redisUtil.scanKeys(swuu.concat("*"));
        Set<Integer> uids = new HashSet<>();
        if (keys.isEmpty())
            return Result.success("查询成功", "");
        else {
            for (String k : keys) {
                if (Integer.parseInt(stringRedisTemplate.opsForValue().get(k)) >= 3)
                    uids.add(Integer.parseInt(k.substring(swuu.length())));
            }
        }

        return Result.success("查询成功", userService.listByIds(uids));
    }

    @GetMapping("/getBannedUsers")
    Result<?> showBannedUsers() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getBanned, 1);
        return Result.success("查询成功", userService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getJvbaoComments")
    Result<?> getJvbaoComments() {
        LambdaQueryWrapper<Comments> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comments::getState, 1);

        return Result.success("查询成功", commentsService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getAdmins")
    Result<?> getAdmins(@RequestParam String aname) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ne(Admin::getAname, "admin");
        if (!aname.equals("admin"))
            return Result.error("非最高管理员，无法查看其他管理员信息", "");
        else
            return Result.success("查询成功", adminService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getAllUsers")
    Result<?> getAllUsers() {
        return Result.success("查询成功", userService.list());
    }

    @GetMapping("/getAllNovels")
    Result<?> getAllNovels() {
        return Result.success("查询成功", novelService.list());
    }

    @GetMapping("/getShenheRecord")
    Result<?> getShenheRecord() {
        return Result.success("查询成功", adminService.getShenheRecord());
    }
}
