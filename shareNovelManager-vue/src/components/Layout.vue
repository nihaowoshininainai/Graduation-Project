<template>
  <el-container class="admin-layout">
    <el-header class="admin-header">
      <Header />
    </el-header>
    <el-container>
      <el-aside width="220px" class="admin-aside">
        <el-menu :default-active="activeMenu" router class="side-menu">
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>主页</span>
          </el-menu-item>
          <el-menu-item index="/admin">
            <el-icon><User /></el-icon>
            <span>管理员管理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/article">
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </el-menu-item>
          <el-menu-item index="/shenhe">
            <el-icon><ChatDotRound /></el-icon>
            <span>内容合规管理</span>
          </el-menu-item>
          <el-menu-item index="/record">
            <el-icon><Memo /></el-icon>
            <span>审核记录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="admin-main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { User, ChatDotRound, Document, House, Memo } from '@element-plus/icons-vue'
import Header from '@/components/Header.vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style lang="less" scoped>
.admin-layout {
  height: 100vh;
}

.admin-header {
  border-bottom: 1px solid #e6e6e6;
  padding: 0;
  height: 56px;
}

.admin-aside {
  border-right: 1px solid #e6e6e6;
  overflow-y: auto;

  .side-menu {
    height: 100%;
    border-right: none;
  }
}

.admin-main {
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
