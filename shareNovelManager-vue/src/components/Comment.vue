<!-- 这是评论管理 -->
<template>
  <el-table :data="comments">
    <el-table-column prop="content" label="内容"/>
    <el-table-column prop="uid" label="用户id"/>
</el-table>  
</template>
<script setup lang="ts">
import { type Comments, type Result } from '@/baseType';
import { getCommets } from '@/request/comments';
import { ref } from 'vue';

const comments = ref<Comments>()
getCommets.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        comments.value = result.data
})
</script>