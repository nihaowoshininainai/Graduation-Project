import request from '.'

export const ifLike = (uid: number, nid: number) => {
  return request.get('showLike', { params: { uid, nid } })
}

export const like = (nid: number) => {
  return request.post('addCol', { id: nid })
}

export const delLike = (nid: number) => {
  return request.post('delCol', { id: nid })
}
