import type { Admin } from '@/baseType'
import request from '.'

export const adminLogin = (admin: Admin) => {
  return request.post('adminLogin', admin)
}

export const getAdminInfo = (aname: string) => {
  return request.get('getAdmins', { params: { aname } })
}

export const addAdmin = (adminTop: Admin, admin: Admin) => {
  return request.post('addAdmin', [adminTop, admin])
}

export const delAdmin = (adminTop: Admin, admin: Admin) => {
  return request.post('delAdmin', [adminTop, admin])
}

export const getArticleRecord = () => {
  return request.get('getShenheRecord')
}
