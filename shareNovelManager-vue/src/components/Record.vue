<template>
  <div class="record-page">
    <h3>审核记录</h3>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="小说审核记录" name="novel">
        <el-table :data="records" stripe>
          <el-table-column prop="record" label="审核记录" min-width="300" />
          <el-table-column prop="createTime" label="审核时间" width="180" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="评论审核记录" name="comment">
        <el-empty description="暂无数据" />
      </el-tab-pane>
      <el-tab-pane label="用户操作记录" name="user">
        <el-empty description="暂无数据" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import type { AdminToNovel, Result } from '@/baseType'
import { getArticleRecord } from '@/request/admin'
import { ref, onMounted } from 'vue'

const activeTab = ref('novel')
const records = ref<AdminToNovel[]>([])

onMounted(async () => {
  const res = await getArticleRecord()
  const result: Result = res.data
  if (result.state === '1') {
    records.value = result.data
  }
})
</script>

<style lang="less" scoped>
.record-page {
  h3 {
    margin: 0 0 16px;
    font-size: 18px;
  }
}
</style>
