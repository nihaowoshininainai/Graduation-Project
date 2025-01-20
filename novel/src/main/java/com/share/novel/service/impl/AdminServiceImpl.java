package com.share.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.share.novel.base.Admin;
import com.share.novel.mapper.AdminMapper;
import com.share.novel.service.AdminService;
import com.share.novel.utils.Result;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

	@Override
	public String login(String aname, String pwd) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aname", aname);
        queryWrapper.eq("pwd", pwd);
        boolean flag = adminMapper.exists(queryWrapper);
        if(flag)
            return Result.success("登陆成功", aname);
		return Result.error("登录名或者密码错误", "");
	}

	@Override
	public String addAdmin(String aname, String pwd, Admin admin) {

        if(admin.getPwd()==null||admin.getPwd().equals(""))
            return Result.error("密码不可为空", "");

        //只允许最高管理员添加管理员
		if(aname.equals("admin")){
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("aname", aname);
            queryWrapper.eq("pwd", pwd);
            boolean flag = adminMapper.exists(queryWrapper);
            if(flag){
                QueryWrapper<Admin> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("aname", admin.getAname());
                boolean flag1 = adminMapper.exists(queryWrapper2);
                if (flag1)
                    return Result.error("重名管理员，添加失败", "");
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
	public String delAdmin(String aname,String pwd, Admin admin) {
		if(!aname.equals("admin"))
            return Result.error("非最高管理员账户，不可删除", "");
        if(admin.getPwd()==null||admin.getPwd().equals(""))
            return Result.error("密码不可为空", "");
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aname", aname);
        queryWrapper.eq("pwd", pwd);
        boolean flag = adminMapper.exists(queryWrapper);
        if(!flag)
            return Result.error("密码错误", "");
        int d = adminMapper.deleteById(admin);
        if(d>0)
            return Result.success("成功移除管理员", "");
        return Result.error("移除失败，请重试", "");

    }

}
