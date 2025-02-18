<template>
    <el-table :data="novels">
        <el-table-column prop="title" label="小说名" />
        <el-table-column prop="createTime" label="收藏时间" />
        <el-table-column prop="views" label="浏览量" />
        <el-table-column prop="state" label="状态" />
    </el-table>
</template>
<script setup lang="ts">
import type { Novel, Result } from '@/baseType';
import { getUserNovel } from '@/request/novel';
import { ref } from 'vue';

const props = defineProps<{
    id: number
}>()

const novels = ref<Novel[]>([])
getUserNovel(props.id).then(res => {
    const result: Result = res.data
    novels.value = result.data
})
</script>