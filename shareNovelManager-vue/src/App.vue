<script lang="ts" setup>
import { defineAsyncComponent, computed, ref } from 'vue'
import { User, ChatDotRound, Document, House } from '@element-plus/icons-vue'

// 替换静态导入为动态组件加载
const Home = defineAsyncComponent(() => import('./components/Home.vue'))
const UserM = defineAsyncComponent(() => import('./components/User.vue'))
const Article = defineAsyncComponent(() => import('./components/Article.vue'))
const Comment = defineAsyncComponent(() => import('./components/Comment.vue'))
const NotFound = defineAsyncComponent(() => import('./components/NotFound.vue'))
const Login = defineAsyncComponent(() => import('./components/Login.vue'))

const routes = {
    '/': Home,
    '/user': UserM,
    '/article': Article,
    '/comment': Comment,
    '/login': Login
}

const currentPath = ref(window.location.hash)

window.addEventListener('hashchange', () => {
    currentPath.value = window.location.hash
})

const currentView = computed(() => {
    const path = currentPath.value.slice(1) || '/'
    return routes[path as keyof typeof routes] || NotFound
})

if (localStorage.getItem('admin') === null)
    window.location.href = '#/login'

</script>

<template>

    <el-container>
        <el-header>
            <Header></Header>
        </el-header>
        <el-container>
            <el-aside width="200px"><el-menu>
                    <el-menu-item index="1">
                        <el-link :underline="false" href="#/">
                            <el-icon>
                                <House />
                            </el-icon>主页
                        </el-link>
                    </el-menu-item>
                    <el-menu-item index="2">
                        <el-link :underline="false" href="#/user">
                            <el-icon>
                                <User />
                            </el-icon>用户管理
                        </el-link>
                    </el-menu-item>
                    <el-menu-item index="3">
                        <el-link :underline="false" href="#/article">
                            <el-icon>
                                <Document />
                            </el-icon>文章管理
                        </el-link>
                    </el-menu-item>
                    <el-menu-item index="4">
                        <el-link :underline="false" href="#/comment">
                            <el-icon>
                                <ChatDotRound />
                            </el-icon>评论管理
                        </el-link>
                    </el-menu-item>
                </el-menu></el-aside>
            <el-main>
                <component :is="currentView"></component>
            </el-main>
        </el-container>
    </el-container>
</template>
<style scoped>
.el-link {
    width: 100%;
}
</style>