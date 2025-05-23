<template>
    <el-table :data="comments" border>
        <el-table-column prop="nid" label="小说id" />
        <el-table-column prop="uid" label="用户id" />
        <el-table-column prop="content" label="评论内容" />
        <el-table-column prop="state" :formatter="(row: Comments) => {
            switch (row.state) {
                case 0:
                    return '正常'
                case 1:
                    return '被举报'
                case -1:
                    return '被封禁'
                default:
                    return '未知状态'
            }

        }" label="状态" />
        <el-table-column>
            <template #default="scope">
                <el-button type="danger" @click="deleteNovel(scope.row)">删除</el-button>
                <el-button @click="modifyNovel(scope.row)">修改</el-button>
            </template>
        </el-table-column>
    </el-table>
</template>
<script setup lang="ts">
import { type Comments, type Result, type User } from '@/baseType';
import { getUserComments, delComments } from '@/request/comments';
import { ref } from 'vue';

const comments = ref<Comments[]>([])
const user = JSON.parse(localStorage.getItem('user') as string) as User

getUserComments(user.uid as number).then(res => {
    const result: Result = res.data
    comments.value = result.data
})

const deleteNovel = (row: Comments) => {
    delComments(row).then(res => {
        const result: Result = res.data
        if (result.state === '1')
            ElMessage.success(result.message)
        else
            ElMessage.error(result.message)
    })
}
</script>