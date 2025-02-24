import type { Novel } from "@/baseType"
import request from "."

//判断是否收藏
export const ifLike = (uid: number, nid: number) => {
    return request.get('showLike', {
        params: {
            uid,
            nid
        }
    })
}

//收藏
export const like = (novel: Novel) => {
    return request.post('addCol', novel)
}

//取消收藏
export const delLike = (novel: Novel) => {
    return request.post('delCol', novel)
}