<!-- 用户信息 -->
<template>
    <el-descriptions border>
        <el-descriptions-item label="用户名">{{ user.uname }}</el-descriptions-item>
        <el-descriptions-item label="用户id">{{ user.uid }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
            <el-text :type="state === '正常' ? 'success' : 'danger'">{{ state }}</el-text>
        </el-descriptions-item>
    </el-descriptions>
    <el-button type="danger" @click="restePassword" v-if="flag">重设密码</el-button>
</template>
<script setup lang="ts">
import type { Result, User } from '@/baseType';
import { changePwd, getUserInfo } from '@/request/user';
import router from '@/router';
import { ref, type Ref } from 'vue';

const props = defineProps<{
    uid: number
}>()

const user: Ref<User> = ref({
    uname: ''
})

const state = ref("正常")

const flag = (props.uid === JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string).uid)

const restePassword = () => {
    ElMessageBox.prompt('请输入旧密码', '重设密码', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        inputType: 'password',
    }
    ).then(oldPwd => {
        ElMessageBox.prompt('请输入新密码', '重设密码', {
            cancelButtonText: '取消',
            confirmButtonText: '确定',
            inputType: 'password',
            inputPattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/,
            inputErrorMessage: '密码必须包含字母和数字，长度在8到12之间'
        }).then(newPwd => {
            if(import.meta.env.DEV)
            console.log(oldPwd, newPwd)
            changePwd(oldPwd.value, newPwd.value).then(res => {
                const result: Result = res.data
                if (result.state === '1') {
                    ElMessage.success({
                        message: result.message + '，请重新登录',
                        duration: 2000,
                        onClose: () => {
                            document.cookie += ';max-age=0'
                            localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
                            router.push('/login')
                        }
                    })
                }
                else {
                    ElMessage.error(result.message)
                }
            })
        })
    })
}

getUserInfo(props.uid).then(res => {
    const result: Result = res.data
    user.value = result.data
    if (user.value.banned === 0) {
        state.value = "正常"
    }
    else {
        state.value = "封禁"
    }
})

</script>