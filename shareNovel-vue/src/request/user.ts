import request from '.'

export const getUname = (uid: number) => {
  return request.get('showUname', { params: { id: uid } })
}

export const register = (uname: string, pwd: string) => {
  return request.post('register', { uname, pwd })
}

export const login = (uname: string, pwd: string) => {
  return request.post('login', { uname, pwd })
}

export const getUid = (uname: string) => {
  return request.get('showUid', { params: { uname } })
}

export const getUserInfo = (uid: number) => {
  return request.get('showUser', { params: { uid } })
}

export const changePwd = (oldPwd: string, newPwd: string) => {
  return request.post('resetPwd', { oldPwd, newPwd })
}

export const deleteUser = (pwd: string) => {
  return request.post('deleteUser', { pwd })
}
