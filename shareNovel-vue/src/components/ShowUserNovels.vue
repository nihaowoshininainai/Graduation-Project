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
        <el-table-column v-if="flag">
            <template #default="scope">
                <el-button type="danger" @click="deleteNovel(scope.row)">删除</el-button>
                <el-button @click="modifyNovel(scope.row)">修改</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-drawer v-model="drawer" title="I am the title" :with-header="false">
        <ModifyNovel :novel="novell" />
    </el-drawer>

</template>
<script setup lang="ts">
import type { Novel, Result, User } from '@/baseType';
import { getUserNovel } from '@/request/novel';
import { delNovel } from '@/request/novel'
import { ref } from 'vue';
import ModifyNovel from './ModifyNovel.vue';

const props = defineProps<{
    id: number
}>()

const user: User = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string) as User
const flag = ref(false)
if (user.uid === Number(props.id)) {
    flag.value = true
}

const novell = ref<Novel>({
    title: '',
    content: ''
})

const drawer = ref(false)

const novels = ref<Novel[]>([])
getUserNovel(props.id).then(res => {
    const result: Result = res.data
    novels.value = result.data
})

const deleteNovel = (novel: Novel) => {
    ElMessageBox.prompt('请输入密码', '删除小说', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password'
    }).then(({ value }) => {
        const user: User = {
            pwd: value,
            uname: ''
        }
        delNovel(novel, user).then(res => {
            const result: Result = res.data
            if (result.state === '1')
                ElMessage.success(result.message)
            else
                ElMessage.error(result.message)

        })
    })
}

const modifyNovel = (novel: Novel) => {
    drawer.value = true
    novell.value = novel
}

</script>