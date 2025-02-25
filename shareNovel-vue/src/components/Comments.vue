<!-- 评论部分 -->
<template>
    <el-row style="width: 100%;">
        <el-col style="justify-content: left;">评论区</el-col>
        <el-col>
            <el-input type="textarea" v-model="input" placeholder="来点友好评论吧" @keydown.enter="addCom" />
            <el-button type="primary" @click="addCom" :disabled="input === '' ? true : false">发送</el-button>
        </el-col>
        <el-col style="justify-content: left;" v-for="comment in comments" :key="comment.id">
            <el-row style="width: 100%;border: 1px solid #ccc;margin-top: 10px;padding: 10px;">
                <el-col style="justify-content: left;">{{ comment.uid }}</el-col>
                <el-col style="justify-content: left;">{{ comment.content }}</el-col>
                <el-col>
                    <svg v-show="!ifLike.get(comment.id as number)" @click="addLike(comment.id as number)"
                        xmlns="http://www.w3.org/2000/svg" width="21" height="21"
                        viewBox="0 0 14 14">-show="getCommentLike(comment.id as number,user.uid as number)"
                        xmlns="http://www.w3.org/2000/svg" width="21" height="21" viewBox="0 0 14 14">
                        <path fill="#999999" fill-rule="evenodd"
                            d="M4.41 12.961a2.5 2.5 0 0 0 1.076.244h5.346a2.5 2.5 0 0 0 2.47-2.114l.626-4.003a2 2 0 0 0-1.976-2.31H8.67V2.422a1.625 1.625 0 0 0-3.044-.794l-2.077 3.71a1.5 1.5 0 0 0-.191.733v5.442a1.5 1.5 0 0 0 .854 1.354l.2.095Zm-3.366-7.44a.996.996 0 0 0-.997.996v5.112a.997.997 0 0 0 .997.997h.496a.5.5 0 0 0 .5-.5V6.02a.5.5 0 0 0-.5-.5h-.496Z"
                            clip-rule="evenodd" />
                    </svg>
                    <svg v-show="ifLike.get(comment.id as number)" @click="delLike(comment.id as number)"
                        xmlns="http://www.w3.org/2000/svg" width="21" height="21" viewBox="0 0 14 14">
                        <path fill="#e11d48" fill-rule="evenodd"
                            d="M4.41 12.961a2.5 2.5 0 0 0 1.076.244h5.346a2.5 2.5 0 0 0 2.47-2.114l.626-4.003a2 2 0 0 0-1.976-2.31H8.67V2.422a1.625 1.625 0 0 0-3.044-.794l-2.077 3.71a1.5 1.5 0 0 0-.191.733v5.442a1.5 1.5 0 0 0 .854 1.354l.2.095Zm-3.366-7.44a.996.996 0 0 0-.997.996v5.112a.997.997 0 0 0 .997.997h.496a.5.5 0 0 0 .5-.5V6.02a.5.5 0 0 0-.5-.5h-.496Z"
                            clip-rule="evenodd" />
                    </svg>
                    <el-button link size="small" @click="reportButton(comment)">举报</el-button>
                </el-col>
            </el-row>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
import type { Comments, Result, User } from '@/baseType';
import { addComment, getComments, reportComments } from '@/request/comments';
import { addCommentLike, deleteCommentLike, getCommentLike } from '@/request/commentslike';
import router from '@/router';
import { computed, onBeforeUpdate, onMounted, onUpdated, ref, watch } from 'vue';

const props = defineProps<{
    id: number
}>()
const input = ref('')
const comments = ref<Comments[]>([])

const ifLike = ref<Map<number, boolean>>(new Map())

const userExist = localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER)
const user: User = JSON.parse(userExist as string)
const aComments = ref<Comments>({
    nid: props.id,
    uid: -1,
    content: ''
})

const getLike = (cid: number, uid: number) => {
    getCommentLike(cid, uid).then(res => {
        const result = res.data
        if (import.meta.env.DEV)
            console.log(ifLike.value)
        ifLike.value.set(cid, result)
    })
}

const getCom = () => {
    getComments(props.id).then(res => {
        const result: Result = res.data
        if (result.state === '1') {
            comments.value.push(...result.data)
            comments.value.forEach((comment) => {
                if (userExist)
                    getLike(comment.id as number, user.uid as number)
            })
        }
    })
}

const addCom = () => {
    if (!userExist)
        ElMessage.error({
            message: '请先登录',
            duration: 1000,
            onClose: () => {
                router.push('/login')
            }
        })
    else {
        aComments.value.content = input.value
        aComments.value.uid = user.uid as number
        addComment(aComments.value).then(res => {
            const result: Result = res.data
            if (result.state === '0')
                ElMessage.error({
                    message: result.message,
                    duration: 1000,
                    onClose: () => {
                        localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
                        document.cookie += ";max-age=0"
                        router.push('/login')
                    }
                })
            else {
                input.value = ''
                getCom()
            }
        }
        )
    }
}

const addLike = (cid: number) => {
    if (userExist) {
        addCommentLike(cid).then(res => {
            const result: Result = res.data
            if (result.state === '1')
                ifLike.value.set(cid, true)
            else
                ElMessage.error(result.message)
        })
    } else
        ElMessage.error({
            message: '请先登录',
            duration: 1000,
            onClose: () => {
                router.push('/login')
            }
        })
}

const delLike = (cid: number) => {
    deleteCommentLike(cid).then(res => {
        const result: Result = res.data
        if (result.state === '1')
            ifLike.value.set(cid, false)
    }
    )
}

const reportButton = (comments:Comments)=>{
    reportComments(comments).then(res=>{
        const result:Result=res.data
        if(result.state==='1')
            ElMessage.success(result.message)
        else
            ElMessage.error(result.message)
    })
}
getCom()
</script>