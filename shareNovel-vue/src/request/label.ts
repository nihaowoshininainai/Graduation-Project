import request from '.'

export const getLabels = () => {
  return request.get('labels')
}
