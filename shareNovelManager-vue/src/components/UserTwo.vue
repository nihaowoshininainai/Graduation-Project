<!-- 解封用户 -->
<template>
    <el-table :data="users">
        <el-table-column prop="uname" label="用户名" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="uid" label="用户id" />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button size="small" @click="jiefengTheUser(scope.$index,scope.row)">解封禁</el-button>
            </template>
        </el-table-column>
    </el-table>
 </template>
 <script setup lang="ts">
import type { Admin, Result, User } from '@/baseType';
import {  getBannedUsers, unbanUser } from '@/request/user';
import { ref } from 'vue';

const users = ref<User[]>()
getBannedUsers.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        users.value = result.data
})

const jiefengTheUser = (index: number, user: User) => {

ElMessageBox.prompt('请输入密码', '提示', {
    confirmButtonText: 'ok',
    cancelButtonText: 'cancel',
    inputPattern:
        /^\S+$/,
    inputErrorMessage: '密码不能为空',
    inputType: 'password'
}).then(({ value }) => {
    const admin: Admin = JSON.parse(localStorage.getItem('admin') as string)
    admin.pwd = value
    unbanUser(admin, user).then(res => {
        const result = res.data
        if (result === '操作成功') {
            users.value?.splice(index, 1)
            ElMessage.success(result)
        } else {
            ElMessage.error(result)
        }
    }
    )
})
}
</script>