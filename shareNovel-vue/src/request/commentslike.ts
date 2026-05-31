import request from '.'

export const addCommentLike = (cid: number) => {
  return request.post('addCommentsLike', null, { params: { cid } })
}

export const deleteCommentLike = (cid: number) => {
  return request.post('delCommentsLike', null, { params: { cid } })
}

export const getCommentLike = (cid: number, uid: number) => {
  return request.get('getCommentsLike', { params: { cid, uid } })
}
