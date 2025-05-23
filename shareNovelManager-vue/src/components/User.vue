<!-- 这是用户管理 -->
<template>
  搜索框：
  <el-input style="width: 200px;" placeholder="输入用户名搜索" v-model="search"></el-input>
  <el-table :data="filterUsers">
    <el-table-column prop="uname" label="用户名" />
    <el-table-column prop="createTime" label="创建时间" />
    <el-table-column prop="uid" label="用户id" />
    <el-table-column prop="banned" label="状态">
      <template #default="{ row }">
        {{ row.banned === 0 ? '正常' : '封禁' }}
      </template>
    </el-table-column>
    <el-table-column>
      <template #default="scope">
        <el-button type="danger" @click="delUser(scope.row.uid)">删除</el-button>
        <el-button type="primary" @click="changePwd(scope.row)">修改密码</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup lang="ts">
import type { Admin, Result, User } from '@/baseType';
import { deleteUser, getAllUsers, changeUserPassword } from '@/request/user';
import { computed, ref } from 'vue';

const search = ref('')

const filterUsers = computed(() => {
  return users.value.filter((user) => {
    if(search.value==='') return true
    return user.uname.includes(search.value)
  })
})

const users = ref<User[]>([])
getAllUsers.then(res => {
  const result: Result = res.data
  users.value = result.data
})
const delUser = (uid: number) => {
  ElMessageBox.prompt('请输入密码', '删除用户', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
  }).then(({ value }) => {
    const admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
    admin.pwd = value
    const user: User = {
      uid: uid,
      uname: ''
    }
    deleteUser(admin, user).then(res => {
      ElMessage.warning(res.data)
    }
    )
  })
}
const changePwd = (user: User) => {
  ElMessageBox.prompt('请输入管理员密码', '验证管理员身份', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
  }).then(({ value }) => {
    const admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
    admin.pwd = value
    ElMessageBox.prompt('请输入新密码', '修改密码', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
    }).then(({ value }) => {
      user.pwd = value
      changeUserPassword(admin, user).then(res => {
        ElMessage.warning(res.data)
      })
    })
  })
}
</script>