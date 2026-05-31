import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

request.interceptors.response.use(
  (response) => response,
  (error) => {
    ElMessage.error('网络错误，请稍后重试')
    return Promise.reject(error)
  },
)

export function clearAdminAuth() {
  document.cookie = 'AdminToken=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;'
  localStorage.removeItem('admin')
}

export function isAdminLoggedIn(): boolean {
  return localStorage.getItem('admin') !== null && document.cookie.includes('AdminToken=')
}

export function getStoredAdmin() {
  const raw = localStorage.getItem('admin')
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

export default request
