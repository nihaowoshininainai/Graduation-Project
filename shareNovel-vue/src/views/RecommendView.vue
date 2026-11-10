<template>
  <div class="recommend-page">
    <h2 class="page-title">为你推荐</h2>
    <div v-if="novels.length" class="novel-grid">
      <el-card v-for="novel in novels" :key="novel.id" shadow="hover" class="novel-card"
        @click="router.push(`/novel/${novel.id}`)">
        <div class="card-body">
          <h3 class="card-title">{{ novel.title }}</h3>
          <div class="card-content" v-html="novel.content"></div>
        </div>
        <div class="card-footer">
          <span class="card-meta">{{ novel.createTime }}</span>
          <span class="card-meta" @click.stop="router.push(`/user/${novel.uid}`)">
            作者: {{ unameMap.get(novel.uid!) || '...' }}
          </span>
          <span class="card-meta">{{ novel.views || 0 }} 次浏览</span>
        </div>
      </el-card>
    </div>
    <el-empty v-else-if="loaded" description="暂无推荐，请先浏览更多小说" />
    <el-skeleton v-else :rows="5" animated />
  </div>
</template>

<script setup lang="ts">
import type { Novel, Result } from '@/baseType'
import { getNovelRecommend } from '@/request/novel'
import { getUname } from '@/request/user'
import { getStoredUser, isLoggedIn } from '@/request'
import router from '@/router'
import { ref, onMounted } from 'vue'

const novels = ref<Novel[]>([])
const unameMap = ref<Map<number, string>>(new Map())
const loaded = ref(false)

onMounted(async () => {
  if (!isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  const user = getStoredUser()
  if (!user?.uid) return
  try {
    const res = await getNovelRecommend(user.uid)
    const result: Result = res.data
    if (result.state === '1') {
      novels.value = result.data
      novels.value.forEach((novel) => {
        if (novel.uid && !unameMap.value.has(novel.uid)) {
          getUname(novel.uid).then((res) => {
            const r: Result = res.data
            if (r.state === '1') unameMap.value.set(novel.uid!, r.data)
          })
        }
      })
    } else {
      ElMessage.error(result.message)
    }
  } finally {
    loaded.value = true
  }
})
</script>

<style lang="less" scoped>
.recommend-page {
  padding: 20px 0;
}

.page-title {
  font-size: 24px;
  font-weight: 800;
  margin: 0 0 24px;
  color: var(--ark-terracotta);
  letter-spacing: 1px;
}

.novel-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.novel-card {
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  border-radius: 14px;
  background: var(--ark-white);
  box-shadow:
    0 4px 16px rgba(196, 80, 40, 0.06),
    0 1px 3px rgba(42, 36, 32, 0.06);
  border: 1px solid rgba(196, 80, 40, 0.08);
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(90deg, var(--ark-terracotta), var(--ark-coral), var(--ark-coral-light));
    opacity: 0;
    transition: opacity 0.25s ease;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow:
      0 12px 32px rgba(196, 80, 40, 0.12),
      0 4px 12px rgba(42, 36, 32, 0.06);
    border-color: rgba(196, 80, 40, 0.15);

    &::before { opacity: 1; }
  }

  .card-body {
    .card-title {
      font-size: 16px;
      font-weight: 700;
      margin: 0 0 12px;
      color: var(--ark-ink);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .card-content {
      height: 120px;
      overflow: hidden;
      font-size: 13px;
      color: var(--ark-muted);
      line-height: 1.6;
    }
  }

  .card-footer {
    display: flex;
    align-items: center;
    gap: 14px;
    padding-top: 12px;
    margin-top: 4px;
    border-top: 1px dashed rgba(196, 80, 40, 0.12);
    font-size: 12px;
    color: var(--ark-faint);
  }
}
</style>
