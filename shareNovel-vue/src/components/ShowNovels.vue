<!-- 这是主页 -->
<script setup lang="ts">
import type { Label, Novel } from '@/baseType';
import { Condition, Sort } from '@/enmu';
import { getIndexNovel } from '@/request/novel';
import { getUname } from '@/request/user';
import router from '@/router';
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const props = defineProps<{
    labels?: Label[],
    currentLabel?: number
}>()
const currentLabel = ref(props.currentLabel)
const pageCount = ref(0)
const route = useRoute()
const current = ref(Number(route.params.page))
const novel = ref<Novel[]>([])
const unames = ref<string[]>([])
const condition = ref<Condition>(Condition.hot)
const sort = ref<Sort>(Sort.desc)
const getData = (labelId?:number) => {
    getIndexNovel(current.value, import.meta.env.VITE_PAGE_SIZE, Condition[condition.value], Sort[sort.value], labelId).then((result) => {
        if (import.meta.env.DEV) {
            console.log(result.data.data)
            console.log(Condition[condition.value]);

        }
        pageCount.value = result.data.data.pages
        novel.value = result.data.data.novels
        if (import.meta.env.DEV)
            console.log(novel.value);
        novel.value.forEach((element, index) => {
            getUname(element.uid as number).then((result) => {
                if (import.meta.env.DEV) {
                    console.log(result)
                }
                unames.value[index] = result.data.data
            })
        })
    })
}
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

getData()
</script>
<template>
    <div class="classify" v-if="currentLabel">
        <div>
            <el-button :type="currentLabel === -1 ? 'primary' : ''" @click="currentLabel = -1;getData()">全部</el-button>
        </div>
        <div v-for="label in labels" :key="label.id">
            <el-button :type="currentLabel === label.id ? 'primary' : ''"
                @click="currentLabel = label.id;getData(label.id)">{{ label.name }}</el-button>
        </div>
    </div>
    <el-row>
        <el-col :span="12">
            <el-button :type="condition === Condition.hot ? 'primary' : ''"
                @click="condition = Condition.hot; getData(currentLabel)">按热度</el-button>
            <el-button :type="condition === Condition.time ? 'primary' : ''"
                @click="condition = Condition.time; getData(currentLabel)">按时间</el-button>
        </el-col>
        <el-col :span="12">
            <el-button :type="sort === Sort.asc ? 'primary' : ''" @click="sort = Sort.asc; getData(currentLabel)">升序</el-button>
            <el-button :type="sort === Sort.desc ? 'primary' : ''" @click="sort = Sort.desc; getData(currentLabel)">降序</el-button>
        </el-col>
    </el-row>
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
                    <span @click="goToUser(item.uid as number)">
                        <el-link>{{ unames[index] }}</el-link>
                    </span>
                </div>
            </template>
        </el-card>
    </div>
    <el-pagination background layout="prev, pager, next" :page-count="pageCount" v-model:current-page="current" />
</template>
<style lang="less">
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

.el-col {
    display: flex;
    justify-content: center;
}
.classify{
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin-bottom: 10px;
    .el-button{
        margin: 10px;
    }
}
</style>