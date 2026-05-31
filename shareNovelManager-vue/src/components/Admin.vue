<template>
  <div class="admin-manage">
    <div class="page-header">
      <h3>管理员管理</h3>
      <el-button type="primary" @click="drawer = true">添加管理员</el-button>
    </div>
    <el-table :data="admins" stripe>
      <el-table-column prop="aname" label="管理员名" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer v-model="drawer" title="添加管理员" size="350px">
      <el-form label-position="top">
        <el-form-item label="当前管理员密码">
          <el-input v-model="currentPwd" type="password" show-password />
        </el-form-item>
        <el-form-item label="新管理员名">
          <el-input v-model="newAdmin.aname" />
        </el-form-item>
        <el-form-item label="新管理员密码">
          <el-input v-model="newAdmin.pwd" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">添加</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import type { Admin, Result } from '@/baseType'
import { addAdmin, delAdmin, getAdminInfo } from '@/request/admin'
import { getStoredAdmin } from '@/request'
import { ref, onMounted } from 'vue'

const admins = ref<Admin[]>([])
const drawer = ref(false)
const currentPwd = ref('')
const newAdmin = ref<Admin>({ aname: '', pwd: '' })

const fetchAdmins = async () => {
  const stored = getStoredAdmin()
  if (!stored?.aname) return
  const res = await getAdminInfo(stored.aname)
  const result: Result = res.data
  if (result.state === '1') {
    admins.value = result.data
  } else {
    ElMessage.error(result.message)
  }
}

const handleAdd = async () => {
  const stored = getStoredAdmin()
  if (!stored) return
  const adminTop: Admin = { aname: stored.aname, pwd: currentPwd.value }
  const res = await addAdmin(adminTop, newAdmin.value)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    drawer.value = false
    currentPwd.value = ''
    newAdmin.value = { aname: '', pwd: '' }
    fetchAdmins()
  } else {
    ElMessage.error(result.message)
  }
}

const handleDelete = async (admin: Admin) => {
  try {
    const { value: pwd } = await ElMessageBox.prompt('请输入当前管理员密码', '删除管理员', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPattern: /\S/,
      inputErrorMessage: '密码不能为空',
    })
    await ElMessageBox.confirm(`确定删除管理员 "${admin.aname}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const stored = getStoredAdmin()
    if (!stored) return
    const adminTop: Admin = { aname: stored.aname, pwd }
    const res = await delAdmin(adminTop, admin)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchAdmins()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

onMounted(() => {
  fetchAdmins()
})
</script>

<style lang="less" scoped>
.admin-manage {
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
