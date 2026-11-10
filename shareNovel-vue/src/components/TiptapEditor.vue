<template>
  <div class="tiptap-editor">
    <div class="toolbar">
      <el-button
        size="small"
        :type="editor?.isActive('bold') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleBold().run()"
      >
        粗体
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('italic') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleItalic().run()"
      >
        斜体
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('strike') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleStrike().run()"
      >
        删除线
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('heading', { level: 1 }) ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleHeading({ level: 1 }).run()"
      >
        H1
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('heading', { level: 2 }) ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleHeading({ level: 2 }).run()"
      >
        H2
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('bulletList') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleBulletList().run()"
      >
        无序列表
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('orderedList') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleOrderedList().run()"
      >
        有序列表
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('blockquote') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleBlockquote().run()"
      >
        引用
      </el-button>
      <el-button
        size="small"
        :type="editor?.isActive('codeBlock') ? 'primary' : 'default'"
        @click="editor?.chain().focus().toggleCodeBlock().run()"
      >
        代码块
      </el-button>
      <el-button size="small" @click="editor?.chain().focus().undo().run()" :disabled="!editor?.can().undo()">
        撤销
      </el-button>
      <el-button size="small" @click="editor?.chain().focus().redo().run()" :disabled="!editor?.can().redo()">
        重做
      </el-button>
    </div>
    <EditorContent :editor="editor" class="editor-content" />
  </div>
</template>

<script setup lang="ts">
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Placeholder from '@tiptap/extension-placeholder'
import { watch } from 'vue'

const props = defineProps<{
  modelValue: string
  placeholder?: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const editor = useEditor({
  content: props.modelValue,
  extensions: [
    StarterKit,
    Placeholder.configure({
      placeholder: props.placeholder || '',
    }),
  ],
  onUpdate: ({ editor }) => {
    emit('update:modelValue', editor.getHTML())
  },
})

watch(() => props.modelValue, (value) => {
  if (editor.value && editor.value.getHTML() !== value) {
    editor.value.commands.setContent(value, false)
  }
})
</script>

<style lang="less" scoped>
.tiptap-editor {
  border: 1px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .toolbar {
    padding: 8px;
    border-bottom: 1px solid #ccc;
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    background-color: #f5f5f5;
  }

  .editor-content {
    flex: 1;
    overflow-y: auto;
    min-height: 500px;

    :deep(.tiptap) {
      min-height: 500px;
      padding: 16px;
      outline: none;

      p.is-editor-empty:first-child::before {
        color: #adb5bd;
        content: attr(data-placeholder);
        float: left;
        height: 0;
        pointer-events: none;
      }

      h1 {
        font-size: 2em;
        margin: 0.67em 0;
      }

      h2 {
        font-size: 1.5em;
        margin: 0.75em 0;
      }

      ul {
        list-style-type: disc;
        padding-left: 1.5em;
      }

      ol {
        list-style-type: decimal;
        padding-left: 1.5em;
      }

      blockquote {
        border-left: 4px solid #ccc;
        padding-left: 1em;
        margin: 1em 0;
        color: #666;
      }

      pre {
        background: #f4f4f4;
        padding: 1em;
        border-radius: 4px;
        overflow-x: auto;
      }
    }
  }
}
</style>
