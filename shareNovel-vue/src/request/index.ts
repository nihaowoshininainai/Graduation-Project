import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

request.interceptors.response.use(
  (response) => {
    const url = response.config.url || ''
    const nonStandardApis = ['/getViews', '/addViews', '/getCommentsLike']
    if (nonStandardApis.some((api) => url.includes(api))) {
      return response
    }
    const result = response.data
    if (result.state === '0') {
      if (result.message === import.meta.env.VITE_MESSAGE_RELOGIN) {
        ElMessage.error({
          message: result.message,
          duration: 1000,
          onClose: () => {
            clearAuth()
            router.push('/login')
          },
        })
      }
    }
    return response
  },
  (error) => {
    ElMessage.error('网络错误，请稍后重试')
    return Promise.reject(error)
  },
)

export function clearAuth() {
  document.cookie = 'Token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;'
  localStorage.removeItem(import.meta.env.VITE_LOCALSTORAGE_USER)
}

export function isLoggedIn(): boolean {
  return document.cookie.includes('Token=')
}

export function getStoredUser() {
  const raw = localStorage.getItem(import.meta.env.VITE_LOCALSTORAGE_USER)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

export default request
