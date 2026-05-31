import type { Comments } from '@/baseType'
import request from '.'

export const getComments = (nid: number) => {
  return request.get('showComments', { params: { nid } })
}

export const addComment = (comments: Comments) => {
  return request.post('addCom', comments)
}

export const getUserComments = (uid: number) => {
  return request.get('showUserComments', { params: { uid } })
}

export const delComments = (id: number) => {
  return request.post('delCom', { id })
}
