<!-- 用户管理 -->
 <template>
    <el-table :data="users">
        <el-table-column prop="uname" label="用户名" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="uid" label="用户id" />
    </el-table>
 </template>
 <script setup lang="ts">
import { type Result, type User } from '@/baseType';
import { getUsers } from '@/request/user';
import { ref } from 'vue';

const users = ref<User[]>()
getUsers.then((res) => {
    const result: Result = res.data
    if (result.state === '1')
        users.value = result.data
})
</script>