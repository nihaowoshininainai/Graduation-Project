import type { Novel, Result, User } from '@/baseType'
import request from '.'

export const getIndexNovel = (current: number, size: number, condition?: string, sort?: string, labelId?: number, name?: string) => {
  if (labelId && labelId !== -1) {
    return request.get('classify', { params: { current, size, condition, sort, labelId } })
  }
  if (name) {
    return request.get('search', { params: { current, size, condition, sort, name } })
  }
  return request.get('index', { params: { current, size, condition, sort } })
}

export const getUserNovel = (uid: number) => {
  return request.get('showNovels', { params: { uid } })
}

export const getUserCollectNovel = (uid: number) => {
  return request.get('showCollections', { params: { uid } })
}

export const uploadUserNovel = (data: { user: User; novel: Novel }) => {
  return request.post('addNovel', data)
}

export const getNovelDetail = (novelId: number) => {
  return request.get('showNovel', { params: { nid: novelId } })
}

export const reportNovel = (id: number) => {
  return request.post('reportNovel', { id })
}

export const getNovelRecommend = (uid: number) => {
  return request.get('showRecommend', { params: { uid } })
}

export const delNovel = (novel: Novel, user: User) => {
  return request.post('delNovel', { novel: { id: novel.id, title: novel.title }, user: { pwd: user.pwd } })
}
