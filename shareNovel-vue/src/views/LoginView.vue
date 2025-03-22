<!-- 这是登陆界面 -->
<template>
    <div class="login">
        <div class="left"></div>
        <el-form ref="userFormRef" :model="user" :rules="rules" label-width="auto">
            <h2>用户登录</h2>
            <el-form-item label="用户名" prop="uname">
                <el-input v-model="user.uname" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="pwd">
                <el-input v-model="user.pwd" type="password" placeholder="请输入密码"
                    @keydown.enter="loginForm(userFormRef)"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="loginForm(userFormRef)">登录</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';
import { getUid, login } from '@/request/user';
import router from '@/router';
import type { FormInstance, FormRules } from 'element-plus';
import type { Result, User } from '@/baseType';

const userFormRef = ref<FormInstance>()
const user = ref<User>({
    uname: '',
    pwd: ''
})
const rules = reactive<FormRules<User>>({
    uname: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    pwd: [
        { required: true, message: '请输入密码', trigger: 'blur' }
    ]
})
const loginForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(valid => {
        if (valid) {
            login(user.value.uname, user.value.pwd ? user.value.pwd : '').then(res => {
                const result: Result = res.data
                if (import.meta.env.DEV) console.log(result)
                if (result.state === '1') {
                    document.cookie = `Token=${result.data};path=/`
                    ElMessage.success({
                        message: result.message,
                        duration: 1000,
                        onClose: () => {
                            getUid(user.value.uname).then(res => {
                                const result: Result = res.data
                                if (result.state === '1') {
                                    user.value.uid = result.data
                                    user.value.pwd = ''
                                    window.localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_USER, JSON.stringify(user.value))
                                    router.go(0)
                                }

                            }
                            )
                        }
                    })
                } else {
                    ElMessage.warning(result.message)
                }
            })
        } else
            ElMessage.warning('请检查输入')
    })
}
</script>
<style lang="less" scoped>
.login{
    width: 70%;
    margin: 200px auto;
    border-radius: 10px;
    border-style: solid;
    border-width: 1px;
    height: 400px;
    display: flex;
    overflow: hidden;
    .left{
        width: 40%;
        background-image: url('/imgs/1.png');
        background-size: cover;
        border-right: 1px solid;
    }
    .el-form{
        text-align: center;
        padding: 10px;
        margin: 60px auto;

    }
}
</style>
