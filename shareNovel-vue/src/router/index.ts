import type { Result, User } from '@/baseType'
import { getUid } from '@/request/user'
import { isLoggedIn, getStoredUser, clearAuth } from '@/request'
import ClassifyView from '@/views/ClassifyView.vue'
import HomeView from '@/views/HomeView.vue'
import RecommendView from '@/views/RecommendView.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/page/1',
    },
    {
      path: '/page/:page',
      name: 'index',
      component: HomeView,
    },
    {
      path: '/classify/page/:page',
      name: 'classify',
      component: ClassifyView,
    },
    {
      path: '/recommend',
      name: 'recommend',
      component: RecommendView,
    },
    {
      path: '/login',
      name: 'login',
      beforeEnter: (_to, _from, next) => {
        if (isLoggedIn()) {
          next('/')
        } else {
          next()
        }
      },
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/register',
      name: 'register',
      beforeEnter: (_to, _from, next) => {
        if (isLoggedIn()) {
          next('/')
        } else {
          next()
        }
      },
      component: () => import('@/views/RegisterView.vue'),
    },
    {
      path: '/user/:id',
      name: 'user',
      beforeEnter: async (_to, _from, next) => {
        if (!isLoggedIn()) {
          ElMessage.error({ message: '请先登录', duration: 1000 })
          next('/login')
          return
        }
        const user = getStoredUser()
        if (!user) {
          clearAuth()
          next('/login')
          return
        }
        if (user.uid !== undefined) {
          next()
        } else {
          try {
            const res = await getUid(user.uname)
            const result: Result = res.data
            if (result.state === '1') {
              user.uid = result.data
              localStorage.setItem(import.meta.env.VITE_LOCALSTORAGE_USER, JSON.stringify(user))
              next()
            } else {
              clearAuth()
              next('/login')
            }
          } catch {
            clearAuth()
            next('/login')
          }
        }
      },
      props: true,
      component: () => import('@/views/UserView.vue'),
    },
    {
      path: '/novel/:id',
      name: 'novel',
      props: true,
      component: () => import('@/views/NovelView.vue'),
    },
    {
      path: '/search/:page',
      name: 'search',
      props: (route) => ({ name: route.query.name as string }),
      component: () => import('@/views/SearchView.vue'),
    },
  ],
})

export default router
