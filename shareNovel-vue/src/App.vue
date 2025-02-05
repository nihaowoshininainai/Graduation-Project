<script setup lang="ts">
import { RouterView } from 'vue-router';


</script>

<template>
    <div class="common-layout">
      <el-container>
        <el-header><Header></Header></el-header>
        <el-main><RouterView></RouterView></el-main>
        <el-footer>Footer</el-footer>
      </el-container>
    </div>
</template>
<style lang="less" scoped>
.el-header{
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
