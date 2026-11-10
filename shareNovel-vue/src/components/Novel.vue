<template>
  <div class="novel-detail" v-if="novel">
    <div class="novel-header">
      <h1 class="novel-title">{{ novel.title }}</h1>
      <div class="novel-meta">
        <span class="meta-item" @click="router.push(`/user/${novel.uid}`)">
          <el-icon><UserIcon /></el-icon>
          {{ uname }}
        </span>
        <span class="meta-item">
          <el-icon><Clock /></el-icon>
          {{ novel.createTime }}
        </span>
        <span class="meta-item">
          <el-icon><View /></el-icon>
          {{ novel.views || 0 }} 次浏览
        </span>
        <span class="meta-action" @click="handleLike">
          <el-icon :size="18" :color="isCollected ? '#e11d48' : '#999'">
            <StarFilled v-if="isCollected" />
            <Star v-else />
          </el-icon>
          {{ isCollected ? '已收藏' : '收藏' }}
        </span>
        <span class="meta-action danger" @click="handleReport">
          <el-icon><Warning /></el-icon>
          举报
        </span>
      </div>
    </div>
    <el-divider />
    <div class="novel-content" v-html="novel.content"></div>
  </div>
  <el-skeleton v-else :rows="10" animated />
</template>

<script setup lang="ts">
import type { Novel, Result } from '@/baseType'
import { ifLike, like, delLike } from '@/request/collection'
import { addView, getView } from '@/request/hotdata'
import { getNovelDetail, reportNovel } from '@/request/novel'
import { getUname } from '@/request/user'
import { isLoggedIn, getStoredUser } from '@/request'
import router from '@/router'
import { ref, onMounted } from 'vue'
import { User as UserIcon, Clock, View, Star, StarFilled, Warning } from '@element-plus/icons-vue'

const props = defineProps<{ nid: number }>()

const novel = ref<Novel>()
const uname = ref('')
const isCollected = ref(false)

const viewedNovels: number[] = (() => {
  const raw = localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL_VIEW)
  return raw ? JSON.parse(raw) : []
})()

onMounted(async () => {
  if (!viewedNovels.includes(props.nid)) {
    await addView(props.nid)
    viewedNovels.push(props.nid)
    localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL_VIEW, JSON.stringify(viewedNovels))
  }

  const res = await getNovelDetail(props.nid)
  const result: Result = res.data
  if (result.state === '1') {
    novel.value = result.data
    if (novel.value?.state === -1 || novel.value?.state === 3) {
      ElMessage.error({ message: '该小说已下架', duration: 2000, onClose: () => router.push('/') })
      return
    }
    const uid = novel.value?.uid
    if (uid) {
      getUname(uid).then((res) => {
        const r: Result = res.data
        if (r.state === '1') uname.value = r.data
      })
    }
    getView(props.nid).then((res) => {
      novel.value!.views = res.data
    })
  } else {
    ElMessage.error({ message: '获取小说失败', duration: 2000, onClose: () => router.push('/') })
  }

  if (isLoggedIn()) {
    const user = getStoredUser()
    if (user?.uid) {
      const likeRes = await ifLike(user.uid, props.nid)
      const likeResult: Result = likeRes.data
      if (likeResult.state === '1') {
        isCollected.value = likeResult.data
      }
    }
  }
})

const handleLike = async () => {
  if (!isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    if (isCollected.value) {
      const res = await delLike(props.nid)
      const result: Result = res.data
      if (result.state === '1') {
        isCollected.value = false
        ElMessage.success(result.message)
      } else {
        ElMessage.error(result.message)
      }
    } else {
      const res = await like(props.nid)
      const result: Result = res.data
      if (result.state === '1') {
        isCollected.value = true
        ElMessage.success(result.message)
      } else {
        ElMessage.error(result.message)
      }
    }
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleReport = async () => {
  if (!isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    await ElMessageBox.confirm('确定要举报该小说吗？', '举报确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await reportNovel(props.nid)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}
</script>

<style lang="less" scoped>
.novel-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 28px;
  background: var(--ark-white);
  border-radius: 16px;
  border: 1px solid rgba(196, 80, 40, 0.08);
  box-shadow:
    0 8px 28px rgba(196, 80, 40, 0.06),
    0 2px 8px rgba(42, 36, 32, 0.04);
}

.novel-header {
  .novel-title {
    font-size: 28px;
    font-weight: 800;
    color: var(--ark-ink);
    margin: 0 0 16px;
    line-height: 1.3;
  }

  .novel-meta {
    display: flex;
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
    font-size: 14px;
    color: var(--ark-muted);

    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
      cursor: default;

      &:first-child {
        cursor: pointer;
        color: var(--ark-terracotta);
        font-weight: 600;

        &:hover {
          text-decoration: underline;
        }
      }
    }

    .meta-action {
      display: flex;
      align-items: center;
      gap: 4px;
      cursor: pointer;
      transition: color 0.2s;

      &:hover {
        color: var(--ark-terracotta);
      }

      &.danger:hover {
        color: var(--el-color-danger);
      }
    }
  }
}

.novel-content {
  line-height: 1.9;
  font-size: 16px;
  color: var(--ark-ink);
  word-break: break-word;

  :deep(img) {
    max-width: 100%;
    border-radius: 10px;
  }
}
</style>
