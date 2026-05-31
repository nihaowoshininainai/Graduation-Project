import type { Novel } from '@/baseType'
import request from '.'

export const getJvbao = () => {
  return request.get('getJvbao')
}

export const getShenhe = () => {
  return request.get('getShenhe')
}

export const auditNovel = (record: string, novel: Novel) => {
  return request.post(`auditNovel?record=${encodeURIComponent(record)}`, { novel })
}

export const getAllNovels = () => {
  return request.get('getAllNovels')
}

export const deleteNovelByAdmin = (novel: Novel) => {
  return request.post('deleteNovelByAdmin', { novel })
}
