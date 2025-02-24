import request from "."

//获取浏览量
export const getView = (nid: number) => {
    return request.get('getViews',{
        params:{
            nid
        }
    })
}

//提升浏览量
export const addView = (nid: number) => {
    return request.get('addViews',{
        params:{
            nid
        }
    })
}