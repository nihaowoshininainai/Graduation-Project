import { createRouter, createWebHistory } from 'vue-router'
import { isAdminLoggedIn } from '@/request'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/components/Login.vue'),
    },
    {
      path: '/',
      component: () => import('@/components/Layout.vue'),
      beforeEnter: (_to, _from, next) => {
        if (!isAdminLoggedIn()) {
          next('/login')
        } else {
          next()
        }
      },
      children: [
        { path: '', name: 'home', component: () => import('@/components/Home.vue') },
        { path: 'admin', name: 'admin', component: () => import('@/components/Admin.vue') },
        { path: 'user', name: 'user', component: () => import('@/components/User.vue') },
        { path: 'article', name: 'article', component: () => import('@/components/Article.vue') },
        { path: 'shenhe', name: 'shenhe', component: () => import('@/components/Shenhe.vue') },
        { path: 'record', name: 'record', component: () => import('@/components/Record.vue') },
      ],
    },
  ],
})

export default router
