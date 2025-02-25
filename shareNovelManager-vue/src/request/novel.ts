import type { Admin, Novel } from "@/baseType";
import request from ".";

//获取被举报文章
export const getJvbao = request.get('getJvbao')

//获取待审核文章
export const getShenhe = request.get('getShenhe')

//审核通过文章
export const pass = (admin:Admin,novel:Novel)=>{
    novel.state = 1
    return request.post('auditNovel',{
        novel,
        admin
    })
}

//审核不通过文章
export const nopass = (admin:Admin,novel:Novel)=>{
    novel.state = -1
    return request.post('auditNovel',{
        novel,
        admin
    })
}

//被举报审核不通过
export const jvbaoNopass = (admin:Admin,novel:Novel)=>{
    novel.state = 3
    return request.post('auditNovel',{
        novel,
        admin
    })
}