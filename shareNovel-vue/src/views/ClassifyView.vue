<script setup lang="ts">
import type { Label } from '@/baseType';
import ShowNovels from '@/components/ShowNovels.vue';
import { getLabels } from '@/request/label';
import { ref } from 'vue';

const labels = ref<Label[]>([])
const currentLabel = -1
getLabels.then((result) => {
    labels.value = result.data.data
    if(import.meta.env.DEV){
        console.log(labels.value)
    }
})
</script>

<!-- 这是分类页面 -->
<template>
    <ShowNovels :labels="labels" :currentLabel="currentLabel"></ShowNovels>
</template>