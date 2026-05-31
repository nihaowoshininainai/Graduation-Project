import request from '.'

export const getView = (nid: number) => {
  return request.get('getViews', { params: { nid } })
}

export const addView = (nid: number) => {
  return request.post('addViews', null, { params: { nid } })
}
