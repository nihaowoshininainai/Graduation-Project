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
export const register = (uname:string,pwd:string) => {
  const user={
    uname,
    pwd
  }
  return request.post("register",user)
}