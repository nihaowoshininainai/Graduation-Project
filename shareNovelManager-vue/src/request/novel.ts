import type { Admin, Novel } from "@/baseType";
import request from ".";

//获取被举报文章
export const getJvbao = request.get('getJvbao')

//获取待审核文章
export const getShenhe = request.get('getShenhe')

//审核通过文章
export const pass = (admin:Admin,novel:Novel)=>{
    novel.state = 1
    const record=admin.aname+'审核通过题为'+novel.title+'的小说'
    return request.post(`auditNovel?record=${record}`,{
        novel,
        admin
    })
}

//审核不通过文章
export const nopass = (admin:Admin,novel:Novel)=>{
    novel.state = -1
    const record=admin.aname+'审核不通过题为'+novel.title+'的小说'
    return request.post(`auditNovel?record=${record}`,{
        novel,
        admin
    })
}

//被举报审核不通过
export const jvbaoNopass = (admin:Admin,novel:Novel)=>{
    novel.state = 3
    const record=admin.aname+'审核不通过被举报题为'+novel.title+'的小说'
    return request.post(`auditNovel?record=${record}`,{
        novel,
        admin
    })
}

//获取所有小说
export const getAllNovels = request.get('getAllNovels')

//删除小说
export const deleteNovel = (admin:Admin,novel:Novel)=>{
    return request.post('deleteNovelByAdmin',{
        admin,
        novel
    })
}