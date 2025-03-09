<!-- 这是推荐界面 -->
<template>
    <div class="common-layout">
        <el-card v-for="(item, index) in novel">
            <template #header>
                <div class="title">
                    <span @click="router.push(`/novel/${item.id}`)">
                        <el-link>{{ item.title }}</el-link>
                    </span>
                </div>
            </template>
            <el-text>{{ item.content }}</el-text>
            <template #footer>
                <div>
                    <span>{{ item.createTime }}</span>
                    <span @click="router.push(`/user/${item.uid}`)">
                        <el-link>{{ unames[index] }}</el-link>
                    </span>
                </div>
            </template>
        </el-card>
    </div>
</template>
<script setup lang="ts">
import type { Novel, Result, User } from '@/baseType';
import { ref } from 'vue';
import router from '@/router';
import { getNovelRecommend } from '@/request/novel';
import { getUname } from '@/request/user';

const novel = ref<Novel[]>([]);
const user: User = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string)
const uid = user.uid as number
const unames = ref<string[]>([])

const getDate = () => {
    getNovelRecommend(uid).then(res => {
        const result: Result = res.data
        novel.value = result.data
        novel.value.forEach((element, index) => {
            getUname(element.uid as number).then((result) => {
                if (import.meta.env.DEV) {
                    console.log(result)
                }
                unames.value[index] = result.data.data
            })
        })
    }
    )
}

getDate()
</script>
<style scoped>
.common-layout {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
}

.el-card {
    margin: 20px;
    height: 360px;
    width: 300px;
}

.title {
    text-align: center;
}
</style>