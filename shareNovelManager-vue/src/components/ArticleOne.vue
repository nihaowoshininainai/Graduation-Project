<!-- 审核文章 -->
<template>
<el-table :data="novels">
    <el-table-column prop="title" label="文章名"/>
    <el-table-column prop="createTime" label="创建时间"/>
    <el-table-column prop="uid" label="用户id"/>
</el-table>
</template>
<script setup lang="ts">
import { type Novel, type Result } from '@/baseType';
import { getShenhe } from '@/request/novel';
import { ref } from 'vue';

const novels = ref<Novel[]>()
getShenhe.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        novels.value = result.data
})
</script>
