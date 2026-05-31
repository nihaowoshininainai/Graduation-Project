<template>
  <div class="admin-header-bar">
    <div class="header-brand">
      <el-icon :size="20"><Setting /></el-icon>
      <span>小说管理后台</span>
    </div>
    <div class="header-right">
      <el-link :href="novelWebUrl" target="_blank" :underline="false" class="visit-link">
        前往前台网站
      </el-link>
      <template v-if="admin">
        <el-divider direction="vertical" />
        <span class="admin-name">管理员: {{ admin.aname }}</span>
        <el-button type="danger" size="small" text @click="handleLogout">退出登录</el-button>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Setting } from '@element-plus/icons-vue'
import { getStoredAdmin, clearAdminAuth } from '@/request'

const router = useRouter()
const novelWebUrl = import.meta.env.VITE_NOVEL_WEB_URL
const admin = computed(() => getStoredAdmin())

const handleLogout = () => {
  clearAdminAuth()
  router.push('/login')
}
</script>

<style lang="less" scoped>
.admin-header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 20px;

  .header-brand {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: var(--el-color-primary);
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;

    .visit-link {
      font-size: 13px;
    }

    .admin-name {
      color: #666;
    }
  }
}
</style>
