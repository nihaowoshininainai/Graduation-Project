import ClassifyView from '@/views/ClassifyView.vue'
import HomeView from '@/views/HomeView.vue'
import RecommendView from '@/views/RecommendView.vue'
import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [{
    path: '/',
    name: 'home',
    redirect: '/page/1'
  },
  {
    path: '/page/:page',
    name: 'index',
    component: HomeView
  },
  {
    path: '/classify/page/:page',
    name: '分类界面',
    component: ClassifyView
  },
  {
    path: '/recommend',
    name: '推荐界面',
    component: RecommendView
  },
  {
    path: '/login',
    name: '登录界面',
    beforeEnter: (to, from, next) => {
      if (import.meta.env.DEV) {
        console.log(to)
        console.log(from)
      }
      if (document.cookie.length > 0) {
        next('/')
      } else
        next()
    },
    component: () => import('@/views/LoginView.vue')
  },
  {
    path: '/register',
    name: '注册界面',
    beforeEnter: (to, from, next) => {
      if (document.cookie.length > 0) {
        next('/')
      } else
        next()
    },
    component: () => import('@/views/RegisterView.vue')
  }
  ],
})

export default router
