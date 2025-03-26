<!-- 登陆 -->
<template>
    <div
        style="display: flex;width: 30%;margin: 200px auto;border: 1px solid #ccc;border-radius: 10px;overflow: hidden;">
        <div class="left" style="width: 40%;background-image: url('/img/1.png');background-size: cover;"></div>
        <div class="login" style="width: 60%;text-align: center;padding: 20px;">
            <h1>登录</h1>
            <el-form ref="adminFormRef" :model="adminForm" :rules="rules" label-width="auto">
                <el-form-item label="用户名" prop="aname">
                    <el-input v-model="adminForm.aname"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="pwd">
                    <el-input v-model="adminForm.pwd" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="login(adminFormRef)">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script setup lang="ts">
import type { Result } from '@/baseType';
import { adminLogin } from '@/request/admin';
import type { FormInstance } from 'element-plus';
import { reactive, ref } from 'vue';

if (localStorage.getItem('admin'))
    location.href = '/#/'

const adminFormRef = ref<FormInstance>()

const adminForm = reactive({
    aname: '',
    pwd: ''
})
const rules = {
    aname: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
    ],
    pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const login = (adminFormRef: FormInstance | undefined) => {
    if (!adminFormRef)
        return
    adminFormRef.validate((values) => {
        if (values)
            adminLogin(adminForm).then((res) => {
                const result: Result = res.data
                if (import.meta.env.DEV) {
                    console.log(result.state);
                }
                if (result.state === '0')
                    ElMessage.error(result.message)
                else {
                    ElMessage.success({
                        message: result.message,
                        duration: 1000,
                        onClose: () => {
                            localStorage.setItem('admin', JSON.stringify(adminForm))
                            location.reload()
                        }
                    })
                }
            })
        else
            ElMessage.error('请输入完整信息')
    })
}

</script>