<!-- 这是小说界面 -->
<template>
    <el-row>
        <el-col>
            <Novel :nid="Number(id)"></Novel>
        </el-col>
        <el-col>
            <Comments :id="Number(id)"></Comments>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
defineProps<{
    id: string
}>()
</script>