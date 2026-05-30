package com.share.novel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.share.novel.base.Admin;
import com.share.novel.base.Label;
import com.share.novel.mapper.AdminMapper;
import com.share.novel.mapper.LabelMapper;

import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final LabelMapper labelMapper;

    @Override
    public void run(String... args) {
        initAdmin();
        initLabels();
    }

    private void initAdmin() {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getAname, "admin");
        if (!adminMapper.exists(wrapper)) {
            Admin admin = new Admin();
            admin.setAname("admin");
            admin.setPwd(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            adminMapper.insert(admin);
        }
    }

    private void initLabels() {
        String[] defaultLabels = {"玄幻", "都市", "科幻", "历史", "悬疑", "言情", "武侠", "游戏"};
        for (String name : defaultLabels) {
            LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Label::getName, name);
            if (!labelMapper.exists(wrapper)) {
                Label label = new Label();
                label.setName(name);
                labelMapper.insert(label);
            }
        }
    }
}
