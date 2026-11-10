<template>
  <div class="collect-novels">
    <el-table :data="novels" stripe style="width: 100%">
      <el-table-column prop="title" label="小说名" min-width="200" />
      <el-table-column prop="createTime" label="创作时间" width="160" />
      <el-table-column prop="views" label="浏览量" width="100" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="stateTagType(row.state)" size="small">{{ stateText(row.state) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="isSelf" label="操作" width="120">
        <template #default="scope">
          <el-button type="warning" size="small" @click="handleUncollect(scope.row as Novel)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { Novel, Result } from '@/baseType'
import { delLike } from '@/request/collection'
import { getUserCollectNovel } from '@/request/novel'
import { getStoredUser } from '@/request'
import { ref, onMounted } from 'vue'

const props = defineProps<{ uid: number }>()

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

const fetchCollections = async () => {
  const res = await getUserCollectNovel(props.uid)
  const result: Result = res.data
  if (result.state === '1') {
    novels.value = result.data
  }
}

const handleUncollect = async (novel: Novel) => {
  if (!novel.id) return
  try {
    await ElMessageBox.confirm('确定取消收藏该小说吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    })
    const res = await delLike(novel.id)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchCollections()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

onMounted(() => {
  const stored = getStoredUser()
  isSelf.value = stored?.uid === props.uid
  fetchCollections()
})
</script>
