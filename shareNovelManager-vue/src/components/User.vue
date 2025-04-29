<!-- 这是用户管理 -->
<template>
<el-tabs v-model="activeName" @tab-click="handleClick">
  <el-tab-pane label="封禁用户" name="封禁用户">
    <BannedUser v-if="name === '封禁用户'" />
  </el-tab-pane>
  <el-tab-pane label="解封用户" name="解封用户">
    <JiefengUser v-if="name === '解封用户'" />
  </el-tab-pane>
</el-tabs>
</template>
<script setup lang="ts">
import type { TabsPaneContext } from 'element-plus';
import { defineAsyncComponent, ref } from 'vue';

const BannedUser = defineAsyncComponent(()=>import('./UserOne.vue'))
const JiefengUser = defineAsyncComponent(()=>import('./UserTwo.vue'))
const activeName = ref('封禁用户')
const name = ref('封禁用户')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  name.value = tab.paneName as string
}
</script>