<!-- 这是用户页 -->
<script setup lang="ts">
defineProps<{
    id: string
}>()
</script>
<template>
    <el-tabs type="border-card">
        <el-tab-pane label="基本信息">
            <UserInfo :uid="Number(id)"></UserInfo>
        </el-tab-pane>
        <el-tab-pane label="我的作品">
            <ShowUserNovels :id="Number(id)"></ShowUserNovels>
        </el-tab-pane>
        <el-tab-pane label="我的收藏">
            <ShowCollectNovels :uid="Number(id)"></ShowCollectNovels>
        </el-tab-pane>
        <el-tab-pane label="上传作品">
            <UploadNovel></UploadNovel>
        </el-tab-pane>
    </el-tabs>
</template>