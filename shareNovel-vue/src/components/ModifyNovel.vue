<!-- 这是小说修改界面 -->
<template>
  <el-button type="primary" :disabled="tiltle === '' || valueHtml === ''" @click="uploadNovel">修改</el-button>
  <el-button type="danger" @click="drawer = true">添加标签</el-button>
  <el-tag type="primary" v-for="label in addLabels" :key="label.id" closable @close="removeLabel(label)">{{ label.name
    }}</el-tag>
  <el-input v-model="tiltle" placeholder="请输入标题"></el-input>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
    <Editor style="height: 500px; overflow-y: hidden;" v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode"
      @onCreated="handleCreated" />
  </div>
  <el-drawer v-model="drawer" title="I am the title" :with-header="false">
    <el-button v-for="label in labels" :key="label.id" :type="addLabels.includes(label) ? 'primary' : ''"
      @click="Labels(label)">{{ label.name }}</el-button>
  </el-drawer>
</template>
<script setup lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { onBeforeUnmount, ref, shallowRef, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { Label, Novel, NovelLabel, Result, User } from '@/baseType'
import { getLabels } from '@/request/label'
import { uploadUserNovel } from '@/request/novel'
import { uploadNovelLabels } from '@/request/novelLabel'

const props = defineProps<{
  novel: Novel
}>()

if (localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL) != null) {
}

const mode = 'default'
const editorRef = shallowRef()

const drawer = ref(false)

//标题
const tiltle = ref(props.novel.title)
// 内容 HTML
const valueHtml = ref(props.novel.content)

const labels = ref<Label[]>([])

const addLabels = ref<Label[]>([])

const toolbarConfig = {}
const editorConfig = { placeholder: '请输入内容...' }

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor: any) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

watch(drawer, (val) => {
  if (val)
    getLabels.then(res => {
      const result: Result = res.data
      labels.value = result.data
    })
})

function Labels(label: Label) {
  const index = addLabels.value.indexOf(label)
  if (index != -1) {
    addLabels.value.splice(index, 1)
  }
  else
    addLabels.value.push(label)
}

function removeLabel(label: Label) {
  const index = addLabels.value.indexOf(label)
  if (index != -1) {
    addLabels.value.splice(index, 1)
  }
}

function uploadNovel() {
  ElMessageBox.prompt('请输入密码', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    inputPattern: /\S/,
    inputErrorMessage: '密码不能为空',
    inputType: 'password'
  }).then(({ value }) => {
    const user: User = {
      uname: '',
      pwd: value
    }
    const novel: Novel = {
      title: tiltle.value,
      content: valueHtml.value
    }
    uploadUserNovel({
      user: user,
      novel: novel
    }).then(res => {
      if (res.state === '1') {
        tiltle.value = ''
        valueHtml.value = ''
        localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL)

        if (addLabels.value.length > 0) {
          const novelLabelList: NovelLabel[] = []
          addLabels.value.forEach((label) => {
            const novelLabel: NovelLabel = {
              nid: res.data,
              lid: label.id
            }
            novelLabelList.push(novelLabel)
          })
          uploadNovelLabels(novelLabelList).then(res => {
            const result: Result = res.data
            if (result.state === '1') {
              addLabels.value = []
              ElMessage.success(result.message)
            }
            else
              ElMessage.error(result.message)
          })
        }
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消上传'
    })
  })
}

function storeNovel() {
  const novel: Novel = {
    title: tiltle.value,
    content: valueHtml.value
  }
  localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL, JSON.stringify(novel))
}
</script>