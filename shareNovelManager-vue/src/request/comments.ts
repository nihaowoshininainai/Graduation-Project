import type { Admin, Comments } from "@/baseType";
import request from ".";

//获取被举报评论
export const getCommets=request.get('getJvbaoComments')

//通过被举报评论
export const pass = (admin:Admin,comments:Comments)=>{
    comments.state = 0
    return request.post('auditComments',{
        admin,
        comments
    })
}

//不通过被举报评论
export const nopass = (admin:Admin,comments:Comments)=>{
    comments.state = -1
    return request.post('auditComments',{
        admin,
        comments
    })
}