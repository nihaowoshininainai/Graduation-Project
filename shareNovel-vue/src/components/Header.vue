<template>
  <div class="nav-bar">
    <div class="nav-brand" @click="router.push('/')">
      <el-icon :size="22"><Reading /></el-icon>
      <span class="brand-text">共享小说</span>
    </div>
    <div class="nav-links">
      <RouterLink to="/" class="nav-link">首页</RouterLink>
      <RouterLink to="/classify/page/1" class="nav-link">分类</RouterLink>
      <RouterLink to="/recommend" class="nav-link">推荐</RouterLink>
    </div>
    <div class="nav-search">
      <el-input
        v-model="searchInput"
        placeholder="搜索小说..."
        size="default"
        :prefix-icon="Search"
        clearable
        @keydown.enter="handleSearch"
      />
    </div>
    <div class="nav-user">
      <template v-if="loggedIn">
        <el-dropdown trigger="click">
          <span class="user-dropdown">
            <el-avatar :size="32" class="user-avatar">{{ user?.uname?.charAt(0) }}</el-avatar>
            <span class="user-name">{{ user?.uname }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push(`/user/${user?.uid}`)">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" size="default" @click="router.push('/login')">登录</el-button>
        <el-button size="default" @click="router.push('/register')">注册</el-button>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Search, Reading } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getStoredUser, isLoggedIn, clearAuth } from '@/request'

const router = useRouter()
const searchInput = ref('')
const loggedIn = ref(isLoggedIn())
const user = ref(getStoredUser())

watch(() => router.currentRoute.value, () => {
  loggedIn.value = isLoggedIn()
  user.value = getStoredUser()
})

function handleSearch() {
  if (searchInput.value.trim()) {
    router.push(`/search/1?name=${encodeURIComponent(searchInput.value.trim())}`)
    searchInput.value = ''
  }
}

async function handleLogout() {
  clearAuth()
  loggedIn.value = false
  user.value = null
  await router.push('/')
}
</script>

<style lang="less" scoped>
.nav-bar {
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 24px;
  gap: 24px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--el-color-primary);
  flex-shrink: 0;

  .brand-text {
    font-size: 18px;
    font-weight: 700;
    letter-spacing: 1px;
  }
}

.nav-links {
  display: flex;
  gap: 8px;
  flex-shrink: 0;

  .nav-link {
    padding: 6px 16px;
    border-radius: 20px;
    font-size: 14px;
    color: #555;
    text-decoration: none;
    transition: all 0.2s;

    &:hover,
    &.router-link-active {
      color: var(--el-color-primary);
      background: rgba(64, 158, 255, 0.08);
    }
  }
}

.nav-search {
  flex: 1;
  max-width: 360px;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;

  .user-dropdown {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;

    .user-avatar {
      background: var(--el-color-primary);
      color: #fff;
      font-size: 14px;
    }

    .user-name {
      font-size: 14px;
      color: #333;
    }
  }
}
</style>
