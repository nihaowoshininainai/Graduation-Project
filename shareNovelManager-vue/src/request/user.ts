import type { User } from '@/baseType'
import request from '.'

export const getBanUsers = () => {
  return request.get('getBanUsers')
}

export const getBannedUsers = () => {
  return request.get('getBannedUsers')
}

export const auditUser = (user: User) => {
  return request.post('auditUser', { user })
}

export const getAllUsers = () => {
  return request.get('getAllUsers')
}

export const deleteUserByAdmin = (user: User) => {
  return request.post('deleteUserByAdmin', { user })
}

export const resetPwdByAdmin = (user: User) => {
  return request.post('resetPwdByAdmin', { user })
}
