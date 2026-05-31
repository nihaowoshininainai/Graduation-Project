<template>
  <div class="novel-page">
    <Novel :nid="Number(id)" />
    <Comments :id="Number(id)" />
  </div>
</template>

<script setup lang="ts">
defineProps<{ id: string }>()
</script>
