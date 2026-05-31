<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-banner"></div>
      <div class="register-form-wrap">
        <h2 class="form-title">用户注册</h2>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="repeatPassword">
            <el-input v-model="form.repeatPassword" type="password" placeholder="请再次输入密码" :prefix-icon="Lock"
              @keydown.enter="handleRegister" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="register-btn" @click="handleRegister" :loading="loading">注册</el-button>
          </el-form-item>
        </el-form>
        <div class="form-footer">
          已有账号？<RouterLink to="/login">立即登录</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { register, getUid } from '@/request/user'
import router from '@/router'
import type { FormInstance, FormRules } from 'element-plus'
import type { Result, User } from '@/baseType'
import { User, Lock } from '@element-plus/icons-vue'

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  repeatPassword: '',
})

const rules = reactive<FormRules<typeof form>>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        const length = value.replace(/[^\x00-\xff]/g, 'xx').length
        if (length > 10) callback(new Error('用户名不能超过10个字符'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        const regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,12}$/
        if (!regex.test(value)) callback(new Error('密码必须包含字母和数字，长度8-12位'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
  repeatPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次输入密码不一致'))
        else callback()
      },
      trigger: 'blur',
    },
  ],
})

const handleRegister = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('请检查输入')
      return
    }
    loading.value = true
    try {
      const res = await register(form.username, form.password)
      const result: Result = res.data
      if (result.state === '1') {
        ElMessage.success({ message: result.message, duration: 1000 })
        router.push('/login')
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
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.register-card {
  display: flex;
  width: 700px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

  .register-banner {
    width: 45%;
    background-image: url('/imgs/1.png');
    background-size: cover;
    background-position: center;
  }

  .register-form-wrap {
    flex: 1;
    padding: 40px 32px;
    background: #fff;

    .form-title {
      text-align: center;
      margin: 0 0 24px;
      font-size: 22px;
      color: #303133;
    }

    .register-btn {
      width: 100%;
    }

    .form-footer {
      text-align: center;
      margin-top: 16px;
      font-size: 13px;
      color: #999;

      a {
        color: var(--el-color-primary);
        text-decoration: none;
      }
    }
  }
}
</style>
