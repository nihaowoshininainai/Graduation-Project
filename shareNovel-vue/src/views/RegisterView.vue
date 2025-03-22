<!-- 这是注册界面 -->
<template>
    <div class="register">
        <div class="left"></div>
        <el-form ref="userFormRef" :model="user" :rules="rules" label-width="auto">
            <h2>注册</h2>
            <el-form-item label="用户名" prop="username">
                <el-input v-model="user.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="user.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="repeatPassword">
                <el-input type="password" v-model="user.repeatPassword" placeholder="请再次输入密码"
                    @keydown.enter="registerForm(userFormRef)"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="registerForm(userFormRef)">注册</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script setup lang="ts">
import { type FormRules, type FormInstance } from 'element-plus';
import { reactive, ref } from 'vue';
import { getUid, register } from '@/request/user';
import router from '@/router';
import type { Result, User } from '@/baseType';

const userFormRef = ref<FormInstance>()
const user = reactive({
    username: '',
    password: '',
    repeatPassword: ''
})
const rules = reactive<FormRules<{ username: string, password: string, repeatPassword: string }>>({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const length = value.replace(/[^\x00-\xff]/g, 'xx').length;
                if (length > 10) {
                    callback(new Error('用户名不能超过10个字符'));
                } else {
                    callback();
                }
            }, trigger: 'blur'
        }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,12}$/;
                if (!regex.test(value)) {
                    callback(new Error('密码必须包含并且只能包含字母和数字，长度在8-12个字符'));
                } else {
                    callback();
                }
            }, trigger: 'blur'
        }
    ],
    repeatPassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== user.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            }, trigger: 'blur'
        }
    ]
})
const registerForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            register(user.username, user.password).then(res => {
                const result: Result = res.data
                if (result.state === '1') {
                    ElMessage.success({
                        message: result.message,
                        duration: 1000,
                        onClose: () => {
                            document.cookie = `Token=${result.data};path=/`
                            const userself: User = {
                                uname: user.username
                            }
                            getUid(userself.uname).then(res => {
                                const result: Result = res.data
                                if (result.state === '1') {
                                    userself.uid = result.data
                                    window.localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_USER, JSON.stringify(userself))
                                    router.go(0)
                                }
                            })
                        }
                    })
                } else {
                    ElMessage.warning(result.message)
                }
            })
        } else {
            ElMessage.error('请检查输入')
        }
    })
}
</script>
<style lang="less" scoped>
.register {
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