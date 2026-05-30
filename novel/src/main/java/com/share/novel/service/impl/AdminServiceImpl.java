package com.share.novel.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Admin;
import com.share.novel.base.AdminToNovel;
import com.share.novel.mapper.AdminMapper;
import com.share.novel.mapper.AdminToNovelMapper;
import com.share.novel.service.AdminService;
import com.share.novel.utils.Result;
import com.share.novel.utils.TransformUtil;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    AdminToNovelMapper adminToNovelMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.key}")
    String key;

	@Override
	public Result<?> login(String aname, String pwd) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aname", aname);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if(admin != null && BCrypt.checkpw(pwd, admin.getPwd()))
            return Result.success("登陆成功", aname);
		return Result.error("登录名或者密码错误", "");
	}

	@Override
	public Result<?> addAdmin(String aname, String pwd, Admin admin) {

        if(admin.getPwd()==null||admin.getPwd().equals(""))
            return Result.error("密码不可为空", "");

        if(aname.equals("admin")){
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("aname", aname);
            Admin storedAdmin = adminMapper.selectOne(queryWrapper);
            if(storedAdmin != null && BCrypt.checkpw(pwd, storedAdmin.getPwd())){
                QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("aname", admin.getAname());
                boolean flag1 = adminMapper.exists(queryWrapper2);
                if (flag1)
                    return Result.error("重名管理员，添加失败", "");
                admin.setPwd(BCrypt.hashpw(admin.getPwd(), BCrypt.gensalt()));
                int a = adminMapper.insert(admin);
                if(a>0)
                    return Result.success("成功添加管理员", "");
                else
                    return Result.error("添加失败，请重试", "");
            }
            return Result.error("密码错误", "");
        }
        else
            return Result.error("非最高管理员账户，不可添加管理员", "");
	}

	@Override
	public Result<?> delAdmin(String aname,String pwd, Admin admin) {
		if(!aname.equals("admin"))
            return Result.error("非最高管理员账户，不可删除", "");
        if(admin.getPwd()==null||admin.getPwd().equals(""))
            return Result.error("密码不可为空", "");
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aname", aname);
        Admin storedAdmin = adminMapper.selectOne(queryWrapper);
        if(storedAdmin == null || !BCrypt.checkpw(pwd, storedAdmin.getPwd()))
            return Result.error("密码错误", "");
        int d = adminMapper.deleteById(admin);
        if(d>0)
            return Result.success("成功移除管理员", "");
        return Result.error("移除失败，请重试", "");

    }

    @Override
    public java.util.List<AdminToNovel> getShenheRecord() {
        return adminToNovelMapper.selectList(null);
    }

    @Override
    public Result<?> adminLogin(String aname, String pwd) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aname", aname);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if(admin == null || !BCrypt.checkpw(pwd, admin.getPwd()))
            return Result.error("登录名或者密码错误", null);

        Map<String,Object> payload = new HashMap<>();
        admin.setPwd(" ");
        payload.put("admin", admin);
        payload.put("change", System.currentTimeMillis());

        String token = JWTUtil.createToken(payload, key.getBytes());
        stringRedisTemplate.opsForValue().set("admin:token:"+admin.getAid()+":"+token, "true", 1, TimeUnit.DAYS);

        return Result.success("登陆成功", token);
    }

    @Override
    public Result<?> testAdminLogin(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if(cookieHeader == null || cookieHeader.isEmpty())
            return Result.error("未登录，请先登录", null);

        Map<String, String> cookie = TransformUtil.strToMap(cookieHeader, ";", "=");
        String token = cookie.get("AdminToken");

        if(token == null || token.isEmpty())
            return Result.error("非合法token", null);

        boolean flag = JWTUtil.verify(token, key.getBytes());
        if(!flag)
            return Result.error("非合法token", null);

        Object payloadObj = JWTUtil.parseToken(token).getPayload("admin");
        if(payloadObj == null)
            return Result.error("非合法token", null);

        Admin admin = JSONUtil.toBean(payloadObj.toString(), Admin.class);
        String exist = stringRedisTemplate.opsForValue().get("admin:token:" + admin.getAid() + ":" + token);

        if(exist != null)
            return Result.success("Token存在", admin);
        else
            return Result.error("Token已过期，请重新登陆", "");
    }

}
