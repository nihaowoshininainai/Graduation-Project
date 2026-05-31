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
  :deep(.el-tabs__content) {
    padding: 20px;
  }
}
</style>
