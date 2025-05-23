<!-- 这是文章管理 -->
<template>
  搜索框：
  <el-input style="width: 200px;" v-model="search" placeholder="请输入标题" />
  <el-table :data="filterNovels">
    <el-table-column prop="title" label="标题" />
    <el-table-column prop="createTime" label="创建时间" />
    <el-table-column prop="uid" label="用户id" />
    <el-table-column prop="state" label="状态">
      <template #default="{ row }">
        {{ map.get(row.state) }}
      </template>
    </el-table-column>
    <el-table-column>
      <template #default="scope">
          <el-button type="danger" @click="delNovel(scope.row)">删除</el-button>
          <el-button type="primary" @click="changePwd(scope.row)">查看小说</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script lang="ts" setup>
import { type Admin, type Novel, type Result } from '@/baseType';
import { deleteNovel, getAllNovels } from '@/request/novel';
import { computed, ref } from 'vue';

const search = ref('')

const filterNovels = computed(() => {
  return novels.value.filter((novel) => {
    if(search.value==='') return true
    return novel.title.includes(search.value)
  })
})

const novels = ref<Novel[]>([])
getAllNovels.then((res) => {
  const result:Result = res.data
  novels.value = result.data
})
const map = new Map<number, string>()
map.set(0, '待审核')
map.set(-1, '审核不通过')
map.set(1, '正常')
map.set(2,'被举报')
map.set(3,'举报核实，被管理员下架')

const delNovel = (novel:Novel)=>{

  ElMessageBox.prompt('请输入管理员密码', '提示', {
    cancelButtonText: '取消',
    confirmButtonText: '确定',
    inputType: 'password',
  }).then(({ value }) => {
    const admin = JSON.parse(localStorage.getItem('admin') as string) as Admin
    admin.pwd = value
    console.log(admin);
    
    deleteNovel(admin,novel).then((res)=>{
      ElMessage.warning(res.data)
    })
  })
}
</script>
