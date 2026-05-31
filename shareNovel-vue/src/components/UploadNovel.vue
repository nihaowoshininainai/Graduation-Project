<template>
  <div class="upload-novel">
    <div class="toolbar-row">
      <el-button :disabled="!title || !valueHtml" @click="storeDraft">暂存草稿</el-button>
      <el-button type="primary" :disabled="!title || !valueHtml" @click="handleUpload">发布小说</el-button>
      <el-button type="success" @click="drawer = true">添加标签</el-button>
      <el-tag v-for="label in selectedLabels" :key="label.id" closable @close="removeLabel(label)" class="label-tag">
        {{ label.name }}
      </el-tag>
    </div>

    <el-input v-model="title" placeholder="请输入小说标题" size="large" class="title-input" />

    <div class="editor-wrapper">
      <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
      <Editor style="height: 500px; overflow-y: hidden;" v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode"
        @onCreated="handleCreated" />
    </div>

    <el-drawer v-model="drawer" title="选择标签" size="300px">
      <div class="label-grid">
        <el-check-tag
          v-for="label in allLabels"
          :key="label.id"
          :checked="selectedLabels.some(l => l.id === label.id)"
          @change="toggleLabel(label)"
        >{{ label.name }}</el-check-tag>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, ref, shallowRef, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { Label, Novel, NovelLabel, Result, User } from '@/baseType'
import { getLabels } from '@/request/label'
import { uploadUserNovel } from '@/request/novel'
import { uploadNovelLabels } from '@/request/novelLabel'
import { getStoredUser } from '@/request'

const mode = 'default'
const editorRef = shallowRef()
const drawer = ref(false)
const title = ref('')
const valueHtml = ref('')
const allLabels = ref<Label[]>([])
const selectedLabels = ref<Label[]>([])
const toolbarConfig = {}
const editorConfig = { placeholder: '请输入小说内容...' }

const savedNovel = localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL)
if (savedNovel) {
  try {
    const novel: Novel = JSON.parse(savedNovel)
    ElMessageBox.confirm('检测到未完成的草稿，是否继续编辑？', '恢复草稿', {
      confirmButtonText: '继续编辑',
      cancelButtonText: '放弃草稿',
      type: 'info',
    }).then(() => {
      title.value = novel.title
      valueHtml.value = novel.content
    }).catch(() => {
      localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL)
    })
  } catch {
    localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL)
  }
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor: any) => {
  editorRef.value = editor
}

watch(drawer, async (val) => {
  if (val && allLabels.value.length === 0) {
    const res = await getLabels()
    const result: Result = res.data
    if (result.state === '1') {
      allLabels.value = result.data
    }
  }
})

const toggleLabel = (label: Label) => {
  const idx = selectedLabels.value.findIndex(l => l.id === label.id)
  if (idx !== -1) {
    selectedLabels.value.splice(idx, 1)
  } else {
    selectedLabels.value.push(label)
  }
}

const removeLabel = (label: Label) => {
  const idx = selectedLabels.value.findIndex(l => l.id === label.id)
  if (idx !== -1) selectedLabels.value.splice(idx, 1)
}

const storeDraft = () => {
  const novel: Novel = { title: title.value, content: valueHtml.value }
  localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL, JSON.stringify(novel))
  ElMessage.success('草稿已保存')
}

const handleUpload = () => {
  ElMessageBox.prompt('请输入密码确认发布', '发布确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
    inputPattern: /\S/,
    inputErrorMessage: '密码不能为空',
  }).then(async ({ value: pwd }) => {
    const user: User = { uname: '', pwd }
    const novel: Novel = { title: title.value, content: valueHtml.value }
    const res = await uploadUserNovel({ user, novel })
    const result: Result = res.data
    if (result.state === '1') {
      ElMessage.success(result.message)
      title.value = ''
      valueHtml.value = ''
      localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_NOVEL)

      if (selectedLabels.value.length > 0) {
        const novelLabelList: NovelLabel[] = selectedLabels.value.map(label => ({
          nid: result.data,
          lid: label.id,
        }))
        const labelRes = await uploadNovelLabels(novelLabelList)
        const labelResult: Result = labelRes.data
        if (labelResult.state === '1') {
          selectedLabels.value = []
          ElMessage.success(labelResult.message)
        } else {
          ElMessage.error(labelResult.message)
        }
      }
    } else {
      ElMessage.error(result.message)
    }
  }).catch(() => {
    ElMessage.info('已取消发布')
  })
}
</script>

<style lang="less" scoped>
.upload-novel {
  .toolbar-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 16px;
    flex-wrap: wrap;

    .label-tag {
      margin-left: 4px;
    }
  }

  .title-input {
    margin-bottom: 16px;
  }

  .editor-wrapper {
    border: 1px solid #ccc;
    border-radius: 8px;
    overflow: hidden;
  }
}

.label-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
