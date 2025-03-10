<!-- 搜索结果界面 -->
<template>
    <ShowNovels :name="name"></ShowNovels>
</template>
<script setup lang="ts">
defineProps<{ name: string }>();
</script>