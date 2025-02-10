import ClassifyView from '@/views/ClassifyView.vue'
import HomeView from '@/views/HomeView.vue'
import RecommendView from '@/views/RecommendView.vue'
import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [{
    path:'/',
    name:'home',
    redirect:'/page/1'
  },
  {
    path:'/page/:page',
    name:'index',
    component:HomeView
  },
  {
    path:'/classify/page/:page',
    name:'分类界面',
    component:ClassifyView
  },
  {
    path:'/recommend',
    name:'推荐界面',
    component:RecommendView
  }
  ],
})

export default router
