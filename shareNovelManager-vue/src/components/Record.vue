<!-- 审核记录 -->
 <template>
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="审核文章记录" name="审核文章记录">
            <RecordOne></RecordOne>
        </el-tab-pane>
        <el-tab-pane label="审核用户记录" name="审核用户记录">
            
        </el-tab-pane>
        <el-tab-pane label="审核评论记录" name="审核评论记录">
            
        </el-tab-pane>
    </el-tabs>
 </template>
 <script setup lang="ts">
import type { TabsPaneContext } from 'element-plus'
import { ref } from 'vue'

const activeName = ref('审核文章记录')
const name = ref('审核文章记录')

const handleClick = (tab: TabsPaneContext, event: Event) => {
    name.value = tab.paneName as string
}
</script>
