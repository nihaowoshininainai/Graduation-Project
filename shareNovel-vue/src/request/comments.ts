import type { Comments } from "@/baseType"
import request from "."

// 获取评论
export const getComments = (id: number) => {
    return request.get("showComments",{
        params:{
            nid:id
        }
    })
}

// 添加评论
export const addComment = (comments:Comments) => {
    return request.post("addCom",comments)
}