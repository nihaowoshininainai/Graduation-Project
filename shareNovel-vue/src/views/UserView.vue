<!-- 这是用户页 -->
<script setup lang="ts">
import type { User } from '@/baseType';
import { ref } from 'vue';

const props = defineProps<{
    id: string
}>()
const user: User = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string) as User
const flag = ref(false)
if (user.uid === Number(props.id)) {
    flag.value = true
}
</script>
<template>
    <el-tabs type="border-card" v-if="flag">
        <el-tab-pane label="基本信息">
            <UserInfo :uid="Number(id)"></UserInfo>
        </el-tab-pane>
        <el-tab-pane label="我的作品">
            <ShowUserNovels :id="Number(id)"></ShowUserNovels>
        </el-tab-pane>
        <el-tab-pane label="我的收藏">
            <ShowCollectNovels :uid="Number(id)"></ShowCollectNovels>
        </el-tab-pane>
        <el-tab-pane label="我的评论">
            <UserComments></UserComments>
        </el-tab-pane>
        <el-tab-pane label="上传作品">
            <UploadNovel></UploadNovel>
        </el-tab-pane>
    </el-tabs>

    <el-tabs type="border-card" v-if="!flag">
        <el-tab-pane label="基本信息">
            <UserInfo :uid="Number(id)"></UserInfo>
        </el-tab-pane>
        <el-tab-pane label="他的作品">
            <ShowUserNovels :id="Number(id)"></ShowUserNovels>
        </el-tab-pane>
        <el-tab-pane label="他的收藏">
            <ShowCollectNovels :uid="Number(id)"></ShowCollectNovels>
        </el-tab-pane>
    </el-tabs>
</template>