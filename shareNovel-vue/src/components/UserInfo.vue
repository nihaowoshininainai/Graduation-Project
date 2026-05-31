<template>
  <div class="user-info-card">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="用户名">{{ user.uname }}</el-descriptions-item>
      <el-descriptions-item label="用户ID">{{ user.uid }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="user.banned === 0 ? 'success' : 'danger'" size="small">
          {{ user.banned === 0 ? '正常' : '封禁' }}
        </el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <div class="user-actions" v-if="isSelf">
      <el-button type="warning" @click="handleChangePwd">修改密码</el-button>
      <el-button type="danger" @click="handleDeleteAccount">注销账号</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Result, User } from '@/baseType'
import { changePwd, deleteUser, getUserInfo } from '@/request/user'
import { clearAuth, getStoredUser } from '@/request'
import router from '@/router'
import { ref, onMounted } from 'vue'

const props = defineProps<{ uid: number }>()

const user = ref<User>({ uname: '' })
const isSelf = ref(false)

onMounted(async () => {
  const stored = getStoredUser()
  isSelf.value = stored?.uid === props.uid

  const res = await getUserInfo(props.uid)
  const result: Result = res.data
  if (result.state === '1') {
    user.value = result.data
  }
})

const handleChangePwd = () => {
  ElMessageBox.prompt('请输入旧密码', '修改密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
  }).then(({ value: oldPwd }) => {
    ElMessageBox.prompt('请输入新密码', '修改密码', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/,
      inputErrorMessage: '密码必须包含字母和数字，长度8-12位',
    }).then(async ({ value: newPwd }) => {
      const res = await changePwd(oldPwd, newPwd)
      const result: Result = res.data
      if (result.state === '1') {
        ElMessage.success({
          message: result.message + '，请重新登录',
          duration: 2000,
          onClose: () => {
            clearAuth()
            router.push('/login')
          },
        })
      } else {
        ElMessage.error(result.message)
      }
    })
  }).catch(() => {})
}

const handleDeleteAccount = async () => {
  try {
    await ElMessageBox.confirm('确定要注销账号吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定注销',
      cancelButtonText: '取消',
      type: 'error',
    })
    const { value: pwd } = await ElMessageBox.prompt('请输入密码确认', '注销确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPattern: /\S/,
      inputErrorMessage: '密码不能为空',
    })
    const res = await deleteUser(pwd)
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success({
        message: '账号已注销',
        duration: 1500,
        onClose: () => {
          clearAuth()
          router.push('/')
        },
      })
    } else {
      ElMessage.error(result.message)
    }
  } catch {
    // cancelled
  }
}
</script>

<style lang="less" scoped>
.user-info-card {
  .user-actions {
    margin-top: 16px;
    display: flex;
    gap: 12px;
  }
}
</style>
