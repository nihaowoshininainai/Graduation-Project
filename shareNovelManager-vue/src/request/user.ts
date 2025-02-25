import type { Admin, User } from "@/baseType";
import request from ".";

//获取违规用户信息
export const getUsers = request.get('getUsers')

//封禁用户
export const banUser = (admin:Admin,user:User)=>{
    user.banned=1
    return request.post('auditUser',{
        admin,
        user
    })
}