<!-- 这是主页 -->
<script setup lang="ts">
import type { Novel } from '@/baseType';
import { getIndexNovel } from '@/request/novel';
import { getUname } from '@/request/user';
import router from '@/router';
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const pageCount = ref(0)
const route = useRoute()
const current = ref(Number(route.params.page))
const novel = ref<Novel[]>([])
const unames = ref<string[]>([])
const getData = () => {
    getIndexNovel(current.value, import.meta.env.VITE_PAGE_SIZE).then((result) => {
        if (import.meta.env.DEV) {
            console.log(result.data.data)
        }
        pageCount.value = result.data.data.pages
        novel.value = result.data.data.novels
        if (import.meta.env.DEV)
            console.log(novel.value);
        novel.value.forEach((element, index) => {
            getUname(element.uid).then((result) => {
                if (import.meta.env.DEV) {
                    console.log(result)
                }
                unames.value[index] = result.data.data
            })
        })
    })
}
getData()
function replacePageRoute(originalUrl: string, newNumber: string | number): string {
    // 使用正则表达式匹配/page后的数字
    const regex = /\/page\/(\d+)/;
    // 替换匹配到的数字部分
    const updatedUrl = originalUrl.replace(regex, `/page/${newNumber}`);
    return updatedUrl;
}
watch(current, (newV, oldV) => {
    if (import.meta.env.DEV) {
        console.log(newV)
    }

    router.push(replacePageRoute(route.fullPath, newV))
    getData()
})
const goToUser = (uid: number) => {
    router.push(`/user/${uid}`)
}
</script>
<template>
    <div class="common-layout">
        <el-card v-for="(item, index) in novel">
            <template #header>
                <div class="title">
                    <span>{{ item.title }}</span>
                </div>
            </template>
            <el-text>{{ item.content }}</el-text>
            <template #footer>
                <div>
                    <span>{{ item.createTime }}</span>
                    <span @click="goToUser(item.uid)">
                        <el-link>{{ unames[index] }}</el-link>
                    </span>
                </div>
            </template>
        </el-card>
    </div>
    <el-pagination background layout="prev, pager, next" :page-count="pageCount" v-model:current-page="current" />
</template>
<style lang="less" scoped>
.el-pagination {
    margin-top: 20px;
    display: flex;
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

.common-layout {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
}
</style>