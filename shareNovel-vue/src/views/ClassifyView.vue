<script setup lang="ts">
import type { Label } from '@/baseType'
import ShowNovels from '@/components/ShowNovels.vue'
import { getLabels } from '@/request/label'
import type { Result } from '@/baseType'
import { ref, onMounted } from 'vue'

const labels = ref<Label[]>([])

onMounted(async () => {
  const res = await getLabels()
  const result: Result = res.data
  if (result.state === '1') {
    labels.value = result.data
  }
})
</script>

<template>
  <ShowNovels :labels="labels" :currentLabel="-1" />
</template>
