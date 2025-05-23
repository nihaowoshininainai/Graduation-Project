<template>
    <el-table :data="record">
        <el-table-column prop="record" label="审核记录" />
        <el-table-column prop="createTime" label="审核时间" />
    </el-table>
</template>
 <script setup lang="ts">
import { type AdminToNovel } from '@/baseType';
import { getArticleRecord } from '@/request/admin';
import { ref } from 'vue';

const record = ref<AdminToNovel[]>([]);
getArticleRecord().then((res) => {
  record.value = res.data.data;
})
</script>