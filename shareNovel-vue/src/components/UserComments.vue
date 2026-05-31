<template>
  <div class="user-comments">
    <el-table :data="comments" stripe style="width: 100%">
      <el-table-column prop="nid" label="小说ID" width="100" />
      <el-table-column prop="content" label="评论内容" min-width="300" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.state === 0 ? 'success' : row.state === 1 ? 'warning' : 'danger'" size="small">
            {{ row.state === 0 ? '正常' : row.state === 1 ? '被举报' : '封禁' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { Comments, Result } from '@/baseType'
import { getUserComments, delComments } from '@/request/comments'
import { getStoredUser } from '@/request'
import { ref, onMounted } from 'vue'

const comments = ref<Comments[]>([])

const fetchComments = async () => {
  const user = getStoredUser()
  if (!user?.uid) return
  const res = await getUserComments(user.uid)
  const result: Result = res.data
  if (result.state === '1') {
    comments.value = result.data
  }
}

const handleDelete = async (id?: number) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确定删除该评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await delComments(id)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchComments()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

onMounted(() => {
  fetchComments()
})
</script>
