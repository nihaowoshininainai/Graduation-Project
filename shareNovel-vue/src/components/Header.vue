<!-- 这是导航栏 -->
<template>
    <el-row>
        <el-col :span="6">小说分享网站</el-col>
        <el-col :span="6" style="display: flex;justify-content: space-around;">
            <RouterLink to="/">
                <el-link>首页</el-link>
            </RouterLink>
            <RouterLink to="/classify/page/1">
                <el-link>分类</el-link>
            </RouterLink>
            <RouterLink to="/recommend">
                <el-link>推荐</el-link>
            </RouterLink>
        </el-col>
        <el-col :span="6" style="display: flex;">
            <el-input size="small" placeholder="请输入内容" v-model="input" @keydown.enter="search"></el-input>
            <el-button type="primary" size="small" :disabled="input === ''"
                @click="search">搜索</el-button>
        </el-col>
        <el-col :span="6" v-if="!(cookie.length !== 0)" style="display: flex;justify-content: center;">
            <RouterLink to="/login">
                <el-link>登录</el-link>
            </RouterLink>
            |
            <RouterLink to="/register">
                <el-link>注册</el-link>
            </RouterLink>
        </el-col>
        <el-col :span="6" v-if="cookie.length !== 0">
            <RouterLink :to="'/user/' + user.uid">
                <el-link>{{ user.uname }}</el-link>
            </RouterLink>
            |
            <el-button link type="primary" @click="loginOut" size="small">退出登陆</el-button>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
import type { User } from '@/baseType';
import router from '@/router';
import { ref } from 'vue';
import { RouterLink } from 'vue-router';

const cookie = document.cookie
const user: User = JSON.parse(window.localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) || '{}') as User;

const loginOut = () => {
    window.localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
    document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;'
    router.go(0)
}

const input = ref('')

const search = () => {
    router.push('/search/1?name=' + input.value) 
    input.value = ''
}
</script>
<style lang="less" scoped>
.el-row {
    font-size: var(--el-font-size-base);

    .el-col {
        text-align: center;
    }
}
</style>