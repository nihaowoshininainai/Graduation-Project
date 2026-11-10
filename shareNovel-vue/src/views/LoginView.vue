<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-banner"></div>
      <div class="login-form-wrap">
        <h2 class="form-title">用户登录</h2>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
          <el-form-item label="用户名" prop="uname">
            <el-input v-model="form.uname" placeholder="请输入用户名" :prefix-icon="UserIcon" />
          </el-form-item>
          <el-form-item label="密码" prop="pwd">
            <el-input v-model="form.pwd" type="password" placeholder="请输入密码" :prefix-icon="Lock"
              @keydown.enter="handleLogin" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
          </el-form-item>
        </el-form>
        <div class="form-footer">
          还没有账号？<RouterLink to="/register">立即注册</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { login, getUid } from '@/request/user'
import { setAuth } from '@/request'
import router from '@/router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import type { Result, User } from '@/baseType'
import { User as UserIcon, Lock } from '@element-plus/icons-vue'

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  uname: '',
  pwd: '',
})

const rules = reactive<FormRules>({
  uname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

const handleLogin = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) {
      ElMessage.warning('请检查输入')
      return
    }
    loading.value = true
    try {
      const res = await login(form.uname, form.pwd)
      const result: Result = res.data
      if (result.state === '1') {
        const uidRes = await getUid(form.uname)
        const uidResult: Result = uidRes.data
        if (uidResult.state === '1') {
          const user: User = { uname: form.uname, uid: uidResult.data }
          setAuth(result.data, user)
          ElMessage.success({ message: result.message, duration: 1000 })
          router.push('/')
        }
      } else {
        ElMessage.warning(result.message)
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
  min-height: 72vh;
}

.login-card {
  display: flex;
  width: 700px;
  border-radius: 18px;
  overflow: hidden;
  box-shadow:
    0 16px 48px rgba(196, 80, 40, 0.12),
    0 4px 16px rgba(42, 36, 32, 0.06);
  border: 1px solid rgba(196, 80, 40, 0.10);

  .login-banner {
    width: 45%;
    background-image: url('/imgs/1.png');
    background-size: cover;
    background-position: center;
  }

  .login-form-wrap {
    flex: 1;
    padding: 40px 32px;
    background: var(--ark-white);

    .form-title {
      text-align: center;
      margin: 0 0 24px;
      font-size: 24px;
      font-weight: 800;
      color: var(--ark-terracotta);
      letter-spacing: 1px;
    }

    .login-btn {
      width: 100%;
      margin-top: 4px;
    }

    .form-footer {
      text-align: center;
      margin-top: 16px;
      font-size: 13px;
      color: var(--ark-muted);

      a {
        color: var(--ark-terracotta);
        text-decoration: none;
        font-weight: 600;
      }
    }
  }
}
</style>
