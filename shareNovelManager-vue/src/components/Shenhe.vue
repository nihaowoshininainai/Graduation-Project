<!-- 这是审核界面 -->
<template>
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="审核文章" name="审核文章">
            <ArticleShenhe v-if="name === '审核文章'"></ArticleShenhe>
        </el-tab-pane>
        <el-tab-pane label="处理被举报文章" name="处理被举报文章">
            <ArticleJvbao v-if="name === '处理被举报文章'"></ArticleJvbao>
        </el-tab-pane>
        <el-tab-pane label="处理被举报评论" name="处理被举报评论">
            <CommentJvbao v-if="name === '处理被举报评论'"></CommentJvbao>
        </el-tab-pane>
        <el-tab-pane label="封禁用户" name="封禁用户">
            <BannedUser v-if="name === '封禁用户'" />
        </el-tab-pane>
        <el-tab-pane label="解封用户" name="解封用户">
            <JiefengUser v-if="name === '解封用户'" />
        </el-tab-pane>
    </el-tabs>
</template>
<script setup lang="ts">
import { defineAsyncComponent, ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
const ArticleJvbao = defineAsyncComponent(() => import('@/components/ArticleTwo.vue'))
const ArticleShenhe = defineAsyncComponent(() => import('@/components/ArticleOne.vue'))
const CommentJvbao = defineAsyncComponent(() => import('@/components/Comment.vue'))
const BannedUser = defineAsyncComponent(() => import('./UserOne.vue'))
const JiefengUser = defineAsyncComponent(() => import('./UserTwo.vue'))

const activeName = ref('审核文章')
const name = ref('审核文章')

const handleClick = (tab: TabsPaneContext, event: Event) => {
    name.value = tab.paneName as string
}
</script>