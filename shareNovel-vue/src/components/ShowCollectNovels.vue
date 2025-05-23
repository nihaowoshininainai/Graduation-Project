<!-- 用户收藏 -->
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
        <el-table-column v-if="flag">
            <template #default="scope">
                <el-button @click="disLike(scope.row)">
                    取消收藏
                </el-button>
            </template>
        </el-table-column>
    </el-table>
</template>
<script setup lang="ts">
import type { Novel, Result, User } from '@/baseType';
import { delLike } from '@/request/collection';
import { getUserCollectNovel } from '@/request/novel';
import router from '@/router';
import { ref } from 'vue';
const prop = defineProps<{
    uid: number
}>()
const novels = ref<Novel[]>([])

const user: User = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string) as User
const flag = ref(false)
if (user.uid === Number(prop.uid)) {
    flag.value = true
}

getUserCollectNovel(prop.uid).then(res => {
    const result: Result = res.data
    novels.value = result.data
})

function disLike(novel: Novel) {
    delLike(novel).then(res => {
        const result: Result = res.data
        if (result.state === '1') {
            ElMessage.success(result.message)
        }
        else {
            ElMessage.error({
                message: result.message,
                duration: 2000,
                onClose: () => {
                    if (result.message === import.meta.env.VITE_MESSAGE_RELOGIN) {
                        localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
                        document.cookie += ";max-age=0"
                        router.push('/login')
                    }
                }
            })
        }
    })
}
</script>