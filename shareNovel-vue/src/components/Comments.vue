<template>
  <div class="comments-section">
    <h3 class="section-title">评论区</h3>
    <div class="comment-input" v-if="isLoggedIn()">
      <el-input
        type="textarea"
        v-model="inputText"
        placeholder="来点友好评论吧..."
        :rows="3"
        maxlength="500"
        show-word-limit
      />
      <el-button type="primary" :disabled="!inputText.trim()" @click="handleAddComment" style="margin-top: 8px;">
        发送评论
      </el-button>
    </div>
    <el-alert v-else type="info" :closable="false" style="margin-bottom: 16px;">
      <template #title>请先登录后参与评论</template>
    </el-alert>

    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <el-avatar :size="28" class="comment-avatar">{{ getUnameById(comment.uid)?.charAt(0) || '?' }}</el-avatar>
          <span class="comment-author">{{ getUnameById(comment.uid) || `用户${comment.uid}` }}</span>
          <span class="comment-time">{{ comment.createTime || '' }}</span>
        </div>
        <div class="comment-body">{{ comment.content }}</div>
        <div class="comment-actions">
          <span class="action-btn" @click="handleLikeComment(comment)">
            <el-icon :color="likeMap.get(comment.id!) ? '#e11d48' : '#999'">
              <StarFilled v-if="likeMap.get(comment.id!)" />
              <Star v-else />
            </el-icon>
            {{ likeMap.get(comment.id!) ? '已赞' : '点赞' }}
          </span>
          <span v-if="isOwnComment(comment)" class="action-btn danger" @click="handleDeleteComment(comment.id!)">
            删除
          </span>
        </div>
      </div>
      <el-empty v-if="comments.length === 0" description="暂无评论" />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Comments, Result } from '@/baseType'
import { getComments, addComment, delComments } from '@/request/comments'
import { addCommentLike, deleteCommentLike, getCommentLike } from '@/request/commentslike'
import { getUname as fetchUname } from '@/request/user'
import { isLoggedIn, getStoredUser } from '@/request'
import router from '@/router'
import { ref, onMounted } from 'vue'
import { Star, StarFilled } from '@element-plus/icons-vue'

const props = defineProps<{ id: number }>()

const inputText = ref('')
const comments = ref<Comments[]>([])
const likeMap = ref<Map<number, boolean>>(new Map())
const unameMap = ref<Map<number, string>>(new Map())

const getUnameById = (uid?: number) => {
  if (!uid) return ''
  return unameMap.value.get(uid)
}

const isOwnComment = (comment: Comments) => {
  const user = getStoredUser()
  return user?.uid === comment.uid
}

const fetchComments = async () => {
  const res = await getComments(props.id)
  const result: Result = res.data
  if (result.state === '1') {
    comments.value = result.data
    comments.value.forEach((comment) => {
      if (comment.uid && !unameMap.value.has(comment.uid)) {
        fetchUname(comment.uid).then((res) => {
          const r: Result = res.data
          if (r.state === '1') unameMap.value.set(comment.uid!, r.data)
        })
      }
    })
    if (isLoggedIn()) {
      const user = getStoredUser()
      if (user?.uid) {
        comments.value.forEach((comment) => {
          if (comment.id) {
            getCommentLike(comment.id, user.uid as number).then((res) => {
              likeMap.value.set(comment.id!, res.data)
            })
          }
        })
      }
    }
  }
}

const handleAddComment = async () => {
  if (!inputText.value.trim()) return
  const user = getStoredUser()
  if (!user?.uid) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  const res = await addComment({ nid: props.id, uid: user.uid, content: inputText.value.trim() })
  const result: Result = res.data
  if (result.state === '1') {
    inputText.value = ''
    ElMessage.success('评论成功')
    fetchComments()
  } else {
    ElMessage.error(result.message)
  }
}

const handleLikeComment = async (comment: Comments) => {
  if (!isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (!comment.id) return
  try {
    if (likeMap.value.get(comment.id)) {
      const res = await deleteCommentLike(comment.id)
      const result: Result = res.data
      if (result.state === '1') {
        likeMap.value.set(comment.id, false)
      }
    } else {
      const res = await addCommentLike(comment.id)
      const result: Result = res.data
      if (result.state === '1') {
        likeMap.value.set(comment.id, true)
      }
    }
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleDeleteComment = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定删除该评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    const res = await delComments(id)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      fetchComments()
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

onMounted(() => {
  fetchComments()
})
</script>

<style lang="less" scoped>
.comments-section {
  max-width: 800px;
  margin: 30px auto 0;
  padding: 24px;
  background: var(--ark-white);
  border-radius: 16px;
  border: 1px solid rgba(196, 80, 40, 0.08);
  box-shadow:
    0 8px 28px rgba(196, 80, 40, 0.06),
    0 2px 8px rgba(42, 36, 32, 0.04);
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 20px;
  color: var(--ark-terracotta);
  letter-spacing: 1px;
}

.comment-input {
  margin-bottom: 24px;
}

.comment-list {
  .comment-item {
    padding: 16px 0;
    border-bottom: 1px dashed rgba(196, 80, 40, 0.10);

    &:last-child {
      border-bottom: none;
    }

    .comment-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;

      .comment-avatar {
        background: linear-gradient(135deg, var(--ark-terracotta), var(--ark-coral));
        color: #fff;
        font-size: 12px;
        flex-shrink: 0;
      }

      .comment-author {
        font-size: 14px;
        font-weight: 600;
        color: var(--ark-brown);
      }

      .comment-time {
        font-size: 12px;
        color: var(--ark-faint);
        margin-left: auto;
      }
    }

    .comment-body {
      font-size: 14px;
      color: var(--ark-muted);
      line-height: 1.7;
      padding-left: 36px;
    }

    .comment-actions {
      display: flex;
      gap: 16px;
      padding-left: 36px;
      margin-top: 8px;

      .action-btn {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: var(--ark-faint);
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
}
</style>
