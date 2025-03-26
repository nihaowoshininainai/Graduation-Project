import type { Admin } from "@/baseType"
import request from "."

//管理员登录
export const adminLogin = (admin:Admin)=>{
    return request.post('alogin',admin)
}