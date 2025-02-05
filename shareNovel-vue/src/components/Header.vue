<!-- 这是导航栏 -->
<template>
    <el-row>
        <el-col :span="6">网站名</el-col>
        <el-col :span="6" style="display: flex;justify-content: space-around;">
            <RouterLink to="/">
                <el-link>首页</el-link>
            </RouterLink>
            <RouterLink to="/classify">
                <el-link>分类</el-link>
            </RouterLink>
            <RouterLink to="/recommend">
                <el-link>推荐</el-link>
            </RouterLink>
        </el-col>
        <el-col :span="6">
            搜索
        </el-col>
        <el-col :span="6" v-if="!flag" style="display: flex;">
            <el-link>登陆</el-link>
            |
            <el-link>注册</el-link>
        </el-col>
        <el-col :span="6" v-if="flag">
            <el-link>用户</el-link>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
import { ref } from 'vue';

const cookie = document.cookie
const flag = ref(true)
if(cookie.length===0)
    flag.value = false

</script>
<style lang="less" scoped>
.el-row{
    font-size: var(--el-font-size-base);
    .el-col{
        text-align: center;
    }
}
.el-link{
    display: block;
    height: 100%;
}
</style>