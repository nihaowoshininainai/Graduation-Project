<template>
  <div class="user-novels">
    <el-table :data="novels" stripe style="width: 100%">
      <el-table-column prop="title" label="小说名" min-width="200" />
      <el-table-column prop="createTime" label="创作时间" width="160" />
      <el-table-column prop="views" label="浏览量" width="100" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="stateTagType(row.state)" size="small">{{ stateText(row.state) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="isSelf" label="操作" width="180">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { Novel, Result, User } from '@/baseType'
import { getUserNovel, delNovel } from '@/request/novel'
import { getStoredUser } from '@/request'
import { ref, onMounted } from 'vue'

const props = defineProps<{ id: number }>()

const novels = ref<Novel[]>([])
const isSelf = ref(false)

const stateText = (state?: number) => {
  const map: Record<number, string> = { 0: '待审核', 1: '已上架', '-1': '审核未通过', 2: '被举报', 3: '已下架' }
  return map[state ?? -2] || '未知'
}

const stateTagType = (state?: number) => {
  const map: Record<number, string> = { 0: 'warning', 1: 'success', '-1': 'danger', 2: 'danger', 3: 'info' }
  return (map[state ?? -2] || 'info') as any
}

const fetchNovels = async () => {
  const res = await getUserNovel(props.id)
  const result: Result = res.data
  if (result.state === '1') {
    novels.value = result.data
  }
}

const handleDelete = (novel: Novel) => {
  ElMessageBox.prompt('请输入密码确认删除', '删除小说', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
    inputPattern: /\S/,
    inputErrorMessage: '密码不能为空',
  }).then(async ({ value: pwd }) => {
    const user: User = { uname: '', pwd }
    const res = await delNovel(novel, user)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchNovels()
    } else {
      ElMessage.error(result.message)
    }
  }).catch(() => {})
}

onMounted(() => {
  const stored = getStoredUser()
  isSelf.value = stored?.uid === props.id
  fetchNovels()
})
</script>
