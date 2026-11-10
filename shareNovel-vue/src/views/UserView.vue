<template>
  <div class="user-page">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="基本信息" name="info">
        <UserInfo :uid="Number(id)" />
      </el-tab-pane>
      <el-tab-pane :label="isSelf ? '我的作品' : '他的作品'" name="novels">
        <ShowUserNovels :id="Number(id)" />
      </el-tab-pane>
      <el-tab-pane :label="isSelf ? '我的收藏' : '他的收藏'" name="collections">
        <ShowCollectNovels :uid="Number(id)" />
      </el-tab-pane>
      <el-tab-pane v-if="isSelf" label="我的评论" name="comments">
        <UserComments />
      </el-tab-pane>
      <el-tab-pane v-if="isSelf" label="上传作品" name="upload">
        <UploadNovel />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { getStoredUser } from '@/request'

const props = defineProps<{ id: string }>()
const activeTab = ref('info')
const isSelf = computed(() => {
  const user = getStoredUser()
  return user?.uid === Number(props.id)
})
</script>

<style lang="less" scoped>
.user-page {
  :deep(.el-tabs) {
    border-radius: 16px;
    overflow: hidden;
    box-shadow:
      0 8px 28px rgba(196, 80, 40, 0.06),
      0 2px 8px rgba(42, 36, 32, 0.04);
    border: 1px solid rgba(196, 80, 40, 0.10);
  }

  :deep(.el-tabs__header) {
    background: var(--ark-white);
    margin: 0;
  }

  :deep(.el-tabs__nav-wrap::after) {
    background-color: rgba(196, 80, 40, 0.08);
  }

  :deep(.el-tabs__item.is-active) {
    color: var(--ark-terracotta);
    font-weight: 600;
  }

  :deep(.el-tabs__active-bar) {
    background: var(--ark-terracotta);
    height: 3px;
    border-radius: 3px;
  }

  :deep(.el-tabs__content) {
    padding: 20px;
    background: var(--ark-white);
  }
}
</style>
