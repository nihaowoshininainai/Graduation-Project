<!-- 审核文章 -->
<template>
    <el-table :data="novels">
        <el-table-column prop="title" label="文章名" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="uid" label="用户id" />
        <el-table-column>
            <template #default="scope">
                <el-button  size="small" @click="passButton(scope.$index,scope.row)">通过</el-button>
                <el-button  size="small" @click="nopassButton(scope.$index,scope.row)">不通过</el-button>
            </template>
        </el-table-column>
    </el-table>
</template>
<script setup lang="ts">
import { type Admin, type Novel, type Result } from '@/baseType';
import { getShenhe, nopass, pass } from '@/request/novel';
import { ref } from 'vue';

const novels = ref<Novel[]>()
getShenhe.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        novels.value = result.data
})

const passButton = (index:number,novel:Novel)=>{
    ElMessageBox.prompt('请输入管理员密码','Tip',{
        confirmButtonText:'ok',
        cancelButtonText: 'cancel',
        inputPattern:
             /^\S+$/,
        inputErrorMessage: '密码不能为空',
    }).then(({ value }) => {
        const admin:Admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
        admin.pwd = value
        pass(admin,novel).then((res)=>{
            const result = res.data
            if(result ==='操作成功'){
                novels.value?.splice(index,1)
                ElMessage.success(result)
            }else
                ElMessage.error(result)
        })
    })
}

const nopassButton = (index:number,novel:Novel)=>{
    ElMessageBox.prompt('请输入管理员密码','Tip',{
        confirmButtonText:'ok',
        cancelButtonText: 'cancel',
        inputPattern:
             /^\S+$/,
        inputErrorMessage: '密码不能为空',
    }).then(({ value }) => {
        const admin:Admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
        admin.pwd = value
        nopass(admin,novel).then((res)=>{
            const result = res.data
            if(result ==='操作成功'){
                novels.value?.splice(index,1)
                ElMessage.success(result)
            }else
                ElMessage.error(result)
        })
    })
}
</script>
