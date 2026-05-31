<template>
  <div class="user-manage">
    <div class="page-header">
      <h3>用户管理</h3>
      <el-input v-model="search" placeholder="搜索用户名" style="width: 240px;" clearable :prefix-icon="Search" />
    </div>
    <el-table :data="filteredUsers" stripe>
      <el-table-column prop="uname" label="用户名" />
      <el-table-column prop="uid" label="用户ID" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.banned === 0 ? 'success' : 'danger'" size="small">
            {{ row.banned === 0 ? '正常' : '封禁' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="warning" size="small" @click="handleChangePwd(scope.row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { Result, User } from '@/baseType'
import { getAllUsers, deleteUserByAdmin, resetPwdByAdmin } from '@/request/user'
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'

const search = ref('')
const users = ref<User[]>([])

const filteredUsers = computed(() => {
  if (!search.value) return users.value
  return users.value.filter(u => u.uname.includes(search.value))
})

const fetchUsers = async () => {
  const res = await getAllUsers()
  const result: Result = res.data
  if (result.state === '1') {
    users.value = result.data
  }
}

const handleDelete = async (user: User) => {
  try {
    await ElMessageBox.confirm(`确定删除用户 "${user.uname}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await deleteUserByAdmin({ uid: user.uid, uname: '' })
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchUsers()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

const handleChangePwd = (user: User) => {
  ElMessageBox.prompt('请输入新密码', `重置用户 "${user.uname}" 的密码`, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /\S/,
    inputErrorMessage: '密码不能为空',
  }).then(async ({ value }) => {
    const res = await resetPwdByAdmin({ uid: user.uid, uname: '', pwd: value })
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
    } else {
      ElMessage.error(result.message)
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchUsers()
})
</script>

<style lang="less" scoped>
.user-manage {
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
