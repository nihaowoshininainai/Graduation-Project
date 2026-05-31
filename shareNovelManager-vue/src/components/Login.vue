<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-banner"></div>
      <div class="login-form-wrap">
        <h2 class="form-title">管理员登录</h2>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
          <el-form-item label="管理员名" prop="aname">
            <el-input v-model="form.aname" placeholder="请输入管理员名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item label="密码" prop="pwd">
            <el-input v-model="form.pwd" type="password" placeholder="请输入密码" :prefix-icon="Lock"
              @keydown.enter="handleLogin" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { adminLogin } from '@/request/admin'
import type { FormInstance, FormRules } from 'element-plus'
import type { Result, Admin } from '@/baseType'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { isAdminLoggedIn } from '@/request'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

if (isAdminLoggedIn()) {
  router.push('/')
}

const form = reactive({ aname: '', pwd: '' })
const rules = reactive<FormRules>({
  aname: [{ required: true, message: '请输入管理员名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

const handleLogin = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('请输入完整信息')
      return
    }
    loading.value = true
    try {
      const res = await adminLogin(form)
      const result: Result = res.data
      if (result.state === '1') {
        document.cookie = `Token=${result.data};path=/`
        const admin: Admin = { aname: form.aname }
        localStorage.setItem('admin', JSON.stringify(admin))
        ElMessage.success({ message: result.message, duration: 1000 })
        router.push('/')
      } else {
        ElMessage.error(result.message)
      }
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="less" scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f0f2f5;
}

.login-card {
  display: flex;
  width: 600px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);

  .login-banner {
    width: 40%;
    background-image: url('/img/1.png');
    background-size: cover;
    background-position: center;
  }

  .login-form-wrap {
    flex: 1;
    padding: 40px 32px;
    background: #fff;

    .form-title {
      text-align: center;
      margin: 0 0 24px;
      font-size: 20px;
      color: #303133;
    }

    .login-btn {
      width: 100%;
    }
  }
}
</style>
