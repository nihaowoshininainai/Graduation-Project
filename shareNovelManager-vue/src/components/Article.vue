<!-- 这是文章管理 -->
<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="审核文章" name="审核文章">
      <ArticleShenhe v-if="name==='审核文章'"></ArticleShenhe>
    </el-tab-pane>
    <el-tab-pane label="处理被举报文章" name="处理被举报文章">
        <ArticleJvbao v-if="name==='处理被举报文章'"></ArticleJvbao>
    </el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
import { defineAsyncComponent, ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
const ArticleJvbao = defineAsyncComponent(() => import('@/components/ArticleTwo.vue'))
const ArticleShenhe = defineAsyncComponent(()=>import('@/components/ArticleOne.vue'))

const activeName = ref('审核文章')
const name = ref('审核文章')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  name.value = tab.paneName as string
}
</script>
