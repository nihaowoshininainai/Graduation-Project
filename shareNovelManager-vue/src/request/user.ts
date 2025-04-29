import type { Admin, User } from "@/baseType";
import request from ".";

//获取违规用户信息
export const getBanUsers = request.get('getBanUsers')

//获取已经封禁用户信息
export const getBannedUsers = request.get('getBannedUsers')

//封禁用户
export const banUser = (admin:Admin,user:User)=>{
    user.banned=1
    return request.post('auditUser',{
        admin,
        user
    })
}

//解封用户
export const unbanUser = (admin:Admin,user:User)=>{
    user.banned=0
    return request.post('auditUser',{
        admin,
        user
    })
}