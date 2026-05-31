<template>
  <div class="home-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="待审核小说" :value="shenheCount">
            <template #suffix>篇</template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="被举报小说" :value="jvbaoCount">
            <template #suffix>篇</template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="被举报评论" :value="jvbaoCommentsCount">
            <template #suffix>条</template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="应封禁用户" :value="banUsersCount">
            <template #suffix>人</template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="stat-card">
          <el-statistic title="已封禁用户" :value="bannedUsersCount">
            <template #suffix>人</template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import type { Result } from '@/baseType'
import { getShenhe, getJvbao } from '@/request/novel'
import { getBanUsers, getBannedUsers } from '@/request/user'
import { getReportedComments } from '@/request/comments'
import { ref, onMounted } from 'vue'

const shenheCount = ref(0)
const jvbaoCount = ref(0)
const jvbaoCommentsCount = ref(0)
const banUsersCount = ref(0)
const bannedUsersCount = ref(0)

onMounted(async () => {
  const [shenheRes, jvbaoRes, commentsRes, banRes, bannedRes] = await Promise.all([
    getShenhe(),
    getJvbao(),
    getReportedComments(),
    getBanUsers(),
    getBannedUsers(),
  ])
  const shenheResult: Result = shenheRes.data
  const jvbaoResult: Result = jvbaoRes.data
  const commentsResult: Result = commentsRes.data
  const banResult: Result = banRes.data
  const bannedResult: Result = bannedRes.data

  if (shenheResult.state === '1') shenheCount.value = shenheResult.data?.length || 0
  if (jvbaoResult.state === '1') jvbaoCount.value = jvbaoResult.data?.length || 0
  if (commentsResult.state === '1') jvbaoCommentsCount.value = commentsResult.data?.length || 0
  if (banResult.state === '1') banUsersCount.value = banResult.data?.length || 0
  if (bannedResult.state === '1') bannedUsersCount.value = bannedResult.data?.length || 0
})
</script>

<style lang="less" scoped>
.home-page {
  padding: 10px 0;
}

.stat-card {
  text-align: center;
}
</style>
