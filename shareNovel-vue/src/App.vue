<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import Header from '@/components/Header.vue'
import { isLoggedIn } from '@/request'

const isScrolled = ref(false)

if (!isLoggedIn()) {
  document.cookie = 'Token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;'
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 0
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="app-layout">
    <header :class="['app-header', { scrolled: isScrolled }]">
      <Header />
    </header>
    <main class="app-main">
      <RouterView />
    </main>
    <footer class="app-footer">
      <p>共享小说平台 - 仅供学习使用</p>
    </footer>
  </div>
</template>

<style lang="less" scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  z-index: 1000;
  background: transparent;
  transition: all 0.3s ease;
  box-shadow: none;

  &.scrolled {
    background: rgba(245, 234, 214, 0.92);
    backdrop-filter: blur(12px);
    box-shadow: 0 2px 20px rgba(196, 80, 40, 0.10);
  }
}

.app-main {
  flex: 1;
  width: 75%;
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 70px;
  padding-bottom: 40px;
}

.app-footer {
  text-align: center;
  padding: 22px 0;
  color: var(--ark-muted);
  font-size: 13px;
  border-top: 1px solid rgba(196, 80, 40, 0.12);
  letter-spacing: 1px;
}
</style>
