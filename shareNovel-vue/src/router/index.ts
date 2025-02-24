import type { Result, User } from '@/baseType'
import { getUid } from '@/request/user'
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
  },
  {
    path: '/user/:id',
    name: '用户界面',
    beforeEnter: (to, from, next) => {
      if (document.cookie.length === 0) {
        ElMessage.error({
          message: '请先登录',
          duration: 1000
        })
        next('/login')
      } else {
        const user: User = JSON.parse(localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER) as string)
        if (user.uid !== undefined)
          next()
        else {
          getUid(user.uname).then(res => {
            const result: Result = res.data
            user.uid = result.data
            localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_USER, JSON.stringify(user))
            next(`/user/${user.uid}`)
          }
          )
        }
      }
    },
    props: true,
    component: () => import('@/views/UserView.vue')
  },
  {
    path: '/novel/:id',
    name: '小说界面',
    props: true,
    component: () => import('@/views/NovelView.vue')
  }
  ],
})

export default router
