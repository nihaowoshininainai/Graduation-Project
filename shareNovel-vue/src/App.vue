<script setup lang="ts">
import { RouterView } from 'vue-router';
import { ref, onMounted, onUnmounted } from 'vue';
import Header from '@/components/Header.vue'; // 确保你已经导入 Header 组件

const isScrolled = ref(false);

if (localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) === null) {
  document.cookie += ";max-age=0;path=/"
}

if (document.cookie === "") {
  localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
}

const handleScroll = () => {
  isScrolled.value = window.scrollY > 0;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header :class="{ 'scrolled': isScrolled }">
        <Header></Header>
      </el-header>
      <el-main>
        <RouterView></RouterView>
      </el-main>
      <el-footer>
        <h1>本项目用于学习使用，若上传小说有侵权，请联系qq:2763588199</h1>
      </el-footer>
    </el-container>
  </div>
</template>
<style lang="less" scoped>
.common-layout {

  .el-header {
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: fixed;
    width: 100%;
    top: 0;
    left: 0;
    transition: background-color 0.3s;
    background-color: transparent;
    z-index: 1000;
  }

  .el-header.scrolled {
    background-color: white;
  }

  .el-main {
    width: 70%;
    margin: 20px auto;
    padding-top: 60px; // 确保内容不被固定的 header 遮挡
  }

  .el-footer{
    text-align: center;
  }
}
</style>