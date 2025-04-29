<!-- Admin管理 -->
<template>
    <el-button type="primary" @click="drawer = true">添加管理员</el-button>
    <el-table :data="admins">
        <el-table-column prop="aname" label="管理员名"></el-table-column>
        <el-table-column prop="pwd" label="密码"></el-table-column>
        <el-table-column>
            <template #default="scope">
                <el-button type="danger" @click="deleteAdmin(scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-drawer v-model="drawer">
        <el-form label-width="auto">
            <h1>添加管理员</h1>
            <el-form-item label="管理员名">
                <el-input v-model="admin.aname"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="admin.pwd"></el-input>
            </el-form-item>
        </el-form>
        <el-button type="primary" @click="addAdmin">添加</el-button>
    </el-drawer>
</template>
<script setup lang="ts">
import type { Admin, Result } from '@/baseType';
import { add, delAdmin, getAdminInfo } from '@/request/admin';
import { ref } from 'vue';

const admins = ref<Admin[]>([]);
const drawer = ref(false);
const admin = ref<Admin>({
    aname: '',
    pwd: ''
})

const addAdmin = () => {
    add(JSON.parse(localStorage.getItem('admin') as string), admin.value).then(value => {
        const result: Result = value.data;
        if (result.state === '1') {
            ElMessage.success({
                duration: 1000,
                message: result.message,
                onClose: () =>{
                    location.reload()
                }
            })
        }
        else
            ElMessage.error(result.message)
    })
}
const deleteAdmin = (admin: Admin) => {
    ElMessageBox.confirm('确定删除该管理员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        delAdmin(JSON.parse(localStorage.getItem('admin') as string), admin).then(value => {
            const result: Result = value.data;
            if (result.state === '1') {
                ElMessage.success({
                    duration: 1000,
                    message: result.message,
                    onClose: () =>{
                        location.reload()
                    }
                })
            }
            else
                ElMessage.error(result.message)

        })
    })
}
getAdminInfo(JSON.parse(localStorage.getItem('admin') as string).aname).then((res) => {
    const result: Result = res.data;
    if (import.meta.env.DEV)
        console.log(result.data)
    if (result.state === '1')
        admins.value = result.data
    else
        ElMessage.error(result.message)
})
</script>