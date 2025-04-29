import type { User } from "@/baseType"
import request from "."

//获取用户名
export const getUname = (uid: number) => {
  return request.get("showUname", {
    params: {
      id: uid
    }
  })
}

//注册
export const register = (uname: string, pwd: string) => {
  const user: User = {
    uname,
    pwd
  }
  return request.post("register", user)
}

//登录
export const login = (uname: string, pwd: string) => {
  const user: User = {
    uname,
    pwd
  }
  return request.post("login", user)
}

//用户名获取用户id
export const getUid = (uname: string) => {
  return request.get("showUid", {
    params: {
      uname
    }
  })
}

//获取用户信息
export const getUserInfo = (uid: number) => {
  return request.get("showUser", {
    params: {
      uid: uid
    }
  })
}

//修改密码
export const changePwd = (oldPwd: string, newPwd: string) => {
  return request.post("resetPwd", {
    oldPwd,
    newPwd
  })

}
