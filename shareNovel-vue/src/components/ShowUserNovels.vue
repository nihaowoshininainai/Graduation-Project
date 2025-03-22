<!-- 用户小说 -->
<template>
    <el-table :data="novels" border>
        <el-table-column prop="title" label="小说名" />
        <el-table-column prop="createTime" label="创作时间" />
        <el-table-column prop="views" label="浏览量" />
        <el-table-column prop="state" :formatter="(row: Novel) => {
            switch (row.state) {
                case 0:
                    return '待审核'
                case 1:
                    return '已上架'
                case -1:
                    return '审核未通过'
                case 2:
                    return '被举报'
                case 3:
                    return '已下架'
                default:
                    return '未知状态'
            }

        }" label="状态" />
    </el-table>
</template>
<script setup lang="ts">
import type { Novel, Result } from '@/baseType';
import { getUserNovel } from '@/request/novel';
import { rowContextKey } from 'element-plus';
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