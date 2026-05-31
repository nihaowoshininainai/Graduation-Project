<script setup lang="ts">
import type { Label, Novel, Result } from '@/baseType'
import { Condition, Sort } from '@/enmu'
import { getIndexNovel } from '@/request/novel'
import { getUname } from '@/request/user'
import router from '@/router'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { View, Clock, User as UserIcon } from '@element-plus/icons-vue'

const props = defineProps<{
  labels?: Label[]
  currentLabel?: number
  name?: string
}>()

const route = useRoute()
const currentLabel = ref(props.currentLabel ?? -1)
const pageCount = ref(0)
const current = ref(Number(route.params.page))
const novels = ref<Novel[]>([])
const unames = ref<Map<number, string>>(new Map())
const condition = ref<Condition>(Condition.hot)
const sort = ref<Sort>(Sort.desc)
const loading = ref(false)

const fetchData = (labelId?: number) => {
  loading.value = true
  getIndexNovel(
    current.value,
    Number(import.meta.env.VITE_PAGE_SIZE),
    Condition[condition.value],
    Sort[sort.value],
    labelId,
    props.name,
  ).then((res) => {
    const result: Result = res.data
    if (result.state === '1') {
      pageCount.value = result.data.pages
      novels.value = result.data.novels
      novels.value.forEach((novel) => {
        if (novel.uid && !unames.value.has(novel.uid)) {
          getUname(novel.uid).then((res) => {
            const r: Result = res.data
            if (r.state === '1') {
              unames.value.set(novel.uid!, r.data)
            }
          })
        }
      })
    }
  }).finally(() => {
    loading.value = false
  })
}

const replacePageRoute = (url: string, newPage: string | number): string => {
  return url.replace(/\/page\/(\d+)/, `/page/${newPage}`)
}

watch(current, (newV) => {
  router.push(replacePageRoute(route.fullPath, newV))
  fetchData(currentLabel.value)
})

const goToUser = (uid: number) => {
  router.push(`/user/${uid}`)
}

const goToNovel = (id: number) => {
  router.push(`/novel/${id}`)
}

const getNovelStateTag = (state?: number) => {
  switch (state) {
    case 0: return { text: '审核中', type: 'warning' }
    case 1: return { text: '', type: '' }
    case -1: return { text: '未通过', type: 'danger' }
    case 2: return { text: '被举报', type: 'danger' }
    case 3: return { text: '已下架', type: 'info' }
    default: return { text: '', type: '' }
  }
}

fetchData()
</script>

<template>
  <div class="novel-list-page">
    <div v-if="labels && labels.length" class="label-bar">
      <el-check-tag
        :checked="currentLabel === -1"
        @change="currentLabel = -1; fetchData()"
      >全部</el-check-tag>
      <el-check-tag
        v-for="label in labels"
        :key="label.id"
        :checked="currentLabel === label.id"
        @change="currentLabel = label.id; fetchData(label.id)"
      >{{ label.name }}</el-check-tag>
    </div>

    <div class="sort-bar">
      <div class="sort-left">
        <el-radio-group v-model="condition" size="small" @change="fetchData(currentLabel)">
          <el-radio-button :value="Condition.hot">按热度</el-radio-button>
          <el-radio-button :value="Condition.time">按时间</el-radio-button>
        </el-radio-group>
      </div>
      <div class="sort-right">
        <el-radio-group v-model="sort" size="small" @change="fetchData(currentLabel)">
          <el-radio-button :value="Sort.desc">降序</el-radio-button>
          <el-radio-button :value="Sort.asc">升序</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div v-if="loading" class="novel-grid">
      <el-card v-for="i in 3" :key="i" shadow="hover" class="novel-card">
        <el-skeleton animated :rows="4" />
      </el-card>
    </div>
    <div v-else class="novel-grid">
      <el-card
        v-for="novel in novels"
        :key="novel.id"
        shadow="hover"
        class="novel-card"
        @click="goToNovel(novel.id!)"
      >
        <div class="card-body">
          <h3 class="card-title">{{ novel.title }}</h3>
          <div class="card-content" v-html="novel.content"></div>
        </div>
        <div class="card-footer">
          <span class="card-meta">
            <el-icon><Clock /></el-icon>
            {{ novel.createTime }}
          </span>
          <span class="card-meta" @click.stop="goToUser(novel.uid!)">
            <el-icon><UserIcon /></el-icon>
            {{ unames.get(novel.uid!) || '...' }}
          </span>
          <span class="card-meta">
            <el-icon><View /></el-icon>
            {{ novel.views || 0 }}
          </span>
          <el-tag v-if="getNovelStateTag(novel.state).text" :type="getNovelStateTag(novel.state).type as any" size="small">
            {{ getNovelStateTag(novel.state).text }}
          </el-tag>
        </div>
      </el-card>
    </div>

    <div class="pagination-wrap">
      <el-pagination
        background
        layout="prev, pager, next"
        :page-count="pageCount"
        v-model:current-page="current"
      />
    </div>
  </div>
</template>

<style lang="less" scoped>
.novel-list-page {
  padding: 20px 0;
}

.label-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
}

.sort-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.novel-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.novel-card {
  cursor: pointer;
  transition: transform 0.2s;
  border-radius: 12px;

  &:hover {
    transform: translateY(-4px);
  }

  .card-body {
    .card-title {
      font-size: 16px;
      font-weight: 600;
      margin: 0 0 12px;
      color: #303133;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .card-content {
      height: 120px;
      overflow: hidden;
      font-size: 13px;
      color: #666;
      line-height: 1.6;
    }
  }

  .card-footer {
    display: flex;
    align-items: center;
    gap: 16px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;
    font-size: 12px;
    color: #999;

    .card-meta {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
}

.pagination-wrap {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>
