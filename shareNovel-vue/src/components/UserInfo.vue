<template>
<el-descriptions>
<el-descriptions-item label="用户名">{{ user.uname }}</el-descriptions-item>
<el-descriptions-item label="用户id">{{ user.uid }}</el-descriptions-item>
<el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
<el-descriptions-item label="状态">
    <el-text :type="state==='正常'?'success':'danger'">{{ state }}</el-text>
</el-descriptions-item>
</el-descriptions>
</template>
<script setup lang="ts">
import type { Result, User } from '@/baseType';
import { getUserInfo } from '@/request/user';
import { ref, type Ref } from 'vue';

const props = defineProps<{
    uid: number
}>()

const user:Ref<User> = ref({
    uname:''
})

const state = ref("正常")

getUserInfo(props.uid).then(res=>{
    const result:Result = res.data
    user.value = result.data
    if(user.value.banned===0)
    {
        state.value = "正常"
    }
    else
    {
        state.value = "封禁"
    }
})
</script>