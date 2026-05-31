<template>
  <div class="shenhe-page">
    <h3>内容合规管理</h3>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待审核小说" name="shenhe">
        <el-table :data="shenheNovels" stripe>
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="uid" label="作者ID" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="success" size="small" @click="handlePass(scope.row)">通过</el-button>
              <el-button type="danger" size="small" @click="handleNoPass(scope.row)">不通过</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="被举报小说" name="jvbao">
        <el-table :data="jvbaoNovels" stripe>
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="uid" label="作者ID" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="success" size="small" @click="handleJvbaoPass(scope.row)">通过</el-button>
              <el-button type="danger" size="small" @click="handleJvbaoNoPass(scope.row)">下架</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="被举报评论" name="comments">
        <el-table :data="jvbaoComments" stripe>
          <el-table-column prop="content" label="内容" min-width="300" />
          <el-table-column prop="uid" label="用户ID" width="100" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="success" size="small" @click="handleCommentPass(scope.row)">通过</el-button>
              <el-button type="danger" size="small" @click="handleCommentNoPass(scope.row)">封禁</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="应封禁用户" name="ban">
        <el-table :data="banUsers" stripe>
          <el-table-column prop="uname" label="用户名" />
          <el-table-column prop="uid" label="用户ID" width="100" />
          <el-table-column prop="createTime" label="注册时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="danger" size="small" @click="handleBan(scope.row)">封禁</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="已封禁用户" name="banned">
        <el-table :data="bannedUsers" stripe>
          <el-table-column prop="uname" label="用户名" />
          <el-table-column prop="uid" label="用户ID" width="100" />
          <el-table-column prop="createTime" label="注册时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="success" size="small" @click="handleUnban(scope.row)">解封</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import type { Comments, Novel, Result, User } from '@/baseType'
import { getShenhe, getJvbao, auditNovel } from '@/request/novel'
import { getBanUsers, getBannedUsers, auditUser } from '@/request/user'
import { getReportedComments, auditComments } from '@/request/comments'
import { getStoredAdmin } from '@/request'
import { ref, onMounted } from 'vue'

const activeTab = ref('shenhe')
const shenheNovels = ref<Novel[]>([])
const jvbaoNovels = ref<Novel[]>([])
const jvbaoComments = ref<Comments[]>([])
const banUsers = ref<User[]>([])
const bannedUsers = ref<User[]>([])

const fetchAll = async () => {
  const [sRes, jRes, cRes, bRes, bdRes] = await Promise.all([
    getShenhe(), getJvbao(), getReportedComments(), getBanUsers(), getBannedUsers(),
  ])
  const sR: Result = sRes.data
  const jR: Result = jRes.data
  const cR: Result = cRes.data
  const bR: Result = bRes.data
  const bdR: Result = bdRes.data
  if (sR.state === '1') shenheNovels.value = sR.data || []
  if (jR.state === '1') jvbaoNovels.value = jR.data || []
  if (cR.state === '1') jvbaoComments.value = cR.data || []
  if (bR.state === '1') banUsers.value = bR.data || []
  if (bdR.state === '1') bannedUsers.value = bdR.data || []
}

const handlePass = async (novel: Novel) => {
  const admin = getStoredAdmin()
  const record = `${admin?.aname || '管理员'}审核通过题为"${novel.title}"的小说`
  novel.state = 1
  const res = await auditNovel(record, novel)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    shenheNovels.value = shenheNovels.value.filter(n => n.id !== novel.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleNoPass = async (novel: Novel) => {
  const admin = getStoredAdmin()
  const record = `${admin?.aname || '管理员'}审核不通过题为"${novel.title}"的小说`
  novel.state = -1
  const res = await auditNovel(record, novel)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    shenheNovels.value = shenheNovels.value.filter(n => n.id !== novel.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleJvbaoPass = async (novel: Novel) => {
  const admin = getStoredAdmin()
  const record = `${admin?.aname || '管理员'}审核通过被举报题为"${novel.title}"的小说`
  novel.state = 1
  const res = await auditNovel(record, novel)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    jvbaoNovels.value = jvbaoNovels.value.filter(n => n.id !== novel.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleJvbaoNoPass = async (novel: Novel) => {
  const admin = getStoredAdmin()
  const record = `${admin?.aname || '管理员'}审核不通过被举报题为"${novel.title}"的小说`
  novel.state = 3
  const res = await auditNovel(record, novel)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    jvbaoNovels.value = jvbaoNovels.value.filter(n => n.id !== novel.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleCommentPass = async (comment: Comments) => {
  comment.state = 0
  const res = await auditComments(comment)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    jvbaoComments.value = jvbaoComments.value.filter(c => c.id !== comment.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleCommentNoPass = async (comment: Comments) => {
  comment.state = -1
  const res = await auditComments(comment)
  const result: Result = res.data
  if (result.state === '1') {
    ElMessage.success(result.message)
    jvbaoComments.value = jvbaoComments.value.filter(c => c.id !== comment.id)
  } else {
    ElMessage.error(result.message)
  }
}

const handleBan = async (user: User) => {
  try {
    await ElMessageBox.confirm(`确定封禁用户 "${user.uname}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    user.banned = 1
    const res = await auditUser(user)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      banUsers.value = banUsers.value.filter(u => u.uid !== user.uid)
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

const handleUnban = async (user: User) => {
  try {
    await ElMessageBox.confirm(`确定解封用户 "${user.uname}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    })
    user.banned = 0
    const res = await auditUser(user)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      bannedUsers.value = bannedUsers.value.filter(u => u.uid !== user.uid)
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}

onMounted(() => {
  fetchAll()
})
</script>

<style lang="less" scoped>
.shenhe-page {
  h3 {
    margin: 0 0 16px;
    font-size: 18px;
  }
}
</style>
