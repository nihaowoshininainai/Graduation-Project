import type { Admin } from "@/baseType"
import request from "."

//管理员登录
export const adminLogin = (admin: Admin) => {
    return request.post('alogin', admin)
}

//获取管理员信息
export const getAdminInfo = (aname: string) => {
    return request.get('getAdmins', {
        params: {
            aname
        }
    })
}

//添加管理员
export const add = (adminTop: Admin, admin: Admin) => {
    return request.post('addAdmin',
        [adminTop, admin]
    )
    
}

//删除管理员
export const delAdmin = (adminTop: Admin, admin: Admin) => {
    return request.post('delAdmin',
        [adminTop, admin]
    )
}