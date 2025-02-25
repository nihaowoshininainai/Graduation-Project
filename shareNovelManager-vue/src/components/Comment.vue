<!-- 这是评论管理 -->
<template>
  <el-table :data="comments">
    <el-table-column prop="content" label="内容"/>
    <el-table-column prop="uid" label="用户id"/>
    <el-table-column>
        <template #default="scope">
            <el-button size="small" @click="passButton(scope.$index,scope.row)">通过</el-button>
            <el-button size="small" @click="nopassButton(scope.$index,scope.row)">不通过</el-button>
        </template>
    </el-table-column>
</el-table>  
</template>
<script setup lang="ts">
import { type Admin, type Comments, type Result } from '@/baseType';
import { getCommets, pass } from '@/request/comments';
import { ref } from 'vue';

const comments = ref<Comments[]>()
getCommets.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        comments.value = result.data
})

const passButton = (index:number, comment:Comments) => {
  ElMessageBox.prompt('请输入密码', '提示', {
      confirmButtonText: 'ok',
      cancelButtonText: 'cancel',
      inputPattern:
      /^\S+$/,
      inputErrorMessage: '密码不能为空',



  }
  ).then(({ value }) =>{
    const admin:Admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
    admin.pwd = value
    pass(admin,comment).then(res=>{
      const result = res.data
      if(result==='操作成功'){
        comments.value?.splice(index,1)
        ElMessage.success(result)
      }else{
        ElMessage.error(result)
      }
    })
  })
}

const nopassButton = (index:number, comment:Comments) => {
  ElMessageBox.prompt('请输入密码', '提示', {
    
    confirmButtonText:'ok',
    cancelButtonText: 'cancel',
    inputPattern:
    /^\S+$/,
    inputErrorMessage: '密码不能为空'
  }
  ).then(({ value }) =>{
    const admin:Admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
    admin.pwd = value
    pass(admin,comment).then(res=>{
      const result = res.data
      if(result==='操作成功'){
        comments.value?.splice(index,1)
        ElMessage.success(result)
      }else{
        ElMessage.error(result)
      }
    })
  })
}
</script>