<!-- 顶栏 -->
 <template>
    <el-row>
        <el-col :span="8">
            小说管理网站
        </el-col>
        <el-col :span="8">
            <el-link :href="href">去看看小说休息一下吧</el-link>
        </el-col>

        <el-col :span="4" v-if="adminJsonStr!==null">
            欢迎管理员{{ admin?.aname }}
        </el-col>
        <el-col :span="4" v-if="adminJsonStr!==null" style="cursor: pointer;" @click="loginOut">退出登陆</el-col>
        <el-col :span="8" style="cursor: pointer;" @click="goToLogin" v-if="adminJsonStr===null">
            登陆
        </el-col>
    </el-row>
 </template>
 <script setup lang="ts">
import type { Admin } from '@/baseType';
import { ref } from 'vue';

 const href = import.meta.env.VITE_NOVEL_WEB_URL
 const adminJsonStr = ref(localStorage.getItem('admin'))
 const admin = ref<Admin>()
 if(adminJsonStr.value){
     admin.value = JSON.parse(adminJsonStr.value)
 }

 function loginOut(){
     localStorage.removeItem('admin')
     adminJsonStr.value = null
     admin.value = undefined
 }
 function goToLogin(){
     window.location.href = '#/login'
 }
</script>
<style scoped>
.el-row{
    height: 100%;
}
.el-col{
    text-align: center;
    line-height: 55px;
}
</style>