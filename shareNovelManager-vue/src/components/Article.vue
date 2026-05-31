<template>
  <div class="article-manage">
    <div class="page-header">
      <h3>文章管理</h3>
      <el-input v-model="search" placeholder="搜索标题" style="width: 240px;" clearable :prefix-icon="Search" />
    </div>
    <el-table :data="filteredNovels" stripe>
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="uid" label="作者ID" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="状态" width="140">
        <template #default="{ row }">
          <el-tag :type="stateTagType(row.state)" size="small">{{ stateText(row.state) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="viewDialog" :title="viewNovel?.title" width="700px">
      <div v-html="viewNovel?.content" style="max-height: 500px; overflow-y: auto;"></div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import type { Novel, Result } from '@/baseType'
import { getAllNovels, deleteNovelByAdmin } from '@/request/novel'
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'

const search = ref('')
const novels = ref<Novel[]>([])
const viewDialog = ref(false)
const viewNovel = ref<Novel | null>(null)

const stateText = (state?: number) => {
  const map: Record<number, string> = { 0: '待审核', '-1': '审核不通过', 1: '正常', 2: '被举报', 3: '已下架' }
  return map[state ?? -2] || '未知'
}

const stateTagType = (state?: number) => {
  const map: Record<number, string> = { 0: 'warning', '-1': 'danger', 1: 'success', 2: 'danger', 3: 'info' }
  return (map[state ?? -2] || 'info') as any
}

const filteredNovels = computed(() => {
  if (!search.value) return novels.value
  return novels.value.filter(n => n.title.includes(search.value))
})

const fetchNovels = async () => {
  const res = await getAllNovels()
  const result: Result = res.data
  if (result.state === '1') {
    novels.value = result.data
  }
}

const handleDelete = async (novel: Novel) => {
  try {
    await ElMessageBox.confirm(`确定删除小说 "${novel.title}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await deleteNovelByAdmin({ id: novel.id, title: '' } as Novel)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchNovels()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

const handleView = (novel: Novel) => {
  viewNovel.value = novel
  viewDialog.value = true
}

onMounted(() => {
  fetchNovels()
})
</script>

<style lang="less" scoped>
.article-manage {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    h3 {
      margin: 0;
      font-size: 18px;
    }
  }
}
</style>
