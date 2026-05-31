import type { Comments } from '@/baseType'
import request from '.'

export const getReportedComments = () => {
  return request.get('getJvbaoComments')
}

export const auditComments = (comments: Comments) => {
  return request.post('auditComments', { comments })
}
