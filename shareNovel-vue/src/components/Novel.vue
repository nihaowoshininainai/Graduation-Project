<!-- 小说内容 -->
<template>
    <el-row style="width: 100%;">
        <el-col>
            <h2>{{ novel?.title }}</h2>
        </el-col>
        <el-col>
            <el-text style="cursor: pointer;" @click="router.push(`/user/${novel?.uid}`)">作者：{{ uname }}</el-text>
            <el-text>创作时间：{{ novel?.createTime }}</el-text>
            <el-text style="cursor: pointer;">
                <svg @click="addLike(novel as Novel)" v-show="!isLike" xmlns="http://www.w3.org/2000/svg" width="21"
                    height="21" viewBox="0 0 26 26">
                    <path fill="#999999"
                        d="M17.869 3.889c-2.096 0-3.887 1.494-4.871 2.524c-.984-1.03-2.771-2.524-4.866-2.524C4.521 3.889 2 6.406 2 10.009c0 3.97 3.131 6.536 6.16 9.018c1.43 1.173 2.91 2.385 4.045 3.729c.191.225.471.355.765.355h.058c.295 0 .574-.131.764-.355c1.137-1.344 2.616-2.557 4.047-3.729C20.867 16.546 24 13.98 24 10.009c0-3.603-2.521-6.12-6.131-6.12" />
                </svg>
                <svg @click="disLike(novel as Novel)" v-show="isLike" xmlns="http://www.w3.org/2000/svg" width="21"
                    height="21" viewBox="0 0 26 26">
                    <path fill="#e11d48"
                        d="M17.869 3.889c-2.096 0-3.887 1.494-4.871 2.524c-.984-1.03-2.771-2.524-4.866-2.524C4.521 3.889 2 6.406 2 10.009c0 3.97 3.131 6.536 6.16 9.018c1.43 1.173 2.91 2.385 4.045 3.729c.191.225.471.355.765.355h.058c.295 0 .574-.131.764-.355c1.137-1.344 2.616-2.557 4.047-3.729C20.867 16.546 24 13.98 24 10.009c0-3.603-2.521-6.12-6.131-6.12" />
                </svg>
            </el-text>
            <el-text>浏览量：{{ novel?.views }}</el-text>
            <el-text style="cursor: pointer;" @click="reportNovel(novel as Novel)">举报</el-text>
        </el-col>
        <el-col v-html="novel?.content"></el-col>
    </el-row>
</template>
<script setup lang="ts">
import type { Novel, Result, User } from '@/baseType';
import { delLike, ifLike, like } from '@/request/collection';
import { addView, getView } from '@/request/hotdata';
import { getNovelDetail, reportNovel } from '@/request/novel';
import { getUname } from '@/request/user';
import router from '@/router';
import { ref } from 'vue';

const props = defineProps<{
    nid: number
}>()

let novelsId:number[] = []
const isExist = localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL_VIEW )
if (isExist) {
    novelsId = JSON.parse(isExist) as number[]
    if (!novelsId.includes(props.nid)) {
        addView(props.nid)
        novelsId.push(props.nid)
        localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL_VIEW, JSON.stringify(novelsId))
    }
}
else{
    addView(props.nid)
    novelsId.push(props.nid)
    localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL_VIEW, JSON.stringify(novelsId))
}

const novel = ref<Novel>()

const uname = ref<string>('')

const isLike = ref<boolean>(false)

getNovelDetail(props.nid).then(res => {
    const result: Result = res.data
    if (result.state === '1') {
        novel.value = result.data
        if ((novel.value ? true : false) && (novel.value?.state === -1 || novel.value?.state === 3)) {
            ElMessage.error({
                message: '该小说已下架',
                duration: 2000,
                onClose: () => router.push('/')
            })
        }
        else {
            getUname(novel.value?.uid as number).then(res => {
                const result: Result = res.data
                if (result.state === '1') {
                    uname.value = result.data
                }
            })
            getView(props.nid).then(res => {
                novel.value!.views = res.data
            }
            )
        }
    }
    else {
        ElMessage.error({
            message: '获取小说失败',
            duration: 2000,
            onClose: () => router.push('/')
        })
    }
})
if (localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER)) {
    const user = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string) as User
    ifLike(user.uid as number, props.nid).then(res => {
        const result: Result = res.data
        if (result.state === '1') {
            isLike.value = result.data
        }
    })
}
function addLike(novel: Novel) {
    like(novel).then(res => {
        const result: Result = res.data
        if (result.state === '1') {
            isLike.value = true
            ElMessage.success(result.message)
        }
        else {
            ElMessage.error(result.message)
        }
    })
}
function disLike(novel: Novel) {
    delLike(novel).then(res => {
        const result: Result = res.data
        if (result.state === '1') {
            isLike.value = false
            ElMessage.success(result.message)
        }
        else {
            ElMessage.error(result.message)
        }
    })
}
</script>
<style lang="less" scoped>

</style>