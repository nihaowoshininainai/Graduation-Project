import request from "."

//评论点赞
export const addCommentLike = (commentId: number) => {
    return request.get('addCommentsLike', {
        params: {
            cid: commentId
        }
    })
}

//取消评论点赞
export const deleteCommentLike = (commentId: number) => {
    return request.get('delCommentsLike',{
        params:{
            cid: commentId
        }
    })
}

//获取评论点赞状态
export const getCommentLike = (commentId: number,uid:number) => {
    return request.get('getCommentsLike',{
        params: {
            cid: commentId,
            uid: uid
        }
    })
}